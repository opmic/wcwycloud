package com.wcwy.company.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * ClassName: RCForgetThePassword
 * Description:
 * date: 2022/12/14 10:25
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@ApiModel("个企推荐官忘记密码")
@Data
public class RCForgetThePassword {
    @ApiModelProperty(value = "电话号码")
    @NotBlank(message = "电话号码不能为空")
    private String phone;


    @ApiModelProperty(value = "验证码")
    @NotBlank(message = "验证码不能为空")
    private String code;
    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    @NotBlank(message = "密码不能为空")
    @Size(max = 18,min = 6 ,message = "密码要求6-18位")
    private String password;

}
