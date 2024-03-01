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
 * 岗位表
 * @TableName station
 */
@TableName(value ="station")
@Data
@ApiModel(value = "岗位表")
public class Station implements Serializable {
    /**
     * 职位id
     */
    @TableId(value = "position_id", type = IdType.AUTO)
    @ApiModelProperty(value = "职位id")
    private Long positionId;

    /**
     * 职位标签
     */
    @TableField(value = "position_label")
    @ApiModelProperty(value = "职位标签")
    private String positionLabel;

    /**
     * 行业类型id
     */
    @TableField(value = "industry_type_id")
    @ApiModelProperty(value = "行业类型id")
    private Integer industryTypeId;


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
        Station other = (Station) that;
        return (this.getPositionId() == null ? other.getPositionId() == null : this.getPositionId().equals(other.getPositionId()))
            && (this.getPositionLabel() == null ? other.getPositionLabel() == null : this.getPositionLabel().equals(other.getPositionLabel()))
            && (this.getIndustryTypeId() == null ? other.getIndustryTypeId() == null : this.getIndustryTypeId().equals(other.getIndustryTypeId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getPositionId() == null) ? 0 : getPositionId().hashCode());
        result = prime * result + ((getPositionLabel() == null) ? 0 : getPositionLabel().hashCode());
        result = prime * result + ((getIndustryTypeId() == null) ? 0 : getIndustryTypeId().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", positionId=").append(positionId);
        sb.append(", positionLabel=").append(positionLabel);
        sb.append(", industryTypeId=").append(industryTypeId);
        sb.append("]");
        return sb.toString();
    }
}