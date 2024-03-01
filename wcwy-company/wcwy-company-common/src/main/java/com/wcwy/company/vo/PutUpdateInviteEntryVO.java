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
@ApiModel("是否同意取消入职接口")
public class PutUpdateInviteEntryVO  {

    /**
     * 邀请入职表
     */
    @ApiModelProperty("邀请入职表id")
    @NotBlank(message = "请输入邀请入职表id")
    private String inviteEntryId;
    /**
     * 投简人是否同意(0:未处理 1:不同意 2:已同意)
     */
    @ApiModelProperty(" 投简人是否同意(0:未处理 1:不同意 2:已同意)")
    @NotNull(message = "请输入投简人是否同意")
    private Integer putInConsent;


    /**
     * 不同意原因
     */
    @TableField(value = "consent_cause")
    @ApiModelProperty("不同意原因")
    private String consentCause;


    @NotBlank(message = "投放简历id不能为空")
    @ApiModelProperty("投放简历id不能为空")
    private String putInResumeId;


}