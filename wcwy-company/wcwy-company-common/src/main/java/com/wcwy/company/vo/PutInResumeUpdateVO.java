package com.wcwy.company.vo;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 投递简历表
 *
 * @TableName put_in_resume
 */
@Data
@ApiModel("更新投递简历实体类")
public class PutInResumeUpdateVO implements Serializable {
    /**
     * 投放简历id
     */
    @ApiModelProperty("投放简历id")
    @NotBlank(message = "投放简历id不能为空")
    private String putInResumeId;

    @ApiModelProperty("投放的岗位")
    @NotBlank(message = "投放的岗位不能为空")
    private String postId;
    @ApiModelProperty("投简人id")
    @NotBlank(message = "投简人id不能为空")
    private String putInUser;

    @ApiModelProperty("求职者id")
    @NotBlank(message = "求职者id不能为空")
    private String jobHunter;
    /**
     * 是否浏览（1：否 2：是）
     */
    @ApiModelProperty("是否浏览（1：否 2：是）")
    private Integer browseIf;


    /**
     * 是否排除（1：否 2：是）
     */
    @ApiModelProperty("是否排除（1：否 2：是）")
    private Integer excludeIf;

    /**
     * 排除原因
     */
    @ApiModelProperty("排除原因")
    private String excludeState;

    /**
     * 是否面试中（1：否 2：是）
     */
    @ApiModelProperty("是否面试中（1：否 2：是）")
    private Integer interviewIf;

    /**
     * 是否淘汰（1：否 2：是）
     */
    @ApiModelProperty("是否淘汰（1：否 2：是）")
    private Integer weedOutIf;

    /**
     * 淘汰原因
     */
    @ApiModelProperty("淘汰原因")
    private String weedOutCause;


    @ApiModelProperty("是否过保(1:否 2:是)")
    private Integer overProtectionIf;
    /**
     * 未过保原因
     */
    @ApiModelProperty("未过保原因")
    private String overProtectionCause;

    @ApiModelProperty("短信提醒(0不提醒 1:提醒)")
    private  Integer remind;
    /**
     * 是否入职（1：否 2：是）
     */
/*    @ApiModelProperty("是否入职（1：否 2：是）")
    private Integer entryIf;*/
/*    @ApiModelProperty("金额")
    private BigDecimal money;
    @ApiModelProperty("支付方式（1：支付宝 2：微信 3无忧币）")
    private String paymentType;
    @ApiModelProperty("订单标题")
    private String title;*/

}