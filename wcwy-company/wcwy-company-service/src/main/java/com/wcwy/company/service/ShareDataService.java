package com.wcwy.company.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wcwy.company.entity.ShareData;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wcwy.company.query.ShareDataQuery;

import java.util.List;

/**
* @author Administrator
* @description 针对表【share_data(分享运维表)】的数据库操作Service
* @createDate 2023-08-25 10:12:51
*/
public interface ShareDataService extends IService<ShareData> {

    /**
     * 按日查询
     * @param shareDataQuery
     * @return
     */
    List<ShareData> day(ShareDataQuery shareDataQuery);
    /**
     * 按日分页查询
     * @param shareDataQuery
     * @return
     */
    IPage<ShareData> iPageDay(IPage iPage, ShareDataQuery shareDataQuery);
    /**
     * 按周查询
     * @param shareDataQuery
     * @return
     */
    List<ShareData> week(ShareDataQuery shareDataQuery);
    /**
     * 按周分页查询
     * @param shareDataQuery
     * @return
     */
    IPage<ShareData> iPageWeek(IPage iPage, ShareDataQuery shareDataQuery);
    /**
     * 按月查询
     * @param shareDataQuery
     * @return
     */
    List<ShareData> month(ShareDataQuery shareDataQuery);
}
