package com.example.springboot.service;

import com.example.springboot.controller.dto.UserDTO;
import com.example.springboot.entity.StdUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Misaki
 * @since 2023-02-17
 */
public interface IStdUserService extends IService<StdUser> {

    UserDTO login(UserDTO userDTO);

    boolean register(UserDTO userDTO);
}
