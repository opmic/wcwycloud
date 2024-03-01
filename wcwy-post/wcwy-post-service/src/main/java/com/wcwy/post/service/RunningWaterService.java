package com.wcwy.post.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wcwy.post.dto.OrderInfoRunningDTO;
import com.wcwy.post.entity.RunningWater;
import com.wcwy.post.query.OrderInfoRunningQuery;

import java.util.List;

/**
* @author Administrator
* @description 针对表【running_water(无忧币流水账表)】的数据库操作Service
* @createDate 2022-10-12 17:04:19
*/
public interface RunningWaterService extends IService<RunningWater> {
    IPage<OrderInfoRunningDTO> selectPage(OrderInfoRunningQuery orderInfoRunningQuery);

    /*boolean insertList(List<RunningWater> );*/
}
