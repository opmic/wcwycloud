package com.wcwy.common.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ClassName: ApplyForInvoice
 * Description:
 * date: 2022/12/1 14:15
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Configuration
public class ApplyForInvoice {
    public static final String CONFIRM_EXCHANGE_NAME = "apply_forInvoice_exchange";
    public static final String CONFIRM_QUEUE_NAME = "apply_forInvoice_queue";
    @Bean(name = "applyForInvoiceQueue")
    public Queue confirmTestQueue() {
        return new Queue(CONFIRM_QUEUE_NAME, true, false, false);
    }

    @Bean("applyForInvoiceExchange")
    public Exchange getExchange()
    {
        return ExchangeBuilder
                .directExchange(CONFIRM_EXCHANGE_NAME)//交换机类型 ;参数为名字
                .durable(true)//是否持久化，true即存到磁盘,false只在内存上
                .build();
    }
    @Bean
    public Binding confirmTestFanoutExchangeAndQueue(
            @Qualifier("applyForInvoiceExchange") DirectExchange exchange,
            @Qualifier("applyForInvoiceQueue") Queue confirmTestQueue) {
        return BindingBuilder.bind(confirmTestQueue).to(exchange).with("invoice");
    }
}
