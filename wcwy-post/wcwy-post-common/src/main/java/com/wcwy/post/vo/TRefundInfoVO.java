package com.wcwy.post.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 退款表
 * @TableName t_refund_info
 */
@Data
@ApiModel("退款实体类")
public class TRefundInfoVO implements Serializable {
    /**
     * 退款单id
     */
    @ApiModelProperty("退款单id")
    private String id;

    /**
     * 商户订单编号
     */
    @ApiModelProperty("商户订单编号")
    private String orderNo;


    /**
     * 退款金额(元)
     */
    @TableField(value = "refund")
    @ApiModelProperty("退款金额(元)")
    private BigDecimal refund;



    /**
     * 退款状态(1:审核中 2审核失败 3:退款成功)
     */
    @TableField(value = "refund_state")
    @ApiModelProperty("退款状态(1:审核中 2审核失败 3:退款成功)")
    private Integer refundState;

    /**
     * 失败原因
     */
    @TableField(value = "state_cause")
    @ApiModelProperty("失败原因")
    private String stateCause;


}