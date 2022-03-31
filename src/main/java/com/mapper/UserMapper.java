package com.mapper;

import com.pojo.Auth;
import com.pojo.Role;
import com.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @Entity com.pojo.User
 */
public interface UserMapper extends BaseMapper<User> {

    List<Role> findRoleByUserId (Integer userId);

    List<Auth> findAuthByUserId (Integer userId);
}




