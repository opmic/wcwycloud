package com.wcwy.company.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 推荐人数据记录
 * @TableName referrer_record
 */
@TableName(value ="referrer_record")
@Data
@ApiModel(value = "推荐人数据记录")
public class ReferrerRecord implements Serializable {
    /**
     * 推荐人记录id
     */
    @TableId(value = "referrer_record_id", type = IdType.AUTO)
    @ApiModelProperty(value = "推荐人记录id")
    private Long referrerRecordId;

    /**
     *  求职者id
     */
    @TableField(value = "t_job_hunter_id")
    @ApiModelProperty(value = "求职者id")
    private String tJobHunterId;

    /**
     * 推荐官id
     */
    @TableField(value = "recommend_id")
    @ApiModelProperty(value = "推荐官id")
    private String recommendId;


    /**
     * 推荐官id
     */
    @TableField(value = "correlation_type")
    @ApiModelProperty(value = "人才来源(0:分享引流 1:人才委托 2新增人才 3应聘简历4:分享投递)")
    private Integer correlationType;
    @TableField(value = "origin")
    @ApiModelProperty(value = "引入来源(0:用户注册推广 1:职位推广)")
    private Integer origin;




    /**
     * 推荐官id
     */
    @TableField(value = "download_if")
    @ApiModelProperty(value = "是否下载(0:未下载 1:已下载)")
    private Integer downloadIf;
    /**
     * 推荐官id
     */
    @TableField(value = "download_time")
    @ApiModelProperty(value = "创建时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime downloadTime;
    /**
     * 我的推荐
     */
    @TableField(value = "referrer")
    @ApiModelProperty(value = "我的推荐")
    private Long referrer;

    /**
     * 浏览
     */
    @TableField(value = "browse")
    @ApiModelProperty(value = "浏览")
    private Long browse;

    /**
     * 约面
     */
    @TableField(value = "appoint")
    @ApiModelProperty(value = "约面")
    private Long appoint;

    /**
     * 面试
     */
    @TableField(value = "interview")
    @ApiModelProperty(value = "面试")
    private Long interview;

    /**
     * offer
     */
    @TableField(value = "offer")
    @ApiModelProperty(value = "offer")
    private Long offer;

    /**
     * 入职
     */
    @TableField(value = "entry")
    @ApiModelProperty(value = "入职")
    private Long entry;

    /**
     * 淘汰
     */
    @TableField(value = "weed_out")
    @ApiModelProperty(value = "淘汰")
    private Long weedOut;

    /**
     * 待反馈
     */
    @TableField(value = "feedback")
    @ApiModelProperty(value = "待反馈")
    private Long feedback;

    /**
     * 逻辑删除
     */
    @TableField(value = "deleted")
    @ApiModelProperty(value = "逻辑删除")
    private Integer deleted;


    @TableField(value = "create_time")
    @ApiModelProperty(value = "创建时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createTime;


    @TableField(value = "delete_time")
    @ApiModelProperty(value = "退出时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime deleteTime;
    @ApiModelProperty(value = "推荐报告")
    @TableField(value = "report",typeHandler = JacksonTypeHandler.class)
    private Object report ;


    @TableField(value = "money")
    @ApiModelProperty("下载金额")
    private BigDecimal money;

    @TableField(value = "type")
    @ApiModelProperty(value = "类型(0:人民币 1:金币)")
    private Integer type;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


}