package com.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;

/**
 * ClassName: SysUserDetails
 * Description:
 * date: 2021/11/22 10:30
 *
 * @author WhiteBear
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysUserDetails extends User implements UserDetails, Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户角色
     */
    private Collection<GrantedAuthority> authorities;

    /**
     * 账号是否过期
     */
    private boolean isAccountNonExpired = false;

    /**
     * 账号是否锁定
     */
    private boolean isAccountNonLocked = false;

    /**
     * 证书是否过期
     */
    private boolean isCredentialsNonExpired = false;

    /**
     * 账号是否有效
     */
    private boolean isEnabled = true;

    private String ip;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities () {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired () {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked () {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired () {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled () {
        return isEnabled;
    }
}
