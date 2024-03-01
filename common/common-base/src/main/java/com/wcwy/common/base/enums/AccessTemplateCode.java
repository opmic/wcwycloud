package com.wcwy.common.base.enums;

/**
 * @author tangzhuo
 * @ClassName: AccessTemplateCode
 * @Description:
 * @date 2022-08-04
 */
public enum AccessTemplateCode {
    REGISTER(1, "SMS_154950909","阿里云短信测试"),//阿里云测试
    RECHARGE(2, "SMS_461355160","网才无忧"),//网才无忧签名
    WEED_OUT(2, "SMS_276295992","网才无忧"),//面试淘汰
    UPDATE_PHONE_RM(1,"SMS_274700039","网才无忧"),//人才推荐管换绑
    UPDATE_PHONE_TJ(1,"SMS_274700039","网才无忧"),//求职者换绑
    UPDATE_PHONE_AD(1,"SMS_274700039","网才无忧"),//企业换绑
    BINDING_CODE(1,"SMS_274720116","网才无忧"),//绑定验证码
    CANCEL_INTERVIEW(1,"SMS_276510992","网才无忧"),//面试被取消
    UPDATE_INTERVIEW(1,"SMS_276361021","网才无忧"),//面试修改
    ACCEPT_INTERVIEW(1,"SMS_276380997","网才无忧"),//接受面试
    REFUSE_INTERVIEW(1,"SMS_276421063","网才无忧"),//拒绝面试
    ACCEPT_OFFER(1,"SMS_276320978","网才无忧"),//发送offer
    LOGIN_CODE(1,"SMS_461810765","网才无忧"),//登录
    ACCEPT_RESUME(1,"SMS_276491008","网才无忧"),//企业收到简历
    INTERVIEW_NOTICE(2,"SMS_276505984","网才无忧"),//面试通知
    ORDER_NOTICE(2,"SMS_461155037","网才无忧"),//产生订单通知
    ORDER_FORM_INFORM(2,"SMS_461440028","网才无忧"),//通知几笔订单未支付
    INFORM_COMPANY(1,"SMS_463201135","网才无忧"),//企业注册通知
    INFORM_JOB_HUNTER(1,"SMS_463131109","网才无忧"),//求职者注册通知
    INFORM_RECOMMEND(1,"SMS_464250670","网才无忧"),//推荐官注册通知

    CHANGE_PASSWORD(3,"SMS_253495189","网才无忧");//注册



    private  int value;
    private  String desc;
    private  String name;
    AccessTemplateCode(int value, String desc,String name){
        this.value=value;
        this.desc=desc;
        this.name=name;
    }
    public int getValue() {
        return value;
    }
    public String getDesc() {
        return desc;
    }
    public String getName() {
        return name;
    }
}