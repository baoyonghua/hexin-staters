package com.myhexin.b2c.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author baoyh
 * @since 2023/10/14
 */
@SpringBootApplication
@MapperScan(basePackages = "com.myhexin.b2c.web.mapper")
@ComponentScan(basePackages = "com.myhexin.b2c.web")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

