package com.example.shop.other;


import org.springframework.stereotype.Component;

public class A {
    public B getB() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        String name = "B";
        Class<?> c = Class.forName("com.example.shop.other." + name);
        B b = (B) c.newInstance();
        return b;
    }
}
