package com.wcwy.post.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 发票抬头信息
 * @TableName invoice
 */
@Data
@ApiModel("添加发票抬头")
public class InvoiceVO {
    /**
     * 抬头类型（1：个人 2：企业 3组织）
     */
    @ApiModelProperty(value = "抬头类型（1：个人 2：企业 3组织）",required = true)
    @NotNull(message = "抬头类型不能为空!")
    private Integer riseType;

    /**
     * 发票类型(1:增值税普通发票,2增值税专用发票)
     */
    @ApiModelProperty(value = "发票类型(1:增值税普通发票,2增值税专用发票)",required = true)
    @NotNull(message = "发票类型不能为空!")
    private Integer invoiceType;

    /**
     * 发票抬头
     */
    @ApiModelProperty(value = "发票抬头",required = true)
    @NotBlank(message = "发票抬头不能为空!")
    private String invoiceTitle;

    /**
     * 纳税人识别号
     */
    @ApiModelProperty(value = "纳税人识别号",required = true)
    @NotBlank(message = "发票抬头不能为空!")
    private String identifyNumber;

    /**
     * 基本开户银行
     */
    @ApiModelProperty(value = "基本开户银行",required = true)
  //  @NotBlank(message = "基本开户银行不能为空!")
    private String bankOfDeposit;

    /**
     * 基本开户银行账号
     */
    @ApiModelProperty(value = "基本开户银行账号",required = true)
   // @NotBlank(message = "基本开户银行账号不能为空!")
    private String accountNumber;

    /**
     * 企业注册地址
     */
    @ApiModelProperty(value = "企业注册地址",required = true)
  //  @NotBlank(message = "企业注册地址不能为空!")
    private String address;

    /**
     * 企业注册电话
     */
    @ApiModelProperty(value = "企业注册电话",required = true)
  //  @NotBlank(message = "企业注册电话不能为空!")
    private String phone;

    /**
     * 绑定用户
     */
/*    @ApiModelProperty(value = "绑定用户注：申请人id",required = true)
    @NotBlank(message = "绑定用户不能为空!")
    private String bindingUser;*/
/*    @ApiModelProperty("默认(0:不默认 1:默认)")
    @NotNull(message = "请选择是否默认!")
    private Integer tacitlyApprove;*/

}