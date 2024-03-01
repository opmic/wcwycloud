package com.wcwy.post;

import org.apache.rocketmq.client.log.ClientLogger;
import org.dromara.dynamictp.core.spring.EnableDynamicTp;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * ClassName: PostServiceApplication
 * Description:
 * date: 2022/9/14 14:44
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.wawy.company"})
//@EnableFeignClients
@MapperScan(basePackages = {"com.wcwy.post.mapper","com.wcwy.system.mapper"})
@EnableScheduling
@ComponentScan(basePackages = {"com.wcwy.common.config", "com.wcwy.post","com.wcwy.system"})
@EnableDynamicTp
@EnableCaching
public class PostServiceApplication {
    public static void main(String[] args) {
        System.setProperty(ClientLogger.CLIENT_LOG_USESLF4J, "true");
        //设置metric.log / block.log / sentinel-record.log 等日志目录
        System.setProperty("csp.sentinel.log.dir", "/home/logs/csp");

//设置eagleeye-self.log目录
        System.setProperty("EAGLEEYE.LOG.PATH", "/home/logs/eagleeye");
        SpringApplication.run(PostServiceApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  启动成功   ლ(´ڡ`ლ)ﾞ  \n");

    }
}
