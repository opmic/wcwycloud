package com.wcwy.company.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wcwy.common.base.result.R;
import com.wcwy.common.base.utils.HandleUtil;
import com.wcwy.common.redis.enums.RedisCache;
import com.wcwy.common.redis.enums.Sole;
import com.wcwy.common.redis.util.IDGenerator;
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.common.web.annotation.AutoIdempotent;
import com.wcwy.company.aliyun.SendSms;
import com.wcwy.company.asyn.InformAsync;
import com.wcwy.company.asyn.ReferrerRecordAsync;
import com.wcwy.company.asyn.RunningWaterAsync;
import com.wcwy.company.dto.NotEntryDTO;
import com.wcwy.company.dto.OneInterviewDTO;
import com.wcwy.company.entity.*;
import com.wcwy.company.service.*;
import com.wcwy.company.session.CompanyMetadata;
import com.wcwy.company.vo.InviteEntryVO;
import com.wcwy.company.vo.PutInResumeRecordVO;
import com.wcwy.post.entity.TPayConfig;
import com.wcwy.system.annotation.Log;
import com.wcwy.system.enums.BusinessType;
import io.swagger.annotations.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

/**
 * ClassName: InviteEntryController
 * Description:
 * date: 2023/4/17 10:26
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Api(tags = "发送offer")
@RequestMapping("/inviteEntry")
@RestController
public class InviteEntryController {
    @Autowired
    private InviteEntryService inviteEntryService;
    @Autowired
    private InterviewResumeService interviewResumeService;
    @Autowired
    private PutInResumeService putInResumeService;
    @Autowired
    private IDGenerator idGenerator;
    @Autowired
    private EiCompanyPostService eiCompanyPostService;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private RunningWaterAsync runningWaterAsync;
    @Autowired
    private CompanyMetadata companyMetadata;
    @Autowired
    private PutInResumeRecordService putInResumeRecordService;
    @Autowired
    private SendSms sendSms;
    @Autowired
    private GetInvolvedService getInvolvedService;
    @Autowired
    private InformAsync informAsync;
    @Autowired
    private ReferrerRecordAsync referrerRecordAsync;
    @PostMapping("/invitation")
    @ApiOperation("岗位发送offer邀请")
    @Log(title = "岗位发送offer邀请", businessType = BusinessType.INSERT)
    @Transactional(rollbackFor = Exception.class)
    @AutoIdempotent
    public R invitation(@Valid @RequestBody InviteEntryVO vo) throws Exception {
        PutInResume byId = putInResumeService.getById(vo.getPutInResumeId());
        if (byId == null) {
            return R.fail("该岗位不存在!");
        }
        if (!byId.getPutInComppany().equals(companyMetadata.userid())) {
            return R.fail("操作企业不一致!");
        }
        //判断岗位发布类型
        EiCompanyPost byId2 = eiCompanyPostService.getById(byId.getPutInPost());
        if(byId2 !=null){
            Integer postType = byId2.getPostType();
            if(postType>=4){
                return R.fail("简历付不能发送面试邀请!");
            }
        }
        //查询是否发送过offer
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("put_in_resume_id", vo.getPutInResumeId());
        queryWrapper.eq("post_id", byId.getPutInPost());
        int count = inviteEntryService.count(queryWrapper);
        if (count > 0) {
            return R.fail("已发送offer,请不要重复发送");
        }
        //查询岗位的入职付和满月付金额
        EiCompanyPost byId1 = eiCompanyPostService.getById(byId.getPutInPost());
        InviteEntry inviteEntry = new InviteEntry();
        inviteEntry.setInviteEntryId(idGenerator.generateCode("IVE"));
        BeanUtils.copyProperties(vo, inviteEntry);
        //  inviteEntry.setPostType(0);
        inviteEntry.setPostId(byId.getPutInPost());
        inviteEntry.setDeleted(0);
        inviteEntry.setHiredBounty(vo.getHiredBounty());
        inviteEntry.setPostType(byId1.getPostType());
        inviteEntry.setCreateTime(LocalDateTime.now());
        inviteEntry.setCreateId(companyMetadata.userid());
        if (byId1.getPostType() != 0) {
            TPayConfig tPayConfig = (TPayConfig) redisUtils.get(RedisCache.TPAYCONFIG.getValue());
            if (byId1.getPostType() == 1) {
                inviteEntry.setPercentage(tPayConfig.getEntryPayment());
            } else if (byId1.getPostType() == 2) {
                inviteEntry.setPercentage(tPayConfig.getFullMoonPayment());
            } else if (byId1.getPostType() == 3) {
                inviteEntry.setPercentage(tPayConfig.getInterviewPayment());
            }
        }
    /*    if(byId1.getPostType() != 0){
            inviteEntry.setHiredBounty(byId1.getHiredBounty());
            inviteEntry.setPostType(byId1.getPostType());
        }*/
    /*    if (byId1.getPostType() == 1) {

        }*/
/*        if (byId1.getPostType() == 2) {
            inviteEntry.setMoneyReward(byId1.getHiredBounty());
            inviteEntry.setPostType(byId1.getPostType());
        }*/
        boolean save = inviteEntryService.save(inviteEntry);
        if (save) {
            //修改面试结果
            QueryWrapper queryWrapper1=new QueryWrapper();
            queryWrapper1.eq("put_in_resume_id",byId.getPutInResumeId());
            queryWrapper1.orderByDesc("create_time");

            Page<InterviewResume> page = interviewResumeService.page(new Page<>(0, 1), queryWrapper1);
            if(page.getTotal()>0){
                InterviewResume interviewResume = page.getRecords().get(0);
                interviewResume.setStage(6);
                interviewResumeService.updateById(interviewResume);
            }
           // page.get
            //保证数据的唯一性
            CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
                putInResumeService.updateRedisPost(byId.getPutInJobhunter(), byId.getPutInPost(), 7, byId.getPutInUser());

                return null;
            });
            putInResumeService.updateResumeState(vo.getPutInResumeId(), 8);
            PutInResumeRecordVO putInResumeRecordVO = new PutInResumeRecordVO();
            putInResumeRecordVO.setCause("");
            putInResumeRecordVO.setDate(new Date());
            putInResumeRecordVO.setState(11);
            putInResumeRecordVO.setIdentity(HandleUtil.handle(companyMetadata.userid()));
            putInResumeRecordVO.setName(putInResumeService.getCompanyName(byId.getPutInResumeId()));
            putInResumeRecordService.addRecord(byId.getPutInResumeId(), putInResumeRecordVO);
            sendSms.acceptOffer(byId.getPutInResumeId());
            runningWaterAsync.offer(companyMetadata.userid(),byId.getPutInJobhunter(),byId.getPutInUser());
            String substring = byId.getPutInUser().substring(0, 2);
            if(substring.equals("TR")){
               referrerRecordAsync.offer(byId.getPutInUser(),byId.getPutInJobhunter());
           }
            //发送offer通知
            informAsync.offer(vo.getPutInResumeId());
            return R.success("发送成功!");
        }
        //没有成功则删除
        return R.fail("发送失败!");
    }

    @GetMapping("/examine")
    @ApiImplicitParam(name = "putInResumeId", required = true, value = "投简ID")
    @ApiOperation(value = "查看offer")
    @Log(title = "查看offer", businessType = BusinessType.SELECT)
    public R<InviteEntry> examine(@RequestParam("putInResumeId") String putInResumeId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("put_in_resume_id", putInResumeId);
        InviteEntry one = inviteEntryService.getOne(queryWrapper);
        //更新一下消息
        if ("TJ".equals(companyMetadata.userid().substring(0, 2))) {
            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("put_in_resume_id", putInResumeId);
            updateWrapper.set("put_in_message", 0);
            putInResumeService.update(updateWrapper);
        }
        return R.success(one);
    }


 /*   @GetMapping("/notOnBoard")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "inviteEntryId", required = true, value = "offer编号"),
            @ApiImplicitParam(name = "cause", required = true, value = "未入职申请原因")
    })
    @ApiOperation(value = "未入职申请")
    @Log(title = "未入职申请", businessType = BusinessType.UPDATE)

    public R notOnBoard(@RequestParam("inviteEntryId") String inviteEntryId, @RequestParam("cause") String cause) {
        InviteEntry byId = inviteEntryService.getById(inviteEntryId);
        if(byId ==null){
            return R.fail("该offer不存在!");
        }
        if(byId.getPostType() !=1 || byId.getPostType()!=2){
            return R.fail("非入职付满月付请线下联系");
        }
        UpdateWrapper updateWrapper=new UpdateWrapper();
        updateWrapper.eq("invite_entry_id",inviteEntryId);
        updateWrapper.eq("create_id",companyMetadata.userid());
        updateWrapper.set("cancel",2);
        updateWrapper.set("cancel_cause",cause);
        updateWrapper.set("put_in_consent",0);
        boolean update = inviteEntryService.update(updateWrapper);
        if(update){
            UpdateWrapper updateWrapper1=new UpdateWrapper();
            updateWrapper1.eq("put_in_resume_id",byId.getPutInResumeId());
            updateWrapper1.set("resume_state",9);
            updateWrapper1.set("entry_if",2);
            updateWrapper1
            putInResumeService.update(updateWrapper1);
            return R.success("操作成功!");
        }
        return R.fail("操作失败!");
    }*/

    @GetMapping("/notEntry")
    @ApiImplicitParam(name = "putInResumeId", required = true, value = "投简ID")
    @ApiOperation(value = "查看未入职申请")
    @Log(title = "查看未入职申请", businessType = BusinessType.SELECT)
    public R<NotEntryDTO> notEntry(@RequestParam("putInResumeId") String putInResumeId) {
        NotEntryDTO notEntryDTO = inviteEntryService.notEntry(putInResumeId);
        return R.success(notEntryDTO);
    }

    @GetMapping("/consentNotEntry")
    @ApiOperation(value = "是否同意未入职申请")
    @Log(title = "是否同意未入职申请", businessType = BusinessType.UPDATE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "putInResumeId", required = true, value = "投简id"),
            @ApiImplicitParam(name = "cause", required = false, value = "介入原因"),
            @ApiImplicitParam(name = "type", required = true, value = "操作状态(1:同意 2:客服介入)")
    })
    @Transactional(rollbackFor = Exception.class)
    @AutoIdempotent
    public R consentNotEntry(@RequestParam("putInResumeId") String putInResumeId, @RequestParam(value = "cause", required = false) String cause, @RequestParam("type") Integer type) {
        //查询是否存在
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("put_in_resume_id", putInResumeId);
        InviteEntry one = inviteEntryService.getOne(queryWrapper);
        if (one == null) {
            return R.fail("未查到入申请");
        }
        //查询是否已经操作
        if (one.getPutInConsent() != 0) {
            return R.fail("请不要重复操作!");
        }


        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("put_in_resume_id", putInResumeId);
        if (type == 1) {
            updateWrapper.set("put_in_consent", 2);
            updateWrapper.set("update_consent_time", LocalDateTime.now());
        } else if (type == 2) {
            updateWrapper.set("put_in_consent", 3);
            updateWrapper.set("update_consent_time", LocalDateTime.now());
            updateWrapper.set("consent_cause", cause);

        }else {
            return R.fail("未知操作！");
        }
        boolean update = inviteEntryService.update(updateWrapper);
        if (update) {
            if (type == 2) {
                GetInvolved getInvolved = new GetInvolved();
                getInvolved.setInviteEntryId(one.getInviteEntryId());
                getInvolved.setCause(cause);
                getInvolved.setPutInResumeId(putInResumeId);
                getInvolved.setCreateTime(LocalDateTime.now());
                getInvolved.setSate(0);
                getInvolvedService.save(getInvolved);
            }

            UpdateWrapper updateWrapper1 = new UpdateWrapper();
            updateWrapper1.eq("put_in_resume_id", putInResumeId);
            if (type == 1) {
                updateWrapper1.set("not_entry", 3);
                updateWrapper1.set("close_an_account_if",1);
                updateWrapper1.set("close_an_account_cause",one.getCancelCause());
            } else if (type == 2) {
                updateWrapper1.set("not_entry", 2);
            }
            updateWrapper1.set("put_in_message",0);
            updateWrapper1.set("update_time",LocalDateTime.now());
            putInResumeService.update(updateWrapper1);
            return R.success("操作成功！");

        }
        return R.fail("操作失败！");
    }


    @GetMapping("/oneInterview")
    @ApiOperation(value = "获取第一场面试")
    @Log(title = "获取第一场面试", businessType = BusinessType.SELECT)
    @ApiImplicitParam(name = "putInResumeId", required = true, value = "投简id")
    public R<OneInterviewDTO> oneInterview(@RequestParam("putInResumeId") String putInResumeId){
        OneInterviewDTO oneInterviewDTO=   inviteEntryService.oneInterview(companyMetadata.userid(),putInResumeId);
        return R.success(oneInterviewDTO);
    }
}
