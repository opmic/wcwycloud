package com.wcwy.company.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wcwy.common.base.enums.Gold;
import com.wcwy.common.base.enums.GoldExplain;
import com.wcwy.common.redis.enums.RedisCache;
import com.wcwy.common.redis.util.IDGenerator;
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.company.entity.RunningWater;
import com.wcwy.company.service.RunningWaterService;
import com.wcwy.company.mapper.RunningWaterMapper;
import com.wcwy.company.service.TCompanyService;
import com.wcwy.company.service.TRecommendService;
import com.wcwy.post.entity.GoldConfig;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Future;

/**
 * @author Administrator
 * @description 针对表【running_water(无忧币和金币流水账表)】的数据库操作Service实现
 * @createDate 2023-06-30 14:35:00
 */
@Service
public class RunningWaterServiceImpl extends ServiceImpl<RunningWaterMapper, RunningWater>
        implements RunningWaterService {
    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private TCompanyService tCompanyService;

    @Autowired
    private IDGenerator idGenerator;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private RunningWaterMapper runningWaterMapper;

    @Override
    public void insert(RunningWater runningWater) {
        this.save(runningWater);
    }


    @Override
    @Async
    public void asyncMethod() {
        List<RunningWater> list = this.list();
        for (RunningWater runningWater : list) {
            System.out.println(runningWater);
        }
    }

    @Override
    @Async()
    public Future<String> asyncMethodWithResult(String a) throws InterruptedException {
        List<RunningWater> list = runningWaterMapper.selectList(null);
        for (RunningWater runningWater : list) {
            System.out.println(runningWater);

        }
        System.out.println(a);
        List<RunningWater> list1 = runningWaterMapper.selectList(null);
        return new AsyncResult<>("result");
    }

}




