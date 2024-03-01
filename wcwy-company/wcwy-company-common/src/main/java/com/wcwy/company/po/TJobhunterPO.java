package com.wcwy.company.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
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
 * @TableName t_jobhunter
 */

@Data
@ApiModel(value = "求职者用户表")
public class TJobhunterPO implements Serializable {
    /**
     * 用户ID
     */
    /**
     * 生日
     */
    @TableField(value = "birthday")
    @ApiModelProperty(value ="生日" )
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @ApiModelProperty(value ="用户ID")
    private String userId;
    /**
     * 参加工作时间
     */
    @ApiModelProperty(value ="参加工作时间" )
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate workTime;

    /**
     * 用户姓名
     */
    @ApiModelProperty(value ="用户姓名" )
    private String userName;


    @ApiModelProperty(value ="现住地址" )
    private CityPO address;
    /**
     * 头像路径
     */
    @ApiModelProperty(value ="头像路径" )
    private String avatar;

    /**
     * 用户性别（0男 1女 2未知）
     */
    @ApiModelProperty(value ="用户性别（0男 1女 2未知）" )
    private Integer sex;
    @ApiModelProperty(value ="是否显示先生/女士（0不显示 1:显示）" )
    private Integer showSex;
    /**
    /**
     * 手机号码
     */
    @ApiModelProperty(value ="手机号码" )
    private String phoneNumber;
    /**
     * 政治面貌(1:中共党员 2中共预备党员 3:共青团员 4:群众 5 民主党派)
     */
    @ApiModelProperty(value ="政治面貌(1:中共党员 2中共预备党员 3:共青团员 4:群众 5 民主党派)" )
    private Integer politicsStatus;
    /**
     * 求职状态(1:实习 2:应届生 3:在职暂无跳槽 4:在职急求机会 5:在职看机会 6:离职看机会)
     */
    @ApiModelProperty(value ="求职状态(1:实习 2:应届生 3:在职暂无跳槽 4:在职急求机会 5:在职看机会 6:离职看机会)" )
    private String jobStatus;

    /**
     * 用户身份z
     */
    @ApiModelProperty(value ="用户身份(职场人,:应届生,在校生)" )
    private String userType;

    /**
     * 目前年薪
     */
    @ApiModelProperty(value ="目前年薪" )
    private BigDecimal currentSalary;
    @ApiModelProperty(value ="是否隐藏或显示目前年薪(0:显示 1:隐藏)" )
    private Integer showCurrentSalary;


    /**
     * 年龄
     */
/*    @ApiModelProperty(value ="年龄" )
    private Integer age;*/
    /**
     * 联系电话
     */
    @TableField(value = "phone")
    @ApiModelProperty(value ="联系电话" )
    private String phone;


    /**
     * 微信号
     */
    @ApiModelProperty(value ="微信号" )
    private String wechatNumber;

    /**
     * 邮箱
     */
    @ApiModelProperty(value ="邮箱" )
    private String email;


    /**
     * 简历附件路径
     */
    @ApiModelProperty(value ="简历附件路径" )
    private String resumePath;

    /**
     * 审核状态(0待审核1审核通过2审核不通过)
     */
    @ApiModelProperty(value ="审核状态(0待审核1审核通过2审核不通过)" )
    private Integer examineStatus;

    /**
     * 审核意见
     */
    @ApiModelProperty(value ="审核意见" )
    private String resumeExamineResult;

    /**
     * 分享注册人
     */
    @ApiModelProperty(value ="分享注册人" )
    private String sharePerson;
    /**
     * 登录时间
     */
/*    @ApiModelProperty(value ="登录时间" )
    private LocalDateTime loginTime;
    *//**
     * 创建时间
     *//*
    @ApiModelProperty(value ="创建时间" )
    private LocalDateTime createTime;

    *//**
     * 更新时间
     *//*
    @TableField(value = "update_time")
    @ApiModelProperty(value ="更新时间" )
    private LocalDateTime updateTime;*/
    @ApiModelProperty(value ="微信openid" )
    private String openid;

    @ApiModelProperty(value = "申请岗位的数量")
    private Integer posts;
    @ApiModelProperty(value = "求职者反馈")
    private Integer feedback;
    @ApiModelProperty("身份(TJ:求职者 TC:企业 TR:推荐官)")
    private List<String> authorization;
}