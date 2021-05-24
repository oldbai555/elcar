package com.atqidi.elcar.mapper;

import com.atqidi.elcar.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author oldbai
 * @since 2021-04-26
 */
@Repository
public interface UserMapper extends BaseMapper<User> {
    /**
     * 分页查询用户
     * @param page
     * @return
     */
    IPage<User> findAllUser(Page<?> page);

    /**
     * 通过手机号查找
     *
     * @param phone
     * @return
     */
    User findByPhone(String phone);

    /**
     * 通过身份证或者教师证等查找
     *
     * @param userIdCard
     * @return
     */
    User findByUserIdCard(String userIdCard);
}
