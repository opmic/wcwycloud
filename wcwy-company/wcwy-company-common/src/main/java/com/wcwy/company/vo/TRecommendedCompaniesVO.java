package com.wcwy.company.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wcwy.company.po.ProvincesCitiesPO;
import com.wcwy.company.po.TIndustryAndTypePO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 推荐官企业表
 * @TableName t_recommended_companies
 */
@Data
@ApiModel("推荐官企业注册")
public class TRecommendedCompaniesVO {


    /**
     * 密码
     */
    @ApiModelProperty(value = "密码",required = true)
    @NotBlank(message = "密码不能为空!")
    private String password;

    /**
     * 手机号码
     */

    @ApiModelProperty(value = "手机号码",required = true)
    @NotBlank(message = "手机号码不能为空!")
    private String phoneNumber;
    /**
     * 联系人
     */
    @ApiModelProperty(value = "联系人",required = true)
    @NotBlank(message = "联系人不能为空!")
    private String contactName;

    /**
     * 联系方式
     */
    @ApiModelProperty("联系方式")
    @NotBlank(message = "手机号码不能为空!")
    private String contactPhone;
    /**
     * 头像路径
     */
 /*   @TableField(value = "avatar")
    @ApiModelProperty("头像路径")
    private String avatar;*/
    /**
     * 微信号
     */

    @ApiModelProperty(value = "微信号",required = true)
    @NotBlank(message = "微信号不能为空!")
    private String wechatNumber;
    /**
     * 企业名称
     */
    @ApiModelProperty(value = "企业名称",required = true)
    @NotBlank(message = "企业名称不能为空!")
    private String companyName;

    /**
     * 企业规模
     */
    @ApiModelProperty(value = "企业规模",required = true)
    @NotBlank(message = "企业规模不能为空!")
    private String firmSize;
    /**
     * 企业类型
     */
    @ApiModelProperty(value = "行业类型",required = true)
    @NotEmpty(message ="行业类型不能为空" )
    private List<TIndustryAndTypePO> industry;






    /**
     * 营业执照
     */
    @ApiModelProperty(value = "营业执照",required = true)
    @NotBlank(message ="营业执照不能为空!" )
    private String businessLicense;

    /**
     * 公司类型id
     */
 /*   @TableField(value = "company_type_id")
    @ApiModelProperty("公司类型id")
    private String companyTypeId;
*/
    /**
     * 企业LOGO
     */

    @ApiModelProperty(value = "企业LOGO",required = true)
    @NotBlank(message ="企业LOGO不能为空!" )
    private String logoPath;

    @ApiModelProperty("合同生效时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull(message = "日期不能为空!")
    private LocalDate beginDate;

    /**
     * 合同有效期
     */
    @ApiModelProperty("合同结束时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Future(message = "合同结束时间不能小于今天")
    private LocalDate contractDate;

    /**
     * 企业简介
     */
    @ApiModelProperty(value = "企业简介",required = true)
    @NotBlank(message ="企业简介不能为空!" )
    @Size(message = "企业简介字数不能小于100!",min = 100)
    private String description;



    /**
     * 详细地址
     */
    @ApiModelProperty("详细地址")
    @NotBlank(message ="详细地址不能为空!" )
    private String address;

    /**
     * 所在省市
     */
    @ApiModelProperty(value = "所在省市",required = true)
    @NotNull(message ="所在省市!" )
    private ProvincesCitiesPO provincesCities;



    /**
     * 职位
     */
    @ApiModelProperty(value = "职位",required = true)
    @NotBlank(message ="职位不能为空!" )
    private String jobTitle;

    /**
     * 邮箱
     */
/*    @TableField(value = "email")
    @ApiModelProperty("邮箱")
    private String email;*/

    /**
     * 分享注册人
     */
    @ApiModelProperty(value = "分享注册人",required = false)
    private String sharePerson;

    @ApiModelProperty(value = "验证码",required = true)
    @NotBlank(message = "验证码不能为空!")
    private String code;


}