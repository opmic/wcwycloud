package com.wcwy.company.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 面试邀请表
 * @TableName interview_resume
 */

@Data
@ApiModel("修改面试邀请")
public class InterviewResumeUpdateVO {
    /**
     * 面试id
     */
    @ApiModelProperty("面试id")
    private String interviewId;

    /**
     * 面试方式(视频,电话,现场)
     */
    @ApiModelProperty("面试方式(视频,电话,现场)")
    private String interviewWay;

    /**
     * 备注
     */

    @ApiModelProperty("备注")
    private String remark;

    /**
     * 面试地点
     */
    @ApiModelProperty("面试地点")
    private String place;

    /**
     * 对接人
     */
    @ApiModelProperty("对接人")
    private String onAccess;

    /**
     * 面试对接电话
     */
    @ApiModelProperty("面试对接电话")
    private String phone;


    /**
     * 修改面试的时间
     */

    @ApiModelProperty("修改面试的时间")
    private LocalDateTime updateInterviewTime;

    /**
     * 修改面试时间原因
     */
    @ApiModelProperty("修改面试时间原因")
    private String updateInterviewCause;

    /**
     * 修改人
     */
    @ApiModelProperty("修改人")
    private String updateUser;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;

    /**
     * 审核状态(1:审核中 2:审核失败 3:审核失败)
     */
    @ApiModelProperty(" 审核状态(1:审核中 2:审核失败 3:审核失败)")
    private Integer auditState;

    /**
     * 审核失败原因
     */

    @ApiModelProperty("审核失败原因")
    private String auditCause;

    /**
     * 审核人
     */
    @ApiModelProperty("审核人")
    private String auditUser;

    /**
     * 审核时间
     */
    @ApiModelProperty("审核时间")
    private LocalDateTime auditTime;


}