package com.wcwy.company.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 推广职位
 * @TableName promotion_post
 */
@TableName(value ="promotion_post")
@Data
@ApiModel(value = "推广职位")
public class PromotionPost implements Serializable {
    /**
     * 推广职位id
     */
    @TableId(value = "promotion_post_id")
    @ApiModelProperty(value = "推广职位id")
    private String promotionPostId;

    /**
     * 岗位id
     */
    @TableField(value = "post_id")
    @ApiModelProperty(value = "岗位id")
    private String postId;

    /**
     * 推广时间
     */
    @TableField(value = "promotion_time")
    @ApiModelProperty(value = "推广时间")
    private LocalDateTime promotionTime;


    @TableField(value = "recommend_id")
    @ApiModelProperty(value = "推荐官ID")
    private String recommendId;
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
        PromotionPost other = (PromotionPost) that;
        return (this.getPromotionPostId() == null ? other.getPromotionPostId() == null : this.getPromotionPostId().equals(other.getPromotionPostId()))
            && (this.getPostId() == null ? other.getPostId() == null : this.getPostId().equals(other.getPostId()))
            && (this.getPromotionTime() == null ? other.getPromotionTime() == null : this.getPromotionTime().equals(other.getPromotionTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getPromotionPostId() == null) ? 0 : getPromotionPostId().hashCode());
        result = prime * result + ((getPostId() == null) ? 0 : getPostId().hashCode());
        result = prime * result + ((getPromotionTime() == null) ? 0 : getPromotionTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", promotionPostId=").append(promotionPostId);
        sb.append(", postId=").append(postId);
        sb.append(", promotionTime=").append(promotionTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}