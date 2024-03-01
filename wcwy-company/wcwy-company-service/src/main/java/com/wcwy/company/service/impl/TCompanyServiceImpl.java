package com.wcwy.company.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wcwy.common.base.enums.Lock;
import com.wcwy.common.base.result.R;
import com.wcwy.common.redis.enums.Cache;
import com.wcwy.common.redis.util.IDGenerator;
import com.wcwy.common.web.config.CacheClient;
import com.wcwy.company.dto.CompanyCollerctPutInResume;
import com.wcwy.company.dto.CompanyHomeDTO;
import com.wcwy.company.dto.SendAResumeRecord;
import com.wcwy.company.entity.RevenueSharing;
import com.wcwy.company.entity.RunningWater;
import com.wcwy.company.entity.TCompany;
import com.wcwy.company.entity.TPermission;
import com.wcwy.company.po.TCompanyBasicInformation;
import com.wcwy.company.po.TCompanyPO;
import com.wcwy.company.po.TCompanySharePO;
import com.wcwy.company.query.ShareQuery;
import com.wcwy.company.query.TCompanyQuery;
import com.wcwy.company.query.inviterQuery;
import com.wcwy.company.service.*;
import com.wcwy.company.mapper.TCompanyMapper;
import com.wcwy.company.vo.TCompanyUserVO;
import com.wcwy.post.api.TCompanyPostApi;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 * @description 针对表【t_company(企业表)】的数据库操作Service实现
 * @createDate 2022-09-01 09:30:00
 */
@Service
public class TCompanyServiceImpl extends ServiceImpl<TCompanyMapper, TCompany>
        implements TCompanyService {
    @Autowired
    private TCompanyMapper mapper;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private RunningWaterService runningWaterService;
    @Autowired
    private    TCompanyPostApi tCompanyPostApi;
    @Autowired
    private  TJobhunterService tJobhunterService;
    @Autowired
    private IDGenerator idGenerator;

    @Autowired
    private PutInResumeService putInResumeService;

    @Autowired
    private RevenueSharingService revenueSharingService;

    @Autowired
    private CompanyUserRoleService companyUserRoleService;

    @Resource
    private CacheClient cacheClient;
    @Override
    public R cc(String id){
        TCompany tCompany = cacheClient.queryWithLogicalExpire(Cache.CACHE_COMPANY.getKey(), id, TCompany.class, this::getById, 2L, TimeUnit.MINUTES);
        return R.success(tCompany);
    }



    @Override
    public List<TPermission> rolePermission(String companyId) {
        return mapper.rolePermission(companyId);
    }

    @Override
    public TCompany getId(String userid) {
        TCompany tCompany = cacheClient.queryWithLogicalExpireCount(Cache.CACHE_COMPANY.getKey(), userid, TCompany.class, this::getById, 2L, TimeUnit.MINUTES);

        return tCompany;
    }

    @Override
    public CompanyCollerctPutInResume CompanyIndustryPutInResume(String id, String industryID) {
        return mapper.CompanyIndustryPutInResume(id, industryID);
    }

    @Override
    public Boolean updateMoney(String tCompanyId, BigDecimal money) {
        return mapper.updateMoney(tCompanyId, money);
    }

    @Override
    public IPage<TCompanyPO> pageList(TCompanyQuery query) {
        return mapper.pageList(query.createPage(), query);
    }

    @Override
    public IPage<TCompanyPO> listInviterCompany(inviterQuery inviterQuery) {
        return mapper.listInviterCompany(inviterQuery.createPage(), inviterQuery);
    }

    @Override
    public IPage<TCompany> selectSubsidiaryCorporation(String loginName, Page page, String keyword) {
        return mapper.selectSubsidiaryCorporation(page, loginName, keyword);
    }

    @Override
    public Boolean subsidiary(String userid, String companyId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("parent_id", userid);
        queryWrapper.eq("company_id", companyId);
        int count = this.count(queryWrapper);

        return count > 0 ? true : false;
    }

    @Override
    public TCompanyBasicInformation basicInformation(String tCompanyId) {
        return mapper.basicInformation(tCompanyId);
    }

    @Override
    public SendAResumeRecord sendAResumeRecord(String userid, String tCompanyId) {
        return mapper.sendAResumeRecord(userid, tCompanyId);
    }

    @Override
    public boolean updateUserinfo(TCompanyUserVO tCompanyUserVO, String companyId) {
        return mapper.updateUserinfo(tCompanyUserVO, companyId);
    }

    @Override
    public String getWeChat(String userid) {
        return mapper.getWeChat(userid);
    }

    @Override
    public IPage<TCompanySharePO> shareTCompany(ShareQuery shareQuery, String userid) {
        return mapper.shareTCompany(shareQuery.createPage(), shareQuery, userid);
    }

    @Override
    public Map<String, String> getSharePerson(String companyId) {
        return mapper.getSharePerson(companyId);
    }

    @Override
    public String phoneNumbers(String company) {
        return mapper.phoneNumbers(company);
    }

    @Override
    public List<String> authorization(String loginName) {
        return mapper.authorization(loginName);
    }

    @Override
    public Map<String, Object> deductGold(BigDecimal money, String userId, String jobHunter, String orderId) throws Exception {
        String companyId = userId;//企业id
        RLock lock = redissonClient.getLock(Lock.DEDUCT_EXPENSES.getLockSion() + companyId);
        boolean tryLock = lock.tryLock(1, 20, TimeUnit.SECONDS); //尝试加锁，最多等待1秒，上锁以后10秒自动解锁
        Map<String, Object> map1 = new ConcurrentHashMap<>(10);

        try {
            if (tryLock) {

                TCompany byId = this.getById(companyId);
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
    public Map<String, Object> deductCurrency(BigDecimal money, String userId, String jobHunter, String orderId) throws Exception {
        BigDecimal bigDecimal = money;//无忧币
        String companyId = userId;//企业id
        RLock lock = redissonClient.getLock(Lock.DEDUCT_EXPENSES.getLockSion() + companyId);
        boolean tryLock = lock.tryLock(1, 20, TimeUnit.SECONDS); //尝试加锁，最多等待1秒，上锁以后10秒自动解锁
        Map<String, Object> map1 = new ConcurrentHashMap();
        TCompany byId = this.getById(companyId);
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
    public Map deductCurrencyExplain(BigDecimal money, String userId, String explain, String orderId) throws Exception {
        BigDecimal bigDecimal = money;//无忧币
        String companyId = userId;//企业id
        RLock lock = redissonClient.getLock(Lock.DEDUCT_EXPENSES.getLockSion() + companyId);
        boolean tryLock = lock.tryLock(10, 20, TimeUnit.SECONDS); //尝试加锁，最多等待1秒，上锁以后10秒自动解锁
        Map<String, Object> map1 = new ConcurrentHashMap();
        TCompany byId = this.getById(companyId);
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
                    runningWater.setInstructions(explain);
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
    public BigDecimal addGold(BigDecimal bigDecimal, String userid) throws Exception {
        RLock lock = redissonClient.getLock(Lock.DEDUCT_EXPENSES.getLockSion() + userid);
        boolean tryLock = lock.tryLock(10, 20, TimeUnit.SECONDS); //尝试加锁，最多等待1秒，上锁以后10秒自动解锁
        try {
            if (tryLock) {
                TCompany byId = this.getById(userid);

                UpdateWrapper updateWrapper = new UpdateWrapper();
                updateWrapper.eq("company_id", userid);
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
    public List<Map> graph(String userid, String year) {
        return mapper.graph(userid,year);
    }

    @Override
    public List<String> selectCompanyName(String keyword) {
        return mapper.selectCompanyName(keyword);
    }

    @Override
    public List<String> selectCompanyName(List<String> CompanyName,String userid) {
        return mapper.selectCompanyNameS(CompanyName,userid);
    }

    @Override
    public List<TCompany> companyLists(List<String> id) {
        List<TCompany> list=new ArrayList<>(id.size());
        for (String s : id) {
            TCompany tCompany = cacheClient.queryWithLogicalExpireCount(Cache.CACHE_COMPANY.getKey(), s, TCompany.class, this::getById, 2L, TimeUnit.MINUTES);
            list.add(tCompany);
        }
        return list;
    }

    @Override
    @Cacheable(value="com:CompanyHomeDTO:cache:one", key="#userid")
    public CompanyHomeDTO selectCompanyHomeDTO(String userid) {
        TCompany byId = this.getId(userid);
        if(byId==null){
           return null;
        }


        CompanyHomeDTO companyHomeDTO=new CompanyHomeDTO();
        companyHomeDTO.setGold(byId.getGold());
        companyHomeDTO.setCurrencyCount(byId.getCurrencyCount());

        QueryWrapper queryWrapper2=new QueryWrapper();
        queryWrapper2.eq("share_person",userid);
        companyHomeDTO.setTalents( tJobhunterService.count(queryWrapper2));
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("put_in_comppany",userid);
        companyHomeDTO.setCurriculumVitae(putInResumeService.count(queryWrapper));
        queryWrapper.eq("browse_if",1);
        companyHomeDTO.setNotCheckCurriculumVitae(putInResumeService.count(queryWrapper));
        RevenueSharing byId1 = revenueSharingService.getById(userid);
        if(byId1 !=null){
            companyHomeDTO.setTotalRevenue(byId1.getTotalRevenue());
            companyHomeDTO.setWithdrawDeposit(byId1.getWithdrawDeposit());
        }else {
            companyHomeDTO.setTotalRevenue(new BigDecimal(0));
            companyHomeDTO.setWithdrawDeposit(new BigDecimal(0));
        }
        QueryWrapper queryWrapper1=new QueryWrapper();
        queryWrapper1.eq("company_id",userid);
        queryWrapper1.eq("deleted",0);
        companyHomeDTO.setDownloadCurriculumVitae(companyUserRoleService.count(queryWrapper1));
        Map<String, Integer> map = tCompanyPostApi.selectCount(userid);

        companyHomeDTO.setPostCount(map.get("postCount"));
        companyHomeDTO.setOnLinePost(map.get("onLinePost"));
        return companyHomeDTO;
    }

    @Override
    public int selectCount(String id, LocalDateTime currentStartDate, LocalDateTime currentEndTime, String city, int i) {
        return mapper.count(id,currentStartDate,currentEndTime,i,city);
    }

    @Override
    public List<Map> mapList(String id, LocalDateTime currentStartDate, LocalDateTime currentEndTime, String city, int i) {
        return mapper.mapList(id,currentStartDate,currentEndTime,i,city);
    }
}



