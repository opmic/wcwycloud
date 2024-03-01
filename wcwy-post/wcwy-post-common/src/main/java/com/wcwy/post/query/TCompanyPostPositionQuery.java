package com.wcwy.post.query;

import com.wcwy.post.po.TIndustryAndTypePO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 企业招聘岗位表
 *
 * @TableName t_company_post
 */
@Data
@ApiModel(value = "查询企业招聘岗位实体类")
public class TCompanyPostPositionQuery  {



    /**
     * 公司名称
     */

    @ApiModelProperty(value = "公司名称")
    private String companyName;



    /**
     * 企业类型
     */

/*    @ApiModelProperty(value = "企业类型")
    private String companyTypeId;*/



    /**
     * 行业类型
     */
    @ApiModelProperty(value = "行业类型")
    private List<TIndustryAndTypePO> industry;

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
    @ApiModelProperty(value = "月薪范围")
    private BigDecimal beginSalary;




    /**
     * 工作经验
     */
    @ApiModelProperty(value = "工作年限")
    private String workExperience;


    /**
     * 学历
     */
    @ApiModelProperty(value = "学历要求")
    private String educationType;


    /**
     * 工作城市
     */
    @ApiModelProperty(value = "工作城市")
    private List<String> city;


    /**
     * 岗位发布类型(0普通改为 1:赏金岗位 2猎头岗位)
     */
/*    @ApiModelProperty(value = " 岗位发布类型(0普通改为 1:赏金岗位 2猎头岗位)")
    private Integer postType;*/

    @ApiModelProperty(value = "工作性质")
    private String jobCategory;



}