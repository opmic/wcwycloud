package com.wcwy.company.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * ClassName: UpdatePersonalDetails
 * Description:
 * date: 2022/12/17 10:44
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@ApiModel("企业修改个人信息")
@Data
public class UpdatePersonalDetails {
    /**
     * 联系人
     */
    @ApiModelProperty(value = "联系人")
    private String contactName;

    /**
     * 职务
     */
    @ApiModelProperty(value = "职务")
    private String jobTitle;

    /**
     * 联系方式
     */
    @ApiModelProperty(value = "联系方式")
    private String contactPhone;
    /**
     * 企业简介
     */
    @ApiModelProperty(value = "企业简介")
    private String description;
}
