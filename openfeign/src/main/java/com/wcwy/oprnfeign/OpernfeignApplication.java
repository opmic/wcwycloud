package com.wcwy.oprnfeign;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @ClassName: OpernfeignApplication
 * @Description: TODO
 * @Author: tangzhuo
 * @Date: 2022-08-26 9:41
 */
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@EnableFeignClients
public class OpernfeignApplication {
    public static void main(String[] args) {
        SpringApplication.run(OpernfeignApplication.class,args);
    }
}
