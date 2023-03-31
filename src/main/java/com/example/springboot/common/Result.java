package com.example.springboot.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @title: Result
 * @Author
 * @Date: 2023/1/14 15:19
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
// 接口统一返回包装类
public class Result {
    private String code;
    private String msg;
    private Object data;

    public static Result success() {
        return new Result(Constants.CODE_200.getCode(), "", null);
    }

    public static Result success(Object data) {
        return new Result(Constants.CODE_200.getCode(), "", data);
    }

    public static Result error() {
        return new Result(Constants.CODE_500.getCode(), "系统错误", null);
    }

    public static Result error(String code, String msg) {
        return new Result(code, msg, null);
    }
}