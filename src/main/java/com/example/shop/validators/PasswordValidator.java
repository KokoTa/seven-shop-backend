package com.example.shop.validators;

import com.example.shop.dto.User;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

// 该类给注解提供判断逻辑
// 第一个参数：自定义注解
// 第二个参数：修饰的目标类
public class PasswordValidator implements ConstraintValidator<PasswordEqual, User> {
    private int min;
    private int max;

    @Override
    public void initialize(PasswordEqual constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(User user, ConstraintValidatorContext constraintValidatorContext) {
        String password1 = user.getPassword1();
        String password2 = user.getPassword2();
        // 这里省略判断 min/max 的逻辑，只判断是否相等
        return password1.equals(password2);
    }
}
