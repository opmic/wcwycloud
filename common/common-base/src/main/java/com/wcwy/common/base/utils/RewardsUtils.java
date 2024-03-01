package com.wcwy.common.base.utils;

import com.wcwy.common.base.result.R;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: RewardsUtils
 * Description:
 * date: 2023/10/10 14:59
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
public class RewardsUtils {

    private final static  String  post="完成发布新职位的奖励";
    private final static  String  login="完成每日登录的奖励";
    private final static  String  register="完成注册的奖励";
    private final static  String  report="完成推荐报告填写及推荐的奖励";
    private final static  String browseReport="完成推荐报告浏览的奖励";
    private final static  String interview="完成面试邀请的奖励";
    private final static  String entry="完成入职邀请的奖励";
    private final static  String offer="完成offer发送的奖励";
    private final static  String shareRegistration="完成邀请链接复制的奖励";
    private final static  String inviter_job_hunter="完成邀请一名求职者注册的奖励";
    private final static  String inviter_company="完成邀请一名企业注册的奖励";
    private final static  String inviter_recommend="完成邀请一名推荐官注册的奖励";


    /**
     * 奖励项目
     * @param type:类型 gold：金额
     * @return
     */
    public static   Map incentiveProject(int type, Integer gold){
        Map map=new HashMap(4);
        map.put("fromUserId","system");
         //发布职位
        if(type==1){

            map.put("msg",post);
            map.put("gold",gold);
        }else if(type==2){
            //登录奖励
            map.put("msg",login);
            map.put("gold",gold);
        }else if(type==3){
            //注册
            map.put("msg",register);
            map.put("gold",gold);
        }else if(type==4){
            //填写推荐报告并投简
            map.put("msg",report);
            map.put("gold",gold);
        }else if(type==5){
            //浏览推荐报告
            map.put("msg",browseReport);
            map.put("gold",gold);
        }else if(type==6){
            //浏览推荐报告
            map.put("msg",interview);
            map.put("gold",gold);
        }else if(type==7){
            //浏览推荐报告
            map.put("msg",entry);
            map.put("gold",gold);
        }else if(type==8){
            //浏览推荐报告
            map.put("msg",offer);
            map.put("gold",gold);
        }else if(type==9){
            //浏览推荐报告
            map.put("msg",shareRegistration);
            map.put("gold",gold);
        }else if(type==10){
            //浏览推荐报告
            map.put("msg",inviter_job_hunter);
            map.put("gold",gold);
        }else if(type==11){
            //浏览推荐报告
            map.put("msg",inviter_recommend);
            map.put("gold",gold);
        }else if(type==12){
            //浏览推荐报告
            map.put("msg",inviter_company);
            map.put("gold",gold);
        }else {
            return null;
        }

        return map;
    }
}
