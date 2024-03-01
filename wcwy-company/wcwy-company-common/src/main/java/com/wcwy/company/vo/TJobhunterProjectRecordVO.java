package com.wcwy.company.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 项目经历表
 * @TableName t_jobhunter_project_record
 */
@Data
@ApiModel(value = "项目经历更新+添加")
public class TJobhunterProjectRecordVO {
    /**
     * 主键Id
     */

    @ApiModelProperty(value = "主键Id 填入则更新 不填入则添加")
    private String projectId;




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
     * 项目名称
     */

    @ApiModelProperty(value = "项目名称",required = true)
    @NotBlank(message = "项目名称不能为空")
    private String projectName;

    /**
     * 项目角色
     */

    @ApiModelProperty(value = "项目角色",required = true)
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