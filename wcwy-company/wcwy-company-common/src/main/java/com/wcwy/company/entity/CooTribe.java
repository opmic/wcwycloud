package com.wcwy.company.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * @TableName coo_tribe
 */
@TableName(value ="coo_tribe")
@Data
public class CooTribe implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("发帖id")
    private Long id;

    /**
     * 标题
     */
    @TableField(value = "title")
    @ApiModelProperty("标题")
    private String title;

    /**
     * 省
     */
    @TableField(value = "province")
    @ApiModelProperty("省")
    private String province;

    /**
     * 类型(0:文章 1:问答 2:心得 3回答)
     */
    @TableField(value = "type")
    @ApiModelProperty("类型(0:文章 1:问答 2:心得 3回答)")
    private Integer type;

    /**
     * 父亲id
     */
    @TableField(value = "father")
    @ApiModelProperty("父亲id")
    private Long father;

    /**
     * 文案
     */
    @TableField(value = "copy_writer")
    @ApiModelProperty("文案")
    private String copyWriter;

    /**
     * 封面
     */
    @TableField(value = "cover")
    @ApiModelProperty("封面")
    private String cover;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    /**
     * 在线(0:在线 1:下线)
     */
    @TableField(value = "on_line")
    @ApiModelProperty("在线(0:在线 1:下线)")
    private Integer onLine;

    /**
     * 审核(0:审核中 1:审核成功 2审核失败)
     */
    @TableField(value = "audit")
    @ApiModelProperty("审核(0:审核中 1:审核成功 2审核失败)")
    private Integer audit;

    /**
     * 失败原因
     */
    @TableField(value = "cause_of_failure")
    @ApiModelProperty("失败原因")
    private String causeOfFailure;
    /**
     * 失败原因
     */
    @TableField(value = "pseudonym")
    @ApiModelProperty("笔名")
    private String pseudonym;
    /**
     * 创建人
     */
    @TableField(value = "user_id")
    @ApiModelProperty("创建人")
    private String userId;

    /**
     * 浏览量
     */
    @TableField(value = "browse")
    @ApiModelProperty("浏览量")
    private Long browse;

    /**
     * 点赞量
     */
    @TableField(value = "zan")
    @ApiModelProperty("点赞量")
    private Long zan;

    /**
     * 评论量
     */
    @TableField(value = "comment")
    @ApiModelProperty("评论量")
    private Long comment;

    /**
     * 分享量
     */
    @TableField(value = "share")
    @ApiModelProperty("分享量")
    private Long share;

    /**
     * 收藏量
     */
    @TableField(value = "collect")
    @ApiModelProperty("收藏量")
    private Long collect;



}