package com.wcwy.company.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wcwy.company.entity.School;
import com.wcwy.company.service.SchoolService;
import com.wcwy.company.mapper.SchoolMapper;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【school(学校表)】的数据库操作Service实现
* @createDate 2022-09-08 16:01:15
*/
@Service
public class SchoolServiceImpl extends ServiceImpl<SchoolMapper, School>
    implements SchoolService{

}




