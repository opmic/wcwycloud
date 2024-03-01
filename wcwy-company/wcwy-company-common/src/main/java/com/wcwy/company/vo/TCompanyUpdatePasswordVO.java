package com.wcwy.company.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * ClassName: TCompanyUpdatePassword
 * Description:
 * date: 2022/9/3 10:42
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel(value = "企业修改密码")
public class TCompanyUpdatePasswordVO {
    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    @NotBlank(message = "密码不能为空")
    @Size(max = 16,min = 6 ,message = "密码要求6-18位")
    private String password;

    @ApiModelProperty(value = "账号")
    @NotBlank(message = "账号不能为空")
    private String loginName;

    @ApiModelProperty(value = "验证码")
    @NotBlank(message = "验证码不能为空")
    private String code;
}
