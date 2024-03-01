package com.wcwy.post.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.wcwy.post.dto.CompanyPostDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 申请发票
 * @TableName apply_for_invoice
 */
@TableName(value ="apply_for_invoice",autoResultMap = true)
@Data
@ApiModel("申请发票")
public class ApplyForInvoice implements Serializable {
    /**
     * 申请发票id
     */
    @TableId(value = "apply_for_invoice_id")
    @ApiModelProperty("申请发票id")
    private String applyForInvoiceId;

    /**
     * 抬头类型（1：个人 2：企业 3组织）
     */
    @TableField(value = "rise_type")
    @ApiModelProperty("抬头类型（1：个人 2：企业 3组织）")
    private Integer riseType;

    /**
     * 发票类型(1:增值税普通发票(电子),2增值税专用发票(纸质))
     */
    @TableField(value = "invoice_type")
    @ApiModelProperty("发票类型(1:增值税普通发票(电子),2增值税专用发票(纸质))")
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
     * 发票性质(0未知1:纸质 2:电子)
     */
    @TableField(value = "type")
    @ApiModelProperty(" 发票性质(0未知1:纸质 2:电子)")
    private Integer type;

    /**
     * 邮寄地址id
     */
    @TableField(value = "consignee_address_id")
    @ApiModelProperty(" 邮寄地址id")
    private String consigneeAddressId;

    /**
     * 企业注册电话
     */
    @TableField(value = "phone")
    @ApiModelProperty(" 企业注册电话")
    private String phone;

    /**
     * 绑定订单
     */
    @TableField(value = "binding_order",typeHandler = JacksonTypeHandler.class)
    @ApiModelProperty(" 绑定订单")
    private List<String> bindingOrder;

    /**
     * 处理状态(0:申请中 1:申请成功 2:申请失败 3:取消申请)
     */
    @TableField(value = "process_state")
    @ApiModelProperty(" 处理状态(0:申请中 1:申请成功 2:申请失败 3:取消申请)")
    private Integer processState;

    /**
     * 申请失败原因
     */
    @TableField(value = "process_cause")
    @ApiModelProperty("申请失败原因")
    private String processCause;

    /**
     * 发票链接地址
     */
    @TableField(value = "invoice_address", typeHandler = JacksonTypeHandler.class)
    @ApiModelProperty("发票链接地址")
    private List<String> invoiceAddress;


    @TableField(value = "express_attachment")
    @ApiModelProperty("发票链接地址")
    private String expressAttachment;
    @TableField(value = "courier_number")
    @ApiModelProperty("发票链接地址")
    private String courierNumber;

    /**
     * 发票号码
     */
    @TableField(value = "number")
    @ApiModelProperty("发票号码")
    private String number;

    /**
     * 发票税率
     */
    @TableField(value = "tax_rate")
    @ApiModelProperty("发票税率")
    private Integer taxRate;

    /**
     * 处理时间
     */
    @TableField(value = "process_time")
    @ApiModelProperty("处理时间")
    private LocalDateTime processTime;

    /**
     * 备注
     */
    @TableField(value = "remark")
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 处理人
     */
    @TableField(value = "process_user")
    @ApiModelProperty("处理人")
    private String processUser;

    /**
     * 发票金额
     */
    @TableField(value = "money")
    @ApiModelProperty("发票金额")
    private BigDecimal money;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

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
     * 更新人
     */
    @TableField(value = "update_user")
    @ApiModelProperty("更新人")
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
        ApplyForInvoice other = (ApplyForInvoice) that;
        return (this.getApplyForInvoiceId() == null ? other.getApplyForInvoiceId() == null : this.getApplyForInvoiceId().equals(other.getApplyForInvoiceId()))
            && (this.getRiseType() == null ? other.getRiseType() == null : this.getRiseType().equals(other.getRiseType()))
            && (this.getInvoiceType() == null ? other.getInvoiceType() == null : this.getInvoiceType().equals(other.getInvoiceType()))
            && (this.getInvoiceTitle() == null ? other.getInvoiceTitle() == null : this.getInvoiceTitle().equals(other.getInvoiceTitle()))
            && (this.getIdentifyNumber() == null ? other.getIdentifyNumber() == null : this.getIdentifyNumber().equals(other.getIdentifyNumber()))
            && (this.getBankOfDeposit() == null ? other.getBankOfDeposit() == null : this.getBankOfDeposit().equals(other.getBankOfDeposit()))
            && (this.getAccountNumber() == null ? other.getAccountNumber() == null : this.getAccountNumber().equals(other.getAccountNumber()))
            && (this.getAddress() == null ? other.getAddress() == null : this.getAddress().equals(other.getAddress()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getConsigneeAddressId() == null ? other.getConsigneeAddressId() == null : this.getConsigneeAddressId().equals(other.getConsigneeAddressId()))
            && (this.getPhone() == null ? other.getPhone() == null : this.getPhone().equals(other.getPhone()))
            && (this.getBindingOrder() == null ? other.getBindingOrder() == null : this.getBindingOrder().equals(other.getBindingOrder()))
            && (this.getProcessState() == null ? other.getProcessState() == null : this.getProcessState().equals(other.getProcessState()))
            && (this.getProcessCause() == null ? other.getProcessCause() == null : this.getProcessCause().equals(other.getProcessCause()))
            && (this.getInvoiceAddress() == null ? other.getInvoiceAddress() == null : this.getInvoiceAddress().equals(other.getInvoiceAddress()))
            && (this.getNumber() == null ? other.getNumber() == null : this.getNumber().equals(other.getNumber()))
            && (this.getTaxRate() == null ? other.getTaxRate() == null : this.getTaxRate().equals(other.getTaxRate()))
            && (this.getProcessTime() == null ? other.getProcessTime() == null : this.getProcessTime().equals(other.getProcessTime()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()))
            && (this.getProcessUser() == null ? other.getProcessUser() == null : this.getProcessUser().equals(other.getProcessUser()))
            && (this.getMoney() == null ? other.getMoney() == null : this.getMoney().equals(other.getMoney()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getCreateUser() == null ? other.getCreateUser() == null : this.getCreateUser().equals(other.getCreateUser()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getUpdateUser() == null ? other.getUpdateUser() == null : this.getUpdateUser().equals(other.getUpdateUser()))
            && (this.getDeleted() == null ? other.getDeleted() == null : this.getDeleted().equals(other.getDeleted()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getApplyForInvoiceId() == null) ? 0 : getApplyForInvoiceId().hashCode());
        result = prime * result + ((getRiseType() == null) ? 0 : getRiseType().hashCode());
        result = prime * result + ((getInvoiceType() == null) ? 0 : getInvoiceType().hashCode());
        result = prime * result + ((getInvoiceTitle() == null) ? 0 : getInvoiceTitle().hashCode());
        result = prime * result + ((getIdentifyNumber() == null) ? 0 : getIdentifyNumber().hashCode());
        result = prime * result + ((getBankOfDeposit() == null) ? 0 : getBankOfDeposit().hashCode());
        result = prime * result + ((getAccountNumber() == null) ? 0 : getAccountNumber().hashCode());
        result = prime * result + ((getAddress() == null) ? 0 : getAddress().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getConsigneeAddressId() == null) ? 0 : getConsigneeAddressId().hashCode());
        result = prime * result + ((getPhone() == null) ? 0 : getPhone().hashCode());
        result = prime * result + ((getBindingOrder() == null) ? 0 : getBindingOrder().hashCode());
        result = prime * result + ((getProcessState() == null) ? 0 : getProcessState().hashCode());
        result = prime * result + ((getProcessCause() == null) ? 0 : getProcessCause().hashCode());
        result = prime * result + ((getInvoiceAddress() == null) ? 0 : getInvoiceAddress().hashCode());
        result = prime * result + ((getNumber() == null) ? 0 : getNumber().hashCode());
        result = prime * result + ((getTaxRate() == null) ? 0 : getTaxRate().hashCode());
        result = prime * result + ((getProcessTime() == null) ? 0 : getProcessTime().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        result = prime * result + ((getProcessUser() == null) ? 0 : getProcessUser().hashCode());
        result = prime * result + ((getMoney() == null) ? 0 : getMoney().hashCode());
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
        sb.append(", applyForInvoiceId=").append(applyForInvoiceId);
        sb.append(", riseType=").append(riseType);
        sb.append(", invoiceType=").append(invoiceType);
        sb.append(", invoiceTitle=").append(invoiceTitle);
        sb.append(", identifyNumber=").append(identifyNumber);
        sb.append(", bankOfDeposit=").append(bankOfDeposit);
        sb.append(", accountNumber=").append(accountNumber);
        sb.append(", address=").append(address);
        sb.append(", type=").append(type);
        sb.append(", consigneeAddressId=").append(consigneeAddressId);
        sb.append(", phone=").append(phone);
        sb.append(", bindingOrder=").append(bindingOrder);
        sb.append(", processState=").append(processState);
        sb.append(", processCause=").append(processCause);
        sb.append(", invoiceAddress=").append(invoiceAddress);
        sb.append(", number=").append(number);
        sb.append(", taxRate=").append(taxRate);
        sb.append(", processTime=").append(processTime);
        sb.append(", remark=").append(remark);
        sb.append(", processUser=").append(processUser);
        sb.append(", money=").append(money);
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