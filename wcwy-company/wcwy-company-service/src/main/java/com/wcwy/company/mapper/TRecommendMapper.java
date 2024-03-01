package com.wcwy.company.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wcwy.company.dto.RecommendDataDTO;
import com.wcwy.company.entity.TPermission;
import com.wcwy.company.entity.TRecommend;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wcwy.company.po.JobHunterShare;
import com.wcwy.company.po.TRecommendPO;
import com.wcwy.company.po.TRecommendShare;
import com.wcwy.company.query.ShareQuery;
import com.wcwy.company.query.TRecommendQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
* @author Administrator
* @description 针对表【t_recommend(推荐官)】的数据库操作Mapper
* @createDate 2022-09-07 14:02:34
* @Entity com.wcwy.company.entity.TRecommend
*/
@Mapper
public interface TRecommendMapper extends BaseMapper<TRecommend> {

    List<TPermission> rolePermission(@Param("recommendId") String recommendId);

    IPage<TRecommendPO> pageList(@Param("page")  IPage page,@Param("tRecommendQuery")  TRecommendQuery tRecommendQuery);
    @Select("SELECT t_company.phone_number FROM  t_company WHERE t_company.company_id = #{companyId}")
    String selectPhone(String companyId);

    IPage<TRecommendShare> shareRecommend(@Param("page") IPage page, @Param("shareQuery") ShareQuery shareQuery, @Param("userid") String userid);
    @Select("SELECT	t_recommend.phone FROM	t_recommend WHERE	t_recommend.id = #{userId}" )
    String phoneNumbers(@Param("userId")String userId);
    @Select("SELECT t_recommend.id as recommendA, t_recommend.share_person as recommendB FROM t_recommend WHERE t_recommend.id =#{id}")
    Map<String, Object> getSharePersonRecommend(String id);

    List<Map> graph(@Param("userid")String userid,@Param("year") String year);

    RecommendDataDTO getRecommendDataDTO(@Param("userid") String userid);

    TRecommend selectBasic(@Param("id") String id);

    @Select("SELECT t_recommend.id FROM t_recommend WHERE t_recommend.parent_id=#{userid} and t_recommend.subsidiary=1")
    List<String> getMember(@Param("userid") String userid);

    TRecommend enterpriseInviter(@Param("companyId") String companyId);

    int count(@Param("id") String id,@Param("currentStartDate") LocalDateTime currentStartDate,@Param("currentEndTime") LocalDateTime currentEndTime,@Param("city") String city,@Param("identity") int identity);

    /**
     * 按天查询总量
     * @param id
     * @param currentStartDate
     * @param currentEndTime
     * @param city
     * @param identity
     * @return
     */
    List<Map> mapList(@Param("id") String id,@Param("currentStartDate") LocalDateTime currentStartDate,@Param("currentEndTime") LocalDateTime currentEndTime,@Param("city") String city,@Param("identity") int identity);

    /**
     * 获取团队的
     * @param list
     * @return
     */
   /* List<RecommendDataDTO> getMemberRecommendDataDTO(@Param("list") List<String> list);*/
}




