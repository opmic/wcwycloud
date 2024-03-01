package com.wcwy.company.entity;

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
 * 无忧币和金币流水账表
 * @TableName running_water
 */
@TableName(value ="running_water")
@Data
@ApiModel(value = "无忧币和金币流水账表")
public class RunningWater implements Serializable {
    /**
     * 流水id
     */
    @TableId(value = "running_water_id")
    @ApiModelProperty("流水id")
    private String runningWaterId;

    /**
     *  来源（1:充值 2:提现 3:购买简历 4:分成 5:消费）
     */
    @TableField(value = "source")
    @ApiModelProperty("来源（1:充值 2:提现 3:购买简历 4:分成 5:消费）")
    private Integer source;

    /**
     * 类型(0:无忧币1:金币)
     */
    @TableField(value = "type")
    @ApiModelProperty("类型(0:无忧币1:金币)")
    private Integer type;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    @ApiModelProperty("用户id")
    private String userId;

    /**
     * 无忧币流水金额
     */
    @TableField(value = "money")
    @ApiModelProperty(" 无忧币流水金额")
    private BigDecimal money;

    /**
     * 1:支出 2:收入
     */
    @TableField(value = "if_income")
    @ApiModelProperty(" 1:支出 2:收入")
    private Integer ifIncome;

    /**
     * 来源订单id
     */
    @TableField(value = "order_id")
    @ApiModelProperty("来源订单id")
    private String orderId;

    /**
     * 资源使用说明
     */
    @TableField(value = "instructions")
    @ApiModelProperty("资源使用说明")
    private String instructions;

    /**
     * 余额
     */
    @TableField(value = "remaining_sum")
    @ApiModelProperty("余额")
    private BigDecimal remainingSum;

    /**
     * 创建时间
     */
    @TableField(value = "crate_time")
    @ApiModelProperty("创建时间")
    private LocalDateTime crateTime;

    /**
     * 是否删除（0:否 1:是）
     */
    @TableField(value = "deleted")
    @ApiModelProperty("是否删除（0:否 1:是）")
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
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getMoney() == null ? other.getMoney() == null : this.getMoney().equals(other.getMoney()))
            && (this.getIfIncome() == null ? other.getIfIncome() == null : this.getIfIncome().equals(other.getIfIncome()))
            && (this.getOrderId() == null ? other.getOrderId() == null : this.getOrderId().equals(other.getOrderId()))
            && (this.getInstructions() == null ? other.getInstructions() == null : this.getInstructions().equals(other.getInstructions()))
            && (this.getRemainingSum() == null ? other.getRemainingSum() == null : this.getRemainingSum().equals(other.getRemainingSum()))
            && (this.getCrateTime() == null ? other.getCrateTime() == null : this.getCrateTime().equals(other.getCrateTime()))
            && (this.getDeleted() == null ? other.getDeleted() == null : this.getDeleted().equals(other.getDeleted()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getRunningWaterId() == null) ? 0 : getRunningWaterId().hashCode());
        result = prime * result + ((getSource() == null) ? 0 : getSource().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getMoney() == null) ? 0 : getMoney().hashCode());
        result = prime * result + ((getIfIncome() == null) ? 0 : getIfIncome().hashCode());
        result = prime * result + ((getOrderId() == null) ? 0 : getOrderId().hashCode());
        result = prime * result + ((getInstructions() == null) ? 0 : getInstructions().hashCode());
        result = prime * result + ((getRemainingSum() == null) ? 0 : getRemainingSum().hashCode());
        result = prime * result + ((getCrateTime() == null) ? 0 : getCrateTime().hashCode());
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
        sb.append(", type=").append(type);
        sb.append(", userId=").append(userId);
        sb.append(", money=").append(money);
        sb.append(", ifIncome=").append(ifIncome);
        sb.append(", orderId=").append(orderId);
        sb.append(", instructions=").append(instructions);
        sb.append(", remainingSum=").append(remainingSum);
        sb.append(", crateTime=").append(crateTime);
        sb.append(", deleted=").append(deleted);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}