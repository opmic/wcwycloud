package com.wcwy.company.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * ClassName: RevenueSharingDTO
 * Description:
 * date: 2023/8/9 17:08
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel(value = "收益数据")
public class RevenueSharingDTO {
    @ApiModelProperty("总收益")
    private BigDecimal totalRevenue;

    @ApiModelProperty("推荐收益")
    private BigDecimal recommend=new BigDecimal(0);

    @ApiModelProperty("分享收益")
    private BigDecimal share=new BigDecimal(0);
}
