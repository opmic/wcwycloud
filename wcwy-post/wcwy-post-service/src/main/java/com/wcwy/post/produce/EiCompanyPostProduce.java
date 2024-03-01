package com.wcwy.post.produce;

import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.common.utils.ApplyForInvoice;
import com.wcwy.common.utils.EiCompanyPostMQ;
import com.wcwy.system.annotation.RocketLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

/**
 * ClassName: EiCompanyPostProduce
 * Description:
 * date: 2023/3/30 9:18
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Service
@Slf4j
public class EiCompanyPostProduce {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    @Autowired
    private RedisUtils redisUtils;
    @RocketLog(title = "岗位基础表", businessType =2)
    public void sendAsyncMessage(String mags) {
        rocketMQTemplate.asyncSend(EiCompanyPostMQ.TOPIC, mags,
                new SendCallback() {
                    @Override
                    public void onSuccess(SendResult sendResult) {
                        System.out.println("发送成功！");
                    }
                    @Override
                    public void onException(Throwable throwable) {
                        redisUtils.sendSet(EiCompanyPostMQ.TOPIC,mags,throwable);
                        System.out.println("发送失败！");
                    }
                });

    }
}
