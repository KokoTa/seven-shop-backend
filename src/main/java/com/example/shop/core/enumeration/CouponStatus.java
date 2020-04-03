package com.example.shop.core.enumeration;

import java.util.stream.Stream;

public enum CouponStatus {
    AVAILABLE(1, "可使用，未过期"),
    USED(2, "已使用"),
    EXPIRED(3, "未使用，已过期");

    private Integer value;
    private String description;

    CouponStatus(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

    public Integer getValue() {
        return this.value;
    }

    public static CouponStatus toType (int value) {
        return Stream.of(CouponStatus.values())
                .filter((cs) -> cs.value == value)
                .findAny()
                .orElse(null);
    }
}
