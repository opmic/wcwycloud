package com.wcwy.company.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * ClassName: CardVo
 * Description:
 * date: 2024/1/16 9:16
 *
 * @author tangzhuo
 * @since JDK 1.8
 */

@Data
@ApiModel("身份证修改")
public class CardVo {
    /**
     * 身份证
     */
    @ApiModelProperty("身份证")
    @NotBlank(message = "身份证号不能为空！")
    private String card;



    @ApiModelProperty("身份证姓名")
    @NotBlank(message = "身份证姓名不能为空！")
    private String realName;
    /**
     * 身份证正面
     */

    @ApiModelProperty("身份证正面")
    @NotBlank(message = "身份证正面照片不能为空！")
    private String cardFront;

    /**
     * 身份证反面
     */
    @ApiModelProperty("身份证反面")
    @NotBlank(message = "身份证反面照片不能为空！")
    private String cardVerso;

    @ApiModelProperty("验证码")
    @NotBlank(message = "验证码不能为空！")
    private String code;
}
