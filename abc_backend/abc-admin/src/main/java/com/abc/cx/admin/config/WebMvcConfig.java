package com.abc.cx.admin.config;

import com.abc.cx.upload.LocalUploadConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author: ChangXuan
 * @Decription: Web配置
 * @Date: 12:39 2021/11/7
 **/
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private LocalUploadConfig uploadConfig;

    @Autowired
    public void setUploadConfig(LocalUploadConfig uploadConfig) {
        this.uploadConfig = uploadConfig;
    }

    /**
     * 允许所有跨域访问
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedHeaders("*")
                .allowedMethods("*")
                .allowedOrigins("*");
    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(uploadConfig.getBaseUrl() + "**").addResourceLocations("file:" + uploadConfig.getBasePath());
    }

}
