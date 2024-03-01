package com.wcwy.post.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 职位福利表
 * @TableName post_weal
 */
@TableName(value ="post_weal")
@Data
@ApiModel(value = "职位福利表")
public class PostWeal implements Serializable {
    /**
     * 岗位福利id
     */
    @TableId(value = "weal_id", type = IdType.AUTO)
    @ApiModelProperty(value = "岗位福利id")
    private Long wealId;

    /**
     * 岗位福利
     */
    @TableField(value = "weal")
    @ApiModelProperty("岗位福利")
    private String weal;

    /**
     * 主节点(0为主节点)
     */
    @TableField(value = "parebt")
    @ApiModelProperty("主节点(0为主节点)")
    private Long parebt;

    /**
     * 逻辑删除
     */
    @TableField(value = "deleted")
    @ApiModelProperty("逻辑删除")
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
        PostWeal other = (PostWeal) that;
        return (this.getWealId() == null ? other.getWealId() == null : this.getWealId().equals(other.getWealId()))
            && (this.getWeal() == null ? other.getWeal() == null : this.getWeal().equals(other.getWeal()))
            && (this.getParebt() == null ? other.getParebt() == null : this.getParebt().equals(other.getParebt()))
            && (this.getDeleted() == null ? other.getDeleted() == null : this.getDeleted().equals(other.getDeleted()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getWealId() == null) ? 0 : getWealId().hashCode());
        result = prime * result + ((getWeal() == null) ? 0 : getWeal().hashCode());
        result = prime * result + ((getParebt() == null) ? 0 : getParebt().hashCode());
        result = prime * result + ((getDeleted() == null) ? 0 : getDeleted().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", wealId=").append(wealId);
        sb.append(", weal=").append(weal);
        sb.append(", parebt=").append(parebt);
        sb.append(", deleted=").append(deleted);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}