package com.wcwy.common.base.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ClassName: PhoneEmailUtils
 * Description:
 * date: 2023/10/17 9:07
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
public class PhoneEmailUtils {

    public static boolean verify(String text){
        //爬取电话号码，邮箱以及手机号
        String regex = "(0\\d{2,6}-?\\d{5,20})|(\\w{1,30}@[0-9a-zA-Z]{2,20}(\\.[0-9a-zA-Z]{2,20}){1,2})" +
                "|(1[3-9]\\d{9})|(400-?\\d{3,9}-?\\d{3,9})";

        //将爬取规则编译成匹配对象
        Pattern pattern = Pattern.compile(regex);

        //获取一个容器来存取匹配对象
        Matcher matcher = pattern.matcher(text);
        boolean isVerify=false;
        //开始匹配
        while (matcher.find()){
            //String re = matcher.group();
            isVerify=true;
            continue;
        }
        isVerify = text.contains("微信");
        isVerify = text.contains("wx");
        isVerify = text.contains("WX");
        isVerify = text.contains("VX");
        isVerify = text.contains("vx");
        return isVerify;
    }
}
