package com.handle.security;

import com.util.ResponseUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ClassName: UserLoginFailureHandler 登录失败处理类
 * Description:
 * date: 2021/11/22 11:35
 *
 * @author WhiteBear
 */
@Component
public class UserLoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) {

        ResponseUtils.responseJson(
                response,
                ResponseUtils.response(
                        500,
                        "登录失败",
                        exception.getMessage()));
    }
}