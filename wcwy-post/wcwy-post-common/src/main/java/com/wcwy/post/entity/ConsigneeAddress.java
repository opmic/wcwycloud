package com.wcwy.post.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.wcwy.post.po.AddressPO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 发票邮寄地址
 * @TableName consignee_address
 */
@TableName(value ="consignee_address",autoResultMap = true)
@Data
@ApiModel("发票邮寄地址")
public class ConsigneeAddress implements Serializable {
    /**
     * 收件地址
     */
    @TableId(value = "consignee_address_id")
    @ApiModelProperty("收件地址")
    private String consigneeAddressId;

    /**
     * 收件人姓名
     */
    @TableField(value = "recipients")
    @ApiModelProperty("收件人姓名")
    private String recipients;

    /**
     * 电话号码
     */
    @TableField(value = "phone")
    @ApiModelProperty("电话号码")
    private String phone;

    /**
     * 地址
     */
    @TableField(value = "address",typeHandler = JacksonTypeHandler.class)
    @ApiModelProperty("电话号码")
    private AddressPO address;

    /**
     * 邮编
     */
    @TableField(value = "postcode")
    @ApiModelProperty("邮编")
    private String postcode;

    /**
     * 是否默认(0:默认 1不默认)
     */
    @TableField(value = "tacitly_approve")
    @ApiModelProperty("是否默认(0:默认 1不默认)")
    private Integer tacitlyApprove;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    @ApiModelProperty("用户id")
    private String userId;

    /**
     * 逻辑删除
     */
    @TableField(value = "deleted")
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
        ConsigneeAddress other = (ConsigneeAddress) that;
        return (this.getConsigneeAddressId() == null ? other.getConsigneeAddressId() == null : this.getConsigneeAddressId().equals(other.getConsigneeAddressId()))
            && (this.getRecipients() == null ? other.getRecipients() == null : this.getRecipients().equals(other.getRecipients()))
            && (this.getPhone() == null ? other.getPhone() == null : this.getPhone().equals(other.getPhone()))
            && (this.getAddress() == null ? other.getAddress() == null : this.getAddress().equals(other.getAddress()))
            && (this.getPostcode() == null ? other.getPostcode() == null : this.getPostcode().equals(other.getPostcode()))
            && (this.getTacitlyApprove() == null ? other.getTacitlyApprove() == null : this.getTacitlyApprove().equals(other.getTacitlyApprove()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getDeleted() == null ? other.getDeleted() == null : this.getDeleted().equals(other.getDeleted()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getConsigneeAddressId() == null) ? 0 : getConsigneeAddressId().hashCode());
        result = prime * result + ((getRecipients() == null) ? 0 : getRecipients().hashCode());
        result = prime * result + ((getPhone() == null) ? 0 : getPhone().hashCode());
        result = prime * result + ((getAddress() == null) ? 0 : getAddress().hashCode());
        result = prime * result + ((getPostcode() == null) ? 0 : getPostcode().hashCode());
        result = prime * result + ((getTacitlyApprove() == null) ? 0 : getTacitlyApprove().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getDeleted() == null) ? 0 : getDeleted().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", consigneeAddressId=").append(consigneeAddressId);
        sb.append(", recipients=").append(recipients);
        sb.append(", phone=").append(phone);
        sb.append(", address=").append(address);
        sb.append(", postcode=").append(postcode);
        sb.append(", tacitlyApprove=").append(tacitlyApprove);
        sb.append(", userId=").append(userId);
        sb.append(", deleted=").append(deleted);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}