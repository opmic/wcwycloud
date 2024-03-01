package com.wcwy.company.produce;

import com.alibaba.fastjson.JSON;
import com.wcwy.common.redis.config.RedisIdWorker;
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.common.utils.UpdateTCompanyPost;
import com.wcwy.system.annotation.RocketLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: UpdateTCompanyPostProduce
 * Description:
 * date: 2023/3/23 17:27
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Service
@Slf4j
public class UpdateTCompanyPostProduce {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private  RedisIdWorker redisIdWorker;
    @Autowired
    private RedisUtils redisUtils;
    @RocketLog(title = "更新岗位基础信息", businessType =2)
    public void updatePostAsyncMessage(String msg) {
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        Map map=new HashMap();
        map.put("data",msg);
        map.put("uuid", redisIdWorker.nextId(UpdateTCompanyPost.UPDATE_COMPANY_TOPIC));
        String toJSONString = JSON.toJSONString(map);
        rocketMQTemplate.asyncSend(UpdateTCompanyPost.UPDATE_COMPANY_TOPIC, toJSONString,
                new SendCallback() {
                    @Override
                    public void onSuccess(SendResult sendResult) {
                        if(sendResult.getSendStatus() != SendStatus.SEND_OK){
                            redisUtils.sendSet(UpdateTCompanyPost.UPDATE_COMPANY_TOPIC,JSON.toJSONString(msg),null);
                        }
                    }

                    @Override
                    public void onException(Throwable throwable) {
                        redisUtils.sendSet(UpdateTCompanyPost.UPDATE_COMPANY_TOPIC,JSON.toJSONString(msg),throwable);
                    }
                });

    }

}
