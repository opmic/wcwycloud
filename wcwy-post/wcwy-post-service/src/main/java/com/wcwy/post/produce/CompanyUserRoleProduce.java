package com.wcwy.post.produce;

import com.alibaba.fastjson.JSON;
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.common.utils.CompanyUserRoleMQ;
import com.wcwy.post.entity.OrderInfo;
import com.wcwy.system.annotation.RocketLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ClassName: CompanyUserRoleProduce
 * Description:
 * date: 2023/4/4 14:31
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Service
@Slf4j
public class CompanyUserRoleProduce {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    @Autowired
    private RedisUtils redisUtils;
    @RocketLog(title = "添加求职者权限", businessType =2)
    public void sendAsyncMessage(OrderInfo orderInfo ) {
        Map<String,Object> map=new ConcurrentHashMap<>();
        map.put("jobHunterId",orderInfo.getJobhunterId());
        map.put("tCompany",orderInfo.getCreateId());
        map.put("put_in_resume_id",orderInfo.getPutInResumeId());
        map.put("UUID",redisUtils.generateCode());
        map.put("identification",orderInfo.getIdentification());
        SendResult sendResult = rocketMQTemplate.syncSend(CompanyUserRoleMQ.TOPIC, JSON.toJSONString(map));
      /*  MessageQueue messageQueue = sendResult.getMessageQueue();*/
 /*       System.out.println(sendResult);
        System.out.println(messageQueue);*/
        if(sendResult.getSendStatus() != SendStatus.SEND_OK){
            redisUtils.sendSet(CompanyUserRoleMQ.TOPIC,JSON.toJSONString(map),null);
            log.error("发送失败");
        }
    }
}
