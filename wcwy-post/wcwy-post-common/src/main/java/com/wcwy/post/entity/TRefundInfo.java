package com.wcwy.post.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 退款表
 * @TableName t_refund_info
 */
@TableName(value ="t_refund_info")
@Data
@ApiModel("退款表实体类")
public class TRefundInfo implements Serializable {
    /**
     * 退款单id
     */
    @TableId(value = "id", type = IdType.INPUT)
    @ApiModelProperty("退款单id")
    private String id;

    /**
     * 商户订单编号
     */
    @TableField(value = "order_no")
    @ApiModelProperty("退款单id")
    private String orderNo;

    /**
     * 商户退款单编号
     */
    @TableField(value = "refund_no")
    @ApiModelProperty("退款单id")
    private String refundNo;

    /**
     * 支付系统退款单号
     */
    @TableField(value = "refund_id")
    @ApiModelProperty("退款单id")
    private String refundId;

    /**
     * 原订单金额(元)
     */
    @TableField(value = "total_fee")
    @ApiModelProperty("原订单金额(元)")
    private BigDecimal totalFee;

    /**
     * 退款金额(元)
     */
    @TableField(value = "refund")
    @ApiModelProperty("退款金额(元)")
    private BigDecimal refund;

    /**
     * 退款原因
     */
    @TableField(value = "reason")
    @ApiModelProperty("退款原因")
    private String reason;

    /**
     * 退款状态
     */
    @TableField(value = "refund_status")
    @ApiModelProperty("退款状态")
    private String refundStatus;

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

    /**
     * 退款人
     */
    @TableField(value = "admin_user")
    @ApiModelProperty("退款人")
    private String adminUser;

    /**
     * 申请退款返回参数
     */
    @TableField(value = "content_return")
    @ApiModelProperty("申请退款返回参数")
    private String contentReturn;

    /**
     * 退款结果通知参数
     */
    @TableField(value = "content_notify")
    @ApiModelProperty("退款结果通知参数")
    private String contentNotify;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        TRefundInfo other = (TRefundInfo) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getOrderNo() == null ? other.getOrderNo() == null : this.getOrderNo().equals(other.getOrderNo()))
            && (this.getRefundNo() == null ? other.getRefundNo() == null : this.getRefundNo().equals(other.getRefundNo()))
            && (this.getRefundId() == null ? other.getRefundId() == null : this.getRefundId().equals(other.getRefundId()))
            && (this.getTotalFee() == null ? other.getTotalFee() == null : this.getTotalFee().equals(other.getTotalFee()))
            && (this.getRefund() == null ? other.getRefund() == null : this.getRefund().equals(other.getRefund()))
            && (this.getReason() == null ? other.getReason() == null : this.getReason().equals(other.getReason()))
            && (this.getRefundStatus() == null ? other.getRefundStatus() == null : this.getRefundStatus().equals(other.getRefundStatus()))
            && (this.getRefundState() == null ? other.getRefundState() == null : this.getRefundState().equals(other.getRefundState()))
            && (this.getStateCause() == null ? other.getStateCause() == null : this.getStateCause().equals(other.getStateCause()))
            && (this.getAdminUser() == null ? other.getAdminUser() == null : this.getAdminUser().equals(other.getAdminUser()))
            && (this.getContentReturn() == null ? other.getContentReturn() == null : this.getContentReturn().equals(other.getContentReturn()))
            && (this.getContentNotify() == null ? other.getContentNotify() == null : this.getContentNotify().equals(other.getContentNotify()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getOrderNo() == null) ? 0 : getOrderNo().hashCode());
        result = prime * result + ((getRefundNo() == null) ? 0 : getRefundNo().hashCode());
        result = prime * result + ((getRefundId() == null) ? 0 : getRefundId().hashCode());
        result = prime * result + ((getTotalFee() == null) ? 0 : getTotalFee().hashCode());
        result = prime * result + ((getRefund() == null) ? 0 : getRefund().hashCode());
        result = prime * result + ((getReason() == null) ? 0 : getReason().hashCode());
        result = prime * result + ((getRefundStatus() == null) ? 0 : getRefundStatus().hashCode());
        result = prime * result + ((getRefundState() == null) ? 0 : getRefundState().hashCode());
        result = prime * result + ((getStateCause() == null) ? 0 : getStateCause().hashCode());
        result = prime * result + ((getAdminUser() == null) ? 0 : getAdminUser().hashCode());
        result = prime * result + ((getContentReturn() == null) ? 0 : getContentReturn().hashCode());
        result = prime * result + ((getContentNotify() == null) ? 0 : getContentNotify().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", orderNo=").append(orderNo);
        sb.append(", refundNo=").append(refundNo);
        sb.append(", refundId=").append(refundId);
        sb.append(", totalFee=").append(totalFee);
        sb.append(", refund=").append(refund);
        sb.append(", reason=").append(reason);
        sb.append(", refundStatus=").append(refundStatus);
        sb.append(", refundState=").append(refundState);
        sb.append(", stateCause=").append(stateCause);
        sb.append(", adminUser=").append(adminUser);
        sb.append(", contentReturn=").append(contentReturn);
        sb.append(", contentNotify=").append(contentNotify);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}