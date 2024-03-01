package com.wcwy.post.dto;

import com.wcwy.post.po.ProvincesCitiesPO;
import com.wcwy.post.po.TIndustryAndTypePO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 推荐官自营岗位
 * @TableName t_recommend_post
 */
@Data
@ApiModel(value = "推荐官自营岗位简要数据")
public class TRecommendPostBriefDTO {
    /**
     * 发布岗位Id
     */
    @ApiModelProperty(value = "发布岗位Id")
    private String postId;

    /**
     * 推荐官id
     */
    @ApiModelProperty(value = "推荐官id")
    private String recommendId;

    /**
     * 公司名称
     */
    @ApiModelProperty(value = "招娉公司名称")
    private String companyName;

    /**
     * 招聘人数
     */
    @ApiModelProperty(value = "招聘人数")
    private Integer postCount;


    /**
     * 所在城市
     */
    @ApiModelProperty(value = "所在城市")
    private ProvincesCitiesPO workCity;



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


    @ApiModelProperty(value = "审核状态(0:审核中 1:审核失败 2:审核成功)")
    private Integer audit;

    @ApiModelProperty(value = "岗位状态(0：停止招聘1:招聘中)")
    private Integer status;



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
     * 岗位名称
     */
    @ApiModelProperty(value = "岗位名称")
    private String postLabel;



    /**
     * 截止日期
     */
    @ApiModelProperty(value = "截止日期")
    private LocalDate expirationDate;


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
     * 猎企认证(0:未认证 1:申请中 2:已认证)
     */
    @ApiModelProperty("猎企认证(0:未认证 1:申请中 2:已认证)")
    private Integer administrator;

    /**
     * 企业名称
     */
    @ApiModelProperty("推荐官企业名称")
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


}