package com;

import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
@MapperScan(value = "com.mapper")
public class PersonnelmanagementsystemApplication {

    public static void main (String[] args) {
        SpringApplication.run(PersonnelmanagementsystemApplication.class, args);
    }

}
