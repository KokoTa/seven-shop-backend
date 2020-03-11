package com.example.shop.core;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ExceptionResponse {
    private Integer code;
    private String message;
    private String requestUrl;
}
