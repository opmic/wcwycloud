package com.wcwy.company.vo;

import com.wcwy.company.po.ProvincesCitiesPO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


/**
 * ClassName: HeadhunterApproveVO
 * Description:
 * date: 2023/6/16 9:29
 *
 * @author tangzhuo
 * @since JDK 1.8
 */

@Data
@ApiModel("猎企业认证申请")
public class HeadhunterApproveVO {


    /**
     * 推荐官id
     */

    @ApiModelProperty("推荐官id")
    @NotBlank(message = "推荐官id不能为空!")
    private String recommendId;

    /**
     * 企业名称
     */
    @ApiModelProperty("企业名称")
    @NotBlank(message = "企业名称不能为空!")
    private String firmName;

    /**
     * 法人姓名
     */
    @ApiModelProperty("法人姓名")
    @NotBlank(message = "法人姓名不能为空!")
    private String legalPerson;

    /**
     * 联系方式
     */
    @ApiModelProperty("联系方式")
    @NotBlank(message = "联系方式不能为空!")
    private String phone;


    /**
     * 详细地址
     */

    @ApiModelProperty(value = "企业地址")
    @NotBlank(message = "企业地址不能为空!")
    private String address;

    /**
     * 所在省市
     */
    @ApiModelProperty(value = "所在省市")
    @Valid
    private ProvincesCitiesPO provincesCities;
    /**
     * 企业规模
     */
    @ApiModelProperty("企业规模")
    @NotBlank(message = "企业规模不能为空!")
    private String scale;

    /**
     * 简介
     */
    @ApiModelProperty("简介")
    @NotBlank(message = "简介不能为空!")
    @Size(message = "企业简介最少100字符",min = 100)
    private String briefIntroduction;

    /**
     * logo
     */
    @ApiModelProperty("logo")
    @NotBlank(message = "logo不能为空!")
    private String logo;

    /**
     * 营业执照
     */
    @ApiModelProperty("营业执照")
    @NotBlank(message = "营业执照不能为空!")
    private String businessLicense;


}
