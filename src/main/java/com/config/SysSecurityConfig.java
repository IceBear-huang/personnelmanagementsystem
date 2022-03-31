package com.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.filter.JWTAuthenticationFilter;
import com.filter.LoginFilter;
import com.handle.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * ClassName: myWebSceurityConfig
 * Description:
 * date: 2021/11/20 16:19
 *
 * @author WhiteBear
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SysSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 无权限处理类
     */
    @Autowired
    private UserAccessDeniedHandler userAccessDeniedHandler;

    /**
     * 用户未登录处理类
     */
    @Autowired
    private UserNotLoginHandler userNotLoginHandler;

    /**
     * 用户登录成功处理类
     */
    @Autowired
    private UserLoginSuccessHandler userLoginSuccessHandler;

    /**
     * 用户登录失败处理类
     */
    @Autowired
    private UserLoginFailureHandler userLoginFailureHandler;

    /**
     * 用户登出成功处理类
     */
    @Autowired
    private UserLogoutSuccessHandler userLogoutSuccessHandler;

    /**
     * 用户登录验证
     */
    @Autowired
    private UserAuthenticationProvider userAuthenticationProvider;

    /**
     * 加密方式
     *
     * @return
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder () {
        return new BCryptPasswordEncoder();
    }


    /**
     * 用户登录验证
     */
    @Override
    protected void configure (AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(userAuthenticationProvider);
    }

    /**
     * 安全权限配置
     */
    @Override
    protected void configure (HttpSecurity http) throws Exception {
        // 权限配置
        http.authorizeRequests()
                // 获取白名单（不进行权限验证）
                .antMatchers(JWTConfig.antMatchers.split(",")).permitAll()
                // 其他的需要登陆后才能访问
                .anyRequest().authenticated()
                // 配置未登录处理类
                .and().httpBasic().authenticationEntryPoint(userNotLoginHandler)
                // 配置登录URL
                .and().formLogin().loginProcessingUrl("/login")
                // 配置登出地址
                .and().logout().logoutUrl("/logout")
                // 配置用户登出处理类
                .logoutSuccessHandler(userLogoutSuccessHandler)
                // 配置没有权限处理类
                .and().exceptionHandling().accessDeniedHandler(userAccessDeniedHandler)
                // 开启跨域
                .and().cors()
                // 禁用跨站请求伪造防护
                .and().csrf().disable();

        // 禁用session（使用Token认证）
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // 禁用缓存
        http.headers().cacheControl();
        // 添加过滤器
        http.addFilter(new JWTAuthenticationFilter(authenticationManager()))
        .addFilterAt(LoginFilter(),UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    LoginFilter LoginFilter() throws Exception {
        LoginFilter filter = new LoginFilter();
        filter.setAuthenticationSuccessHandler(userLoginSuccessHandler);
        filter.setAuthenticationFailureHandler(userLoginFailureHandler);
        filter.setAuthenticationManager(authenticationManagerBean());
        return filter;
    }
}