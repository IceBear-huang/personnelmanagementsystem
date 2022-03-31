package com.config;

import com.handle.interceptor.TokenFlushInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * ClassName: WebMvcConfigurer
 * Description:
 * date: 2021/11/25 17:44
 *
 * @author WhiteBear
 */
@Configuration
public class MyWebMvcConfigurer implements WebMvcConfigurer {

    @Bean
    public TokenFlushInterceptor getInterceptor(){
        return new TokenFlushInterceptor();
    }

    @Override
    public void addInterceptors (InterceptorRegistry registry) {
        registry.addInterceptor(getInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(JWTConfig.antMatchers);
    }
}
