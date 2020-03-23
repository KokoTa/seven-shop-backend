package com.example.shop.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD) // 标注在属性上
@Constraint(validatedBy = TokenPasswordValidator.class)
public @interface TokenPassword {
    String message() default "密码长度错误";
    int min() default 5;
    int max() default 20;

    // 自定义注解需要用到以下两个信息
    // 用于分组
    Class<?>[] groups() default {};
    // 用于预置
    Class<? extends Payload>[] payload() default {};
}
