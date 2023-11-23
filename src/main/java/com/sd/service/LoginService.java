package com.sd.service;

import com.sd.pojo.User;
import com.sd.vo.Result;

import java.util.Map;

public interface LoginService {
    String loginToToken(User user);

    Integer logout(String token);
}
