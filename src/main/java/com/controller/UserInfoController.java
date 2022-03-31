package com.controller;

import com.pojo.User;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


/**
 * ClassName: UserInfoController
 * Description:
 * date: 2021/11/28 15:24
 *
 * @author WhiteBear
 */
@RestController
@RequestMapping("/user")
public class UserInfoController {

    @Autowired
    private UserService userService;

    @GetMapping("/queryInfo/{username}")
    public User queryInfo(@PathVariable String username) {
        return userService.findUserByUserName(username);
    }

    @PreAuthorize(value = "hasAnyRole('vip','svip') " +
            "and hasPermission('/user/update','setHeadAddress')")
    @PutMapping("/updateInfo")
    public boolean updateInfo(@RequestBody User user) {
        return userService.updateUserInfo(user);
    }

    @PreAuthorize(value = "hasRole('svip') " +
            "and hasPermission('/user/growSize','setSize')")
    @PutMapping("/growSize/{username}/{size}")
    public boolean growSize(@PathVariable String username, @PathVariable Integer size) {
        return userService.updateUserSize(username,size);
    }

    @PutMapping("/signIn")
    public boolean signIn(@RequestBody User user) {
        return userService.updateUserSignedIn(user);
    }
}
