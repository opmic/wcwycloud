package com.wcwy.common.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ClassName: ConfirmConfig
 * Description:
 * date: 2022/11/2 17:22
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Configuration
public class ConfirmConfig {
    public static final String CONFIRM_EXCHANGE_NAME = "confirm.exchange";
    public static final String CONFIRM_QUEUE_NAME = "confirm.queue";
  //  public static final String BACKUP_EXCHANGE_NAME = "backup.exchange";
   // public static final String BACKUP_QUEUE_NAME = "backup.queue";
   // public static final String WARNING_QUEUE_NAME = "warning.queue";
   // public static final String BACK_QUEUE = "backQueue";
  //声明确认 Exchange 交换机的备份交换机
  @Bean("confirmExchange")
  public DirectExchange confirmExchangeBackups() {
      ExchangeBuilder exchangeBuilder =
              ExchangeBuilder.directExchange(CONFIRM_EXCHANGE_NAME);
      return exchangeBuilder.build();
  }
    // 声明确认队列
    @Bean("confirmQueue")
    public Queue confirmQueue() {
        return QueueBuilder.durable(CONFIRM_QUEUE_NAME).build();
    }

    //声明确认队列绑定关系
    @Bean
    public Binding queueBinding(@Qualifier("confirmQueue") Queue queue,
                                @Qualifier("confirmExchange") DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("order");
    }

   /* //声明备份 Exchange
    @Bean("backupExchange")
    public FanoutExchange backupExchange() {
        return new FanoutExchange(BACKUP_EXCHANGE_NAME);
    }*/



  /*  // 声明警告队列
    @Bean("warningQueue")
    public Queue warningQueue() {
        return QueueBuilder.durable(WARNING_QUEUE_NAME).build();
    }*/

/*    // 声明报警队列绑定关系
    @Bean
    public Binding warningBinding(@Qualifier("warningQueue") Queue queue,
                                  @Qualifier("backupExchange") FanoutExchange
                                          backupExchange) {
        return BindingBuilder.bind(queue).to(backupExchange);
    }*/

/*    // 声明备份队列
    @Bean(BACK_QUEUE)
    public Queue backQueue() {
        return QueueBuilder.durable(BACKUP_QUEUE_NAME).build();
    }*/
/*
    // 声明备份队列绑定关系
    @Bean
    public Binding backupBinding(@Qualifier("backQueue") Queue queue,
                                 @Qualifier("backupExchange") FanoutExchange backupExchange) {
        return BindingBuilder.bind(queue).to(backupExchange);
    }*/
}
