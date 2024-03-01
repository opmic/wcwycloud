package com.wcwy.company.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.wcwy.company.po.DetailedAddressPO;
import com.wcwy.company.po.InterviewerVO;
import com.wcwy.company.po.ProvincesCitiesPO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 面试邀请表
 * @TableName interview_resume
 */
@Data
@ApiModel(value = "求职者-我的面试")
public class InterviewResumePostDTO implements Serializable {
    /**
     * 面试id
     */

    @ApiModelProperty(value = "面试id")
    private String interviewId;

    /**
     * 简历投递id
     */
    @ApiModelProperty(value = "简历投递id")
    private String putInResumeId;

    /**
     * 面试时间
     */
    @ApiModelProperty(value = "面试时间")
    private LocalDateTime interviewTime;

    /**
     * 面试方式(视频,电话,现场)
     */
    @ApiModelProperty("面试方式(1线上,2现场)")
    private String interviewWay;

    /**
     * 面试平台(1:腾讯会议 2:微信视频 3:电话面试)
     */
    @ApiModelProperty(value = "面试平台(1:腾讯会议 2:微信视频 3:电话面试)")
    private Integer platform;
    /**
     * 面试分钟
     */
    @ApiModelProperty(value = "面试分钟")
    private Integer minute;

    @ApiModelProperty(value = "备注选项")
    private List<String> remarkOption;


    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 面试地点
     */
    @ApiModelProperty(value = "面试地点")
    private DetailedAddressPO place;

    /**
     * 对接人
     */
    @ApiModelProperty(value = "对接人")
    private String onAccess;

    /**
     * 面试官
     */
    @ApiModelProperty(value = "面试官")
    private List<InterviewerVO> interviewer;

    /**
     * 面试对接电话
     */
    @ApiModelProperty(value = "面试对接电话")
    private String phone;

    /**
     * 是否接受面试(1:处理中 2:已接受 3:不接受)
     */

    @ApiModelProperty(value = " 是否接受面试(1:处理中 2:已接受 3:不接受)")
    private Integer take;
    @ApiModelProperty(value = "面试阶段(1:处理中 2:已接受 3:不接受 4取消面试 5面试未通过 6面试通过 7淘汰)")
    private Integer stage;
    /**
     * 建议修改时间
     */
    @ApiModelProperty(value = "建议修改时间")
    private LocalDateTime suggestTime;

    /**
     * 不接受原因
     */
    @ApiModelProperty(value = "不接受原因")
    private String noTakeCause;

    /**
     * 企业是否同意（1:处理中 2:同意 3:不同意）
     */
    @ApiModelProperty(value = "企业是否同意（1:处理中 2:同意 3:不同意）")
    private Integer accept;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createUser;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    /**
     * 修改面试的时间
     */
    @ApiModelProperty(value = "修改面试的时间")
    private LocalDateTime updateInterviewTime;

    /**
     * 修改面试时间原因
     */
    @ApiModelProperty(value = "修改面试时间原因")
    private String updateInterviewCause;

    /**
     * 修改面试地点
     */
    @ApiModelProperty(value = "修改面试地点")
    private String updatePlace;

    /**
     * 修改备注
     */
    @ApiModelProperty(value = "修改备注")
    private String updateRemark;

    /**
     * 面试方式(视频,电话,现场)
     */
    @ApiModelProperty(value = "面试方式(视频,电话,现场)")
    private String updateInterviewWay;

    /**
     * 修改对接人
     */
    @ApiModelProperty(value = "修改对接人")
    private String updateOnAccess;

    /**
     * 修改面试对接电话
     */
    @ApiModelProperty(value = "修改面试对接电话")
    private String updatePhone;

    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人")
    private String updateUser;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;

    /**
     * 修改状态(1:处理中 2:同意修改 3:不同意修改)
     */
    @ApiModelProperty(value = "修改状态(1:处理中 2:同意修改 3:不同意修改)")
    private Integer auditState;

    /**
     * 不同意修改原因
     */
    @ApiModelProperty(value = "不同意修改原因")
    private String auditCause;

    /**
     * 处理人
     */
    @ApiModelProperty(value = "处理人")
    private String auditUser;

    /**
     * 处理时间
     */
    @ApiModelProperty(value = "处理时间")
    private LocalDateTime auditTime;

    /**
     * 是否取消
     */
    @ApiModelProperty(value = "状态(1取消面试 2面试未通过 3面试通过 4淘汰)")
    private Integer state;

    /**
     * 逻辑删除
     */
    @ApiModelProperty(value = "逻辑删除")
    private Integer deleted;
    /**
     * 企业是否同意（1:处理中 2:同意 3:不同意）
     */
    @ApiModelProperty(value = "完成状态(0:未完成 1已完成)")
    private Integer completionStatus;
    /**
     * 取消原因
     */
    @ApiModelProperty(value = "状态原因")
    private String stateCause;

    /**
     * 取消时间
     */
    @ApiModelProperty(value = "状态操作时间")
    private LocalDateTime stateTime;

    /**
     * 岗位id
     */
    @ApiModelProperty(value = "岗位id")
    private String postId;

    /**
     * 企业id
     */
    @ApiModelProperty(value = "企业id")
    private String companyId;

    /**
     * 岗位开始薪资
     */
    @ApiModelProperty(value = "岗位开始薪资")
    private BigDecimal beginSalary;

    /**
     * 岗位结束薪资
     */
    @ApiModelProperty(value = "岗位结束薪资")
    private BigDecimal endSalary;

    /**
     * 所在城市
     */
    @ApiModelProperty(value = "所在城市")
    private ProvincesCitiesPO workCity;
    /**
     * 岗位名称
     */
    @ApiModelProperty(value = "岗位名称")
    private String postLabel;
    @ApiModelProperty(value = "岗位发布类型(0普通改为 1:赏金岗位 2猎头岗位 3到面付)")
    private Integer postType;
    @ApiModelProperty(value = "企业名称")
    private String companyName;
}