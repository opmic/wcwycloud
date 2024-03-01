package com.wcwy.common.redis.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ClassName: Message
 * Description:
 * date: 2022/10/29 8:18
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@AllArgsConstructor
@Getter
public enum Message {
    COMPANY_INTERVIEW("InterviewResumeController","company_interview:"),//公司面试消息
    REFERRER_INTERVIEW("InterviewResumeController","referrer_interview:");//投递岗位的人的消息


    private String kind;//使用的类
    private String key;//文件名
}
