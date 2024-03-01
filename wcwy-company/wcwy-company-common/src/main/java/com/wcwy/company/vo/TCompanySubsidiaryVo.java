package com.wcwy.company.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.wcwy.company.po.ProvincesCitiesPO;
import com.wcwy.company.po.TIndustryAndTypePO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * ClassName: TCompanyVo
 * Description:
 * date: 2022/9/1 16:51
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel("子企业注册实体类")
public class TCompanySubsidiaryVo {


    /**
     * 验证码
     */

/*    @ApiModelProperty(value = "验证码")
    private String code;*/

    /**
     * 密码
     */

    @ApiModelProperty(value = "密码")
    @NotBlank(message = "密码不能为空")
    @Size(max = 16,min = 6 ,message = "密码要求6-18位")
    private String password;

    /**
     * 手机号码
     */

    @ApiModelProperty(value = "手机号码")
    @NotBlank(message = "手机号码不能为空")
    private String phoneNumber;

    /**
     * 头像路径
     */

    @ApiModelProperty(value = "头像路径")
    private String avatar;

    /**
     * 企业名称
     */

    @ApiModelProperty(value = "企业名称")
    @NotBlank(message = "企业名称不能为空")
    private String companyName;

    /**
     * 企业简称
     */

/*
    @ApiModelProperty(value = "企业简称")
    @NotBlank(message = "企业简称不能为空")
    private String shortName;
*/



    /**
     * 营业执照
     */

    @ApiModelProperty(value = "营业执照")
    @NotBlank(message = "营业执照不能为空")
    private String businessLicense;

    /**
     * 公司类型id
     */
    @ApiModelProperty(value = "公司类型")
    @NotBlank(message = "公司类型不能为空")
    private String companyTypeId;



    /**
     * 企业LOGO
     */

    @ApiModelProperty(value = "企业LOGO")
    @NotBlank(message = "企业LOGO不能为空")
    private String logoPath;

    /**
     * 企业简介
     */

    @ApiModelProperty(value = "企业简介")
    @NotBlank(message = "企业简介不能为空")
    @Size(message = "企业简介最少100字符",min = 100)
    private String description;

    /**
     * 详细地址
     */

    @ApiModelProperty(value = "详细地址")
    @NotBlank(message = "详细地址不能为空")
    private String address;

    @ApiModelProperty(value = "所在省市")
    private ProvincesCitiesPO provincesCities;

    /**
     * 联系人
     */

    @ApiModelProperty(value = "联系人")
    @NotBlank(message = "联系人不能为空")
    private String contactName;

    /**
     * 职务
     */
    @ApiModelProperty(value = "职务")
    @NotBlank(message = "职务不能为空")
    private String jobTitle;

    /**
     * 联系方式
     */
    @ApiModelProperty(value = "联系方式")
    @NotBlank(message = "联系方式不能为空")
    private String contactPhone;


    /**
     * 企业规模
     */
    @ApiModelProperty(value = "企业规模")
    @NotBlank(message = "企业规模不能为空")
    private String firmSize;

    /**
     * 行业类型
     */

    @ApiModelProperty(value = "行业类别")
    @NotEmpty(message = "行业类别不能为空!")
    private List<TIndustryAndTypePO> industry;
    @ApiModelProperty(value = "性别(0:未知 1:男 2:女生)",required = true)
    @NotNull(message = "性别不能为空!")
    private Integer sex;
    /**
     * 分享注册人
     */
    @ApiModelProperty(value = "分享注册人")
    private String sharePerson;
    /**
     * 父账号Id(0父账号)
     */

    @ApiModelProperty(value = "父账号Id")
    private String parentId;

    /**
     * 是否为子公司(0否1是 )
     */
    @ApiModelProperty(value = "是否为子公司(0否1是 )")
    private Integer subsidiary;


}
