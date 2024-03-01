package com.wcwy.post.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 资讯
 * @TableName information
 */
@TableName(value ="information")
@Data
@ApiModel(value = "资讯实体类")
public class Information implements Serializable {
    /**
     * 资讯id
     */
    @TableId(value = "information_id")
    @ApiModelProperty(value = "资讯id")
    private String informationId;

    /**
     * 咨询内容
     */
    @TableField(value = "content")
    @ApiModelProperty(value = "咨询内容")
    private String content;

    /**
     * 咨询名称
     */
    @TableField(value = "name")
    @ApiModelProperty(value = "咨询名称")
    private String name;

    /**
     * 话题(1数据增长动态 2:职场资讯 3:公司动态)
     */
    @TableField(value = "theme")
    @ApiModelProperty(value = "话题(1数据增长动态 2:职场资讯 3:公司动态)")
    private Integer theme;

    /**
     * 海报
     */
    @TableField(value = "poster")
    @ApiModelProperty(value = "海报")
    private String poster;

    /**
     * 二级话题
     */
    @TableField(value = "topic")
    @ApiModelProperty(value = "二级话题")
    private String topic;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    /**
     * 逻辑删除
     */
    @TableField(value = "deleted")
    @ApiModelProperty(value = "逻辑删除")
    private Integer deleted;


}