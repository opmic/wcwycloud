package com.wcwy.post.aliyun;

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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Configuration
@Slf4j
@Component
public class SendSms {
    @Value("${access.ketId}")
    private   String accessKetId;
    @Value("${access.secret}")
    private  String accessKeySecret;
    @Value("${access.regionId}")
    private  String regionId;
    @Value("${access.open}")
    private  Boolean open;
    public static void main(String[] args) {
        DefaultProfile profile = DefaultProfile.getProfile("cn-beijing", "LTAI5tSjbbPx6yuaj5YsmFn8", "8l4M2PtpCeDsYCU4NIYWywYRFVdNOX");
        IAcsClient client = new DefaultAcsClient(profile);

        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers("15707393479");//接收短信的手机号码
        request.setSignName("网才无忧");//短信签名名称
        request.setTemplateCode("SMS_257827635");//短信模板CODE
        request.setTemplateParam("{order:OD2211101857262-557}");//短信模板变量对应的实际值

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
    /**
     *@Param: phoneNumbers:电话号码
     *@Param: templateParam:订单id
     *@Param: signName:短信签名
     *@return:String
     *@Author: tangzhuo
     *@date: 2022-08-04
     *@Description:
     */
    @Async
    public  String  SendSmsUtils(String phoneNumbers,String number){
        if(!open){
            return "";
        }
        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKetId, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);

        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(phoneNumbers);//接收短信的手机号码
        request.setSignName(AccessTemplateCode.ORDER_FORM_INFORM.getName());//短信签名名称
        request.setTemplateCode(AccessTemplateCode.ORDER_FORM_INFORM.getDesc());//短信模板CODE
        StringBuffer stringBuffer=new StringBuffer();
        stringBuffer.append("{\"number\":\""+number+"\"}");
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

}