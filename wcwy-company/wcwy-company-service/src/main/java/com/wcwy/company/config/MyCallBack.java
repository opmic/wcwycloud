package com.wcwy.company.config;

import com.wcwy.common.redis.enums.BackQueue;
import com.wcwy.common.redis.enums.BackupExchange;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ClassName: MyCallBack
 * Description:
 * date: 2022/9/19 14:21
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
/*@Component
@Slf4j
public class MyCallBack implements RabbitTemplate.ConfirmCallback ,RabbitTemplate.ReturnCallback{

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private RedisTemplate redisTemplate;
    //rabbitTemplate 注入之后就设置该值
    @PostConstruct
    private void init() {
        rabbitTemplate.setConfirmCallback(this);
        *//**
         * true：
         * 交换机无法将消息进行路由时，会将该消息返回给生产者
         * false：
         * 如果发现消息无法进行路由，则直接丢弃
         *//*
        rabbitTemplate.setMandatory(true);
        //设置回退消息交给谁处理
        rabbitTemplate.setReturnCallback(this);
    }
    *//**
     * 交换机不管是否收到消息的一个回调方法
     * CorrelationData
     * 消息相关数据
     * ack
     * 交换机是否收到消息
     *//*
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData != null ? correlationData.getId() : "";
        SetOperations<String,String> setOperations = redisTemplate.opsForSet();
        String substring = id.substring(0, 16);//判断为订单消息
        if (ack) {
            log.info("交换机已经收到 id 为:{}的消息", id);
            //收到就删除redis的订单缓存
            redisTemplate.delete(id);
        } else {

            log.info("交换机还未收到 id 为:{}消息,由于原因:{}", id, cause);
            if(substring.equals(BackupExchange.EXCHANGE_BACKUPS.getValue())){
                setOperations.add(BackupExchange.EXCHANGE_ORDER1.getValue(),id);
            }

        }
    }
    //当消息无法路由的时候的回调方法
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String
            exchange, String routingKey) {
        log.error(" 消 息 {}, 被交换机 {} 退回，退回原因 :{}, 路 由 key:{}",new String(message.getBody()),exchange,replyText,routingKey);
        if(ConfirmConfig.CONFIRM_EXCHANGE_NAME.equals(exchange)){ //判断为订单则保存
            SetOperations<String,Map> setOperations = redisTemplate.opsForSet();
            Map map=new ConcurrentHashMap();
            map.put("message",message.getBody());
            map.put("exchange",exchange);
            map.put("routingKey",routingKey);
            setOperations.add(BackQueue.QUEUE_ORDER.getValue(),map);
        }

    }
}*/
