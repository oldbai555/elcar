package com.atqidi.elcar.service.impl;

import com.atqidi.elcar.entity.Rose;
import com.atqidi.elcar.mapper.RoseMapper;
import com.atqidi.elcar.service.RoseService;
import com.atqidi.elcar.utils.result.Result;
import com.atqidi.elcar.utils.result.ResultCodeEnum;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
 * TODO 后边添加权限管理时，只允许管理员操作
 *
 * @author oldbai
 * @since 2021-04-26
 */
@Service
public class RoseServiceImpl extends ServiceImpl<RoseMapper, Rose> implements RoseService {

    @Autowired
    RoseMapper roseMapper;

    QueryWrapper<Rose> wrapper;

    /**
     * 添加角色
     * 提供角色名称
     *
     * @param rose
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
    public Result addRose(Rose rose) {
        if (checkRoseByName(rose.getName()).getCode().equals(ResultCodeEnum.FAIL.getCode())) {
            return Result.fail().message("角色名已存在，添加失败");
        }
        int insert = roseMapper.insert(rose);
        if (insert == 0) {
            return Result.fail().message("添加失败");
        }
        return Result.ok().message("添加成功");
    }

    /**
     * 修改角色名称
     * 只需要判断是否名称是否重复就行了
     *
     * @param rose
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
    public Result updateRose(Rose rose) {
        Rose checkRose = (Rose) getRoseById(rose.getId()).getData();
        if (StringUtils.isEmpty(checkRose)) {
            return Result.fail().message("没有要修改的目标");
        }
        if (checkRose.getName().equals(rose.getName())) {
            return Result.fail().message("角色名已存在，添加失败");
        }
        Rose selectById = roseMapper.selectById(rose.getId());
        selectById.setName(rose.getName());
        int update = roseMapper.updateById(selectById);
        if (update == 0) {
            return Result.fail().message("修改失败");
        }
        return Result.ok().message("修改成功");
    }

    /**
     * 通过ID删除角色
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
    public Result deleteRose(Integer id) {
        int delete = roseMapper.deleteById(id);
        if (delete == 0) {
            return Result.fail().message("删除失败");
        }
        return Result.ok().message("删除成功");
    }

    /**
     * 通过ID查找角色
     *
     * @param id
     * @return
     */
    @Override
    public Result getRoseById(Integer id) {
        wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        Rose one = roseMapper.selectOne(wrapper);
        if (StringUtils.isEmpty(one)) {
            return Result.fail().message("查找失败");
        }
        return Result.ok(one).message("查找成功");
    }

    /**
     * 分页查询角色
     *
     * @param page
     * @param size
     * @return
     */
    @Override
    public Result getRoseList(Integer page, Integer size) {
        Page<Rose> rosePage = new Page<>(page, size);
        rosePage = roseMapper.selectPage(rosePage, null);
        return Result.ok(rosePage).message("查找成功");
    }

    /**
     * 通过名称查找角色
     *
     * @param name
     * @return
     */
    @Override
    public Result getRoseByName(String name) {
        Result result = checkRoseByName(name);
        if (result.getCode().equals(ResultCodeEnum.SUCCESS.getCode())) {
            return result.message("查找失败");
        }
        return result.message("查找成功");

    }

    /**
     * 检查角色名是否重复
     *
     * @param name
     * @return
     */
    @Override
    public Result checkRoseByName(String name) {
        wrapper = new QueryWrapper<>();
        wrapper.eq("name", name);
        Rose one = roseMapper.selectOne(wrapper);
        if (StringUtils.isEmpty(one)) {
            return Result.ok().message("角色名未存在");
        }
        return Result.fail(one).message("角色名已存在");
    }
}
