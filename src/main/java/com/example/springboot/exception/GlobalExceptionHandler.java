package com.example.springboot.exception;

import com.example.springboot.common.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @title: ExceptionHandler
 * @Author
 * @Date: 2023/1/14 15:57
 * @Version 1.0
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public Result handle(ServiceException se) {
        return Result.error(se.getCode(), se.getMessage());
    }
}