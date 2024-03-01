package com.wcwy.system.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wcwy.system.entity.LeadingEndMistake;
import com.wcwy.system.service.LeadingEndMistakeService;
import com.wcwy.system.mapper.LeadingEndMistakeMapper;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【leading_end_mistake(前端错误)】的数据库操作Service实现
* @createDate 2023-05-20 11:43:57
*/
@Service
@DS("slave_1")
public class LeadingEndMistakeServiceImpl extends ServiceImpl<LeadingEndMistakeMapper, LeadingEndMistake>
    implements LeadingEndMistakeService{

}




