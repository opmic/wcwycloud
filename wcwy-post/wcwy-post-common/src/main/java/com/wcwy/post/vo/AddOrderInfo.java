package com.wcwy.post.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * ClassName: AddOrderInfo
 * Description:
 * date: 2022/10/17 10:34
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
public class AddOrderInfo {
    @ApiModelProperty("用户id")
    private String userId;
    @ApiModelProperty("金额")
    private BigDecimal money;
    /**
     * 支付方式（1：支付宝 2：微信）
     */
    @ApiModelProperty("支付方式（1：支付宝 2：微信）")
    private String paymentType;
    @ApiModelProperty("订单标题")
    private  String title;
}
