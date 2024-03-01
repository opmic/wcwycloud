package com.wcwy.post.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 接单表
 * @TableName order_receiving
 */
@TableName(value ="order_receiving")
@Data
@ApiModel("接单表")
public class OrderReceiving implements Serializable {
    /**
     * 接单id
     */
    @TableId(value = "order_receiving_id")
    @ApiModelProperty("接单id")
    private String orderReceivingId;

    /**
     * 岗位id
     */
    @TableField(value = "post_id")
    @ApiModelProperty("岗位id")
    private String postId;

    /**
     * 推荐官id
     */
    @TableField(value = "recommerd")
    @ApiModelProperty("推荐官id")
    private String recommerd;

    /**
     * 接单时间
     */
    @TableField(value = "create_time")
    @ApiModelProperty("接单时间")
    private LocalDateTime createTime;

    /**
     * 是否收藏(0:未收藏 1:已收藏)
     */
    @TableField(value = "collect")
    @ApiModelProperty(" 是否收藏(0:未收藏 1:已收藏)")
    private Integer collect;

    /**
     * 收藏时间
     */
    @TableField(value = "collect_time")
    @ApiModelProperty(" 收藏时间")
    private LocalDateTime collectTime;

    /**
     * 是否取消（1:未取消 2：取消）
     */
    @TableField(value = "cancel")
    @ApiModelProperty(" 是否接单（1:未取消 2：取消）")
    private Integer cancel;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

    /**
     * 逻辑删除
     */
    @TableField(value = "deleted")
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
        OrderReceiving other = (OrderReceiving) that;
        return (this.getOrderReceivingId() == null ? other.getOrderReceivingId() == null : this.getOrderReceivingId().equals(other.getOrderReceivingId()))
            && (this.getPostId() == null ? other.getPostId() == null : this.getPostId().equals(other.getPostId()))
            && (this.getRecommerd() == null ? other.getRecommerd() == null : this.getRecommerd().equals(other.getRecommerd()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getCollect() == null ? other.getCollect() == null : this.getCollect().equals(other.getCollect()))
            && (this.getCollectTime() == null ? other.getCollectTime() == null : this.getCollectTime().equals(other.getCollectTime()))
            && (this.getCancel() == null ? other.getCancel() == null : this.getCancel().equals(other.getCancel()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getDeleted() == null ? other.getDeleted() == null : this.getDeleted().equals(other.getDeleted()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getOrderReceivingId() == null) ? 0 : getOrderReceivingId().hashCode());
        result = prime * result + ((getPostId() == null) ? 0 : getPostId().hashCode());
        result = prime * result + ((getRecommerd() == null) ? 0 : getRecommerd().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getCollect() == null) ? 0 : getCollect().hashCode());
        result = prime * result + ((getCollectTime() == null) ? 0 : getCollectTime().hashCode());
        result = prime * result + ((getCancel() == null) ? 0 : getCancel().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getDeleted() == null) ? 0 : getDeleted().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", orderReceivingId=").append(orderReceivingId);
        sb.append(", postId=").append(postId);
        sb.append(", recommerd=").append(recommerd);
        sb.append(", createTime=").append(createTime);
        sb.append(", collect=").append(collect);
        sb.append(", collectTime=").append(collectTime);
        sb.append(", cancel=").append(cancel);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", deleted=").append(deleted);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}