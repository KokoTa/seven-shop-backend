package com.example.shop.other.testClass;


public class A {
    public B getB() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        String name = "B";
        Class<?> c = Class.forName("com.example.shop.other.testClass." + name);
        B b = (B) c.newInstance();
        return b;
    }
}
