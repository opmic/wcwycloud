package com.wcwy.post.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.wcwy.common.base.query.PageQuery;
import com.wcwy.post.po.ProvincesCitiesPO;
import com.wcwy.post.po.TIndustryAndTypePO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * ClassName: OrderReceivingQuery
 * Description:
 * date: 2023/5/25 11:10
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel("查询收藏的岗位")
public class OrderReceivingPostDTO {
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


    @ApiModelProperty(value = "企业属性(0招聘企业 1猎头企业)")
    private Integer companyType;
    /**
     * 岗位发布类型(0普通改为 1:赏金岗位 2猎头岗位)
     */
    @ApiModelProperty(value = "岗位发布类型(0普通岗位 1:入职付 2满月付 3到面付岗位 4:简历付校园 5:简历付职位)")
    private Integer postType;
    @ApiModelProperty(" 是否接单（0未接单 1:已接单 2：取消接单）")
    private Integer cancel;
    @ApiModelProperty(value = "岗位开始薪资")
    private BigDecimal beginSalary;

    /**
     * 岗位结束薪资
     */
    @ApiModelProperty(value = "岗位结束薪资")
    private BigDecimal endSalary;


    /**
     * 接单id
     */
    @ApiModelProperty("接单id")
    private String orderReceivingId;


    /**
     * 是否收藏(0:未收藏 1:已收藏)
     */
    @ApiModelProperty(" 是否收藏(0:未收藏 1:已收藏)")
    private Integer collect;

    /**
     * 收藏时间
     */
    @ApiModelProperty(" 收藏时间")
    private LocalDateTime collectTime;
    @ApiModelProperty(value = "岗位状态(0：停止招聘 1：招聘中)")
    private Integer status;

    @ApiModelProperty(value = "岗位名称")
    private String postLabel;
}
