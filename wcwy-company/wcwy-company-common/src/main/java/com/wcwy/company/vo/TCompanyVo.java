package com.wcwy.company.vo;

import com.wcwy.company.po.ProvincesCitiesPO;
import com.wcwy.company.po.TIndustryAndTypePO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.*;
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
@ApiModel("企业注册实体类")
public class TCompanyVo {



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

    @ApiModelProperty(value = "登录手机号码")
    @NotBlank(message = "登录手机号码不能为空")
    private String phoneNumber;

    @ApiModelProperty(value = "验证码")
    @NotBlank(message = "验证码不能为空")
    private String code;

    /**
     * 头像路径
     */

/*    @ApiModelProperty(value = "头像路径")
    private String avatar;*/

    /**
     * 企业名称
     */
 /*   @ApiModelProperty(value = "企业自定义logo")
    private String customLogo;*/



    @ApiModelProperty(value = "企业名称",required = true)
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

    @ApiModelProperty(value = "企业属性(0招聘企业 1猎头企业)")
    @Max(value = 1,message = "未知企业属性")
    @Min(value=0,message = "未知企业属性")
    @NotNull(message = "请选择企业属性")
    private Integer companyType;

    /**
     * 营业执照
     */

    @ApiModelProperty(value = "营业执照",required = true)
    @NotBlank(message = "营业执照不能为空")
    private String businessLicense;

    /**
     * 公司类型id
     */
    @ApiModelProperty(value = "企业类型",required = true)
    @NotBlank(message = "企业类型不能为空")
    private String companyTypeId;



    /**
     * 企业LOGO
     */

    @ApiModelProperty(value = "企业LOGO",required = true)
    @NotBlank(message = "企业LOGO不能为空")
    private String logoPath;

    /**
     * 企业简介
     */

    @ApiModelProperty(value = "企业简介",required = true)
    @NotBlank(message = "企业简介不能为空")
    @Size(message = "企业简介最少100字符",min = 100)
    private String description;

    /**
     * 详细地址
     */
    @ApiModelProperty(value = "详细地址",required = true)
    @NotBlank(message = "详细地址不能为空")
    private String address;

    @ApiModelProperty(value = "所在省市",required = true)
    @Valid
    private ProvincesCitiesPO provincesCities;

    /**
     * 联系人
     */

    @ApiModelProperty(value = "联系人",required = true)
    @NotBlank(message = "联系人不能为空")
    private String contactName;
/*    @ApiModelProperty(value = "性别(0:未知 1:男 2:女生)",required = true)
    @NotNull(message = "性别不能为空!")
    private Integer sex;*/
    /**
     * 职务
     */
    @ApiModelProperty(value = "职位",required = true)
    @NotBlank(message = "职位不能为空")
    private String jobTitle;

    /**
     * 联系方式
     */
    @ApiModelProperty(value = "联系电话",required = true)
    @NotBlank(message = "联系电话不能为空")
    private String contactPhone;


    /**
     * 企业规模
     */
    @ApiModelProperty(value = "企业规模",required = true)
    @NotBlank(message = "企业规模不能为空")
    private String firmSize;

    /**
     * 行业类型
     */

    @ApiModelProperty(value = "行业类别",required = true)
    private List<TIndustryAndTypePO> industry;

    /**
     * 分享注册人
     */
    @ApiModelProperty(value = "邀请码")
    private String invitationCode;
    @ApiModelProperty("秘钥")
    @NotBlank(message = "秘钥不能为空!")
    private String getKeyRate;

    @ApiModelProperty(value = "引入来源(0:用户注册推广 1:职位推广)")
    private Integer origin;
}
