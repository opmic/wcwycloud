package com.wcwy.post.consumer;

/*import com.wcwy.common.config.PutInResumeMQTemplate;*/
import com.alibaba.fastjson.JSON;
import com.wcwy.common.redis.enums.Record;
import com.wcwy.common.redis.enums.Sole;
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.common.utils.PostShare;
import com.wcwy.post.entity.TPostShare;
import com.wcwy.post.service.TPostShareService;
import com.wcwy.system.annotation.RocketLog;
import lombok.SneakyThrows;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * ClassName: PutInResumeProduce
 * Description:
 * date: 2022/12/7 16:21
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@RocketMQMessageListener(topic = PostShare.TOPIC,consumerGroup = PostShare.GROUP,consumeMode = ConsumeMode.ORDERLY)// 顺序接受
@Component
public class PostShareConsumer implements RocketMQListener<String> {
    @Autowired
   private TPostShareService tPostShareService;
    @Resource
    private RedisTemplate redisTemplate;
    @Autowired
    private RedisUtils redisUtils;
    @SneakyThrows
    @Override
    @RocketLog(title = "更新岗位记录消费", businessType =1)
    public void onMessage(String s) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Map map = JSON.parseObject(s, Map.class);
        try {
            Boolean uuid = valueOperations.setIfAbsent(Sole.UPDATE_POST_RECORD.getKey()+map.get("uuid"), map.get("uuid"));
            if(! uuid){
                return;
            }
            TPostShare tPostShare = (TPostShare) valueOperations.get(Record.POST_RECORD.getValue()+ map.get("post"));
            boolean b1 = tPostShareService.updateById(tPostShare);
            if(! b1){

                throw new Exception("修改失败");
            }
        }catch (Exception e){
            redisTemplate.delete(Sole.UPDATE_POST_RECORD.getKey()+map.get("uuid"));
            redisUtils.acceptSet(PostShare.GROUP,s);
            throw  e;
        }
    }
}
