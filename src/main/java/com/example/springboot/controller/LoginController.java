package com.example.springboot.controller;

import cn.hutool.core.util.StrUtil;
import com.example.springboot.common.Constants;
import com.example.springboot.common.Result;
import com.example.springboot.controller.dto.UserDTO;
import com.example.springboot.service.IUserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @title: LoginController
 * @Author
 * @Date: 2023/1/13 15:11
 * @Version 1.0
 */
@RestController
@RequestMapping("/user")
public class LoginController {

    @Resource
    private IUserService userService;

    @PostMapping("/login")
    public Result login(@RequestBody UserDTO userDTO) {
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();
        if(StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            return Result.error(Constants.CODE_400.getCode(), "参数错误");
        }
        return Result.success(userService.login(userDTO));
    }
}