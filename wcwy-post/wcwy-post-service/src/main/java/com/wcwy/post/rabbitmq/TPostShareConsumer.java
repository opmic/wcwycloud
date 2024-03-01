/*
package com.wcwy.post.rabbitmq;

import com.alibaba.fastjson.JSON;
import com.wcwy.common.redis.enums.Record;
import com.wcwy.common.redis.enums.Sole;
import com.wcwy.post.entity.TPostShare;
import com.wcwy.post.service.TPostShareService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

*/
/**
 * ClassName: TPostShareComsumer
 * Description:
 * date: 2022/11/11 9:58
 *
 * @author tangzhuo
 * @since JDK 1.8
 *//*

@Component
@Slf4j
public class TPostShareConsumer {
    public static final String CONFIRM_POST_RECORD_QUEUE_NAME = "postRecord.queue";
    public static final String BACK_POST_RECORD_QUEUE = "backupPostRecord.queue";

    @Resource
    private TPostShareService tPostShareService;
    @Resource
    private RedisTemplate redisTemplate;

    */
/**
     * 更新岗位记录
     * @param message
     *//*

    @RabbitListener(queues = CONFIRM_POST_RECORD_QUEUE_NAME)
    public void postRecordQueue(Message message) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String msg = new String(message.getBody());
        Map map = JSON.parseObject(msg, Map.class);
        Boolean uuid = valueOperations.setIfAbsent(Sole.UPDATE_POST_RECORD.getKey()+map.get("uuid"), map.get("uuid"));
        if(! uuid){
            return;
        }
        TPostShare tPostShare = (TPostShare) valueOperations.get(Record.POST_RECORD.getValue()+ map.get("post"));
        boolean b = tPostShareService.updateById(tPostShare);
        if(b){
            log.info("更行成功{}",map.get("post"));
        }else {
            log.error("更行失败{}",map.get("post"));
        }

    }
    */
/**
     * 备份没有更新成功的数据重新更新
     * @param message
     *//*

    @RabbitListener(queues = BACK_POST_RECORD_QUEUE)
    public void backupPostRecordQueue(Message message) {
        String key = Sole.AGAIN_UPDATE_POST_RECORD.getKey();
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        String msg = new String(message.getBody());
        setOperations.add(key, msg);
    }
}
*/
