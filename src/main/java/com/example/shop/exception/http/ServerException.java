package com.example.shop.exception.http;

public class ServerException extends HttpException {
    private static final long serialVersionUID = -2667870660773572163L;

    public ServerException(int code) {
        this.code = code;
        this.httpStatusCode = 500;
    }
}
