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
 * @since 2023-04-15
 */
@Getter
@Setter
  @TableName("std_collection")
@ApiModel(value = "StdCollection对象", description = "")
public class StdCollection implements Serializable {

    private static final long serialVersionUID = 1L;

      @ApiModelProperty("用户id")
        @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      @ApiModelProperty("用户名称")
        private String username;

      @ApiModelProperty("院校名称")
      private String name;

      @ApiModelProperty("院校层级")
      private Integer classFlag;

      @ApiModelProperty("院校专业")
      private String specialty;

      @ApiModelProperty("最低分数线")
      private Integer minScore;

      @ApiModelProperty("最低排名")
      private Integer minRank;

      @ApiModelProperty("用户头像")
      private String avatarUser;

      @ApiModelProperty("院校头像")
      private String avatar;

      @ApiModelProperty("院校省份")
      private String province;

      @ApiModelProperty("院校地区")
      private String area;


}
