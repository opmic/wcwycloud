package com.wcwy.common.redis.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * ClassName: MQEntity
 * Description:
 * date: 2023/3/30 9:58
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
public class MQEntity {

    private LocalDateTime time=LocalDateTime.now();

    private String date="";
    private String message;

    private Throwable exception;

}
