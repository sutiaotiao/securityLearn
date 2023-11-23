package com.sd.pojo;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
//实现UserDetails接口，用于SpringSecurity登录认证
public class UserDetailsEntity implements UserDetails {

    //自己的用户对象
    private User user;
    //权限列表
    private List<String> permissions;

    //构造方法
    public UserDetailsEntity(User user, List<String> permissions) {
        this.user = user;
        this.permissions = permissions;
    }



    //存redis的时候不序列化
    @JSONField(serialize = false)
    private List<GrantedAuthority> authorities;
    //权限列表
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
     /*   //把permissions中String类型的权限转换成SimpleGrantedAuthority对象
        ArrayList<GrantedAuthority> authorities = new ArrayList<>();
        for (String permission : permissions) {
            //把String类型的权限转换成GrantedAuthority对象
            GrantedAuthority authority = new SimpleGrantedAuthority(permission);
            //把权限对象添加到集合中
            authorities.add(authority);
        }
        */
        if(authorities != null){
            return authorities;
        }
        authorities = permissions.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserName();
    }

    //以下方法用于判断账户是否过期、是否锁定、是否禁用、密码是否过期
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
