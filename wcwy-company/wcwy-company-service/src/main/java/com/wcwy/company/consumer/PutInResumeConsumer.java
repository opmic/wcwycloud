package com.wcwy.company.consumer;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wcwy.common.redis.enums.Sole;
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.common.utils.EiCompanyPostMQ;
import com.wcwy.common.utils.PutInResumeUtils;
import com.wcwy.company.entity.PutInResume;
import com.wcwy.company.service.PutInResumeService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * ClassName: PutInResumeProduce
 * Description:
 * date: 2022/12/7 16:21
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
//弃用
@Component
@Slf4j
@RocketMQMessageListener(topic = PutInResumeUtils.TOPIC,consumerGroup = PutInResumeUtils.GROUP,consumeMode = ConsumeMode.ORDERLY)// 顺序接受
public class PutInResumeConsumer implements RocketMQListener<String> {

    @Autowired
    private RedisTemplate redisTemplate;
    @Resource
    private PutInResumeService putInResumeService;
    @Resource
    private RedisUtils redisUtils;
    @SneakyThrows
    @Override
    public void onMessage(String s) {
      try {
          Map<String,String> map = JSON.parseObject(s, Map.class);
          String company_id = map.get("company_id");
          String user_id = map.get("user_id");
          String put_in_resume_id = map.get("put_in_resume_id");
          ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
          PutInResume byId = putInResumeService.getById(put_in_resume_id);
          UpdateWrapper updateWrapper = new UpdateWrapper();
          String post = valueOperations.get(Sole.PUT.getKey() + company_id + user_id);
          updateWrapper.eq("put_in_comppany", company_id);
          updateWrapper.eq("put_in_jobhunter", user_id);
          updateWrapper.eq("put_in_post", post);
          updateWrapper.eq("put_in_resume_id", put_in_resume_id);
          updateWrapper.set("download_if", 2);
          if(byId==null){
              return;
          }
          if (byId.getResumeState() < 2) {
              updateWrapper.set("resume_state", 2);
          }
          updateWrapper.set("download_time", LocalDateTime.now());
          boolean save = putInResumeService.update(updateWrapper);
          if (! save) {
              throw  new Exception("PutInResumeConsunmer投简id:"+put_in_resume_id+"企业id为"+company_id+"投简人为"+user_id);
          }
      }catch (Exception e){

      }

    }
}
