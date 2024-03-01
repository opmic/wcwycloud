package com.wcwy.company.asyn;

import com.wcwy.common.base.enums.Gold;
import com.wcwy.common.base.enums.GoldExplain;
import com.wcwy.common.base.utils.RewardsUtils;
import com.wcwy.common.redis.enums.RedisCache;
import com.wcwy.common.redis.util.IDGenerator;
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.company.entity.RunningWater;
import com.wcwy.company.mapper.RunningWaterMapper;
import com.wcwy.company.service.RunningWaterService;
import com.wcwy.company.service.TCompanyService;
import com.wcwy.company.service.TRecommendService;
import com.wcwy.post.api.SendMessageApi;
import com.wcwy.post.entity.GoldConfig;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * ClassName: RunningWaterAsync
 * Description:
 * date: 2023/7/13 10:35
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Component
@Slf4j
public class RunningWaterAsync {
    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private TCompanyService tCompanyService;

    @Autowired
    private SendMessageApi sendMessageApi;
    @Autowired
    private TRecommendService tRecommendService;
    @Autowired
    private IDGenerator idGenerator;

    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private InformAsync informAsync;
    @Autowired
    private RunningWaterService runningWaterService;

    /**
     * 注册奖励
     * @param gold
     * @param userId
     * @param value
     */
    @Async
    public void add(Integer gold, String userId, String value) {
        RunningWater runningWater = new RunningWater();
        runningWater.setRunningWaterId(idGenerator.generateCode("RW"));
        runningWater.setSource(1);
        runningWater.setType(1);
        runningWater.setMoney(new BigDecimal(gold));
        runningWater.setUserId(userId);
        runningWater.setIfIncome(2);
        //   runningWater.setOrderId(topUp.getOrder());
        runningWater.setInstructions(value);
        runningWater.setRemainingSum(new BigDecimal(gold));
        runningWater.setCrateTime(LocalDateTime.now());
        runningWaterService.save(runningWater);
        Map map = RewardsUtils.incentiveProject(3, gold);
   /*     map.put("toUserId",userId);
        sendMessageApi.sendMsg(map);*/
        informAsync.goldReward(userId,map.get("gold").toString());
    }

/*登录奖励*/
    /*@Transactional(rollbackFor = Exception.class)*/
    @Async
    public Future<String> loginGold(String userid) throws Exception {
  /*      List<RunningWater> list = runningWaterMapper.selectList(null);
        for (RunningWater runningWater : list) {
            System.out.println(runningWater);
        }
        List<RunningWater> list1 = runningWaterMapper.selectList(null);
        return new AsyncResult<String>("cc");*/
        boolean set = redisUtils.setIfAbsent(Gold.LOGIN_GOLD.getValue() + userid, userid, 7);
        if (! set) {
            return new AsyncResult<String>("cc");
        }
        GoldConfig o1 = (GoldConfig) redisUtils.get(RedisCache.GOLD_CACHE.getValue());
        RunningWater runningWater = new RunningWater();
        runningWater.setRunningWaterId(idGenerator.generateCode("RW"));
        runningWater.setSource(1);
        runningWater.setType(1);
        runningWater.setMoney(new BigDecimal(o1.getLoginGold()));
        runningWater.setUserId(userid);
        runningWater.setIfIncome(2);
        //  runningWater.setOrderId(topUp.getOrder());
        runningWater.setInstructions(GoldExplain.LOGIN_GOLD.getValue());
        String substring = userid.substring(0, 2);
        BigDecimal money = new BigDecimal(0);
        if (substring.equals("TC")) {
             money = tCompanyService.addGold(new BigDecimal(o1.getLoginGold()), userid);
        } else if (substring.equals("TR")) {
               money = tRecommendService.addGold(new BigDecimal(o1.getLoginGold()), userid);
        }
        runningWater.setRemainingSum(money);
        runningWater.setCrateTime(LocalDateTime.now());
        runningWaterService.save(runningWater);
       // GoldConfig o1 = (GoldConfig) redisUtils.get(RedisCache.GOLD_CACHE.getValue());
        Map map = RewardsUtils.incentiveProject(2, o1.getLoginGold());
        /*map.put("toUserId",userid);
        sendMessageApi.sendMsg(map);*/
        informAsync.goldReward(userid,map.get("gold").toString());
        return new AsyncResult<String>("cc");
    }

/*发布职位*/
    @Async
    //@Transactional(rollbackFor = Exception.class)
    public void postGold(String userid) throws Exception {
        GoldConfig o1 = (GoldConfig) redisUtils.get(RedisCache.GOLD_CACHE.getValue());
        RunningWater runningWater = new RunningWater();
        runningWater.setRunningWaterId(idGenerator.generateCode("RW"));
        runningWater.setSource(1);
        runningWater.setType(1);
        runningWater.setMoney(new BigDecimal(o1.getPostGold()));
        runningWater.setUserId(userid);
        runningWater.setIfIncome(2);
        //   runningWater.setOrderId(topUp.getOrder());
        runningWater.setInstructions(GoldExplain.POST_GOLD.getValue());
        String substring = userid.substring(0, 2);
        BigDecimal money = new BigDecimal(0);
        if (substring.equals("TC")) {
            money = tCompanyService.addGold(new BigDecimal(o1.getPostGold()), userid);
        } else if (substring.equals("TR")) {
            money = tRecommendService.addGold(new BigDecimal(o1.getPostGold()), userid);
        }
        runningWater.setRemainingSum(money);
        runningWater.setCrateTime(LocalDateTime.now());

        runningWaterService.save(runningWater);
        Map map = RewardsUtils.incentiveProject(1, o1.getPostGold());
/*        map.put("toUserId",userid);
        sendMessageApi.sendMsg(map);*/
        informAsync.goldReward(userid,map.get("gold").toString());
    }

    /**
     * 推荐报告填写推荐
     * @param userid
     * @param company
     * @param jobHunter
     * @throws Exception
     */
    @Async
    //@Transactional(rollbackFor = Exception.class)
    public void recommendationReport(String userid, String company, String jobHunter) throws Exception {
        boolean b = redisUtils.sHasKey(Gold.RECOMMENDATION_REPORT.getValue() + company, jobHunter);
        if(b){
            return;
        }
        GoldConfig o1 = (GoldConfig) redisUtils.get(RedisCache.GOLD_CACHE.getValue());
        RunningWater runningWater = new RunningWater();
        runningWater.setRunningWaterId(idGenerator.generateCode("RW"));
        runningWater.setSource(1);
        runningWater.setType(1);
        runningWater.setMoney(new BigDecimal(o1.getRecommendationReport()));
        runningWater.setUserId(userid);
        runningWater.setIfIncome(2);
        //   runningWater.setOrderId(topUp.getOrder());
        runningWater.setInstructions(GoldExplain.RECOMMENDATION_REPORT.getValue());
        String substring = userid.substring(0, 2);
        BigDecimal money = new BigDecimal(0);
        if (substring.equals("TC")) {
            money = tCompanyService.addGold(new BigDecimal(o1.getRecommendationReport()), userid);
        } else if (substring.equals("TR")) {
            money = tRecommendService.addGold(new BigDecimal(o1.getRecommendationReport()), userid);
        }
        runningWater.setRemainingSum(money);
        runningWater.setCrateTime(LocalDateTime.now());
        runningWaterService.save(runningWater);
        redisUtils.sSet(Gold.RECOMMENDATION_REPORT.getValue() + company, jobHunter);
        Map map = RewardsUtils.incentiveProject(4,o1.getRecommendationReport());
      /*  map.put("toUserId",userid);
        sendMessageApi.sendMsg(map);*/
        informAsync.goldReward(userid,map.get("gold").toString());
    }

    /**
     * 浏览推荐报告
     * @param userid
     * @param jobHunter
     * @throws Exception
     */
    @Async
    public void recommendationReportBrowse(String userid,  String jobHunter) throws Exception {
        boolean b = redisUtils.sHasKey(Gold.RECOMMENDATION_REPORT_BROWSE.getValue() + userid, jobHunter);
        if(b){
            return;
        }
        GoldConfig o1 = (GoldConfig) redisUtils.get(RedisCache.GOLD_CACHE.getValue());
        RunningWater runningWater = new RunningWater();
        runningWater.setRunningWaterId(idGenerator.generateCode("RW"));
        runningWater.setSource(1);
        runningWater.setType(1);
        runningWater.setMoney(new BigDecimal(o1.getRecommendationReportBrowse()));
        runningWater.setUserId(userid);
        runningWater.setIfIncome(2);
        //   runningWater.setOrderId(topUp.getOrder());
        runningWater.setInstructions(GoldExplain.RECOMMENDATION_REPORT_BROWSE.getValue());
        String substring = userid.substring(0, 2);
        BigDecimal money = new BigDecimal(0);
        if (substring.equals("TC")) {
            money = tCompanyService.addGold(new BigDecimal(o1.getRecommendationReportBrowse()), userid);
        } else if (substring.equals("TR")) {
            money = tRecommendService.addGold(new BigDecimal(o1.getRecommendationReportBrowse()), userid);
        }
        runningWater.setRemainingSum(money);
        runningWater.setCrateTime(LocalDateTime.now());
        runningWaterService.save(runningWater);
        redisUtils.sSet(Gold.RECOMMENDATION_REPORT_BROWSE.getValue() + userid, jobHunter);
        Map map = RewardsUtils.incentiveProject(5,o1.getRecommendationReportBrowse());
       /* map.put("toUserId",userid);
        sendMessageApi.sendMsg(map);*/
        informAsync.goldReward(userid,map.get("gold").toString());
    }
    /**
     * 发送面试邀请
     * @param userid
     * @param jobHunter
     * @throws Exception
     */
    @Async
    public void interviewInvitation(String userid, String jobHunter,String recommend) throws Exception {
        boolean b = redisUtils.sHasKey(Gold.INTERVIEW_INVITATION.getValue() + userid, jobHunter);
        if(b){
            return;
        }

        GoldConfig o1 = (GoldConfig) redisUtils.get(RedisCache.GOLD_CACHE.getValue());
        RunningWater runningWater = new RunningWater();
        runningWater.setRunningWaterId(idGenerator.generateCode("RW"));
        runningWater.setSource(1);
        runningWater.setType(1);
        runningWater.setMoney(new BigDecimal(o1.getInterviewInvitation()));
        runningWater.setUserId(recommend);
        runningWater.setIfIncome(2);
        //   runningWater.setOrderId(topUp.getOrder());
        runningWater.setInstructions(GoldExplain.INTERVIEW_INVITATION.getValue());
        String substring = recommend.substring(0, 2);
        BigDecimal money = new BigDecimal(0);
        if (substring.equals("TC")) {
            money = tCompanyService.addGold(new BigDecimal(o1.getInterviewInvitation()), recommend);
        } else if (substring.equals("TR")) {
            money = tRecommendService.addGold(new BigDecimal(o1.getInterviewInvitation()), recommend);
        }else {
            return;
        }
        runningWater.setRemainingSum(money);
        runningWater.setCrateTime(LocalDateTime.now());
        runningWaterService.save(runningWater);
        redisUtils.sSet(Gold.INTERVIEW_INVITATION.getValue() + userid, jobHunter);
        Map map = RewardsUtils.incentiveProject(6,o1.getInterviewInvitation());
        /*map.put("toUserId",userid);
        sendMessageApi.sendMsg(map);*/
        informAsync.goldReward(userid,map.get("gold").toString());
    }

//入职奖励金币
    @Async
   // @Transactional(rollbackFor = Exception.class)
    public void entry(String userid, String jobHunter,String recommend) throws Exception {
        boolean b = redisUtils.sHasKey(Gold.ENTRY.getValue() + userid, jobHunter);
        if(b){
            return;
        }

        GoldConfig o1 = (GoldConfig) redisUtils.get(RedisCache.GOLD_CACHE.getValue());
        //奖励金币
        RunningWater runningWater = new RunningWater();
        runningWater.setRunningWaterId(idGenerator.generateCode("RW"));
        runningWater.setSource(1);
        runningWater.setType(1);
        runningWater.setMoney(new BigDecimal(o1.getEntry()));
        runningWater.setUserId(recommend);
        runningWater.setIfIncome(2);
        //   runningWater.setOrderId(topUp.getOrder());
        runningWater.setInstructions(GoldExplain.ENTRY.getValue());
        String substring = recommend.substring(0, 2);
        BigDecimal money = new BigDecimal(0);
        if (substring.equals("TC")) {
            money = tCompanyService.addGold(new BigDecimal(o1.getEntry()), recommend);
        } else if (substring.equals("TR")) {
            money = tRecommendService.addGold(new BigDecimal(o1.getEntry()), recommend);
        }else {
            return;
        }
        runningWater.setRemainingSum(money);
        runningWater.setCrateTime(LocalDateTime.now());
        runningWaterService.save(runningWater);
        redisUtils.sSet(Gold.ENTRY.getValue() + userid, jobHunter);
        Map map = RewardsUtils.incentiveProject(7,o1.getEntry());
       /* map.put("toUserId",userid);
        sendMessageApi.sendMsg(map);*/
        informAsync.goldReward(userid,map.get("gold").toString());
    }


    @Async
   // @Transactional(rollbackFor = Exception.class)
    public void offer(String userid, String jobHunter,String recommend) throws Exception {
        boolean b = redisUtils.sHasKey(Gold.OFFER.getValue() + userid, jobHunter);
        if(b){
            return;
        }

        GoldConfig o1 = (GoldConfig) redisUtils.get(RedisCache.GOLD_CACHE.getValue());
        RunningWater runningWater = new RunningWater();
        runningWater.setRunningWaterId(idGenerator.generateCode("RW"));
        runningWater.setSource(1);
        runningWater.setType(1);
        runningWater.setMoney(new BigDecimal(o1.getOffer()));
        runningWater.setUserId(recommend);
        runningWater.setIfIncome(2);
        //   runningWater.setOrderId(topUp.getOrder());
        runningWater.setInstructions(GoldExplain.OFFER.getValue());
        String substring = recommend.substring(0, 2);
        BigDecimal money = new BigDecimal(0);
        if (substring.equals("TC")) {
            money = tCompanyService.addGold(new BigDecimal(o1.getOffer()), recommend);
        } else if (substring.equals("TR")) {
            money = tRecommendService.addGold(new BigDecimal(o1.getOffer()), recommend);
        }else {
            return;
        }
        runningWater.setRemainingSum(money);
        runningWater.setCrateTime(LocalDateTime.now());
        runningWaterService.save(runningWater);
        redisUtils.sSet(Gold.OFFER.getValue() + userid, jobHunter);
        Map map = RewardsUtils.incentiveProject(8,o1.getOffer());
       // map.put("toUserId",userid);
        //sendMessageApi.sendMsg(map);
        informAsync.goldReward(userid,map.get("gold").toString());
    }


    @Async
   // @Transactional(rollbackFor = Exception.class)
    public void shareRegistration(String userid, String type,String option) {
        GoldConfig o1 = (GoldConfig) redisUtils.get(RedisCache.GOLD_CACHE.getValue());
        long l = redisUtils.incrTime(option+type+Gold.SHARE_REGISTRATION.getValue() + userid);
        if(l>o1.getShareRegistrationCount()){
            return;
        }
        RunningWater runningWater = new RunningWater();
        runningWater.setRunningWaterId(idGenerator.generateCode("RW"));
        runningWater.setSource(1);
        runningWater.setType(1);
        runningWater.setMoney(new BigDecimal(o1.getShareRegistration()));
        runningWater.setUserId(userid);
        runningWater.setIfIncome(2);
        //   runningWater.setOrderId(topUp.getOrder());
        runningWater.setInstructions(GoldExplain.SHARE_REGISTRATION.getValue());
        String substring = userid.substring(0, 2);
        BigDecimal money = new BigDecimal(0);
        if (substring.equals("TC")) {
            try {
                money = tCompanyService.addGold(new BigDecimal(o1.getShareRegistration()), userid);
            } catch (Exception e) {
                log.error("企业添加金币异常"+e);
                e.printStackTrace();
            }
        } else if (substring.equals("TR")) {
            try {
                money = tRecommendService.addGold(new BigDecimal(o1.getShareRegistration()), userid);
            } catch (Exception e) {
                log.error("推荐官添加金币异常"+e);
                e.printStackTrace();
            }
        }
        runningWater.setRemainingSum(money);
        runningWater.setCrateTime(LocalDateTime.now());
        runningWaterService.save(runningWater);
        Map map = RewardsUtils.incentiveProject(9,o1.getShareRegistration());
      //  map.put("toUserId",userid);
        informAsync.goldReward(userid,map.get("gold").toString());
    }

    @Async
   // @Transactional(rollbackFor = Exception.class)
    public void inviter_job_hunter(String userid) throws Exception {
        GoldConfig o1 = (GoldConfig) redisUtils.get(RedisCache.GOLD_CACHE.getValue());
        RunningWater runningWater = new RunningWater();
        runningWater.setRunningWaterId(idGenerator.generateCode("RW"));
        runningWater.setSource(1);
        runningWater.setType(1);
        runningWater.setMoney(new BigDecimal(o1.getInviterJobHunter()));
        runningWater.setUserId(userid);
        runningWater.setIfIncome(2);
        //   runningWater.setOrderId(topUp.getOrder());
        runningWater.setInstructions(GoldExplain.INVITER_JOB_HUNTER.getValue());
        String substring = userid.substring(0, 2);
        BigDecimal money = new BigDecimal(0);
        if (substring.equals("TC")) {
            money = tCompanyService.addGold(new BigDecimal(o1.getInviterJobHunter()), userid);
        } else if (substring.equals("TR")) {
            money = tRecommendService.addGold(new BigDecimal(o1.getInviterJobHunter()), userid);
        }
        runningWater.setRemainingSum(money);
        runningWater.setCrateTime(LocalDateTime.now());
        runningWaterService.save(runningWater);
        Map map = RewardsUtils.incentiveProject(10,o1.getInviterJobHunter());
        /*map.put("toUserId",userid);
        sendMessageApi.sendMsg(map);*/
        informAsync.goldReward(userid,map.get("gold").toString());
    }

    @Async
  //  @Transactional(rollbackFor = Exception.class)
    public void inviter_recommend(String userid) throws Exception {
        GoldConfig o1 = (GoldConfig) redisUtils.get(RedisCache.GOLD_CACHE.getValue());
        RunningWater runningWater = new RunningWater();
        runningWater.setRunningWaterId(idGenerator.generateCode("RW"));
        runningWater.setSource(1);
        runningWater.setType(1);
        runningWater.setMoney(new BigDecimal(o1.getInviterRecommend()));
        runningWater.setUserId(userid);
        runningWater.setIfIncome(2);
        //   runningWater.setOrderId(topUp.getOrder());
        runningWater.setInstructions(GoldExplain.INVITER_RECOMMEND.getValue());
        String substring = userid.substring(0, 2);
        BigDecimal money = new BigDecimal(0);
        if (substring.equals("TC")) {
            money = tCompanyService.addGold(new BigDecimal(o1.getInviterRecommend()), userid);
        } else if (substring.equals("TR")) {
            money = tRecommendService.addGold(new BigDecimal(o1.getInviterRecommend()), userid);
        }
        runningWater.setRemainingSum(money);
        runningWater.setCrateTime(LocalDateTime.now());
        runningWaterService.save(runningWater);
        Map map = RewardsUtils.incentiveProject(11,o1.getInviterRecommend());
        //map.put("toUserId",userid);
        informAsync.goldReward(userid,map.get("gold").toString());
       // sendMessageApi.sendMsg(map);
    }


    @Async
   // @Transactional(rollbackFor = Exception.class)
    public void inviter_company(String userid) throws Exception {
        GoldConfig o1 = (GoldConfig) redisUtils.get(RedisCache.GOLD_CACHE.getValue());
        RunningWater runningWater = new RunningWater();
        runningWater.setRunningWaterId(idGenerator.generateCode("RW"));
        runningWater.setSource(1);
        runningWater.setType(1);
        runningWater.setMoney(new BigDecimal(o1.getInviterCompany()));
        runningWater.setUserId(userid);
        runningWater.setIfIncome(2);
        //   runningWater.setOrderId(topUp.getOrder());
        runningWater.setInstructions(GoldExplain.INVITER_COMPANY.getValue());
        String substring = userid.substring(0, 2);
        BigDecimal money = new BigDecimal(0);
        if (substring.equals("TC")) {
            money = tCompanyService.addGold(new BigDecimal(o1.getInviterCompany()), userid);
        } else if (substring.equals("TR")) {
            money = tRecommendService.addGold(new BigDecimal(o1.getInviterCompany()), userid);
        }
        runningWater.setRemainingSum(money);
        runningWater.setCrateTime(LocalDateTime.now());
        runningWaterService.save(runningWater);
        Map map = RewardsUtils.incentiveProject(12,o1.getInviterCompany());
     //   map.put("toUserId",userid);
        informAsync.goldReward(userid,map.get("gold").toString());
    }



}
