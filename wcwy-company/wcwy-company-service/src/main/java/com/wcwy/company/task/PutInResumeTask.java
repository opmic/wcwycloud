package com.wcwy.company.task;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wcwy.common.base.result.R;
import com.wcwy.common.redis.enums.BackQueue;
import com.wcwy.common.redis.enums.BackupExchange;
import com.wcwy.common.redis.enums.Sole;
import com.wcwy.company.dto.InviteEntryPutInResume;
import com.wcwy.company.dto.PutInResumeDTO;
import com.wcwy.company.entity.PutInResume;
import com.wcwy.company.produce.PostOrderProduce;
import com.wcwy.company.service.*;
import lombok.extern.slf4j.Slf4j;
/*import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;*/
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 * @description 针对表【put_in_resume(投递简历表)】的数据库操作Service
 * @createDate 2022-10-13 17:15:29
 */

@Slf4j
@Component
public class PutInResumeTask {
    @Resource
    private PutInResumeService putInResumeService;
    @Autowired
    private InviteEntryService inviteEntryService;
    @Autowired
    private RedisTemplate redisTemplate;
    /*    @Autowired
        private RabbitTemplate rabbitTemplate;*/
    @Autowired
    private PostOrderProduce postOrderProduce;


    /*//确认入职产生订单
    @Scheduled(cron = "0 0 12 * * ?")//每天中午12点触发
    //@Scheduled(cron = "0/5 * * * * ?")
    public void createOrder() {
        List<InviteEntryPutInResume> list = inviteEntryService.selectList();
        for (InviteEntryPutInResume inviteEntry : list) {
            //保存在set集合里面做数据持久化
            //更新投简记录
            this.updatePutInResume(inviteEntry.getPutInResumeId(), inviteEntry.getEntryTime());
            //生成订单
            //调用入职申请记录
            CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
                putInResumeService.updateRedisPost(inviteEntry.getPutInJobhunter(), inviteEntry.getPutInPost(), 8, inviteEntry.getPutInUser());
                return null;
            });
            Map map=new ConcurrentHashMap();
            map.put("post_id",inviteEntry.getPutInPost());//岗位
            map.put("entryTime",inviteEntry.getEntryTime());//入职时间
            map.put("put_in_resume_id",inviteEntry.getPutInResumeId());//投简
            map.put("referrer_id",inviteEntry.getPutInUser());//推荐人id
            map.put("create_id",inviteEntry.getPutInComppany());//企业id
            map.put("post_type",inviteEntry.getPostType());
           // map.put("referrer_id",inviteEntry.getPutInUser());//推荐人id
       *//*     map.put("post_type",inviteEntry.getPostType());*//*
            map.put("hired_bounty",inviteEntry.getHiredBounty());
            map.put("percentage",inviteEntry.getPercentage());
            map.put("inviteEntryId",inviteEntry.getInviteEntryId());
            map.put("putInJobhunter",inviteEntry.getPutInJobhunter());
            //满月付岗位
      *//*      if(inviteEntry.getPostType()==1){

            }*//*
            //入职付岗位
      *//*      if(inviteEntry.getPostType()==2){
                map.put("money_reward",inviteEntry.getMoneyReward());
            }*//*
            postOrderProduce.sendOrderlyMessage(JSON.toJSONString(map));
        }
    }
*/
    public void updatePutInResume(String putInResumeId, LocalDate entryTime) {
        //保证只修改一次
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Boolean aBoolean = valueOperations.setIfAbsent(Sole.UPDATE_PUT_IN_RESUME.getKey() + putInResumeId, putInResumeId, 60 * 60 * 24 * 2, TimeUnit.SECONDS);
        if (!aBoolean) {
            return;
        }
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("put_in_resume_id", putInResumeId);
        /* updateWrapper.ne("entry_if", 2);*/
        updateWrapper.set("entry_time", entryTime);
        updateWrapper.set("entry_if", 2);
        updateWrapper.set("resume_state", 9);
        updateWrapper.set("update_time",LocalDateTime.now());
        boolean update = putInResumeService.update(updateWrapper);
        if (!update) {
            log.error("定时更新投简{}更新失败", putInResumeId);
        }
    }

    //@Scheduled(cron = "0 0 1 * * ?")//每天凌晨1点执行一次
    //交换机发送给队列失败重新发送
  /*  @Scheduled(fixedDelay = 1000 * 60 * 60)//每隔1小时
    public void redisCreateOrderQueue() {
        SetOperations<String, Map> setOperations = redisTemplate.opsForSet();
        Set<Map> members = setOperations.members(BackQueue.QUEUE_ORDER.getValue());
        for (Map member : members) {
            String message = new String((byte[]) member.get("message"));
            InviteEntryPutInResume inviteEntryPutInResume = JSON.parseObject(message, InviteEntryPutInResume.class);
            CorrelationData correlationData = new CorrelationData(BackQueue.QUEUE_ORDER.getValue() + inviteEntryPutInResume.getInviteEntryId());
            String routingKey = (String) member.get("routingKey");
            String exchange = (String) member.get("exchange");
            //删除缓存
            setOperations.remove(BackQueue.QUEUE_ORDER.getValue(), member);
            rabbitTemplate.convertAndSend(exchange, routingKey, message, correlationData);
        }

    }*/

  /*  //发送给交换机失败重新发送
    @Scheduled(fixedDelay = 1000 * 60 * 60)//每隔1小时
    // @Scheduled(fixedDelay = 6000)
    public void redisCreateOrderExchange() {
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        ValueOperations<String, InviteEntryPutInResume> value = redisTemplate.opsForValue();
        Set<String> members = setOperations.members(BackupExchange.EXCHANGE_ORDER1.getValue());
        for (String member : members) {
            InviteEntryPutInResume inviteEntryPutInResume = value.get(member);
            CorrelationData correlationData = new CorrelationData(BackupExchange.EXCHANGE_BACKUPS.getValue() + inviteEntryPutInResume.getInviteEntryId());
            String text = JSON.toJSONString(inviteEntryPutInResume);
            //删除set集合里面的值
            setOperations.remove(BackupExchange.EXCHANGE_ORDER1.getValue(), member);
            rabbitTemplate.convertAndSend(inviteEntryPutInResume.getExchange(), inviteEntryPutInResume.getKey(), text, correlationData);
        }
    }*/


    //自动确认过保
  /*  @Scheduled(cron = "0 0 1 * * ?")//每天凌晨1点执行一次
    //@Scheduled(cron = "0/5 * * * * ?")
    public void affirmOverProtection() {
        List<PutInResumeDTO> list = putInResumeService.listInviteEntry();
        for (PutInResumeDTO putInResumeDTO : list) {
            LocalDateTime localDateTime = putInResumeDTO.getEntryTime().plusDays(putInResumeDTO.getWorkday());
            boolean before = localDateTime.isBefore(LocalDateTime.now());
            if (before) {
                UpdateWrapper updateWrapper = new UpdateWrapper();
                updateWrapper.eq("put_in_resume_id", putInResumeDTO.getPutInResumeId());
                updateWrapper.set("over_protection_if", 2);
                updateWrapper.set("over_protection_time", LocalDateTime.now());
                updateWrapper.set("resume_state", 9);
                boolean update = putInResumeService.update(updateWrapper);
            }
        }
    }*/
    //确认自动排除
    @Scheduled(cron = "0 0 1 * * ?")//每天凌晨1点执行一次
  //  @Scheduled(cron = "0/5 * * * * ?")
    public void inappropriate() {
        List<PutInResume> list = putInResumeService.inappropriate();
        for (PutInResume putInResume : list) {
            String putInComppany = putInResume.getPutInComppany().substring(0,2);
            if(putInComppany.equals("TR")){
                continue;
            }
            putInResume.setResumeState(3);
            putInResume.setExcludeTime(LocalDateTime.now());
            putInResume.setExcludeIf(2);
            putInResumeService.updateById(putInResume);
            CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
                putInResumeService.updateRedisPost(putInResume.getPutInJobhunter(), putInResume.getPutInPost(), 3, putInResume.getPutInUser());
                return null;
            });
        }
    }

}
