package com.wcwy.common.redis.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * ClassName: RedisData
 * Description:
 * date: 2023/10/21 11:13
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
public class RedisData {
    // LocalDateTime ： 同时含有年月日时分秒的日期对象
    // 并且LocalDateTime是线程安全的！
    private LocalDateTime expireTime;
    private Object data;
}
