package com.example.springboot.config.interceptor;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.springboot.common.Constants;
import com.example.springboot.entity.StdUser;
import com.example.springboot.entity.User;
import com.example.springboot.exception.ServiceException;
import com.example.springboot.service.IStdUserService;
import com.example.springboot.service.IUserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @title: JWTinterceptor
 * @Author
 * @Date: 2023/1/17 15:13
 * @Version 1.0
 */
public class JWTInterceptor implements HandlerInterceptor {

    @Resource
    private IUserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) {

        String token = request.getHeader("token");

        // 若非映射到方法则直接通过
        if(!(handler instanceof HandlerMethod)) {
            return true;
        }

        // 执行认证
        if(StrUtil.isBlank(token)) {
            throw new ServiceException(Constants.CODE_402.getCode(), "Token 不存在, 请重新登录");
        }

        // 获取 token 中的 userId
        String userId;
        try {
            userId = JWT.decode(token).getAudience().get(0);
        } catch(JWTDecodeException j) {
            throw new ServiceException(Constants.CODE_402.getCode(), "Token 验证失败, UserId 不存在, 请重新登录");
        }

        // 通过token的userId查询数据库
        User user = userService.getById(userId);
        if(user == null) {
            throw new ServiceException(Constants.CODE_402.getCode(), "用户不存在, 请重新登录");
        }

        // 用户密码加签验证 token
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256((user.getPassword()))).build();
        try {
            jwtVerifier.verify(token);
        } catch (JWTVerificationException j) {
            throw new ServiceException(Constants.CODE_402.getCode(), "Token 验证失败, 账号密码不存在, 请重新登录");
        }

        return true;
    }
}