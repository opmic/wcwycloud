package com.wcwy.common.redis.enums;

import lombok.Data;

/**
 * ClassName: BackupExchange
 * Description:备份交换机
 * date: 2022/11/10 10:02
 *
 * @author tangzhuo
 * @since JDK 1.8
 */

public enum BackupExchange {
    EXCHANGE_BACKUPS("PutInResumeTask","exchange:backups"),//存储发送给交换机失败的队列数据
    EXCHANGE_ORDER1("PutInResumeTask","exchange:order");//存储发送给交换机失败的id

    private String key; //类名
    private String value;

    BackupExchange(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getKey() {
        return key;
    }
}
