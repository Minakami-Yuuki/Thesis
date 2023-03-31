package com.example.springboot.config;

import com.example.springboot.config.interceptor.JWTInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @title: InterceptorConfig
 * @Author
 * @Date: 2023/1/17 15:49
 * @Version 1.0
 */
@Configuration
public class InterceptorConfig implements  WebMvcConfigurer {

    @Bean
    public JWTInterceptor jwtInterceptor() {
        return new JWTInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor())
                .addPathPatterns("/**")    // 拦截所有请求, 通过token来判断是否合法 是否需要重新登录
                // 排除拦截
                .excludePathPatterns("/user/**",
                                     "/stdUser/**",
                                     "/application/**",
                                     "/**/export",
                                     "/**/import",
                                     "/file/**",
                                     "/front/**",
                                     "/school/**",
                                     "/specialty/**");
    }
}