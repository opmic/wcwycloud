package com.wcwy.company.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.wcwy.company.po.ProvincesCitiesPO;
import com.wcwy.company.po.TIndustryAndTypePO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

/**
 * ClassName: SubsidiaryCorporation
 * Description:
 * date: 2022/12/23 8:28
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel(value = "添加子公司")
public class SubsidiaryCorporation {
    @ApiModelProperty(value = "企业id", required = false)
    private String companyId;

    /**
     * 登录账号
     */
    @ApiModelProperty(value = "登录账号",required = true)
    @NotBlank(message = "账号不能为空!")
    private String loginName;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码",required = true)
    @NotBlank(message = "密码不能为空!")
    private String password;


    /**
     * 企业名称
     */
    @ApiModelProperty(value = "企业名称",required = true)
    @NotBlank(message = "企业名称不能为空!")
    private String companyName;


    /**
     * 联系人
     */
    @ApiModelProperty(value = "联系人",required = true)
    @NotBlank(message = "联系人不能为空!")
    private String contactName;

    @ApiModelProperty(value = "性别(0:未知 1:男 2:女生)")
    @NotNull(message = "性别不能为空!")
    private Integer sex;
    /**
     * 职务
     */
    @ApiModelProperty(value = "职务",required = true)
    @NotBlank(message = "职务不能为空!")
    private String jobTitle;

    /**
     * 联系方式
     */
    @ApiModelProperty(value = "联系方式",required = true)
    @NotBlank(message = "联系方式不能为空!")
    private String contactPhone;


    /**
     * 父账号Id(0父账号)
     */
    @ApiModelProperty(value = "父账号Id",required = true)
    @NotBlank(message = "父账号Id不能为空!")
    private String parentId;

    @ApiModelProperty(value = "账号状态(0启用1停用)",required = true)
    @NotBlank(message = "账号状态不能为空!")
    private String status;
    /**
     * 是否为子公司(0否1是 )
     *//*
    @TableField(value = "subsidiary")
    @ApiModelProperty(value = "是否为子公司(0否1是 )")
    private Integer subsidiary;
*/
}
