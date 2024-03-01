package com.wcwy.post.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.wcwy.company.po.ProvincesCitiesPO;
import com.wcwy.company.po.TIndustryAndTypePO;
import lombok.Data;

/**
 * 企业热度表
 * @TableName t_company_hot
 */
@TableName(value ="t_company_hot")
@Data
public class TCompanyHot implements Serializable {
    /**
     * 热门id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 热度
     */
    @TableField(value = "hot")
    private Long hot;

    /**
     * 企业名称
     */
    @TableField(value = "company_name")
    private String companyName;

    /**
     * 公司log
     */
    @TableField(value = "logo")
    private String logo;
    /**
     * 自定义logo
     */
    @TableField(value = "custom_logo")
    private String custom_logo;
    /**
     * 公司类型id
     */
    @TableField(value = "company_type_id")
    private String companyTypeId;

    /**
     * 行业类别
     */
    @TableField(value = "industry",typeHandler = JacksonTypeHandler.class)
    private List<TIndustryAndTypePO> industry;

    /**
     * 企业规模
     */
    @TableField(value = "firm_size")
    private String firmSize;

    /**
     * 企业id
     */
    @TableField(value = "company_id")
    private String companyId;

    /**
     * 逻辑删除
     */
    @TableField(value = "deleted")
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
        TCompanyHot other = (TCompanyHot) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getHot() == null ? other.getHot() == null : this.getHot().equals(other.getHot()))
            && (this.getCompanyName() == null ? other.getCompanyName() == null : this.getCompanyName().equals(other.getCompanyName()))
            && (this.getLogo() == null ? other.getLogo() == null : this.getLogo().equals(other.getLogo()))
            && (this.getCompanyTypeId() == null ? other.getCompanyTypeId() == null : this.getCompanyTypeId().equals(other.getCompanyTypeId()))
            && (this.getIndustry() == null ? other.getIndustry() == null : this.getIndustry().equals(other.getIndustry()))
            && (this.getFirmSize() == null ? other.getFirmSize() == null : this.getFirmSize().equals(other.getFirmSize()))
            && (this.getCompanyId() == null ? other.getCompanyId() == null : this.getCompanyId().equals(other.getCompanyId()))
            && (this.getDeleted() == null ? other.getDeleted() == null : this.getDeleted().equals(other.getDeleted()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getHot() == null) ? 0 : getHot().hashCode());
        result = prime * result + ((getCompanyName() == null) ? 0 : getCompanyName().hashCode());
        result = prime * result + ((getLogo() == null) ? 0 : getLogo().hashCode());
        result = prime * result + ((getCompanyTypeId() == null) ? 0 : getCompanyTypeId().hashCode());
        result = prime * result + ((getIndustry() == null) ? 0 : getIndustry().hashCode());
        result = prime * result + ((getFirmSize() == null) ? 0 : getFirmSize().hashCode());
        result = prime * result + ((getCompanyId() == null) ? 0 : getCompanyId().hashCode());
        result = prime * result + ((getDeleted() == null) ? 0 : getDeleted().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", hot=").append(hot);
        sb.append(", companyName=").append(companyName);
        sb.append(", logo=").append(logo);
        sb.append(", companyTypeId=").append(companyTypeId);
        sb.append(", industry=").append(industry);
        sb.append(", firmSize=").append(firmSize);
        sb.append(", companyId=").append(companyId);
        sb.append(", deleted=").append(deleted);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}