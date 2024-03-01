package com.wcwy.company.produce;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.wcwy.common.base.utils.SendDingDing;
import com.wcwy.common.redis.entity.MQEntity;
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.common.utils.MessageMQ;
import com.wcwy.common.utils.PostOrder;
import com.wcwy.system.annotation.RocketLog;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Set;

/**
 * ClassName: MessageProduce
 * Description:
 * date: 2023/12/28 17:05
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Service
public class MessageProduce {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    @Autowired
    private RedisUtils redisUtils;
    public void sendOrderlyMessage(String msg){
        if(StringUtils.isEmpty(msg)){
            return;
        }
        rocketMQTemplate.asyncSend(MessageMQ.TOPIC, msg,
                new SendCallback() {
                    @Override
                    public void onSuccess(SendResult sendResult) {

                    }
                    @Override
                    public void onException(Throwable throwable) {
                        redisUtils.sendSet(MessageMQ.TOPIC,msg,throwable);
                    }
                });
    }

    @Scheduled(cron = "0/5 * * * * ?")
    public void retry(){
        Set<Object> set = redisUtils.getSet(MessageMQ.TOPIC);
        if(set==null){
            return;
        }
        for (Object o : set) {
            MQEntity mqEntity = JSONUtil.toBean(o.toString(), MQEntity.class);
            String date = mqEntity.getDate();
            Map map = JSON.parseObject(date, Map.class);
            sendOrderlyMessage(JSON.toJSONString(map));
            redisUtils.delSet(MessageMQ.TOPIC,o);
        }
    }
}
