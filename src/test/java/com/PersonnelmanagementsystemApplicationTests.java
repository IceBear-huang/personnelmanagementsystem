package com;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pojo.User;
import com.service.UserService;
import com.util.JWTTokenUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;

@SpringBootTest
class PersonnelmanagementsystemApplicationTests {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Test
    void contextLoads () {
        System.out.println(encoder.encode("123"));
    }

}
