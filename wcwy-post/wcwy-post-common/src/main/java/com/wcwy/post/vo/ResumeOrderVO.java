package com.wcwy.post.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * ClassName: ResumeOrderVO
 * Description:
 * date: 2023/4/3 20:44
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel(value = "简历下载")
public class ResumeOrderVO {
    @ApiModelProperty(value = "订单标题",required = true)
    @NotBlank(message = "订单标题不能为空")
    private String title;
    /**
     * 岗位id
     */
    @ApiModelProperty(value = "岗位id",required = false)
   // @NotBlank(message = "岗位id不能为空")
    private String postId;
    /**
     * 求职者id（支付产品id）
     */

    @ApiModelProperty(value = "求职者id（支付产品id）",required = true)
    @NotBlank(message = "求职者id不能为空")
    private String jobhunterId;

    @ApiModelProperty("投简id")
   // @NotBlank(message = "投简id不能为空")
    private String putInResumeId;

    @ApiModelProperty("投简人")
    // @NotBlank(message = "投简id不能为空")
    private String putInUser;
    /**
     * 企业id
     */
    @ApiModelProperty(value = "招聘企业id",required = true)
    @NotBlank(message = "招聘企业id不能为空")
    private String companyId;

    @ApiModelProperty(value="支付方式(0无忧币 1金币)",required = true)
    @NotNull(message = "请选择支付方式!")
    @Max(value = 1,message = "请选择正确的支付方式!")
    @Min(value = 0,message = "请选择正确的支付方式!")
    private Integer paymentType;


    @ApiModelProperty(value="来源(0:非简历付 1:简历付)",required = true)
    @NotNull(message = "请传入来源!")
    @Max(value = 1,message = "传入来源不正确!")
    @Min(value = 0,message = "传入来源不正确!")
    private Integer source;



}
