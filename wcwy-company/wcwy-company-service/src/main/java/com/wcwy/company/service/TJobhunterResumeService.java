package com.wcwy.company.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wcwy.common.base.result.R;
import com.wcwy.company.dto.DetailedTJobhunterResumeDTO;
import com.wcwy.company.dto.ReferrerRecordJobHunterDTO;
import com.wcwy.company.entity.TJobhunterResume;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wcwy.company.query.CompanyInviteJobHunterQuery;
import com.wcwy.company.query.SendAResumeQuery;

/**
* @author Administrator
* @description 针对表【t_jobhunter_resume(求职者简历)】的数据库操作Service
* @createDate 2022-10-21 10:09:56
*/
public interface TJobhunterResumeService extends IService<TJobhunterResume> {

    String addResume(String userId);
    String addResume(String userId,String advantage);
    //获取求职者详细信息
    DetailedTJobhunterResumeDTO detailedTJobhunterResumeDTO(String jobhunterId);

    /**
     * 查看
     * @param sendAResumeQuery
     * @return
     */
    Page<DetailedTJobhunterResumeDTO> sendAResume(SendAResumeQuery sendAResumeQuery);

    Boolean inviter(String userid, String resumeId);

    /**
     * 获取个人优势
     * @param userid
     * @return
     */
    String getAdvantage(String userid);

    /**
     * 个人私域
     * @param companyInviteJobHunterQuery
     * @return
     */
    IPage<ReferrerRecordJobHunterDTO> companyInviteJobHunter(CompanyInviteJobHunterQuery companyInviteJobHunterQuery);
}
