package com.wcwy.post.consumer;

import com.alibaba.fastjson.JSONObject;
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.common.utils.TCompany;
import com.wcwy.company.po.TIndustryAndTypePO;
import com.wcwy.post.entity.TCompanyHot;
import com.wcwy.post.service.TCompanyHotService;
import com.wcwy.system.annotation.RocketLog;
import lombok.SneakyThrows;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * ClassName: TestConsumer
 * Description:
 * date: 2023/6/27 17:13
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@RocketMQMessageListener(topic = "test_topic",consumerGroup = "test_group")// 顺序接受
@Component
public class TestConsumer implements RocketMQListener<String> {


    @Override
    @RocketLog(title = "测试消费", businessType =1)
    public void onMessage(String s) {
        System.out.println(s);

    }
}
