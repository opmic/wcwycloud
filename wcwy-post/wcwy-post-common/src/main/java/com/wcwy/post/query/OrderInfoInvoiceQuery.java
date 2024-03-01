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
 * ClassName: OrderInfoInvoiceQuery
 * Description:
 * date: 2022/12/1 18:20
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@ApiModel("查询发票订单")
@Data
public class OrderInfoInvoiceQuery extends PageQuery {
    /**
     * 订单
     */
/*    @ApiModelProperty("订单")
    private String orderId;*/
    /**
     * 岗位id
     */
 /*   @ApiModelProperty("岗位id")
    private String postId;*/

    /**
     * 标识(1:下载简历 2：赏金岗位入职 ：3猎头岗位入职,4充值)
     */
   /* @ApiModelProperty("标识(1:下载简历 2：赏金岗位入职 ：3猎头岗位入职,4充值)")
    private Integer identification;*/
    @ApiModelProperty("关键字")
    private String keyword;

    @ApiModelProperty("开始时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate beginTime;


    @ApiModelProperty("结束时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endTime;

 /*   @ApiModelProperty("发票状态0:查询全部 1:已开发票 2:未开发票")
    public Integer invoiceState;*/
    @ApiModelProperty("当前登录用户")
    @NotBlank(message = "当前登录用户不能为空")
    private String loginUser;
/*    @ApiModelProperty("1:岗位订单,2充值充值订单")
    private Integer option;*/
}
