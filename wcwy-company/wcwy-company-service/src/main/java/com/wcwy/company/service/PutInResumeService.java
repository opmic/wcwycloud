package com.wcwy.company.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wcwy.common.base.query.PageQuery;
import com.wcwy.company.dto.*;
import com.wcwy.company.entity.PutInResume;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wcwy.company.po.PutPostShareDataPO;
import com.wcwy.company.query.*;
import com.wcwy.post.entity.TPostShare;
import com.wcwy.post.pojo.PutInResumeInterviewPOJO;
import org.springframework.scheduling.annotation.Async;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
* @author Administrator
* @description 针对表【put_in_resume(投递简历表)】的数据库操作Service
* @createDate 2022-10-13 17:15:29
*/
public interface PutInResumeService extends IService<PutInResume> {


    Page<PutInResumeInterviewPOJO> pagePutInResumeInterview(PutInResumeQuery putInResumeQuery);

    List<String> selectPutInResumeId( String userid);

    List<String> selectPutInResumeIdTC(String userid);

    String getPostId(String putInResumeId);

    /**
     * 录岗位发布产生的记录
     */
    @Async
    void updateRedisPost(String jobHunterId,String post ,Integer state ,String putInUser);

    /**
     * 取消岗位记录
     * @param jobHunter
     * @param post
     * @param state
     * @param putInUser
     */
    @Async
    void cancelRedisPost(String jobHunter,String post, Integer state, String putInUser);
    @Async
    TPostShare selectPost(String post);
    List<PutInResumeDTO> listInviteEntry();
    /**
     * @Description: 更新投简状态
     * @param putInResumeId 投简id
     * @param resumeState :状态
     * @return null
     * @Author tangzhuo
     * @CreateTime 2022/11/29 8:45
     */

  /*  @Async*/
    void updateResumeState(String putInResumeId,Integer resumeState);

    Page<PutInResumeInterviewPOJO> selectAccountResume(PutInResumeQuery putInResumeQuery);

    RecommendedDataDTO recommendedData(String userid,String companyId,String postId);

    int receiveResume(String companyId);

    Page<PutInResumeInterviewPOJO> sendAResumeInformation(SendAResumeInformationQuery sendAResumeInformationQuery);

    /**
     * 查询企业的岗位投简
     * @param selectCompanyResumeQuery
     * @param userid
     * @return
     */
    IPage<SelectCompanyResumeDTO> selectCompanyResume(SelectCompanyResumeQuery selectCompanyResumeQuery, String userid);

    /**
     * 求职者查看我的申请
     * @param resumeState
     * @return
     */
    Page<PutInResumeEiCompanyPostDTO> getPutInResumeEiCompanyPostDTO(Integer resumeState, String userId,Page page);

    /**
     * 企业获取投简者信息
     * @param putInResumeId
     * @return
     */
    CompanyTJobHunterResumeDTO getJobHunter(String putInResumeId);

    /*修改面试时间*/
  void   updateInterview(String putInResumeId,LocalDateTime interviewTime);

    /**
     * 淘汰这份投简
     * @param putInResumeId
     */
    void weedOut(String putInResumeId,String cause);

    /**
     * 查询到面付是否有投简未处理
     * @return
     */
    int interviewPaymentCount(String postId);

    /**
     * 获取下载或浏览7天未操作的的投简
     * @return
     */
    List<PutInResume> inappropriate();

    /**
     * @Description: 获取企业联系人名称
     * @param putInResumeId：投简id
     * @return null
     * @Author tangzhuo
     * @CreateTime 2023/5/15 13:45
     */

    String getCompanyName(String putInResumeId);
    /**
     * @Description: 获取投简人名称
     * @param putInResumeId：投简id
     * @return null
     * @Author tangzhuo
     * @CreateTime 2023/5/15 13:45
     */
    String getPutName(String putInResumeId);

    Map<String, Long> postDate(String postId,String userId);

    /**
     * 获取企业近6个月的推荐数据
     * @param companyId
     * @return
     */
    Map<String, Long> companyDate(String companyId);

    /**
     * 获取推荐管理
     * @param recommendAdministratorQuery
     * @return
     */
    IPage<JobHunterPostPutInResumeDTO> recommendAdministrator(RecommendAdministratorQuery recommendAdministratorQuery,String userId);

    /**
     * 获取投简的企业
     * @param keyword
     * @param pageSize
     * @param userid
     * @return
     */
    List<String> putInResumeCompany(String keyword, Integer pageSize, String userid);
    /**
     * 获取投简岗位
     * @param keyword
     * @param pageSize
     * @param userid
     * @return
     */
    List<String> putInResumePostLabel(String keyword, Integer pageSize, String userid);

    /**
     *
     * @param putInResumeId
     * @return
     */
    PutInResumeParticularsDTO putInResumeParticulars(String putInResumeId);



    /**
     * 推荐官查询个人求职者历史推荐数据
     * @param pageQuery
     * @return
     */
    IPage<HistorySubmitAResumeDTO> historySubmitAResume(PageQuery pageQuery, String userid, String jobHunterId);

    /**
     * 更新投简消息标识
     * @param userid
     * @param putInResumeId
     */

    void updateMessage(String userid, String putInResumeId);

    /**
     *
     * @param recommendId
     * @return
     */
    int countOut(String recommendId);


    /**
     * 查询接单岗位的数据
     * @param postId
     * @param keyword
     * @param userid
     * @return
     */
    IPage<JobHunterPostPutInResumeDTO> selectPutPost(Page page, String postId, String keyword, String userid,Integer sate);

    /**
     * 查询接单的面试推荐offer入职淘汰数量
     * @param postId
     * @param userid
     * @return
     */
    RecommendedDataDTO recommendedPostData(String userid, String postId);

    /***
     * 查重接口
     * @param phone
     * @param name
     * @param postId
     * @return
     */
    String repetition(String phone, String name, String postId);


    /***
     * 推荐官代招职位查看投简
     * @param resumeState
     * @param keyword
     * @param userid
     * @return
     */
    IPage<SelectCompanyResumeDTO> replacePostPutIn(Page page, int resumeState, String keyword, String userid, String postId);

    /**
     *浏览-按天
     * @param shareDataQuery
     * @return
     */
    List<PutPostShareDataPO> browseDay(ShareDataQuery shareDataQuery);

    /**
     * 投简-按天
     * @param shareDataQuery
     * @return
     */
    List<PutPostShareDataPO> slipDay(ShareDataQuery shareDataQuery);

    /**
     * 面试-按天
     * @param shareDataQuery
     * @return
     */
    List<PutPostShareDataPO> interviewDay(ShareDataQuery shareDataQuery);
    /**
     *浏览-按周
     * @param shareDataQuery
     * @return
     */
    List<PutPostShareDataPO> browseWeeks(ShareDataQuery shareDataQuery);
    /**
     *投简-按周
     * @param shareDataQuery
     * @return
     */
    List<PutPostShareDataPO> slipWeeks(ShareDataQuery shareDataQuery);
    /**
     *面试-按周
     * @param shareDataQuery
     * @return
     */
    List<PutPostShareDataPO> interviewWeeks(ShareDataQuery shareDataQuery);


    List<PutPostShareDataPO> downloadDay(ShareDataQuery shareDataQuery);

    List<PutPostShareDataPO> offerDay(ShareDataQuery shareDataQuery);

    List<PutPostShareDataPO> fullMoonDay(ShareDataQuery shareDataQuery);

    List<PutPostShareDataPO> downloadWeeks(ShareDataQuery shareDataQuery);

    List<PutPostShareDataPO> offerWeeks(ShareDataQuery shareDataQuery);

    List<PutPostShareDataPO> fullMoonWeeks(ShareDataQuery shareDataQuery);

    List<PutPostShareDataPO> entryDay(ShareDataQuery shareDataQuery);

    List<PutPostShareDataPO> entryWeeks(ShareDataQuery shareDataQuery);

    List<PutPostShareDataPO> browseMonth(ShareDataQuery shareDataQuery);

    List<PutPostShareDataPO> slipMonth(ShareDataQuery shareDataQuery);

    List<PutPostShareDataPO> interviewMonth(ShareDataQuery shareDataQuery);

    List<PutPostShareDataPO> downloadMonth(ShareDataQuery shareDataQuery);

    List<PutPostShareDataPO> entryMonth(ShareDataQuery shareDataQuery);

    List<PutPostShareDataPO> offerMonth(ShareDataQuery shareDataQuery);

    List<PutPostShareDataPO> fullMoonMonth(ShareDataQuery shareDataQuery);

    /**
     * 查询所投递的职位
     * @param id：推荐官id
     * @return
     */
    List<Map<String, Object>> putCount(String id);


    /**
     * 获取求职者姓名及投简人
     * @param putInResume
     * @return
     */
    Map basicInformation(String putInResume);

    /**
     *
     * @param id:推荐官id
     * @param currentStartDate
     * @param currentEndTime
     * @param city
     * @param type 岗位类型
     * @param resumeState 1：浏览 2：下载
     * @return
     */
    Long selectCounts(String id, LocalDateTime currentStartDate, LocalDateTime currentEndTime, String city, int type,int resumeState);

    List<Map> mapListCount(String id, LocalDateTime currentStartDate, LocalDateTime currentEndTime, String city, int type, int resumeState);
}
