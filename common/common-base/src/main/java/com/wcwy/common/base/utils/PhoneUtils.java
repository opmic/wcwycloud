package com.wcwy.common.base.utils;

/**
 * ClassName: PhoneUtils
 * Description:
 * date: 2023/8/5 11:23
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
public class PhoneUtils {

    /**
     * 电话号码隐藏工具
     * @param phone
     * @return
     */
    public   static  String hidePhoneByRegular(String phone){
        if(StringUtils.isNotBlank(phone) && phone.length() == 11) {
            return phone.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");
        }
        return phone;
    }
}
