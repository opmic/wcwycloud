package com.wcwy.common.redis.util;

import com.wcwy.common.base.utils.DateUtils;
import com.wcwy.common.base.utils.RandomUtils;
import com.wcwy.common.base.utils.RedisKeyContants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * ClassName: IDGenerator
 * Description:生成唯一工具类
 * date: 2022/9/1 16:31
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Component
public class IDGenerator {

    @Autowired
    private RedisTemplate redisTemplate;

    public  String generateCode(String prefix) {

        String currentDt = DateUtils.getCurrentDateStr(DateUtils.DATE_PARENT_YYMMDDHHMMSS_SSS);
        Long incrtNum=generate(RedisKeyContants.REDIS_ID_PREFIX+prefix,DateUtils.getTodayEndTime());
        if(null != incrtNum){
            return prefix+currentDt+"-"+incrtNum;
        }
        String random = RandomUtils.generateRandomNum(6);
        return prefix+currentDt+"-"+random;
    }

    public  Long generate(String key, Date expireTime) {
        RedisAtomicLong counter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
        counter.expireAt(expireTime);
        return counter.incrementAndGet();
    }

    public  String invitationCode(String prefix) {

        String currentDt = DateUtils.getCurrentDateStr(DateUtils.DATE_PARENT_YYMMDDHHMMSS_SSS);
        Long incrtNum=generate(RedisKeyContants.REDIS_ID_PREFIX+prefix,DateUtils.getTodayEndTime());
        if(null != incrtNum){
            return currentDt+incrtNum;
        }
    /*  String random = RandomUtils.generateRandomNum(6);*/
        return currentDt;
    }
}
