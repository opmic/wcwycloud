package com.wcwy.company.asyn;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wcwy.common.base.utils.MessageUtil;
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.company.entity.*;
import com.wcwy.company.produce.MessageProduce;
import com.wcwy.company.service.*;
import com.wcwy.company.vo.MessageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName: InformAsync
 * Description:通知公告
 * date: 2024/1/3 16:53
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Slf4j
@Component
public class InformAsync {

    @Autowired
    private MessageProduce messageProduce;
    @Autowired
    private EiCompanyPostService eiCompanyPostService;
    @Autowired
    private PutInResumeService putInResumeService;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private ReferrerRecordService referrerRecordService;

    @Autowired
    private TRecommendService tRecommendService;
    @Autowired
    private TJobhunterService tJobhunterService;
    private String key = "perfect:tj";

    /*
     * 举报职位通知
     * */
    @Async
    public void reportPost(Report report) {
        /* MessageUtil.*/
        //  messageProduce.sendOrderlyMessage();
    }

    /**
     * @param putInResume:投简id postId：职位id type：投简身份
     * @return null
     * @Description:
     * @Author tangzhuo
     * @CreateTime 2024/1/4 8:53
     */

    @Async
    public void inviteInterview(String putInResume, String postId) {
        Map mapList = putInResumeService.basicInformation(putInResume);
        //求职者自己投递
        MessageVO messageVO = new MessageVO();
        messageVO.setCode(2);
        Map sy004TjInterview = MessageUtil.SY004_TJ_INTERVIEW;
        EiCompanyPost byId = eiCompanyPostService.getById(postId);
        int putid = MessageUtil.identity(mapList.get("putid").toString());
        sy004TjInterview.put("identity",putid);
        if(putid==0){
            sy004TjInterview.put("router","/Talent");
        }else if(putid==1){
            sy004TjInterview.put("router","/Recommhome");
        }else if(putid==2){
            sy004TjInterview.put("router","/Interview");
        }
        Map map = new HashMap();
        map.put("putInResume", putInResume);
        map.put("postLabel", byId.getPostLabel());
        map.put("company_name", byId.getCompanyName());
        if (mapList.get("name") != null) {
            map.put("jobHunterName", mapList.get("name"));
        }
        sy004TjInterview.put("content", JSON.toJSONString(map));
        sy004TjInterview.put("chatId", mapList.get("putid"));
        messageVO.setMessage(sy004TjInterview);
        messageProduce.sendOrderlyMessage(JSON.toJSONString(messageVO));

    }


    @Async
    public void cancelTheInterview(String putInResume) {
        Map mapList = putInResumeService.basicInformation(putInResume);
        //求职者自己投递
        MessageVO messageVO = new MessageVO();
        messageVO.setCode(2);
        Map sy004TjInterview = MessageUtil.SY004_TJ_CANCEL_THE_INTERVIEW;
        int putid = MessageUtil.identity(mapList.get("putid").toString());
        sy004TjInterview.put("identity",putid);
        if(putid==0){
            sy004TjInterview.put("router","/Talent");
        }else if(putid==1){
            sy004TjInterview.put("router","/Recommhome");
        }else if(putid==2){
            sy004TjInterview.put("router","/Interview");
        }
        EiCompanyPost byId = eiCompanyPostService.getById(mapList.get("postId").toString());
        Map map = new HashMap();
        map.put("putInResume", putInResume);
        map.put("postLabel", byId.getPostLabel());
        map.put("company_name", byId.getCompanyName());
        if (mapList.get("name") != null) {
            map.put("jobHunterName", mapList.get("name"));
        }
        sy004TjInterview.put("content", JSON.toJSONString(map));
        sy004TjInterview.put("chatId", mapList.get("putid"));
        messageVO.setMessage(sy004TjInterview);
        messageProduce.sendOrderlyMessage(JSON.toJSONString(messageVO));
    }

    @Async
    public void dieOut(String putInResume) {
        Map mapList = putInResumeService.basicInformation(putInResume);
        //求职者自己投递
        MessageVO messageVO = new MessageVO();
        messageVO.setCode(2);
        Map sy004TjInterview = MessageUtil.SY004_DIE_OUT;
        EiCompanyPost byId = eiCompanyPostService.getById(mapList.get("postId").toString());
        int putid = MessageUtil.identity(mapList.get("putid").toString());
        sy004TjInterview.put("identity",putid);
        if(putid==0){
            sy004TjInterview.put("router","/Talent");
        }else if(putid==1){
            sy004TjInterview.put("router","/Recommhome");
        }else if(putid==2){
            sy004TjInterview.put("router","/Interview");
        }
        Map map = new HashMap();
        map.put("putInResume", putInResume);
        map.put("postLabel", byId.getPostLabel());
        map.put("company_name", byId.getCompanyName());
        if (mapList.get("name") != null) {
            map.put("jobHunterName", mapList.get("name"));
        }
        sy004TjInterview.put("content", JSON.toJSONString(map));
        sy004TjInterview.put("chatId", mapList.get("putid"));
        messageVO.setMessage(sy004TjInterview);
        messageProduce.sendOrderlyMessage(JSON.toJSONString(messageVO));
    }

    @Async
    public void updateInviteInterview(String putInResume) {
        Map mapList = putInResumeService.basicInformation(putInResume);
        //求职者自己投递
        MessageVO messageVO = new MessageVO();
        messageVO.setCode(2);
        Map sy004TjInterview = MessageUtil.SY004_TJ_UPDATE_INTERVIEW;
        EiCompanyPost byId = eiCompanyPostService.getById(mapList.get("postId").toString());
        int putid = MessageUtil.identity(mapList.get("putid").toString());
        sy004TjInterview.put("identity",putid);
        if(putid==0){
            sy004TjInterview.put("router","/Talent");
        }else if(putid==1){
            sy004TjInterview.put("router","/Recommhome");
        }else if(putid==2){
            sy004TjInterview.put("router","/Interview");
        }
        Map map = new HashMap();
        map.put("putInResume", putInResume);
        map.put("postLabel", byId.getPostLabel());
        map.put("company_name", byId.getCompanyName());
        if (mapList.get("name") != null) {
            map.put("jobHunterName", mapList.get("name"));
        }
        sy004TjInterview.put("content", JSON.toJSONString(map));
        sy004TjInterview.put("chatId", mapList.get("putid"));
        messageVO.setMessage(sy004TjInterview);
        messageProduce.sendOrderlyMessage(JSON.toJSONString(messageVO));

    }

    @Async
    public void offer(String putInResume) {
        Map mapList = putInResumeService.basicInformation(putInResume);

        //求职者自己投递
        MessageVO messageVO = new MessageVO();
        messageVO.setCode(2);
        Map sy004TjInterview = MessageUtil.SY004_TJ_OFFER;
        int putid = MessageUtil.identity(mapList.get("putid").toString());
        sy004TjInterview.put("identity",putid);
        if(putid==0){
            sy004TjInterview.put("router","/Talent");
        }else if(putid==1){
            sy004TjInterview.put("router","/Recommhome");
        }else if(putid==2){
            sy004TjInterview.put("router","/Interview");
        }
        EiCompanyPost byId = eiCompanyPostService.getById(mapList.get("postId").toString());
        Map map = new HashMap();
        map.put("putInResume", putInResume);
        map.put("postLabel", byId.getPostLabel());
        map.put("company_name", byId.getCompanyName());
        if (mapList.get("name") != null) {
            map.put("jobHunterName", mapList.get("name"));
        }
        sy004TjInterview.put("content", JSON.toJSONString(map));
        sy004TjInterview.put("chatId", mapList.get("putid"));
        messageVO.setMessage(sy004TjInterview);
        messageProduce.sendOrderlyMessage(JSON.toJSONString(messageVO));

    }

    //简历完善通知
    @Async
    public void perfectYourResume(String jobHunter) {
        boolean b = redisUtils.sHasKey(key, jobHunter);
        if (b) {
            return;
        }
        redisUtils.sSet(key, jobHunter);
        //求职者自己投递
        MessageVO messageVO = new MessageVO();
        messageVO.setCode(2);
        Map sy004TjInterview = MessageUtil.SY004_TJ_PERFECT_YOUR_RESUME;
        sy004TjInterview.put("chatId", jobHunter);
        messageVO.setMessage(sy004TjInterview);
        messageProduce.sendOrderlyMessage(JSON.toJSONString(messageVO));

    }


    //被投简通知
    @Async
    public void sendAResume(String postId, String jobHunter, String putInResumeId) {
        MessageVO messageVO = new MessageVO();
        messageVO.setCode(2);
        TJobhunter byId = tJobhunterService.getById(jobHunter);
        EiCompanyPost byId1 = eiCompanyPostService.getById(postId);
        Map sy004TcSendAResume = MessageUtil.SY004_TC_SEND_A_RESUME;

        int putid = MessageUtil.identity(byId1.getCompanyId());
        sy004TcSendAResume.put("identity",putid);
        if(putid==0){
            sy004TcSendAResume.put("router","/Talent");
        }else if(putid==1){
            sy004TcSendAResume.put("router","/Recommhome");
        }else if(putid==2){
            sy004TcSendAResume.put("router","/Interview");
        }
        sy004TcSendAResume.put("chatId", byId1.getCompanyId());
        Map map = new HashMap();
        map.put("postLabel", byId1.getPostLabel());
        map.put("jobHunterName", byId.getUserName());
        map.put("putInResumeId", putInResumeId);
        sy004TcSendAResume.put("content", JSON.toJSONString(map));

        messageVO.setMessage(sy004TcSendAResume);
        messageProduce.sendOrderlyMessage(JSON.toJSONString(messageVO));

    }

    //被投简通知
    @Async
    public void sendAResumes(List<String> putInResumeIds) {
        for (String putInResumeId : putInResumeIds) {
            Map mapList = putInResumeService.basicInformation(putInResumeId);
            EiCompanyPost postId = eiCompanyPostService.getById(mapList.get("postId").toString());
            MessageVO messageVO = new MessageVO();
            messageVO.setCode(2);
            Map sy004TcSendAResume = MessageUtil.SY004_TC_SEND_A_RESUME;

            int putid = MessageUtil.identity( postId.getCompanyId());
            sy004TcSendAResume.put("identity",putid);
            if(putid==0){
                sy004TcSendAResume.put("router","/Talent");
            }else if(putid==1){
                sy004TcSendAResume.put("router","/Recommhome");
            }else if(putid==2){
                sy004TcSendAResume.put("router","/Interview");
            }
            sy004TcSendAResume.put("chatId", postId.getCompanyId());
            Map map = new HashMap();
            map.put("postLabel", postId.getPostLabel());
            map.put("jobHunterName", mapList.get("name"));
            map.put("putInResumeId", putInResumeId);
            sy004TcSendAResume.put("content", JSON.toJSONString(map));

            messageVO.setMessage(sy004TcSendAResume);
            messageProduce.sendOrderlyMessage(JSON.toJSONString(messageVO));
            //通知求职者
            beRecommended(mapList.get("putid").toString(), mapList.get("postId").toString(), mapList.get("userId").toString());
        }
    }

    @Async
    public void beRecommended(String tRecommendId, String postId, String jobHunter) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("t_job_hunter_id", jobHunter);
        queryWrapper.eq("recommend_id", tRecommendId);
        queryWrapper.eq("deleted", 0);
        ReferrerRecord one = referrerRecordService.getOne(queryWrapper);
        if (one.getCorrelationType()== 2) {
            return;
        }
        TRecommend tRecommend = tRecommendService.getById(tRecommendId);
        //求职者自己投递
        MessageVO messageVO = new MessageVO();
        messageVO.setCode(2);
        Map sy004TjInterview = MessageUtil.SY004_TJ_BE_RECOMMENDED;
        EiCompanyPost byId = eiCompanyPostService.getById(postId);
        Map map = new HashMap();
        //  map.put("putInResume", putInResume);
        map.put("postLabel", byId.getPostLabel());
        map.put("company_name", byId.getCompanyName());
        map.put("referrer", gentlemanAndLady(tRecommend.getUsername(), tRecommend.getSex()));
        sy004TjInterview.put("content", JSON.toJSONString(map));
        sy004TjInterview.put("chatId", jobHunter);
        messageVO.setMessage(sy004TjInterview);
        messageProduce.sendOrderlyMessage(JSON.toJSONString(messageVO));


    }



    public String gentlemanAndLady(String name, int sex) {
        String substring = name.substring(0, 1);
        if (sex == 1) {
            return substring + "先生";
        } else {
            return substring + "女士";
        }
    }


    @Async
    public void receiveAnInterview(String putInResumeId) {
        Map mapList = putInResumeService.basicInformation(putInResumeId);
        EiCompanyPost postId = eiCompanyPostService.getById(mapList.get("postId").toString());
        MessageVO messageVO = new MessageVO();
        messageVO.setCode(2);
        Map sy004TcSendAResume = MessageUtil.SY004_BE_INTERVIEWED;
        int putid = MessageUtil.identity(postId.getCompanyId());
        sy004TcSendAResume.put("identity",putid);
        if(putid==0){
            sy004TcSendAResume.put("router","/Talent");
        }else if(putid==1){
            sy004TcSendAResume.put("router","/Recommhome");
        }else if(putid==2){
            sy004TcSendAResume.put("router","/Interview");
        }
        sy004TcSendAResume.put("chatId", postId.getCompanyId());
        Map map = new HashMap();
        map.put("postLabel", postId.getPostLabel());
        map.put("jobHunterName", mapList.get("name"));
        map.put("putInResumeId", putInResumeId);
        sy004TcSendAResume.put("content", JSON.toJSONString(map));

        messageVO.setMessage(sy004TcSendAResume);
        messageProduce.sendOrderlyMessage(JSON.toJSONString(messageVO));

    }

    @Async
    public void notReceiveAnInterview(String putInResumeId) {
        Map mapList = putInResumeService.basicInformation(putInResumeId);
        EiCompanyPost postId = eiCompanyPostService.getById(mapList.get("postId").toString());
        MessageVO messageVO = new MessageVO();
        messageVO.setCode(2);
        Map sy004TcSendAResume = MessageUtil.SY004_BE_NOT_INTERVIEWED;
        int putid = MessageUtil.identity(postId.getCompanyId());
        sy004TcSendAResume.put("identity",putid);
        if(putid==0){
            sy004TcSendAResume.put("router","/Talent");
        }else if(putid==1){
            sy004TcSendAResume.put("router","/Recommhome");
        }else if(putid==2){
            sy004TcSendAResume.put("router","/Interview");
        }
        sy004TcSendAResume.put("chatId", postId.getCompanyId());
        Map map = new HashMap();
        map.put("postLabel", postId.getPostLabel());
        map.put("jobHunterName", mapList.get("name"));
        map.put("putInResumeId", putInResumeId);
        sy004TcSendAResume.put("content", JSON.toJSONString(map));

        messageVO.setMessage(sy004TcSendAResume);
        messageProduce.sendOrderlyMessage(JSON.toJSONString(messageVO));

    }


    @Async
    public void interviewPresence(String putInResumeId, LocalDateTime dateTime) {
        Map mapList = putInResumeService.basicInformation(putInResumeId);
        EiCompanyPost postId = eiCompanyPostService.getById(mapList.get("postId").toString());
        MessageVO messageVO = new MessageVO();
        messageVO.setCode(2);
        Map sy004TcSendAResume = MessageUtil.SY004_INTERVIEW_PRESENCE;
        int putid = MessageUtil.identity(postId.getCompanyId());
        sy004TcSendAResume.put("identity",putid);
        if(putid==0){
            sy004TcSendAResume.put("router","/Talent");
        }else if(putid==1){
            sy004TcSendAResume.put("router","/Recommhome");
        }else if(putid==2){
            sy004TcSendAResume.put("router","/Interview");
        }
        sy004TcSendAResume.put("chatId", postId.getCompanyId());
        Map map = new HashMap();
        map.put("postLabel", postId.getPostLabel());
        map.put("jobHunterName", mapList.get("name"));
        map.put("putInResumeId", putInResumeId);
        map.put("putInResumeId", putInResumeId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        map.put("arrivalTime", dateTime.format(formatter));

        sy004TcSendAResume.put("content", JSON.toJSONString(map));

        messageVO.setMessage(sy004TcSendAResume);
        messageProduce.sendOrderlyMessage(JSON.toJSONString(messageVO));

    }

    /**
     * 金币奖励通知
     *
     * @param userId
     * @param gold
     */
    @Async
    public void goldReward(String userId, String gold) {
        String substring = userId.substring(0, 2);

        MessageVO messageVO = new MessageVO();
        messageVO.setCode(2);
        Map sy004TcSendAResume = MessageUtil.SY001_GOLD_REWARD;
        sy004TcSendAResume.put("chatId", userId);
        if(substring.equals("TJ")){
            sy004TcSendAResume.put("identity","2");
            //sy004TcSendAResume.put("router","/TAccounthome");
        }else if(substring.equals("TR")){
            sy004TcSendAResume.put("identity","1");
            sy004TcSendAResume.put("router","/TAccounthome");
        }else {
            sy004TcSendAResume.put("identity","0");
            sy004TcSendAResume.put("router","/AccountGold");
        }
        String content = sy004TcSendAResume.get("content").toString();
        String format = String.format(content, gold);
        sy004TcSendAResume.put("content", format);
        messageVO.setMessage(sy004TcSendAResume);
        messageProduce.sendOrderlyMessage(JSON.toJSONString(messageVO));
    }

    /**
     * 岗位审核通过通知
     *
     * @param userId
     */
    @Async
    public void postAudit(String userId) {
        MessageVO messageVO = new MessageVO();
        messageVO.setCode(2);
        Map sy004TcSendAResume = MessageUtil.SY002_VERIFIED_POST;
        sy004TcSendAResume.put("chatId", userId);
        messageVO.setMessage(sy004TcSendAResume);
        messageProduce.sendOrderlyMessage(JSON.toJSONString(messageVO));
    }

    @Async
    public void noticeOfExpiration(String userId, LocalDate contractDate) {
        MessageVO messageVO = new MessageVO();
        messageVO.setCode(2);
        Map sy004TcSendAResume = MessageUtil.SY002_NOTICE_OF_EXPIRATION;
        String data="客户朋友,你好；你的服务合同于"+contractDate+"到期，请提前30天进行续期处理。";

        sy004TcSendAResume.put("content",data);
        sy004TcSendAResume.put("chatId", userId);
        messageVO.setMessage(sy004TcSendAResume);
        messageProduce.sendOrderlyMessage(JSON.toJSONString(messageVO));
    }


    /**
     * 入职通知
     *
     * @param putInResume
     */
    @Async
    public void entry(String putInResume) {
        Map mapList = putInResumeService.basicInformation(putInResume);
        //求职者自己投递
        MessageVO messageVO = new MessageVO();
        messageVO.setCode(2);
        Map sy004TjInterview = MessageUtil.SY004_ENTRY;
        int putid = MessageUtil.identity( mapList.get("putid").toString());
        sy004TjInterview.put("identity",putid);
        if(putid==0){
            sy004TjInterview.put("router","/Talent");
        }else if(putid==1){
            sy004TjInterview.put("router","/Recommhome");
        }else if(putid==2){
            sy004TjInterview.put("router","/Interview");
        }

        EiCompanyPost byId = eiCompanyPostService.getById(mapList.get("postId").toString());
        Map map = new HashMap();
        map.put("putInResume", putInResume);
        map.put("postLabel", byId.getPostLabel());
        map.put("company_name", byId.getCompanyName());
        if (mapList.get("name") != null) {
            map.put("jobHunterName", mapList.get("name"));
        }
        sy004TjInterview.put("content", JSON.toJSONString(map));
        sy004TjInterview.put("chatId", mapList.get("putid"));
        messageVO.setMessage(sy004TjInterview);
        messageProduce.sendOrderlyMessage(JSON.toJSONString(messageVO));
    }

    @Async
    public void notEntry(String putInResume) {
        Map mapList = putInResumeService.basicInformation(putInResume);
        //求职者自己投递
        MessageVO messageVO = new MessageVO();
        messageVO.setCode(2);
        Map sy004TjInterview = MessageUtil.SY004_NOT_ENTRY;
        int putid = MessageUtil.identity( mapList.get("putid").toString());
        sy004TjInterview.put("identity",putid);
        if(putid==0){
            sy004TjInterview.put("router","/Talent");
        }else if(putid==1){
            sy004TjInterview.put("router","/Recommhome");
        }else if(putid==2){
            sy004TjInterview.put("router","/Interview");
        }
        EiCompanyPost byId = eiCompanyPostService.getById(mapList.get("postId").toString());
        Map map = new HashMap();
        map.put("putInResume", putInResume);
        map.put("postLabel", byId.getPostLabel());
        map.put("company_name", byId.getCompanyName());
        if (mapList.get("name") != null) {
            map.put("jobHunterName", mapList.get("name"));
        }
        sy004TjInterview.put("content", JSON.toJSONString(map));
        sy004TjInterview.put("chatId", mapList.get("putid"));
        messageVO.setMessage(sy004TjInterview);
        messageProduce.sendOrderlyMessage(JSON.toJSONString(messageVO));
    }

    @Async
    public void fullMoon(String putInResume) {
        Map mapList = putInResumeService.basicInformation(putInResume);
        //求职者自己投递
        MessageVO messageVO = new MessageVO();
        messageVO.setCode(2);
        Map sy004TjInterview = MessageUtil.SY004_FULL_MOON;
        int putid = MessageUtil.identity( mapList.get("putid").toString());
        sy004TjInterview.put("identity",putid);
        if(putid==0){
            sy004TjInterview.put("router","/Talent");
        }else if(putid==1){
            sy004TjInterview.put("router","/Recommhome");
        }else if(putid==2){
            sy004TjInterview.put("router","/Interview");
        }
        EiCompanyPost byId = eiCompanyPostService.getById(mapList.get("postId").toString());
        Map map = new HashMap();
        map.put("putInResume", putInResume);
        map.put("postLabel", byId.getPostLabel());
        map.put("company_name", byId.getCompanyName());
        if (mapList.get("name") != null) {
            map.put("jobHunterName", mapList.get("name"));
        }
        sy004TjInterview.put("content", JSON.toJSONString(map));
        sy004TjInterview.put("chatId", mapList.get("putid"));
        messageVO.setMessage(sy004TjInterview);
        messageProduce.sendOrderlyMessage(JSON.toJSONString(messageVO));
    }

    @Async
    public void notFullMoon(String putInResume) {
        Map mapList = putInResumeService.basicInformation(putInResume);
        //求职者自己投递
        MessageVO messageVO = new MessageVO();
        messageVO.setCode(2);
        Map sy004TjInterview = MessageUtil.SY004_NOT_FULL_MOON;
        int putid = MessageUtil.identity( mapList.get("putid").toString());
        sy004TjInterview.put("identity",putid);
        if(putid==0){
            sy004TjInterview.put("router","/Talent");
        }else if(putid==1){
            sy004TjInterview.put("router","/Recommhome");
        }else if(putid==2){
            sy004TjInterview.put("router","/Interview");
        }
        EiCompanyPost byId = eiCompanyPostService.getById(mapList.get("postId").toString());
        Map map = new HashMap();
        map.put("putInResume", putInResume);
        map.put("postLabel", byId.getPostLabel());
        map.put("company_name", byId.getCompanyName());
        if (mapList.get("name") != null) {
            map.put("jobHunterName", mapList.get("name"));
        }
        sy004TjInterview.put("content", JSON.toJSONString(map));
        sy004TjInterview.put("chatId", mapList.get("putid"));
        messageVO.setMessage(sy004TjInterview);
        messageProduce.sendOrderlyMessage(JSON.toJSONString(messageVO));
    }


}
