package com.wcwy.company.vo;

/**
 * ClassName: LoginVo
 * Description:
 * date: 2023/5/12 15:03
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
public class LoginVo {
 private   String  nonceStr;

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    private   String  value;
}
