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
 * 用户屏蔽公司表
 * @TableName t_jobhunter_hide_company
 */
@TableName(value ="t_jobhunter_hide_company")
@Data
@ApiModel(value = "用户屏蔽公司表")
public class TJobhunterHideCompany implements Serializable {
    /**
     * 主键Id
     */
    @TableId(value = "id")
    @ApiModelProperty(value = "主键Id")
    private String id;

    /**
     * 简历Id
     */
    @TableField(value = "resume_id")
    @ApiModelProperty(value = "求职者id")
    private String resumeId;

    /**
     * 屏蔽公司Id
     */
    @TableField(value = "company_id")
    @ApiModelProperty(value = "屏蔽公司Id")
    private String companyId;

    /**
     * 屏蔽公司名称
     */
    @TableField(value = "company_name")
    @ApiModelProperty(value = "屏蔽公司名称")
    private String companyName;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;


}