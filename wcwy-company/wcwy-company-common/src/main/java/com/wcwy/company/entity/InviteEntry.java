package com.wcwy.company.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 发送offer表
 * @TableName invite_entry
 */
@TableName(value ="invite_entry" ,autoResultMap = true)
@Data
@ApiModel(value = "发送offer表")
public class InviteEntry implements Serializable {
    /**
     * 邀请入职表
     */
    @TableId(value = "invite_entry_id")
    @ApiModelProperty(value = "邀请入职表")
    private String inviteEntryId;

    /**
     * 合同路径
     */
    @TableField(value = "contract")
    @ApiModelProperty(value = "合同路径")
    private String contract;

    /**
     * offer路径
     */
    @TableField(value = "offer_path")
    @ApiModelProperty(value = "offer路径")
    private String offerPath;


    @TableField(value = "appointee")
    @ApiModelProperty(value = "录用人")
    private String appointee;
    /**
     * 入职日期
     */
    @TableField(value = "entry_date")
    @ApiModelProperty(value = "入职日期")
    private LocalDate entryDate;

    /**
     * 入职时间
     */
    @TableField(value = "entry_time")
    @ApiModelProperty(value = "入职时间")
    private LocalTime entryTime;

    /**
     * 入职岗位id
     */
    @TableField(value = "post_id")
    @ApiModelProperty(value = "入职岗位id")
    private String postId;

    /**
     * 投简id
     */
    @TableField(value = "put_in_resume_id")
    @ApiModelProperty(value = "投简id")
    private String putInResumeId;

    /**
     * 岗位名称
     */
    @TableField(value = "post_name")
    @ApiModelProperty(value = "岗位名称")
    private String postName;

    /**
     * 赏金金额
     */
    @TableField(value = "hired_bounty")
    @ApiModelProperty(value = "税前薪资")
    private BigDecimal hiredBounty;
    /**
     * 赏金金额
     */
    @ApiModelProperty(value = "赏金金额" )
    @TableField(value = "money_reward", typeHandler = JacksonTypeHandler.class)
    private Object moneyReward;


    /**
     * 对接人姓名
     */
    @TableField(value = "received_by")
    @ApiModelProperty(value = "对接人姓名")
    private String receivedBy;

    /**
     * 电话号码
     */
    @TableField(value = "received_by_phone")
    @ApiModelProperty(value = "电话号码")
    private String receivedByPhone;

    /**
     * 备注
     */
    @TableField(value = "remark")
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 入职年薪薪水
     */
    @TableField(value = "salary")
    @ApiModelProperty(value = "入职年薪薪水")
    private BigDecimal salary;

    /**
     * 入职取消申请(1:未发送取消申请 2:已发送取消申请)
     */
    @TableField(value = "cancel")
    @ApiModelProperty(value = "入职取消申请(1:未发送取消申请 2:已发送取消申请)")
    private Integer cancel;

    /**
     * 佣金率
     */
    @TableField(value = "percentage")
    @ApiModelProperty(value = "佣金率")
    private Integer percentage;

    /**
     * 取消原因
     */
    @TableField(value = "cancel_cause")
    @ApiModelProperty(value = " 取消原因")
    private String cancelCause;

    /**
     * offer状态(1:正常 2:取消)
     */
    @TableField(value = "state_if")
    @ApiModelProperty(value = " offer状态(1:正常 2:取消)")
    private Integer stateIf;

    /**
     * 发送取消申请时间
     */
    @TableField(value = "cancel_time")
    @ApiModelProperty(value = "发送取消申请时间")
    private LocalDateTime cancelTime;

    /**
     * 第几个工作日/保证期天数
     */
    @TableField(value = "workday")
    @ApiModelProperty(value = " 第几个工作日/保证期天数")
    private Integer workday;

    /**
     * 投简人是否同意(0:未处理 1:不同意 2:已同意)
     */
    @TableField(value = "put_in_consent")
    @ApiModelProperty(value = "投简人是否同意(0:未处理 1:不同意 2:已同意 3客服介入)")
    private Integer putInConsent;

    /**
     * 岗位发布类型(0普通岗位 1:入职付 2满月付 3到面付岗位 4:简历付(校园) 5:简历付(职场)
     */
    @TableField(value = "post_type")
    @ApiModelProperty(value = "岗位发布类型(0普通岗位 1:入职付 2满月付 3到面付岗位 4:简历付(校园) 5:简历付(职场)")
    private Integer postType;

    /**
     * 投简人修改时间
     */
    @TableField(value = "update_consent_time")
    @ApiModelProperty(value = "投简人操作时间")
    private LocalDateTime updateConsentTime;

    /**
     * 不同意原因
     */
    @TableField(value = "consent_cause")
    @ApiModelProperty(value = "不同意原因")
    private String consentCause;
    /**
     *  创建人
     */
    @TableField(value = "create_id")
    @ApiModelProperty(value ="创建人" )
    private String createId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @ApiModelProperty(value ="创建时间" )
    private LocalDateTime createTime;
    /**
     * 逻辑删除
     */
    @TableField(value = "deleted")
    @ApiModelProperty(value = "逻辑删除")
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
        InviteEntry other = (InviteEntry) that;
        return (this.getInviteEntryId() == null ? other.getInviteEntryId() == null : this.getInviteEntryId().equals(other.getInviteEntryId()))
            && (this.getContract() == null ? other.getContract() == null : this.getContract().equals(other.getContract()))
            && (this.getOfferPath() == null ? other.getOfferPath() == null : this.getOfferPath().equals(other.getOfferPath()))
            && (this.getEntryDate() == null ? other.getEntryDate() == null : this.getEntryDate().equals(other.getEntryDate()))
            && (this.getEntryTime() == null ? other.getEntryTime() == null : this.getEntryTime().equals(other.getEntryTime()))
            && (this.getPostId() == null ? other.getPostId() == null : this.getPostId().equals(other.getPostId()))
            && (this.getPutInResumeId() == null ? other.getPutInResumeId() == null : this.getPutInResumeId().equals(other.getPutInResumeId()))
            && (this.getPostName() == null ? other.getPostName() == null : this.getPostName().equals(other.getPostName()))
            && (this.getHiredBounty() == null ? other.getHiredBounty() == null : this.getHiredBounty().equals(other.getHiredBounty()))
            && (this.getReceivedBy() == null ? other.getReceivedBy() == null : this.getReceivedBy().equals(other.getReceivedBy()))
            && (this.getReceivedByPhone() == null ? other.getReceivedByPhone() == null : this.getReceivedByPhone().equals(other.getReceivedByPhone()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()))
            && (this.getSalary() == null ? other.getSalary() == null : this.getSalary().equals(other.getSalary()))
            && (this.getCancel() == null ? other.getCancel() == null : this.getCancel().equals(other.getCancel()))
            && (this.getPercentage() == null ? other.getPercentage() == null : this.getPercentage().equals(other.getPercentage()))
            && (this.getCancelCause() == null ? other.getCancelCause() == null : this.getCancelCause().equals(other.getCancelCause()))
            && (this.getStateIf() == null ? other.getStateIf() == null : this.getStateIf().equals(other.getStateIf()))
            && (this.getCancelTime() == null ? other.getCancelTime() == null : this.getCancelTime().equals(other.getCancelTime()))
            && (this.getWorkday() == null ? other.getWorkday() == null : this.getWorkday().equals(other.getWorkday()))
            && (this.getPutInConsent() == null ? other.getPutInConsent() == null : this.getPutInConsent().equals(other.getPutInConsent()))
            && (this.getPostType() == null ? other.getPostType() == null : this.getPostType().equals(other.getPostType()))
            && (this.getUpdateConsentTime() == null ? other.getUpdateConsentTime() == null : this.getUpdateConsentTime().equals(other.getUpdateConsentTime()))
            && (this.getConsentCause() == null ? other.getConsentCause() == null : this.getConsentCause().equals(other.getConsentCause()))
            && (this.getDeleted() == null ? other.getDeleted() == null : this.getDeleted().equals(other.getDeleted()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getInviteEntryId() == null) ? 0 : getInviteEntryId().hashCode());
        result = prime * result + ((getContract() == null) ? 0 : getContract().hashCode());
        result = prime * result + ((getOfferPath() == null) ? 0 : getOfferPath().hashCode());
        result = prime * result + ((getEntryDate() == null) ? 0 : getEntryDate().hashCode());
        result = prime * result + ((getEntryTime() == null) ? 0 : getEntryTime().hashCode());
        result = prime * result + ((getPostId() == null) ? 0 : getPostId().hashCode());
        result = prime * result + ((getPutInResumeId() == null) ? 0 : getPutInResumeId().hashCode());
        result = prime * result + ((getPostName() == null) ? 0 : getPostName().hashCode());
        result = prime * result + ((getHiredBounty() == null) ? 0 : getHiredBounty().hashCode());
        result = prime * result + ((getReceivedBy() == null) ? 0 : getReceivedBy().hashCode());
        result = prime * result + ((getReceivedByPhone() == null) ? 0 : getReceivedByPhone().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        result = prime * result + ((getSalary() == null) ? 0 : getSalary().hashCode());
        result = prime * result + ((getCancel() == null) ? 0 : getCancel().hashCode());
        result = prime * result + ((getPercentage() == null) ? 0 : getPercentage().hashCode());
        result = prime * result + ((getCancelCause() == null) ? 0 : getCancelCause().hashCode());
        result = prime * result + ((getStateIf() == null) ? 0 : getStateIf().hashCode());
        result = prime * result + ((getCancelTime() == null) ? 0 : getCancelTime().hashCode());
        result = prime * result + ((getWorkday() == null) ? 0 : getWorkday().hashCode());
        result = prime * result + ((getPutInConsent() == null) ? 0 : getPutInConsent().hashCode());
        result = prime * result + ((getPostType() == null) ? 0 : getPostType().hashCode());
        result = prime * result + ((getUpdateConsentTime() == null) ? 0 : getUpdateConsentTime().hashCode());
        result = prime * result + ((getConsentCause() == null) ? 0 : getConsentCause().hashCode());
        result = prime * result + ((getDeleted() == null) ? 0 : getDeleted().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", inviteEntryId=").append(inviteEntryId);
        sb.append(", contract=").append(contract);
        sb.append(", offerPath=").append(offerPath);
        sb.append(", entryDate=").append(entryDate);
        sb.append(", entryTime=").append(entryTime);
        sb.append(", postId=").append(postId);
        sb.append(", putInResumeId=").append(putInResumeId);
        sb.append(", postName=").append(postName);
        sb.append(", hiredBounty=").append(hiredBounty);
        sb.append(", receivedBy=").append(receivedBy);
        sb.append(", receivedByPhone=").append(receivedByPhone);
        sb.append(", remark=").append(remark);
        sb.append(", salary=").append(salary);
        sb.append(", cancel=").append(cancel);
        sb.append(", percentage=").append(percentage);
        sb.append(", cancelCause=").append(cancelCause);
        sb.append(", stateIf=").append(stateIf);
        sb.append(", cancelTime=").append(cancelTime);
        sb.append(", workday=").append(workday);
        sb.append(", putInConsent=").append(putInConsent);
        sb.append(", postType=").append(postType);
        sb.append(", updateConsentTime=").append(updateConsentTime);
        sb.append(", consentCause=").append(consentCause);
        sb.append(", deleted=").append(deleted);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}