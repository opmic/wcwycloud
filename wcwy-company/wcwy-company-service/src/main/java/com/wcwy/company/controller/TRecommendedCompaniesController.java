package com.wcwy.company.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wcwy.common.base.constant.Constanst;
import com.wcwy.common.base.enums.SmsEunm;
import com.wcwy.common.base.result.R;
import com.wcwy.common.base.utils.AesUtil;
import com.wcwy.common.base.utils.DateUtils;
import com.wcwy.common.base.utils.PhoneUtil;
import com.wcwy.common.redis.util.IDGenerator;
import com.wcwy.company.entity.*;
import com.wcwy.company.service.TRecommendedCompaniesRoleService;
import com.wcwy.company.service.TRecommendedCompaniesService;
import com.wcwy.company.service.WechatUserService;
import com.wcwy.company.service.WeixinLoginService;
import com.wcwy.company.session.CompanyMetadata;
import com.wcwy.company.vo.*;
import com.wcwy.system.annotation.Log;
import com.wcwy.system.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;

/**
 * ClassName: TRecommendedCompaniesController
 * Description:
 * date: 2022/12/8 17:21
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Api(tags = "企业推荐官接口")
@RestController
@RequestMapping("/tRecommendedCompanies")
@Slf4j
public class TRecommendedCompaniesController {
    @Autowired
    private TRecommendedCompaniesService tRecommendedCompaniesService;
    @Autowired
    private TRecommendedCompaniesRoleService tRecommendedCompaniesRoleService;
    @Autowired
    private IDGenerator idGenerator;
    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private CompanyMetadata companyMetadata;
    @Autowired
    private WeixinLoginService weixinLoginService;
    @Autowired
    private WechatUserService wechatUserService;

    /**
     * @param tRecommendedAcademyVO ：推荐校园注册信息
     * @return 成功状态
     * @Description: 推荐官校园版
     * @Author tangzhuo
     * @CreateTime 2022/12/9 13:36
     */

    @PostMapping("/addTRecommendedAcademy")
    @ApiOperation("校园注册")
    @Transactional
    @Log(title = "校园注册", businessType = BusinessType.INSERT)
    public R addTRecommendedAcademy(@Valid @RequestBody TRecommendedAcademyVO tRecommendedAcademyVO) throws Exception {
        boolean mobileNumber = PhoneUtil.isMobileNumber(tRecommendedAcademyVO.getPhoneNumber());
        if (!mobileNumber) {
            return R.fail("手机号码不规范!");
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("login_name", tRecommendedAcademyVO.getPhoneNumber());
        TRecommendedCompanies one = tRecommendedCompaniesService.getOne(queryWrapper);
        if (one != null) {
            return R.fail("该电话号码已使用!");
        }
        ValueOperations<String, String> stringValueOperations = stringRedisTemplate.opsForValue();
        String code = stringValueOperations.get(SmsEunm.INSERT_CODE.getType() + tRecommendedAcademyVO.getPhoneNumber());
        if (code == null) {
            return R.fail("验证码不正确!");
        }
        if (!code.equals(tRecommendedAcademyVO.getCode())) {
            return R.fail("验证码不正确!");
        }
        TRecommendedCompanies tRecommendedCompanies = new TRecommendedCompanies();
        BeanUtils.copyProperties(tRecommendedAcademyVO, tRecommendedCompanies);
        tRecommendedCompanies.setPassword(bCryptPasswordEncoder.encode(tRecommendedAcademyVO.getPassword()));
        tRecommendedCompanies.setRecommendedCompaniesId(idGenerator.generateCode("RC"));
        tRecommendedCompanies.setCreateTime(LocalDateTime.now());
        tRecommendedCompanies.setStatus("0");
        tRecommendedCompanies.setExamineStatus(0);
        tRecommendedCompanies.setCompanyType(2);
        tRecommendedCompanies.setLoginName(tRecommendedAcademyVO.getPhoneNumber());
        //以后记得删除
        tRecommendedCompanies.setExamineStatus(2);
        boolean save = tRecommendedCompaniesService.save(tRecommendedCompanies);
        if (save) {
            TRecommendedCompaniesRole tRecommendedCompaniesRole = new TRecommendedCompaniesRole();
            tRecommendedCompaniesRole.setTRecommendedCompaniesRoleId(tRecommendedCompanies.getRecommendedCompaniesId());
            tRecommendedCompaniesRole.setRoleId("6");
            tRecommendedCompaniesRole.setCreateTime(LocalDateTime.now());
            boolean save1 = tRecommendedCompaniesRoleService.save(tRecommendedCompaniesRole);
            if (save1) {
                stringRedisTemplate.delete(SmsEunm.INSERT_CODE.getType() + tRecommendedAcademyVO.getPhoneNumber());
                return R.success("注册成功!");
            }
            throw new Exception("注册失败!");
        }

        return R.fail("注册失败!");

    }

    @PostMapping("/addTRecommendedCompanies")
    @ApiOperation("企业推荐官注册")
    @Transactional
    @Log(title = "企业推荐官注册", businessType = BusinessType.INSERT)
    public R addTRecommendedCompanies(@Valid @RequestBody TRecommendedCompaniesVO tRecommendedCompaniesVO) throws Exception {
        boolean mobileNumber = PhoneUtil.isMobileNumber(tRecommendedCompaniesVO.getPhoneNumber());
        if (!mobileNumber) {
            return R.fail("手机号码不规范!");
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("login_name", tRecommendedCompaniesVO.getPhoneNumber());
        TRecommendedCompanies one = tRecommendedCompaniesService.getOne(queryWrapper);
        if (one != null) {
            return R.fail("该电话号码已使用!");
        }
        ValueOperations<String, String> stringValueOperations = stringRedisTemplate.opsForValue();
        String code = stringValueOperations.get(SmsEunm.INSERT_CODE.getType() + tRecommendedCompaniesVO.getPhoneNumber());
        if (code == null) {
            return R.fail("验证码不正确!");
        }
        if (!code.equals(tRecommendedCompaniesVO.getCode())) {
            return R.fail("验证码不正确!");
        }
        TRecommendedCompanies tRecommendedCompanies = new TRecommendedCompanies();
        BeanUtils.copyProperties(tRecommendedCompaniesVO, tRecommendedCompanies);
        tRecommendedCompanies.setPassword(bCryptPasswordEncoder.encode(tRecommendedCompaniesVO.getPassword()));
        tRecommendedCompanies.setRecommendedCompaniesId(idGenerator.generateCode("RC"));
        tRecommendedCompanies.setCreateTime(LocalDateTime.now());
        tRecommendedCompanies.setStatus("0");
        tRecommendedCompanies.setExamineStatus(0);
        tRecommendedCompanies.setCompanyType(1);
        tRecommendedCompanies.setLoginName(tRecommendedCompaniesVO.getPhoneNumber());
        //以后记得删除
        tRecommendedCompanies.setExamineStatus(2);
        boolean save = tRecommendedCompaniesService.save(tRecommendedCompanies);
        if (save) {
            TRecommendedCompaniesRole tRecommendedCompaniesRole = new TRecommendedCompaniesRole();
            tRecommendedCompaniesRole.setTRecommendedCompaniesRoleId(tRecommendedCompanies.getRecommendedCompaniesId());
            tRecommendedCompaniesRole.setRoleId("5");
            tRecommendedCompaniesRole.setCreateTime(LocalDateTime.now());
            boolean save1 = tRecommendedCompaniesRoleService.save(tRecommendedCompaniesRole);
            if (save1) {
                stringRedisTemplate.delete(SmsEunm.INSERT_CODE.getType() + tRecommendedCompaniesVO.getPhoneNumber());
                return R.success("注册成功!");
            }
            throw new Exception("注册失败!");
        }
        return R.fail("注册失败!");
    }


    @ApiOperation("修改密码")
    @PostMapping("/recommendedCompaniesUpdatePassword")
    @Log(title = "修改密码", businessType = BusinessType.UPDATE)
    public R TRecommendedCompaniesUpdatePassword(@Valid @RequestBody TRecommendedCompaniesUpdatePasswordVO updatePasswordVO) {
        String userid = companyMetadata.userid();
        TRecommendedCompanies byId = tRecommendedCompaniesService.getById(userid);
        if (byId == null) {
            return R.fail("该用户不存在!");
        }
        boolean matches = bCryptPasswordEncoder.matches(updatePasswordVO.getOldPassword(), byId.getPassword());
        if (!matches) {
            return R.fail("密码不正确!");
        }
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("recommended_companies_id", userid);
        updateWrapper.set("password", bCryptPasswordEncoder.encode(updatePasswordVO.getPassword()));
        boolean update = tRecommendedCompaniesService.update(updateWrapper);
        if (update) {
            return R.success("修改成功!");
        }
        return R.fail("修改失败!");
    }

    @GetMapping("/selectTRecommendedCompanies")
    @ApiOperation("使用token获取信息")
    @Log(title = "使用token获取信息", businessType = BusinessType.SELECT)
    public R<TRecommendedCompanies> selectTRecommendedCompanies() {
        TRecommendedCompanies byId = tRecommendedCompaniesService.getById(companyMetadata.userid());
        byId.setPassword("草泥马！敢偷窥密码");
        return R.success(byId);
    }


    @ApiOperation("企业通过密码更换登录手机号")
    @PostMapping("/passwordUpdatePhone")
    @Log(title = "企业通过密码更换登录手机号", businessType = BusinessType.UPDATE)
    public R passwordUpdatePhone(@Valid @RequestBody PasswordUpdatePhone updatePhone) {
        if (!companyMetadata.userid().equals(updatePhone.getUserId())) {
            return R.fail("登录与传入id不一致 请检查！");
        }
        TRecommendedCompanies byId = tRecommendedCompaniesService.getById(companyMetadata.userid());
        boolean matches = bCryptPasswordEncoder.matches(updatePhone.getPassword(), byId.getPassword());
        if (!matches) {
            return R.fail("密码不正确!");
        }
        //查询该手机号码有没有被使用
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("phone_number", updatePhone.getNewPhone());
        int count = tRecommendedCompaniesService.count(queryWrapper);
        if (count > 0) {
            return R.fail("该电话号码已被使用!");
        }
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("recommended_companies_id", updatePhone.getUserId());
        updateWrapper.set("login_name", updatePhone.getNewPhone());
        updateWrapper.set("phone_number", updatePhone.getNewPhone());
        boolean update = tRecommendedCompaniesService.update(updateWrapper);
        if (update) {
            return R.success("更新成功");
        }
        return R.fail("更新失败!");
    }

/*    @ApiOperation("企业更换登录手机号")
    @PostMapping("/updatePhone")
    public R updatePhone(@Valid @RequestBody UpdatePhone updatePhone) {
        if (!companyMetadata.userid().equals(updatePhone.getUserId())) {
            return R.fail("登录与传入id不一致 请检查！");
        }
        //查询该手机号码有没有被使用
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("phone_number", updatePhone.getNewPhone());
        int count = tRecommendedCompaniesService.count(queryWrapper);
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
        updateWrapper.eq("recommended_companies_id", updatePhone.getUserId());
        updateWrapper.set("login_name", updatePhone.getNewPhone());
        updateWrapper.set("phone_number", updatePhone.getNewPhone());
        boolean update = tRecommendedCompaniesService.update(updateWrapper);
        if (update) {
            return R.success("更新成功");
        }
        return R.fail("更新失败!");
    }*/

    //忘记密码修改密码
    @ApiOperation("忘记密码")
    @PostMapping("/forgetThePassword")
    @Log(title = "忘记密码", businessType = BusinessType.UPDATE)
    public R forgetThePassword(@Valid @RequestBody RCForgetThePassword rcForgetThePassword) {
        boolean mobileNumber = PhoneUtil.isMobileNumber(rcForgetThePassword.getPhone());
        if (!mobileNumber) {
            return R.fail("电话号码不正确");
        }
        String s = stringRedisTemplate.opsForValue().get(SmsEunm.UPDATE_CODE.getType() + rcForgetThePassword.getPhone());
        if (!rcForgetThePassword.getCode().equals(s)) {
            return R.fail("验证码不正确！");
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("login_name", rcForgetThePassword.getPhone());
        TRecommendedCompanies one = tRecommendedCompaniesService.getOne(queryWrapper);
        if (one == null) {
            return R.fail("用户不存在");
        }
        UpdateWrapper wrapper = new UpdateWrapper();
        wrapper.eq("login_name", rcForgetThePassword.getPhone());
        wrapper.set("password", bCryptPasswordEncoder.encode(rcForgetThePassword.getPassword()));
        wrapper.set("update_time", LocalDateTime.now());
        boolean update = tRecommendedCompaniesService.update(wrapper);
        if (update) {
            stringRedisTemplate.delete(SmsEunm.UPDATE_CODE.getType() + rcForgetThePassword.getPhone());
            return R.success("更新成功！");
        }
        return R.fail("更新失败!");
    }

    @ApiOperation("验证登录手机号码")
    @PostMapping("/verificationNumber")
        @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", required = true, value = "手机号码"),
            @ApiImplicitParam(name = "code", required = true, value = "验证码"),
    })
    @Log(title = "验证登录手机号码", businessType = BusinessType.SELECT)
    public R verificationNumber(@RequestParam String phone,@RequestParam String code){
        boolean mobileNumber = PhoneUtil.isMobileNumber(phone);
        if (!mobileNumber) {
            return R.fail("电话号码不正确");
        }
        String s = stringRedisTemplate.opsForValue().get(SmsEunm.UPDATE_CODE.getType() +phone);
        if (!code.equals(s)) {
            return R.fail("验证码不正确!",false);
        }

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("login_name", phone);
        TRecommendedCompanies one = tRecommendedCompaniesService.getOne(queryWrapper);
        if (one == null) {
            return R.fail("用户不存在",false);
        }
        return R.success("验证码正确!",true);
    }

    /**
     * 绑定用户
     *
     * @param code
     * @param state
     * @return
     */
    @GetMapping(value = "/binDing")
    @Transactional
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "code", value = "code", required = true),
            @ApiImplicitParam(dataType = "String", name = "state", value = "state", required = true),
            @ApiImplicitParam(dataType = "String", name = "id", value = "用户id", required = true)
    })
    @Log(title = "绑定用户", businessType = BusinessType.UPDATE)
    @ApiOperation(value = "绑定用户", notes = "绑定用户")
    public R binDing(String code, String state, String id) {

        if (!id.equals(companyMetadata.userid())) {
            return R.success("登录人与传入用户id不一致！");
        }
        String access_token = null;
        String openid = null;
        String userid = companyMetadata.userid();
        if (code != null && state != null) {
            // 验证state为了用于防止跨站请求伪造攻击
            String decrypt = AesUtil.decrypt(AesUtil.parseHexStr2Byte(state), AesUtil.PASSWORD_SECRET_KEY, 16);
            if (!decrypt.equals(Constanst.PWD_MD5 + DateUtils.getYYYYMMdd())) {
                return R.fail("登录失败，请联系管理员-请检查state!");
            }
            AccessToken access = weixinLoginService.getAccessToken(code);
            if (access.getAccess_token() != null && access.getOpenid() != null) {
                // 把获取到的access_token和openId赋值给变量
                access_token = access.getAccess_token();
                openid = access.getOpenid();
                // 存在则把当前账号信息授权给扫码用户
                // 拿到openid获取微信用户的基本信息
                // 此处可以写业务逻辑
                WechatUserUnionID userUnionID = weixinLoginService.getUserUnionID(access_token, openid);
                if (userUnionID == null) {
                    return R.fail("未从微信获取数据！请检查code是否正确");
                }
                boolean isok = false;
                //绑定求职者
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("openid", openid);
                int count = tRecommendedCompaniesService.count(queryWrapper);
                if (count > 0) {
                    return R.fail("该微信已使用!");
                }
                //查询用户是否存在
                TRecommendedCompanies byId = tRecommendedCompaniesService.getById(userid);
                if (byId == null) {
                    return R.fail("用户不存在");
                }
                //查询该微信账号是否绑定其他用户 则更行
                if (!openid.equals(byId.getOpenid())) {
                    UpdateWrapper updateWrapper = new UpdateWrapper();
                    updateWrapper.eq("recommended_companies_id", userid);
                    updateWrapper.set("openid", openid);
                    updateWrapper.set("update_time", LocalDateTime.now());
                    boolean update = tRecommendedCompaniesService.update(updateWrapper);
                    if (update) {
                        isok = true;
                    }
                }
                if (isok) {
                    //判断是否存在在
                    QueryWrapper queryWrapperOpenid = new QueryWrapper();
                    queryWrapperOpenid.eq("openid", openid);
                    WechatUser selectOne = wechatUserService.getOne(queryWrapper);
                    if (selectOne != null) {
                        WechatUser wechatUser = new WechatUser();
                        wechatUser.setId(selectOne.getId());
                        BeanUtils.copyProperties(userUnionID, wechatUser);
                        boolean b = wechatUserService.updateById(wechatUser);
                        if (!b) {
                            log.error("更新失败！openid={} 时间为{}", openid, System.currentTimeMillis());
                        }
                    } else {
                        WechatUser wechatUser = new WechatUser();
                        BeanUtils.copyProperties(userUnionID, wechatUser);
                        wechatUser.setId(idGenerator.generateCode("WX"));
                        boolean save = wechatUserService.save(wechatUser);
                        if (save) {
                            log.info("存入微信信息");
                        } else {
                            return R.error("存入用户信息失败！请联系管理员 ");
                        }
                    }

                    return R.success("绑定成功");
                }
            } else {
                return R.fail("您的扫码登录已过期,请重新扫码登录！");
            }
        }
        return R.error("绑定失败！请联系管理员");
    }

    @ApiOperation("解绑微信")
    @GetMapping(value = "/unbind")
    @Transactional
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "code", value = "code", required = true),
            @ApiImplicitParam(dataType = "String", name = "state", value = "state", required = true),
            @ApiImplicitParam(dataType = "String", name = "id", value = "用户id", required = true)
    })
    @Log(title = "绑定用户", businessType = BusinessType.UPDATE)
    public R unbind(String code, String state, String id, Integer companyType) {
        if (!id.equals(companyMetadata.userid())) {
            return R.success("登录人与传入用户id不一致！");
        }
        String access_token = null;
        String openid = null;
        String userid = companyMetadata.userid();
        if (code != null && state != null) {
            // 验证state为了用于防止跨站请求伪造攻击
            String decrypt = AesUtil.decrypt(AesUtil.parseHexStr2Byte(state), AesUtil.PASSWORD_SECRET_KEY, 16);
            if (!decrypt.equals(Constanst.PWD_MD5 + DateUtils.getYYYYMMdd())) {
                return R.fail("登录失败，请联系管理员-请检查state!");
            }
            AccessToken access = weixinLoginService.getAccessToken(code);
            if (access.getAccess_token() != null && access.getOpenid() != null) {
                // 把获取到的access_token和openId赋值给变量
                access_token = access.getAccess_token();
                openid = access.getOpenid();
                // 存在则把当前账号信息授权给扫码用户
                // 拿到openid获取微信用户的基本信息
                // 此处可以写业务逻辑
                WechatUserUnionID userUnionID = weixinLoginService.getUserUnionID(access_token, openid);
                if (userUnionID == null) {
                    return R.fail("未从微信获取数据！请检查code是否正确");
                }
                //绑定人才推荐

                TRecommendedCompanies byId = tRecommendedCompaniesService.getById(userid);
                if (byId == null) {
                        return R.fail("用户不存在");
                    }
                    //查询该微信账号是否绑定其他用户 则更新
                    if (openid.equals(byId.getOpenid())) {
                        UpdateWrapper updateWrapper = new UpdateWrapper();
                        updateWrapper.eq("id", userid);
                        updateWrapper.set("wx_openid", "");
                        updateWrapper.set("update_time", LocalDateTime.now());
                        updateWrapper.set("update_id", userid);
                        tRecommendedCompaniesService.update(updateWrapper);
                    }else {
                        return R.fail("微信号不一致 请检查!");
                    }
                return R.success("解绑成功！");
            }
        } else {
            return R.fail("您的扫码登录已过期,请重新扫码登录！");
        }
        return R.error("解绑失败！请联系管理员");
    }



}
