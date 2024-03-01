package com.wcwy.company.service;

import java.math.BigDecimal;
import java.util.Map;

/**
 * ClassName: DivideIntoService
 * Description:分成机制表
 * date: 2023/7/24 10:10
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
public interface DivideIntoService {

    public Map<String, Integer> currencyCount(BigDecimal money);
    //获取简历付的价格
    public Map<String, BigDecimal> resumePayment(String education,BigDecimal money,Integer type);
}
