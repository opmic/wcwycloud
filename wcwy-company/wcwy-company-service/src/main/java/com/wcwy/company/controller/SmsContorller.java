package com.wcwy.company.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wcwy.common.base.enums.AccessTemplateCode;
import com.wcwy.common.base.enums.SmsEunm;
import com.wcwy.common.base.result.R;
import com.wcwy.common.base.utils.IpUtils;
import com.wcwy.common.base.utils.JWT;
import com.wcwy.common.base.utils.PhoneUtil;
import com.wcwy.common.base.utils.UUID;
import com.wcwy.common.redis.util.IDGenerator;
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.common.utils.IpUtil;
import com.wcwy.common.web.annotation.AutoIdempotent;
import com.wcwy.company.aliyun.SendSms;
import com.wcwy.company.entity.*;
import com.wcwy.company.service.*;
import com.wcwy.company.session.CompanyMetadata;
import com.wcwy.company.vo.SmsUpdatePasswordVO;
import com.wcwy.system.annotation.Log;
import com.wcwy.system.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.json.Json;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * ClassName: SmsContorller
 * Description:验证码接口
 * date: 2022/9/2 8:51
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Api(tags = "验证码接口")
@RestController
@RequestMapping("/sms")
@Slf4j
public class  SmsContorller {
    @Autowired
    private SendSms sendSms;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private TCompanyService tCompanyService;
    @Autowired
    private TRecommendService tRecommendService;
    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    @Autowired
    private CompanyMetadata companyMetadata;
    @Autowired
    private IDGenerator idGenerator;
    @Autowired
    private TJobhunterService tJobhunterService;
    @Autowired
    private TJobhunterRoleService tJobhunterRoleService;

    @Autowired
    private TJobhunterResumeService tJobhunterResumeService;
    @Autowired
    private TRecommendedCompaniesService tRecommendedCompaniesService;

    @Autowired
    private ReferrerRecordService referrerRecordService;
    /**
     * @Param:discern (1为登录 2为忘记修改密码)
     * @Param:userPhone (用户电话号码)
     * @Parm:companyType (0招聘企业 1猎头企业 2招聘者)
     * @return: AjaxResult
     * @Author: tangzhuo
     * @date: 2022-08-04
     * @Description:获取验证码
     */
    @GetMapping("/getPhone")
    @ApiOperation(value = "获取验证码", notes = "获取验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userPhone", required = true, value = "电话号码"),
            @ApiImplicitParam(name = "discern", value = "作用：2为忘记修改密码 3：注册", required = true),
            @ApiImplicitParam(name = "companyType", value = "身份：0企业 1推荐官 2求职者", required = true)
    })
    @Log(title = "获取验证码", businessType = BusinessType.SELECT)
    @AutoIdempotent
    public R getPhone(@RequestParam("userPhone") String userPhone, @RequestParam("discern") int discern, @RequestParam("companyType") int companyType,HttpServletRequest request) {
        R intercept = intercept(request);
        if(intercept.getCode()==405){
            return intercept;
        }
        //请求判断
        int identifying = 4;
        //(0招聘企业 1猎头企业 2招聘者)
        if (StrUtil.isEmpty(userPhone)) {
            return R.fail("电话号不能为空！");
        }
        String companyTypeString = companyType + "".trim();
        if (StrUtil.isEmpty(companyTypeString)) {
            return R.fail("请确定您的身份");
        }
        String discernString = discern + "".trim();
        if (StrUtil.isEmpty(discernString)) {
            return R.fail("作用不规范！");
        }
        boolean mobileNumber = PhoneUtil.isMobileNumber(userPhone);
        if (!mobileNumber) {
            return R.fail("电话号码不规范!");
        }

        //注册获取验证码
        if (discern == 3) {
            if (companyType == 0) {
                //判断当前电话号码是否已被使用
                QueryWrapper<TCompany> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("login_name", userPhone);
                TCompany company = tCompanyService.getOne(queryWrapper);
                if (company != null) {
                    return R.fail("该电话号码已被使用！");
                }
            } else if (companyType == 1) {
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("login_name", userPhone);
                TRecommend one = tRecommendService.getOne(queryWrapper);
                if (one != null) {
                    return R.fail("该电话号码已被使用！");
                }
            } else if (companyType == 2) {
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("login_name", userPhone);
                int count = tJobhunterService.count(queryWrapper);
                if (count > 0) {
                    return R.fail("该号码已被使用!");
                }
            }
            identifying = 3;
        }

        //查看人才招聘官是否存在
        if (companyType == 1 && discern == 2) {
            QueryWrapper<TRecommend> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("login_name", userPhone);
            TRecommend one = tRecommendService.getOne(queryWrapper);
            if (one != null) {
                if (one.getStatus().equals("1")) {
                    return R.fail("您已被禁用！");
                }
                identifying = 1;
            }

        }
        //招聘企业获取验证码
        if (companyType == 0 && discern == 2) {
            //获取招聘企业 或者猎头企业信息
            QueryWrapper<TCompany> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("login_name", userPhone);
            TCompany company = tCompanyService.getOne(queryWrapper);
            if (company != null) {
                //判断当前用户是否被禁用
                String status = company.getStatus();
                if (status.equals("1")) {
                    return R.fail("您已被禁用！");
                }
                identifying = 0;
            }
        }
        //求职者获取验证码
        if (companyType == 2 || discern == 2) {
            //获取求职者 或者求职者信息
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("login_name", userPhone);
            TJobhunter one = tJobhunterService.getOne(queryWrapper);
            if (one != null) {
                String status = one.getStatus();
                if (!StringUtils.isEmpty(status) && status.equals("1")) {
                    return R.fail("您已被禁用！");
                }
                identifying = 2;
            }
        }
        if (identifying == 4) {
            return R.error("用户不存在！请检查");
        }
        String s = UUID.randomCode();//生成6位随机数
        boolean b = this.codeCount(userPhone);

        Boolean flag = false;
        if (discern == 2) {
            //如果未超过3次 删除重新发
            if (b) {
                Boolean delete = stringRedisTemplate.delete(SmsEunm.UPDATE_CODE.getType() + userPhone);
            }
            flag = stringRedisTemplate.opsForValue().setIfAbsent(SmsEunm.UPDATE_CODE.getType() + userPhone, s, 60 * 10, TimeUnit.SECONDS);
        } else if (discern == 3) {
            //如果未超过3次 删除重新发
            if (b) {
                Boolean delete = stringRedisTemplate.delete(SmsEunm.INSERT_CODE.getType() + userPhone);
            }
            flag = stringRedisTemplate.opsForValue().setIfAbsent(SmsEunm.INSERT_CODE.getType() + userPhone, s, 60 * 30, TimeUnit.SECONDS);
        } else {
            return R.fail("操作有问题！请检查");
        }

        if (!flag) {
            return R.fail("请勿重复请求!您有请求过验证码未使用！");
        }
        String s1 = sendSms.SendSmsUtils(userPhone, s, AccessTemplateCode.RECHARGE.getName(), AccessTemplateCode.RECHARGE.getDesc());
        boolean okSms = this.isOKSms(s1);
        if (okSms) {
            Long age = stringRedisTemplate.opsForValue().increment(SmsEunm.CODE_COUNT.getType() + userPhone);
            return R.success("发送成功！");
        }
        //如果失败删除redis缓存
        if (discern == 2) {
            stringRedisTemplate.delete(SmsEunm.UPDATE_CODE.getType() + userPhone);
        } else if (discern == 3) {
            stringRedisTemplate.delete(SmsEunm.INSERT_CODE.getType() + userPhone);
        } else {
            return R.fail("操作有问题！请检查");
        }
        return R.fail("发送失败！系统繁忙");
    }

    @GetMapping("/verificationPhone")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", required = true, value = "电话号码"),
            @ApiImplicitParam(name = "code", required = true, value = "验证码"),
            @ApiImplicitParam(name = "discern", value = "作用：2为忘记修改密码 3：注册", required = true),
            @ApiImplicitParam(name = "identity", value = "身份 2:求职者", required = true),
            @ApiImplicitParam(name = "shareId", value = "分享注册人", required = false)
    })
    @ApiOperation(value = "手机号码验证")
    @Transactional(rollbackFor = Exception.class)
    @Log(title = "手机号码验证", businessType = BusinessType.SELECT)
    @AutoIdempotent
    public R verificationPhone(@RequestParam("phone") String phone, @RequestParam("code") String code, @RequestParam("discern") Integer discern, @RequestParam("identity") Integer identity, @RequestParam(value = "shareId",required = false) String shareId) {
        if (discern == null) {
            return R.fail("作用标记不能为空");
        }
        if (discern == 3) {
            String s = stringRedisTemplate.opsForValue().get(SmsEunm.INSERT_CODE.getType() + phone);
            if (code.equals(s)) {
                if (identity == 2) {
                    TJobhunter tJobhunter = new TJobhunter();
                    tJobhunter.setUserId(idGenerator.generateCode("TJ"));
                    tJobhunter.setStatus("0");
                    tJobhunter.setLoginName(phone);
                    tJobhunter.setPhoneNumber(phone);
                    tJobhunter.setPassword(bCryptPasswordEncoder.encode("wcwy888888"));
                    tJobhunter.setCreateTime(LocalDateTime.now());
                    tJobhunter.setAvatar("https://file-1313175594.cos.ap-guangzhou.myqcloud.com/iocn%2Fdefault_handsome.jpg");
                    tJobhunter.setSex(2);
                    if (!StringUtils.isEmpty(shareId)) {
                        tJobhunter.setSharePerson(shareId);
                    }
                    tJobhunter.setExamineStatus(0);
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
                        if (!StringUtils.isEmpty(shareId)) {
                            ReferrerRecord referrerRecord=new ReferrerRecord();
                            referrerRecord.setTJobHunterId(tJobhunter.getUserId());
                            referrerRecord.setRecommendId(shareId);
                            referrerRecord.setCorrelationType(0);
                            referrerRecord.setCreateTime(LocalDateTime.now());
                            boolean save1 = referrerRecordService.save(referrerRecord);
                        }

                        stringRedisTemplate.delete(SmsEunm.INSERT_CODE.getType() + phone);
                        return R.success("注册成功");
                    } else {
                        return R.fail("注册失败");
                    }
                }
                String  verifyPhone= JWT.verifyPhone(phone);
                return R.success("验证成功!",verifyPhone);
            }
        } else if (discern == 2) {
            String s = stringRedisTemplate.opsForValue().get(SmsEunm.UPDATE_CODE.getType() + phone);
            if (code.equals(s)) {
                return R.success("验证成功");
            }
        }

        return R.fail("验证失败");
    }

    @PostMapping("/updatePassword")
    @ApiOperation(value = "忘记密码")
    @Log(title = "忘记密码", businessType = BusinessType.SELECT)
    @AutoIdempotent
    public R updatePassword(@Valid @RequestBody SmsUpdatePasswordVO smsUpdatePasswordVO) {
        boolean mobileNumber = PhoneUtil.isMobileNumber(smsUpdatePasswordVO.getPhone());
        if (!mobileNumber) {
            return R.fail("电话号码不正确");
        }
        String s = stringRedisTemplate.opsForValue().get(SmsEunm.UPDATE_CODE.getType() + smsUpdatePasswordVO.getPhone());
        if (!smsUpdatePasswordVO.getCode().equals(s)) {
            return R.fail("验证码不正确！");
        }
        if (smsUpdatePasswordVO.getCompanyType().equals("0")) {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("login_name", smsUpdatePasswordVO.getPhone());
            TCompany one = tCompanyService.getOne(queryWrapper);
            if (one == null) {
                return R.fail("用户不存在");
            }
            UpdateWrapper wrapper = new UpdateWrapper();
            wrapper.eq("login_name", smsUpdatePasswordVO.getPhone());
            wrapper.set("password", bCryptPasswordEncoder.encode(smsUpdatePasswordVO.getPassword()));
            wrapper.set("update_time", LocalDateTime.now());
            boolean update = tCompanyService.update(wrapper);
            if (update) {
                stringRedisTemplate.delete(SmsEunm.UPDATE_CODE.getType() + smsUpdatePasswordVO.getPhone());
                return R.success("更新成功！");
            }
        }
        if (smsUpdatePasswordVO.getCompanyType().equals("1")) {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("login_name", smsUpdatePasswordVO.getPhone());
            TRecommend one = tRecommendService.getOne(queryWrapper);
            if (one == null) {
                return R.fail("用户不存在");
            }
            UpdateWrapper wrapper = new UpdateWrapper();
            wrapper.eq("login_name", smsUpdatePasswordVO.getPhone());
            wrapper.set("password", bCryptPasswordEncoder.encode(smsUpdatePasswordVO.getPassword()));
            wrapper.set("update_time", LocalDateTime.now());
            boolean update = tRecommendService.update(wrapper);
            if (update) {
                stringRedisTemplate.delete(SmsEunm.UPDATE_CODE.getType() + smsUpdatePasswordVO.getPhone());
                return R.success("更新成功！");
            }
        }
        if (smsUpdatePasswordVO.getCompanyType().equals("2")) {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("login_name", smsUpdatePasswordVO.getPhone());
            TJobhunter one = tJobhunterService.getOne(queryWrapper);
            if (one == null) {
                return R.fail("用户不存在");
            }
            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("login_name", smsUpdatePasswordVO.getPhone());
            updateWrapper.set("password", bCryptPasswordEncoder.encode(smsUpdatePasswordVO.getPassword()));
            updateWrapper.set("update_time", LocalDateTime.now());
            boolean update = tJobhunterService.update(updateWrapper);
            if (update) {
                stringRedisTemplate.delete(SmsEunm.UPDATE_CODE.getType() + smsUpdatePasswordVO.getPhone());
                return R.success("更新成功！");
            }
        }
        return R.fail("更新失败！");
    }


    /**
     * @param response
     * @return null
     * @Description: 判断验证码是否发送成功
     * @Author tangzhuo
     * @CreateTime 2022/9/27 14:48
     */

    public boolean isOKSms(String response) {
        if (StringUtils.isEmpty(response)) {
            return false;
        }
        JSONObject jsonObject = JSONObject.parseObject(response);
        String code = (String) jsonObject.get("code");
        if (!"OK".equals(code)) {
            log.error("发送短信出现了错误======" + jsonObject.toJSONString());
            return false;
        }
        return true;
    }

    @ApiOperation("验证手机号码获取验证码")
    @GetMapping("/updatePhone/{phone}")
    @ApiImplicitParam(name = "phone", required = true, value = "电话号码")
    @Log(title = "验证手机号码获取验证码", businessType = BusinessType.SELECT)
    @AutoIdempotent
    public R updatePhone(@PathVariable("phone") String phone,HttpServletRequest request) {
        if (StringUtils.isEmpty(phone)) {
            return R.fail("电话号码不能为空!");
        }
        R intercept = intercept(request);
        if(intercept.getCode()==405){
            return intercept;
        }
        String userid = companyMetadata.userid();
        boolean equals=false;
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("login_name",phone);
        String substring = userid.substring(0, 2);
        Integer integer=4;
        if("TC".equals(substring)){
            equals=  tCompanyService.count(queryWrapper)==0 ? true:false;
            integer=0;
            queryWrapper.eq("company_id",userid);
            int count = tCompanyService.count(queryWrapper);
            if(count==0){
                return R.fail("账号与手机号码不一致!");
            }
        }else if("TR".equals(substring)){
            equals  = tRecommendService.count(queryWrapper)==0 ? true:false;
            integer=1;
            queryWrapper.eq("id",userid);
            int count = tRecommendService.count(queryWrapper);
            if(count==0){
                return R.fail("账号与手机号码不一致!");
            }
        }else if("TJ".equals(substring)){
            equals = tJobhunterService.count(queryWrapper)==0 ? true:false;
            integer=2;
            queryWrapper.eq("user_id",userid);
            int count = tJobhunterService.count(queryWrapper);
            if(count==0){
                return R.fail("账号与手机号码不一致!");
            }
        }
        if(equals){
            return R.fail("该电话号码已被使用!");
        }
        String s = UUID.randomCode();//生成6位随机数
        boolean b = this.codeCount(phone);
        //如果未超过3次 删除重新发
        if (b) {
            Boolean delete = stringRedisTemplate.delete(SmsEunm.UPDATE_PHONE.getType() + phone);
        }
        Boolean aBoolean = stringRedisTemplate.opsForValue().setIfAbsent(SmsEunm.UPDATE_PHONE.getType() + phone, s, 60 * 10, TimeUnit.SECONDS);
        if (!aBoolean) {
            return R.fail("请勿重复请求!您有请求过验证码");
        }
        String s1="";
        if(integer==0){
            s1 = sendSms.SendSmsUtils(phone, s, AccessTemplateCode.UPDATE_PHONE_AD.getName(), AccessTemplateCode.UPDATE_PHONE_AD.getDesc());
        }else if(integer==1){
            s1 = sendSms.SendSmsUtils(phone, s, AccessTemplateCode.UPDATE_PHONE_RM.getName(), AccessTemplateCode.UPDATE_PHONE_RM.getDesc());
        }else if(integer==2){
            s1 = sendSms.SendSmsUtils(phone, s, AccessTemplateCode.UPDATE_PHONE_TJ.getName(), AccessTemplateCode.UPDATE_PHONE_TJ.getDesc());
        }
        if(StringUtils.isEmpty(s1)){
            return R.fail("身份不正确!");
        }
        boolean okSms = this.isOKSms(s1);
        if (okSms) {
            Long age = stringRedisTemplate.opsForValue().increment(SmsEunm.CODE_COUNT.getType() + phone);
            return R.success("发送成功!");
        }
        //未发送成功删除
        stringRedisTemplate.delete(SmsEunm.UPDATE_PHONE.getType() + phone);
        return R.fail("发送失败！系统繁忙");
    }

    @GetMapping("/getPhoneCode")
    @ApiOperation(value = "企业推荐官及校园版获取验证码", notes = "企业推荐官及校园版获取验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userPhone", required = true, value = "电话号码"),
            @ApiImplicitParam(name = "discern", value = "作用：1:忘记密码 2注册 3：更新电话号码验证码", required = true),
            @ApiImplicitParam(name = "companyType", value = "身份：1推荐官企业 2校园", required = true)
    })
    @Log(title = "企业推荐官及校园版获取验证码", businessType = BusinessType.SELECT)
    @AutoIdempotent
    public R getPhoneCode(@RequestParam("userPhone") String userPhone, @RequestParam("discern") int discern, @RequestParam("companyType") int companyType) {
        if (StringUtils.isEmpty(userPhone) || StringUtils.isEmpty(discern) || StringUtils.isEmpty(companyType)) {
            return R.fail("数据不能为空!");
        }
        ValueOperations<String, String> stringValueOperations = stringRedisTemplate.opsForValue();
        String s = UUID.randomCode();//生成6位随机数
        boolean b = this.codeCount(userPhone);
        if (discern == 2) {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("login_name", userPhone);
            TRecommendedCompanies one = tRecommendedCompaniesService.getOne(queryWrapper);
            if (one != null) {
                String cc = one.getCompanyType() == 1 ? "企业推荐官" : "校园推荐官";
                return R.fail("该手机号已注册为:" + cc);
            }
            //如果未超过3次 删除重新发
            if (b) {
                Boolean delete = stringRedisTemplate.delete(SmsEunm.INSERT_CODE.getType() + userPhone);
            }
            Boolean aBoolean = stringValueOperations.setIfAbsent(SmsEunm.INSERT_CODE.getType() + userPhone, s, 60 * 5, TimeUnit.SECONDS);
            if (!aBoolean) {
                return R.fail("你已经重复发送多次");
            }
            String s1 = sendSms.SendSmsUtils(userPhone, s, AccessTemplateCode.RECHARGE.getName(), AccessTemplateCode.RECHARGE.getDesc());
            boolean okSms = this.isOKSms(s1);
            if (okSms) {
                //发送成功则加1
                Long age = stringValueOperations.increment(SmsEunm.CODE_COUNT.getType() + userPhone);
                return R.success("发送成功!");
            }
            //未发送成功删除
            stringRedisTemplate.delete(SmsEunm.INSERT_CODE.getType() + userPhone);
            return R.fail("发送失败！系统繁忙请重新发送");
        } else if (discern == 3) {
            //如果未超过3次 删除重新发
            if (b) {
                Boolean delete = stringRedisTemplate.delete(SmsEunm.UPDATE_PHONE.getType() + userPhone);
            }
            Boolean aBoolean = stringValueOperations.setIfAbsent(SmsEunm.UPDATE_PHONE.getType() + userPhone, s, 60 * 5, TimeUnit.SECONDS);
            if (!aBoolean) {
                return R.fail("你已经重复发送多次");
            }
            String s1 = sendSms.SendSmsUtils(userPhone, s, AccessTemplateCode.RECHARGE.getName(), AccessTemplateCode.RECHARGE.getDesc());
            boolean okSms = this.isOKSms(s1);
            if (okSms) {
                //发送成功则加1
                Long age = stringValueOperations.increment(SmsEunm.CODE_COUNT.getType() + userPhone);
                return R.success("发送成功!");
            }
            //未发送成功删除
            stringRedisTemplate.delete(SmsEunm.UPDATE_PHONE.getType() + userPhone);
            return R.fail("发送失败！系统繁忙请重新发送");

            //忘记密码
        }else if(discern == 1){
            //如果未超过3次 删除重新发
            if (b) {
                Boolean delete = stringRedisTemplate.delete(SmsEunm.UPDATE_CODE.getType() + userPhone);
            }
            Boolean aBoolean = stringValueOperations.setIfAbsent(SmsEunm.UPDATE_CODE.getType() + userPhone, s, 60 * 5, TimeUnit.SECONDS);
            if (!aBoolean) {
                return R.fail("你已经重复发送多次");
            }
            String s1 = sendSms.SendSmsUtils(userPhone, s, AccessTemplateCode.RECHARGE.getName(), AccessTemplateCode.RECHARGE.getDesc());
            boolean okSms = this.isOKSms(s1);
            if (okSms) {
                //发送成功则加1
                Long age = stringValueOperations.increment(SmsEunm.CODE_COUNT.getType() + userPhone);
                return R.success("发送成功!");
            }
            //未发送成功删除
            stringRedisTemplate.delete(SmsEunm.CODE_COUNT.getType() + userPhone);
            return R.fail("发送失败！系统繁忙请重新发送");
        }
        return R.fail("发送失败！系统繁忙请重新发送");

    }

    //判断验证码是否已经发送5次
    public boolean codeCount(String phone) {
        ValueOperations<String, String> stringValueOperations = stringRedisTemplate.opsForValue();
        String s = stringValueOperations.get(SmsEunm.CODE_COUNT.getType() + phone);
        if(StringUtils.isEmpty(s)){
            stringValueOperations.increment(SmsEunm.CODE_COUNT.getType() + phone);
            long secondsLeftToday = 86400 - DateUtils.getFragmentInSeconds(Calendar.getInstance(), Calendar.DATE);
            stringRedisTemplate.expire(SmsEunm.CODE_COUNT.getType() + phone,secondsLeftToday,TimeUnit.SECONDS);
            s = stringValueOperations.get(SmsEunm.CODE_COUNT.getType() + phone);
        }
        Integer increment = Integer.valueOf(s);

        if (5 < increment) {
            return false;
        }
        return true;
    }


    @ApiOperation("重置密码获取验证码")
    @GetMapping("/resetPasswords/{phone}")
    @ApiImplicitParam(name = "phone", required = true, value = "电话号码")
    @Log(title = "重置密码获取验证码", businessType = BusinessType.SELECT)
    @AutoIdempotent
    public R resetPasswords(@PathVariable("phone") String phone,HttpServletRequest request) {
        if (StringUtils.isEmpty(phone)) {
            return R.fail("电话号码不能为空!");
        }
        R intercept = intercept(request);
        if(intercept.getCode()==405){
            return intercept;
        }
        Boolean authentication = this.authentication(phone);
        if(! authentication){
            return R.fail("登录账号与电话号码不一致!");
        }
        String s = UUID.randomCode();//生成6位随机数
        boolean b = this.codeCount(phone);
        //如果未超过3次 删除重新发
        if (b) {
            Boolean delete = stringRedisTemplate.delete(SmsEunm.PASSWORD_CODE.getType() + phone);
        }
        Boolean aBoolean = stringRedisTemplate.opsForValue().setIfAbsent(SmsEunm.PASSWORD_CODE.getType() + phone, s, 60 * 5, TimeUnit.SECONDS);
        if (!aBoolean) {
            return R.fail("请勿重复请求!您有请求过验证码");
        }
        String s1 = sendSms.SendSmsUtils(phone, s, AccessTemplateCode.CHANGE_PASSWORD.getName(), AccessTemplateCode.CHANGE_PASSWORD.getDesc());
        boolean okSms = this.isOKSms(s1);
        if (okSms) {
            Long age = stringRedisTemplate.opsForValue().increment(SmsEunm.CODE_COUNT.getType() + phone);
            return R.success("发送成功!");
        }
        //未发送成功删除
        stringRedisTemplate.delete(SmsEunm.PASSWORD_CODE.getType() + phone);
        return R.fail("发送失败！系统繁忙");
    }

    @ApiOperation("绑定获取验证码")
    @GetMapping("/bindingPhone")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", required = true, value = "电话号码")
    })
    @Log(title = "绑定获取验证码", businessType = BusinessType.SELECT)
    @AutoIdempotent
    public R bindingPhone(@RequestParam("phone") String phone,HttpServletRequest request){
        if (StringUtils.isEmpty(phone)) {
            return R.fail("电话号码不能为空!");
        }
        R intercept = intercept(request);
        if(intercept.getCode()==405){
            return intercept;
        }
        String userid = companyMetadata.userid();
        boolean equals=false;
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("login_name",phone);
        String substring = userid.substring(0, 2);
        if("TC".equals(substring)){
            equals=  tCompanyService.count(queryWrapper)==0 ? true:false;

        }else if("TR".equals(substring)){
            equals  = tRecommendService.count(queryWrapper)==0 ? true:false;
        }else if("TJ".equals(substring)){
             equals = tJobhunterService.count(queryWrapper)==0 ? true:false;
        }
        if(equals){
            String s = UUID.randomCode();//生成6位随机数
            boolean b = this.codeCount(phone);
            //如果未超过3次 删除重新发
            if (b) {
                Boolean delete = stringRedisTemplate.delete(SmsEunm.BINDING_PHONE.getType() + phone);
            }
            Boolean aBoolean = stringRedisTemplate.opsForValue().setIfAbsent(SmsEunm.BINDING_PHONE.getType() + phone, s, 60 * 10, TimeUnit.SECONDS);
            if (!aBoolean) {
                return R.fail("请勿重复请求!您有请求过验证码");
            }
            String s1 = sendSms.SendSmsUtils(phone, s, AccessTemplateCode.BINDING_CODE.getName(), AccessTemplateCode.BINDING_CODE.getDesc());
            boolean okSms = this.isOKSms(s1);
            if (okSms) {
                Long age = stringRedisTemplate.opsForValue().increment(SmsEunm.CODE_COUNT.getType() + phone);
                return R.success("发送成功!");
            }
            //未发送成功删除
            stringRedisTemplate.delete(SmsEunm.BINDING_PHONE.getType() + phone);
            return R.fail("发送失败！系统繁忙");
        }
        return R.fail("该电话号码已被使用!");
    }

/*    @ApiOperation("验证绑定获取验证码")
    @GetMapping("/verifyBindingPhone")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", required = true, value = "电话号码"),
            @ApiImplicitParam(name = "code", required = true, value = "验证码")
    })
    public R verifyBindingPhone(@RequestParam("phone") String phone,@RequestParam("code") String code){
        ValueOperations<String, String> stringValueOperations = stringRedisTemplate.opsForValue();
        String s = stringValueOperations.get(SmsEunm.BINDING_PHONE.getType() + phone);
        if(code.equals(s)){
            return R.success("验证码正确！");
        }
        return R.fail("验证码不正确!");
    }*/
    @ApiOperation("验证身份安全")
    @GetMapping("/verifyUpdatePhone")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", required = true, value = "电话号码"),
            @ApiImplicitParam(name = "code", required = true, value = "验证码")
    })
    @Log(title = "验证身份安全", businessType = BusinessType.SELECT)
    @AutoIdempotent
    public R verifyUpdatePhone(@RequestParam("phone") String phone,@RequestParam("code") String code){
        ValueOperations<String, String> stringValueOperations = stringRedisTemplate.opsForValue();
        String s = stringValueOperations.get(SmsEunm.UPDATE_PHONE.getType() + phone);
        if(code.equals(s)){
            return R.success("验证码正确！");
        }
        return R.fail("验证码不正确或已过期!");
    }

    /**
     * 判断该手机号是存在
     * @param phone
     * @return
     */

    public Boolean authentication(String  phone){
        String userid = companyMetadata.userid();
        String substring = userid.substring(0, 2);
        boolean equals=false;
        QueryWrapper queryWrapper=new QueryWrapper();

        if("TC".equals(substring)){
            queryWrapper.eq("company_id",userid);
            queryWrapper.eq("login_name",phone);
            equals=  tCompanyService.count(queryWrapper)>0 ? true:false;

        }else if("TR".equals(substring)){
            queryWrapper.eq("id",userid);
            queryWrapper.eq("login_name",phone);
            equals  = tRecommendService.count(queryWrapper)>0 ? true:false;
        }else if("TJ".equals(substring)){
            queryWrapper.eq("user_id",userid);
            queryWrapper.eq("login_name",phone);
            equals  = tJobhunterService.count(queryWrapper)>0 ? true:false;
        }
        return equals;
    }
    @ApiOperation("重置密码验证")
    @GetMapping("/verifyResetPasswords")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", required = true, value = "电话号码"),
            @ApiImplicitParam(name = "code", required = true, value = "验证码")
    })
    @Log(title = "重置密码验证", businessType = BusinessType.SELECT)
    @AutoIdempotent
    public R verifyResetPasswords(@RequestParam("phone") String phone,@RequestParam("code") String code){
        ValueOperations<String, String> stringValueOperations = stringRedisTemplate.opsForValue();
        String s = stringValueOperations.get(SmsEunm.PASSWORD_CODE.getType() + phone);
        if(code.equals(s)){
            return R.success("验证码正确！");
        }
        return R.fail("验证码不正确或已过期!");
    }


    @GetMapping("/loginCode")
    @ApiOperation("获取登录验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", required = true, value = "电话号码"),
            @ApiImplicitParam(name = "companyType", value = "身份：0企业 1推荐官 2求职者", required = true)
    })
    @Log(title = "获取登录验证码", businessType = BusinessType.OTHER)
    @AutoIdempotent
    public R loginCode(@RequestParam("phone") String phone, @RequestParam("companyType") Integer companyType, HttpServletRequest request){
        if (StringUtils.isEmpty(phone)) {
            return R.fail("电话号码不能为空!");
        }

        boolean isExist=true;
        //判断手机号码是否存在
        QueryWrapper queryWrapper=new QueryWrapper();
        if(companyType==0){
            queryWrapper.eq("login_name",phone);
            int count = tCompanyService.count(queryWrapper);
            if(count ==0){

                isExist=false;
                R phone1 = this.getPhone(phone, 3, companyType, request);
                phone1.setData(isExist);
                return phone1;
            }
        }else if(companyType==1){
            queryWrapper.eq("login_name",phone);
            int count = tRecommendService.count(queryWrapper);
            if(count ==0){
                //return R.fail("账号不存在!");
                // return R.fail("账号不存在!");
                isExist=false;
                R phone1 = this.getPhone(phone, 3, companyType, request);
                phone1.setData(isExist);
                return phone1;
            }
        }else if(companyType <0 || companyType >2){
            return R.fail("身份不正确!");
        }
        R intercept = intercept(request);
        if(intercept.getCode()==405){
            return intercept;
        }
        //发送验证码
        String s = UUID.randomCode();//生成6位随机数

        boolean b = this.codeCount(phone);
        //如果未超过3次 删除重新发
        if (b) {
            Boolean delete = stringRedisTemplate.delete(SmsEunm.LOGIN_CODE.getType() + phone);
        }
        Boolean aBoolean = stringRedisTemplate.opsForValue().setIfAbsent(SmsEunm.LOGIN_CODE.getType() + phone, s, 60 * 10, TimeUnit.SECONDS);
        if (!aBoolean) {
            return R.fail("请勿重复请求!您有请求过验证码");
        }
        String s1 = sendSms.SendSmsUtils(phone, s, AccessTemplateCode.LOGIN_CODE.getName(), AccessTemplateCode.LOGIN_CODE.getDesc());
        boolean okSms = this.isOKSms(s1);
        if (okSms) {
            Long age = stringRedisTemplate.opsForValue().increment(SmsEunm.CODE_COUNT.getType() + phone);
            return R.success("发送成功!",isExist);
        }
        //未发送成功删除
        stringRedisTemplate.delete(SmsEunm.LOGIN_CODE.getType() + phone);
        return R.fail("发送失败！系统繁忙");
    }

    public R intercept(HttpServletRequest request){
        String ip = IpUtil.getIpAddr(request);
        ValueOperations<String, String> stringValueOperations = stringRedisTemplate.opsForValue();
        String s = stringValueOperations.get(SmsEunm.CODE_COUNT.getType() + ip);
        stringValueOperations.increment(SmsEunm.CODE_COUNT.getType() + ip);
        if(StringUtils.isEmpty(s)){

            stringRedisTemplate.expire(SmsEunm.CODE_COUNT.getType() + ip, 12, TimeUnit.HOURS);
            s = stringValueOperations.get(SmsEunm.CODE_COUNT.getType() + ip);
        }

        Integer increment = Integer.valueOf(s);
        if(increment>60){
            return R.fail("请求繁忙！");
        }
        return R.success();
    }

    @GetMapping("/authenticationCode")
    @ApiImplicitParam(name = "phone", value = "电话号码", required = true)
    @ApiOperation("实名验证获取验证码")
    @Log(title = "实名验证获取验证码", businessType = BusinessType.SELECT)
    @AutoIdempotent
    public R authenticationCode(@RequestParam("phone") String phone){
      /*  TRecommend byId = tRecommendService.getById(companyMetadata.userid());
      */
        String userid = companyMetadata.userid();
        String substring = userid.substring(0, 2);
        String loginName="";
        if("TR".equals(substring)){
            TRecommend byId = tRecommendService.getById(companyMetadata.userid());
            loginName=byId.getLoginName();
        }else if("TC".equals(substring)){
            TCompany byId = tCompanyService.getById(companyMetadata.userid());
            loginName=byId.getLoginName();
        }
        if(!StringUtils.pathEquals(loginName,phone)){
            return R.fail("电话号码不一致！");
        }
        if(StringUtils.isEmpty(loginName)){
            return R.fail("请联系管理员！");
        }

        String s = UUID.randomCode();//生成6位随机数
        String s1 = sendSms.SendSmsUtils(phone, s, AccessTemplateCode.LOGIN_CODE.getName(), AccessTemplateCode.LOGIN_CODE.getDesc());
        boolean okSms = this.isOKSms(s1);
        if (okSms) {
            Map<String, Object> map = redisUtils.verificationCode(SmsEunm.AUTHENTICATION_CODE.getType(), phone, s, 300);
            boolean isOk =(Boolean) map.get("isOk");
            if(isOk) {
                return R.success();
            }else {
                if(! StringUtils.isEmpty(map.get("msg"))){
                    return R.fail(map.get("msg").toString());
                }
            }
        }
        return R.fail();
    }

    @GetMapping("/verify")
    @ApiImplicitParam(name = "code", value = "电话号码", required = true)
    @ApiOperation("验证实名验证获取验证码")
    @Log(title = "验证实名验证获取验证码", businessType = BusinessType.OTHER)
    @AutoIdempotent
   public R verify(@RequestParam("code") String code){
        String userid = companyMetadata.userid();
        String substring = userid.substring(0, 2);
        String loginName="";
        if("TR".equals(substring)){
            TRecommend byId = tRecommendService.getById(companyMetadata.userid());
            loginName=byId.getLoginName();
        }else if("TC".equals(substring)){
            TCompany byId = tCompanyService.getById(companyMetadata.userid());
            loginName=byId.getLoginName();
        }

        if(StringUtils.isEmpty(loginName)){
            return R.fail("请联系管理员！");
        }

        Object o = redisUtils.get(SmsEunm.AUTHENTICATION_CODE.getType() + loginName);
        if (StringUtils.isEmpty(o)) {
            return R.fail("验证码已过期!");
        }
        if (!StringUtils.pathEquals(o.toString(), code)) {
            return R.fail("验证码不正确!");
        }
        String  verifyPhone= JWT.verifyPhone(loginName);
        return R.success(verifyPhone);

    }


}
