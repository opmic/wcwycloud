package com.wcwy.system.utils;

import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.taobao.api.ApiException;
import lombok.SneakyThrows;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * ClassName: SendDingDing
 * Description:
 * date: 2023/12/11 15:40
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
public class SendDingDing {
    public static void sendText(String content) throws ApiException, NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        DingTalkClient client = DinDin.getClient();
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype("text");
        OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
        text.setContent(content);
        request.setText(text);
        OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
        // isAtAll类型如果不为Boolean，请升级至最新SDK
        at.setIsAtAll(true);
      //  at.setAtMobiles(Arrays.asList("1392xxxxx","155xxxx"));
        request.setAt(at);
        OapiRobotSendResponse response = client.execute(request);
        /* log.info("success:{}, code:{}, errorCode:{}, errorMsg:{}",response.isSuccess(),response.getCode(),response.getErrcode(),response.getErrmsg());*/
    }
    @SneakyThrows
    public static void sendLink(){
        DingTalkClient client = DinDin.getClient();
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype("link");
        OapiRobotSendRequest.Link link = new OapiRobotSendRequest.Link();
        link.setMessageUrl("https://wangcaiwuyou.com/");
        link.setPicUrl("https://dev-1313175594.cos.ap-guangzhou.myqcloud.com/images/42f2439e5cca484aa0eb0c09dad1d65f.png");
        link.setTitle("网才无忧:文总");
        link.setText("文总，你是我们的主心骨，领头羊，公司可以没有我们，但不能没有文总。感谢你默默的付出，你是我们公司生命中的一道光，你的无私和奉献让我们感到无比的敬佩。愿你的付出得到上天的眷顾，愿你的善良得到世界的认可。愿你的每一天都充满阳光，愿你的生活如诗如画。感谢你，我的朋友，愿你幸福安康，一切如意。");
        request.setLink(link);
        OapiRobotSendResponse response = client.execute(request);

    }
}
