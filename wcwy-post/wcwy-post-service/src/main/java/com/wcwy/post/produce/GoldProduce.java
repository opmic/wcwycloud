package com.wcwy.post.produce;

import com.alibaba.fastjson.JSON;
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.common.utils.CompanyUserRoleMQ;
import com.wcwy.common.utils.EiCompanyPostMQ;
import com.wcwy.common.utils.GoldMQ;
import com.wcwy.system.annotation.RocketLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ClassName: GoldProduce
 * Description:
 * date: 2023/7/11 16:05
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Service
@Slf4j
public class GoldProduce {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    @Autowired
    private RedisUtils redisUtils;
    @RocketLog(title = "奖励金币", businessType =2)
    public void sendAsyncMessage(String mags) {
        SendResult sendResult = rocketMQTemplate.syncSend(GoldMQ.TOPIC, mags);
        if(sendResult.getSendStatus() != SendStatus.SEND_OK){
            redisUtils.sendSet(GoldMQ.TOPIC, mags,null);
            //log.error("发送失败");
        }
    }
}
