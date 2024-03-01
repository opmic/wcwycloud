package com.wcwy.common.base.utils;

import java.math.BigDecimal;

/**
 * ClassName: ScaleUtil
 * Description:
 * date: 2023/2/21 13:53
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
public class ScaleUtil {

    public static String screen(String scale) {
        if("不限".equals(scale)){
            return "0";
        }else if("0-50人".equals(scale)){
            return "1";
        }else if("50-150人".equals(scale)){
            return "2";
        }else if("150-500人".equals(scale)){
            return "3";
        }else if("500-1000人".equals(scale)){
            return "4";
        }else if("1000-5000人".equals(scale)){
            return "5";
        }else if("5000-10000人".equals(scale)){
            return "6";
        }else if("10000人以上".equals(scale)){
            return "7";
        }
        return "0";
    }
}
