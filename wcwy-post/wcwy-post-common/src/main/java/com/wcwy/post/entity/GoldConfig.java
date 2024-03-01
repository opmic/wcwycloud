package com.wcwy.post.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import java.io.Serializable;

/**
 * 金币奖励管理
 * @TableName gold_config
 */
@TableName(value ="gold_config")
@Data
@ApiModel("金币奖励管理")
public class GoldConfig implements Serializable {
    /**
     * id
     */
    @TableId(value = "gold_id", type = IdType.AUTO)
    @ApiModelProperty("id")
    private Long goldId;

    /**
     * 注册金币
     */
    @TableField(value = "register_gold")
    @ApiModelProperty("注册金币")
    private Integer registerGold;

    /**
     * 登录金币
     */
    @TableField(value = "login_gold")
    @ApiModelProperty("登录金币")
    private Integer loginGold;

    /**
     * 奖励次数
     */
    @TableField(value = "login_count")
    @ApiModelProperty("奖励次数")
    private Integer loginCount;

    /**
     * 发布岗位金币
     */
    @TableField(value = "post_gold")
    @ApiModelProperty("发布岗位金币")
    private Integer postGold;

    /**
     * 发布岗位的次数
     */
    @TableField(value = "post_count")
    @ApiModelProperty("发布岗位的次数")
    private Integer postCount;

    /**
     * 推荐报告浏览
     */
    @TableField(value = "recommendation_report_browse")
    @ApiModelProperty("推荐报告浏览")
    private Integer recommendationReportBrowse;

    /**
     * 推荐报告
     */
    @TableField(value = "recommendation_report")
    @ApiModelProperty("推荐报告")
    private Integer recommendationReport;

    /**
     * 面试邀请
     */
    @TableField(value = "interview_invitation")
    @ApiModelProperty("面试邀请")
    private Integer interviewInvitation;

    /**
     * 发送offer
     */
    @TableField(value = "offer")
    @ApiModelProperty("发送offer")
    private Integer offer;

    /**
     * 入职
     */
    @TableField(value = "entry")
    @ApiModelProperty("入职")
    private Integer entry;

    /**
     * 分享注册
     */
    @TableField(value = "share_registration")
    @ApiModelProperty("分享注册")
    private Integer shareRegistration;

    /**
     * 分享注册
     */
    @TableField(value = "share_registration_count")
    @ApiModelProperty("分享数量")
    private Integer shareRegistrationCount;

    /**
     * 创建活动
     */
    @TableField(value = "create_activity")
    @ApiModelProperty("创建活动")
    private Integer createActivity;

    /**
     * 上传录播视频
     */
    @TableField(value = "video_uploading")
    @ApiModelProperty("上传录播视频")
    private Integer videoUploading;

    /**
     * 视频订购
     */
    @TableField(value = "video_order")
    @ApiModelProperty("视频订购")
    private Integer videoOrder;


    /**
     * 上传录播视频
     */
    @TableField(value = "inviter_job_hunter")
    @ApiModelProperty("邀请求职者")
    private Integer inviterJobHunter;
    /**
     * 上传录播视频
     */
    @TableField(value = "inviter_recommend")
    @ApiModelProperty("邀请推荐官")
    private Integer inviterRecommend;
    /**
     * 上传录播视频
     */
    @TableField(value = "inviter_company")
    @ApiModelProperty("邀请企业")
    private Integer inviterCompany;

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
        GoldConfig other = (GoldConfig) that;
        return (this.getGoldId() == null ? other.getGoldId() == null : this.getGoldId().equals(other.getGoldId()))
            && (this.getRegisterGold() == null ? other.getRegisterGold() == null : this.getRegisterGold().equals(other.getRegisterGold()))
            && (this.getLoginGold() == null ? other.getLoginGold() == null : this.getLoginGold().equals(other.getLoginGold()))
            && (this.getLoginCount() == null ? other.getLoginCount() == null : this.getLoginCount().equals(other.getLoginCount()))
            && (this.getPostGold() == null ? other.getPostGold() == null : this.getPostGold().equals(other.getPostGold()))
            && (this.getPostCount() == null ? other.getPostCount() == null : this.getPostCount().equals(other.getPostCount()))
            && (this.getRecommendationReportBrowse() == null ? other.getRecommendationReportBrowse() == null : this.getRecommendationReportBrowse().equals(other.getRecommendationReportBrowse()))
            && (this.getRecommendationReport() == null ? other.getRecommendationReport() == null : this.getRecommendationReport().equals(other.getRecommendationReport()))
            && (this.getInterviewInvitation() == null ? other.getInterviewInvitation() == null : this.getInterviewInvitation().equals(other.getInterviewInvitation()))
            && (this.getOffer() == null ? other.getOffer() == null : this.getOffer().equals(other.getOffer()))
            && (this.getEntry() == null ? other.getEntry() == null : this.getEntry().equals(other.getEntry()))
            && (this.getShareRegistration() == null ? other.getShareRegistration() == null : this.getShareRegistration().equals(other.getShareRegistration()))
            && (this.getCreateActivity() == null ? other.getCreateActivity() == null : this.getCreateActivity().equals(other.getCreateActivity()))
            && (this.getVideoUploading() == null ? other.getVideoUploading() == null : this.getVideoUploading().equals(other.getVideoUploading()))
            && (this.getVideoOrder() == null ? other.getVideoOrder() == null : this.getVideoOrder().equals(other.getVideoOrder()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getGoldId() == null) ? 0 : getGoldId().hashCode());
        result = prime * result + ((getRegisterGold() == null) ? 0 : getRegisterGold().hashCode());
        result = prime * result + ((getLoginGold() == null) ? 0 : getLoginGold().hashCode());
        result = prime * result + ((getLoginCount() == null) ? 0 : getLoginCount().hashCode());
        result = prime * result + ((getPostGold() == null) ? 0 : getPostGold().hashCode());
        result = prime * result + ((getPostCount() == null) ? 0 : getPostCount().hashCode());
        result = prime * result + ((getRecommendationReportBrowse() == null) ? 0 : getRecommendationReportBrowse().hashCode());
        result = prime * result + ((getRecommendationReport() == null) ? 0 : getRecommendationReport().hashCode());
        result = prime * result + ((getInterviewInvitation() == null) ? 0 : getInterviewInvitation().hashCode());
        result = prime * result + ((getOffer() == null) ? 0 : getOffer().hashCode());
        result = prime * result + ((getEntry() == null) ? 0 : getEntry().hashCode());
        result = prime * result + ((getShareRegistration() == null) ? 0 : getShareRegistration().hashCode());
        result = prime * result + ((getCreateActivity() == null) ? 0 : getCreateActivity().hashCode());
        result = prime * result + ((getVideoUploading() == null) ? 0 : getVideoUploading().hashCode());
        result = prime * result + ((getVideoOrder() == null) ? 0 : getVideoOrder().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", goldId=").append(goldId);
        sb.append(", registerGold=").append(registerGold);
        sb.append(", loginGold=").append(loginGold);
        sb.append(", loginCount=").append(loginCount);
        sb.append(", postGold=").append(postGold);
        sb.append(", postCount=").append(postCount);
        sb.append(", recommendationReportBrowse=").append(recommendationReportBrowse);
        sb.append(", recommendationReport=").append(recommendationReport);
        sb.append(", interviewInvitation=").append(interviewInvitation);
        sb.append(", offer=").append(offer);
        sb.append(", entry=").append(entry);
        sb.append(", shareRegistration=").append(shareRegistration);
        sb.append(", createActivity=").append(createActivity);
        sb.append(", videoUploading=").append(videoUploading);
        sb.append(", videoOrder=").append(videoOrder);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}