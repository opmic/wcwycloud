package com.wcwy.company.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wcwy.common.redis.enums.Cache;
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.company.entity.InterviewResume;
import com.wcwy.company.service.CooTribeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * ClassName: CooTribeTask
 * Description:
 * date: 2024/1/19 15:28
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Slf4j
@Component
public class CooTribeTask {
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private ThreadPoolExecutor dtpExecutor1;
    @Autowired
    private CooTribeService cooTribeService;

    /**
     * 分享
     */
    @Scheduled(cron = "0 */1 * * * ?")//每天10分钟执行一次
    public void updateTribeShare(){
        Set<Object> set = redisUtils.sGet(Cache.CACHE_RECORD_SHARE_TRIBE.getKey());
        if(set==null){
            return;
        }
        Iterator<Object> iterator = set.iterator();
        while (iterator.hasNext()){
            Object next = iterator.next();
            dtpExecutor1.execute(()->{
                long l = redisUtils.incr1(Cache.CACHE_SHARE_TRIBE.getKey() +next ,0);
                UpdateWrapper  queryWrapper =new UpdateWrapper();
                queryWrapper.eq("id",next);
                queryWrapper.set("share",l);
                cooTribeService.update(queryWrapper);
                redisUtils.setRemove(Cache.CACHE_RECORD_SHARE_TRIBE.getKey(),next);
            });
        }

    }
    /**
     * 点赞
     */
    @Scheduled(cron = "0 */1 * * * ?")//每天10分钟执行一次
    public void updateTribeZan(){
        Set<Object> set = redisUtils.sGet(Cache.CACHE_RECORD_ZAN_TRIBE.getKey());
        if(set==null){
            return;
        }
        Iterator<Object> iterator = set.iterator();
        while (iterator.hasNext()){
            Object next = iterator.next();
            dtpExecutor1.execute(()->{
                 long l = redisUtils.sGetSetSize(Cache.CACHE_ZAN_TRIBE.getKey() + next);
                UpdateWrapper  queryWrapper =new UpdateWrapper();
                queryWrapper.eq("id",next);
                queryWrapper.set("zan",l);
                cooTribeService.update(queryWrapper);
                redisUtils.setRemove(Cache.CACHE_RECORD_ZAN_TRIBE.getKey(),next);
                return;
            });

        }

    }
    /**
     * 浏览
     */
    @Scheduled(cron = "0 */1 * * * ?")//每天10分钟执行一次
    public void updateTribeBrowse(){
        Set<Object> set = redisUtils.sGet(Cache.CACHE_RECORD_TRIBE_BROWSE.getKey());
        if(set==null){
            return;
        }
        Iterator<Object> iterator = set.iterator();
        while (iterator.hasNext()){
            Object next = iterator.next();
            dtpExecutor1.execute(()->{
                long incr = redisUtils.incr1(Cache.CACHE_TRIBE_BROWSE.getKey() + next, 0);
                UpdateWrapper  queryWrapper =new UpdateWrapper();
                queryWrapper.eq("id",next);
                queryWrapper.set("browse",incr);
                cooTribeService.update(queryWrapper);
                redisUtils.setRemove(Cache.CACHE_RECORD_TRIBE_BROWSE.getKey(),next);
            });

        }
    }

    /**
     * 评论
     */
    @Scheduled(cron = "0/10 * * * * ?")
    public void comment(){
        Set<Object> objects = redisUtils.sGet(Cache.CACHE_RECORD_COMMENT_TRIBE.getKey());
        if(objects==null){
            return;
        }
        Iterator<Object> iterator = objects.iterator();
        if(iterator.hasNext()){
            Object next = iterator.next();
            dtpExecutor1.execute(()->{
                long incr = redisUtils.incr1(Cache.CACHE_COMMENT_TRIBE.getKey() + next, 0);
                UpdateWrapper updateWrapper=new UpdateWrapper();
                updateWrapper.eq("id",next);
                updateWrapper.set("comment",incr);
                cooTribeService.update(updateWrapper);
                redisUtils.setRemove(Cache.CACHE_RECORD_COMMENT_TRIBE.getKey(),next);
            });
        }
    }

    /**
     * 收藏
     */
    @Scheduled(cron = "0/10 * * * * ?")
    public void collect(){
        Set<Object> objects = redisUtils.sGet(Cache.CACHE_RECORD_COLLECT_TRIBE.getKey());
        if(objects==null){
            return;
        }
        Iterator<Object> iterator = objects.iterator();
        if(iterator.hasNext()){
            Object next = iterator.next();
            dtpExecutor1.execute(()->{
                long incr = redisUtils.incr1(Cache.CACHE_COLLECT_TRIBE.getKey() + next, 0);
                UpdateWrapper updateWrapper=new UpdateWrapper();
                updateWrapper.eq("id",next);
                updateWrapper.set("collect",incr);
                cooTribeService.update(updateWrapper);
                redisUtils.setRemove(Cache.CACHE_RECORD_COLLECT_TRIBE.getKey(),next);
            });
        }
    }

}
