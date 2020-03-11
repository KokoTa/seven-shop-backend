package com.example.shop.core;

import com.example.shop.core.configuration.ExceptionCodeConfiguration;
import com.example.shop.exception.http.HttpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandle {

    @Autowired
    private ExceptionCodeConfiguration exceptionCodeConfiguration;

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResponse handleException(HttpServletRequest req, Exception e) {
        String method = req.getMethod();
        String url = req.getRequestURI();
        return new ExceptionResponse(10000, "服务器异常", method + " " + url);
    }

    @ExceptionHandler(value = HttpException.class)
    @ResponseBody
    public ResponseEntity<ExceptionResponse> handleHttpException(HttpServletRequest req, HttpException e) {
        String method = req.getMethod();
        String url = req.getRequestURI();
        Integer code = e.getCode();
        String message = exceptionCodeConfiguration.getMessage(code);

        ExceptionResponse exceptionResponse = new ExceptionResponse(code, message, method + " " + url);
        HttpHeaders httpHeaders = new HttpHeaders();
        HttpStatus httpStatus = HttpStatus.resolve(e.getHttpStatusCode());

        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<ExceptionResponse> responseEntity = new ResponseEntity<>(exceptionResponse, httpHeaders, httpStatus);

        return responseEntity;
    }
}
