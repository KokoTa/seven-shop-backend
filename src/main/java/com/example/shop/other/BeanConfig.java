package com.example.shop.other;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    @Conditional(ConditionTest.class)
    public A a() {
        return new A();
    }
}
