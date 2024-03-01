package com.wcwy.company.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wcwy.company.po.DetailedAddressPO;
import com.wcwy.company.po.InterviewerVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 面试邀请表
 * @TableName interview_resume
 */
@Data
@ApiModel("企业修改面试邀请表")
public class UpdateInterviewResume  {
    /**
     * 面试id
     */
    @ApiModelProperty("面试id")
    @NotBlank(message = "请填写面试id!")
    private String interviewId;
    /**
     * 面试时间
     */
    @ApiModelProperty("面试时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    @Future(message = "时间不能小于今天")
    private LocalDateTime interviewTime;

    /**
     * 面试方式(线上,现场)
     */
    @ApiModelProperty(value = "修改面试时间原因")
    @NotBlank(message = "修改原因不能为空！")
    private String updateInterviewCause;
    @ApiModelProperty("面试方式(1线上,2现场)")
    @NotBlank(message = "面试方式不能为空")
    private String interviewWay;
    @ApiModelProperty("面试平台(1:腾讯会议 2:微信视频 3:电话面试)")
    private Integer platform;

    @ApiModelProperty("面试官")
    private List<InterviewerVO> interviewer;

    @ApiModelProperty(value = "面试分钟")
    private Integer minute;

    @ApiModelProperty(value = "备注选项")
    private List<String> remarkOption;
    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 面试地点
     */
    @ApiModelProperty("面试地点")
    private DetailedAddressPO place;

    /**
     * 对接人
     */
    @ApiModelProperty("面试联系人")
    @NotBlank(message = "面试联系人不能为空!")
    private String onAccess;

    /**
     * 面试对接电话
     */
    @ApiModelProperty("联系方式")
    @NotBlank(message = "联系方式不能为空!")
    private String phone;

    @ApiModelProperty("短信提醒(0:不开启 1:已开启)")
    @NotNull(message = "短信提醒选择不能为空!")
    public  Integer remind;



}