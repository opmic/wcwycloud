package com.wcwy.common.redis.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ClassName: Cache
 * Description:
 * date: 2023/1/13 8:32
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@AllArgsConstructor
@Getter
public enum Cache {
    CACHE_COMPANY_COLLECT("TCompanyPostController"," cache_company_collect:"),
    CACHE_CITY("TProvincesController","cache_city:"),
    CACHE_HOTS("TCompanyHotController"," cache_hot:"),
    CACHE_INVITER("OrderInfoTask","inviter:"),//缓存邀请人
    CACHE_COMPANY_HOT("TCompanyHotController"," cache_company_hot:"),
    CACHE_PUT_IN_RESUME_RECORD("PutInResumeController"," cache_put_in_resume_record:"),//缓存投简操记录
    JOB_HUNTER_HIDE_COMPANY("TJobhunterHideCompanyServiceImpl","job_hunter_hide_company"),
    CACHE_NEWS("ChatUserController"," cache_news:"),//求职者缓存
    UNREAD_CACHE_NEWS("ChatUserController","unread_cache_news:"),//未读信息
    CACHE_NEWS_ID("ChatUserController"," cache_news_id:"),//推荐官缓存
    RESUME_PAYMENT_CONFIG("ResumePaymentConfigController"," ResumePaymentConfig"),//简历付配置缓存
    CACHE_RECOMMEND("TCompanyController","cache:recommend:"),//推荐官缓存
    CACHE_SOCKET("WebSocketEndpoint","cache_socket:"),//WebSocket缓存
    CACHE_JOB_HUNTER("TJobhunterController","listJobHunter:"),//求职者缓存
    CACHE_POST("TCompanyPostServiceImpl","ListPost:"),
    NEW_JOB_HUNTER("TJobhunterController","NewJobHunter:"),//查询新增人才的缓存
    CACHE_COMPANY("TCompanyController","listCompany:"),
    CACHE_ZAN_TRIBE("CooTribeController","zanTribe:"),//点赞缓存
    CACHE_RECORD_ZAN_TRIBE("CooTribeController","recordZanTribe:"),//点赞的帖子
    CACHE_RECORD_SHARE_TRIBE("CooTribeController","recordShareTribe:"),//分享的帖子
    CACHE_COMMENT_TRIBE("CooCommentController","commentTribe:"),//评论
    CACHE_RECORD_COMMENT_TRIBE("CooCommentController","recordCommentTribe:"),//记录评论
    CACHE_SHARE_TRIBE("CooTribeController","shareTribe:"),//分享缓存

    CACHE_TRIBE_BROWSE("CooTribeController","tribeBrowse:"),//浏览
    CACHE_RECORD_TRIBE_BROWSE("CooTribeController","recordTribeBrowse:"),//浏览帖子
    CACHE_COLLECT_TRIBE("CooCommentController","collectTribe:"),//评论
    CACHE_RECORD_COLLECT_TRIBE("CooCommentController","recordCollectTribe:");//记录评论


    private String kind;//使用的类
    private String key;//文件名
}
