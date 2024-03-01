package com.wcwy.post.mapper;

import com.wcwy.post.entity.Information;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Administrator
* @description 针对表【information(资讯)】的数据库操作Mapper
* @createDate 2023-04-19 10:47:38
* @Entity com.wcwy.post.entity.Information
*/
@Mapper
public interface InformationMapper extends BaseMapper<Information> {

}




