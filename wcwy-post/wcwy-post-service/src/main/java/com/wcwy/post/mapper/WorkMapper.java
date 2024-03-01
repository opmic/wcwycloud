package com.wcwy.post.mapper;

import com.wcwy.post.entity.Work;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Administrator
* @description 针对表【work(工作经验表)】的数据库操作Mapper
* @createDate 2022-09-14 15:28:36
* @Entity com.wcwy.post.entity.Work
*/
@Mapper
public interface WorkMapper extends BaseMapper<Work> {

}




