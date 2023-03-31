package com.example.springboot.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.JWT;
import com.example.springboot.entity.StdUser;
import com.example.springboot.entity.User;
import com.example.springboot.service.IStdUserService;
import com.example.springboot.service.IUserService;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Objects;

/**
 * @title: TokenUtils
 * @Author
 * @Date: 2023/1/17 14:50
 * @Version 1.0
 */
public class TokenUtils {

    // 静态存储
    private static IUserService staticUserService;

    private static IStdUserService staticStdUserServe;

    // 生成 Token
    public static String genToken(String userId, String sign) {
        // 将 user id 保存到 token 里面, 作为载荷
        return  JWT.create().withAudience(userId)
                .withExpiresAt(DateUtil.offsetHour(new Date(), 2))  // 2小时后token过期
                .sign(Algorithm.HMAC256(sign));  // 以 password 作为 token 的密钥
    }

    // 获取当前登录的用户信息
    public static User getCurrentUser() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
            String token = request.getHeader("token");
            if(StrUtil.isNotBlank(token)) {
                String userId = JWT.decode(token).getAudience().get(0);
                return staticUserService.getById(Integer.valueOf(userId));
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public static void setStaticUserService(IUserService staticUserService) {
        TokenUtils.staticUserService = staticUserService;
    }
}