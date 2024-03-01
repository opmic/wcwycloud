package com.wcwy.company.dto;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.wcwy.company.entity.CompanyGlanceOver;
import com.wcwy.company.po.ProvincesCitiesPO;
import com.wcwy.company.po.TIndustryAndTypePO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 企业浏览表
 * @TableName company_glance_over
 */
@Data
@ApiModel("查询浏览企业")
public class CompanyGlanceOverDTO {
    /**
     * 浏览表id
     */
    @ApiModelProperty("浏览表id")
    private String glanceOverId;

    /**
     * 浏览时间
     */
    @ApiModelProperty("浏览时间")
    private LocalDateTime glanceOverTime;

    /**
     * 企业Id
     */
    @TableId(value = "company_id")
    @ApiModelProperty(value = "企业Id")
    private String companyId;


    /**
     * 手机号码
     */
    @TableField(value = "phone_number")
    @ApiModelProperty(value = "手机号码")
    private String phoneNumber;

    /**
     * 头像路径
     */
    @TableField(value = "avatar")
    @ApiModelProperty(value = "头像路径")
    private String avatar;

    /**
     * 企业名称
     */
    @TableField(value = "company_name")
    @ApiModelProperty(value = "企业名称")
    private String companyName;

    /**
     * 企业简称
     */
    @TableField(value = "short_name")
    @ApiModelProperty(value = "企业简称")
    private String shortName;

    /**
     * 企业属性(0招聘企业 1猎头企业)
     */
    @TableField(value = "company_type")
    @ApiModelProperty(value = "企业属性(0招聘企业 1猎头企业)")
    private Integer companyType;

    /**
     * 营业执照
     */
    @TableField(value = "business_license")
    @ApiModelProperty(value = "营业执照")
    private String businessLicense;

    /**
     * 公司类型id
     */
    @TableField(value = "company_type_id")
    @ApiModelProperty(value = "公司类型id")
    private String companyTypeId;

    /**
     * 签约合同
     */
    @TableField(value = "sign_contract")
    @ApiModelProperty(value = "签约合同")
    private String signContract;

    /**
     * 企业LOGO
     */
    @TableField(value = "logo_path")
    @ApiModelProperty(value = "企业LOGO")
    private String logoPath;

    /**
     * 企业简介
     */
    @TableField(value = "description")
    @ApiModelProperty(value = "企业简介")
    private String description;

    /**
     * 投递须知
     */
    @TableField(value = "remark")
    @ApiModelProperty(value = "投递须知")
    private String remark;

    /**
     * 详细地址
     */
    @TableField(value = "address")
    @ApiModelProperty(value = "详细地址")
    private String address;

    /**
     * 所在省市
     */
    @TableField(value = "provinces_cities", typeHandler = JacksonTypeHandler.class)
    @ApiModelProperty(value = "所在省市")
    private ProvincesCitiesPO provincesCities;

    /**
     * 联系人
     */
    @TableField(value = "contact_name")
    @ApiModelProperty(value = "联系人")
    private String contactName;


    /**
     * 联系方式
     */
    @TableField(value = "contact_phone")
    @ApiModelProperty(value = "联系方式")
    private String contactPhone;

    /**
     * 邮箱
     */
    @TableField(value = "email")
    @ApiModelProperty(value = "邮箱")
    private String email;

    /**
     * 企业规模
     */
    @TableField(value = "firm_size")
    @ApiModelProperty(value = "企业规模")
    private String firmSize;

    /**
     * 行业类型
     */
    @TableField(value = "industry",typeHandler = JacksonTypeHandler.class)
    @ApiModelProperty(value = "行业类型")
    private List<TIndustryAndTypePO> industry;
}