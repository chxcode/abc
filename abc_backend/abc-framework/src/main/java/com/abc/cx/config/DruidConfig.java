package com.abc.cx.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * @Author: ChangXuan
 * @Decription: Druid数据源配置
 * @Date: 10:25 2020/5/8
 **/
@Configuration
public class DruidConfig {

    @Primary
    @Qualifier("primaryDataSource")
    @Bean(name = "primaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.druid.primary")
    public DataSource primaryDataSource() {
        return new DruidDataSource();
    }

}

