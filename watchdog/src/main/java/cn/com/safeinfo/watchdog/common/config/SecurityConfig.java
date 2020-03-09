package cn.com.safeinfo.watchdog.common.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * spring-security配置
 *
 * @author: ly
 * @date: 2020/3/8 13:38
 */
//@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic()//开启httpbasic认证
                .and()
                .authorizeRequests()
                .anyRequest()
                .authenticated();//所有请求都需要登录认证才能访问
    }
}

