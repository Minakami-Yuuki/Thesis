package com.example.springboot.common;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @title: Constants
 * @Author
 * @Date: 2023/1/14 15:21
 * @Version 1.0
 */
public enum Constants {
    CODE_200("200"),  // 成功
    CODE_400("400"),  // 参数错误
    CODE_401("401"),  // 密码错误
    CODE_402("402"),  // Token不存在
    CODE_500("500"),  // 系统错误  (指数据库中有唯一键重复)
    CODE_600("600");  // 用户名已存在

    private final String CODE;

    Constants(String CODE) {
        this.CODE = CODE;
    }

    public String getCode() {
        return CODE;
    }
}