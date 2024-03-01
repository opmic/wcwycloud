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
 * 行业类型表
 * @TableName t_industry_type
 */
@TableName(value ="t_industry_type")
@Data
@ApiModel(value = "行业类型表")
public class TIndustryType implements Serializable {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value ="主键ID" )
    private Integer id;

    /**
     * 标签编码
     */
    @TableField(value = "label_id")
    @ApiModelProperty(value ="标签编码" )
    private String labelId;

    /**
     * 标签名称
     */
    @TableField(value = "label_name")
    @ApiModelProperty(value ="标签名称" )
    private String labelName;

    /**
     * 所属行业编码
     */
    @TableField(value = "code")
    @ApiModelProperty(value ="所属行业编码" )
    private String code;


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
        TIndustryType other = (TIndustryType) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getLabelId() == null ? other.getLabelId() == null : this.getLabelId().equals(other.getLabelId()))
            && (this.getLabelName() == null ? other.getLabelName() == null : this.getLabelName().equals(other.getLabelName()))
            && (this.getCode() == null ? other.getCode() == null : this.getCode().equals(other.getCode()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getLabelId() == null) ? 0 : getLabelId().hashCode());
        result = prime * result + ((getLabelName() == null) ? 0 : getLabelName().hashCode());
        result = prime * result + ((getCode() == null) ? 0 : getCode().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", labelId=").append(labelId);
        sb.append(", labelName=").append(labelName);
        sb.append(", code=").append(code);
        sb.append("]");
        return sb.toString();
    }
}