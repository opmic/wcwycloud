package com.wcwy.post.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 企业招聘岗位表
 *
 * @TableName t_company_post
 */
@Data
@ApiModel(value = "企业招聘岗位表")
public class TCompanyPostPO implements Serializable {
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
    @ApiModelProperty(value = "公司名称")
    private String companyName;
    /**
     * 企业虚拟名称
     */
    @ApiModelProperty(value = "企业虚拟名称")
    private String virtualName;
    /**
     * 是否隐藏虚拟名称(0:不隐藏 1:隐藏)
     */
    @ApiModelProperty(value = "是否隐藏虚拟名称(0:不隐藏 1:隐藏)")
    private int conceal;

    /**
     * 企业logo
     */
    @ApiModelProperty(value = "企业logo")
    private String logo;

    @ApiModelProperty(value = "自定义logo")
    private String customLogo;

    /**
     * 企业规模
     */
    @ApiModelProperty(value = "企业规模")
    private String firmSize;

    /**
     * 企业类型
     */
    @ApiModelProperty(value = "企业类型")
    private String companyTypeId;

    /**
     * 招聘人数
     */
    @ApiModelProperty(value = "招聘人数")
    private Integer postCount;

    /**
     * 岗位属性
     */
    @ApiModelProperty(value = "岗位属性")
    private String postAttribute;

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
     * 所在城市
     */
    @ApiModelProperty(value = "所在城市")
    private ProvincesCitiesPO workCity;

    /**
     * 企业简介
     */
    @ApiModelProperty(value = "企业简介")
    private String enterpriseProfile;


    /**
     * 职位描述
     */
    @ApiModelProperty(value = "职位描述")
    private String description;

    /**
     * 职位福利
     */
    @TableField(value = "post_weal_id" , typeHandler = JacksonTypeHandler.class)
    @ApiModelProperty(value = "职位福利")
    private List<String> postWealId;

    /**
     * 岗位开始薪资
     */
    @ApiModelProperty(value = "岗位开始薪资")
    private BigDecimal beginSalary;

    /**
     * 岗位结束薪资
     */
    @ApiModelProperty(value = "岗位结束薪资")
    private BigDecimal endSalary;


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
     * 岗位名称
     */
    @ApiModelProperty(value = "岗位名称")
    private String postLabel;
    /**
     * 工作城市
     */
  /*  @ApiModelProperty(value = "工作城市")
    private ProvincesCitiesPO address;*/
    /**
     * 岗位状态(0：停止招聘1：取消岗位：2：招聘中)
     */
    @ApiModelProperty(value = "岗位状态(0：停止招聘 1：招聘中)")
    private Integer status;

    /**
     * 截止日期
     */
    @ApiModelProperty(value = "截止日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate expirationDate;

    /**
     * 岗位发布类型(0普通改为 1:赏金岗位 2猎头岗位)
     */
    @ApiModelProperty(value = " 岗位发布类型(0普通改为 1:赏金岗位 2猎头岗位)")
    private Integer postType;

    /**
     * 工作性质
     */
    @ApiModelProperty(value = "工作性质")
    private String jobCategory;


    @ApiModelProperty(value = "上线天数")
    private Integer day;
    @ApiModelProperty(value = "置顶(0 不置顶1置顶)")
    private Integer top;
}