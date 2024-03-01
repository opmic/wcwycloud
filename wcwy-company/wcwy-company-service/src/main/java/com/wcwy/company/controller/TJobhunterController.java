package com.wcwy.company.controller;

import cn.hutool.core.util.PhoneUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wcwy.common.base.enums.AccessTemplateCode;
import com.wcwy.common.base.enums.SmsEunm;
import com.wcwy.common.base.result.R;
import com.wcwy.common.base.utils.BirthdayUtils;
import com.wcwy.common.base.utils.IpUtils;
import com.wcwy.common.base.utils.NameUtils;
import com.wcwy.common.base.utils.PhoneUtils;
import com.wcwy.common.redis.enums.*;
import com.wcwy.common.redis.util.IDGenerator;
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.common.web.config.CacheClient;
import com.wcwy.company.aliyun.SendSms;
import com.wcwy.company.asyn.InformAsync;
import com.wcwy.company.asyn.RegisterAsync;
import com.wcwy.company.asyn.RunningWaterAsync;
import com.wcwy.company.dto.ReferrerRecordJobHunterDTO;
import com.wcwy.company.dto.RmJobHunterDTO;
import com.wcwy.company.entity.*;
import com.wcwy.company.po.SharePO;
import com.wcwy.company.po.TJobhunterPO;
import com.wcwy.company.query.CompanyInviteJobHunterQuery;
import com.wcwy.company.service.*;
import com.wcwy.company.session.CompanyMetadata;
import com.wcwy.company.vo.*;
import com.wcwy.post.api.ResumePaymentConfigApi;
import com.wcwy.post.api.TPayConfigApi;
import com.wcwy.post.entity.ResumePaymentConfig;
import com.wcwy.post.entity.TPayConfig;
import com.wcwy.system.annotation.Log;
import com.wcwy.system.enums.BusinessType;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 * @description 针对表【t_jobhunter(求职者用户表)】的数据库操作Service
 * @createDate 2022-10-08 11:58:24
 */
@RestController
@RequestMapping("/jobhunter")
@Api(tags = "求职者用户接口")
@Slf4j
public class TJobhunterController {
    @Autowired
    private RunningWaterAsync runningWaterAsync;
    @Autowired
    private TJobhunterService tJobhunterService;
    @Resource
    private PutInResumeService putInResumeService;
    @Autowired
    private CompanyMetadata companyMetadata;
    @Autowired
    private InformAsync informAsync;
    @Autowired
    private TJobhunterHideCompanyService tJobhunterHideCompanyService;

    @Autowired
    private CompanyUserRoleService companyUserRoleService;

    @Autowired
    private CacheClient cacheClient;

    @Autowired
    private RecommendJobHunterService recommendJobHunterService;

    @Autowired
    private RedisTemplate redisTemplate;
    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private TJobhunterRoleService tJobhunterRoleService;
    @Resource
    private TJobHunterAttachmentService tJobHunterAttachmentService;
    @Autowired
    private TRecommendService tRecommendService;
    @Autowired
    private TJobhunterResumeService tJobhunterResumeService;
    @Resource
    private CollerctPostService collerctPostService;
    @Autowired
    private ReferrerRecordService referrerRecordService;
    @Resource
    private TJobhunterExpectPositionService tJobhunterExpectPositionService;
    @Autowired
    private TJobhunterEducationRecordService tJobhunterEducationRecordService;
    @Autowired
    private TJobhunterProjectRecordService tJobhunterProjectRecordService;
    @Autowired
    private TJobhunterWorkRecordService tJobhunterWorkRecordService;
    @Autowired
    private IDGenerator idGenerator;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private TCompanyService tCompanyService;
    @Autowired
    private TPayConfigApi tPayConfigApi;
    @Autowired
    private ResumePaymentConfigApi resumePaymentConfigApi;

    @Autowired
    private SendSms sendSms;

    @Autowired
    private DivideIntoService divideIntoService;

    @Autowired
    private RegisterAsync registerAsync;

    /*   @Autowired
       private Redisson redisson;*/
    @ApiOperation("通过token获取求职者信息")
    @GetMapping("/getJobhunter")
    @Log(title = "通过token获取求职者信息", businessType = BusinessType.SELECT)
    public R<TJobhunterPO> getJobhunter() {
        if (!"TJ".equals(companyMetadata.userid().substring(0, 2))) {
            return R.success();
        }
        TJobhunter byId = tJobhunterService.getById(companyMetadata.userid());
        if (byId == null) {
            return null;
        }
        byId.setPassword("草泥马！敢偷窥密码");
        TJobhunterPO tJobhunterPO = new TJobhunterPO();
        //每30分钟记录一次登录时间
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Boolean flag = valueOperations.setIfAbsent(Sole.LOGIN_USER.getKey() + companyMetadata.userid(), "1", 60 * 30, TimeUnit.SECONDS);
        if (flag) {
            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("user_id", companyMetadata.userid());
            updateWrapper.set("login_time", LocalDateTime.now());
            boolean update = tJobhunterService.update(updateWrapper);
        }
        if (byId != null) {
            BeanUtils.copyProperties(byId, tJobhunterPO);
            tJobhunterPO.setPhoneNumber(byId.getPhoneNumber());
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("put_in_user", companyMetadata.userid());
        int count = putInResumeService.count(queryWrapper);
        tJobhunterPO.setPosts(count);
        List<String> authorization = tCompanyService.authorization(byId.getLoginName());
        tJobhunterPO.setAuthorization(authorization);
        if (byId.getPerfect() == null || byId.getPerfect() == 0) {
            tJobhunterService.isSendAResume(companyMetadata.userid());
        }


        return R.success(tJobhunterPO);
    }

    //@ApiOperation("通过id获取求职者信息附件")
 /*   @GetMapping("/seletResumePath/{jobhunterId}")
    public R seletResumePath(){

    }*/

    @ApiOperation("修改求职者信息")
    @PostMapping("/updateJobhunter")
    @Log(title = "修改求职者信息", businessType = BusinessType.UPDATE)
    public R updateJobhunter(@Valid @RequestBody TJobhunterVO tJobhunterVO) {
        boolean phone = PhoneUtil.isPhone(tJobhunterVO.getPhone());
        if (!phone) {
            return R.fail("电话号码不正确！");
        }
        TJobhunter tJobhunter = new TJobhunter();
        BeanUtils.copyProperties(tJobhunterVO, tJobhunter);
        tJobhunter.setJobStatus(tJobhunterVO.getJobStatus().toString());
        //求职者如果没有填写年薪已0代替
        if (tJobhunterVO.getCurrentSalary() == null) {
            tJobhunter.setCurrentSalary(BigDecimal.valueOf(0.00));
        }
        tJobhunter.setUserType(tJobhunterVO.getUserType().toString());
        //求职者如果没有填工作年限已当前时间为工作时间
      /*  if (tJobhunterVO.getWorkTime() == null) {
            tJobhunter.setWorkTime(LocalDate.now());
        }*/

        tJobhunter.setUpdateTime(LocalDateTime.now());
        tJobhunter.setUpdateId(companyMetadata.userid());
        boolean b = tJobhunterService.updateById(tJobhunter);
        if (b) {
            return R.success("更新成功");
        }
        return R.success("更新失败");
    }

    @ApiOperation("更换头像接口")
    @PostMapping("/updateAvatar")
    /*@ApiImplicitParams({
            @ApiImplicitParam(name = "jobhunterId", required = true, value = "求职者id"),
            @ApiImplicitParam(name = "avatar", required = true, value = "头像地址"),
    })*/
    @Log(title = "更换头像接口", businessType = BusinessType.UPDATE)
    public R updateAvatar(@ApiParam("求职者id") @RequestParam("jobhunterId") String jobhunterId, @ApiParam("头像地址") @RequestParam("avatar") String avatar) {
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("user_id", jobhunterId);
        updateWrapper.set("avatar", avatar);
        boolean update = tJobhunterService.update(updateWrapper);
        if (update) {
            return R.success("修改成功");
        }
        return R.fail("修改失败");
    }

   /* @ApiOperation("求职者更换手机号")
    @PostMapping("/updatePhone")
    public R updatePhone(@Valid @RequestBody UpdatePhone updatePhone) {
        if (!companyMetadata.userid().equals(updatePhone.getUserId())) {
            return R.fail("登录与传入id不一致 请检查！");
        }
        ValueOperations<String, String> stringStringValueOperations = stringRedisTemplate.opsForValue();
        String cc = SmsEunm.UPDATE_PHONE.getType() + updatePhone.getFormerPhone();
        String code1 = stringStringValueOperations.get(cc.trim());
        if (!updatePhone.getFormerCode().equals(code1)) {
            return R.fail("旧电话验证码不正确！");
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("phone_number", updatePhone.getNewPhone());
        int count = tJobhunterService.count(queryWrapper);
        if (count > 0) {
            return R.fail("该电话号码已被使用!");
        }
        // String substring = updatePhone.getUserId().substring(0, 2);
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("user_id", updatePhone.getUserId());
        updateWrapper.set("login_name", updatePhone.getNewPhone());
        updateWrapper.set("phone_number", updatePhone.getNewPhone());
        boolean update = tJobhunterService.update(updateWrapper);
        if (update) {
            return R.success("更新成功");
        }
        return R.fail("更新失败!");
    }*/

    // @ApiOperation("修改密码")
    // @PostMapping("/updatePassword")
    @Log(title = "修改密码", businessType = BusinessType.UPDATE)
    public R updatePassword(@Valid @RequestBody TJobhunterUpdatePasswordVO tJobhunterUpdatePasswordVO) {
        TJobhunter byId = tJobhunterService.getById(companyMetadata.userid());
        if (byId == null) {
            return R.fail("无此用户");
        }
        boolean matches = bCryptPasswordEncoder.matches(tJobhunterUpdatePasswordVO.getOldCode(), byId.getPassword());
        if (matches) {
            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("user_id", companyMetadata.userid());
            updateWrapper.set("password", bCryptPasswordEncoder.encode(tJobhunterUpdatePasswordVO.getNewPassword()));
            boolean update = tJobhunterService.update(updateWrapper);
            if (update) {
                return R.success("更新成功");
            }

        }
        return R.fail("旧密码不正确");
    }

    @ApiOperation("/修改求职者状态")
    @GetMapping("/updateJobStatus")
    @Log(title = "修改求职者状态", businessType = BusinessType.UPDATE)
    public R updateJobStatus(@ApiParam("修改状态") @RequestParam("state") Integer state) {
        if (state.equals("")) {
            return R.fail("请选择状态");
        }
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("user_id", companyMetadata.userid());
        updateWrapper.set("job_status", state);
        boolean update = tJobhunterService.update(updateWrapper);
        if (update) {
            return R.success("修改成功！");
        }
        return R.fail("修改失败！");
    }


    @ApiOperation("求职者通过密码更换手机号")
    @PostMapping("/passwordUpdatePhone")
    @Log(title = "求职者通过密码更换手机号", businessType = BusinessType.UPDATE)
    public R passwordUpdatePhone(@Valid @RequestBody PasswordUpdatePhone updatePhone) {
        if (!companyMetadata.userid().equals(updatePhone.getUserId())) {
            return R.fail("登录与传入id不一致 请检查！");
        }
        TJobhunter byId = tJobhunterService.getById(companyMetadata.userid());
        boolean matches = bCryptPasswordEncoder.matches(updatePhone.getPassword(), byId.getPassword());
        if (!matches) {
            return R.fail("密码不正确!");
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("phone_number", updatePhone.getNewPhone());
        int count = tJobhunterService.count(queryWrapper);
        if (count > 0) {
            return R.fail("该电话号码已被使用!");
        }
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("user_id", updatePhone.getUserId());
        updateWrapper.set("login_name", updatePhone.getNewPhone());
        updateWrapper.set("phone_number", updatePhone.getNewPhone());
        boolean update = tJobhunterService.update(updateWrapper);
        if (update) {
            return R.success("更新成功");
        }
        return R.fail("更新失败!");
    }


    @GetMapping("/sharePerson")
    @ApiOperation("添加邀请人")
    @Log(title = "添加邀请人", businessType = BusinessType.INSERT)
    public R sharePerson(@RequestParam("sharePerson") String sharePerson) {
        if (StringUtils.isEmpty(sharePerson)) {
            return R.fail("邀请人id不能为空!");
        }
        String substring = sharePerson.substring(0, 2);
        if (StringUtils.pathEquals("TR", substring)) {
            TRecommend byId = tRecommendService.getById(sharePerson);
            if (byId == null) {
                return R.fail("此邀请人不存在");
            }
            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("user_id", companyMetadata.userid());
            updateWrapper.set("share_person", sharePerson);

            boolean update = tJobhunterService.update(updateWrapper);
            if (update) {
                return R.success("添加成功!");
            }
        }
        return R.fail("请检查账号是否存在!");
    }


    @PostMapping("/insertJobHunter")
    @ApiOperation(value = "求职者注册")
    @Transactional(rollbackFor = Exception.class)
    @Log(title = "求职者注册", businessType = BusinessType.INSERT)
    public R verificationPhone(HttpServletRequest request, @Valid @RequestBody InsertTJobHunterVO insertTJobHunterVO) throws Exception {
        String s = stringRedisTemplate.opsForValue().get(SmsEunm.INSERT_CODE.getType() + insertTJobHunterVO.getLoginName());
        if (insertTJobHunterVO.getCode().equals(s)) {
            TJobhunter tJobhunter = new TJobhunter();
            tJobhunter.setUserId(idGenerator.generateCode("TJ"));
            tJobhunter.setStatus("0");
            tJobhunter.setLoginName(insertTJobHunterVO.getLoginName());
            tJobhunter.setPhoneNumber(insertTJobHunterVO.getLoginName());
            tJobhunter.setPassword(bCryptPasswordEncoder.encode(insertTJobHunterVO.getPassword()));
            tJobhunter.setCreateTime(LocalDateTime.now());
            tJobhunter.setAvatar("https://dev-1313175594.cos.ap-guangzhou.myqcloud.com/head/2.png");
            tJobhunter.setSex(2);
            tJobhunter.setExamineStatus(0);
            if (!StringUtils.isEmpty(insertTJobHunterVO.getInvitationCode())) {

                //获取邀请的推荐官
                Object o = redisUtils.get(InvitationCode.INVITATION_URL_CODE.getType() + insertTJobHunterVO.getInvitationCode());
                if (o != null) {
                    String ipAddr = IpUtils.getIpAddr(request);
                    registerAsync.register(ipAddr, 2, o.toString());
                    tJobhunter.setSharePerson(o.toString());
                    ReferrerRecord referrerRecord = new ReferrerRecord();
                    referrerRecord.setTJobHunterId(tJobhunter.getUserId());
                    referrerRecord.setRecommendId(o.toString());
                    referrerRecord.setCorrelationType(0);
                    referrerRecord.setCreateTime(LocalDateTime.now());
                    boolean save1 = referrerRecordService.save(referrerRecord);
                    if (save1) {
                        runningWaterAsync.inviter_job_hunter(o.toString());

                    }
                }
            }
            boolean save = tJobhunterService.save(tJobhunter);
            if (save) {
                TJobhunterRole tJobhunterRole = new TJobhunterRole();
                tJobhunterRole.setRoleId("4");
                tJobhunterRole.setUserId(tJobhunter.getUserId());
                tJobhunterRole.setCreateTime(LocalDateTime.now());
                tJobhunterRoleService.save(tJobhunterRole);
                tJobhunterResumeService.addResume(tJobhunter.getUserId());
                    /*  if( !isOK){
                          log.error("给求职者"+tJobhunter.getUserId()+"创建简历失败！=================》");
                      }*/


                stringRedisTemplate.delete(SmsEunm.INSERT_CODE.getType() + insertTJobHunterVO.getLoginName());
                String ipAddr = IpUtils.getIpAddr(request);
                registerAsync.register(ipAddr, 2);
                return R.success("注册成功!");
            } else {
                return R.fail("验证码不正确或验证码已超时!");
            }

        }
        return R.fail("验证码已超时!");
    }

    @GetMapping("/refresh")
    @ApiOperation(value = "简历刷新")
    @Log(title = "简历刷新", businessType = BusinessType.UPDATE)
    @ApiImplicitParam(name = "id", required = true, value = "求职者id")
    public R refresh(@RequestParam(value = "id",required = true) String id) {
       /* String userid = companyMetadata.userid();*/
        LocalDateTime now = LocalDateTime.now();
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("user_id", id);
        updateWrapper.set("refresh_time", now);
        boolean update = tJobhunterService.update(updateWrapper);
        if (update) {
            return R.success(now);
        }
        return R.fail("刷新失败!");
    }


    @ApiOperation("主页获取投简申请")
    @GetMapping("/getInfo")
    @Log(title = "主页获取投简申请", businessType = BusinessType.SELECT)
    public R getInfo() {
        String userid = companyMetadata.userid();
        if (!"TJ".equals(userid.substring(0, 2))) {
            return R.success();
        }
        List<String> list = tJobhunterService.getExpectPosition(userid);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("collerct_user_id", userid);
        int count = collerctPostService.count(queryWrapper);

        QueryWrapper queryWrapper1 = new QueryWrapper();
        queryWrapper1.eq("put_in_user", userid);
        queryWrapper1.eq("deleted_put_in", 0);
        int count1 = putInResumeService.count(queryWrapper1);
        Map map = new HashMap();
        int perfect = perfect();
        QueryWrapper queryWrapper2 = new QueryWrapper();
        queryWrapper2.eq("resume_id", companyMetadata.userid());
        int count2 = tJobhunterHideCompanyService.count(queryWrapper2);
        if(list!=null && list.size()!=0 ){
            List list1 = JSON.parseObject(list.get(0), List.class);
            map.put("expectation", list1);
        }else {
            map.put("expectation", 0);

        }
        if(perfect !=100){
            informAsync.perfectYourResume(companyMetadata.userid());
        }
        map.put("perfect", perfect);
        map.put("collect", count);
        map.put("proposer", count1);
        map.put("shieldCompany", count2);
        return R.success(map);
    }


    public int perfect() {
        int a = 0;
        TJobhunter byId = tJobhunterService.getById(companyMetadata.userid());
        if (!StringUtils.isEmpty(byId.getUserName())) {
            // return R.fail("请完善简历信息,该岗位已给您收藏!");
            a = a + 25;
        }
        String advantage = tJobhunterResumeService.getAdvantage(companyMetadata.userid());
        if (!StringUtils.isEmpty(advantage)) {
            //return R.fail("请完善个人优势,该岗位已给您收藏!");
            a = a + 25;
        }
        String eduId = tJobhunterEducationRecordService.getEduId(companyMetadata.userid());
        if (!StringUtils.isEmpty(eduId)) {
            //  return R.fail("请完善个人学历,该岗位已给您收藏!");
            a = a + 25;
        }
        String postionId = tJobhunterExpectPositionService.postionId(companyMetadata.userid());
        if (!StringUtils.isEmpty(postionId)) {
            //return R.fail("请完善求职期望,该岗位已给您收藏!");
            a = a + 25;
        }
        return a;
    }

    /**
     * @param jonHunter:求职者id
     * @return null
     * @Description: 获取求职者的现目前薪资及邀请的推荐官及推荐官邀请的推荐官
     * @Author tangzhuo
     * @CreateTime 2023/4/4 8:29
     */

    @GetMapping("/getCurrentSalary")
    public Map<String, Object> getCurrentSalary(@RequestParam("jonHunter") String jonHunter) {
        SharePO share = tJobhunterService.share(jonHunter);
        Map<String, Object> map = new ConcurrentHashMap(3);

        if (share != null) {
            map.put("recommend_id", "");
            map.put("recommend_share", "");
            map.put("currentSalary", new BigDecimal(0.00));
            if (!StringUtils.isEmpty(share.getSharePerson())) {
                map.put("recommend_id", share.getSharePerson());
            }
            if (!StringUtils.isEmpty(share.getRecommendShare())) {
                map.put("recommend_share", share.getRecommendShare());
            }
            if (share.getCurrentSalary() != null) {
                map.put("currentSalary", share.getCurrentSalary());
            }

            return map;
        }
        return null;
    }

    @GetMapping("/resumePrice")
    @ApiOperation("企业获取简历价格")
    @Log(title = "企业获取简历价格", businessType = BusinessType.SELECT)
    //@ApiImplicitParam(name = "jonHunter", required = true, value = "求职者id")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "jonHunter", required = true, value = "求职者id"),
            @ApiImplicitParam(name = "type", required = false, value = "岗位类型")
    })
    public R resumePrice(@RequestParam("jonHunter") String jonHunter, @RequestParam(value = "type", required = false) Integer type) {
        TJobhunter byId = tJobhunterService.getById(jonHunter);
        if (byId == null) {
            return R.fail("求职者不存在!");
        }
        if (byId.getCurrentSalary() == null) {
            return R.fail("改求职者信息不完善,暂时不支持下载!");
        }
        if (byId.getEducation() == null) {
            return R.fail("改求职者信息不完善,暂时不支持下载!");
        }
        if (!StringUtils.isEmpty(type)) {
            if (type >= 4) {
                Map<String, BigDecimal> stringBigDecimalMap = divideIntoService.resumePayment(byId.getEducation(), byId.getCurrentSalary(), type);
                return R.success(stringBigDecimalMap);
            }
        }
        BigDecimal currentSalary = byId.getCurrentSalary();
        Map<String, Integer> map = divideIntoService.currencyCount(currentSalary);
        return R.success(map);
    }

    @GetMapping("/resumeRecommendPrice")
    @ApiOperation("推荐官获取简历价格")
    @Log(title = "推荐官获取简历价格", businessType = BusinessType.SELECT)
    @ApiImplicitParam(name = "jonHunter", required = true, value = "求职者id")
    public R resumeRecommendPrice(@RequestParam("jonHunter") String jonHunter) {
        TJobhunter byId = tJobhunterService.getById(jonHunter);
        if (byId.getCurrentSalary() == null) {
            return R.fail("改求职者信息不完善,暂时不支持下载!");
        }
        QueryWrapper queryWrapper1 = new QueryWrapper();
        queryWrapper1.eq("job_hunter_id", jonHunter);
        queryWrapper1.eq("recommend_id", companyMetadata.userid());
        queryWrapper1.eq("deleted", 0);
        RecommendJobHunter one1 = recommendJobHunterService.getOne(queryWrapper1);
        if (one1 != null) {
            Map map = new HashMap(2);
            map.put("currencyCount", 0);
            map.put("gold", 0);
            return R.success(map);
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("recommend_id", companyMetadata.userid());
        queryWrapper.eq("t_job_hunter_id", jonHunter);
        ReferrerRecord one = referrerRecordService.getOne(queryWrapper);
        if (one != null) {
            //判断该推荐官是否有权限
            Integer correlationType = one.getCorrelationType();
            Map map = new HashMap(2);
            if (correlationType == 0 || correlationType == 2 || one.getDownloadIf() == 1) {
                map.put("currencyCount", 0);
                map.put("gold", 0);
                return R.success(map);
            }
        }
        BigDecimal currentSalary = byId.getCurrentSalary();
        Map<String, Integer> map = divideIntoService.currencyCount(currentSalary);
        return R.success(map);

    }


    /**
     * @param jobHunterId 求职者id
     * @return Map
     * @Description: 查询求职者的邀请及推荐官的邀请人
     * @Author tangzhuo
     * @CreateTime 2023/4/21 11:21
     */

    @GetMapping("/getSharePersonRecommend")
    public Map<String, String> getSharePersonRecommend(@RequestParam("jobHunterId") String jobHunterId) {
        Map<String, String> map = tJobhunterService.getSharePersonRecommend(jobHunterId);
        return map;
    }

    @GetMapping("/insertCodeJobHunter")
    // @Transactional(rollbackFor = Exception.class)
    @Log(title = "求职者注册", businessType = BusinessType.INSERT)
    public TJobhunter insertCodeJobHunter(@RequestParam("phone") String phone, @RequestParam(value = "code", required = false) String code, @RequestParam(value = "ip", required = false) String ip) throws Exception {
     /*  if(! StringUtils.isEmpty(code)){
           String s = stringRedisTemplate.opsForValue().get(SmsEunm.LOGIN_CODE.getType() + phone);
           if(! StringUtils.pathEquals(s,code)){

           }
       }*/
        if (!StringUtils.isEmpty(phone)) {
            TJobhunter tJobhunter = new TJobhunter();
            tJobhunter.setUserId(idGenerator.generateCode("TJ"));
            tJobhunter.setStatus("0");
            tJobhunter.setLoginName(phone);
            tJobhunter.setPhoneNumber(phone);
            tJobhunter.setUserName("求职者");
            tJobhunter.setCreateTime(LocalDateTime.now());
            tJobhunter.setPassword(bCryptPasswordEncoder.encode("wu888888"));
            tJobhunter.setAvatar("https://dev-1313175594.cos.ap-guangzhou.myqcloud.com/head/2.png");
            tJobhunter.setSex(2);
            tJobhunter.setExamineStatus(0);
            int source = 3;
            if (!StringUtils.isEmpty(code)) {
                Map map = JSON.parseObject(code, Map.class);
                if (!StringUtils.isEmpty(map.get("QRCode"))) {
                    Object o = redisUtils.get(InvitationCode.INVITATION_URL_CODE.getType() + map.get("QRCode"));
                    if (o != null && "TR".equals(o.toString().substring(0, 2))) {
                        try {
                            Object type = map.get("type");
                            if (!StringUtils.isEmpty(type)) {
                                int i = Integer.parseInt(type.toString());
                                //区别是职位推广还是用户注册链接 i:(0:职位推广 1:用户注册推广))
                                source = i == 1 ? 0 : 1;
                                registerAsync.register(ip, i == 1 ? 1 : 0, o.toString());
                                tJobhunter.setOrigin(i);
                            } else {
                                registerAsync.register(ip, 1, o.toString());
                            }

                        } catch (Exception e) {
                            log.error(e.toString());
                        }

                        tJobhunter.setSharePerson(o.toString());
                        ReferrerRecord referrerRecord = new ReferrerRecord();
                        referrerRecord.setTJobHunterId(tJobhunter.getUserId());
                        referrerRecord.setRecommendId(o.toString());
                        referrerRecord.setCreateTime(LocalDateTime.now());
                        if (source != 3) {
                            referrerRecord.setOrigin(source);
                        } else {
                            referrerRecord.setOrigin(0);
                        }
                        referrerRecord.setCorrelationType(0);
                        boolean save1 = referrerRecordService.save(referrerRecord);
                        if (save1) {
                            //求职者注册奖金
                            runningWaterAsync.inviter_job_hunter(o.toString());
                            //通知推荐官
                            sendSms.registerInform(o.toString(), AccessTemplateCode.INFORM_JOB_HUNTER.getName(), AccessTemplateCode.INFORM_JOB_HUNTER.getDesc(), phone);
                        }
                    } else if (o != null && "TC".equals(o.toString().substring(0, 2))) {

                        tJobhunter.setSharePerson(o.toString());
                        CompanyUserRole companyUserRole = new CompanyUserRole();
                        companyUserRole.setCompanyUserRoleId(idGenerator.generateCode("CU"));
                        companyUserRole.setCompanyId(o.toString());
                        companyUserRole.setUserId(tJobhunter.getUserId());
                        companyUserRole.setDeleted(0);
                        companyUserRole.setSource(1);
                        companyUserRole.setCreateTime(LocalDateTime.now());
                        companyUserRoleService.save(companyUserRole);
                        //通知企业
                        sendSms.registerInform(o.toString(), AccessTemplateCode.INFORM_JOB_HUNTER.getName(), AccessTemplateCode.INFORM_JOB_HUNTER.getDesc(), phone);
                    }
                }

            }
            boolean save = tJobhunterService.save(tJobhunter);
            if (save) {
                TJobhunterRole tJobhunterRole = new TJobhunterRole();
                tJobhunterRole.setRoleId("4");
                tJobhunterRole.setUserId(tJobhunter.getUserId());
                tJobhunterRole.setCreateTime(LocalDateTime.now());
                tJobhunterRoleService.save(tJobhunterRole);
                tJobhunterResumeService.addResume(tJobhunter.getUserId());
            }

        /*    String ipAddr = IpUtils.getIpAddr(request);
            System.out.println(ipAddr);*/
            /* registerAsync.register(ipAddr,2);*/
            if (!StringUtils.isEmpty(ip)) {

                registerAsync.register(ip, 2);
            }
            return tJobhunter;
        }
        return null;
    }


    @ApiOperation("求职者更换手机号")
    @PostMapping("/updatePhone")
    @Log(title = "求职者更换手机号", businessType = BusinessType.UPDATE)
    public R updatePhone(@Valid @RequestBody UpdatePhone updatePhone) {
        if (!companyMetadata.userid().equals(updatePhone.getUserId())) {
            return R.fail("登录与传入id不一致 请检查！");
        }

        TJobhunter byId = tJobhunterService.getById(companyMetadata.userid());
        if (byId == null) {
            return R.fail("该求职者不存在！");
        }
        boolean matches = bCryptPasswordEncoder.matches(updatePhone.getPassword(), byId.getPassword());
        if (!matches) {
            return R.fail("密码不正确!");
        }
        //查询该手机号码有没有被使用
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("login_name", updatePhone.getNewPhone());
        int count = tJobhunterService.count(queryWrapper);
        if (count > 0) {
            return R.fail("该电话号码已被使用!");
        }


        ValueOperations<String, String> stringStringValueOperations = stringRedisTemplate.opsForValue();
        /*String cc = SmsEunm.UPDATE_PHONE.getType() + updatePhone.getFormerPhone();*/

        String s = SmsEunm.BINDING_PHONE.getType() + updatePhone.getNewPhone();
        /*String code1 = stringStringValueOperations.get(cc.trim());*/
        String code2 = stringStringValueOperations.get(s.trim());
      /*  if(StringUtils.isEmpty(code1)){
            return R.fail("旧验证码已过期!");
        }*/
        if (StringUtils.isEmpty(code2)) {
            return R.fail("验证码已过期!");
        }
        if (!updatePhone.getNewPhoneCode().equals(code2)) {
            return R.fail("验证码不正确！");
        }
        // String substring = updatePhone.getUserId().substring(0, 2);
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("user_id", updatePhone.getUserId());
        updateWrapper.set("login_name", updatePhone.getNewPhone());
        updateWrapper.set("phone_number", updatePhone.getNewPhone());
        boolean update = tJobhunterService.update(updateWrapper);
        if (update) {
            /*  stringRedisTemplate.delete(cc);*/
            stringRedisTemplate.delete(s);
            return R.success("更新成功");
        }
        return R.fail("更新失败!");
    }

    @GetMapping("/verifyPassword")
    @ApiOperation("验证密码")
    @ApiImplicitParam(name = "password", required = true, value = "密码")
    @Log(title = "验证密码", businessType = BusinessType.SELECT)
    public R verifyPassword(@RequestParam("password") String password) {
        String userid = companyMetadata.userid();
        TJobhunter byId = tJobhunterService.getById(userid);
        boolean matches = bCryptPasswordEncoder.matches(password, byId.getPassword());
        return matches ? R.success("密码正确!", true) : R.fail("密码错误!", false);
    }

    @GetMapping("/weChat")
    @ApiOperation("获取微信名")
    public R weChat() {
        String userid = companyMetadata.userid();
        String name = tJobhunterService.getWeChat(userid);
        return R.success(name);
    }

    @GetMapping("/untieWX")
    @ApiOperation("解绑微信")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", required = true, value = "电话号码"),
            @ApiImplicitParam(name = "code", required = true, value = "验证码")
    })
    @Log(title = "解绑微信", businessType = BusinessType.UPDATE)
    public R untieWX(@RequestParam("phone") String phone, @RequestParam("code") String code) {
        ValueOperations<String, String> stringValueOperations = stringRedisTemplate.opsForValue();
        String s = stringValueOperations.get(SmsEunm.UPDATE_PHONE.getType() + phone);
        if (StringUtils.isEmpty(s)) {
            return R.fail("验证码已过期");
        }
        if (!code.equals(s)) {
            return R.fail("验证码不正确！");
        }
        String userid = companyMetadata.userid();
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("user_id", userid);
        updateWrapper.set("openid", "");
        boolean update = tJobhunterService.update(updateWrapper);
        if (update) {
            Boolean delete = stringRedisTemplate.delete(SmsEunm.UPDATE_PHONE.getType() + phone);
            return R.success("修改成功!");
        }
        return R.fail("修改失败!");
    }

    @PostMapping("/updatePassword")
    @Log(title = "修改密码", businessType = BusinessType.UPDATE)
    @ApiOperation(value = "修改密码", notes = "修改密码")
    public R updatePassword(@Valid @RequestBody TCompanyUpdatePasswordVO tCompanyUpdatePasswordVO) {
        String userid = companyMetadata.userid();
        TJobhunter byId = tJobhunterService.getById(userid);
        if (byId == null) {
            return R.fail("没有该用户！");
        }
        ValueOperations<String, String> stringValueOperations = stringRedisTemplate.opsForValue();
        String s = stringValueOperations.get(SmsEunm.PASSWORD_CODE.getType() + tCompanyUpdatePasswordVO.getLoginName());
        if (!tCompanyUpdatePasswordVO.getCode().equals(s)) {
            return R.fail("验证码不正确或已过期!");
        }
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("user_id", userid);
        updateWrapper.set("password", bCryptPasswordEncoder.encode(tCompanyUpdatePasswordVO.getPassword()));
        updateWrapper.set("update_time", LocalDateTime.now());
        boolean update = tJobhunterService.update(updateWrapper);
        if (update) {
            stringRedisTemplate.delete(SmsEunm.PASSWORD_CODE.getType() + tCompanyUpdatePasswordVO.getLoginName());
            return R.success("修改成功!");
        }
        return R.fail("修改失败!");
    }


    @GetMapping("/resume")
    @ApiOperation("简历上传")
    @Log(title = "简历上传", businessType = BusinessType.UPDATE)
    @ApiImplicitParam(name = "resumePath", required = true, value = "简历路径")
    public R resume(@RequestParam String resumePath) {
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("user_id", companyMetadata.userid());
        updateWrapper.set("update_time", LocalDateTime.now());
        updateWrapper.set("resume_path", resumePath);
        boolean update = tJobhunterService.update(updateWrapper);
        if (update) {
            return R.success();
        }
        return R.fail();
    }



    @GetMapping("/getNewJobHunter")
    @ApiOperation("人才推荐官查询导入简历")
    @Log(title = "人才推荐官-导入简历", businessType = BusinessType.SELECT)
    public R<InsertJobHunterVO> getNewJobHunter(){
        Object o = redisUtils.get(Cache.NEW_JOB_HUNTER.getKey()+companyMetadata.userid());
        if(StringUtils.isEmpty(o)){
            return R.success();
        }
        InsertJobHunterVO insertJobHunterVO = JSON.parseObject(o.toString(), InsertJobHunterVO.class);
        return R.success(insertJobHunterVO);
    }
    @GetMapping("/delNewJobHunter")
    @ApiOperation("人才推荐官删除导入简历")
    @Log(title = "人才推荐官删除导入简历", businessType = BusinessType.DELETE)
    public R delNewJobHunter(){
        redisUtils.del(Cache.NEW_JOB_HUNTER.getKey()+companyMetadata.userid());
        return R.success();
    }
    @PostMapping("/addCacheNewJobHunter")
    @ApiOperation("人才推荐官缓存导入简历")
    @Log(title = "人才推荐官缓存导入简历", businessType = BusinessType.INSERT)
    public R addCacheNewJobHunter(@RequestBody InsertJobHunterVO insertJobHunterVO){


        if( StringUtils.isEmpty(insertJobHunterVO.getUserName()) ||  StringUtils.isEmpty(insertJobHunterVO.getSex()) ||  StringUtils.isEmpty(insertJobHunterVO.getBirthday()) ||  StringUtils.isEmpty(insertJobHunterVO.getPhone())
                ||  StringUtils.isEmpty(insertJobHunterVO.getUserType())||  StringUtils.isEmpty(insertJobHunterVO.getAddress()) ||  StringUtils.isEmpty(insertJobHunterVO.getCurrentSalary()) ||  StringUtils.isEmpty(insertJobHunterVO.getJobStatus()) ||
        StringUtils.isEmpty(insertJobHunterVO.getEmail())){
            return R.fail("请完善基本信息");
        }else {
                insertJobHunterVO.setProcess(1);

        }
        if(! StringUtils.isEmpty(insertJobHunterVO.getAdvantage())){
            if(insertJobHunterVO.getProcess()==1){
                insertJobHunterVO.setProcess(2);
            }

        }
        if(insertJobHunterVO.getTJobhunterEducationRecordVO()!=null && insertJobHunterVO.getTJobhunterEducationRecordVO().size()!=0){
            if(insertJobHunterVO.getProcess()==2){
                insertJobHunterVO.setProcess(3);
            }
        }
        if(insertJobHunterVO.getTJobhunterExpectPositionVO() !=null && insertJobHunterVO.getTJobhunterExpectPositionVO().size()!=0){
            if(insertJobHunterVO.getProcess()==3){
                insertJobHunterVO.setProcess(4);
            }
        }
        if(insertJobHunterVO.getJobhunterWorkRecordVO() !=null && insertJobHunterVO.getJobhunterWorkRecordVO().size()!=0){
            if(insertJobHunterVO.getProcess()==4){
                insertJobHunterVO.setProcess(5);
            }
        }
        String userid =  Cache.NEW_JOB_HUNTER.getKey()+ companyMetadata.userid();
        boolean set = redisUtils.set(userid, JSON.toJSONString(insertJobHunterVO));
        if(set){
            return R.success("缓存成功！");
        }
        return R.fail("缓存失败！");
    }


    @ApiOperation("人才推荐官-导入简历")
    @PostMapping("/addJobHunter")
    @Transactional(rollbackFor = Exception.class)
    @Log(title = "人才推荐官-导入简历", businessType = BusinessType.INSERT)
    public R insertJobHunter(@Valid @RequestBody InsertJobHunterVO insertJobHunterVO) throws Exception {

        String userid = companyMetadata.userid();
        if (!"TR".equals(userid.substring(0, 2))) {
            return R.fail("请使用推荐官身份");
        }
        if(insertJobHunterVO.getUserType()==1){
           if( insertJobHunterVO.getJobhunterWorkRecordVO().size()==0){
                return R.fail("请填写工作经验！");
           }
        }
        RLock lock = redissonClient.getLock(Lock.TO_LEAD.getLock() + companyMetadata.userid());
        boolean res = lock.tryLock(2, 10, TimeUnit.SECONDS); //尝试加锁，最多等待1秒，上锁以后100秒自动解锁
        try {
            if (res) {
                TJobhunter tJobhunter = new TJobhunter();
                tJobhunter.setUserId(idGenerator.generateCode("TJ"));
                BeanUtils.copyProperties(insertJobHunterVO, tJobhunter);
                if (insertJobHunterVO.getCurrentSalary() == null) {
                    tJobhunter.setCurrentSalary(BigDecimal.valueOf(0.00));
                }
                tJobhunter.setJobStatus(insertJobHunterVO.getJobStatus().toString());
                tJobhunter.setSharePerson(companyMetadata.userid());
                tJobhunter.setUpdateTime(LocalDateTime.now());
                tJobhunter.setCreateTime(LocalDateTime.now());
                tJobhunter.setCreateId(companyMetadata.userid());
               if(tJobhunter.getSex()==0){
                   tJobhunter.setAvatar("https://dev-1313175594.cos.ap-guangzhou.myqcloud.com/head/2.png");
               }else {

                   tJobhunter.setAvatar(" https://dev-1313175594.cos.ap-guangzhou.myqcloud.com/head/4.jpg");
               }
                tJobhunter.setUpdateId(companyMetadata.userid());
                tJobhunter.setPerfect(1);
                boolean save = tJobhunterService.save(tJobhunter);
                //创建简历id
                String s = tJobhunterResumeService.addResume(tJobhunter.getUserId(), insertJobHunterVO.getAdvantage());
              /*  if (!StringUtils.isEmpty(insertJobHunterVO.getResumePath())) {
                    Boolean adds = tJobHunterAttachmentService.adds(insertJobHunterVO.getResumePath(), tJobhunter.getUserId());
                }*/
                //添加教育经历
                if (insertJobHunterVO.getTJobhunterEducationRecordVO().size() > 0) {
                    //添加教育经历
                    Boolean adds = tJobhunterEducationRecordService.adds(insertJobHunterVO.getTJobhunterEducationRecordVO(), s, tJobhunter.getUserId());
                }
                if (insertJobHunterVO.getTJobhunterExpectPositionVO().size() > 0) {
                    Boolean adds = tJobhunterExpectPositionService.adds(insertJobHunterVO.getTJobhunterExpectPositionVO(), s);
                }
   /*             if (insertJobHunterVO.getTJobhunterProjectRecordVO().size() > 0) {
                    Boolean adds = tJobhunterProjectRecordService.adds(insertJobHunterVO.getTJobhunterProjectRecordVO(), s);
                }*/

                if (insertJobHunterVO.getJobhunterWorkRecordVO().size() > 0) {
                    Boolean adds = tJobhunterWorkRecordService.adds(insertJobHunterVO.getJobhunterWorkRecordVO(), s);
                }
                ReferrerRecord referrerRecord = new ReferrerRecord();
                referrerRecord.setTJobHunterId(tJobhunter.getUserId());
                referrerRecord.setRecommendId(companyMetadata.userid());
                referrerRecord.setDownloadIf(1);
                referrerRecord.setDownloadTime(LocalDateTime.now());
                referrerRecord.setCorrelationType(2);
                referrerRecord.setCreateTime(LocalDateTime.now());
                boolean save1 = referrerRecordService.save(referrerRecord);
                String userid1 =  Cache.NEW_JOB_HUNTER.getKey()+ companyMetadata.userid();
                redisUtils.del(userid1);
                return R.success(tJobhunter.getUserId());
            } else {
                return R.fail("请不要重复添加！");
            }
        } finally {
            lock.unlock();
        }

    }

    @ApiOperation("推荐官-修改求职者信息")
    @PostMapping("/rmUpdateJobHunter")
    @Log(title = "推荐官-修改求职者信息", businessType = BusinessType.UPDATE)
    public R rmUpdateJobHunter(@Valid @RequestBody RMUpdateJobhunterVO rmUpdateJobhunterVO) {
        Boolean r = referrerRecordService.getCorrelationType(companyMetadata.userid(), rmUpdateJobhunterVO.getUserId());
        if (!r) {
            return R.fail("求职者不存在!");
        }
        TJobhunter tJobhunter = new TJobhunter();
        BeanUtils.copyProperties(rmUpdateJobhunterVO, tJobhunter);
        tJobhunter.setJobStatus(rmUpdateJobhunterVO.getJobStatus().toString());
        tJobhunter.setUserType(rmUpdateJobhunterVO.getUserType()+"");
        tJobhunter.setUpdateId(companyMetadata.userid());
        tJobhunter.setUpdateTime(LocalDateTime.now());
        boolean update = tJobhunterService.updateById(tJobhunter);
        if (update) {
            return R.success("更新成功!");
        }
        return R.fail("更新失败!");
    }


    @ApiOperation("推荐官-获取求职者详情")
    @PostMapping("/selectJobHunter")
    @Log(title = "推荐官-获取求职者详情", businessType = BusinessType.SELECT)
    public R<RmJobHunterDTO> selectJobHunter(@RequestParam("JobHunterId") String JobHunterId) {
        RmJobHunterDTO rmJobHunterDTO = tJobhunterService.selectJobHunter(JobHunterId, companyMetadata.userid());
        String name="";
        String phone="";
        if (rmJobHunterDTO != null) {

            RecommendJobHunter recommendJobHunter = recommendJobHunterService.selectById(companyMetadata.userid(), JobHunterId);
            if (recommendJobHunter == null) {
                name=rmJobHunterDTO.getUserName();
                phone=rmJobHunterDTO.getPhone();
                rmJobHunterDTO.setDownloadIf(0);
                rmJobHunterDTO.setPhone(PhoneUtils.hidePhoneByRegular(rmJobHunterDTO.getPhone()));
                rmJobHunterDTO.setUserName(NameUtils.createAsterisk(rmJobHunterDTO.getUserName(), 1));
            }else {
                rmJobHunterDTO.setDownloadIf(1);
            }

        }

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("t_job_hunter_id", JobHunterId);
        queryWrapper.eq("recommend_id", companyMetadata.userid());
        queryWrapper.eq("deleted", 0);
        ReferrerRecord one = referrerRecordService.getOne(queryWrapper);
        if (one != null) {
            if( one.getCorrelationType()==2){
                rmJobHunterDTO.setPhone(phone);
                rmJobHunterDTO.setUserName(name);
            }
            rmJobHunterDTO.setCorrelationType(one.getCorrelationType());
            rmJobHunterDTO.setReferrerRecordId(one.getReferrerRecordId());
        }
        return R.success(rmJobHunterDTO);
    }

    /**
     * 无忧币计算
     *
     * @param
     * @return
     */
/*    public Map<String, Integer> currencyCount(BigDecimal money) {
        ValueOperations<String, TPayConfig> value = redisTemplate.opsForValue();
        TPayConfig tPayConfig = value.get(RedisCache.TPAYCONFIG.getValue());//获取无忧币分成机制
        if (tPayConfig == null) {
            tPayConfig = tPayConfigApi.select();
        }
        Map<String, Integer> map = new ConcurrentHashMap<>(2);
        if (money.compareTo(new BigDecimal(50)) == 1) {//年薪大于50万
            map.put("currencyCount", tPayConfig.getGradeE());
            map.put("gold", tPayConfig.getGoldC());
            return map;
        } else if (money.compareTo(new BigDecimal(30)) == 1 || money.compareTo(new BigDecimal(30)) == 0) {//年薪大于30万
            map.put("currencyCount", tPayConfig.getGradeE());
            map.put("gold", tPayConfig.getGoldB());
            return map;
       *//* } else if (money.compareTo(new BigDecimal(20)) == 1) { //年薪大于20万
            return tPayConfig.getGradeC();
        } else if (money.compareTo(new BigDecimal(10)) == 1) { //年薪大于10万
            return tPayConfig.getGradeB();
        } else if (-1 == money.compareTo(new BigDecimal(10)) || 0 == money.compareTo(new BigDecimal(10))) { //年薪小于10万
            return tPayConfig.getGradeA();*//*
        }
        map.put("currencyCount", tPayConfig.getGradeA());
        map.put("gold", tPayConfig.getGoldA());
        return map;
        //  return tPayConfig.getGradeA();

    }*/
    @GetMapping("/payResume")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "jobHunter", required = true, value = "求职者id"),
            @ApiImplicitParam(name = "paymentType", required = true, value = "支付方式(0:无忧币 1:金币)"),
            @ApiImplicitParam(name = "postType", required = false)
    })
    public Map<String, Object> payResume(@RequestParam("jobHunter") String jobHunter, @RequestParam("paymentType") Integer paymentType, @RequestParam("postType") Integer postType) throws Exception {

        String userid = companyMetadata.userid();
        TJobhunter byId = tJobhunterService.getById(jobHunter);
        if (byId.getCurrentSalary() == null) {
            return null;
        }
        BigDecimal currentSalary = byId.getCurrentSalary();

        //获取简历价格
        Map<String, Integer> map1 = null;
        Map<String, BigDecimal> stringBigDecimalMap = null;
        BigDecimal bigDecimal = new BigDecimal(0);
        if (postType >= 4) {
            stringBigDecimalMap = divideIntoService.resumePayment(byId.getEducation(), currentSalary, postType);
            bigDecimal = stringBigDecimalMap.get("currencyCount");
        } else {
            map1 = divideIntoService.currencyCount(currentSalary);
            bigDecimal = new BigDecimal(map1.get("currencyCount"));
        }
        if (paymentType == 0) {
            Map<String, Object> map2 = tCompanyService.deductCurrency(bigDecimal, userid, jobHunter, null);
            map2.put("currencyCount", bigDecimal);
            map2.put("jobHunter", byId.getUserId());
            map2.put("userName", byId.getUserName());
            if (byId.getWorkTime() != null) {
                map2.put("workTime", byId.getWorkTime());
            }
            map2.put("education", byId.getEducation());
            map2.put("birthday", byId.getBirthday());
            map2.put("avatar", byId.getAvatar());
            map2.put("currentSalary", byId.getCurrentSalary());
            return map2;
        } else if (paymentType == 1) {
            return tCompanyService.deductGold(new BigDecimal(map1.get("gold")), userid, jobHunter, null);
        }
        return null;
    }

    @GetMapping("/selectId")
    public TJobhunter selectId(@RequestParam("jobHunter") String jobHunter) {
        return tJobhunterService.getById(jobHunter);
    }


    @GetMapping("/showSex")
    @ApiOperation("是否显示全名（0不显示 1:显示）")
    public R showSex() {
        TJobhunter byId = tJobhunterService.getById(companyMetadata.userid());
        if (byId == null) {
            return R.fail("未查到该信息!");
        }
        byId.setShowSex(byId.getShowSex() == 1 ? 0 : 1);
        boolean b = tJobhunterService.updateById(byId);
        if (b) {
            return R.success(byId.getShowSex());
        }
        return R.fail("更新失败!");
    }

    @PostMapping("insertApp")
    @ApiOperation("app+求职者填写简历")
    @Log(title = "app+求职者填写简历", businessType = BusinessType.UPDATE)
    @Transactional(rollbackFor = Exception.class)
    public R insertApp(@Valid @RequestBody InsertJobHunterAppVO insertJobHunterAppVO) {
        TJobhunter tJobhunter = new TJobhunter();
        BeanUtils.copyProperties(insertJobHunterAppVO, tJobhunter);
        tJobhunter.setUserId(companyMetadata.userid());
        tJobhunter.setUpdateTime(LocalDateTime.now());
        boolean b = tJobhunterService.updateById(tJobhunter);
        if (b) {
            //更新个人优势
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("t_jobhunter_id", companyMetadata.userid());
            TJobhunterResume one = tJobhunterResumeService.getOne(queryWrapper);
            if (one == null) {
                String s = tJobhunterResumeService.addResume(companyMetadata.userid());
                one.setResumeId(s);
            }
            LambdaUpdateWrapper<TJobhunterResume> lambdaUpdateWrapper = new LambdaUpdateWrapper();
            lambdaUpdateWrapper.eq(TJobhunterResume::getResumeId, one.getResumeId());
            lambdaUpdateWrapper.set(TJobhunterResume::getAdvantage, JSON.toJSONString(insertJobHunterAppVO.getAdvantage()));
            lambdaUpdateWrapper.set(TJobhunterResume::getUpdateTime, LocalDateTime.now());
            boolean update = tJobhunterResumeService.update(lambdaUpdateWrapper);
            if (update) {
                /**
                 * 更新工作简历
                 */
                TJobhunterWorkRecord tJobhunterWorkRecord = new TJobhunterWorkRecord();
                BeanUtils.copyProperties(insertJobHunterAppVO, tJobhunterWorkRecord);
                tJobhunterWorkRecord.setPositionName(insertJobHunterAppVO.getWorkPositionName());
                tJobhunterWorkRecord.setEndTime(insertJobHunterAppVO.getWorkEndTime());
                tJobhunterWorkRecord.setStartTime(insertJobHunterAppVO.getWorkStartTime());
                tJobhunterWorkRecord.setResumeId(one.getResumeId());
                tJobhunterWorkRecord.setCreateTime(LocalDateTime.now());
                tJobhunterWorkRecord.setWorkId(idGenerator.generateCode("JW"));
                boolean save = tJobhunterWorkRecordService.save(tJobhunterWorkRecord);
                if (save) {
                    //添加教育简历
                    TJobhunterEducationRecord tJobhunterEducationRecord = new TJobhunterEducationRecord();
                    tJobhunterEducationRecord.setResumeId(one.getResumeId());
                    BeanUtils.copyProperties(insertJobHunterAppVO, tJobhunterEducationRecord);
                    tJobhunterEducationRecord.setCreateTime(LocalDateTime.now());
                    tJobhunterEducationRecord.setEduId(idGenerator.generateCode("ER"));
                    boolean save1 = tJobhunterEducationRecordService.save(tJobhunterEducationRecord);
                    if (save1) {
                        /**
                         * 添加求职期望
                         */
                        TJobhunterExpectPosition tJobhunterExpectPosition = new TJobhunterExpectPosition();
                        tJobhunterExpectPosition.setResumeId(one.getResumeId());
                        BeanUtils.copyProperties(insertJobHunterAppVO, tJobhunterExpectPosition);
                        tJobhunterExpectPosition.setCreateTime(LocalDateTime.now());
                        tJobhunterExpectPosition.setPostionId(idGenerator.generateCode("PT"));
                        boolean save2 = tJobhunterExpectPositionService.save(tJobhunterExpectPosition);
                        if (save) {
                            return R.success();
                        }
                    }

                }
            }


        }

        return R.fail();
    }

    @PostMapping("/updateJobHunterApp")
    @ApiOperation("app+求职者修改简历")
    @Log(title = "app+求职者修改简历", businessType = BusinessType.UPDATE)
    public R UpdateJobHunterApp(@Valid @RequestBody UpdateJobHunterAppVO updateJobHunterAppVO) {
        TJobhunter tJobhunter = new TJobhunter();
        BeanUtils.copyProperties(updateJobHunterAppVO, tJobhunter);
        tJobhunter.setUserType(updateJobHunterAppVO.getUserType() + "");
        tJobhunter.setUserId(companyMetadata.userid());
        tJobhunter.setUpdateTime(LocalDateTime.now());
        boolean b = tJobhunterService.updateById(tJobhunter);
        if (b) {
            return R.success("修改成功！");
        }
        return R.fail("修改失败！");
    }

    @GetMapping("/lists")
    public List<TJobhunter> lists(@RequestParam("list") List<String> list) {
        List<TJobhunter> tJobhunters = new ArrayList<>(list.size());
        for (String s : list) {
            TJobhunter tJobhunter = cacheClient.queryWithLogicalExpireCount(Cache.CACHE_JOB_HUNTER.getKey(), s, TJobhunter.class, tJobhunterService::getById, 2L, TimeUnit.MINUTES);
            if (tJobhunter != null) {
                tJobhunters.add(tJobhunter);
            }
        }
        return tJobhunters;
    }

    @PostMapping("/companyInviteJobHunter")
    @ApiOperation("企业查询邀请的求职者")
    @Log(title = "企业查询邀请的求职者", businessType = BusinessType.SELECT)
    public R<ReferrerRecordJobHunterDTO> companyInviteJobHunter(@RequestBody CompanyInviteJobHunterQuery companyInviteJobHunterQuery) {
        companyInviteJobHunterQuery.setUserId(companyMetadata.userid());
        if (!StringUtils.isEmpty(companyInviteJobHunterQuery.getAge())) {
          /*  int currentYear = java.time.Year.now().getValue();
            int birthYear = currentYear - companyInviteJobHunterQuery.getAge();
            companyInviteJobHunterQuery.setAge(birthYear);*/
            Map<String, Date> birthday = BirthdayUtils.getBirthday(companyInviteJobHunterQuery.getAge());
            companyInviteJobHunterQuery.setStartTime(birthday.get("startTime"));
            companyInviteJobHunterQuery.setEndTime(birthday.get("endTime"));

        }
        if (!StringUtils.isEmpty(companyInviteJobHunterQuery.getAp())) {
            if (companyInviteJobHunterQuery.getAp() == 0) {
                companyInviteJobHunterQuery.setApBeginDate(LocalDate.now());
            }
            if (companyInviteJobHunterQuery.getAp() == 1) {
                LocalDate localDate = LocalDate.now().plusDays(-7);
                companyInviteJobHunterQuery.setApBeginDate(LocalDate.now());
                companyInviteJobHunterQuery.setApEndDate(localDate);
            }
            if (companyInviteJobHunterQuery.getAp() == 2) {
                LocalDate localDate = LocalDate.now().plusDays(-30);
                companyInviteJobHunterQuery.setApBeginDate(LocalDate.now());
                companyInviteJobHunterQuery.setApEndDate(localDate);
            }
            if (companyInviteJobHunterQuery.getAp() == 3) {
                LocalDate localDate = LocalDate.now().plusDays(-180);
                companyInviteJobHunterQuery.setApBeginDate(LocalDate.now());
                companyInviteJobHunterQuery.setApEndDate(localDate);
            }
        }
        IPage<ReferrerRecordJobHunterDTO> companyInviteJobHunter = tJobhunterService.companyInviteJobHunter(companyInviteJobHunterQuery);
        return R.success(companyInviteJobHunter);
    }

    @GetMapping("/companyInvitationData")
    @ApiOperation("企业查询邀请数据")
    @Log(title = "企业查询邀请数据", businessType = BusinessType.SELECT)
    public R companyInvitationData() {
        List<Integer> stringList = tJobhunterService.companyInvitationData(companyMetadata.userid());
        return R.success(stringList);
    }

    @GetMapping("/jobHunterRepeat")
    @ApiOperation("求职者查重")
    @Log(title = "求职者查重", businessType = BusinessType.SELECT)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", required = true, value = "电话号码"),
            @ApiImplicitParam(name = "name", required = true, value = "姓名"),
            @ApiImplicitParam(name = "companyName", required = false, value = "企业"),
            @ApiImplicitParam(name = "companyNameA", required = false, value = "企业")
    })
    public R jobHunterRepeat(@RequestParam(value = "name") String name, @RequestParam("phone") String phone, @RequestParam(value = "companyName", required = false) String companyName, @RequestParam(value = "companyNameA", required = false) String companyNameA) {
        LambdaQueryWrapper<TJobhunter> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TJobhunter::getUserName, name);
        queryWrapper.eq(TJobhunter::getPhone, phone);
        List<TJobhunter> list = tJobhunterService.list(queryWrapper);
        if (list == null || list.size() == 0) {
            return R.success(false);
        }

        if (!StringUtils.isEmpty(companyName) || !StringUtils.isEmpty(companyNameA)) {
            List list1 = new ArrayList(list.size());

            for (TJobhunter tJobhunter : list) {
                list1.add(tJobhunter.getUserId());
            }
            QueryWrapper queryWrapper1 = new QueryWrapper();
            queryWrapper1.in("t_jobhunter_id", list1);
            List<TJobhunterResume> list2 = tJobhunterResumeService.list(queryWrapper1);
            if (list2 == null || list2.size() == 0) {
                return R.success(false);
            }
            List list3 = new ArrayList(list2.size());
            for (TJobhunterResume tJobhunterResume : list2) {
                list3.add(tJobhunterResume.getResumeId());
            }

            LambdaQueryWrapper<TJobhunterWorkRecord> queryWrapper2 = new LambdaQueryWrapper<>();
            queryWrapper2.in(TJobhunterWorkRecord::getResumeId, list3);
            if (!StringUtils.isEmpty(companyName) && !StringUtils.isEmpty(companyNameA)) {
                queryWrapper2.in(TJobhunterWorkRecord::getCompanyName, companyNameA, companyName);
            } else if (StringUtils.isEmpty(companyName) && !StringUtils.isEmpty(companyNameA)) {
                queryWrapper2.in(TJobhunterWorkRecord::getCompanyName, companyNameA);
            } else if (!StringUtils.isEmpty(companyName) && StringUtils.isEmpty(companyNameA)) {
                queryWrapper2.in(TJobhunterWorkRecord::getCompanyName, companyName);
            }
            int count1 = tJobhunterWorkRecordService.count(queryWrapper2);
            return R.success(count1 > 0 ? true : false);
        } else {
            return R.success(true);
        }

    }

    @PostMapping("/createJobHunter")
    @ApiOperation("创建求职者简历")
    @Log(title = "创建求职者简历", businessType = BusinessType.INSERT)
    public R createJobHunter(@Valid @RequestBody CreateJobHunterVO createJobHunterVO) {
        String userid = companyMetadata.userid();
        TJobhunter byId = tJobhunterService.getById(userid);
        if (byId == null) {
            return R.fail("该求职者不存在！");
        }
        BeanUtils.copyProperties(createJobHunterVO, byId);
        byId.setUserId(userid);
        byId.setEducation(createJobHunterVO.getEducation() + "");
        byId.setPhone(byId.getLoginName());
        byId.setJobStatus(createJobHunterVO.getJobStatus() + "");
        byId.setUserType(createJobHunterVO.getUserType() + "");
        boolean update = tJobhunterService.updateById(byId);
        return R.success(update);
    }

}