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
 * 工作经历表
 * @TableName t_jobhunter_work_record
 */
@TableName(value ="t_jobhunter_work_record")
@Data
@ApiModel(value = "工作经历表")
public class TJobhunterWorkRecord implements Serializable {
    /**
     * 主键Id
     */
    @TableId(value = "work_id")
    @ApiModelProperty(value ="主键Id")
    private String workId;

    /**
     * 简历Id
     */
    @TableField(value = "resume_id")
    @ApiModelProperty(value ="简历Id")
    private String resumeId;

    /**
     * 公司名称
     */
    @TableField(value = "company_name")
    @ApiModelProperty(value ="公司名称")
    private String companyName;

    /**
     * 所属行业
     */
    @TableField(value = "industry",typeHandler = JacksonTypeHandler.class)
    @ApiModelProperty(value ="所属行业")
    private List<String> industry;

    /**
     * 所属部门
     */
    @TableField(value = "department")
    @ApiModelProperty(value ="所属部门")
    private String department;


    @TableField(value = "position")
    @ApiModelProperty(value ="职位类型")
    private String position;

    @TableField(value = "job_description")
    @ApiModelProperty(value ="工作内容")
    private String jobDescription;

    /**
     * 职位名称
     */
    @TableField(value = "position_name")
    @ApiModelProperty(value ="职位名称")
    private String positionName;

    /**
     * 在职开始时间
     */
    @TableField(value = "start_time")
    @ApiModelProperty(value ="在职开始时间")
    private String startTime;

    /**
     * 在职结束时间
     */
    @TableField(value = "end_time")
    @ApiModelProperty(value ="在职结束时间")
    private String endTime;

    /**
     * 职位业绩
     */
    @TableField(value = "content")
    @ApiModelProperty(value ="职位业绩")
    private String content;

    /**
     * 是否对该公司隐藏简历(0显示1隐藏)
     */
    @TableField(value = "visible")
    @ApiModelProperty(value ="是否对该公司隐藏简历(0显示1隐藏)")
    private Integer visible;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @ApiModelProperty(value ="创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    @ApiModelProperty(value ="更新时间")
    private LocalDateTime updateTime;

}