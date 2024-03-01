package com.wcwy.post.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 发票抬头信息
 * @TableName invoice
 */
@TableName(value ="invoice")
@Data
@ApiModel(value = "发票抬头信息")
public class Invoice implements Serializable {
    /**
     * 发票抬头id
     */
    @TableId(value = "invoice_id")
    @ApiModelProperty("发票抬头id")
    private String invoiceId;

    /**
     * 抬头类型（1：个人 2：企业 3组织）
     */
    @TableField(value = "rise_type")
    @ApiModelProperty("抬头类型（1：个人 2：企业 3组织）")
    private Integer riseType;

    /**
     * 发票类型(1:增值税普通发票,2增值税专用发票)
     */
    @TableField(value = "Invoice_type")
    @ApiModelProperty("发票类型(1:增值税普通发票,2增值税专用发票)")
    private Integer invoiceType;

    /**
     * 发票抬头
     */
    @TableField(value = "invoice_title")
    @ApiModelProperty("发票抬头")
    private String invoiceTitle;

    /**
     * 纳税人识别号
     */
    @TableField(value = "identify_number")
    @ApiModelProperty("纳税人识别号")
    private String identifyNumber;

    /**
     * 基本开户银行
     */
    @TableField(value = "bank_of_deposit")
    @ApiModelProperty("基本开户银行")
    private String bankOfDeposit;

    /**
     * 基本开户银行账号
     */
    @TableField(value = "account_number")
    @ApiModelProperty("基本开户银行账号")
    private String accountNumber;

    /**
     * 企业注册地址
     */
    @TableField(value = "address")
    @ApiModelProperty("企业注册地址")
    private String address;

    /**
     * 企业注册电话
     */
    @TableField(value = "phone")
    @ApiModelProperty("企业注册电话")
    private String phone;

    /**
     * 绑定用户
     */
    @TableField(value = "binding_user")
    @ApiModelProperty("绑定用户")
    private String bindingUser;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @TableField(value = "tacitly_approve")
    @ApiModelProperty("默认(0:不默认 1:默认)")
    private Integer tacitlyApprove;
    /**
     * 创建人
     */
    @TableField(value = "create_user")
    @ApiModelProperty("创建人")
    private String createUser;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;

    /**
     * 更行人
     */
    @TableField(value = "update_user")
    @ApiModelProperty("更行人")
    private String updateUser;

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
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getInvoiceId() == null) ? 0 : getInvoiceId().hashCode());
        result = prime * result + ((getRiseType() == null) ? 0 : getRiseType().hashCode());
        result = prime * result + ((getInvoiceType() == null) ? 0 : getInvoiceType().hashCode());
        result = prime * result + ((getInvoiceTitle() == null) ? 0 : getInvoiceTitle().hashCode());
        result = prime * result + ((getIdentifyNumber() == null) ? 0 : getIdentifyNumber().hashCode());
        result = prime * result + ((getBankOfDeposit() == null) ? 0 : getBankOfDeposit().hashCode());
        result = prime * result + ((getAccountNumber() == null) ? 0 : getAccountNumber().hashCode());
        result = prime * result + ((getAddress() == null) ? 0 : getAddress().hashCode());
        result = prime * result + ((getPhone() == null) ? 0 : getPhone().hashCode());
        result = prime * result + ((getBindingUser() == null) ? 0 : getBindingUser().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getCreateUser() == null) ? 0 : getCreateUser().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getUpdateUser() == null) ? 0 : getUpdateUser().hashCode());
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
        sb.append(", riseType=").append(riseType);
        sb.append(", invoiceType=").append(invoiceType);
        sb.append(", invoiceTitle=").append(invoiceTitle);
        sb.append(", identifyNumber=").append(identifyNumber);
        sb.append(", bankOfDeposit=").append(bankOfDeposit);
        sb.append(", accountNumber=").append(accountNumber);
        sb.append(", address=").append(address);
        sb.append(", phone=").append(phone);
        sb.append(", bindingUser=").append(bindingUser);
        sb.append(", createTime=").append(createTime);
        sb.append(", createUser=").append(createUser);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", updateUser=").append(updateUser);
        sb.append(", deleted=").append(deleted);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}