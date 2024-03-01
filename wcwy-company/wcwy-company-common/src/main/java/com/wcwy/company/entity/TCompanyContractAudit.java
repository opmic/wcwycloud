package com.wcwy.company.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 合同审核记录
 * @TableName t_company_contract_audit
 */
@TableName(value ="t_company_contract_audit")
@Data
@ApiModel(value = "合同审核记录")
public class TCompanyContractAudit implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("id")
    private Long id;

    /**
     * 合同id
     */
    @TableField(value = "contract_id")
    @ApiModelProperty("合同id")
    private String contractId;

    /**
     * 审核时间
     */
    @TableField(value = "audit_time")
    @ApiModelProperty("审核时间")
    private LocalDateTime auditTime;

    /**
     * 审核原因
     */
    @TableField(value = "audit_cause")
    @ApiModelProperty("审核原因")
    private String auditCause;

    @TableField(value = "audit_contract")
    @ApiModelProperty("审核状态(0:审核中 1:审核成功 2:审核失败)")
    private Integer auditContract;
    /**
     * 客服电话
     */
    @TableField(value = "phone")
    @ApiModelProperty("客服电话")
    private String phone;

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
        TCompanyContractAudit other = (TCompanyContractAudit) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getContractId() == null ? other.getContractId() == null : this.getContractId().equals(other.getContractId()))
            && (this.getAuditTime() == null ? other.getAuditTime() == null : this.getAuditTime().equals(other.getAuditTime()))
            && (this.getAuditCause() == null ? other.getAuditCause() == null : this.getAuditCause().equals(other.getAuditCause()))
            && (this.getPhone() == null ? other.getPhone() == null : this.getPhone().equals(other.getPhone()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getContractId() == null) ? 0 : getContractId().hashCode());
        result = prime * result + ((getAuditTime() == null) ? 0 : getAuditTime().hashCode());
        result = prime * result + ((getAuditCause() == null) ? 0 : getAuditCause().hashCode());
        result = prime * result + ((getPhone() == null) ? 0 : getPhone().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", contractId=").append(contractId);
        sb.append(", auditTime=").append(auditTime);
        sb.append(", auditCause=").append(auditCause);
        sb.append(", phone=").append(phone);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}