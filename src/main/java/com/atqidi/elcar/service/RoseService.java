package com.atqidi.elcar.service;

import com.atqidi.elcar.entity.Rose;
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
public interface RoseService extends IService<Rose> {

    Result addRose(Rose rose);

    Result updateRose(Rose rose);

    Result deleteRose(Integer id);

    Result getRoseById(Integer id);

    Result getRoseList(Integer page, Integer size);

    Result getRoseByName(String name);

    Result checkRoseByName(String name);
}
