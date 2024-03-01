package com.wcwy.company.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wcwy.common.base.result.R;
import com.wcwy.common.redis.util.IDGenerator;
import com.wcwy.common.utils.StringUtils;
import com.wcwy.company.dto.DetailedTJobhunterResumeDTO;
import com.wcwy.company.dto.ReferrerRecordJobHunterDTO;
import com.wcwy.company.entity.TJobhunterResume;
import com.wcwy.company.entity.TJobhunterResumeConfig;
import com.wcwy.company.query.CompanyInviteJobHunterQuery;
import com.wcwy.company.query.SendAResumeQuery;
import com.wcwy.company.service.TJobhunterResumeConfigService;
import com.wcwy.company.service.TJobhunterResumeService;
import com.wcwy.company.mapper.TJobhunterResumeMapper;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Administrator
 * @description 针对表【t_jobhunter_resume(求职者简历)】的数据库操作Service实现
 * @createDate 2022-10-21 10:09:56
 */
@Service
@Slf4j
public class TJobhunterResumeServiceImpl extends ServiceImpl<TJobhunterResumeMapper, TJobhunterResume>
        implements TJobhunterResumeService {
    @Resource
    private TJobhunterResumeMapper tJobhunterResumeMapper;
    @Resource
    private TJobhunterResumeConfigService tJobhunterResumeConfigService;
    @Autowired
    private IDGenerator idGenerator;

    @Override
    public String addResume(String userId) {
        TJobhunterResume tJobhunterResume = new TJobhunterResume();
        tJobhunterResume.setResumeId(idGenerator.generateCode("JR"));
        tJobhunterResume.setJobhunterId(userId);
        tJobhunterResume.setResumeName("我的简历");
        tJobhunterResume.setResumeExamineStatus(1);
        tJobhunterResume.setResume(1);
        tJobhunterResume.setDeleted(1);
        tJobhunterResume.setCreateTime(LocalDateTime.now());
        int insert = tJobhunterResumeMapper.insert(tJobhunterResume);
        if (insert > 0) {
            log.info("简历建成功");
        } else {
            log.error("简历建失败");
        }
        TJobhunterResumeConfig tJobhunterResumeConfig = new TJobhunterResumeConfig();
        tJobhunterResumeConfig.setUserId(userId);
        tJobhunterResumeConfig.setCreateTime(LocalDateTime.now());
        boolean save = tJobhunterResumeConfigService.save(tJobhunterResumeConfig);
        if (save) {
            log.info("简历配置创建成功");
        } else {
            log.error("简历配置创建失败");
        }

        return tJobhunterResume.getResumeId();
    }

    @Override
    public String addResume(String userId, String advantage) {
        TJobhunterResume tJobhunterResume = new TJobhunterResume();
        tJobhunterResume.setResumeId(idGenerator.generateCode("JR"));
        tJobhunterResume.setJobhunterId(userId);
        tJobhunterResume.setResumeName("我的简历");
        tJobhunterResume.setResumeExamineStatus(1);
        if (!StringUtils.isEmpty(advantage)) {
            ArrayList<String> strings = new ArrayList<>(1);
            strings.add(advantage);
            tJobhunterResume.setAdvantage(strings);
        }
        tJobhunterResume.setResume(1);
        tJobhunterResume.setDeleted(1);
        tJobhunterResume.setCreateTime(LocalDateTime.now());
        int insert = tJobhunterResumeMapper.insert(tJobhunterResume);
        if (insert > 0) {
            log.info("简历建成功");
        } else {
            log.error("简历建失败");
        }
        TJobhunterResumeConfig tJobhunterResumeConfig = new TJobhunterResumeConfig();
        tJobhunterResumeConfig.setUserId(userId);
        tJobhunterResumeConfig.setCreateTime(LocalDateTime.now());
        boolean save = tJobhunterResumeConfigService.save(tJobhunterResumeConfig);
        if (save) {
            log.info("简历配置创建成功");
        } else {
            log.error("简历配置创建失败");
        }

        return tJobhunterResume.getResumeId();
    }

    @Override
    public DetailedTJobhunterResumeDTO detailedTJobhunterResumeDTO(String jobhunterId) {
        return tJobhunterResumeMapper.detailedTJobhunterResumeDTO(jobhunterId);
    }

    @Override
    public Page<DetailedTJobhunterResumeDTO> sendAResume(SendAResumeQuery sendAResumeQuery) {
        return tJobhunterResumeMapper.sendAResume(sendAResumeQuery.createPage(), sendAResumeQuery);
    }

    @Override
    public Boolean inviter(String userid, String resumeId) {
        int inviter = tJobhunterResumeMapper.inviter(userid, resumeId);
        return inviter > 0 ? true : false;
    }

    @Override
    public String getAdvantage(String userid) {
        return tJobhunterResumeMapper.getAdvantage(userid);
    }

    @Override
    public IPage<ReferrerRecordJobHunterDTO> companyInviteJobHunter(CompanyInviteJobHunterQuery companyInviteJobHunterQuery) {
        return tJobhunterResumeMapper.companyInviteJobHunter(companyInviteJobHunterQuery.createPage(),companyInviteJobHunterQuery);
    }

}




