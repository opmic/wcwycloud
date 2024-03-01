package com.wcwy.company.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.wcwy.company.po.ProvincesCitiesPO;
import com.wcwy.company.po.TIndustryAndTypePO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
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
@ApiModel("修改子企业")
public class UpdateTCompanySubsidiaryVo {
    @ApiModelProperty(value = "企业Id")
    @NotBlank(message = "企业id不能为空")
    private String companyId;
    /**
     * 密码
     */

    @ApiModelProperty(value = "密码")
    @NotBlank(message = "密码不能为空")
    @Size(max = 16,min = 6 ,message = "密码要求6-18位")
    private String password;

    /**
     * 企业名称
     */

    @ApiModelProperty(value = "企业名称")
    @NotBlank(message = "企业名称不能为空")
    private String companyName;

    @ApiModelProperty(value = "账号")
    @NotBlank(message = "账号不能为空")
    private String loginName;



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
    @ApiModelProperty(value = "性别(0:未知 1:男 2:女生)")
    @NotNull(message = "性别不能为空!")
    private Integer sex;
    /**
     * 联系方式
     */
    @ApiModelProperty(value = "联系方式")
    @NotBlank(message = "联系方式不能为空")
    private String contactPhone;

    /**
     * 账号状态(0正常1停用)
     */
    @ApiModelProperty(value = "账号状态(0启用1停用)")
    @NotBlank(message = "请选择账号状态")
    private String status;
}
