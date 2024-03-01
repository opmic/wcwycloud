package com.wcwy.post.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.wcwy.company.po.TIndustryAndTypePO;
import com.wcwy.post.entity.TCompanyPost;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 企业热度表
 * @TableName t_company_hot
 */
@Data
@ApiModel(value = "企业热度及岗位信息")
public class TCompanyHotPostDTO implements Serializable {
    /**
     * 热门id
     */
    @ApiModelProperty(value = "热门id")
    private Integer id;
    /**
     * 热度
     */
    @ApiModelProperty(value = "热度")
    private Long hot;
    /**
     * 企业id
     */
    @ApiModelProperty(value = "企业id")
    private String companyId;
    /**
     * 公司类型id
     */
    @TableField(value = "company_type_id")
    private String companyTypeId;

    /**
     * 行业类别
     */
    @TableField(value = "industry",typeHandler = JacksonTypeHandler.class)
    private List<TIndustryAndTypePO> industry;

    /**
     * 企业规模
     */
    @TableField(value = "firm_size")
    private String firmSize;
    @ApiModelProperty(value = "企业logo")
    private String logo;
    @ApiModelProperty(value = "自定义logo")
    private String customLogo;
    @ApiModelProperty(value = "企业名称")
    private String companyName;

    @ApiModelProperty(value = "岗位信息")
    private List<TCompanyPost> tCompanyPosts;
}