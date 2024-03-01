package com.wcwy.company.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * ClassName: InterviewResumePO
 * Description:
 * date: 2023/4/7 16:31
 *
 * @author tangzhuo
 * @since JDK 1.8
 */

@Data
@ApiModel(value = "企业获取面试记录")
public class InterviewResumePO {
    /**
     * 面试id
     */
    @TableId(value = "interview_id")
    @ApiModelProperty(value = "面试id")
    private String interviewId;

    /**
     * 简历投递id
     */
    @TableField(value = "put_in_resume_id")
    @ApiModelProperty(value = "简历投递id")
    private String putInResumeId;

    /**
     * 面试时间
     */
    @TableField(value = "interview_time")
    @ApiModelProperty(value = "面试时间")
    private LocalDateTime interviewTime;

    /**
     * 面试方式(视频,电话,现场)
     */
    @TableField(value = "interview_way")
    @ApiModelProperty(value = "面试方式(视频,电话,现场)")
    private String interviewWay;

    /**
     * 面试平台(1:腾讯会议 2:微信视频 3:电话面试)
     */
    @TableField(value = "platform")
    @ApiModelProperty(value = "面试平台(1:腾讯会议 2:微信视频 3:电话面试)")
    private Integer platform;

    /**
     * 备注
     */
    @TableField(value = "remark")
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 面试地点
     */
    @TableField(value = "place",typeHandler = JacksonTypeHandler.class)
    @ApiModelProperty(value = "面试地点")
    private DetailedAddressPO place;

    /**
     * 对接人
     */
    @TableField(value = "on_access")
    @ApiModelProperty(value = "对接人")
    private String onAccess;

    /**
     * 面试官
     */
    @TableField(value = "interviewer",typeHandler = JacksonTypeHandler.class)
    @ApiModelProperty(value = "面试官")
    private InterviewerPO interviewer;

    /**
     * 面试对接电话
     */
    @TableField(value = "phone")
    @ApiModelProperty(value = "面试对接电话")
    private String phone;

    /**
     * 是否接受面试(1:处理中 2:已接受 3:不接受)
     */
    @TableField(value = "take")
    @ApiModelProperty(value = " 是否接受面试(1:处理中 2:已接受 3:不接受)")
    private Integer take;

    /**
     * 建议修改时间
     */
    @TableField(value = "suggest_time")
    @ApiModelProperty(value = "建议修改时间")
    private LocalDateTime suggestTime;

    /**
     * 不接受原因
     */
    @TableField(value = "no_take_cause")
    @ApiModelProperty(value = "不接受原因")
    private String noTakeCause;

    /**
     * 企业是否同意（1:处理中 2:同意 3:不同意）
     */
    @TableField(value = "accept")
    @ApiModelProperty(value = "企业是否同意（1:处理中 2:同意 3:不同意）")
    private Integer accept;

    /**
     * 创建人
     */
    @TableField(value = "create_user")
    @ApiModelProperty(value = "创建人")
    private String createUser;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    /**
     * 修改面试的时间
     */
    @TableField(value = "update_interview_time")
    @ApiModelProperty(value = "修改面试的时间")
    private LocalDateTime updateInterviewTime;

    /**
     * 修改面试时间原因
     */
    @TableField(value = "update_interview_cause")
    @ApiModelProperty(value = "修改面试时间原因")
    private String updateInterviewCause;

    /**
     * 修改面试地点
     */
    @TableField(value = "update_place")
    @ApiModelProperty(value = "修改面试地点")
    private String updatePlace;

    /**
     * 修改备注
     */
    @TableField(value = "update_remark")
    @ApiModelProperty(value = "修改备注")
    private String updateRemark;

    /**
     * 面试方式(视频,电话,现场)
     */
    @TableField(value = "update_interview_way")
    @ApiModelProperty(value = "面试方式(视频,电话,现场)")
    private String updateInterviewWay;

    /**
     * 修改对接人
     */
    @TableField(value = "update_on_access")
    @ApiModelProperty(value = "修改对接人")
    private String updateOnAccess;

    /**
     * 修改面试对接电话
     */
    @TableField(value = "update_phone")
    @ApiModelProperty(value = "修改面试对接电话")
    private String updatePhone;

    /**
     * 修改人
     */
    @TableField(value = "update_user")
    @ApiModelProperty(value = "修改人")
    private String updateUser;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;

    /**
     * 修改状态(1:处理中 2:同意修改 3:不同意修改)
     */
    @TableField(value = "audit_state")
    @ApiModelProperty(value = "修改状态(1:处理中 2:同意修改 3:不同意修改)")
    private Integer auditState;

    /**
     * 不同意修改原因
     */
    @TableField(value = "audit_cause")
    @ApiModelProperty(value = "不同意修改原因")
    private String auditCause;

    /**
     * 处理人
     */
    @TableField(value = "audit_user")
    @ApiModelProperty(value = "处理人")
    private String auditUser;

    /**
     * 处理时间
     */
    @TableField(value = "audit_time")
    @ApiModelProperty(value = "处理时间")
    private LocalDateTime auditTime;

    /**
     * 是否取消
     */
    @TableField(value = "cancel_if")
    @ApiModelProperty(value = "是否取消")
    private Integer cancelIf;

    /**
     * 逻辑删除
     */
    @TableField(value = "deleted")
    @ApiModelProperty(value = "逻辑删除")
    private Integer deleted;
    /**
     * 企业是否同意（1:处理中 2:同意 3:不同意）
     */
    @TableField(value = "completion_status")
    @ApiModelProperty(value = "完成状态(0:未完成 1已完成)")
    private Integer completionStatus;
    /**
     * 取消原因
     */
    @TableField(value = "cancel_cause")
    @ApiModelProperty(value = "取消原因")
    private Integer cancelCause;

    /**
     * 取消时间
     */
    @TableField(value = "cancel_time")
    @ApiModelProperty(value = "取消时间")
    private LocalDateTime cancelTime;

}
