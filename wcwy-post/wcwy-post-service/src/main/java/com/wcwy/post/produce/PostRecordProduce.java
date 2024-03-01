package com.wcwy.post.produce;

import com.alibaba.fastjson.JSON;
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.common.utils.PostRecord;
import com.wcwy.system.annotation.RocketLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * ClassName: postRecordProduce
 * Description:
 * date: 2022/12/9 15:41
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Service
@Slf4j
public class PostRecordProduce {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedisUtils redisUtils;
    @RocketLog(title = "更新岗位记录生产", businessType =2)
    public void sendSyncMessage(Map msg){
        String mapJson = JSON.toJSONString(msg);
        SendResult sendResult = rocketMQTemplate.syncSend(PostRecord.TOPIC, mapJson);
        if(sendResult.getSendStatus() != SendStatus.SEND_OK){
            redisUtils.acceptSet(PostRecord.TOPIC,JSON.toJSONString(msg));
            log.error("发送失败");
        }
    }
}
