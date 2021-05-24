package com.atqidi.elcar.service.impl;

import cn.hutool.crypto.digest.BCrypt;
import cn.hutool.crypto.digest.DigestUtil;
import com.atqidi.elcar.entity.RefreshToken;
import com.atqidi.elcar.entity.Rose;
import com.atqidi.elcar.entity.User;
import com.atqidi.elcar.mapper.RefreshTokenMapper;
import com.atqidi.elcar.mapper.RoseMapper;
import com.atqidi.elcar.mapper.UserMapper;
import com.atqidi.elcar.service.UserService;
import com.atqidi.elcar.utils.*;
import com.atqidi.elcar.utils.result.Result;
import com.atqidi.elcar.utils.result.ResultCodeEnum;
import com.atqidi.elcar.utils.tools.PageTools;
import com.atqidi.elcar.vo.LoginUser;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 * TODO 权限认证
 * 删除 修改
 *
 * @author oldbai
 * @since 2021-04-26
 */
@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    UserMapper userMapper;
    @Autowired
    RoseMapper roseMapper;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    RefreshTokenMapper refreshTokenMapper;


    QueryWrapper<User> wrapper;
    HttpServletRequest request = null;
    HttpServletResponse response = null;

    /**
     * 登陆：
     * 1.用户需要携带 ：手机号、密码、验证码
     * TODO 后期使用阿里云短信验证码，可以使用验证码一键登录
     * 2.对数据库进行保存或更新 token 信息
     * 3.对redis进行保存或更新 token 信息
     * 4.登陆成功返回 token 信息，使用 MD5 加密返回 token-key
     *
     * @param user
     * @param captchaKey
     * @param captcha
     * @return
     */
    @Override
    public Result login(LoginUser user, String captchaKey, String captcha) {
        Result result = checkPhone(user.getPhone());
        //通过手机号查找该用户是否存在
        if (result.getCode().equals(ResultCodeEnum.SUCCESS.getCode())) {
            return result.message("手机号不存在");
        }
        //检查验证码
        String cp = (String) redisUtil.get(Constants.User.KEY_CAPTCHA_CONTENT + captchaKey);
        if (StringUtils.isEmpty(cp) || !cp.toLowerCase().equals(captcha.toLowerCase())) {
            return Result.fail().message("验证码错误，请重新获取验证码");
        }
        //验证码成功后，就删了验证码
        redisUtil.del(Constants.User.KEY_CAPTCHA_CONTENT + captchaKey);

        User userByPhone = (User) getUserByPhoneFromDB(user.getPhone());
        //检查密码
        //使用checkpw方法检查被加密的字符串是否与原始字符串匹配：
     /* if (!BCrypt.checkpw(user.getPassword(), userByPhone.getPassword())) {
            return result.message("密码错误");
        }*/
        if (!DigestUtil.md5Hex(user.getPassword().getBytes()).equals(userByPhone.getPassword())) {
            return result.message("密码错误");
        }
        //密码正确则生成 token
        String tokenKey = createToken(userByPhone);
        //返回token-key给前端
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", tokenKey);
        return Result.ok(hashMap).message("登陆成功");
    }

    private User getUserByPhoneFromDB(String phone) {
        wrapper = new QueryWrapper<>();
        wrapper.eq("phone", phone);
        User user = (User) userMapper.selectOne(wrapper);
        return user;
    }

    /**
     * 生成 token
     *
     * @param user
     * @return
     */
    private String createToken(User user) {
        //先通过id删除数据库有的数据
        QueryWrapper<RefreshToken> refreshTokenQueryWrapper = new QueryWrapper<>();
        refreshTokenQueryWrapper.eq("user_id", user.getId());
        refreshTokenMapper.delete(refreshTokenQueryWrapper);
        Map<String, Object> map = ClaimsUtils.User2Claims(user);
        String token = JwtUtil.createToken(map);
        //MD5 序列化生成 token-key
        String tokenKey = BCrypt.hashpw(token);
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUserId(user.getId());
        refreshToken.setRefreshToken(token);
        refreshToken.setTokenKey(tokenKey);
        //存入数据库
        int insert = refreshTokenMapper.insert(refreshToken);
        if (insert == 0) {
            log.info("存入失败");
        }
        //存入redis
        redisUtil.set(Constants.User.KEY_TOKEN + tokenKey, token);
        return tokenKey;
    }

    /**
     * 注册：
     * 1.用户注册需要携带：手机号、密码、验证码
     * 默认普通社会人士，可以进行认证，通过修改用户信息进行认证身份。
     * TODO 后期使用短信认证一键注册，只需要输入密码即可
     * 2.检查手机号是否重复
     * 3.完成注册
     *
     * @param user
     * @param captchaKey
     * @param captcha
     * @return
     */
    @Override
    public Result register(User user, String captchaKey, String captcha) {
        Result result = checkPhone(user.getPhone());
        //检查手机号是否可以用
        if (result.getCode().equals(ResultCodeEnum.FAIL.getCode())) {
            return result;
        }
        //检查手机格式
        if (!ValidateUtil.validateMobile(user.getPhone())) {
            return result.message("手机号格式不对");
        }

        //检查验证码是否正确
        String cp = (String) redisUtil.get(Constants.User.KEY_CAPTCHA_CONTENT + captchaKey);
        if (!cp.toLowerCase().equals(captcha.toLowerCase())) {
            return Result.fail().message("验证码错误");
        }

        //删除redis的验证码缓存
        redisUtil.del(Constants.User.KEY_CAPTCHA_CONTENT + captchaKey);
        //密码加密
        user.setPassword(DigestUtil.md5Hex(user.getPassword().getBytes()));
        //设置用户默认角色
        user.setRoleId(Constants.User.DEF_ROSE);
        int insert = userMapper.insert(user);
        if (insert == 0) {
            return Result.fail().message("注册失败");
        }
        return Result.ok().message("注册成功");
    }


    /**
     * 检查用户是否登录：
     * 1.通过 token-key 去redis查找，有则解析 token 返回用户信息
     * 2.通过 token-key 去mysql查找，有则解析 token 返回用户信息
     * 3.都没有表示用户登录信息过期了，要求重新登陆
     *
     * @param token
     * @return
     */
    @Override
    public Result checkUser(String token) {
        getRequestAndResponse();
        //如果前端不传token过来，我们自己取
        if (StringUtils.isEmpty(token)) {
            token = request.getHeader("token");
        }
        //去redis查找用户是否存在
        String redisToken = (String) redisUtil.get(Constants.User.KEY_TOKEN + token);
        if (StringUtils.isEmpty(redisToken)) {
            //如果为空那就去数据库找
            QueryWrapper<RefreshToken> tokenQueryWrapper = new QueryWrapper<>();
            tokenQueryWrapper.eq("token_key", token);
            RefreshToken refreshToken = refreshTokenMapper.selectOne(tokenQueryWrapper);
            if (StringUtils.isEmpty(refreshToken)) {
                return Result.fail().message("登陆信息失效，请重新登陆");
            }
            //找到token
            redisToken = refreshToken.getRefreshToken();
            //重新将token生成保存
            String hashpw = BCrypt.hashpw(redisToken);
            //TODO 将Token保存进入redis，不足之处，没有延长更新时间
            redisUtil.set(Constants.User.KEY_TOKEN + hashpw, redisToken);
        }
        //解析token,得到用户信息
        User user = ClaimsUtils.claims2ToUser(JwtUtil.parseJWT(redisToken));
        return Result.ok(user).message("用户存在");
    }

    /**
     * 更新用户信息
     * 0.判断是否是同一个用户或者超级管理员
     * 1.判断是否是修改手机号
     * 2.是否是认证用户身份
     * 3.统一规则 ：
     * 改密码 - 1
     * 换手机 - 2
     * 认证身份 - 3
     *
     * @param user
     * @param oldPass
     * @param newPass
     * @return
     */
    @Override
    public Result updateUserByPassWord(User user, String oldPass, String newPass) {
        //TODO 后面开通管理员超级权限
        getRequestAndResponse();
        //改密码：
        //1.检查用户是否登录，登录用户的手机号和修改的用户的手机号是否一致
        User tokenUser = (User) checkUser(null).getData();
        if (StringUtils.isEmpty(tokenUser)) {
            return Result.fail("未登录");
        }
        if (!tokenUser.getPhone().equals(user.getPhone())) {
            return Result.fail("登陆用户不一致");
        }
        //2.检查密码是否为空（前端使用 md5 加密把新旧密码传过来）
        if (StringUtils.isEmpty(oldPass) || StringUtils.isEmpty(newPass)) {
            return Result.fail("密码不为空");
        }
        //3.通过手机号查询数据库得到这个用户的信息
        wrapper = new QueryWrapper<>();
        wrapper.eq("phone", tokenUser.getPhone());
        User selectUser = userMapper.selectOne(wrapper);
        //4.进行旧密码比对，旧密码比对成功后进行新密码更新（可以给新密码和旧密码进行比对看看是否两次密码都一样）
        //密码错误返回 false 正确返回 ture
        if (!oldPass.equals(selectUser.getPassword())) {
            return Result.fail("旧密码不一致");
        }
        //TODO 可以给新密码和旧密码进行比对看看是否两次密码都一样
        if (newPass.equals(selectUser.getPassword())) {
            return Result.fail("两次密码不能一样");
        }
        selectUser.setPassword(newPass);
        int i = userMapper.updateById(selectUser);
        if (i == 0) {
            return Result.fail("更新失败");
        } else {
            //更新成功后，将token给删了
            logout(request.getHeader("token"));
            return Result.ok("更新成功");
        }
    }


    /**
     * 修改手机号
     *
     * @param user
     * @param phone
     * @return
     */
    @Override
    public Result updateUserByPhone(User user, String phone) {
        return null;
    }

    /**
     * 认证身份
     *
     * @param user
     * @return
     */
    @Override
    public Result updateUserByRose(User user) {
        //TODO 后面开通管理员超级权限
        //0.检查用户是否登录，登录用户的手机号和修改的用户的手机号是否一致
        User tokenUser = (User) checkUser(null).getData();
        if (StringUtils.isEmpty(tokenUser)) {
            return Result.fail("未登录");
        }
        if (!tokenUser.getPhone().equals(user.getPhone())) {
            return Result.fail("登陆用户不一致");
        }
        //1.检查是否传入角色ID
        if (StringUtils.isEmpty(user.getRoleId()) || user.getRoleId().equals(Constants.User.SUPER_ADMIN)) {
            return Result.fail("请传入正确角色");
        }
        //还得检查是否有这个角色
        Rose rose = roseMapper.selectById(user.getRoleId());
        if (StringUtils.isEmpty(rose)){
            return Result.fail("请传入正确角色");
        }
        //2.检查是否传入身份ID
        if (StringUtils.isEmpty(user.getUserIdCard())) {
            return Result.fail("请传入身份ID码");
        }
        //3.通过数据库检查，是否存在这个身份的这ID
        wrapper = new QueryWrapper<>();
        wrapper.eq("role_id", user.getRoleId()).eq("user_id_card", user.getUserIdCard());
        User one = userMapper.selectOne(wrapper);
        if (!StringUtils.isEmpty(one)) {
            return Result.fail("认证失败，角色或身份码错误");
        }
        User fromDB = getUserByPhoneFromDB(user.getPhone());
        //TODO 可以考虑加个前缀
        fromDB.setRoleId(user.getRoleId());
        fromDB.setUserIdCard(user.getUserIdCard());
        int update = userMapper.updateById(fromDB);
        if (update == 0) {
            return Result.fail("认证失败");
        }
        //4.如果都没有那么就更新角色的信息
        getRequestAndResponse();
        logout(request.getHeader("token"));
        return Result.ok("认证成功");
    }

    /**
     * 删除用户：
     * 0.判断是否是同一个用户或者超级管理员
     *
     * @param id
     * @return
     */
    @Override
    public Result deleteUser(Integer id) {

        int delete = userMapper.deleteById(id);
        if (delete == 0) {
            return Result.fail().message("删除错误");
        }
        return Result.ok().message("删除成功");
    }

    /**
     * 通过手机号查找用户，无密码
     *
     * @param phone
     * @return
     */
    @Override
    public Result getUserByPhoneNoPassword(String phone) {
        User user = userMapper.findByPhone(phone);
        if (StringUtils.isEmpty(user)) {
            return Result.fail().message("没有该用户");
        }
        return Result.ok(user).message("查找成功");
    }

    /**
     * 通过手机号查找用户，有密码
     *
     * @param phone
     * @return
     */
    public Result getUserByPhone(String phone) {
        wrapper = new QueryWrapper<>();
        wrapper.eq("phone", phone);
        User user = userMapper.selectOne(wrapper);
        if (StringUtils.isEmpty(user)) {
            return Result.fail().message("没有该用户");
        }
        return Result.ok(user).message("查找成功");
    }

    @Override
    public Result getUserPage(Integer page, Integer size) {
        page = PageTools.getPage(page);
        size = PageTools.getSize(size);
        Page<User> userPage = new Page<>(page, size);
        IPage<User> allUser = userMapper.findAllUser(userPage);
        return Result.ok(allUser).message("查找成功");
    }

    /**
     * 前端传token-key过来
     * 退出登陆：
     * 删除 redis 的 token
     * 删除 mysql 的 token
     *
     * @param tokenKey
     * @return
     */
    @Override
    public Result logout(String tokenKey) {
        if (StringUtils.isEmpty(tokenKey)) {
            return Result.fail().message("当前用户未登录");
        }
        QueryWrapper<RefreshToken> refreshTokenQueryWrapper = new QueryWrapper<>();
        refreshTokenQueryWrapper.eq("token_key", tokenKey);
        int delete = refreshTokenMapper.delete(refreshTokenQueryWrapper);
        if (delete == 0) {
            return Result.fail().message("退出登陆失败，请稍后再试");
        }
        redisUtil.del(Constants.User.KEY_TOKEN + tokenKey);
        return Result.ok().message("退出登陆成功");
    }

    /**
     * 检查手机号是否可用
     *
     * @param phone
     * @return
     */
    @Override
    public Result checkPhone(String phone) {
        wrapper = new QueryWrapper<>();
        wrapper.eq("phone", phone).select("phone");
        User user = userMapper.selectOne(wrapper);
        if (!StringUtils.isEmpty(user)) {
            return Result.fail().message("手机号已被注册");
        }
        return Result.ok().message("手机号可用");
    }

    /**
     * 检查身份证信息是否重复
     *
     * @param userIdCard
     * @param roseId
     * @return
     */
    @Override
    public Result checkUserIdCard(String userIdCard, String roseId) {
        wrapper = new QueryWrapper<>();
        wrapper.eq("user_id_card", userIdCard).eq("role_id", roseId).select("user_id_card");
        User user = userMapper.selectOne(wrapper);
        if (StringUtils.isEmpty(user)) {
            return Result.fail().message("证件号重复");
        }
        return Result.ok().message("证件号可用");
    }

    /**
     * 获取人类验证码
     *
     * @return
     */
    @Override
    public void createCaptcha(String captchaKey) throws IOException, FontFormatException {
        getRequestAndResponse();
        //进行判断是否传入key
        if (StringUtils.isEmpty(captchaKey) || !(captchaKey.length() < 20)) {
            return;
        }
        long key = 01;
        try {
            key = Long.parseLong(captchaKey);
        } catch (Exception e) {
            return;
        }
        //可以用了

        // 设置请求头为输出图片类型
        response.setContentType("image/gif");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        // 三个参数分别为宽、高、位数
        SpecCaptcha specCaptcha = new SpecCaptcha(200, 60, 5);
        // 设置字体
        // specCaptcha.setFont(new Font("Verdana", Font.PLAIN, 32));  // 有默认字体，可以不用设置
        specCaptcha.setFont(Captcha.FONT_1);
        // 设置类型，纯数字、纯字母、字母数字混合
        specCaptcha.setCharType(Captcha.TYPE_DEFAULT);

        String content = specCaptcha.text().toLowerCase();
        log.info("captcha content == > " + content);
        // 验证码存入redis , 5 分钟内有效
        //删除时机
        //1.自然过期，比如 10 分钟后过期
        //2.验证码用完后删除
        //3.用完的情况：有get的地方
        redisUtil.set(Constants.User.KEY_CAPTCHA_CONTENT + key, content, Constants.TimeValue.MIN * 5);
        // 输出图片流
        specCaptcha.out(response.getOutputStream());
    }

    public void getRequestAndResponse() {
        this.request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        this.response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }

}
