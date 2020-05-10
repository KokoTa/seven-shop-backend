package com.example.shop.exception.http;

public class NotFoundException extends HttpException {
    private static final long serialVersionUID = 7397112586973685088L;

    public NotFoundException(int code) {
        this.code = code;
        this.httpStatusCode = 404;
    }
}
