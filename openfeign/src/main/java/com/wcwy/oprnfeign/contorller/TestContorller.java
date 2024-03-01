package com.wcwy.oprnfeign.contorller;


import com.wcwy.oprnfeign.entiy.SchoolQuery;
import com.wcwy.oprnfeign.service.TCompanyApi;
import com.wcwy.oprnfeign.service.TCompanyLoginApi;
import com.wcwy.oprnfeign.service.TestService;
import com.wcwy.oprnfeign.service.WorkApi;
import org.checkerframework.checker.units.qual.A;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName: TestContorller
 * @Description: TODO
 * @Author: tangzhuo
 * @Date: 2022-08-26 9:42
 */
@RestController
public class TestContorller {

    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    TestService testService;
    @Autowired
    WorkApi WorkApi;
    @GetMapping("/test")
    public Object test(){
        Object tz = testService.UserRole("tz");
        return tz;
    }
    @GetMapping("/test1")
    public Object test1(){
        Object tz = testService.selectByid(1L);
        return tz;
    }

    @GetMapping("/testcs")
    public String test1c() throws InterruptedException {
        RLock cc = redissonClient.getLock("cc");
        boolean  tryLock= cc.tryLock(5, 500, TimeUnit.SECONDS);
        String tz="";
        try {
            System.out.println("获取锁"+tryLock);
            if(tryLock){
                tz  = testService.testc();
            }
            return tz;
        }finally {
            cc.unlock();
        }
    }
    @GetMapping("/testRLock")
    public String testRLock() throws InterruptedException {
        RLock cc = redissonClient.getLock("cc");
        boolean  tryLock= cc.tryLock(5, 500, TimeUnit.SECONDS);
        String tz="";
        try {
            System.out.println("获取锁"+tryLock);
            if(tryLock){
                tz  = testService.RLock(cc);
            }
            return tz;
        }finally {
            cc.unlock();
        }
    }
    @Autowired
    TCompanyLoginApi tCompanyLoginApi;

    @Autowired
    TCompanyApi tCompanyApi;
    @GetMapping("/testRccc")
    public String testRccc() throws InterruptedException {
      /*  String cc = WorkApi.cc("cc");*/
  /*      String cc1 = tCompanyLoginApi.cc();
        String cc = tCompanyApi.cc();*/
        SchoolQuery schoolQuery=new SchoolQuery();
        schoolQuery.setId("111");
        schoolQuery.setName("12312321");
        String asc = tCompanyApi.asc(schoolQuery);
        return asc;
    }
}
