package com.abc.cx.enums;

import lombok.Getter;

/**
 * @Author: ChangXuan
 * @Decription: 菜单状态
 * @Date: 22:36 2021/11/6
 **/
@Getter
public enum MessageStatusEnum implements CodeEnum {

    /**
     * 未读
     */
    UNREAD(0, "未读"),

    /**
     * 已读
     */
    READ(1, "已读");

    private final Integer code;

    private final String message;

    MessageStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
