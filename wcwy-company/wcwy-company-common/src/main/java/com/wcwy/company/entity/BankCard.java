package com.wcwy.company.entity;

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
 * 银行卡
 * @TableName bank_card
 */
@TableName(value ="bank_card")
@Data
@ApiModel("银行卡")
public class BankCard implements Serializable {
    /**
     * 银行卡id
     */
    @TableId(value = "id")
    @ApiModelProperty("银行卡id")
    private String id;

    /**
     * 持卡人姓名
     */
    @TableField(value = "user_name")
    @ApiModelProperty("持卡人姓名")
    private String userName;

    /**
     * 预留手机号
     */
    @TableField(value = "user_tel")
    @ApiModelProperty("预留手机号")
    private String userTel;

    /**
     * 开户支行
     */
    @TableField(value = "bank_sub_name")
    @ApiModelProperty("开户支行")
    private String bankSubName;

    /**
     * 开户银行
     */
    @TableField(value = "bank_name")
    @ApiModelProperty("开户银行")
    private String bankName;

    /**
     * 申请人账户
     */
    @TableField(value = "bank_num")
    @ApiModelProperty("申请人账户")
    private String bankNum;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    @ApiModelProperty("用户id")
    private String userId;

    /**
     * 身份证姓名
     */
    @TableField(value = "real_name")
    @ApiModelProperty("身份证姓名")
    private String realName;

    /**
     * 身份证号
     */
    @TableField(value = "card")
    @ApiModelProperty("身份证号")
    private String card;

    /**
     * 身份证正面
     */
    @TableField(value = "card_front")
    @ApiModelProperty("身份证正面")
    private String cardFront;

    /**
     * 身份证反面
     */
    @TableField(value = "card_verso")
    @ApiModelProperty("身份证反面")
    private String cardVerso;

    /**
     * 审核状态(0:审核中 1:审核成功 2审核失败)
     */
    @TableField(value = "audit")
    @ApiModelProperty("审核状态(0:审核中 1:审核成功 2审核失败)")
    private Integer audit;

    /**
     * 审核原因
     */
    @TableField(value = "audit_cause")
    @ApiModelProperty("审核原因")
    private String auditCause;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @TableField(value = "audit_time")
    @ApiModelProperty("创建时间")
    private LocalDateTime auditTime;

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
        BankCard other = (BankCard) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserName() == null ? other.getUserName() == null : this.getUserName().equals(other.getUserName()))
            && (this.getUserTel() == null ? other.getUserTel() == null : this.getUserTel().equals(other.getUserTel()))
            && (this.getBankSubName() == null ? other.getBankSubName() == null : this.getBankSubName().equals(other.getBankSubName()))
            && (this.getBankName() == null ? other.getBankName() == null : this.getBankName().equals(other.getBankName()))
            && (this.getBankNum() == null ? other.getBankNum() == null : this.getBankNum().equals(other.getBankNum()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getRealName() == null ? other.getRealName() == null : this.getRealName().equals(other.getRealName()))
            && (this.getCard() == null ? other.getCard() == null : this.getCard().equals(other.getCard()))
            && (this.getCardFront() == null ? other.getCardFront() == null : this.getCardFront().equals(other.getCardFront()))
            && (this.getCardVerso() == null ? other.getCardVerso() == null : this.getCardVerso().equals(other.getCardVerso()))
            && (this.getAudit() == null ? other.getAudit() == null : this.getAudit().equals(other.getAudit()))
            && (this.getAuditCause() == null ? other.getAuditCause() == null : this.getAuditCause().equals(other.getAuditCause()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserName() == null) ? 0 : getUserName().hashCode());
        result = prime * result + ((getUserTel() == null) ? 0 : getUserTel().hashCode());
        result = prime * result + ((getBankSubName() == null) ? 0 : getBankSubName().hashCode());
        result = prime * result + ((getBankName() == null) ? 0 : getBankName().hashCode());
        result = prime * result + ((getBankNum() == null) ? 0 : getBankNum().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getRealName() == null) ? 0 : getRealName().hashCode());
        result = prime * result + ((getCard() == null) ? 0 : getCard().hashCode());
        result = prime * result + ((getCardFront() == null) ? 0 : getCardFront().hashCode());
        result = prime * result + ((getCardVerso() == null) ? 0 : getCardVerso().hashCode());
        result = prime * result + ((getAudit() == null) ? 0 : getAudit().hashCode());
        result = prime * result + ((getAuditCause() == null) ? 0 : getAuditCause().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userName=").append(userName);
        sb.append(", userTel=").append(userTel);
        sb.append(", bankSubName=").append(bankSubName);
        sb.append(", bankName=").append(bankName);
        sb.append(", bankNum=").append(bankNum);
        sb.append(", userId=").append(userId);
        sb.append(", realName=").append(realName);
        sb.append(", card=").append(card);
        sb.append(", cardFront=").append(cardFront);
        sb.append(", cardVerso=").append(cardVerso);
        sb.append(", audit=").append(audit);
        sb.append(", auditCause=").append(auditCause);
        sb.append(", createTime=").append(createTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}