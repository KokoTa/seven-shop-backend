package com.example.shop.exception.http;

public class ForbiddenException extends HttpException {
    private static final long serialVersionUID = -25815998079975852L;

    public ForbiddenException(int code) {
        this.code = code;
        this.httpStatusCode = 403;
    }
}
