package com.wcwy.company.service;

import com.wcwy.company.entity.TJobhunterProjectRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wcwy.company.vo.AddTJobhunterProjectRecordVO;
import com.wcwy.company.vo.TJobhunterProjectRecordVO;

import java.util.List;

/**
* @author Administrator
* @description 针对表【t_jobhunter_project_record(项目经历表)】的数据库操作Service
* @createDate 2022-10-08 11:58:50
*/
public interface TJobhunterProjectRecordService extends IService<TJobhunterProjectRecord> {

    Boolean adds(List<AddTJobhunterProjectRecordVO> tJobhunterProjectRecordVO, String s);

    Integer selectCount(String userId);
}
