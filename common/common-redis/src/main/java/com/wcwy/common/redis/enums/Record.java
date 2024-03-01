package com.wcwy.common.redis.enums;

/**
 * ClassName: Record
 * Description:记录
 * date: 2022/11/11 7:58
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
public enum Record {
   POST_RECORD("PutInResumeController","postRecord:") ;



    Record(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    private String key; //类名
    private String value;
}
