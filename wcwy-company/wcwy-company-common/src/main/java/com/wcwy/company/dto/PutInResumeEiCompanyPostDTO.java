package com.wcwy.company.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.wcwy.company.entity.InterviewResume;
import com.wcwy.company.po.ProvincesCitiesPO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * ClassName: PutInResumeEiCompanyPostDTO
 * Description:
 * date: 2023/4/4 9:29
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel(value = "求职者查看我投的岗位")
public class PutInResumeEiCompanyPostDTO {
    /**
     * 投放简历id
     */
    @TableId(value = "put_in_resume_id")
    @ApiModelProperty("投放简历id")
    private String putInResumeId;

    /**
     * 投放的岗位
     */
    @TableField(value = "put_in_post")
    @ApiModelProperty("投放的岗位")
    private String putInPost;

    /**
     * 投放的公司
     */
    @TableField(value = "put_in_comppany")
    @ApiModelProperty("投放的公司")
    private String putInComppany;
    @ApiModelProperty(value = "企业属性(0招聘企业 1猎头企业 2推荐官猎企)")
    private String companyType;
    /**
     * 投放人
     */
    @TableField(value = "put_in_message")
    @ApiModelProperty("投简人消息处理状态(0:无消息 1:已处理) ")
    private Integer putInMessage;

    /**
     * 投放状态(1:浏览、 2:下载、3排除  4:约面、5:面试中6淘汰、7:offer、8:入职、9:保证期)
     */
    @TableField(value = "resume_state")
    @ApiModelProperty("投放状态(1:浏览、 2:下载、3排除  4:约面、5:面试中 6取消面试 7淘汰、8:offer、9:入职、10:保证期)")
    private Integer resumeState;

    /**
     * 是否浏览（0:未操作1：否 2：是）
     */
    @TableField(value = "browse_if")
    @ApiModelProperty("是否浏览（0:未操作1：否 2：是）")
    private Integer browseIf;

    /**
     * 浏览时间
     */
    @TableField(value = "browse_time")
    @ApiModelProperty("浏览时间")
    private LocalDateTime browseTime;

    /**
     * 是否下载（0:未操作1：否 2：是）
     */
    @TableField(value = "download_if")
    @ApiModelProperty("是否下载（0:未操作1：否 2：是）")
    private Integer downloadIf;

    /**
     * 下载时间
     */
    @TableField(value = "download_time")
    @ApiModelProperty("下载时间")
    private LocalDateTime downloadTime;

    /**
     * 是否排除（0:未操作1：否 2：是）
     */
    @TableField(value = "exclude_if")
    @ApiModelProperty("是否排除（0:未操作1：否 2：是）")
    private Integer excludeIf;

    /**
     * 排除时间
     */
    @TableField(value = "exclude_time")
    @ApiModelProperty("排除时间")
    private LocalDateTime excludeTime;

    /**
     * 排除原因
     */
    @TableField(value = "exclude_state")
    @ApiModelProperty("排除原因")
    private String excludeState;

    /**
     * 是否预约（0:未操作1：否 2：是）
     */
    @TableField(value = "subscribe_if")
    @ApiModelProperty("是否预约（0:未操作1：否 2：是）")
    private Integer subscribeIf;

    /**
     * 是否接受预约（0:未操作1：否 2：是）
     */
    @TableField(value = "accept_subscribe")
    @ApiModelProperty("是否接受预约（0:未操作1：否 2：是）")
    private Integer acceptSubscribe;

    /**
     * 邀请反馈模板
     */
    @TableField(value = "invitation")
    @ApiModelProperty("邀请反馈模板")
    private String invitation;

    /**
     * 不接受原因
     */
    @TableField(value = "accept_subscribe_state")
    @ApiModelProperty("不接受原因")
    private String acceptSubscribeState;

    /**
     * 预约时间
     */
    @TableField(value = "subscribe_time")
    @ApiModelProperty("预约时间")
    private LocalDateTime subscribeTime;

    /**
     * 是否面试中（0:未操作1：否 2：是）
     */
    @TableField(value = "interview_if")
    @ApiModelProperty("是否面试中（0:未操作1：否 2：是）")
    private Integer interviewIf;

    /**
     * 面试时间
     */
    @TableField(value = "interview_time")
    @ApiModelProperty("面试时间")
    private LocalDateTime interviewTime;

    /**
     * 是否取消(0:未操作1：否 2：是)
     */
    @TableField(value = "cancel_if")
    @ApiModelProperty("是否取消(0:未操作1：否 2：是)")
    private Integer cancelIf;

    /**
     * 取消时间
     */
    @TableField(value = "cancel_time")
    @ApiModelProperty("取消时间")
    private LocalDateTime cancelTime;

    /**
     * 取消原因
     */
    @TableField(value = "cancel_cause")
    @ApiModelProperty("取消原因")
    private Integer cancelCause;

    /**
     * 是否淘汰（0:未操作1：否 2：是）
     */
    @TableField(value = "weed_out_if")
    @ApiModelProperty("是否淘汰（0:未操作1：否 2：是）")
    private Integer weedOutIf;

    /**
     * 淘汰时间
     */
    @TableField(value = "weed_out_time")
    @ApiModelProperty("淘汰时间")
    private LocalDateTime weedOutTime;

    /**
     * 淘汰原因
     */
    @TableField(value = "weed_out_cause")
    @ApiModelProperty("淘汰原因")
    private String weedOutCause;

    /**
     * 是否发送offer（0:未操作1：否 2：是）
     */
    @TableField(value = "offer_if")
    @ApiModelProperty("是否发送offer（0:未操作1：否 2：是）")
    private Integer offerIf;

    /**
     * 发送时间
     */
    @TableField(value = "offer_time")
    @ApiModelProperty("发送时间")
    private LocalDateTime offerTime;


    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    /**
     * 岗位开始薪资
     */
    @TableField(value = "begin_salary")
    private BigDecimal beginSalary;

    /**
     * 岗位结束薪资
     */
    @TableField(value = "end_salary")
    private BigDecimal endSalary;

    /**
     * 所在城市
     */
    @TableField(value = "work_city",typeHandler = JacksonTypeHandler.class)
    private ProvincesCitiesPO workCity;
    @ApiModelProperty(value = "企业名称")
    private String companyName;
    @ApiModelProperty(value = "岗位名称")
    private String postLabel;
    @ApiModelProperty(value = "面试邀请表")
    private List<InterviewResume> interview;
    @TableField(value = "dispose_entry_time")
    @ApiModelProperty("处理入职时间")
    private LocalDateTime disposeEntryTime;
    /**
     * 更新时间
     */
    @TableField(value = "dispose_full_moon_time")
    @ApiModelProperty("处理满月时间")
    private LocalDateTime disposeFullMoonTime;
}
