package com.abc.cx.config;

import cn.hutool.crypto.asymmetric.RSA;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @Author: ChangXuan
 * @Decription:
 * @Date: 22:07 2021/11/6
 **/
@Component
@Data
public class Configure {

    @Value("${pool.core-size}")
    private int corePoolSize;

    @Value("${pool.max-size}")
    private int maxPoolSize;

    @Value("${pool.queue-capacity}")
    private int queueCapacity;

    @Value("${spring.session.timeout}")
    private int sessionTimeout;

    @Value("${spring.session.redis.namespace}")
    private String nameSpace;

    @Value("${project.frontend-address}")
    private String frontendAddress;

    @Value("${project.rsa.private-key}")
    private String rsaPrivateKey;

    @Value("${project.pwd-timeout}")
    private int pwdTimeout;

    @Bean
    public RSA getRsa() {
        return new RSA(rsaPrivateKey, null);
    }

}
