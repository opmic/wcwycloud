package com.wcwy.common.redis.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ClassName: State
 * Description:简历状态记录表
 * date: 2022/10/19 8:37
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@AllArgsConstructor
@Getter
public enum State {
    CLOSE_AN_ACCOUNT("close_an_account"),
    OVER_PROTECTION("over_protection"),
    ENTRY("entry"),
    OFFER("offer"),
    WEED_OUT("weed_out"),
    INTERVIEW("interview"),//面试人数
   // ACCEPT_SUBSCRIBE ("acceptSubscribe"),//预约面试
  //  SUBSCRIBE("subscribe"),//是否预约
  //  EXCLUDE("exclude"),//是否排除
    DOWNLOAD("download"),//下载数
    BROWSE("browse");//浏览次数
   private String type;
}
