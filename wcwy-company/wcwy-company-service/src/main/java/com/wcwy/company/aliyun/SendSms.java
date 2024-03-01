package com.wcwy.company.aliyun;

/**
 * @author tangzhuo
 * @ClassName: SendSms
 * @Description:
 * @date 2022-08-04
 */

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.google.gson.Gson;

import com.wcwy.common.base.enums.AccessTemplateCode;
import com.wcwy.company.entity.TCompany;
import com.wcwy.company.entity.TRecommend;
import com.wcwy.company.service.PutInResumeRecordService;
import com.wcwy.company.service.TCompanyService;
import com.wcwy.company.service.TRecommendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

@Configuration
@Slf4j
@Component
public class SendSms {
    @Value("${access.ketId}")
    private String accessKetId;
    @Value("${access.secret}")
    private String accessKeySecret;
    @Value("${access.regionId}")
    private String regionId;
    @Value("${access.open}")
    private Boolean open;

    @Autowired
    private TCompanyService tCompanyService;
    @Autowired
    private PutInResumeRecordService putInResumeRecordService;
    @Autowired
    private TRecommendService tRecommendService;
    public static void main(String[] args) {
        /*DefaultProfile profile = DefaultProfile.getProfile("cn-beijing", "LTAI5tSjbbPx6yuaj5YsmFn8", "8l4M2PtpCeDsYCU4NIYWywYRFVdNOX");
        IAcsClient client = new DefaultAcsClient(profile);
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers("15707393479");//接收短信的手机号码
        request.setSignName("网才无忧");//短信签名名称
        request.setTemplateCode("SMS_253495189");//短信模板CODE
        request.setTemplateParam("{code:1234}");//短信模板变量对应的实际值

        try {
            SendSmsResponse response = client.getAcsResponse(request);
            System.out.println(new Gson().toJson(response));
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            log.error("ErrCode:" + e.getErrCode());
            log.error("ErrCode:" + e.getErrCode());
            log.error("ErrMsg:" + e.getErrMsg());
            log.error("RequestId:" + e.getRequestId());
        }*/
        DefaultProfile profile = DefaultProfile.getProfile("cn-beijing","LTAI5tSjbbPx6yuaj5YsmFn8" , "8l4M2PtpCeDsYCU4NIYWywYRFVdNOX");
        IAcsClient client = new DefaultAcsClient(profile);
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers("15707393479");//接收短信的手机号码
        request.setSignName("网才无忧");//短信签名名称
        request.setTemplateCode("SMS_276361021");//短信模板CODE
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("{\"post\":\"666\"}");

        stringBuffer.append("{\"time\":\"2023-06-21 12:21\"}");
        request.setTemplateParam(stringBuffer.toString());//短信模板变量对应的实际值

        try {
            SendSmsResponse response = client.getAcsResponse(request);
            System.out.println(new Gson().toJson(response));
           /* return new Gson().toJson(response);*/
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            log.error("ErrCode:" + e.getErrCode());
            log.error("ErrCode:" + e.getErrCode());
            log.error("ErrMsg:" + e.getErrMsg());
            log.error("RequestId:" + e.getRequestId());
        }
       /* return null;*/
    }

    /**
     * @Param: phoneNumbers:电话号码
     * @Param: templateParam:验证码
     * @Param: signName:短信签名
     * @return:String
     * @Author: tangzhuo
     * @date: 2022-08-04
     * @Description:
     */
    public String SendSmsUtils(String phoneNumbers, String templateParam, String signName, String code) {
        if (code == null) {
            code = AccessTemplateCode.REGISTER.getDesc();//使用默认CODE
        }
        if (signName == null) {
            signName = AccessTemplateCode.REGISTER.getName();
        }
        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKetId, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);

        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(phoneNumbers);//接收短信的手机号码
        request.setSignName(signName);//短信签名名称
        request.setTemplateCode(code);//短信模板CODE
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("{code:" + templateParam + "}");
        request.setTemplateParam(stringBuffer.toString());//短信模板变量对应的实际值

        try {
            SendSmsResponse response = client.getAcsResponse(request);
            System.out.println(new Gson().toJson(response));
            return new Gson().toJson(response);
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            log.error("ErrCode:" + e.getErrCode());
            log.error("ErrCode:" + e.getErrCode());
            log.error("ErrMsg:" + e.getErrMsg());
            log.error("RequestId:" + e.getRequestId());
        }
        return null;
    }

    /**
     * @Param: phoneNumbers:电话号码
     * @Param: signName:短信签名
     * @return:String
     * @Author: tangzhuo
     * @date: 2022-08-04
     * @Description:
     */

    public String inform(String phoneNumbers, String signName, String code) {
        if (code == null) {
            code = AccessTemplateCode.REGISTER.getDesc();//使用默认CODE
        }
        if (signName == null) {
            signName = AccessTemplateCode.REGISTER.getName();
        }
        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKetId, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);

        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(phoneNumbers);//接收短信的手机号码
        request.setSignName(signName);//短信签名名称
        request.setTemplateCode(code);//短信模板CODE
        StringBuffer stringBuffer = new StringBuffer();
        request.setTemplateParam(stringBuffer.toString());//短信模板变量对应的实际值

        try {
            SendSmsResponse response = client.getAcsResponse(request);
            System.out.println(new Gson().toJson(response));
            return new Gson().toJson(response);
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            log.error("ErrCode:" + e.getErrCode());
            log.error("ErrCode:" + e.getErrCode());
            log.error("ErrMsg:" + e.getErrMsg());
            log.error("RequestId:" + e.getRequestId());
        }
        return null;
    }

    /**
     * putPost：投简id
     * 接受面试
     *
     * @return
     */
    @Async
    public String acceptInterview(String putPost) {
        if (!open) {
            return "";
        }
       Map<String ,String> map= putInResumeRecordService.phoneAndPost(putPost);
        if(map==null){
            return "";
        }
        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKetId, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(map.get("phone"));//接收短信的手机号码
        request.setSignName(AccessTemplateCode.ACCEPT_INTERVIEW.getName());//短信签名名称
        request.setTemplateCode(AccessTemplateCode.ACCEPT_INTERVIEW.getDesc());//短信模板CODE
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("{\"post\":\""+"《"+map.get("postLabel")+"》 "+"\"}");

     //   stringBuffer.append("{\"post\":\""+map.get("postLabel")+"\"}");
        request.setTemplateParam(stringBuffer.toString());//短信模板变量对应的实际值

        try {
            SendSmsResponse response = client.getAcsResponse(request);
            System.out.println(new Gson().toJson(response));
            return new Gson().toJson(response);
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            log.error("ErrCode:" + e.getErrCode());
            log.error("ErrCode:" + e.getErrCode());
            log.error("ErrMsg:" + e.getErrMsg());
            log.error("RequestId:" + e.getRequestId());
        }
        return null;
    }

    /**
     * putPost 投简id
     * 面试淘汰
     *
     * @return
     */
    @Async
    public String weedOut(String putPost) {

        if (!open) {
            return "";
        }
        Map<String ,String> map= putInResumeRecordService.RmPhoneAndPost(putPost);
        if(map==null){
            return "";
        }
        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKetId, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(map.get("phone"));//接收短信的手机号码
        request.setSignName(AccessTemplateCode.WEED_OUT.getName());//短信签名名称
        request.setTemplateCode(AccessTemplateCode.WEED_OUT.getDesc());//短信模板CODE
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("{\"post\":\""+"《"+map.get("postLabel")+"》 "+"\"}");
        // stringBuffer.append("{\"post\":\""+map.get("postLabel")+"\"}");
        request.setTemplateParam(stringBuffer.toString());//短信模板变量对应的实际值

        try {
            SendSmsResponse response = client.getAcsResponse(request);
            System.out.println(new Gson().toJson(response));
            return new Gson().toJson(response);
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            log.error("ErrCode:" + e.getErrCode());
            log.error("ErrCode:" + e.getErrCode());
            log.error("ErrMsg:" + e.getErrMsg());
            log.error("RequestId:" + e.getRequestId());
        }
        return null;
    }


    /**
     * putPost 投简id
     * 发送offer
     *
     * @return
     */
    @Async
    public String acceptOffer(String putPost) {
        if (!open) {
            return "";
        }
        Map<String ,String> map= putInResumeRecordService.RmPhoneAndCompany(putPost);
        if(map==null){
            return "";
        }
        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKetId, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(map.get("phone"));//接收短信的手机号码
        request.setSignName(AccessTemplateCode.ACCEPT_OFFER.getName());//短信签名名称
        request.setTemplateCode(AccessTemplateCode.ACCEPT_OFFER.getDesc());//短信模板CODE
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("{\"company\":\""+"《"+map.get("company")+"》 "+"\"}");
        request.setTemplateParam(stringBuffer.toString());//短信模板变量对应的实际值

        try {
            SendSmsResponse response = client.getAcsResponse(request);
            System.out.println(new Gson().toJson(response));
            return new Gson().toJson(response);
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            log.error("ErrCode:" + e.getErrCode());
            log.error("ErrCode:" + e.getErrCode());
            log.error("ErrMsg:" + e.getErrMsg());
            log.error("RequestId:" + e.getRequestId());
        }
        return null;
    }


    /**
     * putPost 投简id  time:修改面试时间
     * 修改面试通知推荐官
     *
     * @return
     */
    @Async
    public String updateInterview(String putPost,String time) {
        if (!open) {
            return "";
        }
        time=time.replace("T"," ");
       Map<String ,String> map=putInResumeRecordService.RmPhoneAndPost(putPost);
        if(map==null){
            return "";
        }
        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKetId, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(map.get("phone"));//接收短信的手机号码
        request.setSignName(AccessTemplateCode.UPDATE_INTERVIEW.getName());//短信签名名称
        request.setTemplateCode(AccessTemplateCode.UPDATE_INTERVIEW.getDesc());//短信模板CODE
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(" {\"post\":\""+"《"+map.get("postLabel")+"》"+"\",\"time\":\""+time+"\"}");
        request.setTemplateParam(stringBuffer.toString());//短信模板变量对应的实际值

        try {
            SendSmsResponse response = client.getAcsResponse(request);
            System.out.println(new Gson().toJson(response));
            return new Gson().toJson(response);
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            log.error("ErrCode:" + e.getErrCode());
            log.error("ErrCode:" + e.getErrCode());
            log.error("ErrMsg:" + e.getErrMsg());
            log.error("RequestId:" + e.getRequestId());
        }
        return null;
    }

    /**
     * phoneNumbers 电话号码
     * 取消面试
     *
     * @return
     */
    public String cancelInterview(String phoneNumbers) {
        if (!open) {
            return "";
        }
        Map<String ,String> map=putInResumeRecordService.RmPhoneAndPost(phoneNumbers);
        if(map==null){
            return "";
        }

        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKetId, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(phoneNumbers);//接收短信的手机号码
        request.setSignName(AccessTemplateCode.CANCEL_INTERVIEW.getName());//短信签名名称
        request.setTemplateCode(AccessTemplateCode.CANCEL_INTERVIEW.getDesc());//短信模板CODE
        StringBuffer stringBuffer = new StringBuffer();
        /* stringBuffer.append("{post:" + post + "}");*/
        request.setTemplateParam(stringBuffer.toString());//短信模板变量对应的实际值

        try {
            SendSmsResponse response = client.getAcsResponse(request);
            System.out.println(new Gson().toJson(response));
            return new Gson().toJson(response);
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            log.error("ErrCode:" + e.getErrCode());
            log.error("ErrCode:" + e.getErrCode());
            log.error("ErrMsg:" + e.getErrMsg());
            log.error("RequestId:" + e.getRequestId());
        }
        return null;
    }

    /**
     * company 企业id post：岗位
     * 接收到简历
     *
     * @return
     */
    @Async
    public String acceptResume(String company, String post) {
        if (!open) {
            return "";
        }
        String phoneNumbers = tCompanyService.phoneNumbers(company);

        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKetId, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(phoneNumbers);//接收短信的手机号码
        request.setSignName(AccessTemplateCode.ACCEPT_RESUME.getName());//短信签名名称
        request.setTemplateCode(AccessTemplateCode.ACCEPT_RESUME.getDesc());//短信模板CODE
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("{\"post\":\""+"《"+post+"》 "+"\"}");
        request.setTemplateParam(stringBuffer.toString());//短信模板变量对应的实际值

        try {
            SendSmsResponse response = client.getAcsResponse(request);
            System.out.println(new Gson().toJson(response));
            return new Gson().toJson(response);
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            log.error("ErrCode:" + e.getErrCode());
            log.error("ErrCode:" + e.getErrCode());
            log.error("ErrMsg:" + e.getErrMsg());
            log.error("RequestId:" + e.getRequestId());
        }
        return null;
    }

    public Boolean isRecommend(String userId){
        return "TR".equals(userId.substring(0,2));
    }
    /**
     * 面试邀请通知
     * dateTime:面试时间
     * userId:投简人id
     */
    @Async
    public String interviewNotice(String dateTime,String userId){
        if (!open || !this.isRecommend(userId)) {
            return "";
        }
        dateTime= dateTime.replace("T"," ");
        String phoneNumbers = tRecommendService.phoneNumbers(userId);
        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKetId, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(phoneNumbers);//接收短信的手机号码
        request.setSignName(AccessTemplateCode.INTERVIEW_NOTICE.getName());//短信签名名称
        request.setTemplateCode(AccessTemplateCode.INTERVIEW_NOTICE.getDesc());//短信模板CODE
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("{\"time\":\""+dateTime+"\"}");
        request.setTemplateParam(stringBuffer.toString());//短信模板变量对应的实际值

        try {
            SendSmsResponse response = client.getAcsResponse(request);
            System.out.println(new Gson().toJson(response));
            return new Gson().toJson(response);
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            log.error("ErrCode:" + e.getErrCode());
            log.error("ErrCode:" + e.getErrCode());
            log.error("ErrMsg:" + e.getErrMsg());
            log.error("RequestId:" + e.getRequestId());
        }
        return null;
    }

    /**
     * putPost:投简id
     * 拒绝面试邀请
     * @return
     */
    @Async
    public String refuseInterview(String putPost){
        if (!open) {
            return "";
        }
        Map<String ,String> map= putInResumeRecordService.phoneAndPost(putPost);
        if(map==null){
            return "";
        }
        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKetId, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(map.get("phone"));//接收短信的手机号码
        request.setSignName(AccessTemplateCode.REFUSE_INTERVIEW.getName());//短信签名名称
        request.setTemplateCode(AccessTemplateCode.REFUSE_INTERVIEW.getDesc());//短信模板CODE
        StringBuffer stringBuffer = new StringBuffer();
       /* stringBuffer.append("{post:" + map.get("postLabel") + "}");*/
        request.setTemplateParam(stringBuffer.toString());//短信模板变量对应的实际值

        try {
            SendSmsResponse response = client.getAcsResponse(request);
            System.out.println(new Gson().toJson(response));
            return new Gson().toJson(response);
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            log.error("ErrCode:" + e.getErrCode());
            log.error("ErrCode:" + e.getErrCode());
            log.error("ErrMsg:" + e.getErrMsg());
            log.error("RequestId:" + e.getRequestId());
        }
        return null;
    }

    @Async
    public String orderNotice(String putPost){
        if (!open) {
            return "";
        }
        Map<String ,String> map= putInResumeRecordService.phoneAndPost(putPost);
        if(map==null){
            return "";
        }
        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKetId, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(map.get("phone"));//接收短信的手机号码
        request.setSignName(AccessTemplateCode.ORDER_NOTICE.getName());//短信签名名称
        request.setTemplateCode(AccessTemplateCode.ORDER_NOTICE.getDesc());//短信模板CODE
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("{\"post\":\""+map.get("postLabel")+"\"}");
        request.setTemplateParam(stringBuffer.toString());//短信模板变量对应的实际值

        try {
            SendSmsResponse response = client.getAcsResponse(request);
            System.out.println(new Gson().toJson(response));
            return new Gson().toJson(response);
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            log.error("ErrCode:" + e.getErrCode());
            log.error("ErrCode:" + e.getErrCode());
            log.error("ErrMsg:" + e.getErrMsg());
            log.error("RequestId:" + e.getRequestId());
        }
        return null;
    }

    /**
     *注册通知邀请人
     * @param recommendId recommendId 推荐官id
     * @param signName 签名
     * @param templateCode 模版CODE
     * @param name 名称
     */

    @Async
    public void registerInform(String recommendId,String signName, String templateCode,String name){
        if (!open) {
            return;
        }
        String substring = recommendId.substring(0, 2);
        String phoneNumber="";
        if(substring.equals("TR")){
            TRecommend byId = tRecommendService.getById(recommendId);
            if(byId==null){
                return;
            }
            phoneNumber=byId.getLoginName();
        }else if(substring.equals("TC")){
            TCompany id = tCompanyService.getId(recommendId);
            if(id==null){
                return;
            }
            phoneNumber=id.getLoginName();
        }

        if(StringUtils.isEmpty(phoneNumber)){
            return;
        }

        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKetId, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(phoneNumber);//接收短信的手机号码
        request.setSignName(signName);//短信签名名称
        request.setTemplateCode(templateCode);//短信模板CODE
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("{\"name\":\""+name+"\"}");
        request.setTemplateParam(stringBuffer.toString());//短信模板变量对应的实际值

        try {
            SendSmsResponse response = client.getAcsResponse(request);
            System.out.println(new Gson().toJson(response));
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            log.error("ErrCode:" + e.getErrCode());
            log.error("ErrCode:" + e.getErrCode());
            log.error("ErrMsg:" + e.getErrMsg());
            log.error("RequestId:" + e.getRequestId());
        }
    }
}