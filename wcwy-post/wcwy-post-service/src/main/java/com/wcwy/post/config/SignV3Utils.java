package com.wcwy.post.config;

/**
 * ClassName: SignV3Utils
 * Description:
 * date: 2023/8/23 15:11
 *
 * @author tangzhuo
 * @since JDK 1.8
 */

import okhttp3.HttpUrl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.*;

@Component
public class SignV3Utils {

    //V3主商户ID
    private static String merchantId = "1626157987";
    //微信商户平台APIv3证书序列号
    private static String certificateSerialNo = "3052B25BB0739852F533F98DEB8582D21C266F73";
    //私钥（不要把私钥文件暴露在公共场合，如上传到Github，写在客户端代码等。）
    private static String privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC6LkwgxqJ5SwmB" +
            "ZLFFCb7DKrK2UK3OQjNQLygsW97lE4xZcRBu93VA8mT5oz7TCfcUL5alQ4ML7C9V" +
            "Whhyd1Fjto4SyXMJTVWTDH/UodbXEaRRpSBkN6/Kd9KsQ4JJx2XwtrBUhwx9FxJk" +
            "U5jFsXnh3cDe3OngkAHG98W0Bs2f8IX3YMxUT7K59+8XbQPYFVd5syNrGTrKlqBI" +
            "SmqDH7bxEGobMBdCAh7BF+C1x6sB3G//rHfPFo8DzcqhncV+jsaDmAxvxPHKMah7" +
            "dTP0kvWOXvPFQtDhfQw3xPwyp9uFqhOPHqkFljwLxOEk8l0hQYwgzBI+yx7rLThU" +
            "XqOkSeCNAgMBAAECggEAeqgVr2ItmAs/b+eOghUxgBKZOlNcrg4fECyBIvhDmFeQ" +
            "lzxNv014ChmnPG+37AEcrkfLz4TyyOAJLwn0ITVKgY67etUM3Iv+XKpXbRZSG86b" +
            "ms5W0K9/OLxIJwUyyLh9CGDTOlzedWrFyyLn+xY5XfxiHHEeA7vb7+E8dqYkwcDs" +
            "B6KmYuQuqub7wssD82yj7G0S4uOu+7Rw2fMylHZ4o+DkZNa828fvC4SP3APN2hBU" +
            "c2sZro3RwXcoXVl2mgWwGkZ7Adfug2Vx66OvDAVQsjGOJ/uhKiXMvfz3fcvbmndn" +
            "OmrLnbNuEvQGoCcHB44pVRIYueNcID/vyvKoWJKBAQKBgQDkxuS7v2x3xQ9XUvjE" +
            "1rQI55MwBM2ItuaNuMyCqgQ/kDyiLixP41uXpd9SsqLm7k8HpbRUaB4MQBQPrfMB" +
            "yUVl12HGbQAoijMIgAEp9L+GpFg71lJkvL1DN+2m/6LsWsF6CGQ4866AYTuMEcTZ" +
            "3Y/j1mgjCYFuG1DS6MgQv/Er6QKBgQDQVdKkow4Z5pj1oNatuZv8gQrxE1evbJsU" +
            "a4NVbA0EJc+Co3lPQKTZz3KkHO0FKwr5PUZHZQdnNbQTJuJ5ySLNJ2fnwg03Il9b" +
            "t/ggS4Ebk3I/rf3jBpZhzvlnusE86+YTX3DtVu1O0LVLR15r+5x/JXdxV797ACv9" +
            "YYSHiUG9BQKBgQDibXg12mAgqolkhFpzd4z4wzqKbDaA+YV2/1BqgptxzfA1FD4H" +
            "U59zmFhQIT3aEkNl7jtszx/uP/2bBy9ctThac7HyEi/179JSt15viC2HtWEe2CD0" +
            "U1l/DfvJLXqzM6AKiAOp2oT7y0CEgZGzj/a6KZsoEmBn+eEk3gAlk9O/AQKBgFS2" +
            "4T2TFBPSIdaXfVQNCnHFo0ZeICS0G+dUxIXCtxQ9r8CmapigZ6gt46b6ICMe2op4" +
            "sRAs87KzMrMq96Kf+CfF40lpLeiCcJYiG6I+MZSeAzIDtR5QumuxNtdIKHV2UwsD" +
            "ny7TTxHiaiXfMnTkTkGhYY1UKgeBDWIt+i8G0BVlAoGAVAmBdW4LrC3AeljFkPdl" +
            "zrSvQjEkxGNGa+8mQfhAFZGbOc7DH6/0h8V8yLYHeUK3XmGq2+ONao878ce+5G67" +
            "mWUxZTkZEu2+WuZmUhjir6WAIZVXHKUz3DTc/92uYfGMP3QThBoESn08zB0g+Rxb" +
            "GrH5/JAPSZwwJTDoNlO1kdM=";
    //配置文件配置好主商户号
  /*  @Value("${wechat.v3.merchantId}")
    public void setMerchantId(String merchantId) {
        SignV3Utiles.merchantId = merchantId;
    }
    //配置文件配置好序列号
    @Value("${wechat.v3.certificateSerialNo}")
    public void setCertificateSerialNo(String certificateSerialNo) {
        SignV3Utiles.certificateSerialNo = certificateSerialNo;
    }
    //配置文件配置好私钥
    @Value("${wechat.v3.privateKey}")
    public void setPrivateKey(String privateKey) {
        SignV3Utiles.privateKey = privateKey;
    }*/

    /**
     * 使用方法
     *
     * @param method 请求方法
     * @param url    请求url
     * @param body   请求内容
     * @return
     */
    public static HashMap<String, String> getSignMap(String method, String url, String body) throws InvalidKeySpecException, NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException, SignatureException {
        String authorization = getSign(method, url, body);

        HashMap<String, String> headsMap = new HashMap<>();
        headsMap.put("Authorization", authorization);
        headsMap.put("Content-Type", "application/json");
        headsMap.put("Accept", "application/json");

        return headsMap;
    }

    public static String getSign(String method, String url, String body) throws NoSuchAlgorithmException, SignatureException, InvalidKeySpecException, InvalidKeyException, UnsupportedEncodingException {
        return "WECHATPAY2-SHA256-RSA2048 " + getToken(method, HttpUrl.parse(url), body);
    }

    public static String getToken(String method, HttpUrl url, String body) throws UnsupportedEncodingException, SignatureException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException {
        String nonceStr = nonceString();
        long timestamp = System.currentTimeMillis() / 1000;
        String message = buildMessage(method, url, timestamp, nonceStr, body);
        String signature = sign(message.getBytes("utf-8"));
        return "mchid=\"" + merchantId + "\","
                + "nonce_str=\"" + nonceStr + "\","
                + "timestamp=\"" + timestamp + "\","
                + "serial_no=\"" + certificateSerialNo + "\","
                + "signature=\"" + signature + "\"";
    }

    public static String sign(byte[] message) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {
        Signature sign = Signature.getInstance("SHA256withRSA");
        sign.initSign(getPKCS8PrivateKey(privateKey));
        sign.update(message);

        return Base64.getEncoder().encodeToString(sign.sign());
    }

    public static String sign1(Map<String, String> params, String key) throws Exception {
        // 按照参数名ASCII码从小到大排序
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        // 拼接参数名和参数值
        StringBuilder sb = new StringBuilder();
        for (String k : keys) {
            String v = params.get(k);
            if (v != null && !"".equals(v.trim())) {
                sb.append(k).append("=").append(v.trim()).append("&");
            }
        }
        sb.append("key=").append(key);
        Signature sign = Signature.getInstance("SHA256withRSA");
        sign.initSign(getPKCS8PrivateKey(key));
        String s = sb.toString();
        sign.update(s.getBytes("utf-8"));

        return Base64.getEncoder().encodeToString(sign.sign());
    }

    public static String buildMessage(String method, HttpUrl url, long timestamp, String nonceStr, String body) {
        String canonicalUrl = url.encodedPath();
        if (url.encodedQuery() != null) {
            canonicalUrl += "?" + url.encodedQuery();
        }

        return method + "\n"
                + canonicalUrl + "\n"
                + timestamp + "\n"
                + nonceStr + "\n"
                + body + "\n";
    }


    private static PrivateKey getPKCS8PrivateKey(String strPk) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String realPK = strPk.replaceAll("-----END PRIVATE KEY-----", "")
                .replaceAll("-----BEGIN PRIVATE KEY-----", "")
                .replaceAll("\n", "");

        byte[] b1 = Base64.getDecoder().decode(realPK);

        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(b1);

        KeyFactory kf = KeyFactory.getInstance("RSA");

        return kf.generatePrivate(spec);
    }

    public static String nonceString() {

        String currTime = String.format("%d", (long) System.currentTimeMillis() / 1000);

        String strTime = currTime.substring(8, currTime.length());

        Random random = new Random();
        int num = (int) (random.nextDouble() * (1000000 - 100000) + 100000);
        String code = String.format("%06d", num);

        String nonce_str = currTime.substring(2) + code;
        return nonce_str;

    }


}

