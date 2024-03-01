package com.wcwy.post.task;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wawy.company.api.PutInResumeApi;
import com.wcwy.common.base.utils.DateUtils;
import com.wcwy.post.entity.CompanyUserRole;
import com.wcwy.post.produce.PutInResumeProduce;
import com.wcwy.post.service.CompanyUserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * ClassName: CompanyUserRoleController
 * Description:
 * date: 2022/10/17 13:48
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Configuration
@Slf4j
public class CompanyUserRoleTask {
    @Autowired
    private CompanyUserRoleService companyUserRoleService;
    /**
     * 从第0秒开始每隔30秒执行1次 查询30秒支付后开放权限更新投简记录
     */
    @Autowired
    private  PutInResumeApi putInResumeApi;
    @Autowired
    private PutInResumeProduce putInResumeProduce;
   // @Scheduled(cron = "0/30 * * * * ?")
    public void updatePutState() throws Exception {
        log.info("updatePutState 被执行......");
        QueryWrapper queryWrapper=new QueryWrapper();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = new GregorianCalendar();
        Date date = DateUtils.getTimeTwo();
        c.setTime(new Date());
        c.add(Calendar.SECOND,60);//更新两次
        Date date1=c.getTime();
        queryWrapper.between("create_time",df.format(date),df.format(date1) );
        List<CompanyUserRole> list = companyUserRoleService.list(queryWrapper);
        if(list.size()>0){
            for (CompanyUserRole companyUserRole : list) {
                Map map=new HashMap();
                map.put("company_id",companyUserRole.getCompanyId());
                map.put("user_id",companyUserRole.getUserId());
                map.put("put_in_resume_id",companyUserRole.getPutInResumeId());
                String toJSONString = JSON.toJSONString(map);
                putInResumeProduce.sendOrderlyMessage(toJSONString);
               // putInResumeApi.updateDownloadResume(companyUserRole.getCompanyId(),companyUserRole.getUserId(),companyUserRole.getPutInResumeId());
            }
        }
    }

}
