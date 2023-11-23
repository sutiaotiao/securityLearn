package com.sd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sd.mapper.UserMapper;
import com.sd.pojo.UserDetailsEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.sd.pojo.User;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Objects;

/**
 * @program:
 * @description: 用户登录逻辑实现类
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private UserMapper userMapper;
    /**
     * 根据用户名创建框架自己的用户对象,用于登录认证
     * @param username
     * @return 框架自己的用户对象UserDetails
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        User user = userMapper.selectOne(wrapper.eq(User::getUserName, username));
        //判断数据库中是否存在该用户
        if(Objects.isNull(user)){
            throw new UsernameNotFoundException("用户不存在");
        }
        //为了方便测试，我们自己创建权限列表
        ArrayList<String> permissions = new ArrayList<>();
        permissions.add("test");
        permissions.add("admin");
        //返回我们自己实现的UserDetails用户对象
        return new UserDetailsEntity(user,permissions);
    }


}
