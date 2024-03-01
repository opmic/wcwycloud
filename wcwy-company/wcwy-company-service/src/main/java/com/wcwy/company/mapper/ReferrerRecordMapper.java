package com.wcwy.company.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wcwy.company.dto.RMDownloadDTO;
import com.wcwy.company.dto.ReferrerRecordJobHunterDTO;
import com.wcwy.company.entity.ReferrerRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wcwy.company.query.RMDownloadQuery;
import com.wcwy.company.query.ReferrerRecordQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
* @author Administrator
* @description 针对表【referrer_record(推荐人数据记录)】的数据库操作Mapper
* @createDate 2022-12-28 10:04:50
* @Entity com.wcwy.company.entity.ReferrerRecord
*/
@Mapper
public interface ReferrerRecordMapper extends BaseMapper<ReferrerRecord> {

    ReferrerRecord referrerRecordSUM(@Param("userid") String userid);
    @Select("SELECT COUNT(*) as talents, referrer_record.correlation_type as correlationType FROM referrer_record WHERE referrer_record.recommend_id = #{userid} GROUP BY referrer_record.correlation_type ")
    List<Map> talents(String userid);

    IPage<ReferrerRecordJobHunterDTO> selectReferrerRecordJobHunter(@Param("page") IPage page,@Param("referrerRecordQuery")  ReferrerRecordQuery referrerRecordQuery ,@Param("userid") String userid);
    List<String> expectation(@Param("keyword") String keyword,@Param("pageSize") Integer pageSize,@Param("userid") String userid);

    IPage<RMDownloadDTO> selectDownload(@Param("page") IPage page,@Param("rmDownloadQuery") RMDownloadQuery rmDownloadQuery,@Param("userid") String userid);

    List<Map> graph(@Param("userid") String userid,@Param("year") String year);

    int countJobHunter(@Param("userid")String userid);
}




