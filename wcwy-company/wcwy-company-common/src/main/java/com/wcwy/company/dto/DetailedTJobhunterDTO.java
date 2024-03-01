package com.wcwy.company.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.wcwy.company.entity.*;
import com.wcwy.company.po.CityPO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 求职者用户表
 *
 * @TableName t_jobhunter
 */
@Data
@ApiModel(value = "求职者详细信息")
public class DetailedTJobhunterDTO implements Serializable {
    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID")
    private String userId;


    /**
     * 用户姓名
     */
    @ApiModelProperty(value = "用户姓名")
    private String userName;
    /**
     * 现住地址
     */
    @ApiModelProperty(value = "现住地址")
    private CityPO address;
    /**
     * 头像路径
     */

    @ApiModelProperty(value = "头像路径")
    private String avatar;

    /**
     * 用户性别（0男 1女 2未知）
     */
    @ApiModelProperty(value = "用户性别（0男 1女 2未知）")
    private Integer sex;


    /**
     * 用户性别（0男 1女 2未知）
     */
    @ApiModelProperty(value = "是否显示先生/女士（0不显示 1:显示）")
    private Integer showSex;
    /**
     * 手机号码
     */
    @ApiModelProperty(value = "手机号码")
    private String phoneNumber;


    /**
     * 政治面貌(1:中共党员 2中共预备党员 3:共青团员 4:群众 5 民主党派)
     */
    @ApiModelProperty(value = "政治面貌(1:中共党员 2中共预备党员 3:共青团员 4:群众 5 民主党派)")
    private Integer politicsStatus;
    /**
     * 求职状态(1:实习 2:应届生 3:在职暂无跳槽 4:在职急求机会 5:在职看机会 6:离职看机会)
     */
    @ApiModelProperty(value = "求职状态(1:实习 2:应届生 3:在职暂无跳槽 4:在职急求机会 5:在职看机会 6:离职看机会)")
    private String jobStatus;

    /**
     * 用户身份(职场人,:应届生,在校生)
     */
    @ApiModelProperty(value = "用户身份(职场人,:应届生,在校生)")
    private String userType;

    /**
     * 学历
     */
    @ApiModelProperty(value = "学历")
    private String education;

    /**
     * 是否隐藏或显示目前年薪(0:显示 1:隐藏)
     */
    @ApiModelProperty(value = "是否隐藏或显示目前年薪(0:显示 1:隐藏)")
    private Integer showCurrentSalary;

    /**
     * 目前年薪
     */
    @ApiModelProperty(value = "目前年薪")
    private BigDecimal currentSalary;

    /**
     * 期望年薪
     */
/*    @ApiModelProperty(value ="期望年薪" )
    private BigDecimal expectSalary*/;

    /**
     * 年龄
     */
    @ApiModelProperty(value = "年龄")
    private Integer age;

    /**
     * 生日
     */
    @ApiModelProperty(value = "生日")
    private LocalDate birthday;

    /**
     * 参加工作时间
     */
  /*  @TableField(value = "work_time")
    @ApiModelProperty(value ="参加工作时间" )
    private LocalDate workTime;*/

    /**
     * 微信号
     */
    @TableField(value = "wechat_number")
    @ApiModelProperty(value = "微信号")
    private String wechatNumber;

    /**
     * 邮箱
     */
    @TableField(value = "email")
    @ApiModelProperty(value = "邮箱")
    private String email;

    /**
     * 所属行业
     */
/*    @TableField(value = "industry_code")
    @ApiModelProperty(value ="所属行业" )
    private String industryCode;*/

    /**
     * 帐号状态（0正常 1停用）
     */
    @TableField(value = "status")
    @ApiModelProperty(value = "帐号状态（0正常 1停用）")
    private String status;


    /**
     * 技能标签
     */
    @TableField(value = "skill", typeHandler = JacksonTypeHandler.class)
    @ApiModelProperty(value = "技能标签")
    private List<String> skill;

    /**
     * 简历附件路径
     */
    @TableField(value = "resume_path")
    @ApiModelProperty(value = "简历附件路径")
    private String resumePath;


    /**
     * 分享注册人
     */
    @ApiModelProperty(value = "分享注册人")
    private String sharePerson;
    /**
     * 登录时间
     */
    @ApiModelProperty(value = "登录时间")
    private LocalDateTime loginTime;

    @ApiModelProperty(value = "求职者简历")
    private DetailedTJobhunterResumeDTO detailedTJobhunterResumeDTO;

}