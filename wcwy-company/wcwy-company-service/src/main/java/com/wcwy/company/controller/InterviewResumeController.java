package com.wcwy.company.controller;

import cn.hutool.core.util.PhoneUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wcwy.common.base.result.R;
import com.wcwy.common.base.utils.HandleUtil;
import com.wcwy.common.redis.enums.Message;
import com.wcwy.common.redis.util.IDGenerator;
import com.wcwy.common.web.annotation.AutoIdempotent;
import com.wcwy.company.aliyun.SendSms;
import com.wcwy.company.asyn.InformAsync;
import com.wcwy.company.asyn.ReferrerRecordAsync;
import com.wcwy.company.asyn.RunningWaterAsync;
import com.wcwy.company.dto.InterviewResumePostDTO;
import com.wcwy.company.entity.EiCompanyPost;
import com.wcwy.company.entity.InterviewResume;
import com.wcwy.company.entity.PutInResume;
import com.wcwy.company.po.DetailedAddressPO;
import com.wcwy.company.service.*;
import com.wcwy.company.session.CompanyMetadata;
import com.wcwy.company.vo.*;
import com.wcwy.system.annotation.Log;
import com.wcwy.system.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

/**
 * @author Administrator
 * @description 针对表【interview_resume(面试邀请表)】的数据库操作Service
 * @createDate 2022-10-27 11:44:12
 */
@Api(tags = "面试邀请接口")
@RequestMapping("/interviewResumeService")
@RestController
public class InterviewResumeController {
    @Autowired
    private InterviewResumeService interviewResumeService;
    @Autowired
    private CompanyMetadata companyMetadata;
    @Autowired
    private IDGenerator idGenerator;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RunningWaterService runningWaterService;
    @Autowired
    private RunningWaterAsync runningWaterAsync;
    @Autowired
    private ReferrerRecordAsync referrerRecordAsync;
    @Resource
    private PutInResumeService putInResumeService;
    @Autowired
    private CollectJobHunterService collectJobHunterService;
    @Autowired
    private CompanyUserRoleService companyUserRoleService;
    @Autowired
    private PutInResumeRecordService putInResumeRecordService;
    @Autowired
    private SendSms sendSms;
    @Autowired
    private InformAsync informAsync;
    @Autowired
    private  EiCompanyPostService eiCompanyPostService;
    @PostMapping("/addInterviewResume")
    @ApiOperation("添加面试邀请")
    @Transactional(rollbackFor = Exception.class)
    @Log(title = "添加面试邀请", businessType = BusinessType.INSERT)
    @AutoIdempotent
    public R addInterviewResume(@Valid @RequestBody InterviewResumeVO interviewResumeVO) throws Exception {
        /*SetOperations<String, Object> setOperations = redisTemplate.opsForSet();*/
        boolean mobileNumber = com.wcwy.common.base.utils.PhoneUtil.isMobileNumber(interviewResumeVO.getPhone());
        if (!mobileNumber) {
            return R.fail("联系方式不能为座机,请检查手机号是否正确!");
        }

        if(Integer.parseInt(interviewResumeVO.getInterviewWay())==2 ){
            DetailedAddressPO place = interviewResumeVO.getPlace();
            if (place == null || StringUtils.isEmpty(place.getAddress())) {
                return R.fail("详细地址不能为空！");
            }
        }

        PutInResume byId = putInResumeService.getById(interviewResumeVO.getPutInResumeId());
        if (byId == null) {
            return R.fail("该投简不存在!");
        }
        String putInComppany = byId.getPutInComppany().substring(0,2);
        if(putInComppany.equals("TR")){
            return R.fail("自营岗位不能才加面试!");
        }
        //判断岗位发布类型
        EiCompanyPost byId1 = eiCompanyPostService.getById(byId.getPutInPost());
        if(byId1 !=null){
            Integer postType = byId1.getPostType();
            if(postType>=4){
                return R.fail("简历付不能发送面试邀请!");
            }
        }


        if (byId.getResumeState() == 3 || byId.getResumeState() >= 7) {
            return R.fail("此次投简已处理完毕,请不要重复发送面试邀请!");
        }
        if (!byId.getPutInComppany().equals(companyMetadata.userid())) {
            return R.fail("非法操作！");
        }
        String jobHunterId = interviewResumeVO.getJobHunterId();
        //判断企业是否下载过该简历
        String putInUser = byId.getPutInUser();
        //判断是否是求职者投简 求职者投简则需要判断是否下载  推荐官推荐可以直接发起面试邀请
        if ("TJ".equals(putInUser.substring(0, 2))) {
            if ("TJ".equals(jobHunterId.substring(0, 2))) {
                QueryWrapper queryWrapper1 = new QueryWrapper();
                queryWrapper1.eq("user_id", interviewResumeVO.getJobHunterId());
                queryWrapper1.eq("company_id", companyMetadata.userid());
                queryWrapper1.eq("deleted", 0);
                int count1 = companyUserRoleService.count(queryWrapper1);
                if (count1 == 0) {
                    return R.fail("请先下载该简历!");
                }
            }
        }
        boolean phone = PhoneUtil.isPhone(interviewResumeVO.getPhone());
        if (!phone) {
            return R.fail("电话格式不正确" + interviewResumeVO.getPhone());
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("put_in_resume_id", interviewResumeVO.getPutInResumeId());
        queryWrapper.eq("completion_status", 0);
        int count = interviewResumeService.count(queryWrapper);
        if (count > 0) {
            return R.fail("您有一场面试未处理,不能重复发送面试邀请!");
        }
        InterviewResume interviewResume = new InterviewResume();
        BeanUtils.copyProperties(interviewResumeVO, interviewResume);
        interviewResume.setInterviewId(idGenerator.generateCode("IWR"));
        interviewResume.setCreateTime(LocalDateTime.now());
        interviewResume.setInterviewer(interviewResume.getInterviewer());
        interviewResume.setTake(1);
        interviewResume.setStage(1);
        interviewResume.setCreateUser(companyMetadata.userid());
        interviewResume.setDeleted(0);
        interviewResume.setUpdateTime(LocalDateTime.now());
        interviewResume.setUpdateUser(companyMetadata.userid());
        boolean save = interviewResumeService.save(interviewResume);
        if (save) {
            PutInResumeRecordVO putInResumeRecordVO = new PutInResumeRecordVO();
            putInResumeRecordVO.setCause("");
            putInResumeRecordVO.setDate(new Date());
            putInResumeRecordVO.setState(4);
            putInResumeRecordVO.setIdentity(HandleUtil.handle(companyMetadata.userid()));
            putInResumeRecordVO.setName(putInResumeService.getCompanyName(interviewResumeVO.getPutInResumeId()));
            putInResumeRecordService.addRecord(interviewResumeVO.getPutInResumeId(), putInResumeRecordVO);
         /*   CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
                putInResumeService.updateRedisPost(interviewResumeVO.getJobHunterId(), interviewResumeVO.getPostId(), 4, interviewResumeVO.getPutInUser());
                return null;
            });*/
            putInResumeService.updateRedisPost(interviewResumeVO.getJobHunterId(), interviewResumeVO.getPostId(), 4, interviewResumeVO.getPutInUser());

            //  setOperations.add(Message.REFERRER_INTERVIEW.getKey() + interviewResumeVO.getPutInResumeId(), interviewResumeVO.getPutInResumeId());//给投递岗位的人一条消息
            this.putInResumeUpdateExclude(interviewResumeVO.getPutInResumeId(), 2, null, "", interviewResumeVO.getInterviewTime());
            if ("TR".equals(interviewResumeVO.getPutInUser().substring(0, 2))) {
                sendSms.interviewNotice(interviewResumeVO.getInterviewTime().toString(), interviewResumeVO.getPutInUser());
                referrerRecordAsync.appoint(interviewResumeVO.getPutInUser(),interviewResumeVO.getJobHunterId());
            }
            runningWaterAsync.interviewInvitation(companyMetadata.userid(),interviewResumeVO.getJobHunterId(),byId.getPutInUser());
            //消息通知
            informAsync.inviteInterview(interviewResumeVO.getPutInResumeId(),interviewResumeVO.getPostId());
            return R.success("已发送邀请！");
        }
        return R.fail("发送邀请失败!");
    }

    @PostMapping("/SuggestInterviewResume")
    @ApiOperation("是否接受面试试邀请")
    @Transactional(rollbackFor = Exception.class)
    @Log(title = "是否接受面试试邀请", businessType = BusinessType.UPDATE)
    @AutoIdempotent
    public R suggestInterviewResume(@Valid @RequestBody SuggestInterviewResume suggestInterviewResume) {
        InterviewResume byId1 = interviewResumeService.getById(suggestInterviewResume.getInterviewId());
        if (byId1.getCompletionStatus() != 0) {
            return R.fail("此次面试已经结束,记录不能修改!");
        }
        if (byId1.getTake() != 1) {
            return R.fail("此次面试你已处理,请不要重复处理!");
        }
        if (suggestInterviewResume.getTake() == 3 && StringUtils.isEmpty(suggestInterviewResume.getNoTakeCause())) {
            return R.fail("请填写不接受原因");
        }

        //减少一个面试人
        if (suggestInterviewResume.getTake() == 3) {
            PutInResume byId = putInResumeService.getById(suggestInterviewResume.getPutInResumeId());

            if (!byId.getPutInUser().equals(companyMetadata.userid())) {
                return R.fail("非法操作！");
            }
            if (byId != null) {
                //不接受则清除一条面试记录
           /*     CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
                    putInResumeService.cancelRedisPost(byId.getPutInJobhunter(), byId.getPutInPost(), 4, byId.getPutInUser());
                    return null;
                });*/
                putInResumeService.cancelRedisPost(byId.getPutInJobhunter(), byId.getPutInPost(), 4, byId.getPutInUser());
                String substring = companyMetadata.userid().substring(0, 2);
                if(substring.equals("TR")){
                    referrerRecordAsync.interview(companyMetadata.userid(),byId.getPutInJobhunter());
                }
            }
        }
        UpdateWrapper updateWrapper = new UpdateWrapper();
        if (suggestInterviewResume.getTake() == 3) {
            updateWrapper.set("completion_status", 1);

        }
        updateWrapper.eq("interview_id", suggestInterviewResume.getInterviewId());
        updateWrapper.set("take", suggestInterviewResume.getTake());
        updateWrapper.set("stage", suggestInterviewResume.getTake());
        updateWrapper.set("no_take_cause", suggestInterviewResume.getNoTakeCause());
        /* updateWrapper.set("suggest_time", suggestInterviewResume.getSuggestTime());*/
        updateWrapper.set("accept", 1);
        updateWrapper.set("update_time", LocalDateTime.now());
        updateWrapper.set("update_user", companyMetadata.userid());
        boolean update = interviewResumeService.update(updateWrapper);
        if (update) {
            // setOperations.remove(Message.REFERRER_INTERVIEW.getKey() + suggestInterviewResume.getPutInResumeId(), suggestInterviewResume.getPutInResumeId());//给投递岗位的人一条消息
            //  setOperations.add(Message.COMPANY_INTERVIEW.getKey() + suggestInterviewResume.getPutInResumeId(), suggestInterviewResume.getPutInResumeId());//给投递岗位的人一条消息
            this.disposeInterview(suggestInterviewResume.getPutInResumeId(), suggestInterviewResume.getTake() == 3 ? 1 : 2, suggestInterviewResume.getNoTakeCause(), byId1.getInterviewTime());
            PutInResumeRecordVO putInResumeRecordVO = new PutInResumeRecordVO();
            putInResumeRecordVO.setCause(suggestInterviewResume.getNoTakeCause());
            putInResumeRecordVO.setDate(new Date());
            if (suggestInterviewResume.getTake() == 2) {
                putInResumeRecordVO.setState(5);
            }
            if (suggestInterviewResume.getTake() == 3) {
                putInResumeRecordVO.setState(6);
            }

            putInResumeRecordVO.setIdentity(HandleUtil.handle(companyMetadata.userid()));
            putInResumeRecordVO.setName(putInResumeService.getPutName(suggestInterviewResume.getPutInResumeId()));
            putInResumeRecordService.addRecord(suggestInterviewResume.getPutInResumeId(), putInResumeRecordVO);
            if ("TR".equals(companyMetadata.userid().substring(0, 2))) {

                if (suggestInterviewResume.getTake() == 3) {
                    sendSms.refuseInterview(byId1.getPutInResumeId());
                } else if (suggestInterviewResume.getTake() == 2) {
                    sendSms.acceptInterview(byId1.getPutInResumeId());
                }
            }
            if(suggestInterviewResume.getTake()==2){
                informAsync.receiveAnInterview(byId1.getPutInResumeId());
            }else if(suggestInterviewResume.getTake()==3){
                informAsync.notReceiveAnInterview(byId1.getPutInResumeId());
            }
            return R.success();
        }
        return R.fail("操作失败!");
    }

/*    @GetMapping("/acceptInterviewResume")
    @ApiOperation("企业是否接受面试推荐人建议时间")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "interviewId", required = true, value = "面试id"),
            @ApiImplicitParam(name = "putInResumeId", required = true, value = "简历投递id"),
            @ApiImplicitParam(name = "accept", required = true, value = "企业是否同意(2:同意 3:不同意)")
    })
    @Transactional(rollbackFor = Exception.class)
    public R acceptInterviewResume(@RequestParam("interviewId") String interviewId, @RequestParam("putInResumeId") String putInResumeId, @RequestParam("accept") Integer accept) {
        if (StringUtils.isEmpty(interviewId) && StringUtils.isEmpty(putInResumeId) && "".equals(accept)) {
            return R.fail("请按要求填写数据");
        }
        SetOperations<String, Object> setOperations = redisTemplate.opsForSet();
        InterviewResume byId = interviewResumeService.getById(interviewId);
        if (byId == null) {
            return R.fail("无此面试邀请");
        }
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("interview_id", interviewId);
        updateWrapper.set("accept", accept);
        updateWrapper.set("take", accept);
        if (accept == 2) {
            PutInResume putInResume = putInResumeService.getById(putInResumeId);
            if (putInResume != null) {
                CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
                    putInResumeService.updateRedisPost(putInResume.getPutInJobhunter(), putInResume.getPutInPost(), 4, putInResume.getPutInUser());
                    return null;
                });

            }
            updateWrapper.set("interview_time", byId.getSuggestTime());
        }
        boolean update = interviewResumeService.update(updateWrapper);
        if (update) {
            //setOperations.add(Message.REFERRER_INTERVIEW.getKey() + putInResumeId, putInResumeId);//给投递岗位的人一条消息
            // setOperations.remove(Message.COMPANY_INTERVIEW.getKey() + putInResumeId, putInResumeId);//给投递岗位的人一条消息
            this.putInResumeUpdateExclude(putInResumeId, 2, accept == 3 ? 1 : 2, null, byId.getSuggestTime());
            return R.success("操作成功!");
        }
        return R.fail("操作失败!");
    }*/

    @PostMapping("/updateInterviewResume")
    @ApiOperation("修改面试邀请")
    @Transactional(rollbackFor = Exception.class)
    @Log(title = "修改面试邀请", businessType = BusinessType.UPDATE)
    @AutoIdempotent
    public R updateInterviewResume(@Valid @RequestBody UpdateInterviewResume updateInterviewResume) {
        boolean mobileNumber = com.wcwy.common.base.utils.PhoneUtil.isMobileNumber(updateInterviewResume.getPhone());
        if (!mobileNumber) {
            return R.fail("联系方式不能为座机,请检查手机号是否正确!");
        }
        InterviewResume byId = interviewResumeService.getById(updateInterviewResume.getInterviewId());
        if (byId == null) {
            return R.fail("无此面试邀请");
        }
        if (byId.getCompletionStatus() != 0) {
            return R.fail("此次面试已完成,不能再次修改!");
        }
        if (!byId.getCreateUser().equals(companyMetadata.userid())) {
            return R.fail("非法操作！");
        }
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("interview_id", updateInterviewResume.getInterviewId());
        updateWrapper.set("interview_time", updateInterviewResume.getInterviewTime());
        updateWrapper.set("interview_way", updateInterviewResume.getInterviewWay());
        updateWrapper.set("platform", updateInterviewResume.getPlatform());
        updateWrapper.set("remark", updateInterviewResume.getRemark());
        updateWrapper.set("place", JSON.toJSONString(updateInterviewResume.getPlace()));
        updateWrapper.set("on_access", updateInterviewResume.getOnAccess());
        updateWrapper.set("interviewer", JSON.toJSONString(updateInterviewResume.getInterviewer()));
        updateWrapper.set("update_interview_cause", updateInterviewResume.getUpdateInterviewCause());
        updateWrapper.set("remark_option", JSON.toJSONString(updateInterviewResume.getRemarkOption()));
        updateWrapper.set("phone", updateInterviewResume.getPhone());
        updateWrapper.set("take", 1);
        updateWrapper.set("stage", 1);
        updateWrapper.set("update_time", LocalDateTime.now());
        updateWrapper.set("update_user", companyMetadata.userid());
        boolean update = interviewResumeService.update(updateWrapper);
        if (update) {
            putInResumeService.updateInterview(byId.getPutInResumeId(), updateInterviewResume.getInterviewTime());
            PutInResumeRecordVO putInResumeRecordVO = new PutInResumeRecordVO();
            putInResumeRecordVO.setCause(updateInterviewResume.getUpdateInterviewCause());
            putInResumeRecordVO.setDate(new Date());
            putInResumeRecordVO.setState(7);
            putInResumeRecordVO.setIdentity(HandleUtil.handle(companyMetadata.userid()));
            putInResumeRecordVO.setName(putInResumeService.getCompanyName(byId.getPutInResumeId()));
            putInResumeRecordService.addRecord(byId.getPutInResumeId(), putInResumeRecordVO);
            sendSms.updateInterview(byId.getPutInResumeId(),updateInterviewResume.getInterviewTime().toString());
            informAsync.updateInviteInterview(byId.getPutInResumeId());
            return R.success("修改成功!");
        }
        return R.fail("修改失败!");
    }


    @GetMapping("/passTheInterview")
    @ApiOperation("面试通过")
    @Transactional(rollbackFor = Exception.class)
    @ApiImplicitParam(name = "interviewId", required = true, value = "面试id")
    @Log(title = "面试通过", businessType = BusinessType.UPDATE)
    @AutoIdempotent
    public R passTheInterview(@RequestParam("interviewId") String interviewId) {
        if (StringUtils.isEmpty(interviewId)) {
            return R.fail("传入值不能为为空!");
        }
        InterviewResume byId = interviewResumeService.getById(interviewId);
        if (byId == null) {
            return R.fail("无此面试邀请");
        }
        if (byId.getCompletionStatus() != 0) {
            return R.fail("此次面试已完成,不能再次修改!");
        }
        if (!byId.getCreateUser().equals(companyMetadata.userid())) {
            return R.fail("非法操作！");
        }
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("interview_id", interviewId);
        updateWrapper.eq("create_user", companyMetadata.userid());
        updateWrapper.set("completion_status", 1);
        updateWrapper.set("state", 3);
        updateWrapper.set("stage", 6);
        updateWrapper.set("state_time", LocalDateTime.now());
        updateWrapper.set("update_time", LocalDateTime.now());
        updateWrapper.set("update_user", companyMetadata.userid());
        boolean update = interviewResumeService.update(updateWrapper);
        if (update) {
            PutInResumeRecordVO putInResumeRecordVO = new PutInResumeRecordVO();
            putInResumeRecordVO.setCause("");
            putInResumeRecordVO.setDate(new Date());
            putInResumeRecordVO.setState(8);
            putInResumeRecordVO.setIdentity(HandleUtil.handle(companyMetadata.userid()));
            putInResumeRecordVO.setName(putInResumeService.getCompanyName(byId.getPutInResumeId()));
            putInResumeRecordService.addRecord(byId.getPutInResumeId(), putInResumeRecordVO);
            //sendSms.acceptResume()
            //如果是到面付产生订单

            return R.success("操作成功!");
        }
        return R.fail("操作失败!");
    }


    @GetMapping("/weedOut")
    @ApiOperation("面试淘汰")
    @Transactional(rollbackFor = Exception.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "interviewId", required = true, value = "面试id"),
            @ApiImplicitParam(name = "cause", required = true, value = "淘汰原因")
    })
    @Log(title = "面试淘汰", businessType = BusinessType.UPDATE)
    @AutoIdempotent
    public R weedOut(@RequestParam("interviewId") String interviewId, @RequestParam("cause") String cause) {
        if (StringUtils.isEmpty(interviewId)) {
            return R.fail("传入值不能为为空!");
        }
        if (StringUtils.isEmpty(cause)) {
            return R.fail("淘汰原因不能为空!");
        }
        InterviewResume byId = interviewResumeService.getById(interviewId);
        if (byId == null) {
            return R.fail("无此面试邀请!");
        }
        if (byId.getCompletionStatus() != 0) {
            return R.fail("此次面试已完成,不能再次修改!");
        }
        if (!byId.getCreateUser().equals(companyMetadata.userid())) {
            return R.fail("非法操作！");
        }
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("interview_id", interviewId);
        updateWrapper.eq("create_user", companyMetadata.userid());
        updateWrapper.set("completion_status", 1);
        updateWrapper.set("state", 4);
        updateWrapper.set("stage", 7);
        updateWrapper.set("state_cause", cause);
        updateWrapper.set("state_time", LocalDateTime.now());
        updateWrapper.set("update_time", LocalDateTime.now());
        updateWrapper.set("update_user", companyMetadata.userid());
        boolean update = interviewResumeService.update(updateWrapper);
        if (update) {
            putInResumeService.weedOut(byId.getPutInResumeId(), cause);
            PutInResume byId1 = putInResumeService.getById(byId.getPutInResumeId());
            String substring = byId1.getPutInUser().substring(0, 2);
            if(substring.equals("TR")){
                referrerRecordAsync.weedOut(byId1.getPutInUser(),byId1.getPutInJobhunter());
            }
            CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
                putInResumeService.updateRedisPost(byId1.getPutInJobhunter(), byId1.getPutInPost(), 6, byId1.getPutInUser());
                return null;
            });
            PutInResumeRecordVO putInResumeRecordVO = new PutInResumeRecordVO();
            putInResumeRecordVO.setCause(cause);
            putInResumeRecordVO.setDate(new Date());
            putInResumeRecordVO.setState(9);
            putInResumeRecordVO.setIdentity(HandleUtil.handle(companyMetadata.userid()));
            putInResumeRecordVO.setName(putInResumeService.getCompanyName(byId.getPutInResumeId()));
            putInResumeRecordService.addRecord(byId.getPutInResumeId(), putInResumeRecordVO);
            sendSms.weedOut(byId.getPutInResumeId());
            //如果是到面付产生订单

            informAsync.dieOut(byId.getPutInResumeId());
            return R.success("操作成功!");
        }
        return R.fail("操作失败!");
    }
/*
    @PostMapping("/auditInterviewResume")
    @ApiOperation("投简人是否同意企业修改")
    public R auditInterviewResume(@RequestBody AuditInterviewResume auditInterviewResume) {
        InterviewResume byId = interviewResumeService.getById(auditInterviewResume.getInterviewId());
        if (byId == null) {
            return R.fail("无此面试邀请");
        }
        UpdateWrapper updateWrapper = new UpdateWrapper();
        SetOperations<String, Object> setOperations = redisTemplate.opsForSet();
        updateWrapper.eq("interview_id", auditInterviewResume.getInterviewId());
        if (auditInterviewResume.getAuditState() == 3 && StringUtils.isEmpty(auditInterviewResume.getAuditCause())) {
            return R.fail("请填写不同意修改原因");
        }
        if (auditInterviewResume.getAuditState() == 3) {
            updateWrapper.set("take", 3);
            //减少一个面试人
            PutInResume putInResume = putInResumeService.getById(auditInterviewResume.getPutInResumeId());
            this.putInResumeUpdateExclude(auditInterviewResume.getPutInResumeId(), 2, auditInterviewResume.getAuditState() == 3 ? 1 : 2, auditInterviewResume.getAuditCause(), byId.getSuggestTime());
            if (byId != null) {
                CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
                    putInResumeService.cancelRedisPost(putInResume.getPutInJobhunter(), putInResume.getPutInPost(), 4, putInResume.getPutInUser());
                    return null;
                });

            }

        }
        if (auditInterviewResume.getAuditState() == 2) {
            updateWrapper.set("interview_time", byId.getUpdateInterviewTime());
            updateWrapper.set("interview_way", byId.getInterviewWay());
            updateWrapper.set("remark", byId.getRemark());
            updateWrapper.set("place", byId.getUpdatePlace());
            updateWrapper.set("on_access", byId.getUpdateOnAccess());
            updateWrapper.set("phone", byId.getUpdatePhone());
            updateWrapper.set("take", 2);
            this.putInResumeUpdateExclude(auditInterviewResume.getPutInResumeId(), 2, 2, null, byId.getUpdateInterviewTime());
        }
        updateWrapper.set("audit_state", auditInterviewResume.getAuditState());
        updateWrapper.set("audit_cause", auditInterviewResume.getAuditCause());
        updateWrapper.set("audit_user", companyMetadata.userid());
        updateWrapper.set("audit_time", LocalDateTime.now());
        boolean update = interviewResumeService.update(updateWrapper);
        if (update) {
            setOperations.remove(Message.REFERRER_INTERVIEW.getKey() + auditInterviewResume.getPutInResumeId(), auditInterviewResume.getPutInResumeId());//给投递岗位的人一条消息
            setOperations.add(Message.COMPANY_INTERVIEW.getKey() + auditInterviewResume.getPutInResumeId(), auditInterviewResume.getPutInResumeId());//给投递岗位的人一条消息
            return R.success("操作成功!");
        }
        return R.fail("操作失败!");
    }
*/


    /**
     * @param putInResumeId:投放简历id
     * @param subscribeIf:是否预约（1：否       2：是）
     * @param acceptSubscribe:是否接受预约（1：否 2：是）
     * @param acceptSubscribeState:不接受原因
     * @param subscribeTime:预约时间
     * @return null
     * @Description: 修改投简记录表
     * @Author tangzhuo
     * @CreateTime 2022/10/31 16:38
     */
    public Boolean putInResumeUpdateExclude(String putInResumeId, Integer subscribeIf, Integer acceptSubscribe, String acceptSubscribeState, LocalDateTime subscribeTime) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("put_in_resume_id", putInResumeId);
        queryWrapper.gt("state", 1);
        int count = interviewResumeService.count(queryWrapper);

        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("put_in_resume_id", putInResumeId);
        if (null != subscribeIf) {
            updateWrapper.set("subscribe_if", subscribeIf);

            if (count > 1) {
                updateWrapper.set("resume_state", 5);
            } else {
                updateWrapper.set("resume_state", 4);
            }
            updateWrapper.set("put_in_message", 1);
            updateWrapper.set("t_company_message", 0);
            updateWrapper.set("interview_if", 0);
            updateWrapper.set("exclude_if", 1);
            updateWrapper.set("exclude_time", LocalDateTime.now());
            updateWrapper.set("cancel_if", 0);
        }
        if (null != acceptSubscribe) {
            updateWrapper.set("accept_subscribe", acceptSubscribe);
        }
        updateWrapper.set("accept_subscribe_state", acceptSubscribeState);
        if (null != subscribeTime) {
            updateWrapper.set("subscribe_time", subscribeTime);
        }
        updateWrapper.set("update_time", LocalDateTime.now());
        boolean update = putInResumeService.update(updateWrapper);
        return update;
    }

    @GetMapping("/select")
    @ApiOperation("获取面试信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "putInResumeId", required = true, value = "投简id"),
            @ApiImplicitParam(name = "pageNo", required = true)
    })
    @Log(title = "获取面试信息", businessType = BusinessType.SELECT)
    public R<InterviewResume> select(@RequestParam("putInResumeId") String putInResumeId, @RequestParam("pageNo") Integer pageNo) {
        if (pageNo == null) {
            pageNo = 1;
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("put_in_resume_id", putInResumeId);
        //     queryWrapper.eq("create_user", companyMetadata.userid());
        queryWrapper.orderByDesc("create_time");
        Page<InterviewResume> page = interviewResumeService.page(new Page<>(pageNo, 1), queryWrapper);
        putInResumeService.updateMessage(companyMetadata.userid(),putInResumeId);
        return R.success(page);
    }

    @GetMapping("/cancelInterview")
    @ApiOperation("取消面试")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "interviewId", required = true, value = "面试id"),
            @ApiImplicitParam(name = "cause", required = true, value = "取消原因")
    })
    @Log(title = "取消面试", businessType = BusinessType.UPDATE)
    @AutoIdempotent
    public R cancelInterview(@RequestParam("interviewId") String interviewId, @RequestParam("cause") String cause) {

        if (StringUtils.isEmpty(interviewId)) {
            return R.fail("面试id不能为空!");
        }
        if (StringUtils.isEmpty(cause)) {
            return R.fail("取消原因不能为空!");
        }
        InterviewResume byId = interviewResumeService.getById(interviewId);
        if (byId == null) {
            return R.fail("不存在此面试");
        }
        if (byId.getTake() != 1) {
            return R.fail("此次面试投简人已处理,不能取消面试");
        }
        if (byId.getState() != 0) {
            return R.fail("该面试已处理,请不要重复处理！");
        }
        if (!byId.getCreateUser().equals(companyMetadata.userid())) {
            return R.fail("非法操作！");
        }
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("interview_id", interviewId);
        updateWrapper.set("state", 1);
        updateWrapper.set("state_cause", cause);
        updateWrapper.set("completion_status", 1);
        updateWrapper.set("stage", 4);
        updateWrapper.set("state_time", LocalDateTime.now());
        updateWrapper.set("update_time", LocalDateTime.now());
        updateWrapper.set("update_user", companyMetadata.userid());
        updateWrapper.set("take",4);
        boolean update = interviewResumeService.update(updateWrapper);
        if (update) {
            PutInResumeRecordVO putInResumeRecordVO = new PutInResumeRecordVO();
            putInResumeRecordVO.setCause(cause);
            putInResumeRecordVO.setDate(new Date());
            putInResumeRecordVO.setState(10);
            putInResumeRecordVO.setIdentity(HandleUtil.handle(companyMetadata.userid()));
            putInResumeRecordVO.setName(putInResumeService.getCompanyName(byId.getPutInResumeId()));
            putInResumeRecordService.addRecord(byId.getPutInResumeId(), putInResumeRecordVO);
            sendSms.cancelInterview(byId.getPutInResumeId());
            informAsync.cancelTheInterview(byId.getPutInResumeId());
            return R.success("取消成功!");
        }

        return R.fail("取消失败!");
    }


    /**
     * @param putInResumeId:投放简历id
     * @param acceptSubscribe:是否接受预约（1：否 2：是）
     * @param acceptSubscribeState:不接受原因
     * @param subscribeTime:预约时间
     * @return null
     * @Description: 修改投简记录表
     * @Author tangzhuo
     * @CreateTime 2022/10/31 16:38
     */
    public Boolean disposeInterview(String putInResumeId, Integer acceptSubscribe, String acceptSubscribeState, LocalDateTime subscribeTime) {
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("put_in_resume_id", putInResumeId);

        if (null != acceptSubscribe) {
            if (acceptSubscribe == 2) {
                updateWrapper.set("resume_state", 5);
                updateWrapper.set("interview_if", 2);

            }
            updateWrapper.set("put_in_message", 0);
            updateWrapper.set("t_company_message", 1);
            updateWrapper.set("accept_subscribe", acceptSubscribe);
        }
        updateWrapper.set("accept_subscribe_state", acceptSubscribeState);
        if (null != subscribeTime) {
            updateWrapper.set("interview_time", subscribeTime);
        }
        updateWrapper.set("update_time", LocalDateTime.now());
        boolean update = putInResumeService.update(updateWrapper);
        return update;
    }

    @GetMapping("/selectInterviewResumePost")
    @ApiOperation("求职者-我的面试")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "take", required = true, value = "是否接受面试(0全部 1:处理中 2:已接受 3:不接受)"),
            @ApiImplicitParam(name = "pageSize", required = true),
            @ApiImplicitParam(name = "pageNo", required = true)
    })
    @Log(title = "求职者-我的面试", businessType = BusinessType.SELECT)
    public R<InterviewResumePostDTO> selectInterviewResumePost(@RequestParam("take") Integer take, @RequestParam("pageSize") Integer pageSize, @RequestParam("pageNo") Integer pageNo) {
        if (pageSize == null) {
            pageSize = 10;
        }
        if (pageNo == null) {
            pageNo = 0;
        }
        String userid = companyMetadata.userid();
        IPage<InterviewResumePostDTO> iPage = interviewResumeService.selectInterviewResumePost(take, userid, new Page(pageNo, pageSize));
        return R.success(iPage);
    }


    /*cancelInterview*/

}
