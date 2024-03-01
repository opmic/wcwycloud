package com.wcwy.company.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * ClassName: CompanyHomeDTO
 * Description:
 * date: 2023/11/21 15:01
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel("企业首页")
public class CompanyHomeDTO {
    @ApiModelProperty(value ="收到简历")
    private Integer curriculumVitae;

    @ApiModelProperty(value ="金币")
    private BigDecimal gold;

    @ApiModelProperty(value ="无忧币")
    private BigDecimal currencyCount;

    @ApiModelProperty(value ="下载的简历")
    private Integer downloadCurriculumVitae;

    @ApiModelProperty(value = "人才私域")
    private Integer talents;

    @ApiModelProperty(value = "全部岗位")
    private Integer postCount;

    @ApiModelProperty(value = "在线岗位")
    private Integer onLinePost;

    @ApiModelProperty(value = "简历未查看")
    private Integer notCheckCurriculumVitae;

    @ApiModelProperty("总收益")
    private BigDecimal totalRevenue;


    /**
     * 提现
     */
    @ApiModelProperty("提现")
    private BigDecimal withdrawDeposit;
}
