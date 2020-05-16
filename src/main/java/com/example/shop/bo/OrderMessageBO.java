package com.example.shop.bo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderMessageBO {
    private Long orderId;
    private Long userId;
    private Long couponId;
    private String message;

    public OrderMessageBO(String message) {
        this.message = message;
        String[] temp = message.split(",");
        this.userId = Long.valueOf(temp[0]);
        this.orderId = Long.valueOf(temp[1]);
        this.couponId = Long.valueOf(temp[2]);
    }
}
