package com.wcwy.post.produce;

import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.common.utils.ApplyForInvoice;
import com.wcwy.common.utils.RunningWaterMQ;
import com.wcwy.system.annotation.RocketLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ClassName: RunningWaterProduce
 * Description:
 * date: 2023/6/30 16:19
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Service
@Slf4j
public class RunningWaterProduce {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    @Autowired
    private RedisUtils redisUtils;
    //弃用
    @RocketLog(title = "流水通知", businessType =2)
    public void sendOrderlyMessage(String msg) {

        rocketMQTemplate.asyncSend(RunningWaterMQ.TOPIC,msg ,
                new SendCallback() {
                    @Override

                    public void onSuccess(SendResult sendResult) {

                    }

                    @Override

                    public void onException(Throwable throwable) {
                        redisUtils.sendSet(RunningWaterMQ.TOPIC,msg,throwable);
                    }
                });
    }
}
