package com.atqidi.elcar.mapper;

import com.atqidi.elcar.entity.Car;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author oldbai
 * @since 2021-04-26
 */
@Repository
public interface CarMapper extends BaseMapper<Car> {
    /**
     * 查询所有,自定义方法
     */
    List<Car> getAllCar();
    IPage<Car> selectPageVo(Page<?> page);
    Car getCarById(Integer id);
    Car getOneCarByName(String name);
//    Page<Car> selectListPage(Page<Car> carPage);
}
