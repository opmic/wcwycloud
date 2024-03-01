package com.wcwy.post.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 岗位订单表
 * @TableName order_info
 */
@Data
@ApiModel("岗位订单实体类")
public class OrderInfoVO  {

    /**
     * 订单标题
     */
    @ApiModelProperty("订单标题")
    @NotBlank(message = "订单标题不能为空")
    private String title;
    /**
     * 岗位id
     */
    @ApiModelProperty("岗位id")
    @NotBlank(message = "岗位id不能为空")
    private String postId;
    /**
     * 支付方式（1：支付宝 2：微信 3无忧币）
     */

    @ApiModelProperty("支付方式（1：支付宝 2：微信 3无忧币）")
    @NotBlank(message = "支付方式不能为空")
    private String paymentType;

    /**
     * 求职者id（支付产品id）
     */

    @ApiModelProperty("求职者id（支付产品id）")
    @NotBlank(message = "求职者id不能为空")
    private String jobhunterId;
    /**
     * 推荐官id
     **/
    @ApiModelProperty("邀请求职者的人才推荐官id")
    private String recommend;

    @ApiModelProperty("投简id")
    @NotBlank(message = "投简id不能为空")
    private String putInResumeId;
    /**
     * 企业id
     */
    @ApiModelProperty("招聘企业id")
    @NotBlank(message = "招聘企业id不能为空")
    private String companyId;


    /**
     * 求职者期望最低年薪
     */

    @ApiModelProperty("求职者期望最低年薪")
    @NotNull(message = "求职者期望最低年薪不能为空")
    private BigDecimal money;

    /**
     * 标识(1:下载简历 2：赏金岗位入职 ：3猎头岗位入职,4充值)
     */
    @ApiModelProperty("标识(1:下载简历 2：赏金岗位入职 ：3猎头岗位入职,4充值)")
    @NotNull(message = "标识不能为空")
    private Integer identification;


}