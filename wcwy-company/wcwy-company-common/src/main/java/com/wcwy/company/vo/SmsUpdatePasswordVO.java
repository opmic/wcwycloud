package com.wcwy.company.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * ClassName: SmsUpdatePassword
 * Description:
 * date: 2022/9/9 13:44
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@ApiModel(value = "短信验证修改密码实体类")
@Data
public class SmsUpdatePasswordVO {
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
    @Size(max = 16,min = 6 ,message = "密码要求6-18位")
    private String password;

    @ApiModelProperty(value = "身份：0企业 1推荐官 2求职者")
    @NotBlank(message = "身份不能为空")
    public String companyType;

}
