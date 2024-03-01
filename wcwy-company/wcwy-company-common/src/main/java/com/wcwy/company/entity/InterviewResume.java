package com.wcwy.company.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.wcwy.company.po.DetailedAddressPO;
import com.wcwy.company.po.InterviewerPO;
import com.wcwy.company.po.InterviewerVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

/**
 * 面试邀请表
 * @TableName interview_resume
 */
@TableName(value ="interview_resume",autoResultMap = true)
@Data
@ApiModel(value = "面试邀请表")
public class InterviewResume implements Serializable {
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
    @ApiModelProperty(value = "面试方式(1线上,2现场)")
    private String interviewWay;

    /**
     * 面试平台(1:腾讯会议 2:微信视频 3:电话面试)
     */
    @TableField(value = "platform")
    @ApiModelProperty(value = "面试平台(1:腾讯会议 2:微信视频 3:电话面试)")
    private Integer platform;
    /**
     * 面试分钟
     */
    @TableField(value = "minute")
    @ApiModelProperty(value = "面试分钟")
    private Integer minute;

    @TableField(value = "remark_option",typeHandler = JacksonTypeHandler.class)
    @ApiModelProperty(value = "备注选项")
    private List<String> remarkOption;


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
    private List<InterviewerVO> interviewer;

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
    @TableField(value = "stage")
    @ApiModelProperty(value = "面试阶段(1:处理中 2:已接受 3:不接受 4取消面试 5面试未通过 6面试通过 7淘汰)")
    private Integer stage;
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
    @TableField(value = "state")
    @ApiModelProperty(value = "状态(1取消面试 2面试未通过 3面试通过 4淘汰)")
    private Integer state;

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
    @TableField(value = "state_cause")
    @ApiModelProperty(value = "状态原因")
    private String stateCause;

    /**
     * 取消时间
     */
    @TableField(value = "state_time")
    @ApiModelProperty(value = "状态操作时间")
    private LocalDateTime stateTime;


}