package com.wcwy.company.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.wcwy.common.base.enums.AccessTemplateCode;
import com.wcwy.common.base.enums.Gold;
import com.wcwy.common.base.enums.GoldExplain;
import com.wcwy.common.base.enums.SmsEunm;
import com.wcwy.common.base.result.R;
import com.wcwy.common.base.utils.*;
import com.wcwy.common.redis.enums.*;
import com.wcwy.common.redis.util.IDGenerator;
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.common.web.annotation.AutoIdempotent;
import com.wcwy.common.web.config.CacheClient;
import com.wcwy.company.aliyun.SendSms;
import com.wcwy.company.asyn.RegisterAsync;
import com.wcwy.company.asyn.RunningWaterAsync;
import com.wcwy.company.asyn.ShareAsync;
import com.wcwy.company.asyn.ShareDataAsync;
import com.wcwy.company.config.CosUtils;
import com.wcwy.company.dto.RecommendDataDTO;
import com.wcwy.company.entity.*;
import com.wcwy.company.po.TCompanyPO;
import com.wcwy.company.po.TJobhunterPO;
import com.wcwy.company.po.TRecommendPO;
import com.wcwy.company.query.TRecommendQuery;
import com.wcwy.company.query.inviterQuery;
import com.wcwy.company.service.*;
import com.wcwy.company.session.CompanyMetadata;
import com.wcwy.company.vo.*;
import com.wcwy.post.api.RecommendBasicsApi;
import com.wcwy.post.entity.GoldConfig;
import com.wcwy.post.entity.RecommendBasics;
import com.wcwy.system.annotation.Log;
import com.wcwy.system.enums.BusinessType;
import io.jsonwebtoken.Claims;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * ClassName: TRecommendController
 * Description:
 * date: 2022/9/7 14:03
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Api(tags = "推荐官接口")
@RestController
@RequestMapping("/tRecommend")
@Slf4j
public class TRecommendController {
    @Autowired
    private TRecommendService tRecommendService;
    @Autowired
    private CompanyMetadata companyMetadata;
    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    @Autowired
    private IDGenerator idGenerator;
    @Autowired
    private TRecommendRoleService tRecommendRoleService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private CacheClient cacheClient;
    @Autowired
    private SendSms sendSms;
    @Autowired
    private ShareAsync shareAsync;
    @Autowired
    private ReferrerRecordService referrerRecordService;
    @Autowired
    private ShareDataAsync shareDataAsync;
    @Autowired
    private TCompanyService tCompanyService;
    @Autowired
    private TJobhunterService tJobhunterService;
    @Autowired
    private RunningWaterService runningWaterService;
    @Autowired
    private RunningWaterAsync runningWaterAsync;
    @Autowired
    private WechatUserService wechatUserService;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private RecommendBasicsApi recommendBasicsApi;
    @Autowired
    private CosUtils cosUtils;
    @Autowired
    private RegisterAsync registerAsync;

    @PostMapping("/insert")
    @ApiOperation("推荐官注册")
    @Log(title = "推荐官注册", businessType = BusinessType.INSERT)
    @GlobalTransactional(rollbackFor = Exception.class)
    public R insert(HttpServletRequest request, @Valid @RequestBody InsertRecommendVO insertRecommendVO) throws Exception {
        if (insertRecommendVO.getIdentity() != 0 && insertRecommendVO.getIdentity() != 1) {
            return R.fail("请现在正确的身份！");
        }
        boolean mobileNumber = PhoneUtil.isMobileNumber(insertRecommendVO.getLoginName());
        if (!mobileNumber) {
            return R.fail("登录电话号码格式不正确！");
        }
        boolean mobileNumber1 = PhoneUtil.isMobileNumber(insertRecommendVO.getPhone());
        if (!mobileNumber1) {
            return R.fail("联系电话号码格式不正确！");
        }
        /* String s = stringValueOperations.get(SmsEunm.INSERT_CODE.getType() + insertRecommendVO.getLoginName());*/
        Claims claimsFromJwt = JWT.getClaimsFromJwt(insertRecommendVO.getKeyRate());
        if (claimsFromJwt == null) {
            return R.fail("操作时间过长，请重新操作!");
        }
        Object phone = claimsFromJwt.get("phone");
        if (StringUtils.isEmpty(phone) || !insertRecommendVO.getLoginName().equals(phone)) {
            return R.fail("电话号码不匹配");
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("login_name", insertRecommendVO.getLoginName());
        int count = tRecommendService.count(queryWrapper);
        if (count > 0) {
            return R.fail("该账号已被使用!");
        }
        TRecommend tRecommend = new TRecommend();
        BeanUtils.copyProperties(insertRecommendVO, tRecommend);
        tRecommend.setId(idGenerator.generateCode("TR"));
        tRecommend.setLoginName(insertRecommendVO.getLoginName());
        tRecommend.setRegistrantTime(LocalDateTime.now());
        tRecommend.setEmpiricalValue(0L);
        tRecommend.setHeadPath(insertRecommendVO.getHeadPath());
        tRecommend.setExamineStatus(4);
        tRecommend.setStatus(0);
        tRecommend.setDeleted(0);
        tRecommend.setCurrencyCount(new BigDecimal("0"));
        tRecommend.setPassword(bCryptPasswordEncoder.encode(insertRecommendVO.getPassword()));
        GoldConfig o1 = (GoldConfig) redisUtils.get(RedisCache.GOLD_CACHE.getValue());
        tRecommend.setGold(new BigDecimal(o1.getRegisterGold()));
        tRecommend.setRegistrantTime(LocalDateTime.now());
        Object o = null;
        if (!StringUtils.isEmpty(insertRecommendVO.getInvitationCode())) {
            o = redisUtils.get(InvitationCode.INVITATION_URL_CODE.getType() + insertRecommendVO.getInvitationCode());
            if (o != null) {
                String ipAddr = IpUtils.getIpAddr(request);
                registerAsync.register(ipAddr, insertRecommendVO.getIdentity() == 0 ? 4 : 5, o.toString());
                tRecommend.setSharePerson(o.toString());
                runningWaterAsync.inviter_recommend(o.toString());

            }
        }
        boolean save = tRecommendService.save(tRecommend);
        if (save) {
            runningWaterAsync.add(o1.getRegisterGold(), tRecommend.getId(), GoldExplain.REGISTER_GOLD.getValue());
            TRecommendRole tRecommendRole = new TRecommendRole();
            tRecommendRole.setDeleted(0);
            tRecommendRole.setCreateTime(LocalDateTime.now());
            tRecommendRole.setRecommendId(tRecommend.getId());
            tRecommendRole.setRoleId("3");
            boolean save1 = tRecommendRoleService.save(tRecommendRole);
            RecommendBasics recommendBasics = new RecommendBasics();
            recommendBasics.setRecommendId(tRecommend.getId());
            recommendBasics.setPhone(tRecommend.getPhone());
            recommendBasics.setHeadPath(tRecommend.getHeadPath());
            recommendBasics.setSex(tRecommend.getSex());
            recommendBasics.setUsername(tRecommend.getUsername());
            recommendBasicsApi.save(recommendBasics);
            if (save1) {
                long l = redisUtils.sGetSetSize(Register.RECOMMEND_REGISTER.getType());
                if (l < 1200) {
                    redisUtils.sSet(Register.RECOMMEND_REGISTER.getType(), tRecommend.getId());
                }
                //注册成功删除验证码
                stringRedisTemplate.delete(SmsEunm.INSERT_CODE.getType() + insertRecommendVO.getLoginName());
                String ipAddr = IpUtils.getIpAddr(request);
                registerAsync.register(ipAddr, 1);
                if (!StringUtils.isEmpty(o)) {
                    sendSms.registerInform(o.toString(), AccessTemplateCode.INFORM_RECOMMEND.getName(), AccessTemplateCode.INFORM_RECOMMEND.getDesc(), insertRecommendVO.getUsername());
                }
                return R.success("注册成功");
            }


        }
        return R.fail("注册失败!");
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改信息")
    @Log(title = "修改信息", businessType = BusinessType.UPDATE)
    @GlobalTransactional(rollbackFor = Exception.class)
    public R update(@Valid @RequestBody TRecommendVO tRecommendVO) {
        //验证电话号码
        boolean mobileNumber = PhoneUtil.isMobileNumber(tRecommendVO.getPhone());
        if (!mobileNumber) {
            return R.fail("该电话号码格式不正确！");
        }
        TRecommend tRecommend = new TRecommend();
        BeanUtils.copyProperties(tRecommendVO, tRecommend);
        tRecommend.setUpdateTime(LocalDateTime.now());
        tRecommend.setExamineStatus(0);
        tRecommend.setUpdateId(companyMetadata.userid());
        boolean save = tRecommendService.updateById(tRecommend);
        if (save) {
            RecommendBasics recommendBasics = new RecommendBasics();
            recommendBasics.setRecommendId(tRecommend.getId());
            recommendBasics.setPhone(tRecommend.getPhone());
            recommendBasics.setHeadPath(tRecommend.getHeadPath());
            recommendBasics.setSex(tRecommend.getSex());
            recommendBasics.setUsername(tRecommend.getUsername());
            recommendBasicsApi.save(recommendBasics);
            return R.success("更新成功!");
        }
        return R.fail("更新失败!");
    }

    @GetMapping("/selectOne")
    @ApiOperation(value = "通过token获取推荐官信息")
    @Log(title = "通过token获取推荐官信息", businessType = BusinessType.SELECT)
    public R<TRecommend> selectOne() throws Exception {
        String userid = companyMetadata.userid();
        if (null == userid && "".equals(userid)) {
            return R.fail("系统错误！请联系管理员");
        }
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Boolean flag = valueOperations.setIfAbsent(Sole.LOGIN_USER.getKey() + userid, userid, 60 * 30, TimeUnit.SECONDS);
        if (flag) {
            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("id", userid);
            updateWrapper.set("logout_time", LocalDateTime.now());
            boolean update = tRecommendService.update(updateWrapper);
        }

        runningWaterAsync.loginGold(userid);
        TRecommend byId = tRecommendService.getById(userid);
        if (null == byId) {
            return R.fail("无效token");
        }
        byId.setCard("");
        byId.setCardVerso("");
        byId.setCardFront("");
        byId.setPassword("草泥马！敢偷窥密码");
        List<String> authorization = tCompanyService.authorization(byId.getLoginName());
        byId.setAuthorization(authorization);
        return R.success(byId);
    }

    @ApiOperation(value = "查询推荐官信息")
    @PostMapping("/select")
    @Log(title = "查询推荐官信息", businessType = BusinessType.SELECT)
    public R<TRecommendPO> select(@RequestBody TRecommendQuery tRecommendQuery) {
/*        QueryWrapper queryWrapper=new QueryWrapper();
       if(StrUtil.isNotEmpty(tRecommendQuery.getId())){
            queryWrapper.eq("id",tRecommendQuery.getId());
       }
        if(StrUtil.isNotEmpty(tRecommendQuery.getUsername())){
            queryWrapper.likeLeft("username",tRecommendQuery.getUsername());
        }
        if(StrUtil.isNotEmpty(tRecommendQuery.getEducation())){
            queryWrapper.eq("education",tRecommendQuery.getEducation());
        }
        if(tRecommendQuery.getSex() !=null){
            queryWrapper.eq("sex",tRecommendQuery.getSex());
        }
        if(tRecommendQuery.getSex() !=null){
            queryWrapper.likeLeft("phone",tRecommendQuery.getPhone());
        }
        if(StrUtil.isNotEmpty(tRecommendQuery.getCard())){
            queryWrapper.eq("card",tRecommendQuery.getCard());
        }
        if(StrUtil.isNotEmpty(tRecommendQuery.getAcademy())){
            queryWrapper.likeLeft("academy",tRecommendQuery.getAcademy());
        }
        if(StrUtil.isNotEmpty(tRecommendQuery.getCareerId())){
            queryWrapper.eq("career_id",tRecommendQuery.getCareerId());
        }
        if(tRecommendQuery.getRecruitment().toString() !=null){
            queryWrapper.eq("recruitment",tRecommendQuery.getRecruitment());
        }
        if(tRecommendQuery.getIdentity() !=null){
            queryWrapper.eq("identity",tRecommendQuery.getIdentity());
        }
        if(StrUtil.isNotEmpty(tRecommendQuery.getCompanyId())){
            queryWrapper.eq("company_id",tRecommendQuery.getCompanyId());
        }
        if(StrUtil.isNotEmpty(tRecommendQuery.getSharePerson())){
            queryWrapper.eq("share_person",tRecommendQuery.getSharePerson());
        }
        if(tRecommendQuery.getExamineStatus()!=null){
            queryWrapper.eq("examine_status",tRecommendQuery.getExamineStatus());
        }
        if(tRecommendQuery.getStatus() !=null){
            queryWrapper.eq("status",tRecommendQuery.getStatus());
        }*/
        IPage<TRecommendPO> page = tRecommendService.pageList(tRecommendQuery);

        return R.success(page);
    }

    @PostMapping("/updatePassword")
    @ApiOperation(value = "修改密码")
    @Log(title = "修改密码", businessType = BusinessType.UPDATE)
    public R updatePassword(@RequestBody UpdateTRecommendPasswordVO updateTRecommendPasswordVO) {
        String userid = companyMetadata.userid();
        /*TRecommend byId = tRecommendService.getById(userid);*/
        /*  boolean matches = bCryptPasswordEncoder.matches(updateTRecommendPasswordVO.getOldPassword(), byId.getPassword());*/
        ValueOperations<String, String> stringValueOperations = stringRedisTemplate.opsForValue();
        String s = stringValueOperations.get(SmsEunm.PASSWORD_CODE.getType() + updateTRecommendPasswordVO.getPhone());
        if (StringUtils.isEmpty(s)) {
            return R.fail("验证码不正确或已过期!");
        }
        if (updateTRecommendPasswordVO.getCode().equals(s)) {
            TRecommend tRecommend = new TRecommend();
            tRecommend.setId(userid);
            tRecommend.setPassword(bCryptPasswordEncoder.encode(updateTRecommendPasswordVO.getPassword()));
            tRecommend.setUpdateTime(LocalDateTime.now());
            tRecommend.setUpdateId(userid);
            boolean b = tRecommendService.updateById(tRecommend);
            if (b) {
                stringRedisTemplate.delete(SmsEunm.PASSWORD_CODE.getType() + updateTRecommendPasswordVO.getPhone());
                return R.success("修改成功！");
            }
        }
        return R.fail("修改失败！验证码不正确!");
    }


/*
    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改用户信息")
    @ApiImplicitParam(name = "id", required = true, value = "推荐官id")
    public R update(@RequestBody TRecommendUpdateVO tRecommendVO, @PathVariable("id") String id) {
        if (StrUtil.isEmpty(id)) {
            return R.fail("推荐官id不能为空");
        }
        TRecommend tRecommend = tRecommendService.getById(id);
        if (tRecommend == null) {
            return R.fail("该推荐官不存在！");
        }
        String userid = companyMetadata.userid();
        BeanUtils.copyProperties(tRecommendVO, tRecommend);
        tRecommend.setId(id);
        tRecommend.setUpdateId(userid);
        tRecommend.setUpdateTime(LocalDateTime.now());
        boolean b = tRecommendService.updateById(tRecommend);
        if (b) {
            return R.success("修改成功");
        }
        return R.fail("修改成功失败");
    }
*/

    @ApiOperation("通过密码修改账号")
    @PostMapping("/updateRecommend")
    @Log(title = "通过密码修改账号", businessType = BusinessType.UPDATE)
    public R updateRecommend(@Valid @RequestBody PasswordUpdatePhone passwordUpdatePhone) {
        TRecommend tRecommend = tRecommendService.getById(passwordUpdatePhone.getUserId());
        if (tRecommend == null) {
            return R.fail("该推荐官不存在！");
        }
        //查询该手机号码有没有被使用
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("phone", passwordUpdatePhone.getNewPhone());
        int count = tRecommendService.count(queryWrapper);
        if (count > 0) {
            return R.fail("该电话号码已被使用!");
        }
        boolean matches = bCryptPasswordEncoder.matches(passwordUpdatePhone.getPassword(), tRecommend.getPassword());
        if (matches) {
            tRecommend.setLoginName(passwordUpdatePhone.getNewPhone());
            tRecommend.setPhone(passwordUpdatePhone.getNewPhone());
            boolean b = tRecommendService.updateById(tRecommend);
            if (b) {
                return R.success("修改成功!");
            }
            return R.fail("修改失败");
        }
        return R.fail("密码不正确!");
    }

    @ApiOperation("推荐官业更换手机号")
    @PostMapping("/updatePhone")
    @Log(title = "推荐官业更换手机号", businessType = BusinessType.UPDATE)
    public R updatePhone(@Valid @RequestBody UpdatePhone updatePhone) {
        if (!companyMetadata.userid().equals(updatePhone.getUserId())) {
            return R.fail("登录与传入id不一致 请检查！");
        }

        TRecommend tRecommend = tRecommendService.getById(companyMetadata.userid());
        if (tRecommend == null) {
            return R.fail("该推荐官不存在！");
        }
        boolean matches = bCryptPasswordEncoder.matches(updatePhone.getPassword(), tRecommend.getPassword());

        if (!matches) {
            return R.fail("密码不正确!");
        }


        //查询该手机号码有没有被使用
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("login_name", updatePhone.getNewPhone());
        int count = tRecommendService.count(queryWrapper);
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
        updateWrapper.eq("id", updatePhone.getUserId());
        updateWrapper.set("login_name", updatePhone.getNewPhone());
        updateWrapper.set("phone", updatePhone.getNewPhone());
        boolean update = tRecommendService.update(updateWrapper);
        if (update) {
            /*  stringRedisTemplate.delete(cc);*/
            stringRedisTemplate.delete(s);
            return R.success("更新成功");
        }
        return R.fail("更新失败!");
    }

    @ApiOperation("获取邀请的企业")
    @PostMapping("/inviterCompany")
    @Log(title = "获取邀请的企业", businessType = BusinessType.UPDATE)
    public R<TCompanyPO> inviterCompany(@Valid @RequestBody inviterQuery inviterQuery) {
        if (!StringUtils.pathEquals(inviterQuery.getLoginUser(), companyMetadata.userid())) {
            return R.fail("账号不一致!");
        }
        if (inviterQuery.getEndDate() != null || inviterQuery.getBeginDate() != null) {
            if (inviterQuery.getEndDate() != null) {
                inviterQuery.setEndDate(inviterQuery.getEndDate().plusDays(1));
            } else {
                inviterQuery.setEndDate(LocalDate.now().plusDays(1));
            }
            if (inviterQuery.getBeginDate() == null) {
                inviterQuery.setBeginDate(LocalDate.parse("2022-08-08"));

            }
        }
        IPage<TCompanyPO> tCompanyPO = tCompanyService.listInviterCompany(inviterQuery);
        return R.success(tCompanyPO);
    }

    @ApiOperation("获取邀请的求职者")
    @PostMapping("/inviterIndustry")
    @Log(title = "获取邀请的求职者", businessType = BusinessType.SELECT)
    public R<TJobhunterPO> inviterIndustry(@Valid @RequestBody inviterQuery inviterQuery) {
        if (!StringUtils.pathEquals(inviterQuery.getLoginUser(), companyMetadata.userid())) {
            return R.fail("账号不一致!");
        }
        if (inviterQuery.getEndDate() != null || inviterQuery.getBeginDate() != null) {
            if (inviterQuery.getEndDate() != null) {
                inviterQuery.setEndDate(inviterQuery.getEndDate().plusDays(1));
            } else {
                inviterQuery.setEndDate(LocalDate.now().plusDays(1));
            }
            if (inviterQuery.getBeginDate() == null) {
                inviterQuery.setBeginDate(LocalDate.parse("2022-08-08"));

            }
        }
        IPage<TJobhunterPO> tCompanyPO = tJobhunterService.listInviterIndustry(inviterQuery);
        return R.success(tCompanyPO);
    }


    @ApiOperation("解绑微信")
    @GetMapping("/unbindWechat")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", required = true, value = "电话号码"),
            @ApiImplicitParam(name = "code", required = true, value = "验证码")
    })
    @Log(title = "解绑微信", businessType = BusinessType.UPDATE)
    public R unbindWechat(@RequestParam("phone") String phone, @RequestParam("code") String code) {
        ValueOperations<String, String> stringValueOperations = stringRedisTemplate.opsForValue();
        String s = stringValueOperations.get(SmsEunm.UPDATE_PHONE.getType() + phone);
        String userid = companyMetadata.userid();
        if (code.equals(s)) {
            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("id", userid);
            updateWrapper.set("wx_openid", "");
            boolean update = tRecommendService.update(updateWrapper);
            if (update) {
                stringRedisTemplate.delete(SmsEunm.UPDATE_PHONE.getType() + phone);
                return R.success("解绑成功!");
            }
            return R.fail("解绑失败!");
        }
        return R.fail("验证码不正确或已过期!");
    }


    @ApiOperation("获取微信名称")
    @GetMapping("/weChatName")
    @Log(title = "获取微信名称", businessType = BusinessType.SELECT)
    public R weChatName() {
        TRecommend byId = tRecommendService.getById(companyMetadata.userid());
        Map map = new HashMap(2);
        if (StringUtils.isEmpty(byId.getWxOpenid())) {
            map.put("binding", false);
            map.put("name", "");
            return R.success(map);
        }

        map.put("binding", true);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("openid", byId.getWxOpenid());
        WechatUser one = wechatUserService.getOne(queryWrapper);

        map.put("name", one.getNickname());
        return R.success(map);
    }

    @ApiOperation("密码验证")
    @GetMapping("/validatePassword")
    @ApiImplicitParam(name = "password", required = true, value = "密码")
    @Log(title = "密码验证", businessType = BusinessType.SELECT)
    public R validatePassword(@RequestParam("password") String password) {
        TRecommend tRecommend = tRecommendService.getById(companyMetadata.userid());
        if (tRecommend == null) {
            return R.fail("该推荐官不存在！");
        }
        boolean matches = bCryptPasswordEncoder.matches(password, tRecommend.getPassword());

        if (!matches) {
            return R.fail("密码不正确!");
        }
        return R.success("验证通过!");
    }


    // @SneakyThrows
    @PostMapping("/getQRCode")
    @ApiOperation("获取邀请链接")
/*    @ApiImplicitParams({
            @ApiImplicitParam(name = "path", required = true, value = "链接地址"),
            @ApiImplicitParam(name = "qrWidth", required = true, value = "二维码宽度"),
            @ApiImplicitParam(name = "qrHeight", required = true, value = "二维码高度"),
            @ApiImplicitParam(name = "type", required = true, value = "身份(0:企业 1:推荐官 2:求职者 3:猎企 4:校园推荐官)")
    })*/
    @Log(title = "获取邀请链接", businessType = BusinessType.SELECT)
    public R getQRCode(@RequestBody QRCodeVO qrCodeVO) throws Exception {
        //添加分享记录
        if (qrCodeVO.getType() == null) {
            return R.fail("请选择邀请身份！");
        }
        shareAsync.addAmount(companyMetadata.userid(), qrCodeVO.getType());
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
        String s = cosUtils.uploadFile(encode, "/QRCode");
        /* redisUtils.sSet(InvitationCode.FILE_CODE.getType()).*/
        Map map = new HashMap();
        map.put("QRCode", s);
        map.put("https", qrCodeVO.getPath());
        redisUtils.set(InvitationCode.QR_CODE.getType() + companyMetadata.userid() + pathUrl + qrCodeVO.getQrWidth() + qrCodeVO.getQrHeight(), map, 60 * 60 * 2);//存2个小时
        // runningWaterAsync.shareRegistration(companyMetadata.userid());

        return R.success(map);
    }


    @GetMapping("/selects")
    public List<TRecommend> selects() {
        List<TRecommend> list = tRecommendService.list();
        return list;
    }


    /**
     * 获取推荐官及推荐官邀请人
     * id:推荐官id
     */
    @GetMapping("/getSharePersonRecommend")
    public Map<String, Object> getSharePersonRecommend(@RequestParam("id") String id) {
        return tRecommendService.getSharePersonRecommend(id);
    }


    @GetMapping("/getRecommendDataDTO")
    @ApiOperation("获取推荐官资产人才数据")
    @Log(title = "获取推荐官资产人才数据", businessType = BusinessType.SELECT)
    public R<RecommendDataDTO> getRecommendDataDTO() {
        String userid = companyMetadata.userid();
        TRecommend tRecommend = selectId(userid);
        if(tRecommend.getAdministrator()==2){
            //获取团队成员
          List<String>  list= tRecommendService.getMember(userid);
          if(list !=null && list.size()>0){
              List<RecommendDataDTO> recommendDataDTOList=  tRecommendService.getMemberRecommendDataDTO(list);
          }

        }
        RecommendDataDTO recommendDataDTO = tRecommendService.getRecommendDataDTO(userid);
        return R.success(recommendDataDTO);
    }



    /*
        @GetMapping("/deductCurrency")
        @Log(title = "下载简历", businessType = BusinessType.UPDATE)
        public Map<String, Object> deductCurrency(@RequestParam("userId") String userId, @RequestParam("jobHunter") String jobHunter, @RequestParam("orderId") String orderId) throws Exception {
            Map map=null;
            QueryWrapper queryWrapper=new QueryWrapper();
            queryWrapper.eq("t_job_hunter_id",jobHunter);
            queryWrapper.eq("recommend_id",userId);
            ReferrerRecord one = referrerRecordService.getOne(queryWrapper);
            if(one !=null){
                TJobhunter byId = tJobhunterService.getById(userId);
                if(byId.getCurrentSalary().compareTo(new BigDecimal(50))==1 || byId.getCurrentSalary().compareTo(new BigDecimal(50))==0 ){
                    map  = tRecommendService.deductCurrency(new BigDecimal(9), userId, jobHunter, orderId);
                    Integer state = (Integer) map.get("state");
                    if(state==0){

                    }
                }
            }else {
                map=new HashMap(2);
                map.put("state",1);
                map.put("msg", "该求职者未和你有确定关系!");
            }



            return map;
        }

    */
    private final static Integer FILE_SIZE = 10;//文件上传限制大小
    private final static String FILE_UNIT = "M";//文件上传限制单位（B,K,M,G）

    @PostMapping("/uploadFileIdentityCard")
    @ApiOperation(value = "身份证上传")
    @Log(title = "身份证上传", businessType = BusinessType.OTHER)
    public R uploadFileIdentityCard(MultipartFile file) throws Exception {
        if (ObjectUtils.isEmpty(file) || file.getSize() <= 0) {
            return R.fail("上传文件大小为空");
        }
        byte[] bytes = PicUtils.compressPicForScale(file.getBytes(), 120);

        boolean flag = checkFileSize(file.getSize(), FILE_SIZE, FILE_UNIT);
        if (!flag) {
            throw new RuntimeException("上传文件大小超出10M限制");
        }

        if (file == null) {
            return R.fail("上传文件失败:文件为空.");
        }
        String s = cosUtils.uploadFile(file, "/RMIdentityCard");
        return R.success("上传成功", s);
    }

    /**
     * @param len  文件长度
     * @param size 限制大小
     * @param unit 限制单位（B,K,M,G）
     * @描述 判断文件大小
     */
    public static boolean checkFileSize(Long len, int size, String unit) {
        double fileSize = 0;
        if ("B".equalsIgnoreCase(unit)) {
            fileSize = (double) len;
        } else if ("K".equalsIgnoreCase(unit)) {
            fileSize = (double) len / 1024;
        } else if ("M".equalsIgnoreCase(unit)) {
            fileSize = (double) len / 1048576;
        } else if ("G".equalsIgnoreCase(unit)) {
            fileSize = (double) len / 1073741824;
        }
        return !(fileSize > size);
    }

    @GetMapping("/postSharing")
    @ApiOperation(value = "分享注册金币奖励")
    @Log(title = "分享注册金币奖励", businessType = BusinessType.OTHER)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", required = true, value = "身份(0:企业 1:推荐官 2:求职者 3:猎企 4:校园推荐官)"),
            @ApiImplicitParam(name = "option", required = true, value = "选择(0:链接 1:海报 )")
    })
    public void goldReward(@RequestParam("type") Integer type, @RequestParam("option") Integer option) {
        String userid = companyMetadata.userid();
        String substring = userid.substring(0, 2);
        if(substring.equals("TR")){
            runningWaterAsync.shareRegistration(companyMetadata.userid(), type + "", option + "");
           if(type==0){
               shareDataAsync.inviteLink(companyMetadata.userid(),3);
           }else if(type==1){
               shareDataAsync.inviteLink(companyMetadata.userid(),4);
           }else if(type==2){
               shareDataAsync.inviteLink(companyMetadata.userid(),1);
           }else if(type==3){
               shareDataAsync.inviteLink(companyMetadata.userid(),2);
           }else if(type==4){
               shareDataAsync.inviteLink(companyMetadata.userid(),5);
           }
        }



    }


    @GetMapping("/selectBasic")
    @ApiImplicitParam(name = "id", required = true, value = "推荐官id")
    @Log(title = "获取推荐官基本信息", businessType = BusinessType.SELECT)
    @ApiOperation(value = "获取推荐官基本信息")
    public R<TRecommend> selectBasic(@RequestParam("id") String id) {
        TRecommend byId = tRecommendService.selectBasic(id);
        return R.success(byId);
    }


    @PostMapping("/realName")
    @Log(title = "推荐官实名认证", businessType = BusinessType.UPDATE)
    @ApiOperation(value = "推荐官实名认证")
    @AutoIdempotent
    public R realName(@RequestBody RealNameVO realNameVO) {
        String userid = companyMetadata.userid();
        Claims claimsFromJwt = JWT.getClaimsFromJwt(realNameVO.getKeyRate());
        if (claimsFromJwt == null) {
            return R.fail("操作时间过长，请重新操作!");
        }
        if (!CardUtil.isCard(realNameVO.getCard())) {
            return R.fail("请检查身份证是否正确!");
        }
        String substring = userid.substring(0, 2);
        if (substring.equals("TR")) {
            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("id", userid);
            updateWrapper.set("real_name", realNameVO.getRealName());
            updateWrapper.set("card", realNameVO.getCard());
            updateWrapper.set("card_front", realNameVO.getCardFront());
            updateWrapper.set("card_verso", realNameVO.getCardBack());
            boolean update = tRecommendService.update(updateWrapper);
            if (update) {
                //redisUtils.del(Cache.CACHE_RECOMMEND.getKey()+userid);
                cacheClient.deleteCache(Cache.CACHE_RECOMMEND.getKey(),userid);
                return R.success("更新成功!");
            }
            return R.fail("更新失败!");
        }
        return R.fail("请使用推荐官账号!");
    }


    @GetMapping("/selectId")
    public TRecommend selectId(@RequestParam("id") String id) {
        TRecommend tRecommend = cacheClient.queryWithLogicalExpireCount(Cache.CACHE_RECOMMEND.getKey(), id, TRecommend.class, tRecommendService::getById, 2L, TimeUnit.MINUTES);
        return tRecommend;
    }

    @GetMapping("/lists")
    public List<TRecommend> lists(@RequestParam("list") List<String> list){
        List<TRecommend> listTRecommend=new ArrayList<>(list.size());
      if(list !=null && list.size()>0){
          for (String s : list) {
              TRecommend tRecommend = cacheClient.queryWithLogicalExpireCount(Cache.CACHE_RECOMMEND.getKey(), s, TRecommend.class, tRecommendService::getById, 2L, TimeUnit.MINUTES);
             if(tRecommend !=null){
                 listTRecommend.add(tRecommend);
             }
          }
      }
      return listTRecommend;
    }


    @GetMapping("/enterpriseInviter")
    @Log(title = "获取企业的邀请人", businessType = BusinessType.SELECT)
    @ApiOperation(value = "获取企业的邀请人")
    @ApiImplicitParam(name = "companyId", required = true, value = "企业id")
    public R<TRecommend> enterpriseInviter(@RequestParam("companyId") String companyId){
        TRecommend tRecommend= tRecommendService.enterpriseInviter(companyId);
        return R.success(tRecommend);
    }

    /*@PostMapping("/updateCard")
    @Log(title = "修改身份证号", businessType = BusinessType.UPDATE)
    @ApiOperation(value = "修改身份证号")
    public R<TRecommend> updateCard(@RequestBody CardVo cardVo){

        return R.success(tRecommend);
    }*/


}
