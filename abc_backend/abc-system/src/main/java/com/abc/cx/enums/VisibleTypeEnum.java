package com.abc.cx.enums;

import lombok.Getter;

/**
 * @Author: ChangXuan
 * @Decription:
 * @Date: 22:37 2021/11/6
 **/
@Getter
public enum VisibleTypeEnum implements CodeEnum {

    /**
     * 显示
     */
    VISIBLE(0, "显示"),

    /**
     * 隐藏
     */
    HIDE(1, "隐藏");

    private final Integer code;

    private final String message;

    VisibleTypeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
