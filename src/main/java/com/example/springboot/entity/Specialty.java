package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
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
 * @since 2023-02-15
 */
@Getter
@Setter
@TableName("sys_specialty")
@ApiModel(value = "Specialty对象", description = "")
public class Specialty implements Serializable {

    private static final long serialVersionUID = 1L;

      @ApiModelProperty("id")
      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      @ApiModelProperty("专业名称")
      private String name;

      @ApiModelProperty("专业代码")
      private Integer code;

      @ApiModelProperty("专业学科")
      private String specialty;

      @ApiModelProperty("专业大类")
      private String category;


}
