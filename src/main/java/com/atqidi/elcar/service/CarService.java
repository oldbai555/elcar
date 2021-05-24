package com.atqidi.elcar.service;

import com.atqidi.elcar.entity.Car;
import com.atqidi.elcar.utils.result.Result;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author oldbai
 * @since 2021-04-26
 */
public interface CarService extends IService<Car> {

    Result addCar(Car car, String tokenKey);

    Result updateCar(Car car);

    Result deleteCarById(Integer carId);

    Result getCarById(Integer carId);

    Result getCarList(Integer page, Integer size);

    Result getCarByName(String carName);

    Result checkCarName(String carName);

}
