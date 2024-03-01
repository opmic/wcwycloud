package com.wcwy.company.controller;


import com.wcwy.common.base.result.Captcha;
import com.wcwy.common.base.result.R;
import com.wcwy.company.service.impl.CaptchaService;
import com.wcwy.company.vo.LoginVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * ClassName: CaptchaController
 * Description:
 * date: 2023/5/12 11:09
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@RestController
@RequestMapping("/captcha")
@Api(tags = "生成验证码")
public class CaptchaController {

    @Autowired
    private CaptchaService captchaService;

    @ApiOperation(value = "生成验证码拼图")
    @PostMapping("/get-captcha")
    public R getCaptcha(@RequestBody Captcha captcha) {
        return R.success(captchaService.getCaptcha(captcha));
    }

    @PostMapping("/cc")
    //@CrossOrigin
    public Object login(@RequestBody LoginVo loginVo) {
        System.out.println(loginVo.toString());
        String msg = captchaService.checkImageCode(loginVo.getNonceStr(),loginVo.getValue());
        if (StringUtils.isNotBlank(msg)) {
            return msg;
        }
        return "成功";
    }

}

