package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @title: File
 * @Author
 * @Date: 2023/1/18 16:08
 * @Version 1.0
 */
@Data
@TableName("sys_file")
public class FileSQL {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;

    private String type;

    private long size;

    private String url;

    private Boolean isDelete;

    private Boolean enable;

    private String md5;
}