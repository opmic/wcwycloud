package com.wcwy.post.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 无忧币流水账表
 * @TableName running_water
 */
@TableName(value ="running_water")
@Data
public class RunningWater implements Serializable {
    /**
     * 无忧币流水id
     */
    @TableId(value = "running_water_id")
    private String runningWaterId;

    /**
     *  来源（1:充值 2:提现 3:购买简历 4:分成）
     */
    @TableField(value = "source")
    private Integer source;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    private String userId;


    /**
     * 无忧币流水金额
     */
    @TableField(value = "money")
    @ApiModelProperty(value = "无忧币流水金额")
    private BigDecimal money;
    /**
     * 1:支出 2:收入
     */
    @TableField(value = "if_income")
    @ApiModelProperty(value = "1:支出 2:收入")
    private Integer ifIncome;

    /**
     * 来源订单id
     */
    @TableField(value = "order_id")
    @ApiModelProperty(value = "来源订单id")
    private String orderId;

    /**
     * 是否删除（0:否 1:是）
     */
    @TableField(value = "deleted")
    @ApiModelProperty(value = "是否删除（0:否 1:是）")
    @TableLogic
    private Integer deleted;

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
        RunningWater other = (RunningWater) that;
        return (this.getRunningWaterId() == null ? other.getRunningWaterId() == null : this.getRunningWaterId().equals(other.getRunningWaterId()))
            && (this.getSource() == null ? other.getSource() == null : this.getSource().equals(other.getSource()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getIfIncome() == null ? other.getIfIncome() == null : this.getIfIncome().equals(other.getIfIncome()))
            && (this.getOrderId() == null ? other.getOrderId() == null : this.getOrderId().equals(other.getOrderId()))
            && (this.getDeleted() == null ? other.getDeleted() == null : this.getDeleted().equals(other.getDeleted()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getRunningWaterId() == null) ? 0 : getRunningWaterId().hashCode());
        result = prime * result + ((getSource() == null) ? 0 : getSource().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getIfIncome() == null) ? 0 : getIfIncome().hashCode());
        result = prime * result + ((getOrderId() == null) ? 0 : getOrderId().hashCode());
        result = prime * result + ((getDeleted() == null) ? 0 : getDeleted().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", runningWaterId=").append(runningWaterId);
        sb.append(", source=").append(source);
        sb.append(", userId=").append(userId);
        sb.append(", ifIncome=").append(ifIncome);
        sb.append(", orderId=").append(orderId);
        sb.append(", deleted=").append(deleted);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}