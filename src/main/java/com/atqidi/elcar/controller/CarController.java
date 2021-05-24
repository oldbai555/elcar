package com.atqidi.elcar.controller;


import com.atqidi.elcar.entity.Car;
import com.atqidi.elcar.service.CarService;
import com.atqidi.elcar.utils.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author oldbai
 * @since 2021-04-26
 */
@Api(description = "车辆")
@RestController
@RequestMapping("/elcar/car")
@CrossOrigin
public class CarController {

    @Autowired
    private CarService carService;

    @ApiOperation("添加车辆，需要提供车辆类型ID和车辆名称")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", dataType = "String", required = true, value = "token")
    })
    @PostMapping("/car")
    public Result addCar(@RequestBody Car car,@RequestHeader("token") String tokenKey) {
        return carService.addCar(car,tokenKey);
    }

    @ApiOperation("修改车辆，需要提供车辆类型、车辆名称和车辆ID")
    @PutMapping("/car")
    public Result updateCar(@RequestBody Car car) {
        return carService.updateCar(car);
    }

    @ApiOperation("删除车辆，需要提供车辆ID")
    @DeleteMapping("/car/{car_id}")
    public Result deleteCar(@PathVariable("car_id") Integer carId) {
        return carService.deleteCarById(carId);
    }

    @ApiOperation("获取车辆，需要提供车辆ID")
    @GetMapping("/car/{car_id}")
    public Result getCarById(@PathVariable("car_id") Integer carId) {
        return carService.getCarById(carId);
    }

    @ApiOperation("获取车辆，需要提供车辆名称")
    @GetMapping("/name/{car_name}")
    public Result getCarByName(@PathVariable("car_name") String carName) {
        return carService.getCarByName(carName);
    }

    @ApiOperation("获取车辆列表")
    @GetMapping("/list/{page}/{size}")
    public Result getCarList(@PathVariable("page") Integer page,
                             @PathVariable("size") Integer size) {
        return carService.getCarList(page, size);
    }

    @ApiOperation("检查车辆是否存在，需要提供车辆名称")
    @GetMapping("/check/{car_name}")
    public Result checkCarName(@PathVariable("car_name") String carName) {
        return carService.checkCarName(carName);
    }
}

