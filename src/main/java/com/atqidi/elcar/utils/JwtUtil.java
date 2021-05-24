package com.atqidi.elcar.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;

/**
 * jwt生成工具类
 *
 * @author 老白
 */
public class JwtUtil {
    /**
     * 盐值,也叫秘钥
     */
    private static String key = "ad128433d8e3356e7024009bf6add2ab";

    //单位是毫秒
//    private static long ttl = Constants.TimeValueInMillions.HOUR_2;//2个小时
    /**
     * 一天
     */
    private static long ttl = Constants.RedisTime.DAY;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        JwtUtil.key = key;
    }

    public long getTtl() {
        return ttl;
    }

    public void setTtl(long ttl) {
        JwtUtil.ttl = ttl;
    }

    /**
     * double token 的解决方案。
     * 续费token...
     * 我们可能通过一个  RefreshToken (1个月有效期，保存在数据库里)来生成新的token (2个小时有效期，保存在redis中)
     *
     * @param userId
     * @param ttl
     * @return
     */
    public static String createRefreshToken(String userId, long ttl) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        JwtBuilder builder = Jwts.builder().
                setId(userId)
                .setIssuedAt(now)
                .signWith(SignatureAlgorithm.HS256, key);
        if (ttl > 0) {
            builder.setExpiration(new Date(nowMillis + ttl));
        }
        return builder.compact();
    }

    /**
     * @param claims 载荷内容
     * @param ttl    有效时长
     * @return
     */
    public static String createToken(Map<String, Object> claims, long ttl) {
        JwtUtil.ttl = ttl;
        return createToken(claims);
    }

    public static String createToken(String id, String username, Map<String, Object> claims, long ttl) {
        JwtUtil.ttl = ttl;
        return createToken(id, username, claims);
    }


    /**
     * @param claims 载荷
     * @return token
     */
    public static String createToken(Map<String, Object> claims) {

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        JwtBuilder builder = Jwts.builder()
                .setIssuedAt(now)
                .signWith(SignatureAlgorithm.HS256, key);

        if (claims != null) {
            builder.setClaims(claims);
        }

        if (ttl > 0) {
            builder.setExpiration(new Date(nowMillis + ttl));
        }
        return builder.compact();
    }

    public static String createToken(String id, String username, Map<String, Object> claims) {

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        JwtBuilder builder = Jwts.builder()
                .setId(id)
                .setSubject(username)
                .setIssuedAt(now)
                .signWith(SignatureAlgorithm.HS256, key);

        if (claims != null) {
            builder.setClaims(claims);
        }

        if (ttl > 0) {
            builder.setExpiration(new Date(nowMillis + ttl));
        }
        return builder.compact();
    }

    /**
     * 解析token
     *
     * @param jwtStr
     * @return
     */
    public static Claims parseJWT(String jwtStr) {
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(jwtStr)
                .getBody();
    }
}
