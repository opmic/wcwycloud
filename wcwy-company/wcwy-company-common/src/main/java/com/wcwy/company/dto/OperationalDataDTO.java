package com.wcwy.company.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * ClassName: OperationalDataDTO
 * Description:
 * date: 2024/1/2 11:30
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel(value = "运营数据1")
public class OperationalDataDTO {

    @ApiModelProperty("职位")
    private long postCount;

    @ApiModelProperty("昨日职位")
    private long postCountYesterday;

    @ApiModelProperty("求职者")
    private long jobHunterCount;
    @ApiModelProperty("昨日求职者")
    private long jobHunterCountYesterday;
    @ApiModelProperty("今日求职者")
    private long jobHunterCountDay;
    @ApiModelProperty("推荐官(职)")
    private long socialRecommend;
    @ApiModelProperty("推荐官(校)")
    private long schoolRecommend;
    @ApiModelProperty("昨日推荐官")
    private long schoolRecommendYesterday;
    @ApiModelProperty("今日推荐官")
    private long schoolRecommendDay;
    @ApiModelProperty("企业")
    private long ordinaryCompany;
    @ApiModelProperty("昨日企业")
    private long ordinaryCompanyYesterday;
    @ApiModelProperty("今日企业")
    private long ordinaryCompanyDay;
    @ApiModelProperty("猎企")
    private long  headhunterCompany;
    @ApiModelProperty("到面付")
    private long  interviewPay;


    @ApiModelProperty("入职付")
    private long  entryPay;

    @ApiModelProperty("满月付")
    private long  fullMoonPay;
    @ApiModelProperty("简历付(校)")
    private long  schoolResumePay;
    @ApiModelProperty("简历付(职)")
    private long  resumePay;
}
