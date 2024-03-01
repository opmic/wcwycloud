package com.wcwy.company.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 推广职位明细
 * @TableName promotion_post_detail
 */
@TableName(value ="promotion_post_detail")
@Data
@ApiModel(value = "推广职位明细")
public class PromotionPostDetail implements Serializable {
    /**
     * 推广明细id
     */
    @TableId(value = "promotion_post_detail_id", type = IdType.AUTO)
    @ApiModelProperty(value = "推广明细id")
    private Long promotionPostDetailId;

    /**
     * 推广时间
     */
    @TableField(value = "promotion_time")
    @ApiModelProperty(value = "推广时间")
    private LocalDateTime promotionTime;

    /**
     * 推广id
     */
    @TableField(value = "promotion_post_id")
    @ApiModelProperty(value = "推广id")
    private String promotionPostId;

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
        PromotionPostDetail other = (PromotionPostDetail) that;
        return (this.getPromotionPostDetailId() == null ? other.getPromotionPostDetailId() == null : this.getPromotionPostDetailId().equals(other.getPromotionPostDetailId()))
            && (this.getPromotionTime() == null ? other.getPromotionTime() == null : this.getPromotionTime().equals(other.getPromotionTime()))
            && (this.getPromotionPostId() == null ? other.getPromotionPostId() == null : this.getPromotionPostId().equals(other.getPromotionPostId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getPromotionPostDetailId() == null) ? 0 : getPromotionPostDetailId().hashCode());
        result = prime * result + ((getPromotionTime() == null) ? 0 : getPromotionTime().hashCode());
        result = prime * result + ((getPromotionPostId() == null) ? 0 : getPromotionPostId().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", promotionPostDetailId=").append(promotionPostDetailId);
        sb.append(", promotionTime=").append(promotionTime);
        sb.append(", promotionPostId=").append(promotionPostId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}