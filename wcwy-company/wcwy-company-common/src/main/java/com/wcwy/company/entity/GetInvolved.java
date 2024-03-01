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
 * 客服介入表
 * @TableName get_involved
 */
@TableName(value ="get_involved")
@Data
@ApiModel(value = "客服介入表")
public class GetInvolved implements Serializable {
    /**
     * 介入编号
     */
    @TableId(value = "get_involved_id", type = IdType.AUTO)
    @ApiModelProperty(value = "介入编号")
    private Long getInvolvedId;

    /**
     * 投简id
     */
    @TableField(value = "put_in_resume_id")
    @ApiModelProperty(value = "投简id")
    private String putInResumeId;

    /**
     * 邀请入职表
     */
    @TableField(value = "invite_entry_id")
    @ApiModelProperty(value = "邀请入职表")
    private String inviteEntryId;

    /**
     * 处理状态(0:已收到 1:处理中 2:处理完成)
     */
    @TableField(value = "sate")
    @ApiModelProperty(value = " 处理状态(0:已收到 1:处理中 2:处理完成)")
    private Integer sate;

    /**
     * 处理反馈
     */
    @TableField(value = "result")
    @ApiModelProperty(value = "处理反馈")
    private String result;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    /**
     * 处理时间
     */
    @TableField(value = "dispose_time")
    @ApiModelProperty(value = "处理时间")
    private LocalDateTime disposeTime;

    /**
     * 处理人
     */
    @TableField(value = "dispose_name")
    @ApiModelProperty(value = "处理人")
    private String disposeName;

    /**
     * 介入原因
     */
    @TableField(value = "cause")
    @ApiModelProperty(value = "介入原因")
    private String cause;

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
        GetInvolved other = (GetInvolved) that;
        return (this.getGetInvolvedId() == null ? other.getGetInvolvedId() == null : this.getGetInvolvedId().equals(other.getGetInvolvedId()))
            && (this.getPutInResumeId() == null ? other.getPutInResumeId() == null : this.getPutInResumeId().equals(other.getPutInResumeId()))
            && (this.getInviteEntryId() == null ? other.getInviteEntryId() == null : this.getInviteEntryId().equals(other.getInviteEntryId()))
            && (this.getSate() == null ? other.getSate() == null : this.getSate().equals(other.getSate()))
            && (this.getResult() == null ? other.getResult() == null : this.getResult().equals(other.getResult()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getDisposeTime() == null ? other.getDisposeTime() == null : this.getDisposeTime().equals(other.getDisposeTime()))
            && (this.getDisposeName() == null ? other.getDisposeName() == null : this.getDisposeName().equals(other.getDisposeName()))
            && (this.getCause() == null ? other.getCause() == null : this.getCause().equals(other.getCause()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getGetInvolvedId() == null) ? 0 : getGetInvolvedId().hashCode());
        result = prime * result + ((getPutInResumeId() == null) ? 0 : getPutInResumeId().hashCode());
        result = prime * result + ((getInviteEntryId() == null) ? 0 : getInviteEntryId().hashCode());
        result = prime * result + ((getSate() == null) ? 0 : getSate().hashCode());
        result = prime * result + ((getResult() == null) ? 0 : getResult().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getDisposeTime() == null) ? 0 : getDisposeTime().hashCode());
        result = prime * result + ((getDisposeName() == null) ? 0 : getDisposeName().hashCode());
        result = prime * result + ((getCause() == null) ? 0 : getCause().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", getInvolvedId=").append(getInvolvedId);
        sb.append(", putInResumeId=").append(putInResumeId);
        sb.append(", inviteEntryId=").append(inviteEntryId);
        sb.append(", sate=").append(sate);
        sb.append(", result=").append(result);
        sb.append(", createTime=").append(createTime);
        sb.append(", disposeTime=").append(disposeTime);
        sb.append(", disposeName=").append(disposeName);
        sb.append(", cause=").append(cause);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}