package com.wcwy.company.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.wcwy.company.po.ProvincesCitiesPO;
import com.wcwy.company.po.TIndustryAndTypePO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 企业表
 *
 * @TableName t_company
 */
@TableName(value = "t_company", autoResultMap = true)
@Data
@ApiModel(value = "企业表")
public class TCompany implements Serializable {
    /**
     * 企业Id
     */
    @TableId(value = "company_id", type = IdType.INPUT)
    @ApiModelProperty(value = "企业Id")
    private String companyId;

    /**
     * 登录账号
     */
    @TableField(value = "login_name")
    @ApiModelProperty(value = "登录账号")
    private String loginName;

    /**
     * 密码
     */
    @TableField(value = "password")
    @ApiModelProperty(value = "密码")
    private String password;

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
     * 合同有效期
     */
    @TableField(value = "contract_date")
    @ApiModelProperty(value = "合同有效期")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    /*  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")*/
    private LocalDate contractDate;
    /**
     * 企业LOGO
     */
    @TableField(value = "logo_path")
    @ApiModelProperty(value = "企业LOGO")
    private String logoPath;
    @TableField(value = "custom_logo")
    @ApiModelProperty(value = "企业自定义logo")
    private String customLogo;
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


    @TableField(value = "welfare", typeHandler = JacksonTypeHandler.class)
    @ApiModelProperty(value = "福利")
    private List<String> welfare;


    @TableField(value = "url")
    @ApiModelProperty(value = "网址")
    private String url;


    @TableField(value = "reg_time")
    @ApiModelProperty(value = "公司成立时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime regTime;
    /**
     * 联系人
     */
    @TableField(value = "contact_name")
    @ApiModelProperty(value = "联系人")
    private String contactName;

    /**
     * 性别
     */
    @TableField(value = "sex")
    @ApiModelProperty(value = "性别(0:未知 1:男 2:女生)")
    private Integer sex;
    /**
     * 职务
     */
    @TableField(value = "job_title")
    @ApiModelProperty(value = "职务")
    private String jobTitle;


    @ApiModelProperty(value = "出生日")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @TableField(value = "birthday")
    private LocalDateTime birthday;
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
    @TableField(value = "industry", typeHandler = JacksonTypeHandler.class)
    @ApiModelProperty(value = "行业类型")
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "mytype", include = JsonTypeInfo.As.PROPERTY)
    @JsonSubTypes({ //设置对应子类的识别码值
            @JsonSubTypes.Type(value = LinkedHashMap.class, name = "TIndustryAndTypePO"),
            @JsonSubTypes.Type(value = TIndustryAndTypePO.class, name = "TIndustryAndTypePO")
    })
    private List<TIndustryAndTypePO> industry;

    /**
     * 父账号Id(0父账号)
     */
    @TableField(value = "parent_id")
    @ApiModelProperty(value = "父账号Id(0父账号)")
    private String parentId;

    /**
     * 是否为子公司(0否1是 )
     */
    @TableField(value = "subsidiary")
    @ApiModelProperty(value = "是否为子公司(0否1是 )")
    private Integer subsidiary;

    /**
     * 企业关系证明
     */
    @TableField(value = "relation_img")
    @ApiModelProperty(value = "企业关系证明")
    private String relationImg;

    /**
     * 工作证明
     */
    @TableField(value = "work_img")
    @ApiModelProperty(value = "工作证明")
    private String workImg;

    /**
     * 无忧币数量
     */
    @TableField(value = "currency_count")
    @ApiModelProperty(value = "无忧币数量")
    private BigDecimal currencyCount;
    /**
     * 金币
     */
    @TableField(value = "gold")
    @ApiModelProperty(value = "金币")
    private BigDecimal gold;
    /**
     * 审核状态(0待审核1审核中2通过3未通过)
     */
    @TableField(value = "examine_status")
    @ApiModelProperty(value = "审核状态(0待审核1审核中2通过3未通过)")
    private Integer examineStatus;

    /**
     * 账号状态(0正常1停用)
     */
    @TableField(value = "status")
    @ApiModelProperty(value = "账号状态(0正常1停用)")
    private String status;

    /**
     * 搜索简历状态(0是启用1是停用)
     */
    @TableField(value = "serach_resume_status")
    @ApiModelProperty(value = "搜索简历状态(0是启用1是停用)")
    private Integer serachResumeStatus;

    /**
     * 查看简历状态(0是启用1是停用)
     */
    @TableField(value = "view_resume_status")
    @ApiModelProperty(value = "查看简历状态(0是启用1是停用)")
    private Integer viewResumeStatus;

    /**
     * 审核意见
     */
    @TableField(value = "examine_result")
    @ApiModelProperty(value = "审核意见")
    private String examineResult;

    /**
     * 聊天ID
     */
    @TableField(value = "chat_id")
    @ApiModelProperty(value = "聊天ID")
    private String chatId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @ApiModelProperty(value = "创建时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    @ApiModelProperty(value = "更新时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime updateTime;

    /**
     * 微信openid
     */
    @TableField(value = "openid")
    @ApiModelProperty(value = "微信openid")
    private String openid;

    /**
     * 分享注册人
     */
    @TableField(value = "share_person")
    @ApiModelProperty(value = "分享注册人")
    private String sharePerson;

    /**
     * 等级积分
     */
    @TableField(value = "grade_integral")
    @ApiModelProperty(value = "等级积分")
    private Long gradeIntegral;

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

    /*  @ApiModelProperty(value = "是否绑定微信")
      private Boolean bindWechat;*/
    @TableField(value = "origin")
    @ApiModelProperty(value = "引入来源(0:用户注册推广 1:职位推广)")
    private Integer origin;

    /**
     * 逻辑删除
     */
    @TableField(value = "deleted")
    @ApiModelProperty(value = "逻辑删除")
    @TableLogic
    private Integer deleted;

    @ApiModelProperty("身份(TJ:求职者 TC:企业 TR:推荐官)")
    @TableField(exist = false)
    private List<String> authorization;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        TCompany other = (TCompany) that;
        return (this.getCompanyId() == null ? other.getCompanyId() == null : this.getCompanyId().equals(other.getCompanyId()))
                && (this.getLoginName() == null ? other.getLoginName() == null : this.getLoginName().equals(other.getLoginName()))
                && (this.getPassword() == null ? other.getPassword() == null : this.getPassword().equals(other.getPassword()))
                && (this.getPhoneNumber() == null ? other.getPhoneNumber() == null : this.getPhoneNumber().equals(other.getPhoneNumber()))
                && (this.getAvatar() == null ? other.getAvatar() == null : this.getAvatar().equals(other.getAvatar()))
                && (this.getCompanyName() == null ? other.getCompanyName() == null : this.getCompanyName().equals(other.getCompanyName()))
                && (this.getShortName() == null ? other.getShortName() == null : this.getShortName().equals(other.getShortName()))
                && (this.getCompanyType() == null ? other.getCompanyType() == null : this.getCompanyType().equals(other.getCompanyType()))
                && (this.getBusinessLicense() == null ? other.getBusinessLicense() == null : this.getBusinessLicense().equals(other.getBusinessLicense()))
                && (this.getCompanyTypeId() == null ? other.getCompanyTypeId() == null : this.getCompanyTypeId().equals(other.getCompanyTypeId()))
                && (this.getSignContract() == null ? other.getSignContract() == null : this.getSignContract().equals(other.getSignContract()))
                && (this.getLogoPath() == null ? other.getLogoPath() == null : this.getLogoPath().equals(other.getLogoPath()))
                && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
                && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()))
                && (this.getAddress() == null ? other.getAddress() == null : this.getAddress().equals(other.getAddress()))
                && (this.getProvincesCities() == null ? other.getProvincesCities() == null : this.getProvincesCities().equals(other.getProvincesCities()))
                && (this.getContactName() == null ? other.getContactName() == null : this.getContactName().equals(other.getContactName()))
                && (this.getJobTitle() == null ? other.getJobTitle() == null : this.getJobTitle().equals(other.getJobTitle()))
                && (this.getContactPhone() == null ? other.getContactPhone() == null : this.getContactPhone().equals(other.getContactPhone()))
                && (this.getEmail() == null ? other.getEmail() == null : this.getEmail().equals(other.getEmail()))
                && (this.getFirmSize() == null ? other.getFirmSize() == null : this.getFirmSize().equals(other.getFirmSize()))
                && (this.getIndustry() == null ? other.getIndustry() == null : this.getIndustry().equals(other.getIndustry()))
                && (this.getParentId() == null ? other.getParentId() == null : this.getParentId().equals(other.getParentId()))
                && (this.getSubsidiary() == null ? other.getSubsidiary() == null : this.getSubsidiary().equals(other.getSubsidiary()))
                && (this.getRelationImg() == null ? other.getRelationImg() == null : this.getRelationImg().equals(other.getRelationImg()))
                && (this.getWorkImg() == null ? other.getWorkImg() == null : this.getWorkImg().equals(other.getWorkImg()))
                && (this.getCurrencyCount() == null ? other.getCurrencyCount() == null : this.getCurrencyCount().equals(other.getCurrencyCount()))
                && (this.getExamineStatus() == null ? other.getExamineStatus() == null : this.getExamineStatus().equals(other.getExamineStatus()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
                && (this.getSerachResumeStatus() == null ? other.getSerachResumeStatus() == null : this.getSerachResumeStatus().equals(other.getSerachResumeStatus()))
                && (this.getViewResumeStatus() == null ? other.getViewResumeStatus() == null : this.getViewResumeStatus().equals(other.getViewResumeStatus()))
                && (this.getExamineResult() == null ? other.getExamineResult() == null : this.getExamineResult().equals(other.getExamineResult()))
                && (this.getChatId() == null ? other.getChatId() == null : this.getChatId().equals(other.getChatId()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
                && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
                && (this.getOpenid() == null ? other.getOpenid() == null : this.getOpenid().equals(other.getOpenid()))
                && (this.getSharePerson() == null ? other.getSharePerson() == null : this.getSharePerson().equals(other.getSharePerson()))
                && (this.getGradeIntegral() == null ? other.getGradeIntegral() == null : this.getGradeIntegral().equals(other.getGradeIntegral()))
                && (this.getLoginTime() == null ? other.getLoginTime() == null : this.getLoginTime().equals(other.getLoginTime()))
                && (this.getLogoutTime() == null ? other.getLogoutTime() == null : this.getLogoutTime().equals(other.getLogoutTime()))
                && (this.getDeleted() == null ? other.getDeleted() == null : this.getDeleted().equals(other.getDeleted()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getCompanyId() == null) ? 0 : getCompanyId().hashCode());
        result = prime * result + ((getLoginName() == null) ? 0 : getLoginName().hashCode());
        result = prime * result + ((getPassword() == null) ? 0 : getPassword().hashCode());
        result = prime * result + ((getPhoneNumber() == null) ? 0 : getPhoneNumber().hashCode());
        result = prime * result + ((getAvatar() == null) ? 0 : getAvatar().hashCode());
        result = prime * result + ((getCompanyName() == null) ? 0 : getCompanyName().hashCode());
        result = prime * result + ((getShortName() == null) ? 0 : getShortName().hashCode());
        result = prime * result + ((getCompanyType() == null) ? 0 : getCompanyType().hashCode());
        result = prime * result + ((getBusinessLicense() == null) ? 0 : getBusinessLicense().hashCode());
        result = prime * result + ((getCompanyTypeId() == null) ? 0 : getCompanyTypeId().hashCode());
        result = prime * result + ((getSignContract() == null) ? 0 : getSignContract().hashCode());
        result = prime * result + ((getLogoPath() == null) ? 0 : getLogoPath().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        result = prime * result + ((getAddress() == null) ? 0 : getAddress().hashCode());
        result = prime * result + ((getProvincesCities() == null) ? 0 : getProvincesCities().hashCode());
        result = prime * result + ((getContactName() == null) ? 0 : getContactName().hashCode());
        result = prime * result + ((getJobTitle() == null) ? 0 : getJobTitle().hashCode());
        result = prime * result + ((getContactPhone() == null) ? 0 : getContactPhone().hashCode());
        result = prime * result + ((getEmail() == null) ? 0 : getEmail().hashCode());
        result = prime * result + ((getFirmSize() == null) ? 0 : getFirmSize().hashCode());
        result = prime * result + ((getIndustry() == null) ? 0 : getIndustry().hashCode());
        result = prime * result + ((getParentId() == null) ? 0 : getParentId().hashCode());
        result = prime * result + ((getSubsidiary() == null) ? 0 : getSubsidiary().hashCode());
        result = prime * result + ((getRelationImg() == null) ? 0 : getRelationImg().hashCode());
        result = prime * result + ((getWorkImg() == null) ? 0 : getWorkImg().hashCode());
        result = prime * result + ((getCurrencyCount() == null) ? 0 : getCurrencyCount().hashCode());
        result = prime * result + ((getExamineStatus() == null) ? 0 : getExamineStatus().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getSerachResumeStatus() == null) ? 0 : getSerachResumeStatus().hashCode());
        result = prime * result + ((getViewResumeStatus() == null) ? 0 : getViewResumeStatus().hashCode());
        result = prime * result + ((getExamineResult() == null) ? 0 : getExamineResult().hashCode());
        result = prime * result + ((getChatId() == null) ? 0 : getChatId().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getOpenid() == null) ? 0 : getOpenid().hashCode());
        result = prime * result + ((getSharePerson() == null) ? 0 : getSharePerson().hashCode());
        result = prime * result + ((getGradeIntegral() == null) ? 0 : getGradeIntegral().hashCode());
        result = prime * result + ((getLoginTime() == null) ? 0 : getLoginTime().hashCode());
        result = prime * result + ((getLogoutTime() == null) ? 0 : getLogoutTime().hashCode());
        result = prime * result + ((getDeleted() == null) ? 0 : getDeleted().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", companyId=").append(companyId);
        sb.append(", loginName=").append(loginName);
        sb.append(", password=").append(password);
        sb.append(", phoneNumber=").append(phoneNumber);
        sb.append(", avatar=").append(avatar);
        sb.append(", companyName=").append(companyName);
        sb.append(", shortName=").append(shortName);
        sb.append(", companyType=").append(companyType);
        sb.append(", businessLicense=").append(businessLicense);
        sb.append(", companyTypeId=").append(companyTypeId);
        sb.append(", signContract=").append(signContract);
        sb.append(", logoPath=").append(logoPath);
        sb.append(", description=").append(description);
        sb.append(", remark=").append(remark);
        sb.append(", address=").append(address);
        sb.append(", provincesCities=").append(provincesCities);
        sb.append(", contactName=").append(contactName);
        sb.append(", jobTitle=").append(jobTitle);
        sb.append(", contactPhone=").append(contactPhone);
        sb.append(", email=").append(email);
        sb.append(", firmSize=").append(firmSize);
        sb.append(", industry=").append(industry);
        sb.append(", parentId=").append(parentId);
        sb.append(", subsidiary=").append(subsidiary);
        sb.append(", relationImg=").append(relationImg);
        sb.append(", workImg=").append(workImg);
        sb.append(", currencyCount=").append(currencyCount);
        sb.append(", examineStatus=").append(examineStatus);
        sb.append(", status=").append(status);
        sb.append(", serachResumeStatus=").append(serachResumeStatus);
        sb.append(", viewResumeStatus=").append(viewResumeStatus);
        sb.append(", examineResult=").append(examineResult);
        sb.append(", chatId=").append(chatId);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", openid=").append(openid);
        sb.append(", sharePerson=").append(sharePerson);
        sb.append(", gradeIntegral=").append(gradeIntegral);
        sb.append(", loginTime=").append(loginTime);
        sb.append(", logoutTime=").append(logoutTime);
        sb.append(", deleted=").append(deleted);
        sb.append("]");
        return sb.toString();
    }
}