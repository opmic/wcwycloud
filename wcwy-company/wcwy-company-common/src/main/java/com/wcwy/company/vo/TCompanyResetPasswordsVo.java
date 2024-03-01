package com.wcwy.company.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * ClassName: TCompanyResetPasswordsVo
 * Description:
 * date: 2022/12/19 13:47
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel(value = "企业重置密码")
public class TCompanyResetPasswordsVo {
    @ApiModelProperty(value = "电话号码")
    @NotBlank(message = "电话号码不能为空!")
    private String phone;
    @ApiModelProperty(value = "验证码")
    @NotBlank(message = "验证码不能为空!")
    private String code;
    @ApiModelProperty(value = "重置密码")
    @NotBlank(message = "重置密码不能为空!")
    private String password;

}
