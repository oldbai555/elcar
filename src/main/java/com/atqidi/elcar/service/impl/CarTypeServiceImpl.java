package com.atqidi.elcar.service.impl;

import com.atqidi.elcar.entity.CarType;
import com.atqidi.elcar.mapper.CarTypeMapper;
import com.atqidi.elcar.service.CarTypeService;
import com.atqidi.elcar.utils.result.Result;
import com.atqidi.elcar.utils.tools.PageTools;
import com.atqidi.elcar.vo.CarTyoeVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author oldbai
 * @since 2021-04-26
 */
@Service
@Slf4j
public class CarTypeServiceImpl extends ServiceImpl<CarTypeMapper, CarType> implements CarTypeService {

    @Autowired
    private CarTypeMapper carTypeMapper;

    private QueryWrapper<CarType> wrapper = null;

    /**
     * 添加车辆类型
     * 先判断是否有相同名称的车
     * 没有就添加，并且判断添加是否成功
     *
     * @param carType
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
    public Result addCarType(CarTyoeVO carType) {

        String typeName = carType.getTypeName();
        wrapper = new QueryWrapper<>();
        wrapper.eq("type_name", typeName);
        CarType type = carTypeMapper.selectOne(wrapper);
        if (!StringUtils.isEmpty(type)) {
            return Result.fail().message("已经存在相同名称的类型");
        }
        //赋值操作
        CarType newCarType = new CarType();
        newCarType.setTypeName(carType.getTypeName());
        int insert = carTypeMapper.insert(newCarType);
        if (insert == 0) {
            return Result.fail().message("添加失败");
        }
        return Result.ok().message("添加成功");
    }

    /**
     * 通过Id查找车辆类型
     *
     * @param typeId
     * @return
     */
    @Override
    public Result getCarTypeById(Integer typeId) {
        wrapper = new QueryWrapper<>();
        wrapper.eq("id", typeId);
        CarType carType = carTypeMapper.selectOne(wrapper);
        log.info(carType.toString());
        if (StringUtils.isEmpty(carType)) {
            return Result.fail().message("没有该车辆类型");
        }
        return Result.ok(carType).message("获取车辆类型成功");
    }

    /**
     * 获取车辆列表
     * 当前页
     * 一页多少条数据
     *
     * @param page
     * @param size
     * @return
     */
    @Override
    public Result getCarTypeList(Integer page, Integer size) {
        page = PageTools.getPage(page);
        size = PageTools.getSize(size);
        Page<CarType> carPage = new Page<>(page, size);
        carPage = carTypeMapper.selectPage(carPage, null);
        return Result.ok(carPage).message("获取车辆类型列表成功");
    }

    /**
     * 更新车辆信息
     * 如果更新的名称有重复 不让更新
     * 否则更新完判断是否更新成功
     * 乐观锁使用：先查询 用查询完了的数据进行修改
     *
     * @param carType
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
    public Result updateCarType(CarType carType) {
        CarType type = (CarType) getCarTypeByName(carType.getTypeName()).getData();
        if (!StringUtils.isEmpty(type)) {
            return Result.fail().message("已经存在相同名称的类型");
        }
        type = (CarType) getCarTypeById(carType.getId()).getData();
        type.setTypeName(carType.getTypeName());
        int update = carTypeMapper.updateById(type);
        if (update == 0) {
            return Result.fail().message("更新失败");
        }
        return Result.ok().message("更新成功");
    }

    /**
     * 通过名称获取车辆类型
     *
     * @param name
     * @return
     */
    @Override
    public Result getCarTypeByName(String name) {
        wrapper = new QueryWrapper<>();
        wrapper.eq("type_name", name);
        CarType carType = carTypeMapper.selectOne(wrapper);
        if (StringUtils.isEmpty(carType)) {
            return Result.fail().message("没有该名称的车");
        }
        return Result.ok(carType).message("获取成功");
    }

    /**
     * 删除车辆类型
     *
     * @param typeId
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
    public Result deleteCarType(Integer typeId) {
        int delete = carTypeMapper.deleteById(typeId);
        if (delete == 0) {
            return Result.fail().message("删除失败");
        }
        return Result.ok().message("删除成功");
    }
}
