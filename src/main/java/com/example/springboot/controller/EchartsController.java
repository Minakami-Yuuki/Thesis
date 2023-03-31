package com.example.springboot.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.Quarter;
import cn.hutool.core.io.unit.DataUnit;
import com.example.springboot.common.Result;
import com.example.springboot.entity.StdUser;
import com.example.springboot.entity.User;
import com.example.springboot.service.IStdUserService;
import com.example.springboot.service.IUserService;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @title: EchartsController
 * @Author
 * @Date: 2023/1/20 14:59
 * @Version 1.0
 */
@RestController
@RequestMapping("/echarts")
public class EchartsController {

    @Resource
    private IUserService userService;

    @Resource
    private IStdUserService stdUserService;

    // 静态注入图形
    @GetMapping("/example")
    public Result get() {
        Map<String, Object> map = new HashMap<>();
        map.put("x", CollUtil.newArrayList("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"));
        map.put("y", CollUtil.newArrayList(150, 230, 224, 218, 135, 147, 260));
        return Result.success(map);
    }

    // 动态注入图形
    @GetMapping("/members")
    public Result members() {
        // 按创建时间季度划分
        List<User> list = userService.list();
        int q1 = 0;
        int q2 = 0;
        int q3 = 0;
        int q4 = 0;
        for (User user : list) {
            Date createTime = user.getCreateTime();
            Quarter quarter = DateUtil.quarterEnum(createTime);
            switch (quarter) {
                case Q1: q1 += 1; break;
                case Q2: q2 += 1; break;
                case Q3: q3 += 1; break;
                case Q4: q4 += 1; break;
                default: break;
            }
        }

        return Result.success(CollUtil.newArrayList(q1, q2, q3, q4));
    }

    // 动态注入图形
    @GetMapping("/stdMembers")
    public Result stdMembers() {
        // 按创建时间季度划分
        List<StdUser> list = stdUserService.list();
        int q1 = 0;
        int q2 = 0;
        int q3 = 0;
        int q4 = 0;
        for (StdUser stdUser : list) {
            Date createTime = stdUser.getCreateTime();
            Quarter quarter = DateUtil.quarterEnum(createTime);
            switch (quarter) {
                case Q1: q1 += 1; break;
                case Q2: q2 += 1; break;
                case Q3: q3 += 1; break;
                case Q4: q4 += 1; break;
                default: break;
            }
        }

        return Result.success(CollUtil.newArrayList(q1, q2, q3, q4));
    }
}