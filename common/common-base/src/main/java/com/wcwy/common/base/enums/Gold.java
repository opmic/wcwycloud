package com.wcwy.common.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ClassName: Gold
 * Description:
 * date: 2023/7/11 14:07
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@AllArgsConstructor
@Getter
public enum Gold {
    SHARE_REGISTRATION("share_registration:",8),
    OFFER("offer:",7),
    ENTRY("entry:",6),
    INTERVIEW_INVITATION("interview_invitation:",5),
    RECOMMENDATION_REPORT_BROWSE("recommendation_report_browse:",4),
    RECOMMENDATION_REPORT("recommendation_report:",3),
    POST_GOLD("post_gold:",1),
    LOGIN_GOLD("login_gold:",2);

    private  String value;
    private Integer type;
}
