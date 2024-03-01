package com.wcwy.post.config;

/**
 * ClassName: CreateSign02
 * Description:
 * date: 2023/8/24 10:25
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.security.*;
import java.util.Base64;
@Component
public class CreateSign02 {
    @Resource
  private   WxPayConfig wxPayConfig;
  public   String getToken(String appid,String prepay_id) throws IOException, SignatureException, NoSuchAlgorithmException, InvalidKeyException {
        //随机字符串
        String nonceStr = getNonceStr();//真！随机字符串
        //时间戳
        long timestamp = System.currentTimeMillis() / 1000;
        //从下往上依次生成
        String message = buildMessage(appid, timestamp, nonceStr, prepay_id);
        //签名
        String signature = sign(message.getBytes("utf-8"));
        return  signature ;
    }
    public   String getToken1(String appid,String prepay_id,String nonce_str,long timestamp) throws IOException, SignatureException, NoSuchAlgorithmException, InvalidKeyException {
        //随机字符串
        String nonceStr = nonce_str ;//真！随机字符串
        //时间戳
        //long timestamp = System.currentTimeMillis() / 1000;
        //从下往上依次生成
        String message = buildMessage(appid, timestamp, nonceStr, prepay_id);
        //签名
        String signature = sign(message.getBytes("utf-8"));
        return  signature ;
    }
    public   String sign(byte[] message) throws NoSuchAlgorithmException, SignatureException, IOException, InvalidKeyException {
        //签名方式
        Signature sign = Signature.getInstance("SHA256withRSA");
        //私钥，通过MyPrivateKey来获取，这是个静态类可以接调用方法 ，需要的是_key.pem文件的绝对路径配上文件名
        PrivateKey privateKey = wxPayConfig.getPrivateKey(wxPayConfig.getPrivateKeyPath());
        sign.initSign(privateKey);
        sign.update(message);

        return Base64.getEncoder().encodeToString(sign.sign());
    }

    /**
     *  按照前端签名文档规范进行排序，\n是换行
     * @param appid
     * @param timestamp
     * @param nonceStr
     * @param prepay_id
     * @return
     */
    String buildMessage(String appid, long timestamp,String nonceStr,String prepay_id) {

        return appid + "\n"
                + timestamp + "\n"
                + nonceStr + "\n"
                + prepay_id + "\n";
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
