package com.wcwy.company.produce;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.wcwy.common.redis.entity.MQEntity;
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.common.utils.PostOrder;
import com.wcwy.common.utils.PostRecord;
import com.wcwy.system.annotation.RocketLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

/**
 * ClassName: postRecordProduce
 * Description:
 * date: 2022/12/9 15:41
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Service
@Slf4j
public class PostRecordProduce {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    @Autowired
    private RedisUtils redisUtils;
    @RocketLog(title = "发布岗位纪录表", businessType =2)
    public void sendSyncMessage(Map msg){
        String mapJson = JSON.toJSONString(msg);
        SendResult sendResult = rocketMQTemplate.syncSend(PostRecord.TOPIC, mapJson);
        if(sendResult.getSendStatus() != SendStatus.SEND_OK){
            redisUtils.sendSet(PostRecord.TOPIC,JSON.toJSONString(msg),null);
        }
    }

    //出现未发生成功的信息
    @Scheduled(cron = "0/5 * * * * ?")
    public void retry(){
        Set<Object> set = redisUtils.getSet(PostRecord.TOPIC);
        if(set==null){
            return;
        }
        for (Object o : set) {
            MQEntity mqEntity = JSONUtil.toBean(o.toString(), MQEntity.class);
            String date = mqEntity.getDate();
            /*  String s = JSON.parseObject(date, String.class);*/
            Map map = JSON.parseObject(date, Map.class);
            sendSyncMessage(map);
            redisUtils.delSet(PostOrder.TOPIC,o);
        }
    }
}
