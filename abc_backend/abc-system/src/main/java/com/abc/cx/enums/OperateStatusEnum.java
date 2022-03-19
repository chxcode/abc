package com.abc.cx.enums;

import lombok.Getter;

/**
 * @Author: ChangXuan
 * @Decription: 操作状态
 * @Date: 22:36 2021/11/6
 **/
@Getter
public enum OperateStatusEnum implements CodeEnum {

    /**
     * 成功
     */
    SUCCESS(0, "成功"),

    /**
     * 失败
     */
    FAIL(1, "失败");

    private final Integer code;

    private final String message;

    OperateStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
