package com.wcwy.company.produce;

import com.alibaba.fastjson.JSON;
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.common.utils.PostOrder;
import com.wcwy.common.utils.TCompanyHotMQ;
import com.wcwy.system.annotation.RocketLog;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * ClassName: CollectionProduce
 * Description:
 * date: 2023/1/12 16:59
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Service
public class CollectionProduce {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    @Autowired
    private RedisUtils redisUtils;
    @RocketLog(title = "发送增加企业热度表", businessType =2)
    public void sendAsyncMessage(String msg) {
        Map map=new HashMap();
        map.put("companyId",msg);
        map.put("uuid", UUID.randomUUID());
        String toJSONString = JSON.toJSONString(map);
        for (int i = 0; i < 10; i++) {
            rocketMQTemplate.asyncSend(TCompanyHotMQ.TOPIC, toJSONString,
                    new SendCallback() {
                        @Override
                        public void onSuccess(SendResult sendResult){
                            System.out.println("发送成功！");
                        }
                        @Override
                        public void onException(Throwable throwable) {
                            redisUtils.sendSet(TCompanyHotMQ.TOPIC,toJSONString,throwable);
                            System.out.println("发送失败！");
                        }
                    });
        }
    }
}
