package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author Misaki
 * @since 2023-02-28
 */
@Getter
@Setter
@TableName("std_application")
@ApiModel(value = "StdApplication对象", description = "")
public class StdApplication implements Serializable {

    private static final long serialVersionUID = 1L;

      @ApiModelProperty("用户id")
      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      @ApiModelProperty("用户名")
      private String name;

      @ApiModelProperty("报考分数")
      private Integer score;

      @ApiModelProperty("志愿院校1")
      private String application1;

      @ApiModelProperty("志愿院校2")
      private String application2;

      @ApiModelProperty("志愿院校3")
      private String application3;

      @ApiModelProperty("志愿院校4")
      private String application4;

      @ApiModelProperty("志愿院校5")
      private String application5;

      @ApiModelProperty("用户头像")
      private String avatar;


}
