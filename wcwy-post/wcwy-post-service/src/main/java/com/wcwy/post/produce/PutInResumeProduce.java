package com.wcwy.post.produce;

/*import com.wcwy.common.config.PutInResumeMQTemplate;*/
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.common.utils.PutInResumeUtils;
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
public class PutInResumeProduce {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    @Autowired
    private RedisUtils redisUtils;

    //弃用
    public void sendOrderlyMessage(String msg){
        SendResult sendResult = rocketMQTemplate.syncSendOrderly(PutInResumeUtils.TOPIC, msg, PutInResumeUtils.TOPIC);
        if(sendResult.getSendStatus() != SendStatus.SEND_OK){
            redisUtils.acceptSet(PutInResumeUtils.TOPIC,msg);
            log.error("发送失败");
        }
    }
}
