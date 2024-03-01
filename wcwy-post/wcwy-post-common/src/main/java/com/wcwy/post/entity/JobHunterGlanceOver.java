package com.wcwy.post.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 求职者浏览岗位表
 * @TableName job_hunter_glance_over
 */
@TableName(value ="job_hunter_glance_over")
@Data
@ApiModel(value = "求职者浏览岗位表")
public class JobHunterGlanceOver implements Serializable {
    /**
     * 浏览表id
     */
    @TableId(value = "glance_over_id")
    @ApiModelProperty(value = "浏览表id")
    private String glanceOverId;

    /**
     * 浏览过岗位
     */
    @TableField(value = "glance_over_post")
    @ApiModelProperty(value = "浏览过岗位")
    private String glanceOverPost;

    /**
     * 浏览时间
     */
    @TableField(value = "glance_over_time")
    @ApiModelProperty(value = "浏览时间")
    private LocalDateTime glanceOverTime;

    /**
     * 求职者id
     */
    @TableField(value = "jobhunter_id")
    @ApiModelProperty(value = "求职者id")
    private String jobhunterId;

    /**
     * 删除时间
     */
    @TableField(value = "deleted_time")
    @ApiModelProperty(value = "删除时间")
    private LocalDateTime deletedTime;

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
        JobHunterGlanceOver other = (JobHunterGlanceOver) that;
        return (this.getGlanceOverId() == null ? other.getGlanceOverId() == null : this.getGlanceOverId().equals(other.getGlanceOverId()))
            && (this.getGlanceOverPost() == null ? other.getGlanceOverPost() == null : this.getGlanceOverPost().equals(other.getGlanceOverPost()))
            && (this.getGlanceOverTime() == null ? other.getGlanceOverTime() == null : this.getGlanceOverTime().equals(other.getGlanceOverTime()))
            && (this.getJobhunterId() == null ? other.getJobhunterId() == null : this.getJobhunterId().equals(other.getJobhunterId()))
            && (this.getDeletedTime() == null ? other.getDeletedTime() == null : this.getDeletedTime().equals(other.getDeletedTime()))
            && (this.getDeleted() == null ? other.getDeleted() == null : this.getDeleted().equals(other.getDeleted()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getGlanceOverId() == null) ? 0 : getGlanceOverId().hashCode());
        result = prime * result + ((getGlanceOverPost() == null) ? 0 : getGlanceOverPost().hashCode());
        result = prime * result + ((getGlanceOverTime() == null) ? 0 : getGlanceOverTime().hashCode());
        result = prime * result + ((getJobhunterId() == null) ? 0 : getJobhunterId().hashCode());
        result = prime * result + ((getDeletedTime() == null) ? 0 : getDeletedTime().hashCode());
        result = prime * result + ((getDeleted() == null) ? 0 : getDeleted().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", glanceOverId=").append(glanceOverId);
        sb.append(", glanceOverPost=").append(glanceOverPost);
        sb.append(", glanceOverTime=").append(glanceOverTime);
        sb.append(", jobhunterId=").append(jobhunterId);
        sb.append(", deletedTime=").append(deletedTime);
        sb.append(", deleted=").append(deleted);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}