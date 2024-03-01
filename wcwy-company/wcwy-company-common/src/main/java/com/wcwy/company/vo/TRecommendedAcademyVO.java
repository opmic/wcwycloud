package com.wcwy.company.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wcwy.company.po.ProvincesCitiesPO;
import com.wcwy.company.po.TIndustryAndTypePO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 推荐官企业表
 * @TableName t_recommended_companies
 */
@Data
@ApiModel("推荐校园注册")
public class TRecommendedAcademyVO implements Serializable {


    /**
     * 密码
     */
    @ApiModelProperty(value = "密码",required = true)
    @NotBlank(message = "密码不能为空!")
    private String password;

    /**
     * 手机号码
     */
    @ApiModelProperty(value = "手机号码",required = true)
    @NotBlank(message = "手机号码不能为空!")
    private String phoneNumber;

    @ApiModelProperty("联系方式")
    @NotBlank(message = "手机号码不能为空!")
    private String contactPhone;
    /**
     * 头像路径
     */
/*    @TableField(value = "avatar")
    @ApiModelProperty("头像路径")
    private String avatar;*/

    /**
     * 企业名称
     */
    @ApiModelProperty(value = "院校名称",required = true)
    @NotBlank(message = "企业名称不能为空!")
    private String companyName;

    /**
     * 企业简称
     */
 /*   @ApiModelProperty(value = "院校简称",required = true)
    @NotBlank(message = "院校简称不能为空!")
    private String shortName;*/

    /**
     * 企业属性(0未知 1推荐官企业版 2推荐官校园版)
     */
/*
    @TableField(value = "company_type")
    @ApiModelProperty("企业属性(0未知 1推荐官企业版 2推荐官校园版)")
    private Integer companyType;
*/

/*    *//**
     * 营业执照
     *//*
    @TableField(value = "business_license")
    @ApiModelProperty("营业执照")
    private String businessLicense;*/

    /**
     * 企业LOGO
     */
    @ApiModelProperty("院校LOGO")
    @NotBlank(message = "企业LOGO不能为空!")
    private String logoPath;


    /**
     * 企业简介
     */
    @ApiModelProperty(value = "院校简介",required = true)
    @NotBlank(message = "院校简介不能为空!")
    @Size(message = "院校简介字数不能少于100",min = 100)
    private String description;
    /**
     * 微信号
     */
    @ApiModelProperty(value = "微信号",required = true)
    @NotBlank(message = "微信号不能为空!")
    private String wechatNumber;

    /**
     * 详细地址
     */
    @ApiModelProperty(value = "详细地址",required = true)
    @NotBlank(message = "详细地址不能为空!")
    private String address;

    /**
     * 所在省市
     */
    @ApiModelProperty(value = "所在省市",required = true)
    @NotNull(message = "所在省市不能为空!")
    private ProvincesCitiesPO provincesCities;
    /**
     * 职位
     */
    @ApiModelProperty(value = "职位",required = true)
    @NotBlank(message = "职位不能为空!")
    private String jobTitle;

    /**
     * 邮箱
     */
/*    @TableField(value = "email")
    @ApiModelProperty("邮箱")
    private String email;*/

    /**
     * 企业规模
     */
    @ApiModelProperty(value = "院校规模",required = true)
    @NotBlank(message = "企业规模不能为空!")
    private String firmSize;

    /**
     * 企业类型
     */
    @ApiModelProperty(value = "企业类型",required = true)
    @NotEmpty(message = "企业类型不能为空!")
    private List<TIndustryAndTypePO> industry;

    /**
     * 院校性质(0:未知 1:民办 2:公办)
     */
    @ApiModelProperty(value = "院校性质(0:未知 1:民办 2:公办)",required = true)
    @NotNull(message = "院校性质不能为空!")
    private Integer nature;

    @ApiModelProperty(value = "合同开始期",required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull(message = "合同开始时间不能为空")
    private LocalDate beginDate;

    /**
     * 合同有效期
     */
    @ApiModelProperty(value = "合同结束期",required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Future(message = "合同结束时间必须是将来时间!")
    private LocalDate contractDate;

    /**
     * 签约合同
     */
    @ApiModelProperty(value = "签约合同",required = true)
    @NotBlank(message = "签约合同不能为空!")
    private String signContract;

    @ApiModelProperty(value = "验证码",required = true)
    @NotBlank(message = "验证码不能为空!")
    private String code;
    /**
     * 分享注册人
     */
    @ApiModelProperty("分享注册人")
    private String sharePerson;

}