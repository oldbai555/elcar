package com.atqidi.elcar.controller;


import com.atqidi.elcar.entity.User;
import com.atqidi.elcar.service.UserService;
import com.atqidi.elcar.utils.Constants;
import com.atqidi.elcar.utils.result.Result;
import com.atqidi.elcar.vo.LoginUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author oldbai
 * @since 2021-04-26
 */
@Slf4j
@Api(description = "用户")
@RestController
@RequestMapping("/elcar/user")
@CrossOrigin
public class UserController {

    @Autowired
    UserService userService;

    @ApiOperation("登陆")
    @PostMapping("login")
    public Result login(@RequestBody LoginUser user,
                        @RequestParam("captcha_key") String captchaKey,
                        @RequestParam("captcha") String captcha) {
        return userService.login(user, captchaKey, captcha);
    }

    @ApiOperation("退出登陆")
    @GetMapping("logout")
    public Result logout(@RequestHeader("token") String token) {
        return userService.logout(token);
    }

    /**
     * TODO 未使用阿里云短信认证服务，使用了就用验证码进行注册
     *
     * @return
     */
    @ApiOperation("注册")
    @PostMapping("register")
    public Result register(@RequestBody User user,
                           @RequestParam("captcha_key") String captchaKey,
                           @RequestParam("captcha") String captcha) {
        return userService.register(user, captchaKey, captcha);
    }

    @ApiOperation("获取登陆验证码,前端携带一个唯一标识，标识是这一次的验证码")
    @GetMapping("getCaptcha")
    public void getCaptcha(@RequestParam("captcha_key") String captchaKey) {
        try {
            userService.createCaptcha(captchaKey);
        } catch (Exception e) {
            log.error(e.toString());
        }
    }

    @ApiOperation("检查用户是否登录,通过请求头携带token,key为token,value为token的值。登陆则返回当前设备登陆的用户信息")
    @PostMapping("checkUser")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", dataType = "String", required = true, value = "token")
    })
    public Result checkUser(@RequestHeader("token") String token) {
        return userService.checkUser(token);
    }

    @ApiOperation("更新用户，记得携带用户ID(有可能用户想更换手机号)")
    @PutMapping("user/{choose}")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", dataType = "String", required = true, value = "token")
    })
    public Result update(@RequestBody User user,
                         @PathVariable(value = "choose", required = true) Integer choose,
                         @RequestParam(value = "oldPass", required = false) String oldPass,
                         @RequestParam(value = "newPass", required = false) String newPass,
                         @RequestParam(value = "phone", required = false) String phone
    ) {
        switch (choose) {
            case Constants.User.CHANGEPASS:
                return userService.updateUserByPassWord(user, oldPass, newPass);
            case Constants.User.CHANGEPHONE:
                return userService.updateUserByPhone(user, phone);
            case Constants.User.CHANGEIDCARD:
                return userService.updateUserByRose(user);
            default:
                return Result.fail("修改失败");
        }
    }

    @ApiOperation("通过ID删除用户")
    @DeleteMapping("{ID}")
    public Result deleteUser(@PathVariable("ID") Integer id) {
        return userService.deleteUser(id);
    }

    @ApiOperation("通过手机号获取用户")
    @GetMapping("user/{phone}")
    public Result getUserByPhone(@PathVariable("phone") String phone) {
        return userService.getUserByPhoneNoPassword(phone);
    }

    @ApiOperation("获取用户列表")
    @GetMapping("user/{page}/{size}")
    public Result getUserByPhone(@PathVariable("page") Integer page,
                                 @PathVariable("size") Integer size) {
        return userService.getUserPage(page, size);
    }


}

