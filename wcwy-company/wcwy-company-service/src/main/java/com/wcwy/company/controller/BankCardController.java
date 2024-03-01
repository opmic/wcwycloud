package com.wcwy.company.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wcwy.common.base.enums.SmsEunm;
import com.wcwy.common.base.result.R;
import com.wcwy.common.base.utils.*;
import com.wcwy.common.redis.enums.QRCode;
import com.wcwy.common.redis.util.IDGenerator;
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.common.web.annotation.AutoIdempotent;
import com.wcwy.company.entity.BankCard;
import com.wcwy.company.entity.TRecommend;
import com.wcwy.company.service.BankCardService;
import com.wcwy.company.service.TRecommendService;
import com.wcwy.company.session.CompanyMetadata;
import com.wcwy.company.vo.BankCardVO;
import com.wcwy.company.vo.IdentityCardVO;
import com.wcwy.system.annotation.Log;
import com.wcwy.system.enums.BusinessType;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * ClassName: BankCardController
 * Description:
 * date: 2023/8/17 11:03
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@RestController
@RequestMapping("/bankCard")
@Api(tags = "银行卡接口")
public class BankCardController {

    @Autowired
    private BankCardService bankCardService;

    @Autowired
    private CompanyMetadata companyMetadata;

    @Autowired
    private TRecommendService tRecommendService;
    @Autowired
    private IDGenerator idGenerator;
    @Autowired
    private RedisUtils redisUtils;

    @PostMapping("/identityCard")
    @ApiOperation("身份证信息+银行卡填写")
    @Log(title = "身份证信息+银行卡填写", businessType = BusinessType.UPDATE)
    @AutoIdempotent
    public R identityCard(@Valid @RequestBody IdentityCardVO identityCardVO) {
        if (! PhoneUtil.isMobileNumber(identityCardVO.getUserTel())) {
            return R.fail("预留手机号码不正确!");
        }
        if(! CardUtil.isCard(identityCardVO.getCard())){
            return R.fail("请检查身份证是否正确!");
        }
        if(!BankCardUtils.checkBankCard(identityCardVO.getBankNum())){
            return R.fail("请检查银行卡号！");
        }

        Claims claimsFromJwt = JWT.getClaimsFromJwt(identityCardVO.getKeyRate());
        if (claimsFromJwt == null) {
            return R.fail("操作时间过长，请重新操作!");
        }
 /*       Object phone = claimsFromJwt.get("phone");
        if (StringUtils.isEmpty(phone) || !byId.getLoginName().equals(phone)) {
            return R.fail("电话号码不匹配");
        }*/
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id", companyMetadata.userid());
        int count = bankCardService.count(queryWrapper);
        if (count > 0) {
            return R.fail("已存在,请不要重复添加!");
        }
        BankCard bankCard = new BankCard();
        BeanUtils.copyProperties(identityCardVO, bankCard);
        bankCard.setId(idGenerator.generateCode("BC"));
        bankCard.setCreateTime(LocalDateTime.now());
        bankCard.setUserId(companyMetadata.userid());
        boolean save = bankCardService.save(bankCard);
        if (save) {
            return R.success("已提交审核!");
        }
        return R.fail("提交失败!");
    }


    @GetMapping("/select")
    @ApiOperation("获取提现银行卡信息")
    @Log(title = "获取提现银行卡信息", businessType = BusinessType.UPDATE)
    public R<BankCard> select() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id", companyMetadata.userid());
        BankCard one = bankCardService.getOne(queryWrapper);
        return R.success(one);
    }


    @PostMapping("/update")
    @ApiOperation("换绑银行卡")
    @Log(title = "换绑银行卡", businessType = BusinessType.UPDATE)
    @AutoIdempotent
    public R update(@Valid @RequestBody BankCardVO bankCardVO){
        if (! PhoneUtil.isMobileNumber(bankCardVO.getUserTel())) {
            return R.fail("预留手机号码不正确!");
        }
        if(!BankCardUtils.checkBankCard(bankCardVO.getBankNum())){
            return R.fail("请检查银行卡号！");
        }
        TRecommend byId = tRecommendService.getById(companyMetadata.userid());
        Claims claimsFromJwt = JWT.getClaimsFromJwt(bankCardVO.getKeyRate());
        if (claimsFromJwt == null) {
            return R.fail("操作时间过长，请重新操作!");
        }
        Object phone = claimsFromJwt.get("phone");
        if (StringUtils.isEmpty(phone) || !byId.getLoginName().equals(phone)) {
            return R.fail("电话号码不匹配");
        }
        Date date = new Date(System.currentTimeMillis() + 71000);
        System.out.println(DateUtils.formatDate(date));

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id", companyMetadata.userid());
        BankCard one = bankCardService.getOne(queryWrapper);
        if( StringUtils.isEmpty(one)){
            return R.fail("请先实名注册！");
        }
       BeanUtils.copyProperties(bankCardVO,one);
        one.setAudit(0);
        boolean b = bankCardService.updateById(one);
        if(b){
            return R.success();
        }
        return R.fail();
    }
}
