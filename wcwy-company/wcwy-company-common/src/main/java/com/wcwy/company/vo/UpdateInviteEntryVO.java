package com.wcwy.company.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 发送offer表
 * @TableName invite_entry
 */
@Data
@ApiModel("取消入职申请")
public class UpdateInviteEntryVO  {
/*
    */
/**
     * 邀请入职表
     *//*

    @ApiModelProperty(value = "邀请入职表id",required = true)
    @NotBlank(message = "请填入邀请入职表id")
    private String inviteEntryId;

    */
/**
     * 入职取消申请(1:未发送取消申请 2:已发送取消申请)
     *//*

    @ApiModelProperty(value = "入职取消申请(1:未发送取消申请 2:已发送取消申请)",required = true)
    @NotNull(message = "请填入入职取消申请")
    private Integer cancel;
*/



    /**
     * 取消原因
     */
    @ApiModelProperty("取消原因")
    private String cancelCause;

    @NotBlank(message = "投放简历id不能为空" )
    @ApiModelProperty(value = "投放简历id不能为空",required = true)
    private String putInResumeId;




}