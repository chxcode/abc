package com.abc.cx.module.captcha;

import java.lang.annotation.*;

/**
 * @Author: ChangXuan
 * @Decription: 核验验证码注解
 * 使用此注解，必须保证前端的参数名称为 captcha 与 captchaId
 * @Date: 15:06 2021/3/9
 **/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface VerifyCaptcha {

}
