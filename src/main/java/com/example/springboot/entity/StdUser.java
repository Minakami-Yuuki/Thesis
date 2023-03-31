package com.example.springboot.entity;

import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

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
 * @since 2023-02-17
 */
@Getter
@Setter
@TableName("std_user")
@ApiModel(value = "StdUser对象", description = "")
public class StdUser implements Serializable {

    private static final long serialVersionUID = 1L;

      @ApiModelProperty("id")
      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      @Alias("用户名")
      @ApiModelProperty("用户名")
      private String username;

      @Alias("密码")
      @ApiModelProperty("密码")
      private String password;

      @Alias("昵称")
      @ApiModelProperty("昵称")
      private String nickname;

      @Alias("创建时间")
      @ApiModelProperty("时间戳")
      private Date createTime;

      @Alias("头像")
      @ApiModelProperty("头像")
      private String avatar;


}
