package com.wcwy.common.redis.enums;

/**
 * ClassName: PostRecord
 * Description:记录岗位职位投简唯一性
 * date: 2022/11/11 11:29
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
public enum PostRecordSole {
    FLOW("","flow:"),//浏览
    BROWSE("","browse:"),//浏览
    SHARE("","share:"),//推荐
    DOWNLOAD("","download:"),
    INTERVIEW("","interview:"),//面试
    ENTRY("","entry:"),//入职
    SUBSCRIBE("","subscribe:"),//预约面试
    OFFER("","offer:"),//预约面试
    WEED_OUT("","weed_out:"),//淘汰
    OVER_INSURED("","over_insured:");//过保
    private String key; //类名
    private String value;

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

    PostRecordSole(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
