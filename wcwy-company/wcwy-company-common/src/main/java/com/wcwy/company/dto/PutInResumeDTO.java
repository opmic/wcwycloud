package com.wcwy.company.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 投递简历表
 *
 * @TableName put_in_resume
 */

@Data
public class PutInResumeDTO  {
    /**
     * 投放简历id
     */
    @ApiModelProperty("投放简历id")
    private String putInResumeId;


    /**
     * 入职时间
     */
    @ApiModelProperty("入职时间")
    private LocalDateTime entryTime;



    @ApiModelProperty(" 第几个工作日/保证期天数")
    private Integer workday;



}