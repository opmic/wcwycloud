package com.wcwy.common.base.utils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ClassName: CompensationUtil
 * Description:薪资工具类
 * date: 2023/2/16 13:35
 *
 * @author tangzhuo
 * @since JDK 1.8
 */

public class CompensationUtil {

    public static Map<String, BigDecimal> salary(Integer id) {
        Map<String, BigDecimal> map = new ConcurrentHashMap(2);
        switch (id) {
            case 1:
                map.put("beginSalary", BigDecimal.valueOf(3));
                map.put("endSalary", BigDecimal.valueOf(0));
                break;
            case 2:
                map.put("beginSalary", BigDecimal.valueOf(3));
                map.put("endSalary", BigDecimal.valueOf(5));
                break;
            case 3:
                map.put("beginSalary", BigDecimal.valueOf(5));
                map.put("endSalary", BigDecimal.valueOf(10));
                break;
            case 4:
                map.put("beginSalary", BigDecimal.valueOf(10));
                map.put("endSalary", BigDecimal.valueOf(20));
                break;
            case 5:
                map.put("beginSalary", BigDecimal.valueOf(20));
                map.put("endSalary", BigDecimal.valueOf(50));
                break;
            case 6:
                map.put("beginSalary", BigDecimal.valueOf(50));
                map.put("endSalary", BigDecimal.valueOf(50));
                break;
            default:
                map.put("beginSalary", BigDecimal.valueOf(0));
                map.put("endSalary", BigDecimal.valueOf(0));
                break;
        }
        return map;
    }
}
