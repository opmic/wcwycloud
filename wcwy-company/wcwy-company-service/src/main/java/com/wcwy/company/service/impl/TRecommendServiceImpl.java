package com.wcwy.company.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wcwy.common.base.enums.Lock;
import com.wcwy.common.redis.util.IDGenerator;
import com.wcwy.company.dto.RecommendDataDTO;
import com.wcwy.company.entity.RunningWater;
import com.wcwy.company.entity.TCompany;
import com.wcwy.company.entity.TPermission;
import com.wcwy.company.entity.TRecommend;
import com.wcwy.company.po.JobHunterShare;
import com.wcwy.company.po.TRecommendPO;
import com.wcwy.company.po.TRecommendShare;
import com.wcwy.company.query.ShareQuery;
import com.wcwy.company.query.TRecommendQuery;
import com.wcwy.company.service.RunningWaterService;
import com.wcwy.company.service.TRecommendService;
import com.wcwy.company.mapper.TRecommendMapper;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 * @description 针对表【t_recommend(推荐官)】的数据库操作Service实现
 * @createDate 2022-09-07 14:02:34
 */
@Service
public class TRecommendServiceImpl extends ServiceImpl<TRecommendMapper, TRecommend>
        implements TRecommendService {

    @Autowired
    private TRecommendMapper tRecommendMapper;
    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private RunningWaterService runningWaterService;
    @Autowired
    private IDGenerator idGenerator;
    @Override
    public List<TPermission> rolePermission(String companyId) {
        return tRecommendMapper.rolePermission(companyId);
    }

    /**
     * @param tRecommend 企业id
     * @param money      金额
     * @param tRecommend ifIncome 1支出 2收入
     * @return boolean
     * @Description: 更新无忧币流水
     * @Author tangzhuo
     * @CreateTime 2022/10/19 15:07
     */

    @Override
    public boolean UpdateCurrencyCount(String tRecommend, BigDecimal money, int ifIncome) {
        if (ifIncome == 1) {
            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("id", tRecommend);
            String moneySql = "currency_count=currency_count-%s";
            updateWrapper.setSql(String.format(moneySql, money));
            int update = tRecommendMapper.update(null, updateWrapper);
            return update > 0 ? true : false;
        } else if (ifIncome == 2) {
            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("id", tRecommend);
            String moneySql = "currency_count=currency_count+%s";
            updateWrapper.setSql(String.format(moneySql, money));
            int update = tRecommendMapper.update(null, updateWrapper);
            return update > 0 ? true : false;
        }
        return false;
    }

    @Override
    public IPage<TRecommendPO> pageList(TRecommendQuery tRecommendQuery) {
        return tRecommendMapper.pageList(tRecommendQuery.createPage(), tRecommendQuery);
    }


    @Override
    public String selectPhone(String companyId) {
        return tRecommendMapper.selectPhone(companyId);
    }

    @Override
    public IPage<TRecommendShare> shareRecommend(ShareQuery shareQuery, String userid) {
        return tRecommendMapper.shareRecommend(shareQuery.createPage(), shareQuery, userid);
    }

    @Override
    public String phoneNumbers(String userId) {
        return tRecommendMapper.phoneNumbers(userId);
    }

    @Override
    public BigDecimal addGold(BigDecimal bigDecimal, String userid) throws Exception {
        RLock lock = redissonClient.getLock(Lock.DEDUCT_EXPENSES.getLockSion() + userid);
        boolean tryLock = lock.tryLock(1, 20, TimeUnit.SECONDS); //尝试加锁，最多等待1秒，上锁以后10秒自动解锁

        try {
            if (tryLock) {
                TRecommend byId = this.getById(userid);
                UpdateWrapper updateWrapper = new UpdateWrapper();
                updateWrapper.eq("id", userid);
                String money = "gold=gold+%s";
                updateWrapper.setSql(String.format(money, bigDecimal));
                boolean update = this.update(updateWrapper);
                if(update){
                    return byId.getGold().add(bigDecimal);
                }

            }
        } finally {
            lock.unlock();
        }
        return new BigDecimal(0);
    }

    @Override
    public Map<String, Object> getSharePersonRecommend(String id) {
        return tRecommendMapper.getSharePersonRecommend(id);
    }

    @Override
    public Map<String, Object> deductCurrency(BigDecimal money, String userId, String jobHunter, String orderId) throws Exception {
        BigDecimal bigDecimal = money;//无忧币
        RLock lock = redissonClient.getLock(Lock.DEDUCT_EXPENSES.getLockSion() + userId);
        boolean tryLock = lock.tryLock(1, 20, TimeUnit.SECONDS); //尝试加锁，最多等待1秒，上锁以后10秒自动解锁
        Map<String, Object> map1 = new ConcurrentHashMap();
        TRecommend byId = this.getById(userId);
        try {
            if (tryLock) {
                BigDecimal gold = byId.getCurrencyCount();
                if (gold.compareTo(bigDecimal) == 0 || gold.compareTo(bigDecimal) == 1) {
                    map1.put("state", 0);
                    map1.put("msg", "支付成功!");
                    byId.setCurrencyCount(gold.subtract(bigDecimal));
                    boolean b = this.updateById(byId);
                    if (!b) {
                        throw new Exception("无忧币失败!");
                    }
                    RunningWater runningWater = new RunningWater();
                    runningWater.setRemainingSum(gold.subtract(bigDecimal));
                    runningWater.setRunningWaterId(idGenerator.generateCode("RW"));
                    runningWater.setSource(1);
                    runningWater.setType(0);
                    runningWater.setMoney(money);
                    runningWater.setUserId(userId);
                    runningWater.setIfIncome(1);
                    runningWater.setOrderId(orderId);
                    runningWater.setInstructions("下载求职简历，求职者ID:" + jobHunter);
                    runningWater.setCrateTime(LocalDateTime.now());
                    runningWaterService.insert(runningWater);
                } else if (gold.compareTo(bigDecimal) == -1) {
                    map1.put("state", 1);
                    map1.put("msg", "无忧币不足,请及时充值");
                }
            }
            return map1;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Map<String, Object> deductGold(BigDecimal money, String userId, String jobHunter, String orderId) throws Exception {

        RLock lock = redissonClient.getLock(Lock.DEDUCT_EXPENSES.getLockSion() + userId);
        boolean tryLock = lock.tryLock(1, 10, TimeUnit.SECONDS); //尝试加锁，最多等待1秒，上锁以后10秒自动解锁
        Map<String, Object> map1 = new ConcurrentHashMap<>(10);

        try {
            if (tryLock) {

                TRecommend byId = this.getById(userId);
                BigDecimal gold = byId.getGold();
                if (gold.compareTo(money) == 0 || gold.compareTo(money) == 1) {
                    map1.put("state", 0);
                    map1.put("msg", "支付成功!");
                    byId.setGold(gold.subtract(money));
                    boolean b = this.updateById(byId);
                    if (!b) {
                        throw new Exception("金币扣除失败");
                    }
                    RunningWater runningWater = new RunningWater();
                    runningWater.setRemainingSum(gold.subtract(money));
                    runningWater.setRunningWaterId(idGenerator.generateCode("RW"));
                    runningWater.setSource(1);
                    runningWater.setType(1);
                    runningWater.setMoney(money);
                    runningWater.setUserId(userId);
                    runningWater.setIfIncome(1);
                    runningWater.setOrderId(orderId);
                    runningWater.setInstructions("下载求职简历，求职者ID:" + jobHunter);
                    runningWater.setCrateTime(LocalDateTime.now());
                    runningWaterService.insert(runningWater);
                } else if (gold.compareTo(money) == -1) {
                    map1.put("state", 1);
                    map1.put("msg", "金币不足,请及时充值");
                }
            }
            return map1;
        } finally {
            lock.unlock();
        }


    }

    @Override
    public List<Map> graph(String userid, String year) {
        return tRecommendMapper.graph(userid,year);
    }

    @Override
    public RecommendDataDTO getRecommendDataDTO(String userid) {
        return tRecommendMapper.getRecommendDataDTO(userid);
    }

    @Override
    public TRecommend selectBasic(String id) {
        return tRecommendMapper.selectBasic(id);
    }

    @Override
    public List<String> getMember(String userid) {
        return tRecommendMapper.getMember(userid);
    }

    @Override
    public List<RecommendDataDTO> getMemberRecommendDataDTO(List<String> list) {
        return null;
    }

    @Override
    public TRecommend enterpriseInviter(String companyId) {
        return tRecommendMapper.enterpriseInviter(companyId);
    }

    @Override
    public int selectCount(String id, LocalDateTime currentStartDate, LocalDateTime currentEndTime, String city, int identity) {
        return tRecommendMapper.count(id,currentStartDate,currentEndTime,city,identity);
    }

    @Override
    public List<Map> mapList(String id, LocalDateTime currentStartDate, LocalDateTime currentEndTime, String city, int identity) {
        return tRecommendMapper.mapList(id,currentStartDate,currentEndTime,city,identity);
    }

}




