package com.wcwy.post.query;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.wcwy.common.base.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * ClassName: TCompanyPostQuery
 * Description:
 * date: 2022/9/15 9:28
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel(value = "企业招聘岗位条件查询实体类")
public class TCompanyPostQuery extends PageQuery {

    /**
     * 发布岗位Id
     */
    @TableId(value = "post_id")
    @ApiModelProperty(value = "发布岗位Id")
    private String postId;
    /**
     * 企业Id
     */
    @TableField(value = "company_id")
    @ApiModelProperty(value = "企业Id")
    private String companyId;

    /**
     * 公司名称
     */
    @ApiModelProperty(value = "公司名称")
    private String companyName;


    /**
     * 企业规模
     */
    @TableField(value = "firm_size")
    @ApiModelProperty(value = "企业规模")
    private String firmSize;
    /**
     * 岗位名称
     */
    @ApiModelProperty(value = "岗位名称")
    private String postLabel;


  /*  *//**
     * 行业类型
     *//*
    @ApiModelProperty(value = "行业类型")
    private List<TIndustryAndTypePO> industry;*/

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
    @ApiModelProperty(value = "工作经验")
    private String workExperience;


    /**
     * 学历
     */
    @ApiModelProperty(value = "学历")
    private String educationType;




    /**
     * 工作城市
     */
    @ApiModelProperty(value = "工作城市")
    private List<String> city;

    /**
     * 岗位发布类型(0普通改为 1:赏金岗位 2猎头岗位)
     */
    @ApiModelProperty(value = " 岗位发布类型(0普通改为 1:赏金岗位 2猎头岗位)")
    private Integer postType;




    @ApiModelProperty(value = "工作性质")
    private String jobCategory;
    @ApiModelProperty(value = "岗位状态(0：停止招聘1:招聘中)")
    private Integer status;

    @ApiModelProperty(value = "审核状态(0:审核中 1:审核失败 2:审核成功)")
    private Integer audit;
}
