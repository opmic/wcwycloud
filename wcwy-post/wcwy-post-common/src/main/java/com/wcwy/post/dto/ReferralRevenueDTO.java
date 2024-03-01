package com.wcwy.post.dto;

import com.wcwy.post.po.ParticularsPO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * ClassName: ReferralRevenueDTO
 * Description:
 * date: 2023/8/10 16:07
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel(value = "推荐收益详情")
public class ReferralRevenueDTO {
    @ApiModelProperty("投简id")
    private String putInResumeId;

    @ApiModelProperty("收益")
    private BigDecimal referrerMoney;
    @ApiModelProperty("推荐时间")
    private LocalDateTime recommendTime;
    private String particulars;

    private ParticularsPO particularsPO;
}
