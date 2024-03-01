package com.wcwy.company.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ClassName: TCompanybasicInformation
 * Description:
 * date: 2023/2/14 13:41
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel(value = "公司基础信息")
public class TCompanyBasicInformation {
    /**
     * 企业Id
     */
    @ApiModelProperty(value = "企业Id")
    private String companyId;
    @ApiModelProperty(value = "企业属性(0招聘企业 1猎头企业)")
    private Integer companyType;
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
    @ApiModelProperty(value = "审核状态(0待审核1审核中2通过3未通过)")
    private Integer examineStatus;
}
