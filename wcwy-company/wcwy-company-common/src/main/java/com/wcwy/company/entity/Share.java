package com.wcwy.company.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 分享次数记录接口
 * @TableName share
 */
@TableName(value ="share")
@Data
public class Share implements Serializable {
    /**
     * 推荐官id
     */
    @TableId(value = "user_id")
    private String userId;

    /**
     * 分享邀请企业
     */
    @TableField(value = "company_amout")
    private Long companyAmout;

    /**
     * 分享邀请求职者数量
     */
    @TableField(value = "job_hunter_amount")
    private Long jobHunterAmount;

    /**
     * 分享邀请推荐官
     */
    @TableField(value = "recommend_amount")
    private Long recommendAmount;

    /**
     * 猎企
     */
    @TableField(value = "headhunter_amount")
    private Long headhunterAmount;

    /**
     * 校园推荐官
     */
    @TableField(value = "campus_recommend_amount")
    private Long campusRecommendAmount;

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
        Share other = (Share) that;
        return (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
                && (this.getCompanyAmout() == null ? other.getCompanyAmout() == null : this.getCompanyAmout().equals(other.getCompanyAmout()))
                && (this.getJobHunterAmount() == null ? other.getJobHunterAmount() == null : this.getJobHunterAmount().equals(other.getJobHunterAmount()))
                && (this.getRecommendAmount() == null ? other.getRecommendAmount() == null : this.getRecommendAmount().equals(other.getRecommendAmount()))
                && (this.getHeadhunterAmount() == null ? other.getHeadhunterAmount() == null : this.getHeadhunterAmount().equals(other.getHeadhunterAmount()))
                && (this.getCampusRecommendAmount() == null ? other.getCampusRecommendAmount() == null : this.getCampusRecommendAmount().equals(other.getCampusRecommendAmount()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getCompanyAmout() == null) ? 0 : getCompanyAmout().hashCode());
        result = prime * result + ((getJobHunterAmount() == null) ? 0 : getJobHunterAmount().hashCode());
        result = prime * result + ((getRecommendAmount() == null) ? 0 : getRecommendAmount().hashCode());
        result = prime * result + ((getHeadhunterAmount() == null) ? 0 : getHeadhunterAmount().hashCode());
        result = prime * result + ((getCampusRecommendAmount() == null) ? 0 : getCampusRecommendAmount().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", userId=").append(userId);
        sb.append(", companyAmout=").append(companyAmout);
        sb.append(", jobHunterAmount=").append(jobHunterAmount);
        sb.append(", recommendAmount=").append(recommendAmount);
        sb.append(", headhunterAmount=").append(headhunterAmount);
        sb.append(", campusRecommendAmount=").append(campusRecommendAmount);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}