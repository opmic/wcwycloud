package com.wcwy.post.dto;

import com.baomidou.mybatisplus.annotation.TableField;
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

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 企业招聘岗位表
 *
 * @TableName t_company_post
 */

@ApiModel(value = "企业招聘岗位表及企业信息表")
@Data
public class TCompanyAndPost implements Serializable {
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

    /**
     * 公司名称
     */
    @ApiModelProperty(value = "公司名称")
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
    @ApiModelProperty(value = "企业规模")
    private String firmSize;

    /**
     * 企业类型
     */
    @ApiModelProperty(value = "企业类型")
    private String companyTypeId;

    @ApiModelProperty(value = "企业属性(0招聘企业 1猎头企业 2推荐官猎企)")
    private Integer companyType;
    /**
     * 招聘人数
     */
    @ApiModelProperty(value = "招聘人数")
    private Integer postCount;

    /**
     * 岗位属性
     */
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
    @ApiModelProperty(value = "职位类别")
    private List<String> position;

    /**
     * 所在城市
     */
    @ApiModelProperty(value = "所在城市")
    private ProvincesCitiesPO workCity;

    /**
     * 省份编码
     */
    @ApiModelProperty(value = "省份编码")
    private Integer provinceid;

    /**
     * 城市编码
     */
    @ApiModelProperty(value = "城市编码")
    private String cityid;

    /**
     * 企业简介
     */
    @ApiModelProperty(value = "企业简介")
    private String enterpriseProfile;

    /**
     * 汇报对象
     */
    @ApiModelProperty(value = "汇报对象")
    private Integer reObject;

    /**
     * 自定义汇报对象
     */
    @ApiModelProperty(value = "自定义汇报对象")
    private String customReportObj;

    /**
     * 下属团队
     */
    @ApiModelProperty(value = "下属团队")
    private Integer team;

    /**
     * 自定义下属团队
     */
    @ApiModelProperty(value = "自定义下属团队")
    private String customTeam;

    /**
     * 职位描述
     */
    @ApiModelProperty(value = "职位描述")
    private String description;

    /**
     * 职位福利
     */
    @ApiModelProperty(value = "职位福利")
    private List<String> postWealId;

    /**
     * 岗位开始薪资
     */
    @ApiModelProperty(value = "岗位开始薪资")
    private BigDecimal beginSalary;

    /**
     * 岗位结束薪资
     */
    @ApiModelProperty(value = "岗位结束薪资")
    private BigDecimal endSalary;

    /**
     * 岗位薪资组成
     */
    @ApiModelProperty(value = "岗位薪资组成")
    private String salaryRemark;

    /**
     * 工作经验
     */
    @ApiModelProperty(value = "工作经验")
    private String workExperience;

    /**
     * 自定义开始工作经验
     */
    @ApiModelProperty(value = "自定义开始工作经验")
    private Integer customBeginExperience;

    /**
     * 自定义结束工作经验
     */
    @ApiModelProperty(value = "自定义结束工作经验")
    private Integer customEndExperience;

    /**
     * 学历
     */
    @ApiModelProperty(value = "学历")
    private String educationType;

    /**
     * 是否统招(0否1是)
     */
    @ApiModelProperty(value = "是否统招(0否1是)")
    private Integer isRecruit;

    /**
     * 院校要求
     */
    @ApiModelProperty(value = "院校要求")
    private Integer collegeType;

    /**
     * 岗位名称
     */
    @ApiModelProperty(value = "岗位名称")
    private String postLabel;

    /**
     * 行业要求
     */
    @ApiModelProperty(value = "行业要求")
    private String industryContent;

    /**
     * 语言要求
     */
    @ApiModelProperty(value = "语言要求")
    private String languageType;

    /**
     * 面试信息
     */
    @ApiModelProperty(value = "面试信息")
    private String auditionInfo;

    /**
     * 工作城市
     */
    @ApiModelProperty(value = "工作城市")
    private String address;

    /**
     * 第几个工作日/保证期天数
     */
    @ApiModelProperty(value = "第几个工作日/保证期天数")
    private Integer workday;

    /**
     * 入职赏金/服务结算金
     */
    @ApiModelProperty(value = " 入职赏金/服务结算金")
    private BigDecimal hiredBounty;



    /**
     * 佣金率
     */
    @ApiModelProperty(value = " 佣金率")
    private Integer percentage;
    /**
     * 是否派单(0否1是)
     */
    @ApiModelProperty(value = " 是否派单(0否1是)")
    private Integer isDispatch;

    /**
     * 岗位状态(0：停止招聘1：取消岗位：2：招聘中)
     */
    @ApiModelProperty(value = "岗位状态(0：停止招聘 1：招聘中)")
    private Integer status;

    /**
     * 截止日期
     */
    @ApiModelProperty(value = "截止日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate expirationDate;

    /**
     * 岗位发布类型(0普通改为 1:赏金岗位 2猎头岗位)
     */
    @ApiModelProperty(value = "岗位发布类型(0普通岗位 1:入职付 2满月付 3到面付岗位 4:简历付校园 5:简历付职位)")
    private Integer postType;

    /**
     * 工作性质
     */
    @ApiModelProperty(value = "工作性质")
    private String jobCategory;

    /**
     * 岗位编码
     */
    @ApiModelProperty(value = "岗位编码")
    private String postCode;



    @ApiModelProperty(value = "企业信息")
    private CompanyCollerctPutInResume tcompany;
    @ApiModelProperty(value = "猎头岗位金额记录表")
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

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

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



    @ApiModelProperty(" 是否收藏(0:未收藏 1:已收藏)")
    private Integer collect=0;

    /**
     * 是否取消（1:未取消 2：取消）
     */
    @ApiModelProperty(" 是否接单（1:未取消 2：取消）")
    private Integer cancel=0;


    @ApiModelProperty(value = "岗位性质(0:普通岗位 1:人才推荐服务)")
    private Integer postNature;


    @ApiModelProperty(value = "简历付类型选择(0:校园 1:职场)")
    private Integer postNatureType;

    @ApiModelProperty("预计佣金")
    private String brokerage;
}