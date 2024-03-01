package com.wcwy.post.query;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wcwy.common.base.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

/**
 * 岗位订单表
 * @TableName order_info
 */
@Data
@ApiModel("查询订单及无忧币流水")
public class OrderInfoRunningQuery extends PageQuery {

    @ApiModelProperty("登录用户")
    @NotBlank(message = "登录用户id不能为空")
    private String loginUser;
    /**
     * 订单
     */
    @ApiModelProperty("订单")
    private String orderId;
    /**
     * 岗位id
     */
    @ApiModelProperty("岗位id")
    private String postId;
    /**
     * 支付方式（1：支付宝 2：微信 3无忧币）
     */
    @ApiModelProperty("支付方式（1：支付宝 2：微信 3无忧币）")
    private String paymentType;
    /**
     * 交易状态(1:交易中 2:交易成功 3：交易超时已关闭 4:用户已取消 5 退款中 6 已退款 7退款异常)
     */
    @ApiModelProperty("交易状态(1:交易中 2:交易成功 3：交易超时已关闭 4:用户已取消 5 退款中 6 已退款 7退款异常)")
    private Integer state;

    /**
     * 求职者id（支付产品id）
     */
    @ApiModelProperty("求职者id（支付产品id）")
    private String jobhunterId;
    /**
     *  推荐官id
     */
    @TableField(value = "recommend_id")
    @ApiModelProperty("推荐官id")
    private String recommendId;
    /**
     * 标识(1:下载简历 2：赏金岗位入职 ：3猎头岗位入职,4充值)
     */
    @ApiModelProperty("标识(1:下载简历 2：赏金岗位入职 ：3猎头岗位入职,4充值)")
    private Integer identification;


    @ApiModelProperty("开始时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate beginTime;


    @ApiModelProperty("结束时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endTime;

    /**
     * 1:支出 2:收入
     */
    @TableField(value = "if_income")
    @ApiModelProperty(value = "1:支出 2:收入")
    private Integer ifIncome;
}