package com.example.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.entity.StdApplication;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import com.example.springboot.common.Result;

import org.springframework.web.bind.annotation.RestController;

import com.example.springboot.service.IStdCollectionService;
import com.example.springboot.entity.StdCollection;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Misaki
 * @since 2023-04-14
 */
@RestController
@RequestMapping("/collection")
public class StdCollectionController {

    @Resource
    private IStdCollectionService stdCollectionService;

    // 增加 更新
    @PostMapping
    public Result save(@RequestBody StdCollection stdCollection) {
        return Result.success(stdCollectionService.saveOrUpdate(stdCollection));
    }

    // 删除 (根据id)
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(stdCollectionService.removeById(id));
    }

    // 删除 (根据名称)
    @DeleteMapping("/deleteByName/{name}")
    public Result deleteByName(@PathVariable String name) {
        QueryWrapper<StdCollection> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(name != null, "name", name);
        return Result.success(stdCollectionService.remove(queryWrapper));
    }

    // 批量删除
    @DeleteMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        return Result.success(stdCollectionService.removeByIds(ids));
    }

    // 查询所有
    @GetMapping
    public Result findAll(){
        return Result.success(stdCollectionService.list());
    }

    // 查询单个
    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(stdCollectionService.getById(id));
    }

    // 查询单个 (根据名称)
    @GetMapping("/searchByName")
    public Result findByName(@RequestParam String name) {
        long count = stdCollectionService.count(new QueryWrapper<StdCollection>()
                .eq("name", name));
        if (count <= 0) {
            return Result.error();
        }
        else {
            return Result.success();
        }
    }

    // 分页查询
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String name) {
        QueryWrapper<StdCollection> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(name != null, "name", name);
        // queryWrapper.like(username != null, "username", username);
        // 逆序
        queryWrapper.orderByDesc("id");
        return Result.success(stdCollectionService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }
}
