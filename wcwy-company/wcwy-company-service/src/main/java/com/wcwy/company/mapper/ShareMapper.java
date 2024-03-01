package com.wcwy.company.mapper;

import com.wcwy.company.entity.Share;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Administrator
* @description 针对表【share(分享次数记录接口)】的数据库操作Mapper
* @createDate 2023-07-20 13:51:44
* @Entity com.wcwy.company.entity.Share
*/
@Mapper
public interface ShareMapper extends BaseMapper<Share> {

}




