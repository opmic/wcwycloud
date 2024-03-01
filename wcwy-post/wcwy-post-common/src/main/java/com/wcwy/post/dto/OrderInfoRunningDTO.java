package com.wcwy.post.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 岗位订单表
 * @TableName order_info
 */
@Data
@ApiModel("订单及无忧币流水")
public class OrderInfoRunningDTO  {

    /**
     * 无忧币流水id
     */
    @TableId(value = "running_water_id")
    private String runningWaterId;

    /**
     *  来源（1:充值 2:提现 3:购买简历 4:分成）
     */
    @TableField(value = "source")
    private Integer source;


    /**
     * 无忧币流水金额
     */
    @TableField(value = "money")
    @ApiModelProperty(value = "无忧币流水金额")
    private BigDecimal money;

    @TableField(value = "money")
    @ApiModelProperty(value = "无忧币流水金额")
    private BigDecimal paymentMoney;
    /**
     * 1:支出 2:收入
     */
    @TableField(value = "if_income")
    @ApiModelProperty(value = "1:支出 2:收入")
    private Integer ifIncome;

    /**
     * 订单
     */
    @TableId(value = "order_id")
    @ApiModelProperty("订单")
    private String orderId;

    /**
     * 订单标题
     */
    @TableField(value = "title")
    @ApiModelProperty("订单标题")
    private String title;

    /**
     *  商户订单编号
     */
    @TableField(value = "order_on")
    @ApiModelProperty("商户订单编号")
    private String orderOn;

    /**
     * 岗位id
     */
    @TableField(value = "post_id")
    @ApiModelProperty("岗位id")
    private String postId;

    /**
     * 支付方式（1：支付宝 2：微信 3无忧币）
     */
    @TableField(value = "payment_type")
    @ApiModelProperty("支付方式（1：支付宝 2：微信 3无忧币）")
    private String paymentType;

    /**
     * 投简id
     */
    @TableField(value = "put_in_resume_id")
    @ApiModelProperty("投简id")
    private String putInResumeId;

    /**
     * 交易状态(1:交易中 2:交易成功 3：交易超时已关闭 4:用户已取消 5 退款中 6 已退款 7退款异常)
     */
    @TableField(value = "state")
    @ApiModelProperty("交易状态(1:交易中 2:交易成功 3：交易超时已关闭 4:用户已取消 5 退款中 6 已退款 7退款异常)")
    private Integer state;

    /**
     * 关闭及失败原因
     */
    @TableField(value = "state_cause")
    @ApiModelProperty("关闭及失败原因")
    private String stateCause;

    /**
     * 求职者id（支付产品id）
     */
    @TableField(value = "jobhunter_id")
    @ApiModelProperty("求职者id（支付产品id）")
    private String jobhunterId;



    /**
     *  推荐官id
     */
    @TableField(value = "recommend_id")
    @ApiModelProperty("推荐官id")
    private String recommendId;



    /**
     * 推荐人id
     */
    @TableField(value = "referrer_id")
    @ApiModelProperty("推荐人id")
    private String referrerId;

    /**
     * 发票
     */
    @TableField(value = "invoice")
    @ApiModelProperty("发票")
    private String invoice;

    /**
     * 标识(1:下载简历 2：赏金岗位入职 ：3猎头岗位入职,4充值)
     */
    @TableField(value = "identification")
    @ApiModelProperty("标识(1:下载简历 2：赏金岗位入职 ：3猎头岗位入职,4充值)")
    private Integer identification;



    /**
     * 推荐官邀请人id
     */
    @TableField(value = "share_user_id")
    @ApiModelProperty("推荐官邀请人id")
    private String shareUserId;


    /**
     * 推荐官邀请人分成
     */
    @TableField(value = "share_money")
    @ApiModelProperty("推荐官邀请人分成")
    private BigDecimal shareMoney;
    /**
     * 推荐官分成
     */
    @TableField(value = "referrer_money")
    @ApiModelProperty("推荐官分成")
    private BigDecimal referrerMoney;

    /**
     * 平台分成
     */
    @TableField(value = "platform_money")
    @ApiModelProperty("平台分成")
    private BigDecimal platformMoney;

    /**
     * 创建人
     */
    @TableField(value = "create_id")
    @ApiModelProperty("创建人")
    private String createId;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    @ApiModelProperty("支付时间")
    private LocalDateTime updateTime;


}