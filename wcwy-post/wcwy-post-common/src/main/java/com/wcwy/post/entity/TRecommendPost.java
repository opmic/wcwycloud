package com.wcwy.post.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.wcwy.post.po.ProvincesCitiesPO;
import com.wcwy.post.po.TIndustryAndTypePO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 推荐官自营岗位
 * @TableName t_recommend_post
 */
@TableName(value ="t_recommend_post" ,autoResultMap = true)
@Data
@ApiModel(value = "推荐官自营岗位")
public class TRecommendPost implements Serializable {
    /**
     * 发布岗位Id
     */
    @TableId(value = "post_id")
    @ApiModelProperty(value = "发布岗位Id")
    private String postId;

    /**
     * 推荐官id
     */
    @TableField(value = "recommend_id")
    @ApiModelProperty(value = "推荐官id")
    private String recommendId;

    /**
     * 公司名称
     */
    @TableField(value = "company_name")
    @ApiModelProperty(value = "公司名称")
    private String companyName;

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
     * 职位描述
     */
    @TableField(value = "description")
    @ApiModelProperty(value = "职位描述")
    private String description;

    /**
     * 岗位开始薪资
     */
    @TableField(value = "begin_salary")
    @ApiModelProperty(value = "岗位开始薪资")
    private BigDecimal beginSalary;

    /**
     * 职位福利
     */
    @TableField(value = "post_weal_id" , typeHandler = JacksonTypeHandler.class)
    @ApiModelProperty(value = "职位福利")
    private List<String> postWealId;

    /**
     * 岗位结束薪资
     */
    @TableField(value = "end_salary")
    @ApiModelProperty(value = "岗位结束薪资")
    private BigDecimal endSalary;

    /**
     * 招聘紧急程度
     */
    @TableField(value = "urgency")
    @ApiModelProperty(value = "招聘紧急程度")
    private Integer urgency;

    /**
     * 岗位薪资组成
     */
    @TableField(value = "salary_remark")
    @ApiModelProperty(value = "岗位薪资组成")
    private String salaryRemark;

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
     * 岗位状态(0：停止招聘1:招聘中)
     */
    @TableField(value = "status")
    @ApiModelProperty(value = "岗位状态(0：停止招聘1:招聘中)")
    private Integer status;

    /**
     * 截止日期
     */
    @TableField(value = "expiration_date")
    @ApiModelProperty(value = "截止日期")
    private LocalDate expirationDate;

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
    @ApiModelProperty(value = "创建人")
    private String createId;

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
    private LocalDateTime updateTime;

    /**
     * 逻辑删除
     */
    @TableField(value = "deleted")
    @ApiModelProperty(value = "逻辑删除")
    private Integer deleted;

    /**
     * 上线天数
     */
    @TableField(value = "day")
    @ApiModelProperty(value = "上线天数")
    private Integer day;

    /**
     * 置顶(0 不置顶1置顶)
     */
    @TableField(value = "top")
    @ApiModelProperty(value = "置顶(0 不置顶1置顶)")
    private Integer top;

    /**
     * 上线时间
     */
    @TableField(value = "day_time")
    @ApiModelProperty(value = "上线时间")
    private LocalDateTime dayTime;
    /**
     * 浏览量
     */
    @TableField(value = "page_view")
    @ApiModelProperty("浏览量")
    private Long pageView;

    /**
     * 投简数量
     */
    @TableField(value = "applicant_quantity")
    @ApiModelProperty("投简数量")
    private Long applicantQuantity;

    /**
     * 已浏览简历
     */
    @TableField(value = "browse")
    @ApiModelProperty("已浏览简历")
    private Long browse;

    /**
     * 下载量
     */
    @TableField(value = "download")
    @ApiModelProperty("下载量")
    private Long download;
}