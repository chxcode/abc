package com.abc.cx.config.security;

import com.abc.cx.config.RedisConfig;
import org.apache.catalina.security.SecurityConfig;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 * @Author: ChangXuan
 * @Decription: 安全模块session使用配置的redis session
 * @Date: 12:29 2021/11/7
 **/
public class SecurityInitializer extends AbstractSecurityWebApplicationInitializer {

    public SecurityInitializer() {
        super(SecurityConfig.class, RedisConfig.class);
    }

}
