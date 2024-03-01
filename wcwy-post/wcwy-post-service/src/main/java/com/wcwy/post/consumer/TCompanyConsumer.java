package com.wcwy.post.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wcwy.common.redis.enums.Sole;
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.common.utils.PostShare;
import com.wcwy.common.utils.TCompany;
import com.wcwy.company.po.TIndustryAndTypePO;
import com.wcwy.post.entity.TCompanyHot;
import com.wcwy.post.service.TCompanyHotService;
import com.wcwy.system.annotation.RocketLog;
import lombok.SneakyThrows;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * ClassName: TCompanyConsumer
 * Description:
 * date: 2023/1/12 11:03
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@RocketMQMessageListener(topic = TCompany.TOPIC,consumerGroup = TCompany.GROUP)// 顺序接受
@Component
public class TCompanyConsumer implements RocketMQListener<String> {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private TCompanyHotService tCompanyHotService;
    @Autowired
    private RedisUtils redisUtils;
    @SneakyThrows
    @Override
    @RocketLog(title = "添加企业热度表", businessType =1)
    public void onMessage(String s) {
        SetOperations setOperations = redisTemplate.opsForSet();
        Map<String,Object> strMap= JSONObject.parseObject(s);
        Map<String,Object> strMap1= JSONObject.parseObject((String) strMap.get("data"));
        String uuid= (String) strMap.get("uuid");
        Boolean member = setOperations.isMember(TCompany.GROUP, uuid);
       try {
           if(member){
               return;
           }
           TCompanyHot tCompanyHot=new TCompanyHot();
           tCompanyHot.setDeleted(0);
           tCompanyHot.setHot(0L);
           tCompanyHot.setCompanyId((String) strMap1.get("company_id"));
           tCompanyHot.setLogo((String) strMap1.get("logo_path"));
           tCompanyHot.setCompanyName((String) strMap1.get("company_name"));
           tCompanyHot.setCompanyTypeId((String) strMap1.get("company_type_id"));
           tCompanyHot.setIndustry((List<TIndustryAndTypePO>) strMap1.get("industry"));
           tCompanyHot.setFirmSize((String) strMap1.get("firm_size"));
           boolean save = tCompanyHotService.save(tCompanyHot);
           if(! save){
               setOperations.remove(TCompany.GROUP, uuid);
               throw new Exception("新增失败!");
           }
       }catch (Exception e){
           setOperations.remove(TCompany.GROUP, uuid);
           redisUtils.acceptSet(TCompany.GROUP,s);
           throw e;
       }

    }
}
