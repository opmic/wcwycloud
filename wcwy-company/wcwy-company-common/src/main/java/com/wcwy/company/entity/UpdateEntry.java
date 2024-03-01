package com.wcwy.company.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 修改邀请入职记录表
 * @TableName update_entry
 */
@TableName(value ="update_entry")
@Data
@ApiModel("修改邀请入职记录表")
public class UpdateEntry implements Serializable {
    /**
     * 修改入职id
     */
    @TableId(value = "update_entry_id")
    @ApiModelProperty("修改入职id")
    private String updateEntryId;

    /**
     * 修改时间
     */
    @TableField(value = "update_entry_time")
    @ApiModelProperty("修改时间")
    private LocalDate updateEntryTime;

    /**
     * 邀请offerid
     */
    @TableField(value = "invite_entry_id")
    @ApiModelProperty("邀请offerid")
    private String inviteEntryId;

    /**
     * 修改原因
     */
    @TableField(value = "update_cause")
    @ApiModelProperty("修改原因")
    private String updateCause;

    /**
     * 管理员审核状态(0:处理中 1:同意 2:不同意)
     */
    @TableField(value = "admin_audit")
    @ApiModelProperty("管理员审核状态(0:处理中 1:同意 2:不同意)")
    private Integer adminAudit;

    /**
     * 管理员处理时间
     */
    @TableField(value = "admin_time")
    @ApiModelProperty("管理员处理时间")
    private LocalDateTime adminTime;

    /**
     * 不同意原因
     */
    @TableField(value = "admin_audit_cause")
    @ApiModelProperty("不同意原因")
    private String adminAuditCause;

    /**
     * 处理人
     */
    @TableField(value = "admin_id")
    @ApiModelProperty("处理人")
    private String adminId;

    /**
     * 投简人id
     */
    @TableField(value = "put_id")
    @ApiModelProperty("投简人id")
    private String putId;

    /**
     * 投简人审核状态(0:处理中 1:同意 2:不同意)
     */
    @TableField(value = "put_audit")
    @ApiModelProperty("投简人审核状态(0:处理中 1:同意 2:不同意)")
    private Integer putAudit;

    /**
     * 投简人处理时间
     */
    @TableField(value = "put_time")
    @ApiModelProperty("投简人处理时间")
    private LocalDateTime putTime;

    /**
     * 投简人不同意原因
     */
    @TableField(value = "put_audit_cause")
    @ApiModelProperty("投简人不同意原因")
    private String putAuditCause;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    @TableField(value = "create_id")
    @ApiModelProperty("创建人")
    private String createId;

    /**
     * 逻辑删除
     */
    @TableField(value = "deleted")
    @ApiModelProperty("逻辑删除")
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
        UpdateEntry other = (UpdateEntry) that;
        return (this.getUpdateEntryId() == null ? other.getUpdateEntryId() == null : this.getUpdateEntryId().equals(other.getUpdateEntryId()))
            && (this.getUpdateEntryTime() == null ? other.getUpdateEntryTime() == null : this.getUpdateEntryTime().equals(other.getUpdateEntryTime()))
            && (this.getInviteEntryId() == null ? other.getInviteEntryId() == null : this.getInviteEntryId().equals(other.getInviteEntryId()))
            && (this.getUpdateCause() == null ? other.getUpdateCause() == null : this.getUpdateCause().equals(other.getUpdateCause()))
            && (this.getAdminAudit() == null ? other.getAdminAudit() == null : this.getAdminAudit().equals(other.getAdminAudit()))
            && (this.getAdminTime() == null ? other.getAdminTime() == null : this.getAdminTime().equals(other.getAdminTime()))
            && (this.getAdminAuditCause() == null ? other.getAdminAuditCause() == null : this.getAdminAuditCause().equals(other.getAdminAuditCause()))
            && (this.getAdminId() == null ? other.getAdminId() == null : this.getAdminId().equals(other.getAdminId()))
            && (this.getPutId() == null ? other.getPutId() == null : this.getPutId().equals(other.getPutId()))
            && (this.getPutAudit() == null ? other.getPutAudit() == null : this.getPutAudit().equals(other.getPutAudit()))
            && (this.getPutTime() == null ? other.getPutTime() == null : this.getPutTime().equals(other.getPutTime()))
            && (this.getPutAuditCause() == null ? other.getPutAuditCause() == null : this.getPutAuditCause().equals(other.getPutAuditCause()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getCreateId() == null ? other.getCreateId() == null : this.getCreateId().equals(other.getCreateId()))
            && (this.getDeleted() == null ? other.getDeleted() == null : this.getDeleted().equals(other.getDeleted()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getUpdateEntryId() == null) ? 0 : getUpdateEntryId().hashCode());
        result = prime * result + ((getUpdateEntryTime() == null) ? 0 : getUpdateEntryTime().hashCode());
        result = prime * result + ((getInviteEntryId() == null) ? 0 : getInviteEntryId().hashCode());
        result = prime * result + ((getUpdateCause() == null) ? 0 : getUpdateCause().hashCode());
        result = prime * result + ((getAdminAudit() == null) ? 0 : getAdminAudit().hashCode());
        result = prime * result + ((getAdminTime() == null) ? 0 : getAdminTime().hashCode());
        result = prime * result + ((getAdminAuditCause() == null) ? 0 : getAdminAuditCause().hashCode());
        result = prime * result + ((getAdminId() == null) ? 0 : getAdminId().hashCode());
        result = prime * result + ((getPutId() == null) ? 0 : getPutId().hashCode());
        result = prime * result + ((getPutAudit() == null) ? 0 : getPutAudit().hashCode());
        result = prime * result + ((getPutTime() == null) ? 0 : getPutTime().hashCode());
        result = prime * result + ((getPutAuditCause() == null) ? 0 : getPutAuditCause().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getCreateId() == null) ? 0 : getCreateId().hashCode());
        result = prime * result + ((getDeleted() == null) ? 0 : getDeleted().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", updateEntryId=").append(updateEntryId);
        sb.append(", updateEntryTime=").append(updateEntryTime);
        sb.append(", inviteEntryId=").append(inviteEntryId);
        sb.append(", updateCause=").append(updateCause);
        sb.append(", adminAudit=").append(adminAudit);
        sb.append(", adminTime=").append(adminTime);
        sb.append(", adminAuditCause=").append(adminAuditCause);
        sb.append(", adminId=").append(adminId);
        sb.append(", putId=").append(putId);
        sb.append(", putAudit=").append(putAudit);
        sb.append(", putTime=").append(putTime);
        sb.append(", putAuditCause=").append(putAuditCause);
        sb.append(", createTime=").append(createTime);
        sb.append(", createId=").append(createId);
        sb.append(", deleted=").append(deleted);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}