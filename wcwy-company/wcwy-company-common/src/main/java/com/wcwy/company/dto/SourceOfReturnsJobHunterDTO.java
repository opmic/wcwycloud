package com.wcwy.company.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * ClassName: SourceOfReturnsJobHunterDTO
 * Description:
 * date: 2023/7/21 8:42
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel(value = "收益详情求职者")
public class SourceOfReturnsJobHunterDTO {
    /**
     * 收益来源id
     */
    @ApiModelProperty("收益来源id")
    private String sourceOfReturnsId;
    /**
     * 总金额
     */
    @ApiModelProperty("总金额")
    private BigDecimal aggregateAmount;


    @ApiModelProperty("支付人")
    private String payer;

    /**
     * 收益金额
     */
    @TableField(value = "earnings")
    @ApiModelProperty("收益金额")
    private BigDecimal earnings;

    /**
     * 订单完成时间
     */
    @ApiModelProperty("订单完成时间")
    private LocalDateTime orderTime;
    /**
     * 头像路径
     */

    @ApiModelProperty(value = "头像路径")
    private String avatar;

    /**
     * 企业名称
     */
    @ApiModelProperty(value = "企业名称")
    private String companyName;

}
