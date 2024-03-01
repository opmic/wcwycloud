package com.wcwy.company.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * ClassName: RecommendedDataDTO
 * Description:
 * date: 2023/1/13 13:43
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel(value = "我的推荐数据")
public class RecommendedDataDTO {
    @ApiModelProperty(value = "我的人才")
    private Integer talents;

    @ApiModelProperty(value = "已推荐")
    private Integer recommendCount;
    @ApiModelProperty(value = "浏览量")
    private Integer browse;

    @ApiModelProperty(value = "未浏览")
    private Integer  notViewed;

    @ApiModelProperty(value = "面约")
    private  Integer subscribeCount;

    @ApiModelProperty(value = "面试中")
    private Integer interviewCount;

    @ApiModelProperty(value ="offer")
    private Integer offerCount;

    @ApiModelProperty(value ="淘汰")
    private Integer weedOutCount;

    @ApiModelProperty(value = "入职")
    private Integer entryCount;

    @ApiModelProperty(value = "发布岗位")
    private Integer postCount;
    @ApiModelProperty(value = "收到简历")
    private Integer resume;

    @ApiModelProperty(value = "收益")
    private BigDecimal earnings;
}
