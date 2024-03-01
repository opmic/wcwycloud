package com.wcwy.post.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wcwy.post.entity.HeadhunterPositionRecord;
import com.wcwy.post.entity.TPostShare;
import com.wcwy.post.po.DetailedAddress;
import com.wcwy.post.po.ProvincesCitiesPO;
import com.wcwy.post.po.TIndustryAndTypePO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * ClassName: PostShare
 * Description:
 * date: 2022/9/15 16:49
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel(value = "岗位表+发布岗位纪录表")
public class PostShare {
    /**
     * 发布岗位Id
     */
    @TableId(value = "post_id")
    @ApiModelProperty(value = "发布岗位Id")
    private String postId;
    @ApiModelProperty(value = "企业属性(0招聘企业 1猎头企业)")
    private Integer companyType;
    /**
     * 企业Id
     */
    @TableField(value = "company_id")
    @ApiModelProperty(value = "企业Id")
    private String companyId;
    /**
     * 企业简介
     */
    @ApiModelProperty(value = "企业简介")
    private String enterpriseProfile;
    /**
     * 公司名称
     */
    @TableField(value = "company_name")
    @ApiModelProperty(value = "公司名称")
    private String companyName;
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
    private int conceal;
    /**
     * 企业logo
     */
    @TableField(value = "logo")
    @ApiModelProperty(value = "企业logo")
    private String logo;
    @ApiModelProperty(value = "自定义logo")
    private String customLogo;
    /**
     * 企业规模
     */
    @TableField(value = "firm_size")
    @ApiModelProperty(value = "企业规模")
    private String firmSize;

    /**
     * 企业类型
     */
    @TableField(value = "company_type_id")
    @ApiModelProperty(value = "企业类型")
    private String companyTypeId;

    /**
     * 岗位名称
     */
    @TableField(value = "post_name")
    @ApiModelProperty(value = "岗位名称(注：已经弃用)")
    private String postName;

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
    @TableField(value = "industry" , typeHandler = JacksonTypeHandler.class)
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
     * 所属区/县编码
     */
    @TableField(value = "post_area")
    @ApiModelProperty(value = "所属区/县编码")
    private String postArea;

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
     * 下属团队
     */
    @TableField(value = "team")
    @ApiModelProperty(value = "下属团队")
    private Integer team;

    /**
     * 自定义下属团队
     */
    @TableField(value = "custom_team")
    @ApiModelProperty(value = "自定义下属团队")
    private String customTeam;

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
/*
    @TableField(value = "address" , typeHandler = JacksonTypeHandler.class)
    @ApiModelProperty(value = "工作城市")
    private DetailedAddress address;
*/

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
    @ApiModelProperty(value = "岗位状态(0：停止招聘1：招聘中)")
    private Integer status;

    /**
     * 截止日期
     */
    @TableField(value = "expiration_date")
    @ApiModelProperty(value = "截止日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate expirationDate;

    /**
     * 岗位发布类型(0普通岗位 1:入职付 2满月付 3到面付岗位 4:简历付校园 5:简历付职位)
     */
    @TableField(value = "post_type")
    @ApiModelProperty(value = "岗位发布类型(0普通岗位 1:入职付 2满月付 3到面付岗位 4:简历付校园 5:简历付职位)")
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

/*    @ApiModelProperty(value = "发布岗位纪录表")
    private TPostShare tPostShare;*/

    @ApiModelProperty(value = "猎头岗位金额记录表")
    private List<HeadhunterPositionRecord> headhunterPositionRecord;


    /**
     * 分享id
     */
    @ApiModelProperty(value = "分享id")
    private String shareId;

    /**
     * 岗位id
     */
    @ApiModelProperty(value = "岗位id")
    private String companyPostId;


    @ApiModelProperty(value = "热度")
    private Long flow;

    /**
     * 推荐次数
     */
    @ApiModelProperty(value = "推荐次数")
    private Long shareSize;

    /**
     * 下载次数
     */
    @ApiModelProperty(value = "下载次数")
    private Long downloadSize;

    /**
     * 浏览次数
     */
    @ApiModelProperty(value = "浏览次数")
    private Long browseSize;

    /**
     * 面试人数
     */
    @ApiModelProperty(value = "面试人数")
    private Long interviewSize;

    /**
     * 入职人数
     */
    @ApiModelProperty(value = "入职人数")
    private Long entrySize;
    /**
     * 淘汰人数
     */
    @ApiModelProperty(value = "淘汰人数")
    private Long weedOut;
    /**
     * 预约面试
     */
    @ApiModelProperty(value = "预约面试")
    private Long subscribe;

    /**
     * offer数量
     */
    @ApiModelProperty(value = "offer数量")
    private Long offerSize;


    @ApiModelProperty(value = "接单数量")
    private Long orderReceiving=0L;
    /**
     * 过保数量
     */
    @ApiModelProperty(value = "过保数量")
    private Long overInsured;

    /**
     * 结算
     */
    @ApiModelProperty(value = "结算")
    private BigDecimal closeAnAccount;


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



    @ApiModelProperty(value = "职位亮点")
    private String lightspot;

    @ApiModelProperty(value = "社保福利")
    private String socialSecurityWelfare;

    @ApiModelProperty(value = "招聘紧急程度")
    private Integer urgency;

    @ApiModelProperty(value = "假期福利(0:其他 1:国家标准)")
    private String vacationWelfare;

    @ApiModelProperty(value = "上线天数")
    private Integer day;
    @ApiModelProperty(value = "置顶(0 不置顶1置顶)")
    private Integer top;
    @ApiModelProperty(value = "上线时间")
    private LocalDateTime dayTime;



    @ApiModelProperty(value = "岗位性质(0:普通岗位 1:人才推荐服务)")
    private Integer postNature;
    @ApiModelProperty(value = "个体或机构(0:机构 1:个体)")
    private Integer individualOrTeam;

    @ApiModelProperty(value = "简历付类型选择(0:校园 1:职场)")
    private Integer postNatureType;
}
