package com.example.springboot.exception;

import lombok.Getter;

/**
 * @title: ServiceException
 * @Author
 * @Date: 2023/1/14 16:00
 * @Version 1.0
 */

// 自定义异常
@Getter
public class ServiceException extends RuntimeException{
    private final String code;

    public ServiceException(String code, String msg) {
        super(msg);
        this.code = code;
    }
}