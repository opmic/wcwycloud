package com.wcwy.post.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wcwy.post.dto.OrderInfoRunningDTO;
import com.wcwy.post.entity.RunningWater;
import com.wcwy.post.query.OrderInfoRunningQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Administrator
* @description 针对表【running_water(无忧币流水账表)】的数据库操作Mapper
* @createDate 2022-10-12 17:04:19
* @Entity com.wcwy.company.entity.RunningWater
*/
@Mapper
public interface RunningWaterMapper extends BaseMapper<RunningWater> {

    IPage<OrderInfoRunningDTO> selectPages(@Param("page") IPage page,@Param("orderInfoRunningQuery")OrderInfoRunningQuery orderInfoRunningQuery);
}




