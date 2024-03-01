package com.wcwy.company.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 项目经历表
 * @TableName t_jobhunter_project_record
 */
@TableName(value ="t_jobhunter_project_record")
@Data
@ApiModel(value = "项目经历表")
public class TJobhunterProjectRecord implements Serializable {
    /**
     * 主键Id
     */
    @TableId(value = "project_id")
    @ApiModelProperty("主键Id")
    private String projectId;

    /**
     * 简历id
     */
    @TableField(value = "resume_id")
    @ApiModelProperty("简历id")
    private String resumeId;

    /**
     * 项目名称
     */
    @TableField(value = "project_name")
    @ApiModelProperty("项目名称")
    private String projectName;


    @TableField(value = "company_name")
    @ApiModelProperty("公司名称")
    private String companyName;
    /**
     * 项目角色
     */
    @TableField(value = "project_role")
    @ApiModelProperty("项目角色")
    private String projectRole;

    /**
     * 项目链接
     */
    @TableField(value = "project_url")
    @ApiModelProperty("项目链接")
    private String projectUrl;

    /**
     * 项目开始时间
     */
    @TableField(value = "start_time")
    @ApiModelProperty("项目开始时间")
    private String startTime;

    /**
     * 项目结束时间
     */
    @TableField(value = "end_time")
    @ApiModelProperty("项目结束时间")
    private String endTime;

    /**
     * 项目职责
     */
    @TableField(value = "responsibility")
    @ApiModelProperty("项目职责")
    private String responsibility;

    /**
     * 项目业绩
     */
    @TableField(value = "performance")
    @ApiModelProperty("项目业绩")
    private String performance;

    /**
     * 项目描述
     */
    @TableField(value = "description")
    @ApiModelProperty("项目描述")
    private String description;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

}