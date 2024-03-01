package com.wcwy.company.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.xiaoymin.knife4j.annotations.DynamicParameter;
import com.github.xiaoymin.knife4j.annotations.DynamicResponseParameters;
import com.wcwy.common.base.result.R;
import com.wcwy.common.base.utils.*;
import com.wcwy.common.redis.enums.QRCode;
import com.wcwy.common.redis.enums.Sole;
import com.wcwy.common.redis.util.IDGenerator;
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.common.web.annotation.AutoIdempotent;
import com.wcwy.company.dto.RMDownloadDTO;
import com.wcwy.company.dto.ReferrerRecordDTO;
import com.wcwy.company.dto.ReferrerRecordJobHunterDTO;
import com.wcwy.company.entity.*;
import com.wcwy.company.query.RMDownloadQuery;
import com.wcwy.company.query.ReferrerRecordQuery;
import com.wcwy.company.service.*;
import com.wcwy.company.session.CompanyMetadata;
import com.wcwy.company.vo.PutInResumeRecordVO;
import com.wcwy.post.api.OrderInfoApi;
import com.wcwy.post.api.OrderReceivingApi;
import com.wcwy.post.api.TCompanyPostApi;
import com.wcwy.post.entity.OrderInfo;
import com.wcwy.post.po.ParticularsPO;
import com.wcwy.system.annotation.Log;
import com.wcwy.system.enums.BusinessType;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.annotations.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Administrator
 * @description 针对表【referrer_record(推荐人数据记录)】的数据库操作Service
 * @createDate 2022-12-28 10:04:50
 */
@Api(tags = "推荐人数据记录")
@RestController
@RequestMapping("/referrerRecord")
public class ReferrerRecordController {
    @Autowired
    private ReferrerRecordService referrerRecordService;
    @Autowired
    private CompanyMetadata companyMetadata;
    @Autowired
    private TJobhunterService tJobhunterService;
    @Autowired
    private DivideIntoService divideIntoService;
    @Autowired
    private OrderInfoApi orderInfoApi;
    @Autowired
    private IDGenerator idGenerator;

    @Autowired
    private TJobHunterAttachmentService tJobHunterAttachmentService;

    @Autowired
    private TCompanyPostApi tCompanyPostApi;

    @Autowired
    private PutInResumeRecordService putInResumeRecordService;
    @Resource
    private PutInResumeService putInResumeService;
    @Resource
    private OrderReceivingApi orderReceivingApi;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private  TRecommendService tRecommendService;
    @Autowired
    private RecommendJobHunterService recommendJobHunterService;

    @Autowired
    private RedisUtils redisUtils;

    @GetMapping("/selectReferrerRecord")
    @ApiOperation("我的推荐数据")
    @Log(title = "我的推荐数据", businessType = BusinessType.SELECT)
    public R<ReferrerRecordDTO> selectReferrerRecord() {
        String userid = companyMetadata.userid();
        ReferrerRecord referrerRecord = this.selectReferrerRecord(userid);
        ReferrerRecordDTO referrerRecordDTO = new ReferrerRecordDTO();
        if (referrerRecord != null) {
            BeanUtils.copyProperties(referrerRecord, referrerRecordDTO);
        }
        QueryWrapper download = new QueryWrapper();
        download.eq("put_in_user", userid);
        download.eq("download_if", 2);
        int count8 = putInResumeService.count(download);
        referrerRecordDTO.setDownload((long) count8);

    /*    QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("share_person", userid);
        queryWrapper*/
        referrerRecordDTO.setTJobHunterSUM( tJobhunterService.mineJobSeeker(userid));

        Map<String, Long> talents = referrerRecordService.countTalents(companyMetadata.userid());
       // referrerRecordDTO.setTalents(talents.get("talents"));
        referrerRecordDTO.setTalents(referrerRecordDTO.getTJobHunterSUM());
        referrerRecordDTO.setPersonSelected(talents.get("personSelected"));
        Map<String, Integer> map = orderReceivingApi.selectCount(companyMetadata.userid());
        referrerRecordDTO.setPost(map.get("post"));
        referrerRecordDTO.setCollectPost(map.get("collectPost"));

        R<Map<String, Integer>> amount = tCompanyPostApi.amount(1);
        Map<String, Integer> data = amount.getData();
        referrerRecordDTO.setMinePost(data.get("count"));
        referrerRecordDTO.setMineOnlinePost(data.get("onLine"));
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("put_in_comppany",companyMetadata.userid());
        referrerRecordDTO.setInterviewResume(putInResumeService.count(queryWrapper));

        queryWrapper.in("browse_if",1,0);
        referrerRecordDTO.setNotViewed(putInResumeService.count(queryWrapper));

        return R.success(referrerRecordDTO);
    }


    /**
     * @param recommendId:推荐官id
     * @return ReferrerRecord
     * @Description: 获取推荐数据
     * @Author tangzhuo
     * @CreateTime 2023/1/3 13:55
     */
    public ReferrerRecord selectReferrerRecord(String recommendId) {
        ReferrerRecord referrerRecord = new ReferrerRecord();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("put_in_user", recommendId);
        int count = putInResumeService.count(queryWrapper);
        referrerRecord.setReferrer((long) count);
        QueryWrapper browse = new QueryWrapper();
        browse.eq("put_in_user", recommendId);
        browse.eq("browse_if", 2);
        int count1 = putInResumeService.count(browse);
        referrerRecord.setBrowse((long) count1);
        QueryWrapper subscribe = new QueryWrapper();
        subscribe.eq("put_in_user", recommendId);
        subscribe.eq("subscribe_if", 2);
        int count2 = putInResumeService.count(subscribe);
        referrerRecord.setAppoint((long) count2);

        QueryWrapper interview = new QueryWrapper();
        interview.eq("put_in_user", recommendId);
        interview.eq("interview_if", 2);
        int count3 = putInResumeService.count(interview);
        referrerRecord.setInterview((long) count3);

        QueryWrapper offer = new QueryWrapper();
        offer.eq("put_in_user", recommendId);
        offer.eq("offer_if", 2);
        int count4 = putInResumeService.count(offer);
        referrerRecord.setOffer((long) count4);
        QueryWrapper entry = new QueryWrapper();
        entry.eq("put_in_user", recommendId);
        entry.eq("offer_if", 2);
        int count5 = putInResumeService.count(entry);
        referrerRecord.setEntry((long) count5);




     /*   QueryWrapper weedOut = new QueryWrapper();
        weedOut.eq("put_in_user", recommendId);

        weedOut.eq("weed_out_if", 2);
        weedOut.or();
        weedOut.eq("exclude_if",2);*/
        int count6 = putInResumeService.countOut(recommendId);
        referrerRecord.setWeedOut((long) count6);

        QueryWrapper feedback = new QueryWrapper();
        feedback.eq("put_in_user", recommendId);
        feedback.eq("resume_state", 5);
        int count7 = putInResumeService.count(feedback);
        referrerRecord.setFeedback((long) count7);



        return referrerRecord;
    }


    @GetMapping("deleteTalents")
    @ApiOperation("删除我的人才")
    @Log(title = "删除我的人才", businessType = BusinessType.DELETE)
    @ApiImplicitParam(name = "jobHunterId", required = true, value = "求职者id")
    @Transactional(rollbackFor = Exception.class)
    public R deleteTalents(@RequestParam("jobHunterId") String jobHunterId){
        QueryWrapper queryWrapper1=new QueryWrapper();
        queryWrapper1.eq("put_in_user",companyMetadata.userid());
        queryWrapper1.eq("put_in_jobhunter",jobHunterId);
        queryWrapper1.notIn("resume_state" ,3,4);
        queryWrapper1.eq("close_an_account_if",0);
        int count = putInResumeService.count(queryWrapper1);
        if(count != 0){
            return R.fail("该求职者应聘未结束，请完成应聘流程才能删除！");
        }

        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("t_job_hunter_id",jobHunterId);
        queryWrapper.eq("recommend_id",companyMetadata.userid());
        ReferrerRecord one = referrerRecordService.getOne(queryWrapper);
        if(one==null){
            return R.fail("未查到该求职者！");
        }
   /*     if(one.getCorrelationType()==0){
            UpdateWrapper updateWrapper=new UpdateWrapper();
            updateWrapper.eq("user_id",one.getTJobHunterId());
            updateWrapper.set("share_person","");
            tJobhunterService.update(updateWrapper);
        }*/
        boolean b = referrerRecordService.removeById(one.getReferrerRecordId());
        if(b){
            return R.success("删除成功！");
        }
        return R.fail("删除失败！");
    }

    @GetMapping("/talents")
    @ApiOperation("我的人才")
    @Log(title = "我的人才", businessType = BusinessType.SELECT)
    public R<Map> talents() {
        /*List<Map> map=  referrerRecordService.talents(companyMetadata.userid());*/
        Map map = new HashMap();
        QueryWrapper insert = new QueryWrapper();
        insert.eq("recommend_id", companyMetadata.userid());
        insert.eq("correlation_type", 2);
        map.put("insert", referrerRecordService.count(insert));//新增
        /*@ApiModelProperty("今日增长")
        private Integer todayIncrease=0;*/


        //今日增长
        map.put("todayIncrease", tJobhunterService.mineJobSeekerDay(companyMetadata.userid()));
/*        QueryWrapper drainage = new QueryWrapper();
        drainage.eq("share_person", companyMetadata.userid());
        drainage.eq("perfect",1);
        map.put("drainage", tJobhunterService.count(drainage));//引流*/

        map.put("drainage", referrerRecordService.countJobHunter(companyMetadata.userid()));//引流


        QueryWrapper entrust = new QueryWrapper();
        entrust.eq("recommend_id", companyMetadata.userid());
        entrust.eq("correlation_type", 1);
        map.put("entrust", referrerRecordService.count(entrust));//委托
        QueryWrapper apply = new QueryWrapper();
        apply.eq("recommend_id", companyMetadata.userid());
        apply.eq("correlation_type", 3);
        map.put("apply", referrerRecordService.count(apply));//应聘
        return R.success(map);
    }

    @PostMapping("/select")
    @ApiOperation("我的人才详情")
    @Log(title = "我的人才详情", businessType = BusinessType.SELECT)
    public R<ReferrerRecordJobHunterDTO> select(@RequestBody ReferrerRecordQuery referrerRecordQuery) {


        if(referrerRecordQuery.getWorkExperience() !=null){
            Map<String,Date> map = WorkExperienceUtil.transitionDate(referrerRecordQuery.getWorkExperience());
            referrerRecordQuery.setBeginWorkTime(map.get("A"));
            referrerRecordQuery.setEndWorkTime(map.get("B"));
        }
        if (referrerRecordQuery.getBirthday() != null) {
            Map<String, Date> birthday = BirthdayUtils.getBirthday(referrerRecordQuery.getBirthday());
            referrerRecordQuery.setBeginAge(birthday.get("startTime"));
            referrerRecordQuery.setEndAge(birthday.get("endTime"));
        }
        IPage<ReferrerRecordJobHunterDTO> iPage = referrerRecordService.selectReferrerRecordJobHunter(companyMetadata.userid(), referrerRecordQuery);
        List<ReferrerRecordJobHunterDTO> records = iPage.getRecords();
        if(records.size()>0){
            for (ReferrerRecordJobHunterDTO record : records) {
                if(! StringUtils.isEmpty(referrerRecordQuery.getPostId())){
                    String resume = Sole.SEND_A_RESUME.getKey() + referrerRecordQuery.getPostId();
                    record.setSendAResume( redisUtils.sHasKey(resume,record.getUserId().trim()));
                }

                QueryWrapper queryWrapper=new QueryWrapper();
                queryWrapper.eq("t_job_hunter_id",record.getUserId());
                queryWrapper.eq("top",0);
                List<TJobHunterAttachment> list = tJobHunterAttachmentService.list(queryWrapper);
                if(list !=null && list.size()>0){
                    record.setResumePath(list.get(0).getPath());
                }
                try {
                    if(record.getDownloadIf() ==0){
                        String asterisk = NameUtils.createAsterisk(record.getUserName(), 1);
                        record.setUserName(asterisk);
                        record.setPhone(PhoneUtils.hidePhoneByRegular(record.getPhone()));
                    }
                    record.setReport(JSON.parseObject(record.getReport().toString(),Map.class));
                }catch (Exception e){
                    continue;
                }
            }
        }
        return R.success(iPage);
    }

    @GetMapping("/expectation")
    @ApiOperation("获取求职期望")
    @Log(title = "获取求职期望", businessType = BusinessType.SELECT)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", required = false, value = "关键字"),
            @ApiImplicitParam(name = "pageSize", value = "条数", required = true)
    })
    public R<List<String>> expectation(@RequestParam(value = "keyword", required = false) String keyword, @RequestParam("pageSize") Integer pageSize) {
        List<String> list = referrerRecordService.expectation(keyword, pageSize, companyMetadata.userid());
        List list1 = new ArrayList();
        for (String s : list) {
            list1.add(s.trim().substring(1, s.length() - 1));
        }
        return R.success(list1);
    }


    @GetMapping("/correct")
    @ApiOperation("矫正接口")
    public void correct() {
        List<TJobhunter> list = tJobhunterService.list();
        for (TJobhunter tJobhunter : list) {
            //查看求职者与推荐官的关系
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("t_job_hunter_id", tJobhunter.getUserId());
            queryWrapper.eq("recommend_id", companyMetadata.userid());
            queryWrapper.eq("deleted", 0);
            ReferrerRecord one = referrerRecordService.getOne(queryWrapper);
            if (one == null) {
                if (!StringUtils.isEmpty(tJobhunter.getSharePerson())) {
                    ReferrerRecord referrerRecord = new ReferrerRecord();
                    referrerRecord.setTJobHunterId(tJobhunter.getUserId());
                    referrerRecord.setRecommendId(tJobhunter.getSharePerson());
                    referrerRecord.setCorrelationType(0);
                    referrerRecord.setCreateTime(LocalDateTime.now());
                    boolean save1 = referrerRecordService.save(referrerRecord);
                }
            }
        }
    }

    @GetMapping("/download")
    @ApiOperation(" 推荐官下载简历操作")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "jobHunter", value = "求职者id", required = true),
            @ApiImplicitParam(name = "typePayment", value = "支付方式(1:无忧币 2:金币)", required = true),
            @ApiImplicitParam(name = "putInResumeId", value = "投简id", required = false)
    })
    @Log(title = "推荐官下载简历操作", businessType = BusinessType.UPDATE)
    @GlobalTransactional(rollbackFor = Exception.class)
    @AutoIdempotent
    public R download(@RequestParam("jobHunter") String jobHunter, @RequestParam("typePayment") Integer typePayment,@RequestParam(value = "putInResumeId",required = false) String putInResumeId) throws Exception {
        if (typePayment != 2 && typePayment != 1) {
            return R.fail("未知支付类型！");
        }
        if(StringUtils.isEmpty(putInResumeId)){
            TRecommend tRecommend = tRecommendService.getById(companyMetadata.userid());
            if(StringUtils.isEmpty(tRecommend.getRealName())){
                return R.fail("请先实名认证!");
            }
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("t_job_hunter_id", jobHunter);
        queryWrapper.eq("recommend_id", companyMetadata.userid());
        queryWrapper.eq("deleted", 0);
        ReferrerRecord one = referrerRecordService.getOne(queryWrapper);
        if (one == null) {
            if(! StringUtils.isEmpty(putInResumeId)){
                ReferrerRecord referrerRecord = new ReferrerRecord();
                referrerRecord.setTJobHunterId(jobHunter);
                referrerRecord.setRecommendId(companyMetadata.userid());
                referrerRecord.setCreateTime(LocalDateTime.now());
                referrerRecord.setOrigin(0);
                referrerRecord.setCorrelationType(3);
                boolean save1 = referrerRecordService.save(referrerRecord);
                if(save1){
                    one=referrerRecordService.getOne(queryWrapper);
                }
            }
            if(one==null){
                return R.fail("未查到该求职者条记录！");
            }

        }
        QueryWrapper queryWrapper1=new QueryWrapper();
        queryWrapper1.eq("job_hunter_id",jobHunter);
        queryWrapper1.eq("recommend_id",companyMetadata.userid());
        queryWrapper1.eq("deleted",0);
        RecommendJobHunter one1 = recommendJobHunterService.getOne(queryWrapper1);
        if(one1 != null){
            //更新投简记录
            if(!StringUtils.isEmpty(putInResumeId)){
                UpdateWrapper updateWrapper=new UpdateWrapper();
                updateWrapper.eq("put_in_resume_id",putInResumeId);
                updateWrapper.set("resume_state",2);
                updateWrapper.set("download_if",2);
                updateWrapper.set("download_time",LocalDateTime.now());
                putInResumeService.update(updateWrapper);
                PutInResumeRecordVO putInResumeRecordVO=new PutInResumeRecordVO();
                putInResumeRecordVO.setCause("");
                putInResumeRecordVO.setDate(new Date());
                putInResumeRecordVO.setState(2);
                putInResumeRecordVO.setIdentity(HandleUtil.handle(companyMetadata.userid()));
                putInResumeRecordVO.setName( putInResumeService.getCompanyName(putInResumeId));
                putInResumeRecordService.addRecord(putInResumeId,putInResumeRecordVO);
                if(one.getDownloadIf() !=1){
                    one.setDownloadIf(1);
                    one.setDownloadTime(LocalDateTime.now());
                    referrerRecordService.updateById(one);
                }
                PutInResume byId = putInResumeService.getById(putInResumeId);
                if(byId !=null){
                    putInResumeService.updateRedisPost(byId.getPutInJobhunter(), byId.getPutInPost(), 2, byId.getPutInUser());
                }


                return R.success("下载成功!");
            }
            //如果存在权限则更新下载
            one.setDownloadIf(1);
            one.setDownloadTime(LocalDateTime.now());
            boolean b = referrerRecordService.updateById(one);
            return R.fail("已下载该简历，请不要重复下载！");
        }
        R<Map> sendAResume = tJobhunterService.isSendAResume(one.getTJobHunterId());
        Map data = sendAResume.getData();
        Boolean o = (Boolean) data.get("isOK");
        if(! o){
            return sendAResume;
        }
        one.setMoney(new BigDecimal(0));
        TJobhunter byId = tJobhunterService.getById(one.getTJobHunterId());
        Map<String, Integer> map = divideIntoService.currencyCount( byId.getCurrentSalary());
        if (one.getCorrelationType() == 3) {
            R payment = payment(typePayment, byId);
            if(payment !=null && payment.getCode()==405){
                return payment;
            }
            //添加权限
            if(typePayment==1){
                one.setMoney(new BigDecimal(map.get("currencyCount")));
            }else if(typePayment==2){
                one.setMoney(new BigDecimal(map.get("gold")));
            }
    //如果处于分享投递则需要钱
        }else if(one.getCorrelationType()==4){
            if(typePayment==2){
                return R.fail("此份简历只能使用无忧币下载！");
            }
            payment(typePayment,byId);
            if(typePayment==1){
                one.setMoney(new BigDecimal(map.get("currencyCount")));
            }else if(typePayment==2){
                one.setMoney(new BigDecimal(map.get("gold")));
            }
        }
        one.setDownloadIf(1);
        one.setDownloadTime(LocalDateTime.now());
        one.setType(typePayment-1);

        boolean b = referrerRecordService.updateById(one);
        if (b) {
            recommendJobHunterService.add(companyMetadata.userid(),one.getTJobHunterId());
            //更新投简记录
            if(!StringUtils.isEmpty(putInResumeId)){
                UpdateWrapper updateWrapper=new UpdateWrapper();
                updateWrapper.eq("put_in_resume_id",putInResumeId);
                updateWrapper.set("resume_state",2);
                updateWrapper.set("download_if",2);
                updateWrapper.set("download_time",LocalDateTime.now());
                putInResumeService.update(updateWrapper);
                PutInResumeRecordVO putInResumeRecordVO=new PutInResumeRecordVO();
                putInResumeRecordVO.setCause("");
                putInResumeRecordVO.setDate(new Date());
                putInResumeRecordVO.setState(2);
                putInResumeRecordVO.setIdentity(HandleUtil.handle(companyMetadata.userid()));
                putInResumeRecordVO.setName( putInResumeService.getCompanyName(putInResumeId));
                putInResumeRecordService.addRecord(putInResumeId,putInResumeRecordVO);
                PutInResume byId11 = putInResumeService.getById(putInResumeId);
                if(byId !=null){
                    putInResumeService.updateRedisPost(byId11.getPutInJobhunter(), byId11.getPutInPost(), 2, byId11.getPutInUser());
                }

                return R.success("下载成功!");
            }
            return R.success("下载成功!");
        }
        return R.fail("下载失败!");
    }


    /**
     * @Description: 支付产品
     * @param typePayment 支付方式  byId：求职者信息
     * @return null
     * @Author tangzhuo
     * @CreateTime 2023/8/31 15:02
     */

    private R payment(Integer typePayment,TJobhunter byId) throws Exception {
        Map<String, Integer> map = divideIntoService.currencyCount( byId.getCurrentSalary());
        //扣除无忧币或者金币
        if (typePayment == 1) {

            //添加订单记录
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setOrderId(idGenerator.generateCode("OD"));
            //扣除无忧币
            Map<String, Object> map1 = tRecommendService.deductCurrency(new BigDecimal(map.get("currencyCount")), companyMetadata.userid(), byId.getUserId(), orderInfo.getOrderId());
            if(map1.get("state").equals(1)){
                return R.fail(map1.get("msg").toString());
            }
            orderInfo.setPayer(companyMetadata.userid());
            orderInfo.setPayerTime(LocalDateTime.now());
            orderInfo.setPaymentType("3");
            orderInfo.setTitle("简历下载!");
            orderInfo.setState(2);
            orderInfo.setJobhunterId(byId.getUserId());
            orderInfo.setMoney(new BigDecimal(map.get("currencyCount")));
            orderInfo.setPaymentAmount(new BigDecimal(map.get("currencyCount")));
            orderInfo.setIdentification(1);
            orderInfo.setDivideIntoIf(4);
            orderInfo.setCreateId(companyMetadata.userid());
            orderInfo.setPayer(companyMetadata.userid());
            orderInfo.setPayerTime(LocalDateTime.now());
            orderInfo.setCreateName(companyMetadata.userName());
            orderInfo.setCreateTime(LocalDateTime.now());
            orderInfo.setDeleted(0);
            ParticularsPO particularsPO = new ParticularsPO();
            particularsPO.setJobHunter(byId.getUserId());
            particularsPO.setAvatar(byId.getAvatar());
            particularsPO.setUserName(byId.getUserName());
            particularsPO.setWorkTime(byId.getWorkTime());
            particularsPO.setEducation(byId.getEducation());
            particularsPO.setCurrentSalary(byId.getCurrentSalary());
            particularsPO.setBirthday(byId.getBirthday());
            orderInfo.setParticulars(JSON.toJSONString(particularsPO));
            Boolean aBoolean = orderInfoApi.addOrder(JSONObject.toJSONString(orderInfo));
            if(! aBoolean){
                throw new Exception("订单创建失败");
            }
        } else if (typePayment == 2) {
            //扣除金币
            Map<String, Object> map1 = tRecommendService.deductGold(new BigDecimal(map.get("gold")), companyMetadata.userid(), byId.getUserId(), null);
            if(map1.get("state").equals(1)){
                return R.fail(map1.get("msg").toString());
            }
        }
        return null;
    }




    @PostMapping("/selectDownload")
    @ApiOperation("推荐官-我的下载")
    public R<RMDownloadDTO> selectDownload(@RequestBody RMDownloadQuery rmDownloadQuery) {
        if (rmDownloadQuery.getBirthday() != null) {
            Map<String, Date> birthday = BirthdayUtils.getBirthday(rmDownloadQuery.getBirthday());
            rmDownloadQuery.setStartTime(birthday.get("startTime"));
            rmDownloadQuery.setEndTime(birthday.get("endTime"));
        }
        IPage<RMDownloadDTO> iPage = referrerRecordService.selectDownload(rmDownloadQuery, companyMetadata.userid());
        return R.success(iPage);
    }


    @GetMapping("/exist")
    public Boolean exist(@RequestParam("johHunter") String johHunter) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("t_job_hunter_id", johHunter);
        queryWrapper.eq("recommend_id", companyMetadata.userid());
        ReferrerRecord one = referrerRecordService.getOne(queryWrapper);
        return one.getDownloadIf() == 1 ? true : false;
    }


}
