package com.wcwy.company.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 期望职位表
 * @TableName t_jobhunter_expect_position
 */
@TableName(value ="t_jobhunter_expect_position",autoResultMap = true)
@Data
@ApiModel(value = "期望职位表")
public class TJobhunterExpectPosition implements Serializable {
    /**
     * 主键Id
     */
    @TableId(value = "postion_id")
    @ApiModelProperty(value = "主键Id")
    private String postionId;

    /**
     * 简历id
     */
    @TableField(value = "resume_id")
    @ApiModelProperty(value = "简历id")
    private String resumeId;

    /**
     * 期望职位
     */
    @TableField(value = "position_name",typeHandler = JacksonTypeHandler.class)
    @ApiModelProperty(value = "期望职位")
    private List<String> positionName;


    /**
     * 期望行业
     */
    @TableField(value = "desired_industry",typeHandler = JacksonTypeHandler.class)
    @ApiModelProperty(value = "期望行业")
    private List<String> desiredIndustry;
    /**
     * 工作城市
     */
    @TableField(value = "work_city",typeHandler = JacksonTypeHandler.class)
    @ApiModelProperty(value = "工作城市")
    private List<String> workCity;

    /**
     * 目前年薪
     */
    @TableField(value = "current_salary")
    @ApiModelProperty(value = "目前年薪")
    private BigDecimal currentSalary;

    /**
     * 期望年薪
     */
    @TableField(value = "expect_salary")
    @ApiModelProperty(value = "期望年薪")
    private BigDecimal expectSalary;
    /**
     * 结束期望年薪
     */
    @TableField(value = "end_expect_salary")
    @ApiModelProperty(value = "结束期望年薪")
    private BigDecimal endExpectSalary;
    /**
     * 最快到岗时间
     */
    @TableField(value = "arrival_time")
    @ApiModelProperty(value = "最快到岗时间")
    private Integer arrivalTime;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;





}