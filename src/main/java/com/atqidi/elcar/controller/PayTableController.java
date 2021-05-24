package com.atqidi.elcar.controller;


import com.atqidi.elcar.entity.PayTable;
import com.atqidi.elcar.entity.model.ModelEntity;
import com.atqidi.elcar.service.PayTableService;
import com.atqidi.elcar.utils.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author oldbai
 * @since 2021-04-26
 */
@Api(description = "收费表")
@RestController
@RequestMapping("/elcar/pay-table")
@CrossOrigin
public class PayTableController {



    @Autowired
    PayTableService payTableService;

    @ApiOperation("添加收费表")
    @PostMapping("/pay")
    public Result addPayTable(@RequestBody PayTable payTable) {
        return payTableService.addPayTable(payTable);
    }

    @ApiOperation("删除收费表，通过ID")
    @DeleteMapping("/{id}")
    public Result deletePayTable(@PathVariable(value = "id") Integer id) {
        return payTableService.deletePayTable(id);
    }

    @ApiOperation("获取单张收费表，通过ID")
    @GetMapping("/{id}")
    public Result getPayTable(@PathVariable(value = "id") Integer id) {
        return payTableService.getPayTable(id);
    }

    @ApiOperation("获取收费表列表，分页查询")
    @GetMapping("/list/{page}/{size}")
    public Result getPayTableList(@PathVariable(value = "page") Integer page,
                                  @PathVariable(value = "size") Integer size
    ) {
        return payTableService.getPayTableList(page,size);
    }

    @ApiOperation("更新收费表，记得要携带id")
    @PutMapping("pay")
    public Result updatePayTable(@RequestBody PayTable payTable) {
        return payTableService.updatePayTable(payTable);
    }

    @ApiOperation("通过车辆类型获取收费表")
    @GetMapping("car_type/{car_type_id}")
    public Result getPayTableByCarType(@PathVariable(value = "car_type_id") Integer carTypeId) {
        return payTableService.getPayTableByCarType(carTypeId);
    }

}

