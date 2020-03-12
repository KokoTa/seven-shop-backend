package com.example.shop.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) // 运行时执行
@Target(ElementType.TYPE) // 这个注解标在类上
@Constraint(validatedBy = PasswordValidator.class) // 载入验证逻辑
public @interface PasswordEqual {
    String message() default "自定义错误：密码不一致";
    int min() default 3;
    int max() default 10;

    // 自定义注解需要用到以下两个信息
    // 用于分组
    Class<?>[] groups() default {};
    // 用于预置
    Class<? extends Payload>[] payload() default {};
}
