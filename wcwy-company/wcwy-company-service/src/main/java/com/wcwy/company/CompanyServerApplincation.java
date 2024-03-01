package com.wcwy.company;

//import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.apache.rocketmq.client.log.ClientLogger;
import org.dromara.dynamictp.core.spring.EnableDynamicTp;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @ClassName: CompanyServerApplincation
 * @Description: TODO
 * @Author: tangzhuo
 * @Date: 2022-08-25 14:14
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.wcwy.post"})
@MapperScan(basePackages = {"com.wcwy.company.mapper","com.wcwy.common","com.wcwy.system.mapper"})
@EnableAsync
@ComponentScan(basePackages ={"com.wcwy.common.config","com.wcwy.company","com.wcwy.system"})
@EnableScheduling
//@EnableAdminServer
@EnableDynamicTp
@EnableCaching
public class CompanyServerApplincation {

    public static void main(String[] args) {
        System.setProperty(ClientLogger.CLIENT_LOG_USESLF4J,"true");
        //设置metric.log / block.log / sentinel-record.log 等日志目录
        System.setProperty("csp.sentinel.log.dir", "/home/logs/csp");

//设置eagleeye-self.log目录
        System.setProperty("EAGLEEYE.LOG.PATH", "/home/logs/eagleeye");

        SpringApplication.run(CompanyServerApplincation.class,args);
    }
}
