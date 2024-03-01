package com.wcwy.company.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 分享收益提现表
 * @TableName revenue_sharing
 */
@TableName(value ="revenue_sharing")
@Data
@ApiModel("分享收益提现表")
public class RevenueSharing implements Serializable {
    /**
     * 收益人
     */
    @TableId(value = "user_id")
    @ApiModelProperty("收益人")
    private String userId;

    /**
     * 总收益
     */
    @TableField(value = "total_revenue")
    @ApiModelProperty("总收益")
    private BigDecimal totalRevenue;

    /**
     * 提现
     */
    @TableField(value = "withdraw_deposit")
    @ApiModelProperty("提现")
    private BigDecimal withdrawDeposit;

    /**
     * 剩余金额
     */
    @TableField(value = "remaining_sum")
    @ApiModelProperty("剩余金额")
    private BigDecimal remainingSum;

    /**
     * 类型(1:分享 2:接单)
     */
    @TableField(value = "type")
    @ApiModelProperty("类型(1:分享 2:接单)")
    private Integer type;

    /**
     * 是否禁止提现(0:未禁止 1:禁止提现)
     */
    @TableField(value = "forbid")
    @ApiModelProperty("是否禁止提现(0:未禁止 1:禁止提现)")
    private Integer forbid;

    /**
     * 禁止原因
     */
    @TableField(value = "forbid_cause")
    @ApiModelProperty("禁止原因")
    private String forbidCause;

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
        RevenueSharing other = (RevenueSharing) that;
        return (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getTotalRevenue() == null ? other.getTotalRevenue() == null : this.getTotalRevenue().equals(other.getTotalRevenue()))
            && (this.getWithdrawDeposit() == null ? other.getWithdrawDeposit() == null : this.getWithdrawDeposit().equals(other.getWithdrawDeposit()))
            && (this.getRemainingSum() == null ? other.getRemainingSum() == null : this.getRemainingSum().equals(other.getRemainingSum()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getForbid() == null ? other.getForbid() == null : this.getForbid().equals(other.getForbid()))
            && (this.getForbidCause() == null ? other.getForbidCause() == null : this.getForbidCause().equals(other.getForbidCause()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getTotalRevenue() == null) ? 0 : getTotalRevenue().hashCode());
        result = prime * result + ((getWithdrawDeposit() == null) ? 0 : getWithdrawDeposit().hashCode());
        result = prime * result + ((getRemainingSum() == null) ? 0 : getRemainingSum().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getForbid() == null) ? 0 : getForbid().hashCode());
        result = prime * result + ((getForbidCause() == null) ? 0 : getForbidCause().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", userId=").append(userId);
        sb.append(", totalRevenue=").append(totalRevenue);
        sb.append(", withdrawDeposit=").append(withdrawDeposit);
        sb.append(", remainingSum=").append(remainingSum);
        sb.append(", type=").append(type);
        sb.append(", forbid=").append(forbid);
        sb.append(", forbidCause=").append(forbidCause);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}