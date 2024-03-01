package com.wcwy.common.redis.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * ClassName: RedisIdWorker
 * Description:
 * date: 2023/3/29 17:23
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Component
public class RedisIdWorker {

    /**
     * 开始时间戳   下面数值来源于2022 1月1日 0时0分0秒 获取的时间戳为基准
     */
    private static final long BEGIN_TIMESTAMP = 1640995200L;

    /**
     *  序列号的位数
     */
    private static final int COUNT_BITS = 32 ;
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisIdWorker(StringRedisTemplate stringRedisTemplate){
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public long nextId(String keyPrefix){
        //1 生成时间戳
        LocalDateTime now = LocalDateTime.now();
        long nowSecond = now.toEpochSecond(ZoneOffset.UTC);
        long timeStamp = nowSecond - BEGIN_TIMESTAMP;
        //2  生成序号
        //2.1 获取当前日期， 精确到天
        String date = now.format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));
        Long count = stringRedisTemplate.opsForValue().increment("mq:" + keyPrefix + ":" + date);

        //3  拼接返回
        return timeStamp << COUNT_BITS | count;
    }
}