package com.wcwy.company.dto;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * ClassName: IdentitySharingDTO
 * Description:分享分身份收益
 * date: 2023/8/11 10:23
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@ApiModel("分享分身份收益")
@Data
public class  IdentitySharingDTO {

    @ApiModelProperty("推荐官")
    private BigDecimal recommendSharing=new BigDecimal(0);

    @ApiModelProperty("求职者")
    private BigDecimal jobHunterSharing=new BigDecimal(0);
    @ApiModelProperty("企业")
    private BigDecimal companySharing=new BigDecimal(0);

    @ApiModelProperty("分享收益")
    private BigDecimal  revenueSharing=new BigDecimal(0);

    @ApiModelProperty("推荐收益")
    private BigDecimal  referralRevenue=new BigDecimal(0);
    @ApiModelProperty("我的收益详情分享收益")
    private IPage<IdentityEarningsDTO> iPage;

}
