package com.atqidi.elcar.service;

import com.atqidi.elcar.entity.Equipment;
import com.atqidi.elcar.utils.result.Result;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author oldbai
 * @since 2021-04-26
 */
public interface EquipmentService extends IService<Equipment> {

    Result addEquipment(Equipment equipment);

    Result deleteEquipment(String id);

    Result selectEquipment(String id);

    Result listEquipment(Integer page, Integer size);

    Result updateEquipment(Equipment equipment);

    Result checkNameIfExit(String equipmentName);

    Result updateEquipmentState(Equipment equipment, Integer state);

    Result selectEquipmentByCarType(Integer car_type_id);

    Result checkEquipmentState(Integer id);
}
