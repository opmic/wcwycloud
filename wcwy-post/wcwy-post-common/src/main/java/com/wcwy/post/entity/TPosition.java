package com.wcwy.post.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 职位表
 * @TableName t_position
 */
@TableName(value ="t_position")
@Data
public class TPosition implements Serializable {
    /**
     * 职位id
     */
    @TableId(value = "code", type = IdType.INPUT)
    private Integer code;

    /**
     * 类型名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 几级标题
     */
    @TableField(value = "rank")
    private Integer rank;

    /**
     * 父类
     */
    @TableField(value = "father_code")
    private Integer fatherCode;

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
        TPosition other = (TPosition) that;
        return (this.getCode() == null ? other.getCode() == null : this.getCode().equals(other.getCode()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getRank() == null ? other.getRank() == null : this.getRank().equals(other.getRank()))
            && (this.getFatherCode() == null ? other.getFatherCode() == null : this.getFatherCode().equals(other.getFatherCode()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getCode() == null) ? 0 : getCode().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getRank() == null) ? 0 : getRank().hashCode());
        result = prime * result + ((getFatherCode() == null) ? 0 : getFatherCode().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", code=").append(code);
        sb.append(", name=").append(name);
        sb.append(", rank=").append(rank);
        sb.append(", fatherCode=").append(fatherCode);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}