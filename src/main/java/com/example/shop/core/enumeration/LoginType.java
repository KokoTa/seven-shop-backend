package com.example.shop.core.enumeration;

public enum LoginType {
    WX(0, "微信登录"), EMAIL(1, "邮箱登录");

    private Integer value;
    private String description;

    LoginType(Integer value, String description) {
        this.value = value;
        this.description = description;
    }
}
