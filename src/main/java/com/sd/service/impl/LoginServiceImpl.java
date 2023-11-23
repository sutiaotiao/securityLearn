package com.sd.service.impl;
import com.sd.pojo.User;
import com.sd.pojo.UserDetailsEntity;
import com.sd.service.LoginService;
import com.sd.untils.RedisUtil;
import com.sd.untils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private AuthenticationManager authenticationManager;


    @Override
    public String loginToToken(User user) {
        //创建认证对象
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        //认证用户信息 会调用UserDetailsServiceImpl.loadUserByUsername
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("用户名或密码错误");
        }
        //从认证对象中获取用户信息
        UserDetailsEntity principal = (UserDetailsEntity)authenticate.getPrincipal();
        //认证成功后生成JTW
        String token = TokenUtil.loginSign(principal.getUser(), principal.getPassword());
        //存到redis中
        redisUtil.setCacheObject(token, principal);
        return token;
    }

    @Override
    public Integer logout(String token) {
        //当前线程肯定是有authentication对象的因为能到这里说明已经通过认证了
        //这里之所以要拿到id是因为我们需要跟token存的id对比是不是同一个人在操作。
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsEntity userDetails = (UserDetailsEntity)authentication.getPrincipal();
        int id = userDetails.getUser().getId();

        try {
            //获取传过来的token中的id
            int tokenId = TokenUtil.getCurrentUser(token).getId();
            if(id == tokenId){
                //删除redis中的token
                redisUtil.deleteObject(token);
                //退出成功
                return 1;
            }
        } catch (Exception e) {
            throw new RuntimeException("token不合法");
        }
        return 0;
    }
}
