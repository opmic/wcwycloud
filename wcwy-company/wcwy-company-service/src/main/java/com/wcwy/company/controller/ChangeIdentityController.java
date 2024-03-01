package com.wcwy.company.controller;

import com.alibaba.fastjson.JSON;
import com.wcwy.common.base.result.R;
import com.wcwy.common.web.annotation.AutoIdempotent;
import com.wcwy.company.config.ChangeIdentity;
import com.wcwy.company.entity.TCompany;
import com.wcwy.company.entity.TJobhunter;
import com.wcwy.company.entity.TRecommend;
import com.wcwy.company.service.TCompanyService;
import com.wcwy.company.service.TJobhunterService;
import com.wcwy.company.service.TRecommendService;
import com.wcwy.company.session.CompanyMetadata;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.httpclient.methods.PostMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.HttpClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * ClassName: ChangeIdentity
 * Description:
 * date: 2023/1/16 13:40
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@RestController
@RequestMapping("/changeIdentity")
@Api(tags = "身份切换接口")
public class ChangeIdentityController {
    @Autowired
    private TCompanyService tCompanyService;
    @Autowired
    private TJobhunterService tJobhunterService;
    @Autowired
    private TRecommendService tRecommendService;
    @Autowired
    private ChangeIdentity changeIdentity;
    @Autowired
    private CompanyMetadata companyMetadata;
    @GetMapping("/updateToken")
    @ApiOperation("更换身份token")
    @ApiImplicitParam(name = "identity", required = true, value = "需要切换的身份(1:企业 2:推荐官 3企业)")
    @AutoIdempotent
    public R updateToken(@RequestParam("identity") Integer identity) throws Exception {
        if(identity==null || identity<1 || identity>3 ){
            return R.fail("切换身份不正确!");
        }
        String userid = companyMetadata.userid();
        String substring = userid.substring(0, 2);
        String loginName="";
        if(substring.equals("TC")){
            TCompany byId = tCompanyService.getById(userid);
            loginName=byId.getLoginName();
        }else if(substring.equals("TJ")){
            TJobhunter byId = tJobhunterService.getById(userid);
            loginName=byId.getLoginName();
        }else if(substring.equals("TR")){
            TRecommend byId = tRecommendService.getById(userid);
            loginName=byId.getLoginName();
        }else {
            return R.fail("恶意token");
        }
        try {
            String password="";
            if(identity==1){
                password="tangzhuowcwy666";
            }else  if(identity==2){
                password="wcwytangzhuo666";
            }else if(identity==3){
                password="wcwytangzhuo123";
            }
            String uri = changeIdentity.path;
            PostMethod postMethod = new PostMethod(uri);
            postMethod.setRequestHeader("content-type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);
            NameValuePair[] nameValuePairs = {
                    new NameValuePair("client_id", "c1"),
                    new NameValuePair("client_secret", "wcwy"),
                    new NameValuePair("grant_type", "rm_password"),
                    new NameValuePair("username","wcwy"+ loginName),
                    new NameValuePair("password", password)
            };
            postMethod.setRequestBody(nameValuePairs);
            HttpClient httpClient = new HttpClient();
            int responseCode = httpClient.executeMethod(postMethod);
            System.out.println("响应码：" + responseCode);
            String responseBodyAsString = postMethod.getResponseBodyAsString();
            Map<String,String> map= JSON.parseObject(responseBodyAsString,Map.class);
            return R.success("access_token",map.get("access_token"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.fail();
    }

}
