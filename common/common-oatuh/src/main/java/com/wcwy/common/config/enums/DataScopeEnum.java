package com.wcwy.common.config.enums;

import java.util.Arrays;

/**
 * 数据域枚举
 *
 * @author liming
 * @date 2022/2/8 4:39 PM
 */
public enum DataScopeEnum {

    ALL("ALL", "所有数据"),
    THIS_LEVEL("THIS_LEVEL", "本级"),
    THIS_LEVEL_CHILDREN("THIS_LEVEL_CHILDREN", "本级以及子级"),
    CUSTOMIZE("CUSTOMIZE", "自定义"),
    SELF("SELF", "个人"),
    UNKNOWN("", "未知");


    private String code;
    private String desc;

    DataScopeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据Code获取数据域类型
     *
     * @param code :
     * @return {@link DataScopeEnum}
     * @date 2022/2/8 4:41 PM
     * @author liming
     */
    public static DataScopeEnum byCode(String code) {
        return Arrays.stream(values()).filter(s -> s.getCode().equals(code)).findFirst().orElse(UNKNOWN);
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
