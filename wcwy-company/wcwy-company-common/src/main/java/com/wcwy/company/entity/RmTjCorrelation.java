package com.wcwy.company.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 推荐官和求职者关联表
 * @TableName rm_tj_correlation
 */
@TableName(value ="rm_tj_correlation")
@Data
@ApiModel(value = "推荐官和求职者关联表")
public class RmTjCorrelation implements Serializable {
    /**
     * 关联主键
     */
    @TableId(value = "correlation_id")
    @ApiModelProperty(value = "关联编号")
    private String correlationId;

    /**
     * 人才来源(0:分享引流 1:人才委托 2新增人才 3应聘简历)
     */
    @TableField(value = "correlation_type")
    @ApiModelProperty(value = "人才来源(0:分享引流 1:人才委托 2新增人才 3应聘简历)")
    private Integer correlationType;

    /**
     * 求职者id
     */
    @TableField(value = "job_hunter_id")
    @ApiModelProperty(value = "求职者id")
    private String jobHunterId;

    /**
     * 推荐官id
     */
    @TableField(value = "recommend_id")
    @ApiModelProperty(value = "推荐id")
    private String recommendId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    /**
     * 逻辑删除
     */
    @TableField(value = "deleted")
    @TableLogic
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
        RmTjCorrelation other = (RmTjCorrelation) that;
        return (this.getCorrelationId() == null ? other.getCorrelationId() == null : this.getCorrelationId().equals(other.getCorrelationId()))
            && (this.getCorrelationType() == null ? other.getCorrelationType() == null : this.getCorrelationType().equals(other.getCorrelationType()))
            && (this.getJobHunterId() == null ? other.getJobHunterId() == null : this.getJobHunterId().equals(other.getJobHunterId()))
            && (this.getRecommendId() == null ? other.getRecommendId() == null : this.getRecommendId().equals(other.getRecommendId()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getDeleted() == null ? other.getDeleted() == null : this.getDeleted().equals(other.getDeleted()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getCorrelationId() == null) ? 0 : getCorrelationId().hashCode());
        result = prime * result + ((getCorrelationType() == null) ? 0 : getCorrelationType().hashCode());
        result = prime * result + ((getJobHunterId() == null) ? 0 : getJobHunterId().hashCode());
        result = prime * result + ((getRecommendId() == null) ? 0 : getRecommendId().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getDeleted() == null) ? 0 : getDeleted().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", correlationId=").append(correlationId);
        sb.append(", correlationType=").append(correlationType);
        sb.append(", jobHunterId=").append(jobHunterId);
        sb.append(", recommendId=").append(recommendId);
        sb.append(", createTime=").append(createTime);
        sb.append(", deleted=").append(deleted);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}