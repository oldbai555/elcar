package com.atqidi.elcar.utils;

import io.swagger.models.auth.In;

/**
 * 设置默认属性
 *
 * @author 老白
 */
public interface Constants {

    /**
     * 用户的初始化
     */
    interface User {
        //md5的token kdy
        String KEY_TOKEN = "key_token_";
        //验证码的key
        String KEY_CAPTCHA_CONTENT = "key_captcha_content_";
        //默认角色
        Integer DEF_ROSE = 4;
        //超级管理员
        Integer SUPER_ADMIN = 1;
        //修改密码
        int CHANGEPASS = 1;
        //修改手机号
        int CHANGEPHONE = 2;
        //认证身份
        int CHANGEIDCARD = 3;
    }

    /**
     * 单位 毫秒
     */
    interface RedisTime {
        int init = 1000;
        int MIN = 60 * init;
        int HOUR = 60 * MIN;
        int DAY = 24 * HOUR;
        int WEEK = 7 * DAY;
        int MONTH = 30 * DAY;
        int YEAR = 365 * DAY;
    }

    /**
     * redis
     * 统一时间
     * 单位 秒
     */
    interface TimeValue {
        int MIN = 60;
        int HALFT_MIN = 15;
        int HOUR = 60 * MIN;
        int DAY = 24 * HOUR;
        int WEEK = 7 * DAY;
        int MONTH = 30 * DAY;
        int YEAR = 365 * DAY;
    }

    /**
     * 分页配制
     */
    interface Page {
        int DEFAULT_PAGE = 1;
        int MIN_SIZE = 5;
    }

    /**
     * 图片格式限制
     */
    interface ImageType {
        String PREFIX = "image/";
        String TYPE_JGP = "jpg";
        String TYPE_PNG = "png";
        String TYPE_GIF = "gif";
        String TYPE_JPEG = "jpeg";
        String TYPE_JGP_WITH_PREFIX = PREFIX + TYPE_JGP;
        String TYPE_PNG_WITH_PREFIX = PREFIX + TYPE_PNG;
        String TYPE_GIF_WITH_PREFIX = PREFIX + TYPE_GIF;
        String TYPE_JPEG_WITH_PREFIX = PREFIX + TYPE_JPEG;
    }

    /**
     * 设备的默认状态
     */
    interface Equipment {
        Integer CANUSE = 0;
        Integer HAVEUSE = 1;
        Integer DISUSE = 2;
        Integer RTSTATE = 2;
        Integer LTSTATE = 0;
    }

    /**
     * 订单状态：
     * 0-未开始
     * 1-正在进行
     * 2-结束
     * 3-未确认开始超时结束
     */
    interface OrderTable {
        Integer UNSTART = 0;
        Integer STARTING = 1;
        Integer END = 2;
        Integer TIMEOVER = 3;
        String FIRSTNAME = "EL";
    }
}

