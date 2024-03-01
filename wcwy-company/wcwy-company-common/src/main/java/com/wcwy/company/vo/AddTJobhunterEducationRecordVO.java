package com.wcwy.company.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 教育经历表
 * @TableName t_jobhunter_education_record
 */

@Data
@ApiModel(value = "导入教育经历表")
public class AddTJobhunterEducationRecordVO {
    /**
     * 学校名称
     */

    @ApiModelProperty(value = "学校名称")
    @NotBlank(message = "学校名称不能为空")
    private String shcoolName;

    /**
     * 学制类型(0其他1全日制2非全日制)
     */
    @ApiModelProperty(value = "学制类型(0其他1全日制2非全日制)")
    private Integer edulType;

    /**
     * 学历
     */
    @ApiModelProperty(value = "学历")
    @NotBlank(message = "学历不能为空")
    private String education;

    /**
     * 专业
     */
    @ApiModelProperty(value = "专业")
    @NotBlank(message = "专业不能为空")
    private String major;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    @NotBlank(message = "专业不能为空")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    @NotBlank(message = "专业不能为空")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String endTime;

    /**
     * 在校经历
     */
    @ApiModelProperty(value = "在校经历")
    private String description;



}