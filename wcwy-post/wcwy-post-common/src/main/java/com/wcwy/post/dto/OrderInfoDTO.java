package com.wcwy.post.dto;

import com.wcwy.post.entity.OrderInfo;
import com.wcwy.post.po.ParticularsPO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * ClassName: OrderInfoDTO
 * Description:
 * date: 2023/7/7 11:36
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel("订单解析详情")
public class OrderInfoDTO {
    @ApiModelProperty("订单")
   private OrderInfo orderInfo;
    @ApiModelProperty("详情")
    private ParticularsPO particularsPO;
}
