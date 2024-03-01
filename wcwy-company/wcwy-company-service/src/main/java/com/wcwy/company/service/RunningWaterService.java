package com.wcwy.company.service;

import com.wcwy.company.entity.RunningWater;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;

import java.util.concurrent.Future;

/**
* @author Administrator
* @description 针对表【running_water(无忧币和金币流水账表)】的数据库操作Service
* @createDate 2023-06-30 14:35:00
*/
public interface RunningWaterService extends IService<RunningWater> {


     void insert(RunningWater runningWater);


     void asyncMethod();
     Future<String> asyncMethodWithResult(String a) throws InterruptedException;
}
