package com.wcwy.post.task;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wcwy.post.entity.TCompanyPost;
import com.wcwy.post.service.TCompanyPostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

/**
 * ClassName: TCompanyPostTask
 * Description:岗位定时任务
 * date: 2022/9/16 15:42
 *
 * @author tangzhuo
 * @since JDK 1.8
 */

@Configuration
@Slf4j
public class TCompanyPostTask {
    /**
     * @Description:定时关闭岗位
     * @return null
     * @Author tangzhuo
     * @CreateTime 2022/9/16 15:43
     */
    @Autowired
   private TCompanyPostService tCompanyPostService;


     // @Scheduled(cron = "0/5 * * * * ?")
  //   @Scheduled(cron = "0 0 1 * * ?")//每天凌晨1点执行一次
    private void configureTasks() {
        log.info("执行定时关闭岗位任务=======================");
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("status",1);
        queryWrapper.le("expiration_date", LocalDate.now());
        List<TCompanyPost> list = tCompanyPostService.list(queryWrapper);
        for (TCompanyPost tCompanyPost : list) {
            if(tCompanyPost.getExpirationDate()==null){
                continue;
            }
           if( LocalDate.now().isAfter(tCompanyPost.getExpirationDate())){
               UpdateWrapper updateWrapper=new UpdateWrapper();
               updateWrapper.eq("post_id",tCompanyPost.getPostId());
               updateWrapper.set("status",0);
               log.info("岗位id为"+tCompanyPost.getPostId()+"已被停止");
               boolean update = tCompanyPostService.update(updateWrapper);
               if(!update){
                   log.error("岗位id为"+tCompanyPost.getPostId()+"已被停止异常");
               }
           }
        }
    }
    /**
     * @Description: 添加天数
     * @return null
     * @Author tangzhuo
     * @CreateTime 2023/1/5 8:35
     */

    @Scheduled(cron = "0 0 1 * * ?")//每天凌晨1点执行一次
  //  @Scheduled(cron = "0/5 * * * * ?")
   // @Scheduled(cron = "0 0 0,13,18,21 * * ? ")
    private void updatePostTasks() {
       QueryWrapper queryWrapper=new QueryWrapper();
       queryWrapper.eq("status",1);
        List<TCompanyPost> list = tCompanyPostService.list(queryWrapper);
        for (TCompanyPost tCompanyPost : list) {
            tCompanyPost.setDay(tCompanyPost.getDay()+1);
            tCompanyPostService.updateById(tCompanyPost);
        }
    }
    @Scheduled(cron = "0 0 1 * * ?")//每天凌晨1点执行一次
    //  @Scheduled(cron = "0/5 * * * * ?")
    // @Scheduled(cron = "0 0 0,13,18,21 * * ? ")
    private void updatePostCreate() {
        QueryWrapper queryWrapper=new QueryWrapper();
        Date today = new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24);
        queryWrapper.le("create_time",today);
        List<TCompanyPost> list = tCompanyPostService.list(queryWrapper);
        for (TCompanyPost tCompanyPost : list) {
            tCompanyPost.setCreateTime(tCompanyPost.getCreateTime().plusDays(1));
            tCompanyPostService.updateById(tCompanyPost);
        }
    }
}
