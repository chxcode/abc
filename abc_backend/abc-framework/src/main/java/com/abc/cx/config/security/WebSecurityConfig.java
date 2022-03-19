package com.abc.cx.config.security;

import com.abc.cx.config.CustomConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.session.HttpSessionEventPublisher;

/**
 * @Author: ChangXuan
 * @Decription: Security 配置
 * @Date: 12:29 2021/11/7
 **/
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableConfigurationProperties(CustomConfig.class)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    private CustomConfig customConfig;

    private AuthenticationSuccessHandler authenticationSuccessHandler;
    private AuthenticationFailureHandler authenticationFailureHandler;
    private ExitSuccessHandler exitSuccessHandler;
    private AuthenticationEntryPointHandler authenticationEntryPointHandler;
    private AuthenticationAccessDeniedHandler authenticationAccessDeniedHandler;
    private CustomAuthenticationProvider customAuthenticationProvider;
    private CustomAuthenticationDetailsSource customAuthenticationDetailsSource;

    @Autowired
    public void setCustomConfig(CustomConfig customConfig) {
        this.customConfig = customConfig;
    }

    @Autowired
    public void setCustomAuthenticationDetailsSource(CustomAuthenticationDetailsSource customAuthenticationDetailsSource) {
        this.customAuthenticationDetailsSource = customAuthenticationDetailsSource;
    }

    @Autowired
    public void setCustomAuthenticationProvider(CustomAuthenticationProvider customAuthenticationProvider) {
        this.customAuthenticationProvider = customAuthenticationProvider;
    }


    @Autowired
    public void setAuthenticationSuccessHandler(AuthenticationSuccessHandler authenticationSuccessHandler) {
        this.authenticationSuccessHandler = authenticationSuccessHandler;
    }

    @Autowired
    public void setAuthenticationFailureHandler(AuthenticationFailureHandler authenticationFailureHandler) {
        this.authenticationFailureHandler = authenticationFailureHandler;
    }

    @Autowired
    public void setExitSuccessHandler(ExitSuccessHandler exitSuccessHandler) {
        this.exitSuccessHandler = exitSuccessHandler;
    }

    @Autowired
    public void setAuthenticationEntryPointHandler(AuthenticationEntryPointHandler authenticationEntryPointHandler) {
        this.authenticationEntryPointHandler = authenticationEntryPointHandler;
    }

    @Autowired
    public void setAuthenticationAccessDeniedHandler(AuthenticationAccessDeniedHandler authenticationAccessDeniedHandler) {
        this.authenticationAccessDeniedHandler = authenticationAccessDeniedHandler;
    }


    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        ignoreConfigure(httpSecurity);
        httpSecurity
                .headers()
                .frameOptions()
                .disable()
                .and()
                .csrf()
                .disable()
                .authorizeRequests()
                //权限控制
                .antMatchers("/api/**")
                .authenticated()
                .antMatchers("/iapi/**")
                .access("@rbacAuthorityService.hasPermission(request,authentication)")
                .and()
                .exceptionHandling()
                //访问敏感资源无法验证身份时的处理
                .authenticationEntryPoint(authenticationEntryPointHandler)
                //已鉴权但权限不足
                .accessDeniedHandler(authenticationAccessDeniedHandler)
                .and()
                .formLogin()
                //登录地址
                .loginProcessingUrl("/login")
                //登录成功的处理
                .successHandler(authenticationSuccessHandler)
                //登录失败的处理
                .failureHandler(authenticationFailureHandler)
                .permitAll()
                .authenticationDetailsSource(customAuthenticationDetailsSource)
                .and()
                .rememberMe()
                //保存时长
                .tokenValiditySeconds(60 * 60 * 24)
                .and()
                .logout()
                //登出成功的处理
                .logoutSuccessHandler(exitSuccessHandler)
                .permitAll()
                .and()
                .sessionManagement()
                //.invalidSessionStrategy(authenticationExpressionHandler)
                .maximumSessions(1)
                .expiredUrl("/login")
                .sessionRegistry(sessionRegistry());

    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    // 设置自定义认证处理器
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customAuthenticationProvider);
        super.configure(auth);
    }


    /**
     * 免认证、免鉴权接口
     * @param httpSecurity
     */
    public void ignoreConfigure(HttpSecurity httpSecurity) {

        // 忽略 GET
        customConfig.getIgnores().getGet().forEach(url -> {
            try {
                httpSecurity.authorizeRequests().antMatchers(HttpMethod.GET, url).permitAll();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // 忽略 POST
        customConfig.getIgnores().getPost().forEach(url -> {
            try {
                httpSecurity.authorizeRequests().antMatchers(HttpMethod.POST, url).permitAll();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // 忽略 DELETE
        customConfig.getIgnores().getDelete().forEach(url -> {
            try {
                httpSecurity.authorizeRequests().antMatchers(HttpMethod.DELETE, url).permitAll();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // 忽略 PUT
        customConfig.getIgnores().getPut().forEach(url -> {
            try {
                httpSecurity.authorizeRequests().antMatchers(HttpMethod.PUT, url).permitAll();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // 忽略 HEAD
        customConfig.getIgnores().getHead().forEach(url -> {
            try {
                httpSecurity.authorizeRequests().antMatchers(HttpMethod.HEAD, url).permitAll();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // 忽略 PATCH
        customConfig.getIgnores().getPatch().forEach(url -> {
            try {
                httpSecurity.authorizeRequests().antMatchers(HttpMethod.PATCH, url).permitAll();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // 忽略 OPTIONS
        customConfig.getIgnores().getOptions().forEach(url -> {
            try {
                httpSecurity.authorizeRequests().antMatchers(HttpMethod.OPTIONS, url).permitAll();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // 忽略 TRACE
        customConfig.getIgnores().getTrace().forEach(url -> {
            try {
                httpSecurity.authorizeRequests().antMatchers(HttpMethod.TRACE, url).permitAll();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // 按照请求格式忽略
        customConfig.getIgnores().getPattern().forEach(url -> {
            try {
                httpSecurity.authorizeRequests().antMatchers(url).permitAll();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    /**
     * 注册bean sessionRegistry
     */
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

}
