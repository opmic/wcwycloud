package com.wcwy.post.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单申请取消
 * @TableName order_apply_for
 */
@TableName(value ="order_apply_for",autoResultMap = true)
@Data
@ApiModel("订单申请取消")
public class OrderApplyFor implements Serializable {
    /**
     * 申请id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("申请id")
    private Long id;

    /**
     * 申请原因
     */
    @TableField(value = "cause")
    @ApiModelProperty("申请原因")
    private String cause;

    /**
     * 审核状态(1:审核中 2已通过 3:未通过)
     */
    @TableField(value = "state")
    @ApiModelProperty("审核状态(1:审核中 2已通过 3:未通过)")
    private Integer state;

    /**
     * 反馈时间
     */
    @TableField(value = "feedback_time")
    @ApiModelProperty("反馈时间")
    private LocalDateTime feedbackTime;

    /**
     * 客服电话
     */
    @TableField(value = "service_tel")
    @ApiModelProperty("客服电话")
    private String serviceTel;

    /**
     * 反馈原因
     */
    @TableField(value = "feedback_cause")
    @ApiModelProperty("反馈原因")
    private String feedbackCause;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    /**
     * 订单号
     */
    @TableField(value = "order_id")
    @ApiModelProperty("订单号")
    private String orderId;

    /**
     * 创建人
     */
    @TableField(value = "create_id")
    @ApiModelProperty("创建人")
    private String createId;

    /**
     * 审核人
     */
    @TableField(value = "audit_id")
    @ApiModelProperty("审核人")
    private String auditId;

    /**
     * 处理状态(1:未处理 2已处理)
     */
    @TableField(value = "dispose_state")
    @ApiModelProperty("处理状态(1:未处理 2已处理)")
    private Integer disposeState;


    @TableField(value = "attachment",typeHandler = JacksonTypeHandler.class )
    @ApiModelProperty("附件地址")
    private List<String> attachment;


}