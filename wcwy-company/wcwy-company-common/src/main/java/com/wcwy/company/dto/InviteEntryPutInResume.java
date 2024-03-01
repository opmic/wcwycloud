package com.wcwy.company.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 发送offer表
 * @TableName invite_entry
 */
@Data
@ApiModel("投简创建订单")
public class InviteEntryPutInResume {
    /**
     * 邀请入职表
     */
    @ApiModelProperty("邀请入职表")
    private String inviteEntryId;

    /**
     * 入职时间
     */
    @ApiModelProperty("入职时间")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate entryTime;

    /**
     * 赏金金额
     */
    @ApiModelProperty("税前薪资")
    private BigDecimal hiredBounty;

    /**
     * 入职年薪薪水
     */
    @ApiModelProperty("入职年薪薪水")
    private BigDecimal salary;

    /**
     * 佣金率
     */
    /*@ApiModelProperty("佣金率")
    private Integer percentage;*/


    /**
     * 第几个工作日/保证期天数
     */
    @ApiModelProperty(" 第几个工作日/保证期天数")
    private Integer workday;


    /**
     * 岗位发布类型(0普通改为 1:赏金岗位 2猎头岗位)
     */
    @ApiModelProperty("岗位发布类型(0普通改为 1:赏金岗位 2猎头岗位)")
    private Integer postType;



    /**
     * 投放简历id
     */

    @ApiModelProperty("投放简历id")
    private String putInResumeId;

    /**
     * 投放的岗位
     */

    @ApiModelProperty("投放的岗位")
    private String putInPost;

    /**
     * 投放的公司
     */

    @ApiModelProperty("投放的公司")
    private String putInComppany;

    /**
     * 求职者
     */

    @ApiModelProperty("求职者")
    private String putInJobhunter;


    /**
     * 投放人
     */
    @ApiModelProperty("投放人")
    private String putInUser;
/*    @TableField(value = "money_reward", typeHandler = JacksonTypeHandler.class)
    private Object moneyReward;*/
    @ApiModelProperty(value = "分享注册人")
    private String sharePerson;

    @ApiModelProperty(value = "佣金率")
    private Integer percentage;
}