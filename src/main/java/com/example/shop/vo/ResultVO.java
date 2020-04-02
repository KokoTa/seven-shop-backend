package com.example.shop.vo;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultVO {
    private Integer code;
    private Object data;
    private String message;
}
