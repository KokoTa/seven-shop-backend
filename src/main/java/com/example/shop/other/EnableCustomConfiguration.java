package com.example.shop.other;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(BeanSelector.class)
public @interface EnableCustomConfiguration {
}
