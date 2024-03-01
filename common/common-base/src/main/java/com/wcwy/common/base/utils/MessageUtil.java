package com.wcwy.common.base.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: MessageUtil
 * Description:消息通知机器人及模版
 * date: 2023/12/29 9:30
 *
 * @author tangzhuo
 * @since JDK 1.8
 */

public class MessageUtil {

    //SY001 无忧福利官 SY002 无忧小秘书(通知) SY003 无忧小秘书(举报) SY004 无忧小助手(举报)
    public static  String assistant="SY001";
    public static  String assistant1="SY002";
    public static  String assistant2="SY003";
    public static  String assistant4="SY004";


    /**
    * 求职者模块
    * */
    //简历完善通知
    public static Map SY004_TJ_PERFECT_YOUR_RESUME=new HashMap<String,Object>(){
        {
            put("content","亲爱的人才用户朋友，请优化和完善你的简历，因一份完善的简历能让你获得更多企业和猎头顾问的青睐。");
            put("type", "0");
            put("title","简历完善通知");
            put("fromId", "SY004");
            put("messageType", "0");
            put("timestamp", System.currentTimeMillis());
        }
    };


    public static Map SY004_TJ_CANCEL_THE_INTERVIEW=new HashMap<String,Object>(){
        {
            put("content","");
            put("type", "0");
            put("title","面试取消通知");
            put("fromId", "SY004");
            put("messageType", "0");
            put("timestamp", System.currentTimeMillis());
        }
    };
    //被推荐通知
    public static Map SY004_TJ_BE_RECOMMENDED=new HashMap<String,Object>(){
        {
            put("content","");
            put("type", "0");
            put("title","被推荐通知");
            put("fromId", "SY004");
            put("messageType", "0");
            put("timestamp", System.currentTimeMillis());
        }
    };
    //被推荐通知
    public static Map SY003_TJ_REPORT_ME=new HashMap<String,Object>(){
        {
            put("content","亲爱的网才用户，由于收到用户举报，你在平台的在线简历工作经验及学历造假，存在过分包装简历的行为，已违反了网才无忧平台相关规范，将通知你在五个工作日内对简历进行规范且真实的信息弥补，若未及时处理，平台将对你的简历进行下架处理，若有任何疑问可通过服务热线:0769-8559 5466了解更多详情。");
            put("type", "0");
            put("fromId", "SY003");
            put("title","举报我的");
            put("messageType", "0");
            put("timestamp", System.currentTimeMillis());
        }
    };
    //简历完善通知
    public static Map SY004_DIE_OUT=new HashMap<String,Object>(){
        {
            put("content","");
            put("type", "0");
            put("fromId", "SY004");
            put("title","淘汰通知");
            put("messageType", "0");
            put("timestamp", System.currentTimeMillis());
        }
    };

    //面试修改通知
    public static Map SY004_TJ_UPDATE_INTERVIEW=new HashMap<String,Object>(){
        {
            put("content","");
            put("type", "0");
            put("fromId", "SY004");
            put("title","面试修改通知");
            put("messageType", "0");
            put("timestamp", System.currentTimeMillis());
        }
    };

    //面试修改通知
    public static Map SY004_TJ_INTERVIEW=new HashMap<String,Object>(){
        {
            put("content","");
            put("type", "0");
            put("fromId", "SY004");
            put("title","邀请面试通知");
            put("messageType", "0");
            put("timestamp", System.currentTimeMillis());
        }
    };
    //简历完善通知
    public static Map SY004_TJ_OFFER=new HashMap<String,Object>(){
        {
            put("content","");
            put("type", "0");
            put("fromId", "SY004");
            put("title","OFFER通知");
            put("messageType", "0");
            put("timestamp", System.currentTimeMillis());
        }
    };

    //金币奖励
    public static Map SY001_GOLD_REWARD=new HashMap<String,Object>(){
        {
            put("content","恭喜你获得金币%s，可在资产管理进行查看");
            put("type", "0");
            put("fromId", "SY001");
            put("title","金币奖励");
            put("messageType", "0");
            put("timestamp", System.currentTimeMillis());
        }
    };

    //金币奖励
    public static Map SY001_TJ_GOLD_REWARD_EXPIRATION=new HashMap<String,Object>(){
        {
            put("content","尊敬的用户朋友，你的金币将于%s失效，请在有效期内及时进行处理。");
            put("type", "0");
            put("fromId", "SY001");
            put("title","金币奖励到期提醒");
            put("messageType", "0");
            put("timestamp", System.currentTimeMillis());
        }
    };




    /*====================================企业===================================================*/

    /**
     * 岗位审核通过通知
     */
    public static Map SY002_VERIFIED_POST=new HashMap<String,Object>(){
        {
            put("content","恭喜，你的岗位已通过审核，祝你早日招聘到合适的人才。");
            put("type", "0");
            put("fromId", "SY002");
            put("title","岗位审核通知");
            put("messageType", "0");
            put("timestamp", System.currentTimeMillis());
        }
    };
    /**
     * 岗位审核通过通知
     */
    public static Map SY003_MY_REPORT=new HashMap<String,Object>(){
        {
            put("content","");
            put("type", "0");
            put("fromId", "SY003");
            put("title","我的举报");
            put("messageType", "0");
            put("timestamp", System.currentTimeMillis());
        }
    };
    /**
     * 岗位审核通过通知
     */
    public static Map SY004_BE_INTERVIEWED=new HashMap<String,Object>(){
        {
            put("content","");
            put("type", "0");
            put("fromId", "SY004");
            put("title","接受面试通知");
            put("messageType", "0");
            put("timestamp", System.currentTimeMillis());
        }
    };
    public static Map SY004_BE_NOT_INTERVIEWED=new HashMap<String,Object>(){
        {
            put("content","");
            put("type", "0");
            put("fromId", "SY004");
            put("title","未接受面试通知");
            put("messageType", "0");
            put("timestamp", System.currentTimeMillis());
        }
    };
 /*   public static Map SY004_LESS_THAN_A_MONTH=new HashMap<String,Object>(){
        {
            put("content","");
            put("type", "0");
            put("fromId", "SY004");
            put("title","未满月反馈通知");
            put("messageType", "0");
            put("timestamp", System.currentTimeMillis());
        }
    };*/
        public static Map SY004_TC_UPDATE=new HashMap<String,Object>(){
        {
            put("content","");
            put("type", "0");
            put("fromId", "SY004");
            put("title","信息更新通知");
            put("messageType", "0");
            put("timestamp", System.currentTimeMillis());
        }
    };
    public static Map SY004_TC_SEND_A_RESUME=new HashMap<String,Object>(){
        {
            put("content","");
            put("type", "0");
            put("fromId", "SY004");
            put("title","投简通知");
            put("messageType", "0");
            put("timestamp", System.currentTimeMillis());
        }
    };
    public static Map SY004_TC_REPORT_ME=new HashMap<String,Object>(){
        {
            put("content","");
            put("type", "0");
            put("fromId", "SY004");
            put("title","举报我的");
            put("messageType", "0");
            put("timestamp", System.currentTimeMillis());
        }
    };

    public static  Map SY004_INTERVIEW_PRESENCE=new HashMap<String,Object>(){
        {
            put("content","");
            put("type", "0");
            put("fromId", "SY004");
            put("title","到场面试通知");
            put("messageType", "0");
            put("timestamp", System.currentTimeMillis());
        }
    };

    public static  Map SY002_NOTICE_OF_EXPIRATION=new HashMap<String,Object>(){
        {
            put("content","客户朋友,你好；你的服务合同于2024年4月12日到期，请提前30天进行续期处理。");
            put("type", "0");
            put("fromId", "SY002");
            put("title","合同到期通知");
            put("messageType", "0");
            put("timestamp", System.currentTimeMillis());
        }
    };

    public static  Map SY002_ORDER_PAYMENT=new HashMap<String,Object>(){
        {
            put("content","");
            put("type", "0");
            put("fromId", "SY002");
            put("title","订单支付通知");
            put("messageType", "0");
            put("timestamp", System.currentTimeMillis());
        }
    };

public static  int identity(String id){
    String substring = id.substring(0, 2);
    if("TJ".equals(substring)){
        return 2;
    }else if("TR".equals(substring)){
        return 1;
    }
    return 0;
}

    //=====================================================推荐官=============================
    /*
    * 订单支付通知
    * */
    public static  Map SY002_TOP_UP=new HashMap<String,Object>(){
        {
            put("content","");
            put("type", "0");
            put("fromId", "SY002");
            put("title","订单支付通知");
            put("messageType", "0");
            put("timestamp", System.currentTimeMillis());
        }
    };


    /*
     * 入职通知
     * */
    public static  Map SY004_ENTRY=new HashMap<String,Object>(){
        {
            put("content","");
            put("type", "0");
            put("fromId", "SY004");
            put("title","入职通知");
            put("messageType", "0");
            put("timestamp", System.currentTimeMillis());
        }
    };

    /*
     * 入职通知
     * */
    public static  Map SY004_NOT_ENTRY=new HashMap<String,Object>(){
        {
            put("content","");
            put("type", "0");
            put("fromId", "SY004");
            put("title","未入职申请通知");
            put("messageType", "0");
            put("timestamp", System.currentTimeMillis());
        }
    };

    /*
     * 未满月申请通知
     * */
    public static  Map SY004_FULL_MOON=new HashMap<String,Object>(){
        {
            put("content","");
            put("type", "0");
            put("fromId", "SY004");
            put("title","满月通知");
            put("messageType", "0");
            put("timestamp", System.currentTimeMillis());
        }
    };
    /*
     * 未满月申请通知
     * */
    public static  Map SY004_NOT_FULL_MOON=new HashMap<String,Object>(){
        {
            put("content","");
            put("type", "0");
            put("fromId", "SY004");
            put("title","未满月申请通知");
            put("messageType", "0");
            put("timestamp", System.currentTimeMillis());
        }
    };
}
