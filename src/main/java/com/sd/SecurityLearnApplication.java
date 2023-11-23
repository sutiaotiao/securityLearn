package com.sd;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.sd.mapper")
@SpringBootApplication
public class SecurityLearnApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityLearnApplication.class, args);
    }

}
