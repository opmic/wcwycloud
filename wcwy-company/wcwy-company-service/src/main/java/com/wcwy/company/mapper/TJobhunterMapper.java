package com.wcwy.company.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wcwy.company.dto.RmJobHunterDTO;
import com.wcwy.company.entity.TJobhunter;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wcwy.company.entity.TPermission;
import com.wcwy.company.po.JobHunterShare;
import com.wcwy.company.po.SharePO;
import com.wcwy.company.po.TCompanySharePO;
import com.wcwy.company.po.TJobhunterPO;
import com.wcwy.company.query.ShareQuery;
import com.wcwy.company.query.inviterQuery;
import com.wcwy.company.vo.TJobhunterVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
* @author Administrator
* @description 针对表【t_jobhunter(求职者用户表)】的数据库操作Mapper
* @createDate 2022-10-08 11:58:24
* @Entity com.wcwy.company.entity.TJobhunter
*/
@Mapper
public interface TJobhunterMapper extends BaseMapper<TJobhunter> {

    List<TPermission> rolePermission(@Param("userId") String userId);
    @Select("SELECT t_jobhunter.share_person FROM t_jobhunter WHERE t_jobhunter.user_id =#{jobHunterId} ")
    String getSharePerson(String jobHunterId);

    IPage<TJobhunterPO> listInviterIndustry(@Param("page")IPage page, @Param("inviterQuery") inviterQuery inviterQuery);
    @Select("SELECT t_jobhunter.phone FROM t_jobhunter WHERE t_jobhunter.user_id =#{putInUser} ")
    String selectPhone(String putInUser);

    List<String> getExpectPosition(@Param("userid") String userid);

    SharePO share(@Param("jonHunter") String jonHunter);

    IPage<JobHunterShare> shareJobHunter(@Param("page")IPage page, @Param("shareQuery") ShareQuery shareQuery, @Param("userid") String userid);

    @Select("SELECT referrer_record.recommend_id AS recommendA, t_recommend.share_person AS recommendB FROM t_recommend INNER JOIN referrer_record ON t_recommend.id = referrer_record.recommend_id WHERE referrer_record.t_job_hunter_id = #{jobHunterId} AND referrer_record.correlation_type = 0 ORDER BY referrer_record.create_time DESC LIMIT 1")
    Map<String, String> getSharePersonRecommend(@Param("jobHunterId") String jobHunterId);
    @Select("SELECT wechat_user.nickname FROM t_jobhunter INNER JOIN wechat_user ON t_jobhunter.openid = wechat_user.openid WHERE t_jobhunter.user_id=#{userid}")
    String getWeChat(String userid);

    RmJobHunterDTO selectJobHunter(@Param("jobHunterId") String jobHunterId, @Param("userid") String userid);

    List<Integer> companyInvitationData( @Param("userid") String userid);

    Long mineJobSeeker(@Param("userid") String userid);

    Long mineJobSeekerDay(@Param("userid") String userid);

    int count(@Param("id") String id,@Param("currentStartDate") LocalDateTime currentStartDate,@Param("currentEndTime") LocalDateTime currentEndTime,@Param("city") String city);

    /**
     * 按天获取求职者的注册量
     * @param id
     * @param currentStartDate
     * @param currentEndTime
     * @param city
     * @return
     */
    List<Map> mapList(@Param("id") String id,@Param("currentStartDate") LocalDateTime currentStartDate,@Param("currentEndTime") LocalDateTime currentEndTime,@Param("city") String city);
}




