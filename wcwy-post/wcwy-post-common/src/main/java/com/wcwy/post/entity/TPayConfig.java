package com.wcwy.post.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 支付兑换配置表
 * @TableName t_pay_config
 */
@TableName(value ="t_pay_config")
@Data
@ApiModel("支付兑换配置表")
public class TPayConfig implements Serializable {
    /**
     * 主键Id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("主键Id")
    private Integer id;

    /**
     * 人民币
     */
    @ApiModelProperty("人民币")
    @TableField(value = "money")
    private BigDecimal money;

    /**
     * 无忧豆
     */
    @ApiModelProperty("无忧豆")
    @TableField(value = "bean_count")
    private Integer beanCount;

    /**
     * 简历等级A(30万< 年薪)
     */
    @ApiModelProperty("简历等级A(30万< 年薪)")
    @TableField(value = "grade_A")
    private Integer gradeA;

    /**
     * 简历等级B(10万< 年薪 ≤ 20万)
     */
    @TableField(value = "grade_B")
    @ApiModelProperty("简历等级B(10万< 年薪 ≤ 20万)")
    private Integer gradeB;

    /**
     * 简历等级C(20万< 年薪 ≤ 30万)
     */
    @TableField(value = "grade_C")
    @ApiModelProperty("简历等级C(20万< 年薪 ≤ 30万)")
    private Integer gradeC;

    /**
     * 简历等级D(30万≤ 年薪 ≤ 50万)
     */
    @TableField(value = "grade_D")
    @ApiModelProperty("简历等级D(30万≤ 年薪 ≤ 50万)")
    private Integer gradeD;

    /**
     * 简历等级E(50万>年薪)
     */
    @TableField(value = "grade_E")
    @ApiModelProperty("简历等级E(50万>年薪)")
    private Integer gradeE;

    /**
     * 查看简历有效期
     */
    @TableField(value = "view_resume_time")
    @ApiModelProperty("查看简历有效期")
    private Integer viewResumeTime;

    /**
     * 下载简历有效期
     */
    @TableField(value = "download_resume_time")
    @ApiModelProperty("下载简历有效期")
    private Integer downloadResumeTime;

    /**
     * 注册(无忧币)
     */
    @TableField(value = "register_count")
    @ApiModelProperty("注册(无忧币)")
    private Integer registerCount;

    /**
     * 注册(金币)
     */
    @TableField(value = "register_gold")
    @ApiModelProperty("注册(金币)")
    private Integer registerGold;

    /**
     * 金币下载-简历等级A(30万< 年薪)
     */
    @TableField(value = "gold_A")
    @ApiModelProperty("金币下载-简历等级A(30万< 年薪)")
    private Integer goldA;

    /**
     * 金币下载-简历等级B(30万≤ 年薪 ≤ 50万)
     */
    @TableField(value = "gold_B")
    @ApiModelProperty("金币下载-简历等级B(30万≤ 年薪 ≤ 50万)")
    private Integer goldB;

    /**
     * 金币下载-简历等级C(50万>年薪)
     */
    @TableField(value = "gold_C")
    @ApiModelProperty("金币下载-简历等级C(50万>年薪)")
    private Integer goldC;

    /**
     * 充值
     */
    @TableField(value = "recharge_count")
    @ApiModelProperty("充值")
    private Integer rechargeCount;

    /**
     * 接单
     */
    @TableField(value = "orders_count")
    @ApiModelProperty("接单")
    private Integer ordersCount;

    /**
     * 简历推荐
     */
    @TableField(value = "resume_count")
    @ApiModelProperty("简历推荐")
    private Integer resumeCount;

    /**
     * 邀请推荐官的简历下载分成(简历)
     */
    @TableField(value = "invite_job_hunter_download_B")
    @ApiModelProperty("邀请推荐官的简历下载分成(简历)")
    private Integer inviteJobHunterDownloadB;

    /**
     * 邀请求职者的简历下载分成(简历)
     */
    @TableField(value = "invite_job_hunter_download_A")
    @ApiModelProperty("邀请求职者的简历下载分成(简历)")
    private Integer inviteJobHunterDownloadA;

    /**
     * 邀请企业的下载简历分成(简历)
     */
    @TableField(value = "invite_firm_download_A")
    @ApiModelProperty("邀请企业的下载简历分成(简历)")
    private Integer inviteFirmDownloadA;

    /**
     * 邀请推荐官的企业的下载简历分成(简历)
     */
    @TableField(value = "invite_firm_download_B")
    @ApiModelProperty("邀请推荐官的分成(职位)")
    private Integer inviteFirmDownloadB;

    /**
     * 邀请推荐官的分成(职位)
     */
    @TableField(value = "invite_firm_post_B")
    @ApiModelProperty("邀请推荐官的分成(职位)")
    private Integer inviteFirmPostB;

    /**
     * 邀请企业的分成(职位)
     */
    @TableField(value = "invite_firm_post_A")
    @ApiModelProperty("邀请企业的分成(职位)")
    private Integer inviteFirmPostA;

    /**
     * 交付职位的推荐官分成
     */
    @TableField(value = "invite_interaction_post_A")
    @ApiModelProperty("交付职位的推荐官分成")
    private Integer inviteInteractionPostA;

    /**
     * 邀请交付职位推荐官的分成
     */
    @TableField(value = "invite_interaction_post_B")
    @ApiModelProperty("邀请交付职位推荐官的分成")
    private Integer inviteInteractionPostB;

    /**
     * 一面
     */
    @TableField(value = "one_side_count")
    @ApiModelProperty("一面")
    private Integer oneSideCount;

    /**
     * offer
     */
    @TableField(value = "offer_count")
    @ApiModelProperty("offer")
    private Integer offerCount;

    /**
     * 过保
     */
    @TableField(value = "warranty_count")
    @ApiModelProperty("过保")
    private Integer warrantyCount;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    /**
     * 无忧币与金币比例
     */
    @TableField(value = "proportion")
    @ApiModelProperty("无忧币与金币比例")
    private Integer proportion;

    /**
     * 折扣(无忧币)
     */
    @TableField(value = "discount_currency")
    @ApiModelProperty("折扣(无忧币)")
    private Integer discountCurrency;

    /**
     * 折扣金币
     */
    @TableField(value = "discount_gold")
    @ApiModelProperty("折扣金币")
    private Integer discountGold;

    /**
     * 满月付百分比
     */
    @TableField(value = "full_moon_payment")
    @ApiModelProperty("满月付百分比")
    private Integer fullMoonPayment;

    /**
     * 入职付百分比
     */
    @TableField(value = "entry_payment")
    @ApiModelProperty("入职付百分比")
    private Integer entryPayment;

    /**
     * 面试付百分比
     */
    @TableField(value = "interview_payment")
    @ApiModelProperty("面试付百分比")
    private Integer interviewPayment;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


    @TableField(value = "resume_pay")
    @ApiModelProperty("简历付比例")
    private Integer resumePay;


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
        TPayConfig other = (TPayConfig) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getMoney() == null ? other.getMoney() == null : this.getMoney().equals(other.getMoney()))
            && (this.getBeanCount() == null ? other.getBeanCount() == null : this.getBeanCount().equals(other.getBeanCount()))
            && (this.getGradeA() == null ? other.getGradeA() == null : this.getGradeA().equals(other.getGradeA()))
            && (this.getGradeB() == null ? other.getGradeB() == null : this.getGradeB().equals(other.getGradeB()))
            && (this.getGradeC() == null ? other.getGradeC() == null : this.getGradeC().equals(other.getGradeC()))
            && (this.getGradeD() == null ? other.getGradeD() == null : this.getGradeD().equals(other.getGradeD()))
            && (this.getGradeE() == null ? other.getGradeE() == null : this.getGradeE().equals(other.getGradeE()))
            && (this.getViewResumeTime() == null ? other.getViewResumeTime() == null : this.getViewResumeTime().equals(other.getViewResumeTime()))
            && (this.getDownloadResumeTime() == null ? other.getDownloadResumeTime() == null : this.getDownloadResumeTime().equals(other.getDownloadResumeTime()))
            && (this.getRegisterCount() == null ? other.getRegisterCount() == null : this.getRegisterCount().equals(other.getRegisterCount()))
            && (this.getRegisterGold() == null ? other.getRegisterGold() == null : this.getRegisterGold().equals(other.getRegisterGold()))
            && (this.getGoldA() == null ? other.getGoldA() == null : this.getGoldA().equals(other.getGoldA()))
            && (this.getGoldB() == null ? other.getGoldB() == null : this.getGoldB().equals(other.getGoldB()))
            && (this.getGoldC() == null ? other.getGoldC() == null : this.getGoldC().equals(other.getGoldC()))
            && (this.getRechargeCount() == null ? other.getRechargeCount() == null : this.getRechargeCount().equals(other.getRechargeCount()))
            && (this.getOrdersCount() == null ? other.getOrdersCount() == null : this.getOrdersCount().equals(other.getOrdersCount()))
            && (this.getResumeCount() == null ? other.getResumeCount() == null : this.getResumeCount().equals(other.getResumeCount()))
            && (this.getInviteJobHunterDownloadB() == null ? other.getInviteJobHunterDownloadB() == null : this.getInviteJobHunterDownloadB().equals(other.getInviteJobHunterDownloadB()))
            && (this.getInviteJobHunterDownloadA() == null ? other.getInviteJobHunterDownloadA() == null : this.getInviteJobHunterDownloadA().equals(other.getInviteJobHunterDownloadA()))
            && (this.getInviteFirmDownloadA() == null ? other.getInviteFirmDownloadA() == null : this.getInviteFirmDownloadA().equals(other.getInviteFirmDownloadA()))
            && (this.getInviteFirmDownloadB() == null ? other.getInviteFirmDownloadB() == null : this.getInviteFirmDownloadB().equals(other.getInviteFirmDownloadB()))
            && (this.getInviteFirmPostB() == null ? other.getInviteFirmPostB() == null : this.getInviteFirmPostB().equals(other.getInviteFirmPostB()))
            && (this.getInviteFirmPostA() == null ? other.getInviteFirmPostA() == null : this.getInviteFirmPostA().equals(other.getInviteFirmPostA()))
            && (this.getInviteInteractionPostA() == null ? other.getInviteInteractionPostA() == null : this.getInviteInteractionPostA().equals(other.getInviteInteractionPostA()))
            && (this.getInviteInteractionPostB() == null ? other.getInviteInteractionPostB() == null : this.getInviteInteractionPostB().equals(other.getInviteInteractionPostB()))
            && (this.getOneSideCount() == null ? other.getOneSideCount() == null : this.getOneSideCount().equals(other.getOneSideCount()))
            && (this.getOfferCount() == null ? other.getOfferCount() == null : this.getOfferCount().equals(other.getOfferCount()))
            && (this.getWarrantyCount() == null ? other.getWarrantyCount() == null : this.getWarrantyCount().equals(other.getWarrantyCount()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getProportion() == null ? other.getProportion() == null : this.getProportion().equals(other.getProportion()))
            && (this.getDiscountCurrency() == null ? other.getDiscountCurrency() == null : this.getDiscountCurrency().equals(other.getDiscountCurrency()))
            && (this.getDiscountGold() == null ? other.getDiscountGold() == null : this.getDiscountGold().equals(other.getDiscountGold()))
            && (this.getFullMoonPayment() == null ? other.getFullMoonPayment() == null : this.getFullMoonPayment().equals(other.getFullMoonPayment()))
            && (this.getEntryPayment() == null ? other.getEntryPayment() == null : this.getEntryPayment().equals(other.getEntryPayment()))
            && (this.getInterviewPayment() == null ? other.getInterviewPayment() == null : this.getInterviewPayment().equals(other.getInterviewPayment()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getMoney() == null) ? 0 : getMoney().hashCode());
        result = prime * result + ((getBeanCount() == null) ? 0 : getBeanCount().hashCode());
        result = prime * result + ((getGradeA() == null) ? 0 : getGradeA().hashCode());
        result = prime * result + ((getGradeB() == null) ? 0 : getGradeB().hashCode());
        result = prime * result + ((getGradeC() == null) ? 0 : getGradeC().hashCode());
        result = prime * result + ((getGradeD() == null) ? 0 : getGradeD().hashCode());
        result = prime * result + ((getGradeE() == null) ? 0 : getGradeE().hashCode());
        result = prime * result + ((getViewResumeTime() == null) ? 0 : getViewResumeTime().hashCode());
        result = prime * result + ((getDownloadResumeTime() == null) ? 0 : getDownloadResumeTime().hashCode());
        result = prime * result + ((getRegisterCount() == null) ? 0 : getRegisterCount().hashCode());
        result = prime * result + ((getRegisterGold() == null) ? 0 : getRegisterGold().hashCode());
        result = prime * result + ((getGoldA() == null) ? 0 : getGoldA().hashCode());
        result = prime * result + ((getGoldB() == null) ? 0 : getGoldB().hashCode());
        result = prime * result + ((getGoldC() == null) ? 0 : getGoldC().hashCode());
        result = prime * result + ((getRechargeCount() == null) ? 0 : getRechargeCount().hashCode());
        result = prime * result + ((getOrdersCount() == null) ? 0 : getOrdersCount().hashCode());
        result = prime * result + ((getResumeCount() == null) ? 0 : getResumeCount().hashCode());
        result = prime * result + ((getInviteJobHunterDownloadB() == null) ? 0 : getInviteJobHunterDownloadB().hashCode());
        result = prime * result + ((getInviteJobHunterDownloadA() == null) ? 0 : getInviteJobHunterDownloadA().hashCode());
        result = prime * result + ((getInviteFirmDownloadA() == null) ? 0 : getInviteFirmDownloadA().hashCode());
        result = prime * result + ((getInviteFirmDownloadB() == null) ? 0 : getInviteFirmDownloadB().hashCode());
        result = prime * result + ((getInviteFirmPostB() == null) ? 0 : getInviteFirmPostB().hashCode());
        result = prime * result + ((getInviteFirmPostA() == null) ? 0 : getInviteFirmPostA().hashCode());
        result = prime * result + ((getInviteInteractionPostA() == null) ? 0 : getInviteInteractionPostA().hashCode());
        result = prime * result + ((getInviteInteractionPostB() == null) ? 0 : getInviteInteractionPostB().hashCode());
        result = prime * result + ((getOneSideCount() == null) ? 0 : getOneSideCount().hashCode());
        result = prime * result + ((getOfferCount() == null) ? 0 : getOfferCount().hashCode());
        result = prime * result + ((getWarrantyCount() == null) ? 0 : getWarrantyCount().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getProportion() == null) ? 0 : getProportion().hashCode());
        result = prime * result + ((getDiscountCurrency() == null) ? 0 : getDiscountCurrency().hashCode());
        result = prime * result + ((getDiscountGold() == null) ? 0 : getDiscountGold().hashCode());
        result = prime * result + ((getFullMoonPayment() == null) ? 0 : getFullMoonPayment().hashCode());
        result = prime * result + ((getEntryPayment() == null) ? 0 : getEntryPayment().hashCode());
        result = prime * result + ((getInterviewPayment() == null) ? 0 : getInterviewPayment().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", money=").append(money);
        sb.append(", beanCount=").append(beanCount);
        sb.append(", gradeA=").append(gradeA);
        sb.append(", gradeB=").append(gradeB);
        sb.append(", gradeC=").append(gradeC);
        sb.append(", gradeD=").append(gradeD);
        sb.append(", gradeE=").append(gradeE);
        sb.append(", viewResumeTime=").append(viewResumeTime);
        sb.append(", downloadResumeTime=").append(downloadResumeTime);
        sb.append(", registerCount=").append(registerCount);
        sb.append(", registerGold=").append(registerGold);
        sb.append(", goldA=").append(goldA);
        sb.append(", goldB=").append(goldB);
        sb.append(", goldC=").append(goldC);
        sb.append(", rechargeCount=").append(rechargeCount);
        sb.append(", ordersCount=").append(ordersCount);
        sb.append(", resumeCount=").append(resumeCount);
        sb.append(", inviteJobHunterDownloadB=").append(inviteJobHunterDownloadB);
        sb.append(", inviteJobHunterDownloadA=").append(inviteJobHunterDownloadA);
        sb.append(", inviteFirmDownloadA=").append(inviteFirmDownloadA);
        sb.append(", inviteFirmDownloadB=").append(inviteFirmDownloadB);
        sb.append(", inviteFirmPostB=").append(inviteFirmPostB);
        sb.append(", inviteFirmPostA=").append(inviteFirmPostA);
        sb.append(", inviteInteractionPostA=").append(inviteInteractionPostA);
        sb.append(", inviteInteractionPostB=").append(inviteInteractionPostB);
        sb.append(", oneSideCount=").append(oneSideCount);
        sb.append(", offerCount=").append(offerCount);
        sb.append(", warrantyCount=").append(warrantyCount);
        sb.append(", createTime=").append(createTime);
        sb.append(", proportion=").append(proportion);
        sb.append(", discountCurrency=").append(discountCurrency);
        sb.append(", discountGold=").append(discountGold);
        sb.append(", fullMoonPayment=").append(fullMoonPayment);
        sb.append(", entryPayment=").append(entryPayment);
        sb.append(", interviewPayment=").append(interviewPayment);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}