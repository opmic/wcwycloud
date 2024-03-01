package com.wcwy.company.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wcwy.company.po.ProvincesCitiesPO;
import com.wcwy.company.po.TIndustryAndTypePO;
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

/**
 * 推荐官
 * @TableName t_recommend
 */
@Data
@ApiModel(value = "推荐官注册")
public class InsertRecommendVO {

    /**
     * 登录名(使用电话号码)
     */
    @ApiModelProperty("账号")
    @NotBlank(message = "账号不能为空!")
    private String loginName;

    /**
     * 密码
     */
    @ApiModelProperty("密码")
    @NotBlank(message = "密码不能为空!")
    @Size(min = 6,max = 16,message = "密码不能小于6位数")
    private String password;


    @ApiModelProperty("验证码")
    @NotBlank(message = "验证码不能为空!")
    private String code;


    /**
     * 姓名
     */
    @ApiModelProperty("联系人")
    @NotBlank(message = "联系人不能为空!")
    private String username;
    /**
     * 出生年月日
     */
    @ApiModelProperty("出生年月日")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull(message = "出生年月日不能为空!")
    private LocalDate birth;

    /**
     * 头像地址
     */
    @ApiModelProperty("头像地址")
    @NotBlank(message = "头像地址不能为空!")
    private String headPath;

    /**
     * 学历
     */
    @ApiModelProperty("学历")
    @NotBlank(message = "学历不能为空!")
    private String education;

    /**
     * 性别:(1:男 2:女生)
     */
    @ApiModelProperty("性别:(1:男 2:女生)")
    @NotNull(message = "请选择性别!")
    private Integer sex;

    /**
     * 联系方式
     */
    @ApiModelProperty("联系电话")
    @NotBlank(message = "联系电话不能为空!")
    private String phone;



    @ApiModelProperty(value = "所住地")
    @Valid
    private ProvincesCitiesPO address;

    /**
     * 自我介绍
     */
    @ApiModelProperty("自我介绍")
    @NotBlank(message = "自我介绍不能为空!")
    private String manMessage;

    /**
     * 院校名称
     */
    @ApiModelProperty("院校名称")
    @NotBlank(message = "院校名称不能为空!")
    private String academy;

    /**
     * 专业
     */
    @ApiModelProperty("专业")
    @NotBlank(message = "专业不能为空!")
    private String careerId;

    /**
     * 入学时间
     */
    @ApiModelProperty("入学时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")

    private LocalDate entranceTime;

    /**
     * 毕业时间
     */
    @ApiModelProperty("毕业时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate graduateTime;


    @ApiModelProperty(value = "擅长行业")
    @NotEmpty(message = "擅长行业不能为空!")
    private List<TIndustryAndTypePO> industry;

    @ApiModelProperty(value = "推荐职位所在城市")
    @NotEmpty(message = "推荐职位所在城市不能为空")
    private List<ProvincesCitiesPO> recommendedCity;

    /**
     * 身份(0:职场基因 1：校园基因)
     */
    @ApiModelProperty("身份(0:职场精英 1：校园英才)")
    @NotNull(message = "请选择您的身份!")
    private Integer identity;
    /**
     * 分享注册人
     */
    @ApiModelProperty(value = "邀请码")
    private String invitationCode;

    @ApiModelProperty("秘钥")
    @NotBlank(message = "秘钥不能为空!")
    private String keyRate;

    @ApiModelProperty(value = "引入来源(0:用户注册推广 1:职位推广)")
    private Integer origin;
}