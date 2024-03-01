package com.wcwy.common.redis.enums;

/**
 * ClassName: BackQueue
 * Description:备份对列
 * date: 2022/11/10 10:02
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
public enum BackQueue {

    QUEUE_ORDER("PutInResumeTask","queue:order");

    private String key; //类名
    private String value;

    private BackQueue(String key, String value) {
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
}
