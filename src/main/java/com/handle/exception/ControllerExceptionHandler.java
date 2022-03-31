package com.handle.exception;

import com.util.ResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 47550
 */
@ControllerAdvice
public class ControllerExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(value = AccessDeniedException.class)
    @ResponseBody
    public ResponseUtils exceptionHandler(HttpServletRequest request, AccessDeniedException exception){
        logger.error("不允许访问！原因是:",request.getRequestURI(), exception.getMessage(),exception);
        return ResponseUtils.fail("权限不够，不允许访问！");
    }

    @ExceptionHandler(value = Exception.class)
    public String exceptionHandler (HttpServletRequest request, Exception exception) {
        logger.error("Request URL : {},Exception : {}", request.getRequestURI(), exception.getMessage(),exception);
        return "error";
    }
}
