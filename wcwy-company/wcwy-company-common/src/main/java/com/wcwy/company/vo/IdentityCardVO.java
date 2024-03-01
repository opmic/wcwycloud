package com.wcwy.company.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * ClassName: IdentityCardVO
 * Description:
 * date: 2023/8/16 14:55
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel(value = "身份证信息填写")
public class IdentityCardVO {

    @ApiModelProperty("身份证姓名")
    @NotBlank( message = "身份证姓名不能为空！")
    private String realName;
    /**
     * 身份证正面
     */

    @ApiModelProperty("身份证正面")
    @NotBlank( message = "身份证正面不能为空！")
    private String cardFront;

    /**
     * 身份证反面
     */
    @ApiModelProperty("身份证反面")
    @NotBlank( message = "身份证反面不能为空！")
    private String cardVerso;

    @ApiModelProperty("身份证号")
    @NotBlank( message = "身份证号不能为空！")
    private String card;

    @ApiModelProperty("秘钥")
    @NotBlank( message = "秘钥不能为空！")
    private String keyRate;
    /**
     * 持卡人姓名
     */
    @ApiModelProperty("持卡人姓名")
    @NotBlank( message = "持卡人姓名不能为空！")
    private String userName;

    /**
     * 预留手机号
     */
    @ApiModelProperty("预留手机号")
    @NotBlank( message = "预留手机号不能为空！")
    private String userTel;

    /**
     * 开户支行
     */
    @ApiModelProperty("开户支行")
    @NotBlank( message = "开户支行不能为空！")
    private String bankSubName;

    /**
     * 开户银行
     */
    @ApiModelProperty("开户银行")
    @NotBlank( message = "开户银行不能为空！")
    private String bankName;

    /**
     * 申请人账户
     */
    @ApiModelProperty("申请人账户")
    @NotBlank( message = "申请人账户不能为空！")
    private String bankNum;


}
