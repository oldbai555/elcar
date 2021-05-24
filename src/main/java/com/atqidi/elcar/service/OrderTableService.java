package com.atqidi.elcar.service;

import com.atqidi.elcar.entity.OrderTable;
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
public interface OrderTableService extends IService<OrderTable> {

    Result addOrderTable(OrderTable orderTable);

    Result updateOrderTableByOrderId(String orderId,Integer state);

    Result orderList(String token);

    Result orderListPage(String token, Integer page, Integer size);

    Result selectOrderById(String token, String order_id);

    Result selectOrderByState(String token, Integer choose);
}
