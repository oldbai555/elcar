package com.atqidi.elcar.service.impl;

import com.atqidi.elcar.entity.Car;
import com.atqidi.elcar.entity.CarType;
import com.atqidi.elcar.entity.User;
import com.atqidi.elcar.mapper.CarMapper;
import com.atqidi.elcar.service.CarService;
import com.atqidi.elcar.service.CarTypeService;
import com.atqidi.elcar.service.UserService;
import com.atqidi.elcar.utils.JwtUtil;
import com.atqidi.elcar.utils.result.Result;
import com.atqidi.elcar.utils.result.ResultCodeEnum;
import com.atqidi.elcar.utils.tools.PageTools;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class CarServiceImpl extends ServiceImpl<CarMapper, Car> implements CarService {


    @Autowired
    CarMapper carMapper;

    @Autowired
    CarTypeService carTypeService;

    @Autowired
    UserService userService;

    QueryWrapper<Car> wrapper;

    /**
     * 添加车辆
     * 需要前端提供。
     * 车辆类型 - id 、 typeName
     * 车辆名称 - carName
     * 先查看这辆车是否存在
     * 不存在则设置它的车辆类型
     * 然后添加
     * TODO 记得把用户的信息添加进去，通过请求头拿到token
     *
     * @param car
     * @param tokenKey
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
    public Result addCar(Car car, String tokenKey) {
        //通过名字查找，看看这个车辆是否存在
        Car carByName = (Car) getCarByName(car.getCarName()).getData();
        if (!StringUtils.isEmpty(carByName)) {
            return Result.fail().message("该车辆已经存在");
        }
        //获取车辆类型
        getCarTypeId(car);
        //TODO 后期使用 token 的时候，把用户信息写入
        User user = (User) userService.checkUser(tokenKey).getData();
        car.setUserId(user.getId());
        int insert = carMapper.insert(car);
        if (insert == 0) {
            return Result.fail().message("添加车辆失败");
        }
        return Result.ok().message("添加车辆成功");
    }

    /**
     * 更新车辆
     * 先查看这辆车是否存在,存在则取出来
     * 然后检查要修改的牌号是否存在重复的
     * 判断是否只修改车辆类型
     * TODO 判断用户是否是同一个人，是就让修改。或者超级管理员也可以修改
     *
     * @param car
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
    public Result updateCar(Car car) {
        Car carById = (Car) getCarById(car.getId()).getData();
        if (StringUtils.isEmpty(carById)) {
            return Result.fail().message("修改车辆不存在");
        }
        Car select = carMapper.selectById(carById.getId());
        //如果修改前和修改后名称不一样，那么就要判断这里车的车辆名称是否重复
        if (!carById.getCarName().equals(car.getCarName())) {
            if (checkCarName(car.getCarName()).getCode().equals(ResultCodeEnum.FAIL.getCode())) {
                return Result.fail().message("修改车辆失败,名称重复");
            }
            select.setCarName(car.getCarName());
        }
        getCarTypeId(car);
        select.setCarTypeId(car.getCarTypeId());
        int update = carMapper.updateById(select);
        if (update == 0) {
            return Result.fail().message("修改车辆失败");
        }
        return Result.ok().message("修改车辆成功");
    }

    /**
     * 给车辆类型ID赋值
     * 先看前端是否直接赋值
     * 如果没有则看是否在车辆类型里填写了车辆类型ID
     * 如果没有就通过名字找到这辆车并且给它赋值
     *
     * @param car
     */
    private void getCarTypeId(Car car) {
        if (StringUtils.isEmpty(car.getCarTypeId())) {
            if (StringUtils.isEmpty(car.getCarType().getId())) {
                CarType carType = (CarType) carTypeService.getCarTypeByName(car.getCarType().getTypeName()).getData();
                car.setCarTypeId(carType.getId());
            } else {
                car.setCarTypeId(car.getCarType().getId());
            }
        }
    }

    /**
     * 通过ID删除车辆
     * 逻辑删除
     * TODO 判断用户是否是同一个人，是就让修改。或者超级管理员也可以修改
     *
     * @param carId
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
    public Result deleteCarById(Integer carId) {
        int delete = carMapper.deleteById(carId);
        if (delete == 0) {
            return Result.fail().message("删除车辆失败");
        }
        return Result.ok().message("删除车辆成功");
    }

    /**
     * 通过ID查找车辆
     *
     * @param carId
     * @return
     */
    @Override
    public Result getCarById(Integer carId) {
        Car car = carMapper.getCarById(carId);
        if (StringUtils.isEmpty(car)) {
            return Result.fail().message("没有该ID的车辆");
        }
        return Result.ok(car).message("查找成功");
    }


    /**
     * 分页查找
     *
     * @param page
     * @param size
     * @return
     */
    @Override
    public Result getCarList(Integer page, Integer size) {
        page = PageTools.getPage(page);
        size = PageTools.getSize(size);
        Page<Car> carPage = new Page<>(page, size);
        IPage<Car> carIPage = carMapper.selectPageVo(carPage);
        return Result.ok(carIPage).message("获取成功");
    }

    /**
     * 通过车辆名称查找车辆
     *
     * @param carName
     * @return
     */
    @Override
    public Result getCarByName(String carName) {
        Car car = carMapper.getOneCarByName(carName);
        if (StringUtils.isEmpty(car)) {
            return Result.fail().message("没有该名称的车辆");
        }
        return Result.ok(car).message("查找成功");
    }


    /**
     * 检查车辆名称是否重复
     *
     * @param carName
     * @return
     */
    @Override
    public Result checkCarName(String carName) {
        Car carByName = (Car) getCarByName(carName).getData();
        if (!StringUtils.isEmpty(carByName)) {
            return Result.fail().message("该车辆已经存在");
        }
        return Result.ok(carByName).message("可以使用该名称");
    }

}
