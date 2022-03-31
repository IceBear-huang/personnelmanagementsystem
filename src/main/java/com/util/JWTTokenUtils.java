package com.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.config.JWTConfig;
import com.pojo.SysUserDetails;
import com.service.SysUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * ClassName: JWTTokenUtils
 * Description:
 * date: 2021/11/22 11:17
 *
 * @author WhiteBear
 */
@Slf4j
@Component
public class JWTTokenUtils {

    /**
     * 时间格式化
     */
    private static final DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private SysUserDetailsService sysUserDetailsService;

    private static JWTTokenUtils jwtTokenUtils;

    @PostConstruct
    public void init () {
        jwtTokenUtils = this;
        jwtTokenUtils.sysUserDetailsService = this.sysUserDetailsService;
    }

    /**
     * 创建Token
     *
     * @param sysUserDetails 用户信息
     * @return
     */
    public static String createAccessToken (SysUserDetails sysUserDetails) {
        // 设置JWT
        String token = Jwts.builder()
                // 用户Id
                .setId(sysUserDetails.getId().toString())
                // 用户名
                .setSubject(sysUserDetails.getUsername())
                // 签发时间
                .setIssuedAt(new Date())
                // 签发者
                .setIssuer("whitebear")
                // 过期时间
                .setExpiration(new Date(System.currentTimeMillis() + JWTConfig.expiration))
                // 签名算法、密钥
                .signWith(SignatureAlgorithm.HS512, JWTConfig.secret)
                // 自定义其他属性，如用户组织机构ID，用户所拥有的角色，用户权限信息等
                .claim("authorities", JSON.toJSONString(sysUserDetails.getAuthorities()))
                .claim("ip", sysUserDetails.getIp()).compact();

        return JWTConfig.tokenPrefix + token;
    }

    /**
     * 刷新Token
     *
     * @param oldToken 过期但未超过刷新时间的Token
     * @return
     */
    public static String refreshAccessToken (String oldToken) {
        String username = JWTTokenUtils.getUserNameByToken(oldToken);
        SysUserDetails sysUserDetails =
                (SysUserDetails) jwtTokenUtils.sysUserDetailsService
                .loadUserByUsername(username);

        sysUserDetails.setIp(JWTTokenUtils.getIpByToken(oldToken));
        return createAccessToken(sysUserDetails);
    }

    /**
     * 解析Token
     *
     * @param token Token信息
     * @return
     */
    public static SysUserDetails parseAccessToken (String token) {
        SysUserDetails sysUserDetails = null;
        if (StringUtils.isNotEmpty(token)) {
            try {
                // 去除JWT前缀
                token = token.substring(JWTConfig.tokenPrefix.length());

                // 解析Token
                Claims claims = Jwts.parser().setSigningKey(JWTConfig.secret).parseClaimsJws(token).getBody();

                // 获取用户信息
                sysUserDetails = new SysUserDetails();
                sysUserDetails.setId(Integer.parseInt(claims.getId()));
                sysUserDetails.setUsername(claims.getSubject());
                // 获取角色
                Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
                String authority = claims.get("authorities").toString();
                if (StringUtils.isNotEmpty(authority)) {
                    List<Map<String, String>> authorityList = JSON.parseObject(authority,
                            new TypeReference<List<Map<String, String>>>() {
                            });
                    for (Map<String, String> role : authorityList) {
                        if (!role.isEmpty()) {
                            authorities.add(new SimpleGrantedAuthority(role.get("authority")));
                        }
                    }
                }
                sysUserDetails.setAuthorities(authorities);
            } catch (Exception e) {
                log.error("解析Token异常：" + e);
            }
        }
        return sysUserDetails;
    }


    /**
     * 保存Token信息到Redis中
     *
     * @param token    Token信息
     * @param username 用户名
     * @param ip       IP
     */
    public static void setTokenInfo (String token, String username, String ip) {
        if (StringUtils.isNotEmpty(token)) {
            // 去除JWT前缀
            token = token.substring(JWTConfig.tokenPrefix.length());

            LocalDateTime localDateTime = LocalDateTime.now();

            RedisUtils.hset(token, "username", username);
            RedisUtils.set(username, token);
            RedisUtils.hset(token, "ip", ip);

            RedisUtils.hset(token, "expiration",
                    df.format(localDateTime.plus(JWTConfig.expiration, ChronoUnit.MILLIS)));
        }
    }

    /**
     * Redis中删除Token
     *
     * @param token Token信息
     */
    public static void deleteRedisToken (String token) {
        if (StringUtils.isNotEmpty(token)) {
            // 去除JWT前缀
            token = token.substring(JWTConfig.tokenPrefix.length());
            RedisUtils.deleteKey(token);
        }
    }

    /**
     * 是否过期
     *
     * @param expiration 过期时间，字符串
     * @return 过期返回True，未过期返回false
     */
    public static boolean isExpiration (String expiration) {
        LocalDateTime expirationTime = LocalDateTime.parse(expiration, df);
        LocalDateTime localDateTime = LocalDateTime.now();
        if (localDateTime.compareTo(expirationTime) > 0) {
            return true;
        }
        return false;
    }

    /**
     * 查询redis中的username的token
     *
     */
    public static String queryToken(String username) {
        if (!StringUtils.isEmpty(username)) {
            if (RedisUtils.get(username) == null) {
                return null;
            }
            return RedisUtils.get(username).toString();
        }
        return null;
    }

    /**
     * 检查Redis中是否存在Token
     *
     * @param token Token信息
     * @return
     */
    public static boolean hasToken (String token) {
        if (StringUtils.isNotEmpty(token)) {
            // 去除JWT前缀
            token = token.substring(JWTConfig.tokenPrefix.length());
            return RedisUtils.hasKey(token);
        }
        return false;
    }

    /**
     * 从Redis中获取过期时间
     *
     * @param token Token信息
     * @return 过期时间，字符串
     */
    public static String getExpirationByToken (String token) {
        if (StringUtils.isNotEmpty(token)) {
            // 去除JWT前缀
            token = token.substring(JWTConfig.tokenPrefix.length());
            return RedisUtils.hget(token, "expiration").toString();
        }
        return null;
    }

    /**
     * 从Redis中获取用户名
     *
     * @param token Token信息
     * @return
     */
    public static String getUserNameByToken (String token) {
        if (StringUtils.isNotEmpty(token)) {
            // 去除JWT前缀
            token = token.substring(JWTConfig.tokenPrefix.length());
            return RedisUtils.hget(token, "username").toString();
        }
        return null;
    }

    /**
     * 从Redis中获取IP
     *
     * @param token Token信息
     * @return
     */
    public static String getIpByToken (String token) {
        if (StringUtils.isNotEmpty(token)) {
            // 去除JWT前缀
            token = token.substring(JWTConfig.tokenPrefix.length());
            return RedisUtils.hget(token, "ip").toString();
        }
        return null;
    }
}
