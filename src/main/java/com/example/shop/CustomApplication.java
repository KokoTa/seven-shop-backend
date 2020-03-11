package com.example.shop;

import com.example.shop.other.A;
import com.example.shop.other.BeanConfig;
import com.example.shop.other.BeanSelector;
import com.example.shop.other.EnableCustomConfiguration;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

//@ComponentScan
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
