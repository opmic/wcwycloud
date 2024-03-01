package com.wcwy.company.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * 工作经历表
 * @TableName t_jobhunter_work_record
 */
@Data
@ApiModel(value = "导入工作经历表")
public class AddTJobhunterWorkRecordVO implements Serializable {

/*    @TableField(value = "department")
    @ApiModelProperty(value ="所属部门")
    private String department;*/

    /**
     * 公司名称
     */
    @ApiModelProperty(value ="公司名称")
    @NotBlank(message = "公司名称不能为空")
    @Size(message = "公司名称必须大于8个字符",min = 8,max = 20)
    private String companyName;
    @ApiModelProperty(value ="工作内容")
    @NotBlank(message = "工作内容不能为空")
    @Size(message = "工作内容必须大于40个字符",min = 40)
    private String jobDescription;
    /**
     * 所属行业
     */
    @ApiModelProperty(value ="所属行业")
    @NotNull(message = "所属行业不能为空")
    private List<String> industry;

    @ApiModelProperty(value ="职位类型")
    @NotNull(message = "职位类型不能为空")
    private String position;

    /**
     * 职位名称
     */
    @ApiModelProperty(value ="职位名称")
    @NotBlank(message = "职位名称不能为空")
    private String positionName;

    /**
     * 在职开始时间
     */
    @ApiModelProperty(value ="在职开始时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotBlank(message = "在职开始时间不能为空")
    private String startTime;

    /**
     * 在职结束时间
     */
    @ApiModelProperty(value ="在职结束时间")
    @NotBlank(message = "在职结束时间不能为空")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String endTime;

    /**
     * 职位业绩
     */
/*
    @ApiModelProperty(value ="职位业绩")
    private String content;
*/

    /**
     * 是否对该公司隐藏简历(0显示1隐藏)
     */
    @ApiModelProperty(value ="是否对该公司隐藏简历(0显示1隐藏)")
    @NotNull(message = "是否对该公司隐藏简历?")
    private Integer visible;



}