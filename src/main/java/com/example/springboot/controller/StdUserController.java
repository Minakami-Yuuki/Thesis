package com.example.springboot.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Constants;
import com.example.springboot.common.Result;
import com.example.springboot.controller.dto.UserDTO;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RestController;

import com.example.springboot.service.IStdUserService;
import com.example.springboot.entity.StdUser;

import java.util.List;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Misaki
 * @since 2023-02-17
 */
@RestController
@RequestMapping("/stdUser")
public class StdUserController {

    @Resource
    private IStdUserService stdUserService;

    // 增加 更新
    @PostMapping
    public Result save(@RequestBody StdUser stdUser) {
        return Result.success(stdUserService.saveOrUpdate(stdUser));
    }

    // 删除
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(stdUserService.removeById(id));
    }

    // 批量删除
    @DeleteMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        return Result.success(stdUserService.removeByIds(ids));
    }

    // 查询所有
    @GetMapping
    public Result findAll(){
        return Result.success(stdUserService.list());
    }

    // 查询单个 -- id
    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(stdUserService.getById(id));
    }

    // 分页查询
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<StdUser> queryWrapper = new QueryWrapper<>();
        // queryWrapper.like(username != null, "username", username);
        // 逆序
        queryWrapper.orderByDesc("id");
        return Result.success(stdUserService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    @PostMapping("/login")
    public Result login(@RequestBody UserDTO userDTO) {
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();
        if(StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            return Result.error(Constants.CODE_400.getCode(), "参数错误");
        }
        return Result.success(stdUserService.login(userDTO));
    }

    @PostMapping("/register")
    public Result register(@RequestBody UserDTO userDTO) {
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();
        if(StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            return Result.error(Constants.CODE_400.getCode(), "参数错误");
        }
        return Result.success(stdUserService.register(userDTO));
    }
}

