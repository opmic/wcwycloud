package com.wcwy.post.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wawy.company.api.TRecommendApi;
import com.wcwy.common.base.enums.Gold;
import com.wcwy.common.base.enums.Lock;
import com.wcwy.common.base.result.R;
import com.wcwy.common.base.utils.*;
import com.wcwy.common.redis.enums.Cache;
import com.wcwy.common.redis.enums.RedisCache;
import com.wcwy.common.redis.util.IDGenerator;
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.common.web.annotation.AutoIdempotent;
import com.wcwy.company.entity.TRecommend;
import com.wcwy.post.asyn.UpdatePostRecordAsync;
import com.wcwy.post.dto.PostShare;
import com.wcwy.post.dto.TRecommendPostDTO;
import com.wcwy.post.entity.*;
import com.wcwy.post.produce.EiCompanyPostProduce;
import com.wcwy.post.produce.GoldProduce;
import com.wcwy.post.service.*;
import com.wcwy.post.session.CompanyMetadata;
import com.wcwy.post.vo.SaveRecommendPostVO;
import com.wcwy.system.annotation.Log;
import com.wcwy.system.enums.BusinessType;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * ClassName: TRecommendPostController
 * Description:
 * date: 2023/6/15 16:46
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@RestController
@Api(tags = "自营岗位接口")
@RequestMapping("/recommendPost")
@Slf4j
public class TRecommendPostController {
    @Autowired
    private UpdatePostRecordAsync updatePostRecordAsync;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private IDGenerator idGenerator;
    @Autowired
    private CompanyMetadata companyMetadata;
    @Autowired
    private RecommendBasicsService recommendBasicsService;
    @Autowired
    private TCompanyPostService tCompanyPostService;
    @Autowired
    private GoldProduce goldProduce;
    @Autowired
    private TPostShareService tPostShareService;
    @Autowired
    private TRecommendApi tRecommendApi;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private OrderReceivingService orderReceivingService;
    @Autowired
    private EiCompanyPostProduce eiCompanyPostProduce;

    @PostMapping("/insert")
    @ApiOperation(value = "添加自营岗位")
    @Transactional(rollbackFor = Exception.class)
    @Log(title = "添加自营岗位", businessType = BusinessType.SELECT)
    @AutoIdempotent
    public R insert(@Valid @RequestBody SaveRecommendPostVO saveRecommendPostVO) throws Exception {

    //先判断是否机构认证
  /*      if(StringUtils.isEmpty(tRecommend.getRealName())){
            RecommendBasics byId = recommendBasicsService.getById(companyMetadata.userid());
            if (byId == null || byId.getAdministrator() == 0) {
                return R.fail("请使用猎企认证账号或实名认证账号!");
            }
            if (byId.getAdministrator() == 1) {
                return R.fail("猎企认证在审核中！");
            }
        }*/
        TRecommend tRecommend=null;
        Integer individualOrTeam=1;
 /*       tRecommend = tRecommendApi.selectId(companyMetadata.userid());
        if(tRecommend !=null){
            if(tRecommend.getAdministrator()==0){
                if(StringUtils.isEmpty(tRecommend.getRealName())){
                    return R.fail("请使用猎企认证账号或实名认证账号!");
                }else {
                    //个人发布做标识
                    individualOrTeam=1;
                }
            }else if(tRecommend.getAdministrator()==1){
                return R.fail("猎企认证在审核中！");
            }
        }else {
            return R.fail("服务器错误！");
        }*/
        boolean verify = PhoneEmailUtils.verify(saveRecommendPostVO.getDescription());
        if(verify){
            return R.fail("职位详情不能存在联系方式！");
        }
        Boolean aBoolean = DateUtils.exceedDate(saveRecommendPostVO.getExpirationDate());
        if (!aBoolean) {
            return R.fail("截止日期不能大于一年！");
        }
        RLock lock = redissonClient.getLock(Lock.INSERT_PT.getLockSion() + companyMetadata.userid());
        boolean isLock = lock.tryLock(5, 10, TimeUnit.SECONDS);
        try {
            if (isLock) {
        /*        QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("post_label", saveRecommendPostVO.getPostLabel().trim());
                queryWrapper.eq("company_id", companyMetadata.userid());
                int count = tCompanyPostService.count(queryWrapper);
                if (count > 0) {
                    return R.fail("已有相同的职位名称，请修改后重新发布!");
                }*/
                TCompanyPost tCompanyPost = new TCompanyPost();

                BeanUtils.copyProperties(saveRecommendPostVO, tCompanyPost);
                tCompanyPost.setPostId(idGenerator.generateCode("PS"));
                tCompanyPost.setDeleted(0);
                tCompanyPost.setCompanyId(companyMetadata.userid());
                tCompanyPost.setCreateTime(LocalDateTime.now());
                tCompanyPost.setUpdateTime(LocalDateTime.now());
                tCompanyPost.setUpdateId(companyMetadata.userid());
                tCompanyPost.setCreateId(companyMetadata.userid());
                tCompanyPost.setStatus(1);
                tCompanyPost.setDayTime(LocalDateTime.now());
                tCompanyPost.setIndividualOrTeam(individualOrTeam);
                tCompanyPost.setFirmSize(ScaleUtil.screen(saveRecommendPostVO.getFirmSize()));
                tCompanyPost.setEducationId(EducationUtil.screen(saveRecommendPostVO.getEducationType()));
                tCompanyPost.setCompanyType(2);
                tCompanyPost.setPostType(0);
                tCompanyPost.setLogo("https://dev-1313175594.cos.ap-guangzhou.myqcloud.com/logo/logo.png");
                tCompanyPost.setWorkExperienceId(WorkExperienceUtil.screenOut(saveRecommendPostVO.getWorkExperience()));
                String key = Cache.CACHE_CITY.getKey();
                tCompanyPost.setCityid("");
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
                tCompanyPost.setPostType(0);
                //需要优化
                tCompanyPost.setAudit(2);
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
                    if (tCompanyPost.getHiredBounty() != null) {
                        map.put("hired_bounty", tCompanyPost.getHiredBounty());
                    }
                    // map.put("money_reward", "[]");
           /*            if (tCompanyPostVO.getHeadhunterPositionRecordVOS().size() > 0 && tCompanyPostVO.getHeadhunterPositionRecordVOS() != null) {
                        map.put("money_reward", JSON.toJSONString(tCompanyPostVO.getHeadhunterPositionRecordVOS()));
                    }*/

                    map.put("uuid", redisUtils.generateCode());
                    eiCompanyPostProduce.sendAsyncMessage(JSON.toJSONString(map));
                    //金币奖励
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

    @PostMapping("/update/{postId}")
    @ApiOperation(value = "更新自营岗位")
    @Log(title = "更新自营岗位", businessType = BusinessType.UPDATE)
    @ApiImplicitParam(name = "postId", required = true, value = "岗位编号")
    @AutoIdempotent
    public R update(@Valid @RequestBody SaveRecommendPostVO saveRecommendPostVO, @PathVariable("postId") String postId) throws Exception {




        boolean verify = PhoneEmailUtils.verify(saveRecommendPostVO.getDescription());
        if(verify){
            return R.fail("职位详情不能存在联系方式！");
        }
/*        RecommendBasics byId = recommendBasicsService.getById(companyMetadata.userid());
        if (byId == null || byId.getAdministrator() == 0) {
            return R.fail("请使用猎企认证账号!");
        }
        if (byId.getAdministrator() == 1) {
            return R.fail("猎企认证在审核中！");
        }*/
        Boolean aBoolean = DateUtils.exceedDate(saveRecommendPostVO.getExpirationDate());
        if (!aBoolean) {
            return R.fail("截止日期不能大于一年！");
        }
/*        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("post_label", saveRecommendPostVO.getPostLabel().trim());
        queryWrapper.eq("company_id", companyMetadata.userid());
        int count = tCompanyPostService.count(queryWrapper);
        if (count > 1) {
            return R.fail("线上已有相同职位，请修改后重新发布!");
        }*/
        TCompanyPost byId1 = tCompanyPostService.getById(postId);
        if(byId1==null){
            return R.fail("该职位不存在！");
        }

        Integer individualOrTeam=0;
        TRecommend tRecommend = tRecommendApi.selectId(companyMetadata.userid());
        if(tRecommend.getAdministrator()==0){
            if(! StringUtils.isEmpty(tRecommend.getRealName())){
                 individualOrTeam=1;
            }
        }
        if(byId1.getIndividualOrTeam()==1 &&  individualOrTeam==0){
            saveRecommendPostVO.setExpirationDate(byId1.getExpirationDate());
        }
        TCompanyPost tCompanyPost = new TCompanyPost();
        BeanUtils.copyProperties(saveRecommendPostVO, tCompanyPost);
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
        tCompanyPost.setPostId(postId);
        tCompanyPost.setEducationId(EducationUtil.screen(saveRecommendPostVO.getEducationType()));
        tCompanyPost.setWorkExperienceId(WorkExperienceUtil.screenOut(saveRecommendPostVO.getWorkExperience()));
        tCompanyPost.setCityid("");
        tCompanyPost.setFirmSize(ScaleUtil.screen(saveRecommendPostVO.getFirmSize()));
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
            //记录职位更新记录
            updatePostRecordAsync.add(postId, companyMetadata.userid(), JSON.toJSONString(byId1));
            Map map = new ConcurrentHashMap();
            map.put("post_id", byId1.getPostId());
            map.put("company_id", byId1.getCompanyId());
            map.put("begin_salary", byId1.getBeginSalary());
            map.put("end_salary", byId1.getEndSalary());
            map.put("work_city", byId1.getWorkCity());
            map.put("post_label", byId1.getPostLabel());
            map.put("post_type",0);
            map.put("uuid", redisUtils.generateCode());
            map.put("company_name", tCompanyPost.getCompanyName());
            eiCompanyPostProduce.sendAsyncMessage(JSON.toJSONString(map));
            return R.success("更新成功");
        }
        return R.fail("更新失败");
    }


    @GetMapping("/select")
    @ApiOperation("推荐官获取自营岗位")
    @Log(title = "更新自营岗位", businessType = BusinessType.SELECT)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageSize", required = true),
            @ApiImplicitParam(name = "pageNo", required = true),
            @ApiImplicitParam(name = "keyword", required = false, value = "关键字"),
            @ApiImplicitParam(name = "state", value = "状态(1:上线 2:未上线 3:审核中 4:未通过)",required = false),
            @ApiImplicitParam(name = "individualOrTeam", value = "个体或机构(0:机构 1:个体)",required = true)
    })
    public R<PostShare> select(@RequestParam("pageSize") Integer pageSize, @RequestParam("pageNo") Integer pageNo, @RequestParam(value = "keyword", required = false) String keyword, @RequestParam(value = "state",required = false) Integer state,@RequestParam(value = "individualOrTeam") Integer individualOrTeam) {
        Page page = new Page(pageNo, pageSize);
        if(individualOrTeam==null){
            individualOrTeam=0;
        }
        Page<PostShare> page1 = tCompanyPostService.selectRecommend(page, keyword, companyMetadata.userid(), state,individualOrTeam);
        return R.success(page1);
    }


    @GetMapping("/selectById/{postId}")
    @ApiImplicitParam(name = "postId", value = "岗位id")
    @ApiOperation("自营岗位详细信息")
    @Log(title = "自营岗位详细信息", businessType = BusinessType.SELECT)
    public R<TCompanyPost> selectById(@PathVariable("postId") String postId) {
        TCompanyPost byId = tCompanyPostService.getById(postId);
        return R.success(byId);
    }

    @GetMapping("/amount")
    @ApiOperation("查看岗位总数")
    @Log(title = "查看岗位总数", businessType = BusinessType.SELECT)
    @ApiImplicitParam(name = "individualOrTeam", value = "个体或机构(0:机构 1:个体)",required = true)
    public R<Map<String,Integer>> amount(@RequestParam(value = "individualOrTeam") Integer individualOrTeam) {
        if(individualOrTeam==null){
            individualOrTeam=0;
        }
        Map<String,Integer> map = new HashMap(5);
        QueryWrapper onLine = new QueryWrapper();
        onLine.eq("status", 1);
        onLine.eq("create_id", companyMetadata.userid());
        onLine.eq("individual_or_team",individualOrTeam);
        map.put("onLine", tCompanyPostService.count(onLine));

        QueryWrapper notOnline = new QueryWrapper();
        notOnline.eq("status", 0);
        notOnline.eq("create_id", companyMetadata.userid());
        notOnline.eq("individual_or_team",individualOrTeam);
        map.put("notOnline", tCompanyPostService.count(notOnline));


        QueryWrapper underReview = new QueryWrapper();
        underReview.eq("audit", 0);
        underReview.eq("create_id", companyMetadata.userid());
        underReview.eq("individual_or_team",individualOrTeam);
        map.put("underReview", tCompanyPostService.count(underReview));


        QueryWrapper notPass = new QueryWrapper();
        notPass.eq("audit", 1);
        notPass.eq("create_id", companyMetadata.userid());
        notPass.eq("individual_or_team",individualOrTeam);
        map.put("notPass", tCompanyPostService.count(notPass));

        QueryWrapper count=new QueryWrapper();
        count.eq("create_id", companyMetadata.userid());
        map.put("count", tCompanyPostService.count(count));
        return R.success(map);
    }

    @GetMapping("/refresh")
    @ApiImplicitParam(name = "postId", value = "岗位id")
    @ApiOperation("刷新岗位")
     @Log(title = "刷新岗位", businessType = BusinessType.SELECT)
    public R refresh(@RequestParam("postId") List<String> postId) {
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.in("post_id", postId);
        updateWrapper.set("refresh_time", LocalDateTime.now());
        updateWrapper.set("create_id",companyMetadata.userid());
        boolean update = tCompanyPostService.update(updateWrapper);
        if(update){
            return R.success();
        }
        return R.fail();
    }
   @GetMapping("/beOffline")
    @ApiImplicitParam(name = "postId", value = "岗位id")
    @ApiOperation("下线")
    @Log(title = "下线", businessType = BusinessType.UPDATE)
   public R  beOffline(@RequestParam("postId") List<String> postId){
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.in("post_id", postId);
        updateWrapper.eq("create_id",companyMetadata.userid());
        updateWrapper.set("status", 0);
        updateWrapper.set("update_id",companyMetadata.userid());
        updateWrapper.set("update_time",LocalDateTime.now());
        boolean update = tCompanyPostService.update(updateWrapper);
        return R.success();
   }


   @GetMapping("/popUpOnline")
    @ApiImplicitParam(name = "postIds", value = "岗位id")
    @ApiOperation("上线")
    @Log(title = "上线", businessType = BusinessType.UPDATE)
    public R  popUpOnline(@RequestParam("postIds") List<String> postIds){
       for (String postId : postIds) {
           TCompanyPost byId = tCompanyPostService.getById(postId);
           if(byId==null){
               return R.fail("未查到该职位");
           }

           TRecommend tRecommend = tRecommendApi.selectId(companyMetadata.userid());
           Integer individualOrTeam=0;
           if(tRecommend.getAdministrator()==0){
               if(! StringUtils.isEmpty(tRecommend.getRealName())){
                   individualOrTeam=1;
               }
           }
           if(byId.getIndividualOrTeam()==1 && individualOrTeam==0){
                 return R.fail("["+byId.getPostLabel()+"]岗位为未加入机构岗位不能上线!");
           }
           LocalDate expirationDate = byId.getExpirationDate();
           if(expirationDate.isBefore(LocalDate.now())){
               return R.fail("["+byId.getPostLabel()+"]岗位截止日期不能小于今天!");
           }

       }
       UpdateWrapper updateWrapper = new UpdateWrapper();
       updateWrapper.in("post_id", postIds);
       updateWrapper.eq("create_id",companyMetadata.userid());
       updateWrapper.set("status", 1);
       updateWrapper.set("update_id",companyMetadata.userid());
       updateWrapper.set("update_time",LocalDateTime.now());
       boolean update = tCompanyPostService.update(updateWrapper);
       if(update){
           return R.success("已上线!");
       }
       return R.fail("未上线!");
    }

    @GetMapping("/individualOrTeam")
    @ApiOperation("获取是否发布过机构或个人职位")
    public R<Map> individualOrTeam(){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("company_id",companyMetadata.userid());
        queryWrapper.eq("individual_or_team",0);
        int count = tCompanyPostService.count(queryWrapper);

        QueryWrapper queryWrapper1=new QueryWrapper();
        queryWrapper1.eq("company_id",companyMetadata.userid());
        queryWrapper1.eq("individual_or_team",1);
        int count1 = tCompanyPostService.count(queryWrapper1);
        Map map=new HashMap(2);
        map.put("individual",count1>0? true:false);
        map.put("team",count>0? true:false);
        return R.success(map);
    }

}
