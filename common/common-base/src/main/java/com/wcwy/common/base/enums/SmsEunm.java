package com.wcwy.common.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ClassName: SmsEunm
 * Description:
 * date: 2022/9/2 9:11
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@AllArgsConstructor
@Getter
public enum SmsEunm {
    LOGIN_CODE ( "login:code"),//验证码登录
    UPDATE_CODE ( "update:code"), //验证码
    UPDATE_PHONE("updatePhone:code"),//更新电话号码验证码
    CODE_COUNT("code:count"),//统计发送验证码的数量计数
    PASSWORD_CODE("password:code"),//重置密码
    BINDING_PHONE("bindingPhone:code"),//绑定电话号码
    AUTHENTICATION_CODE("authentication:code"),//注册验证码
    INSERT_CODE("insert:code");//注册验证码



    private final String type;
}
