package com.wcwy.company.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 期望职位表
 * @TableName t_jobhunter_expect_position
 */
@Data
@ApiModel(value = "期望职位表")
public class TJobhunterExpectPositionPO implements Serializable {
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



}