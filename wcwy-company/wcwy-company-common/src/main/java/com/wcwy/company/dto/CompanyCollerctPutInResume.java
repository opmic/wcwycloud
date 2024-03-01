package com.wcwy.company.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.wcwy.company.po.ProvincesCitiesPO;
import com.wcwy.company.po.TIndustryAndTypePO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * ClassName: CompanyIndustryPutInResume
 * Description:岗位信息及是否投简及收藏
 * date: 2022/10/13 16:44
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel(value = "岗位信息及是否投简及收藏")

public class CompanyCollerctPutInResume {
    /**
     * 企业Id
     */
    @TableId(value = "company_id", type = IdType.INPUT)
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
/*    @TableField(value = "sign_contract")
    @ApiModelProperty(value = "签约合同")
    private String signContract;*/

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
/*    @TableField(value = "remark")
    @ApiModelProperty(value = "投递须知")
    private String remark;*/

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

    @ApiModelProperty(value = "性别(0:未知 1:男 2:女生)")
    private Integer sex;
    /**
     * 职务
     */
    @TableField(value = "job_title")
    @ApiModelProperty(value = "职务")
    private String jobTitle;

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

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME,property = "mytype", include=JsonTypeInfo.As.PROPERTY)
    @JsonSubTypes({ //设置对应子类的识别码值
            @JsonSubTypes.Type(value = LinkedHashMap.class, name = "TIndustryAndTypePO"),
            @JsonSubTypes.Type(value = TIndustryAndTypePO.class, name = "TIndustryAndTypePO")
    })
    private List<TIndustryAndTypePO> industry;

    /**
     * 父账号Id(0父账号)
     */
/*    @TableField(value = "parent_id")
    @ApiModelProperty(value = "父账号Id(0父账号)")
    private Long parentId;*/

    /**
     * 是否为子公司(0否1是 )
     */
/*    @TableField(value = "subsidiary")
    @ApiModelProperty(value = "是否为子公司(0否1是 )")
    private Integer subsidiary;*/

    /**
     * 搜索简历状态(0是启用1是停用)
     */
/*    @TableField(value = "serach_resume_status")
    @ApiModelProperty(value = "搜索简历状态(0是启用1是停用)")
    private Integer serachResumeStatus;*/

    /**
     * 查看简历状态(0是启用1是停用)
     */
/*    @TableField(value = "view_resume_status")
    @ApiModelProperty(value = "查看简历状态(0是启用1是停用)")
    private Integer viewResumeStatus;*/

    /**
     * 审核意见
     */
/*
    @TableField(value = "examine_result")
    @ApiModelProperty(value = "审核意见")
    private String examineResult;
*/

    /**
     * 聊天ID
     */
/*
    @TableField(value = "chat_id")
    @ApiModelProperty(value = "聊天ID")
    private String chatId;
*/

    /**
     * 创建时间
     */
/*    @TableField(value = "create_time")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;*/

    /**
     * 更新时间
     */
/*    @TableField(value = "update_time")
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;*/

    /**
     * 微信openid
     */
/*    @TableField(value = "openid")
    @ApiModelProperty(value = "微信openid")
    private String openid;*/

    /**
     * 分享注册人
     */
/*    @TableField(value = "share_person")
    @ApiModelProperty(value = "分享注册人")
    private String sharePerson;*/

    /**
     * 等级积分
     */
/*    @TableField(value = "grade_integral")
    @ApiModelProperty(value = "等级积分")
    private Long gradeIntegral;*/

    /**
     * 登录时间
     */
    @TableField(value = "login_time")
    @ApiModelProperty(value = "登录时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime loginTime;

    /**
     * 退出时间
     */
    @TableField(value = "logout_time")
    @ApiModelProperty(value = "退出时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime logoutTime;


    @ApiModelProperty(value = "是否收藏")
    private boolean collerct;
    @ApiModelProperty(value = "收藏id")
    private String collerctId;

    @ApiModelProperty(value = "是否投简历x")
    private boolean isPutinResume;

    @ApiModelProperty(value = "投简id")
    private String putInResumeId;
}
