package com.wcwy.common.redis.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ClassName: Sole
 * Description:保证数据的唯一性
 * date: 2022/10/26 10:55
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@AllArgsConstructor
@Getter
public enum Sole {
    INSERT_COLLERCT_POST("CollerctPostController"," insertCollerctPost:"),
    INSERT_COLLERCT_POST_DLE("CollerctPostController","insertCollerctPostDle:"),
    INSERT_PUT_IN_RESUME("PutInResumeController"," insertPutInResume:"),
    INSERT_PUT_IN_RESUME_DLE("PutInResumeController"," insertPutInResumeDle:"),//投简
    DELIVER_TJ("PutInResumeController","TJ:"),
    PUT("OrderInfoController","put"),
    SELECT_TJ("PutInResumeController","select:tj"),//推荐官查看求职者信息
    SEND_A_RESUME("PutInResumeController","send_a_resume:"),//推荐官查看求职者信息
    DELIVER_TR("PutInResumeController","TR:"),//推荐官投
    UPDATE_PUT_IN_RESUME("PutInResumeTask","updatePutInResume:"),//推荐官投
    UPDATE_POST_RECORD("PutInResumeServiceImpl","updatePost:record"),//记录岗位操作的次数
    INSERT_OFFER("PutInResumeController","insertOffer:"),//添加offer
    AGAIN_UPDATE_POST_RECORD("TPostShareConsumer","againUpdatePost:record"),//记录岗位失败的操作的数据进行二次消费
    LOGIN_USER("TJobhunterController,TCompanyController,TRecommendController","login:user"),//记录岗位失败的操作的数据进行二次消费
    MESSAGE("WebSocketServer","message:"),//消息通知请求头
    SESSION("WebSocketServer","Session:"),//消息通知请求头
    UPDATE_ORDER("WebSocketServer","update_order:"),//消息通知请求头
    UPDATE_ORDER_INVOICE("ApplyForInvoiceConsumer","updateOrder:invoice"),//更新订单发票
    INSERT_INVOICE("ConfirmConsumer"," insert:invoice"),
    COMPANY("TCompanyPostController","listCompany:"),//存储企业信息
    LIST_COMPANY("TCompanyPostController","list_company:"),//存储企业所以数据
    HOME("TCompanyPostController","home"),//存储企业所以数据
    INSERT_ORDER("ConfirmConsumer","insert:order");//保证订单之被消费一次



    private String kind;//使用的类
    private String key;//文件名
}
