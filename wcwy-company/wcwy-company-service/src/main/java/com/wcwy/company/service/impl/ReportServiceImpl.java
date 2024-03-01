package com.wcwy.company.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wcwy.company.entity.Report;
import com.wcwy.company.service.ReportService;
import com.wcwy.company.mapper.ReportMapper;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【report(举报)】的数据库操作Service实现
* @createDate 2023-09-20 15:16:24
*/
@Service
public class ReportServiceImpl extends ServiceImpl<ReportMapper, Report>
    implements ReportService{

}




