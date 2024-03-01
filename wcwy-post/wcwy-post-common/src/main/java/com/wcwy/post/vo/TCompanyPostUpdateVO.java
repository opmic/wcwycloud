package com.wcwy.post.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wcwy.post.po.DetailedAddress;
import com.wcwy.post.po.ProvincesCitiesPO;
import com.wcwy.post.po.TIndustryAndTypePO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * ClassName: TCompanyPostUpdateVO
 * Description:
 * date: 2022/9/16 14:03
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@ApiModel(value = "更新岗位实体类")
@Data
public class TCompanyPostUpdateVO {
    /**
     * 发布岗位Id
     */
    @ApiModelProperty(value = "发布岗位Id",required = true)
    @NotBlank(message = "岗位id不能为空")
    private String postId;

    /**
     * 企业Id
     */

    @ApiModelProperty(value = "企业Id",required = true)
    @NotBlank(message = "企业Id不能为空！")
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

    @NotBlank(message = "企业logo不能为空！")
    @ApiModelProperty(value = "企业logo",required = true)
    private String logo;
/*    @ApiModelProperty(value = "自定义logo")

    private String customLogo;*/
    /**
     * 企业规模
     */

  /*  @ApiModelProperty(value = "企业规模")
    @NotBlank(message = "企业规模不能为空！")
    private String firmSize;*/

    /**
     * 企业类型
     */
/*

    @ApiModelProperty(value = "企业类型")
    @NotBlank(message = "企业类型不能为空！")
    private String companyTypeId;
*/
    @NotBlank(message = "企业规模不能为空！")
    @ApiModelProperty(value = "企业规模",required = true)
    private String firmSize;


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
    @NotEmpty(message = "行业类型不能为空！")
    private List<TIndustryAndTypePO> industry;

    /**
     * 职位类别
     */

    @ApiModelProperty(value = "职位类别")
  /*  @NotEmpty(message = "职位类别不能为空！")*/
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
    @NotBlank(message = "职位描述不能为空！")
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
/*
    @ApiModelProperty(value = "行业要求")
    @NotBlank(message = "任职要求不能为空！")
    private String industryContent;*/
    /**
     * 企业简介
     */
  /*  @ApiModelProperty(value = "企业简介")
    @NotBlank(message = "企业简介不能为空！")
    @Size(message = "企业简介100个字",min = 100)
    private String enterpriseProfile;*/


    /**
     * 工作城市
     */
 /*   @ApiModelProperty(value = "工作城市")
    @Valid
    private DetailedAddress address;*/

    /**
     * 第几个工作日/保证期天数
     */

/*    @ApiModelProperty(value = "支付时间")
    private Integer workday;*/
    @NotBlank(message = "详细地址不能为空！")
    @ApiModelProperty(value = "详细地址",required = true)
    private String address;
    /**
     * 入职赏金/服务结算金
     */

/*    @ApiModelProperty(value = " 入职赏金")
    private BigDecimal hiredBounty;*/

    /**
     * 截止日期
     */

    @ApiModelProperty(value = "截止日期" )
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Future(message = "截止日期必须将来的日期! ")
    private LocalDate expirationDate;

    /**
     * 岗位发布类型(0普通改为 1:赏金岗位 2猎头岗位)
     */

/*    @ApiModelProperty(value = " 岗位发布类型(0普通改为 1:入职付 2满月付 3到面付 4简历付)",required = true)
    @NotNull(message = "请选择岗位类型！")
    @Max(value = 4,message = "岗位发布类型选择不正确！")
    @Min(value = 0,message = "岗位发布类型选择不正确！")
    private Integer postType;

    @ApiModelProperty(value = "岗位性质(0:普通岗位 1:人才推荐服务)",required = true)
    @NotNull(message = "请选择岗位性质!")
    @Max(value = 1,message = "职位性质选择不正确！")
    @Min(value = 0,message = "职位性质选择不正确！")
    private Integer postNature;


    @ApiModelProperty(value = "简历付类型选择(0:校园 1:职场)")
    @Max(value = 1,message = "职位性质选择不正确！")
    @Min(value = 0,message = "职位性质选择不正确！")
    private Integer postNatureType;*/
    /**
     * 工作性质
     */
    @ApiModelProperty(value = "工作性质",required = true)
    @NotNull(message = "工作性质不能为空")
    private String jobCategory;

/*    @ApiModelProperty(value = " 猎头岗位金额")
    private List<HeadhunterPositionRecordVO> headhunterPositionRecordVOS;*/


    @ApiModelProperty(value = "职位亮点")
    private String lightspot;

    @ApiModelProperty(value = "招聘紧急程度")
    private Integer urgency;

}
