package com.wcwy.common.redis.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ClassName: Lock
 * Description:
 * date: 2022/10/17 16:08
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@AllArgsConstructor
@Getter
public enum Lock {
    UPDATE_CURRENCY_COUNT("currencyCountLock"),
    UPDATE_HOT("update_hot:"),
    PAYMENT("payment"),//支付锁
    COLLECT_JOB_HUNTER("CollectJobHunter"),//支付锁
    TO_LEAD("to_lead"),//导入简历
    DOWNLOAD("download");
   private String lock;
}
