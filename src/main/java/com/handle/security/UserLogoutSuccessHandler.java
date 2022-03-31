package com.handle.security;

import com.config.JWTConfig;
import com.pojo.SysUserDetails;
import com.util.JWTTokenUtils;
import com.util.RedisUtils;
import com.util.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ClassName: UserLogoutSuccessHandler 登出成功处理类
 * Description:
 * date: 2021/11/22 11:37
 *
 * @author WhiteBear
 */
@Component
public class UserLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                Authentication authentication) {
        // 删除token的所有信息
        String token = request.getHeader(JWTConfig.tokenHeader);
        JWTTokenUtils.deleteRedisToken(token);

        RedisUtils.delete(request.getParameter("username"));

        SecurityContextHolder.clearContext();
        ResponseUtils.responseJson(response,
                ResponseUtils.response(200, "登出成功", null));
    }
}