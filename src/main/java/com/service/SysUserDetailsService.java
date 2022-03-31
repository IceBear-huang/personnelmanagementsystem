package com.service;

import com.pojo.Role;
import com.pojo.SysUserDetails;
import com.pojo.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * ClassName: MyUserDetailsService
 * Description:
 * date: 2021/11/20 20:37
 *
 * @author WhiteBear
 */
@Service
public class SysUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername (String username)
            throws UsernameNotFoundException {
        User user = userService.findUserByUserName(username);

        if (user != null) {
            SysUserDetails sysUserDetails = new SysUserDetails();
            BeanUtils.copyProperties(user, sysUserDetails);

            // 角色集合
            Set<GrantedAuthority> authorities = new HashSet<>();

            List<Role> roleList = userService.findRoleByUserId(sysUserDetails.getId());
            roleList.forEach(role -> {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
            });

            sysUserDetails.setAuthorities(authorities);

            return sysUserDetails;
        }
        return null;
    }

}
