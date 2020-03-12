package com.example.shop.other;

import com.example.shop.other.testClass.A;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

// 手动实现一个启动类
// @ComponentScan
@EnableCustomConfiguration
public class CustomApplication {
    public static void main(String[] args) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        ConfigurableApplicationContext context = new SpringApplicationBuilder(CustomApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);
        A a = (A) context.getBean("a");
        a.getB().say();
    }
}
