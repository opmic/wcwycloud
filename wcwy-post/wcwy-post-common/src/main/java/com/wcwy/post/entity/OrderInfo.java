package com.wcwy.post.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wcwy.post.po.ParticularsPO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 岗位订单表
 *
 * @TableName order_info
 */
@TableName(value = "order_info",autoResultMap = true)
@Data
@ApiModel("岗位订单表")
public class OrderInfo implements Serializable {
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
     * 商户订单编号
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
    @ApiModelProperty("支付方式（1：支付宝 2：微信 3无忧币,4金币支付）")
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
    @ApiModelProperty("交易状态(1:交易中 2:交易成功 3：交易超时已关闭 4:用户已取消 5 退款中 6 已退款 7退款异常 8:异常取消)")
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
     * 订单二维码链接
     */
    @TableField(value = "code_url")
    @ApiModelProperty("订单二维码链接")
    private String codeUrl;

    /**
     * 推荐官id
     */
    @TableField(value = "recommend_id")
    @ApiModelProperty("推荐官id")
    private String  recommendId;

    /**
     * 金额（订单金额 元）
     */
    @TableField(value = "money")
    @ApiModelProperty("金额（订单金额 元）")
    private BigDecimal money;

    @TableField(value = "payment_amount")
    @ApiModelProperty("支付金额")
    private BigDecimal paymentAmount;

    /**
     * 待支付时间
     */
    @TableField(value = "no_payment_time")
    @ApiModelProperty("待支付时间")
    private LocalDate noPaymentTime;

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
     * 标识(1:下载简历 2：入职付 ：3满月付,4充值,5到面付,6简历付)
     */
    @TableField(value = "identification")
    @ApiModelProperty("标识(1:下载简历 2：入职付 ：3满月付,4充值,5到面付,6简历付校 7简历付职)")
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
     * 是否分成(0:未分成 1:已分成 2待入账 3:已入账 )
     */
    @TableField(value = "divide_into_if")
    @ApiModelProperty("是否分成(0:未分成 1:已分成 2待入账 3:已入账 4:不做分成 )")
    private Integer divideIntoIf;
    /**
     * 创建人
     */
    @TableField(value = "create_id")
    @ApiModelProperty("创建人")
    private String createId;


    @TableField(value = "payer")
    @ApiModelProperty("支付人")
    private String payer;

    @TableField(value = "payer_time")
    @ApiModelProperty("支付时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime payerTime;

    @TableField(value = "create_name")
    @ApiModelProperty("创建人姓名")
    private String createName;
    @TableField(value = "particulars")
    @ApiModelProperty("订单详情数据")
    private String particulars;
    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    /**
     * 修改人
     */
    @TableField(value = "update_id")
    @ApiModelProperty("修改人")
    private String updateId;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;


    @TableField(value = "recommend_time")
    @ApiModelProperty("推荐时间")
    private LocalDateTime recommendTime;

    /**
     * 企业邀请人id
     */
    @TableField(value = "inviter_company")
    @ApiModelProperty("企业邀请人id")
    private String inviterCompany;


    /**
     * 企业邀请人分成
     */
    @TableField(value = "inviter_company_money")
    @ApiModelProperty("企业邀请人分成")
    private BigDecimal inviterCompanyMoney;


    @TableField(value = "inviter_company_recommend")
    @ApiModelProperty("邀请企业的推荐官的推荐官id")
    private String inviterCompanyRecommend;
    /**
     * 邀请企业的推荐官的推荐官分成
     */
    @TableField(value = "inviter_company_recommend_money")
    @ApiModelProperty("邀请企业的推荐官的推荐官分成")
    private BigDecimal inviterCompanyRecommendMoney;


    @TableField(value = "deleted")
    @TableLogic
    private Integer deleted;
}