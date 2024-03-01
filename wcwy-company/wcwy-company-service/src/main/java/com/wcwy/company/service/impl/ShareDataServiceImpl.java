package com.wcwy.company.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wcwy.company.entity.ShareData;
import com.wcwy.company.query.ShareDataQuery;
import com.wcwy.company.service.ShareDataService;
import com.wcwy.company.mapper.ShareDataMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Administrator
 * @description 针对表【share_data(分享运维表)】的数据库操作Service实现
 * @createDate 2023-08-25 10:12:51
 */
@Service
public class ShareDataServiceImpl extends ServiceImpl<ShareDataMapper, ShareData>
        implements ShareDataService {

    @Resource
    private ShareDataMapper shareDataMapper;

    @Override
    public List<ShareData> day(ShareDataQuery shareDataQuery) {
        return shareDataMapper.day(shareDataQuery);
    }

    @Override
    public IPage<ShareData> iPageDay(IPage iPage, ShareDataQuery shareDataQuery) {
        return shareDataMapper.iPageDay(iPage,shareDataQuery);
    }

    @Override
    public List<ShareData> week(ShareDataQuery shareDataQuery) {
        return shareDataMapper.week(shareDataQuery);
    }

    @Override
    public IPage<ShareData> iPageWeek(IPage iPage, ShareDataQuery shareDataQuery) {
        return shareDataMapper.iPageWeek(iPage,shareDataQuery);
    }

    @Override
    public List<ShareData> month(ShareDataQuery shareDataQuery) {
        return shareDataMapper.month(shareDataQuery);
    }
}




