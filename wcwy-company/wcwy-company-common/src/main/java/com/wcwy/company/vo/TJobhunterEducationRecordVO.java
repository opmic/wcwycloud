package com.wcwy.company.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 教育经历表
 * @TableName t_jobhunter_education_record
 */

@Data
@ApiModel(value = "添加教育经历表")
public class TJobhunterEducationRecordVO  {
    /**
     * 主键Id
     */
    @ApiModelProperty(value = "主键Id 填入则更新 不填入则添加" )
    private String eduId;
    @ApiModelProperty(value = "求职者id",required = true)
    @NotBlank(message = "求职者id不能为空")
    private String userId;
    /**
     * 简历id
     */
    @ApiModelProperty(value = "简历id",required = true)
    @NotBlank(message = "简历id不能为空")
    private String resumeId;

    /**
     * 学校名称
     */

    @ApiModelProperty(value = "学校名称",required = true)
    @NotBlank(message = "学校名称不能为空")
    private String shcoolName;

    /**
     * 学制类型(0其他1全日制2非全日制)
     */
    @ApiModelProperty(value = "学制类型(0其他1全日制2非全日制)默认传1",required = true)
    @NotNull(message = "学制类型不能为空")
    @Min(value = 0,message = "类型不正确")
    @Max(value = 2,message = "类型不正确")
    private Integer edulType;

    /**
     * 学历
     */
    @ApiModelProperty(value = "学历",required = true)
    @NotBlank(message = "学历不能为空")
    private String education;

    /**
     * 专业
     */
    @ApiModelProperty(value = "专业",required = true)
    @NotBlank(message = "专业不能为空")
    private String major;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间",required = true)
    @NotBlank(message = "专业不能为空")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间",required = true)
    @NotBlank(message = "专业不能为空")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String endTime;

    /**
     * 在校经历
     */
    @ApiModelProperty(value = "在校经历")
    private String description;



}