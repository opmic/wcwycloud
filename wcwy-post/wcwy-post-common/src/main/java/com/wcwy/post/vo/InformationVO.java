package com.wcwy.post.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 资讯
 * @TableName information
 */
@Data
@ApiModel(value = "资讯实体类")
public class InformationVO {
    @ApiModelProperty(value = "资讯id(注意:填入则更新 )")
    private String informationId;
    /**
     * 咨询内容
     */
    @ApiModelProperty(value = "咨询内容")
    @NotBlank(message = "咨询内容不能为空")
    private String content;

    /**
     * 咨询名称
     */
    @ApiModelProperty(value = "咨询名称")
    @NotBlank(message = "咨询名称不能为空")
    private String name;

    /**
     * 话题(1数据增长动态 2:职场资讯 3:公司动态)
     */
    @ApiModelProperty(value = "话题(1数据增长动态 2:职场资讯 3:公司动态)")
    @NotNull(message = "话题不能为空")
    private Integer theme;

    /**
     * 海报
     */
    @ApiModelProperty(value = "海报")
    @NotBlank(message = "海报不能为空")
    private String poster;

    /**
     * 二级话题
     */
    @ApiModelProperty(value = "二级话题")
    @NotBlank(message = "二级话题不能为空")
    private String topic;


}