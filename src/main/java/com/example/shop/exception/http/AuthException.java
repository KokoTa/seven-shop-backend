package com.example.shop.exception.http;

public class AuthException extends HttpException {
    public AuthException(int code) {
        this.code = code;
        this.httpStatusCode = 401;
    }
}
