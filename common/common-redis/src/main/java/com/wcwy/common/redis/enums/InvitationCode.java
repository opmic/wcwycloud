package com.wcwy.common.redis.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ClassName: InvitationCode
 * Description:
 * date: 2023/4/10 11:21
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@AllArgsConstructor
@Getter
public enum InvitationCode {
    QR_CODE("QR_code:"),
    INVITATION_URL_CODE("invitation_url_code:"),
    CODE("invitationCode:");
    private String type;
}
