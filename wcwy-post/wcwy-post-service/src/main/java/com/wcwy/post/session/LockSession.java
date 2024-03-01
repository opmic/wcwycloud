package com.wcwy.post.session;

import com.wcwy.common.redis.enums.Lock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * ClassName: LockSession
 * Description:
 * date: 2022/10/18 9:59
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
//@Component
public class LockSession {
    @Autowired
    private RedissonClient redissonClient;

    @Bean
    @Qualifier("currencyCountLock")
    public RLock getLock(){
      return   redissonClient.getLock(Lock.UPDATE_CURRENCY_COUNT.getLock());
    }
}
