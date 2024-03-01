package com.wcwy.company.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wcwy.company.entity.Station;
import com.wcwy.company.service.StationService;
import com.wcwy.company.mapper.StationMapper;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【station(岗位表)】的数据库操作Service实现
* @createDate 2022-09-02 14:49:27
*/
@Service
public class StationServiceImpl extends ServiceImpl<StationMapper, Station>
    implements StationService{

}




