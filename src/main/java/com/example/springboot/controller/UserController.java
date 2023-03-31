package com.example.springboot.controller;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Result;
import com.example.springboot.utils.TokenUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

import com.example.springboot.service.IUserService;
import com.example.springboot.entity.User;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Misaki
 * @since 2023-01-11
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private IUserService userService;

    User currentUser = TokenUtils.getCurrentUser();

    // 增加 更新
    @PostMapping
    public Result save(@RequestBody User user) {
        return Result.success(userService.saveOrUpdate(user));
    }

    // 删除
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(userService.removeById(id));
    }

    // 批量删除
    @DeleteMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        return Result.success(userService.removeByIds(ids));
    }

    // 查询所有
    @GetMapping
    public Result findAll(){
        return Result.success(userService.list());
    }

    // 查询单个
    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(userService.getById(id));
    }

    // 分页查询
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                               @RequestParam Integer pageSize,
                               @RequestParam(defaultValue = "") String username,
                               @RequestParam(defaultValue = "") String nickname) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(username != null, "username", username);
        queryWrapper.like(nickname != null, "nickname", nickname);
        // 逆序
//      queryWrapper.orderByDesc("id");
        return Result.success(userService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    // 导出接口
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // 数据库检索数据
        List<User> list = userService.list();
        // 数据写磁盘
        // ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath + "/用户信息.xlsx");
        // 数据写浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        // 自定义名称
        // writer.addHeaderAlias("username", "用户名");
        // writer.addHeaderAlias("password", "密码");
        // writer.addHeaderAlias("nickname", "昵称");
        // writer.addHeaderAlias("createTime", "创建时间");

        // 强制写出 list 到 excel 中
        writer.write(list, true);

        // 浏览器响应请求头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("用户信息", "utf-8");
        response.setHeader("Content-Disposition", "attachment;fileNname=" + fileName + ".xlsx");

        // 刷新输出流
        ServletOutputStream outputStream = response.getOutputStream();
        writer.flush(outputStream, true);
        outputStream.close();
        writer.close();
    }

    // 导入接口
    @PostMapping("/import")
    public boolean imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader excelReader = ExcelUtil.getReader(inputStream);
        // 保持数据格式一致
        List<User> list = excelReader.readAll(User.class);
        System.out.println(list);
        return userService.saveBatch(list);
    }
}

