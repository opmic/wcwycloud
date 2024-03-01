package com.wcwy.company.produce;

import com.alibaba.fastjson.JSON;
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.common.utils.TCompany;
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
import java.util.UUID;

/**
 * ClassName: TCompanyProduce
 * Description:
 * date: 2022/12/26 16:08
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Service
@Slf4j
public class TCompanyProduce {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedisUtils redisUtils;
    //添加企业热度表
    @RocketLog(title = "添加企业热度表", businessType =2)
    public void sendSyncMessageCompanyHot(String msg) {
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        Map map=new HashMap();
        map.put("data",msg);
        map.put("uuid", UUID.randomUUID());
        String toJSONString = JSON.toJSONString(map);
        rocketMQTemplate.asyncSend(TCompany.TOPIC, toJSONString,
                new SendCallback() {
                    @Override
                    public void onSuccess(SendResult sendResult) {
                        if(sendResult.getSendStatus() != SendStatus.SEND_OK){
                            setOperations.add(TCompany.TOPIC,msg);
                            log.error("发送失败");
                        }
                        System.out.println("发送成功！");
                    }
                    @Override
                    public void onException(Throwable throwable) {
                        redisUtils.sendSet(TCompany.TOPIC,JSON.toJSONString(msg),throwable);
                    }
                });
    }


}
