package com.wcwy.company.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wcwy.company.entity.Education;
import com.wcwy.company.service.EducationService;
import com.wcwy.company.mapper.EducationMapper;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【education(学历表)】的数据库操作Service实现
* @createDate 2022-09-07 14:08:36
*/
@Service
public class EducationServiceImpl extends ServiceImpl<EducationMapper, Education>
    implements EducationService{

}




