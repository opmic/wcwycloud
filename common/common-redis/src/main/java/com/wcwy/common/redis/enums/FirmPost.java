package com.wcwy.common.redis.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ClassName: FirmPost
 * Description:
 * date: 2023/4/12 11:06
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@AllArgsConstructor
@Getter
public enum FirmPost {
    FIRM_POST("firm_post:");
    private String type;
}
