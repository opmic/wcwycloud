package com.wcwy.company.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wcwy.company.po.UpdateEntryPO;
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
@TableName(value ="invite_entry")
@Data
@ApiModel("发送offer及修改入职申请")
public class InviteEntryDTO implements Serializable {
    /**
     * 邀请入职表
     */
    @ApiModelProperty("邀请入职表")
    private String inviteEntryId;

    /**
     * 合同路径
     */
    @ApiModelProperty("合同路径")
    private String contract;

    /**
     * offer路径
     */
    @ApiModelProperty("offer路径")
    private String offerPath;

    /**
     * 入职时间
     */
    @ApiModelProperty("入职时间")
    private LocalDate entryTime;

    /**
     * 入职岗位id
     */
    @ApiModelProperty("入职岗位id")
    private String postId;

    /**
     * 投简id
     */
    @ApiModelProperty("投简id")
    private String putInResumeId;

    /**
     * 赏金金额
     */
    @ApiModelProperty("赏金金额")
    private BigDecimal hiredBounty;

    /**
     * 入职年薪薪水
     */
    @ApiModelProperty("入职年薪薪水")
    private BigDecimal salary;

    /**
     * 入职取消申请(1:未发送取消申请 2:已发送取消申请)
     */
    @ApiModelProperty("入职取消申请(1:未发送取消申请 2:已发送取消申请)")
    private Integer cancel;


    @ApiModelProperty("offer状态(1:正常 2:取消)")
    private Integer stateIf;
    /**
     * 佣金率
     */
    @ApiModelProperty("佣金率")
    private Integer percentage;

    /**
     * 取消原因
     */
    @ApiModelProperty("取消原因")
    private String cancelCause;

    /**
     * 发送取消申请时间
     */
    @ApiModelProperty("发送取消申请时间")
    private LocalDateTime cancelTime;

    /**
     * 第几个工作日/保证期天数
     */
    @ApiModelProperty(" 第几个工作日/保证期天数")
    private Integer workday;

    /**
     * 投简人是否同意(0:未处理 1:不同意 2:已同意)
     */
    @ApiModelProperty(" 投简人是否同意(0:未处理 1:不同意 2:已同意)")
    private Integer putInConsent;

    /**
     * 岗位发布类型(0普通改为 1:赏金岗位 2猎头岗位)
     */
    @ApiModelProperty("岗位发布类型(0普通改为 1:赏金岗位 2猎头岗位)")
    private Integer postType;

    /**
     * 投简人修改时间
     */
    @ApiModelProperty("投简人修改时间")
    private LocalDateTime updateConsentTime;

    /**
     * 不同意原因
     */
    @ApiModelProperty("不同意原因")
    private String consentCause;

    @ApiModelProperty("修改入职申请")
    private UpdateEntryPO updateEntryPO;
}