package com.wcwy.company.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 期望职位表
 * @TableName t_jobhunter_expect_position
 */
@Data
@ApiModel(value = "添加期望职位")
public class TJobhunterExpectPositionVO{

    /**
     * 简历id
     */
    @ApiModelProperty(value="求职者id",required = true)
    @NotBlank(message = "求职者id不能为空")
    private String userId;
    @ApiModelProperty(value = "简历id",required = true)
    @NotBlank(message = "简历id不能为空")
    private String resumeId;
    /**
     * 期望职位
     */
    @ApiModelProperty(value = "期望职位",required = true)
    @NotEmpty(message = "期望职位不能为空")
    private List<String> positionName;
    /**
     * 期望行业
     */
    @ApiModelProperty(value = "期望行业",required = true)
    @NotEmpty(message = "期望行业不能为空")
    private List<String> desiredIndustry;
    /**
     * 工作城市
     */
    @ApiModelProperty(value = "工作城市",required = true)
    @NotEmpty(message = "工作城市不能为空")
    private List<String> workCity;

    /**
     * 期望年薪
     */

    @ApiModelProperty(value = "期望年薪(单位:W)",required = true)
    @NotNull(message = "期望年薪不能为空")
    private BigDecimal expectSalary;
    /**
     * 主键Id
     */
    @ApiModelProperty(value = "主键Id 注:填入则更新 不填入则添加")
    private String postionId;
/*
    @ApiModelProperty(value = "结束期望年薪")
    private BigDecimal endExpectSalary;*/
}