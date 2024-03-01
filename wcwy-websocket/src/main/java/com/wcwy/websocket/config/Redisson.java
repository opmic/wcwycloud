package com.wcwy.websocket.config;

import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * ClassName: Redisson
 * Description:
 * date: 2022/11/14 14:43
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Configuration
public class Redisson {
    // redission通过redissonClient对象使用 // 如果是多个redis集群，可以配置
    @Value("${redisson.address}")
    public String address;
    @Value("${redisson.password}")
    public String password;
    @Bean
    public RedissonClient redissonClient(){
        Config config = new Config();
        // 创建单例模式的配置
        if(StringUtils.isEmpty(password)){
            config.useSingleServer().setAddress(address).setDatabase(5);
        }else {
            config.useSingleServer().setAddress(address).setPassword(password).setDatabase(6).setTimeout(1000000000);
        }
        return org.redisson.Redisson.create(config);
    }
}
