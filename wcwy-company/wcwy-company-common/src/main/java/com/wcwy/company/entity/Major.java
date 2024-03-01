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
 * 专业名称表
 * @TableName major
 */
@TableName(value ="major")
@Data
@ApiModel(value = "专业名称表")
public class Major implements Serializable {
    /**
     * 专业代码
     */
    @TableField(value = "majo_code")
    @ApiModelProperty(value = "专业代码")
    private String majoCode;

    /**
     * 专业名称
     */
    @TableField(value = "major_name")
    @ApiModelProperty(value = "专业名称")
    private String majorName;

    /**
     * 专业状态
     */
    @TableField(value = "major_state")
    @ApiModelProperty(value = "专业状态")
    private Integer majorState;

    /**
     * 父类id
     */
    @TableField(value = "parent_id")
    @ApiModelProperty(value = "父类id")
    private String parentId;

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
        Major other = (Major) that;
        return (this.getMajoCode() == null ? other.getMajoCode() == null : this.getMajoCode().equals(other.getMajoCode()))
            && (this.getMajorName() == null ? other.getMajorName() == null : this.getMajorName().equals(other.getMajorName()))
            && (this.getMajorState() == null ? other.getMajorState() == null : this.getMajorState().equals(other.getMajorState()))
            && (this.getParentId() == null ? other.getParentId() == null : this.getParentId().equals(other.getParentId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getMajoCode() == null) ? 0 : getMajoCode().hashCode());
        result = prime * result + ((getMajorName() == null) ? 0 : getMajorName().hashCode());
        result = prime * result + ((getMajorState() == null) ? 0 : getMajorState().hashCode());
        result = prime * result + ((getParentId() == null) ? 0 : getParentId().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", majoCode=").append(majoCode);
        sb.append(", majorName=").append(majorName);
        sb.append(", majorState=").append(majorState);
        sb.append(", parentId=").append(parentId);
        sb.append("]");
        return sb.toString();
    }
}