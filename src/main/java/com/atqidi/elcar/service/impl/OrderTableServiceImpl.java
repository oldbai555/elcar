package com.atqidi.elcar.service.impl;

import com.atqidi.elcar.entity.Equipment;
import com.atqidi.elcar.entity.OrderTable;
import com.atqidi.elcar.entity.User;
import com.atqidi.elcar.mapper.CarMapper;
import com.atqidi.elcar.mapper.EquipmentMapper;
import com.atqidi.elcar.mapper.OrderTableMapper;
import com.atqidi.elcar.mapper.PayTableMapper;
import com.atqidi.elcar.service.EquipmentService;
import com.atqidi.elcar.service.OrderTableService;
import com.atqidi.elcar.service.UserService;
import com.atqidi.elcar.utils.Constants;
import com.atqidi.elcar.utils.RedisUtil;
import com.atqidi.elcar.utils.SnowflakeIdWorker;
import com.atqidi.elcar.utils.result.Result;
import com.atqidi.elcar.utils.tools.PageTools;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author oldbai
 * @since 2021-04-26
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
public class OrderTableServiceImpl extends ServiceImpl<OrderTableMapper, OrderTable> implements OrderTableService {

    @Autowired
    UserService userService;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;
    @Autowired
    private OrderTableMapper orderTableMapper;

    @Autowired
    private CarMapper carMapper;

    @Autowired
    private PayTableMapper payTableMapper;

    @Autowired
    private EquipmentMapper equipmentMapper;

    @Autowired
    private EquipmentService equipmentService;

    private QueryWrapper wrapper;

    @Override
    public Result addOrderTable(OrderTable orderTable) {
        Result result = userService.checkUser(null);
        User user = (User) result.getData();
        if (StringUtils.isEmpty(user)) {
            return Result.fail().message("尚未登录，无法提交订单");
        }
        //判断登陆用户和提交订单的用户是否一致
        if (!orderTable.getUserPhone().equals(user.getPhone())) {
            return Result.fail().message("用户不一致，无法提交订单");
        }
        //在哪个设备
        if (StringUtils.isEmpty(orderTable.getEqId())
                || StringUtils.isEmpty(equipmentMapper.selectById(orderTable.getEqId()))
        ) {
            return Result.fail().message("该设备不存在");
        }
        if (!(boolean) equipmentService.checkEquipmentState(orderTable.getEqId()).getData()) {
            return Result.fail().message("该设备不可用");
        }
        //TODO 需要一个接口计算收费多少
        //要收多少钱
        if (StringUtils.isEmpty(orderTable.getPay())) {
            return Result.fail().message("金额异常");
        }
        //生成订单号
        String orderId = Constants.OrderTable.FIRSTNAME + snowflakeIdWorker.nextId();
        orderTable.setOrderId(orderId);
        //设置默认状态
        orderTable.setState(Constants.OrderTable.UNSTART);
        //写入mysql和redis
        int insert = orderTableMapper.insert(orderTable);
        if (insert == 0) {
            return Result.fail().message("订单创建失败");
        }
        redisUtil.set(orderId, orderTable, Constants.TimeValue.MIN * 5);
        //设置设备状态,设置为已用
        Equipment equipment = equipmentMapper.selectById(orderTable.getEqId());
        equipmentService.updateEquipmentState(equipment, Constants.Equipment.HAVEUSE);
        Map<String, String> map = new HashMap<>();
        map.put("orderId", orderId);
        return Result.ok(map);
    }

    @Override
    public Result updateOrderTableByOrderId(String orderId, Integer state) {

        //查询数据库是否有该订单
        wrapper = new QueryWrapper();
        wrapper.eq("order_id", orderId);
        OrderTable orderTableFormDB = orderTableMapper.selectOne(wrapper);
        if (StringUtils.isEmpty(orderTableFormDB)) {
            return Result.fail().message("订单已超时");
        }
        //检查用户
        Result result = userService.checkUser(null);
        User user = (User) result.getData();
        if (StringUtils.isEmpty(user)) {
            return Result.fail().message("尚未登录，无法提交订单");
        }
        //判断登陆用户和提交订单的用户是否一致
        if (!orderTableFormDB.getUserPhone().equals(user.getPhone())) {
            return Result.fail().message("用户不一致，无法修改订单");
        }

        //确保订单可以操作。不是超时，不是结束订单了。
        if (orderTableFormDB.getState().equals(Constants.OrderTable.TIMEOVER)
                || orderTableFormDB.getState().equals(Constants.OrderTable.END)) {
            return Result.fail().message("订单已关闭");
        }
        //如果是设置超时，那么直接设。
        if (state.equals(Constants.OrderTable.TIMEOVER)) {
            //但是，该订单不能是已经确认的。
            if (!orderTableFormDB.getState().equals(Constants.OrderTable.STARTING)) {
                //修改订单状态。
                orderTableFormDB.setState(state);
                orderTableMapper.update(orderTableFormDB, wrapper);
                redisUtil.del(orderId);
                return Result.ok();
            } else {
                return Result.fail().message("已经确认的订单不允许超时");
            }
        }
        //设置确认订单和结束订单。如果有，查redis是否还存在该订单缓存.确认订单需要查redis
        OrderTable orderTableFromRedis = (OrderTable) redisUtil.get(orderId);
        //确认订单，如果redis没有就说明超时了。
        if (StringUtils.isEmpty(orderTableFromRedis) && state.equals(Constants.OrderTable.STARTING)) {
            //不存在则删除数据库的订单。
            orderTableFormDB.setState(state);
            orderTableMapper.update(orderTableFormDB, wrapper);
            orderTableMapper.delete(wrapper);
            return Result.fail().message("订单已超时");
        }
        //结束订单，必须是已经确认才可以
        if (state.equals(Constants.OrderTable.END)
                && !orderTableFormDB.getState().equals(Constants.OrderTable.STARTING)) {
            return Result.fail().message("订单未确认");
        }
        //确认订单，结束订单。存在则修改订单状态。
        orderTableFormDB.setState(state);
        int update = orderTableMapper.update(orderTableFormDB, wrapper);
        //如果是结束订单，记得更新设备状态
        if (state.equals(Constants.OrderTable.END)) {
            Equipment equipment = equipmentMapper.selectById(orderTableFormDB.getEqId());
            equipment.setState(Constants.Equipment.CANUSE);
            equipmentMapper.updateById(equipment);
        }
        //如果是确认订单，删除redis的记录
        if (state.equals(Constants.OrderTable.STARTING)) {
            redisUtil.del(orderId);
        }
        if (update == 0) {
            return Result.fail();
        }
        return Result.ok();
    }

    @Override
    public Result orderList(String token) {
        User user = (User) userService.checkUser(token).getData();
        if (StringUtils.isEmpty(user)) {
            return Result.fail().message("用户未登录");
        }
        wrapper = new QueryWrapper();
        wrapper.eq("user_phone", user.getPhone());
        List list = orderTableMapper.selectList(wrapper);
        return Result.ok(list);
    }

    @Override
    public Result orderListPage(String token, Integer page, Integer size) {
        page = PageTools.getPage(page);
        size = PageTools.getSize(size);
        Page<OrderTable> tablePage = new Page<>(page, size);
        User user = (User) userService.checkUser(token).getData();
        if (StringUtils.isEmpty(user)) {
            return Result.fail().message("用户未登录");
        }
        wrapper = new QueryWrapper();
        wrapper.eq("user_phone", user.getPhone());
        tablePage = orderTableMapper.selectPage(tablePage, wrapper);
        return Result.ok(tablePage);
    }

    @Override
    public Result selectOrderById(String token, String order_id) {
        User user = (User) userService.checkUser(token).getData();
        if (StringUtils.isEmpty(user)) {
            return Result.fail().message("用户未登录");
        }
        wrapper = new QueryWrapper();
        wrapper.eq("order_id", order_id);
        OrderTable orderTable = orderTableMapper.selectOne(wrapper);
        if (StringUtils.isEmpty(orderTable)) {
            return Result.fail().message("订单不存在");
        }
        return Result.ok(orderTable);
    }

    @Override
    public Result selectOrderByState(String token, Integer choose) {
        if (choose < Constants.OrderTable.UNSTART || choose > Constants.OrderTable.TIMEOVER){
            return Result.fail().message("获取失败，状态错误。");
        }
        User user = (User) userService.checkUser(token).getData();
        if (StringUtils.isEmpty(user)) {
            return Result.fail().message("用户未登录");
        }
        wrapper = new QueryWrapper();
        wrapper.eq("user_phone", user.getPhone());
        wrapper.eq("state",choose);
        List list = orderTableMapper.selectList(wrapper);
        return Result.ok(list);
    }
}
