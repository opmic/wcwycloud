package com.wcwy.company.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 学校表
 * @TableName school
 */
@TableName(value ="school")
@Data
@ApiModel(value = "学校表")
public class School implements Serializable {
    /**
     * 主键id
     */
    @TableId(value = "id",type = IdType.AUTO)
    @ApiModelProperty(value ="主键id" )
    private Integer id;

    /**
     * 学校id
     */
    @TableField(value = "school_id")
    @ApiModelProperty(value ="学校id" )
    private String schoolId;

    /**
     * 学校名字
     */
    @TableField(value = "school_name")
    @ApiModelProperty(value ="学校名字" )
    private String schoolName;

    /**
     * 民办公办
     */
    @TableField(value = "other")
    @ApiModelProperty(value ="民办公办" )
    private String other;

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
        School other = (School) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getSchoolId() == null ? other.getSchoolId() == null : this.getSchoolId().equals(other.getSchoolId()))
            && (this.getSchoolName() == null ? other.getSchoolName() == null : this.getSchoolName().equals(other.getSchoolName()))
            && (this.getOther() == null ? other.getOther() == null : this.getOther().equals(other.getOther()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getSchoolId() == null) ? 0 : getSchoolId().hashCode());
        result = prime * result + ((getSchoolName() == null) ? 0 : getSchoolName().hashCode());
        result = prime * result + ((getOther() == null) ? 0 : getOther().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", schoolId=").append(schoolId);
        sb.append(", schoolName=").append(schoolName);
        sb.append(", other=").append(other);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}