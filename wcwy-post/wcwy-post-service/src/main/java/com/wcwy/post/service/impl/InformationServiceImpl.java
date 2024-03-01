package com.wcwy.post.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wcwy.post.entity.Information;
import com.wcwy.post.service.InformationService;
import com.wcwy.post.mapper.InformationMapper;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【information(资讯)】的数据库操作Service实现
* @createDate 2023-04-19 10:47:38
*/
@Service
public class InformationServiceImpl extends ServiceImpl<InformationMapper, Information>
    implements InformationService{

}




