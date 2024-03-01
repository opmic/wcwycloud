package com.wcwy.company.consumer;

import com.alibaba.fastjson.JSON;
import com.wcwy.common.redis.enums.Record;
import com.wcwy.common.redis.enums.Sole;
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.common.utils.EiCompanyPostMQ;
import com.wcwy.common.utils.TCompanyHotMQ;
import com.wcwy.company.entity.EiCompanyPost;
import com.wcwy.company.po.ProvincesCitiesPO;
import com.wcwy.company.service.EiCompanyPostService;
import com.wcwy.post.entity.TPostShare;
import com.wcwy.system.annotation.RocketLog;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Map;

/**
 * ClassName: EiCompanyPostConsumer
 * Description:
 * date: 2023/3/30 13:53
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@RocketMQMessageListener(topic = EiCompanyPostMQ.TOPIC, consumerGroup = EiCompanyPostMQ.GROUP)
@Component
@Slf4j
public class EiCompanyPostConsumer implements RocketMQListener<String> {

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private EiCompanyPostService eiCompanyPostService;
    @SneakyThrows
    @Override
    @RocketLog(title = "岗位基础表", businessType =1)
    public void onMessage(String msg) {
        Map map = JSON.parseObject(msg, Map.class);
        try {
            Boolean uuid = redisUtils.setIfAbsent(EiCompanyPostMQ.GROUP+":"+map.get("uuid"), map.get("uuid").toString(),24);
            if(! uuid){
                return;
            }
       /*     EiCompanyPost post_id = eiCompanyPostService.getById((String)map.get("post_id"));
            if(post_id !=null){
                return;
            }*/
            EiCompanyPost eiCompanyPost =new EiCompanyPost();
            eiCompanyPost.setPostId((String) map.get("post_id"));
            eiCompanyPost.setPostLabel((String) map.get("post_label"));
            eiCompanyPost.setCompanyId((String) map.get("company_id"));
            //根据类型转化
            if( map.get("begin_salary") instanceof  Integer){
                eiCompanyPost.setBeginSalary(new BigDecimal((Integer) map.get("begin_salary")));
            }else if( map.get("begin_salary") instanceof  BigDecimal){
                eiCompanyPost.setBeginSalary((BigDecimal) map.get("begin_salary"));
            }else if( map.get("begin_salary") instanceof  Double){
                eiCompanyPost.setBeginSalary(new BigDecimal((Double) map.get("begin_salary")));
            }
            if( map.get("end_salary") instanceof  Integer){
                eiCompanyPost.setEndSalary(new BigDecimal((Integer) map.get("end_salary")));
            }else if( map.get("end_salary") instanceof  BigDecimal){
                eiCompanyPost.setEndSalary((BigDecimal) map.get("end_salary"));
            }else if( map.get("end_salary") instanceof  Double){
                eiCompanyPost.setEndSalary(new BigDecimal((Double) map.get("end_salary")));
            }
            if(!StringUtils.isEmpty(map.get("company_name"))){
                eiCompanyPost.setCompanyName(map.get("company_name").toString());
            }
            eiCompanyPost.setWorkCity(JSON.parseObject(map.get("work_city").toString(),ProvincesCitiesPO.class) );
      /*      if( map.get("hired_bounty") instanceof  Integer){
                eiCompanyPost.setHiredBounty(new BigDecimal((Integer) map.get("hired_bounty")));
            }else if( map.get("hired_bounty") instanceof  BigDecimal){
                eiCompanyPost.setHiredBounty((BigDecimal) map.get("hired_bounty"));
            }else if( map.get("hired_bounty") instanceof  Double){
                eiCompanyPost.setHiredBounty(new BigDecimal((Double) map.get("hired_bounty")));
            }*/

            //eiCompanyPost.setHiredBounty( new BigDecimal((Integer) map.get("hired_bounty")));
          //  eiCompanyPost.setMoneyReward(map.get("money_reward"));
            eiCompanyPost.setPostType((Integer) map.get("post_type"));
           /* boolean save = eiCompanyPostService.saveOrUpdate(eiCompanyPost);*/
             boolean save = eiCompanyPostService.saveOrUpdate(eiCompanyPost);
            if(!save){
                redisUtils.del(EiCompanyPostMQ.GROUP+":"+map.get("uuid"));
                redisUtils.acceptSet(EiCompanyPostMQ.GROUP,msg);
                throw new Exception("添加失败!");
            }
        }catch (Exception e){
            redisUtils.del(EiCompanyPostMQ.GROUP+":"+map.get("uuid"));
            redisUtils.acceptSet(EiCompanyPostMQ.GROUP,msg);
            throw  e;
        }

    }
}
