package com.wcwy.common.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ClassName: PostRecord
 * Description:
 * date: 2022/11/11 9:24
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Configuration
public class PostRecord {
    public static final String CONFIRM_POST_RECORD_EXCHANGE_NAME = "postRecord.exchange";
    public static final String CONFIRM_POST_RECORD_QUEUE_NAME = "postRecord.queue";
    public static final String BACKUP_POST_RECORD_EXCHANGE_NAME = "backupPostRecord.exchange";
    public static final String BACK_POST_RECORD_QUEUE = "backupPostRecord.queue";

    // 声明确认队列
    @Bean("postRecordQueue")
    public Queue confirmQueue() {
        return QueueBuilder.durable(CONFIRM_POST_RECORD_QUEUE_NAME).build();
    }

    //声明确认队列绑定关系
    @Bean
    public Binding queueBinding(@Qualifier("postRecordQueue") Queue queue,
                                @Qualifier("postRecordExchange") DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("postRecord");
    }

    //声明备份 Exchange
    @Bean("backupPostRecordExchange")
    public FanoutExchange backupExchange() {
        return new FanoutExchange(BACKUP_POST_RECORD_EXCHANGE_NAME);
    }

    //声明确认 Exchange 交换机的备份交换机
    @Bean("postRecordExchange")
    public DirectExchange confirmExchangeBackups() {
        ExchangeBuilder exchangeBuilder =
                ExchangeBuilder.directExchange(CONFIRM_POST_RECORD_EXCHANGE_NAME)
                        .durable(true)
                        //设置该交换机的备份交换机
                        .withArgument("alternate-exchange", BACKUP_POST_RECORD_EXCHANGE_NAME);
        return (DirectExchange) exchangeBuilder.build();
    }


    // 声明备份队列
    @Bean("backupPostRecordQueue")
    public Queue backQueue() {
        return QueueBuilder.durable(BACK_POST_RECORD_QUEUE).build();
    }

    // 声明备份队列绑定关系
    @Bean
    public Binding backupBinding(@Qualifier("backupPostRecordQueue") Queue queue,
                                 @Qualifier("backupPostRecordExchange") FanoutExchange backupExchange) {
        return BindingBuilder.bind(queue).to(backupExchange);
    }
}
