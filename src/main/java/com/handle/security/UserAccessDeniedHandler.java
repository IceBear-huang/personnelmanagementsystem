package com.handle.security;

import com.util.ResponseUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * ClassName: UserAccessDeniedHandler 无权限处理类
 * Description:
 * date: 2021/11/22 11:28
 *
 * @author WhiteBear
 */
@Component
public class UserAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException)
            throws IOException, ServletException {

        ResponseUtils.responseJson(
                response,
                ResponseUtils.response(
                        403,
                        "拒绝访问",
                        accessDeniedException.getMessage()));
    }

}