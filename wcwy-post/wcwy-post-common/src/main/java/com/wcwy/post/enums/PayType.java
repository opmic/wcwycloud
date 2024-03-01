package com.wcwy.post.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PayType {
    /**
     * 微信
     */
    WXPAY("微信",2),


    /**
     * 支付宝
     */
    ALIPAY("支付宝",1);

    /**
     * 类型
     */
    private final String type;
    private final Integer state;
}
