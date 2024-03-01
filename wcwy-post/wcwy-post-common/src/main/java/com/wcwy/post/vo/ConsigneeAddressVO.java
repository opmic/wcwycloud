package com.wcwy.post.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.wcwy.post.po.AddressPO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 发票邮寄地址
 * @TableName consignee_address
 */
@Data
@ApiModel("发票邮寄地址")
public class ConsigneeAddressVO {

    /**
     * 收件地址
     */
    @ApiModelProperty(value = "收件地址id",required = false)
    private String consigneeAddressId;

    /**
     * 收件人姓名
     */
    @ApiModelProperty(value = "收件人姓名",required = true)
    @NotBlank(message = "收件人姓名不能为空!")
    private String recipients;

    /**
     * 电话号码
     */
    @ApiModelProperty(value = "电话号码" , required = true)
    @NotBlank(message = "电话号码不能为空!")
    private String phone;

    /**
     * 地址
     */
    @ApiModelProperty(value = "地址",required = true)
    @Valid
    private AddressPO address;

    /**
     * 邮编
     */
    @ApiModelProperty(value = "邮编",required = true)
    @NotBlank(message = "邮编不能为空!")
    private String postcode;

    /**
     * 是否默认(0:默认 1不默认)
     */
    @ApiModelProperty(value = "是否默认(0:默认 1不默认)",required = true)
    @NotNull(message = " 是否默认(0:默认 1不默认)不能为空!")
    private Integer tacitlyApprove;


}