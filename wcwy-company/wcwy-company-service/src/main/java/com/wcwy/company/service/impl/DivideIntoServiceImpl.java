package com.wcwy.company.service.impl;

import com.wcwy.common.redis.enums.Cache;
import com.wcwy.common.redis.enums.RedisCache;
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.company.service.DivideIntoService;
import com.wcwy.post.api.ResumePaymentConfigApi;
import com.wcwy.post.api.TPayConfigApi;
import com.wcwy.post.entity.ResumePaymentConfig;
import com.wcwy.post.entity.TPayConfig;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * ClassName: DivideIntoServiceImpl
 * Description:
 * date: 2023/7/24 10:10
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Service
public class DivideIntoServiceImpl implements DivideIntoService {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private TPayConfigApi tPayConfigApi;
    @Autowired
    private ResumePaymentConfigApi resumePaymentConfigApi;
    @Autowired
    private RedisUtils redisUtils;

    @Override
    public Map<String, Integer> currencyCount(BigDecimal money) {
        ValueOperations<String, TPayConfig> value = redisTemplate.opsForValue();
        TPayConfig tPayConfig = value.get(RedisCache.TPAYCONFIG.getValue());//获取无忧币分成机制
        if (tPayConfig == null) {
            tPayConfig = tPayConfigApi.select();
        }

        Map<String, Integer> map = new ConcurrentHashMap<>(2);
        if (money.compareTo(new BigDecimal(50)) == 1) {//年薪大于50万
            map.put("currencyCount", tPayConfig.getGradeE());
            map.put("gold", tPayConfig.getGoldC());
            return map;
        } else if (money.compareTo(new BigDecimal(30)) == 1 || money.compareTo(new BigDecimal(30)) == 0) {//年薪大于30万
            map.put("currencyCount", tPayConfig.getGradeD());
            map.put("gold", tPayConfig.getGoldB());
            return map;
       /* } else if (money.compareTo(new BigDecimal(20)) == 1) { //年薪大于20万
            return tPayConfig.getGradeC();
        } else if (money.compareTo(new BigDecimal(10)) == 1) { //年薪大于10万
            return tPayConfig.getGradeB();
        } else if (-1 == money.compareTo(new BigDecimal(10)) || 0 == money.compareTo(new BigDecimal(10))) { //年薪小于10万
            return tPayConfig.getGradeA();*/
        }
        map.put("currencyCount", tPayConfig.getGradeA());
        map.put("gold", tPayConfig.getGoldA());
        return map;
        //  return tPayConfig.getGradeA();

    }

    /**
     * 获取简历付对应的价格
     *
     * @param education 学历
     * @param money     简历价格
     * @param type      职位类型
     * @return
     */

    @Override
    public Map<String, BigDecimal> resumePayment(String education, BigDecimal money, Integer type) {
        List<ResumePaymentConfig>  resumePaymentConfigs = resumePaymentConfigApi.cache();
        ResumePaymentConfig resumePaymentConfig = null;
        Map<String, BigDecimal> map = new HashMap<>(2);
        if (type == 4) {
            for (ResumePaymentConfig paymentConfig : resumePaymentConfigs) {
                if(paymentConfig.getType()==0){
                    resumePaymentConfig=paymentConfig;
                }
            }
            int i = Integer.parseInt(education);
            if (i <= 4) {
                BigDecimal gradeA = resumePaymentConfig.getGradeA();
                map.put("currencyCount", gradeA);
            } else if (i == 5) {
                BigDecimal gradeB = resumePaymentConfig.getGradeB();
                map.put("currencyCount", gradeB);
            } else if (i == 6) {
                BigDecimal gradeC = resumePaymentConfig.getGradeC();
                map.put("currencyCount", gradeC);
            } else if (i == 7) {
                BigDecimal gradeD = resumePaymentConfig.getGradeD();
                map.put("currencyCount", gradeD);
            }

        } else if (type == 5) {
            for (ResumePaymentConfig paymentConfig : resumePaymentConfigs) {
                if(paymentConfig.getType()==1){
                    resumePaymentConfig=paymentConfig;
                }
            }
            if (money.compareTo(new BigDecimal(50)) == 1) {//年薪大于50万
                map.put("currencyCount", resumePaymentConfig.getGradeC());

            } else if (money.compareTo(new BigDecimal(30)) == 1 || money.compareTo(new BigDecimal(30)) == 0) {//年薪大于30万
                map.put("currencyCount", resumePaymentConfig.getGradeB());
       /* } else if (money.compareTo(new BigDecimal(20)) == 1) { //年薪大于20万
            return tPayConfig.getGradeC();
        } else if (money.compareTo(new BigDecimal(10)) == 1) { //年薪大于10万
            return tPayConfig.getGradeB();
        } else if (-1 == money.compareTo(new BigDecimal(10)) || 0 == money.compareTo(new BigDecimal(10))) { //年薪小于10万
            return tPayConfig.getGradeA();*/
            }else {
                map.put("currencyCount", resumePaymentConfig.getGradeA());
            }

        }
        return map;
    }
}
