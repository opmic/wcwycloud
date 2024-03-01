package com.wcwy.company.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 项目经历表
 * @TableName t_jobhunter_project_record
 */
@Data
@ApiModel(value = "导入项目经历更新+添加")
public class AddTJobhunterProjectRecordVO {



    /**
     * 项目名称
     */

    @ApiModelProperty("项目名称")
    @NotBlank(message = "项目名称不能为空")
    private String projectName;

    /**
     * 项目角色
     */

    @ApiModelProperty("项目角色")
    @NotBlank(message = "项目角色不能为空")
    private String projectRole;

    /**
     * 项目链接
     */

/*    @ApiModelProperty("项目链接")
    private String projectUrl;*/
    @NotBlank(message = "公司名称不能为空")
    @ApiModelProperty("公司名称")
    @Size(message = "公司名称不能少于8个字",min = 8)
    private String companyName;
    /**
     * 项目开始时间
     */

    @ApiModelProperty("项目开始时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotBlank(message = "项目开始时间不能为空")
    private String startTime;

    /**
     * 项目结束时间
     */

    @ApiModelProperty("项目结束时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotBlank(message = "项目结束时间不能为空")
    private String endTime;

    /**
     * 项目职责
     */

    @ApiModelProperty("项目职责")
    @NotBlank(message = "项目职责不能为空")
    @Size(message = "项目职责不能少于40个字",min = 40)
    private String responsibility;

    /**
     * 项目业绩
     */

    @ApiModelProperty("项目业绩")
/*    @NotBlank(message = "项目业绩不能为空")
    @Size(message = "项目业绩不能少于40个字",min = 40)*/
    private String performance;

    /**
     * 项目描述
     */

    @ApiModelProperty("项目描述")
    @NotBlank(message = "项目描述不能为空")
    @Size(message = "项目描述不能少于40个字",min = 40)
    private String description;

}