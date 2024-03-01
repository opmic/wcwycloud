package com.wcwy.company.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 企业浏览表
 * @TableName company_glance_over
 */
@TableName(value ="company_glance_over")
@Data
@ApiModel("企业浏览表")
public class CompanyGlanceOver implements Serializable {
    /**
     * 浏览表id
     */
    @TableId(value = "glance_over_id",type = IdType.ASSIGN_ID)
    @ApiModelProperty("浏览表id")
    private String glanceOverId;

    /**
     * 浏览过的求职者
     */
    @TableField(value = "glance_over_user_id")
    @ApiModelProperty("浏览过的求职者")
    private String glanceOverUserId;

    /**
     * 浏览时间
     */
    @TableField(value = "glance_over_time")
    @ApiModelProperty("浏览时间")
    private LocalDateTime glanceOverTime;

    /**
     * 企业id
     */
    @TableField(value = "company_id")
    @ApiModelProperty("企业id")
    private String companyId;

    /**
     * 删除时间
     */
    @TableField(value = "deleted_time")
    @ApiModelProperty("删除时间")
    private LocalDateTime deletedTime;

    /**
     * 逻辑删除
     */
    @TableField(value = "deleted")
    @ApiModelProperty("逻辑删除")
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
        CompanyGlanceOver other = (CompanyGlanceOver) that;
        return (this.getGlanceOverId() == null ? other.getGlanceOverId() == null : this.getGlanceOverId().equals(other.getGlanceOverId()))
            && (this.getGlanceOverUserId() == null ? other.getGlanceOverUserId() == null : this.getGlanceOverUserId().equals(other.getGlanceOverUserId()))
            && (this.getGlanceOverTime() == null ? other.getGlanceOverTime() == null : this.getGlanceOverTime().equals(other.getGlanceOverTime()))
            && (this.getCompanyId() == null ? other.getCompanyId() == null : this.getCompanyId().equals(other.getCompanyId()))
            && (this.getDeletedTime() == null ? other.getDeletedTime() == null : this.getDeletedTime().equals(other.getDeletedTime()))
            && (this.getDeleted() == null ? other.getDeleted() == null : this.getDeleted().equals(other.getDeleted()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getGlanceOverId() == null) ? 0 : getGlanceOverId().hashCode());
        result = prime * result + ((getGlanceOverUserId() == null) ? 0 : getGlanceOverUserId().hashCode());
        result = prime * result + ((getGlanceOverTime() == null) ? 0 : getGlanceOverTime().hashCode());
        result = prime * result + ((getCompanyId() == null) ? 0 : getCompanyId().hashCode());
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
        sb.append(", glanceOverUserId=").append(glanceOverUserId);
        sb.append(", glanceOverTime=").append(glanceOverTime);
        sb.append(", companyId=").append(companyId);
        sb.append(", deletedTime=").append(deletedTime);
        sb.append(", deleted=").append(deleted);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}