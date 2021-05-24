package com.atqidi.elcar.controller;


import com.atqidi.elcar.entity.Equipment;
import com.atqidi.elcar.service.EquipmentService;
import com.atqidi.elcar.utils.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 *
 *
 * <p>
 * 前端控制器
 * </p>
 *
 * @author oldbai
 * @since 2021-04-26
 */
@Api(description = "设备表")
@RestController
@RequestMapping("/elcar/equipment")
@CrossOrigin
public class EquipmentController {

    @Autowired
    private EquipmentService equipmentService;

    @ApiOperation("添加设备")
    @PostMapping("/equip")
    public Result addEquipment(@RequestBody Equipment equipment) {
        return equipmentService.addEquipment(equipment);
    }

    @ApiOperation("删除设备")
    @DeleteMapping("/{id}")
    public Result deleteEquipment(@PathVariable String id) {
        return equipmentService.deleteEquipment(id);
    }

    @ApiOperation("通过ID查询设备")
    @GetMapping("/{id}")
    public Result selectEquipment(@PathVariable String id) {
        return equipmentService.selectEquipment(id);
    }

    @ApiOperation("通过车辆类型查询设备")
    @GetMapping("/car_type/{car_type_id}")
    public Result selectEquipment(@PathVariable Integer car_type_id) {
        return equipmentService.selectEquipmentByCarType(car_type_id);
    }

    @ApiOperation("检查名称是否可用")
    @GetMapping("/equipment/{name}")
    public Result checkNameIfExit(@PathVariable String name) {
        return equipmentService.checkNameIfExit(name);
    }

    @ApiOperation("分页查询设备")
    @GetMapping("/list/{page}/{size}")
    public Result listEquipment(@PathVariable Integer page, @PathVariable Integer size) {
        return equipmentService.listEquipment(page, size);
    }

    @ApiOperation("修改设备,主要是修改信息")
    @PutMapping("/equip")
    public Result updateEquipment(@RequestBody Equipment equipment) {
        return equipmentService.updateEquipment(equipment);
    }

    @ApiOperation("修改设备,主要是修改状态")
    @PutMapping("/equip/{state}")
    public Result updateEquipmentState(@RequestBody Equipment equipment,
                                       @PathVariable(value = "state") Integer state) {
        return equipmentService.updateEquipmentState(equipment, state);
    }

    @ApiOperation("检查设备是否可用")
    @GetMapping("/state/{id}")
    public Result checkEquipmentState(@PathVariable Integer id) {
        return equipmentService.checkEquipmentState(id);
    }


}

