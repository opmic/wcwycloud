package com.wcwy.company.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wcwy.company.po.CityPO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * 求职者用户表
 * @TableName t_jobhunter
 */
@TableName(value ="t_jobhunter",autoResultMap = true)
@Data
@ApiModel(value = "求职者用户表")
public class TJobhunter implements Serializable {
    /**
     * 用户ID
     */
    @TableId(value = "user_id",type = IdType.INPUT)
    @ApiModelProperty(value ="用户ID")
    private String userId;

    /**
     * 登录账号
     */
    @TableField(value = "login_name")
    @ApiModelProperty(value ="登录账号" )
    private String loginName;



    /**
     * 密码
     */
    @TableField(value = "password")
    @ApiModelProperty(value ="密码" )
    private String password;

    /**
     * 用户姓名
     */
    @TableField(value = "user_name")
    @ApiModelProperty(value ="用户姓名" )
    private String userName;
    /**
     * 现住地址
     */
    @TableField(value = "address",typeHandler = JacksonTypeHandler.class)
    @ApiModelProperty(value ="现住地址" )
    private CityPO address;
    /**
     * 头像路径
     */
    @TableField(value = "avatar")
    @ApiModelProperty(value ="头像路径" )
    private String avatar;

    /**
     * 用户性别（0男 1女 2未知）
     */
    @TableField(value = "sex")
    @ApiModelProperty(value ="用户性别（0男 1女 2未知）" )
    private Integer sex;


    /**
     * 用户性别（0男 1女 2未知）
     */
    @TableField(value = "show_sex")
    @ApiModelProperty(value ="是否显示先生/女士（0不显示 1:显示）" )
    private Integer showSex;
    /**
     * 手机号码
     */
    @TableField(value = "phone_number")
    @ApiModelProperty(value ="手机号码" )
    private String phoneNumber;


    /**
     * 政治面貌(1:中共党员 2中共预备党员 3:共青团员 4:群众 5 民主党派)
     */
    @TableField(value = "politics_status")
    @ApiModelProperty(value ="政治面貌(1:中共党员 2中共预备党员 3:共青团员 4:群众 5 民主党派)" )
    private Integer politicsStatus;
    /**
     * 求职状态(1:实习 2:应届生 3:在职暂无跳槽 4:在职急求机会 5:在职看机会 6:离职看机会)
     */
    @TableField(value = "job_status")
    @ApiModelProperty(value ="求职状态(1:离校-随时到岗(离职-随时到岗) 2:在校-月内到岗(在职-月内到岗) 3:在校-考虑机会(在职-考虑机会) 4:在校-暂不考虑(在职-暂不考虑))" )
    private String jobStatus;

    /**
     * 用户身份(职场人,:应届生,在校生)
     */
    @TableField(value = "user_type")
    @ApiModelProperty(value ="用户身份(1职场精英,2校园人才)" )
    private String userType;

    /**
     * 学历
     */
    @TableField(value = "education")
    @ApiModelProperty(value ="学历" )
    private String education;

    /**
     * 是否隐藏或显示目前年薪(0:显示 1:隐藏)
     */
    @TableField(value = "show_current_salary")
    @ApiModelProperty(value ="是否隐藏或显示目前年薪(0:显示 1:隐藏)" )
    private Integer showCurrentSalary;

    /**
     * 目前年薪
     */
    @TableField(value = "current_salary")
    @ApiModelProperty(value ="目前年薪" )
    private BigDecimal currentSalary;

    /**
     * 期望年薪
     */
    @TableField(value = "expect_salary")
    @ApiModelProperty(value ="期望年薪" )
    private BigDecimal expectSalary;

    /**
     * 年龄
     */
    @TableField(value = "age")
    @ApiModelProperty(value ="年龄" )
    private Integer age;
    /**
     * 联系电话
     */
    @TableField(value = "phone")
    @ApiModelProperty(value ="联系电话" )
    private String phone;
    /**
     * 生日
     */
    @TableField(value = "birthday")
    @ApiModelProperty(value ="生日" )
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    /**
     * 参加工作时间
     */
    @TableField(value = "work_time",updateStrategy= FieldStrategy.IGNORED)
    @ApiModelProperty(value ="参加工作时间" )
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate workTime;

    /**
     * 微信号
     */
    @TableField(value = "wechat_number")
    @ApiModelProperty(value ="微信号" )
    private String wechatNumber;



    /**
     * 邮箱
     */
    @TableField(value = "email")
    @ApiModelProperty(value ="邮箱" )
    private String email;

    /**
     * 个人优势
     */
    @TableField(value = "advantage")
    @ApiModelProperty(value ="个人优势" )
    private String advantage;

    /**
     * 所属行业
     */
    @TableField(value = "industry_code")
    @ApiModelProperty(value ="所属行业" )
    private String industryCode;

    /**
     * 帐号状态（0正常 1停用）
     */
    @TableField(value = "status")
    @ApiModelProperty(value ="帐号状态（0正常 1停用）" )
    private String status;


    /**
     * 技能标签
     */
    @TableField(value = "skill",typeHandler = JacksonTypeHandler.class)
    @ApiModelProperty(value ="技能标签" )
    private List<String> skill;

    /**
     * 简历附件路径
     */
    @TableField(value = "resume_path")
    @ApiModelProperty(value ="简历附件路径" )
    private String resumePath;

    /**
     * 审核状态(0待审核1审核通过2审核不通过)
     */
    @TableField(value = "examine_status")
    @ApiModelProperty(value ="审核状态(0待审核1审核通过2审核不通过)" )
    private Integer examineStatus;

    /**
     * 简历刷新时间
     */
    @TableField(value = "refresh_time")
    @ApiModelProperty(value ="简历刷新时间" )
    private LocalDateTime refreshTime;

    /**
     * 聊天ID
     */
    @TableField(value = "chat_id")
    @ApiModelProperty(value ="聊天ID" )
    private String chatId;

    /**
     * 简历审核状态(0待审核1审核通过2审核不通过)
     */
    @TableField(value = "resume_examine_status")
    @ApiModelProperty(value ="简历审核状态(0待审核1审核通过2审核不通过)" )
    private Integer resumeExamineStatus;
    @TableField(value = "perfect")
    @ApiModelProperty(value ="是否完善(0:未完善1:已完善)" )
    private Integer perfect;
    /**
     * 审核意见
     */
    @TableField(value = "resume_examine_result")
    @ApiModelProperty(value ="审核意见" )
    private String resumeExamineResult;

    /**
     * 分享注册人
     */
    @TableField(value = "share_person")
    @ApiModelProperty(value ="分享注册人" )
    private String sharePerson;
    @TableField(value = "origin")
    @ApiModelProperty(value = "引入来源(0:用户注册推广 1:职位推广)")
    private Integer origin;
    /**
     * 登录时间
     */
    @TableField(value = "login_time")
    @ApiModelProperty(value ="登录时间" )
    private LocalDateTime loginTime;

    /**
     *  创建人
     */
    @TableField(value = "create_id")
    @ApiModelProperty(value ="创建人" )
    private String createId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @ApiModelProperty(value ="创建时间" )
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    @TableField(value = "update_id")
    @ApiModelProperty(value ="更新人" )
    private String updateId;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    @ApiModelProperty(value ="更新时间" )
    private LocalDateTime updateTime;

    /**
     * 微信openid
     */
    @TableField(value = "openid")
    @ApiModelProperty(value ="微信openid" )
    private String openid;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        TJobhunter other = (TJobhunter) that;
        return (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getLoginName() == null ? other.getLoginName() == null : this.getLoginName().equals(other.getLoginName()))
            && (this.getPassword() == null ? other.getPassword() == null : this.getPassword().equals(other.getPassword()))
            && (this.getUserName() == null ? other.getUserName() == null : this.getUserName().equals(other.getUserName()))
            && (this.getAvatar() == null ? other.getAvatar() == null : this.getAvatar().equals(other.getAvatar()))
            && (this.getSex() == null ? other.getSex() == null : this.getSex().equals(other.getSex()))
            && (this.getPhoneNumber() == null ? other.getPhoneNumber() == null : this.getPhoneNumber().equals(other.getPhoneNumber()))
            && (this.getJobStatus() == null ? other.getJobStatus() == null : this.getJobStatus().equals(other.getJobStatus()))
            && (this.getUserType() == null ? other.getUserType() == null : this.getUserType().equals(other.getUserType()))
            && (this.getEducation() == null ? other.getEducation() == null : this.getEducation().equals(other.getEducation()))
            && (this.getCurrentSalary() == null ? other.getCurrentSalary() == null : this.getCurrentSalary().equals(other.getCurrentSalary()))
            && (this.getExpectSalary() == null ? other.getExpectSalary() == null : this.getExpectSalary().equals(other.getExpectSalary()))
            && (this.getAge() == null ? other.getAge() == null : this.getAge().equals(other.getAge()))
            && (this.getBirthday() == null ? other.getBirthday() == null : this.getBirthday().equals(other.getBirthday()))
            && (this.getWorkTime() == null ? other.getWorkTime() == null : this.getWorkTime().equals(other.getWorkTime()))
            && (this.getWechatNumber() == null ? other.getWechatNumber() == null : this.getWechatNumber().equals(other.getWechatNumber()))
            && (this.getEmail() == null ? other.getEmail() == null : this.getEmail().equals(other.getEmail()))
            && (this.getAdvantage() == null ? other.getAdvantage() == null : this.getAdvantage().equals(other.getAdvantage()))
            && (this.getIndustryCode() == null ? other.getIndustryCode() == null : this.getIndustryCode().equals(other.getIndustryCode()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getResumePath() == null ? other.getResumePath() == null : this.getResumePath().equals(other.getResumePath()))
            && (this.getExamineStatus() == null ? other.getExamineStatus() == null : this.getExamineStatus().equals(other.getExamineStatus()))
            && (this.getRefreshTime() == null ? other.getRefreshTime() == null : this.getRefreshTime().equals(other.getRefreshTime()))
            && (this.getChatId() == null ? other.getChatId() == null : this.getChatId().equals(other.getChatId()))
            && (this.getResumeExamineStatus() == null ? other.getResumeExamineStatus() == null : this.getResumeExamineStatus().equals(other.getResumeExamineStatus()))
            && (this.getResumeExamineResult() == null ? other.getResumeExamineResult() == null : this.getResumeExamineResult().equals(other.getResumeExamineResult()))
            && (this.getSharePerson() == null ? other.getSharePerson() == null : this.getSharePerson().equals(other.getSharePerson()))
            && (this.getLoginTime() == null ? other.getLoginTime() == null : this.getLoginTime().equals(other.getLoginTime()))
            && (this.getCreateId() == null ? other.getCreateId() == null : this.getCreateId().equals(other.getCreateId()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateId() == null ? other.getUpdateId() == null : this.getUpdateId().equals(other.getUpdateId()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getOpenid() == null ? other.getOpenid() == null : this.getOpenid().equals(other.getOpenid()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getLoginName() == null) ? 0 : getLoginName().hashCode());
        result = prime * result + ((getPassword() == null) ? 0 : getPassword().hashCode());
        result = prime * result + ((getUserName() == null) ? 0 : getUserName().hashCode());
        result = prime * result + ((getAvatar() == null) ? 0 : getAvatar().hashCode());
        result = prime * result + ((getSex() == null) ? 0 : getSex().hashCode());
        result = prime * result + ((getPhoneNumber() == null) ? 0 : getPhoneNumber().hashCode());
        result = prime * result + ((getJobStatus() == null) ? 0 : getJobStatus().hashCode());
        result = prime * result + ((getUserType() == null) ? 0 : getUserType().hashCode());
        result = prime * result + ((getEducation() == null) ? 0 : getEducation().hashCode());
        result = prime * result + ((getCurrentSalary() == null) ? 0 : getCurrentSalary().hashCode());
        result = prime * result + ((getExpectSalary() == null) ? 0 : getExpectSalary().hashCode());
        result = prime * result + ((getAge() == null) ? 0 : getAge().hashCode());
        result = prime * result + ((getBirthday() == null) ? 0 : getBirthday().hashCode());
        result = prime * result + ((getWorkTime() == null) ? 0 : getWorkTime().hashCode());
        result = prime * result + ((getWechatNumber() == null) ? 0 : getWechatNumber().hashCode());
        result = prime * result + ((getEmail() == null) ? 0 : getEmail().hashCode());
        result = prime * result + ((getAdvantage() == null) ? 0 : getAdvantage().hashCode());
        result = prime * result + ((getIndustryCode() == null) ? 0 : getIndustryCode().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getResumePath() == null) ? 0 : getResumePath().hashCode());
        result = prime * result + ((getExamineStatus() == null) ? 0 : getExamineStatus().hashCode());
        result = prime * result + ((getRefreshTime() == null) ? 0 : getRefreshTime().hashCode());
        result = prime * result + ((getChatId() == null) ? 0 : getChatId().hashCode());
        result = prime * result + ((getResumeExamineStatus() == null) ? 0 : getResumeExamineStatus().hashCode());
        result = prime * result + ((getResumeExamineResult() == null) ? 0 : getResumeExamineResult().hashCode());
        result = prime * result + ((getSharePerson() == null) ? 0 : getSharePerson().hashCode());
        result = prime * result + ((getLoginTime() == null) ? 0 : getLoginTime().hashCode());
        result = prime * result + ((getCreateId() == null) ? 0 : getCreateId().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateId() == null) ? 0 : getUpdateId().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getOpenid() == null) ? 0 : getOpenid().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", userId=").append(userId);
        sb.append(", loginName=").append(loginName);
        sb.append(", password=").append(password);
        sb.append(", userName=").append(userName);
        sb.append(", avatar=").append(avatar);
        sb.append(", sex=").append(sex);
        sb.append(", phoneNumber=").append(phoneNumber);
        sb.append(", jobStatus=").append(jobStatus);
        sb.append(", userType=").append(userType);
        sb.append(", education=").append(education);
        sb.append(", currentSalary=").append(currentSalary);
        sb.append(", expectSalary=").append(expectSalary);
        sb.append(", age=").append(age);
        sb.append(", birthday=").append(birthday);
        sb.append(", workTime=").append(workTime);
        sb.append(", wechatNumber=").append(wechatNumber);
        sb.append(", email=").append(email);
        sb.append(", advantage=").append(advantage);
        sb.append(", industryCode=").append(industryCode);
        sb.append(", status=").append(status);
        sb.append(", resumePath=").append(resumePath);
        sb.append(", examineStatus=").append(examineStatus);
        sb.append(", refreshTime=").append(refreshTime);
        sb.append(", chatId=").append(chatId);
        sb.append(", resumeExamineStatus=").append(resumeExamineStatus);
        sb.append(", resumeExamineResult=").append(resumeExamineResult);
        sb.append(", sharePerson=").append(sharePerson);
        sb.append(", loginTime=").append(loginTime);
        sb.append(", createId=").append(createId);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateId=").append(updateId);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", openid=").append(openid);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}