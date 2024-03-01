package com.wcwy.company.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wcwy.company.po.ProvincesCitiesPO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户简历信息
 * @TableName t_job_hunter_userinfo
 */
@TableName(value ="t_job_hunter_userinfo")
@Data
@ApiModel(value = "用户简历信息")
public class TJobHunterUserinfo implements Serializable {
    /**
     * 用户信息id
     */
    @TableId(value = "userinfo")
    @ApiModelProperty(value = "用户信息id")
    private String userinfo;

    /**
     * 简历id
     */
    @TableField(value = "resume_id")
    @ApiModelProperty(value = "简历id")
    private String resumeId;

    /**
     * 姓名
     */
    @TableField(value = "name")
    @ApiModelProperty(value = "姓名")
    private String name;

    /**
     * 身份(1职场人 2应届生 3 在校生)
     */
    @TableField(value = "identity")
    @ApiModelProperty(value = "身份(1职场人 2应届生 3 在校生)")
    private String identity;

    /**
     * 性别(1男 2女)
     */
    @TableField(value = "sex")
    @ApiModelProperty(value = "性别(1男 2女)")
    private Integer sex;

    /**
     * 年薪
     */
    @TableField(value = "annual_salary")
    @ApiModelProperty(value = "年薪")
    private BigDecimal annualSalary;

    /**
     * 城市
     */
    @TableField(value = "city" ,typeHandler = JacksonTypeHandler.class)
    @ApiModelProperty(value = "城市")
    private ProvincesCitiesPO city;

    /**
     * 求职状态(1:实习 2:应届生 3:在职暂无跳槽 4:在职急求机会 5:在职看机会 6:离职看机会)
     */
    @TableField(value = "job_wanted_state")
    @ApiModelProperty(value = "求职状态(1:实习 2:应届生 3:在职暂无跳槽 4:在职急求机会 5:在职看机会 6:离职看机会)")
    private String jobWantedState;

    /**
     * 生日
     */
    @TableField(value = "birthday")
    @ApiModelProperty(value = "生日")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    /**
     * 政治面貌(1:中共党员 2中共预备党员 3:共青团员 4:群众 5 民主党派)
     */
    @TableField(value = "politics_status")
    @ApiModelProperty(value = " 政治面貌(1:中共党员 2中共预备党员 3:共青团员 4:群众 5 民主党派)")
    private String politicsStatus;

    /**
     * 参加工作时间
     */
    @TableField(value = "work_date")
    @ApiModelProperty(value = "参加工作时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate workDate;

    /**
     * 电话
     */
    @TableField(value = "phone")
    @ApiModelProperty(value = "电话")
    private String phone;

    /**
     * 邮箱
     */
    @TableField(value = "mailbox")
    @ApiModelProperty(value = "邮箱")
    private String mailbox;

    /**
     * 微信号
     */
    @TableField(value = "wechat_number")
    @ApiModelProperty(value = "微信号")
    private String wechatNumber;

    /**
     * 删除
     */
    @TableField(value = "deleted")
    private Integer deleted;

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
        TJobHunterUserinfo other = (TJobHunterUserinfo) that;
        return (this.getUserinfo() == null ? other.getUserinfo() == null : this.getUserinfo().equals(other.getUserinfo()))
            && (this.getResumeId() == null ? other.getResumeId() == null : this.getResumeId().equals(other.getResumeId()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getIdentity() == null ? other.getIdentity() == null : this.getIdentity().equals(other.getIdentity()))
            && (this.getSex() == null ? other.getSex() == null : this.getSex().equals(other.getSex()))
            && (this.getAnnualSalary() == null ? other.getAnnualSalary() == null : this.getAnnualSalary().equals(other.getAnnualSalary()))
            && (this.getCity() == null ? other.getCity() == null : this.getCity().equals(other.getCity()))
            && (this.getJobWantedState() == null ? other.getJobWantedState() == null : this.getJobWantedState().equals(other.getJobWantedState()))
            && (this.getBirthday() == null ? other.getBirthday() == null : this.getBirthday().equals(other.getBirthday()))
            && (this.getPoliticsStatus() == null ? other.getPoliticsStatus() == null : this.getPoliticsStatus().equals(other.getPoliticsStatus()))
            && (this.getWorkDate() == null ? other.getWorkDate() == null : this.getWorkDate().equals(other.getWorkDate()))
            && (this.getPhone() == null ? other.getPhone() == null : this.getPhone().equals(other.getPhone()))
            && (this.getMailbox() == null ? other.getMailbox() == null : this.getMailbox().equals(other.getMailbox()))
            && (this.getWechatNumber() == null ? other.getWechatNumber() == null : this.getWechatNumber().equals(other.getWechatNumber()))
            && (this.getDeleted() == null ? other.getDeleted() == null : this.getDeleted().equals(other.getDeleted()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getUserinfo() == null) ? 0 : getUserinfo().hashCode());
        result = prime * result + ((getResumeId() == null) ? 0 : getResumeId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getIdentity() == null) ? 0 : getIdentity().hashCode());
        result = prime * result + ((getSex() == null) ? 0 : getSex().hashCode());
        result = prime * result + ((getAnnualSalary() == null) ? 0 : getAnnualSalary().hashCode());
        result = prime * result + ((getCity() == null) ? 0 : getCity().hashCode());
        result = prime * result + ((getJobWantedState() == null) ? 0 : getJobWantedState().hashCode());
        result = prime * result + ((getBirthday() == null) ? 0 : getBirthday().hashCode());
        result = prime * result + ((getPoliticsStatus() == null) ? 0 : getPoliticsStatus().hashCode());
        result = prime * result + ((getWorkDate() == null) ? 0 : getWorkDate().hashCode());
        result = prime * result + ((getPhone() == null) ? 0 : getPhone().hashCode());
        result = prime * result + ((getMailbox() == null) ? 0 : getMailbox().hashCode());
        result = prime * result + ((getWechatNumber() == null) ? 0 : getWechatNumber().hashCode());
        result = prime * result + ((getDeleted() == null) ? 0 : getDeleted().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", userinfo=").append(userinfo);
        sb.append(", resumeId=").append(resumeId);
        sb.append(", name=").append(name);
        sb.append(", identity=").append(identity);
        sb.append(", sex=").append(sex);
        sb.append(", annualSalary=").append(annualSalary);
        sb.append(", city=").append(city);
        sb.append(", jobWantedState=").append(jobWantedState);
        sb.append(", birthday=").append(birthday);
        sb.append(", politicsStatus=").append(politicsStatus);
        sb.append(", workDate=").append(workDate);
        sb.append(", phone=").append(phone);
        sb.append(", mailbox=").append(mailbox);
        sb.append(", wechatNumber=").append(wechatNumber);
        sb.append(", deleted=").append(deleted);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}