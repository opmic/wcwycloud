package com.wcwy.company.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * ClassName: ShareDateDTO
 * Description:
 * date: 2023/7/20 15:44
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel(value = "分享数据")
public class ShareDateDTO {
    @ApiModelProperty(value = "求职者收益")
    private String jobHunterEarnings="0";
    @ApiModelProperty(value = "求职者分享数")
    private Long jobHunterShare;
    @ApiModelProperty(value = "求职者注册数")
    private Integer jobHunterRegister;


    @ApiModelProperty(value = "推荐官收益")
    private String recommendEarnings="0";
    @ApiModelProperty(value = "推荐官分享数")
    private Long recommendShare;
    @ApiModelProperty(value = "推荐官注册数")
    private Integer recommendRegister;


    @ApiModelProperty(value = "企业收益")
    private String companyEarnings="0";
    @ApiModelProperty(value = "企业分享数")
    private Long companyShare;
    @ApiModelProperty(value = "企业注册数")
    private Integer companyRegister;



    @ApiModelProperty(value = "今日盈利")
    private String todayProfit;

    @ApiModelProperty(value = "本月盈利")
    private String monthProfit;

    @ApiModelProperty(value = "累计收益")
    private String sumProfit;

/*
    @ApiModelProperty(value = "求职者累计收益")
    private String jobHunterProfit;
    @ApiModelProperty(value = "推荐官累计收益")
    private String recommendProfit;
    @ApiModelProperty(value = "企业累计收益")
    private String companyProfit;
*/

}
