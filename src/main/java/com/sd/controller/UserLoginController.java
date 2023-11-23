package com.sd.controller;

import com.sd.pojo.User;
import com.sd.service.impl.LoginServiceImpl;
import com.sd.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
@Controller
public class UserLoginController {


    @Autowired
    private LoginServiceImpl loginService;

    @PostMapping("/user/login")
    public Result login(@RequestBody User user) {
        String token = loginService.loginToToken(user);
        return new Result(200, "登录成功", token);
    }

    @PostMapping("/user/logout")
    public Result logout(HttpServletRequest request) {
        String token = request.getHeader("token");
        loginService.logout(token);
        return new Result(200, "退出成功");
    }


    @GetMapping("/tomain")
    @PreAuthorize("hasAnyAuthority('1test')")
    public Result toMain(){
        return new Result(200,"hello world");
    }
}
