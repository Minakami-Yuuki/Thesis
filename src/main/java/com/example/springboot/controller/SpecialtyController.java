package com.example.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import com.example.springboot.common.Result;

import org.springframework.web.bind.annotation.RestController;

import com.example.springboot.service.ISpecialtyService;
import com.example.springboot.entity.Specialty;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Misaki
 * @since 2023-02-15
 */
@RestController
@RequestMapping("/specialty")
public class SpecialtyController {

    @Resource
    private ISpecialtyService specialtyService;

    // 增加 更新
    @PostMapping
    public Result save(@RequestBody Specialty specialty) {
        return Result.success(specialtyService.saveOrUpdate(specialty));
    }

    // 删除
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(specialtyService.removeById(id));
    }

    // 批量删除
    @DeleteMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        return Result.success(specialtyService.removeByIds(ids));
    }

    // 查询所有
    @GetMapping
    public Result findAll(){
        return Result.success(specialtyService.list());
    }

    // 查询单个
    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(specialtyService.getById(id));
    }

    // 分页查询 -- 专业名称
    @GetMapping("/pageName")
    public Result findNamePage(@RequestParam Integer pageNum,
                               @RequestParam Integer pageSize,
                               @RequestParam (defaultValue = "") String name) {
        QueryWrapper<Specialty> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(name != null, "name", name);
        // 正序
        queryWrapper.orderByAsc("code");
        return Result.success(specialtyService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    // 分页查询 -- 专业类别
    @GetMapping("/pageSpecialty")
    public Result findSpecialtyPage(@RequestParam Integer pageNum,
                                    @RequestParam Integer pageSize,
                                    @RequestParam (defaultValue = "") String category) {
        QueryWrapper<Specialty> queryWrapper = new QueryWrapper<>();
        if (category.equals("全部")) {
            category = "";
            queryWrapper.like("category", category);
        }
        else {
            queryWrapper.eq("category", category);
        }
        // 正序
        queryWrapper.orderByAsc("code");
        return Result.success(specialtyService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }
}

