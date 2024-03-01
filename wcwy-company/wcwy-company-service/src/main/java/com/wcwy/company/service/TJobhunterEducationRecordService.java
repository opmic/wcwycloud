package com.wcwy.company.service;

import com.wcwy.company.dto.JobHunterEducationRecordDTO;
import com.wcwy.company.entity.TJobhunterEducationRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wcwy.company.vo.AddTJobhunterEducationRecordVO;
import com.wcwy.company.vo.TJobhunterEducationRecordVO;

import java.util.List;

/**
* @author Administrator
* @description 针对表【t_jobhunter_education_record(教育经历表)】的数据库操作Service
* @createDate 2022-10-08 11:58:38
*/
public interface TJobhunterEducationRecordService extends IService<TJobhunterEducationRecord> {

    String getEduId(String userid);

    /**
     * 查询是否最后一份教育简历
     * @param userId
     * @return
     */
    Integer selectCount(String userId);

    //查询求职者学历接口
    List<JobHunterEducationRecordDTO> correct();



    /**
     * 添加学历
     * @param s
     * @return
     */
    Boolean adds(List<AddTJobhunterEducationRecordVO> tJobhunterEducationRecordVO, String s,String userid) throws Exception;
}
