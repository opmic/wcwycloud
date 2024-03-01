package com.wcwy.company.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wcwy.company.entity.TCities;
import com.wcwy.company.service.TCitiesService;
import com.wcwy.company.mapper.TCitiesMapper;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【t_cities(城市信息表)】的数据库操作Service实现
* @createDate 2022-09-02 10:33:31
*/
@Service
public class TCitiesServiceImpl extends ServiceImpl<TCitiesMapper, TCities>
    implements TCitiesService{

}




