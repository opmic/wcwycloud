package com.wcwy.company.po;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 企业表
 * @TableName t_company
 */

@Data
@ApiModel(value = "查看企业表")
public class TCompanyPO implements Serializable {
    /**
     * 企业Id
     */
    @ApiModelProperty(value = "企业Id")
    private String companyId;



    /**
     * 手机号码
     */
    @ApiModelProperty(value = "手机号码")
    private String phoneNumber;

    /**
     * 头像路径
     */
    @ApiModelProperty(value = "头像路径")
    private String avatar;

    /**
     * 企业名称
     */
    @ApiModelProperty(value = "企业名称")
    private String companyName;

    /**
     * 企业简称
     */
    @ApiModelProperty(value = "企业简称")
    private String shortName;

    /**
     * 企业属性(0招聘企业 1猎头企业)
     */
    @ApiModelProperty(value = "企业属性(0招聘企业 1猎头企业)")
    private Integer companyType;

    /**
     * 营业执照
     */
    @ApiModelProperty(value = "营业执照")
    private String businessLicense;

    /**
     * 公司类型id
     */
    @ApiModelProperty(value = "公司类型id")
    private String companyTypeId;



    /**
     * 企业LOGO
     */
    @ApiModelProperty(value = "企业LOGO")
    private String logoPath;
    @ApiModelProperty(value = "自定义logo")
    private String customLogo;

    /**
     * 企业简介
     */
    @ApiModelProperty(value = "企业简介")
    private String description;



    /**
     * 详细地址
     */
    @ApiModelProperty(value = "详细地址")
    private String address;

    /**
     * 所在省市
     */
    @ApiModelProperty(value = "所在省市")
    private ProvincesCitiesPO provincesCities;

    /**
     * 联系人
     */
    @ApiModelProperty(value = "联系人")
    private String contactName;
    @ApiModelProperty(value = "性别(0:未知 1:男 2:女生)")
    private Integer sex;
    /**
     * 职务
     */
    @ApiModelProperty(value = "职务")
    private String jobTitle;

    /**
     * 联系方式
     */
    @ApiModelProperty(value = "联系方式")
    private String contactPhone;

    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱")
    private String email;

    /**
     * 企业规模
     */
    @ApiModelProperty(value = "企业规模")
    private String firmSize;

    /**
     * 行业类型
     */
    @ApiModelProperty(value = "行业类型")
    private List<TIndustryAndTypePO> industry;


    /**
     * 登录时间
     */
    @ApiModelProperty(value = "登录时间")
    private LocalDateTime loginTime;

    /**
     * 退出时间
     */
    @ApiModelProperty(value = "退出时间")
    private LocalDateTime logoutTime;
    @ApiModelProperty(value = "在线职位")
    private Integer onLinePost;
}