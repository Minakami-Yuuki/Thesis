package com.example.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.entity.Specialty;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.*;

import com.example.springboot.common.Result;

import org.springframework.web.bind.annotation.RestController;

import com.example.springboot.service.ISchoolService;
import com.example.springboot.entity.School;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Misaki
 * @since 2023-02-13
 */
@RestController
@RequestMapping("/school")
public class SchoolController {

    @Resource
    private ISchoolService schoolService;

    // 增加 更新
    @PostMapping
    public Result save(@RequestBody School school) {
        return Result.success(schoolService.saveOrUpdate(school));
    }

    // 删除
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(schoolService.removeById(id));
    }

    // 批量删除
    @DeleteMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        return Result.success(schoolService.removeByIds(ids));
    }

    // 查询所有
    @GetMapping
    public Result findAll(){
        return Result.success(schoolService.list());
    }

    // 查询单个
    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(schoolService.getById(id));
    }

    // 分页查询 -- 学校名称
    @GetMapping("/pageName")
    public Result findNamePage(@RequestParam Integer pageNum,
                               @RequestParam Integer pageSize,
                               @RequestParam (defaultValue = "") String name) {
        QueryWrapper<School> queryWrapper = new QueryWrapper<>();
        // 模糊查询
        queryWrapper.like(name != null, "name", name);
        // queryWrapper.like(username != null, "username", username);
        // 逆序 (双元素排序)
        queryWrapper.orderByAsc("min_rank");
        return Result.success(schoolService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    // 分页查询 -- 学校省份地区 层级
    @GetMapping("/pageSchool")
    public Result findSchoolPage(@RequestParam Integer pageNum,
                                 @RequestParam Integer pageSize,
                                 @RequestParam (defaultValue = "") String province,
                                 @RequestParam (defaultValue = "") Integer classFlag,
                                 @RequestParam (defaultValue = "0") Integer minScore,
                                 @RequestParam (defaultValue = "750") Integer maxScore) {
        // queryWrapper.like(username != null, "username", username);
        if (province.equals("全部")) {
            province = "";
        }
        QueryWrapper<School> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("province", province);

        if (Objects.equals(classFlag, 0)) {
            return Result.success(schoolService.page(new Page<>(pageNum, pageSize), queryWrapper.eq("class_flag", classFlag)));
        }
        queryWrapper.ge(classFlag != null, "class_flag", classFlag);
        queryWrapper.ge(minScore != null, "min_score", minScore);
        queryWrapper.le(maxScore != null, "min_score", maxScore);
        // 逆序
        queryWrapper.orderByAsc("min_rank");
        return Result.success(schoolService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    // 分页查询 -- 专业查询
    @GetMapping("/pageSpecialty")
    public Result findSpecialtyPage(@RequestParam Integer pageNum,
                                    @RequestParam Integer pageSize,
                                    @RequestParam (defaultValue = "") String specialty) {
        // queryWrapper.like(username != null, "username", username);
        QueryWrapper<School> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(specialty != null, "specialty", specialty);
        // 逆序
        queryWrapper.orderByAsc("min_rank");
        return Result.success(schoolService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }
}

