package com.wcwy.company.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wcwy.common.base.constant.Constanst;
import com.wcwy.common.base.result.R;
import com.wcwy.common.base.utils.AesUtil;
import com.wcwy.common.base.utils.DateUtils;
import com.wcwy.common.redis.util.IDGenerator;
import com.wcwy.company.entity.TCompany;
import com.wcwy.company.entity.TJobhunter;
import com.wcwy.company.entity.TRecommend;
import com.wcwy.company.entity.WechatUser;
import com.wcwy.company.service.*;
import com.wcwy.company.session.CompanyMetadata;
import com.wcwy.company.vo.AccessToken;
import com.wcwy.company.vo.WechatUserUnionID;
import com.wcwy.system.annotation.Log;
import com.wcwy.system.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ClassName: WXLoginUtils
 * Description:
 * date: 2022/9/26 8:30
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@RequestMapping("/wxLogin")
@Api(tags = "微信登录工具类")
@Slf4j
@Controller
public class WXLoginController {
    @Autowired
    private IDGenerator idGenerator;
    @Autowired
    private WeixinLoginService weixinLoginService;
    @Autowired
    private WechatUserService wechatUserService;
    @Autowired
    private TCompanyService tCompanyService;
    @Autowired
    private TJobhunterService tJobhunterService;
    @Autowired
    private CompanyMetadata CompanyMetadata;
    @Autowired
    private TRecommendService tRecommendService;

    @GetMapping("/state")
    @ApiOperation(value = "生成state", notes = "生成state")
    @ResponseBody
    @Log(title = "生成state", businessType = BusinessType.OTHER)
    public R gainState() {
        String state = weixinLoginService.state();
        return R.success(state);
    }

    /**
     * 重定向到微信扫码登录地址
     *
     * @return
     */
    @GetMapping("/weixinLogin")
    public String weixinLogin() {
        String url = weixinLoginService.genLoginUrl();
        return "redirect:" + url;
    }

    /**
     * 回调地址处理
     * @param code
     * @param state
     * @return
     */
  /*  @GetMapping(value = "/weixinconnect")
    @ResponseBody
    public ModelAndView callback(String code, String state) {
        System.out.println(code);
        System.out.println(state);
        String access_token=null;
        String openid=null;
        ModelAndView mav=new ModelAndView();
        if (code != null && state != null) {
            // 验证state为了用于防止跨站请求伪造攻击
            String decrypt = AesUtil.decrypt(AesUtil.parseHexStr2Byte(state), AesUtil.PASSWORD_SECRET_KEY, 16);
            if (!decrypt.equals(Constanst.PWD_MD5 + DateUtils.getYYYYMMdd())) {
                mav.addObject("error","登录失败，请联系管理员！");
                mav.setViewName("loginError");
                return mav;
            }
            AccessToken access = weixinLoginService.getAccessToken(code);
            if (access != null) {
                // 把获取到的access_token和openId赋值给变量
                access_token=access.getAccess_token();
                openid=access.getOpenid();

                // 存在则把当前账号信息授权给扫码用户
                // 拿到openid获取微信用户的基本信息

                // 此处可以写业务逻辑

                WechatUserUnionID userUnionID = weixinLoginService.getUserUnionID(access_token,openid);
                mav.addObject("userInfo",userUnionID);
                mav.setViewName("main");
                return mav;

            }
       }
        return "code==="+code +"==sta"+state;
    }*/

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
            @ApiImplicitParam(dataType = "String", name = "id", value = "用户id", required = true),
            @ApiImplicitParam(dataType = "int", name = "companyType", value = "企业属性(0招聘企业 1人才推荐,2求职者)", required = true)
    })
    @Log(title = "绑定用户", businessType = BusinessType.UPDATE)
    @ApiOperation(value = "绑定用户", notes = "绑定用户")
    public R binDing(String code, String state, String id, Integer companyType) {

        if (!id.equals(CompanyMetadata.userid())) {
            return R.success("登录人与传入用户id不一致！");
        }
        String access_token = null;
        String openid = null;
        String userid = CompanyMetadata.userid();
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
                if (companyType == 2) {
                    QueryWrapper queryWrapper=new QueryWrapper();
                    queryWrapper.eq("openid",openid);
                    int count = tJobhunterService.count(queryWrapper);
                    if(count>0){
                        return R.fail("该微信已使用!");
                    }
                    //查询用户是否存在
                    TJobhunter one = tJobhunterService.getById(userid);
                    if(one ==null){
                        return R.fail("用户不存在");
                    }
                    //查询该微信账号是否绑定其他用户 则更行
                    if (!openid.equals(one.getOpenid())) {
                        UpdateWrapper updateWrapper = new UpdateWrapper();
                        updateWrapper.eq("user_id", userid);
                        updateWrapper.set("openid", openid);
                        updateWrapper.set("update_time", LocalDateTime.now());
                        tJobhunterService.update(updateWrapper);
                    }
                    isok = true;
                    //查询该微信账号是否绑定其他用户
                }


                //招聘企业
                if (companyType == 0) {
                    QueryWrapper queryWrapper=new QueryWrapper();
                    queryWrapper.eq("openid",openid);
                    int count = tCompanyService.count(queryWrapper);
                    if(count>0){
                        return R.fail("该微信已使用!");
                    }
                    TCompany id1 = tCompanyService.getId(userid);
                    if (id1 == null) {
                        return R.fail("用户不存在");
                    }
                    //查询该微信账号是否绑定其他用户 则更行
                    if (!openid.equals(id1.getOpenid())) {
                        UpdateWrapper updateWrapper = new UpdateWrapper();
                        updateWrapper.eq("company_id", userid);
                        updateWrapper.set("openid", openid);
                        updateWrapper.set("update_time", LocalDateTime.now());
                        tCompanyService.update(updateWrapper);
                    }
                    isok = true;
                }


                //绑定人才推荐
                if (companyType == 1) {
                    QueryWrapper queryWrapper=new QueryWrapper();
                    queryWrapper.eq("wx_openid",openid);
                    int count = tRecommendService.count(queryWrapper);
                    if(count>0){
                        return R.fail("该微信已使用!");
                    }
                    TRecommend byId = tRecommendService.getById(userid);
                    if (byId == null) {
                        return R.fail("用户不存在");
                    }
                    //查询该微信账号是否绑定其他用户 则更新
                    if (!openid.equals(byId.getWxOpenid())) {
                        UpdateWrapper updateWrapper = new UpdateWrapper();
                        updateWrapper.eq("id", userid);
                        updateWrapper.set("wx_openid", openid);
                        updateWrapper.set("update_time", LocalDateTime.now());
                        updateWrapper.set("update_id", userid);
                        tRecommendService.update(updateWrapper);
                    }
                    isok = true;
                }


                if (isok) {
                    //判断是否存在在
                    QueryWrapper queryWrapper = new QueryWrapper();
                    queryWrapper.eq("openid", openid);
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
            @ApiImplicitParam(dataType = "String", name = "id", value = "用户id", required = true),
            @ApiImplicitParam(dataType = "int", name = "companyType", value = "企业属性(0招聘企业 1人才推荐,2求职者)", required = true)
    })
    @Log(title = "解绑微信", businessType = BusinessType.UPDATE)
    public R unbind(String code, String state, String id, Integer companyType) {
        if (!id.equals(CompanyMetadata.userid())) {
            return R.success("登录人与传入用户id不一致！");
        }
        String access_token = null;
        String openid = null;
        String userid = CompanyMetadata.userid();
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
                //绑定求职者
                if (companyType == 2) {
                    //查询用户是否存在
                    QueryWrapper queryWrapper=new QueryWrapper();
                    queryWrapper.eq("user_id",userid);
                    TJobhunter one = tJobhunterService.getOne(queryWrapper);
                    if(one ==null){
                        return R.fail("用户不存在");
                    }
                    //查询该微信账号是否绑账户是否一致
                    if (openid.equals(one.getOpenid())) {
                        UpdateWrapper updateWrapper = new UpdateWrapper();
                        updateWrapper.eq("user_id", userid);
                        updateWrapper.set("openid", "");
                        updateWrapper.set("update_time", LocalDateTime.now());
                        tJobhunterService.update(updateWrapper);
                    }else {
                       return R.fail("微信号不一致 请检查!");
                    }
                    //查询该微信账号是否绑定其他用户
                }


                //招聘企业
                if (companyType == 0) {
                    TCompany id1 = tCompanyService.getId(userid);
                    if (id1 == null) {
                        return R.fail("用户不存在");
                    }
                    //查询该微信账号是否绑定其他用户 则更行
                    if (openid.equals(id1.getOpenid())) {
                        UpdateWrapper updateWrapper = new UpdateWrapper();
                        updateWrapper.eq("company_id", userid);
                        updateWrapper.set("openid", "");
                        updateWrapper.set("update_time", LocalDateTime.now());
                        tCompanyService.update(updateWrapper);
                    }else {
                        return R.fail("微信号不一致 请检查!");
                    }
                }


                //绑定人才推荐
                if (companyType == 1) {
                    TRecommend byId = tRecommendService.getById(userid);
                    if (byId == null) {
                        return R.fail("用户不存在");
                    }
                    //查询该微信账号是否绑定其他用户 则更新
                    if (openid.equals(byId.getWxOpenid())) {
                        UpdateWrapper updateWrapper = new UpdateWrapper();
                        updateWrapper.eq("id", userid);
                        updateWrapper.set("wx_openid", "");
                        updateWrapper.set("update_time", LocalDateTime.now());
                        updateWrapper.set("update_id", userid);
                        tRecommendService.update(updateWrapper);
                    }else {
                        return R.fail("微信号不一致 请检查!");
                    }

                }
                    return R.success("解绑成功！");
                }
            } else {
                return R.fail("您的扫码登录已过期,请重新扫码登录！");
            }
        return R.error("解绑失败！请联系管理员");
    }

}
