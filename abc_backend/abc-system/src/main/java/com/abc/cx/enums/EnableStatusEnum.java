package com.abc.cx.enums;

import lombok.Getter;

/**
 * @Author: ChangXuan
 * @Decription: 状态
 * @Date: 22:36 2021/11/6
 **/
@Getter
public enum EnableStatusEnum implements CodeEnum {
    /**
     * 启用
     */
    ENABLED(0, "启用"),

    /**
     * 停用
     */
    DISABLED(1, "停用");

    private final Integer code;

    private final String message;

    EnableStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
