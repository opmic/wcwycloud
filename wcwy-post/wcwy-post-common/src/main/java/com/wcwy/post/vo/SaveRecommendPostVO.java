package com.wcwy.post.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.wcwy.post.po.ProvincesCitiesPO;
import com.wcwy.post.po.TIndustryAndTypePO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.*;
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
@ApiModel(value = "添加自营岗位")
public class  SaveRecommendPostVO {


    /**
     * 公司名称
     */

    @ApiModelProperty(value = "公司名称",required = true)
    @NotBlank(message = "公司名称不能为空！")
    private String companyName;



    @NotBlank(message = "企业规模不能为空！")
    @ApiModelProperty(value = "企业规模",required = true)
    private String firmSize;



  /*  @ApiModelProperty(value = "自定义logo")
    private String customLogo;*/
    /**
     * 企业规模
     */



    /**
     * 企业类型
     */

    @ApiModelProperty(value = "企业类型")
    @NotBlank(message = "企业类型不能为空！")
    private String companyTypeId;


    /**
     * 招聘人数
     */

    @ApiModelProperty(value = "招聘人数",required = true)
    @NotNull(message = "招聘人数不能为空！")
    private Integer postCount;


    /**
     * 行业类型
     */

    @ApiModelProperty(value = "行业类型",required = true)
   // @NotEmpty(message = "行业类型不能为空！")
    private List<TIndustryAndTypePO> industry;



    @ApiModelProperty(value = "职位类别")
      @NotEmpty(message = "职位类别不能为空！")
    private List<String> position;










    /**
     * 所在城市
     */
    @ApiModelProperty(value = "所在城市",required = true)
    @Valid
    private ProvincesCitiesPO workCity;


    /**
     * 职位描述
     */

    @ApiModelProperty(value = "职位描述",required = true)
    @NotBlank(message = "职位描述描述！")
    @Size(max = 2000,min = 0,message = "职位描述字数不能超过2000！")
    private String description;

    /**
     * 职位福利
     */

    @ApiModelProperty(value = "职位福利")
    private List<String> postWealId;

    /**
     * 岗位开始薪资
     */

    @ApiModelProperty(value = "岗位开始薪资",required = true)
    @NotNull(message = "岗位开始薪资不能为空")
    private BigDecimal beginSalary;

    /**
     * 岗位结束薪资
     */

    @ApiModelProperty(value = "岗位结束薪资",required = true)
    @NotNull(message = "岗位结束薪资不能为空")
    private BigDecimal endSalary;


    /**
     * 工作经验
     */

    @ApiModelProperty(value = "工作经验",required = true)
    @NotBlank(message = "工作经验不能为空！")
    private String workExperience;


    /**
     * 学历
     */

    @ApiModelProperty(value = "学历",required = true)
    @NotBlank(message = "学历不能为空！")
    private String educationType;

    /**
     * 是否统招(0否1是)
     */

    @ApiModelProperty(value = "是否统招(0否1是)",required = true)
    @NotNull(message = "是否统招不能为空！")
    private Integer isRecruit;

    /**
     * 岗位名称
     */

    @ApiModelProperty(value = "岗位名称",required = true)
    @NotBlank(message = "岗位名称不能为空！")
    private String postLabel;

    /**
     * 行业要求
     */

 /*   @ApiModelProperty(value = "行业要求")
    @NotBlank(message = "任职要求不能为空！")
    private String industryContent;*/
    @NotBlank(message = "详细地址不能为空！")
    @ApiModelProperty(value = "详细地址",required = true)
    private String address;

    /**
     * 工作城市
     */
  /*  @ApiModelProperty(value = "工作城市")
    @Valid
    private DetailedAddress address;
*/
    /**
     * 第几个工作日/保证期天数
     */

/*
    @ApiModelProperty(value = "支付时间")
    private Integer workday;
*/

    /**
     * 入职赏金/服务结算金
     */

/*    @ApiModelProperty(value = " 入职赏金")
    private BigDecimal hiredBounty;*/


 /*   @ApiModelProperty(value = "佣金率")
    private Integer percentage;*/
    /**
     * 截止日期
     */

    @ApiModelProperty(value = "截止日期",required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Future(message = "截止日期必须是一个将来的日期")
    @NotNull(message = "截止日期不能为空")
    private LocalDate expirationDate;

    /**
     * 工作性质
     */
    @ApiModelProperty(value = "工作性质",required = true)
    @NotNull(message = "工作性质不能为空")
    private String jobCategory;




}