package com.wcwy.websocket;

import org.dromara.dynamictp.core.spring.EnableDynamicTp;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.tio.websocket.starter.EnableTioWebSocketServer;

/**
 * ClassName: WebsocketApplication
 * Description:
 * date: 2023/3/27 14:30
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@EnableDiscoveryClient
@SpringBootApplication
@EnableDynamicTp
@EnableAsync
@MapperScan(basePackages = {"com.wcwy.websocket.mapper"})
@EnableFeignClients(basePackages = {"com.wawy.company"})
//@ComponentScan(basePackages ={"com.wcwy.websocket"})
@EnableCaching
@EnableTioWebSocketServer
public class WebsocketApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebsocketApplication.class, args);
    }

}
