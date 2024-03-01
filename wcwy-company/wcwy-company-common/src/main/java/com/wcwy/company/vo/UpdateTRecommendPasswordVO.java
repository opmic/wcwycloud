package com.wcwy.company.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * ClassName: UpdateTRecommendPasswordVO
 * Description:
 * date: 2022/9/9 9:35
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel(value = "推荐官修改密码实体类")
public class UpdateTRecommendPasswordVO {
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
    @Size(max = 16, min = 6, message = "密码要求6-18位")
    private String password;

/*    @ApiModelProperty(value = "旧密码")
    @NotBlank(message = "旧密码不能为空")
    @Size(max = 16,min =6  ,message = "旧密码要求6-18位")
    private String oldPassword;*/
}
