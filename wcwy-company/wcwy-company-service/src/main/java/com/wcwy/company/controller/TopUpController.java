package com.wcwy.company.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wcwy.common.base.enums.Lock;
import com.wcwy.common.redis.util.IDGenerator;
import com.wcwy.company.entity.RunningWater;
import com.wcwy.company.entity.TCompany;
import com.wcwy.company.entity.TRecommend;
import com.wcwy.company.service.RunningWaterService;
import com.wcwy.company.service.TCompanyService;
import com.wcwy.company.service.TRecommendService;
import com.wcwy.company.vo.TopUp;
import com.wcwy.system.annotation.Log;
import com.wcwy.system.enums.BusinessType;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * ClassName: TopUpController
 * Description:
 * date: 2022/10/17 15:17
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@RestController
@RequestMapping("/topUp")
@Slf4j
public class TopUpController {
    @Resource
    private TRecommendService tRecommendService;
    @Autowired
    private IDGenerator idGenerator;
    @Autowired
    private RedissonClient redissonClient;
    @Resource
    private TCompanyService tCompanyService;

    @Autowired
    private RunningWaterService runningWaterService;

    /**
     * @param topUp
     * @return null
     * @Description: 更新 及记录无忧币接口
     * @Author tangzhuo
     * @CreateTime 2022/10/17 15:59
     */

    @PostMapping("/updateCurrencyCount")
    @Log(title = "更新及记录无忧币接口", businessType = BusinessType.UPDATE)
    public Boolean updateCurrencyCount(@RequestBody TopUp topUp) throws InterruptedException {
        // log.info("更行充值流水");
        RLock lock = redissonClient.getLock(Lock.DEDUCT_EXPENSES.getLockSion() + topUp.getUserId());
        boolean tryLock = lock.tryLock(5, 20, TimeUnit.SECONDS); //尝试加锁，最多等待1秒，上锁以后10秒自动解锁
        try {
            if (tryLock) {
                RunningWater runningWater = new RunningWater();
                Boolean isOk = false;
                if (true) {
                    String substring = topUp.getUserId().substring(0, 2);
                    //推荐官
                    if ("TR".equals(substring)) {
                        //当用户不存在直接返回true
             /*   QueryWrapper queryWrapper=new QueryWrapper();
                queryWrapper.eq("id",topUp.getUserId());*/
                        TRecommend byId = tRecommendService.getById(topUp.getUserId());
                        if (byId == null) {
                            isOk = false;
                        }
                        UpdateWrapper updateWrapper = new UpdateWrapper();
                        updateWrapper.eq("id", topUp.getUserId());
                        String money = "currency_count=currency_count+%s";
                        updateWrapper.setSql(String.format(money, topUp.getMoney()));
                        boolean update = tRecommendService.update(updateWrapper);
                        if (update) {
                            runningWater.setRemainingSum(byId.getCurrencyCount().add(topUp.getMoney()));
                            isOk = true;
                        }
                    } else if ("TC".equals(substring)) {
                        //当用户不存在直接返回true
               /* QueryWrapper queryWrapper=new QueryWrapper();
                queryWrapper.eq("company_id",topUp.getUserId());*/
                        TCompany byId = tCompanyService.getById(topUp.getUserId());
                        if (byId == null) {
                            isOk = false;
                        }
                        UpdateWrapper updateWrapper = new UpdateWrapper();
                        updateWrapper.eq("company_id", topUp.getUserId());
                        String money = "currency_count=currency_count+%s";
                        updateWrapper.setSql(String.format(money, topUp.getMoney()));
                        boolean update = tCompanyService.update(updateWrapper);
                        if (update) {
                            runningWater.setRemainingSum(byId.getCurrencyCount().add(topUp.getMoney()));
                            isOk = true;
                        }
                    } else {
                        log.error("更新充值接口未找到用户");
                    }
                }

                if (isOk) {
                    runningWater.setRunningWaterId(idGenerator.generateCode("RW"));
                    runningWater.setSource(1);
                    runningWater.setType(0);
                    runningWater.setMoney(topUp.getMoney());
                    runningWater.setUserId(topUp.getUserId());
                    runningWater.setIfIncome(2);
                    runningWater.setOrderId(topUp.getOrder());
                    runningWater.setInstructions("充值，交易订单号" + topUp.getOrder());
                    runningWater.setCrateTime(LocalDateTime.now());
                    runningWaterService.insert(runningWater);
                }
                return isOk;

            }
        } finally {
            lock.unlock();
        }
        return null;
    }


}
