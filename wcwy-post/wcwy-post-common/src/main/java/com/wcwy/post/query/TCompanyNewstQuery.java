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
@ApiModel(value = "主页面企业招聘岗位条件查询实体类")
public class TCompanyNewstQuery extends PageQuery {

    /**
     * 发布岗位Id
     */
    @ApiModelProperty(value = "发布岗位Id")
    private String postId;

    /**
     * 企业Id
     */

    @ApiModelProperty(value = "企业Id")
    private String companyId;

    /**
     * 公司名称
     */
    @ApiModelProperty(value = "岗位名称")
    private String postLabel;
    @ApiModelProperty(value = "公司名称")
    private String companyName;

    @ApiModelProperty(value = "求职者id")
    private String jobhunter;

    /**
     * 行业类型
     */
    @ApiModelProperty(value = "行业类型")
    private List<String> industry;
    /**
     * 职位类别
     */
    @ApiModelProperty(value = "职位类别")
    private List<String> position;

    /**
     * 职位福利
     */
    @ApiModelProperty(value = "职位福利")
    private List<String> postWealId;
    /**
     * 岗位开始薪资
     */
    @ApiModelProperty(value = "工作年薪(1:10-30万 2：30-50万 3：50万以上)")
    private String salary;

    /**
     * 工作经验
     */
    @ApiModelProperty(value = "工作年限")
    private String workExperience;


    /**
     * 工作城市
     */
    @ApiModelProperty(value = "工作城市")
    private List<String> city;


    /**
     * 学历
     */
    @ApiModelProperty(value = "学历")
    private String educationType;


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

}
