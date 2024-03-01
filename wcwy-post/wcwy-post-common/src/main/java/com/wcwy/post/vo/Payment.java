package com.wcwy.post.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * ClassName: nativePay
 * Description:
 * date: 2022/10/12 16:25
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel("支付订单")
public class Payment {
    @ApiModelProperty("用户id")
    @NotBlank(message = "用户id不能为空")
    private String userId;
    @ApiModelProperty("金额")
    @NotNull(message = "金额不能为空")
    private BigDecimal money;
    /**
     * 支付方式（1：支付宝 2：微信）
     */
    @ApiModelProperty("支付方式（1：支付宝 2：微信 3银联支付）")
    @NotBlank(message = "支付方式不能为空")
    private String paymentType;
    @ApiModelProperty("订单标题")
    @NotBlank(message = "订单标题不能为空")
    private  String title;
}
