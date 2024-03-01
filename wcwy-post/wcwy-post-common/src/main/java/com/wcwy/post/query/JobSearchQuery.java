package com.wcwy.post.query;

import com.wcwy.common.base.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * ClassName: TCompanyNewstQuery
 * Description:
 * date: 2022/10/10 8:24
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel(value = "职位搜索")
public class JobSearchQuery extends PageQuery {

    /**
     * 公司名称
     */
    @ApiModelProperty(value = "岗位名称及公司名称")
    private String companyName;


    /**
     * 职位类别
     */
    @ApiModelProperty(value = "职位类型")
    private List<String> position;

    @ApiModelProperty(value = "行业")
    private String industry;

    /**
     * 职位福利
     */

    @ApiModelProperty(value = "职位福利")
    private List<String> postWealId;

    @ApiModelProperty(value = "0:不限 1:3万以下 2:3-5万 3:5-10万 4:10-20万 5:20-50万 6:50万以上")
    private Integer annualSalary;

    /**
     * 工作经验
     */
    @ApiModelProperty(value = "工作年限 0:不限 1:在校生 2:应届生 3:1年以内 4:1-3年 5:3-5年 6:5-10年 7:10年以上")
    private List<Integer> workExperience;

    @ApiModelProperty(value = "企业规模 0:不限 1:0-50人 2:50-150人 3:150-500人 4:500-1000人 5:1000-5000人 6:5000-10000人 7:10000人以上")
    private List<Integer> firmSize;
    /**
     * 工作城市
     */
    @ApiModelProperty(value = "工作城市")
    private List<String> city;


    /**
     * 学历
     */
    @ApiModelProperty(value = "学历 0:不限 1:初中及以下 2:中专/中技 3:高中 4:大专 5:本科 6:硕士 7:博士")
    private List<Integer> educationType;


    /**
     * 岗位发布类型(0普通改为 1:赏金岗位 2猎头岗位)
     */
    @ApiModelProperty(value = " 岗位发布类型(0普通改为 1:赏金岗位 2猎头岗位 4:赏金猎头岗位)")
    private Integer postType;
    @ApiModelProperty(value = "工作性质")
    private String jobCategory;

    @ApiModelProperty(value = "不需要传值")
    private BigDecimal beginSalary;
    @ApiModelProperty(value = "不需要传值")
    private BigDecimal endSalary;

    @ApiModelProperty(value = "求职者id")
    public String jobHunter;
}
