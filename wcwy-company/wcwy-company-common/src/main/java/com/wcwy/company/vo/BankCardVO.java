package com.wcwy.company.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * ClassName: BankCardVO
 * Description:
 * date: 2023/8/17 11:05
 *
 * @author tangzhuo
 * @since JDK 1.8
 */

@Data
@ApiModel("更换绑定银行卡")
public class BankCardVO {



    /**
     * 持卡人姓名
     */
    @ApiModelProperty("持卡人姓名")
    @NotBlank(message = "持卡人姓名不能为空!")
    private String userName;

    /**
     * 预留手机号
     */
    @ApiModelProperty("预留手机号")
    @NotBlank(message = "预留手机号不能为空!")
    private String userTel;

    /**
     * 开户支行
     */
    @ApiModelProperty("开户支行")
    @NotBlank(message = "开户支行不能为空!")
    private String bankSubName;

    /**
     * 开户银行
     */
    @ApiModelProperty("开户银行")
    @NotBlank(message = "开户银行不能为空!")
    private String bankName;

    /**
     * 申请人账户
     */
    @ApiModelProperty("申请人账户")
    @NotBlank(message = "申请人账户不能为空!")
    private String bankNum;

    @ApiModelProperty("秘钥")
    @NotBlank( message = "秘钥不能为空！")
    private String keyRate;

}
