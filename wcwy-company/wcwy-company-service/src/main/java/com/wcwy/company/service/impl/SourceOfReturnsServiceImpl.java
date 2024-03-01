package com.wcwy.company.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wcwy.company.dto.IdentityEarningsDTO;
import com.wcwy.company.dto.SourceOfReturnsCompanyDTO;
import com.wcwy.company.dto.SourceOfReturnsJobHunterDTO;
import com.wcwy.company.entity.SourceOfReturns;
import com.wcwy.company.query.ShareRevenueDetailsQuery;
import com.wcwy.company.query.SourceOfReturnsCompanyQuery;
import com.wcwy.company.query.SourceOfReturnsQuery;
import com.wcwy.company.service.SourceOfReturnsService;
import com.wcwy.company.mapper.SourceOfReturnsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @description 针对表【source_of_returns(分享收益来源详情)】的数据库操作Service实现
 * @createDate 2023-07-19 11:44:01
 */
@Service
public class SourceOfReturnsServiceImpl extends ServiceImpl<SourceOfReturnsMapper, SourceOfReturns>
        implements SourceOfReturnsService {
    @Autowired
    private SourceOfReturnsMapper sourceOfReturnsMapper;


    @Override
    public List<Map> shareData(String userid) {
        return sourceOfReturnsMapper.shareDate(userid);
    }

    @Override
    public IPage<SourceOfReturnsJobHunterDTO> selectJobHunter(SourceOfReturnsQuery sourceOfReturnsQuery) {
        return sourceOfReturnsMapper.selectJobHunter(sourceOfReturnsQuery.createPage(),sourceOfReturnsQuery);
    }

    @Override
    public IPage<SourceOfReturnsCompanyDTO> selectCompany(SourceOfReturnsCompanyQuery sourceOfReturnsQuery) {
        return sourceOfReturnsMapper.selectCompany(sourceOfReturnsQuery.createPage(),sourceOfReturnsQuery);
    }

    @Override
    public String todayProfit(String userid) {
        return sourceOfReturnsMapper.todayProfit(userid);
    }

    @Override
    public String setMonthProfit(String userid) {
        return sourceOfReturnsMapper.setMonthProfit(userid);
    }

    @Override
    public String setSumProfit(String userid) {
        return sourceOfReturnsMapper.setSumProfit(userid);
    }

    @Override
    public List<Map<Integer, Object>> earnings(String userid) {
        return sourceOfReturnsMapper.earnings(userid);
    }

    @Override
    public List<String> selectCompanyName(String userid, String keyword) {
        return sourceOfReturnsMapper.selectCompanyName(userid,keyword);
    }

    @Override
    public List<Map<String, Object>> identityEarnings(String userid,String year,String month) {
        return sourceOfReturnsMapper.identityEarnings(userid,year,month);
    }

    @Override
    public IPage<IdentityEarningsDTO> identityEarningsJobHunter(ShareRevenueDetailsQuery shareRevenueDetailsQuery) {
        return sourceOfReturnsMapper.identityEarningsJobHunter(shareRevenueDetailsQuery.createPage(),shareRevenueDetailsQuery);
    }

    @Override
    public IPage<IdentityEarningsDTO> identityEarningsRecommend(ShareRevenueDetailsQuery shareRevenueDetailsQuery) {
        return sourceOfReturnsMapper.identityEarningsRecommend(shareRevenueDetailsQuery.createPage(),shareRevenueDetailsQuery);
    }

    @Override
    public IPage<IdentityEarningsDTO> identityEarningsCompany(ShareRevenueDetailsQuery shareRevenueDetailsQuery) {
        return sourceOfReturnsMapper.identityEarningsCompany(shareRevenueDetailsQuery.createPage(),shareRevenueDetailsQuery);
    }

    @Override
    public List<Map<String, Object>> identityShareEarnings(String userid, String year, String month) {
        return sourceOfReturnsMapper.identityShareEarnings(userid,year,month);
    }
}




