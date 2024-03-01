package com.wcwy.company.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 隐藏简历配置表
 * @TableName t_jobhunter_resume_config
 */
@Data
@ApiModel(value = "隐藏简历配置")
public class TJobhunterResumeConfigPO implements Serializable {
    /**
     * 主键Id
     */

    @ApiModelProperty(value = "主键Id")
    private Long id;

    /**
     * 隐藏简历状态(0显示 1隐藏)
     */
    @ApiModelProperty(value = "简历配置 1:所以可看 2:都不可看 3:推荐官可看")
    private Integer visible;





}