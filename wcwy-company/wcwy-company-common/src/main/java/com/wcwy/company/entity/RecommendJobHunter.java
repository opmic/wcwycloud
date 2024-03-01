package com.wcwy.company.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 推荐官求职者权限表
 * @TableName recommend_job_hunter
 */
@TableName(value ="recommend_job_hunter")
@Data
public class RecommendJobHunter implements Serializable {
    /**
     * 权限id
     */
    @TableId(value = "role_id")
    private String roleId;

    /**
     * 求职者id
     */
    @TableField(value = "job_hunter_id")
    private String jobHunterId;

    /**
     * 推荐官id
     */
    @TableField(value = "recommend_id")
    private String recommendId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    /**
     * 逻辑删除
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
        RecommendJobHunter other = (RecommendJobHunter) that;
        return (this.getRoleId() == null ? other.getRoleId() == null : this.getRoleId().equals(other.getRoleId()))
            && (this.getJobHunterId() == null ? other.getJobHunterId() == null : this.getJobHunterId().equals(other.getJobHunterId()))
            && (this.getRecommendId() == null ? other.getRecommendId() == null : this.getRecommendId().equals(other.getRecommendId()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getDeleted() == null ? other.getDeleted() == null : this.getDeleted().equals(other.getDeleted()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getRoleId() == null) ? 0 : getRoleId().hashCode());
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
        sb.append(", roleId=").append(roleId);
        sb.append(", jobHunterId=").append(jobHunterId);
        sb.append(", recommendId=").append(recommendId);
        sb.append(", createTime=").append(createTime);
        sb.append(", deleted=").append(deleted);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}