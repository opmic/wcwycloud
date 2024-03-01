package com.wcwy.post.consumer;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wcwy.common.base.utils.ScaleUtil;
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.common.utils.PostShare;
import com.wcwy.common.utils.TCompany;
import com.wcwy.common.utils.UpdateTCompanyPost;
import com.wcwy.company.vo.TCompanyUpdateVo;
import com.wcwy.post.service.TCompanyHotService;
import com.wcwy.post.service.TCompanyPostService;
import com.wcwy.system.annotation.RocketLog;
import lombok.SneakyThrows;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * ClassName: UpdateTCompanyPostProduceConsumer
 * Description:
 * date: 2023/3/23 17:29
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@RocketMQMessageListener(topic = UpdateTCompanyPost.UPDATE_COMPANY_TOPIC,consumerGroup = UpdateTCompanyPost.UPDATE_COMPANY_GROUP)// 顺序接受
@Component
public class UpdateTCompanyPostProduceConsumer implements RocketMQListener<String> {
    @Autowired
    private TCompanyPostService tCompanyPostService;
    @Autowired
    private TCompanyHotService tCompanyHotService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedisUtils redisUtils;
    @SneakyThrows
    @Override
    @RocketLog(title = "更新岗位基础信息", businessType =1)
    public void onMessage(String message) {
        Map map1 = JSON.parseObject(message, Map.class);
      try {
          TCompanyUpdateVo map = JSON.parseObject((String) map1.get("data"), TCompanyUpdateVo.class);
          ValueOperations valueOperations = redisTemplate.opsForValue();
          Boolean uuid = valueOperations.setIfAbsent(UpdateTCompanyPost.UPDATE_COMPANY_GROUP+ map1.get("uuid"), map1.get("uuid"));
          if(! uuid){
              return;
          }
          UpdateWrapper updateWrapper=new UpdateWrapper();
          updateWrapper.eq("company_id",map.getCompanyId());
          updateWrapper.set("logo",map.getLogoPath());
          updateWrapper.set("company_type_id",map.getCompanyTypeId());
          updateWrapper.set("firm_size", ScaleUtil.screen(map.getFirmSize()));
          updateWrapper.set("company_type",map.getCompanyType());
          boolean update = tCompanyPostService.update(updateWrapper);
          boolean update1 = tCompanyHotService.updateCompany(map);
          if(!update){
              throw new Exception("新增失败!");

          }
      }catch (Exception e){
          redisUtils.del(UpdateTCompanyPost.UPDATE_COMPANY_GROUP+map1.get("uuid"));
          redisUtils.acceptSet(UpdateTCompanyPost.UPDATE_COMPANY_GROUP,message);
          throw e;
      }
    }
}
