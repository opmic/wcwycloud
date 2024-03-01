package com.wcwy.company.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wcwy.common.base.result.R;
import com.wcwy.company.dto.ReferrerRecordJobHunterDTO;
import com.wcwy.company.dto.RmJobHunterDTO;
import com.wcwy.company.entity.TJobhunter;
import com.wcwy.company.entity.TPermission;
import com.wcwy.company.po.JobHunterShare;
import com.wcwy.company.po.SharePO;
import com.wcwy.company.po.TCompanySharePO;
import com.wcwy.company.po.TJobhunterPO;
import com.wcwy.company.query.CompanyInviteJobHunterQuery;
import com.wcwy.company.query.ShareQuery;
import com.wcwy.company.query.inviterQuery;
import com.wcwy.company.service.*;
import com.wcwy.company.mapper.TJobhunterMapper;
import com.wcwy.company.vo.TJobhunterVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @description 针对表【t_jobhunter(求职者用户表)】的数据库操作Service实现
 * @createDate 2022-10-08 11:58:24
 */
@Service
public class TJobhunterServiceImpl extends ServiceImpl<TJobhunterMapper, TJobhunter>
        implements TJobhunterService {
    @Autowired
    private TJobhunterMapper tJobhunterMapper;
    @Autowired
    private ReferrerRecordService referrerRecordService;
    @Autowired
    private TJobhunterEducationRecordService tJobhunterEducationRecordService;
    @Resource
    private TJobhunterExpectPositionService tJobhunterExpectPositionService;
    @Autowired
    private TJobhunterResumeService tJobhunterResumeService;
    @Override
    public List<TPermission> rolePermission(String userId) {
        return tJobhunterMapper.rolePermission(userId);
    }
    @Autowired
    private TJobhunterWorkRecordService tJobhunterWorkRecordService;
    @Override
    public String getSharePerson(String jobHunterId) {
        return tJobhunterMapper.getSharePerson(jobHunterId);
    }

    @Override
    public IPage<TJobhunterPO> listInviterIndustry(inviterQuery inviterQuery) {
        return tJobhunterMapper.listInviterIndustry(inviterQuery.createPage(),inviterQuery);
    }

    @Override
    public String selectPhone(String putInUser) {
        return tJobhunterMapper.selectPhone(putInUser);
    }

    @Override
    public Integer talentsCount(String recommend) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("examine_status",1);
        queryWrapper.eq("share_person",recommend);
        queryWrapper.eq("status",0);

        return this.count(queryWrapper);
    }


    /**
     * @Description: 验证修改身份是否合法
     * @param loginUserId:登录id
     * @param userId:求职者id
     * @return null
     * @Author tangzhuo
     * @CreateTime 2023/2/2 15:26
     */

    @Override
    public R jurisdiction(String loginUserId, String userId){

        String substring = loginUserId.substring(0, 2);
        if("TJ".equals(substring)){
            boolean equals = loginUserId.equals(userId);
            if(equals){
                return R.success();
            }
            return R.fail("身份不一致!");
        }
        if("TR".equals(substring)){
            QueryWrapper queryWrapper=new QueryWrapper();
            queryWrapper.eq("t_job_hunter_id",userId);
            queryWrapper.eq("recommend_id",loginUserId);
            queryWrapper.eq("correlation_type",2);
            int count = referrerRecordService.count(queryWrapper);
            if (count==0) {
                return R.fail("该账号未绑定此求职者!");
            }
            return R.success();
        }
        return R.fail("身份不正确!");
    }

    @Override
    public List<String> getExpectPosition(String userid) {
        return tJobhunterMapper.getExpectPosition(userid);
    }

    @Override
    public SharePO share(String jonHunter) {
        return tJobhunterMapper.share(jonHunter);
    }

    @Override
    public IPage<JobHunterShare> shareJobHunter(ShareQuery shareQuery, String userid) {
        return tJobhunterMapper.shareJobHunter(shareQuery.createPage(),shareQuery,userid);
    }

    @Override
    public Map<String, String> getSharePersonRecommend(String jobHunterId) {
        return tJobhunterMapper.getSharePersonRecommend(jobHunterId);
    }

    @Override
    public String getWeChat(String userid) {
        return tJobhunterMapper.getWeChat(userid);
    }

    @Override
    public RmJobHunterDTO selectJobHunter(String jobHunterId, String userid) {
        return tJobhunterMapper.selectJobHunter(jobHunterId,userid);
    }

    @Override
    public R<Map> isSendAResume(String userid) {
        Map map=new HashMap(2);
        TJobhunter byId = this.getById(userid);
        map.put("isOK",false);
        if(byId.getPerfect() ==null || byId.getPerfect()==0){
            if (byId==null) {
                map.put("conditions",0);
                return R.fail("该求职者不存在!",map);
            }
            if (StringUtils.isEmpty(byId.getUserName())|| "求职者".equals(byId.getUserName())) {
                map.put("conditions",1);
                return R.fail("请完善简历信息!",map);
            }


            String postionId=  tJobhunterExpectPositionService.postionId(userid);
            if(StringUtils.isEmpty(postionId)){
                map.put("conditions",2);
                return R.fail("请完善求职期望!",map);
            }

            String eduId=  tJobhunterEducationRecordService.getEduId(userid);
            if(StringUtils.isEmpty(eduId)){
                map.put("conditions",3);
                return R.fail("请完善个人教育经历!",map);
            }
            String advantage= tJobhunterResumeService.getAdvantage(userid);
            if(StringUtils.isEmpty(advantage)){
                map.put("conditions",4);
                return R.fail("请完善个人优势!",map);
            }
        }


        map.put("education",byId.getEducation());
        map.put("isOK",true);
        map.put("userType",byId.getUserType());
        if(byId.getPerfect()==null || byId.getPerfect()==0){
            UpdateWrapper updateWrapper=new UpdateWrapper();
            updateWrapper.eq("user_id",userid);
            updateWrapper.set("perfect",1);
            this.update(updateWrapper);
        }
        return R.success(map);
    }

    @Override
    public IPage<ReferrerRecordJobHunterDTO> companyInviteJobHunter(CompanyInviteJobHunterQuery companyInviteJobHunterQuery) {
        return tJobhunterResumeService.companyInviteJobHunter(companyInviteJobHunterQuery);
    }

    @Override
    public List<Integer> companyInvitationData(String userid) {
        List<Integer> list = tJobhunterMapper.companyInvitationData(userid);
        return  list;
    }

    @Override
    public Map createPerfectDegree(String userid) {
        Map map=new HashMap(2);
        TJobhunter byId = this.getById(userid);
        map.put("isOK",false);
        if (byId==null) {
            map.put("conditions",0);
            return map ;
        }
        if (StringUtils.isEmpty(byId.getUserName())|| "求职者".equals(byId.getUserName())) {
            map.put("conditions",1);
            return map;
        }
  /*      String advantage= tJobhunterResumeService.getAdvantage(userid);
        if(StringUtils.isEmpty(advantage)){
            map.put("conditions",4);
            return map;
        }*/

        String postionId=  tJobhunterExpectPositionService.postionId(userid);
        if(StringUtils.isEmpty(postionId)){
            map.put("conditions",2);
            return map;
        }
        String eduId=  tJobhunterEducationRecordService.getEduId(userid);
        if(StringUtils.isEmpty(eduId)){
            map.put("conditions",3);
            return map;
        }
        if(byId.getWorkTime() !=null){
          String workId=  tJobhunterWorkRecordService.getWorkId(userid);
            if(StringUtils.isEmpty(workId)){
                map.put("conditions",4);
                return map;
            }
        }
       // map.put("education",byId.getEducation());
        map.put("isOK",true);
        return map;

    }

    @Override
    public Long mineJobSeeker(String userid) {
        return tJobhunterMapper.mineJobSeeker(userid);
    }

    @Override
    public Long mineJobSeekerDay(String userid) {
        return tJobhunterMapper.mineJobSeekerDay(userid);
    }

    @Override
    @Cacheable(value="com:TJobhunter:cache:one", key="#jobHunter")
    public String byId(String jobHunter) {
        TJobhunter byId = this.getById(jobHunter);
        if(byId==null){
            return null;
        }

        return JSON.toJSONString(byId);
    }

    @Override
    public int selectCount(String id, LocalDateTime currentStartDate, LocalDateTime currentEndTime, String city) {
        return tJobhunterMapper.count(id,currentStartDate,currentEndTime,city);
    }

    @Override
    public List<Map> mapList(String id, LocalDateTime currentStartDate, LocalDateTime currentEndTime, String city) {
        return tJobhunterMapper.mapList(id,currentStartDate,currentEndTime,city);
    }


}




