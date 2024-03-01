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
 * 公司规模字典表
 * @TableName t_company_firm_size
 */
@TableName(value ="t_company_firm_size")
@Data
@ApiModel(value = "公司规模字典表")
public class TCompanyFirmSize implements Serializable {
    /**
     * 企业规模id
     */
    @TableId(value = "firm_id", type = IdType.AUTO)
    @ApiModelProperty(value ="企业规模id" )
    private Integer firmId;

    /**
     * 企业规模
     */
    @TableField(value = "firm_size")
    @ApiModelProperty(value ="企业规模" )
    private String firmSize;


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
        TCompanyFirmSize other = (TCompanyFirmSize) that;
        return (this.getFirmId() == null ? other.getFirmId() == null : this.getFirmId().equals(other.getFirmId()))
            && (this.getFirmSize() == null ? other.getFirmSize() == null : this.getFirmSize().equals(other.getFirmSize()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getFirmId() == null) ? 0 : getFirmId().hashCode());
        result = prime * result + ((getFirmSize() == null) ? 0 : getFirmSize().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", firmId=").append(firmId);
        sb.append(", firmSize=").append(firmSize);
        sb.append("]");
        return sb.toString();
    }
}