package com.example.springboot.controller.dto;

import cn.hutool.core.annotation.Alias;
import lombok.Data;

/**
 * @title: UserDTO
 * @Author
 * @Date: 2023/1/13 15:13
 * @Version 1.0
 */

// 接受用户登录、注册相关数据
@Data
public class UserDTO {

    @Alias("id")
    private Integer id;

    @Alias("用户名")
    private String username;

    @Alias("密码")
    private String password;

    @Alias("昵称")
    private String nickname;

    @Alias("头像")
    private String avatar;

    // JWT密钥
    private String token;
}