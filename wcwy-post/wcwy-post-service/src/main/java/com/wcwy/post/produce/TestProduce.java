package com.wcwy.post.produce;

import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.common.utils.PutInResumeUtils;
import com.wcwy.system.annotation.RocketLog;
import com.wcwy.system.enums.BusinessType;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ClassName: TestProduce
 * Description:
 * date: 2023/6/27 14:37
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Service
@Slf4j
public class TestProduce {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    @Autowired
    private RedisUtils redisUtils;

    //弃用
    @RocketLog(title = "测试", businessType =2)
    public void sendOrderlyMessage(String msg) {
        SendResult sendResult = rocketMQTemplate.syncSendOrderly("test_topic", msg, "test_topic");

        if(sendResult.getSendStatus() != SendStatus.SEND_OK){
            redisUtils.acceptSet("test_topic",msg);
            log.error("发送失败");

        }
    }
}
