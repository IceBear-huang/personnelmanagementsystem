package com.service;

import com.pojo.Auth;
import com.pojo.Role;
import com.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *
 */
public interface UserService extends IService<User> {
    /**
     * 根据用户名称查询用户信息
     *
     * @param username 用户名称
     * @return
     */
    User findUserByUserName(String username);

    /**
     * 根据用户ID查询角色
     *
     * @param userId 用户ID
     * @return
     */
    List<Role> findRoleByUserId(Integer userId);

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return
     */
    List<Auth> findAuthByUserId(Integer userId);

    /**
     * 更新信息
     *
     * @param user 用户
     * @return boolean
     */
    boolean updateUserInfo(User user);

    boolean updateUserSize(String username, Integer size);
    boolean updateUserSignedIn(User user);
}
