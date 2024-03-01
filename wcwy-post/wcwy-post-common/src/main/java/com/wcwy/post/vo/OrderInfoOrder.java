package com.wcwy.post.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 岗位订单表
 * @TableName order_info
 */
@Data
@ApiModel("支付订单实体类")
public class OrderInfoOrder{
    /**
     * 订单
     */
    @ApiModelProperty("订单")
    @NotBlank(message = "订单id不能为空")
    private String orderId;

    @ApiModelProperty("支付方式（1：支付宝 2：微信 3无忧币）")
    @NotBlank(message = "支付方式:注:暂时只能无忧币支付")
    private String paymentType;

    /**
     * 创建人
     */
    @TableField(value = "create_id")
    @ApiModelProperty("支付人id")
    @NotBlank(message = "支付人id不能为空")
    private String createId;




}