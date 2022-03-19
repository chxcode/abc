package com.abc.cx.annotation;

import com.abc.cx.enums.OperateTypeEnum;

import java.lang.annotation.*;

/**
 * @Author: ChangXuan
 * @Decription: 日志记录
 * @Date: 21:55 2021/11/6
 **/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {

    /**
     * 模块
     */
    String title() default "";

    /**
     * 操作
     */
    OperateTypeEnum operateType() default OperateTypeEnum.OTHER;

}
