package com.wcwy.company.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 职位表
 * @TableName t_position
 */
@TableName(value ="t_position")
@Data
@ApiModel(value = "职位表")
public class TPosition implements Serializable {
    /**
     * 职位id
     */
    @TableId(value = "position_id", type = IdType.AUTO)
    @ApiModelProperty(value = "职位id")
    private Integer positionId;

    /**
     * 职位
     */
    @TableField(value = "position")
    @ApiModelProperty(value="职位")
    private String position;

    /**
     * 逻辑删除
     */
    @TableField(value = "deleted")
    @TableLogic
    private Integer deleted;


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
        TPosition other = (TPosition) that;
        return (this.getPositionId() == null ? other.getPositionId() == null : this.getPositionId().equals(other.getPositionId()))
            && (this.getPosition() == null ? other.getPosition() == null : this.getPosition().equals(other.getPosition()))
            && (this.getDeleted() == null ? other.getDeleted() == null : this.getDeleted().equals(other.getDeleted()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getPositionId() == null) ? 0 : getPositionId().hashCode());
        result = prime * result + ((getPosition() == null) ? 0 : getPosition().hashCode());
        result = prime * result + ((getDeleted() == null) ? 0 : getDeleted().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", positionId=").append(positionId);
        sb.append(", position=").append(position);
        sb.append(", deleted=").append(deleted);
        sb.append("]");
        return sb.toString();
    }
}