package com.wcwy.post.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 申请发票
 *
 * @TableName apply_for_invoice
 */
@Data
@ApiModel(value = "申请发票")
public class ApplyForInvoiceVO {

    /**
     * 抬头类型（1：个人 2：企业 3组织）
     */
    @ApiModelProperty(value = "抬头类型（1：个人 2：企业 3组织）", required = true)
    @NotNull(message = "抬头类型不能为空")
    private Integer riseType;

    /**
     * 发票类型(1:增值税普通发票(电子),2增值税专用发票(纸质))
     */
    @ApiModelProperty(value = "发票类型(1:增值税普通发票(电子),2增值税专用发票(纸质))", required = true)
    @NotNull(message = "发票类型")
    private Integer invoiceType;

    /**
     * 发票抬头
     */
    @ApiModelProperty(value = "发票抬头", required = true)
    @NotBlank(message = "发票抬头")
    private String invoiceTitle;

    /**
     * 纳税人识别号
     */
    @ApiModelProperty(value = "纳税人识别号", required = true)
    @NotBlank(message = "纳税人识别号")
    private String identifyNumber;

    /**
     * 基本开户银行
     */
    @ApiModelProperty(value = "基本开户银行", required = true)
  //  @NotBlank(message = "基本开户银行")
    private String bankOfDeposit;

    /**
     * 基本开户银行账号
     */
    @ApiModelProperty(value = "基本开户银行账号", required = true)
   // @NotBlank(message = "基本开户银行账号")
    private String accountNumber;
    @ApiModelProperty("备注")
    @Size(max = 100,message = "字数不能超过100")
    private String remark;
    /**
     * 企业注册地址
     */
    @ApiModelProperty(value = "企业注册地址", required = true)
   // @NotBlank(message = "企业注册地址")
    private String address;

    /**
     * 发票类型(0未知1:纸质 2:电子)
     */
    @ApiModelProperty(" 发票类型(0未知1:纸质 2:电子)")
    @NotNull(message = "发票类型不能为空!")
    private Integer type;

    /**
     * 邮寄地址id
     */
    @ApiModelProperty(value = " 邮寄地址id", required = false)
    private String consigneeAddressId;

    /**
     * 企业注册电话
     */
    @ApiModelProperty(value = " 企业注册电话", required = true)
   // @NotBlank(message = "企业注册电话")
    private String phone;

    /**
     * 绑定订单
     */
    @ApiModelProperty(value = " 绑定订单", required = true)
    @NotNull(message = "订单不能为空!")
    private List<String> bindingOrder;
    /**
     * 发票税率
     */
/*    @ApiModelProperty(value = "发票税率", required = true)
    @NotNull(message = "发票税率不能为空!")
    private Integer taxRate;*/

    /**
     * 发票金额
     */
    @ApiModelProperty(value = "发票金额", required = true)
    @NotNull(message = "发票金额不能为空!")
    private BigDecimal money;

}