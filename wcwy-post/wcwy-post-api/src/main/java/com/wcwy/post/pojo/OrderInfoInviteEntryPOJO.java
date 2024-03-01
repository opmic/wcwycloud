package com.wcwy.post.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wcwy.company.dto.InviteEntryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * ClassName: OrderInfoInviteEntryPOJO
 * Description:
 * date: 2022/11/24 10:15
 *
 * @author tangzhuo
 * @since JDK 1.8
 */

@Data
@ApiModel("订单及求职者信息")
public class  OrderInfoInviteEntryPOJO {
    /**
     * 订单
     */
    @ApiModelProperty("订单")
    private String orderId;

    /**
     * 订单标题
     */
    @ApiModelProperty("订单标题")
    private String title;

    /**
     * 商户订单编号
     */
    @ApiModelProperty("商户订单编号")
    private String orderOn;

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
     * 投简id
     */
    @ApiModelProperty("投简id")
    private String putInResumeId;

    /**
     * 交易状态(1:交易中 2:交易成功 3：交易超时已关闭 4:用户已取消 5 退款中 6 已退款 7退款异常)
     */
    @ApiModelProperty("交易状态(1:交易中 2:交易成功 3：交易超时已关闭 4:用户已取消 5 退款中 6 已退款 7退款异常)")
    private Integer state;

    /**
     * 关闭及失败原因
     */
    @ApiModelProperty("关闭及失败原因")
    private String stateCause;

    /**
     * 求职者id（支付产品id）
     */
    @ApiModelProperty("求职者id（支付产品id）")
    private String jobhunterId;

    /**
     * 订单二维码链接
     */
    @ApiModelProperty("订单二维码链接")
    private String codeUrl;

    /**
     * 推荐官id
     */
    @ApiModelProperty("推荐官id")
    private String recommendId;

    /**
     * 金额（订单金额 元）
     */
    @ApiModelProperty("金额（订单金额 元）")
    private BigDecimal money;


    /**
     * 待支付时间
     */
    @ApiModelProperty("待支付时间")
    private LocalDate noPaymentTime;

    /**
     * 推荐人id
     */
    @ApiModelProperty("推荐人id")
    private String referrerId;

    /**
     * 发票
     */
    @ApiModelProperty("发票")
    private String invoice;

    /**
     * 标识(1:下载简历 2：赏金岗位入职 ：3猎头岗位入职,4充值)
     */
    @ApiModelProperty("标识(1:下载简历 2：赏金岗位入职 ：3猎头岗位入职,4充值)")
    private Integer identification;


    /**
     * 推荐官邀请人id
     */
    @ApiModelProperty("推荐官邀请人id")
    private String shareUserId;


    /**
     * 推荐官邀请人分成
     */
    @ApiModelProperty("推荐官邀请人分成")
    private BigDecimal shareMoney;
    /**
     * 推荐官分成
     */
    @ApiModelProperty("推荐官分成")
    private BigDecimal referrerMoney;

    /**
     * 平台分成
     */
    @ApiModelProperty("平台分成")
    private BigDecimal platformMoney;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private String createId;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("修改人")
    private String updateId;



    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("发送offer表及求职者的信息")
    private InviteEntryDTO inviteEntryDTO;
}
