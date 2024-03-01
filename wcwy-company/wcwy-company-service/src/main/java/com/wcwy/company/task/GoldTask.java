package com.wcwy.company.task;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taobao.api.ApiException;
import com.wcwy.common.base.utils.LocalDateTimeUtils;
import com.wcwy.common.base.utils.MessageUtil;
import com.wcwy.common.base.utils.SendDingDing;
import com.wcwy.common.redis.util.IDGenerator;
import com.wcwy.company.entity.RunningWater;
import com.wcwy.company.entity.TCompany;
import com.wcwy.company.entity.TJobhunter;
import com.wcwy.company.entity.TRecommend;
import com.wcwy.company.produce.MessageProduce;
import com.wcwy.company.service.RunningWaterService;
import com.wcwy.company.service.TCompanyService;
import com.wcwy.company.service.TJobhunterService;
import com.wcwy.company.service.TRecommendService;
import com.wcwy.company.vo.MessageVO;
import com.wcwy.post.po.ParticularsPO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName: GoldTask
 * Description:
 * date: 2024/1/3 10:47
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Slf4j
@Component
public class GoldTask {

    @Autowired
    private MessageProduce messageProduce;

    @Autowired
    private TRecommendService tRecommendService;
    @Autowired
    private TCompanyService tCompanyService;
    @Autowired
    private RunningWaterService runningWaterService;
    @Autowired
    private IDGenerator idGenerator;

    /*
     * 清空金币
     * */
    @Scheduled(cron = "0 0 0 1 1 ?")//表示每年1月1日凌晨的0时0分0秒执行
    //@Scheduled(cron = "0/3 * * * * ?")
    public void clearTRGold() throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, ApiException {
        //List<TJobhunter> list = tJobhunterService.list();
        /*QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.in("id", "TR2308040927917-1", "TR2307251649746-1");*/
        List<TRecommend> list = tRecommendService.list();
        for (TRecommend tRecommend : list) {
            if (tRecommend.getGold().compareTo(new BigDecimal(0)) == 0) {
                continue;
            }
            tRecommend.setGold(new BigDecimal(0));
            try {
                boolean b = tRecommendService.updateById(tRecommend);
                if (b) {
                    RunningWater runningWater = new RunningWater();
                    runningWater.setRemainingSum(new BigDecimal(0));
                    runningWater.setRunningWaterId(idGenerator.generateCode("RW"));
                    runningWater.setSource(1);
                    runningWater.setType(1);
                    runningWater.setMoney(tRecommend.getGold());
                    runningWater.setUserId(tRecommend.getId());
                    runningWater.setIfIncome(1);
                    runningWater.setOrderId("");
                    runningWater.setInstructions("到期扣除");
                    runningWater.setCrateTime(LocalDateTime.now());
                    runningWaterService.insert(runningWater);
                    // MessageVO messageVO=new MessageVO();
                    // messageVO.setCode(2);
                    // Map map=new HashMap();
                    // messageProduce.sendOrderlyMessage(JSON.toJSONString(messageVO));
                }
            } catch (Exception e) {
                SendDingDing.sendText("推荐官金币清空出错!" + tRecommend.getId() + "报错原因" + e);

            }


        }


    }

    @Scheduled(cron = "0 0 0 1 1 ?")//表示每年1月1日凌晨的0时0分0秒执行
    // @Scheduled(cron = "0/3 * * * * ?")
    public void clearTCGold() throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, ApiException {
        //List<TJobhunter> list = tJobhunterService.list();
        List<TCompany> list = tCompanyService.list();
        for (TCompany tCompany : list) {
            if (tCompany.getGold().compareTo(new BigDecimal(0)) == 0) {
                continue;
            }
            tCompany.setGold(new BigDecimal(0));
            try {
                boolean b = tCompanyService.updateById(tCompany);
                if (b) {
                    RunningWater runningWater = new RunningWater();
                    runningWater.setRemainingSum(new BigDecimal(0));
                    runningWater.setRunningWaterId(idGenerator.generateCode("RW"));
                    runningWater.setSource(1);
                    runningWater.setType(1);
                    runningWater.setMoney(tCompany.getGold());
                    runningWater.setUserId(tCompany.getCompanyId());
                    runningWater.setIfIncome(1);
                    runningWater.setOrderId("");
                    runningWater.setInstructions("到期扣除");
                    runningWater.setCrateTime(LocalDateTime.now());
                    runningWaterService.insert(runningWater);

                }
            } catch (Exception e) {
                SendDingDing.sendText("推荐官金币清空出错!" + tCompany.getCompanyId() + "报错原因" + e);
            }

        }

    }


    @Scheduled(cron = "0 0 12 1/5 12 ?")//12月的第一天开始，每5天的中午12点
    // @Scheduled(cron = "0/3 * * * * ?")
    public void expireTCGold() throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, ApiException {
        //List<TJobhunter> list = tJobhunterService.list();
        List<TCompany> list = tCompanyService.list();
         for (TCompany tCompany : list) {
             MessageVO messageVO=new MessageVO();
             messageVO.setCode(2);
             Map sy001TjGoldRewardExpiration = MessageUtil.SY001_TJ_GOLD_REWARD_EXPIRATION;
             sy001TjGoldRewardExpiration.put("identity",0);
             sy001TjGoldRewardExpiration.put("router", "/AccountGold");
             sy001TjGoldRewardExpiration.put("chatId",tCompany.getCompanyId());
             String content = sy001TjGoldRewardExpiration.get("content").toString();
             DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
             String format1 = LocalDateTimeUtils.yearEndTime().format(formatter);
             String format = String.format(content, format1);
             sy001TjGoldRewardExpiration.put("content",format);
             messageVO.setMessage(sy001TjGoldRewardExpiration);
             messageProduce.sendOrderlyMessage(JSON.toJSONString(messageVO));
         }

    }

    @Scheduled(cron = "0 0 12 1/5 12 ?")//12月的第一天开始，每5天的中午12点
  //  @Scheduled(cron = "0/3 * * * * ?")
    public void expireTRGold() throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, ApiException {
        //List<TJobhunter> list = tJobhunterService.list();
        List<TRecommend> list = tRecommendService.list();
        for (TRecommend tRecommend : list) {
            MessageVO messageVO=new MessageVO();
            messageVO.setCode(2);
            Map sy001TjGoldRewardExpiration = MessageUtil.SY001_TJ_GOLD_REWARD_EXPIRATION;
            sy001TjGoldRewardExpiration.put("identity",0);
            sy001TjGoldRewardExpiration.put("router", "/TAccounthome");
            sy001TjGoldRewardExpiration.put("chatId",tRecommend.getId());
            String content = sy001TjGoldRewardExpiration.get("content").toString();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
            String format1 = LocalDateTimeUtils.yearEndTime().format(formatter);
            String format = String.format(content, format1);
            sy001TjGoldRewardExpiration.put("content",format);
            messageVO.setMessage(sy001TjGoldRewardExpiration);
            messageProduce.sendOrderlyMessage(JSON.toJSONString(messageVO));
        }

    }
}
