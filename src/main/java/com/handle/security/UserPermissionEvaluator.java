package com.handle.security;

import com.pojo.Auth;
import com.pojo.SysUserDetails;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * ClassName: UserPermissionEvaluator
 * Description:用户权限注解处理类
 * date: 2021/11/22 17:24
 *
 * @author WhiteBear
 */
@Configuration
public class UserPermissionEvaluator implements PermissionEvaluator {

    @Autowired
    private UserService userService;

    /**
     * 判断是否拥有权限
     *
     * @param authentication 用户身份
     * @param targetUrl      目标路径
     * @param permission     路径权限
     * @return 是否拥有权限
     */
    @Override
    public boolean hasPermission (Authentication authentication,
                                  Object targetUrl, Object permission) {

        SysUserDetails sysUserDetails = (SysUserDetails) authentication.getPrincipal();

        // 用户权限
        Set<String> permissions = new HashSet<>();

        List<Auth> authList = userService.findAuthByUserId(sysUserDetails.getId());
        authList.forEach(auth -> {
            permissions.add(auth.getAuthName());
        });

        // 判断是否拥有权限
        if (permissions.contains(permission.toString())) {
            return true;
        }
        return false;
    }

    @Override
    public boolean hasPermission (Authentication authentication,
                                  Serializable targetId, String targetType,
                                  Object permission) {
        return false;
    }

}