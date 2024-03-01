package com.wcwy.common.base.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: WorkExperienceUtil
 * Description:
 * date: 2023/2/15 10:57
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
public class WorkExperienceUtil {

   public static  Integer  screenOut(String workExperience){
       if("经验不限".equals(workExperience) ||"不限".equals(workExperience) ){
            return 0;
       }else  if("在校生".equals(workExperience)){
          return 1;
       }else  if("应届生".equals(workExperience)) {
          return 2;
       }else  if("1年以内".equals(workExperience)) {
          return 3;
       }else if("1-3年".equals(workExperience)){
          return 4;
       }else if("3-5年".equals(workExperience)){
          return 5;
       }else if("5-10年".equals(workExperience)){
          return 6;
       }else if("10年以上".equals(workExperience)){
          return 7;
       }
       return 0;
   }

   public static Map<String,Date> transitionDate(int i){
       Map<String, Date> dateMap=new HashMap<>(2);
       if(i==1){
           Date date = new Date();
           dateMap.put("A",date);
           Date time7 = DateUtils.getTime7();
           dateMap.put("B",time7);
       }else if(i==2){
           Date date = DateUtils.getTime7(1);
           dateMap.put("A",date);
           Date time7 = DateUtils.getTime7(3);
           dateMap.put("B",time7);
       }else if(i==3){
           Date date = DateUtils.getTime7(3);
           dateMap.put("A",date);
           Date time7 = DateUtils.getTime7(5);
           dateMap.put("B",time7);
       }else if(i==4){
           Date date = DateUtils.getTime7(5);
           dateMap.put("A",date);
           Date time7 = DateUtils.getTime7(10);
           dateMap.put("B",time7);
       }else if(i==5){
           Date date = DateUtils.getTime7(5);
           dateMap.put("A",date);
           Date time7 = DateUtils.getTime7(60);
           dateMap.put("B",time7);

       }else {
           Date date = new Date();
           dateMap.put("A",date);
           Date time7 = DateUtils.getTime7();
           dateMap.put("B",time7);
       }

       return dateMap;
   }
}
