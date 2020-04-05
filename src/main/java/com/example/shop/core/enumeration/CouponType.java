package com.example.shop.core.enumeration;

import java.util.stream.Stream;

public enum CouponType {
    FULL_MINUS(1, "满减券"),
    FULL_OFF(2, "满减折扣"),
    NO_THRESHOLD_MINUS(3, "无门槛减除券");

    private Integer value;
    private String description;

    CouponType(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

    public Integer getValue() {
        return this.value;
    }

    public static CouponType toType (int value) {
        return Stream.of(CouponType.values())
                .filter((cs) -> cs.value == value)
                .findAny()
                .orElse(null);
    }
}
