package com.atqidi.elcar.service.impl;

import com.atqidi.elcar.entity.PayTable;
import com.atqidi.elcar.mapper.CarTypeMapper;
import com.atqidi.elcar.mapper.PayTableMapper;
import com.atqidi.elcar.service.PayTableService;
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

import java.util.List;

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
public class PayTableServiceImpl extends ServiceImpl<PayTableMapper, PayTable> implements PayTableService {

    @Autowired
    private PayTableMapper payTableMapper;

    @Autowired
    private CarTypeMapper carTypeMapper;

    private QueryWrapper<PayTable> queryWrapper;

/*
    {
        "carTypeId": 0,
        "chargeEnd": 0,
        "chargeStart": 0,
        "payCount": 0,
        "payFee": 0,
        "payMoney": 0,
        "payTimeName": "string",
    }
*/


    /**
     * 添加收费表
     * TODO 合计 = 服务费 + 电费
     *
     * @param payTable
     * @return
     */
    @Override
    public Result addPayTable(PayTable payTable) {
        //判断车辆类型是否填写，并且判断是否有效
        //如果类型不为空，并且查找得到就OK
        if (StringUtils.isEmpty(payTable.getCarTypeId()) || StringUtils.isEmpty(carTypeMapper.selectById(payTable.getCarTypeId()))) {
            return Result.fail().message("请输入正确的车辆类型。");
        }
        //判断开始时间和结束时间（按小时）是否为空， 0<= x <= 24
        if (startTimeOrEndTimeIsOk(payTable.getChargeStart()) || startTimeOrEndTimeIsOk(payTable.getChargeEnd())) {
            return Result.fail().message("请输入正确的开始时间或结束时间。");
        }
        //判断三个费用是否填写，并且不能为负数
        //需要支付金额
        //服务费
        //合计
        if (payNumberIsOk(payTable.getPayFee()) || payNumberIsOk(payTable.getPayMoney()) || payNumberIsOk(payTable.getPayCount())) {
            return Result.fail().message("请检查输入金额是否正确。");
        }
        //时段名称
        if (StringUtils.isEmpty(payTable.getPayTimeName())) {
            return Result.fail().message("时段名不可缺少");
        }
        int insert = payTableMapper.insert(payTable);
        if (insert == 0) {
            return Result.fail().message("添加失败");
        }
        return Result.ok().message("添加成功");
    }

    public boolean payNumberIsOk(Double money) {
        final Double zeroMoney = 0.0;
        //判断是否为空
        if (StringUtils.isEmpty(money)) {
            return true;
        }
        //判断是否为负数
        if (money < zeroMoney) {
            return true;
        }
        return false;
    }

    /**
     * 判断时间是否可行
     *
     * @param time
     * @return
     */
    public boolean startTimeOrEndTimeIsOk(Integer time) {
        final Integer endTime = 24;
        final Integer startTime = 0;

        //判断是否为空
        if (StringUtils.isEmpty(time)) {
            return true;
        }
        // 0<= x <= 24
        if (time > endTime || startTime < 0) {
            return true;
        }
        return false;
    }

    /**
     * 删除收费表
     *
     * @param id
     * @return
     */
    @Override
    public Result deletePayTable(Integer id) {
        int delete = payTableMapper.deleteById(id);
        if (delete == 0) {
            return Result.fail().message("删除失败");
        }
        return Result.ok().message("删除成功");
    }

    /**
     * 获取收费表通过ID
     *
     * @param id
     * @return
     */
    @Override
    public Result getPayTable(Integer id) {
        PayTable payTable = payTableMapper.selectById(id);
        if (StringUtils.isEmpty(payTable)) {
            return Result.fail().message("不存在该收费表");
        }
        return Result.ok(payTable).message("获取成功");
    }

    /**
     * 获取收费表列表
     *
     * @param page
     * @param size
     * @return
     */
    @Override
    public Result getPayTableList(Integer page, Integer size) {
        page = PageTools.getPage(page);
        size = PageTools.getSize(size);
        Page<PayTable> payPage = new Page<>(page, size);
        payPage = payTableMapper.selectPage(payPage, null);
        return Result.ok(payPage).message("获取成功");
    }

    /**
     * 更新收费表
     *
     * @param payTable
     * @return
     */
    @Override
    public Result updatePayTable(PayTable payTable) {
        PayTable payTableFormDB = null;
        //判断是否有这个对象
        if (!StringUtils.isEmpty(payTable.getId())) {
            payTableFormDB = payTableMapper.selectById(payTable.getId());
            if (StringUtils.isEmpty(payTableFormDB)) {
                return Result.fail().message("要修改的对象不存在");
            }
        }
        //更新时段
        //判断开始时间和结束时间（按小时）是否为空， 0<= x <= 24
        if (startTimeOrEndTimeIsOk(payTable.getChargeStart()) || startTimeOrEndTimeIsOk(payTable.getChargeEnd())) {
            return Result.fail().message("请输入正确的开始时间或结束时间。");
        }
        payTableFormDB.setChargeStart(payTable.getChargeStart());
        payTableFormDB.setChargeEnd(payTable.getChargeEnd());
        //更新金额
        if (payNumberIsOk(payTable.getPayFee()) || payNumberIsOk(payTable.getPayMoney()) || payNumberIsOk(payTable.getPayCount())) {
            return Result.fail().message("请检查输入金额是否正确。");
        }
        payTableFormDB.setPayCount(payTable.getPayCount());
        payTableFormDB.setPayMoney(payTable.getPayMoney());
        payTableFormDB.setPayFee(payTable.getPayFee());
        //更新车辆类型
        if (StringUtils.isEmpty(payTable.getCarTypeId()) || StringUtils.isEmpty(carTypeMapper.selectById(payTable.getCarTypeId()))) {
            return Result.fail().message("请输入正确的车辆类型。");
        }
        payTableFormDB.setCarTypeId(payTable.getCarTypeId());
        int update = payTableMapper.updateById(payTableFormDB);
        if (update == 0) {
            return Result.fail().message("更新失败");
        }
        return Result.ok().message("更新成功");
    }

    @Override
    public Result getPayTableByCarType(Integer carTypeId) {
        if (StringUtils.isEmpty(carTypeId) || StringUtils.isEmpty(carTypeMapper.selectById(carTypeId))) {
            return Result.fail().message("请输入正确的车辆类型。");
        }
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("car_type_id", carTypeId);
        List<PayTable> payTables = payTableMapper.selectList(queryWrapper);
        return Result.ok(payTables).message("获取成功");
    }
}
