package com.abc.cx.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @Author: ChangXuan
 * @Decription: 限流注解
 * @Date: 22:05 2021/11/6
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimiter {
    long DEFAULT_REQUEST = 8;

    /**
     * 最大请求数
     */
    @AliasFor("max") long value() default DEFAULT_REQUEST;

    /**
     * 最大请求数
     */
    @AliasFor("value") long max() default DEFAULT_REQUEST;

    /**
     * 限流 key
     */
    String key() default "";

    /**
     * 超时时长
     */
    long timeout() default 1;

    /**
     * 时间单位
     */
    TimeUnit timeUnit() default TimeUnit.MINUTES;
}