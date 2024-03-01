package com.wcwy.post.entity;

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
 * 发票审核
 * @TableName audit_invoice
 */
@TableName(value ="audit_invoice")
@Data
@ApiModel("发票审核")
public class AuditInvoice implements Serializable {
    /**
     * id
     */
    @ApiModelProperty("id")
    @TableId(value = "invoice_id", type = IdType.AUTO)
    private Long invoiceId;

    /**
     * 审核状态(0成功  1:失败)
     */
    @ApiModelProperty("审核状态(0成功  1:失败)")
    @TableField(value = "audit_sate")
    private Integer auditSate;

    /**
     * 原因
     */
    @ApiModelProperty("原因")
    @TableField(value = "reason")
    private String reason;

    /**
     * 客服电话
     */
    @ApiModelProperty("客服电话")
    @TableField(value = "phone")
    private String phone;

    /**
     * 反馈时间
     */
    @ApiModelProperty("反馈时间")
    @TableField(value = "carte_time")
    private LocalDateTime carteTime;

    /**
     * 申请发票编号
     */
    @ApiModelProperty("申请发票编号")
    @TableField(value = "apply_for_invoice")
    private String applyForInvoice;

    /**
     * 审核人
     */
    @ApiModelProperty("审核人")
    @TableField(value = "audit_user")
    private String auditUser;

    /**
     * 逻辑删除
     */
    @ApiModelProperty("逻辑删除")
    @TableField(value = "deleted")
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
        AuditInvoice other = (AuditInvoice) that;
        return (this.getInvoiceId() == null ? other.getInvoiceId() == null : this.getInvoiceId().equals(other.getInvoiceId()))
            && (this.getAuditSate() == null ? other.getAuditSate() == null : this.getAuditSate().equals(other.getAuditSate()))
            && (this.getReason() == null ? other.getReason() == null : this.getReason().equals(other.getReason()))
            && (this.getPhone() == null ? other.getPhone() == null : this.getPhone().equals(other.getPhone()))
            && (this.getCarteTime() == null ? other.getCarteTime() == null : this.getCarteTime().equals(other.getCarteTime()))
            && (this.getApplyForInvoice() == null ? other.getApplyForInvoice() == null : this.getApplyForInvoice().equals(other.getApplyForInvoice()))
            && (this.getAuditUser() == null ? other.getAuditUser() == null : this.getAuditUser().equals(other.getAuditUser()))
            && (this.getDeleted() == null ? other.getDeleted() == null : this.getDeleted().equals(other.getDeleted()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getInvoiceId() == null) ? 0 : getInvoiceId().hashCode());
        result = prime * result + ((getAuditSate() == null) ? 0 : getAuditSate().hashCode());
        result = prime * result + ((getReason() == null) ? 0 : getReason().hashCode());
        result = prime * result + ((getPhone() == null) ? 0 : getPhone().hashCode());
        result = prime * result + ((getCarteTime() == null) ? 0 : getCarteTime().hashCode());
        result = prime * result + ((getApplyForInvoice() == null) ? 0 : getApplyForInvoice().hashCode());
        result = prime * result + ((getAuditUser() == null) ? 0 : getAuditUser().hashCode());
        result = prime * result + ((getDeleted() == null) ? 0 : getDeleted().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", invoiceId=").append(invoiceId);
        sb.append(", auditSate=").append(auditSate);
        sb.append(", reason=").append(reason);
        sb.append(", phone=").append(phone);
        sb.append(", carteTime=").append(carteTime);
        sb.append(", applyForInvoice=").append(applyForInvoice);
        sb.append(", auditUser=").append(auditUser);
        sb.append(", deleted=").append(deleted);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}