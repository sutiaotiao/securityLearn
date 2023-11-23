package com.sd.config;

import com.sd.filter.JwtAuthenticationTokenFilter;
import com.sd.handler.AuthenticationEntryPointImpl;
import com.sd.handler.FulAccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//开启全局认证
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Configuration
public class securityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private AuthenticationEntryPointImpl authenticationEntryPoint;

    @Autowired
    private FulAccessDeniedHandler accessDeniedHandler;

    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //认证管理器
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 重写configure方法,配置用户信息服务
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //自定义登录页面
       /* http.formLogin()
                //.loginPage("/login.html")
                //登录请求的url
                //.loginProcessingUrl("/user/login")
                //登录成功后跳转的页面
                .defaultSuccessUrl("/toMain.html")
                //登录失败后跳转的页面
                .failureUrl("/err.html");*/

        //添加token过滤器放在 security认证前,UsernamePasswordAuthenticationFilter是入口点
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        //authorizeRequests()方法表示开始说明需要的权限
        http.authorizeRequests()
                //放行录页面
                .antMatchers("/login.html","/err.html").permitAll()
                //放行登录请求
                .antMatchers("/user/login","/err").permitAll()
                //anyRequest()表示任意的请求
                .anyRequest()
                //authenticated()表示认证后才能访问
                .authenticated();

        //关闭csrf防护,不通过Session获取SecurityContext
        http.csrf().disable()
               .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint) // 认证失败异常处理器
                .accessDeniedHandler(accessDeniedHandler); // 授权失败异常处理器

    }


}
