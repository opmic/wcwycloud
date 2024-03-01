package com.wcwy.company.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * ClassName: RealNameVO
 * Description:
 * date: 2023/10/17 13:56
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel("实名认证")
public class RealNameVO {
    @ApiModelProperty("真实姓名")
    @NotBlank(message = "真实姓名不能为空")
    private String realName;
    @ApiModelProperty("身份证号")
    @NotBlank(message = "身份证号不能为空")
    private String card;
    @ApiModelProperty("身份证正面")
    @NotBlank(message = "身份证正面不能为空")
    private String cardFront;
    @ApiModelProperty("身份证正面")
    @NotBlank(message = "身份证正面不能为空")
    private String cardBack;
    @ApiModelProperty("秘钥")
    @NotBlank( message = "秘钥不能为空！")
    private String keyRate;

}
