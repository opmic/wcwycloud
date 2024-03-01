package com.wcwy.common.config.enums;


import java.util.Arrays;

/**
 * 客户端类型
 *
 * @author liming
 * @date 2021/12/6 2:30 下午
 */
public enum ClientTypeEnum {

    APP("app", "app程序"),
    WEB("web", "后台"),
    MINI_APP("mini_app", "小程序"),
    H5("h5", "H5页面"),
    API("api", "Api调用"),
    UNKNOWN("", "未知的客户端");

    private String code;

    private String desc;

    ClientTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 获取请求的客户端类型
     *
     * @param code 客户端编码
     * @return {@link ClientTypeEnum}
     * @date 2021/12/6 2:43 下午
     * @author liming
     */
    public static ClientTypeEnum getClientTypeByCode(String code) {
        return Arrays.stream(values()).filter(s -> s.getCode().equals(code)).findFirst().orElse(UNKNOWN);
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
