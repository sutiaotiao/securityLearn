package com.sd;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;

@SpringBootTest
class SecurityLearnApplicationTests {

    @Resource
    private BCryptPasswordEncoder passwordEncoder;
    @Test
    void contextLoads() {
    }

    @Test
    void checkPassWord() {
        System.out.println(
                passwordEncoder.matches("123", "$2a$10$yHONhVcyz22A6O7rCxe0iOODnZ3Y.rjMOYUruNyca/9W0cJFNgvNe")
        );
    }

    @Test
    void encodePassWord() {
        System.out.println(
                passwordEncoder.encode("123")
        );
    }

}
