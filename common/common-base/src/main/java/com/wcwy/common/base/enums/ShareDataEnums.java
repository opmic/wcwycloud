package com.wcwy.common.base.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * ClassName: ShareDataEnums
 * Description:
 * date: 2023/8/25 15:32
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@AllArgsConstructor
@Getter
public enum ShareDataEnums {
    SD_USER_ID("sd_user_id:"),//用户
    INSERT_JOB_HUNTER ("insert_job_hunter:"),//求职者注册
    DOWNLOAD_POST ("sb_download_post:"),//职位下载简历
    INSERT_USER ("sd_insert_user:"),//用户注册
    INVITE_LINK ("invite_link:"),//邀请次数记录
    INSERT_POST("insert_post:"),//岗位求职者注册
    INSERT_HUNT_COMPANY("insert_hunt_company:"),//猎企统计
    INSERT_COMPANY("insert_company:"),//企业统计
    INSERT_WORK_RECOMMEND("insert_work_recommend:"),//猎企统计
    INSERT_CAMPUS_RECOMMEND("insert_campus_recommend:"),//企业统计
    SD_POST_SHARING("sd_post_sharing:"),//分享岗位次数
    SD_POST_SHARING_RECOMMEND("sd_post_sharing_recommend:");//链接记录
    private String shareData;
}
