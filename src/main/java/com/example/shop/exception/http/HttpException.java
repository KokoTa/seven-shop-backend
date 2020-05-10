package com.example.shop.exception.http;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class HttpException extends RuntimeException {
    private static final long serialVersionUID = -9172523943109004663L;
    protected Integer code;
    protected Integer httpStatusCode = 500;
}
