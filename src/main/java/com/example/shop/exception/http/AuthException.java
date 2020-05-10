package com.example.shop.exception.http;

public class AuthException extends HttpException {
    private static final long serialVersionUID = 115575626821387805L;

    public AuthException(int code) {
        this.code = code;
        this.httpStatusCode = 401;
    }
}
