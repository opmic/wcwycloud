package com.wcwy.post.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wawy.company.api.*;
import com.wcwy.common.base.annotation.RateLimiter;
import com.wcwy.common.base.enums.Gold;
import com.wcwy.common.base.enums.Lock;
import com.wcwy.common.base.query.PageQuery;
import com.wcwy.common.base.result.R;
import com.wcwy.common.base.utils.*;
import com.wcwy.common.redis.enums.Cache;
import com.wcwy.common.redis.enums.FirmPost;
import com.wcwy.common.redis.enums.RedisCache;
import com.wcwy.common.redis.enums.Sole;
import com.wcwy.common.redis.util.IDGenerator;
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.common.web.annotation.AutoIdempotent;
import com.wcwy.company.dto.CompanyCollerctPutInResume;
import com.wcwy.company.entity.TCompany;
import com.wcwy.post.api.TCompanyPostApi;
import com.wcwy.post.asyn.UpdatePostRecordAsync;
import com.wcwy.post.dto.*;
import com.wcwy.post.entity.*;
import com.wcwy.post.po.LatestPostPO;
import com.wcwy.post.po.PostLabel;
import com.wcwy.post.po.StateSum;
import com.wcwy.post.po.TCompanyPostPO;
import com.wcwy.post.pojo.TCompanyPostRecord;
import com.wcwy.post.produce.EiCompanyPostProduce;
import com.wcwy.post.produce.GoldProduce;
import com.wcwy.post.query.*;
import com.wcwy.post.service.*;
import com.wcwy.post.session.CompanyMetadata;
import com.wcwy.post.vo.HeadhunterPositionRecordVO;
import com.wcwy.post.vo.TCompanyPostStopVO;
import com.wcwy.post.vo.TCompanyPostUpdateVO;
import com.wcwy.post.vo.TCompanyPostVO;
import com.wcwy.system.annotation.Log;
import com.wcwy.system.enums.BusinessType;
import io.swagger.annotations.*;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.ls.LSInput;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * ClassName: TCompanyPost
 * Description:
 * date: 2022/9/14 14:55
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@RestController
@Api(tags = "岗位信息接口")
@RequestMapping("/tCompanyPost")
public class TCompanyPostController {
    @Autowired
    private TCompanyPostService tCompanyPostService;
    @Autowired
    private CompanyMetadata companyMetadata;
    @Resource
    private TPayConfigService tPayConfigService;
    @Autowired
    private UpdatePostRecordService updatePostRecordService;
    @Autowired
    private UpdatePostRecordAsync updatePostRecordAsync;
    @Autowired
    private IDGenerator idGenerator;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private TPostShareService tPostShareService;
    @Autowired
    private TCompanyApi tCompanyApi;
    @Autowired
    private TCompanyLoginApi tCompanyLoginApi;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private CollerctPostApi collerctPostApi;
    @Autowired
    JavaMailSenderImpl mailSender;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private EiCompanyPostProduce eiCompanyPostProduce;
    @Autowired
    private HeadhunterPositionRecordService headhunterPositionRecordService;
    @Autowired
    private TCompanyContractApi tCompanyContractApi;

    @Autowired
    private ResumePaymentConfigService resumePaymentConfigService;

    @Autowired
    private GoldProduce goldProduce;
    @Autowired
    private OrderReceivingService orderReceivingService;

  /*  @PostMapping("/select")
    @ApiOperation(value = "查询岗位信息接口")
    public R<TCompanyPost> select(@RequestBody TCompanyPostQuery tCompanyPostQuery) {
        System.out.println(tCompanyPostQuery.toString());
        IPage<TCompanyPost> iPage = tCompanyPostService.selectListPage(tCompanyPostQuery.createPage(), tCompanyPostQuery);
        return R.success(iPage);
    }*/

    /**
     * 查询全部及上线职位
     * @return
     */




    @GetMapping("/selectCount")
    public Map<String,Integer> selectCount(@RequestParam("userId") String userId){
        Map<String,Integer> map=new HashMap(2);
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("company_id",userId);
        queryWrapper.eq("deleted",0);
        map.put("postCount", tCompanyPostService.count(queryWrapper));
        queryWrapper.eq("status",1);
        map.put("onLinePost", tCompanyPostService.count(queryWrapper));
        return map;
    }


    @PostMapping("/selectPostShare")
    @ApiOperation(value = "查询岗位信息及岗位记录接口")
    @Log(title = "查询岗位信息及岗位记录接口", businessType = BusinessType.SELECT)
    public R<PostShare> selectPostShare(@RequestBody TCompanyPostQuery tCompanyPostQuery) {
        String userid = companyMetadata.userid();
        String substring = userid.substring(0, 2);
        if ("TC".equals(substring)) {
            tCompanyPostQuery.setCompanyId(userid);
        }
        IPage<PostShare> iPage = tCompanyPostService.selectListPagePostShare(tCompanyPostQuery.createPage(), tCompanyPostQuery);
        return R.success(iPage);
    }

    @GetMapping("/selectSonPostShare")
    @ApiOperation("查询子账号岗位信息及岗位记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "identification", required = true, value = "标识(人才推荐管 全部:0普通岗位 1:赏金岗位 2猎头岗位 4:人才推荐官全部)"),
            @ApiImplicitParam(name = "postName", required = false, value = "关键字"),
            @ApiImplicitParam(name = "pageSize", required = true),
            @ApiImplicitParam(name = "pageNo", required = true),
            @ApiImplicitParam(name = "companyId", required = true, value = "企业id")
    })
    @Log(title = "查询子账号岗位信息及岗位记录", businessType = BusinessType.SELECT)
    public R<PostShare> selectSonPostShare(@RequestParam("identification") Integer identification, @RequestParam(value = "postName", required = false) String postName, @RequestParam("pageSize") Integer pageSize, @RequestParam("pageNo") Integer pageNo, @RequestParam("companyId") String companyId) {
        if (pageSize == null) {
            pageSize = 10;
        }
        if (pageNo == null) {
            pageNo = 0;
        }
        if (StringUtils.isEmpty(companyId)) {
            return R.fail("子企业id不能为空!");
        }
        IPage<PostShare> iPage = tCompanyPostService.selectSonPostShare(new Page(pageNo, pageSize), identification, postName, companyId);
        return R.success(iPage);
    }

    @GetMapping("/stateSum")
    @ApiOperation("获取岗位状态数量")
    @Log(title = "获取岗位状态数量", businessType = BusinessType.SELECT)
    public R<StateSum> stateSum() {
        StateSum stateSum = new StateSum();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("company_id", companyMetadata.userid());
        queryWrapper.eq("status", 1);
        queryWrapper.eq("deleted", 0);
        stateSum.setOnline(tCompanyPostService.count(queryWrapper));

        QueryWrapper queryWrapper1 = new QueryWrapper();
        queryWrapper1.eq("company_id", companyMetadata.userid());
        queryWrapper1.eq("status", 0);
        queryWrapper.eq("deleted", 0);
        stateSum.setNotOnline(tCompanyPostService.count(queryWrapper1));
        QueryWrapper queryWrapper2 = new QueryWrapper();
        queryWrapper2.eq("company_id", companyMetadata.userid());
        queryWrapper2.eq("audit", 0);
        queryWrapper.eq("deleted", 0);
        stateSum.setInReview(tCompanyPostService.count(queryWrapper2));
        QueryWrapper queryWrapper3 = new QueryWrapper();
        queryWrapper3.eq("company_id", companyMetadata.userid());
        queryWrapper3.eq("audit", 1);
        queryWrapper.eq("deleted", 0);
        stateSum.setNotPass(tCompanyPostService.count(queryWrapper3));
        return R.success(stateSum);
    }


    @PostMapping("/insert")
    @ApiOperation(value = "添加岗位")
    @Transactional(rollbackFor = Exception.class)
    @Log(title = "添加岗位", businessType = BusinessType.INSERT)
    @AutoIdempotent
    public R insert(@Valid @RequestBody TCompanyPostVO tCompanyPostVO) throws Exception {
        Boolean aBoolean = DateUtils.exceedDate(tCompanyPostVO.getExpirationDate());
        if (!aBoolean) {
            return R.fail("截止日期不能大于一年！");
        }
        boolean verify = PhoneEmailUtils.verify(tCompanyPostVO.getDescription());
        if(verify){
            return R.fail("职位详情不能存在联系方式！");
        }
        RLock lock = redissonClient.getLock(Lock.INSERT_PT.getLockSion() + tCompanyPostVO.getCompanyId());
        boolean isLock = lock.tryLock(5, 10, TimeUnit.SECONDS);
        if (tCompanyPostVO.getPostNature() == 1) {
            if(tCompanyPostVO.getPostType()!=4){
                //判断身份是否有权限
                Map<String, Object> map = tCompanyContractApi.passTheAudit();
                Boolean passTheAudit = (Boolean) map.get("passTheAudit");
                if (!passTheAudit) {
                    return R.fail(map.get("msg").toString());
                }
            }

        }
        try {
            if (isLock) {
         /*       QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("post_label", tCompanyPostVO.getPostLabel().trim());
                queryWrapper.eq("company_id", companyMetadata.userid());
                int count = tCompanyPostService.count(queryWrapper);
                if (count > 0) {
                    return R.fail("已有相同的职位名称，请修改后重新发布!");
                }*/
                TCompanyPost tCompanyPost = new TCompanyPost();

                BeanUtils.copyProperties(tCompanyPostVO, tCompanyPost);
                tCompanyPost.setPostId(idGenerator.generateCode("PS"));  
                tCompanyPost.setDeleted(0);
                tCompanyPost.setCompanyId(companyMetadata.userid());
                tCompanyPost.setCreateTime(LocalDateTime.now());
                tCompanyPost.setUpdateTime(LocalDateTime.now());
                tCompanyPost.setUpdateId(companyMetadata.userid());
                tCompanyPost.setCreateId(companyMetadata.userid());
                tCompanyPost.setStatus(1);
                tCompanyPost.setDayTime(LocalDateTime.now());
                tCompanyPost.setFirmSize(ScaleUtil.screen(tCompanyPostVO.getFirmSize()));
                tCompanyPost.setEducationId(EducationUtil.screen(tCompanyPostVO.getEducationType()));
                tCompanyPost.setWorkExperienceId(WorkExperienceUtil.screenOut(tCompanyPostVO.getWorkExperience()));
                //如果是简历付则判断是校园或职场
                if (tCompanyPostVO.getPostType() == 4) {
                    if (StringUtils.isEmpty(tCompanyPostVO.getPostNatureType())) {
                        return R.fail("简历付请选择(校园和职场)!");
                    }
                    tCompanyPost.setPostType(tCompanyPostVO.getPostType() + tCompanyPostVO.getPostNatureType());
                }

                String key = Cache.CACHE_CITY.getKey();
                tCompanyPost.setCityid("");
    /*            if (tCompanyPostVO.getPostType() != 0) {
                    TPayConfig tPayConfig = (TPayConfig) redisUtils.get(RedisCache.TPAYCONFIG.getValue());
                    if (tCompanyPostVO.getPostType() == 1) {
                        tCompanyPost.setPercentage(tPayConfig.getEntryPayment());
                    } else if (tCompanyPostVO.getPostType() == 2) {
                        tCompanyPost.setPercentage(tPayConfig.getFullMoonPayment());
                    } else if (tCompanyPostVO.getPostType() == 3) {
                        tCompanyPost.setPercentage(tPayConfig.getInterviewPayment());
                    }
                }*/
                //获取城市编码
                if (!StringUtils.isEmpty(tCompanyPost.getWorkCity().getCity())) {
                    String o = redisUtils.get(key + tCompanyPost.getWorkCity().getCity()) + "";
                    if (!StringUtils.isEmpty(o)) {
                        tCompanyPost.setCityid(o);
                    }

                }
                //获取省份编码
                tCompanyPost.setProvinceid(0);
                if (!StringUtils.isEmpty(tCompanyPost.getWorkCity().getProvince())) {
                    Integer o = Integer.getInteger(redisUtils.get(key + tCompanyPost.getWorkCity().getProvince()) + "");
                    if (!StringUtils.isEmpty(o)) {
                        tCompanyPost.setProvinceid(o);
                    }
                }
                //需要优化
                Integer postType = tCompanyPostVO.getPostType();
                tCompanyPost.setAudit(2);
                if (postType != 0) {
                    //注意以后修改
                    tCompanyPost.setAudit(2);
                }
                boolean r = tCompanyPostService.save(tCompanyPost);
                if (r) {
                    TPostShare tPostShare = new TPostShare();
                    tPostShare.setShareId(idGenerator.generateCode("PH"));
                    tPostShare.setCompanyPostId(tCompanyPost.getPostId());
                    tPostShare.setShareSize(0L);
                    tPostShare.setDownloadSize(0L);
                    tPostShare.setBrowseSize(0L);
                    tPostShare.setInterviewSize(0L);
                    tPostShare.setEntrySize(0L);
                    tPostShare.setSubscribe(0L);
                    tPostShare.setOfferSize(0L);
                    tPostShare.setCloseAnAccount(new BigDecimal(0));
                    tPostShareService.save(tPostShare);
       /*             if (tCompanyPostVO.getHeadhunterPositionRecordVOS() != null && tCompanyPostVO.getHeadhunterPositionRecordVOS().size() > 0 && tCompanyPostVO.getPostType() == 2) {
                        List<HeadhunterPositionRecordVO> headhunterPositionRecordVOS = tCompanyPostVO.getHeadhunterPositionRecordVOS();
                        List<HeadhunterPositionRecord> list = new ArrayList<>();
                        for (HeadhunterPositionRecordVO headhunterPositionRecordVO : headhunterPositionRecordVOS) {
                            if (headhunterPositionRecordVO.getMoney().compareTo(new BigDecimal(5000)) == -1) {
                                throw new Exception("按月付不能小于5000元!");
                            }
                            HeadhunterPositionRecord headhunterPositionRecord = new HeadhunterPositionRecord();
                            BeanUtils.copyProperties(headhunterPositionRecordVO, headhunterPositionRecord);
                            headhunterPositionRecord.setPostId(tCompanyPost.getPostId());
                            list.add(headhunterPositionRecord);
                            headhunterPositionRecord = null;
                        }
                        headhunterPositionRecordService.saveBatch(list);
                    }*/
                    /*   tCompanyPostService.getById( tCompanyPost.getPostId()).v;*/
                    Map map = new ConcurrentHashMap();
                    map.put("post_id", tCompanyPost.getPostId());
                    map.put("company_id", tCompanyPost.getCompanyId());
                    map.put("begin_salary", tCompanyPost.getBeginSalary());
                    map.put("end_salary", tCompanyPost.getEndSalary());
                    map.put("work_city", tCompanyPost.getWorkCity());
                    map.put("post_label", tCompanyPost.getPostLabel());
                    map.put("post_type", tCompanyPost.getPostType());
                    map.put("company_name", tCompanyPost.getCompanyName());
                    //map.put("hired_bounty", 0.00);
                   /* if (tCompanyPostVO.getHiredBounty() != null) {
                        map.put("hired_bounty", tCompanyPostVO.getHiredBounty());
                    }*/
                    // map.put("money_reward", "[]");
                    /*   if (tCompanyPostVO.getHeadhunterPositionRecordVOS().size() > 0 && tCompanyPostVO.getHeadhunterPositionRecordVOS() != null) {
                        map.put("money_reward", JSON.toJSONString(tCompanyPostVO.getHeadhunterPositionRecordVOS()));
                    }*/

                    map.put("uuid", redisUtils.generateCode());
                    eiCompanyPostProduce.sendAsyncMessage(JSON.toJSONString(map));
                    long l = redisUtils.incrTime(Gold.POST_GOLD.getValue() + companyMetadata.userid());
                    GoldConfig o1 = (GoldConfig) redisUtils.get(RedisCache.GOLD_CACHE.getValue());
                    if (l <= o1.getPostCount()) {
                        Map map1 = new HashMap(3);
                        map1.put("uuid", redisUtils.generateCode());
                        map1.put("userid", companyMetadata.userid());
                        map1.put("type", Gold.POST_GOLD.getType());
                        goldProduce.sendAsyncMessage(JSON.toJSONString(map1));

                    }
                    return R.success("添加成功！");
                }
            }
        } finally {
            lock.unlock();
        }

        return R.fail("添加失败！");
    }


    @PostMapping("/updateTCompanyPost")
    @ApiOperation(value = "更新岗位")
    @Transactional(rollbackFor = Exception.class)
    @Log(title = "更新岗位", businessType = BusinessType.UPDATE)
    @AutoIdempotent
    public R updateTCompanyPost(@Valid @RequestBody TCompanyPostUpdateVO tCompanyPostUpdateVO) {
        Boolean aBoolean1 = DateUtils.exceedDate(tCompanyPostUpdateVO.getExpirationDate());
        if (!aBoolean1) {
            return R.fail("截止日期不能大于一年！");
        }
        boolean verify = PhoneEmailUtils.verify(tCompanyPostUpdateVO.getDescription());
        if(verify){
            return R.fail("职位详情不能存在联系方式！");
        }
        TCompanyPost byId = tCompanyPostService.getById(tCompanyPostUpdateVO.getPostId());

        if (byId.getPostType() >= 1 && byId.getPostType() <= 3 ) {

            //判断身份是否有权限
            Map<String, Object> map = tCompanyContractApi.passTheAudit();
            Boolean passTheAudit = (Boolean) map.get("passTheAudit");
            if (!passTheAudit) {
                return R.fail(map.get("msg").toString());
            }
        }

        //判断是否能进行修改
        //如果是普通岗位和简历付就直接修改
        //入职付  满月付
        //到面付
    /*    Boolean aBoolean = tCompanyPostService.postAmend(tCompanyPostUpdateVO.getPostId());
        if (aBoolean == null) {
            return R.fail("岗位不存在!");
        }
        if (aBoolean == false) {
            return R.fail("该岗位存在投简未处理!");
        }*/

   /*     QueryWrapper queryWrapper1 = new QueryWrapper();
        queryWrapper1.eq("post_label", tCompanyPostUpdateVO.getPostLabel().trim());
        queryWrapper1.eq("company_id", companyMetadata.userid());
        queryWrapper1.ne("post_id", tCompanyPostUpdateVO.getPostId());
        int count = tCompanyPostService.count(queryWrapper1);
        if (count > 0) {
            return R.fail("线上已有相同职位，请修改后重新发布!");
        }*/
        TCompanyPost tCompanyPost = new TCompanyPost();
        BeanUtils.copyProperties(tCompanyPostUpdateVO, tCompanyPost);
        tCompanyPost.setUpdateTime(LocalDateTime.now());
        tCompanyPost.setUpdateId(companyMetadata.userid());
        //如果是简历付则判断是校园或职场
/*        if (tCompanyPostUpdateVO.getPostType() == 4) {
            if (StringUtils.isEmpty(tCompanyPostUpdateVO.getPostNatureType())) {
                return R.fail("简历付请选择(校园和职场)!");
            }
            tCompanyPost.setPostType(tCompanyPostUpdateVO.getPostType() + tCompanyPostUpdateVO.getPostNatureType());
        }*/

        tCompanyPost.setStatus(1);
        tCompanyPost.setEducationId(EducationUtil.screen(tCompanyPostUpdateVO.getEducationType()));
        tCompanyPost.setWorkExperienceId(WorkExperienceUtil.screenOut(tCompanyPostUpdateVO.getWorkExperience()));
        tCompanyPost.setCityid("");
        tCompanyPost.setFirmSize(ScaleUtil.screen(tCompanyPostUpdateVO.getFirmSize()));
        /*if (tCompanyPostUpdateVO.getPostType() != 0) {
            TPayConfig tPayConfig = (TPayConfig) redisUtils.get(RedisCache.TPAYCONFIG.getValue());
            if (tCompanyPostUpdateVO.getPostType() == 1) {
                tCompanyPost.setPercentage(tPayConfig.getEntryPayment());
            } else if (tCompanyPostUpdateVO.getPostType() == 2) {
                tCompanyPost.setPercentage(tPayConfig.getFullMoonPayment());
            } else if (tCompanyPostUpdateVO.getPostType() == 3) {
                tCompanyPost.setPercentage(tPayConfig.getInterviewPayment());
            }
        }*/
        String key = Cache.CACHE_CITY.getKey();
        if (!StringUtils.isEmpty(tCompanyPost.getWorkCity().getCity())) {
            String o = redisUtils.get(key + tCompanyPost.getWorkCity().getCity()) + "";
            if (!StringUtils.isEmpty(o)) {
                tCompanyPost.setCityid(o);
            }

        }
        tCompanyPost.setProvinceid(0);
        if (!StringUtils.isEmpty(tCompanyPost.getWorkCity().getProvince())) {
            Integer o = Integer.getInteger(redisUtils.get(key + tCompanyPost.getWorkCity().getProvince()) + "");
            if (!StringUtils.isEmpty(o)) {
                tCompanyPost.setProvinceid(o);
            }
        }
        //需要优化
        //Integer postType = tCompanyPostUpdateVO.getPostType();
        tCompanyPost.setAudit(2);
    /*    if (tCompanyPostUpdateVO.getConceal() == 1) {
            tCompanyPost.setLogo("https://dev-1313175594.cos.ap-guangzhou.myqcloud.com/head%2F107.jpg");
        }*/
        //重新审核
       /* if (postType != 0) {
            tCompanyPost.setAudit(2);
            // tCompanyPost.setAudit(0);
        }*/

        boolean b = tCompanyPostService.updateById(tCompanyPost);

        if (b) {
            updatePostRecordAsync.add(byId.getPostId(), companyMetadata.userid(), JSON.toJSONString(byId));
            Map map = new ConcurrentHashMap();
            map.put("post_id", tCompanyPost.getPostId());
            map.put("company_id", tCompanyPost.getCompanyId());
            map.put("begin_salary", tCompanyPost.getBeginSalary());
            map.put("end_salary", tCompanyPost.getEndSalary());
            map.put("work_city", tCompanyPost.getWorkCity());
            map.put("post_label", tCompanyPost.getPostLabel());
            map.put("post_type", byId.getPostType());
            map.put("uuid", redisUtils.generateCode());
            map.put("company_name", tCompanyPost.getCompanyName());
            eiCompanyPostProduce.sendAsyncMessage(JSON.toJSONString(map));
            return R.success("更新成功");
        }
        return R.fail("更新失败");
    }


    @PostMapping("/stopTCompanyPost")
    @ApiOperation(value = "停止+招聘中+取消 岗位接口")
    @Transactional
    @Log(title = "停止+招聘中+取消 岗位接口", businessType = BusinessType.UPDATE)
    @AutoIdempotent
    public R stopTCompanyPost(@RequestBody TCompanyPostStopVO tCompanyPostStopVO) throws Exception {
        if (tCompanyPostStopVO.getStatus() == 2) {
            boolean b = tCompanyPostService.removeByIds(tCompanyPostStopVO.getPostId());
            if (b) {
                return R.success("取消成功");
            }
            return R.fail("取消失败");
        }
        boolean isUpdate = true;
        for (String s : tCompanyPostStopVO.getPostId()) {
            TCompanyPost byId = tCompanyPostService.getById(s);
            LocalDate date = LocalDate.now();
            if (byId != null) {
                LocalDate expirationDate = byId.getExpirationDate();
                if (expirationDate != null) {
                    boolean after = date.isAfter(expirationDate);
                    if (after) {
                        // throw new Exception("请修改岗位" + byId.getPostLabel() + "的招聘截止日期");
                        return R.fail("请修改岗位" + byId.getPostLabel() + "的招聘截止日期");
                    }
                }
            }
            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("post_id", s);
            updateWrapper.set("status", tCompanyPostStopVO.getStatus());
            updateWrapper.set("update_time", LocalDateTime.now());
            updateWrapper.set("update_id", companyMetadata.userid());
            if (tCompanyPostStopVO.getStatus() == 1) {
                updateWrapper.set("day_time", LocalDateTime.now());
            }
            isUpdate = tCompanyPostService.update(updateWrapper);
        }
        return R.success("操作成功");
    }

    /**
     * @param beginSalary：岗位开始薪资
     * @param hiredBounty：赏金金额
     * @return null
     * @Description: 判断赏金是否符合要求
     * @Author tangzhuo
     * @CreateTime 2022/9/16 8:35
     */
    public Map<Integer, String> isHiredBounty(BigDecimal beginSalary, BigDecimal hiredBounty) {
        Map<Integer, String> MapR = new ConcurrentHashMap();
        Map<BigDecimal, BigDecimal> map = new ConcurrentHashMap<>();
        map.put(new BigDecimal(10), new BigDecimal(10000));
        map.put(new BigDecimal(20), new BigDecimal(15000));
        map.put(new BigDecimal(30), new BigDecimal(20000));
        map.put(new BigDecimal(40), new BigDecimal(25000));
        map.put(new BigDecimal(50), new BigDecimal(30000));
        if (new BigDecimal(10).compareTo(beginSalary) == -1) {
            /*10万内---设置金额不得低于0.5万*/
            if (hiredBounty.compareTo(new BigDecimal(0.5)) == -1) {
                MapR.put(101, "10万内---设置金额不得低于0.5万");

            } else if (hiredBounty.compareTo(new BigDecimal(5000)) == 1 && hiredBounty.compareTo(new BigDecimal(5000)) == 0) {
                MapR.put(200, "操作正确！");
            }
        }

        if (beginSalary.compareTo(new BigDecimal(10)) == 1 || new BigDecimal(10).compareTo(beginSalary) == 0) {
            System.out.println(map.get(new BigDecimal(10)));
            if (hiredBounty.compareTo(map.get(new BigDecimal(10))) == -1) {
                /*10万-20万---设置金额不得低于1.0万*/
                MapR.put(101, "10万-20万---设置金额不得低于1.0万");
            } else if (hiredBounty.compareTo(map.get(new BigDecimal(10))) == 0 || hiredBounty.compareTo(map.get(new BigDecimal(10))) == 1) {
                MapR.put(200, "操作正确！");
            }
        }

        if (beginSalary.compareTo(new BigDecimal(20)) == 1 || new BigDecimal(20).compareTo(beginSalary) == 0) {
            if (hiredBounty.compareTo(map.get(new BigDecimal(20))) == -1) {
                /*20万-30万-设置金额不得低于1.5万*/
                MapR.put(101, "20万-30万-设置金额不得低于1.5万");
            } else if (hiredBounty.compareTo(map.get(new BigDecimal(20))) == 0 || hiredBounty.compareTo(map.get(new BigDecimal(20))) == 1) {
                MapR.put(200, "操作正确！");
            }
        }

        if (beginSalary.compareTo(new BigDecimal(30)) == 1 || new BigDecimal(30).compareTo(beginSalary) == 0) {
            if (hiredBounty.compareTo(map.get(new BigDecimal(30))) == -1) {
                /*30万-40万--设置金额不得低于2万*/
                MapR.put(101, "30万-40万--设置金额不得低于2万");
            } else if (hiredBounty.compareTo(map.get(new BigDecimal(30))) == 0 || hiredBounty.compareTo(map.get(new BigDecimal(30))) == 1) {
                MapR.put(200, "操作正确！");
            }
        }

        if (beginSalary.compareTo(new BigDecimal(40)) == 1 || new BigDecimal(40).compareTo(beginSalary) == 0) {
            if (hiredBounty.compareTo(map.get(new BigDecimal(40))) == -1) {
                /*40万-50万--设置金额不得低于2.5万*/
                MapR.put(101, "40万-50万--设置金额不得低于2.5万");
            } else if (hiredBounty.compareTo(map.get(new BigDecimal(40))) == 0 || hiredBounty.compareTo(map.get(new BigDecimal(40))) == 1) {
                MapR.put(200, "操作正确！");
            }
        }

        if (beginSalary.compareTo(new BigDecimal(50)) == 1 || new BigDecimal(50).compareTo(beginSalary) == 0) {
            if (hiredBounty.compareTo(map.get(new BigDecimal(50))) == -1) {
                /*50万-60万-设置金额不得低于3万*/
                MapR.put(101, "50万-60万-设置金额不得低于3万");
            } else if (hiredBounty.compareTo(map.get(new BigDecimal(50))) == 0 || hiredBounty.compareTo(map.get(new BigDecimal(50))) == 1) {
                MapR.put(200, "操作正确！");
            }
        }


        return MapR;
    }


    /**
     * 主页面获取岗位的接口
     */
    @PostMapping("/selectNewest")
    @ApiOperation(value = "职位获取岗位信息")
    @Log(title = "职位获取岗位信息", businessType = BusinessType.SELECT)
    public R<CompanyPostDTO> selectNewest(@RequestBody TCompanyNewstQuery tCompanyNewstQuery) {
        /* ValueOperations<String,CompanyCollerctPutInResume> valueOperations = redisTemplate.opsForValue();*/
        if (!StringUtils.isEmpty(tCompanyNewstQuery.getSalary())) {
            Map<String, BigDecimal> salary = this.salary(tCompanyNewstQuery.getSalary());
            tCompanyNewstQuery.setEndSalary(salary.get("endSalary"));
            tCompanyNewstQuery.setBeginSalary(salary.get("beginSalary"));
        }
        //清空工作经验筛选
        if ("不限".equals(tCompanyNewstQuery.getWorkExperience()) || "经验不限".equals(tCompanyNewstQuery.getWorkExperience())) {
            tCompanyNewstQuery.setWorkExperience("");
        }
        IPage<CompanyPostDTO> page = tCompanyPostService.listPost(tCompanyNewstQuery);
        if (!StringUtils.isEmpty(tCompanyNewstQuery.getJobhunter())) {
            List<CompanyPostDTO> records = page.getRecords();
            List<CompanyPostDTO> rd = new ArrayList<>(records.size());
            for (CompanyPostDTO record : records) {
                Boolean member = redisTemplate.opsForSet().isMember(Sole.INSERT_COLLERCT_POST.getKey() + tCompanyNewstQuery.getJobhunter(), record.getPostId());
/*
                if(member){*/
                record.setCollect(member);
                String resume = Sole.DELIVER_TJ.getKey() + record.getPostId() + tCompanyNewstQuery.getJobhunter();
                String s = (String) redisTemplate.opsForValue().get(resume);
                record.setHandInResume(!StringUtils.isEmpty(s));

                /*}else {
                    boolean collect = collerctPostApi.isCollect(tCompanyNewstQuery.getJobhunter(), record.getPostId());
                    if(collect){
                        redisTemplate.opsForSet().add(Sole.INSERT_COLLERCT_POST.getKey() + tCompanyNewstQuery.getJobhunter(), record.getPostId());
                    }
                    record.setCollect(collect);
                }*/
                rd.add(record);
            }
            page.setRecords(rd);
        }
        return R.success(page);
    }

    @GetMapping("/selectTCompanyPostPO")
    public TCompanyPostPO selectTCompanyPostPO(@RequestParam("postId") String postId) {
        TCompanyPost byId = tCompanyPostService.byId(postId);
        if(byId==null){
            return null;
        }
        TCompanyPostPO tCompanyPostPO = new TCompanyPostPO();
        BeanUtils.copyProperties(byId, tCompanyPostPO);
        return tCompanyPostPO;
    }


    public Map<String, BigDecimal> salary(String salary) {
        Map<String, BigDecimal> map = new ConcurrentHashMap();
        if ("1".equals(salary)) {
            map.put("beginSalary", new BigDecimal(10));
            map.put("endSalary", new BigDecimal(30));
        }
        if ("2".equals(salary)) {
            map.put("beginSalary", new BigDecimal(30));
            map.put("endSalary", new BigDecimal(50));
        }

        if ("3".equals(salary)) {
            map.put("beginSalary", new BigDecimal(50));
            map.put("endSalary", new BigDecimal(1000));
        }
        return map;
    }


    @GetMapping("/selectOne")
    @ApiOperation(value = "通过id获取岗位信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "postId", required = true, value = "岗位id"),
            @ApiImplicitParam(name = "companyId", required = true, value = "企业id"),
            @ApiImplicitParam(name = "jobHunterId", required = false, value = "求职者id")
    })
    @AutoIdempotent
    @Log(title = "通过id获取岗位信息", businessType = BusinessType.SELECT)
    public R<TCompanyAndPost> selectOnePost(@RequestParam("postId") String postId, @RequestParam("companyId") String companyId, @RequestParam(value = "jobHunterId", required = false) String jobHunterId) {
        if (StringUtils.isEmpty(postId)) {
            return R.fail("id不能为空");
        }
        TCompanyPost byId = tCompanyPostService.getById(postId);
        TCompanyAndPost tCompanyAndPost = new TCompanyAndPost();
        if (byId == null) {
            return R.success(null);
        }
        BeanUtils.copyProperties(byId, tCompanyAndPost);


        //条件判断
        if (byId.getPostType() == 0) {
            tCompanyAndPost.setPostNature(0);
        } else if (byId.getPostType() >= 1 && byId.getPostType() <= 3) {
            tCompanyAndPost.setPostNature(1);
        } else if (byId.getPostType() >= 4 && byId.getPostType() <= 5) {
            tCompanyAndPost.setPostNature(1);
            tCompanyAndPost.setPostNatureType(byId.getPostType() == 4 ? 0 : 1);
        }

        //获取赏金金额
      /*  QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("post_id", byId.getPostId());
        List list = headhunterPositionRecordService.list(queryWrapper);
        if (list.size() > 0) {
            tCompanyAndPost.setHeadhunterPositionRecord(list);
        }*/

        if (!StringUtils.isEmpty(jobHunterId)) {
            if ("TJ".equals(jobHunterId.substring(0, 2))) {
                CompanyCollerctPutInResume companyCollerctPutInResume = tCompanyApi.tCompanyById(companyId, jobHunterId, postId);
                tCompanyAndPost.setTcompany(companyCollerctPutInResume);
            } else if ("RM".equals(jobHunterId.substring(0, 2))) {
                /*CompanyCollerctPutInResume companyCollerctPutInResume = tCompanyApi.tCompanyById(companyId, jobHunterId, postId);
                tCompanyAndPost.setTcompany(companyCollerctPutInResume);*/
            }

        }

        return R.success(tCompanyAndPost);
    }


/*    public static void main(String[] args) {
        LocalDate date = LocalDate.now();
        LocalDate firstDay = LocalDate.parse("2021-11-11", DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        boolean before = date.isBefore(firstDay);
        boolean after = date.isAfter(firstDay);
        boolean before2 = date.isEqual(firstDay);
        // System.out.println(before);
        System.out.println(after);
        // System.out.println(before2);
    }*/

    @PostMapping("/refresh")
    @ApiOperation("刷新岗位")
    @ApiImplicitParam(name = "postId", required = true, value = "岗位id")
    @Log(title = "刷新岗位", businessType = BusinessType.SELECT)
    public R refresh(@RequestBody List<String> postId) {

        if (postId.size() < 0) {
            return R.fail("传入数据不能为空!");
        }
        UpdateWrapper updateWrapper = new UpdateWrapper();
        if (postId.size() > 0) {
            for (String s : postId) {
                updateWrapper.or();
                updateWrapper.eq("post_id", s);
            }
        }
        updateWrapper.eq("company_id", companyMetadata.userid());
        updateWrapper.set("update_time", LocalDateTime.now());
        boolean update = tCompanyPostService.update(updateWrapper);
        if (update) {
            return R.success("刷新成功!");
        }
        return R.fail("刷新失败!");
    }


    @PostMapping("/recommendSelect")
    @ApiOperation("接单大厅")
    @Log(title = "接单大厅", businessType = BusinessType.SELECT)
    public R<OrderReceivingDTO> recommendSelect(@Valid @RequestBody OrderReceivingQuery orderReceivingQuery) {
        IPage<OrderReceivingDTO> orderReceivingDTOIPage = null;

        if (orderReceivingQuery.getAnnualSalary() != null) {
            Map<String, BigDecimal> salary = CompensationUtil.salary(orderReceivingQuery.getAnnualSalary());
            orderReceivingQuery.setBeginSalary(salary.get("beginSalary"));
            orderReceivingQuery.setEndSalary(salary.get("endSalary"));
        }
        if (orderReceivingQuery.getPost() == 0 || orderReceivingQuery.getPost() == null) {
            orderReceivingDTOIPage = tCompanyPostService.recommendSelect(orderReceivingQuery, companyMetadata.userid());
        } else if (orderReceivingQuery.getPost() == 1) {
            orderReceivingDTOIPage = tCompanyPostService.recommendSelectB(orderReceivingQuery, companyMetadata.userid());
        }else if(orderReceivingQuery.getPost() == 2){
            orderReceivingDTOIPage = tCompanyPostService.recommendSelectC(orderReceivingQuery, companyMetadata.userid());
        }
        List<OrderReceivingDTO> records = orderReceivingDTOIPage.getRecords();
        TPayConfig tPayConfig = tPayConfigService.selectOne();
        ResumePaymentConfig campusRecruitment = resumePaymentConfigService.campusRecruitment();
        ResumePaymentConfig jobMarket = resumePaymentConfigService.jobMarket();
        for (OrderReceivingDTO record : records) {
            if(! StringUtils.isEmpty(orderReceivingQuery.getJobHunter())){
                String resume = Sole.SEND_A_RESUME.getKey() + record.getPostId();
                record.setSendAResume( redisUtils.sHasKey(resume,orderReceivingQuery.getJobHunter()));
            }

            if(record.getPostType()==1){
                String brokerage = DivideIntoUtil.brokerage(record.getBeginSalary(), record.getEndSalary(), tPayConfig.getEntryPayment(),tPayConfig.getInviteInteractionPostA());
                record.setBrokerage(brokerage);
            }else if(record.getPostType()==2){
                String brokerage = DivideIntoUtil.brokerage(record.getBeginSalary(), record.getEndSalary(), tPayConfig.getFullMoonPayment(),tPayConfig.getInviteInteractionPostA());
                record.setBrokerage(brokerage);
            }else if(record.getPostType()==3){
                String brokerage = DivideIntoUtil.brokerage(record.getBeginSalary(), record.getEndSalary(), tPayConfig.getInterviewPayment(),tPayConfig.getInviteInteractionPostA());
                record.setBrokerage(brokerage);
            }else if(record.getPostType()==4 ){

                if(record.getEducationId()==4){
                    record.setBrokerage(DivideIntoUtil.campusRecruitment(campusRecruitment.getGradeA(),campusRecruitment.getGradeD(),tPayConfig.getResumePay()));
                }else if(record.getEducationId()==5){
                    record.setBrokerage(DivideIntoUtil.campusRecruitment(campusRecruitment.getGradeB(),campusRecruitment.getGradeD(),tPayConfig.getResumePay()));
                }else if(record.getEducationId()==6){
                    record.setBrokerage(DivideIntoUtil.campusRecruitment(campusRecruitment.getGradeC(),campusRecruitment.getGradeD(),tPayConfig.getResumePay()));
                }else if(record.getEducationId()==7){
                    record.setBrokerage(DivideIntoUtil.campusRecruitment(campusRecruitment.getGradeD(),null,tPayConfig.getResumePay()));
                }
            }else if(record.getPostType()==5){
                record.setBrokerage(DivideIntoUtil.jobMarket(record.getBeginSalary(), record.getEndSalary(),tPayConfig.getResumePay(),jobMarket.getGradeA(),jobMarket.getGradeB(),jobMarket.getGradeC()));
            }
        }
        orderReceivingDTOIPage.setRecords(records);
        return R.success(orderReceivingDTOIPage);
    }


    /**
     * 计算
     */

    @GetMapping("/remind")
    @ApiOperation("岗位提醒")
    @ApiImplicitParam(name = "postId", required = true, value = "岗位id")
    @Log(title = "岗位提醒", businessType = BusinessType.UPDATE)
    public R remind(@RequestParam("postId") String postId) throws MessagingException {
        TCompanyPost byId = tCompanyPostService.getById(postId);
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setSubject("岗位审核提醒");
        helper.setText("公司名称为" + byId.getCompanyName() + "所发布" + byId.getPostLabel() + "岗位提醒你审核", true);//true参数代表前面的内容支持html
        //发送邮件时加单个附件,加多个附件多使用几个addAttachment方法
        helper.setFrom("2536321008@qq.com");
        helper.setTo("582248384@qq.com");
        mailSender.send(mimeMessage);
        return R.success();
    }


    @PostMapping("/home")
    @ApiOperation("首页获取岗位信息")
    /*@RateLimiter()*/
    @Log(title = "首页获取岗位信息", businessType = BusinessType.SELECT)
    public R<HomeDTO> home() {
    /*    ValueOperations<String,HomeDTO> valueOperations = redisTemplate.opsForValue();
        HomeDTO homeDTO = valueOperations.get(Sole.HOME.getKey());
        if(homeDTO==null){
            homeDTO=new HomeDTO();
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("audit", 2);
            queryWrapper.eq("status", 1);
            queryWrapper.orderByAsc("conceal");
            queryWrapper.orderByDesc("create_time");
            Page<TCompanyPost> page = tCompanyPostService.page(new Page<>(0, 4), queryWrapper);
            homeDTO.setLatestPost(page.getRecords());
            IPage<TCompanyHotPostDTO> tCompanyHotPostDTOIPage = tCompanyPostService.tCompanyHotPostDTOS(new Page<>(0, 12));
            homeDTO.setTCompanyHotPostDTOS(tCompanyHotPostDTOIPage.getRecords());
            IPage<TCompanyPost> companyPostIPage = tCompanyPostService.hotJob(new Page<>(0, 9));
            homeDTO.setHotJob(companyPostIPage.getRecords());
            if(homeDTO !=null){
                valueOperations.setIfAbsent(Sole.HOME.getKey(),homeDTO,1, TimeUnit.MINUTES);
            }
        }*/
        HomeDTO homeDTO = new HomeDTO();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("audit", 2);
        queryWrapper.eq("status", 1);
        //queryWrapper.orderByAsc("conceal");
        queryWrapper.orderByDesc("update_time");
        Page<TCompanyPost> page = tCompanyPostService.page(new Page<>(0, 5), queryWrapper);
        homeDTO.setLatestPost(page.getRecords());
        IPage<TCompanyHotPostDTO> tCompanyHotPostDTOIPage = tCompanyPostService.tCompanyHotPostDTOS(new Page<>(0, 12));
        homeDTO.setTCompanyHotPostDTOS(tCompanyHotPostDTOIPage.getRecords());
        IPage<TCompanyPost> companyPostIPage = tCompanyPostService.hotJob(new Page<>(0, 9));
        homeDTO.setHotJob(companyPostIPage.getRecords());
        return R.success(homeDTO);
    }


    @GetMapping("hotJob")
    @ApiOperation("热招职位")
    @ApiImplicitParams({

            @ApiImplicitParam(name = "pageSize", required = true),
            @ApiImplicitParam(name = "pageNo", required = true)
    })
    @Log(title = "热招职位", businessType = BusinessType.SELECT)
    public R<TCompanyPost> selectHotJob(@RequestParam("pageSize") Integer pageSize, @RequestParam("pageNo") Integer pageNo) {
        if (pageSize == null) {
            pageSize = 9;
        }
        if (pageNo == null) {
            pageNo = 0;
        }
        IPage<TCompanyPost> tCompanyPostIPage = tCompanyPostService.hotJob(new Page<>(pageNo, pageSize));
        return R.success(tCompanyPostIPage);
    }

    @GetMapping("tCompanyHotPostDTOS")
    @ApiOperation("热门企业")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageSize", required = true),
            @ApiImplicitParam(name = "pageNo", required = true)
    })
    @Log(title = "热门企业", businessType = BusinessType.SELECT)
    public R<TCompanyHotPostDTO> tCompanyHotPostDTOS(@RequestParam("pageSize") Integer pageSize, @RequestParam("pageNo") Integer pageNo) {
        if (pageSize == null) {
            pageSize = 12;
        }
        if (pageNo == null) {
            pageNo = 0;
        }
        IPage<TCompanyHotPostDTO> tCompanyHotPostDTOIPage = tCompanyPostService.tCompanyHotPostDTOS(new Page<>(pageNo, pageSize));
        return R.success(tCompanyHotPostDTOIPage);
    }


    /**
     * @param companyId
     * @return null
     * @Description: 获取6个月发布岗位的数量
     * @Author tangzhuo
     * @CreateTime 2023/1/14 10:55
     */

    @GetMapping("/postCount")
    public int postCount(@RequestParam("companyId") String companyId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("company_id", companyId);
        queryWrapper.ge("create_time", DateUtils.getTimeMonth(-6));
        return tCompanyPostService.count(queryWrapper);
    }

    /**
     * @param companyId:企业id
     * @return null
     * @Description: 获取该公司的在线职位
     * @Author tangzhuo
     * @CreateTime 2023/1/16 8:14
     */


/*    @GetMapping("/onLinePost")
    public Integer onLinePost(String companyId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("status", 1);
        queryWrapper.eq("audit", 2);
        queryWrapper.eq("company_id", companyId);
        int count = tCompanyPostService.count(queryWrapper);
        return count;
    }*/
    @GetMapping("/onLinePost")
    public Integer onLinePost(String companyId, Integer type) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("status", 1);
        queryWrapper.eq("audit", 2);
        queryWrapper.eq("company_id", companyId);
        if (type == 1) {
            queryWrapper.ne("post_type", 0);
        }
        int count = tCompanyPostService.count(queryWrapper);
        return count;
    }

    @GetMapping("/hot")
    @ApiOperation("获取热门职位")
    @Log(title = "获取热门职位", businessType = BusinessType.SELECT)
    public R hot() {
        List<String> list = tCompanyPostService.hot();
        List<String> list1 = new ArrayList<>();
        for (String s : list) {
            int length = s.length();
            list1.add(s.substring(1, length - 1));
        }
        return R.success(list1);
    }


    /**
     * @param
     * @return null
     * @Description: 获取近1周的职位
     * @Author tangzhuo
     * @CreateTime 2023/2/14 11:07
     */

    @PostMapping("/latestPost/{companyId}")
    @ApiOperation("公司最新职位")
    @ApiImplicitParam(name = "companyId", required = true, value = "企业id")
    @Log(title = "公司最新职位", businessType = BusinessType.SELECT)
    public R<LatestPostPO> latestPost(@PathVariable("companyId") String companyId, @RequestBody PageQuery pageQuery) {
        IPage<LatestPostPO> postPOIPage = tCompanyPostService.latestPost(companyId, pageQuery);
        return R.success(postPOIPage);
    }

    @PostMapping("/openForPositions")
    @ApiOperation("在招职位")
    @Log(title = "在招职位", businessType = BusinessType.SELECT)
    public R<PostCollectDTO> openForPositions(@RequestBody PositionQuery positionQuery) {
        if (positionQuery.getAnnualSalary() != null) {
            Map<String, BigDecimal> salary = CompensationUtil.salary(positionQuery.getAnnualSalary());
            positionQuery.setBeginSalary(salary.get("beginSalary"));
            positionQuery.setEndSalary(salary.get("endSalary"));
        }
        List<Integer> workExperience = positionQuery.getWorkExperience();
        if (workExperience.size() > 0) {
            for (int i = 0; i < workExperience.size(); i++) {
                if (workExperience.get(i) == 0 || workExperience.get(i) == null) {
                    workExperience.remove(i);
                }
            }
            positionQuery.setWorkExperience(workExperience);
        }
        IPage<PostCollectDTO> page = tCompanyPostService.openForPositions(positionQuery);
        if (!StringUtils.isEmpty(positionQuery.getJobHunterId())) {
            List<PostCollectDTO> records = page.getRecords();
            int pageSize = Math.toIntExact(positionQuery.getPageSize());
            List<PostCollectDTO> arrayList = new ArrayList(pageSize);
            for (PostCollectDTO record : records) {
                boolean collect = collerctPostApi.isCollect(positionQuery.getJobHunterId(), record.getPostId());
                record.setCollect(collect);
                arrayList.add(record);
            }
            page.setRecords(arrayList);
        }
        return R.success(page);
    }


    @GetMapping("/jobType/{companyId}")
    @ApiOperation("该公司发布岗位现有职位类型")
    @Log(title = "该公司发布岗位现有职位类型", businessType = BusinessType.SELECT)
    @ApiImplicitParam(name = "companyId", required = true, value = "企业id")
    public R<ArrayList<String>> jobType(@PathVariable("companyId") String companyId) {
        List<String> list = tCompanyPostService.jobType(companyId);
        List<String> list1 = new ArrayList<>();
        for (String s : list) {
            if (StringUtils.isEmpty(s) || s.equals("null") || "\"\"".equals(s)) {
                continue;
            }
            String substring = s.substring(1, s.length() - 1);
            list1.add(substring);
        }
        return R.success(list1);
    }

 /*   @GetMapping("/JobTitle/{companyId}")
    @ApiOperation("该公司的岗位名称")
    @ApiImplicitParam(name = "companyId", required = true, value = "企业id")
    public R<ArrayList<String>> JobTitle(@PathVariable("companyId") String companyId) {
        List<String> list = tCompanyPostService.JobTitle(companyId);
        List<String> list1 = new ArrayList<>();
        for (String s : list) {
            if (s.equals("null") || StringUtils.isEmpty(s) || "\"\"".equals(s)) {
                continue;
            }
            String substring = s.substring(1, s.length() - 1);
            list1.add(substring);
        }
        return R.success(list1);
    }
*/

    @PostMapping("/position")
    @ApiOperation(value = "职位中心")
    @Log(title = "职位中心", businessType = BusinessType.SELECT)
    public R<PostShare> selectPosition(@RequestBody PostShareQuery postShareQuery) {
        String userid = companyMetadata.userid();
        //String userid="TC2305091633270-3";
        String substring = userid.substring(0, 2);
        if ("TC".equals(substring)) {
            postShareQuery.setCompanyId(userid);
        }
        IPage<PostShare> iPage = tCompanyPostService.selectPosition(postShareQuery.createPage(), postShareQuery);
        List<PostShare> records = iPage.getRecords();
        for (PostShare record : records) {
            if (record.getPostType() == 0) {
                record.setPostNature(0);
            } else if (record.getPostType() >= 1 && record.getPostType() <= 3) {
                record.setPostNature(1);
            } else if (record.getPostType() >= 4 && record.getPostType() <= 5) {
                record.setPostNature(1);
                record.setPostNatureType(record.getPostType() == 4 ? 0 : 1);
            }
        }
        if (!CollectionUtils.isEmpty(records)) {
            List<String> postId = records.stream().map(record -> record.getPostId()).collect(Collectors.toList());
            List<PostShare> lists = orderReceivingService.getCount(postId);

            records.stream().map(record -> {
                return lists.stream().filter(list -> {
                    //条件判断
                    //  return true;
                    return record.getPostId().equals(list.getPostId());
                }).map(list -> {
                    record.setOrderReceiving(list.getOrderReceiving());
                    return record;                            //返回的结果
                }).collect(Collectors.toList());
            }).flatMap(List::stream).collect(Collectors.toList());//设置返回结果类型
            // iPage.setRecords(postShareList);
        }

        return R.success(iPage);
    }

    @GetMapping("/overhead")
    @ApiOperation(value = "职位顶置")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyId", required = true, value = "企业id"),
            @ApiImplicitParam(name = "postId", required = true, value = "岗位id")
    })
    @Log(title = "职位顶置", businessType = BusinessType.UPDATE)
    public R overhead(@RequestParam("postId") String postId, @RequestParam("companyId") String companyId) {
        if (StringUtils.isEmpty(postId) && StringUtils.isEmpty(companyId)) {
            return R.fail("传入参数不能为空!");
        }
        String userid = companyMetadata.userid();
        if (!userid.equals(companyId)) {
            return R.fail("账号不一致!");
        }
        TCompanyPost byId = tCompanyPostService.getById(postId);
        if (byId == null) {
            return R.fail("该岗位不存在!");
        }
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("company_id", companyId);
        updateWrapper.eq("post_id", postId);
        updateWrapper.set("top", byId.getTop() == 0 ? 1 : 0);
        boolean update = tCompanyPostService.update(updateWrapper);
        if (update) {
            return R.success("操作成功!");
        }
        return R.fail("操作成功!");
    }


    @PostMapping("/jobSearch")
    @ApiOperation("职位")
    @Log(title = "职位", businessType = BusinessType.SELECT)
    public R<CompanyPostDTO> jobSearch(@RequestBody JobSearchQuery jobSearchQuery) {
        if (jobSearchQuery.getAnnualSalary() != null) {
            Map<String, BigDecimal> salary = CompensationUtil.salary(jobSearchQuery.getAnnualSalary());
            jobSearchQuery.setBeginSalary(salary.get("beginSalary"));
            jobSearchQuery.setEndSalary(salary.get("endSalary"));
        }
        //清空工作经验筛选
        jobSearchQuery.setCompanyName(jobSearchQuery.getCompanyName().trim());
        IPage<CompanyPostDTO> page = tCompanyPostService.jobSearch(jobSearchQuery);
        if (page.getRecords().size() == 0) {
            String companyName = jobSearchQuery.getCompanyName();
            jobSearchQuery.setPosition(new ArrayList<String>(Arrays.asList(companyName)));
            jobSearchQuery.setCompanyName("");
            page = tCompanyPostService.jobSearch(jobSearchQuery);
        }
        if (!StringUtils.isEmpty(jobSearchQuery.getJobHunter())) {
            List<CompanyPostDTO> records = page.getRecords();
            List<CompanyPostDTO> rd = new ArrayList<>(records.size());
            for (CompanyPostDTO record : records) {
                Boolean member = redisUtils.sHasKey(Sole.INSERT_COLLERCT_POST.getKey() + jobSearchQuery.getJobHunter(), record.getPostId());
/*
                if(member){*/
                record.setCollect(member);
                String resume = Sole.DELIVER_TJ.getKey() + record.getPostId() + jobSearchQuery.getJobHunter();
                String s = (String) redisTemplate.opsForValue().get(resume);
                record.setHandInResume(!StringUtils.isEmpty(s));

                /*}else {
                    boolean collect = collerctPostApi.isCollect(tCompanyNewstQuery.getJobhunter(), record.getPostId());
                    if(collect){
                        redisTemplate.opsForSet().add(Sole.INSERT_COLLERCT_POST.getKey() + tCompanyNewstQuery.getJobhunter(), record.getPostId());
                    }
                    record.setCollect(collect);
                }*/
                rd.add(record);
            }
            page.setRecords(rd);
        }
        return R.success(page);
    }

    @GetMapping("/sdsadas")
    /**
     * 更新一下岗位的字段 例如 博士后——>4
     */
    /* @ApiOperation("sdsadas")*/
    public void test() {
        List<TCompanyPost> list = tCompanyPostService.list();
        for (TCompanyPost tCompanyPost : list) {
            tCompanyPost.setEducationId(EducationUtil.screen(tCompanyPost.getEducationType()));
            tCompanyPost.setWorkExperienceId(WorkExperienceUtil.screenOut(tCompanyPost.getWorkExperience()));
            tCompanyPost.setCityid("");
            String key = Cache.CACHE_CITY.getKey();

            if (!StringUtils.isEmpty(tCompanyPost.getWorkCity().getCity())) {
                String o = redisUtils.get(key + tCompanyPost.getWorkCity().getCity()) + "";
                if (!StringUtils.isEmpty(o)) {
                    tCompanyPost.setCityid(o);
                }

            }
            tCompanyPost.setProvinceid(0);
            if (!StringUtils.isEmpty(tCompanyPost.getWorkCity().getProvince())) {
                Integer o = Integer.getInteger(redisUtils.get(key + tCompanyPost.getWorkCity().getProvince()) + "");
                if (!StringUtils.isEmpty(o)) {
                    tCompanyPost.setProvinceid(o);
                }
            }
            boolean b = tCompanyPostService.updateById(tCompanyPost);
        }
    }

    @GetMapping("/scale")
    @ApiOperation("修改规模")
    @Log(title = "修改规模", businessType = BusinessType.UPDATE)
    public void scale() {
        List<TCompanyPost> list = tCompanyPostService.list();
        for (TCompanyPost tCompanyPost : list) {
            TCompany tCompany = tCompanyApi.selectId(tCompanyPost.getCompanyId());
            if (tCompany != null) {
                tCompanyPost.setFirmSize(ScaleUtil.screen(tCompany.getFirmSize()));
                tCompanyPost.setCompanyTypeId(tCompany.getCompanyTypeId());
                tCompanyPost.setCustomLogo(tCompany.getCustomLogo());
                boolean b = tCompanyPostService.updateById(tCompanyPost);
            }
        }

    }

    @GetMapping("/list")
    public List<TCompanyPostRecord> list() {
        Object qwe = redisUtils.get("qwe");
        List<TCompanyPostRecord> list=null;
        if(qwe==null){
           list = tCompanyPostService.list1();
            redisUtils.set("qwe",JSON.toJSONString(list),1000);
        }
        list=JSON.parseObject(qwe.toString(), List.class);
        return list;
    }

    @GetMapping("/postLabel")
    @ApiOperation("获取该公司的所有岗位名称")
    @Log(title = "获取该公司的所有岗位名称", businessType = BusinessType.SELECT)
    @ApiImplicitParam(name = "keyword", required = false, value = "关键字")
    public R<List<PostLabel>> postLabel(@RequestParam(value = "keyword", required = false) String keyword) {
        String userid = companyMetadata.userid();
        List<PostLabel> list = tCompanyPostService.postLabel(userid, keyword);
        return R.success(list);
    }

    @GetMapping("/firmPost")
    @ApiOperation("搜索关键字")
    @ApiImplicitParam(name = "keyword", required = true, value = "关键字")
    @Log(title = "搜索关键字", businessType = BusinessType.SELECT)
    public R<List<String>> firmPost(@RequestParam(value = "keyword", required = true) String keyword) {
        keyword = keyword.trim();
        List<String> listCache = (List<String>) redisUtils.get(FirmPost.FIRM_POST.getType() + keyword);
        if (listCache != null && listCache.size() > 0) {
            return R.success(listCache);
        }
        List<String> list = tCompanyPostService.firm(keyword);
        if (list.size() < 10) {
            List<String> list1 = tCompanyPostService.post(keyword, 10 - list.size());
            list.addAll(list1);
        }
        redisUtils.set(FirmPost.FIRM_POST.getType() + keyword, list, 60 * 5);
        return R.success(list);
    }

    /**
     * 获取岗位的基本信息
     *
     * @param postId
     * @return
     */
    @GetMapping("/postRecord")
    public TCompanyPostRecord postRecord(@RequestParam("postId") String postId) {
        return tCompanyPostService.postRecord(postId);
    }

    @GetMapping("/postAmend")
    @ApiOperation(value = "判断岗位是否能修改")
    @Log(title = "判断岗位是否能修改", businessType = BusinessType.SELECT)
    public R postAmend(@RequestParam("postId") String postId) {
        Boolean byId = tCompanyPostService.postAmend(postId);
        Map map = new HashMap(2);
        if (byId == null) {
            map.put("amend", false);
            map.put("msg", "岗位不存在!");
            return R.success(map);
        }
        if (byId == false) {
            map.put("amend", false);
            map.put("msg", "该岗位有投简记录没有处理!");
            return R.success(map);
        }
        if (byId == true) {
            map.put("amend", true);
            map.put("msg", "");
            return R.success(map);
        }
        map.put("amend", true);
        map.put("msg", "");
        return R.success(map);
    }

    /*
     * 更新岗位类型
     *
     * */
    @Autowired
    private TPositionService positionService;

    @GetMapping("/updatePosition")
    @ApiOperation(value = "更新岗位类型")
    @Log(title = "更新岗位类型", businessType = BusinessType.UPDATE)
    public String updatePosition() {
        List<TCompanyPost> list = tCompanyPostService.list();
        for (TCompanyPost tCompanyPost : list) {
            List<String> position = tCompanyPost.getPosition();
            List list1 = null;
            UpdateWrapper updateWrapper = null;
            if (position == null) {
                continue;
            }
            if (position.size() == 1) {
                list1 = new ArrayList();
                String s = position.get(0);
                if (s == null || "其他".equals(s)) {
                    continue;
                }
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("name", s);
                queryWrapper.eq("rank", 2);
                TPosition one = positionService.getOne(queryWrapper);
                if (one == null) {
                    continue;
                }

                QueryWrapper queryWrapper1 = new QueryWrapper();
                queryWrapper1.eq("code", one.getFatherCode());
                queryWrapper1.eq("rank", 1);
                TPosition one1 = positionService.getOne(queryWrapper1);

                QueryWrapper queryWrapper2 = new QueryWrapper();
                queryWrapper2.eq("code", one1.getFatherCode());
                queryWrapper1.eq("rank", 0);
                TPosition one2 = positionService.getOne(queryWrapper2);
                list1.add(one2.getName());
                list1.add(one1.getName());
                list1.add(one.getName());
                updateWrapper = new UpdateWrapper();
                updateWrapper.eq("post_id", tCompanyPost.getPostId());
                updateWrapper.set("position", JSON.toJSONString(list1));
                tCompanyPostService.update(updateWrapper);
            }
        }
        return "ok";
    }

    @GetMapping("/selectPostId/{postId}")
    @ApiOperation("通过岗位id获取岗位信息")
    @ApiImplicitParam(name = "postId", required = true, value = "岗位id")
    @Log(title = "通过岗位id获取岗位信息", businessType = BusinessType.SELECT)
    public R<TCompanyAndPost> selectPostId(@PathVariable("postId") String postId) {
        if (StringUtils.isEmpty(postId)) {
            return R.fail("id不能为空");
        }
        TCompanyPost byId = tCompanyPostService.getById(postId);
        TCompanyAndPost tCompanyAndPost = new TCompanyAndPost();
        if (byId == null) {
            return R.success(null);
        }
        BeanUtils.copyProperties(byId, tCompanyAndPost);
        return R.success(tCompanyAndPost);
    }

    @GetMapping("/rmPostId/{postId}")
    @ApiOperation("推荐官获取职位信息")
    @ApiImplicitParam(name = "postId", required = true, value = "岗位id")
    @Log(title = "推荐官获取职位信息", businessType = BusinessType.SELECT)
    public R<TCompanyAndPost> rmPostId(@PathVariable("postId") String postId) {
        if (StringUtils.isEmpty(postId)) {
            return R.fail("id不能为空");
        }
        TCompanyPost byId = tCompanyPostService.getById(postId);
        TCompanyAndPost tCompanyAndPost = new TCompanyAndPost();
        if (byId == null) {
            return R.success(null);
        }
        BeanUtils.copyProperties(byId, tCompanyAndPost);
        if ("TR".equals(companyMetadata.userid().substring(0, 2))) {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("recommerd", companyMetadata.userid());
            queryWrapper.eq("post_id", postId);
            OrderReceiving one = orderReceivingService.getOne(queryWrapper);
            if (one != null) {
                tCompanyAndPost.setCollect(one.getCollect());
                tCompanyAndPost.setCancel(one.getCancel());
            }
        }
        TPayConfig tPayConfig = tPayConfigService.selectOne();
        ResumePaymentConfig campusRecruitment = resumePaymentConfigService.campusRecruitment();
        ResumePaymentConfig jobMarket = resumePaymentConfigService.jobMarket();
        if(byId.getPostType()==1){
            String brokerage = DivideIntoUtil.brokerage(byId.getBeginSalary(), byId.getEndSalary(), tPayConfig.getEntryPayment(),tPayConfig.getInviteInteractionPostA());
            tCompanyAndPost.setBrokerage(brokerage);
        }else if(byId.getPostType()==2){
            String brokerage = DivideIntoUtil.brokerage(byId.getBeginSalary(), byId.getEndSalary(), tPayConfig.getFullMoonPayment(),tPayConfig.getInviteInteractionPostA());
            tCompanyAndPost.setBrokerage(brokerage);
        }else if(byId.getPostType()==3){
            String brokerage = DivideIntoUtil.brokerage(byId.getBeginSalary(), byId.getEndSalary(), tPayConfig.getInterviewPayment(),tPayConfig.getInviteInteractionPostA());
            tCompanyAndPost.setBrokerage(brokerage);
        }else if(byId.getPostType()==4 ){

            if(byId.getEducationId()==4){
                tCompanyAndPost.setBrokerage(DivideIntoUtil.campusRecruitment(campusRecruitment.getGradeA(),campusRecruitment.getGradeD(),tPayConfig.getResumePay()));
            }else if(byId.getEducationId()==5){
                tCompanyAndPost.setBrokerage(DivideIntoUtil.campusRecruitment(campusRecruitment.getGradeB(),campusRecruitment.getGradeD(),tPayConfig.getResumePay()));
            }else if(byId.getEducationId()==6){
                tCompanyAndPost.setBrokerage(DivideIntoUtil.campusRecruitment(campusRecruitment.getGradeC(),campusRecruitment.getGradeD(),tPayConfig.getResumePay()));
            }else if(byId.getEducationId()==7){
                tCompanyAndPost.setBrokerage(DivideIntoUtil.campusRecruitment(campusRecruitment.getGradeD(),null,tPayConfig.getResumePay()));
            }
        }else if(byId.getPostType()==5){
            tCompanyAndPost.setBrokerage(DivideIntoUtil.jobMarket(byId.getBeginSalary(), byId.getEndSalary(),tPayConfig.getResumePay(),jobMarket.getGradeA(),jobMarket.getGradeB(),jobMarket.getGradeC()));
        }
        return R.success(tCompanyAndPost);
    }


    /**
     * 判断职位是否在线
     *
     * @param postId
     * @return
     */
    @GetMapping("/onLine")
    public Boolean onLine(@RequestParam("postId") String postId) {
        TCompanyPost byId = tCompanyPostService.byId(postId);
        if(byId==null){
            return false;
        }
        Integer status = byId.getStatus();
        return status == 0 ? false : true;
    }

    @GetMapping("/getCompanyName")
    public List<Map<String, Object>> getCompanyName(@RequestParam("list") List<String> list) {
        List<Map<String, Object>> list1 = tCompanyPostService.companyName(list);
        return list1;
    }


    @GetMapping("/schoolyard")
    @ApiOperation("校园查询")
    @ApiImplicitParams({
                    @ApiImplicitParam(name = "keyword", required = false, value = "关键字"),
                    @ApiImplicitParam(name = "pageSize", required = true),
                    @ApiImplicitParam(name = "pageNo", required = true)
            })
    @Log(title = "校园查询", businessType = BusinessType.SELECT)
    public R<SchoolyardDTO> schoolyard(@RequestParam(value = "keyword", required = false) String keyword,@RequestParam(value = "pageSize") Integer pageSize,@RequestParam(value = "pageNo") Integer pageNo) {
        IPage<SchoolyardDTO> iPage = tCompanyPostService.selectCompanyId(new Page(pageNo, pageSize), keyword);
        List<SchoolyardDTO> records = iPage.getRecords();
        List<String> stringList=null;
        if (records != null && records.size() > 0) {
             stringList = new ArrayList<>(records.size());
            for (SchoolyardDTO record : records) {
                stringList.add(record.getCompanyId());
            }
            List<TCompany> tCompanies = tCompanyApi.companyLists(stringList);
            for (SchoolyardDTO record : records) {
                for (TCompany tCompany : tCompanies) {
                    if(tCompany.getCompanyId().equals(record.getCompanyId())){
                        BeanUtils.copyProperties(tCompany,record);
                        continue;
                    }
                }
            }
        }
        if(stringList !=null){
            List<TCompanyPost> tCompanyPostList = tCompanyPostService.selectPostName(stringList);
                if(tCompanyPostList !=null &&  tCompanyPostList.size()>0){
                    for (SchoolyardDTO record : records) {
                        for (TCompanyPost tCompanyPost : tCompanyPostList) {
                            if(tCompanyPost.getCompanyId().equals(record.getCompanyId())){
                                List<Map> postName = record.getPostName();
                                if(postName ==null ){
                                    postName=new ArrayList<>();
                                }
                                Map map=new HashMap(2);
                                map.put("postLabel",tCompanyPost.getPostLabel());
                                map.put("postId",tCompanyPost.getPostId());
                                postName.add(map);
                                record.setPostName(postName);
                                Set<String> postWealId = record.getPostWealId();
                                if(postWealId ==null ){
                                    postWealId=new HashSet<>();
                                }
                                postWealId.addAll(tCompanyPost.getPostWealId());
                                record.setPostWealId(postWealId);
                            }
                        }
                    }
                }
        }

        iPage.setRecords(records);
        // IPage<SchoolyardDTO> schoolyardDTOIPage = tCompanyPostService.schoolyard(keyword);
        return R.success(iPage);
    }

/*
    @GetMapping("/keywordSchoolyard")
    @ApiOperation("校园查询搜索关键字")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", required = false, value = "关键字"),
            @ApiImplicitParam(name = "pageSize", required = true),
            @ApiImplicitParam(name = "pageNo", required = true)
    })
    public R<List<String>> keywordSchoolyard(@RequestParam(value = "keyword") String keyword, Integer pageSize, Integer pageNo) {
        List<String> list= tCompanyPostService.keywordSchoolyardCompany(new Page(pageNo, pageSize),keyword);
        return R.success(list);
    }
*/

    @GetMapping("/countSchoolyard")
    @ApiOperation("获取校园职位及企业招聘数量")
    @Log(title = "获取校园职位及企业招聘数量", businessType = BusinessType.SELECT)
    public R countSchoolyard(){
        Map map=new HashMap(2);
        LambdaQueryWrapper<TCompanyPost> lambdaQueryWrapper=new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(TCompanyPost::getStatus,1);
        lambdaQueryWrapper.ne(TCompanyPost::getCompanyType,2);
        lambdaQueryWrapper.and(i->i.like(TCompanyPost::getPostLabel,"应届生").or().like(TCompanyPost::getPostLabel,"实习生").or().eq(TCompanyPost::getWorkExperienceId,0).or().eq(TCompanyPost::getWorkExperienceId,2).or().eq(TCompanyPost::getJobCategory,"校园"));
        int count = tCompanyPostService.count(lambdaQueryWrapper);
        map.put("post",count);
       // lambdaQueryWrapper.groupBy(TCompanyPost::getCompanyId);
        IPage<SchoolyardDTO> iPage = tCompanyPostService.selectCompanyId(new Page(0, 1), null);
        map.put("company",iPage.getTotal());
        return R.success(map);
    }


    @GetMapping("/selectPostType")
    @ApiOperation("获取已有发布的职位的类型")
    @Log(title = "获取已有发布的职位的类型", businessType = BusinessType.SELECT)
    @ApiImplicitParam(name = "state", required = true,value ="(0在线中 1:未上线 2审核中 3:未通过)" )
    public R<List> selectPostType(@RequestParam("state") Integer state){
       List<Integer> list= tCompanyPostService.selectPostType(companyMetadata.userid(),state);
        if(list ==null){
            list=new ArrayList<>();
        }
        list.add(6);
       return R.success(list);

    }


    @GetMapping("/workPlace")
    @ApiOperation("获取已发布职位的工作地址")
    @ApiImplicitParam(name = "id", required = true,value ="企业id" )
    @Log(title = "获取已发布职位的工作地址", businessType = BusinessType.SELECT)
    public R workPlace(@RequestParam("id") String id){
      List<String> workPlaceList=  tCompanyPostService.workPlace(id);
      List<String> replace = new ArrayList<>(workPlaceList.size());
        for (String s : workPlaceList) {
            System.out.println(s);
            s=s.replace("\"","");
            replace.add(s);

        }
      return R.success(replace);
    }


    @GetMapping("/basic")
    @ApiOperation("通过岗位id获取岗位基础信息")
    @ApiImplicitParam(name = "postId", required = true,value ="岗位id" )
    @Log(title = "通过岗位id获取岗位基础信息", businessType = BusinessType.SELECT)
    public R<CompanyPostDTO> basicInformation(@RequestParam("postId") String postId){
        String companyPostDTO=tCompanyPostService.basic(postId);
        ResumePaymentConfig campusRecruitment = resumePaymentConfigService.campusRecruitment();
        ResumePaymentConfig jobMarket = resumePaymentConfigService.jobMarket();
        TPayConfig tPayConfig = tPayConfigService.selectOne();
        if(! StringUtils.isEmpty(companyPostDTO)){
            CompanyPostDTO record = JSON.parseObject(companyPostDTO, CompanyPostDTO.class);
            if(record.getPostType()==1){
                String brokerage = DivideIntoUtil.brokerage(record.getBeginSalary(), record.getEndSalary(), tPayConfig.getEntryPayment(),tPayConfig.getInviteInteractionPostA());
                record.setBrokerage(brokerage);
            }else if(record.getPostType()==2){
                String brokerage = DivideIntoUtil.brokerage(record.getBeginSalary(), record.getEndSalary(), tPayConfig.getFullMoonPayment(),tPayConfig.getInviteInteractionPostA());
                record.setBrokerage(brokerage);
            }else if(record.getPostType()==3){
                String brokerage = DivideIntoUtil.brokerage(record.getBeginSalary(), record.getEndSalary(), tPayConfig.getInterviewPayment(),tPayConfig.getInviteInteractionPostA());
                record.setBrokerage(brokerage);
            }else if(record.getPostType()==4 ){

                if(record.getEducationId()==4){
                    record.setBrokerage(DivideIntoUtil.campusRecruitment(campusRecruitment.getGradeA(),campusRecruitment.getGradeD(),tPayConfig.getResumePay()));
                }else if(record.getEducationId()==5){
                    record.setBrokerage(DivideIntoUtil.campusRecruitment(campusRecruitment.getGradeB(),campusRecruitment.getGradeD(),tPayConfig.getResumePay()));
                }else if(record.getEducationId()==6){
                    record.setBrokerage(DivideIntoUtil.campusRecruitment(campusRecruitment.getGradeC(),campusRecruitment.getGradeD(),tPayConfig.getResumePay()));
                }else if(record.getEducationId()==7){
                    record.setBrokerage(DivideIntoUtil.campusRecruitment(campusRecruitment.getGradeD(),null,tPayConfig.getResumePay()));
                }
            }else if(record.getPostType()==5){
                record.setBrokerage(DivideIntoUtil.jobMarket(record.getBeginSalary(), record.getEndSalary(),tPayConfig.getResumePay(),jobMarket.getGradeA(),jobMarket.getGradeB(),jobMarket.getGradeC()));
            }
            return R.success(record);
        }
        return R.fail("未查到该职位！");
    }

}
