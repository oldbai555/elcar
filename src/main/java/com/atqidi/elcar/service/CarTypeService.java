package com.atqidi.elcar.service;

import com.atqidi.elcar.entity.CarType;
import com.atqidi.elcar.utils.result.Result;
import com.atqidi.elcar.vo.CarTyoeVO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author oldbai
 * @since 2021-04-26
 */
public interface CarTypeService extends IService<CarType> {
    /**
     * 添加车辆类型
     *
     * @param carType
     * @return
     */
    Result addCarType(CarTyoeVO carType);

    Result getCarTypeById(Integer typeId);

    Result getCarTypeByName(String name);

    Result getCarTypeList(Integer page, Integer size);

    Result updateCarType(CarType carType);

    Result deleteCarType(Integer typeId);
}
