package com.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mapper.UserMapper;
import com.pojo.Auth;
import com.pojo.Role;
import com.pojo.User;
import com.pojo.UserRole;
import com.service.UserRoleService;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 *
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    @Qualifier(value = "myRedisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    /**
     * 根据用户名称查询用户信息
     *
     * @param username 用户名称
     * @return
     */
    @Override
    public User findUserByUserName (String username) {
        //查询redis
        Object o = redisTemplate.opsForValue().get(username + "Info");
        if (o == null) {
            //查询mysql
            LambdaQueryWrapper<User> queryWrapper =
                    new QueryWrapper<User>().lambda().eq(User::getUsername, username);
            User user = this.baseMapper.selectOne(queryWrapper);

            //存储到redis
            redisTemplate.opsForValue().set(username + "Info", user);
            return user;
        }
        //把json变成对象
        return JSONObject.parseObject(JSONObject.toJSONString(o), User.class);
    }

    /**
     * 根据用户ID查询角色
     *
     * @param userId 用户ID
     * @return
     */
    @Override
    public List<Role> findRoleByUserId (Integer userId) {
        return this.baseMapper.findRoleByUserId(userId);
    }

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return
     */
    @Override
    public List<Auth> findAuthByUserId (Integer userId) {
        return this.baseMapper.findAuthByUserId(userId);
    }

    /**
     * 更新用户信息
     *
     * @param user 用户
     * @return boolean
     */
    @Override
    public boolean updateUserInfo (User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        //更新mysql数据库
        boolean flag = this.updateById(user);
        if (flag) {
            //删除redis里面缓存的usernameInfo
            redisTemplate.opsForValue().getOperations().delete(user.getUsername() + "Info");

            LambdaQueryWrapper<User> queryWrapper =
                    new QueryWrapper<User>().lambda().eq(User::getUsername, user.getUsername());
            User newUser = this.baseMapper.selectOne(queryWrapper);

            //存储到redis
            redisTemplate.opsForValue().set(newUser.getUsername() + "Info", newUser);
        }
        return flag;
    }

    /**
     * 更新用户的使用容量大小
     *
     * @param username 用户名
     * @param size     大小
     * @return boolean
     */
    @Override
    public boolean updateUserSize (String username, Integer size) {
        //更新mysql数据库
        boolean flag = this.update(
                        new UpdateWrapper<User>()
                                .lambda()
                                .set(User::getSize,size)
                                .eq(User::getUsername,username));

        if (flag) {
            //删除redis里面缓存的usernameInfo
            redisTemplate.opsForValue().getOperations().delete(username + "Info");

            LambdaQueryWrapper<User> queryWrapper =
                    new QueryWrapper<User>().lambda().eq(User::getUsername, username);
            User newUser = this.baseMapper.selectOne(queryWrapper);

            //存储到redis
            redisTemplate.opsForValue().set(newUser.getUsername() + "Info", newUser);
        }
        return flag;
    }

    /**
     * 更新用户签到情况
     *
     * @param user 用户
     * @return boolean
     */
    @Override
    public boolean updateUserSignedIn (User user) {
        LocalDate now = LocalDate.now();
        if (user.getSignDate() == null ||
                (user.getSignDate().plusDays(1).compareTo(now)) > 0) {
            return updateSign(user);
        }
        return false;
    }

    /**
     * 更新标志
     * 内部方法，供updateUserSignedIn使用
     * @param user 用户
     * @return boolean
     */
    private boolean updateSign(User user){
        if (user.getExperience()+10 == 100) {
            userRoleService.update(
                    new UpdateWrapper<UserRole>()
                            .lambda()
                            .set(UserRole::getRoleId,2)
                            .eq(UserRole::getUserId,user.getId()));
        }
        //更新mysql数据库
        boolean flag = this.update(
                new UpdateWrapper<User>()
                        .lambda()
                        .set(User::getExperience,user.getExperience()+10)
                        .set(User::getSignDay,user.getSignDay()+1)
                        .eq(User::getUsername,user.getUsername()));

        if (flag) {
            //删除redis里面缓存的usernameInfo
            redisTemplate.opsForValue().getOperations().delete(user.getUsername() + "Info");

            LambdaQueryWrapper<User> queryWrapper =
                    new QueryWrapper<User>().lambda().eq(User::getUsername, user.getUsername());
            User newUser = this.baseMapper.selectOne(queryWrapper);

            //存储到redis
            redisTemplate.opsForValue().set(newUser.getUsername() + "Info", newUser);
        }
        return flag;
    }
}




