package com.wcwy.post.produce;

/*import com.wcwy.common.config.PutInResumeMQTemplate;*/
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.common.utils.PostShare;

import com.wcwy.system.annotation.RocketLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ClassName: PutInResumeProduce
 * Description:
 * date: 2022/12/7 16:21
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Service
@Slf4j
public class PostShareProduce {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    @Autowired
    private RedisUtils redisUtils;
    @RocketLog(title = "更新岗位记录生产", businessType =2)
    public void sendOrderlyMessage(String msg){
        SendResult sendResult = rocketMQTemplate.syncSendOrderly(PostShare.TOPIC, msg, PostShare.TOPIC);
        if(sendResult.getSendStatus() != SendStatus.SEND_OK){
            redisUtils.acceptSet(PostShare.TOPIC,msg);
            log.error("发送失败");
        }
    }
}
