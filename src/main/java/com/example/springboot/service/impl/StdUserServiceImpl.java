package com.example.springboot.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.springboot.common.Constants;
import com.example.springboot.controller.dto.UserDTO;
import com.example.springboot.entity.StdUser;
import com.example.springboot.entity.User;
import com.example.springboot.exception.ServiceException;
import com.example.springboot.mapper.StdUserMapper;
import com.example.springboot.mapper.UserMapper;
import com.example.springboot.service.IStdUserService;
import com.example.springboot.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.utils.TokenUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Misaki
 * @since 2023-01-11
 */

// 具体业务实现
@Service
public class StdUserServiceImpl extends ServiceImpl<StdUserMapper, StdUser> implements IStdUserService {

    StdUser stdUser;

    @Override
    public UserDTO login(UserDTO userDTO) {
        // 判断用户是否已存在
        isExistDataInAll(userDTO);

        if(stdUser != null) {
            // 浅拷贝: 只获取需要的部分数据即可
            BeanUtil.copyProperties(stdUser, userDTO, true);
            // 获取 Token
            String token = TokenUtils.genToken(stdUser.getId().toString(), stdUser.getPassword());
            userDTO.setToken(token);
            return userDTO;
        }
        else {
            throw new ServiceException(Constants.CODE_401.getCode(), "用户名或密码错误");
        }
    }

    @Override
    public boolean register(UserDTO userDTO) {
        // 判断用户是否已存在
        isExistDataInUsername(userDTO);

        // 若数据库不存在用户信息 则复制相关信息到数据库保存
        if(stdUser == null) {
            stdUser = new StdUser();
            BeanUtil.copyProperties(userDTO, stdUser, true);
            save(stdUser);
            return true;
        }
        else {
            throw new ServiceException(Constants.CODE_600.getCode(), "用户已存在");
        }
    }

    public void isExistDataInAll(UserDTO userDTO) {
        QueryWrapper<StdUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userDTO.getUsername());
        queryWrapper.eq("password", userDTO.getPassword());
        try {
            // 数据库查询匹配
            stdUser = getOne(queryWrapper);
        } catch (Exception e) {
            throw new ServiceException(Constants.CODE_500.getCode(), "系统错误");
        }
    }

    public void isExistDataInUsername(UserDTO userDTO) {
        QueryWrapper<StdUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userDTO.getUsername());
        try {
            // 数据库查询匹配
            stdUser = getOne(queryWrapper);
        } catch (Exception e) {
            throw new ServiceException(Constants.CODE_500.getCode(), "系统错误");
        }
    }
}
