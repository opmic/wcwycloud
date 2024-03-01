package com.wcwy.post.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 发布岗位纪录表
 *
 * @TableName t_post_share
 */
@Data
@ApiModel(value = "总和发布岗位纪录表")
public class TotalPostShare {

    @ApiModelProperty(value = "热度")
    private Long flow;

    /**
     * 推荐次数
     */
    @ApiModelProperty(value = "推荐次数")
    private Long shareSize;

    /**
     * 下载次数
     */
    @ApiModelProperty(value = "下载次数")
    private Long downloadSize;

    /**
     * 浏览次数
     */
    @ApiModelProperty(value = "浏览次数")
    private Long browseSize;

    /**
     * 面试人数
     */
    @ApiModelProperty(value = "面试人数")
    private Long interviewSize;

    /**
     * 入职人数
     */
    @ApiModelProperty(value = "入职人数")
    private Long entrySize;
    /**
     * 淘汰人数
     */
    @ApiModelProperty(value = "淘汰人数")
    private Long weedOut;
    /**
     * 预约面试
     */
    @ApiModelProperty(value = "预约面试")
    private Long subscribe;

    /**
     * offer数量
     */
    @ApiModelProperty(value = "offer数量")
    private Long offerSize;

    /**
     * 过保数量
     */
  /*  @ApiModelProperty(value = "过保数量")
    private Long overInsured;*/


    @ApiModelProperty(value = "在线职位")
    public Long onLinePost;
}