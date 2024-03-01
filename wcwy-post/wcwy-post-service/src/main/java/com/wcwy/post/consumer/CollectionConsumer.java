package com.wcwy.post.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wcwy.common.redis.enums.Cache;
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.common.utils.EiCompanyPostMQ;
import com.wcwy.common.utils.PostOrder;
import com.wcwy.common.utils.TCompanyHotMQ;
import com.wcwy.post.entity.TCompanyHot;
import com.wcwy.post.service.TCompanyHotService;
import com.wcwy.system.annotation.RocketLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * ClassName: CollectionProduce
 * Description:
 * date: 2023/1/12 16:59
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@RocketMQMessageListener(topic = TCompanyHotMQ.TOPIC, consumerGroup = TCompanyHotMQ.GROUP)
@Component
@Slf4j
public class CollectionConsumer implements RocketMQListener<String> {
    @Autowired
    private TCompanyHotService tCompanyHotService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedisUtils redisUtils;
    @Override
    @RocketLog(title = "发送增加企业热度表", businessType =1)
    public void onMessage(String msg) {
        SetOperations setOperations = redisTemplate.opsForSet();
        ValueOperations<String,TCompanyHot> valueOperations = redisTemplate.opsForValue();
        Map<String,String> map= JSON.parseObject(msg,Map.class);
        try {

            String companyId =  map.get("companyId");
            if(StringUtils.isEmpty(companyId)){
                return;
            }
            String uuid = (String) map.get("uuid");
            Boolean member = setOperations.isMember(TCompanyHotMQ.GROUP, uuid);
            if(! member){
                TCompanyHot tCompanyHot = valueOperations.get(Cache.CACHE_COMPANY_HOT.getKey() + companyId);
                if(tCompanyHot==null){
                    return;
                }
                tCompanyHot.setHot(tCompanyHot.getHot()+1L);
                redisTemplate.delete(Cache.CACHE_COMPANY_HOT.getKey() + companyId);
                valueOperations.set(Cache.CACHE_COMPANY_HOT.getKey() + companyId,tCompanyHot);
                tCompanyHotService.updateById(tCompanyHot);
                setOperations.add(TCompanyHotMQ.GROUP,uuid);
            }
        }catch (Exception e){
            redisUtils.acceptSet(TCompanyHotMQ.GROUP,msg);
            throw  e;
        }

    }
}
