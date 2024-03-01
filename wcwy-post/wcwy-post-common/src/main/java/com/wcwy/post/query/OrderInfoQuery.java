package com.wcwy.post.query;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wcwy.common.base.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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
@ApiModel("岗位订单条件判断")
public class OrderInfoQuery extends PageQuery {

    /**
     * 订单
     */
    @ApiModelProperty("订单")
    private String orderId;
    /**
     * 岗位id
     */
/*    @ApiModelProperty("岗位id")
    private String postId;*/

    @ApiModelProperty("关键字")
    private String keyword;

    /**
     * 交易状态(1:交易中 2:交易成功 3：交易超时已关闭 4:用户已取消 5 退款中 6 已退款 7退款异常)
     */
    @ApiModelProperty("交易状态(1:交易中 2:交易成功 3：交易超时已关闭 4:用户已取消 5 退款中 6 已退款 7退款异常 8:异常关单)")
    private Integer state;



    @ApiModelProperty("1：入职付 ：2满月付3 到面付 4:简历付(校) 5:简历付(职))")
    @Max(value = 5,message = "未知选项！")
    @Min(value = 1,message = "未知选项！")
    private Integer identification;

    @ApiModelProperty("开始时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate beginTime;


    @ApiModelProperty("结束时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endTime;

    @ApiModelProperty("1:岗位订单,2充值充值订单 3简历下载")
    private Integer option;


}