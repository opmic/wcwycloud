package com.wcwy.company.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wcwy.common.base.enums.AccessTemplateCode;
import com.wcwy.common.base.query.PageQuery;
import com.wcwy.common.base.result.R;
import com.wcwy.common.base.utils.BirthdayUtils;
import com.wcwy.common.base.utils.DateUtils;
import com.wcwy.common.base.utils.HandleUtil;
import com.wcwy.common.base.utils.PhoneUtils;
import com.wcwy.common.redis.enums.*;
import com.wcwy.common.redis.util.IDGenerator;
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.common.web.annotation.AutoIdempotent;
import com.wcwy.company.aliyun.SendSms;
import com.wcwy.company.asyn.InformAsync;
import com.wcwy.company.asyn.ReferrerRecordAsync;
import com.wcwy.company.asyn.RunningWaterAsync;
import com.wcwy.company.dto.*;
import com.wcwy.company.entity.*;
import com.wcwy.company.produce.PostOrderProduce;
import com.wcwy.company.query.PutInResumeQuery;
import com.wcwy.company.query.RecommendAdministratorQuery;
import com.wcwy.company.query.SelectCompanyResumeQuery;
import com.wcwy.company.query.SendAResumeInformationQuery;
import com.wcwy.company.service.*;
import com.wcwy.company.session.CompanyMetadata;
import com.wcwy.company.vo.*;
import com.wcwy.post.api.CompanyUserRoleApi;
import com.wcwy.post.api.OrderInfoApi;
import com.wcwy.post.api.TCompanyPostApi;
import com.wcwy.post.api.TPostShareApi;
import com.wcwy.post.entity.TPayConfig;
import com.wcwy.post.po.TCompanyPostPO;
import com.wcwy.post.pojo.PutInResumeInterviewPOJO;
import com.wcwy.system.annotation.Log;
import com.wcwy.system.enums.BusinessType;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Administrator
 * @description 针对表【put_in_resume(投递简历表)】的数据库操作Service
 * @createDate 2022-10-13 17:15:29
 */
@RestController
@RequestMapping("/putInResume")
@Api(tags = "投递简历接口")
@Slf4j
public class PutInResumeController {
    @Resource
    private PutInResumeService putInResumeService;
    @Autowired
    private RunningWaterService runningWaterService;


    @Autowired
    private TCompanyPostApi tCompanyPostApi;
    @Autowired
    private RunningWaterAsync runningWaterAsync;

    @Autowired
    private ReferrerRecordAsync referrerRecordAsync;
    @Autowired
    private OrderInfoApi orderInfoApi;

    @Autowired
    private IDGenerator idGenerator;
    @Autowired
    private CompanyMetadata companyMetadata;
    @Autowired
    private TCompanyService tCompanyService;
    @Autowired
    private CompanyUserRoleApi companyUserRoleApi;
    @Autowired
    private TJobhunterService tJobhunterService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private TProvincesService tProvincesService;
    @Autowired
    private InviteEntryService inviteEntryService;
    @Autowired
    private UpdateEntryService updateEntryService;
    @Autowired
    private SendSms sendSms;
    @Autowired
    private TPostShareApi tPostShareApi;
    @Autowired
    private PostOrderProduce postOrderProduce;
    @Autowired
    private TJobhunterEducationRecordService tJobhunterEducationRecordService;
    /*    @Autowired
        private WebSocketServer webSocketServer;*/
    @Autowired
    private ReferrerRecordService referrerRecordService;
    @Autowired
    private EiCompanyPostService eiCompanyPostService;
    @Autowired
    private CollectJobHunterService collectJobHunterService;
    @Autowired
    private TJobhunterHideCompanyService tJobhunterHideCompanyService;
    @Autowired
    private CompanyUserRoleService companyUserRoleService;
    @Autowired
    private TJobhunterResumeService tJobhunterResumeService;
    @Autowired
    private TJobhunterExpectPositionService tJobhunterExpectPositionService;
    @Autowired
    private CollerctPostController collerctPostController;
    @Autowired
    private PutInResumeRecordService putInResumeRecordService;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private TRecommendService tRecommendService;
    @Autowired
    private SourceOfReturnsService sourceOfReturnsService;
    @Autowired
    private PositionAppliedService positionAppliedService;
    @Autowired
    private GetInvolvedService getInvolvedService;
    @Autowired
    private DivideIntoService divideIntoService;
    @Autowired
    private InformAsync informAsync;
/*    @PostConstruct
    public void cache(){

    }*/


    @ApiOperation("投简接口")
    @PostMapping("/save")
    @Log(title = "投简接口", businessType = BusinessType.INSERT)
    @AutoIdempotent
    @GlobalTransactional(rollbackFor = Exception.class)
    public R save(@RequestBody @Validated ValidList<PutInResumeVO> putInResumes) {
        if (putInResumes.size() == 0) {
            return R.fail("请选择岗位!");
        }
        String userid = companyMetadata.userid().substring(0, 2);
        String name = "";

        if ("TJ".equals(userid)) {
            TJobhunter byId = tJobhunterService.getById(companyMetadata.userid());
            name = byId.getUserName();
            if (byId.getPerfect() == null || byId.getPerfect() == 0) {
                if (StringUtils.isEmpty(byId.getUserName())) {
                    collectPost(putInResumes);
                    return R.fail("请完善简历信息,该岗位已给您收藏!");
                }
                String advantage = tJobhunterResumeService.getAdvantage(companyMetadata.userid());
                if (StringUtils.isEmpty(advantage)) {
                    collectPost(putInResumes);
                    return R.fail("请完善个人优势,该岗位已给您收藏!");
                }
                String eduId = tJobhunterEducationRecordService.getEduId(companyMetadata.userid());
                if (StringUtils.isEmpty(eduId)) {
                    collectPost(putInResumes);
                    return R.fail("请完善个人学历,该岗位已给您收藏!");
                }
                String postionId = tJobhunterExpectPositionService.postionId(companyMetadata.userid());
                if (StringUtils.isEmpty(postionId)) {
                    collectPost(putInResumes);
                    return R.fail("请完善求职期望,该岗位已给您收藏!");
                }
            }


        }
        for (PutInResumeVO putInResumeVO : putInResumes) {
            //判断职位是否下线
            Boolean aBoolean1 = tCompanyPostApi.onLine(putInResumeVO.getPutInPost());
            if (!aBoolean1) {
                return R.fail("该职位已下线！");
            }
            EiCompanyPost byId = eiCompanyPostService.getById(putInResumeVO.getPutInPost());
            if (byId == null) {
                byId = eiCompanyPostService.add(putInResumeVO.getPutInPost());
                if (byId == null) {
                    return R.fail("该职位不存在!");
                }
            }

            //查询是否屏蔽了企业
            List<Object> objects = tJobhunterHideCompanyService.cacheCompany(putInResumeVO.getPutInJobhunter());
            boolean contains = objects.contains(putInResumeVO.getPutInComppany());
            if (contains) {
                return R.fail("您屏蔽了岗位" + byId.getPostLabel() + "的企业,请解除屏蔽再选择投递");
            }
            Boolean aBoolean3 = positionAppliedService.sendAResume(putInResumeVO.getPutInPost(), putInResumeVO.getTime(), putInResumeVO.getQrcode(), companyMetadata.userid());
            if ("TJ".equals(userid) && byId.getPostType() != 0) {
              /*  if(putInResumes.size()>0){

                }
                if(aBoolean){
                    return R.success("请委托推荐官投该岗位!");
                }*/
                return R.fail("请委托推荐官投该岗位!");
            }
            //   SetOperations<String, Object> putInResumeId = redisTemplate.opsForSet();
            //设置7天内不能重复投简
            ValueOperations<String, Object> setOperations = redisTemplate.opsForValue();
            SetOperations setOperations1 = redisTemplate.opsForSet();
            //有28天的查看权限
            //  setOperations.set(Sole.SELECT_TJ.getKey() + putInResumeVO.getPutInComppany() + putInResumeVO.getPutInJobhunter(), putInResumeVO.getPutInJobhunter());
            Boolean aBoolean = false;
            if ("TR".equals(userid)) { //推荐官投
                if (StringUtils.isEmpty(putInResumeVO.getExplains())) {
                    return R.fail("请填输入简说明");
                }
                String resume = Sole.DELIVER_TR.getKey() + putInResumeVO.getPutInPost() + companyMetadata.userid();
                aBoolean = !setOperations1.isMember(resume, putInResumeVO.getPutInJobhunter());
                if (aBoolean) {
                    setOperations1.add(resume, putInResumeVO.getPutInJobhunter());
                }
            } else if ("TJ".equals(userid)) {
                //设置过期时间
                String resume = Sole.DELIVER_TJ.getKey() + putInResumeVO.getPutInPost() + companyMetadata.userid();
                aBoolean = setOperations.setIfAbsent(resume, putInResumeVO.getPutInJobhunter(), 7, TimeUnit.DAYS);
            }
            try {
                if (!aBoolean) {       //求职者投
                    if (putInResumes.size() == 1) {
                        return R.fail("七日内不可重复投递 !");
                    }
                    continue;
                }
                PutInResume putInResume = new PutInResume();
                putInResume.setPutInResumeId(idGenerator.generateCode("PR"));
                BeanUtils.copyProperties(putInResumeVO, putInResume);
                putInResume.setCreateTime(LocalDateTime.now());
                putInResume.setPutInJobhunter(putInResumeVO.getPutInJobhunter());
                putInResume.setUpdateTime(LocalDateTime.now());
                putInResume.setPutInComppany(byId.getCompanyId());
                putInResume.setPutInUser(companyMetadata.userid());
                if ("TR".equals(userid)) {
                    putInResume.setEasco("1");
                } else if ("TJ".equals(userid)) {
                    putInResume.setEasco("0");
                }
                //如果有邀请码添加职位推广人
                if (!StringUtils.isEmpty(putInResumeVO.getQrcode())) {
                    Object o = redisUtils.get(InvitationCode.INVITATION_URL_CODE.getType() + putInResumeVO.getQrcode());
                    if (!StringUtils.isEmpty(o)) {
                        putInResume.setInvitation(o.toString());
                    }
                }
                putInResume.setBrowseIf(1);
                putInResume.setResumeState(1);
                putInResume.setDeletedPutIn(0);
                putInResume.setDeletedCompany(0);
                putInResume.setTCompanyMessage(1);
                boolean save = putInResumeService.save(putInResume);
                if (save) {
                    PutInResumeRecordVO putInResumeRecordVO = new PutInResumeRecordVO();
                    putInResumeRecordVO.setCause("");
                    putInResumeRecordVO.setDate(new Date());
                    putInResumeRecordVO.setState(0);
                    putInResumeRecordVO.setIdentity(HandleUtil.handle(companyMetadata.userid()));
                    putInResumeRecordVO.setName(name);
                    putInResumeRecordService.addRecord(putInResume.getPutInResumeId(), putInResumeRecordVO);

                    //短信通知
                    sendSms.acceptResume(byId.getCompanyId(), byId.getPostLabel());
                    // putInResumeId.add(Message.COMPANY_INTERVIEW.getKey() + putInResume.getPutInResumeId(), putInResume.getPutInResumeId());//给投递岗位的人一条消息
                    //this.sendMessage(putInResume.getPutInResumeId());
                    CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
                        putInResumeService.updateRedisPost(putInResumeVO.getPutInJobhunter(), putInResumeVO.getPutInPost(), 0, userid);
                        return null;
                    });

                    informAsync.sendAResume(putInResumeVO.getPutInPost(),putInResumeVO.getPutInJobhunter(),putInResume.getPutInResumeId());
                    String substring = byId.getCompanyId().substring(0, 2);
                    if (substring.equals("TR")) {
                        referrerRecordService.positionApplied(byId.getCompanyId(), companyMetadata.userid());
                    }
                } else {
                    return R.fail("投简失败！");
                }
            } catch (Exception e) {
                if ("TR".equals(userid)) { //推荐官投
                    String resume = Sole.DELIVER_TR.getKey() + putInResumeVO.getPutInPost() + companyMetadata.userid();
                    setOperations1.remove(resume, putInResumeVO.getPutInJobhunter());
                } else if ("TJ".equals(userid)) {
                    //设置过期时间
                    String resume = Sole.DELIVER_TJ.getKey() + putInResumeVO.getPutInPost() + companyMetadata.userid();
                    redisTemplate.delete(resume);
                }
            }
        }
        return R.success("投简成功！");

    }

    @PostMapping("/recommendationReport")
    @ApiOperation("推荐官-投简说明")
    @Log(title = "推荐官-投简说明", businessType = BusinessType.INSERT)
    @Transactional(rollbackFor = Exception.class)
    @AutoIdempotent
    public R recommendationReport(@Valid @RequestBody @Validated ValidList<RecommendationReportVO> recommendationReportVOValidList) {
        String userid = companyMetadata.userid().substring(0, 2);
        TRecommend byId = tRecommendService.getById(companyMetadata.userid());
        if (byId == null) {
            return R.fail("该推荐官不存在！");
        }

        for (RecommendationReportVO recommendationReportVO : recommendationReportVOValidList) {

            /*TCompanyPostPO tCompanyPostPO = tCompanyPostApi.selectTCompanyPostPO(recommendationReportVO.getPutInPost());*/
            EiCompanyPost eiCompanyPost = eiCompanyPostService.getById(recommendationReportVO.getPutInPost());
            if (eiCompanyPost == null) {
                eiCompanyPost = eiCompanyPostService.add(recommendationReportVO.getPutInPost());
                if (eiCompanyPost == null) {
                    return R.fail("该职位不存在!");
                }
            }

            //解决简历付不用填写推荐报告
            if (eiCompanyPost.getPostType() <= 3) {
                Map aNull = isNull(recommendationReportVO);
                Boolean isOk = (Boolean) aNull.get("isOk");
                if (!isOk) {
                    return R.fail(aNull.get("msg").toString());
                }
            }

            //判断职位是否下线
            Boolean aBoolean1 = tCompanyPostApi.onLine(recommendationReportVO.getPutInPost());
            if (!aBoolean1) {
                return R.fail("该职位已下线！");
            }

            List<Object> objects = tJobhunterHideCompanyService.cacheCompany(recommendationReportVO.getPutInJobhunter());
            boolean contains = objects.contains(recommendationReportVO.getPutInComppany());
            if (contains) {
                return R.fail("求职者屏蔽了岗位" + eiCompanyPost.getPostLabel() + "的企业,请解除屏蔽再选择投递");
            }

            if ("TJ".equals(userid) && eiCompanyPost.getPostType() != 0) {
                return R.fail("请注册为推荐官投该岗位!");
            }
            /*推荐官不能投普通岗位*/
            if ("TR".equals(userid) && eiCompanyPost.getPostType() == 0) {
                return R.fail("您的身份暂未开通投普通岗位的权限!");
            }

            R sendAResume = tJobhunterService.isSendAResume(recommendationReportVO.getPutInJobhunter());
            Map data = (Map) sendAResume.getData();
            Boolean isOk = (Boolean) data.get("isOK");
            if (!isOk) {
                return sendAResume;
            }
            Object education = data.get("education");
            if (!StringUtils.isEmpty(education)) {
                int i = Integer.parseInt(education.toString());
                if (eiCompanyPost.getPostType() == 4) {
                    if (i <= 3) {
                        return R.fail("该职位为简历付职位,请选择大专以上的学历简历!");
                    }
                }
            }
            Object userType = data.get("userType");
            if (eiCompanyPost.getPostType() == 3) {
                int i = Integer.parseInt(userType.toString());
                if (i == 2) {
                    return R.fail("校园人才不能投递到面付职位!");
                }
            }

            //查看求职者与推荐官的关系
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("t_job_hunter_id", recommendationReportVO.getPutInJobhunter());
            queryWrapper.eq("recommend_id", companyMetadata.userid());
            queryWrapper.eq("deleted", 0);
            ReferrerRecord one = referrerRecordService.getOne(queryWrapper);
            if (one == null) {
                return R.fail("该求职者暂未绑定!");
            }
    /*    if (one.getCorrelationType() == 3) {
            return R.fail("应聘简历不能投简！");
        }*/
            SetOperations setOperations1 = redisTemplate.opsForSet();

            Boolean aBoolean = false;
            if ("TR".equals(userid)) { //推荐官投  求职者只能投一次这种岗位
                String resume = Sole.SEND_A_RESUME.getKey() + recommendationReportVO.getPutInPost();
                aBoolean = !setOperations1.isMember(resume, recommendationReportVO.getPutInJobhunter());
                if (aBoolean) {
                    setOperations1.add(resume, recommendationReportVO.getPutInJobhunter());
                }
            }
            //赋予查看权限
            ValueOperations<String, Object> setOperations = redisTemplate.opsForValue();
            setOperations.set(Sole.SELECT_TJ.getKey() + recommendationReportVO.getPutInComppany() + recommendationReportVO.getPutInJobhunter(), recommendationReportVO.getPutInJobhunter());
            try {
                if (!aBoolean) {
                    return R.fail("该求职者已投放此岗位简历!");
                }
                PutInResume putInResume = new PutInResume();
                putInResume.setPutInResumeId(idGenerator.generateCode("PR"));
                BeanUtils.copyProperties(recommendationReportVO, putInResume);
                putInResume.setCreateTime(LocalDateTime.now());
                putInResume.setUpdateTime(LocalDateTime.now());
                putInResume.setPutInUser(companyMetadata.userid());
                if ("TR".equals(userid)) {
                    putInResume.setEasco("1");

                } else if ("TJ".equals(userid)) {
                    putInResume.setEasco("0");
                }

                putInResume.setBrowseIf(1);
                putInResume.setResumeState(1);
                putInResume.setDeletedPutIn(0);
                putInResume.setDeletedCompany(0);
                putInResume.setTCompanyMessage(1);
                boolean save = putInResumeService.save(putInResume);
                if (save) {

                    //存储更换求职者的推荐报告
                    referrerRecordService.updateReport(recommendationReportVO, companyMetadata.userid());
                    PutInResumeRecordVO putInResumeRecordVO = new PutInResumeRecordVO();
                    putInResumeRecordVO.setCause("");
                    putInResumeRecordVO.setDate(new Date());
                    putInResumeRecordVO.setState(0);
                    putInResumeRecordVO.setIdentity(HandleUtil.handle(companyMetadata.userid()));
                    putInResumeRecordVO.setName(byId.getUsername());
                    putInResumeRecordService.addRecord(putInResume.getPutInResumeId(), putInResumeRecordVO);
                    CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
                        putInResumeService.updateRedisPost(putInResume.getPutInJobhunter(), recommendationReportVO.getPutInPost(), 0, userid);
                        return null;
                    });
                    sendSms.acceptResume(eiCompanyPost.getCompanyId(), eiCompanyPost.getPostLabel());
                    runningWaterAsync.recommendationReport(companyMetadata.userid(), recommendationReportVO.getPutInComppany(), recommendationReportVO.getPutInJobhunter());
                    informAsync.sendAResume(recommendationReportVO.getPutInPost(),recommendationReportVO.getPutInJobhunter(),putInResume.getPutInResumeId());
                    if(one.getCorrelationType()!=2){
                        informAsync.beRecommended(companyMetadata.userid(), recommendationReportVO.getPutInPost(),recommendationReportVO.getPutInJobhunter());
                    }


                } else {
                    return R.fail("投简失败！");
                }
            } catch (Exception e) {
                if ("TR".equals(userid)) { //推荐官投
                    String resume = Sole.SEND_A_RESUME.getKey() + recommendationReportVO.getPutInPost();
                    setOperations1.remove(resume, recommendationReportVO.getPutInJobhunter());
                }
            }

        }
        return R.success("投简成功！");
    }


    @GetMapping("/resumePayment")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "postIds", required = true, value = "投简id"),
            @ApiImplicitParam(name = "jobHunters", required = true, value = "求职者id")
    })
    @Log(title = "简历付投简", businessType = BusinessType.SELECT)
    @ApiOperation("简历付投简")
    @GlobalTransactional(rollbackFor = Exception.class)
    public R resumePayment(@RequestParam("postIds") Set<String> postIds, @RequestParam("jobHunters") Set<String> jobHunters) {

        String userid = companyMetadata.userid().substring(0, 2);
        if (!"TR".equals(userid)) {
            return R.fail("请使用运营官账号!");
        }
        if (postIds == null || postIds.size() == 0) {
            return R.fail("请选择职位!");
        }
        if (jobHunters == null || jobHunters.size() == 0) {
            return R.fail("请选择求职者!");
        }
        if(postIds.size()>1 && jobHunters.size()>1){
            return R.fail("请正确选着对应关系！");
        }
        if(postIds.size()>10){
            return R.fail("请选择10个以下岗位数量！");
        }
        if(jobHunters.size()>10){
            return R.fail("请选择10个以下简历数量！");
        }
        SetOperations setOperations1 = redisTemplate.opsForSet();

        for (String postId : postIds) {
            String resume = Sole.SEND_A_RESUME.getKey() + postId;
            //判断职位是否下线
            TCompanyPostPO tCompanyPostPO = tCompanyPostApi.selectTCompanyPostPO(postId);
            if(tCompanyPostPO ==null){
            return  R.fail("请联系管理员！");
            }
            if (tCompanyPostPO.getStatus() == 0) {
                return R.fail(tCompanyPostPO.getPostLabel() + "该职位已下线！");
            }
            if ("TR".equals(userid) && tCompanyPostPO.getPostType() == 0) {
                return R.fail("您的身份暂未开通投普通岗位的权限!");
            }
            if(tCompanyPostPO.getPostType() !=4 && tCompanyPostPO.getPostType() !=5 ){
                return R.fail(tCompanyPostPO.getPostLabel()+"不是简历付职位，请选择简历付职位！");
            }
            for (String jobHunter : jobHunters) {


                String byId = tJobhunterService.byId(jobHunter);
                if (byId == null) {
                    return R.fail("操作错误！");
                }
                TJobhunter tJobhunter = JSON.parseObject(byId, TJobhunter.class);

                if (setOperations1.isMember(resume, jobHunter)) {
                    return R.fail(tJobhunter.getUserName()+"已被推荐过"+tCompanyPostPO.getPostLabel()+"职位！");
                }


                if (tJobhunter.getPerfect() == 0) {
                    return R.fail("请完善" + tJobhunter.getUserName() + "求职者简历!");
                }

                List<Object> objects = tJobhunterHideCompanyService.cacheCompany(jobHunter);
                boolean contains = objects.contains(tCompanyPostPO.getCompanyId());
                if (contains) {
                    return R.fail(tJobhunter.getUserName() + "求职者屏蔽了岗位" + tCompanyPostPO.getPostLabel() + "的企业,请解除屏蔽再选择投递");
                }
                String education = tJobhunter.getEducation();
                if (!StringUtils.isEmpty(education)) {
                    int i = Integer.parseInt(education.toString());
                    if (tCompanyPostPO.getPostType() == 4) {
                        if (i <= 3) {
                            return R.fail("该职位为简历付职位,请选择大专以上的学历简历!");
                        }
                    }
                }
                String userType = tJobhunter.getUserType();
                if(tCompanyPostPO.getPostType()==3){
                    int i = Integer.parseInt(userType.toString());
                    if(i==2){
                        return R.fail("校园人才不能投递到面付职位!");
                    }
                }
            }

        }

        //查看求职者与推荐官的关系
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.in("t_job_hunter_id", jobHunters);
        queryWrapper.eq("recommend_id", companyMetadata.userid());
        queryWrapper.eq("deleted", 0);
        int count = referrerRecordService.count(queryWrapper);
        if (count == 0) {
            return R.fail("您所选的求职者中有存在未绑定人才！");
        }

        TRecommend byId = tRecommendService.getById(companyMetadata.userid());
        if (byId == null) {
            return R.fail("该推荐官不存在！");
        }


        try {
            List<PutInResume> putInResumeList=new ArrayList<>();//投简
           Map<String,PutInResumeRecordVO> putInResumeRecordVOMap=new HashMap<>();//投简记录
           Map<String,String> postLabel=new HashMap<>();//短信通知
            List<String> list=new ArrayList<>();
            for (String postId : postIds) {

                //判断职位是否下线
                TCompanyPostPO tCompanyPostPO = tCompanyPostApi.selectTCompanyPostPO(postId);
                for (String jobHunter : jobHunters) {
                    PutInResume putInResume = new PutInResume();
                    putInResume.setPutInResumeId(idGenerator.generateCode("PR"));
                    putInResume.setCreateTime(LocalDateTime.now());
                    putInResume.setPutInComppany(tCompanyPostPO.getCompanyId());
                    putInResume.setUpdateTime(LocalDateTime.now());
                    putInResume.setPutInUser(companyMetadata.userid());
                    putInResume.setPutInJobhunter(jobHunter);
                    putInResume.setPutInPost(postId);
                    putInResume.setEasco("1");
                    putInResume.setBrowseIf(1);
                    putInResume.setResumeState(1);
                    putInResume.setDeletedPutIn(0);
                    putInResume.setDeletedCompany(0);
                    putInResume.setTCompanyMessage(1);
                    putInResumeList.add(putInResume);
                    PutInResumeRecordVO putInResumeRecordVO = new PutInResumeRecordVO();
                    putInResumeRecordVO.setCause("");
                    putInResumeRecordVO.setDate(new Date());
                    putInResumeRecordVO.setState(0);
                    putInResumeRecordVO.setIdentity(HandleUtil.handle(companyMetadata.userid()));
                    putInResumeRecordVO.setName(byId.getUsername());
                    putInResumeRecordVOMap.put(putInResume.getPutInResumeId(),putInResumeRecordVO);
                    postLabel.put(tCompanyPostPO.getCompanyId(),tCompanyPostPO.getPostLabel());
                    list.add(putInResume.getPutInResumeId());
                }
            }
            boolean save = putInResumeService.saveBatch(putInResumeList);
            if (save) {
                Set<String> strings = putInResumeRecordVOMap.keySet();
                Iterator<String> iterator = strings.iterator();
               while ( iterator.hasNext()){
                   String next = iterator.next();
                   PutInResumeRecordVO putInResumeRecordVO = putInResumeRecordVOMap.get(next);
                   putInResumeRecordService.addRecord(next, putInResumeRecordVO);
               }
                for (PutInResume putInResume : putInResumeList) {
                    CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
                        putInResumeService.updateRedisPost(putInResume.getPutInJobhunter(), putInResume.getPutInPost(), 0, userid);
                        return null;
                    });
                }
                Set<String> strings1 = postLabel.keySet();
                Iterator<String> iterator1 = strings1.iterator();
                while (iterator1.hasNext()){
                    String next = iterator1.next();
                    sendSms.acceptResume(next, postLabel.get(next));
                }
                for (String postId : postIds) {
                    String resume = Sole.SEND_A_RESUME.getKey() + postId;
                    for (String jobHunter : jobHunters) {
                        setOperations1.add(resume, jobHunter);
                    }

                }

              /*  //赋予查看权限
                ValueOperations<String, Object> setOperations = redisTemplate.opsForValue();
                setOperations.set(Sole.SELECT_TJ.getKey() + recommendationReportVO.getPutInComppany() + recommendationReportVO.getPutInJobhunter(), recommendationReportVO.getPutInJobhunter());*/

                    informAsync.sendAResumes(list);

            } else {
                return R.fail("投简失败！");
            }
        } catch (Exception e) {
            if ("TR".equals(userid)) { //推荐官投
                for (String postId : postIds) {
                    String resume = Sole.SEND_A_RESUME.getKey() + postId;
                    for (String jobHunter : jobHunters) {
                        setOperations1.remove(resume, jobHunter);
                    }
                }
            }
            throw  e;
        }

        return R.success("投简成功！");


    }


    @GetMapping("/deletePuInResume")
    @ApiOperation("删除投简记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "putInResumeId", required = true, value = "投简id"),
            @ApiImplicitParam(name = "identity", required = true, value = "身份(1:企业2:投放人)")
    })
    @Log(title = "删除投简记录", businessType = BusinessType.DELETE)
    @AutoIdempotent
    public R deletePuInResume(@RequestParam("putInResumeId") String putInResumeId, @RequestParam("identity") Integer identity) {
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("put_in_resume_id", putInResumeId);
        if (identity == 1) {
            updateWrapper.eq("put_in_comppany", companyMetadata.userid());
            updateWrapper.set("deleted_company", 1);
        } else if (identity == 2) {
            updateWrapper.eq("put_in_user", companyMetadata.userid());
            updateWrapper.set("deleted_put_in", 1);
        }

        boolean update = putInResumeService.update(updateWrapper);
        if (update) {
            return R.success("删除成功！");
        }
        return R.fail("删除失败！");
    }


    /**
     * @param phone:电话号码
     * @return null
     * @Description:淘汰提醒
     * @Author tangzhuo
     * @CreateTime 2022/12/23 14:05
     */
    @Async
    public void weedOutRemind(String phone) {
        String s1 = sendSms.inform(phone, AccessTemplateCode.WEED_OUT.getName(), AccessTemplateCode.WEED_OUT.getDesc());
    }

    /**
     * @return null
     * @Description: 下载简历接口
     * @Author tangzhuo
     * @CreateTime 2022/10/17 10:50
     */
/*    @GetMapping("/downloadResume/{companyId}/{jobhunterId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyId", required = true, value = "企业id"),
            @ApiImplicitParam(name = "jobhunterId", required = true, value = "求职者id")
    })
    @ApiOperation("/查看联系方式")
    public R downloadResume(@PathVariable("companyId") String companyId, @PathVariable("jobhunterId") String jobhunterId) {
        R<Boolean> booleanR = companyUserRoleApi.selectExamineRole(companyId, jobhunterId);
        Boolean data = booleanR.getData();
        if (data) {
            //获取求职者的简历下载地址
            TJobhunter byId = tJobhunterService.getById(jobhunterId);
            String resumePath = byId.getResumePath();
            return R.success("下载文件地址", resumePath);
        }
        return R.success("无权限下载", "");
    }*/
    @PostMapping("/selectResume")
    @ApiOperation("查询在7天内的用户投简更新的接口")
    @Log(title = "查询在7天内的用户投简更新的接口", businessType = BusinessType.SELECT)
    public R<PutInResumeInterviewPOJO> selectResume(@RequestBody PutInResumeQuery putInResumeQuery) {
        SetOperations<String, Object> setOperations = redisTemplate.opsForSet();
        String userid = companyMetadata.userid();
        String substring = userid.substring(0, 2);
        if ("TC".equals(substring)) {
            putInResumeQuery.setPutInComppany(userid);
            putInResumeQuery.setIdentity(1);
        } else if ("TJ".equals(substring)) {
            putInResumeQuery.setPutInUser(userid);
            putInResumeQuery.setIdentity(2);
        } else if ("TR".equals(substring)) {
            putInResumeQuery.setPutInUser(userid);
            putInResumeQuery.setIdentity(2);
        }
        Boolean information = false;

        Page<PutInResumeInterviewPOJO> list = putInResumeService.pagePutInResumeInterview(putInResumeQuery);
        List<PutInResumeInterviewPOJO> records = list.getRecords();

        for (PutInResumeInterviewPOJO record : records) {
            //身份(1:企业2:投放人)
            if (putInResumeQuery.getIdentity() == 1) {
                information = setOperations.isMember(Sole.MESSAGE.getKey() + userid, userid + record.getPutInResumeId());
            } else if (putInResumeQuery.getIdentity() == 2) {
                information = setOperations.isMember(Sole.MESSAGE.getKey() + userid, userid + record.getPutInResumeId());
            }
            TCompanyPostPO tCompanyPostPO = tCompanyPostApi.selectTCompanyPostPO(record.getPutInPost());
            if (tCompanyPostPO != null) {
                record.setTCompanyPostPO(tCompanyPostPO);
            }
            record.setInformation(information);
        }
        return R.success(list);
    }


    /**
     * @param companyId    ：企业id
     * @param userId:求职者id
     * @return null
     * @Description: 更改企业下载状态
     * @Author tangzhuo
     * @CreateTime 2022/10/28 9:01
     */
    @GetMapping("/updateDownloadResume")
    public void updateDownloadResume(@RequestParam("companyId") String companyId, @RequestParam("userId") String userId, @RequestParam("putInResumeId") String putInResumeId) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        PutInResume byId = putInResumeService.getById(putInResumeId);
        UpdateWrapper updateWrapper = new UpdateWrapper();
        String post = valueOperations.get(Sole.PUT.getKey() + companyId + userId);
        updateWrapper.eq("put_in_comppany", companyId);
        updateWrapper.eq("put_in_jobhunter", userId);
        updateWrapper.eq("put_in_post", post);
        updateWrapper.eq("put_in_resume_id", putInResumeId);
        updateWrapper.set("download_if", 2);
        if (byId.getResumeState() < 2) {
            updateWrapper.set("resume_state", 2);
        }
        updateWrapper.set("download_time", LocalDateTime.now());
        boolean save = putInResumeService.update(updateWrapper);
        if (save) {
            log.info("========>更新成功");
        }
        log.warn("============>更新失败");
    }


    @GetMapping("/getMessage")
    @ApiOperation("获取当前用户的岗位发布及投简消息数量")
    @Log(title = "获取当前用户的岗位发布及投简消息数量", businessType = BusinessType.SELECT)
    public R getMessage() {
        String userid = companyMetadata.userid();
        String substring = userid.substring(0, 2);
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        /* List<String> putInResumeId = null;*/
        Long size = setOperations.size(Sole.MESSAGE.getKey() + userid);
  /*      if ("TC".equals(substring)) {
            putInResumeId = putInResumeService.selectPutInResumeIdTC(userid);
            for (String s : putInResumeId) {
                size += setOperations.size(Message.COMPANY_INTERVIEW.getKey() + s);
            }

        } else {
            putInResumeId = putInResumeService.selectPutInResumeId(userid);

            for (String s : putInResumeId) {
                size += setOperations.size(Message.COMPANY_INTERVIEW.getKey() + putInResumeId);
            }
        }*/
        return R.success(size);
    }


  /*  @GetMapping("/updateMessage")
    @ApiOperation("当浏览时删除该投简消息")
    @ApiImplicitParam(name = "putInResumeId", required = true, value = "投放简历id")
    public R updateMessage(@RequestParam("putInResumeId") String putInResumeId) {
        if (StringUtils.isEmpty(putInResumeId)) {
            return R.fail("投放简历id不能为空！");
        }
        String userid = companyMetadata.userid();
        String substring = userid.substring(0, 2);
        SetOperations<String, Object> setOperations = redisTemplate.opsForSet();
        Long size = 0L;
        if ("TC".equals(substring)) {
            size = setOperations.remove(Message.COMPANY_INTERVIEW.getKey() + putInResumeId, putInResumeId);
        } else {
            size = setOperations.remove(Message.COMPANY_INTERVIEW.getKey() + putInResumeId, putInResumeId);
        }
        return R.success(size);
    }*/




/*    @PostMapping("/jobHunterInviteEntry")
    @ApiOperation("求职者投简发送offer表")
    public R JobHunterInviteEntryVO(@Valid @RequestBody InviteEntryVO vo) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        //确保推荐官只能投一次
        String key = Sole.INSERT_OFFER.getKey() + vo.getPutInResumeId() + vo.getPutInUser() + vo.getJobHunterId();
        Boolean member = false;
        //如果是求职者则下次投重复岗位也能发送offer
        String substring = vo.getPutInUser().substring(0, 2);
        if (substring.equals("TJ")) {
            member = ops.setIfAbsent(key, vo.getPutInUser(), 60 * 60 * 24 * 7, TimeUnit.SECONDS);
        } else {
            member = ops.setIfAbsent(key, vo.getPutInUser());
        }
        if (!member) {
            return R.fail("已经发送offer,请不要重复发送！");
        }
        InviteEntry inviteEntry = new InviteEntry();
        BeanUtils.copyProperties(vo, inviteEntry);
        inviteEntry.setInviteEntryId(idGenerator.generateCode("IE"));
        inviteEntry.setDeleted(0);
        inviteEntry.setCancel(1);
        inviteEntry.setStateIf(1);
        boolean save = inviteEntryService.save(inviteEntry);
        if (save) {
            //保证数据的唯一性
            putInResumeService.updateRedisPost(vo.getJobHunterId(), vo.getPostId(), 7, vo.getPutInUser());
            this.sendMessage(vo.getPutInResumeId());
            return R.success("发送成功!");
        }
        //没有成功则删除
        redisTemplate.delete(key);
        return R.fail("发送失败!");
    }*/


    @PostMapping("/putUpdateInvite")
    @ApiOperation("投简人是否同意取消入职申请")
    @Log(title = "投简人是否同意取消入职申请", businessType = BusinessType.SELECT)
    @AutoIdempotent
    public R putUpdateInvite(@Valid @RequestBody PutUpdateInviteEntryVO putUpdateInviteEntryVO) {
        if (putUpdateInviteEntryVO.getPutInConsent() == 1) {
            if (StringUtils.isEmpty(putUpdateInviteEntryVO.getConsentCause())) {
                return R.fail("请填入不同意原因");
            }
        }
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("invite_entry_id", putUpdateInviteEntryVO.getInviteEntryId());
        updateWrapper.set("put_in_consent", putUpdateInviteEntryVO.getPutInConsent());
        updateWrapper.set("consent_cause", putUpdateInviteEntryVO.getConsentCause());
        if (putUpdateInviteEntryVO.getPutInConsent() == 2) {
            updateWrapper.set("state_if", 2);
            //修改投简表
            UpdateWrapper updateWrapper1 = new UpdateWrapper();
            updateWrapper1.eq("put_in_resume_id", putUpdateInviteEntryVO.getPutInResumeId());
            updateWrapper1.set("entry_if", 1);
            String cancelCause = inviteEntryService.selectCancelCause(putUpdateInviteEntryVO.getInviteEntryId());
            updateWrapper1.set("entry_cause", cancelCause);
            boolean update = putInResumeService.update(updateWrapper1);
            if (!update) {
                log.error("投简人是否同意取消入职申请中" + putUpdateInviteEntryVO.getPutInResumeId() + "投简更新失败！");
            }
        }
        updateWrapper.set("update_consent_time", LocalDateTime.now());
        boolean update = inviteEntryService.update(updateWrapper);
        if (update) {
            //取消一下入职人数
            if (putUpdateInviteEntryVO.getPutInConsent() == 2) {
                PutInResume byId = putInResumeService.getById(putUpdateInviteEntryVO.getPutInResumeId());
                if (byId != null) {
                    CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
                        putInResumeService.cancelRedisPost(byId.getPutInJobhunter(), byId.getPutInPost(), 7, byId.getPutInUser());
                        return null;
                    });

                }

            }

            //发送消息
            //  this.sendMessage(putUpdateInviteEntryVO.getPutInResumeId());
            return R.success("已处理成功!");
        }
        return R.fail("已处理失败!");

    }


    @PostMapping("/updateEntry")
    @ApiOperation("企业修改入职申请")
    @Log(title = "企业修改入职申请", businessType = BusinessType.UPDATE)
    @AutoIdempotent
    public R updateEntry(@Valid @RequestBody UpdateEntryVO updateEntryVO) {

        UpdateEntry updateEntry = new UpdateEntry();
        BeanUtils.copyProperties(updateEntryVO, updateEntry);
        updateEntry.setUpdateEntryId(idGenerator.generateCode("UE"));
        updateEntry.setCreateTime(LocalDateTime.now());
        updateEntry.setCreateId(companyMetadata.userid());
        updateEntry.setDeleted(0);
        updateEntry.setPutAudit(0);
        updateEntry.setAdminAudit(0);
        boolean save = updateEntryService.save(updateEntry);
        if (save) {
            //  this.sendMessage(updateEntryVO.getPutInResumeId());
            return R.success("已发送申请,等待处理");
        }
        return R.fail("发送申请失败！联系管理员");
    }

    @PostMapping("/adminUpdateEntry")
    @ApiOperation("管理员审核修改入职时间")
    @Log(title = "管理员审核修改入职时间", businessType = BusinessType.UPDATE)
    @AutoIdempotent
    public R adminUpdateEntry(@Valid @RequestBody AdminUpdateEntryVO adminUpdateEntryVO) {

        UpdateWrapper updateWrapper = new UpdateWrapper();
        if (adminUpdateEntryVO.getAdminAudit() == 2) {
            if (StringUtils.isEmpty(adminUpdateEntryVO.getAdminAuditCause())) {
                return R.fail("请收入不同意原因");
            }
        }
        updateWrapper.eq("update_entry_id", adminUpdateEntryVO.getUpdateEntryId());
        updateWrapper.set("admin_audit", adminUpdateEntryVO.getAdminAudit());
        updateWrapper.set("admin_audit_cause", adminUpdateEntryVO.getAdminAuditCause());
        updateWrapper.set("admin_id", companyMetadata.userid());
        updateWrapper.set("admin_time", LocalDateTime.now());

        boolean update = updateEntryService.update(updateWrapper);
        if (update) {

            this.updateInviteEntryTime(adminUpdateEntryVO.getUpdateEntryId());
            //  this.sendMessage(adminUpdateEntryVO.getPutInResumeId());
            return R.success("处理成功!");
        }
        return R.fail("处理失败!");

    }

    @PostMapping("/putUpdateEntryVO")
    @ApiOperation("投简人审核修改入职时间")
    @Log(title = "投简人审核修改入职时间", businessType = BusinessType.UPDATE)
    @AutoIdempotent
    public R putUpdateEntryVO(@Valid @RequestBody PutUpdateEntryVO putUpdateEntryVO) {


        if (putUpdateEntryVO.getPutAudit() == 2) {
            if (StringUtils.isEmpty(putUpdateEntryVO.getPutAuditCause())) {
                return R.fail("请输入不同意原因！");
            }
        }
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("update_entry_id", putUpdateEntryVO.getUpdateEntryId());
        updateWrapper.set("put_id", companyMetadata.userid());
        updateWrapper.set("put_audit", putUpdateEntryVO.getPutAudit());
        updateWrapper.set("put_time", LocalDateTime.now());
        updateWrapper.set("put_audit_cause", putUpdateEntryVO.getPutAuditCause());
        boolean update = updateEntryService.update(updateWrapper);
        if (update) {
            this.updateInviteEntryTime(putUpdateEntryVO.getUpdateEntryId());
            //  this.sendMessage(putUpdateEntryVO.getPutInResumeId());
            return R.success("处理成功!");
        }
        return R.fail("处理失败!");
    }

    //修改入职时间
    public void updateInviteEntryTime(String updateEntryId) {
        UpdateEntry byId = updateEntryService.getById(updateEntryId);
        if (byId == null) {
            return;
        }
        if (byId.getPutAudit() == 1 && byId.getAdminAudit() == 1) {
            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("invite_entry_id", byId.getInviteEntryId());
            updateWrapper.set("entry_time", byId.getUpdateEntryTime());
            boolean update = inviteEntryService.update(updateWrapper);
            if (update) {
                log.info("单号为" + byId.getInviteEntryId() + "修改成功");
            }
            log.error("单号为" + byId.getInviteEntryId() + "修改失败");
        }
    }

    //发送消息
 /*   public void sendMessage(String putInResumeId) {
        String userid = companyMetadata.userid();
        String substring = userid.substring(0, 2);
        SetOperations<String, Object> setOperations = redisTemplate.opsForSet();
        setOperations.add(Sole.MESSAGE.getKey() + userid);
        if ("TR".equals(substring)|| "TJ".equals(substring)) {
            setOperations.add(Message.REFERRER_INTERVIEW.getKey()+userid + putInResumeId, putInResumeId);
        } else {
            setOperations.remove(Message.COMPANY_INTERVIEW.getKey()+userid + putInResumeId, putInResumeId);
        }
    }*/



   /* @PostMapping("/companyPutInResume")
    @ApiOperation("企业查看通提简历的状态")
    public R companyPutInResume(@RequestBody PutInResumeQuery putInResumeQuery){

    }
*/


    /*   @GetMapping("/cc/{postId}/{state}")
       @ApiOperation("测试")
       public R cc(@PathVariable("postId") String postId,@PathVariable("state") Integer state) {
           putInResumeService.updateRedisPost(postId,state);
           return R.success();
       }*/
    @GetMapping("/selectInviteEntry")
    public InviteEntryDTO selectInviteEntry(@RequestParam("resumeId") String resumeId) {
        InviteEntryDTO inviteEntryDTO = inviteEntryService.selectResumeId(resumeId);
        return inviteEntryDTO;
    }

   /* @GetMapping("/selectcc/{postId}")
    @ApiOperation("cse")
    public R selectcc(@PathVariable("postId") String postId) throws ExecutionException, InterruptedException {
        CompletableFuture<TPostShare> completableFuture = CompletableFuture.supplyAsync(() -> {
            return putInResumeService.selectPost(postId);
        });
       *//* System.out.println(completableFuture.get().toString());*//*

        return R.success();
    }*/

    @PostMapping("/selectAccountResume")
    @ApiOperation("查询在子企业用户投岗详细信息")
    @Log(title = "查询在子企业用户投岗详细信息", businessType = BusinessType.SELECT)
    public R<PutInResumeInterviewPOJO> selectAccountResume(@RequestBody PutInResumeQuery putInResumeQuery) {
        if (StringUtils.isEmpty(putInResumeQuery.getPutInComppany())) {
            return R.fail("投放的公司不能为空!");
        }
        putInResumeQuery.setIdentity(1);
        String userid = companyMetadata.userid();
        Boolean isSubsidiary = tCompanyService.subsidiary(userid, putInResumeQuery.getPutInComppany());
        if (!isSubsidiary) {
            return R.fail("该企业不是您的子公司!");
        }
        Boolean information = false;
        Page<PutInResumeInterviewPOJO> list = putInResumeService.selectAccountResume(putInResumeQuery);
        List<PutInResumeInterviewPOJO> records = list.getRecords();

        for (PutInResumeInterviewPOJO record : records) {
            TCompanyPostPO tCompanyPostPO = tCompanyPostApi.selectTCompanyPostPO(record.getPutInPost());
            if (tCompanyPostPO != null) {
                record.setTCompanyPostPO(tCompanyPostPO);
            }
            record.setInformation(information);
        }
        return R.success(list);
    }


    @GetMapping("/recommendedData")
    @ApiOperation("推荐官->我的推荐数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyId", required = false, value = "企业id"),
            @ApiImplicitParam(name = "postId", required = false, value = "岗位id")
    })
    @Log(title = "推荐官->我的推荐数据", businessType = BusinessType.SELECT)
    public R<RecommendedDataDTO> recommendedData(@RequestParam(value = "companyId", required = false) String companyId, @RequestParam(value = "postId", required = false) String postId) {
        String userid = companyMetadata.userid();
        RecommendedDataDTO recommendedData = putInResumeService.recommendedData(userid, companyId, postId);
        QueryWrapper queryWrapper = new QueryWrapper();

        recommendedData.setTalents(tJobhunterService.talentsCount(userid));
        return R.success(recommendedData);
    }

    /**
     * @param companyId:企业id
     * @return null
     * @Description: 获取公司发布6个月的发布的岗位数量
     * @Author tangzhuo
     * @CreateTime 2023/1/13 15:51
     */

    @GetMapping("/receiveResume")
    @ApiOperation("获取公司发布6个月招聘数据")
    @Log(title = "获取公司发布6个月招聘数据", businessType = BusinessType.SELECT)
    public R<RecommendedDataDTO> receiveResume(@RequestParam("companyId") String companyId) {
        RecommendedDataDTO recommendedData = putInResumeService.recommendedData(null, companyId, null);
        recommendedData.setResume(putInResumeService.receiveResume(companyId));
        recommendedData.setPostCount(tCompanyPostApi.postCount(companyId));
        return R.success(recommendedData);
    }


    @PostMapping("/sendAResumeInformation")
    @ApiOperation("子企业投简记录")
    @Log(title = "子企业投简记录", businessType = BusinessType.INSERT)
    public R<PutInResumeInterviewPOJO> sendAResumeInformation(@Valid @RequestBody SendAResumeInformationQuery sendAResumeInformationQuery) {
        String userid = companyMetadata.userid();
        Boolean isSubsidiary = tCompanyService.subsidiary(userid, sendAResumeInformationQuery.getCompany());
        if (!isSubsidiary) {
            return R.fail("该企业不是您的子公司!");
        }
        Page<PutInResumeInterviewPOJO> list = putInResumeService.sendAResumeInformation(sendAResumeInformationQuery);
        return R.success(list);
    }


    @PostMapping("/selectCompanyResume")
    @ApiOperation("企业查看投简消息")
    @Log(title = "企业查看投简消息", businessType = BusinessType.SELECT)
    public R<SelectCompanyResumeDTO> selectCompanyResume(@RequestBody SelectCompanyResumeQuery selectCompanyResumeQuery) {
        String userid = companyMetadata.userid();
        String substring = userid.substring(0, 2);
        if (!"TC".equals(substring)) {
            return R.fail("请使用企业账号");
        }
        if (selectCompanyResumeQuery.getBirthday() != null) {
            Map<String, Date> birthday = BirthdayUtils.getBirthday(selectCompanyResumeQuery.getBirthday());
            selectCompanyResumeQuery.setStartTime(birthday.get("startTime"));
            selectCompanyResumeQuery.setEndTime(birthday.get("endTime"));
        }
        IPage<SelectCompanyResumeDTO> list = putInResumeService.selectCompanyResume(selectCompanyResumeQuery, userid);
        List<SelectCompanyResumeDTO> records = list.getRecords();
        for (SelectCompanyResumeDTO selectCompanyResumeDTO : records) {
            selectCompanyResumeDTO.setIsCollect(collectJobHunterService.isCollect(userid, selectCompanyResumeDTO.getPutInJobhunter()));
            QueryWrapper queryWrapper1 = new QueryWrapper();
            queryWrapper1.eq("user_id", selectCompanyResumeDTO.getPutInJobhunter());
            queryWrapper1.eq("company_id", userid);
            int count1 = companyUserRoleService.count(queryWrapper1);

            if (count1 == 0) {
                if (selectCompanyResumeDTO.getUserName() != null) {
                    selectCompanyResumeDTO.setUserName(selectCompanyResumeDTO.getUserName().substring(0, 1) + "**");
                }
            } else {
                selectCompanyResumeDTO.setDownloadIf(2);
            }
        }
        return R.success(list);
    }


    @GetMapping("/browse")
    @ApiOperation("企业浏览")
    @ApiImplicitParam(name = "putInResumeId", required = true, value = "投简id")
    @Log(title = "企业浏览", businessType = BusinessType.SELECT)
    public R browse(@RequestParam("putInResumeId") String putInResumeId) {
        PutInResume byId = putInResumeService.getById(putInResumeId);
        if (byId == null) {
            return R.fail("未存在改投简!");
        }
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("put_in_resume_id", putInResumeId);
        updateWrapper.ne("browse_if", 2);
        updateWrapper.set("browse_if", 2);
        updateWrapper.set("browse_time", LocalDateTime.now());
        updateWrapper.set("t_company_message", 0);
        boolean update = putInResumeService.update(updateWrapper);
        if (update) {
            CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
                putInResumeService.updateRedisPost(byId.getPutInJobhunter(), byId.getPutInPost(), 1, byId.getPutInUser());
                return null;
            });
            PutInResumeRecordVO putInResumeRecordVO = new PutInResumeRecordVO();
            putInResumeRecordVO.setCause("");
            putInResumeRecordVO.setDate(new Date());
            putInResumeRecordVO.setState(1);
            putInResumeRecordVO.setIdentity(HandleUtil.handle(companyMetadata.userid()));
            putInResumeRecordVO.setName(putInResumeService.getCompanyName(putInResumeId));
            putInResumeRecordService.addRecord(putInResumeId, putInResumeRecordVO);
            if (byId.getPutInUser().substring(0, 2).equals("TR")) {
                referrerRecordAsync.browse(byId.getPutInUser(), byId.getPutInJobhunter());
            }
            return R.success();
        }
        return R.success();
    }

    @GetMapping("/inappropriate")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "putInResumeId", required = true, value = "投简id"),
            @ApiImplicitParam(name = "weedOutCause", required = true, value = "不合适原因")
    })
    @ApiOperation("不合适调用接口")
    @Log(title = "不合适调用接口", businessType = BusinessType.UPDATE)
    @AutoIdempotent
    public R inappropriate(@RequestParam("putInResumeId") String putInResumeId, @RequestParam("weedOutCause") String weedOutCause) {
        PutInResume byId = putInResumeService.getById(putInResumeId);
        if (byId == null) {
            return R.fail("未存在改投简!");
        }
        String putInComppany = byId.getPutInComppany().substring(0, 2);
        if (putInComppany.equals("TR")) {
            return R.fail("自营岗位不能才加面试!");
        }
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("put_in_resume_id", putInResumeId);
        if (StringUtils.isEmpty(weedOutCause)) {
            return R.fail("请填写不合适原因!");
        } else {
            CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
                putInResumeService.updateRedisPost(byId.getPutInJobhunter(), byId.getPutInPost(), 3, byId.getPutInUser());
                return null;
            });
        }
        updateWrapper.set("exclude_if", 2);
        updateWrapper.set("exclude_time", LocalDateTime.now());
        updateWrapper.set("exclude_state", weedOutCause);
        updateWrapper.set("resume_state", 3);
        updateWrapper.set("t_company_message", 0);
        updateWrapper.set("put_in_message", 1);
        boolean update = putInResumeService.update(updateWrapper);
        if (update) {
            PutInResumeRecordVO putInResumeRecordVO = new PutInResumeRecordVO();
            putInResumeRecordVO.setCause(weedOutCause);
            putInResumeRecordVO.setDate(new Date());
            putInResumeRecordVO.setState(3);
            putInResumeRecordVO.setIdentity(HandleUtil.handle(companyMetadata.userid()));
            putInResumeRecordVO.setName(putInResumeService.getCompanyName(putInResumeId));
            putInResumeRecordService.addRecord(putInResumeId, putInResumeRecordVO);
            String substring = byId.getPutInUser().substring(0, 2);
            if (substring.equals("TR")) {
                referrerRecordAsync.weedOut(byId.getPutInUser(), byId.getPutInJobhunter());
            }
            informAsync.dieOut(putInResumeId);
            return R.success();
        }
        return R.fail();
    }


    /*    @PostMapping("/updatePuInResume")
        @ApiOperation("更新投简记录")
        public R updatePuInResume(@Valid @RequestBody PutInResumeUpdateVO putInResumeUpdateVO) {
            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("put_in_resume_id", putInResumeUpdateVO.getPutInResumeId());
            if (null != putInResumeUpdateVO.getBrowseIf()) {
                updateWrapper.set("browse_if", putInResumeUpdateVO.getBrowseIf());
                updateWrapper.set("browse_time", LocalDateTime.now());
                updateWrapper.set("resume_state", 1);
                CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
                    putInResumeService.updateRedisPost(putInResumeUpdateVO.getJobHunter(), putInResumeUpdateVO.getPostId(), 1, putInResumeUpdateVO.getPutInUser());
                    return null;
                });
            }
           *//* if (null != putInResumeUpdateVO.getDownloadIf()) {
            updateWrapper.set("download_if", putInResumeUpdateVO.getDownloadIf());
            updateWrapper.set("download_time", LocalDateTime.now());
            updateWrapper.set("resume_state", 2);
            //生成订单
        }*//*
        if (null != putInResumeUpdateVO.getExcludeIf()) {
            if (2 == putInResumeUpdateVO.getExcludeIf() && StringUtils.isEmpty(putInResumeUpdateVO.getExcludeState())) {
                return R.fail("请填写排除原因!");
            } else {
                CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
                    putInResumeService.updateRedisPost(putInResumeUpdateVO.getJobHunter(), putInResumeUpdateVO.getPostId(), 3, putInResumeUpdateVO.getPutInUser());
                    return null;
                });

            }
            updateWrapper.set("exclude_if", putInResumeUpdateVO.getExcludeIf());
            updateWrapper.set("exclude_time", LocalDateTime.now());
            updateWrapper.set("exclude_state", putInResumeUpdateVO.getExcludeState());
            updateWrapper.set("resume_state", 3);
        }
        if (null != putInResumeUpdateVO.getInterviewIf()) {
            updateWrapper.set("resume_state", 5);
            updateWrapper.set("interview_if", putInResumeUpdateVO.getInterviewIf());
            updateWrapper.set("interview_time", LocalDateTime.now());
        }
        if (null != putInResumeUpdateVO.getWeedOutIf()) {
            //
            updateWrapper.set("interview_if", putInResumeUpdateVO.getInterviewIf());
            updateWrapper.set("interview_time", LocalDateTime.now());
            updateWrapper.set("resume_state", 6);
            updateWrapper.set("weed_out_if", putInResumeUpdateVO.getWeedOutIf());
            updateWrapper.set("weed_out_time", LocalDateTime.now());
            if (2 == putInResumeUpdateVO.getWeedOutIf() && StringUtils.isEmpty(putInResumeUpdateVO.getWeedOutCause())) {
                return R.fail("请填写淘汰原因!");
            } else {
                CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
                    putInResumeService.updateRedisPost(putInResumeUpdateVO.getJobHunter(), putInResumeUpdateVO.getPostId(), 6, putInResumeUpdateVO.getPutInUser());
                    return null;
                });

            }
            updateWrapper.set("weed_out_cause", putInResumeUpdateVO.getWeedOutCause());
        }
    *//*    if (null != putInResumeUpdateVO.getOfferIf()) {
            updateWrapper.set("resume_state", 7);
            updateWrapper.set("offer_if", putInResumeUpdateVO.getOfferIf());
            updateWrapper.set("offer_time", LocalDateTime.now());
        }*//*

        if (null != putInResumeUpdateVO.getOverProtectionIf()) {
            updateWrapper.set("resume_state", 9);
            updateWrapper.set("over_protection_if", putInResumeUpdateVO.getOverProtectionIf());
            updateWrapper.set("over_protection_time", LocalDateTime.now());
            // 创建订单
            if (1 == putInResumeUpdateVO.getOverProtectionIf() && StringUtils.isEmpty(putInResumeUpdateVO.getOverProtectionCause())) {
                return R.fail("请填写入未过保原因原因!");
            } else {
                CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
                    putInResumeService.updateRedisPost(putInResumeUpdateVO.getJobHunter(), putInResumeUpdateVO.getPostId(), 9, putInResumeUpdateVO.getPutInUser());
                    return null;
                });

            }
        }
        updateWrapper.set("update_time", LocalDateTime.now());
        boolean update = putInResumeService.update(updateWrapper);
        if (update) {
            //短信提醒淘汰
            if (putInResumeUpdateVO.getWeedOutIf() != null && putInResumeUpdateVO.getRemind() != null) {
                if (2 == putInResumeUpdateVO.getWeedOutIf() && 1 == putInResumeUpdateVO.getRemind()) {
                    String substring = putInResumeUpdateVO.getPutInUser().substring(0, 2);
                    String phone = null;
                    if (substring.equals("TJ")) {
                        phone = tJobhunterService.selectPhone(putInResumeUpdateVO.getPutInUser());
                    } else if (substring.equals("TR")) {
                        phone = tProvincesService.selectPhone(putInResumeUpdateVO.getPutInUser());
                    }
                    if (!StringUtils.isEmpty(phone)) {
                        this.weedOutRemind(phone);
                    }


                }
            }

            return R.success("更新成功！");
        }
        return R.fail("更新失败！");
    }*/
    @GetMapping("/getPutInResumeEiCompanyPostDTO")
    @ApiOperation("求职者获取我的申请")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "resumeState", required = true, value = "投放状态(0:全部 1:浏览、 2:下载、3:约面  4:offer 5:不合适)"),
            @ApiImplicitParam(name = "pageSize", required = true),
            @ApiImplicitParam(name = "pageNo", required = true)
    })
    @Log(title = "求职者获取我的申请", businessType = BusinessType.SELECT)
    public R<PutInResumeEiCompanyPostDTO> getPutInResumeEiCompanyPostDTO(@RequestParam("resumeState") Integer resumeState, @RequestParam("pageSize") Integer pageSize, @RequestParam("pageNo") Integer pageNo) {
        if (pageSize == null || pageSize > 10) {
            pageSize = 7;
        }
        if (pageNo == null) {
            pageNo = 0;
        }
        Page<PutInResumeEiCompanyPostDTO> list = putInResumeService.getPutInResumeEiCompanyPostDTO(resumeState, companyMetadata.userid(), new Page(pageNo, pageSize));
        List<PutInResumeEiCompanyPostDTO> records = list.getRecords();
        List<String> list1 = new ArrayList<String>(records.size());
        if (records.size() > 0) {
            for (PutInResumeEiCompanyPostDTO record : records) {
                if (StringUtils.isEmpty(record.getCompanyName())) {
                    list1.add(record.getPutInPost());
                }
            }
            //调用第三方接口查询企业名称
            List<Map<String, Object>> companyName = tCompanyPostApi.getCompanyName(list1);
            for (PutInResumeEiCompanyPostDTO record : records) {
                for (Map<String, Object> map : companyName) {
                    if (record.getPutInPost().equals(map.get("post_id"))) {
                        record.setCompanyName(map.get("company_name").toString());
                        String company_type = map.get("company_type").toString();
                        record.setCompanyType(company_type);
                    }
                }
            }


        }
        return R.success(list);
    }

    /*    @GetMapping("/weedOut")
        @ApiImplicitParams({
                @ApiImplicitParam(name = "putInResumeId", required = true, value = "投简id"),
                @ApiImplicitParam(name = "weedOutCause", required = false, value = "不合适原因")
        })
        public R weedOut(){
            PutInResume byId = putInResumeService.getById(putInResumeId);
            if(byId==null){
                return R.fail("未存在改投简!");
            }
            UpdateWrapper updateWrapper = new UpdateWrapper();
                updateWrapper.set("interview_if", putInResumeUpdateVO.getInterviewIf());
                updateWrapper.set("interview_time", LocalDateTime.now());
                updateWrapper.set("resume_state", 6);
                updateWrapper.set("weed_out_if", putInResumeUpdateVO.getWeedOutIf());
                updateWrapper.set("weed_out_time", LocalDateTime.now());
                if (2 == putInResumeUpdateVO.getWeedOutIf() && StringUtils.isEmpty(putInResumeUpdateVO.getWeedOutCause())) {
                    return R.fail("请填写淘汰原因!");
                } else {
                    CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
                        putInResumeService.updateRedisPost(putInResumeUpdateVO.getJobHunter(), putInResumeUpdateVO.getPostId(), 6, putInResumeUpdateVO.getPutInUser());
                        return null;
                    });

                }
                updateWrapper.set("weed_out_cause", putInResumeUpdateVO.getWeedOutCause());

        }*/
    @GetMapping("/getJobHunter")
    @ApiOperation("获取简历详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "jobHunter", required = true, value = "求职者id"),
            @ApiImplicitParam(name = "putInResumeId", required = true, value = "投简id")
    })
    @Log(title = "获取简历详情", businessType = BusinessType.SELECT)
    public R<CompanyTJobHunterResumeDTO> getJobHunter(@RequestParam("jobHunter") String jobHunter, @RequestParam("putInResumeId") String putInResumeId) {
        //查看该企业是否被屏蔽
        String userid = companyMetadata.userid();
        /*  TCompany byId = tCompanyService.getById(userid);*/
        // String companyName = byId.getCompanyName();
     /*   QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("resume_id", jobHunter);
        queryWrapper.eq("company_name", companyName);
        int count = tJobhunterHideCompanyService.count();
        if (count > 0) {
            return R.fail("该求职者对你屏蔽了简历");
        }
*/
        Boolean download = companyUserRoleService.isDownload(jobHunter, companyMetadata.userid());
        if (!download) {
            List<Object> objects = tJobhunterHideCompanyService.cacheCompany(jobHunter);
            boolean contains = objects.contains(companyMetadata.userid());
            if (contains) {
                return R.fail("该求职者屏蔽了简历！");
            }
        }

        CompanyTJobHunterResumeDTO companyTJobHunterResumeDTO = putInResumeService.getJobHunter(putInResumeId);
        companyTJobHunterResumeDTO.setDownload(false);
        if (companyTJobHunterResumeDTO != null) {
          /*  QueryWrapper queryWrapper1 = new QueryWrapper();
            queryWrapper1.eq("user_id", jobHunter);
            queryWrapper1.eq("company_id", userid);
            int count1 = companyUserRoleService.count(queryWrapper1);*/

            companyTJobHunterResumeDTO.setDownload(download);
            QueryWrapper queryWrapper2 = new QueryWrapper();
            queryWrapper2.eq("t_job_hunter_id", jobHunter);
            queryWrapper2.eq("t_company_id", userid);
            CollectJobHunter one = collectJobHunterService.getOne(queryWrapper2);
            companyTJobHunterResumeDTO.setCollect(one != null ? true : false);
            if (one != null) {
                companyTJobHunterResumeDTO.setCollectId(one.getCollectId());
            }

            if (!download) {
                //隐藏求职者姓名及电话号码
                companyTJobHunterResumeDTO.setPhone(PhoneUtils.hidePhoneByRegular(companyTJobHunterResumeDTO.getPhone()));
                companyTJobHunterResumeDTO.setUserName(companyTJobHunterResumeDTO.getUserName().substring(0, 1) + "**");
                companyTJobHunterResumeDTO.setEmail("*********");
                //获取求职者简历价格
                if (!StringUtils.isEmpty(companyTJobHunterResumeDTO.getPutInPost())) {
                    EiCompanyPost byId = eiCompanyPostService.getById(companyTJobHunterResumeDTO.getPutInPost());
                    if (!StringUtils.isEmpty(byId)) {
                        companyTJobHunterResumeDTO.setPostType(byId.getPostType());
                        if (byId.getPostType() >= 4) {
                            Map<String, BigDecimal> stringBigDecimalMap = divideIntoService.resumePayment(companyTJobHunterResumeDTO.getEducation(), companyTJobHunterResumeDTO.getCurrentSalary(), byId.getPostType());
                            // return R.success(stringBigDecimalMap);
                            if (stringBigDecimalMap != null) {
                                companyTJobHunterResumeDTO.setCurrencyCount(stringBigDecimalMap.get("currencyCount"));
                            }
                        } else {
                            BigDecimal currentSalary = companyTJobHunterResumeDTO.getCurrentSalary();
                            Map<String, Integer> map = divideIntoService.currencyCount(currentSalary);
                            if (map != null) {
                                companyTJobHunterResumeDTO.setCurrencyCount(map.get("currencyCount"));
                                companyTJobHunterResumeDTO.setGold(map.get("gold"));
                            }
                        }
                    }
                }

            }
        }
        companyTJobHunterResumeDTO.setPutInResumeId(putInResumeId);
        return R.success(companyTJobHunterResumeDTO);
    }


    @GetMapping("/downloadJurisdiction")
    @ApiOperation("企业下载简历是否有权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "jobHunter", required = true, value = "求职者id"),
            @ApiImplicitParam(name = "putInResumeId", required = true, value = "投简id")
    })
    @Log(title = "企业下载简历是否有权限", businessType = BusinessType.SELECT)
    public R downloadJurisdiction(@RequestParam("jobHunter") String jobHunter, @RequestParam("putInResumeId") String putInResumeId) {
        String userid = companyMetadata.userid();
        QueryWrapper queryWrapper1 = new QueryWrapper();
        queryWrapper1.eq("user_id", jobHunter);
        queryWrapper1.eq("company_id", userid);
        int count1 = companyUserRoleService.count(queryWrapper1);
        if (count1 > 0) {
            if (!StringUtils.isEmpty(putInResumeId)) {
                PutInResume byId = putInResumeService.getById(putInResumeId);
                if (byId != null) {
                    UpdateWrapper updateWrapper = new UpdateWrapper();
                    updateWrapper.eq("put_in_resume_id", putInResumeId);
                    if (byId.getResumeState() > 2) {
                        updateWrapper.set("resume_state", 2);
                    }
                    updateWrapper.set("download_if", 2);
                    updateWrapper.set("download_time", LocalDateTime.now());
                    putInResumeService.update(updateWrapper);
                }
            }
        }
        return R.success(count1 > 0 ? true : false);

    }

    @GetMapping("/postAmend")
    public Boolean postAmend(@RequestParam("postId") String postId, @RequestParam("type") Integer type) {
        QueryWrapper queryWrapper = new QueryWrapper();
        String[] resumeState = null;
        int count = 0;
        if (type != 3 && type != 0) {
            resumeState = new String[]{"1", "2", "4", "5", "8"};
            queryWrapper.in("resume_state", resumeState);
            queryWrapper.eq("put_in_post", postId);
            count = putInResumeService.count(queryWrapper);
        } else if (type == 3) {
            count = putInResumeService.interviewPaymentCount(postId);
        }
        if (count > 0) {
            return false;
        }
        return true;
    }

    @GetMapping("/entry/{putInResumeId}")
    @ApiOperation("确认入职")
    @ApiImplicitParam(name = "putInResumeId", required = true, value = "投简id")
    @Log(title = "确认入职", businessType = BusinessType.UPDATE)
    @GlobalTransactional(rollbackFor = Exception.class)
    @AutoIdempotent
    public R entry(@PathVariable("putInResumeId") String putInResumeId) throws Exception {
        PutInResume byId = putInResumeService.getById(putInResumeId);
        if (byId == null) {
            return R.fail("没有该投简记录!");
        }
        if (byId.getEntryIf() != 0) {
            if (byId.getEntryIf() == 1) {
                return R.fail("已确认未入职,请不要重复操作");
            } else if (byId.getEntryIf() == 2) {
                return R.fail("已确认入职,请不要重复操作");
            }
        }
        if (!byId.getPutInComppany().equals(companyMetadata.userid())) {
            return R.fail("非法操作!");
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("put_in_resume_id", putInResumeId);
        InviteEntry one = inviteEntryService.getOne(queryWrapper);
        if (one == null) {
            return R.fail("未查到该份offer!");
        }

        //保证只修改一次
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("put_in_resume_id", putInResumeId);
        updateWrapper.set("entry_time", LocalDateTime.of(one.getEntryDate(), one.getEntryTime()));
        updateWrapper.set("entry_if", 2);
        updateWrapper.set("resume_state", 9);
        updateWrapper.set("t_company_message", 0);
        //入职付则锁定订单
        if (one.getPostType() == 1) {
            updateWrapper.set("close_an_account_if", 2);
            updateWrapper.set("close_an_account_begin_time", LocalDateTime.now());
        }
        updateWrapper.set("put_in_message", 0);
        updateWrapper.set("dispose_entry_time", LocalDateTime.now());
        boolean update = putInResumeService.update(updateWrapper);
        if (update) {
            //当不为普通岗位才产生订单
            CompletableFuture<Object> completableFuture = CompletableFuture.supplyAsync(() -> {
                putInResumeService.updateRedisPost(byId.getPutInJobhunter(), byId.getPutInPost(), 8, byId.getPutInUser());
                return null;
            });
            //添加投简记录
            PutInResumeRecordVO putInResumeRecordVO = new PutInResumeRecordVO();
            putInResumeRecordVO.setCause("");
            putInResumeRecordVO.setDate(new Date());
            putInResumeRecordVO.setState(12);
            putInResumeRecordVO.setIdentity(HandleUtil.handle(companyMetadata.userid()));
            putInResumeRecordVO.setName(putInResumeService.getCompanyName(byId.getPutInResumeId()));
            putInResumeRecordService.addRecord(byId.getPutInResumeId(), putInResumeRecordVO);

            //产生订单
            if (one.getPostType() == 1 && byId.getCloseAnAccountIf() == 0) {
                TJobhunter byId1 = tJobhunterService.getById(byId.getPutInJobhunter());
                Map map = new ConcurrentHashMap();
                map.put("post_id", byId.getPutInPost());//岗位
                map.put("put_in_resume_id", byId.getPutInResumeId());//投简
                map.put("referrer_id", byId.getPutInUser());//推荐人id
                map.put("create_id", companyMetadata.userid());//创建人
                map.put("create_name", companyMetadata.userName());//创建人姓名
                map.put("payer", byId.getPutInComppany());//支付人
                map.put("post_type", one.getPostType());
                map.put("hired_bounty", one.getHiredBounty());
                map.put("recommend_time", byId.getCreateTime());
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime of = LocalDateTime.of(one.getEntryDate(), one.getEntryTime());
                map.put("entryTime", of.format(fmt));//入职时间

                map.put("avatar", byId1.getAvatar());
                map.put("jobHunter", byId1.getUserId());
                map.put("userName", byId1.getUserName());
                map.put("workTime", byId1.getWorkTime());
                map.put("education", byId1.getEducation());
                map.put("birthday", byId1.getBirthday());

          /*  map.put("percentage", one.getPercentage());
            map.put("inviteEntryId", one.getInviteEntryId());*/
                map.put("putInJobhunter", byId.getPutInJobhunter());
                map.put("entryDate", one.getEntryDate());//入职时间
      /*      //满月付岗位
            if (one.getPostType() == 1) {
                map.put("hired_bounty", one.getHiredBounty());
            }
            //入职付岗位
            if (one.getPostType() == 2) {
                map.put("money_reward", one.getMoneyReward());
            }*/
                postOrderProduce.sendOrderlyMessage(JSON.toJSONString(map));

            }
            //入职奖励
            runningWaterAsync.entry(companyMetadata.userid(), byId.getPutInJobhunter(), byId.getPutInUser());
            String substring = byId.getPutInUser().substring(0, 2);
            if (substring.equals("TR")) {
                //添加入职记录
                referrerRecordAsync.entry(byId.getPutInUser(), byId.getPutInJobhunter());
            }
            informAsync.entry(byId.getPutInResumeId());
            return R.success("操作成功!");
        }
        return R.fail("操作失败!");
    }

    @PostMapping("/updateInvitation")
    @ApiOperation("未入职")
    @Transactional(rollbackFor = Exception.class)
    @Log(title = "未入职", businessType = BusinessType.UPDATE)
    @AutoIdempotent
    public R updateInvitation(@Valid @RequestBody UpdateInviteEntryVO updateInviteEntryVO) {
        PutInResume byId = putInResumeService.getById(updateInviteEntryVO.getPutInResumeId());
        if (byId == null) {
            return R.fail("没有该投简记录!");
        }

        if (byId.getEntryIf() != 0) {
            if (byId.getEntryIf() == 1) {
                return R.fail("已确认未入职,请不要重复操作");
            } else if (byId.getEntryIf() == 2) {
                return R.fail("已确认入职,请不要重复操作");
            }

        }
        if (StringUtils.isEmpty(updateInviteEntryVO.getCancelCause())) {
            return R.fail("未入职原因不能为空");
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("put_in_resume_id", updateInviteEntryVO.getPutInResumeId());
        InviteEntry one = inviteEntryService.getOne(queryWrapper);

        //取消offer  避免入职超时的产生订单
        UpdateWrapper wrapper = new UpdateWrapper();
        wrapper.eq("put_in_resume_id", updateInviteEntryVO.getPutInResumeId());
        if (one.getPostType() == 0) {
            wrapper.set("cancel", 1);
            wrapper.set("state_if", 2);

        } else if (one.getPostType() == 1 || one.getPostType() == 2) {
            wrapper.set("cancel", 2);
        }
        wrapper.set("cancel_cause", updateInviteEntryVO.getCancelCause());
        wrapper.set("cancel_time", LocalDateTime.now());
        wrapper.set("put_in_consent", 0);
        boolean update = inviteEntryService.update(wrapper);
        if (update) {
            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("put_in_resume_id", updateInviteEntryVO.getPutInResumeId());
            updateWrapper.set("resume_state", 9);
            updateWrapper.set("entry_if", 1);
            updateWrapper.set("entry_cause", updateInviteEntryVO.getCancelCause());
            updateWrapper.set("update_time", LocalDateTime.now());
            updateWrapper.set("entry_cause", updateInviteEntryVO.getCancelCause());
            updateWrapper.set("t_company_message", 0);
            if (one.getPostType() == 1 || one.getPostType() == 2) {
                updateWrapper.set("not_entry", 1);
                updateWrapper.set("put_in_message", 1);
            } else {
                updateWrapper.set("put_in_message", 0);
            }
            updateWrapper.set("dispose_entry_time", LocalDateTime.now());
            boolean update1 = putInResumeService.update(updateWrapper);
            if (update1) {
                PutInResumeRecordVO putInResumeRecordVO = new PutInResumeRecordVO();
                putInResumeRecordVO.setCause(updateInviteEntryVO.getCancelCause());
                putInResumeRecordVO.setDate(new Date());
                putInResumeRecordVO.setState(13);
                putInResumeRecordVO.setIdentity(HandleUtil.handle(companyMetadata.userid()));
                putInResumeRecordVO.setName(putInResumeService.getCompanyName(byId.getPutInResumeId()));
                putInResumeRecordService.addRecord(byId.getPutInResumeId(), putInResumeRecordVO);
            }
            //发送消息
            //  this.sendMessage(updateInviteEntryVO.getPutInResumeId());
            informAsync.notEntry(byId.getPutInResumeId());
            return R.success("操作成功!");
        }

        return R.fail("发送申请失败");
    }
    /*    @GetMapping("")*/


    /**
     * 收藏岗位
     */
    public void collectPost(List<PutInResumeVO> putInResumes) {
        CollerctPostVO collerctPostVO = new CollerctPostVO();
        List<String> list = new ArrayList();
        putInResumes.forEach((i) -> list.add(i.getPutInPost()));
        collerctPostVO.setPost(list);
        collerctPostVO.setCollerctUserId(companyMetadata.userid());
        collerctPostVO.setIdentity(1);
        collerctPostController.save(collerctPostVO);
    }

    @GetMapping("/selectPutInResumeRecord")
    @ApiOperation("获取投简记录明细")
    @ApiImplicitParam(name = "putInResumeId", required = true, value = "投简id")
    @Log(title = "获取投简记录明细", businessType = BusinessType.SELECT)
    public R<PutInResumeRecord> selectPutInResumeRecord(@RequestParam("putInResumeId") String putInResumeId) {
        PutInResumeRecord putInResumeRecord = new PutInResumeRecord();
        List<Object> objects = redisUtils.lGet(Cache.CACHE_PUT_IN_RESUME_RECORD.getKey() + putInResumeId, 0, redisUtils.lGetListSize(Cache.CACHE_PUT_IN_RESUME_RECORD.getKey() + putInResumeId));

        if (objects.size() == 0) {
            putInResumeRecord = putInResumeRecordService.getById(putInResumeId);
            if (putInResumeId != null) {
                redisUtils.lSets(Cache.CACHE_PUT_IN_RESUME_RECORD.getKey() + putInResumeId, putInResumeRecord.getContent());
            }
        } else {
            putInResumeRecord.setPutInResumeId(putInResumeId);
            putInResumeRecord.setContent(objects);
        }
/*        List<Object> content = putInResumeRecord.getContent();
        for (Object o : content) {
            System.out.println(o.toString());
   *//*         PutInResumeRecordVO putInResumeRecordVO = JSON.parseObject(o.toString(), PutInResumeRecordVO.class);
            System.out.println(putInResumeRecordVO.toString());*//*

        }*/
        Collections.reverse(putInResumeRecord.getContent());
        return R.success(putInResumeRecord);
    }


    @GetMapping("/postDate")
    @ApiOperation("推荐官获取此岗位推荐数据")
    @Log(title = "推荐官获取此岗位推荐数据", businessType = BusinessType.SELECT)
    @ApiImplicitParam(name = "postId", required = true, value = "岗位id")
    public R postDate(@RequestParam("postId") String postId) {
        Map<String, Long> map = putInResumeService.postDate(postId, companyMetadata.userid());
        return R.success(map);
    }


    /*   @GetMapping("/companyDate")
       @ApiOperation("获取公司6个月招娉数据")
       @Log(title = "获取公司6个月招娉数据", businessType = BusinessType.SELECT)
       @ApiImplicitParam(name = "companyId", required = true, value = "企业id")
       public R companyDate(@RequestParam String companyId){
           Map<String, Long> map = putInResumeService.companyDate(companyId);
           return R.success(map);
       }
   */
    @PostMapping("/recommendAdministrator")
    @ApiOperation("推荐管理")
    @Log(title = "推荐管理", businessType = BusinessType.SELECT)
    public R<JobHunterPostPutInResumeDTO> recommendAdministrator(@RequestBody RecommendAdministratorQuery recommendAdministratorQuery) {
        if (StringUtils.isEmpty(recommendAdministratorQuery.getPageNo())) {
            recommendAdministratorQuery.setPageNo(1L);
        }
        IPage<JobHunterPostPutInResumeDTO> iPage = putInResumeService.recommendAdministrator(recommendAdministratorQuery, companyMetadata.userid());
        return R.success(iPage);
    }

    @ApiOperation("获取投简企业")
    @GetMapping("/putInResumeCompany")
    @Log(title = "获取投简企业", businessType = BusinessType.SELECT)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", required = false, value = "关键字"),
            @ApiImplicitParam(name = "pageSize", required = true, value = "条数")
    })
    public R<List<String>> putInResumeCompany(@RequestParam(value = "keyword", required = false) String keyword, @RequestParam("pageSize") Integer pageSize) {
        List<String> list = putInResumeService.putInResumeCompany(keyword, pageSize, companyMetadata.userid());
        return R.success(list);
    }

    @ApiOperation("获取投简岗位")
    @GetMapping("/putInResumePostLabel")
    @Log(title = "获取投简岗位", businessType = BusinessType.SELECT)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", required = false, value = "关键字"),
            @ApiImplicitParam(name = "pageSize", required = true, value = "条数")
    })
    public R<List<String>> putInResumePostLabel(@RequestParam(value = "keyword", required = false) String keyword, @RequestParam("pageSize") Integer pageSize) {
        List<String> list = putInResumeService.putInResumePostLabel(keyword, pageSize, companyMetadata.userid());
        return R.success(list);
    }


    @GetMapping("/affirmInterview")
    @ApiOperation("确认面试")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "putInResumeId", required = true, value = "投简id"),
            @ApiImplicitParam(name = "dateTime", required = true, value = "面试时间")
    })
    @Log(title = "确认面试", businessType = BusinessType.UPDATE)
    @AutoIdempotent
    @GlobalTransactional(rollbackFor = Exception.class)
    public R affirmInterview(@RequestParam("putInResumeId") String putInResumeId, @RequestParam("dateTime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime dateTime) {
        String userid = companyMetadata.userid();
        if (!"TR".equals(userid.substring(0, 2))) {
            return R.fail("操作身份有误！");
        }
        PutInResume byId = putInResumeService.getById(putInResumeId);
        EiCompanyPost byId1 = eiCompanyPostService.getById(byId.getPutInPost());
        if (byId1 == null) {
            byId1 = eiCompanyPostService.add(byId.getPutInPost());
            if (byId1.getPostType() != 3) {
                return R.fail("该岗位不需要确定到面!");
            }
        }
        if (byId == null) {
            return R.fail("推荐不存在!");
        }
        Integer closeAnAccountIf = byId.getCloseAnAccountIf();
        if (closeAnAccountIf == 2) {
            return R.fail("该职位已经产生订单!");
        }
        if (byId.getResumeState() < 5) {
            return R.fail("该职位未发送面试邀请!");
        }
        if (!byId.getPutInUser().equals(companyMetadata.userid())) {
            return R.fail("非法操作！");
        }
        byId.setAffirmInterviewTime(dateTime);
        byId.setCloseAnAccountIf(2);
        byId.setCloseAnAccountBeginTime(LocalDateTime.now());
        byId.setAffirmInterview(1);
        boolean b = putInResumeService.updateById(byId);
        if (b) {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("post_id", byId.getPutInPost());
            EiCompanyPost one = eiCompanyPostService.getOne(queryWrapper);
            if (one == null) {
                one = eiCompanyPostService.add(byId.getPutInPost());
            }
            TJobhunter byId2 = tJobhunterService.getById(byId.getPutInJobhunter());
            Map map = new ConcurrentHashMap();
            map.put("post_id", byId.getPutInPost());//岗位
            map.put("put_in_resume_id", byId.getPutInResumeId());//投简
            map.put("referrer_id", byId.getPutInUser());//推荐人id
            map.put("create_id", companyMetadata.userid());//创建人
            map.put("create_name", companyMetadata.userName());//创建人姓名
            map.put("payer", byId.getPutInComppany());//支付人
            map.put("recommend_time", byId.getCreateTime());
            map.put("post_type", one.getPostType());//岗位类型
            map.put("dateTime", dateTime);//面试时间
            map.put("hired_bounty", byId.getCurrentAnnualSalary());//待支付金额
            //  map.put("percentage", byId.getCancelCause());//百分比
            /*   map.put("inviteEntryId", one.getInviteEntryId());//入职id*/

            map.put("putInJobhunter", byId.getPutInJobhunter());
            map.put("entryDate", LocalDateTime.now());//待支付时间
            map.put("avatar", byId2.getAvatar());
            map.put("jobHunter", byId2.getUserId());
            map.put("userName", byId2.getUserName());
            if (byId2.getWorkTime() != null) {
                map.put("workTime", byId2.getWorkTime());
            }

            map.put("education", byId2.getEducation());
            map.put("birthday", byId2.getBirthday());
            postOrderProduce.sendOrderlyMessage(JSON.toJSONString(map));


            sendSms.orderNotice(byId.getPutInResumeId());
            informAsync.interviewPresence(putInResumeId,dateTime);
            return R.success("操作成功");
        }
        return R.fail("操作失败！");
    }

    @GetMapping("/putInResumeParticulars")
    @ApiImplicitParam(name = "putInResumeId", required = true, value = "推荐id")
    @Log(title = "推荐详情", businessType = BusinessType.SELECT)
    @ApiOperation("推荐详情")
    public R<PutInResumeParticularsDTO> putInResumeParticulars(@RequestParam("putInResumeId") String putInResumeId) {
        PutInResumeParticularsDTO putInResumeParticularsDTO = putInResumeService.putInResumeParticulars(putInResumeId);
        if (putInResumeParticularsDTO == null) {
            return R.fail("未查到该条数据！ ");
        }
        putInResumeParticularsDTO.setRmJobHunterDTO(tJobhunterService.selectJobHunter(putInResumeParticularsDTO.getPutInJobhunter(), companyMetadata.userid()));
        TPayConfig tPayConfig = (TPayConfig) redisUtils.get(RedisCache.TPAYCONFIG.getValue());
        Integer postType = putInResumeParticularsDTO.getPostType();

        //岗位发布类型(0普通岗位 1:入职付 2满月付 3到面付)
        if (postType == 1) {
            putInResumeParticularsDTO.setCommission(tPayConfig.getEntryPayment());
            if (putInResumeParticularsDTO.getResumeState() >= 8) {
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("put_in_resume_id", putInResumeParticularsDTO.getPutInResumeId());
                InviteEntry one = inviteEntryService.getOne(queryWrapper);
                if (one != null) {
                    BigDecimal multiply1 = one.getHiredBounty().multiply(new BigDecimal(10000)).divide(new BigDecimal(12), 3, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(putInResumeParticularsDTO.getCommission())).multiply(new BigDecimal(0.01)).multiply(new BigDecimal(tPayConfig.getInviteInteractionPostA()).multiply(new BigDecimal(0.01))).setScale(0, BigDecimal.ROUND_HALF_UP);
                    putInResumeParticularsDTO.setDivideInto(multiply1.toString());
                }
            } else {
                BigDecimal divide = putInResumeParticularsDTO.getBeginSalary().multiply(new BigDecimal(10000)).divide(new BigDecimal(12), 3, BigDecimal.ROUND_HALF_UP);
                BigDecimal multiply1 = divide.multiply(new BigDecimal(putInResumeParticularsDTO.getCommission()).multiply(new BigDecimal(0.01))).multiply(new BigDecimal(tPayConfig.getInviteInteractionPostA()).multiply(new BigDecimal(0.01))).setScale(0, BigDecimal.ROUND_HALF_UP);
                BigDecimal divide1 = putInResumeParticularsDTO.getEndSalary().multiply(new BigDecimal(10000)).divide(new BigDecimal(12), 3, BigDecimal.ROUND_HALF_UP);
                BigDecimal multiply2 = divide1.multiply(new BigDecimal(putInResumeParticularsDTO.getCommission()).multiply(new BigDecimal(0.01))).multiply(new BigDecimal(tPayConfig.getInviteInteractionPostA()).multiply(new BigDecimal(0.01))).setScale(0, BigDecimal.ROUND_HALF_UP);
                putInResumeParticularsDTO.setDivideInto(multiply1.toString() + "-" + multiply2.toString());
            }
        } else if (postType == 2) {
            putInResumeParticularsDTO.setCommission(tPayConfig.getFullMoonPayment());
            if (putInResumeParticularsDTO.getResumeState() >= 8) {
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("put_in_resume_id", putInResumeParticularsDTO.getPutInResumeId());
                InviteEntry one = inviteEntryService.getOne(queryWrapper);
                if (one != null) {
                    BigDecimal multiply1 = one.getHiredBounty().multiply(new BigDecimal(10000)).divide(new BigDecimal(12), 3, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(putInResumeParticularsDTO.getCommission()).multiply(new BigDecimal(0.01))).multiply(new BigDecimal(tPayConfig.getInviteInteractionPostA()).multiply(new BigDecimal(0.01))).setScale(0, BigDecimal.ROUND_HALF_UP);
                    putInResumeParticularsDTO.setDivideInto(multiply1.toString());
                }
            } else {
                BigDecimal divide = putInResumeParticularsDTO.getBeginSalary().multiply(new BigDecimal(10000)).divide(new BigDecimal(12), 3, BigDecimal.ROUND_HALF_UP);
                BigDecimal multiply1 = divide.multiply(new BigDecimal(putInResumeParticularsDTO.getCommission()).multiply(new BigDecimal(0.01))).multiply(new BigDecimal(tPayConfig.getInviteInteractionPostA()).multiply(new BigDecimal(0.01))).setScale(0, BigDecimal.ROUND_HALF_UP);
                BigDecimal divide1 = putInResumeParticularsDTO.getEndSalary().multiply(new BigDecimal(10000)).divide(new BigDecimal(12), 3, BigDecimal.ROUND_HALF_UP);
                BigDecimal multiply2 = divide1.multiply(new BigDecimal(putInResumeParticularsDTO.getCommission()).multiply(new BigDecimal(0.01))).multiply(new BigDecimal(tPayConfig.getInviteInteractionPostA()).multiply(new BigDecimal(0.01))).setScale(0, BigDecimal.ROUND_HALF_UP);
                putInResumeParticularsDTO.setDivideInto(multiply1.toString() + "-" + multiply2.toString());
            }

        } else if (postType == 3) {
            putInResumeParticularsDTO.setCommission(tPayConfig.getInterviewPayment());
            BigDecimal divide = putInResumeParticularsDTO.getCurrentAnnualSalary().multiply(new BigDecimal(10000)).divide(new BigDecimal(12), 3, BigDecimal.ROUND_HALF_UP);
            BigDecimal multiply1 = divide.multiply(new BigDecimal(putInResumeParticularsDTO.getCommission()).multiply(new BigDecimal(0.01))).multiply(new BigDecimal(tPayConfig.getInviteInteractionPostA()).multiply(new BigDecimal(0.01))).setScale(0, BigDecimal.ROUND_HALF_UP);
            //  BigDecimal multiply = multiply1.multiply(new BigDecimal(tPayConfig.getInviteInteractionPostA()).multiply(new BigDecimal(0.01)).multiply(new BigDecimal(tPayConfig.getInviteInteractionPostA()).multiply(new BigDecimal(0.01)))).setScale(0, BigDecimal.ROUND_DOWN);

            putInResumeParticularsDTO.setDivideInto(multiply1.toString());
        } else {
            putInResumeParticularsDTO.setCommission(0);
        }
        return R.success(putInResumeParticularsDTO);
    }


    @PostMapping("/historySubmitAResume/{jobHunterId}")
    @ApiImplicitParam(name = "jobHunterId", required = true, value = "求职者id")
    @ApiOperation("查询单个取求职者个人推荐数据")
    @Log(title = "查询单个取求职者个人推荐数据", businessType = BusinessType.SELECT)
    public R<HistorySubmitAResumeDTO> historySubmitAResume(@RequestBody PageQuery pageQuery, @PathVariable("jobHunterId") String jobHunterId) {
        IPage<HistorySubmitAResumeDTO> iPage = putInResumeService.historySubmitAResume(pageQuery, companyMetadata.userid(), jobHunterId);
        return R.success(iPage);
    }

    @GetMapping("/recommendReport/{id}")
    @ApiOperation("企业获取推荐人选的电话号码")
    @Log(title = "企业获取推荐人选的电话号码", businessType = BusinessType.SELECT)
    @ApiImplicitParam(name = "id", required = true, value = "投简人id")
    public R<Map> recommendReport(@PathVariable("id") String id) {
        TRecommend byId = tRecommendService.getById(id);
        if (StringUtils.isEmpty(byId)) {
            return null;
        }
        Map map = new HashMap(2);
        map.put("name", byId.getUsername());
        map.put("phone", byId.getPhone());
        return R.success(map);
    }

    @GetMapping("/reportBrowsing")
    @ApiOperation("推荐报告浏览")
    @Log(title = "推荐报告浏览", businessType = BusinessType.INSERT)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userid", required = true, value = "企业id"),
            @ApiImplicitParam(name = "jobHunter", required = true, value = "求职者id")
    })
    public void reportBrowsing(String userid, String jobHunter) throws Exception {
        runningWaterAsync.recommendationReportBrowse(userid, jobHunter);
    }

    /**
     * 更新投简订单
     */
    @GetMapping("/updateCloseAnAccount")
    public void updateCloseAnAccount(@RequestParam("putInResume") String putInResume, @RequestParam("state") Integer state) {
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("put_in_resume_id", putInResume);
        updateWrapper.set("close_an_account_if", state);
        updateWrapper.set("close_an_account_finish_time", LocalDateTime.now());
        putInResumeService.update(updateWrapper);
    }


    //矫正推荐管理接口
    @GetMapping("/correct")
    public LocalDateTime correct(@RequestParam("id") String id) {
        PutInResume byId = putInResumeService.getById(id);
        return byId.getCreateTime();
    }


    @ApiOperation("岗位数据")
    @GetMapping("/selectPutPost")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "postId", required = true, value = "岗位id "),
            @ApiImplicitParam(name = "keyword", required = false, value = "关键字"),
            // @ApiImplicitParam(name = "sate", required = false, value = "1:面试 2:offer 3:入职 4:淘汰"),
            @ApiImplicitParam(name = "sate", required = false, value = "1:未浏览 2:已下载 3:面试 4:offer 5:入职 6:淘汰"),
            @ApiImplicitParam(name = "pageSize"),
            @ApiImplicitParam(name = "pageNo")
    })
    @Log(title = "岗位数据", businessType = BusinessType.SELECT)
    public R<JobHunterPostPutInResumeDTO> selectPutPost(@RequestParam(value = "postId") String postId, @RequestParam(value = "keyword", required = false) String keyword, @RequestParam(value = "pageSize", required = false) Long pageSize, @RequestParam(value = "pageNo", required = false) Long pageNo, @RequestParam(value = "sate", required = false) Integer sate) {
        if (pageSize == null && pageNo == null) {
            pageSize = 10L;
            pageNo = 1L;
        }
        IPage<JobHunterPostPutInResumeDTO> iPage = putInResumeService.selectPutPost(new Page(pageNo, pageSize), postId, keyword, companyMetadata.userid(), sate);
        return R.success(iPage);
    }

    @GetMapping("/adduction")
    @Log(title = "投简数量", businessType = BusinessType.SELECT)
    @ApiImplicitParam(name = "postId", required = true, value = "岗位id ")
    @ApiOperation("投简数量")
    public R<RecommendedDataDTO> adduction(@RequestParam(value = "postId") String postId) {
        RecommendedDataDTO recommendedData = putInResumeService.recommendedPostData(companyMetadata.userid(), postId);
        BigDecimal bigDecimal = orderInfoApi.postServiceCharge(postId);
        recommendedData.setNotViewed(recommendedData.getRecommendCount() - recommendedData.getBrowse());
        recommendedData.setEarnings(bigDecimal);
        return R.success(recommendedData);
    }


    @GetMapping("/repetition")
    @ApiOperation("推荐查重")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", required = true, value = "电话号码"),
            @ApiImplicitParam(name = "name", required = true, value = "姓名"),
            @ApiImplicitParam(name = "postId", required = true, value = "岗位id")
    })
    public R repetition(@RequestParam(value = "phone") String phone, @RequestParam("name") String name, @RequestParam("postId") String postId) {
        String post = putInResumeService.repetition(phone, name, postId);
        return R.success(!StringUtils.isEmpty(post));
    }

    @GetMapping("/repetitionId")
    @ApiOperation("通过求职者id查询查重")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "jobHunter", required = true, value = "求职者id"),
            @ApiImplicitParam(name = "postId", required = true, value = "岗位id")
    })
    public R repetition(@RequestParam(value = "jobHunter") String jobHunter, @RequestParam("postId") String postId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("put_in_jobhunter", jobHunter);
        queryWrapper.eq("put_in_post", postId);
        int count = putInResumeService.count(queryWrapper);
        return R.success(count > 0 ? true : false);
    }


    @GetMapping("/fullMoon/{putInResumeId}")
    @ApiOperation("确认满月")
    @Log(title = "确认满月", businessType = BusinessType.UPDATE)
    @Transactional(rollbackFor = Exception.class)
    @AutoIdempotent
    public R fullMoon(@PathVariable("putInResumeId") String putInResumeId) {
        PutInResume byId = putInResumeService.getById(putInResumeId);
        if (byId == null) {
            return R.fail("没有该投简记录!");
        }
        if (byId.getFullMoonIf() != 0) {
            if (byId.getFullMoonIf() == 1) {
                return R.fail("已确认未满月,请不要重复操作");
            } else if (byId.getFullMoonIf() == 2) {
                return R.fail("已确认满月,请不要重复操作");
            }
        }
        if (!byId.getPutInComppany().equals(companyMetadata.userid())) {
            return R.fail("非法操作!");
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("put_in_resume_id", putInResumeId);
        InviteEntry one = inviteEntryService.getOne(queryWrapper);
        if (one == null) {
            return R.fail("未查到该份offer!");
        }

        //保证只修改一次
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("put_in_resume_id", putInResumeId);
        // updateWrapper.set("entry_time", LocalDateTime.of(one.getEntryDate(), one.getEntryTime()));
        updateWrapper.set("full_moon_if", 2);
        updateWrapper.set("resume_state", 10);
        updateWrapper.set("t_company_message", 0);
        //入职付则锁定订单
        if (one.getPostType() == 2) {
            updateWrapper.set("close_an_account_if", 2);
            updateWrapper.set("close_an_account_begin_time", LocalDateTime.now());
        }
        updateWrapper.set("put_in_message", 0);
        updateWrapper.set("dispose_full_moon_time", LocalDateTime.now());
        boolean update = putInResumeService.update(updateWrapper);
        if (update) {
            //当不为普通岗位才产生订单
            CompletableFuture<Object> completableFuture = CompletableFuture.supplyAsync(() -> {
                putInResumeService.updateRedisPost(byId.getPutInJobhunter(), byId.getPutInPost(), 9, byId.getPutInUser());
                return null;
            });
            //添加投简记录
            PutInResumeRecordVO putInResumeRecordVO = new PutInResumeRecordVO();
            putInResumeRecordVO.setCause("");
            putInResumeRecordVO.setDate(new Date());
            putInResumeRecordVO.setState(14);
            putInResumeRecordVO.setIdentity(HandleUtil.handle(companyMetadata.userid()));
            putInResumeRecordVO.setName(putInResumeService.getCompanyName(byId.getPutInResumeId()));
            putInResumeRecordService.addRecord(byId.getPutInResumeId(), putInResumeRecordVO);

            //产生订单
            if (one.getPostType() == 2 && byId.getCloseAnAccountIf() == 0) {
                TJobhunter byId1 = tJobhunterService.getById(byId.getPutInJobhunter());
                Map map = new ConcurrentHashMap();
                map.put("post_id", byId.getPutInPost());//岗位
                map.put("put_in_resume_id", byId.getPutInResumeId());//投简
                map.put("referrer_id", byId.getPutInUser());//推荐人id
                map.put("create_id", companyMetadata.userid());//创建人
                map.put("create_name", companyMetadata.userName());//创建人姓名
                map.put("payer", byId.getPutInComppany());//支付人
                map.put("post_type", one.getPostType());
                map.put("hired_bounty", one.getHiredBounty());
                map.put("recommend_time", byId.getCreateTime());
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime of = LocalDateTime.of(one.getEntryDate(), one.getEntryTime());
                map.put("entryTime", of.format(fmt));//入职时间

                map.put("avatar", byId1.getAvatar());
                map.put("jobHunter", byId1.getUserId());
                map.put("userName", byId1.getUserName());
                map.put("workTime", byId1.getWorkTime());
                map.put("education", byId1.getEducation());
                map.put("birthday", byId1.getBirthday());

          /*  map.put("percentage", one.getPercentage());
            map.put("inviteEntryId", one.getInviteEntryId());*/
                map.put("putInJobhunter", byId.getPutInJobhunter());
                map.put("entryDate", one.getEntryDate());//入职时间
      /*      //满月付岗位
            if (one.getPostType() == 1) {
                map.put("hired_bounty", one.getHiredBounty());
            }
            //入职付岗位
            if (one.getPostType() == 2) {
                map.put("money_reward", one.getMoneyReward());
            }*/
                postOrderProduce.sendOrderlyMessage(JSON.toJSONString(map));

            }
            //入职奖励
            //runningWaterAsync.entry(companyMetadata.userid(), byId.getPutInJobhunter(), byId.getPutInUser());
           /* String substring = byId.getPutInUser().substring(0, 2);
            if (substring.equals("TR")) {
                //添加入职记录
                referrerRecordAsync.entry(byId.getPutInUser(), byId.getPutInJobhunter());
            }*/
            informAsync.fullMoon(byId.getPutInResumeId());
            return R.success("操作成功!");
        }
        return R.fail("操作失败!");
    }


    @PostMapping("/notFullMoon")
    @ApiOperation("未满月")
    @Log(title = "未满月", businessType = BusinessType.UPDATE)
    @Transactional(rollbackFor = Exception.class)
    @AutoIdempotent
    public R notFullMoon(@RequestBody @Valid FullMoonVO fullMoonVO) {
        PutInResume byId = putInResumeService.getById(fullMoonVO.getPutInResumeId());
        if (byId == null) {
            return R.fail("没有该投简记录!");
        }
        if (byId.getFullMoonIf() != 0) {
            if (byId.getFullMoonIf() == 1) {
                return R.fail("已确认未满月,请不要重复操作");
            } else if (byId.getFullMoonIf() == 2) {
                return R.fail("已确认满月,请不要重复操作");
            }
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("put_in_resume_id", fullMoonVO.getPutInResumeId());
        InviteEntry one = inviteEntryService.getOne(queryWrapper);
        if (one == null) {
            return R.fail("请先发送offer！");
        }
        //查看是否入职或者是否操作入职
        Integer offerIf = byId.getOfferIf();
        if (offerIf == 0) {
            return R.fail("请确认入职!");
        } else if (offerIf == 1) {
            return R.fail("该求职者未入职,不能确认满月");
        }
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("put_in_resume_id", fullMoonVO.getPutInResumeId());
        updateWrapper.set("resume_state", 10);
        updateWrapper.set("full_moon_if", 1);
        updateWrapper.set("full_moon_cause", fullMoonVO.getFullMoonCause());
        updateWrapper.set("update_time", LocalDateTime.now());
        updateWrapper.set("dimission_time", LocalDateTime.now());
        updateWrapper.set("materials", JSON.toJSONString(fullMoonVO.getMaterials()));
        updateWrapper.set("t_company_message", 0);
        if (one.getPostType() == 2) {
            updateWrapper.set("not_full_moon", 1);
            updateWrapper.set("put_in_message", 1);
        } else {
            updateWrapper.set("put_in_message", 0);
        }
        updateWrapper.set("dispose_full_moon_time", LocalDateTime.now());
        boolean update1 = putInResumeService.update(updateWrapper);
        if (update1) {
            PutInResumeRecordVO putInResumeRecordVO = new PutInResumeRecordVO();
            putInResumeRecordVO.setCause(fullMoonVO.getFullMoonCause());
            putInResumeRecordVO.setDate(new Date());
            putInResumeRecordVO.setState(15);
            putInResumeRecordVO.setIdentity(HandleUtil.handle(companyMetadata.userid()));
            putInResumeRecordVO.setName(putInResumeService.getCompanyName(byId.getPutInResumeId()));
            putInResumeRecordService.addRecord(byId.getPutInResumeId(), putInResumeRecordVO);
            informAsync.notFullMoon(byId.getPutInResumeId());
            return R.success("操作成功!");
        }

        return R.fail("发送申请失败");
    }

    /**
     * 判断推荐报告是否为空
     *
     * @param recommendationReportVO
     * @return
     */
    public Map isNull(RecommendationReportVO recommendationReportVO) {
        Map map = new HashMap(2);
        map.put("isOk", false);
 /*      if(StringUtils.isEmpty(recommendationReportVO.getPutInPost())){
           map.put("msg","岗位id不能为空!");
       }*/
     /*   if(StringUtils.isEmpty(recommendationReportVO.getPutInComppany())){
            map.put("msg","投放的公司id不能为空!");
        }
*/
     /*   if (StringUtils.isEmpty(recommendationReportVO.getPutInComppany())) {
            map.put("msg", "投放的公司id不能为空!");

        }*/

        if (StringUtils.isEmpty(recommendationReportVO.getIntention())) {
            map.put("msg", "人选意向不能为空!");
            return map;
        }

        if (StringUtils.isEmpty(recommendationReportVO.getArrivalTime())) {
            map.put("msg", "到岗时间选项不能为空!");
            return map;
        }/*else if(! StringUtils.isEmpty(recommendationReportVO.getArrivalTime()) &&  recommendationReportVO.getArrivalTime()<1 &&  recommendationReportVO.getArrivalTime()>3 ){
            map.put("msg","没有该选项!");
        }*/

        if (StringUtils.isEmpty(recommendationReportVO.getCurrentAnnualSalary())) {
            map.put("msg", "目前年薪不能为空!");
            return map;
        }
        if (StringUtils.isEmpty(recommendationReportVO.getExpectAnnualSalary())) {
            map.put("msg", "期望税前年薪不能为空!");
            return map;
        }
        if (StringUtils.isEmpty(recommendationReportVO.getApplicationInterviewTime())) {
            map.put("msg", "面试时间不能为空!");
            return map;
        }
        if (StringUtils.isEmpty(recommendationReportVO.getExplains())) {
            map.put("msg", "推荐原因不能为空!");
            return map;
        }
     /*   if(StringUtils.isEmpty(recommendationReportVO.getExplains())){
            map.put("msg","推荐原因不能为空!");
        }*/
        map.put("isOk", true);
        return map;
    }


    @GetMapping("/consentNotFullMoon")
    @ApiOperation(value = "是否同意")
    @Log(title = "是否同意未满月申请", businessType = BusinessType.UPDATE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "putInResumeId", required = true, value = "投简id"),
            @ApiImplicitParam(name = "cause", required = false, value = "介入原因"),
            @ApiImplicitParam(name = "type", required = true, value = "操作状态(1:同意 2:客服介入)")
    })
    @Transactional(rollbackFor = Exception.class)
    @AutoIdempotent
    public R consentNotFullMoon(@RequestParam("putInResumeId") String putInResumeId, @RequestParam(value = "cause", required = false) String cause, @RequestParam("type") Integer type) {
        PutInResume byId = putInResumeService.getById(putInResumeId);
        if (byId == null) {
            return R.fail("未查寻到该投简!");
        }
        if (byId.getNotFullMoon() != 1) {
            return R.fail("请不要重复操作!");
        }
        if (type == 2) {
            GetInvolved getInvolved = new GetInvolved();
            getInvolved.setInviteEntryId("");
            getInvolved.setCause(cause);
            getInvolved.setPutInResumeId(putInResumeId);
            getInvolved.setCreateTime(LocalDateTime.now());
            getInvolved.setSate(0);
            getInvolvedService.save(getInvolved);
        }
        UpdateWrapper updateWrapper1 = new UpdateWrapper();
        updateWrapper1.eq("put_in_resume_id", putInResumeId);
        if (type == 1) {
            updateWrapper1.set("not_full_moon", 3);
            updateWrapper1.set("full_moon_if", 1);
        } else if (type == 2) {
            updateWrapper1.set("not_full_moon", 2);
        }
        updateWrapper1.set("put_in_message", 0);
        updateWrapper1.set("update_time", LocalDateTime.now());
        boolean update = putInResumeService.update(updateWrapper1);
        if (update) {
            return R.success("操作成功！");
        }
        return R.fail("操作失败！");
    }

    @GetMapping("/replacePostPutIn")
    @ApiOperation("代招职位查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "resumeState", required = true, value = "状态(0:全部 1:待浏览 2:已浏览 3:已下载)"),
            @ApiImplicitParam(name = "keyword", required = false, value = "关键字"),
            @ApiImplicitParam(name = "pageSize", required = true),
            @ApiImplicitParam(name = "pageNo", required = true),
            @ApiImplicitParam(name = "postId", required = true, value = "岗位id")
    })
    public R<SelectCompanyResumeDTO> replacePostPutIn(@RequestParam("pageSize") Long pageSize, @RequestParam("pageNo") Long pageNo, @RequestParam("resumeState") int resumeState, @RequestParam(value = "keyword", required = false) String keyword, @RequestParam("postId") String postId) {
        String userid = companyMetadata.userid();
        String substring = userid.substring(0, 2);
        if (!"TR".equals(substring)) {
            return R.fail("请使用推荐官账号!");
        }
        IPage<SelectCompanyResumeDTO> list = putInResumeService.replacePostPutIn(new Page(pageNo, pageSize), resumeState, keyword, userid, postId);
      /*  List<SelectCompanyResumeDTO> records = list.getRecords();
        for (SelectCompanyResumeDTO selectCompanyResumeDTO : records) {
            selectCompanyResumeDTO.setIsCollect(collectJobHunterService.isCollect(userid, selectCompanyResumeDTO.getPutInJobhunter()));
            QueryWrapper queryWrapper1 = new QueryWrapper();
            queryWrapper1.eq("user_id", selectCompanyResumeDTO.getPutInJobhunter());
            queryWrapper1.eq("company_id", userid);
            int count1 = companyUserRoleService.count(queryWrapper1);
            if (count1 == 0) {
                if (selectCompanyResumeDTO.getUserName() != null) {
                    selectCompanyResumeDTO.setUserName(selectCompanyResumeDTO.getUserName().substring(0, 1) + "**");
                }
            }
        }*/
        return R.success(list);
    }

}
