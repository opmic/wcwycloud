/*
package com.wcwy.post.rabbitmq;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.rabbitmq.client.Channel;
import com.wcwy.common.redis.enums.Sole;
import com.wcwy.common.utils.AckMode;
import com.wcwy.post.entity.OrderInfo;
import com.wcwy.post.service.OrderInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

*/
/**
 * ClassName: ApplyForInvoiceConsumer
 * Description:
 * date: 2022/12/1 14:35
 *
 * @author tangzhuo
 * @since JDK 1.8
 *//*

@Component
@Slf4j
public class ApplyForInvoiceConsumer {
    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private RedisTemplate redisTemplate;
    @RabbitListener(queues = "apply_forInvoice_queue", ackMode = AckMode.MANUAL)
    public void UpdateOrderInfo(String msg, Channel channel, Message message) throws IOException {
        try {
            log.info(msg);
            Map map = JSON.parseObject(msg);
           List<String> list= JSON.parseArray(map.get("msg").toString(),String.class);
            String applyForInvoiceId = map.get("applyForInvoiceId").toString();
            SetOperations setOperations = redisTemplate.opsForSet();
            for (String s : list) {
                Boolean aBoolean = setOperations.isMember(Sole.UPDATE_ORDER_INVOICE.getKey(), s);
                if(! aBoolean){
                    OrderInfo byId = orderInfoService.getById(s);
                    if(byId==null){
                        continue;
                    }
                    UpdateWrapper updateWrapper=new UpdateWrapper();
                    updateWrapper.eq("order_id",s);
                    updateWrapper.set("invoice",applyForInvoiceId);
                    boolean update = orderInfoService.update(updateWrapper);
                    if(update){
                        setOperations.add(Sole.UPDATE_ORDER_INVOICE.getKey(),s);
                    }
                }
            }
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
        }
    }

}
*/
