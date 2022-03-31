package com.filter;

import com.config.JWTConfig;
import com.pojo.SysUserDetails;
import com.util.AccessAddressUtils;
import com.util.JWTTokenUtils;
import com.util.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * ClassName: JWTAuthenticationFilter
 * Description:JWT权限过滤器，用于验证Token是否合法
 * date: 2021/11/22 17:27
 *
 * @author WhiteBear
 */
@Slf4j
public class JWTAuthenticationFilter extends BasicAuthenticationFilter {

    public JWTAuthenticationFilter (AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws IOException, ServletException {
        // 取出Token
        String token = request.getHeader(JWTConfig.tokenHeader);

        if (token != null && token.startsWith(JWTConfig.tokenPrefix)) {
            // 是否存在于Redis中
            if (JWTTokenUtils.hasToken(token)) {
                String ip = AccessAddressUtils.getIpAddress(request);
                String expiration = JWTTokenUtils.getExpirationByToken(token);
                String username = JWTTokenUtils.getUserNameByToken(token);

                // 判断是否过期
                if (JWTTokenUtils.isExpiration(expiration)) {

                    JWTTokenUtils.deleteRedisToken(token);
                    ResponseUtils.responseJson(response,
                            ResponseUtils.response(
                                    505,
                                    "Token已过期.请重新登陆",
                                    null));
                    return;
                }

                SysUserDetails sysUserDetails = JWTTokenUtils.parseAccessToken(token);

                if (sysUserDetails != null) {
                    // 校验IP
                    if (!StringUtils.equals(ip, JWTTokenUtils.getIpByToken(token))) {

                        log.info("用户{}请求IP与Token中IP信息不一致", username);

                        JWTTokenUtils.deleteRedisToken(token);
                        ResponseUtils.responseJson(response,
                                ResponseUtils.response(
                                        505,
                                        "Token已删除，可能存在IP伪造风险",
                                        "请重新登陆"));

                        return;
                    }

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    sysUserDetails,
                                    sysUserDetails.getId(),
                                    sysUserDetails.getAuthorities());

                    SecurityContextHolder.getContext()
                            .setAuthentication(authentication);
                }
            }else{
                ResponseUtils.responseJson(response,
                        ResponseUtils.response(
                                505,
                                "Token已过期.请重新登陆",
                                null));
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

}

