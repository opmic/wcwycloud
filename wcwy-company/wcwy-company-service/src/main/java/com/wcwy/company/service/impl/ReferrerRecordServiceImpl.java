package com.wcwy.company.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wcwy.common.web.annotation.AutoIdempotent;
import com.wcwy.company.dto.RMDownloadDTO;
import com.wcwy.company.dto.ReferrerRecordJobHunterDTO;
import com.wcwy.company.entity.ReferrerRecord;
import com.wcwy.company.entity.TJobhunter;
import com.wcwy.company.query.RMDownloadQuery;
import com.wcwy.company.query.ReferrerRecordQuery;
import com.wcwy.company.service.ReferrerRecordService;
import com.wcwy.company.mapper.ReferrerRecordMapper;
import com.wcwy.company.service.TJobhunterService;
import com.wcwy.company.vo.RecommendationReportVO;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Administrator
 * @description 针对表【referrer_record(推荐人数据记录)】的数据库操作Service实现
 * @createDate 2022-12-28 10:04:50
 */
@Service
public class ReferrerRecordServiceImpl extends ServiceImpl<ReferrerRecordMapper, ReferrerRecord>
        implements ReferrerRecordService {
    @Resource
    private ReferrerRecordMapper referrerRecordMapper;
    @Autowired
    private TJobhunterService tJobhunterService;

    @Override
    public ReferrerRecord referrerRecordSUM(String userid) {
        return referrerRecordMapper.referrerRecordSUM(userid);
    }

    @Override
    public void augment(String tJobHunter, String recommend, Integer identification) {
        //1：推荐 2:浏览 3:面约 4：面试 5：offer 6:入职 7：淘汰  8:待反馈
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("t_job_hunter_id", tJobHunter);
        queryWrapper.eq("recommend_id", recommend);
        ReferrerRecord one = this.getOne(queryWrapper);
        if (one == null) {
            //查看是否是子公司如果是则创建
            QueryWrapper queryWrapper1 = new QueryWrapper();
            queryWrapper1.eq("user_id", queryWrapper1);
            TJobhunter one1 = tJobhunterService.getOne(queryWrapper1);
            if (one1.getSharePerson() != null && recommend.equals(one1.getSharePerson())) {
                ReferrerRecord referrerRecord = new ReferrerRecord();
                referrerRecord.setTJobHunterId(tJobHunter);
                referrerRecord.setRecommendId(recommend);
                this.save(referrerRecord);
            }
            one = this.getOne(queryWrapper);
        }
        if (one == null) {
            return;
        }
        switch (identification) {
            case 1:
                one.setReferrer(one.getReferrer() + 1);
                break;
            case 2:
                one.setBrowse(one.getBrowse() + 1);
                break;
            case 3:
                one.setAppoint(one.getAppoint() + 1);
                break;
            case 4:
                one.setInterview(one.getInterview() + 1);
                break;
            case 5:
                one.setOffer(one.getOffer() + 1);
                break;
            case 6:
                one.setEntry(one.getEntry() + 1);
                break;
            case 7:
                one.setWeedOut(one.getWeedOut() + 1);
                break;
            case 8:
                one.setFeedback(one.getFeedback() + 1);
                break;

        }
        boolean b = this.updateById(one);
    }

    @Override
    public void reduce(String tJobHunter, String recommend, Integer identification) {
        //1：推荐 2:浏览 3:面约 4：面试 5：offer 6:入职 7：淘汰  8:待反馈
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("t_job_hunter_id", tJobHunter);
        queryWrapper.eq("recommend_id", recommend);
        ReferrerRecord one = this.getOne(queryWrapper);
        if (one == null) {
            //查看是否是子公司如果是则创建
            QueryWrapper queryWrapper1 = new QueryWrapper();
            queryWrapper1.eq("user_id", queryWrapper1);
            TJobhunter one1 = tJobhunterService.getOne(queryWrapper1);
            if (one1.getSharePerson() != null && recommend.equals(one1.getSharePerson())) {
                ReferrerRecord referrerRecord = new ReferrerRecord();
                referrerRecord.setTJobHunterId(tJobHunter);
                referrerRecord.setRecommendId(recommend);
                this.save(referrerRecord);
            }
            one = this.getOne(queryWrapper);
        }
        if (one == null) {
            return;
        }
        switch (identification) {
            case 1:
                one.setReferrer(one.getReferrer() - 1);
                break;
            case 2:
                one.setBrowse(one.getBrowse() - 1);
                break;
            case 3:
                one.setAppoint(one.getAppoint() - 1);
                break;
            case 4:
                one.setInterview(one.getInterview() - 1);
                break;
            case 5:
                one.setOffer(one.getOffer() - 1);
                break;
            case 6:
                one.setEntry(one.getEntry() - 1);
                break;
            case 7:
                one.setWeedOut(one.getWeedOut() - 1);
                break;
            case 8:
                one.setFeedback(one.getFeedback() - 1);
                break;

        }
        boolean b = this.updateById(one);
    }

    @Override
    public List<Map> talents(String userid) {
        return referrerRecordMapper.talents(userid);
    }

    @Override
    public IPage<ReferrerRecordJobHunterDTO> selectReferrerRecordJobHunter(String userid, ReferrerRecordQuery referrerRecordQuery) {
        return referrerRecordMapper.selectReferrerRecordJobHunter(referrerRecordQuery.createPage(), referrerRecordQuery, userid);
    }

    @Override
    public List<String> expectation(String keyword, Integer pageSize, String userid) {
        return referrerRecordMapper.expectation(keyword, pageSize, userid);
    }

    @Override
    public Boolean getCorrelationType(String userId, String jobHunter) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("t_job_hunter_id", jobHunter);
        queryWrapper.eq("recommend_id", userId);
        queryWrapper.eq("correlation_type", 2);
        int count = this.count(queryWrapper);
        return count > 0 ? true : false;
    }

    @Override
    public Map<String, Long> countTalents(String userid) {
        Map<String, Long> map = new HashMap<>();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("recommend_id", userid);
      /*  map.put("talents", (long) this.count(queryWrapper));*/
        queryWrapper.ne("correlation_type", 3);
        map.put("personSelected", (long) this.count(queryWrapper));
        return map;
    }

    @Override
    public IPage<RMDownloadDTO> selectDownload(RMDownloadQuery rmDownloadQuery, String userid) {
        return referrerRecordMapper.selectDownload(rmDownloadQuery.createPage(), rmDownloadQuery, userid);
    }

    @Override
    @Async
    public void updateReport(RecommendationReportVO recommendationReportVO, String userId) {
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("t_job_hunter_id", recommendationReportVO.getPutInJobhunter());
        updateWrapper.eq("recommend_id", userId);
        ReferrerRecord one = this.getOne(updateWrapper);
        if (one == null) {
            return;
        }
        Map map = new ConcurrentHashMap();

        if(! StringUtils.isEmpty(recommendationReportVO.getIntention())){
            map.put("intention", recommendationReportVO.getIntention());
        }
        if(! StringUtils.isEmpty(recommendationReportVO.getArrivalTime())){
            map.put("arrivalTime", recommendationReportVO.getArrivalTime());
        }
        if(! StringUtils.isEmpty(recommendationReportVO.getCurrentAnnualSalary())){
            map.put("currentAnnualSalary", recommendationReportVO.getCurrentAnnualSalary());
        }
        if(! StringUtils.isEmpty(recommendationReportVO.getExpectAnnualSalary())){
            map.put("expectAnnualSalary", recommendationReportVO.getExpectAnnualSalary());
        }
        if(! StringUtils.isEmpty(recommendationReportVO.getApplicationInterviewTime())){
            map.put("applicationInterviewTime", recommendationReportVO.getApplicationInterviewTime());
        }
        if(! StringUtils.isEmpty(recommendationReportVO.getExplains())){
            map.put("explains", recommendationReportVO.getExplains());
        }
        updateWrapper.set("report", JSON.toJSONString(map));
        updateWrapper.set("referrer", one.getReferrer() + 1);
        this.update(updateWrapper);
    }

    @Override
    public List<Map> graph(String userid, String year) {
        return referrerRecordMapper.graph(userid,year);
    }

    @Override
    public void positionApplied(String userId, String jobHunter) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("t_job_hunter_id",jobHunter);
        queryWrapper.eq("recommend_id", userId);
        ReferrerRecord one = this.getOne(queryWrapper);
        //如果等于null直接添加
        if(one==null){
            ReferrerRecord referrerRecord = new ReferrerRecord();
            referrerRecord.setTJobHunterId(jobHunter);
            referrerRecord.setRecommendId(userId);
            referrerRecord.setDownloadIf(0);
            referrerRecord.setDownloadTime(LocalDateTime.now());
            referrerRecord.setCorrelationType(3);
            referrerRecord.setCreateTime(LocalDateTime.now());
            boolean save1 = this.save(referrerRecord);
        }else {
            //查看优先级顺序
            Integer correlationType = one.getCorrelationType();
            //如果优先级大于应聘简历则更新
            if(correlationType>3){
                one.setCorrelationType(3);
                one.setCreateTime(LocalDateTime.now());
                this.updateById(one);
            }
        }
    }

    @Override
    public int countJobHunter(String userid) {
        return referrerRecordMapper.countJobHunter(userid);
    }


}




