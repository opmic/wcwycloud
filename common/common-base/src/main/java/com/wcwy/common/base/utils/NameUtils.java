package com.wcwy.common.base.utils;

/**
 * ClassName: NameUtils
 * Description:
 * date: 2023/8/5 11:02
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
public class NameUtils {
    //生成很多个*号
    public static String createAsterisk(String name,int length) {
        if (name.length() <= 1) {
            System.out.println("*");
        } else {
            name=   name.replaceAll("([\\u4e00-\\u9fa5]{1})(.*)", "$1" + createAsterisk(name.length() - 1));
           return name;
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < length; i++) {
            stringBuffer.append("*");
        }
        return stringBuffer.toString();
    }
    public static String  createAsterisk(int length) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < length; i++) {
            stringBuffer.append("*");
        }
        return stringBuffer.toString();
    }
}
