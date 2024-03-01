package com.wcwy.company.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;

/**
 * ClassName: RecommendDataDTO
 * Description:
 * date: 2023/8/8 17:01
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel("推荐官首页数据")
public class RecommendDataDTO {
    /**
     * 总收益
     */

    @ApiModelProperty("总收益")
    private BigDecimal totalRevenue;
    /**
     * 提现
     */
    @ApiModelProperty("提现")
    private BigDecimal withdrawDeposit;

    @ApiModelProperty("无忧币")
    private BigDecimal currencyCount;


    @ApiModelProperty("金币")
    private BigDecimal gold;

    @ApiModelProperty("我的人才")
    private Long talents;
}
