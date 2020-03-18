package com.example.shop.exception.http;

public class JsonConvertException extends HttpException {
    public JsonConvertException(int code) {
        this.code = code;
        this.httpStatusCode = 500;
    }
}
