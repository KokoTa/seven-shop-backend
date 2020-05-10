package com.example.shop.exception.http;

public class ParameterException extends HttpException {
    private static final long serialVersionUID = -2667870660773572163L;

    public ParameterException(int code) {
        this.code = code;
        this.httpStatusCode = 400;
    }
}