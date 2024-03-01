package com.wcwy.common.base.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: BirthdayUtils
 * Description:获取年份
 * date: 2023/4/3 17:19
 *
 * @author tangzhuo
 * @since JDK 1.8
 */

public class BirthdayUtils {

    public static Map<String, Date> getBirthday(int i){
        HashMap<String, Date> stringDateHashMap = new HashMap<>(2);
        Date startTime=null;
        Date endTime=null;
        if(i==1){
            startTime = DateUtils.getTime7(18);
            endTime = DateUtils.getTime7(25);
        }else  if(i==2){
            startTime = DateUtils.getTime7(26);
            endTime = DateUtils.getTime7(30);
        }else  if(i==3){
            startTime = DateUtils.getTime7(31);
            endTime = DateUtils.getTime7(35);
        }else  if(i==4){
            startTime = DateUtils.getTime7(36);
            endTime = DateUtils.getTime7(40);
        }else  if(i==5){
            startTime = DateUtils.getTime7(41);
            endTime = DateUtils.getTime7(45);
        }else  if(i==6){
            startTime = DateUtils.getTime7(46);
            endTime = DateUtils.getTime7(46);
        }else {
            startTime = DateUtils.getTime7(18);
            endTime = DateUtils.getTime7(25);
        }


        stringDateHashMap.put("startTime",startTime);
        stringDateHashMap.put("endTime",endTime);
        return stringDateHashMap;
    }
}
