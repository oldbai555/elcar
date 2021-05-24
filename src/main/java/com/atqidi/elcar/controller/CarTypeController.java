package com.atqidi.elcar.controller;


import com.atqidi.elcar.entity.CarType;
import com.atqidi.elcar.service.CarTypeService;
import com.atqidi.elcar.utils.result.Result;
import com.atqidi.elcar.vo.CarTyoeVO;
import io.swagger.annotations.Api;
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
@Api(description = "车辆类型")
@RestController
@RequestMapping("/elcar/car-type")
@CrossOrigin
public class CarTypeController {

    @Autowired
    private CarTypeService carTypeService;

    @ApiOperation("添加车辆类型，只需要给车辆类型名称")
    @PostMapping("car_type")
    public Result addCarType(@RequestBody CarTyoeVO carType) {

        return carTypeService.addCarType(carType);
    }

    @ApiOperation("通过车辆类型ID更新车辆类型列表")
    @PutMapping("car_type")
    public Result updateCarType(@RequestBody CarType carType) {
        return carTypeService.updateCarType(carType);
    }

    @ApiOperation("通过车辆类型ID删除车辆类型")
    @DeleteMapping("car_type/{type_id}")
    public Result deleteCarType(@PathVariable("type_id") Integer typeId) {
        return carTypeService.deleteCarType(typeId);
    }

    @ApiOperation("通过车辆类型ID查找车辆类型")
    @GetMapping("/car_type/{type_id}")
    public Result getCarTypeById(@PathVariable("type_id") Integer typeId) {
        return carTypeService.getCarTypeById(typeId);
    }

    @ApiOperation("通过车辆名称查找车辆类型")
    @GetMapping("/name/{name}")
    public Result getCarTypeByName(@PathVariable("name") String name) {
        return carTypeService.getCarTypeByName(name);
    }

    @ApiOperation("获取车辆类型列表")
    @GetMapping("/list/{page}/{size}")
    public Result getCarTypeList(@PathVariable("page") Integer page,
                                 @PathVariable("size") Integer size) {
        return carTypeService.getCarTypeList(page, size);
    }

}

