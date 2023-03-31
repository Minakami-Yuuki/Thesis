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
 * @since 2023-03-10
 */
@Getter
@Setter
  @TableName("sys_school")
@ApiModel(value = "School对象", description = "")
public class School implements Serializable {

    private static final long serialVersionUID = 1L;

      @ApiModelProperty("id")
        @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      @ApiModelProperty("学校名称")
        private String name;

      @ApiModelProperty("学校地区(省)")
      private String province;

      @ApiModelProperty("学校地区(市)")
      private String area;

      @ApiModelProperty("学校专业")
      private String specialty;

      @ApiModelProperty("学校层级 (3为985 2为211 1为双一流 0为双非)")
      private Integer classFlag;

      @ApiModelProperty("最低录取分数线")
      private Integer minScore;

      @ApiModelProperty("最低录取排名")
      private Integer minRank;

      @ApiModelProperty("头像")
      private String avatar;

      @ApiModelProperty("院校描述")
      private String description;

      @ApiModelProperty("招生连接")
      private String link;


}
