package com.wcwy.company.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 导入简历
 * @TableName import_resume
 */
@TableName(value ="import_resume")
@Data
@ApiModel(value = "导入简历")
public class ImportResume implements Serializable {
    /**
     * 简历id
     */
    @TableId(value = "resume_id")
    @ApiModelProperty(value = "简历id")
    private String resumeId;

    /**
     * 求职者头像
     */
    @TableField(value = "head_portrait")
    @ApiModelProperty(value = "求职者头像")
    private String headPortrait;

    /**
     * 求职者姓名
     */
    @TableField(value = "job_hunter_name")
    @ApiModelProperty(value = "求职者姓名")
    private String jobHunterName;

    /**
     * 性别(0:男 1女)
     */
    @TableField(value = "sex")
    @ApiModelProperty(value = " 性别(0:男 1女)")
    private Integer sex;

    /**
     * 用户身份(1职场人,2:应届生,3在校生)
     */
    @TableField(value = "job_hunter_type")
    @ApiModelProperty(value = "用户身份(1职场人,2:应届生,3在校生)")
    private Integer jobHunterType;

    /**
     * 优势亮点
     */
    @TableField(value = "advantage")
    @ApiModelProperty(value = "优势亮点")
    private Object advantage;

    /**
     * 目前年薪
     */
    @TableField(value = "current_salary")
    @ApiModelProperty(value = "目前年薪")
    private BigDecimal currentSalary;

    /**
     * 是否保密(0公开1:保密)
     */
    @TableField(value = "show_current_salary")
    @ApiModelProperty(value = "是否保密(0公开1:保密)")
    private Integer showCurrentSalary;

    /**
     * 当前城市
     */
    @TableField(value = "address")
    @ApiModelProperty(value = "当前城市")
    private Object address;

    /**
     * 出生日期
     */
    @TableField(value = "birthday")
    @ApiModelProperty(value = "出生日期")
    private LocalDate birthday;

    /**
     * 政治面貌(1:中共党员 2中共预备党员 3:共青团员 4:群众 5 民主党派)
     */
    @TableField(value = "politics_status")
    @ApiModelProperty(value = "政治面貌(1:中共党员 2中共预备党员 3:共青团员 4:群众 5 民主党派)")
    private Integer politicsStatus;

    /**
     * 参加工作时间
     */
    @TableField(value = "work_time")
    @ApiModelProperty(value = "参加工作时间")
    private LocalDate workTime;

    /**
     * 求职状态(1:实习 2:应届生 3:在职暂无跳槽 4:在职急求机会 5:在职看机会 6:离职看机会)
     */
    @TableField(value = "job_status")
    @ApiModelProperty(value = "求职状态(1:实习 2:应届生 3:在职暂无跳槽 4:在职急求机会 5:在职看机会 6:离职看机会)")
    private String jobStatus;

    /**
     * 联系电话
     */
    @TableField(value = "phone")
    @ApiModelProperty(value = "联系电话")
    private String phone;

    /**
     * 邮箱
     */
    @TableField(value = "email")
    @ApiModelProperty(value = "邮箱")
    private String email;

    /**
     * 推荐官id
     */
    @TableField(value = "recommend")
    @ApiModelProperty(value = "推荐官id")
    private String recommend;

    /**
     * 微信号
     */
    @TableField(value = "wechat_number")
    @ApiModelProperty(value = "微信号")
    private String wechatNumber;

    /**
     * 简历审核状态(0待审核1审核通过2审核不通过)
     */
    @TableField(value = "resume_examine_status")
    @ApiModelProperty(value = "简历审核状态(0待审核1审核通过2审核不通过)")
    private Integer resumeExamineStatus;

    /**
     * 审核意见
     */
    @TableField(value = "resume_examine_result")
    @ApiModelProperty(value = "审核意见")
    private String resumeExamineResult;

    /**
     * 是否默认(1:是2:否)
     */
    @TableField(value = "resume")
    @ApiModelProperty(value = "是否默认(1:是2:否)")
    private Integer resume;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;

    /**
     * 是否能删除
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
        ImportResume other = (ImportResume) that;
        return (this.getResumeId() == null ? other.getResumeId() == null : this.getResumeId().equals(other.getResumeId()))
            && (this.getHeadPortrait() == null ? other.getHeadPortrait() == null : this.getHeadPortrait().equals(other.getHeadPortrait()))
            && (this.getJobHunterName() == null ? other.getJobHunterName() == null : this.getJobHunterName().equals(other.getJobHunterName()))
            && (this.getSex() == null ? other.getSex() == null : this.getSex().equals(other.getSex()))
            && (this.getJobHunterType() == null ? other.getJobHunterType() == null : this.getJobHunterType().equals(other.getJobHunterType()))
            && (this.getAdvantage() == null ? other.getAdvantage() == null : this.getAdvantage().equals(other.getAdvantage()))
            && (this.getCurrentSalary() == null ? other.getCurrentSalary() == null : this.getCurrentSalary().equals(other.getCurrentSalary()))
            && (this.getShowCurrentSalary() == null ? other.getShowCurrentSalary() == null : this.getShowCurrentSalary().equals(other.getShowCurrentSalary()))
            && (this.getAddress() == null ? other.getAddress() == null : this.getAddress().equals(other.getAddress()))
            && (this.getBirthday() == null ? other.getBirthday() == null : this.getBirthday().equals(other.getBirthday()))
            && (this.getPoliticsStatus() == null ? other.getPoliticsStatus() == null : this.getPoliticsStatus().equals(other.getPoliticsStatus()))
            && (this.getWorkTime() == null ? other.getWorkTime() == null : this.getWorkTime().equals(other.getWorkTime()))
            && (this.getJobStatus() == null ? other.getJobStatus() == null : this.getJobStatus().equals(other.getJobStatus()))
            && (this.getPhone() == null ? other.getPhone() == null : this.getPhone().equals(other.getPhone()))
            && (this.getEmail() == null ? other.getEmail() == null : this.getEmail().equals(other.getEmail()))
            && (this.getRecommend() == null ? other.getRecommend() == null : this.getRecommend().equals(other.getRecommend()))
            && (this.getWechatNumber() == null ? other.getWechatNumber() == null : this.getWechatNumber().equals(other.getWechatNumber()))
            && (this.getResumeExamineStatus() == null ? other.getResumeExamineStatus() == null : this.getResumeExamineStatus().equals(other.getResumeExamineStatus()))
            && (this.getResumeExamineResult() == null ? other.getResumeExamineResult() == null : this.getResumeExamineResult().equals(other.getResumeExamineResult()))
            && (this.getResume() == null ? other.getResume() == null : this.getResume().equals(other.getResume()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getDeleted() == null ? other.getDeleted() == null : this.getDeleted().equals(other.getDeleted()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getResumeId() == null) ? 0 : getResumeId().hashCode());
        result = prime * result + ((getHeadPortrait() == null) ? 0 : getHeadPortrait().hashCode());
        result = prime * result + ((getJobHunterName() == null) ? 0 : getJobHunterName().hashCode());
        result = prime * result + ((getSex() == null) ? 0 : getSex().hashCode());
        result = prime * result + ((getJobHunterType() == null) ? 0 : getJobHunterType().hashCode());
        result = prime * result + ((getAdvantage() == null) ? 0 : getAdvantage().hashCode());
        result = prime * result + ((getCurrentSalary() == null) ? 0 : getCurrentSalary().hashCode());
        result = prime * result + ((getShowCurrentSalary() == null) ? 0 : getShowCurrentSalary().hashCode());
        result = prime * result + ((getAddress() == null) ? 0 : getAddress().hashCode());
        result = prime * result + ((getBirthday() == null) ? 0 : getBirthday().hashCode());
        result = prime * result + ((getPoliticsStatus() == null) ? 0 : getPoliticsStatus().hashCode());
        result = prime * result + ((getWorkTime() == null) ? 0 : getWorkTime().hashCode());
        result = prime * result + ((getJobStatus() == null) ? 0 : getJobStatus().hashCode());
        result = prime * result + ((getPhone() == null) ? 0 : getPhone().hashCode());
        result = prime * result + ((getEmail() == null) ? 0 : getEmail().hashCode());
        result = prime * result + ((getRecommend() == null) ? 0 : getRecommend().hashCode());
        result = prime * result + ((getWechatNumber() == null) ? 0 : getWechatNumber().hashCode());
        result = prime * result + ((getResumeExamineStatus() == null) ? 0 : getResumeExamineStatus().hashCode());
        result = prime * result + ((getResumeExamineResult() == null) ? 0 : getResumeExamineResult().hashCode());
        result = prime * result + ((getResume() == null) ? 0 : getResume().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getDeleted() == null) ? 0 : getDeleted().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", resumeId=").append(resumeId);
        sb.append(", headPortrait=").append(headPortrait);
        sb.append(", jobHunterName=").append(jobHunterName);
        sb.append(", sex=").append(sex);
        sb.append(", jobHunterType=").append(jobHunterType);
        sb.append(", advantage=").append(advantage);
        sb.append(", currentSalary=").append(currentSalary);
        sb.append(", showCurrentSalary=").append(showCurrentSalary);
        sb.append(", address=").append(address);
        sb.append(", birthday=").append(birthday);
        sb.append(", politicsStatus=").append(politicsStatus);
        sb.append(", workTime=").append(workTime);
        sb.append(", jobStatus=").append(jobStatus);
        sb.append(", phone=").append(phone);
        sb.append(", email=").append(email);
        sb.append(", recommend=").append(recommend);
        sb.append(", wechatNumber=").append(wechatNumber);
        sb.append(", resumeExamineStatus=").append(resumeExamineStatus);
        sb.append(", resumeExamineResult=").append(resumeExamineResult);
        sb.append(", resume=").append(resume);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", deleted=").append(deleted);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}