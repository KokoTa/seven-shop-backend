package com.example.shop.exception.http;

public class JsonConvertException extends HttpException {
    private static final long serialVersionUID = 4727955781043117607L;

    public JsonConvertException(int code) {
        this.code = code;
        this.httpStatusCode = 500;
    }
}
