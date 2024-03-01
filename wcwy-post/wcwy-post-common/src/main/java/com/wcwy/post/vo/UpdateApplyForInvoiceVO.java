package com.wcwy.post.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 申请发票
 * @TableName apply_for_invoice
 */
@Data
@ApiModel(value ="申请发票表" )
public class UpdateApplyForInvoiceVO{
    /**
     * 申请发票id
     */
    @ApiModelProperty(value = "申请发票id")
    @NotBlank(message = "发票id不能为空")
    private String applyForInvoiceId;

    /**
     * 抬头类型（1：个人 2：企业 3组织）
     */
    @ApiModelProperty(value = "抬头类型（1：个人 2：企业 3组织）")
    @NotNull(message = "抬头类型不能为空!")
    private Integer riseType;

    /**
     * 发票类型(1:增值税普通发票,2增值税专用发票)
     */
    @ApiModelProperty(value = "发票类型(1:增值税普通发票,2增值税专用发票)")
    @NotNull(message = "发票类型不能为空!")
    private Integer invoiceType;

    /**
     * 发票抬头
     */
    @ApiModelProperty(value = "发票抬头")
    @NotBlank(message = "发票抬头不能为空!")
    private String invoiceTitle;

    /**
     * 纳税人识别号
     */
    @ApiModelProperty(value = "纳税人识别号")
    @NotBlank(message = "纳税人识别号不能为空!")
    private String identifyNumber;

    /**
     * 基本开户银行
     */
    @ApiModelProperty(value = "基本开户银行")
    @NotBlank(message = "基本开户银行不能为空!")
    private String bankOfDeposit;

    /**
     * 基本开户银行账号
     */
    @ApiModelProperty(value = "基本开户银行账号")
    @NotBlank(message = "基本开户银行账号不能为空!")
    private String accountNumber;

    /**
     * 企业注册地址
     */
    @ApiModelProperty(value = "address")
    @NotBlank(message = "企业注册地址不能为空!")
    private String address;

    /**
     * 企业注册电话
     */
    @ApiModelProperty(value = "企业注册电话")
    @NotBlank(message = "企业注册电话不能为空!")
    private String phone;

    /**
     * 绑定订单
     */
 /*   @ApiModelProperty(value = "绑定订单")
    @NotBlank(message = "绑定订单不能为空!")
    private List<String> bindingOrder;*/



}