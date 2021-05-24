package com.atqidi.elcar.service;

import com.atqidi.elcar.entity.User;
import com.atqidi.elcar.utils.result.Result;
import com.atqidi.elcar.vo.LoginUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.awt.*;
import java.io.IOException;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author oldbai
 * @since 2021-04-26
 */
public interface UserService extends IService<User> {

    Result login(LoginUser user, String captchaKey, String captcha);

    Result register(User user, String captchaKey, String captcha);


    Result checkUser(String token);

    Result updateUserByPassWord(User user, String oldPass, String newPass);

    Result updateUserByPhone(User user, String phone);

    Result updateUserByRose(User user);

    Result deleteUser(Integer id);

    Result getUserByPhoneNoPassword(String phone);

    Result getUserPage(Integer page, Integer size);

    Result logout(String token);

    Result checkPhone(String phone);

    Result checkUserIdCard(String userIdCard, String roseId);

    void createCaptcha(String captchaKey) throws IOException, FontFormatException;
}
