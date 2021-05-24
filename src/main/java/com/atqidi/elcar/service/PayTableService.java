package com.atqidi.elcar.service;

import com.atqidi.elcar.entity.PayTable;
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
public interface PayTableService extends IService<PayTable> {

    Result addPayTable(PayTable payTable);

    Result deletePayTable(Integer id);

    Result getPayTable(Integer id);

    Result getPayTableList(Integer page, Integer size);

    Result updatePayTable(PayTable payTable);

    Result getPayTableByCarType(Integer carTypeId);
}
