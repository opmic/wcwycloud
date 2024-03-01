package com.wcwy.company.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * ClassName: TCompanyUserVO
 * Description:
 * date: 2023/3/27 9:24
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@ApiModel("修改企业个人信息")
public class TCompanyUserVO {
    /**
     * 联系人
     */

    @ApiModelProperty(value = "联系人",required = true)
    @NotBlank(message = "联系人不能为空")
    private String contactName;
    /**
     * 职务
     */
    @ApiModelProperty(value = "职位",required = true)
    @NotBlank(message = "职位不能为空")
    private String jobTitle;

    /**
     * 联系方式
     */

    @NotBlank(message = "联系方式不能为空")
    @ApiModelProperty(value = "联系方式",required = true)
    private String contactPhone;

    @ApiModelProperty(value = "头像路径")
    @NotBlank(message = "头像路径不能为空!")
    private String avatar;
    /**
     * 性别
     */
    @ApiModelProperty(value = "性别(0:未知 1:男 2:女生)")
    @NotNull(message = "性别不能为空!")
    private Integer sex;


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "出生日")
    @NotNull(message = "出生日不能为空!")
    private LocalDateTime birthday;
}
