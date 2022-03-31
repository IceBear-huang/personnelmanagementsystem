package com.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pojo.User;
import com.pojo.UserRole;
import com.service.UserRoleService;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * ClassName: RegisterController
 * Description:
 * date: 2021/11/27 19:51
 *
 * @author WhiteBear
 */
@RestController
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private BCryptPasswordEncoder encoder;


    @PostMapping("/user")
    public String register(@RequestBody User user) {
        System.out.println(user);
        user.setPassword(encoder.encode(user.getPassword()));
        user.setExperience(0);
        user.setSignDay(0);
        user.setSize(50);

        userService.save(user);

        User newUser = userService.getOne(new QueryWrapper<User>()
                .eq("username", user.getUsername()));

        UserRole userRole = new UserRole();
        userRole.setRoleId(1);
        userRole.setUserId(newUser.getId());
        userRoleService.save(userRole);

        return "true";
    }

    @GetMapping("/isRepeatName/{username}")
    public boolean isRepeatName(@PathVariable String username)  {

        return userService.getOne(new QueryWrapper<User>()
                .eq("username", username)) == null;
    }
}
