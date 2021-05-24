package com.atqidi.elcar.utils;

import com.atqidi.elcar.entity.User;
import io.jsonwebtoken.Claims;

import java.util.HashMap;
import java.util.Map;

/**
 * token 的 Claims 工具类
 *
 * @author 老白
 */
public class ClaimsUtils {


    public static final String ID = "id";
    public static final String PHONE = "phone";
    public static final String ROLES = "role";

    /**
     * 封装user信息
     * 得到一个jwt载荷
     *
     * @return
     */
    public static Map<String, Object> User2Claims(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(ID, user.getId());
        claims.put(PHONE, user.getPhone());
        claims.put(ROLES, user.getRose());
        return claims;
    }

    /**
     * 解析jwt载荷
     * 得到user对象
     *
     * @param claims
     * @return
     */
    public static User claims2ToUser(Claims claims) {
        User user = new User();
        user.setId((Integer) claims.get(ID));
        user.setPhone((String) claims.get(PHONE));
        user.setRose((String) claims.get(ROLES));
        return user;
    }
}
