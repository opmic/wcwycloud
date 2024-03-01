package com.wcwy.company.controller;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wcwy.common.base.enums.*;
import com.wcwy.common.base.enums.Lock;
import com.wcwy.common.base.result.R;
import com.wcwy.common.base.utils.*;
import com.wcwy.common.base.utils.UUID;
import com.wcwy.common.redis.entity.RedisData;
import com.wcwy.common.redis.enums.*;
import com.wcwy.common.redis.util.IDGenerator;
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.common.web.annotation.AutoIdempotent;
import com.wcwy.common.web.config.CacheClient;
import com.wcwy.company.aliyun.SendSms;
import com.wcwy.company.asyn.InformAsync;
import com.wcwy.company.asyn.RegisterAsync;
import com.wcwy.company.asyn.RunningWaterAsync;
import com.wcwy.company.config.CosUtils;
import com.wcwy.company.dto.CompanyCollerctPutInResume;
import com.wcwy.company.dto.CompanyHomeDTO;
import com.wcwy.company.dto.SendAResumeRecord;
import com.wcwy.company.po.TCompanyBasicInformation;
import com.wcwy.company.produce.MessageProduce;
import com.wcwy.company.produce.TCompanyProduce;
import com.wcwy.company.produce.UpdateTCompanyPostProduce;
import com.wcwy.post.api.SendMessageApi;
import com.wcwy.post.api.TCompanyPostApi;
import com.wcwy.post.api.TPostShareApi;
import com.wcwy.post.entity.GoldConfig;
import com.wcwy.post.pojo.TCompanyTotalPostSharePOJO;
import com.wcwy.company.entity.*;
import com.wcwy.company.po.TCompanyPO;
import com.wcwy.company.query.TCompanyQuery;
import com.wcwy.company.service.*;
import com.wcwy.company.session.CompanyMetadata;
import com.wcwy.company.vo.*;
import com.wcwy.system.annotation.Log;
import com.wcwy.system.enums.BusinessType;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * ClassName: TCompanyController
 * Description: 企业信息接口
 * date: 2022/9/1 10:00
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@RestController
@RequestMapping("/company")
@Api(tags = "企业信息接口")
@Slf4j
public class TCompanyController {
    @Autowired
    private CompanyMetadata companyMetadata;
    @Autowired
    private IDGenerator idGenerator;
    @Autowired
    private SendMessageApi sendMessageApi;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private UpdateTCompanyPostProduce updateTCompanyPostProduce;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private TCompanyRoleService tCompanyRoleService;
    @Autowired
    private RunningWaterService runningWaterService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    @Autowired
    private TCompanyService tCompanyService;
    @Autowired
    private RegisterAsync registerAsync;
    @Autowired
    private RunningWaterAsync runningWaterAsync;
    @Autowired
    private TRecommendService tRecommendService;
    @Autowired
    private CollerctPostService collerctPostService;
    @Autowired
    private PutInResumeService putInResumeService;
    @Autowired
    private TJobhunterService tJobhunterService;
    @Autowired
    private CosUtils cosUtils;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private SendSms sendSms;
    @Autowired
    private TPostShareApi tPostShareApi;
    @Autowired
    private TCompanyProduce tCompanyProduce;
    @Autowired
    private TCompanyPostApi tCompanyPostApi;

    @Autowired
    private CacheClient cacheClient;
    @Autowired
    private ReferrerRecordService referrerRecordService;
    @Autowired
    private InformAsync informAsync;
    @Autowired
    private MessageProduce messageProduce;
/*    @Autowired
    private WorkApi workApi;*/
@Autowired
private ThreadPoolExecutor dtpExecutor1;
    @GetMapping("/cc")
    @ApiOperation("测试")
    public R cc(HttpServletRequest request) throws IOException {
        informAsync.beRecommended("TR2303171351261-2","PS2312041444863-9","TJ2211231442113-2");
        return R.success();
    }

    @GetMapping("/longinTimeSum")
    @ApiOperation("登录时间")
    @ApiImplicitParam(name = "second", required = true, value = "时长")
    public void  longinTimeSum(@RequestParam("second") Integer second){
        String userid = companyMetadata.userid();
        dtpExecutor1.execute(() -> {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime localDateTime = LocalDateTime.now();
            String dateStr = localDateTime.format(fmt);
            Object o = redisUtils.get("loginTime:" + userid);
            if(StringUtils.isEmpty(o)){
                redisUtils.set("loginTime:"+userid,dateStr,60*5);

            }else {
                redisUtils.set("loginTime:"+userid,o,60*5);
            }
             redisUtils.incr("loginTimeSum:"+userid,second);
        });
    }

    @PostConstruct
    public void ListCompany() {
        List<TCompany> list = tCompanyService.list();
        for (TCompany tCompany : list) {
            RedisData redisData = new RedisData();
            redisData.setData(tCompany);
            redisData.setExpireTime(LocalDateTime.now().plusSeconds(120));  // 过期时间
            stringRedisTemplate.opsForValue().set(Cache.CACHE_COMPANY.getKey() + tCompany.getCompanyId(), JSONUtil.toJsonStr(redisData));
        }
    }

 /*   @GetMapping("/uuidc")
    @ApiOperation(value = "uuid", notes = "uuid")
    public String uuidc(){
        return idGenerator.generateCode("cc");
    }*/

    @PostMapping("/insertCompany")
    @ApiOperation(value = "企业注册", notes = "企业注册")
    @Transactional(rollbackFor = Exception.class)
    @Log(title = "企业注册", businessType = BusinessType.INSERT)
    @AutoIdempotent
    public R insertCompany(HttpServletRequest request, @Valid @RequestBody TCompanyVo tCompanyVo) throws Exception {

        //验证验证码
  /*      String code = stringRedisTemplate.opsForValue().get(SmsEunm.INSERT_CODE.getType() + tCompanyVo.getPhoneNumber());
        if (StringUtils.isEmpty(code)) {
            // code= stringRedisTemplate.opsForValue().get(SmsEunm.LOGIN_CODE.getType() + tCompanyVo.getPhoneNumber());
            return R.fail("验证码已过期,请重新获取验证码");
        }
        if (!code.equals(tCompanyVo.getCode())) {
            return R.fail("验证码不正确！");
        }*/
        Claims claimsFromJwt = JWT.getClaimsFromJwt(tCompanyVo.getGetKeyRate());
        if (claimsFromJwt == null) {
            return R.fail("操作时间过长，请重新操作!");
        }
        Object phone = claimsFromJwt.get("phone");
        if (StringUtils.isEmpty(phone) || !tCompanyVo.getPhoneNumber().equals(phone)) {
            return R.fail("电话号码不匹配");
        }

        RLock lock = redissonClient.getLock(Lock.INSERT_TC.getLockSion() + tCompanyVo.getPhoneNumber());
        boolean isLock = lock.tryLock(5, 10, TimeUnit.SECONDS);
        if (isLock) {
            try {
                //验证电话号码格式
                if (!PhoneUtil.isMobileNumber(tCompanyVo.getContactPhone())) {
                    return R.fail("联系方式格式不正确");
                }
                //查询用户是否存在
                QueryWrapper<TCompany> queryWrapper = new QueryWrapper();
                queryWrapper.eq("login_name", tCompanyVo.getPhoneNumber());
                TCompany phone_number = tCompanyService.getOne(queryWrapper);
                if (phone_number != null) {
                    return R.fail("该电话号码已被使用！");
                }
                TCompany tCompany = new TCompany();
                BeanUtils.copyProperties(tCompanyVo, tCompany);
                tCompany.setAvatar("https://dev-1313175594.cos.ap-guangzhou.myqcloud.com/head/0.png");
                tCompany.setPassword(bCryptPasswordEncoder.encode(tCompanyVo.getPassword()));
                tCompany.setCompanyId(idGenerator.generateCode("TC"));
                tCompany.setExamineStatus(2);//
                tCompany.setStatus("0");
                tCompany.setDeleted(0);
                tCompany.setGradeIntegral(0L);
                tCompany.setCurrencyCount(new BigDecimal("0"));
                GoldConfig o1 = (GoldConfig) redisUtils.get(RedisCache.GOLD_CACHE.getValue());
                tCompany.setGold(new BigDecimal(o1.getRegisterGold()));
                tCompany.setLoginName(tCompanyVo.getPhoneNumber());
                tCompany.setCreateTime(LocalDateTime.now());
                if (!StringUtils.isEmpty(tCompanyVo.getInvitationCode())) {
                    Object o = redisUtils.get(InvitationCode.INVITATION_URL_CODE.getType() + tCompanyVo.getInvitationCode());
                    if (o != null) {
                        String ipAddr = IpUtils.getIpAddr(request);
                        registerAsync.register(ipAddr, tCompanyVo.getCompanyType() == 0 ? 3 : 2, o.toString());
                        tCompany.setSharePerson(o.toString());
                        runningWaterAsync.inviter_company(o.toString());
                        sendSms.registerInform(o.toString(), AccessTemplateCode.INFORM_COMPANY.getName(), AccessTemplateCode.INFORM_COMPANY.getDesc(), tCompanyVo.getCompanyName());
                    }
                }
                boolean save = tCompanyService.save(tCompany);
                if (save) {
                    boolean isok = tCompanyRoleService.add(tCompany.getCompanyId());
                    if (isok) {
                        runningWaterAsync.add(o1.getRegisterGold(), tCompany.getCompanyId(), GoldExplain.REGISTER_GOLD.getValue());
                        Map map = new HashMap();
                        map.put("company_id", tCompany.getCompanyId());
                        map.put("company_name", tCompany.getCompanyName());
                        map.put("logo_path", tCompany.getLogoPath());
                        map.put("company_type_id", tCompany.getCompanyTypeId());
                        map.put("industry", tCompany.getIndustry());
                        map.put("firm_size", tCompany.getFirmSize());
                        map.put("company_type", tCompany.getCompanyType());
                        String toJSONString = JSON.toJSONString(map);
                        tCompanyProduce.sendSyncMessageCompanyHot(toJSONString);
                        //注册成功删除验证码
                        stringRedisTemplate.delete(SmsEunm.INSERT_CODE.getType() + tCompanyVo.getPhoneNumber());
                        String ipAddr = IpUtils.getIpAddr(request);
                        registerAsync.register(ipAddr, 0);

                        return R.success("注册成功");
                    }
                }
            } finally {
                // 释放锁
                lock.unlock();
            }
        }

        return R.fail("注册失败！");
    }


    @PostMapping("/insertCompanySubsidiaryVo")
    @ApiOperation(value = "子企业注册", notes = "子企业注册")
    @Transactional
    @AutoIdempotent
    public R insertCompanySubsidiaryVo(@Valid @RequestBody TCompanySubsidiaryVo tCompanyVo) throws Exception {

        if (StringUtils.isEmpty(tCompanyVo.getParentId()) || StringUtils.isEmpty(tCompanyVo.getSubsidiary())) {
            return R.fail("绑定账号不能为空");
        }
        RLock lock = redissonClient.getLock(Lock.INSERT_TC.getLockSion() + tCompanyVo.getPhoneNumber());
        boolean isLock = lock.tryLock(5, 10, TimeUnit.SECONDS);
        if (isLock) {
            try {
                //验证电话号码格式
                if (!PhoneUtil.isMobileNumber(tCompanyVo.getContactPhone())) {
                    return R.fail("联系方式格式不正确");
                }

                //查询用户是否存在
                QueryWrapper<TCompany> queryWrapper = new QueryWrapper();
                queryWrapper.eq("login_name", tCompanyVo.getPhoneNumber());
                TCompany phone_number = tCompanyService.getOne(queryWrapper);
                if (phone_number != null) {
                    return R.fail("该电话号码已被使用！");
                }
                TCompany tCompany = new TCompany();
                tCompanyVo.setAvatar("https://dev-1313175594.cos.ap-guangzhou.myqcloud.com/head/0.png");
                BeanUtils.copyProperties(tCompanyVo, tCompany);
                tCompany.setPassword(bCryptPasswordEncoder.encode(tCompanyVo.getPassword()));
                tCompany.setCompanyId(idGenerator.generateCode("TC"));
                tCompany.setExamineStatus(2);//
                tCompany.setStatus("0");
                tCompany.setDeleted(0);
                tCompany.setGradeIntegral(0L);
                tCompany.setCurrencyCount(new BigDecimal("0"));
                tCompany.setLoginName(tCompanyVo.getPhoneNumber());
                GoldConfig o1 = (GoldConfig) redisUtils.get(RedisCache.GOLD_CACHE.getValue());
                tCompany.setCreateTime(LocalDateTime.now());
                boolean save = tCompanyService.save(tCompany);
                if (save) {
                    boolean isok = tCompanyRoleService.add(tCompany.getCompanyId());
                    if (isok) {
                        runningWaterAsync.add(o1.getRegisterGold(), tCompany.getCompanyId(), GoldExplain.REGISTER_GOLD.getValue());
                        Map map = new HashMap();
                        map.put("company_id", tCompany.getCompanyId());
                        map.put("company_name", tCompany.getCompanyName());
                        map.put("logo_path", tCompany.getLogoPath());
                        map.put("company_type_id", tCompany.getCompanyTypeId());
                        map.put("industry", tCompany.getIndustry());
                        map.put("firm_size", tCompany.getFirmSize());
                        map.put("company_type", tCompany.getCompanyType());
                        String toJSONString = JSON.toJSONString(map);
                        tCompanyProduce.sendSyncMessageCompanyHot(toJSONString);
                        //注册成功删除验证码
                        stringRedisTemplate.delete(SmsEunm.INSERT_CODE.getType() + tCompanyVo.getPhoneNumber());
                        return R.success("添加成功");
                    }
                }
            } finally {
                // 释放锁
                lock.unlock();
            }
        }

        return R.fail("添加失败");
    }


    @GetMapping("/selectOne")
    @ApiOperation(value = "通过token获取企业信息")
    @Log(title = "通过token获取企业信息", businessType = BusinessType.SELECT)
    public R<TCompany> selectOne() throws Exception {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String userid = companyMetadata.userid();
        if (null == userid && "".equals(userid)) {
            return R.fail("系统错误！请联系管理员");
        }
        Boolean flag = valueOperations.setIfAbsent(Sole.LOGIN_USER.getKey() + userid, userid, 60 * 30, TimeUnit.SECONDS);
        if (flag) {
            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("company_id", userid);
            updateWrapper.set("login_time", LocalDateTime.now());
            boolean update = tCompanyService.update(updateWrapper);
        }
        TCompany byId = tCompanyService.getId(userid);
        if (null == byId) {
            return R.fail("无效token");
        }


        byId.setPassword("草泥马！敢偷窥密码");
       /* byId.setBindWechat(false);
        if(! StringUtils.isEmpty(byId.getOpenid())){
            byId.setBindWechat(true);
            byId.setOpenid("");
        }*/
        //如果是子账号则获取主账号的营业执照及企业简介
      /*  if(byId.getSubsidiary()==1){
            if(! StringUtils.isEmpty(byId.getParentId())){
                TCompany id = tCompanyService.getId(byId.getParentId());
                if(id !=null){
                    byId.setLogoPath(byId.getLogoPath());
                }
            }

        }*/
        List<String> authorization = tCompanyService.authorization(byId.getLoginName());
        byId.setAuthorization(authorization);
        runningWaterAsync.loginGold(userid);
        return R.success(byId);
    }

    @PostMapping("/selectList")
    @ApiOperation(value = "查询企业信息")
    @Log(title = "查询企业信息", businessType = BusinessType.SELECT)
    public R<TCompanyPO> selectList(@RequestBody TCompanyQuery query) {
        IPage<TCompanyPO> page = tCompanyService.pageList(query);
        return R.success(page);
    }

    @PostMapping("/updatePassword")
    @Log(title = "修改密码", businessType = BusinessType.UPDATE)
    @ApiOperation(value = "修改密码", notes = "修改密码")
    @AutoIdempotent
    public R updatePassword(@Valid @RequestBody TCompanyUpdatePasswordVO tCompanyUpdatePasswordVO) {
        String userid = companyMetadata.userid();
        TCompany byId = tCompanyService.getById(userid);
        if (byId == null) {
            return R.fail("没有该用户！");
        }
        ValueOperations<String, String> stringValueOperations = stringRedisTemplate.opsForValue();
        String s = stringValueOperations.get(SmsEunm.PASSWORD_CODE.getType() + tCompanyUpdatePasswordVO.getLoginName());
        if (!tCompanyUpdatePasswordVO.getCode().equals(s)) {
            return R.fail("验证码不正确或已过期!");
        }
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("company_id", userid);
        updateWrapper.set("password", bCryptPasswordEncoder.encode(tCompanyUpdatePasswordVO.getPassword()));
        updateWrapper.set("update_time", LocalDateTime.now());
        boolean update = tCompanyService.update(updateWrapper);
        if (update) {
            stringRedisTemplate.delete(SmsEunm.PASSWORD_CODE.getType() + tCompanyUpdatePasswordVO.getLoginName());
            return R.success("修改成功");
        }
        return R.fail("修改失败!");
    }


    @PostMapping("/updateCompany")
    @Log(title = "企业基本信息更新接口", businessType = BusinessType.UPDATE)
    @ApiOperation(value = "企业基本信息更新接口", notes = "企业基本信息更新接口")
    @AutoIdempotent
    public R update(@Valid @RequestBody TCompanyUpdateVo tCompanyUpdateVo) {
        String userid = companyMetadata.userid();
        if (!userid.equals(tCompanyUpdateVo.getCompanyId())) {
            return R.fail("登录账号不一致!");
        }
        TCompany byId = tCompanyService.getById(userid);
        if (byId == null) {
            return R.fail("该企业不存在！");
        }

        BeanUtils.copyProperties(tCompanyUpdateVo, byId);
        byId.setUpdateTime(LocalDateTime.now());
        boolean b = tCompanyService.updateById(byId);
        if (b) {
            cacheClient.deleteCache(Cache.CACHE_COMPANY.getKey(), userid);
            String toJSONString = JSON.toJSONString(tCompanyUpdateVo);
            updateTCompanyPostProduce.updatePostAsyncMessage(toJSONString);
            return R.success("更新成功！");
        }
        return R.fail("更新失败！");
    }

    /**
     * @param id         企业id
     * @param industryID 求职者id
     * @return null
     * @Description: 通过id查询企业
     * @Author tangzhuo
     * @CreateTime 2022/10/13 15:43
     */
    @GetMapping("/tCompanyById")
    public CompanyCollerctPutInResume tCompanyById(@RequestParam("id") String id, @RequestParam("industry") String industryID, @RequestParam("postID") String postID) {
        /*   CompanyCollerctPutInResume companyIndustryPutInResume = tCompanyService.CompanyIndustryPutInResume(id,industryID);*/

        /*  TCompany byId = tCompanyService.getById(id);*/

        CompanyCollerctPutInResume companyIndustryPutInResume = new CompanyCollerctPutInResume();
        if (!StringUtils.isEmpty(industryID)) {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("post", postID);
            queryWrapper.eq("collerct_user_id", industryID);
            CollerctPost one1 = collerctPostService.getOne(queryWrapper);
            companyIndustryPutInResume.setCollerct(one1 != null);
            if (one1 != null) {
                companyIndustryPutInResume.setCollerctId(one1.getCollerctPostId());
            }

            QueryWrapper queryWrapper1 = new QueryWrapper();
            queryWrapper1.eq("put_in_jobhunter", industryID);
            queryWrapper1.eq("put_in_post", postID);
            queryWrapper1.gt("create_time", DateUtils.getPastDate(7, new Date()));
            int count = putInResumeService.count(queryWrapper1);

            companyIndustryPutInResume.setPutinResume(count > 0);
           /* if (one != null) {
                companyIndustryPutInResume.setPutInResumeId(one.getPutInResumeId());
            }*/
        }
        return companyIndustryPutInResume;
    }


    /**
     * 扣除无忧币
     *
     * @return
     */
  /*   @PostMapping("/deductExpenses")
   public ProportionDTO deductExpenses(@RequestBody ProportionVO proportionVO) throws Exception {
        RLock lock = redissonClient.getLock(Lock.DEDUCT_EXPENSES.getLockSion() + proportionVO.getTCompanyId());
        boolean tryLock = lock.tryLock(10, 20, TimeUnit.SECONDS); //尝试加锁，最多等待1秒，上锁以后10秒自动解锁
        if (tryLock) {
            try {
                ProportionDTO proportionDTO = new ProportionDTO();
                //扣除企业无忧币
                Boolean isOk = tCompanyService.updateMoney(proportionVO.getTCompanyId(), proportionVO.getMoney());
                proportionDTO.setTCompanyId(proportionVO.getTCompanyId());
                proportionDTO.setTRecommend(proportionVO.getTRecommend());
                proportionDTO.setTotalMoney(proportionVO.getMoney());
                proportionDTO.setPaySuccess(false);
                //查找该企业的邀请人
                String sharePerson = null;
              *//*  if (!StringUtils.isEmpty(proportionVO.getTCompanyId())) {
                    TCompany byId = tCompanyService.getById(proportionVO.getTCompanyId());
                    if (null != byId && !StringUtils.isEmpty(byId.getSharePerson())) {
                        sharePerson = byId.getSharePerson();
                    }
                }*//*
                //查询求职者邀请人
                proportionVO.setTRecommend(tJobhunterService.getSharePerson(proportionVO.getJobhunterId()));
                Map<String, Object> stringObjectMap = null;
                if (proportionVO.getIdentification() == 1) {
                    stringObjectMap = DivideIntoUtil.downloadResume( 0.1,proportionVO.getMoney(), proportionVO.getTRecommend(), sharePerson);
                } else if (proportionVO.getIdentification() == 2) {
                    stringObjectMap = DivideIntoUtil.earnestMoney(proportionVO.getMoney(), proportionVO.getTRecommend(), sharePerson);
                }
                if (isOk) {
                    List<RunningWater> list = new ArrayList();
                    //企业无忧币流水
                    RunningWater runningWaterCompany = new RunningWater();
                    runningWaterCompany.setRunningWaterId(idGenerator.generateCode("RW"));
                    runningWaterCompany.setSource(4);
                    runningWaterCompany.setMoney(proportionVO.getMoney());
                    runningWaterCompany.setUserId(proportionVO.getTCompanyId());
                    runningWaterCompany.setIfIncome(1);
                    runningWaterCompany.setOrderId(proportionVO.getOrderId());
                    list.add(runningWaterCompany);
                    proportionDTO.setPlatformMoney((BigDecimal) stringObjectMap.get("platformMoney"));//平台分成
                    //推荐官不为空
                    if (!StringUtils.isEmpty(proportionVO.getTRecommend()) && "TR".equals(proportionVO.getTRecommend().substring(0, 2))) {
                        //推荐人抽成
                        RunningWater runningWater = new RunningWater();
                        runningWater.setRunningWaterId(idGenerator.generateCode("RW"));
                        runningWater.setSource(4);
                        runningWater.setMoney((BigDecimal) stringObjectMap.get(proportionVO.getTRecommend()));
                        runningWater.setUserId(proportionVO.getTRecommend());
                        runningWater.setIfIncome(2);
                        runningWater.setOrderId(proportionVO.getOrderId());
                        proportionDTO.setTRecommend(proportionVO.getTRecommend());//推荐官
                        proportionDTO.setReferrerMoney((BigDecimal) stringObjectMap.get(proportionVO.getTRecommend()));//推荐官的分成
                        list.add(runningWater);
                    }
                    //分享推荐人抽成
                    if (!StringUtils.isEmpty(sharePerson) && "TR".equals(sharePerson.substring(0, 2))) {
                        RunningWater runningWaterSharePerson = new RunningWater();
                        runningWaterSharePerson.setRunningWaterId(idGenerator.generateCode("RW"));
                        runningWaterSharePerson.setSource(4);
                        runningWaterSharePerson.setMoney((BigDecimal) stringObjectMap.get(sharePerson));
                        runningWaterSharePerson.setUserId(sharePerson);
                        runningWaterSharePerson.setIfIncome(2);
                        runningWaterSharePerson.setOrderId(proportionVO.getOrderId());
                        list.add(runningWaterSharePerson);
                        proportionDTO.setSharer(sharePerson);//邀请推荐官的人
                        proportionDTO.setSharerMoney((BigDecimal) stringObjectMap.get(sharePerson));//邀请推荐官的人分成
                    }
                    proportionDTO.setList(list);//存储在返回值里面
                    boolean b1 = false;
                    //更新无忧币
                    //1 推荐官的无忧币
                    if (!StringUtils.isEmpty(proportionVO.getTRecommend()) && "TR".equals(proportionVO.getTRecommend().substring(0, 2))) {
                        b1 = tRecommendService.UpdateCurrencyCount(proportionVO.getTRecommend(), (BigDecimal) stringObjectMap.get(proportionVO.getTRecommend()), 2);
                    } else {
                        b1 = true;
                    }

                    // updateWrapper.eq()
                    //2 分享注册推荐官的无忧币
                    boolean b2 = false;
                    if (!StringUtils.isEmpty(sharePerson) && "TR".equals(sharePerson.substring(0, 2))) {
                        b2 = tRecommendService.UpdateCurrencyCount(sharePerson, (BigDecimal) stringObjectMap.get(sharePerson), 2);
                    } else {
                        b2 = true;
                    }

                    if (b1 && b2) {  //两次都成功了
                        proportionDTO.setPaySuccess(true);
                    }

                } else {
                    proportionDTO.setCause("无忧币不足");
                }

                return proportionDTO;
            } finally {
                lock.unlock();
            }
        }
        return null;
    }*/


    /**
     * 扣除无忧币
     *
     * @return
     */
 /*   @PostMapping("/paymentDivideInto")
    public DeductGold paymentDivideInto(@RequestBody OrderInfo orderInfo) throws Exception {
        RLock lock = redissonClient.getLock(Lock.DEDUCT_EXPENSES.getLockSion() + orderInfo.getCreateId());
        boolean tryLock = lock.tryLock(10, 20, TimeUnit.SECONDS); //尝试加锁，最多等待1秒，上锁以后10秒自动解锁
        DeductGold deductGold = new DeductGold();
        deductGold.setIsOK(false);
        if (tryLock) {
            try {
                //扣除企业无忧币
                Boolean isOk = tCompanyService.updateMoney(orderInfo.getCreateId(), orderInfo.getMoney());
                if (isOk) {
                    List<RunningWater> list = new ArrayList();
                    //企业无忧币流水
                    RunningWater runningWaterCompany = new RunningWater();
                    runningWaterCompany.setRunningWaterId(idGenerator.generateCode("RW"));
                    runningWaterCompany.setSource(4);
                    runningWaterCompany.setMoney(orderInfo.getMoney());
                    runningWaterCompany.setUserId(orderInfo.getCreateId());
                    runningWaterCompany.setIfIncome(1);
                    runningWaterCompany.setOrderId(orderInfo.getOrderId());
                    list.add(runningWaterCompany);
                    //推荐官不为空
                    if (!StringUtils.isEmpty(orderInfo.getRecommendId()) && "TR".equals(orderInfo.getRecommendId().substring(0, 2))) {
                        //推荐人抽成
                        RunningWater runningWater = new RunningWater();
                        runningWater.setRunningWaterId(idGenerator.generateCode("RW"));
                        runningWater.setSource(4);
                        runningWater.setMoney(orderInfo.getReferrerMoney());
                        runningWater.setUserId(orderInfo.getPutInResumeId());
                        runningWater.setIfIncome(2);
                        runningWater.setOrderId(orderInfo.getOrderId());
                        list.add(runningWater);

                    }
                    //分享推荐人抽成
                    if (!StringUtils.isEmpty(orderInfo.getShareUserId()) && "TR".equals(orderInfo.getShareUserId().substring(0, 2))) {
                        RunningWater runningWaterSharePerson = new RunningWater();
                        runningWaterSharePerson.setRunningWaterId(idGenerator.generateCode("RW"));
                        runningWaterSharePerson.setSource(4);
                        runningWaterSharePerson.setMoney(orderInfo.getShareMoney());
                        runningWaterSharePerson.setUserId(orderInfo.getShareUserId());
                        runningWaterSharePerson.setIfIncome(2);
                        runningWaterSharePerson.setOrderId(orderInfo.getOrderId());
                        list.add(runningWaterSharePerson);
                    }
                    deductGold.setList(list);

                    //更新无忧币
                    //1 推荐官的无忧币
                    boolean b1 = false;
                    if (!StringUtils.isEmpty(orderInfo.getPutInResumeId()) && "TR".equals(orderInfo.getPutInResumeId().substring(0, 2))) {
                        b1 = tRecommendService.UpdateCurrencyCount(orderInfo.getPutInResumeId(), orderInfo.getReferrerMoney(), 2);
                    } else {
                        b1 = true;
                    }

                    // updateWrapper.eq()
                    //2 分享注册推荐官的无忧币
                    boolean b2 = false;
                    if (!StringUtils.isEmpty(orderInfo.getShareUserId()) && "TR".equals(orderInfo.getShareUserId().substring(0, 2))) {
                        b2 = tRecommendService.UpdateCurrencyCount(orderInfo.getShareUserId(), orderInfo.getShareMoney(), 2);
                    } else {
                        b2 = true;
                    }
                    if (b1 && b2) {  //两次都成功了
                        deductGold.setIsOK(true);
                    } else {
                        deductGold.setIsOK(false);
                    }


                } else {
                    deductGold.setCause("无忧币不足!");
                }

                return deductGold;
            } finally {
                lock.unlock();
            }
        }
        return null;
    }
*/
    @GetMapping("/getPhone")
    public String selectPhone(@RequestParam("companyId") String companyId) {
        String phoneNumber = tRecommendService.selectPhone(companyId);
        return phoneNumber;
    }

/*
    @ApiOperation("企业修改密码")
    @PostMapping("/updatePassword")
    public R updatePassword(@Valid @RequestBody TJobhunterUpdatePasswordVO tJobhunterUpdatePasswordVO) {
        TCompany byId = tCompanyService.getById(companyMetadata.userid());
        if (byId == null) {
            return R.fail("无此用户");
        }
        boolean matches = bCryptPasswordEncoder.matches(tJobhunterUpdatePasswordVO.getOldCode(), byId.getPassword());
        if (matches) {
            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("company_id", companyMetadata.userid());
            updateWrapper.set("password", bCryptPasswordEncoder.encode(tJobhunterUpdatePasswordVO.getNewPassword()));
            boolean update = tCompanyService.update(updateWrapper);
            if (update) {
                return R.success("更新成功");
            }
        }
        return R.fail("旧密码不正确");
    }*/


    @ApiOperation("企业通过密码更换手机号")
    @PostMapping("/passwordUpdatePhone")
    @AutoIdempotent
    public R passwordUpdatePhone(@Valid @RequestBody PasswordUpdatePhone updatePhone) {
        ValueOperations<String, String> stringValueOperations = stringRedisTemplate.opsForValue();
        if (!companyMetadata.userid().equals(updatePhone.getUserId())) {
            return R.fail("登录与传入id不一致 请检查！");
        }
        TCompany byId = tCompanyService.getById(companyMetadata.userid());
        boolean matches = bCryptPasswordEncoder.matches(updatePhone.getPassword(), byId.getPassword());
        if (!matches) {
            return R.fail("密码不正确!");
        }
        String s = stringValueOperations.get(SmsEunm.BINDING_PHONE.getType() + updatePhone.getNewPhone());
        if (!updatePhone.getCode().equals(s)) {
            return R.fail("验证码不正确或验证码已过期!");
        }
        //查询该手机号码有没有被使用
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("phone_number", updatePhone.getNewPhone());
        int count = tCompanyService.count(queryWrapper);
        if (count > 0) {
            return R.fail("该电话号码已被使用!");
        }
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("company_id", updatePhone.getUserId());
        updateWrapper.set("login_name", updatePhone.getNewPhone());
        updateWrapper.set("phone_number", updatePhone.getNewPhone());
        updateWrapper.set("update_time", LocalDateTime.now());
        boolean update = tCompanyService.update(updateWrapper);
        if (update) {
            stringRedisTemplate.delete(SmsEunm.BINDING_PHONE.getType() + updatePhone.getNewPhone());
            cacheClient.deleteCache(Sole.COMPANY.getKey(), companyMetadata.userid());
            return R.success("更新成功");
        }
        return R.fail("更新失败!");
    }

/*    @ApiOperation("企业更换手机号")
    @PostMapping("/updatePhone")
    public R updatePhone(@Valid @RequestBody UpdatePhone updatePhone) {
        if (!companyMetadata.userid().equals(updatePhone.getUserId())) {
            return R.fail("登录与传入id不一致 请检查！");
        }
        //查询该手机号码有没有被使用
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("phone_number", updatePhone.getNewPhone());
        int count = tCompanyService.count(queryWrapper);
        if (count > 0) {
            return R.fail("该电话号码已被使用!");
        }
        ValueOperations<String, String> stringStringValueOperations = stringRedisTemplate.opsForValue();
        String cc = SmsEunm.UPDATE_PHONE.getType() + updatePhone.getFormerPhone();
        String code1 = stringStringValueOperations.get(cc.trim());
        if (!updatePhone.getFormerCode().equals(code1)) {
            return R.fail("旧电话验证码不正确！");
        }
        // String substring = updatePhone.getUserId().substring(0, 2);
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("company_id", updatePhone.getUserId());
        updateWrapper.set("login_name", updatePhone.getNewPhone());
        updateWrapper.set("phone_number", updatePhone.getNewPhone());
        boolean update = tCompanyService.update(updateWrapper);
        if (update) {
            return R.success("更新成功");
        }
        return R.fail("更新失败!");
    }*/

/*    @ApiOperation("添加合同")
    @PostMapping("/addContract")
    public R addContract(@Valid @RequestBody AddContractVO addContractVO) {
        if (!companyMetadata.userid().equals(addContractVO.getCompanyId())) {
            return R.fail("登录与传入id不一致 请检查！");
        }
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("company_id", addContractVO.getCompanyId());
        updateWrapper.set("contract_date", addContractVO.getContractDate());
        updateWrapper.set("sign_contract", addContractVO.getSignContract().toString());

        boolean update = tCompanyService.update(updateWrapper);
        if (update) {
            return R.success("添加成功!");
        }
        return R.fail("添加失败!请联系管理员");
    }*/


    /*
        @GetMapping("/existContract")
        @ApiOperation("查看合同是否存在或者是是否过期")
        public R existContract() {
            TCompany byId = tCompanyService.getById(companyMetadata.userid());
            if (byId != null) {
                if (StringUtils.isEmpty(byId.getSignContract())) {
                    return R.success("请下载上传您的合同!", false);
                }
                if (!LocalDate.now().isBefore(byId.getContractDate())) {
                    return R.success("您的合同已过期，请重新下载上传！", false);
                }
                return R.success("合同有效", true);
            }
            return R.fail("请求错误!请联系管理员");
        }
    */
    @GetMapping("/sharePerson")
    @ApiOperation("添加邀请人")
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
            updateWrapper.eq("company_id", companyMetadata.userid());
            updateWrapper.set("share_person", sharePerson);

            boolean update = tCompanyService.update(updateWrapper);
            if (update) {
                return R.success("添加成功!");
            }

        }
        return R.fail("请检查账号是否存在!");
    }


    @ApiOperation("修改个人信息")
    @PostMapping("/updatePersonalDetails")
    @Log(title = "修改个人信息", businessType = BusinessType.UPDATE)
    @AutoIdempotent
    public R updatePersonalDetails(@RequestBody UpdatePersonalDetails updatePersonalDetails) {
        String userid = companyMetadata.userid();
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("company_id", userid);
        if (!StringUtils.isEmpty(updatePersonalDetails.getContactName())) {
            updateWrapper.set("contact_name", updatePersonalDetails.getContactName());
        }
        if (!StringUtils.isEmpty(updatePersonalDetails.getJobTitle())) {
            updateWrapper.set("job_title", updatePersonalDetails.getJobTitle());
        }
        if (!StringUtils.isEmpty(updatePersonalDetails.getContactPhone())) {
            updateWrapper.set("contact_phone", updatePersonalDetails.getContactPhone());
        }
        if (!StringUtils.isEmpty(updatePersonalDetails.getDescription())) {
            updateWrapper.set("description", updatePersonalDetails.getDescription());
        }
        updateWrapper.set("update_time", LocalDateTime.now());
        boolean update = tCompanyService.update(updateWrapper);
        if (update) {
            cacheClient.deleteCache(Sole.COMPANY.getKey(), userid);
            return R.success("更新成功!");
        }
        return R.fail("更新失败!");
    }

    @ApiOperation("修改登录手机号码")
    @GetMapping("/updateLoginPhone")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", required = true, value = "电话号码"),
            @ApiImplicitParam(name = "code", required = true, value = "验证码"),
            @ApiImplicitParam(name = "password", required = true, value = "密码")
    })
    @Log(title = "修改登录手机号码", businessType = BusinessType.UPDATE)
    @AutoIdempotent
    public R updateLoginPhone(@RequestParam("phone") String phone, @RequestParam("code") String code, @RequestParam("password") String password) {
        String userid = companyMetadata.userid();
        TCompany byId = tCompanyService.getById(userid);
        boolean matches = bCryptPasswordEncoder.matches(password, byId.getPassword());
        if (!matches) {
            return R.fail("密码不正确!");
        }
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(code)) {
            return R.fail("电话号码及验证码不能为空!");
        }
        boolean mobileNumber = PhoneUtil.isMobileNumber(phone);
        if (!mobileNumber) {
            return R.fail("电话号码格式不正确!");
        }
        //查询该手机号码有没有被使用
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("login_name", phone);
        int count = tCompanyService.count(queryWrapper);
        if (count > 0) {
            return R.fail("该电话号码已被使用!");
        }
        String codePhone = stringRedisTemplate.opsForValue().get(SmsEunm.BINDING_PHONE.getType() + phone);
        if (codePhone == null) {
            return R.fail("验证码不存在!");
        }
        if (code.equals(codePhone)) {
            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("company_id", userid);
            updateWrapper.set("login_name", phone);
            boolean update = tCompanyService.update(updateWrapper);
            if (update) {
                stringRedisTemplate.delete(SmsEunm.BINDING_PHONE.getType() + phone);
                return R.success("修改成功");
            }
            return R.fail("修改失败,请重新修改!");
        }
        return R.fail("验证码不正确");
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
        updateWrapper.eq("company_id", userid);
        updateWrapper.set("openid", "");
        boolean update = tCompanyService.update(updateWrapper);
        if (update) {
            Boolean delete = stringRedisTemplate.delete(SmsEunm.UPDATE_PHONE.getType() + phone);
            return R.success("修改成功!");
        }
        return R.fail("修改失败!");
    }


    @ApiOperation("重置密码")
    @PostMapping("/resetPasswords")
    @Log(title = "重置密码", businessType = BusinessType.UPDATE)
    public R resetPasswords(@Valid @RequestBody TCompanyResetPasswordsVo tCompanyResetPasswordsVo) {
        boolean mobileNumber = PhoneUtil.isMobileNumber(tCompanyResetPasswordsVo.getPhone());
        if (!mobileNumber) {
            return R.fail("电话号码格式不正确!");
        }
        String s = stringRedisTemplate.opsForValue().get(SmsEunm.PASSWORD_CODE.getType() + tCompanyResetPasswordsVo.getPhone());
        if (s == null) {
            return R.fail("未查到该手机号码验证码!");
        }
        if (!s.equals(tCompanyResetPasswordsVo.getCode())) {
            return R.fail("验证码不正确或验证码已过期!");
        }
        String userid = companyMetadata.userid();
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("company_id", userid);
        updateWrapper.set("password", bCryptPasswordEncoder.encode(tCompanyResetPasswordsVo.getPassword()));
        boolean update = tCompanyService.update(updateWrapper);
        if (update) {
            stringRedisTemplate.delete(SmsEunm.PASSWORD_CODE.getType() + tCompanyResetPasswordsVo.getPhone());
            return R.success("修改成功!");
        }
        return R.fail("修改失败!");
    }


    @ApiOperation("添加子公司")
    @PostMapping("/addSubsidiaryCorporation")
    @Log(title = "添加子公司", businessType = BusinessType.INSERT)
    public R addSubsidiaryCorporation(@Valid @RequestBody SubsidiaryCorporation subsidiaryCorporation) {
        if (!PhoneUtil.isMobileNumber(subsidiaryCorporation.getContactPhone())) {
            return R.fail("电话号码格式不正确!");
        }
        //查看账号是否被使用过
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("login_name", subsidiaryCorporation.getLoginName());
        int count = tCompanyService.count(queryWrapper);
        if (count > 0) {
            return R.fail("该账号已被使用!");
        }

        TCompany tCompany = new TCompany();
        BeanUtils.copyProperties(subsidiaryCorporation, tCompany);
        tCompany.setCompanyId(idGenerator.generateCode("TC"));
        tCompany.setCreateTime(LocalDateTime.now());
        tCompany.setPhoneNumber(subsidiaryCorporation.getLoginName());
        tCompany.setUpdateTime(LocalDateTime.now());
        tCompany.setPassword(bCryptPasswordEncoder.encode(subsidiaryCorporation.getPassword()));
        tCompany.setSubsidiary(1);
        tCompany.setExamineStatus(2);
        tCompany.setDeleted(0);
        tCompany.setAvatar("https://oss.aliyuncs.com/aliyun_id_photo_bucket/default_trade.jpg");
        tCompany.setCurrencyCount(new BigDecimal("0"));
        boolean save = tCompanyService.save(tCompany);
        if (save) {
            boolean isok = tCompanyRoleService.add(tCompany.getCompanyId());
            if (isok) {
                return R.success("添加成功!");
            }

        }
        return R.fail("添加失败!");
    }


    @ApiOperation("子账号管理")
    @GetMapping("/selectSubsidiaryCorporation")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageSize", required = true, value = "每页记录条数"),
            @ApiImplicitParam(name = "keyword", required = false, value = "关键字"),
            @ApiImplicitParam(name = "pageNo", required = true, value = "第几页")
    })
    public R<TCompany> selectSubsidiaryCorporation(@RequestParam("pageSize") Integer pageSize, @RequestParam("pageNo") Integer pageNo, @RequestParam(value = "keyword", required = false) String keyword) {
        String userid = companyMetadata.userid();
        if (pageSize == null) {
            pageSize = 10;
        }
        if (pageNo == null) {
            pageNo = 0;
        }
        Page page = new Page(pageNo, pageSize);
        IPage<TCompany> tCompanyIPage = tCompanyService.selectSubsidiaryCorporation(userid, page, keyword);
        return R.success(tCompanyIPage);
    }

    @PostMapping("updateSubsidiaryCorporation")
    @ApiOperation("修改子账户信息")
    @AutoIdempotent
    public R updateSubsidiaryCorporation(@Valid @RequestBody SubsidiaryCorporation subsidiaryCorporation) {
        if (StringUtils.isEmpty(subsidiaryCorporation.getCompanyId())) {
            return R.fail("企业id不能为空!");
        }
        TCompany tCompany = new TCompany();
        BeanUtils.copyProperties(subsidiaryCorporation, tCompany);
        tCompany.setUpdateTime(LocalDateTime.now());
        tCompany.setPassword(bCryptPasswordEncoder.encode(subsidiaryCorporation.getPassword()));
        boolean b = tCompanyService.updateById(tCompany);
        if (b) {
            return R.success("修改成功!");
        }
        return R.fail("修改失败!");
    }

    @GetMapping("/getQRCode")
    @ApiOperation("获取邀请链接")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "path", required = true, value = "链接地址"),
            @ApiImplicitParam(name = "qrWidth", required = true, value = "二维码宽度"),
            @ApiImplicitParam(name = "qrHeight", required = true, value = "二维码高度")
    })
    @Log(title = "获取邀请链接", businessType = BusinessType.SELECT)
    public R getQRCode(String path, int qrWidth, int qrHeight) throws IOException {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        ListOperations listOperations = redisTemplate.opsForList();
        String os = System.getProperties().getProperty("os.name");
        String pathLogs = null;
        //Windows操作系统
        if (os != null && os.toLowerCase().startsWith("windows")) {
            pathLogs = TCompanyController.class.getResource("/static/logs.png").getPath();
        } else if (os != null && os.toLowerCase().startsWith("linux")) {//Linux操作系统
            pathLogs = Paths.get("/wx/logs.png").toString();
        } else { //其它操作系统
            System.out.println(String.format("当前系统版本是:%s", os));
        }
        UUID uuid = UUID.randomUUID();
        path = path + "?userid=" + companyMetadata.userid() + "&versions=" + uuid;
        //5天有效期
        valueOperations.setIfAbsent(QRCode.QR_CODE.getValue() + uuid, path, 432000, TimeUnit.SECONDS);
        MultipartFile encode = QRCodeUtil.encode("UTF-8", path, qrWidth, qrHeight, pathLogs);
        String s = cosUtils.uploadFile(encode, "/QRCode");
        Map map = new HashMap();
        map.put("QRCode", s);
        map.put("https", path);
        listOperations.leftPush(QRCode.QR_IMAGES.getValue(), s);
        return R.success(map);
    }


    @GetMapping("/selectQRCode")
    @ApiOperation("查询邀请是否过期")
    @ApiImplicitParam(name = "versions", required = true, value = "版本")
    @Log(title = "查询邀请是否过期", businessType = BusinessType.SELECT)
    public R selectQRCode(@RequestParam("versions") String versions) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String s = valueOperations.get(QRCode.QR_CODE.getValue() + versions);
        return R.success(s != null);
    }

    @PostMapping("/updateTCompanySubsidiary")
    @ApiModelProperty("修改子帐户信息")
    public R updateTCompanySubsidiary(@Valid @RequestBody UpdateTCompanySubsidiaryVo updateTCompanySubsidiaryVo) {
        TCompany byId = tCompanyService.getById(updateTCompanySubsidiaryVo.getCompanyId());
        if (byId == null) {
            return R.fail("该企业不存在");
        }
        if (!companyMetadata.userid().equals(byId.getParentId())) {
            return R.fail("请不要非法操作!");
        }
        TCompany tCompany = new TCompany();
        BeanUtils.copyProperties(updateTCompanySubsidiaryVo, tCompany);
        tCompany.setUpdateTime(LocalDateTime.now());
        tCompany.setPhoneNumber(updateTCompanySubsidiaryVo.getLoginName());
        boolean b = tCompanyService.updateById(tCompany);
        if (b) {
            return R.success("修改成功!");
        }
        return R.fail("修改失败!");
    }


    @GetMapping("/enableCompany")
    @ApiOperation("启用停止子账号")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyId", required = true, value = "企业id"),
            @ApiImplicitParam(name = "enable", required = true, value = "状态(0:启用 1:停止)")
    })
    public R enableCompany(@RequestParam("companyId") String companyId, @RequestParam("enable") Integer enable) {
        if (StringUtils.isEmpty(companyId) || enable == null) {
            return R.fail("企业id和状态不能为空!");
        }
        if (enable != 1 && enable != 0) {
            return R.fail("操作状态不正确!");
        }
        TCompany byId = tCompanyService.getById(companyId);
        if (byId == null) {
            return R.fail("此账号不存在!");
        }
        if (!byId.getParentId().equals(companyMetadata.userid())) {
            return R.fail("该账号不是您的子账户,请不要非法操作!");
        }
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("company_id", companyId);
        updateWrapper.set("status", enable);
        boolean update = tCompanyService.update(updateWrapper);
        if (update) {
            return R.success("操作成功!");
        }
        return R.fail("操作失败!");
    }

    @GetMapping("selectSubsidiary")
    @ApiOperation("子帐号数据动态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageSize", required = true, value = "每页记录条数"),
            @ApiImplicitParam(name = "keyword", required = false, value = "关键字"),
            @ApiImplicitParam(name = "pageNo", required = true, value = "第几页")
    })
    public R<TCompanyTotalPostSharePOJO> selectSubsidiary(@RequestParam("pageSize") Integer pageSize, @RequestParam("pageNo") Integer pageNo, @RequestParam(value = "keyword", required = false) String keyword) {
        String userid = companyMetadata.userid();
        if (pageSize == null) {
            pageSize = 10;
        }
        if (pageNo == null) {
            pageNo = 0;
        }
        Page page = new Page(pageNo, pageSize);
        IPage<TCompany> tCompanyIPage = tCompanyService.selectSubsidiaryCorporation(userid, page, keyword);
        List<TCompany> records = tCompanyIPage.getRecords();
        List list = new ArrayList();
        TCompanyTotalPostSharePOJO tCompanyTotalPostSharePOJO = null;
        for (TCompany record : records) {
            tCompanyTotalPostSharePOJO = new TCompanyTotalPostSharePOJO();
            BeanUtils.copyProperties(record, tCompanyTotalPostSharePOJO);
            tCompanyTotalPostSharePOJO.setTotalPostShare(tPostShareApi.selectTotalPostShare(record.getCompanyId()));
            list.add(tCompanyTotalPostSharePOJO);
            tCompanyTotalPostSharePOJO = null;
        }
        page.setRecords(list);
        return R.success(page);
    }

    @GetMapping("/selectId")
    /* @ApiOperation("通过id查询")*/
    public TCompany selectId(@RequestParam("companyId") String companyId) {


        return tCompanyService.getId(companyId);
    }


    @GetMapping("/selectTCompany/{tCompanyId}")
    @ApiOperation("获取公司信息")
    @Log(title = "获取公司信息", businessType = BusinessType.SELECT)
    public R<TCompanyPO> selectTCompany(@PathVariable("tCompanyId") String tCompanyId) {
        TCompany tCompany1 = tCompanyService.getId(tCompanyId);
        TCompanyPO tCompanyPO = new TCompanyPO();
        if (tCompany1 != null) {
            String userid = companyMetadata.userid();
            if (StringUtils.isEmpty(userid) || "TJ".equals(userid.substring(0, 2))) {
                tCompanyPO.setOnLinePost(tCompanyPostApi.onLinePost(tCompany1.getCompanyId(), 0));
            } else if (!StringUtils.isEmpty(userid) && "TR".equals(userid.substring(0, 2))) {
                tCompanyPO.setOnLinePost(tCompanyPostApi.onLinePost(tCompany1.getCompanyId(), 1));
            }
            BeanUtils.copyProperties(tCompany1, tCompanyPO);
        }
        return R.success(tCompanyPO);
    }

    @GetMapping("/basicInformation/{tCompanyId}")
    @ApiOperation("获取公司基础信息")
    @ApiImplicitParam(name = "tCompanyId", required = true, value = "公司id")
    public R<TCompanyBasicInformation> basicInformation(@PathVariable("tCompanyId") String tCompanyId) {
        TCompanyBasicInformation tCompanyBasicInformation = tCompanyService.basicInformation(tCompanyId);
        return R.success(tCompanyBasicInformation);
    }

    @PostMapping("/inviteUrl")
    @ApiOperation("邀请链接")
    public R inviteUrl(@Valid @RequestBody InviteVO inviteVO) {
        java.util.UUID uuid = java.util.UUID.randomUUID();
        String toJSONString = JSON.toJSONString(inviteVO);
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.setIfAbsent(uuid.toString(), toJSONString, 7, TimeUnit.DAYS);
        return R.success(inviteVO.getUrl() + uuid);
    }


    @GetMapping("/verificationUrl")
    @ApiOperation("验证链接是否过期")
    @ApiImplicitParam(name = "verificationCode", required = true, value = "邀请code")
    public R verificationUrl(@RequestParam("verificationCode") String verificationCode) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String o = valueOperations.get(verificationCode);
        if (StringUtils.isEmpty(o)) {
            return R.fail("邀请连接已过期或者已使用!");
        }
        InviteVO inviteVO = JSON.parseObject(o, InviteVO.class);
        return R.success(inviteVO);
    }

    @GetMapping("/subsidiaryCorporation")
    @ApiOperation("绑定企业")
    @ApiImplicitParam(name = "verificationCode", required = true, value = "邀请code")
    @AutoIdempotent
    public R subsidiaryCorporation(@RequestParam("verificationCode") String verificationCode) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String o = valueOperations.get(verificationCode);
        if (StringUtils.isEmpty(o)) {
            return R.fail("该链接已经使用!");
        }
        InviteVO inviteVO = JSON.parseObject(o, InviteVO.class);
        String userid = companyMetadata.userid();
        if (userid.equals(inviteVO.getTCompanyId())) {
            return R.fail("自己不能成自己的子公司");
        }
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("company_id", userid);
        updateWrapper.set("parent_id", inviteVO.getTCompanyId());
        updateWrapper.set("subsidiary", 1);
        boolean update = tCompanyService.update(updateWrapper);
        if (update) {
            Boolean delete = redisTemplate.delete(verificationCode);
            return R.success("绑定成功!");
        }
        return R.fail("绑定失败,请联系管理员!");
    }

    @GetMapping("/phoneBinding")
    @ApiOperation("通过电话号码绑定企业")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "verificationCode", required = true, value = "邀请code"),
            @ApiImplicitParam(name = "phone", required = false, value = "子企业电话号码")
    })
    @AutoIdempotent
    public R phoneBinding(@RequestParam("verificationCode") String verificationCode, @RequestParam("phone") String phone) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String o = valueOperations.get(verificationCode);
        if (StringUtils.isEmpty(o)) {
            return R.fail("该链接已经使用!");
        }
        InviteVO inviteVO = JSON.parseObject(o, InviteVO.class);

        if (phone.equals(inviteVO.getLoginName())) {
            return R.fail("自己不能成自己的子公司");
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("login_name", phone);
        TCompany one = tCompanyService.getOne(queryWrapper);
        if (one == null) {
            return R.fail("无此公司!");

        }
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("login_name", phone);
        updateWrapper.set("parent_id", inviteVO.getTCompanyId());
        updateWrapper.set("subsidiary", 1);
        boolean update = tCompanyService.update(updateWrapper);
        if (update) {
            Boolean delete = redisTemplate.delete(verificationCode);
            return R.success("绑定成功!");
        }
        return R.fail("绑定失败,请联系管理员!");
    }


    @GetMapping("/unbind")
    @ApiOperation("解绑子企业")
    @ApiImplicitParam(name = "tCompanyId", required = true, value = "子企业id")
    @AutoIdempotent
    public R unbind(@RequestParam("tCompanyId") String tCompanyId) {
        String userid = companyMetadata.userid();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("parent_id", userid);
        queryWrapper.eq("company_id", tCompanyId);
        TCompany one = tCompanyService.getOne(queryWrapper);
        if (one == null) {
            return R.fail("该公司暂未与你绑定!");
        }
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("company_id", tCompanyId);
        updateWrapper.set("parent_id", "");
        updateWrapper.set("subsidiary", 0);
        boolean update = tCompanyService.update(updateWrapper);
        if (update) {
            return R.success("解绑成功!");
        }
        return R.fail("解绑失败!");
    }

    @GetMapping("/sendAResumeRecord")
    @ApiOperation("子企业投简数据")
    @ApiImplicitParam(name = "tCompanyId", required = true, value = "子企业id")
    public R<SendAResumeRecord> sendAResumeRecord(@RequestParam("tCompanyId") String tCompanyId) {
        String userid = companyMetadata.userid();
        SendAResumeRecord sendAResumeRecord = tCompanyService.sendAResumeRecord(userid, tCompanyId);
        if (sendAResumeRecord == null) {
            return R.fail("无该企业!");
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("put_in_comppany", tCompanyId);
        queryWrapper.eq("easco", 0);
        sendAResumeRecord.setJobSeeker(putInResumeService.count(queryWrapper));
        QueryWrapper queryWrapper1 = new QueryWrapper();
        queryWrapper1.eq("put_in_comppany", tCompanyId);
        queryWrapper1.eq("easco", 1);
        sendAResumeRecord.setRecommend(putInResumeService.count(queryWrapper1));
        return R.success(sendAResumeRecord);
    }

    @GetMapping("/money")
    @ApiOperation("获取企业金额")
    public R money() {
        String userid = companyMetadata.userid();
        TCompany byId = tCompanyService.getById(userid);
        Map map = new ConcurrentHashMap(2);
        map.put("currencyCount", byId.getCurrencyCount());
        map.put("gold", byId.getGold());
        return R.success(map);
    }

    @PostMapping("/tCompanyProfileUpdate")
    @ApiOperation("修改企业介绍")
    @Log(title = "修改企业介绍", businessType = BusinessType.UPDATE)
    @AutoIdempotent
    public R tCompanyProfileUpdate(@Valid @RequestBody TCompanyProfileUpdate tCompanyProfileUpdate) {
        if (!companyMetadata.userid().equals(tCompanyProfileUpdate.getCompanyId())) {
            return R.fail("请不要非法操作!");
        }
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("company_id", tCompanyProfileUpdate.getCompanyId());
        updateWrapper.set("url", tCompanyProfileUpdate.getUrl());
        updateWrapper.set("reg_time", tCompanyProfileUpdate.getRegTime());
        updateWrapper.set("description", tCompanyProfileUpdate.getDescription());
        updateWrapper.set("update_time", LocalDateTime.now());
        boolean update = tCompanyService.update(updateWrapper);
        if (update) {
            cacheClient.deleteCache(Sole.COMPANY.getKey(), companyMetadata.userid());
            return R.success("修改成功!");
        }
        return R.fail("修改失败!");
    }


    @PostMapping("/tCompanyWelfare")
    @ApiOperation("工作地址及福利")
    @Log(title = "工作地址及福利", businessType = BusinessType.UPDATE)
    public R tCompanyWelfareVO(@Valid @RequestBody TCompanyWelfareVO tCompanyWelfareVO) {
        String userid = companyMetadata.userid();
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("company_id", userid);
        updateWrapper.set("address", tCompanyWelfareVO.getAddress());
        updateWrapper.set("provinces_cities", JSON.toJSONString(tCompanyWelfareVO.getProvincesCities()));
        updateWrapper.set("welfare", JSON.toJSONString(tCompanyWelfareVO.getWelfare()));
        updateWrapper.set("update_time", LocalDateTime.now());
        boolean update = tCompanyService.update(updateWrapper);
        if (update) {
            cacheClient.deleteCache(Sole.COMPANY.getKey(), userid);
            return R.success("修改成功!");
        }
        return R.fail("修改失败!");
    }

    @PostMapping("/tCompanyUser")
    @ApiOperation("修改企业个人信息")
    @Log(title = "修改企业个人信息", businessType = BusinessType.UPDATE)
    @AutoIdempotent
    public R tCompanyUserVO(@Valid @RequestBody TCompanyUserVO tCompanyUserVO) {
        String userid = companyMetadata.userid();
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("company_id", userid);
        updateWrapper.set("avatar", tCompanyUserVO.getAvatar());
        updateWrapper.set("contact_name", tCompanyUserVO.getContactName());
        updateWrapper.set("sex", tCompanyUserVO.getSex());
        updateWrapper.set("job_title", tCompanyUserVO.getJobTitle());
        updateWrapper.set("birthday", tCompanyUserVO.getBirthday());
        updateWrapper.set("contact_phone", tCompanyUserVO.getContactPhone());
        updateWrapper.set("update_time", LocalDateTime.now());
        /* boolean update = tCompanyService.update(updateWrapper);*/
        boolean update = tCompanyService.updateUserinfo(tCompanyUserVO, userid);
        if (update) {
            cacheClient.deleteCache(Sole.COMPANY.getKey(), userid);
            return R.success("修改成功!");
        }
        return R.fail("修改失败!");

    }

    @GetMapping("/verifyPassword")
    @ApiOperation("验证密码")
    @ApiImplicitParam(name = "password", required = true, value = "密码")
    public R verifyPassword(@RequestParam("password") String password) {
        String userid = companyMetadata.userid();
        TCompany byId = tCompanyService.getById(userid);
        boolean matches = bCryptPasswordEncoder.matches(password, byId.getPassword());
        return matches ? R.success("密码正确!", true) : R.fail("密码错误!", false);
    }

    @GetMapping("/weChat")
    @ApiOperation("获取微信名")
    public R weChat() {
        String userid = companyMetadata.userid();
        String name = tCompanyService.getWeChat(userid);
        return R.success(name);
    }


    /**
     * 扣除金币
     *
     * @return
     */
    @GetMapping("/deductGold")
    @Log(title = "扣除金币", businessType = BusinessType.UPDATE)
    public Map<String, Object> deductGold(@RequestParam("money") BigDecimal money, @RequestParam("userId") String userId, @RequestParam("jobHunter") String jobHunter, @RequestParam("orderId") String orderId) throws Exception {
        Map<String, Object> map1 = tCompanyService.deductGold(money, userId, jobHunter, orderId);
        return map1;
    }


    /**
     * 扣除无忧币
     *
     * @return
     */

    @GetMapping("/deductCurrency")
    @Log(title = "扣除无忧币", businessType = BusinessType.UPDATE)
    public Map<String, Object> deductCurrency(@RequestParam("money") BigDecimal money, @RequestParam("userId") String userId, @RequestParam("jobHunter") String jobHunter, @RequestParam("orderId") String orderId) throws Exception {

        Map map = tCompanyService.deductCurrency(money, userId, jobHunter, orderId);
        return map;
    }

    @GetMapping("/deductCurrencyExplain")
    @Log(title = "扣除无忧币", businessType = BusinessType.UPDATE)
    public Map<String, Object> deductCurrencyExplain(@RequestParam("money") BigDecimal money, @RequestParam("userId") String userId, @RequestParam("explain") String explain, @RequestParam("orderId") String orderId) throws Exception {

        Map map = tCompanyService.deductCurrencyExplain(money, userId, explain, orderId);
        return map;
    }

    /**
     * @param companyId 企业id
     * @return null
     * @Description: 查询企业的邀请及推荐官的邀请人
     * @Author tangzhuo
     * @CreateTime 2023/4/21 11:21
     */

    @GetMapping("/getSharePersonRecommend")
    public Map<String, String> getSharePersonRecommend(@RequestParam("companyId") String companyId) {
        Map<String, String> map = tCompanyService.getSharePerson(companyId);
        return map;
    }

    @GetMapping(value = "/asccc")
    @ApiOperation("测试")
    public String asccc() throws InterruptedException {

     /*   GoldConfig o1 = (GoldConfig) redisUtils.get(RedisCache.GOLD_CACHE.getValue());
        runningWaterService.asyncMethod(); // 异步方法不会被异步执行
        Future<String> future = runningWaterService.asyncMethodWithResult("cc"); // 异步方法会被异步执行
        return o1.toString();*/
        informAsync.goldReward(companyMetadata.userid(),"20");
        return null;

    }


    @GetMapping("/selectCompanyName")
    @ApiOperation("查询企业名称")
    @ApiImplicitParam(name = "keyword", required = false, value = "企业名称")
    public List<String> selectCompanyName(@RequestParam(value = "keyword", required = false) String keyword) {
        List<String> list = tCompanyService.selectCompanyName(keyword);
        return list;
    }


    @GetMapping("/companyLists")
    @ApiOperation("查询企业名称")
    public List<TCompany> companyLists(@RequestParam("id") List<String> id) {
        List<TCompany> list = tCompanyService.companyLists(id);
        return list;
    }


    // @SneakyThrows
    @PostMapping("/getQRCode")
    @ApiOperation("企业获取邀请链接")
/*    @ApiImplicitParams({
            @ApiImplicitParam(name = "path", required = true, value = "链接地址"),
            @ApiImplicitParam(name = "qrWidth", required = true, value = "二维码宽度"),
            @ApiImplicitParam(name = "qrHeight", required = true, value = "二维码高度"),
            @ApiImplicitParam(name = "type", required = true, value = "身份(0:企业 1:推荐官 2:求职者 3:猎企 4:校园推荐官)")
    })*/
    @Log(title = "企业获取邀请链接", businessType = BusinessType.SELECT)
    public R getQRCode(@RequestBody QRCodeVO qrCodeVO) throws Exception {
        //添加分享记录
        if (qrCodeVO.getType() == null) {
            return R.fail("请选择邀请身份！");
        }
        //  shareAsync.addAmount(companyMetadata.userid(), qrCodeVO.getType());
        Object o = redisUtils.get(InvitationCode.QR_CODE.getType() + companyMetadata.userid() + qrCodeVO.getPath() + qrCodeVO.getQrWidth() + qrCodeVO.getQrHeight());
        String pathUrl = qrCodeVO.getPath();
        if (o != null) {

            return R.success(o);
        }

        String os = System.getProperties().getProperty("os.name");
        String pathLogs = null;
        //Windows操作系统
        if (os != null && os.toLowerCase().startsWith("windows")) {
            pathLogs = "D:/static/logs.png";
        } else if (os != null && os.toLowerCase().startsWith("linux")) {//Linux操作系统
            pathLogs = Paths.get("/wx/logs.png").toString();
        } else { //其它操作系统
            System.out.println(String.format("当前系统版本是:%s", os));
        }
        String code = (String) redisUtils.get(InvitationCode.CODE.getType() + companyMetadata.userid());
        if (StringUtils.isEmpty(code)) {
            code = idGenerator.invitationCode("invitationCode");
            redisUtils.set(InvitationCode.CODE.getType() + companyMetadata.userid(), code);
            // redisUtils.set(InvitationCode.INVITATION_URL_CODE.getType() + code, companyMetadata.userid());
        }
        redisUtils.set(InvitationCode.INVITATION_URL_CODE.getType() + code, companyMetadata.userid());
        qrCodeVO.setPath(qrCodeVO.getPath() + "&QRCode=" + code);
        //5天有效期
        MultipartFile encode = QRCodeUtil.encode("UTF-8", qrCodeVO.getPath(), qrCodeVO.getQrWidth(), qrCodeVO.getQrHeight(), pathLogs);

        String s = Base64Util.multipartFileToBase64(encode);
        // = cosUtils.uploadFile(encode, "/QRCode");
        /* redisUtils.sSet(InvitationCode.FILE_CODE.getType()).*/
        Map map = new HashMap();
        map.put("QRCode", s);
        map.put("https", qrCodeVO.getPath());
        redisUtils.set(InvitationCode.QR_CODE.getType() + companyMetadata.userid() + pathUrl + qrCodeVO.getQrWidth() + qrCodeVO.getQrHeight(), map, 60 * 60 * 2);//存2个小时
        // runningWaterAsync.shareRegistration(companyMetadata.userid());

        return R.success(map);
    }

    @GetMapping("/selectCompanyHomeDTO")
    @ApiOperation("企业首页")
    @Log(title = "企业首页", businessType = BusinessType.SELECT)
    public R<CompanyHomeDTO> selectCompanyHomeDTO() {
        String userid = companyMetadata.userid();
        CompanyHomeDTO companyHomeDTO = tCompanyService.selectCompanyHomeDTO(userid);

        return R.success(companyHomeDTO);
    }


    @PostMapping("/selectUser")
    @ApiOperation("获取所有用户信息")
    @Log(title = "获取所有用户信息", businessType = BusinessType.SELECT)
    public Map<String, Map> selectUser(@RequestBody List<String> id) {
        Map<String, Map> map1 = new HashMap(id.size());
        List<String> TC = null;
        List<String> TR = null;
        List<String> TJ = null;
        if (id != null) {
            for (String s : id) {
                String substring = s.substring(0, 2);
                if ("TC".equals(substring)) {
                    if (TC == null) {
                        TC = new ArrayList<>(id.size() / 2);
                    }
                    TC.add(s);
                } else if ("TR".equals(substring)) {
                    if (TR == null) {
                        TR = new ArrayList<>(id.size() / 2);
                    }
                    TR.add(s);
                } else if ("TJ".equals(substring)) {
                    if (TJ == null) {
                        TJ = new ArrayList<>(id.size() / 2);
                    }
                    TJ.add(s);
                }
            }
            if (TC != null) {
                List<TCompany> list = this.companyLists(TC);
                for (TCompany tCompany : list) {
                    Map map = new HashMap();
                    map.put("id", tCompany.getCompanyId());
                    map.put("name", tCompany.getContactName());
                    map.put("avatar", tCompany.getAvatar());
                    map.put("mobile", tCompany.getPhoneNumber());
                    map.put("sex", tCompany.getSex());
                    map1.put(tCompany.getCompanyId(), map);
                }
            }
            if (TR != null) {
                QueryWrapper queryWrapper1 = new QueryWrapper();
                queryWrapper1.in("id", TR);
                List<TRecommend> list = tRecommendService.list(queryWrapper1);
                for (TRecommend tRecommend : list) {
                    Map map = new HashMap();
                    map.put("id", tRecommend.getId());
                    map.put("name", tRecommend.getUsername());
                    map.put("avatar", tRecommend.getHeadPath());
                    map.put("mobile", tRecommend.getPhone());
                    map.put("sex", tRecommend.getSex());
                    map1.put(tRecommend.getId(), map);
                }
            }
            if (TJ != null) {
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.in("user_id", TJ);
                List<TJobhunter> list = tJobhunterService.list(queryWrapper);
                for (TJobhunter tJobhunter : list) {
                    Map map = new HashMap();
                    map.put("id", tJobhunter.getUserId());
                    map.put("name", tJobhunter.getUserName());
                    map.put("avatar", tJobhunter.getAvatar());
                    map.put("mobile", tJobhunter.getPhone());
                    map.put("sex", tJobhunter.getSex());
                    map1.put(tJobhunter.getUserId(), map);
                }
            }
            return map1;
        }
        return null;
    }

}





   /* public static void main(String[] args) {
        BigDecimal bigDecimal = new BigDecimal(20);
        Map<String, Object> stringObjectMap = downloadResume(bigDecimal,"cc","TRaa");
        System.out.println(stringObjectMap.get("cc"));
        System.out.println(stringObjectMap.get("TRaa"));

   *//*     NumberFormat currency = NumberFormat.getCurrencyInstance(); //建立货币格式化引用
        NumberFormat percent = NumberFormat.getPercentInstance();  //建立百分比格式化引用
        percent.setMaximumFractionDigits(3); //百分比小数点最多3位

        BigDecimal loanAmount = new BigDecimal("15000.48"); //贷款金额
        BigDecimal interestRate = new BigDecimal("0.008"); //利率
        BigDecimal interest = loanAmount.multiply(interestRate); //相乘

        System.out.println("贷款金额:\t" + currency.format(loanAmount));
        System.out.println("利率:\t" + percent.format(interestRate));
        System.out.println("利息:\t" + currency.format(interest));*//*

    }*/


  /*  @GetMapping("/cccs")
    public String cc() {
        TCompany tCompany=new TCompany();
        tCompany.setCompanyId("252");
        boolean b = tCompanyService.removeById(tCompany);
        return "cccc";
    }*/

 /*   @PostMapping("/asc")
    public String asc(@RequestBody SchoolQuery schoolQuery) {
        System.out.println(schoolQuery);
        return "cccc";
    }*/



