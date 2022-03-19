package com.abc.cx.enums;

import lombok.Getter;

/**
 * @Author: ChangXuan
 * @Decription: 性别
 * @Date: 22:36 2021/11/6
 **/
@Getter
public enum GenderTypeEnum implements CodeEnum {

    /**
     * 女
     */
    WOMAN(0, "女"),

    /**
     * 男
     */
    MAN(1, "男"),

    /**
     * 保密
     */
    PRIVACY(2, "保密");

    private final Integer code;

    private final String message;

    GenderTypeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
