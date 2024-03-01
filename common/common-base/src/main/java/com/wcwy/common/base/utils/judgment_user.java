package com.wcwy.common.base.utils;

import com.wcwy.common.base.result.R;

/**
 * ClassName: judgment_user
 * Description:判断用户是否是一个人
 * date: 2022/10/24 11:52
 *
 * @author tangzhuo
 * @since JDK 1.8
 */

public class judgment_user {

    public static Boolean UserEql(String loginUserId ,String userId) throws Exception {
        if(! loginUserId.equals(userId)){
           throw new Exception("登录人与id不符合！请检查");
        }
        return true;
    }
}
