package com.wcwy.company.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.wcwy.company.entity.TJobhunterEducationRecord;
import com.wcwy.company.entity.TJobhunterExpectPosition;
import com.wcwy.company.entity.TJobhunterProjectRecord;
import com.wcwy.company.entity.TJobhunterWorkRecord;
import com.wcwy.company.po.TJobhunterPO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 求职者简历
 * @TableName t_jobhunter_resume
 */
@ApiModel("求职者简历详细信息")
@Data
public class DetailedTJobhunterResumeDTO implements Serializable {
    /**
     * 简历id
     */
    @ApiModelProperty("简历id")
    private String resumeId;
    /**
     * 简历名称
     */
    @ApiModelProperty("简历名称")
    private String resumeName;

    /**
     * 优势亮点
     */
    @ApiModelProperty("优势亮点")
    private List<String> advantage;

    /**
     * 简历审核状态(0待审核1审核通过2审核不通过)
     */
    @ApiModelProperty("简历审核状态")
    private Integer resumeExamineStatus;

    /**
     * 审核意见
     */
    @ApiModelProperty("审核意见")
    private String resumeExamineResult;


    @ApiModelProperty(value = "期望职位")
    private List<TJobhunterExpectPosition> tJobhunterExpectPosition;

    @ApiModelProperty(value = "工作经历")
    private List<TJobhunterWorkRecord> tJobhunterWorkRecord;

    @ApiModelProperty("项目经历")
    private List<TJobhunterProjectRecord> tJobhunterProjectRecord;

    @ApiModelProperty("教育经历")
    private List<TJobhunterEducationRecord> tJobhunterEducationRecord;

    @ApiModelProperty("求职者用户表")
    private TJobhunterPO tJobhunterPO;

    @ApiModelProperty("企业是否有权查看详细信息")
    public Boolean viewDetails;

}