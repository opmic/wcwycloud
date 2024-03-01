package com.wcwy.company.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.wcwy.company.po.ProvincesCitiesPO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 推荐官猎企业认证
 * @TableName headhunter_approve
 */
@TableName(value ="headhunter_approve", autoResultMap = true)
@Data
@ApiModel("推荐官猎企业认证")
public class HeadhunterApprove implements Serializable {
    /**
     * 猎头申请
     */
    @TableField(value = "headhunter_approve_id")
    @ApiModelProperty("猎头申请id")
    private String headhunterApproveId;

    /**
     * 推荐官id
     */
    @TableField(value = "recommend_id")
    @ApiModelProperty("推荐官id")
    private String recommendId;

    /**
     * 企业名称
     */
    @TableField(value = "firm_name")
    @ApiModelProperty("企业名称")
    private String firmName;

    /**
     * 法人姓名
     */
    @TableField(value = "legal_person")
    @ApiModelProperty("法人姓名")
    private String legalPerson;

    /**
     * 联系方式
     */
    @TableField(value = "phone")
    @ApiModelProperty("联系方式")
    private String phone;

    /**
     * 企业地址
     */
    @TableField(value = "address")
    @ApiModelProperty("企业地址")
    private String address;

    /**
     * 企业规模
     */
    @TableField(value = "scale")
    @ApiModelProperty("企业规模")
    private String scale;

    /**
     * 简介
     */
    @TableField(value = "brief_introduction")
    @ApiModelProperty("简介")
    private String briefIntroduction;

    /**
     * logo
     */
    @TableField(value = "logo")
    @ApiModelProperty("logo")
    private String logo;

    /**
     * 营业执照
     */
    @TableField(value = "business_license")
    @ApiModelProperty("营业执照")
    private String businessLicense;

    /**
     * 审核状态(0:审核中 1:审核成功 2:审核失败)
     */
    @TableField(value = "audit")
    @ApiModelProperty("审核状态(0:审核中 1:审核成功 2:审核失败)")
    private Integer audit;
    @TableField(value = "provinces_cities", typeHandler = JacksonTypeHandler.class)
    @ApiModelProperty(value = "所在省市")
    private ProvincesCitiesPO provincesCities;
    /**
     * 审核失败原因
     */
    @TableField(value = "audit_cause")
    @ApiModelProperty("审核失败原因")
    private String auditCause;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    @TableField(value = "create_name")
    @ApiModelProperty("创建人")
    private String createName;

    /**
     * 审核时间
     */
    @TableField(value = "audit_time")
    @ApiModelProperty("审核时间")
    private LocalDateTime auditTime;

    /**
     * 审核人
     */
    @TableField(value = "audit_name")
    @ApiModelProperty("审核人")
    private String auditName;

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
        HeadhunterApprove other = (HeadhunterApprove) that;
        return (this.getHeadhunterApproveId() == null ? other.getHeadhunterApproveId() == null : this.getHeadhunterApproveId().equals(other.getHeadhunterApproveId()))
            && (this.getRecommendId() == null ? other.getRecommendId() == null : this.getRecommendId().equals(other.getRecommendId()))
            && (this.getFirmName() == null ? other.getFirmName() == null : this.getFirmName().equals(other.getFirmName()))
            && (this.getLegalPerson() == null ? other.getLegalPerson() == null : this.getLegalPerson().equals(other.getLegalPerson()))
            && (this.getPhone() == null ? other.getPhone() == null : this.getPhone().equals(other.getPhone()))
            && (this.getAddress() == null ? other.getAddress() == null : this.getAddress().equals(other.getAddress()))
            && (this.getScale() == null ? other.getScale() == null : this.getScale().equals(other.getScale()))
            && (this.getBriefIntroduction() == null ? other.getBriefIntroduction() == null : this.getBriefIntroduction().equals(other.getBriefIntroduction()))
            && (this.getLogo() == null ? other.getLogo() == null : this.getLogo().equals(other.getLogo()))
            && (this.getBusinessLicense() == null ? other.getBusinessLicense() == null : this.getBusinessLicense().equals(other.getBusinessLicense()))
            && (this.getAudit() == null ? other.getAudit() == null : this.getAudit().equals(other.getAudit()))
            && (this.getAuditCause() == null ? other.getAuditCause() == null : this.getAuditCause().equals(other.getAuditCause()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getCreateName() == null ? other.getCreateName() == null : this.getCreateName().equals(other.getCreateName()))
            && (this.getAuditTime() == null ? other.getAuditTime() == null : this.getAuditTime().equals(other.getAuditTime()))
            && (this.getAuditName() == null ? other.getAuditName() == null : this.getAuditName().equals(other.getAuditName()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getHeadhunterApproveId() == null) ? 0 : getHeadhunterApproveId().hashCode());
        result = prime * result + ((getRecommendId() == null) ? 0 : getRecommendId().hashCode());
        result = prime * result + ((getFirmName() == null) ? 0 : getFirmName().hashCode());
        result = prime * result + ((getLegalPerson() == null) ? 0 : getLegalPerson().hashCode());
        result = prime * result + ((getPhone() == null) ? 0 : getPhone().hashCode());
        result = prime * result + ((getAddress() == null) ? 0 : getAddress().hashCode());
        result = prime * result + ((getScale() == null) ? 0 : getScale().hashCode());
        result = prime * result + ((getBriefIntroduction() == null) ? 0 : getBriefIntroduction().hashCode());
        result = prime * result + ((getLogo() == null) ? 0 : getLogo().hashCode());
        result = prime * result + ((getBusinessLicense() == null) ? 0 : getBusinessLicense().hashCode());
        result = prime * result + ((getAudit() == null) ? 0 : getAudit().hashCode());
        result = prime * result + ((getAuditCause() == null) ? 0 : getAuditCause().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getCreateName() == null) ? 0 : getCreateName().hashCode());
        result = prime * result + ((getAuditTime() == null) ? 0 : getAuditTime().hashCode());
        result = prime * result + ((getAuditName() == null) ? 0 : getAuditName().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", headhunterApproveId=").append(headhunterApproveId);
        sb.append(", recommendId=").append(recommendId);
        sb.append(", firmName=").append(firmName);
        sb.append(", legalPerson=").append(legalPerson);
        sb.append(", phone=").append(phone);
        sb.append(", address=").append(address);
        sb.append(", scale=").append(scale);
        sb.append(", briefIntroduction=").append(briefIntroduction);
        sb.append(", logo=").append(logo);
        sb.append(", businessLicense=").append(businessLicense);
        sb.append(", audit=").append(audit);
        sb.append(", auditCause=").append(auditCause);
        sb.append(", createTime=").append(createTime);
        sb.append(", createName=").append(createName);
        sb.append(", auditTime=").append(auditTime);
        sb.append(", auditName=").append(auditName);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}