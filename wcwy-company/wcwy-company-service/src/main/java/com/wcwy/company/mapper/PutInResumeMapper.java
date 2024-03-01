package com.wcwy.company.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wcwy.company.dto.*;
import com.wcwy.company.entity.PutInResume;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wcwy.company.po.PutPostShareDataPO;
import com.wcwy.company.query.*;
import com.wcwy.post.pojo.PutInResumeInterviewPOJO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @description 针对表【put_in_resume(投递简历表)】的数据库操作Mapper
 * @createDate 2022-10-13 17:15:29
 * @Entity com.wcwy.company.entity.PutInResume
 */
@Mapper
public interface PutInResumeMapper extends BaseMapper<PutInResume> {

    Page<PutInResumeInterviewPOJO> pagePutInResumeInterview(@Param("page") IPage page, @Param("putInResumeQuery") PutInResumeQuery putInResumeQuery);

    @Select("SELECT put_in_resume.put_in_resume_id  FROM  put_in_resume WHERE put_in_resume.put_in_user = #{userid}")
    List<String> selectPutInResumeId(String userid);

    @Select("SELECT put_in_resume.put_in_resume_id  FROM  put_in_resume WHERE put_in_resume.put_in_comppany = #{userid}")
    List<String> selectPutInResumeIdTC(String userid);

    @Select("SELECT put_in_resume.put_in_post FROM put_in_resume WHERE put_in_resume.put_in_resume_id  = #{putInResumeId}")
    String getPostId(String putInResumeId);

    List<PutInResumeDTO> listInviteEntry();

    int updateResumeState(@Param("putInResumeId") String putInResumeId, @Param("resumeState") Integer resumeState);

    Page<PutInResumeInterviewPOJO> selectAccountResume(@Param("page") IPage page, @Param("putInResumeQuery") PutInResumeQuery putInResumeQuery);

    RecommendedDataDTO recommendedData(@Param("userid") String userid, @Param("companyId") String companyId, @Param("postId") String postId);

    int receiveResume(@Param("companyId") String companyId);


    Page<PutInResumeInterviewPOJO> sendAResumeInformation(@Param("page") IPage page, @Param("sendAResumeInformationQuery") SendAResumeInformationQuery sendAResumeInformationQuery);

    IPage<SelectCompanyResumeDTO> selectCompanyResume(@Param("selectCompanyResumeQuery") SelectCompanyResumeQuery selectCompanyResumeQuery, @Param("userid") String userid, @Param("page") IPage page);

    Page<PutInResumeEiCompanyPostDTO> getPutInResumeEiCompanyPostDTO(@Param("resumeState") Integer resumeState, @Param("userId") String userId, @Param("page") IPage page);

    CompanyTJobHunterResumeDTO getJobHunter(@Param("putInResumeId") String putInResumeId);

    int interviewPaymentCount(@Param("postId") String postId);


    List<PutInResume> inappropriate();

    @Select("SELECT t_company.contact_name FROM put_in_resume INNER JOIN t_company ON put_in_resume.put_in_comppany = t_company.company_id WHERE put_in_resume.put_in_resume_id =#{putInResumeId}  AND t_company.deleted = 0")
    String getCompanyName(String putInResumeId);

    /**
     * @param putInResumeId:投简id
     * @return null
     * @Description: 获取求职者名字
     * @Author tangzhuo
     * @CreateTime 2023/5/15 15:53
     */

    @Select("SELECT  t_jobhunter.user_name FROM put_in_resume  INNER JOIN  t_jobhunter ON  put_in_resume.put_in_user = t_jobhunter.user_id WHERE put_in_resume.put_in_resume_id = #{putInResumeId}")
    String getJobHunterName(String putInResumeId);

    /**
     * @param putInResumeId:投简id
     * @return null
     * @Description: 获取推荐官名字
     * @Author tangzhuo
     * @CreateTime 2023/5/15 15:53
     */
    @Select("SELECT t_recommend.username FROM put_in_resume INNER JOIN t_recommend ON  put_in_resume.put_in_user = t_recommend.id WHERE put_in_resume.put_in_resume_id =#{putInResumeId} ")
    String getRecommendName(String putInResumeId);

    /**
     * 推荐官获取我的推荐数据
     *
     * @param postId
     * @param userid
     * @return
     */
    @Select("SELECT COUNT(put_in_resume.put_in_resume_id) as quantity, put_in_resume.resume_state FROM put_in_resume WHERE put_in_resume.put_in_post = #{postId} AND put_in_resume.put_in_user =#{userid}   GROUP BY put_in_resume.resume_state")
    List<Map<String, Object>> postDate( @Param("postId") String postId, @Param("userid") String userid);
    @Select("SELECT COUNT(*)  as quantity,  put_in_resume.resume_state FROM put_in_resume WHERE put_in_resume.create_time > ( select DATE_SUB(NOW(),INTERVAL 6 MONTH)) AND put_in_resume.put_in_comppany = #{companyId} GROUP BY put_in_resume.put_in_message")
    List<Map<String, Object>>  companyDate(String companyId);

    IPage<JobHunterPostPutInResumeDTO> recommendAdministrator(@Param("page") IPage page,@Param("recommendAdministratorQuery") RecommendAdministratorQuery recommendAdministratorQuery,@Param("userId") String userId);

    List<String> putInResumeCompany(@Param("keyword") String keyword,@Param("pageSize") Integer pageSize,@Param("userid") String userid);

    List<String> putInResumePostLabel(@Param("keyword") String keyword,@Param("pageSize") Integer pageSize,@Param("userid") String userid);

    PutInResumeParticularsDTO putInResumeParticulars(@Param("putInResumeId") String putInResumeId);

    IPage<HistorySubmitAResumeDTO> historySubmitAResume(@Param("page")IPage page,@Param("userid") String userid, @Param("jobHunterId")String jobHunterId);
    @Select("SELECT COUNT(*) FROM put_in_resume WHERE put_in_resume.put_in_user = #{recommendId} AND (put_in_resume.weed_out_if = 2 OR put_in_resume.exclude_if = 2)")
    int countOut(@Param("recommendId") String recommendId);

    IPage<JobHunterPostPutInResumeDTO> selectPutPost(@Param("page")IPage page,@Param("postId") String postId,@Param("keyword") String keyword,@Param("userId") String userid,@Param("sate") Integer sate);

    String repetition(@Param("phone") String phone,@Param("name") String name,@Param("postId") String postId);

    /**
     * 查看自营岗位投简
     * @param page
     * @param resumeState
     * @param keyword
     * @param userid
     * @param postId
     * @return
     */
    IPage<SelectCompanyResumeDTO> replacePostPutIn(@Param("page") Page page,@Param("resumeState") int resumeState,@Param("keyword") String keyword,@Param("userid") String userid,@Param("postId") String postId);



    /**
     * 查询浏览-按天
     * @param shareDataQuery
     * @return
     */
    List<PutPostShareDataPO> browseDay(@Param("shareDataQuery") ShareDataQuery shareDataQuery);

    /**
     * 查询投简-按天
     * @param shareDataQuery
     * @return
     */
    List<PutPostShareDataPO> slipDay(@Param("shareDataQuery") ShareDataQuery shareDataQuery);
    /**
     * 查询面试-按天
     * @param shareDataQuery
     * @return
     */
    List<PutPostShareDataPO> interviewDay(@Param("shareDataQuery") ShareDataQuery shareDataQuery);

    /**
     * 查询浏览-按周
     * @param shareDataQuery
     * @return
     */
    List<PutPostShareDataPO> browseWeeks(@Param("shareDataQuery")ShareDataQuery shareDataQuery);
    /**
     * 查询投简-按周
     * @param shareDataQuery
     * @return
     */
    List<PutPostShareDataPO> slipWeeks(@Param("shareDataQuery") ShareDataQuery shareDataQuery);
    /**
     * 查询面试-按周
     * @param shareDataQuery
     * @return
     */
    List<PutPostShareDataPO> interviewWeeks(@Param("shareDataQuery") ShareDataQuery shareDataQuery);

    List<PutPostShareDataPO> downloadDay(@Param("shareDataQuery") ShareDataQuery shareDataQuery);

    List<PutPostShareDataPO> offerDay(@Param("shareDataQuery") ShareDataQuery shareDataQuery);

    List<PutPostShareDataPO> fullMoonDay(@Param("shareDataQuery") ShareDataQuery shareDataQuery);

    List<PutPostShareDataPO> downloadWeeks(@Param("shareDataQuery")ShareDataQuery shareDataQuery);

    List<PutPostShareDataPO> offerWeeks(@Param("shareDataQuery")ShareDataQuery shareDataQuery);

    List<PutPostShareDataPO> fullMoonWeeks(@Param("shareDataQuery")ShareDataQuery shareDataQuery);

    List<PutPostShareDataPO> entryDay(@Param("shareDataQuery") ShareDataQuery shareDataQuery);

    List<PutPostShareDataPO> entryWeeks(@Param("shareDataQuery") ShareDataQuery shareDataQuery);

    List<PutPostShareDataPO> browseMonth(@Param("shareDataQuery")ShareDataQuery shareDataQuery);

    List<PutPostShareDataPO> slipMonth(@Param("shareDataQuery")ShareDataQuery shareDataQuery);

    List<PutPostShareDataPO> interviewMonth(@Param("shareDataQuery")ShareDataQuery shareDataQuery);

    List<PutPostShareDataPO> downloadMonth(@Param("shareDataQuery")ShareDataQuery shareDataQuery);

    List<PutPostShareDataPO> entryMonth(@Param("shareDataQuery")ShareDataQuery shareDataQuery);

    List<PutPostShareDataPO> offerMonth(@Param("shareDataQuery")ShareDataQuery shareDataQuery);

    List<PutPostShareDataPO> fullMoonMonth(@Param("shareDataQuery")ShareDataQuery shareDataQuery);

    /**
     * 查询投简记录总数
     * @param id
     * @return
     */
   List< Map<String, Object>> putCount(@Param("id") String id);
    @Select("SELECT put_in_resume.put_in_user as putid,    put_in_resume.put_in_post as postId,  t_jobhunter.user_name  as name ,t_jobhunter.user_id as userId FROM put_in_resume INNER JOIN t_jobhunter ON put_in_resume.put_in_jobhunter = t_jobhunter.user_id WHERE put_in_resume.put_in_resume_id =#{putInResume} ")
    Map basicInformation(@Param("putInResume") String putInResume);

    /**
     *
     * @param id
     * @param currentStartDate
     * @param currentEndTime
     * @param city
     * @param type
     * @return
     */
    Long selectCounts(@Param("id") String id,@Param("currentStartDate") LocalDateTime currentStartDate,@Param("currentEndTime") LocalDateTime currentEndTime,@Param("city") String city,@Param("type") int type,@Param("resumeState") int resumeState);

    List<Map> mapListCount(@Param("id") String id,@Param("currentStartDate") LocalDateTime currentStartDate,@Param("currentEndTime") LocalDateTime currentEndTime,@Param("city") String city,@Param("type") int type,@Param("resumeState") int resumeState);
}




