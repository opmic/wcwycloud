package com.wcwy.company.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 工作经历表
 * @TableName t_jobhunter_work_record
 */
@Data
@ApiModel(value = "工作经历表")
public class    TJobhunterWorkRecordVO implements Serializable {
    /**
     * 主键Id
     */
    @ApiModelProperty(value ="主键Id 如果填id就更行  如果不填就添加")
    private String workId;

    @ApiModelProperty(value = "求职者id",required = true)
    @NotBlank(message = "求职者id不能为空")
    private String userId;
    /**
     * 简历Id
     */
    @ApiModelProperty(value ="简历Id",required = true)
    @NotBlank(message = "简历id不能为空")
    private String resumeId;

    @ApiModelProperty(value ="所属部门")
    private String department;

    /**
     * 公司名称
     */
    @ApiModelProperty(value ="公司名称",required = true)
    @NotBlank(message = "公司名称不能为空")
    @Size(message = "公司名称必须大于8个字符",min = 8,max = 20)
    private String companyName;

    @ApiModelProperty(value ="工作职责及业绩",required = true)
    @NotBlank(message = "工作职责及业绩不能为空")
    @Size(message = "工作职责及业绩必须大于40个字符",min = 40,max = 2000)
    private String jobDescription;
    /**
     * 所属行业
     */
    @ApiModelProperty(value ="所属行业",required = true)
    @NotNull(message = "所属行业不能为空")
    private List<String> industry;

    @ApiModelProperty(value ="职位类型")
    @NotNull(message = "职位类型不能为空")
    private String position;

    /**
     * 职位名称
     */
    @ApiModelProperty(value ="职位名称",required = true)
    @NotBlank(message = "职位名称不能为空")
    private String positionName;

    /**
     * 在职开始时间
     */
    @ApiModelProperty(value ="在职开始时间",required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotBlank(message = "在职开始时间不能为空")
    private String startTime;

    /**
     * 在职结束时间
     */
    @ApiModelProperty(value ="在职结束时间",required = true)
    @NotBlank(message = "在职结束时间不能为空")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String endTime;

    /**
     * 职位业绩
     */
    @ApiModelProperty(value ="职位业绩")
    private String content;

    /**
     * 是否对该公司隐藏简历(0显示1隐藏)
     */
    @ApiModelProperty(value ="是否对该公司隐藏简历(0显示1隐藏)",required = true)
     @NotNull(message = "是否对该公司隐藏简历?")
    private Integer visible;



}