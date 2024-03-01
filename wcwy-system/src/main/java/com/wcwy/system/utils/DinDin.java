package com.wcwy.system.utils;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * ClassName: DinDin
 * Description:
 * date: 2023/12/11 15:17
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
public class DinDin {
    private static final String SECRET = "SEC2447f87019635ee219751a1a521a49539324c505d190dd2930e1bc8e630a5804";
    private static final String URL = "https://oapi.dingtalk.com/robot/send?access_token=408c053f9ad7ffb2a8d7b7c7259162936491cdc923f03f659095e1b66f4c1871";
    /**
     * 组装签名url
     * @return url
     */
    public static String getURL()throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        Long timestamp = System.currentTimeMillis();
        String stringToSign = timestamp + "\n" + SECRET;
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        byte[] signData = mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
        String sign = URLEncoder.encode(new String(Base64.encodeBase64(signData)), "UTF-8");
        String signResult = "&timestamp=" + timestamp + "&sign=" + sign;
        // 得到拼接后的 URL
        return URL + signResult;
    }
    /**
     * 获取客户端
     * @return
     */
    public static DingTalkClient getClient()throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException{
        return new DefaultDingTalkClient(getURL());
    }

}
