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
import org.apache.ibatis.annotations.Mapper;

/**
 * 教育经历表
 * @TableName t_jobhunter_education_record
 */
@TableName(value ="t_jobhunter_education_record")
@Data
@ApiModel(value = "教育经历表")
public class TJobhunterEducationRecord implements Serializable {
    /**
     * 主键Id
     */
    @TableId(value = "edu_id")
    @ApiModelProperty(value = "主键Id")
    private String eduId;

    /**
     * 用户Id
     */
    @TableField(value = "resume_id")
    @ApiModelProperty(value = "简历id")
    private String resumeId;

    /**
     * 学校名称
     */
    @TableField(value = "shcool_name")
    @ApiModelProperty(value = "学校名称")
    private String shcoolName;

    /**
     * 学制类型(0其他1全日制2非全日制)
     */
    @TableField(value = "edul_type")
    @ApiModelProperty(value = "学制类型(0其他1全日制2非全日制)")
    private Integer edulType;

    /**
     * 学历
     */
    @TableField(value = "education")
    @ApiModelProperty(value = "学历")
    private String education;

    /**
     * 专业
     */
    @TableField(value = "major")
    @ApiModelProperty(value = "专业")
    private String major;

    /**
     * 开始时间
     */
    @TableField(value = "start_time")
    @ApiModelProperty(value = "开始时间")
    private String startTime;

    /**
     * 结束时间
     */
    @TableField(value = "end_time")
    @ApiModelProperty(value = "结束时间")
    private String endTime;

    /**
     * 在校经历
     */
    @TableField(value = "description")
    @ApiModelProperty(value = "在校经历")
    private String description;

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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


}