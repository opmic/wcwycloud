package com.wcwy.post.dto;

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
@Data
@ApiModel(value = "推荐官自营岗位")
public class TRecommendPostDTO{
    /**
     * 发布岗位Id
     */
    @ApiModelProperty(value = "发布岗位Id")
    private String postId;
    @ApiModelProperty(value = "审核状态(0:审核中 1:审核失败 2:审核成功)")
    private Integer audit;
    /**
     * 推荐官id
     */
    @ApiModelProperty(value = "推荐官id")
    private String recommendId;

    /**
     * 公司名称
     */
    @ApiModelProperty(value = "公司名称")
    private String companyName;

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
     * 职位描述
     */
    @ApiModelProperty(value = "职位描述")
    private String description;

    /**
     * 岗位开始薪资
     */
    @ApiModelProperty(value = "岗位开始薪资")
    private BigDecimal beginSalary;

    /**
     * 职位福利
     */
    @ApiModelProperty(value = "职位福利")
    private List<String> postWealId;

    /**
     * 岗位结束薪资
     */
    @ApiModelProperty(value = "岗位结束薪资")
    private BigDecimal endSalary;

    /**
     * 招聘紧急程度
     */
    @ApiModelProperty(value = "招聘紧急程度")
    private Integer urgency;



    /**
     * 职位亮点
     */
    @ApiModelProperty(value = "职位亮点")
    private String lightspot;



    /**
     * 工作经验
     */
    @ApiModelProperty(value = "工作经验")
    private String workExperience;



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
     * 岗位名称
     */
    @ApiModelProperty(value = "岗位名称")
    private String postLabel;


    /**
     * 工作城市
     */
    @ApiModelProperty(value = "工作城市")
    private String address;
    /**
     * 截止日期
     */
    @ApiModelProperty(value = "截止日期")
    private LocalDate expirationDate;

    /**
     * 工作性质
     */
    @ApiModelProperty(value = "工作性质")
    private String jobCategory;

    /**
     * 上线天数
     */
/*    @ApiModelProperty(value = "上线天数")
    private Integer day;*/

    /**
     * 上线时间
     */
    @ApiModelProperty(value = "上线时间")
    private LocalDateTime dayTime;


    /**
     * 姓名
     */
    @ApiModelProperty("姓名")
    private String username;

    /**
     * 头像
     */
    @ApiModelProperty("头像")
    private String headPath;

    /**
     * 性别:(1:男 2:女生)
     */
    @ApiModelProperty("性别:(1:男 2:女生)")
    private Integer sex;

    /**
     * 电话号码
     */
    @ApiModelProperty("电话号码")
    private String phone;

    /**
     * 猎企认证(0:未认证 1:申请中 2:已认证)
     */
    @ApiModelProperty("猎企认证(0:未认证 1:申请中 2:已认证)")
    private Integer administrator;

    /**
     * 企业名称
     */
    @ApiModelProperty("企业名称")
    private String firmName;
    /**
     * 浏览量
     */
    @ApiModelProperty("浏览量")
    private Long pageView;

    /**
     * 投简数量
     */
    @ApiModelProperty("投简数量")
    private Long applicantQuantity;

    /**
     * 已浏览简历
     */
    @ApiModelProperty("已浏览简历")
    private Long browse;

    /**
     * 下载量
     */
    @ApiModelProperty("下载量")
    private Long download;
    @ApiModelProperty(value = "岗位状态(0：停止招聘1:招聘中)")
    private Integer status;

}