package com.abc.cx.module.captcha;

import lombok.Getter;

/**
 * @Author: ChangXuan
 * @Decription: 验证码类型
 * @Date: 10:44 2021/3/9
 **/
@Getter
public enum CaptchaEnum{

    /**
     * 算数
     */
    ARITHMETIC(9, "算术"),
    /**
     * 中文
     */
    CHINESE(1, "中文"),
    /**
     * 中文动图
     */
    CHINESE_GIF(2, "中文动图"),
    /**
     * 动图
     */
    GIF(3, "动图"),
    /**
     * 特殊
     */
    SPEC(4, "特殊");

    private final Integer code;

    private final String message;

    CaptchaEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
