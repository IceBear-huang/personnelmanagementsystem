package com.handle.interceptor;

import com.config.JWTConfig;
import com.pojo.SysUserDetails;
import com.util.AccessAddressUtils;
import com.util.JWTTokenUtils;
import com.util.RedisUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ClassName: TokenFlushInterceptor
 * Description:
 * date: 2021/11/25 17:42
 *
 * @author WhiteBear
 */
public class TokenFlushInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle (HttpServletRequest request,
                              HttpServletResponse response,
                              Object handler) throws Exception {
        // 刷新token，暂时不使用了
       /*
        String token = request.getHeader(JWTConfig.tokenHeader);

        if (!StringUtils.isEmpty(token)) {
            //刷新token
            String ip = AccessAddressUtils.getIpAddress(request);
            String expiration = JWTTokenUtils.getExpirationByToken(token);
            String username = JWTTokenUtils.getUserNameByToken(token);
            String newToke = JWTTokenUtils.refreshAccessToken(token);
            String tokenIp = JWTTokenUtils.getIpByToken(token);
            SysUserDetails sysUserDetails = JWTTokenUtils.parseAccessToken(token);

            if ((!JWTTokenUtils.isExpiration(expiration)) && ip.equals(tokenIp)) {
                //删除之前的token，添加新的token
                JWTTokenUtils.deleteRedisToken(token);
                RedisUtils.delete(username);
                JWTTokenUtils.setTokenInfo(newToke, username, ip);

                //
                response.setHeader(JWTConfig.tokenHeader, newToke);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                sysUserDetails,
                                sysUserDetails.getId(),
                                sysUserDetails.getAuthorities());

                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);
            }
        }*/

        return true;
    }


}
