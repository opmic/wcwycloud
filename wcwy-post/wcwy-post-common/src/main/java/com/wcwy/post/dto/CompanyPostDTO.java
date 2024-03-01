package com.wcwy.post.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.wcwy.company.dto.CompanyCollerctPutInResume;
import com.wcwy.post.entity.HeadhunterPositionRecord;
import com.wcwy.post.po.DetailedAddress;
import com.wcwy.post.po.ProvincesCitiesPO;
import com.wcwy.post.po.TIndustryAndTypePO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * ClassName: CompanyPost
 * Description:
 * date: 2022/10/13 11:45
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@ApiModel("岗位信息及企业信息")
@Data
@TableName(value = "t_company_post",autoResultMap = true)
public class CompanyPostDTO {

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
    @ApiModelProperty(value = "企业属性(0招聘企业 1猎头企业 2推荐官猎企)")
    private Integer companyType;
    /**
     * 公司名称
     */
    @ApiModelProperty(value = "公司名称+岗位名称")
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
    @ApiModelProperty(value = "企业规模 1:0-50人 2:50-150人 3:150-500人 4:500-1000人 5:1000-5000人 6:5000-10000人 7:10000人以上")
    private String firmSize;

    /**
     * 企业类型
     */
    @ApiModelProperty(value = "企业类型")
    private String companyTypeId;


    /**
     * 招聘人数
     */
    @TableField(value = "post_count")
    @ApiModelProperty(value = "招聘人数")
    private Integer postCount;

    /**
     * 岗位属性
     */
    @TableField(value = "post_attribute")
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
/*    @TableField(value = "provinceid")
    @ApiModelProperty(value = "省份编码")
    private Integer provinceid;*/

    /**
     * 城市编码
     */
 /*   @TableField(value = "cityid")
    @ApiModelProperty(value = "城市编码")
    private String cityid;*/

    /**
     * 企业简介
     */
    @TableField(value = "enterprise_profile")
    @ApiModelProperty(value = "企业简介")
    private String enterpriseProfile;

    /**
     * 汇报对象
     */
/*    @TableField(value = "re_object")
    @ApiModelProperty(value = "汇报对象")
    private Integer reObject;*/

    /**
     * 自定义汇报对象
     */
/*    @TableField(value = "custom_report_obj")
    @ApiModelProperty(value = "自定义汇报对象")
    private String customReportObj;*/

    /**
     * 下属团队
     */
/*
    @TableField(value = "team")
    @ApiModelProperty(value = "下属团队")
    private Integer team;
*/

    /**
     * 自定义下属团队
     */
/*    @TableField(value = "custom_team")
    @ApiModelProperty(value = "自定义下属团队")
    private String customTeam;*/

    /**
     * 职位描述
     */
    @TableField(value = "description")
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
/*
    @TableField(value = "salary_remark")
    @ApiModelProperty(value = "岗位薪资组成")
    private String salaryRemark;
*/

    /**
     * 工作经验
     */
    @TableField(value = "work_experience")
    @ApiModelProperty(value = "工作经验")
    private String workExperience;

    /**
     * 自定义开始工作经验
     */
/*    @TableField(value = "custom_begin_experience")
    @ApiModelProperty(value = "自定义开始工作经验")
    private Integer customBeginExperience;*/

    /**
     * 自定义结束工作经验
     */
/*
    @TableField(value = "custom_end_experience")
    @ApiModelProperty(value = "自定义结束工作经验")
    private Integer customEndExperience;
*/

    /**
     * 学历
     */
    @TableField(value = "education_type")
    @ApiModelProperty(value = "学历")
    private String educationType;

    /**
     * 是否统招(0否1是)
     */
    @TableField(value = "is_recruit")
    @ApiModelProperty(value = "是否统招(0否1是)")
    private Integer isRecruit;

    /**
     * 院校要求
     */
/*    @TableField(value = "college_type")
    @ApiModelProperty(value = "院校要求")
    private Integer collegeType;*/

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
/*    @TableField(value = "language_type")
    @ApiModelProperty(value = "语言要求")
    private String languageType;*/

    /**
     * 面试信息
     */
  /*  @TableField(value = "audition_info")
    @ApiModelProperty(value = "面试信息")
    private String auditionInfo;
*/


    /**
     * 第几个工作日/保证期天数
     */
    @TableField(value = "workday")
    @ApiModelProperty(value = "第几个工作日/保证期天数")
    private Integer workday;
    @ApiModelProperty(value = "详细地址")
    private String address;
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
/*    @TableField(value = "is_dispatch")
    @ApiModelProperty(value = " 是否派单(0否1是)")
    private Integer isDispatch;*/

    /**
     * 岗位状态(0：停止招聘1：取消岗位：2：招聘中)
     */
/*    @TableField(value = "status")
    @ApiModelProperty(value = "岗位状态(0：停止招聘 1：招聘中)")
    private Integer status;*/

    /**
     * 截止日期
     */
    @TableField(value = "expiration_date")
    @ApiModelProperty(value = "截止日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate expirationDate;

    /**
     * 岗位发布类型(0普通改为 1:赏金岗位 2猎头岗位)
     */
    @TableField(value = "post_type")
    @ApiModelProperty(value = " 岗位发布类型(0普通改为 1:赏金岗位 2猎头岗位)")
    private Integer postType;

    /**
     * 工作性质
     */
/*
    @TableField(value = "job_category")
    @ApiModelProperty(value = "工作性质")
    private String jobCategory;
*/

    /**
     * 岗位编码
     */
 /*   @TableField(value = "post_code")
    @ApiModelProperty(value = "岗位编码")
    private String postCode;*/

    /**
     * 岗位刷新时间
     */
/*    @TableField(value = "refresh_time")
    @ApiModelProperty(value = "岗位刷新时间")
    private LocalDateTime refreshTime;*/

    /**
     * 审核状态(0:审核中 1:审核失败 2:审核成功)
     */
/*    @TableField(value = "audit")
    @ApiModelProperty(value = "审核状态(0:审核中 1:审核失败 2:审核成功)")
    private Integer audit;*/

    /**
     * 审核失败原因
     */
/*    @TableField(value = "cause_of_failure")
    @ApiModelProperty(value = "审核失败原因")
    private String causeOfFailure;*/

    /**
     * 创建人
     */
/*    @TableField(value = "create_id")
    @ApiModelProperty(value = " 创建人")
    private String createId;*/

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    /**
     * 推荐次数
     */
    @TableField(value = "share")
    @ApiModelProperty(value = "推荐次数")
    private Integer share;


    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("企业信息+是否投简+是否收藏")
    private CompanyCollerctPutInResume tCompany;

    @ApiModelProperty("企业发布岗位的数量")
    public Integer quantity;
    @ApiModelProperty("猎头岗位金额记录表")
    private List<HeadhunterPositionRecord> headhunterPositionRecord;

    @ApiModelProperty(value = "所属部门")
    private String department;
    @ApiModelProperty(value = "部门信息")
    private String departmentMessage;

    @ApiModelProperty(value = "开放原因(1:新增 2替换 3补缺)")
    private String publishCause;

    @ApiModelProperty(value = "是否管理岗(0否 1是)")
    private Integer managerialPosition;
    /**
     * 职位类别
     */
    @TableField(value = "custom_team")
    @ApiModelProperty(value = "下属团队规模")
    private String customTeam;
    @TableField(value = "custom_report_obj")
    @ApiModelProperty(value = "自定义汇报对象")
    private String customReportObj;


    @ApiModelProperty(value = "职位亮点")
    private String lightspot;

    @ApiModelProperty(value = "社保福利")
    private String socialSecurityWelfare;

    @ApiModelProperty(value = "招聘紧急程度")
    private Integer urgency;

    @ApiModelProperty(value = "假期福利(0:其他 1:国家标准)")
    private String vacationWelfare;
    @ApiModelProperty(value = "岗位薪资组成")
    private String salaryRemark;

    @ApiModelProperty(value = "上线天数")
    private Integer day;
    @ApiModelProperty(value = "置顶(0 不置顶1置顶)")
    private Integer top;

    @ApiModelProperty("是否收藏")
    private Boolean collect;
    @ApiModelProperty("是否投简")
    public Boolean handInResume;

    @ApiModelProperty(value = "发布时间")
    private LocalDateTime dayTime;
    @ApiModelProperty(value = "联系人")
    private String contactName;
    @ApiModelProperty(value = "学历id")
    private Integer educationId;
    /**
     * 性别
     */
    @ApiModelProperty(value = "性别(0:未知 1:男 2:女生)")
    private Integer sex;
    /**
     * 职务
     */
    @ApiModelProperty(value = "职务")
    private String jobTitle;

    @ApiModelProperty(value = "登录时间")
    private LocalDateTime loginTime;

    @ApiModelProperty("预计佣金")
    private String brokerage;
}
