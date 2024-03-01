package com.wcwy.company.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 岗位收藏表
 * @TableName collerct_post
 */
@TableName(value ="collerct_post")
@Data
@ApiModel("岗位收藏表")
public class CollerctPost implements Serializable {
    /**
     * 岗位收藏id
     */
    @TableId(value = "collerct_post_id")
    @ApiModelProperty("岗位收藏id")
    private String collerctPostId;

    /**
     * 收藏的岗位
     */
    @TableField(value = "post")
    @ApiModelProperty("收藏的岗位")
    private String post;

    /**
     * 身份(1:求职者 2:推荐官)
     */
    @TableField(value = "identity")
    @ApiModelProperty("身份(1:求职者 2:推荐官)")
    private Integer identity;

    /**
     * 收藏岗位用户id
     */
    @TableField(value = "collerct_user_id")
    @ApiModelProperty("收藏岗位用户id")
    private String collerctUserId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    /**
     * 逻辑删除
     */
    @TableField(value = "deleted")
    //@TableLogic
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
        CollerctPost other = (CollerctPost) that;
        return (this.getCollerctPostId() == null ? other.getCollerctPostId() == null : this.getCollerctPostId().equals(other.getCollerctPostId()))
            && (this.getPost() == null ? other.getPost() == null : this.getPost().equals(other.getPost()))
            && (this.getIdentity() == null ? other.getIdentity() == null : this.getIdentity().equals(other.getIdentity()))
            && (this.getCollerctUserId() == null ? other.getCollerctUserId() == null : this.getCollerctUserId().equals(other.getCollerctUserId()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getDeleted() == null ? other.getDeleted() == null : this.getDeleted().equals(other.getDeleted()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getCollerctPostId() == null) ? 0 : getCollerctPostId().hashCode());
        result = prime * result + ((getPost() == null) ? 0 : getPost().hashCode());
        result = prime * result + ((getIdentity() == null) ? 0 : getIdentity().hashCode());
        result = prime * result + ((getCollerctUserId() == null) ? 0 : getCollerctUserId().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getDeleted() == null) ? 0 : getDeleted().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", collerctPostId=").append(collerctPostId);
        sb.append(", post=").append(post);
        sb.append(", identity=").append(identity);
        sb.append(", collerctUserId=").append(collerctUserId);
        sb.append(", createTime=").append(createTime);
        sb.append(", deleted=").append(deleted);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}