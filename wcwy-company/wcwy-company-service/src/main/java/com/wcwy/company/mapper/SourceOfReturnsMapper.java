package com.wcwy.company.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wcwy.company.dto.IdentityEarningsDTO;
import com.wcwy.company.dto.SourceOfReturnsCompanyDTO;
import com.wcwy.company.dto.SourceOfReturnsJobHunterDTO;
import com.wcwy.company.entity.SourceOfReturns;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wcwy.company.query.ShareRevenueDetailsQuery;
import com.wcwy.company.query.SourceOfReturnsCompanyQuery;
import com.wcwy.company.query.SourceOfReturnsQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
* @author Administrator
* @description 针对表【source_of_returns(分享收益来源详情)】的数据库操作Mapper
* @createDate 2023-07-19 11:44:01
* @Entity com.wcwy.company.entity.SourceOfReturns
*/
@Mapper
public interface SourceOfReturnsMapper extends BaseMapper<SourceOfReturns> {


    List<Map> shareDate(@Param("userid") String userid);

    IPage<SourceOfReturnsJobHunterDTO> selectJobHunter(@Param("page") IPage page,@Param("sourceOfReturnsQuery") SourceOfReturnsQuery sourceOfReturnsQuery);

    IPage<SourceOfReturnsCompanyDTO> selectCompany(@Param("page") IPage page,@Param("sourceOfReturnsQuery") SourceOfReturnsCompanyQuery sourceOfReturnsQuery);

    String todayProfit(@Param("userid")String userid);

    String setMonthProfit(@Param("userid") String userid);

    String setSumProfit(@Param("userid") String userid);

    List<Map<Integer, Object>> earnings(@Param("userid")String userid);

    List<String> selectCompanyName(@Param("userid") String userid,@Param("keyword") String keyword);


    List<Map<String, Object>> identityEarnings(@Param("userid") String userid,@Param("year") String year,@Param("month") String month);

    IPage<IdentityEarningsDTO> identityEarningsJobHunter(@Param("page") IPage page,@Param("shareRevenueDetailsQuery") ShareRevenueDetailsQuery shareRevenueDetailsQuery);


    IPage<IdentityEarningsDTO> identityEarningsRecommend(@Param("page") IPage page,@Param("shareRevenueDetailsQuery") ShareRevenueDetailsQuery shareRevenueDetailsQuery);

    IPage<IdentityEarningsDTO> identityEarningsCompany(@Param("page") IPage page,@Param("shareRevenueDetailsQuery")ShareRevenueDetailsQuery shareRevenueDetailsQuery);

    List<Map<String, Object>> identityShareEarnings(@Param("userid") String userid,@Param("year") String year,@Param("month") String month);
}




