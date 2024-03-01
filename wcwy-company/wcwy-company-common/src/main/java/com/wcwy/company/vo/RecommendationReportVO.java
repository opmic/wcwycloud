package com.wcwy.company.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * ClassName: RecommendationReportVO
 * Description:
 * date: 2023/2/2 9:23
 *
 * @author tangzhuo
 * @since JDK 1.8
 */

@Data
@ApiModel("投递简历表")
public class RecommendationReportVO {
    /**
     * 投放的岗位
     */
    @ApiModelProperty("投放的岗位")
    @NotBlank(message = "岗位id不能为空")
    private String putInPost;

    /**
     * 投放的公司
     */
    @ApiModelProperty("投放的公司")
    @NotBlank(message = "投放的公司id不能为空")
    private String putInComppany;


    @ApiModelProperty("人选意向")
   // @NotBlank(message = "人选意向不能为空")
    private String intention;

    @ApiModelProperty(" 到岗时间(1:离职-随时到岗 2:在职-2周内到岗 3:在职-1一个月到岗)")
   // @NotNull(message = "到岗时间选项不能为空")
    @Max(message = "没有该选项",value = 3)
    @Min(message = "没有该选项",value = 1)
    private Integer arrivalTime;



    @ApiModelProperty("目前年薪")
   // @NotNull(message = "目前年薪不能为空!")
    private BigDecimal currentAnnualSalary;

    @ApiModelProperty("期望税前年薪")
   // @NotBlank(message = "期望税前年薪不能为空")
    private String expectAnnualSalary;


    @ApiModelProperty("面试时间(1:可协商 2:提前1天通知 3:期望先电话或视频面试)")
   // @NotNull(message = "面试时间不能为空")
    @Max(message = "没有该选项",value = 3)
    @Min(message = "没有该选项",value = 1)
    private Integer applicationInterviewTime;

    @ApiModelProperty("推荐原因")
   // @NotBlank(message = "推荐原因不能为空")
    private String explains;
    /**
     * 求职者
     */
    @ApiModelProperty("求职者")
    @NotBlank(message = "求职者id不能为空")
    private String putInJobhunter;
}
