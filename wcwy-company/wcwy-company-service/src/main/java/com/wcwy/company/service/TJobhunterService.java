package com.wcwy.company.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wcwy.common.base.result.R;
import com.wcwy.company.dto.ReferrerRecordJobHunterDTO;
import com.wcwy.company.dto.RmJobHunterDTO;
import com.wcwy.company.entity.TJobhunter;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wcwy.company.entity.TPermission;
import com.wcwy.company.po.JobHunterShare;
import com.wcwy.company.po.SharePO;
import com.wcwy.company.po.TCompanySharePO;
import com.wcwy.company.po.TJobhunterPO;
import com.wcwy.company.query.CompanyInviteJobHunterQuery;
import com.wcwy.company.query.ShareQuery;
import com.wcwy.company.query.inviterQuery;
import com.wcwy.company.vo.TJobhunterVO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
* @author Administrator
* @description 针对表【t_jobhunter(求职者用户表)】的数据库操作Service
* @createDate 2022-10-08 11:58:24
*/
public interface TJobhunterService extends IService<TJobhunter> {
    /**
     * 获取求职者权限
     * */
    List<TPermission> rolePermission(String userId);

    String getSharePerson(String jobHunterId);

    IPage<TJobhunterPO> listInviterIndustry(inviterQuery inviterQuery);

    String selectPhone(String putInUser);

    Integer talentsCount(String recommend);
    /**
     * @Description: 验证修改身份是否合法
     * @param loginUserId:登录id
     * @param userId:求职者id
     * @return null
     * @Author tangzhuo
     * @CreateTime 2023/2/2 15:26
     */
    public R jurisdiction(String loginUserId, String userId);

    List<String> getExpectPosition(String userid);

    /**
     * 获取求职者目前年薪+邀请注册人+推荐官邀请人
     * @param jonHunter
     * @return
     */
    SharePO share(String jonHunter);

    /**
     * 获取求职者分享提成消息
     * @param shareQuery
     * @param userid
     * @return
     */
    IPage<JobHunterShare> shareJobHunter(ShareQuery shareQuery, String userid);
    /**
     * @Description:  查询企业的邀请及推荐官的邀请人
     * @param jobHunterId  求职者id
     * @return Map
     * @Author tangzhuo
     * @CreateTime 2023/4/21 11:21
     */

    Map<String, String> getSharePersonRecommend(String jobHunterId);

    /**
     * 获取微信名称
     * @param userid
     * @return
     */
    String getWeChat(String userid);

    RmJobHunterDTO selectJobHunter(String jobHunterId, String userid);

    /**
     * 判断求职者简历是否完善
     * @param userid
     * @return
     */
    R<Map> isSendAResume(String userid);

    /**
     * 人才私域
     * @param companyInviteJobHunterQuery
     * @return
     */
    IPage<ReferrerRecordJobHunterDTO> companyInviteJobHunter(CompanyInviteJobHunterQuery companyInviteJobHunterQuery);

    List<Integer> companyInvitationData(String userid);

    /**
     * 查看简历完善度
     * @param userid
     * @return
     */
    Map createPerfectDegree(String userid);


    /**
     * 我的人才
     * @param userid
     * @return
     */
    Long mineJobSeeker(String userid);

    /**
     * 今日增长
     * @param userid
     * @return
     */
    Long mineJobSeekerDay(String userid);

    /**
     * 查询求职者
     * @param jobHunter
     * @return
     */
    String byId(String jobHunter);

    /**
     *
     * 查询邀请求职者
     * @param id
     * @param currentStartDate
     * @param currentEndTime
     * @param city
     * @return
     */
    int selectCount(String id, LocalDateTime currentStartDate, LocalDateTime currentEndTime, String city);

    /**
     * 按天获取求职者的注册量
     * @param id
     * @param currentStartDate
     * @param currentEndTime
     * @param city
     * @return
     */
    List<Map> mapList(String id, LocalDateTime currentStartDate, LocalDateTime currentEndTime, String city);
}
