package com.wcwy.post.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.wcwy.post.po.ProvincesCitiesPO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 接单表
 * @TableName order_receiving
 */
@Data
@ApiModel("接单大厅")
public class OrderReceivingDTO implements Serializable {
    /**
     * 接单id
     */
    @ApiModelProperty("接单id")
    private String orderReceivingId;

    private Integer educationId;
    /**
     * 岗位id
     */
    @ApiModelProperty("岗位id")
    private String postId;

    /**
     * 推荐官id
     */
    @ApiModelProperty("推荐官id")
    private String recommerd;

    /**
     * 接单时间
     */
    @ApiModelProperty("接单时间")
    private LocalDateTime createTime;

    /**
     * 是否收藏(0:未收藏 1:已收藏)
     */
    @ApiModelProperty(" 是否收藏(0:未收藏 1:已收藏)")
    private Integer collect=0;

    /**
     * 收藏时间
     */
    @ApiModelProperty(" 收藏时间")
    private LocalDateTime collectTime;

    /**
     * 是否取消（1:未取消 2：取消）
     */
    @ApiModelProperty(" 接单是否取消（0未接单 1:已接单 2：取消接单）")
    private Integer cancel=0;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;



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


    @ApiModelProperty(value = "企业属性(0招聘企业 1猎头企业)")
    private Integer companyType;
    /**
     * 企业虚拟名称
     */
    @ApiModelProperty(value = "企业虚拟名称")
    private String virtualName;

    /**
     * 是否隐藏虚拟名称(0:不隐藏 1:隐藏)
     */
    @ApiModelProperty(value = "是否隐藏虚拟名称(0:不隐藏 1:隐藏)")
    private Integer conceal;



    /**
     * 企业logo
     */
    @ApiModelProperty(value = "企业logo")
    private String logo;

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
     * 工作经验
     */
    @TableField(value = "work_experience")
    @ApiModelProperty(value = "工作经验")
    private String workExperience;

    /**
     * 学历
     */
    @TableField(value = "education_type")
    @ApiModelProperty(value = "学历")
    private String educationType;

    /**
     * 岗位名称
     */
    @TableField(value = "post_label")
    @ApiModelProperty(value = "岗位名称")
    private String postLabel;



    @ApiModelProperty(value = "岗位状态(0：停止招聘 1：招聘中)")
    private Integer status;

    /**
     * 截止日期
     */
    @ApiModelProperty(value = "截止日期")
    private LocalDate expirationDate;

    /**
     * 岗位发布类型(0普通改为 1:赏金岗位 2猎头岗位)
     */
    @ApiModelProperty(value = "岗位发布类型(0普通岗位 1:入职付 2满月付 3到面付岗位 4:简历付(校园) 5:简历付(职场)")
    private Integer postType;
    @ApiModelProperty(value = "上线时间")
    private LocalDateTime dayTime;

    /**
     * 推荐次数
     */
    @TableField(value = "share_size")
    @ApiModelProperty(value = "推荐次数")
    private Long shareSize;
    /**
     * 浏览次数
     */
    @TableField(value = "browse_size")
    @ApiModelProperty(value = "浏览次数")
    private Long browseSize;

    /**
     * 入职人数
     */
    @TableField(value = "entry_size")
    @ApiModelProperty(value = "入职人数")
    private Long entrySize;
    /**
     * 淘汰人数
     */
    @TableField(value = "weed_out")
    @ApiModelProperty(value = "淘汰人数")
    private Long weedOut;
    /**
     * 预约面试
     */
/*    @TableField(value = "subscribe")
    @ApiModelProperty(value = "预约面试")
    private Long subscribe;*/

    /**
     * offer数量
     */
    @TableField(value = "offer_size")
    @ApiModelProperty(value = "offer数量")
    private Long offerSize;

    @ApiModelProperty(value = "岗位数量")
    private Long posts;

    @ApiModelProperty(value = "面试人数")
    private Long interviewSize;
    @ApiModelProperty(value = "下载次数")
    private Long downloadSize;
    @ApiModelProperty(value = "满月数量")
    private Long overInsured;

    @ApiModelProperty("预计佣金")
    private String brokerage;

    @ApiModelProperty("是否投简")
    private Boolean sendAResume;
}