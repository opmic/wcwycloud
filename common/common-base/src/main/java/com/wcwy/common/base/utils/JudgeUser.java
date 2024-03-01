package com.wcwy.common.base.utils;

/**
 * ClassName: judgeUser
 * Description:判断用户身份
 * date: 2022/10/26 9:57
 *
 * @author tangzhuo
 * @since JDK 1.8
 */

public class JudgeUser {
    public static  int userIdentity(String userID){
        String substring = userID.substring(0, 2);
        //0企业 1推荐官 2求职者
        if("TC".equals(substring)){
            return 0;
        }else if("TR".equals(substring)) {
            return 1;
        }else if("TJ".equals(substring)) {
            return 3;
        }
        return 4;
    }
}
