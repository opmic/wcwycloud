package com.wcwy.company.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * ClassName: inviteVO
 * Description:
 * date: 2023/3/8 9:52
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel(value = "企业邀请子账号")
public class InviteVO {
    @ApiModelProperty("企业id")
    @NotBlank(message = "企业id不能为空")
    private String tCompanyId;


    @ApiModelProperty(value = "头像路径")
    @NotBlank(message = "头像路径")
    private String avatar;



    @ApiModelProperty(value = "企业名称")
    @NotBlank(message = "企业名称")
    private String companyName;
    /**
     * 联系人
     */
    @ApiModelProperty(value = "邀请人")
    @NotBlank(message = "邀请人")
    private String contactName;



    @ApiModelProperty(value = "职务")
    @NotBlank(message = "职务")
    private String jobTitle;

    @ApiModelProperty(value = "请求地址")
    @NotBlank(message = "请求地址地址不能为空")
    private String url;

    @ApiModelProperty(value = "登录账号")
    @NotBlank(message = "登录账号不能为空!")
    private String loginName;
}
