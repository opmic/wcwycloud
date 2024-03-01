package com.wcwy.post.service.impl;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wcwy.post.dto.OrderInfoRunningDTO;
import com.wcwy.post.entity.RunningWater;
import com.wcwy.post.mapper.RunningWaterMapper;
import com.wcwy.post.query.OrderInfoRunningQuery;
import com.wcwy.post.service.RunningWaterService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Administrator
 * @description 针对表【running_water(无忧币流水账表)】的数据库操作Service实现
 * @createDate 2022-10-12 17:04:19
 */
@Service
public class RunningWaterServiceImpl extends ServiceImpl<RunningWaterMapper, RunningWater>
        implements RunningWaterService {

    @Resource
    private RunningWaterMapper runningWaterMapper;

    @Override
    public IPage<OrderInfoRunningDTO> selectPage(OrderInfoRunningQuery orderInfoRunningQuery) {
        return runningWaterMapper.selectPages(orderInfoRunningQuery.createPage(),orderInfoRunningQuery);
    }



   /* @Override
    public boolean insertList(List<RunningWater> list) {
        return runningWaterMapper.;
    }*/
}




