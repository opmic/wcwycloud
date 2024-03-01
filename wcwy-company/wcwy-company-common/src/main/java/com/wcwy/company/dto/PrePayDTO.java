package com.wcwy.company.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ClassName: PrePayDTO
 * Description:
 * date: 2023/8/19 10:23
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "小程序支付订单")
public class PrePayDTO {

    private String appid;// 微信开放平台 - 应用 - AppId，注意和微信小程序、公众号 AppId 可能不一致
    private String partnerid;// 微信支付商户号
    private String prepayId;//  统一下单订单号
    private String package1;
    private String noncestr;// 随机字符串
    private long timestamp;// 时间戳（单位：秒）
    private String sign; // 签名，这里用的 MD5/RSA 签名
    private String signType;

}
