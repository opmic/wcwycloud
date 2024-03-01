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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 投递简历表
 * @TableName put_in_resume
 */
@TableName(value ="put_in_resume",autoResultMap = true)
@Data
@ApiModel(value = "投递简历表")
public class PutInResume implements Serializable {
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

    /**
     * 求职者
     */
    @TableField(value = "put_in_jobhunter")
    @ApiModelProperty("求职者")
    private String putInJobhunter;

    /**
     * 投简说明
     */
    @TableField(value = "explains")
    @ApiModelProperty("投简说明")
    private String explains;

    /**
     * 人选意向
     */
    @TableField(value = "intention")
    @ApiModelProperty("人选意向")
    private String intention;

    /**
     * 到岗时间(1:离职-随时到岗 2:在职-2周内到岗 3:在职-1一个月到岗)
     */
    @TableField(value = "arrival_time")
    @ApiModelProperty("到岗时间(1:离职-随时到岗 2:在职-2周内到岗 3:在职-1一个月到岗)")
    private Integer arrivalTime;

    /**
     * 目前年薪
     */
    @TableField(value = "current_annual_salary")
    @ApiModelProperty("目前年薪")
    private BigDecimal currentAnnualSalary;

    /**
     * 期望谁前年薪
     */
    @TableField(value = "expect_annual_salary")
    @ApiModelProperty("期望谁前年薪")
    private String expectAnnualSalary;

    /**
     * 面试时间(1:可协商 2:提前1天通知 3:期望先电话或视频面试)
     */
    @TableField(value = "application_interview_time")
    @ApiModelProperty("面试时间(1:可协商 2:提前1天通知 3:期望先电话或视频面试)")
    private Integer applicationInterviewTime;

    /**
     * 投放人
     */
    @TableField(value = "put_in_user")
    @ApiModelProperty("投放人")
    private String putInUser;

    /**
     * 投放人
     */
    @TableField(value = "put_in_message")
    @ApiModelProperty("投简人消息处理状态(0:无消息 1:已处理) ")
    private Integer putInMessage;

    /**
     * 投放人
     */
    @TableField(value = "t_company_message")
    @ApiModelProperty("企业消息处理状态(0:无消息 1:已处理)")
    private Integer tCompanyMessage;

    /**
     * 是的代投(0:不是 1:是)
     */
    @TableField(value = "easco")
    @ApiModelProperty("是的代投(0:不是 1:是)")
    private String easco;

    /**
     * 投放状态(1:浏览、 2:下载、3排除  4:约面、5:面试中6淘汰、7:offer、8:入职、9:保证期)
     */
    @TableField(value = "resume_state")
    @ApiModelProperty("投放状态(1:浏览、 2:下载、3排除  4:约面、5:面试中 6取消面试 7淘汰、8:offer、9:入职、10:满月))")
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
     * 确认面试(0:未操作 1:确认面试)
     */
    @TableField(value = "affirm_interview")
    @ApiModelProperty("确认面试(0:未操作 1:确认面试)")
    private Integer affirmInterview;

    @TableField(value = "affirm_interview_time")
    @ApiModelProperty("面试时间")
    private LocalDateTime affirmInterviewTime;

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

    /**
     * 是否入职（0:未操作1：否 2：是）
     */
    @TableField(value = "entry_if")
    @ApiModelProperty("是否入职（0:未操作1：否 2：是）")
    private Integer entryIf;

    /**
     * 未入职原因
     */
    @TableField(value = "entry_cause")
    @ApiModelProperty("未入职原因")
    private String entryCause;

    /**
     * 入职时间
     */
    @TableField(value = "entry_time")
    @ApiModelProperty("入职时间")
    private LocalDateTime entryTime;

    /**
     * 是否过保(0:未操作1:否 2:是)
     */
    @TableField(value = "full_moon_if")
    @ApiModelProperty("是否满月(0:未操作1:否 2:是)")
    private Integer fullMoonIf;

    @TableField(value = "not_full_moon")
    @ApiModelProperty("未满月状态(1待确认 2客服介入 3未满月)")
    private Integer notFullMoon;
    /**
     * 未满月原因
     */
    @TableField(value = "full_moon_cause")
    @ApiModelProperty("未满月原因")
    private String fullMoonCause;


    @TableField(value = "materials",typeHandler = JacksonTypeHandler.class)
    @ApiModelProperty("未满月举证")
    private List<String> materials;

    /**
     * 离职时间
     */
    @TableField(value = "dimission_time")
    @ApiModelProperty("离职时间")
    private LocalDateTime dimissionTime;

    /**
     * 是否结算(0:未操作1:否 2:是)
     */
    @TableField(value = "close_an_account_if")
    @ApiModelProperty("是否结算(0:未操作1:否 2:结算中 3完成结算 )")
    private Integer closeAnAccountIf;

    /**
     * 结算开始日期
     */
    @TableField(value = "close_an_account_begin_time")
    @ApiModelProperty("结算开始日期")
    private LocalDateTime closeAnAccountBeginTime;

    /**
     * 结算完成日期
     */
    @TableField(value = "close_an_account_finish_time")
    @ApiModelProperty("结算完成日期")
    private LocalDateTime closeAnAccountFinishTime;

    /**
     * 未结算原因
     */
    @TableField(value = "close_an_account_cause")
    @ApiModelProperty("未结算原因")
    private String closeAnAccountCause;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    /**
     * 修改人
     */
    @TableField(value = "update_id")
    @ApiModelProperty("修改人")
    private String updateId;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
    /**
     * 更新时间
     */
    @TableField(value = "dispose_entry_time")
    @ApiModelProperty("处理入职时间")
    private LocalDateTime disposeEntryTime;
    /**
     * 更新时间
     */
    @TableField(value = "dispose_full_moon_time")
    @ApiModelProperty("处理满月时间")
    private LocalDateTime disposeFullMoonTime;
    /**
     * 企业是否删除
     */
    @TableField(value = "deleted_company")
    @ApiModelProperty("企业是否删除")
    private Integer deletedCompany;
    @TableField(value = "not_entry")
    @ApiModelProperty(value ="未入职状态(1:待确认2客服介入3:未入职) " )
    private Integer notEntry;
    /**
     * 投放人是否删除
     */
    @TableField(value = "deleted_put_in")
    @ApiModelProperty("投放人是否删除")
    private Integer deletedPutIn;



    @TableField(value = "inviter")
    @ApiModelProperty("职位推广邀请人")
    private String inviter;



}