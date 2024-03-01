package com.wcwy.company.service;

import com.wcwy.company.entity.TJobhunterWorkRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wcwy.company.vo.AddTJobhunterWorkRecordVO;
import com.wcwy.company.vo.TJobhunterWorkRecordVO;

import java.util.List;

/**
* @author Administrator
* @description 针对表【t_jobhunter_work_record(工作经历表)】的数据库操作Service
* @createDate 2022-10-08 11:58:59
*/
public interface TJobhunterWorkRecordService extends IService<TJobhunterWorkRecord> {

    /**
     * 添加工作经验
     * @param jobhunterWorkRecordVO
     * @param s
     * @return
     */
    Boolean adds(List<AddTJobhunterWorkRecordVO> jobhunterWorkRecordVO, String s);

    /**
     * 判断是否存在工作经验
     * @param userid
     * @return
     */
    String getWorkId(String userid);

    /*
    * 查看是否最后一份
    * */
    Integer selectCount(String userId);
}
