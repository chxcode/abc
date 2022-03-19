package com.abc.cx.enums;

import lombok.Getter;

/**
 * @Author: ChangXuan
 * @Decription: 菜单类型
 * @Date: 22:36 2021/11/6
 **/
@Getter
public enum MenuTypeEnum implements CodeEnum {

    /**
     * 主菜单
     */
    MAIN(0, "主菜单"),

    /**
     * 子菜单
     */
    CHILD(1, "子菜单"),

    /**
     * 按钮
     */
    BUTTON(2, "按钮");

    private final Integer code;

    private final String message;

    MenuTypeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
