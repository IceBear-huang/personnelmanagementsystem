package com.handle.security;

import com.pojo.SysUserDetails;
import com.util.AccessAddressUtils;
import com.util.JWTTokenUtils;
import com.util.RedisUtils;
import com.util.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: UserLoginSuccessHandler 登录成功处理类
 * Description:
 * date: 2021/11/22 11:32
 *
 * @author WhiteBear
 */
@Component
@Slf4j
public class UserLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) {
        SysUserDetails sysUserDetails = (SysUserDetails) authentication.getPrincipal();
        String oldToken = JWTTokenUtils.queryToken(sysUserDetails.getUsername());
        String ip = AccessAddressUtils.getIpAddress(request);
        if (oldToken != null ) {
            String oldIp = (String) RedisUtils.hget(oldToken, "ip");
            String expiration = (String) RedisUtils.hget(oldToken, "expiration");

            if (ip.equals(oldIp) && !JWTTokenUtils.isExpiration(expiration)){
                log.info("用户{}没有登退，使用redis还没有过期的Token",
                        sysUserDetails.getUsername());

                Map<String, String> tokenMap = new HashMap<>();
                tokenMap.put("token", "Bearer "+oldToken);
                tokenMap.put("userId",sysUserDetails.getId().toString());
                ResponseUtils.responseJson(response,
                        ResponseUtils.response(
                                200, "登录成功", tokenMap));
            }else {
                //删除没用的redis缓存
                JWTTokenUtils.deleteRedisToken("Bearer "+oldToken);
                RedisUtils.delete(sysUserDetails.getUsername());

                sysUserDetails.setIp(ip);
                String token = JWTTokenUtils.createAccessToken(sysUserDetails);

                // 保存Token信息到Redis中
                JWTTokenUtils.setTokenInfo(token, sysUserDetails.getUsername(), ip);

                log.info("用户{}登录成功，Token信息已保存到Redis", sysUserDetails.getUsername());

                Map<String, String> tokenMap = new HashMap<>();
                tokenMap.put("token", token);
                tokenMap.put("userId",sysUserDetails.getId().toString());
                ResponseUtils.responseJson(response,
                        ResponseUtils.response(
                                200, "登录成功", tokenMap));
            }
        }else {
            sysUserDetails.setIp(ip);
            String token = JWTTokenUtils.createAccessToken(sysUserDetails);

            // 保存Token信息到Redis中
            JWTTokenUtils.setTokenInfo(token, sysUserDetails.getUsername(), ip);

            log.info("用户{}登录成功，Token信息已保存到Redis", sysUserDetails.getUsername());

            Map<String, String> tokenMap = new HashMap<>();
            tokenMap.put("token", token);
            tokenMap.put("userId",sysUserDetails.getId().toString());
            ResponseUtils.responseJson(response,
                    ResponseUtils.response(
                            200, "登录成功", tokenMap));
        }
    }
}