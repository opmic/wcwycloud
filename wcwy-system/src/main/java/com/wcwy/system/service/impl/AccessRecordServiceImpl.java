package com.wcwy.system.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wcwy.system.entity.AccessRecord;
import com.wcwy.system.service.AccessRecordService;
import com.wcwy.system.mapper.AccessRecordMapper;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【access_record】的数据库操作Service实现
* @createDate 2023-08-01 11:50:05
*/
@Service
@DS("slave_1")
public class AccessRecordServiceImpl extends ServiceImpl<AccessRecordMapper, AccessRecord>
    implements AccessRecordService{

}




