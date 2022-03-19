package com.abc.cx.annotation;

import java.lang.annotation.*;

/**
 * 注解添加在需要实现接口幂等性的 Controller 方法中，即可实现接口的幂等性。
 * @Author: ChangXuan
 * @Decription: 幂等性校验
 * @Date: 10:39 2021/3/2
 **/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Idempotent {

}