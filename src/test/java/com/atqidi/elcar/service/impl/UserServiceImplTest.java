package com.atqidi.elcar.service.impl;

import cn.hutool.crypto.digest.BCrypt;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.digest.MD5;
import com.atqidi.elcar.entity.User;
import com.atqidi.elcar.mapper.UserMapper;
import com.atqidi.elcar.service.UserService;
import com.atqidi.elcar.utils.result.Result;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jdk.nashorn.internal.ir.CallNode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplTest {

    @Autowired
    UserService userService;

    @Test
    public void test() {
        System.out.println(DigestUtil.md5Hex("123456".getBytes()));
        System.out.println(DigestUtil.md5Hex("1234567".getBytes()));
    }
}