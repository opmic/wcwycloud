package com.wcwy.common.redis.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ClassName: QRCode
 * Description:
 * date: 2022/12/26 11:16
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@AllArgsConstructor
@Getter
public enum QRCode {
    QR_IMAGES("TCompanyController","QRImages"),
    QR_CODE("TCompanyController","QRCode:");//存储邀请链接过期时间

    private String key; //类名
    private String value;
}
