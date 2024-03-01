package com.wcwy.common.base.result;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author： 乐哥聊编程(全平台同号)
 */
@AllArgsConstructor
@NoArgsConstructor
public enum ResultCode implements IResultCode, Serializable {

    SUCCESS("200", "成功"),
    SYSTEM_EXECUTION_ERROR("999999", "系统执行出错"),
    USERNAME_OR_PASSWORD_ERROR("601", "用户名或密码错误"),
    USER_NOT_EXIST("602", "用户不存在"),
    CLIENT_AUTHENTICATION_FAILED("603", "客户端认证失败"),
    ACCESS_UNAUTHORIZED("401", "未授权"),
    TOKEN_INVALID_OR_EXPIRED("604", "token非法或失效"),
    TOKEN_ACCESS_FORBIDDEN("605", "token禁止访问"),
    FLOW_LIMITING("606", "系统限流"),
    DEGRADATION("607", "系统功能降级"),
    SERVICE_NO_AUTHORITY("608", "服务未授权"),
    PARAM_IS_NOT_EMPTY("609", "参数不能为空"),
    ;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    private String code;

    private String msg;

    @Override
    public String toString() {
        return "{" +
                "\"code\":\"" + code + '\"' +
                ", \"msg\":\"" + msg + '\"' +
                '}';
    }

}
