package com.wcwy.common.utils;
import lombok.extern.slf4j.Slf4j;
import java.util.Set;

/**
 * ClassName: TCompanyHotController
 * Description:
 * date: 2023/1/12 11:41
 *
 * @author tangzhuo
 * @since JDK 1.8
 */

@Slf4j
public class TCompanyHotMQ {
    public static final String TOPIC = "company_hot_topic";//发送增加企业热度表
    public static final String GROUP = "company_hot_group";//接受增加企业热度表

}
