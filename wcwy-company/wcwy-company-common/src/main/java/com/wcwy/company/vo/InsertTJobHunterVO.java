package com.wcwy.company.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * ClassName: InsertTJobHunterVO
 * Description:
 * date: 2023/2/17 9:16
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel(value = "求职者注册")
public class InsertTJobHunterVO {
    /**
     * 登录名(使用电话号码)
     */
    @ApiModelProperty("账号")
    @NotBlank(message = "账号不能为空!")
    @Size(min =11,max =11,message = "请输入11位号码")
    private String loginName;

    /**
     * 密码
     */
    @ApiModelProperty("密码")
    @NotBlank(message = "验证码不能为空!")
    @Size(min = 6,max = 16,message = "密码不能小于6位数")
    private String password;


    @ApiModelProperty("验证码")
    @NotBlank(message = "验证码不能为空!")
    @Size(min = 6,max = 6,message = "验证码为6位")
    private String code;


    @ApiModelProperty("邀请码")
    private  String invitationCode;

}
