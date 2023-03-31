package com.example.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.common.Result;
import com.example.springboot.entity.User;
import com.example.springboot.service.IUserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @title: PersonInfoController
 * @Author
 * @Date: 2023/1/16 15:01
 * @Version 1.0
 */
@RestController
@RequestMapping("/personInfo")
public class PersonInfoController {

    @Resource
    private IUserService userService;

    @GetMapping("/id/{id}")
    public Result findOne(@PathVariable Integer id) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        return Result.success(userService.getOne(queryWrapper));
    }
}