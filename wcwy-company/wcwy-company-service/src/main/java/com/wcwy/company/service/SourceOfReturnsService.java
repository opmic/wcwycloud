package com.wcwy.company.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wcwy.company.dto.IdentityEarningsDTO;
import com.wcwy.company.dto.SourceOfReturnsCompanyDTO;
import com.wcwy.company.dto.SourceOfReturnsJobHunterDTO;
import com.wcwy.company.entity.SourceOfReturns;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wcwy.company.query.ShareRevenueDetailsQuery;
import com.wcwy.company.query.SourceOfReturnsCompanyQuery;
import com.wcwy.company.query.SourceOfReturnsQuery;

import java.util.List;
import java.util.Map;

/**
* @author Administrator
* @description 针对表【source_of_returns(分享收益来源详情)】的数据库操作Service
* @createDate 2023-07-19 11:44:01
*/
public interface SourceOfReturnsService extends IService<SourceOfReturns> {


    List<Map> shareData(String userid);

    IPage<SourceOfReturnsJobHunterDTO> selectJobHunter(SourceOfReturnsQuery sourceOfReturnsQuery);


    IPage<SourceOfReturnsCompanyDTO> selectCompany(SourceOfReturnsCompanyQuery sourceOfReturnsQuery);

    String todayProfit(String userid);

    String setMonthProfit(String userid);

    String setSumProfit(String userid);

    /***
     * 获取推荐官的推荐收益及分享收益
     * @param userid
     * @return
     */
    List<Map<Integer, Object>> earnings(String userid);

    /**
     * 查询推荐官推荐过的企业名称
     * @param userid
     * @param keyword
     * @return
     */
    List<String> selectCompanyName(String userid, String keyword);

    /**
     * 根据分享身份获取每个身份的金额
     * @param userid
     * @return
     */
    List<Map<String, Object>> identityEarnings(String userid,String year,String month);

    /**
     *分享收益- 简历下载
     * @param shareRevenueDetailsQuery
     * @return
     */
    IPage<IdentityEarningsDTO> identityEarningsJobHunter(ShareRevenueDetailsQuery shareRevenueDetailsQuery);
    /**
     *分享收益- 推荐官下载
     * @param shareRevenueDetailsQuery
     * @return
     */
    IPage<IdentityEarningsDTO> identityEarningsRecommend(ShareRevenueDetailsQuery shareRevenueDetailsQuery);


    /**
     *分享收益-引入企业
     * @param shareRevenueDetailsQuery
     * @return
     */
    IPage<IdentityEarningsDTO> identityEarningsCompany(ShareRevenueDetailsQuery shareRevenueDetailsQuery);

    /**
     * 分享收益按身份查询
     * @param userid
     * @param year
     * @param month
     * @return
     */
    List<Map<String, Object>> identityShareEarnings(String userid, String year, String month);
}
