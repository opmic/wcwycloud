package com.wcwy.company.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wcwy.common.base.utils.DateUtils;
import com.wcwy.common.redis.enums.RedisCache;
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.company.entity.CompanyUserRole;
import com.wcwy.company.service.CompanyUserRoleService;
import com.wcwy.post.entity.TPayConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * ClassName: CompanyUserRoleTask
 * Description:
 * date: 2023/7/6 14:25
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Slf4j
@Component
public class CompanyUserRoleTask {
    @Autowired
    private CompanyUserRoleService companyUserRoleService;
    @Autowired
    private RedisUtils redisUtils;
    //交换机发送给队列失败重新发送
    @Scheduled(cron = "0 0 1 * * ?")//每天凌晨1点执行一次
  //  @Scheduled(fixedDelay = 1000 * 1)//每隔1小时
  //  @Scheduled(cron = "0/5 * * * * ?")
    public void delete() {
        TPayConfig tPayConfig = (TPayConfig) redisUtils.get(RedisCache.TPAYCONFIG.getValue());
        QueryWrapper queryWrapper=new QueryWrapper();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        //设置当前时间
        cal.setTime(new Date());
        cal.add(Calendar.DATE, -tPayConfig.getViewResumeTime());
        queryWrapper.le("create_time",format.format(cal.getTime()));
        queryWrapper.eq("source",0);
        List<CompanyUserRole> list = companyUserRoleService.list(queryWrapper);
        for (CompanyUserRole companyUserRole : list) {
            companyUserRoleService.removeById(companyUserRole.getCompanyUserRoleId());
        }

    }
}
