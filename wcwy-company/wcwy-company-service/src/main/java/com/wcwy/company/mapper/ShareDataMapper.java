package com.wcwy.company.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wcwy.company.entity.ShareData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wcwy.company.query.ShareDataQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Administrator
* @description 针对表【share_data(分享运维表)】的数据库操作Mapper
* @createDate 2023-08-25 10:12:51
* @Entity com.wcwy.company.entity.ShareData
*/
@Mapper
public interface ShareDataMapper extends BaseMapper<ShareData> {


    List<ShareData> day(@Param("shareDataQuery") ShareDataQuery shareDataQuery);

    List<ShareData> week(@Param("shareDataQuery") ShareDataQuery shareDataQuery);
    List<ShareData> month(@Param("shareDataQuery") ShareDataQuery shareDataQuery);

    IPage<ShareData> iPageDay(@Param("iPage") IPage iPage,@Param("shareDataQuery") ShareDataQuery shareDataQuery);

    IPage<ShareData> iPageWeek(@Param("iPage") IPage iPage,@Param("shareDataQuery") ShareDataQuery shareDataQuery);
}




