package com.atqidi.elcar.service.impl;

import com.atqidi.elcar.entity.Equipment;
import com.atqidi.elcar.mapper.CarTypeMapper;
import com.atqidi.elcar.mapper.EquipmentMapper;
import com.atqidi.elcar.service.EquipmentService;
import com.atqidi.elcar.utils.Constants;
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
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
@Service
public class EquipmentServiceImpl extends ServiceImpl<EquipmentMapper, Equipment> implements EquipmentService {

    @Autowired
    private EquipmentMapper equipmentMapper;

    @Autowired
    private CarTypeMapper carTypeMapper;

    private QueryWrapper<Equipment> wrapper;

    /*
   {
    "carTypeId": 1,
     "eqName": "EL-001",
     "place": "广西科技大学"
    }
    * */

    /**
     * 添加设备
     *
     * @param equipment
     * @return
     */
    @Override
    public Result addEquipment(Equipment equipment) {
        //判断车辆类型是否存在
        if (StringUtils.isEmpty(equipment.getCarTypeId()) ||
                StringUtils.isEmpty(carTypeMapper.selectById(equipment.getCarTypeId()))) {
            return Result.fail().message("请选择正确的车辆类型");
        }
        //判断设备名字是否重复
        if (StringUtils.isEmpty(equipment.getEqName()) || !(Boolean) checkNameIfExit(equipment.getEqName()).getData()) {
            return Result.fail().message("请选择正确的设备名称");
        }
        //让收费表的ID就是车辆类型表，因为收费表就是根据车辆类型表来设计的。
        equipment.setPayId(equipment.getCarTypeId());
        //判断地点是否存在
        if (StringUtils.isEmpty(equipment.getPlace())) {
            return Result.fail().message("输入设备地点");
        }
        //设置默认状态，默认可用
        if (StringUtils.isEmpty(equipment.getState())) {
            equipment.setState(Constants.Equipment.CANUSE);
        }
        int insert = equipmentMapper.insert(equipment);
        if (insert == 0) {
            return Result.fail().message("添加失败");
        }
        return Result.ok().message("添加成功");
    }

    @Override
    public Result deleteEquipment(String id) {
        int delete = equipmentMapper.deleteById(id);
        if (delete == 0) {
            return Result.fail();
        }
        return Result.ok();
    }

    @Override
    public Result selectEquipment(String id) {
        if (StringUtils.isEmpty(id)) {
            return Result.fail();
        }
        Equipment equipment = equipmentMapper.selectById(id);
        if (StringUtils.isEmpty(equipment)) {
            return Result.fail().message("该用户不存在");
        }
        return Result.ok(equipment);
    }

    @Override
    public Result listEquipment(Integer page, Integer size) {
        page = PageTools.getPage(page);
        size = PageTools.getSize(size);
        Page<Equipment> equipmentPage = new Page<>(size, page);
        equipmentPage = equipmentMapper.selectPage(equipmentPage, null);
        return Result.ok(equipmentPage);
    }

    @Override
    public Result updateEquipment(Equipment equipment) {
        if (StringUtils.isEmpty(equipment.getId())) {
            return Result.fail().message("查找不到修改设备");
        }
        Equipment selectById = equipmentMapper.selectById(equipment.getId());
        //判断车辆类型是否存在
        if (StringUtils.isEmpty(equipment.getCarTypeId()) ||
                StringUtils.isEmpty(carTypeMapper.selectById(equipment.getCarTypeId()))) {
            return Result.fail().message("请选择正确的车辆类型");
        }
        selectById.setCarTypeId(equipment.getCarTypeId());
        //让收费表的ID就是车辆类型表，因为收费表就是根据车辆类型表来设计的。
        selectById.setPayId(equipment.getCarTypeId());
        //判断设备名字是否重复
        if (StringUtils.isEmpty(equipment.getEqName()) ||
                (!(Boolean) checkNameIfExit(equipment.getEqName()).getData()
                        && !equipment.getEqName().equals(selectById.getEqName()))) {
            return Result.fail().message("请选择正确的设备名称");
        }
        selectById.setEqName(equipment.getEqName());
        //判断地点是否存在
        if (StringUtils.isEmpty(equipment.getPlace())) {
            return Result.fail().message("输入设备地点");
        }
        selectById.setPlace(equipment.getPlace());
        int update = equipmentMapper.updateById(selectById);
        if (update == 0) {
            return Result.fail();
        }
        return Result.ok();
    }

    @Override
    public Result checkNameIfExit(String equipmentName) {
        wrapper = new QueryWrapper<>();
        wrapper.eq("eq_name", equipmentName);
        Equipment equipment = equipmentMapper.selectOne(wrapper);
        if (StringUtils.isEmpty(equipment)) {
            return Result.ok(true).message("名称可用");
        }
        return Result.fail(false).message("名称不可用");
    }

    @Override
    public Result updateEquipmentState(Equipment equipment, Integer state) {
        if (StringUtils.isEmpty(equipment.getId())) {
            return Result.fail().message("查找不到修改设备");
        }
        Equipment selectById = equipmentMapper.selectById(equipment.getId());
        if (StringUtils.isEmpty(state)) {
            return Result.fail();
        }
        if (state > Constants.Equipment.RTSTATE || state < Constants.Equipment.LTSTATE){
            return Result.fail();
        }
        selectById.setState(state);
        int update = equipmentMapper.updateById(selectById);
        if (update == 0) {
            return Result.fail();
        }
        return Result.ok();
    }

    @Override
    public Result selectEquipmentByCarType(Integer car_type_id) {
        wrapper = new QueryWrapper<>();
        wrapper.eq("car_type_id",car_type_id);
        List<Equipment> equipment = equipmentMapper.selectList(wrapper);
        return Result.ok(equipment);
    }

    @Override
    public Result checkEquipmentState(Integer id) {
        Equipment equipment = equipmentMapper.selectById(id);
        if (equipment.getState().equals(Constants.Equipment.CANUSE)){
            return Result.ok(true);
        }
        return Result.fail(false);
    }
}
