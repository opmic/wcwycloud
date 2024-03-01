package com.wcwy.post.mapper;

import com.wcwy.post.dto.TotalPostShare;
import com.wcwy.post.entity.TPostShare;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* @author Administrator
* @description 针对表【t_post_share(发布岗位纪录表)】的数据库操作Mapper
* @createDate 2022-09-15 17:26:24
* @Entity com.wcwy.post.entity.TPostShare
*/
@Mapper
public interface TPostShareMapper extends BaseMapper<TPostShare> {

    TotalPostShare selectTotalPostShare(@Param("companyId") String companyId);
}




