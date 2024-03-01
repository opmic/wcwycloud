package com.wcwy.company.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wcwy.common.base.utils.HandleUtil;
import com.wcwy.common.redis.util.IDGenerator;
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.common.utils.CompanyUserRoleMQ;
import com.wcwy.common.utils.EiCompanyPostMQ;
import com.wcwy.company.asyn.ShareDataAsync;
import com.wcwy.company.entity.CompanyUserRole;
import com.wcwy.company.entity.EiCompanyPost;
import com.wcwy.company.entity.PutInResume;
import com.wcwy.company.po.ProvincesCitiesPO;
import com.wcwy.company.service.CompanyUserRoleService;
import com.wcwy.company.service.EiCompanyPostService;
import com.wcwy.company.service.PutInResumeRecordService;
import com.wcwy.company.service.PutInResumeService;
import com.wcwy.company.session.CompanyMetadata;
import com.wcwy.company.vo.PutInResumeRecordVO;
import com.wcwy.system.annotation.RocketLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ClassName: CompanyUserRoleConsumer
 * Description:
 * date: 2023/4/4 15:22
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@RocketMQMessageListener(topic = CompanyUserRoleMQ.TOPIC, consumerGroup = CompanyUserRoleMQ.GROUP)
@Component
@Slf4j
public class CompanyUserRoleConsumer implements RocketMQListener<String> {
    @Resource
    private RedisUtils redisUtils;
    @Autowired
    private CompanyUserRoleService companyUserRoleService;
    @Autowired
    private IDGenerator idGenerator;
    @Resource
    private PutInResumeService putInResumeService;
    @Autowired
    private PutInResumeRecordService putInResumeRecordService;

    @Autowired
    private EiCompanyPostService eiCompanyPostService;

    @Autowired
    private ShareDataAsync shareDataAsync;

    @Autowired
    private CompanyMetadata companyMetadata;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @RocketLog(title = "添加求职者权限", businessType =1)
    public void onMessage(String s) {
        Map map = JSON.parseObject(s, Map.class);
        try {
            Boolean uuid = redisUtils.setIfAbsent(CompanyUserRoleMQ.GROUP+":"+map.get("UUID"), map.get("UUID").toString(),24);
            if(! uuid){
                return;
            }
            //避免产生重复的数据
            QueryWrapper queryWrapper=new QueryWrapper();
            queryWrapper.eq("company_id",map.get("tCompany").toString());
            queryWrapper.eq("user_id",map.get("jobHunterId").toString());
            queryWrapper.eq("deleted",0);
            int count = companyUserRoleService.count(queryWrapper);
            if(count>0){
                return;
            }

            CompanyUserRole companyUserRole=new CompanyUserRole();
            companyUserRole.setCompanyUserRoleId(idGenerator.generateCode("CU"));
            companyUserRole.setCompanyId(map.get("tCompany").toString());
            companyUserRole.setUserId(map.get("jobHunterId").toString());
            companyUserRole.setDeleted(0);
            companyUserRole.setCreateTime(LocalDateTime.now());
            Object put_in_resume_id1 = map.get("put_in_resume_id");
            if (! StringUtils.isEmpty(put_in_resume_id1)) {
                companyUserRole.setPutInResumeId(map.get("put_in_resume_id").toString());
                PutInResumeRecordVO putInResumeRecordVO=new PutInResumeRecordVO();
                putInResumeRecordVO.setCause("");
                putInResumeRecordVO.setDate(new Date());
                putInResumeRecordVO.setState(2);
                putInResumeRecordVO.setIdentity(HandleUtil.handle(map.get("tCompany").toString()));
                putInResumeRecordVO.setName(putInResumeService.getCompanyName(put_in_resume_id1.toString()));
                putInResumeRecordService.addRecord(put_in_resume_id1.toString(),putInResumeRecordVO);
            }
            boolean save = companyUserRoleService.save(companyUserRole);
            if(save){

                //更新简历的下载
                UpdateWrapper updateWrapper=new UpdateWrapper();
                updateWrapper.eq("put_in_resume_id",map.get("put_in_resume_id").toString());
                PutInResume put_in_resume_id = putInResumeService.getById(map.get("put_in_resume_id").toString());
               if(put_in_resume_id==null){
                   return;
               }
               //添加下载记录

               if(! StringUtils.isEmpty(put_in_resume_id.getInviter())){
                   shareDataAsync.shareDataDownload(put_in_resume_id.getPutInPost(),put_in_resume_id.getInviter());
               }


                Integer resumeState = put_in_resume_id.getResumeState();
                if(resumeState < 2){
                    //暂定
                   // updateWrapper.set("resume_state",2);
                }
                updateWrapper.set("download_if",2);
                updateWrapper.set("download_time",LocalDateTime.now());
                //如果是职位付则完成订单
                EiCompanyPost byId = eiCompanyPostService.getById(put_in_resume_id.getPutInPost());
                if(byId!=null){
                    if(byId.getPostType()>=4){
                        updateWrapper.set("close_an_account_if",3);
                        updateWrapper.set("close_an_account_finish_time",LocalDateTime.now());
                    }
                }
                Object identification = map.get("identification");
                if(identification !=null){
                    int i = Integer.parseInt(identification.toString());
                    if(i==6 || i==7){
                        updateWrapper.set("close_an_account_if",3);
                        updateWrapper.set("close_an_account_finish_time",LocalDateTime.now());
                    }
                }
                putInResumeService.update(updateWrapper);
            }
        }catch (Exception e){
            redisUtils.del(CompanyUserRoleMQ.GROUP+":"+map.get("UUID"));
            redisUtils.acceptSet(CompanyUserRoleMQ.GROUP,s);
            throw  e;
        }

    }
}
