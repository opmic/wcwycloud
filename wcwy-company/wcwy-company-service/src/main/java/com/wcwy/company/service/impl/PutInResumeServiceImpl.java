package com.wcwy.company.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.Update;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wcwy.common.base.query.PageQuery;
import com.wcwy.common.redis.enums.PostRecordSole;
import com.wcwy.common.redis.enums.Record;
import com.wcwy.common.redis.util.IDGenerator;
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.company.dto.*;
import com.wcwy.company.entity.PutInResume;
import com.wcwy.company.po.PutPostShareDataPO;
import com.wcwy.company.produce.PostRecordProduce;
import com.wcwy.company.query.*;
import com.wcwy.company.service.PutInResumeService;
import com.wcwy.company.mapper.PutInResumeMapper;
import com.wcwy.company.session.CompanyMetadata;
import com.wcwy.post.api.TPostShareApi;
import com.wcwy.post.entity.TPostShare;
import com.wcwy.post.pojo.PutInResumeInterviewPOJO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 * @description 针对表【put_in_resume(投递简历表)】的数据库操作Service实现
 * @createDate 2022-10-13 17:15:29
 */
@Service()
@Slf4j
public class PutInResumeServiceImpl extends ServiceImpl<PutInResumeMapper, PutInResume>
        implements PutInResumeService {
    @Autowired
    private PutInResumeMapper putInResumeMapper;
    @Autowired
    private TPostShareApi tPostShareApi;
    @Autowired
    private IDGenerator idGenerator;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedisUtils redisUtils;
    /*   @Autowired
       private RabbitTemplate rabbitTemplate;*/
    @Autowired
    private PostRecordProduce postRecordProduce;

    @Autowired
    private CompanyMetadata companyMetadata;

    @Override
    public Page<PutInResumeInterviewPOJO> pagePutInResumeInterview(PutInResumeQuery putInResumeQuery) {
        return putInResumeMapper.pagePutInResumeInterview(putInResumeQuery.createPage(), putInResumeQuery);
    }

    @Override
    public List<String> selectPutInResumeId(String userid) {
        return putInResumeMapper.selectPutInResumeId(userid);
    }

    @Override
    public List<String> selectPutInResumeIdTC(String userid) {
        return putInResumeMapper.selectPutInResumeIdTC(userid);
    }

    @Override
    public String getPostId(String putInResumeId) {
        return putInResumeMapper.getPostId(putInResumeId);
    }


    /**
     * @param post  岗位id
     * @param state 状态
     * @return null
     * @Description:
     * @Author tangzhuo
     * @CreateTime 2022/11/11 8:53
     */
    @Override
    public void updateRedisPost(String jobHunter, String post, Integer state, String putInUser) {
        Map map = new HashMap();
        map.put("jobHunter", jobHunter);
        map.put("post", post);
        map.put("state", state);
        map.put("putInUser", putInUser);
        map.put("type", 0);//操做新增还是操作减少(0操作updateRedisPost 1操作cancelRedisPost)
        map.put("uuid",redisUtils.generateCode() );
        /*String mapJson = JSON.toJSONString(map);*/
        /*   rabbitTemplate.convertAndSend("postRecord.exchange", "postRecord", mapJson, correlationData1);*/
        postRecordProduce.sendSyncMessage(map);

       /* ValueOperations<String, TPostShare> valueOperations = redisTemplate.opsForValue();
        ValueOperations<String, String> shareValueOperations = redisTemplate.opsForValue();
        //0:推荐 1:浏览、 2:下载、3排除  4:约面、5:面试中6淘汰、7:offer、8:入职、9:保证期
        Long time = 604800L;
        TPostShare tPostShare = this.selectPost(post);
        if (state == 0) {
            String browse = PostRecordSole.SHARE.getValue() + post + jobHunter + putInUser;
            Boolean member = shareValueOperations.setIfAbsent(browse, jobHunter, time, TimeUnit.SECONDS);
            if (member) {
                tPostShare.setShareSize(tPostShare.getShareSize() + 1);
            }
        } else if (state == 1) {
            String browse = PostRecordSole.BROWSE.getValue() + post + jobHunter + putInUser;
            Boolean member = shareValueOperations.setIfAbsent(browse, jobHunter, time, TimeUnit.SECONDS);
            if (member) {
                tPostShare.setBrowseSize(tPostShare.getBrowseSize() + 1);
            }
        } else if (state == 2) {
            String download = PostRecordSole.DOWNLOAD.getValue() + post + jobHunter + putInUser;
            Boolean member = shareValueOperations.setIfAbsent(download, jobHunter, time, TimeUnit.SECONDS);
            if (member) {
                tPostShare.setDownloadSize(tPostShare.getDownloadSize() + 1);
            }

        } else if (state == 4 || state == 5) {

            String subscribe = PostRecordSole.SUBSCRIBE.getValue() + post + jobHunter + putInUser;
            Boolean member = shareValueOperations.setIfAbsent(subscribe, jobHunter);
            if (member) {
                tPostShare.setSubscribe(tPostShare.getSubscribe() + 1);

            }
            String interview = PostRecordSole.INTERVIEW.getValue() + post + jobHunter + putInUser;
            Boolean members = shareValueOperations.setIfAbsent(interview, jobHunter, time, TimeUnit.SECONDS);
            if (members) {
                tPostShare.setInterviewSize(tPostShare.getInterviewSize() + 1);
            }
        } else if (state == 6 || state == 3) {
            String weedOut = PostRecordSole.WEED_OUT.getValue() + post + jobHunter + putInUser;
            Boolean member = shareValueOperations.setIfAbsent(weedOut, jobHunter, time, TimeUnit.SECONDS);
            if (member) {
                tPostShare.setWeedOut(tPostShare.getWeedOut() + 1);
            }
            //当淘汰人员则减少预约面试人员
            //查询是否存在  存在则删除
            String subscribe = PostRecordSole.SUBSCRIBE.getValue() + post + jobHunter + putInUser;
            String s = shareValueOperations.get(subscribe);
            if (state == 6 && !StringUtils.isEmpty(s)) {
                redisTemplate.delete(subscribe);
                if (tPostShare.getSubscribe() > 0) {
                    tPostShare.setSubscribe(tPostShare.getSubscribe() - 1);
                }

            }

        } else if (state == 7) {
            String offer = PostRecordSole.OFFER.getValue() + post + jobHunter + putInUser;
            Boolean member = shareValueOperations.setIfAbsent(offer, jobHunter, time, TimeUnit.SECONDS);
            if (member) {
                //当发送offer人员则减少预约面试人员
                String subscribe = PostRecordSole.SUBSCRIBE.getValue() + post + jobHunter + putInUser;
                String s = shareValueOperations.get(subscribe);
                if (!StringUtils.isEmpty(s)) {
                    redisTemplate.delete(subscribe);
                    if (tPostShare.getSubscribe() > 0) {
                        tPostShare.setSubscribe(tPostShare.getSubscribe() - 1);
                    }

                }
                tPostShare.setOfferSize(tPostShare.getOfferSize() + 1);
            }

        } else if (state == 8) {
            String entry = PostRecordSole.ENTRY.getValue() + post + jobHunter + putInUser;
            Boolean member = shareValueOperations.setIfAbsent(entry, jobHunter, time, TimeUnit.SECONDS);
            if (member) {
                tPostShare.setEntrySize(tPostShare.getEntrySize() + 1);
            }

        } else if (state == 9) {
            String overInsured = PostRecordSole.OVER_INSURED.getValue() + post + jobHunter + putInUser;
            Boolean member = shareValueOperations.setIfAbsent(overInsured, jobHunter, time, TimeUnit.SECONDS);
            if (member) {
                tPostShare.setOverInsured(tPostShare.getOverInsured() + 1);
            }
        }
        redisTemplate.delete(Record.POST_RECORD.getValue() + post);//删除重新插入
        valueOperations.set(Record.POST_RECORD.getValue() + post, tPostShare);
        String s = UUID.randomUUID().toString();*/
        /*  CorrelationData correlationData1 = new CorrelationData(s);*/

    }

    /**
     * 取消入职
     * @param post 岗位id
     * @param jobHunter 求职者id
     */
 /*   public void cancelEntry(String post, String jobHunter){
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        ValueOperations<String, TPostShare> valueOperations = redisTemplate.opsForValue();
        TPostShare tPostShare = this.selectPost(post);
        String entry = PostRecordSole.ENTRY.getValue()+post;
        setOperations.remove(entry,jobHunter);

        redisTemplate.delete(Record.POST_RECORD.getValue()+ post);//删除重新插入
        valueOperations.set(Record.POST_RECORD.getValue() + post, tPostShare);
    }*/

    /**
     * @return null
     * @Description: 返回发布岗位的记录数
     * @Author tangzhuo
     * @CreateTime 2022/11/11 8:51
     */

    @Override
    public TPostShare selectPost(String post) {
        ValueOperations<String, TPostShare> valueOperations = redisTemplate.opsForValue();
        TPostShare select = valueOperations.get(Record.POST_RECORD.getValue() + post);
        if (select == null) {
            select = tPostShareApi.select(post);
            if (select != null) {
                redisTemplate.delete(Record.POST_RECORD.getValue() + post);//删除重新插入
                valueOperations.set(Record.POST_RECORD.getValue() + post, select);
            }
        }
        return select;
    }


    /**
     * @return null
     * @Description: 取消岗位记录
     * @Author tangzhuo
     * @CreateTime 2022/11/28 14:20
     */

    @Override
    public void cancelRedisPost(String jobHunter, String post, Integer state, String putInUser) {
        Map map = new HashMap();
        map.put("jobHunter", jobHunter);
        map.put("post", post);
        map.put("state", state);
        map.put("putInUser", putInUser);
        map.put("type", 1);//操做新增还是操作减少(0操作updateRedisPost 1操作cancelRedisPost)
        map.put("uuid",redisUtils.generateCode() );
        /*String mapJson = JSON.toJSONString(map);*/
        /*   rabbitTemplate.convertAndSend("postRecord.exchange", "postRecord", mapJson, correlationData1);*/
        postRecordProduce.sendSyncMessage(map);
        /* SetOperations<String, String> setOperations = redisTemplate.opsForSet();*/
        /*ValueOperations<String, TPostShare> valueOperations = redisTemplate.opsForValue();
        ValueOperations<String, String> shareValueOperations = redisTemplate.opsForValue();
        //1:浏览、 2:下载、3排除  4:约面、5:面试中6淘汰、7:offer、8:入职、9:保证期
        TPostShare tPostShare = this.selectPost(post);
        Long time = 604800L;
        if (state == 1) {
            String browse = PostRecordSole.BROWSE.getValue() + post + jobHunter + putInUser;
            String s = shareValueOperations.get(browse);
            if (!StringUtils.isEmpty(s)) {
                redisTemplate.delete(browse);
                tPostShare.setBrowseSize(tPostShare.getBrowseSize() - 1);
            }
        } else if (state == 2) {
            String download = PostRecordSole.DOWNLOAD.getValue() + post + jobHunter + putInUser;
            String s = shareValueOperations.get(download);
            if (!StringUtils.isEmpty(s)) {
                redisTemplate.delete(download);
                tPostShare.setDownloadSize(tPostShare.getDownloadSize() - 1);
            }

        } else if (state == 4) {
            String subscribe = PostRecordSole.SUBSCRIBE.getValue() + post + jobHunter + putInUser;
            String s = shareValueOperations.get(subscribe);
            if (!StringUtils.isEmpty(s)) {
                redisTemplate.delete(subscribe);//进行删除
                if (tPostShare.getInterviewSize() > 0) {
                    tPostShare.setInterviewSize(tPostShare.getInterviewSize() - 1);
                }
            }
            String interview = PostRecordSole.INTERVIEW.getValue() + post + jobHunter + putInUser;
            String s1 = shareValueOperations.get(interview);
            if (!StringUtils.isEmpty(s1)) {
                redisTemplate.delete(interview);//进行删除
                tPostShare.setInterviewSize(tPostShare.getInterviewSize() - 1);
            }

        } else if (state == 5) {
            String interview = PostRecordSole.INTERVIEW.getValue() + post + jobHunter + putInUser;
            String s = shareValueOperations.get(interview);
            if (!StringUtils.isEmpty(s)) {
                redisTemplate.delete(interview);//进行删除
                tPostShare.setInterviewSize(tPostShare.getInterviewSize() - 1);
            }

        } else if (state == 6 || state == 3) {
            String weedOut = PostRecordSole.WEED_OUT.getValue() + post + jobHunter + putInUser;
            String s = shareValueOperations.get(weedOut);
            if (!StringUtils.isEmpty(s)) {
                redisTemplate.delete(weedOut);//进行删除
                tPostShare.setWeedOut(tPostShare.getWeedOut() - 1);
            }

        } else if (state == 7) {
            String offer = PostRecordSole.OFFER.getValue() + post + jobHunter + putInUser;
            String s = shareValueOperations.get(offer);
            if (!StringUtils.isEmpty(s)) {
                redisTemplate.delete(offer);//进行删除
                if (tPostShare.getOfferSize() > 0) {
                    tPostShare.setOfferSize(tPostShare.getOfferSize() - 1);
                }

            }

        } else if (state == 8) {
            String entry = PostRecordSole.ENTRY.getValue() + post + jobHunter + putInUser;
            String s = shareValueOperations.get(entry);
            if (!StringUtils.isEmpty(s)) {
                redisTemplate.delete(entry);//进行删除
                tPostShare.setEntrySize(tPostShare.getEntrySize() - 1);
            }

        } else if (state == 9) {
            String overInsured = PostRecordSole.OVER_INSURED.getValue() + post + jobHunter + putInUser;
            String s = shareValueOperations.get(overInsured);
            if (!StringUtils.isEmpty(s)) {
                redisTemplate.delete(overInsured);//进行删除
                tPostShare.setOverInsured(tPostShare.getOverInsured() - 1);
            }
        }
        redisTemplate.delete(Record.POST_RECORD.getValue() + post);//删除重新插入
        valueOperations.set(Record.POST_RECORD.getValue() + post, tPostShare);
        String s = UUID.randomUUID().toString();
        Map map = new HashMap();
        map.put("post", post);
        map.put("state", state);
        map.put("uuid", s);
        String mapJson = JSON.toJSONString(map);
      //  rabbitTemplate.convertAndSend("postRecord.exchange", "postRecord", mapJson, correlationData1);
        postRecordProduce.sendSyncMessage(map);*/
    }

    @Override
    public List<PutInResumeDTO> listInviteEntry() {
        return putInResumeMapper.listInviteEntry();
    }

    @Override
    public void updateResumeState(String putInResumeId, Integer resumeState) {
        PutInResume byId = this.getById(putInResumeId);
        UpdateWrapper updateWrapper=new UpdateWrapper();
        updateWrapper.eq("put_in_resume_id",putInResumeId);
        if(byId.getResumeState()<resumeState){
            updateWrapper.set("resume_state",resumeState);
        }
        updateWrapper.set("t_company_message",0);
        updateWrapper.set("update_time",LocalDateTime.now());
        updateWrapper.set("put_in_message",1);
        updateWrapper.set("offer_if",2);
        updateWrapper.set("offer_time", LocalDateTime.now());

        updateWrapper.set("weed_out_if",1);
        updateWrapper.set("weed_out_time",LocalDateTime.now());
        boolean update = this.update(updateWrapper);
        if(update){
            log.info("修改成功");
        }
        //putInResumeMapper.updateResumeState(putInResumeId, resumeState);
    }

    @Override
    public Page<PutInResumeInterviewPOJO> selectAccountResume(PutInResumeQuery putInResumeQuery) {

        return putInResumeMapper.selectAccountResume( putInResumeQuery.createPage(), putInResumeQuery);
    }

    @Override
    public RecommendedDataDTO recommendedData(String userid,String companyId,String postId) {
        return putInResumeMapper.recommendedData(userid,companyId,postId);
    }

    @Override
    public int receiveResume(String companyId) {
        return putInResumeMapper.receiveResume(companyId);
    }

    @Override
    public Page<PutInResumeInterviewPOJO> sendAResumeInformation(SendAResumeInformationQuery sendAResumeInformationQuery) {
        return putInResumeMapper.sendAResumeInformation(sendAResumeInformationQuery.createPage(),sendAResumeInformationQuery);
    }

    @Override
    public IPage<SelectCompanyResumeDTO> selectCompanyResume(SelectCompanyResumeQuery selectCompanyResumeQuery, String userid) {
        return putInResumeMapper.selectCompanyResume(selectCompanyResumeQuery,userid,selectCompanyResumeQuery.createPage());
    }

    @Override
    public Page<PutInResumeEiCompanyPostDTO> getPutInResumeEiCompanyPostDTO(Integer resumeState, String userId,Page page) {
        return putInResumeMapper.getPutInResumeEiCompanyPostDTO(resumeState,userId,page);
    }

    @Override
    public CompanyTJobHunterResumeDTO getJobHunter(String putInResumeId) {
        return putInResumeMapper.getJobHunter(putInResumeId);
    }

    @Override
    public void updateInterview(String putInResumeId,LocalDateTime interviewTime) {
        UpdateWrapper updateWrapper=new UpdateWrapper();
        updateWrapper.eq("put_in_resume_id",putInResumeId);
        updateWrapper.set("interview_time",interviewTime);
        updateWrapper.set("subscribe_time",interviewTime);
        updateWrapper.set("accept_subscribe",1);
        updateWrapper.set("t_company_message",0);
        updateWrapper.set("update_time",LocalDateTime.now());
        updateWrapper.set("put_in_message",1);
        this.update(updateWrapper);
    }

    @Override
    public void weedOut(String putInResumeId,String cause) {

        UpdateWrapper updateWrapper=new UpdateWrapper();
        updateWrapper.eq("put_in_resume_id",putInResumeId);

        updateWrapper.set("resume_state",7);
        updateWrapper.set("weed_out_if",2);
        updateWrapper.set("weed_out_time",LocalDateTime.now());
        updateWrapper.set("put_in_message",1);
        updateWrapper.set("t_company_message",1);
        updateWrapper.set("weed_out_cause",cause);
        this.update(updateWrapper);

    }

    @Override
    public int interviewPaymentCount(String postId) {
        return putInResumeMapper.interviewPaymentCount(postId);
    }

    @Override
    public List<PutInResume> inappropriate() {
        return putInResumeMapper.inappropriate();
    }

    @Override
    public String getCompanyName(String putInResumeId) {
        return putInResumeMapper.getCompanyName(putInResumeId);
    }

    @Override
    public String getPutName(String putInResumeId) {
        PutInResume byId = this.getById(putInResumeId);
        String easco = byId.getEasco();

        if("0".equals(easco)){
            return putInResumeMapper.getJobHunterName(putInResumeId);
        }else if("1".equals(easco)){
            return  putInResumeMapper.getRecommendName(putInResumeId);
        }
        return null;
    }

    @Override
    public Map<String, Long> postDate(String postId,String userId) {
        Map<String,Long> map=new HashMap();
        Long total=0L;
        map.put("offer",0L);//offer
        map.put("weedOut",0L);//淘汰
        map.put("interview",0L);//面试中
        map.put("invite",0L);//邀约
        map.put("recommend",0L);//总推荐
        map.put("entry",0L);//入职
        List<Map<String, Object>> maps = putInResumeMapper.postDate(postId, userId);
        for (Map<String, Object> stringStringMap : maps) {
            Integer integer = (Integer) stringStringMap.get("resume_state");
            total=total+(Long) stringStringMap.get("quantity");
            if(integer==3 || integer==7){
                map.put("weedOut",map.get("weedOut")+(Long)stringStringMap.get("quantity"));
            }
            if(integer==4){
                map.put("invite",(Long)stringStringMap.get("quantity"));//邀约
            }
            if(integer==5){
                map.put("interview",(Long)stringStringMap.get("quantity"));//邀约
            }
            if(integer==8){
                map.put("offer",(Long)stringStringMap.get("quantity"));//邀约
            }
            if(integer==9){
                map.put("entry",(Long)stringStringMap.get("quantity"));//邀约
            }
        }
        map.put("recommend",total);//总推荐
        return map;
    }

    @Override
    public Map<String, Long> companyDate(String companyId) {
        Map<String,Long> map=new HashMap();
        Long total=0L;
        map.put("offer",0L);//offer
        map.put("weedOut",0L);//淘汰
        map.put("interview",0L);//面试中
        map.put("invite",0L);//邀约
        map.put("recommend",0L);//总推荐
        map.put("entry",0L);//入职
        List<Map<String, Object>> maps = putInResumeMapper.companyDate(companyId);
        for (Map<String, Object> stringStringMap : maps) {
            Integer integer = (Integer) stringStringMap.get("resume_state");
            total=total+(Long) stringStringMap.get("quantity");
            if(integer==3 || integer==7){
                map.put("weedOut",map.get("weedOut")+(Long)stringStringMap.get("quantity"));
            }
            if(integer==4){
                map.put("invite",(Long)stringStringMap.get("quantity"));//邀约
            }
            if(integer==5){
                map.put("interview",(Long)stringStringMap.get("quantity"));//邀约
            }
            if(integer==8){
                map.put("offer",(Long)stringStringMap.get("quantity"));//邀约
            }
            if(integer==9){
                map.put("entry",(Long)stringStringMap.get("quantity"));//邀约
            }
        }
        map.put("recommend",total);//总推荐
        return map;

    }

    @Override
    public IPage<JobHunterPostPutInResumeDTO> recommendAdministrator(RecommendAdministratorQuery recommendAdministratorQuery, String userId) {
        return putInResumeMapper.recommendAdministrator(recommendAdministratorQuery.createPage(),recommendAdministratorQuery,userId);
    }

    @Override
    public List<String> putInResumeCompany(String keyword, Integer pageSize, String userid) {
        return putInResumeMapper.putInResumeCompany(keyword,pageSize,userid);
    }

    @Override
    public List<String> putInResumePostLabel(String keyword, Integer pageSize, String userid) {
        return putInResumeMapper.putInResumePostLabel(keyword,pageSize,userid);
    }

    @Override
    public PutInResumeParticularsDTO putInResumeParticulars(String putInResumeId) {
        return putInResumeMapper.putInResumeParticulars(putInResumeId);
    }

    @Override
    public IPage<HistorySubmitAResumeDTO> historySubmitAResume(PageQuery pageQuery, String userid, String jobHunterId) {
        return putInResumeMapper.historySubmitAResume(pageQuery.createPage(),userid,jobHunterId);
    }

    @Override
    @Async
    public void updateMessage(String userid, String putInResumeId) {
        String substring = userid.substring(0, 2);
        if(! "TC".equals(substring)){
            UpdateWrapper updateWrapper=new UpdateWrapper();
            updateWrapper.eq("put_in_resume_id",putInResumeId);
            updateWrapper.eq("put_in_user",userid);
            updateWrapper.set("put_in_message",0);
            this.update(updateWrapper);
        }
    }

    @Override
    public int countOut(String recommendId) {
        return putInResumeMapper.countOut(recommendId);
    }

    @Override
    public IPage<JobHunterPostPutInResumeDTO> selectPutPost(Page page, String postId, String keyword, String userid,Integer sate) {
        return putInResumeMapper.selectPutPost(page,postId,keyword,userid,sate);
    }

    @Override
    public RecommendedDataDTO recommendedPostData(String userid, String postId) {
        RecommendedDataDTO recommendedDataDTO=new RecommendedDataDTO();
        QueryWrapper recommendCount =new QueryWrapper();
        recommendCount.eq("put_in_post",postId);
        recommendCount.eq("put_in_user",userid);

        recommendedDataDTO.setRecommendCount(this.count(recommendCount));

        QueryWrapper browse =new QueryWrapper();
        browse.eq("put_in_post",postId);
        browse.eq("put_in_user",userid);
        browse.eq("browse_if",2);
        recommendedDataDTO.setBrowse(this.count(browse));


        QueryWrapper subscribeCount =new QueryWrapper();
        subscribeCount.eq("put_in_post",postId);
        subscribeCount.eq("put_in_user",userid);
        subscribeCount.eq("subscribe_if",2);
        recommendedDataDTO.setSubscribeCount(this.count(subscribeCount));

        QueryWrapper offerCount =new QueryWrapper();
        offerCount.eq("put_in_post",postId);
        offerCount.eq("put_in_user",userid);
        offerCount.eq("offer_if",2);
        recommendedDataDTO.setOfferCount(this.count(offerCount));

        QueryWrapper entryCount =new QueryWrapper();
        entryCount.eq("put_in_post",postId);
        entryCount.eq("put_in_user",userid);
        entryCount.eq("entry_if",2);
        recommendedDataDTO.setEntryCount(this.count(entryCount));


        LambdaQueryWrapper<PutInResume> weedOutCount =new LambdaQueryWrapper();
        weedOutCount.eq(PutInResume::getPutInPost,postId);
        weedOutCount.eq(PutInResume::getPutInUser,userid);
        weedOutCount.and(i->i.eq(PutInResume::getExcludeIf,2).or().eq(PutInResume::getWeedOutIf,2));

        recommendedDataDTO.setWeedOutCount(this.count(weedOutCount));

        return recommendedDataDTO;
    }

    @Override
    public String repetition(String phone, String name, String postId) {
        return putInResumeMapper.repetition(phone,name,postId);
    }

    @Override
    public IPage<SelectCompanyResumeDTO> replacePostPutIn(Page page, int resumeState, String keyword, String userid, String postId) {
        return putInResumeMapper.replacePostPutIn(page,resumeState,keyword,userid,postId);
    }

    @Override
    public List<PutPostShareDataPO> browseDay(ShareDataQuery shareDataQuery) {
        List<PutPostShareDataPO> list = putInResumeMapper.browseDay(shareDataQuery);
        return list;
    }

    @Override
    public List<PutPostShareDataPO> slipDay(ShareDataQuery shareDataQuery) {
        return putInResumeMapper.slipDay(shareDataQuery);
    }

    @Override
    public List<PutPostShareDataPO> interviewDay(ShareDataQuery shareDataQuery) {
        return putInResumeMapper.interviewDay(shareDataQuery);
    }

    @Override
    public List<PutPostShareDataPO> browseWeeks(ShareDataQuery shareDataQuery) {
        return putInResumeMapper.browseWeeks(shareDataQuery);
    }

    @Override
    public List<PutPostShareDataPO> slipWeeks(ShareDataQuery shareDataQuery) {
        return putInResumeMapper.slipWeeks(shareDataQuery);
    }

    @Override
    public List<PutPostShareDataPO> interviewWeeks(ShareDataQuery shareDataQuery) {
        return putInResumeMapper.interviewWeeks(shareDataQuery);
    }

    @Override
    public List<PutPostShareDataPO> downloadDay(ShareDataQuery shareDataQuery) {
        return putInResumeMapper.downloadDay(shareDataQuery);
    }

    @Override
    public List<PutPostShareDataPO> offerDay(ShareDataQuery shareDataQuery) {
        return putInResumeMapper.offerDay(shareDataQuery);
    }

    @Override
    public List<PutPostShareDataPO> fullMoonDay(ShareDataQuery shareDataQuery) {
        return putInResumeMapper.fullMoonDay(shareDataQuery);
    }

    @Override
    public List<PutPostShareDataPO> downloadWeeks(ShareDataQuery shareDataQuery) {
        return putInResumeMapper.downloadWeeks(shareDataQuery);
    }

    @Override
    public List<PutPostShareDataPO> offerWeeks(ShareDataQuery shareDataQuery) {
        return putInResumeMapper.offerWeeks(shareDataQuery);
    }

    @Override
    public List<PutPostShareDataPO> fullMoonWeeks(ShareDataQuery shareDataQuery) {
        return putInResumeMapper.fullMoonWeeks(shareDataQuery);
    }

    @Override
    public List<PutPostShareDataPO> entryDay(ShareDataQuery shareDataQuery) {
        return putInResumeMapper.entryDay(shareDataQuery);
    }

    @Override
    public List<PutPostShareDataPO> entryWeeks(ShareDataQuery shareDataQuery) {
        return putInResumeMapper.entryWeeks(shareDataQuery);
    }

    @Override
    public List<PutPostShareDataPO> browseMonth(ShareDataQuery shareDataQuery) {
        return putInResumeMapper.browseMonth(shareDataQuery);
    }

    @Override
    public List<PutPostShareDataPO> slipMonth(ShareDataQuery shareDataQuery) {
        return putInResumeMapper.slipMonth(shareDataQuery);
    }

    @Override
    public List<PutPostShareDataPO> interviewMonth(ShareDataQuery shareDataQuery) {
        return putInResumeMapper.interviewMonth(shareDataQuery);
    }

    @Override
    public List<PutPostShareDataPO> downloadMonth(ShareDataQuery shareDataQuery) {
        return putInResumeMapper.downloadMonth(shareDataQuery);
    }

    @Override
    public List<PutPostShareDataPO> entryMonth(ShareDataQuery shareDataQuery) {
        return putInResumeMapper.entryMonth(shareDataQuery);
    }

    @Override
    public List<PutPostShareDataPO> offerMonth(ShareDataQuery shareDataQuery) {
        return putInResumeMapper.offerMonth(shareDataQuery);
    }

    @Override
    public List<PutPostShareDataPO> fullMoonMonth(ShareDataQuery shareDataQuery) {
        return putInResumeMapper.fullMoonMonth(shareDataQuery);
    }

    @Override
    public List<Map<String, Object>> putCount(String id) {
        return putInResumeMapper.putCount(id);
    }

    @Override
    public Map basicInformation(String putInResume) {
        return putInResumeMapper.basicInformation(putInResume);
    }

    @Override
    public Long selectCounts(String id, LocalDateTime currentStartDate, LocalDateTime currentEndTime, String city, int type,int resumeState) {

        return putInResumeMapper.selectCounts(id,currentStartDate,currentEndTime,city,type,resumeState);
    }

    @Override
    public List<Map> mapListCount(String id, LocalDateTime currentStartDate, LocalDateTime currentEndTime, String city, int type, int resumeState) {
        return putInResumeMapper.mapListCount(id,currentStartDate,currentEndTime,city,type,resumeState);
    }


}




