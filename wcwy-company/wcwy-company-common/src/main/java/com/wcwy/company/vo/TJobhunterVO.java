package com.wcwy.company.vo;


import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wcwy.company.po.CityPO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 求职者用户表
 *
 * @TableName t_jobhunter
 */
@Data
@ApiModel(value = "修改求职者")
public class  TJobhunterVO implements Serializable {
    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID")
    @NotBlank(message = "用户ID不能为空")
    private String userId;


    /**
     * 用户姓名
     */

    @ApiModelProperty(value = "用户姓名")
    @NotBlank(message = "用户姓名不能为空")
    private String userName;
    /**
     * 现住地址
     */

    @ApiModelProperty(value = "现住地址")
    @Valid
    @NotNull(message = "现住地址信息不能为空")
    private CityPO address;

    /**
     * 用户性别（0男 1女 2未知）
     */

    @ApiModelProperty(value = "用户性别（0男 1女 2未知）")
    @NotNull(message = "用户性别信息不能为空")
    @Max(value = 2,message = "性别选择不正确!")
    @Min(value = 0,message = "性别选择不正确!")
    private Integer sex;

    /**
     * 联系电话
     */

    @ApiModelProperty(value ="联系电话" )
    @NotNull(message = "联系电话不能为空")
    private String phone;
    /**
     * 用户性别（0男 1女 2未知）
     */
 /*   @ApiModelProperty(value = "是否显示先生/女士（0不显示 1:显示）")
    @NotNull(message = "是否显示先生/女士不能为空")

    private Integer showSex;*/

    /**
     * 求职状态(1:实习 2:应届生 3:在职暂无跳槽 4:在职急求机会 5:在职看机会 6:离职看机会)
     */
    @ApiModelProperty(value = "求职状态(1:离校-随时到岗(离职-随时到岗) 2:在校-月内到岗(在职-月内到岗) 3:在校-考虑机会(在职-考虑机会) 4:在校-暂不考虑(在职-暂不考虑))")
    @NotNull(message = "求职状态不为空")
    @Max(value = 4,message = "身份选择不正确!")
    @Min(value = 1,message = "身份选择不正确!")
    private Integer jobStatus;


    @ApiModelProperty(value = "政治面貌(1:中共党员 2中共预备党员 3:共青团员 4:群众 5 民主党派)")
    /*@NotNull(message = "政治面貌不能为空")*/
    private Integer politicsStatus;
    /**
     * 用户身份(职场人,:应届生,在校生)
     */
    @TableField(value = "user_type")
    @ApiModelProperty(value ="用户身份(1职场精英,2校园人才)" )
    @NotNull(message = "用户身份不能为空!")
    @Max(value = 2,message = "身份选择不正确!")
    @Min(value = 1,message = "身份选择不正确!")
    private Integer userType;


    /**
     * 是否隐藏或显示目前年薪(0:显示 1:隐藏)
     */
/*    @TableField(value = "show_current_salary")
    @ApiModelProperty(value = "是否隐藏或显示目前年薪(0:显示 1:隐藏)")
    @NotNull(message = "是否隐藏或显示目前年薪")
    private Integer showCurrentSalary;*/

    /**
     * 目前年薪
     */
    @ApiModelProperty(value = "目前年薪(单位W)")
    @NotNull(message = "目前年薪可以填0！")
    private BigDecimal currentSalary;


    /**
     * 生日
     */
    @ApiModelProperty(value = "生日")
    @Past(message = "出生日期必须是一个过去的日期")
    @NotNull(message = "出生日期不能为空")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthday;
    /**
     * 参加工作时间
     */
    @ApiModelProperty(value ="参加工作时间" )
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
/*    @NotNull(message = "参加工作时间可以选择当天！")*/
    private LocalDate workTime;
    /**
     * 微信号
     */
    @ApiModelProperty(value = "微信号")
   // @NotBlank(message = "微信号不能为空")
    private String wechatNumber;

    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱")
    @Email(message = "邮箱不正确")
    private String email;


    /**
     * 简历附件路径
     */
    @ApiModelProperty(value = "简历附件路径")
    private String resumePath;


}