package com.wcwy.post.task;

import com.alibaba.fastjson.JSON;
import com.wcwy.common.redis.enums.Sole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Set;

/**
 * ClassName: TPostShareConsumerTask
 * Description:
 * date: 2022/11/11 10:27
 *
 * @author tangzhuo
 * @since JDK 1.8
 */

@Configuration
@Slf4j
public class TPostShareConsumerTask {

    @Resource
    private RedisTemplate redisTemplate;
   /* @Resource
    private RabbitTemplate rabbitTemplate;*/
    /**
     * @Description: 记录岗位失败的操作的数据进行二次消费
     * @return null
     * @Author tangzhuo
     * @CreateTime 2022/11/14 14:10
     */

   /* @Scheduled(cron = "0/30 * * * * ?")
    public void updateTPostShare() throws Exception {
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        String key = Sole.AGAIN_UPDATE_POST_RECORD.getKey();
        Set<String> members = setOperations.members(key);
        for (String o : members) {
            Map map = JSON.parseObject(o, Map.class);
            CorrelationData correlationData1 = new CorrelationData((String) map.get("uuid"));
            rabbitTemplate.convertAndSend("postRecord.exchange", "postRecord", o , correlationData1);
            setOperations.remove(key,o);
        }

    }*/
}
