package com.wcwy.post.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * ClassName: TopUpVO
 * Description:
 * date: 2023/3/6 15:52
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel("充值接口")
public class TopUpVO {

    @ApiModelProperty("订单标题")
    @NotBlank(message = "订单标题不能为空")
    private String title;


    @ApiModelProperty("支付方式（1：支付宝 2：微信 ）")
    @NotBlank(message = "支付方式不能为空")
    private String paymentType;


    @ApiModelProperty("订单金额")
    @NotNull(message = "订单金额不能为空")
    @DecimalMin(message = "金额不能小于等于0" ,value = "0")
    private BigDecimal money;

}
