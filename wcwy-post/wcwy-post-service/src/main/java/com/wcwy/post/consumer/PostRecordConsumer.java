package com.wcwy.post.consumer;

import com.alibaba.fastjson.JSON;
import com.wcwy.common.redis.enums.PostRecordSole;
import com.wcwy.common.redis.enums.Record;
import com.wcwy.common.redis.enums.Sole;
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.common.utils.PostRecord;
import com.wcwy.post.entity.TPostShare;
import com.wcwy.post.service.TPostShareService;
import com.wcwy.system.annotation.RocketLog;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * ClassName: postRecordProduce
 * Description:
 * date: 2022/12/9 15:41
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Component
@RocketMQMessageListener(topic = PostRecord.TOPIC, consumerGroup = PostRecord.GROUP)//默认 集群
@Slf4j
public class PostRecordConsumer implements RocketMQListener<String> {
    @Resource
    private TPostShareService tPostShareService;
    @Resource
    private RedisUtils redisUtils;


    @SneakyThrows
    @Override
    @RocketLog(title = "发布岗位纪录表", businessType = 1)
    public void onMessage(String msg) {
        Map map = JSON.parseObject(msg, Map.class);
        try {

            Boolean uuid = redisUtils.setIfAbsent(Sole.UPDATE_POST_RECORD.getKey() + map.get("uuid"), map.get("uuid").toString(), 24);
            if (!uuid) {
                return;
            }


            Integer type = (Integer) map.get("type");
            Integer state = (Integer) map.get("state");
            if(type==0){
                tPostShareService.updateRedisPost(map.get("jobHunter").toString(),map.get("post").toString(),state ,map.get("putInUser").toString());
            }else if(type==1){
                tPostShareService.cancelRedisPost(map.get("jobHunter").toString(),map.get("post").toString(),state ,map.get("putInUser").toString());
            }


            // boolean b = tPostShareService.updateById(tPostShare);

        } catch (Exception e) {
            redisUtils.del(Sole.UPDATE_POST_RECORD.getKey() + map.get("uuid"));
            redisUtils.acceptSet(PostRecord.GROUP, msg);
            throw new Exception(e);
        }


    }


}
