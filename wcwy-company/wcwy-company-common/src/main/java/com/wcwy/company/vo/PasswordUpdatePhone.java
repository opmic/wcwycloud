package com.wcwy.company.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * ClassName: UpdatePhone
 * Description:
 * date: 2022/10/24 15:49
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel("密码更换更换电话号码实体类")
public class PasswordUpdatePhone {

    @ApiModelProperty("用户id")
    @NotBlank(message = "用户id不能为空")
    private String userId;

    @ApiModelProperty("密码")
    @NotBlank(message = "密码不能为空")
    private String password;

    @ApiModelProperty("新电话号码")
    @NotBlank(message = "新电话号码不能为空")
    private String newPhone;

    @ApiModelProperty("新电话号码验证码")
    @NotBlank(message = "验证码不能为空")
    private String code;
}
