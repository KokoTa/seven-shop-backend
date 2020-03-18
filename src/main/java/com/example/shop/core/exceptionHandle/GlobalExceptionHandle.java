package com.example.shop.core.exceptionHandle;

import com.example.shop.core.configuration.ExceptionCodeConfiguration;
import com.example.shop.exception.http.HttpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandle {

    @Autowired
    private ExceptionCodeConfiguration exceptionCodeConfiguration;

    // 全局异常
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResponse handleException(HttpServletRequest req, Exception e) {
        String method = req.getMethod();
        String url = req.getRequestURI();
        String errStr = e.getMessage();
        return new ExceptionResponse(99999, errStr, method + " " + url);
    }

    // 自定义的 Http 异常
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

    // body 参数的异常
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleMethodArgumentNotValidException(HttpServletRequest req, MethodArgumentNotValidException e) {
        String method = req.getMethod();
        String url = req.getRequestURI();
        // 可能有多个验证错误
        List<ObjectError> errors = e.getBindingResult().getAllErrors();
        String errMsg = this.formatAllErrorMessages(errors);

        return new ExceptionResponse(10001, errMsg, method + " " + url);
    }

    private String formatAllErrorMessages(List<ObjectError> errors) {
        StringBuffer msg = new StringBuffer();
        errors.forEach(err -> {
            msg.append(err.getDefaultMessage()).append("; ");
        });
        return msg.toString();
    }

    // 查询参数的异常
    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleConstraintViolationException(HttpServletRequest req, ConstraintViolationException e) {
        String method = req.getMethod();
        String url = req.getRequestURI();
        // 可能有多个验证错误
        String errMsg = e.getMessage();

        return new ExceptionResponse(10001, errMsg, method + " " + url);
    }

}
