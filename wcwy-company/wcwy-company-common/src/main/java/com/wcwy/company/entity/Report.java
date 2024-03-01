package com.wcwy.company.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 举报
 * @TableName report
 */
@TableName(value ="report")
@Data
@ApiModel(value = "举报")
public class Report implements Serializable {
    /**
     * id
     */
    @TableId(value = "report_id", type = IdType.AUTO)
    @ApiModelProperty("id")
    private Long reportId;

    /**
     * 举报原因
     */
    @TableField(value = "cause")
    @ApiModelProperty("举报原因")
    private String cause;

    /**
     * 证据上传
     */
    @TableField(value = "evidence", typeHandler = JacksonTypeHandler.class)
    @ApiModelProperty("证据上传")
    private List<String> evidence;


    /**
     * 补充说明
     */
    @TableField(value = "replenish")
    @ApiModelProperty("补充说明")
    private String replenish;


    @TableField(value = "feedback")
    @ApiModelProperty("反馈信息")
    private String feedback;
    /**
     * 联系方式
     */
    @TableField(value = "phone")
    @ApiModelProperty("联系方式")
    private String phone;

    /**
     * 企业名称
     */
    @TableField(value = "company_name")
    @ApiModelProperty("企业名称")
    private String companyName;

    /**
     * 职位名称
     */
    @TableField(value = "post_name")
    @ApiModelProperty("职位名称")
    private String postName;

    /**
     * 职位id
     */
    @TableField(value = "post_id")
    @ApiModelProperty("职位id")
    private String postId;

    /**
     * 举报时间
     */
    @TableField(value = "create_time")
    @ApiModelProperty("举报时间")
    private LocalDateTime createTime;

    /**
     * 处理状态(0:待处理1:处理中2:处理结束)
     */
    @TableField(value = "state")
    @ApiModelProperty("处理状态(0:待处理1:处理中2:处理结束)")
    private Integer state;

    /**
     * 求职者id
     */
    @TableField(value = "job_hunter_id")
    @ApiModelProperty("求职者id")
    private String jobHunterId;

    /**
     * 求职者名称
     */
    @TableField(value = "job_hunter_name")
    @ApiModelProperty("求职者名称")
    private String jobHunterName;

    /**
     * 类型(0:岗位1:求职者)
     */
    @TableField(value = "type")
    @ApiModelProperty("类型(0:岗位1:简历)")
    private Integer type;

    /**
     * 举报身份(0:企业1:推荐官2:求职者)
     */
    @TableField(value = "identity")
    @ApiModelProperty("举报身份(0:企业1:推荐官2:求职者)")
    private Integer identity;

    /**
     * 处理时间
     */
    @TableField(value = "update_time")
    @ApiModelProperty("处理时间")
    private LocalDateTime updateTime;

    /**
     * 举报人
     */
    @TableField(value = "report_name")
    @ApiModelProperty("举报人")
    private String reportName;

    /**
     * 举报人id
     */
    @TableField(value = "report_user_id")
    @ApiModelProperty("举报人id")
    private String reportUserId;

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
        Report other = (Report) that;
        return (this.getReportId() == null ? other.getReportId() == null : this.getReportId().equals(other.getReportId()))
            && (this.getCause() == null ? other.getCause() == null : this.getCause().equals(other.getCause()))
            && (this.getEvidence() == null ? other.getEvidence() == null : this.getEvidence().equals(other.getEvidence()))
            && (this.getPhone() == null ? other.getPhone() == null : this.getPhone().equals(other.getPhone()))
            && (this.getCompanyName() == null ? other.getCompanyName() == null : this.getCompanyName().equals(other.getCompanyName()))
            && (this.getPostName() == null ? other.getPostName() == null : this.getPostName().equals(other.getPostName()))
            && (this.getPostId() == null ? other.getPostId() == null : this.getPostId().equals(other.getPostId()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getState() == null ? other.getState() == null : this.getState().equals(other.getState()))
            && (this.getJobHunterId() == null ? other.getJobHunterId() == null : this.getJobHunterId().equals(other.getJobHunterId()))
            && (this.getJobHunterName() == null ? other.getJobHunterName() == null : this.getJobHunterName().equals(other.getJobHunterName()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getIdentity() == null ? other.getIdentity() == null : this.getIdentity().equals(other.getIdentity()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getReportName() == null ? other.getReportName() == null : this.getReportName().equals(other.getReportName()))
            && (this.getReportUserId() == null ? other.getReportUserId() == null : this.getReportUserId().equals(other.getReportUserId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getReportId() == null) ? 0 : getReportId().hashCode());
        result = prime * result + ((getCause() == null) ? 0 : getCause().hashCode());
        result = prime * result + ((getEvidence() == null) ? 0 : getEvidence().hashCode());
        result = prime * result + ((getPhone() == null) ? 0 : getPhone().hashCode());
        result = prime * result + ((getCompanyName() == null) ? 0 : getCompanyName().hashCode());
        result = prime * result + ((getPostName() == null) ? 0 : getPostName().hashCode());
        result = prime * result + ((getPostId() == null) ? 0 : getPostId().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getState() == null) ? 0 : getState().hashCode());
        result = prime * result + ((getJobHunterId() == null) ? 0 : getJobHunterId().hashCode());
        result = prime * result + ((getJobHunterName() == null) ? 0 : getJobHunterName().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getIdentity() == null) ? 0 : getIdentity().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getReportName() == null) ? 0 : getReportName().hashCode());
        result = prime * result + ((getReportUserId() == null) ? 0 : getReportUserId().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", reportId=").append(reportId);
        sb.append(", cause=").append(cause);
        sb.append(", evidence=").append(evidence);
        sb.append(", phone=").append(phone);
        sb.append(", companyName=").append(companyName);
        sb.append(", postName=").append(postName);
        sb.append(", postId=").append(postId);
        sb.append(", createTime=").append(createTime);
        sb.append(", state=").append(state);
        sb.append(", jobHunterId=").append(jobHunterId);
        sb.append(", jobHunterName=").append(jobHunterName);
        sb.append(", type=").append(type);
        sb.append(", identity=").append(identity);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", reportName=").append(reportName);
        sb.append(", reportUserId=").append(reportUserId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}