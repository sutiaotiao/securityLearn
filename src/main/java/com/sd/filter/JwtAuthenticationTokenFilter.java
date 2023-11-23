package com.sd.filter;

import com.sd.pojo.UserDetailsEntity;
import com.sd.untils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Objects;

/**
 * 验证token过滤器
 * OncePerRequestFilter: 保证一次请求只通过一次filter，而不需要重复执行
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 获取请求头中的token
        String token = request.getHeader("token");
        //没有token，直接放行交给spring security处理
        if(StringUtils.isEmpty(token)){
            filterChain.doFilter(request,response);
            return;
        }

        //有token，去查询redis中是否存在
        UserDetailsEntity userDetails = redisUtil.getCacheObject(token);
        if(!Objects.isNull(userDetails)){
            //从userDetails中获取权限列表
            Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
            //如果存在，把用户信息存入spring security上下文中
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            filterChain.doFilter(request,response);
        }else {
            //throw new RuntimeException("请重新登录");
            filterChain.doFilter(request,response);
        }


    }
}
