package com.abc.cx.validator;

import cn.hutool.core.lang.Validator;
import com.abc.cx.annotation.PasswordValidate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @Author: ChangXuan
 * @Decription:
 * @Date: 22:03 2021/11/6
 **/
public class PasswordValidator implements ConstraintValidator<PasswordValidate, Object> {

    @Override
    public void initialize(PasswordValidate constraintAnnotation) {
        System.out.println("password validator init");
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        String password = (String) value;
        return Validator.isGeneral(password);
    }

}
