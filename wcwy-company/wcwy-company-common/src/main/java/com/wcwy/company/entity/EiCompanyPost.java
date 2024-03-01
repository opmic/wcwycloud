package com.wcwy.company.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.wcwy.company.po.ProvincesCitiesPO;
import lombok.Data;

/**
 * 岗位基础信息
 * @TableName ei_company_post
 */
@TableName(value ="ei_company_post",autoResultMap = true)
@Data
public class EiCompanyPost implements Serializable {
    /**
     * 岗位id
     */
    @TableId(value = "post_id")
    private String postId;

    /**
     * 企业id
     */
    @TableField(value = "company_id")
    private String companyId;

    /**
     * 岗位开始薪资
     */
    @TableField(value = "begin_salary")
    private BigDecimal beginSalary;

    /**
     * 岗位结束薪资
     */
    @TableField(value = "end_salary")
    private BigDecimal endSalary;

    /**
     * 所在城市
     */
    @TableField(value = "work_city",typeHandler = JacksonTypeHandler.class)
    private ProvincesCitiesPO workCity;
    /**
     * 岗位名称
     */
    @TableField(value = "post_label")
    private String postLabel;
    /**
     * 岗位发布类型(0普通岗位 1:入职付 2满月付 3到面付岗位  4:简历付校园 5:简历付职位)
     */
    @TableField(value = "post_type")
    private Integer postType;

    /**
     * 满月付赏金
     */
    @TableField(value = "money_reward")
    private Object moneyReward;
    @TableField(value = "company_name")
    private String companyName;
    /**
     * 入职付赏金
     */
    @TableField(value = "hired_bounty")
    private BigDecimal hiredBounty;

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
        EiCompanyPost other = (EiCompanyPost) that;
        return (this.getPostId() == null ? other.getPostId() == null : this.getPostId().equals(other.getPostId()))
            && (this.getCompanyId() == null ? other.getCompanyId() == null : this.getCompanyId().equals(other.getCompanyId()))
            && (this.getBeginSalary() == null ? other.getBeginSalary() == null : this.getBeginSalary().equals(other.getBeginSalary()))
            && (this.getEndSalary() == null ? other.getEndSalary() == null : this.getEndSalary().equals(other.getEndSalary()))
            && (this.getWorkCity() == null ? other.getWorkCity() == null : this.getWorkCity().equals(other.getWorkCity()))
            && (this.getPostType() == null ? other.getPostType() == null : this.getPostType().equals(other.getPostType()))
            && (this.getMoneyReward() == null ? other.getMoneyReward() == null : this.getMoneyReward().equals(other.getMoneyReward()))
            && (this.getHiredBounty() == null ? other.getHiredBounty() == null : this.getHiredBounty().equals(other.getHiredBounty()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getPostId() == null) ? 0 : getPostId().hashCode());
        result = prime * result + ((getCompanyId() == null) ? 0 : getCompanyId().hashCode());
        result = prime * result + ((getBeginSalary() == null) ? 0 : getBeginSalary().hashCode());
        result = prime * result + ((getEndSalary() == null) ? 0 : getEndSalary().hashCode());
        result = prime * result + ((getWorkCity() == null) ? 0 : getWorkCity().hashCode());
        result = prime * result + ((getPostType() == null) ? 0 : getPostType().hashCode());
        result = prime * result + ((getMoneyReward() == null) ? 0 : getMoneyReward().hashCode());
        result = prime * result + ((getHiredBounty() == null) ? 0 : getHiredBounty().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", postId=").append(postId);
        sb.append(", companyId=").append(companyId);
        sb.append(", beginSalary=").append(beginSalary);
        sb.append(", endSalary=").append(endSalary);
        sb.append(", workCity=").append(workCity);
        sb.append(", postType=").append(postType);
        sb.append(", moneyReward=").append(moneyReward);
        sb.append(", hiredBounty=").append(hiredBounty);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}