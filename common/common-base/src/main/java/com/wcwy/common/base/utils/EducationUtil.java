package com.wcwy.common.base.utils;

/**
 * ClassName: EducationUtil
 * Description:
 * date: 2023/2/15 11:30
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
public class EducationUtil{
    public static  Integer screen(String education){
        if("不限".equals(education)){
            return 0;
        }if("初中及以下".equals(education)){
            return 1;
        }if("中专/中技".equals(education)){
            return 2;
        }if("高中".equals(education)){
            return 3;
        }if("大专".equals(education)){
            return 4;
            }if("本科".equals(education)){
            return 5;
        }if("硕士".equals(education)){
            return 6;
        }if("博士".equals(education)){
            return 7;
        }
        return 0;
    }

}
