package com.wcwy.company.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wcwy.company.dto.CollectJobHunterDTO;
import com.wcwy.company.entity.CollectJobHunter;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wcwy.company.query.CollectJobHunterQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* @author Administrator
* @description 针对表【collect_job_hunter(企业收藏表)】的数据库操作Mapper
* @createDate 2023-04-03 14:04:49
* @Entity com.wcwy.company.entity.CollectJobHunter
*/
@Mapper
public interface CollectJobHunterMapper extends BaseMapper<CollectJobHunter> {


    Integer isCollect(@Param("tCompany") String tCompany,@Param("tJobHunter") String tJobHunter);


    IPage<CollectJobHunterDTO> select(@Param("page") IPage page,@Param("userId") String userId,@Param("collectJobHunterQuery") CollectJobHunterQuery collectJobHunterQuery);
}




