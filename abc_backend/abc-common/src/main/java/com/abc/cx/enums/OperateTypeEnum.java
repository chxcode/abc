package com.abc.cx.enums;

import lombok.Getter;

/**
 * @Author: ChangXuan
 * @Decription: 业务操作类型
 * @Date: 22:00 2021/11/6
 **/
@Getter
public enum OperateTypeEnum implements CodeEnum {

    /**
     * 其它
     */
    OTHER(0, "其它"),

    /**
     * 新增
     */
    ADD(1, "新增"),

    /**
     * 修改
     */
    UPDATE(2, "修改"),

    /**
     * 删除
     */
    DELETE(3, "删除"),

    /**
     * 导出
     */
    EXPORT(4, "导出"),

    /**
     * 导入
     */
    IMPORT(5, "导入"),

    /**
     * 强退
     */
    FORCE(6, "强退");


    private final Integer code;

    private final String message;

    OperateTypeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }


}
