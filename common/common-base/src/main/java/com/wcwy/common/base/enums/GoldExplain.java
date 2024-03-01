package com.wcwy.common.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ClassName: GoldExplain
 * Description:
 * date: 2023/7/11 14:13
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@AllArgsConstructor
@Getter
public enum GoldExplain {

    video_order("平台奖励-推荐报告浏览"),
    video_uploading("平台奖励-推荐报告浏览"),
    create_activity("平台奖励-推荐报告浏览"),
    share_registration("平台奖励-推荐报告浏览"),

    INVITER_COMPANY("平台奖励-邀请企业入驻"),
    INVITER_JOB_HUNTER("平台奖励-邀请求职者入驻"),
    INVITER_RECOMMEND("平台奖励-邀请推荐官入驻"),
    SHARE_REGISTRATION("平台奖励-分享注册连接"),
    ENTRY("平台奖励-推荐的人选成功入职"),
    OFFER("平台奖励-推荐的人选收到offer"),
    INTERVIEW_INVITATION("平台奖励-推荐的人选被企业约面"),
    RECOMMENDATION_REPORT("平台奖励-推荐报告"),
    RECOMMENDATION_REPORT_BROWSE("平台奖励-推荐报告浏览"),
    POST_GOLD("平台奖励-发布岗位"),
    LOGIN_GOLD("平台奖励-登录平台"),
    REGISTER_GOLD("平台奖励-注册");

    private  String value;
}
