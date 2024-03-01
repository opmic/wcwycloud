package com.wcwy.company.vo;


import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wcwy.company.po.CityPO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 求职者用户表
 *
 * @TableName t_jobhunter
 */
@Data
@ApiModel(value = "修改求职者密码")
public class TJobhunterUpdatePasswordVO  {

    @ApiModelProperty("旧密码")
    @NotBlank(message = "旧密码不能为空!")
    private String oldCode;

    @ApiModelProperty("新密码")
    @NotBlank(message = "新密码不能为空!")
    private String newPassword;


}