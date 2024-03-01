package com.wcwy.post.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 发布岗位纪录表
 *
 * @TableName t_post_share
 */
@TableName(value = "t_post_share")
@Data
@ApiModel(value = "发布岗位纪录表")
public class TPostShare implements Serializable {
    /**
     * 分享id
     */
    @TableId(value = "share_id")
    @ApiModelProperty(value = "分享id")
    private String shareId;

    /**
     * 岗位id
     */
    @TableField(value = "company_post_id")
    @ApiModelProperty(value = "岗位id")
    private String companyPostId;

    @TableField(value = "flow")
    @ApiModelProperty(value = "热度")
    private Long flow;

    /**
     * 推荐次数
     */
    @TableField(value = "share_size")
    @ApiModelProperty(value = "推荐次数")
    private Long shareSize;

    /**
     * 下载次数
     */
    @TableField(value = "download_size")
    @ApiModelProperty(value = "下载次数")
    private Long downloadSize;

    /**
     * 浏览次数
     */
    @TableField(value = "browse_size")
    @ApiModelProperty(value = "浏览次数")
    private Long browseSize;

    /**
     * 面试人数
     */
    @TableField(value = "interview_size")
    @ApiModelProperty(value = "面试人数")
    private Long interviewSize;

    /**
     * 入职人数
     */
    @TableField(value = "entry_size")
    @ApiModelProperty(value = "入职人数")
    private Long entrySize;
    /**
     * 淘汰人数
     */
    @TableField(value = "weed_out")
    @ApiModelProperty(value = "淘汰人数")
    private Long weedOut;
    /**
     * 预约面试
     */
    @TableField(value = "subscribe")
    @ApiModelProperty(value = "预约面试")
    private Long subscribe;

    /**
     * offer数量
     */
    @TableField(value = "offer_size")
    @ApiModelProperty(value = "offer数量")
    private Long offerSize;

    /**
     * 过保数量
     */
    @TableField(value = "over_insured")
    @ApiModelProperty(value = "过保数量")
    private Long overInsured;
    /**
     * 过保数量
     */
    @TableField(value = "attention")
    @ApiModelProperty(value = "关注量")
    private Long attention;
    /**
     * 结算
     */
    @TableField(value = "close_an_account")
    @ApiModelProperty(value = "结算")
    private BigDecimal closeAnAccount;

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
        TPostShare other = (TPostShare) that;
        return (this.getShareId() == null ? other.getShareId() == null : this.getShareId().equals(other.getShareId()))
                && (this.getCompanyPostId() == null ? other.getCompanyPostId() == null : this.getCompanyPostId().equals(other.getCompanyPostId()))
                && (this.getShareSize() == null ? other.getShareSize() == null : this.getShareSize().equals(other.getShareSize()))
                && (this.getDownloadSize() == null ? other.getDownloadSize() == null : this.getDownloadSize().equals(other.getDownloadSize()))
                && (this.getBrowseSize() == null ? other.getBrowseSize() == null : this.getBrowseSize().equals(other.getBrowseSize()))
                && (this.getInterviewSize() == null ? other.getInterviewSize() == null : this.getInterviewSize().equals(other.getInterviewSize()))
                && (this.getEntrySize() == null ? other.getEntrySize() == null : this.getEntrySize().equals(other.getEntrySize()))
                && (this.getSubscribe() == null ? other.getSubscribe() == null : this.getSubscribe().equals(other.getSubscribe()))
                && (this.getOfferSize() == null ? other.getOfferSize() == null : this.getOfferSize().equals(other.getOfferSize()))
                && (this.getOverInsured() == null ? other.getOverInsured() == null : this.getOverInsured().equals(other.getOverInsured()))
                && (this.getCloseAnAccount() == null ? other.getCloseAnAccount() == null : this.getCloseAnAccount().equals(other.getCloseAnAccount()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getShareId() == null) ? 0 : getShareId().hashCode());
        result = prime * result + ((getCompanyPostId() == null) ? 0 : getCompanyPostId().hashCode());
        result = prime * result + ((getShareSize() == null) ? 0 : getShareSize().hashCode());
        result = prime * result + ((getDownloadSize() == null) ? 0 : getDownloadSize().hashCode());
        result = prime * result + ((getBrowseSize() == null) ? 0 : getBrowseSize().hashCode());
        result = prime * result + ((getInterviewSize() == null) ? 0 : getInterviewSize().hashCode());
        result = prime * result + ((getEntrySize() == null) ? 0 : getEntrySize().hashCode());
        result = prime * result + ((getSubscribe() == null) ? 0 : getSubscribe().hashCode());
        result = prime * result + ((getOfferSize() == null) ? 0 : getOfferSize().hashCode());
        result = prime * result + ((getOverInsured() == null) ? 0 : getOverInsured().hashCode());
        result = prime * result + ((getCloseAnAccount() == null) ? 0 : getCloseAnAccount().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", shareId=").append(shareId);
        sb.append(", companyPostId=").append(companyPostId);
        sb.append(", shareSize=").append(shareSize);
        sb.append(", downloadSize=").append(downloadSize);
        sb.append(", browseSize=").append(browseSize);
        sb.append(", interviewSize=").append(interviewSize);
        sb.append(", entrySize=").append(entrySize);
        sb.append(", subscribe=").append(subscribe);
        sb.append(", offerSize=").append(offerSize);
        sb.append(", overInsured=").append(overInsured);
        sb.append(", closeAnAccount=").append(closeAnAccount);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}