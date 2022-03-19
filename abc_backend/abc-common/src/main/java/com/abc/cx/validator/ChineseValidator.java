package com.abc.cx.validator;

import cn.hutool.core.lang.Validator;
import com.abc.cx.annotation.ChineseValidate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @Author: ChangXuan
 * @Decription: 中文校验器
 * @Date: 21:30 2021/11/6
 **/
public class ChineseValidator implements ConstraintValidator<ChineseValidate, Object> {

    @Override
    public void initialize(ChineseValidate constraintAnnotation) {
        System.out.println("chinese validator init");
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        String chinese = (String) value;
        return Validator.isChinese(chinese);
    }

}
