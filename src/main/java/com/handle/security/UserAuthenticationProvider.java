package com.handle.security;

import com.pojo.SysUserDetails;
import com.service.SysUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * ClassName: UserAuthenticationProvider 用户登录验证处理类
 * Description:
 * date: 2021/11/22 15:52
 *
 * @author WhiteBear
 */
@Component
public class UserAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private SysUserDetailsService userDetailsService;

    /**
     * 身份验证
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 获取用户名
        String username = authentication.getName();
        // 获取密码
        String password = authentication.getCredentials().toString();

        SysUserDetails sysUserDetails =
                (SysUserDetails) userDetailsService.loadUserByUsername(username);
        if (sysUserDetails == null) {
            throw new UsernameNotFoundException("用户名不存在");
        }

        if (!new BCryptPasswordEncoder().matches(password, sysUserDetails.getPassword())) {
            throw new BadCredentialsException("用户名或密码错误");
        }

        return new UsernamePasswordAuthenticationToken
                (sysUserDetails, password, sysUserDetails.getAuthorities());
    }

    /**
     * 支持指定的身份验证
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }

}