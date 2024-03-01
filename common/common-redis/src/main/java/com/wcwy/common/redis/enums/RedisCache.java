package com.wcwy.common.redis.enums;

import lombok.Data;

import java.util.Arrays;

/**
 * ClassName: RedisCache
 * Description:
 * date: 2022/9/2 17:03
 *
 * @author tangzhuo
 * @since JDK 1.8
 */

public enum RedisCache {
    TINDUSTRYTYPEDTO("TIndustryTypeDTO","cache:TIndustryTypeDTO"),//TIndustryTypeDTO的缓存
    MAHORPARENTDTO("MajorParentDTO","cache:MajorParentDTO"),
    SCHOOL("School","cache:School"),
    TPRVINCESCITIEDTO("TProvincesCitieDTO","cache:TProvincesCitieDTO"),
    EDUCATION("Education","cache:Education"),//Education
    TJOBHUNTERRESUMECONFIG("TJobhunterResumeConfig","cache:TJobhunterResumeConfig"),//TJobhunterResumeConfig
    WORK("WorkController","cache:work"),
    WEAL("PostWealController","cache:weal"),//公司福利
    GOLD_CACHE("GoldConfig","cache:GoldConfig"),//金币缓存
    TPAYCONFIG("TPayConfig","cache:TPayConfig");//TPayConfig

    private String key; //类名
    private String value;
    RedisCache(String key, String value) {
        this.key = key;
        this.value = value;
    }

    RedisCache(String key) {
        this.key = key;
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
