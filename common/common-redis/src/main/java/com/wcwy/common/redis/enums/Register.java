package com.wcwy.common.redis.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ClassName: Register
 * Description:
 * date: 2023/4/6 14:14
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@AllArgsConstructor
@Getter
public enum Register {

    RECOMMEND_REGISTER("recommend_register");
    private String type;
}
