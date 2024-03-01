package com.wcwy.post.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.wcwy.post.po.DetailedAddress;
import com.wcwy.post.po.ProvincesCitiesPO;
import com.wcwy.post.po.TIndustryAndTypePO;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


import javafx.concurrent.Worker;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 企业招聘岗位表
 * @TableName t_company_post
 */
@TableName(value ="t_company_post",autoResultMap = true)
@Data
@ApiModel(value = "企业招聘岗位表")
public class TCompanyPost implements Serializable {
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
    @TableField(value = "company_name")
    @ApiModelProperty(value = "公司名称")
    private String companyName;


    @ApiModelProperty(value = "企业属性(0招聘企业 1猎头企业 2推荐官猎企)")
    @TableField(value = "company_type")
    private Integer companyType;
    /**
     * 企业虚拟名称
     */
    @TableField(value = "virtual_name")
    @ApiModelProperty(value = "企业虚拟名称")
    private String virtualName;

    /**
     * 是否隐藏虚拟名称(0:不隐藏 1:隐藏)
     */
    @TableField(value = "conceal")
    @ApiModelProperty(value = "是否隐藏虚拟名称(0:不隐藏 1:隐藏)")
    private Integer conceal;

    /**
     * 企业logo
     */
    @TableField(value = "logo")
    @ApiModelProperty(value = "企业logo")
    private String logo;

    @TableField(value = "custom_logo")
    @ApiModelProperty(value = "自定义logo")
    private String customLogo;
    /**
     * 企业规模
     */
    @TableField(value = "firm_size")
    @ApiModelProperty(value = "企业规模")
    private String firmSize;
    @TableField(value = "individual_or_team")
    @ApiModelProperty(value = "个体或机构(0:机构 1:个体)")
    private Integer individualOrTeam;
    /**
     * 企业类型
     */
    @TableField(value = "company_type_id")
    @ApiModelProperty(value = "企业类型")
    private String companyTypeId;

    /**
     * 招聘人数
     */
    @TableField(value = "post_count")
    @ApiModelProperty(value = "招聘人数")
    private Integer postCount;

    /**
     * 所属部门
     */
    @TableField(value = "department")
    @ApiModelProperty(value = "所属部门")
    private String department;

    /**
     * 部门信息
     */
    @TableField(value = "department_message")
    @ApiModelProperty(value = "部门信息")
    private String departmentMessage;

    /**
     * 开放原因(1:新增 2替换 3补缺)
     */
    @TableField(value = "publish_cause")
    @ApiModelProperty(value = "开放原因(1:新增 2替换 3补缺)")
    private String publishCause;

    /**
     * 是否管理岗
     */
    @TableField(value = "managerial_position")
    @ApiModelProperty(value = "是否管理岗(0否 1是)")
    private Integer managerialPosition;

    /**
     * 岗位属性
     */
    @TableField(value = "post_attribute")
    @ApiModelProperty(value = "岗位属性")
    private String postAttribute;

    /**
     * 行业类型
     */
    @TableField(value = "industry" , typeHandler = JacksonTypeHandler.class)
    @ApiModelProperty(value = "行业类型")
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME,property = "mytype", include=JsonTypeInfo.As.PROPERTY)
    @JsonSubTypes({ //设置对应子类的识别码值
            @JsonSubTypes.Type(value = LinkedHashMap.class, name = "TIndustryAndTypePO"),
            @JsonSubTypes.Type(value = TIndustryAndTypePO.class, name = "TIndustryAndTypePO")
    })
    private List<TIndustryAndTypePO> industry;

    /**
     * 职位类别
     */
    @TableField(value = "position" , typeHandler = JacksonTypeHandler.class)
    @ApiModelProperty(value = "职位类别")
    private List<String> position;


    /**
     * 所在城市
     */
    @TableField(value = "work_city" , typeHandler = JacksonTypeHandler.class)
    @ApiModelProperty(value = "所在城市")
    private ProvincesCitiesPO workCity;
    /**
     * 省份编码
     */
    @TableField(value = "provinceid")
    @ApiModelProperty(value = "省份编码")
    private Integer provinceid;

    /**
     * 城市编码
     */
    @TableField(value = "cityid")
    @ApiModelProperty(value = "城市编码")
    private String cityid;
    /**
     * 企业简介
     */
    @TableField(value = "enterprise_profile")
    @ApiModelProperty(value = "企业简介")
    private String enterpriseProfile;

    /**
     * 汇报对象
     */
    @TableField(value = "re_object")
    @ApiModelProperty(value = "汇报对象")
    private Integer reObject;

    /**
     * 自定义汇报对象
     */
    @TableField(value = "custom_report_obj")
    @ApiModelProperty(value = "自定义汇报对象")
    private String customReportObj;

    /**
     * 下属团队f
     */
    @TableField(value = "team")
    @ApiModelProperty(value = "下属团队")
    private Integer team;

    /**
     * 下属团队规模
     */
    @TableField(value = "custom_team")
    @ApiModelProperty(value = "下属团队规模")
    private String customTeam;

    /**
     * 职位描述
     */
    @TableField(value = "description")
    @ApiModelProperty(value = "职位描述")
    private String description;

    /**
     * 假期福利(0:其他 1:国家标准)
     */
    @TableField(value = "vacation_welfare")
    @ApiModelProperty(value = "假期福利(0:其他 1:国家标准)")
    private String vacationWelfare;

    /**
     * 职位福利
     */
    @TableField(value = "post_weal_id" , typeHandler = JacksonTypeHandler.class)
    @ApiModelProperty(value = "职位福利")
    private List<String> postWealId;

    /**
     * 招聘紧急程度
     */
    @TableField(value = "urgency")
    @ApiModelProperty(value = "招聘紧急程度")
    private Integer urgency;

    /**
     * 职位亮点
     */
    @TableField(value = "lightspot")
    @ApiModelProperty(value = "职位亮点")
    private String lightspot;

    /**
     * 社保福利
     */
    @TableField(value = "social_security_welfare")
    @ApiModelProperty(value = "社保福利")
    private String socialSecurityWelfare;

    /**
     * 岗位开始薪资
     */
    @TableField(value = "begin_salary")
    @ApiModelProperty(value = "岗位开始薪资")
    private BigDecimal beginSalary;

    /**
     * 岗位结束薪资
     */
    @TableField(value = "end_salary")
    @ApiModelProperty(value = "岗位结束薪资")
    private BigDecimal endSalary;

    /**
     * 岗位薪资组成
     */
    @TableField(value = "salary_remark")
    @ApiModelProperty(value = "岗位薪资组成")
    private String salaryRemark;

    /**
     * 工作经验
     */
    @TableField(value = "work_experience")
    @ApiModelProperty(value = "工作经验")
    private String workExperience;

    /**
     * 工作经验id
     */
    @TableField(value = "work_experience_id")
    @ApiModelProperty(value = "工作经验id")
    private Integer workExperienceId;
    /**
     * 自定义开始工作经验
     */
    @TableField(value = "custom_begin_experience")
    @ApiModelProperty(value = "自定义开始工作经验")
    private Integer customBeginExperience;

    /**
     * 自定义结束工作经验
     */
    @TableField(value = "custom_end_experience")
    @ApiModelProperty(value = "自定义结束工作经验")
    private Integer customEndExperience;

    /**
     * 学历
     */
    @TableField(value = "education_type")
    @ApiModelProperty(value = "学历")
    private String educationType;

    /**
     * 学历id
     */
    @TableField(value = "education_id")
    @ApiModelProperty(value = "学历id")
    private Integer educationId;


    /**
     * 是否统招(0否1是)
     */
    @TableField(value = "is_recruit")
    @ApiModelProperty(value = "是否统招(0否1是)")
    private Integer isRecruit;

    /**
     * 院校要求
     */
    @TableField(value = "college_type")
    @ApiModelProperty(value = "院校要求")
    private Integer collegeType;

    /**
     * 岗位名称
     */
    @TableField(value = "post_label")
    @ApiModelProperty(value = "岗位名称")
    private String postLabel;

    /**
     * 行业要求
     */
    @TableField(value = "industry_content")
    @ApiModelProperty(value = "行业要求")
    private String industryContent;
    /**
     * 语言要求
     */
    @TableField(value = "language_type")
    @ApiModelProperty(value = "语言要求")
    private String languageType;


    /**
     * 面试信息
     */
    @TableField(value = "audition_info")
    @ApiModelProperty(value = "面试信息")
    private String auditionInfo;

    /**
     * 工作城市
     */
    @TableField(value = "address")
    @ApiModelProperty(value = "工作城市")
    private String address;

    /**
     * 第几个工作日/保证期天数
     */
    @TableField(value = "workday")
    @ApiModelProperty(value = "第几个工作日/保证期天数")
    private Integer workday;

    /**
     * 入职赏金/服务结算金
     */
    @TableField(value = "hired_bounty")
    @ApiModelProperty(value = " 入职赏金/服务结算金")
    private BigDecimal hiredBounty;

    /**
     * 佣金率
     */
    @TableField(value = "percentage")
    @ApiModelProperty(value = " 佣金率")
    private Integer percentage;

    /**
     * 是否派单(0否1是)
     */
    @TableField(value = "is_dispatch")
    @ApiModelProperty(value = " 是否派单(0否1是)")
    private Integer isDispatch;

    /**
     * 岗位状态(0：停止招聘1：取消岗位：2：招聘中)
     */
    @TableField(value = "status")
    @ApiModelProperty(value = "岗位状态(0：停止招聘 1：招聘中)")
    private Integer status;

    /**
     * 截止日期
     */
    @TableField(value = "expiration_date")
    @ApiModelProperty(value = "截止日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate expirationDate;

    /**
     *岗位发布类型(0普通岗位 1:入职付 2满月付 3到面付岗位 4:简历付(校园) 5:简历付(职场)
     */
    @TableField(value = "post_type")
    @ApiModelProperty(value = "岗位发布类型(0普通岗位 1:入职付 2满月付 3到面付岗位 4:简历付(校园) 5:简历付(职场)")
    private Integer postType;
    /**
     * 工作性质
     */
    @TableField(value = "job_category")
    @ApiModelProperty(value = "工作性质")
    private String jobCategory;

    /**
     * 岗位编码
     */
    @TableField(value = "post_code")
    @ApiModelProperty(value = "岗位编码")
    private String postCode;

    /**
     * 岗位刷新时间
     */
    @TableField(value = "refresh_time")
    @ApiModelProperty(value = "岗位刷新时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime refreshTime;

    /**
     * 审核状态(0:审核中 1:审核失败 2:审核成功)
     */
    @TableField(value = "audit")
    @ApiModelProperty(value = "审核状态(0:审核中 1:审核失败 2:审核成功)")
    private Integer audit;

    /**
     * 审核失败原因
     */
    @TableField(value = "cause_of_failure")
    @ApiModelProperty(value = "审核失败原因")
    private String causeOfFailure;

    /**
     * 创建人
     */
    @TableField(value = "create_id")
    @ApiModelProperty(value = " 创建人")
    private String createId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @ApiModelProperty(value = "创建时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createTime;

    /**
     * 推荐次数
     */
    @TableField(value = "share")
    @ApiModelProperty(value = "推荐次数")
    private Integer share;

    /**
     * 更新人
     */
    @TableField(value = "update_id")
    @ApiModelProperty(value = "更新人")
    private String updateId;
    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    @ApiModelProperty(value = "更新时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime updateTime;


    @TableField(value = "day")
    @ApiModelProperty(value = "上线天数")
    private Integer day;
    @TableField(value = "top")
    @ApiModelProperty(value = "置顶(0 不置顶1置顶)")
    private Integer top;

    /**
     * 创建时间
     */
    @TableField(value = "day_time")
    @ApiModelProperty(value = "上线时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime dayTime;

    /**
     * 逻辑删除
     */
    @TableField(value = "deleted")
    @TableLogic
    private Integer deleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

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
        TCompanyPost other = (TCompanyPost) that;
        return (this.getPostId() == null ? other.getPostId() == null : this.getPostId().equals(other.getPostId()))
            && (this.getCompanyId() == null ? other.getCompanyId() == null : this.getCompanyId().equals(other.getCompanyId()))
            && (this.getCompanyName() == null ? other.getCompanyName() == null : this.getCompanyName().equals(other.getCompanyName()))
            && (this.getVirtualName() == null ? other.getVirtualName() == null : this.getVirtualName().equals(other.getVirtualName()))
            && (this.getConceal() == null ? other.getConceal() == null : this.getConceal().equals(other.getConceal()))
            && (this.getLogo() == null ? other.getLogo() == null : this.getLogo().equals(other.getLogo()))
            && (this.getFirmSize() == null ? other.getFirmSize() == null : this.getFirmSize().equals(other.getFirmSize()))
            && (this.getCompanyTypeId() == null ? other.getCompanyTypeId() == null : this.getCompanyTypeId().equals(other.getCompanyTypeId()))
            && (this.getPostCount() == null ? other.getPostCount() == null : this.getPostCount().equals(other.getPostCount()))
            && (this.getDepartment() == null ? other.getDepartment() == null : this.getDepartment().equals(other.getDepartment()))
            && (this.getDepartmentMessage() == null ? other.getDepartmentMessage() == null : this.getDepartmentMessage().equals(other.getDepartmentMessage()))
            && (this.getPublishCause() == null ? other.getPublishCause() == null : this.getPublishCause().equals(other.getPublishCause()))
            && (this.getManagerialPosition() == null ? other.getManagerialPosition() == null : this.getManagerialPosition().equals(other.getManagerialPosition()))
            && (this.getPostAttribute() == null ? other.getPostAttribute() == null : this.getPostAttribute().equals(other.getPostAttribute()))
            && (this.getIndustry() == null ? other.getIndustry() == null : this.getIndustry().equals(other.getIndustry()))
            && (this.getPosition() == null ? other.getPosition() == null : this.getPosition().equals(other.getPosition()))
            && (this.getWorkCity() == null ? other.getWorkCity() == null : this.getWorkCity().equals(other.getWorkCity()))
            && (this.getProvinceid() == null ? other.getProvinceid() == null : this.getProvinceid().equals(other.getProvinceid()))
            && (this.getCityid() == null ? other.getCityid() == null : this.getCityid().equals(other.getCityid()))
            && (this.getEnterpriseProfile() == null ? other.getEnterpriseProfile() == null : this.getEnterpriseProfile().equals(other.getEnterpriseProfile()))
            && (this.getReObject() == null ? other.getReObject() == null : this.getReObject().equals(other.getReObject()))
            && (this.getCustomReportObj() == null ? other.getCustomReportObj() == null : this.getCustomReportObj().equals(other.getCustomReportObj()))
            && (this.getTeam() == null ? other.getTeam() == null : this.getTeam().equals(other.getTeam()))
            && (this.getCustomTeam() == null ? other.getCustomTeam() == null : this.getCustomTeam().equals(other.getCustomTeam()))
            && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
            && (this.getVacationWelfare() == null ? other.getVacationWelfare() == null : this.getVacationWelfare().equals(other.getVacationWelfare()))
            && (this.getPostWealId() == null ? other.getPostWealId() == null : this.getPostWealId().equals(other.getPostWealId()))
            && (this.getUrgency() == null ? other.getUrgency() == null : this.getUrgency().equals(other.getUrgency()))
            && (this.getLightspot() == null ? other.getLightspot() == null : this.getLightspot().equals(other.getLightspot()))
            && (this.getSocialSecurityWelfare() == null ? other.getSocialSecurityWelfare() == null : this.getSocialSecurityWelfare().equals(other.getSocialSecurityWelfare()))
            && (this.getBeginSalary() == null ? other.getBeginSalary() == null : this.getBeginSalary().equals(other.getBeginSalary()))
            && (this.getEndSalary() == null ? other.getEndSalary() == null : this.getEndSalary().equals(other.getEndSalary()))
            && (this.getSalaryRemark() == null ? other.getSalaryRemark() == null : this.getSalaryRemark().equals(other.getSalaryRemark()))
            && (this.getWorkExperience() == null ? other.getWorkExperience() == null : this.getWorkExperience().equals(other.getWorkExperience()))
            && (this.getCustomBeginExperience() == null ? other.getCustomBeginExperience() == null : this.getCustomBeginExperience().equals(other.getCustomBeginExperience()))
            && (this.getCustomEndExperience() == null ? other.getCustomEndExperience() == null : this.getCustomEndExperience().equals(other.getCustomEndExperience()))
            && (this.getEducationType() == null ? other.getEducationType() == null : this.getEducationType().equals(other.getEducationType()))
            && (this.getIsRecruit() == null ? other.getIsRecruit() == null : this.getIsRecruit().equals(other.getIsRecruit()))
            && (this.getCollegeType() == null ? other.getCollegeType() == null : this.getCollegeType().equals(other.getCollegeType()))
            && (this.getPostLabel() == null ? other.getPostLabel() == null : this.getPostLabel().equals(other.getPostLabel()))
            && (this.getIndustryContent() == null ? other.getIndustryContent() == null : this.getIndustryContent().equals(other.getIndustryContent()))
            && (this.getLanguageType() == null ? other.getLanguageType() == null : this.getLanguageType().equals(other.getLanguageType()))
            && (this.getAuditionInfo() == null ? other.getAuditionInfo() == null : this.getAuditionInfo().equals(other.getAuditionInfo()))
            && (this.getAddress() == null ? other.getAddress() == null : this.getAddress().equals(other.getAddress()))
            && (this.getWorkday() == null ? other.getWorkday() == null : this.getWorkday().equals(other.getWorkday()))
            && (this.getHiredBounty() == null ? other.getHiredBounty() == null : this.getHiredBounty().equals(other.getHiredBounty()))
            && (this.getPercentage() == null ? other.getPercentage() == null : this.getPercentage().equals(other.getPercentage()))
            && (this.getIsDispatch() == null ? other.getIsDispatch() == null : this.getIsDispatch().equals(other.getIsDispatch()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getExpirationDate() == null ? other.getExpirationDate() == null : this.getExpirationDate().equals(other.getExpirationDate()))
            && (this.getPostType() == null ? other.getPostType() == null : this.getPostType().equals(other.getPostType()))
            && (this.getJobCategory() == null ? other.getJobCategory() == null : this.getJobCategory().equals(other.getJobCategory()))
            && (this.getPostCode() == null ? other.getPostCode() == null : this.getPostCode().equals(other.getPostCode()))
            && (this.getRefreshTime() == null ? other.getRefreshTime() == null : this.getRefreshTime().equals(other.getRefreshTime()))
            && (this.getAudit() == null ? other.getAudit() == null : this.getAudit().equals(other.getAudit()))
            && (this.getCauseOfFailure() == null ? other.getCauseOfFailure() == null : this.getCauseOfFailure().equals(other.getCauseOfFailure()))
            && (this.getCreateId() == null ? other.getCreateId() == null : this.getCreateId().equals(other.getCreateId()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getShare() == null ? other.getShare() == null : this.getShare().equals(other.getShare()))
            && (this.getUpdateId() == null ? other.getUpdateId() == null : this.getUpdateId().equals(other.getUpdateId()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getDeleted() == null ? other.getDeleted() == null : this.getDeleted().equals(other.getDeleted()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getPostId() == null) ? 0 : getPostId().hashCode());
        result = prime * result + ((getCompanyId() == null) ? 0 : getCompanyId().hashCode());
        result = prime * result + ((getCompanyName() == null) ? 0 : getCompanyName().hashCode());
        result = prime * result + ((getVirtualName() == null) ? 0 : getVirtualName().hashCode());
        result = prime * result + ((getConceal() == null) ? 0 : getConceal().hashCode());
        result = prime * result + ((getLogo() == null) ? 0 : getLogo().hashCode());
        result = prime * result + ((getFirmSize() == null) ? 0 : getFirmSize().hashCode());
        result = prime * result + ((getCompanyTypeId() == null) ? 0 : getCompanyTypeId().hashCode());
        result = prime * result + ((getPostCount() == null) ? 0 : getPostCount().hashCode());
        result = prime * result + ((getDepartment() == null) ? 0 : getDepartment().hashCode());
        result = prime * result + ((getDepartmentMessage() == null) ? 0 : getDepartmentMessage().hashCode());
        result = prime * result + ((getPublishCause() == null) ? 0 : getPublishCause().hashCode());
        result = prime * result + ((getManagerialPosition() == null) ? 0 : getManagerialPosition().hashCode());
        result = prime * result + ((getPostAttribute() == null) ? 0 : getPostAttribute().hashCode());
        result = prime * result + ((getIndustry() == null) ? 0 : getIndustry().hashCode());
        result = prime * result + ((getPosition() == null) ? 0 : getPosition().hashCode());
        result = prime * result + ((getWorkCity() == null) ? 0 : getWorkCity().hashCode());
        result = prime * result + ((getProvinceid() == null) ? 0 : getProvinceid().hashCode());
        result = prime * result + ((getCityid() == null) ? 0 : getCityid().hashCode());
        result = prime * result + ((getEnterpriseProfile() == null) ? 0 : getEnterpriseProfile().hashCode());
        result = prime * result + ((getReObject() == null) ? 0 : getReObject().hashCode());
        result = prime * result + ((getCustomReportObj() == null) ? 0 : getCustomReportObj().hashCode());
        result = prime * result + ((getTeam() == null) ? 0 : getTeam().hashCode());
        result = prime * result + ((getCustomTeam() == null) ? 0 : getCustomTeam().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getVacationWelfare() == null) ? 0 : getVacationWelfare().hashCode());
        result = prime * result + ((getPostWealId() == null) ? 0 : getPostWealId().hashCode());
        result = prime * result + ((getUrgency() == null) ? 0 : getUrgency().hashCode());
        result = prime * result + ((getLightspot() == null) ? 0 : getLightspot().hashCode());
        result = prime * result + ((getSocialSecurityWelfare() == null) ? 0 : getSocialSecurityWelfare().hashCode());
        result = prime * result + ((getBeginSalary() == null) ? 0 : getBeginSalary().hashCode());
        result = prime * result + ((getEndSalary() == null) ? 0 : getEndSalary().hashCode());
        result = prime * result + ((getSalaryRemark() == null) ? 0 : getSalaryRemark().hashCode());
        result = prime * result + ((getWorkExperience() == null) ? 0 : getWorkExperience().hashCode());
        result = prime * result + ((getCustomBeginExperience() == null) ? 0 : getCustomBeginExperience().hashCode());
        result = prime * result + ((getCustomEndExperience() == null) ? 0 : getCustomEndExperience().hashCode());
        result = prime * result + ((getEducationType() == null) ? 0 : getEducationType().hashCode());
        result = prime * result + ((getIsRecruit() == null) ? 0 : getIsRecruit().hashCode());
        result = prime * result + ((getCollegeType() == null) ? 0 : getCollegeType().hashCode());
        result = prime * result + ((getPostLabel() == null) ? 0 : getPostLabel().hashCode());
        result = prime * result + ((getIndustryContent() == null) ? 0 : getIndustryContent().hashCode());
        result = prime * result + ((getLanguageType() == null) ? 0 : getLanguageType().hashCode());
        result = prime * result + ((getAuditionInfo() == null) ? 0 : getAuditionInfo().hashCode());
        result = prime * result + ((getAddress() == null) ? 0 : getAddress().hashCode());
        result = prime * result + ((getWorkday() == null) ? 0 : getWorkday().hashCode());
        result = prime * result + ((getHiredBounty() == null) ? 0 : getHiredBounty().hashCode());
        result = prime * result + ((getPercentage() == null) ? 0 : getPercentage().hashCode());
        result = prime * result + ((getIsDispatch() == null) ? 0 : getIsDispatch().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getExpirationDate() == null) ? 0 : getExpirationDate().hashCode());
        result = prime * result + ((getPostType() == null) ? 0 : getPostType().hashCode());
        result = prime * result + ((getJobCategory() == null) ? 0 : getJobCategory().hashCode());
        result = prime * result + ((getPostCode() == null) ? 0 : getPostCode().hashCode());
        result = prime * result + ((getRefreshTime() == null) ? 0 : getRefreshTime().hashCode());
        result = prime * result + ((getAudit() == null) ? 0 : getAudit().hashCode());
        result = prime * result + ((getCauseOfFailure() == null) ? 0 : getCauseOfFailure().hashCode());
        result = prime * result + ((getCreateId() == null) ? 0 : getCreateId().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getShare() == null) ? 0 : getShare().hashCode());
        result = prime * result + ((getUpdateId() == null) ? 0 : getUpdateId().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getDeleted() == null) ? 0 : getDeleted().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", postId=").append(postId);
        sb.append(", companyId=").append(companyId);
        sb.append(", companyName=").append(companyName);
        sb.append(", virtualName=").append(virtualName);
        sb.append(", conceal=").append(conceal);
        sb.append(", logo=").append(logo);
        sb.append(", firmSize=").append(firmSize);
        sb.append(", companyTypeId=").append(companyTypeId);
        sb.append(", postCount=").append(postCount);
        sb.append(", department=").append(department);
        sb.append(", departmentMessage=").append(departmentMessage);
        sb.append(", publishCause=").append(publishCause);
        sb.append(", managerialPosition=").append(managerialPosition);
        sb.append(", postAttribute=").append(postAttribute);
        sb.append(", industry=").append(industry);
        sb.append(", position=").append(position);
        sb.append(", workCity=").append(workCity);
        sb.append(", provinceid=").append(provinceid);
        sb.append(", cityid=").append(cityid);
        sb.append(", enterpriseProfile=").append(enterpriseProfile);
        sb.append(", reObject=").append(reObject);
        sb.append(", customReportObj=").append(customReportObj);
        sb.append(", team=").append(team);
        sb.append(", customTeam=").append(customTeam);
        sb.append(", description=").append(description);
        sb.append(", vacationWelfare=").append(vacationWelfare);
        sb.append(", postWealId=").append(postWealId);
        sb.append(", urgency=").append(urgency);
        sb.append(", lightspot=").append(lightspot);
        sb.append(", socialSecurityWelfare=").append(socialSecurityWelfare);
        sb.append(", beginSalary=").append(beginSalary);
        sb.append(", endSalary=").append(endSalary);
        sb.append(", salaryRemark=").append(salaryRemark);
        sb.append(", workExperience=").append(workExperience);
        sb.append(", customBeginExperience=").append(customBeginExperience);
        sb.append(", customEndExperience=").append(customEndExperience);
        sb.append(", educationType=").append(educationType);
        sb.append(", isRecruit=").append(isRecruit);
        sb.append(", collegeType=").append(collegeType);
        sb.append(", postLabel=").append(postLabel);
        sb.append(", industryContent=").append(industryContent);
        sb.append(", languageType=").append(languageType);
        sb.append(", auditionInfo=").append(auditionInfo);
        sb.append(", address=").append(address);
        sb.append(", workday=").append(workday);
        sb.append(", hiredBounty=").append(hiredBounty);
        sb.append(", percentage=").append(percentage);
        sb.append(", isDispatch=").append(isDispatch);
        sb.append(", status=").append(status);
        sb.append(", expirationDate=").append(expirationDate);
        sb.append(", postType=").append(postType);
        sb.append(", jobCategory=").append(jobCategory);
        sb.append(", postCode=").append(postCode);
        sb.append(", refreshTime=").append(refreshTime);
        sb.append(", audit=").append(audit);
        sb.append(", causeOfFailure=").append(causeOfFailure);
        sb.append(", createId=").append(createId);
        sb.append(", createTime=").append(createTime);
        sb.append(", share=").append(share);
        sb.append(", updateId=").append(updateId);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", deleted=").append(deleted);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}