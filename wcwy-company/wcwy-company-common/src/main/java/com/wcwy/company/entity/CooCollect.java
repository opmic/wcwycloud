package com.wcwy.company.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * coo收藏
 * @TableName coo_collect
 */
@TableName(value ="coo_collect")
@Data
@ApiModel("coo收藏")
public class CooCollect implements Serializable {
    /**
     * id
     */
    @TableId(value = "id")
    @ApiModelProperty("coo收藏id")
    private Long id;

    /**
     * id
     */
    @TableField(value = "coo_tribe")
    @ApiModelProperty("发帖id")
    private Long cooTribe;

    /**
     * 收藏时间
     */
    @TableField(value = "create_time")
    @ApiModelProperty("收藏时间")
    private LocalDateTime createTime;

    /**
     * 收藏人
     */
    @TableField(value = "create_user")
    @ApiModelProperty("收藏人")
    private String createUser;

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
        CooCollect other = (CooCollect) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getCooTribe() == null ? other.getCooTribe() == null : this.getCooTribe().equals(other.getCooTribe()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getCreateUser() == null ? other.getCreateUser() == null : this.getCreateUser().equals(other.getCreateUser()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getCooTribe() == null) ? 0 : getCooTribe().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getCreateUser() == null) ? 0 : getCreateUser().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", cooTribe=").append(cooTribe);
        sb.append(", createTime=").append(createTime);
        sb.append(", createUser=").append(createUser);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}