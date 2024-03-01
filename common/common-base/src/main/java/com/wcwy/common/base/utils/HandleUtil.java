package com.wcwy.common.base.utils;

/**
 * ClassName: HandleUtil
 * Description:处理人
 * date: 2023/12/11 16:24
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
public class HandleUtil {

    public static int handle(String uerId){
        String substring = uerId.substring(0, 2);
        if("TC".equals(substring)){
            return 0;
        }else if("TR".equals(substring)){
            return 1;
        }
        return 2;
    }
}
