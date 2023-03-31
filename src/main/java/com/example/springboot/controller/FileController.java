package com.example.springboot.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.entity.FileSQL;
import com.example.springboot.mapper.FileMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * @title: FileController
 * @Author
 * @Date: 2023/1/18 14:57
 * @Version 1.0
 */
@RestController
@RequestMapping("/file")
public class FileController {

    @Value("${files.upload.path}")
    private String fileUploadPath;

    @Resource
    private FileMapper fileMapper;

    // 加载图片接口
    @PostMapping("/upload")
    public String upload(@RequestParam MultipartFile file) throws IOException {

        // 名字、类型、大小
        String originalFilename = file.getOriginalFilename();
        String type = FileUtil.extName(originalFilename);
        long size = file.getSize();

        // 通过对比文件的 md5, 避免上传相同文件至磁盘中
        String uuid = IdUtil.fastSimpleUUID();
        String fileName = uuid + StrUtil.DOT + type;
        File uploadFile = new File(fileUploadPath + fileName);
        File parentFile = uploadFile.getParentFile();

        // 判断配置文件目录是否存在 : 不在则创建
        if(!parentFile.exists()) {
            parentFile.mkdirs();
        }

        // 获取文件 url
        String url;
        // 存储磁盘目录
        file.transferTo(uploadFile);
        // 定义文件唯一标识码
        String md5 = SecureUtil.md5(uploadFile);
        FileSQL fileByMd5 = getFileByMd5(md5);

        // 获取文件 url
        // 判断md5是否存在:
        if(fileByMd5 != null) {
            // 若存在则删除刚才保存至磁盘的文件
            url = fileByMd5.getUrl();
            uploadFile.delete();
        }
        else {
            // 若不存在则存磁盘
            url = "http://localhost:8089/file/" + fileName;
        }

        // 存储数据库
        FileSQL fileSQL = new FileSQL();
        fileSQL.setName(originalFilename);
        fileSQL.setType(type);
        fileSQL.setSize(size / 1024);
        fileSQL.setUrl(url);
        fileSQL.setMd5(md5);
        fileMapper.insert(fileSQL);

        return url;
    }

    // 下载图片接口
    @GetMapping("/{fileName}")
    public void download(@PathVariable String fileName, HttpServletResponse response) throws IOException {

        // 根据文件唯一标识获取文件
        File uploadFile = new File(fileUploadPath + fileName);
        // 设置输出流格式

        ServletOutputStream os = response.getOutputStream();
        response.addHeader("Content-Disposition",
                "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        response.setContentType("application/octet-stream");

        // 读取文件字节流并写出
        os.write(FileUtil.readBytes(uploadFile));
        os.flush();
        os.close();
    }

    // MD5 查询图片
    private FileSQL getFileByMd5(String md5) {
        // 查询文件md5是否存在
        QueryWrapper<FileSQL> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("md5", md5);
        List<FileSQL> fileSQLList = fileMapper.selectList(queryWrapper);
        return fileSQLList.size() == 0 ? null : fileSQLList.get(0);
    }
}