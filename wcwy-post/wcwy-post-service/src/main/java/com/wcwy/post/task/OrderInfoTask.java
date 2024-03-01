package com.wcwy.post.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wawy.company.api.TCompanyApi;
import com.wawy.company.api.TCompanyLoginApi;
import com.wawy.company.api.TJobHunterApi;
import com.wawy.company.api.TRecommendApi;
import com.wcwy.common.base.utils.DivideIntoUtil;
import com.wcwy.common.redis.enums.RedisCache;
import com.wcwy.common.redis.enums.Register;
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.company.entity.TCompany;
import com.wcwy.post.aliyun.SendSms;
import com.wcwy.post.asyn.InformAsync;
import com.wcwy.post.entity.OrderInfo;
import com.wcwy.post.entity.TPayConfig;
import com.wcwy.post.enums.OrderStatus;
import com.wcwy.post.po.ParticularsPO;
import com.wcwy.post.service.OrderInfoService;
import com.wcwy.post.service.TPayConfigService;
import com.wcwy.post.vo.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @description 针对表【order_info(岗位订单表)】的数据库操作Service
 * @createDate 2022-10-12 17:06:43
 */
@Configuration
@Slf4j
public class OrderInfoTask {
    @Autowired
    private OrderInfoService orderInfoService;
    @Autowired
    private SendSms sendSms;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private TCompanyApi tCompanyApi;
    @Autowired
    private TJobHunterApi tJobHunterApi;
    @Resource
    private TPayConfigService tPayConfigService;
    @Resource
    private TRecommendApi tRecommendApi;
    @Resource
    private InformAsync informAsync;
    //发送催单请求
    @Scheduled(cron = "0 0 12 * * ?")//每天中午12点触发
    // @Scheduled(cron = "0/5 * * * * ?")
    public void noticeOrder() {
/*        LambdaQueryWrapper<OrderInfo> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(OrderInfo::getState, 1);
        queryWrapper.and(i -> i.eq(OrderInfo::getIdentification, 2).or().eq(OrderInfo::getIdentification, 3));
        LocalDate now = LocalDate.now();
        queryWrapper.between(OrderInfo::getNoPaymentTime, now.plusDays(-3), now);*/
        List<Map<String, String>> list = orderInfoService.companys();
        for (Map<String, String> map : list) {
            String phone = tCompanyApi.selectPhone(map.get("createId"));
            System.out.println(phone);
            sendSms.SendSmsUtils(phone, map.get("number"));
        }
        // sendSms.SendSmsUtils("15707393479","2");
    }
    //发送催单请求
    @Scheduled(cron = "0 0 12 * * ?")//每天中午12点触发
     //@Scheduled(cron = "0/5 * * * * ?")
    public void noticeOrder1() {
/*        LambdaQueryWrapper<OrderInfo> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(OrderInfo::getState, 1);
        queryWrapper.and(i -> i.eq(OrderInfo::getIdentification, 2).or().eq(OrderInfo::getIdentification, 3));
        LocalDate now = LocalDate.now();
        queryWrapper.between(OrderInfo::getNoPaymentTime, now.plusDays(-3), now);*/
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.in("identification",2,3,5);
        queryWrapper.eq("state",1);
        queryWrapper.ge("no_payment_time",LocalDate.now());
        List<OrderInfo> list = orderInfoService.list(queryWrapper);
        for (OrderInfo orderInfo : list) {
            String particulars = orderInfo.getParticulars();
            ParticularsPO particularsPO = JSON.parseObject(particulars, ParticularsPO.class);

            informAsync.orderPayment(orderInfo.getCreateId(),orderInfo.getState(),"招聘付服务",particularsPO.getPostLabel(),orderInfo.getOrderId(),orderInfo.getNoPaymentTime(),orderInfo.getIdentification());
        }
        // sendSms.SendSmsUtils("15707393479","2");
    }


    //推荐分成
    //@Scheduled(cron = "0 0 2 * * ?")  //每天凌晨2:00执行任务
    @Scheduled(cron = "0/5 * * * * ?")
    public void divideInto() {
        QueryWrapper queryWrapper = new QueryWrapper();
        Integer[] strings = new Integer[]{1, 2, 3};
        queryWrapper.eq("state", 2);
        queryWrapper.eq("divide_into_if", 0);
        queryWrapper.ne("identification", 4);
        queryWrapper.in("payment_type", strings);
        List<OrderInfo> list = orderInfoService.list(queryWrapper);
        TPayConfig tPayConfig = (TPayConfig) redisUtils.get(RedisCache.TPAYCONFIG.getValue());
        if (tPayConfig == null) {
            List<TPayConfig> list1 = tPayConfigService.list();
            tPayConfig = list1.get(0);

        }
        for (OrderInfo orderInfo : list) {
            if (orderInfo.getIdentification() == 1) {
                //获取求职者邀请推荐官及邀请推荐官的推荐官
                if (orderInfo.getJobhunterId() != null) {
                    //获取求职者的推荐官
                    Map<String, String> sharePersonRecommend = tJobHunterApi.getSharePersonRecommend(orderInfo.getJobhunterId());
                    //邀请企业的推荐官
                    Map<String, String> sharePersonRecommend1 = tCompanyApi.getSharePersonRecommend(orderInfo.getCreateId());
                    if (sharePersonRecommend != null) {
                        //获取到邀请求职者的推荐官
                        String recommendA = sharePersonRecommend.get("recommendA");
                        if (recommendA != null) {
                            //下载简历
                            //获取到邀请求职者的推荐官的推荐官邀请人
                            String recommendB = sharePersonRecommend.get("recommendB");
                            if (!StringUtils.isEmpty(recommendA)) {
                                //判断是否1200内注册的
                                // boolean recommend_id = redisUtils.sHasKey(Register.RECOMMEND_REGISTER.getType(), recommendA);
                                DecimalFormat df = new DecimalFormat("0.00");
                                Double recommendAPercentage = Double.valueOf(df.format((float) tPayConfig.getInviteJobHunterDownloadA() / 100));
                                Double recommendBPercentage = Double.valueOf(df.format((float) tPayConfig.getInviteJobHunterDownloadB() / 100));
                                Map<String, Object> map = DivideIntoUtil.downloadResume(orderInfo.getMoney(), recommendA, recommendB, recommendAPercentage, recommendBPercentage);
                                orderInfo.setReferrerId(recommendA);
                                orderInfo.setReferrerMoney((BigDecimal) map.get(recommendA));
                                //邀请推荐推荐官分成
                                if (!StringUtils.isEmpty(recommendB)) {
                                    orderInfo.setShareUserId(recommendB);
                                    orderInfo.setShareMoney((BigDecimal) map.get(recommendB));
                                }

                            }
                        }
                    }

                    if (sharePersonRecommend1 != null) {
                        //获取到邀请求职者的推荐官
                        String recommendA = sharePersonRecommend1.get("recommendA");
                        if (recommendA != null) {
                            //下载简历
                            //获取到邀请求职者的推荐官的推荐官邀请人
                            String recommendB = sharePersonRecommend1.get("recommendB");
                            if (!StringUtils.isEmpty(recommendA)) {
                                //  boolean recommend_id = redisUtils.sHasKey(Register.RECOMMEND_REGISTER.getType(), recommendA);
                                //判断是否1200内注册的
                                DecimalFormat df = new DecimalFormat("0.00");
                                Double recommendAPercentage = Double.valueOf(df.format((float) tPayConfig.getInviteFirmDownloadA() / 100));
                                Double recommendBPercentage = Double.valueOf(df.format((float) tPayConfig.getInviteFirmDownloadB() / 100));
                                Map<String, Object> map = null;
                                    if (!StringUtils.isEmpty(sharePersonRecommend) && ! StringUtils.isEmpty(sharePersonRecommend.get("recommendB")) && ! StringUtils.isEmpty(recommendB) && sharePersonRecommend.get("recommendB").equals(recommendB)) {
                                        map = DivideIntoUtil.downloadResume(orderInfo.getMoney(), recommendA, null, recommendAPercentage, recommendBPercentage);
                                    } else {
                                        map = DivideIntoUtil.downloadResume(orderInfo.getMoney(), recommendA, recommendB, recommendAPercentage, recommendBPercentage);
                                    }



                                orderInfo.setInviterCompany(recommendA);
                                orderInfo.setInviterCompanyMoney((BigDecimal) map.get(recommendA));
                                //邀请推荐推荐官分成
                                if (!StringUtils.isEmpty(recommendB)) {
                                    orderInfo.setInviterCompanyRecommend(recommendB);
                                    orderInfo.setInviterCompanyRecommendMoney(map.get(recommendB) == null ? new BigDecimal(0) : (BigDecimal) map.get(recommendB));
                                } else {
                                    orderInfo.setInviterCompanyRecommendMoney(new BigDecimal(0));
                                }

                            }
                        }
                    }
                }

                BigDecimal subtract = orderInfo.getMoney().subtract(orderInfo.getReferrerMoney()).subtract(orderInfo.getShareMoney()).subtract(orderInfo.getInviterCompanyMoney()).subtract(orderInfo.getInviterCompanyRecommendMoney());
                orderInfo.setPlatformMoney(subtract);
                orderInfo.setDivideIntoIf(2);
                orderInfoService.updateById(orderInfo);
            } else if (orderInfo.getIdentification() == 2 || orderInfo.getIdentification() == 3 || orderInfo.getIdentification() == 5) {
                //获取推荐人推荐官及上级推荐官
                Map<String, Object> sharePersonRecommend = tRecommendApi.getSharePersonRecommend(orderInfo.getRecommendId());
                //邀请企业的推荐官
                Map<String, String> sharePersonRecommend1 = null;
                if (orderInfo.getIdentification() == 5) {
                    sharePersonRecommend1 = tCompanyApi.getSharePersonRecommend(orderInfo.getPayer());
                } else {
                    sharePersonRecommend1 = tCompanyApi.getSharePersonRecommend(orderInfo.getCreateId());
                }

                if (sharePersonRecommend != null) {
                    //获取到邀请求职者的推荐官
                    String recommendA = (String) sharePersonRecommend.get("recommendA");
                    if (recommendA != null) {
                        //下载简历
                        //获取到邀请求职者的推荐官的推荐官邀请人
                        String recommendB = (String) sharePersonRecommend.get("recommendB");
                        if (!StringUtils.isEmpty(recommendA)) {
                            //判断是否1200内注册的
                            // boolean recommend_id = redisUtils.sHasKey(Register.RECOMMEND_REGISTER.getType(), recommendA);
                            DecimalFormat df = new DecimalFormat("0.00");
                            Double recommendAPercentage = Double.valueOf(df.format((float) tPayConfig.getInviteInteractionPostA() / 100));
                            Double recommendBPercentage = Double.valueOf(df.format((float) tPayConfig.getInviteInteractionPostB() / 100));
                            Map<String, BigDecimal> map = DivideIntoUtil.postDivideInto(orderInfo.getMoney(), recommendA, recommendB, recommendAPercentage, recommendBPercentage);
                            orderInfo.setReferrerId(recommendA);
                            orderInfo.setReferrerMoney(map.get(recommendA));
                            //邀请推荐推荐官分成
                            if (!StringUtils.isEmpty(recommendB)) {
                                orderInfo.setShareUserId(recommendB);
                                orderInfo.setShareMoney(map.get(recommendB));
                            } else {
                                orderInfo.setShareMoney(new BigDecimal(0));
                            }
                        }
                    }
                }
                if (sharePersonRecommend1 != null) {
                    //获取到邀请求职者的推荐官
                    String recommendA = sharePersonRecommend1.get("recommendA");
                    if (recommendA != null) {
                        //下载简历
                        //获取到邀请求职者的推荐官的推荐官邀请人
                        String recommendB = sharePersonRecommend1.get("recommendB");
                        if (!StringUtils.isEmpty(recommendA)) {
                            //  boolean recommend_id = redisUtils.sHasKey(Register.RECOMMEND_REGISTER.getType(), recommendA);
                            //判断是否1200内注册的
                            DecimalFormat df = new DecimalFormat("0.00");
                            Double recommendAPercentage = Double.valueOf(df.format((float) tPayConfig.getInviteFirmPostA() / 100));
                            Double recommendBPercentage = Double.valueOf(df.format((float) tPayConfig.getInviteFirmPostB() / 100));
                            Map<String, BigDecimal> map = null;
                            if (! StringUtils.isEmpty(sharePersonRecommend.get("recommendB")) && ! StringUtils.isEmpty(recommendB) && sharePersonRecommend.get("recommendB").equals(sharePersonRecommend1.get("recommendB"))) {
                                map = DivideIntoUtil.postDivideInto(orderInfo.getMoney(), recommendA, null, recommendAPercentage, recommendBPercentage);
                            } else {
                                map = DivideIntoUtil.postDivideInto(orderInfo.getMoney(), recommendA, recommendB, recommendAPercentage, recommendBPercentage);
                            }

                            orderInfo.setInviterCompany(recommendA);
                            orderInfo.setInviterCompanyMoney(map.get(recommendA));
                            //邀请推荐推荐官分成
                            if (!StringUtils.isEmpty(recommendB)) {
                                orderInfo.setInviterCompanyRecommend(recommendB);
                                orderInfo.setInviterCompanyRecommendMoney(map.get(recommendB) == null ? new BigDecimal(0) : (BigDecimal) map.get(recommendB));
                            } else {
                                orderInfo.setInviterCompanyRecommendMoney(new BigDecimal(0));
                            }

                        }
                    }
                }
            }else if (orderInfo.getIdentification() == 6 || orderInfo.getIdentification() == 7){
                //获取推荐人推荐官及上级推荐官
                Map<String, Object> sharePersonRecommend = tRecommendApi.getSharePersonRecommend(orderInfo.getRecommendId());
                //邀请企业的推荐官
                Map<String, String> sharePersonRecommend1 = null;
                if (orderInfo.getIdentification() == 5) {
                    sharePersonRecommend1 = tCompanyApi.getSharePersonRecommend(orderInfo.getPayer());
                } else {
                    sharePersonRecommend1 = tCompanyApi.getSharePersonRecommend(orderInfo.getCreateId());
                }

                if (sharePersonRecommend != null) {
                    //获取到邀请求职者的推荐官
                    String recommendA = (String) sharePersonRecommend.get("recommendA");
                    if (recommendA != null) {
                        //下载简历
                        //获取到邀请求职者的推荐官的推荐官邀请人
                        String recommendB = (String) sharePersonRecommend.get("recommendB");
                        if (!StringUtils.isEmpty(recommendA)) {
                            //判断是否1200内注册的
                            // boolean recommend_id = redisUtils.sHasKey(Register.RECOMMEND_REGISTER.getType(), recommendA);
                            DecimalFormat df = new DecimalFormat("0.00");
                            Double recommendAPercentage = Double.valueOf(df.format((float) tPayConfig.getResumePay() / 100));
                            Double recommendBPercentage = Double.valueOf(df.format((float) tPayConfig.getInviteInteractionPostB() / 100));
                            Map<String, BigDecimal> map = DivideIntoUtil.postDivideInto(orderInfo.getMoney(), recommendA, recommendB, recommendAPercentage, recommendBPercentage);
                            orderInfo.setReferrerId(recommendA);
                            orderInfo.setReferrerMoney(map.get(recommendA));
                            //邀请推荐推荐官分成
                            if (!StringUtils.isEmpty(recommendB)) {
                                orderInfo.setShareUserId(recommendB);
                                orderInfo.setShareMoney(map.get(recommendB));
                            } else {
                                orderInfo.setShareMoney(new BigDecimal(0));
                            }
                        }
                    }
                }
                if (sharePersonRecommend1 != null) {
                    //获取到邀请求职者的推荐官
                    String recommendA = sharePersonRecommend1.get("recommendA");
                    if (recommendA != null) {
                        //下载简历
                        //获取到邀请求职者的推荐官的推荐官邀请人
                        String recommendB = sharePersonRecommend1.get("recommendB");
                        if (!StringUtils.isEmpty(recommendA)) {
                            //  boolean recommend_id = redisUtils.sHasKey(Register.RECOMMEND_REGISTER.getType(), recommendA);
                            //判断是否1200内注册的
                            DecimalFormat df = new DecimalFormat("0.00");
                            Double recommendAPercentage = Double.valueOf(df.format((float) tPayConfig.getInviteFirmPostA() / 100));
                            Double recommendBPercentage = Double.valueOf(df.format((float) tPayConfig.getInviteFirmPostB() / 100));
                            Map<String, BigDecimal> map = null;
                            if (! StringUtils.isEmpty(sharePersonRecommend.get("recommendB")) && ! StringUtils.isEmpty(recommendB) && sharePersonRecommend.get("recommendB").equals(sharePersonRecommend1.get("recommendB"))) {
                                map = DivideIntoUtil.postDivideInto(orderInfo.getMoney(), recommendA, null, recommendAPercentage, recommendBPercentage);
                            } else {
                                map = DivideIntoUtil.postDivideInto(orderInfo.getMoney(), recommendA, recommendB, recommendAPercentage, recommendBPercentage);
                            }

                            orderInfo.setInviterCompany(recommendA);
                            orderInfo.setInviterCompanyMoney(map.get(recommendA));
                            //邀请推荐推荐官分成
                            if (!StringUtils.isEmpty(recommendB)) {
                                orderInfo.setInviterCompanyRecommend(recommendB);
                                orderInfo.setInviterCompanyRecommendMoney(map.get(recommendB) == null ? new BigDecimal(0) : (BigDecimal) map.get(recommendB));
                            } else {
                                orderInfo.setInviterCompanyRecommendMoney(new BigDecimal(0));
                            }

                        }
                    }
                }
            }
            BigDecimal subtract = orderInfo.getMoney().subtract(orderInfo.getReferrerMoney()).subtract(orderInfo.getShareMoney()).subtract(orderInfo.getInviterCompanyMoney()).subtract(orderInfo.getInviterCompanyRecommendMoney());
            orderInfo.setPlatformMoney(subtract);
            orderInfo.setDivideIntoIf(2);
            orderInfoService.updateById(orderInfo);
        }


    }


}
