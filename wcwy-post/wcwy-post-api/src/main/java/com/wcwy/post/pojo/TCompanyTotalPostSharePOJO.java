package com.wcwy.post.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.wcwy.company.po.ProvincesCitiesPO;
import com.wcwy.company.po.TIndustryAndTypePO;
import com.wcwy.post.dto.TotalPostShare;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 企业表
 * @TableName t_company
 */
@Data
@ApiModel(value = "子企业数据动态")
public class TCompanyTotalPostSharePOJO {
    /**
     * 企业Id
     */
    @ApiModelProperty(value = "企业Id")
    private String companyId;

    /**
     * 登录账号
     */
    @ApiModelProperty(value = "登录账号")
    private String loginName;


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
     * 营业执照
     */
    @ApiModelProperty(value = "营业执照")
    private String businessLicense;



    /**
     * 签约合同
     */
    @ApiModelProperty(value = "签约合同")
    private String signContract;
    /**
     * 合同有效期
     */
    @ApiModelProperty(value = "合同有效期")
    private LocalDate contractDate;
    /**
     * 企业LOGO
     */
    @ApiModelProperty(value = "企业LOGO")
    private String logoPath;

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
     * 行业类型
     */
    @ApiModelProperty(value = "行业类型")
    private List<TIndustryAndTypePO> industry;
    /**
     * 无忧币数量
     */
    @ApiModelProperty(value = "无忧币数量")
    private BigDecimal currencyCount;
    /**
     * 账号状态(0正常1停用)
     */
    @ApiModelProperty(value = "账号状态(0正常1停用)")
    private String status;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;
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
    @ApiModelProperty(value = "总和发布岗位纪录表")
    private TotalPostShare totalPostShare;
}