package com.wcwy.common.base.utils;

/**
 * ClassName: WechatPaySignUtil
 * Description:
 * date: 2023/8/19 10:34
 *
 * @author tangzhuo
 * @since JDK 1.8
 */

/**
 * @author Heb
 * @version 1.0
 * @description: TODO
 * @date 2023/2/21 14:33
 */
import java.security.MessageDigest;
import java.util.*;

public class WechatPaySignUtil {

    /**
     * 对参数进行签名
     * @param params 待签名的参数
     * @param key API密钥
     * @return 签名结果
     */
    public static String sign(Map<String, String> params, String key) {
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

        // 对字符串进行MD5加密
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
           // MessageDigest md = MessageDigest.getInstance("RSA");
            byte[] bytes = md.digest(sb.toString().getBytes("UTF-8"));
            String sign = bytesToHexString(bytes).toUpperCase();
            return sign;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将字节数组转换为十六进制字符串
     * @param bytes 字节数组
     * @return 十六进制字符串
     */
    private static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(b & 0xFF);
            if (hex.length() == 1) {
                sb.append("0");
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("appid", "");
        String key = "";//api2秘钥
        /**
         * 一次签名
         * ------------------------------
         */
//        params.put("nonce_str", "");
//        params.put("attach","");
//        params.put("body", "");
//        params.put("mch_id", "");
//        params.put("notify_url", "");
//        params.put("out_trade_no", "");
//        params.put("total_fee", "");
//        params.put("spbill_create_ip", "");
//        params.put("trade_type", "");

///**
// * 二次签名
// * ------------------------------
// */
        params.put("partnerid","");
        params.put("prepayid","");
        params.put("package","Sign=WXPay");
        params.put("noncestr", "FOBvoTUvG8sM5lMvYrYofUdhmYUSDGRw");
        params.put("timestamp","1677135129641");


        String sign = WechatPaySignUtil.sign(params, key);
        System.out.println("sign = " + sign);
        params.put("sign", sign);
    }
    /**
     *    生成随机字符串
     * @return
     */
    public static String getNonceStr() {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 32; i++) {
            sb.append(str.charAt((int) (Math.random() * str.length())));
        }
        return sb.toString();
    }



}
