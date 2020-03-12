package com.example.shop.other;

import com.example.shop.other.testClass.A;
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
