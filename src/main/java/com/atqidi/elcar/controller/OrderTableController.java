package com.atqidi.elcar.controller;


import com.atqidi.elcar.entity.Equipment;
import com.atqidi.elcar.entity.OrderTable;

import com.atqidi.elcar.service.OrderTableService;
import com.atqidi.elcar.utils.Constants;

import com.atqidi.elcar.utils.result.Result;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author oldbai
 * @since 2021-04-26
 */
@Api(description = "订单表")
@RestController
@RequestMapping("/elcar/order-table")
@CrossOrigin
public class OrderTableController {

    @Autowired
    OrderTableService orderTableService;


    /**
     * 创建订单，插入时间就是订单开始时间
     * 订单开始创建放redis里，五分钟内不付款，就取消订单。
     * 订单状态：
     * 0-未开始
     * 1-正在进行
     * 2-结束
     * 3-未确认开始超时结束
     *
     * @param orderTable
     * @return
     */
    @ApiOperation("添加订单")
    @PostMapping("order")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", dataType = "String", required = true, value = "token")
    })
    public Result addOrderTable(@RequestBody OrderTable orderTable) {
        return orderTableService.addOrderTable(orderTable);
    }


    @ApiOperation("确认订单")
    @PutMapping("start/{orderId}")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", dataType = "String", required = true, value = "token")
    })
    public Result updateOrderTableByOrderIdAndStart(@PathVariable String orderId) {
        return orderTableService.updateOrderTableByOrderId(orderId, Constants.OrderTable.STARTING);
    }

    @ApiOperation("结束订单")
    @PutMapping("end/{orderId}")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", dataType = "String", required = true, value = "token")
    })
    public Result updateOrderTableByOrderIdAndEnd(@PathVariable String orderId) {
        return orderTableService.updateOrderTableByOrderId(orderId, Constants.OrderTable.END);
    }

    @ApiOperation("订单超时")
    @PutMapping("overTime/{orderId}")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", dataType = "String", required = true, value = "token")
    })
    public Result updateOrderTableByOrderIdAndOverTime(@PathVariable String orderId) {
        return orderTableService.updateOrderTableByOrderId(orderId, Constants.OrderTable.TIMEOVER);
    }


    /**
     * 1.获取当前用户的订单（分页或不分页）
     * <p>
     * 2.合计金额
     */
    @ApiOperation("获取当前用户的所有订单")
    @GetMapping("list")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", dataType = "String", required = true, value = "token")
    })
    public Result orderList(@RequestHeader(value = "token") String token) {
        return orderTableService.orderList(token);
    }

    @ApiOperation("获取当前用户的所有订单-分页")
    @GetMapping("list/{page}/{size}")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", dataType = "String", required = true, value = "token")
    })
    public Result orderListPage(@RequestHeader(value = "token") String token,
                                @PathVariable Integer page,
                                @PathVariable Integer size) {
        return orderTableService.orderListPage(token, page, size);
    }

    @ApiOperation("获取当前用户的订单")
    @GetMapping("order/{order_id}")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", dataType = "String", required = true, value = "token")
    })
    public Result selectOrderById(@RequestHeader(value = "token") String token,
                                  @PathVariable String order_id) {
        return orderTableService.selectOrderById(token, order_id);
    }

    @ApiOperation("获取当前用户不同状态的订单")
    @GetMapping("state/{choose}")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", dataType = "String", required = true, value = "token")
    })
    public Result selectOrderByState(@RequestHeader(value = "token") String token,
                                     @PathVariable Integer choose) {
        return orderTableService.selectOrderByState(token,choose);
    }
}

