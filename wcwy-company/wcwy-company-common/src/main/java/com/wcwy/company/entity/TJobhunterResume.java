package com.wcwy.company.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 求职者简历
 * @TableName t_jobhunter_resume
 */
@TableName(value ="t_jobhunter_resume",autoResultMap = true)
@Data
@ApiModel("求职者简历")
public class TJobhunterResume implements Serializable {
    /**
     * 简历id
     */
    @TableId(value = "resume_id")
    @ApiModelProperty("简历id")
    private String resumeId;

    /**
     * 求职者id
     */
    @TableField(value = "t_jobhunter_id")
    @ApiModelProperty("求职者id")
    private String jobhunterId;

    /**
     * 简历名称
     */
    @TableField(value = "resume_name")
    @ApiModelProperty("简历名称")
    private String resumeName;

    /**
     * 优势亮点
     */
    @TableField(value = "advantage",typeHandler = JacksonTypeHandler.class)
    @ApiModelProperty("优势亮点")
    private List<String> advantage;

    /**
     * 简历审核状态(0待审核1审核通过2审核不通过)
     */
    @TableField(value = "resume_examine_status")
    @ApiModelProperty("简历审核状态")
    private Integer resumeExamineStatus;

    /**
     * 审核意见
     */
    @TableField(value = "resume_examine_result")
    @ApiModelProperty("审核意见")
    private String resumeExamineResult;

    /**
     * 是否默认(1:是2:否)
     */
    @TableField(value = "resume")
    @ApiModelProperty("是否默认(1:是2:否)")
    private Integer resume;

    /**
     * 是否能删除（1不能 2能）
     */
    @TableField(value = "deleted")
    @ApiModelProperty("是否能删除（1不能 2能）")
    private Integer deleted;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;


}