package com.wcwy.company.consumer;

import com.alibaba.fastjson.JSON;
import com.wcwy.common.base.enums.Gold;
import com.wcwy.common.base.utils.RewardsUtils;
import com.wcwy.common.redis.enums.Sole;
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.common.utils.EiCompanyPostMQ;
import com.wcwy.common.utils.GoldMQ;
import com.wcwy.company.asyn.RunningWaterAsync;
import com.wcwy.company.service.RunningWaterService;
import com.wcwy.system.annotation.RocketLog;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * ClassName: GoldConsumer
 * Description:
 * date: 2023/7/11 16:11
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@RocketMQMessageListener(topic = GoldMQ.TOPIC, consumerGroup = GoldMQ.GROUP, consumeMode = ConsumeMode.ORDERLY)
@Component
@Slf4j
public class GoldConsumer implements RocketMQListener<String> {
    @Resource
    private RedisUtils redisUtils;

    @Autowired
    private RunningWaterAsync runningWaterAsync;

    @SneakyThrows
    @Override
    @RocketLog(title = "奖励金币", businessType = 1)
    public void onMessage(String s) {
        HashMap hashMap = JSON.parseObject(s, HashMap.class);
        Boolean uuid = redisUtils.setIfAbsent(GoldMQ.GROUP + ":" + hashMap.get("uuid"), hashMap.get("uuid").toString(), 24 * 7);
        if (!uuid) {
            return;
        }
        try {
            String userid = (String) hashMap.get("userid");
            Integer integer = (Integer) hashMap.get("type");
            if(integer==1){
                runningWaterAsync.postGold(userid);

            }



        } catch (Exception e) {
            redisUtils.del(GoldMQ.GROUP + ":" + hashMap.get("uuid"));
            redisUtils.acceptSet(GoldMQ.GROUP, s);
            throw e;
        }

    }
}
