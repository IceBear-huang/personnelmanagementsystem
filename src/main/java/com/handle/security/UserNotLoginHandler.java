package com.handle.security;

import com.util.ResponseUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * ClassName: UserNotLoginHandler 未登录处理类
 * Description:
 * date: 2021/11/22 11:31
 *
 * @author WhiteBear
 */
@Component
public class UserNotLoginHandler implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException, ServletException {

        ResponseUtils.responseJson(
                response,
                ResponseUtils.response(
                        401,
                        "未登录",
                        authException.getMessage()));
    }
}
