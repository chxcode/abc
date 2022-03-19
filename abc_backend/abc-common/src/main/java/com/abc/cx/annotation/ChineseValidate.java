package com.abc.cx.annotation;

import com.abc.cx.validator.ChineseValidator;

import javax.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: ChangXuan
 * @Decription: 中文校验
 * @Date: 21:25 2021/11/6
 **/
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ChineseValidator.class )
public @interface ChineseValidate {

}
