/*
package com.wcwy.post.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

*/
/**
 * ClassName: MyCallBack
 * Description:
 * date: 2022/9/19 14:21
 *
 * @author tangzhuo
 * @since JDK 1.8
 *//*

@Component
@Slf4j
public class MyCallBack implements RabbitTemplate.ConfirmCallback ,RabbitTemplate.ReturnCallback{

    @Autowired
    private RabbitTemplate rabbitTemplate;

    //rabbitTemplate 注入之后就设置该值
    @PostConstruct
    private void init() {
        rabbitTemplate.setConfirmCallback(this);
        */
/**
         * true：
         * 交换机无法将消息进行路由时，会将该消息返回给生产者
         * false：
         * 如果发现消息无法进行路由，则直接丢弃
         *//*

        rabbitTemplate.setMandatory(true);
        //设置回退消息交给谁处理
        rabbitTemplate.setReturnCallback(this);
    }
    */
/**
     * 交换机不管是否收到消息的一个回调方法
     * CorrelationData
     * 消息相关数据
     * ack
     * 交换机是否收到消息
     *//*

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData != null ? correlationData.getId() : "";
        if (ack) {
            log.info("交换机已经收到 id 为:{}的消息", id);
        } else {
            log.info("交换机还未收到 id 为:{}消息,由于原因:{}", id, cause);
        }
    }
    //当消息无法路由的时候的回调方法
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String
            exchange, String routingKey) {
        log.error(" 消 息 {}, 被交换机 {} 退回，退回原因 :{}, 路 由 key:{}",new String(message.getBody()),exchange,replyText,routingKey);
    }
}
*/
