package com.wcwy.post.util;

/**
 * @ClassName: HttpUtil
 * @Description: TODO
 * @Author: tangzhuo
 * @Date: 2022-08-16 19:40
 */

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


/**
 * 微信支付专用类 请求操作方法
 *
 * @author Administrator
 */
@Slf4j
public class HttpUtil {
    /**
     * 发起批量转账API 批量转账到零钱
     *
     * @param requestUrl
     * @param requestJson 组合参数
     * @param wechatPayserialNo 商户证书序列号
     * @param mchID4M  商户号
     * @param privatekeypath  商户私钥证书路径
     * @return
     */
    public static String postTransBatRequest(
            String requestUrl,
            String requestJson,
            String wechatPayserialNo,
            String mchID4M,
            String privatekeypath) {
        //通过wechatPayHttpClientBui lden构造的HttpClient,会自动的处理签名和验签，并进行证书自动更新
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        try {
            //商户私钥证书
            HttpPost httpPost = new HttpPost(requestUrl);
            // NOTE: 建议指定charset=utf-8。低于4.4.6版本的HttpCore，不能正确的设置字符集，可能导致签名错误
            httpPost.addHeader("Content-Type", "application/json");
            httpPost.addHeader("Accept", "application/json");
            //"55E551E614BAA5A3EA38AE03849A76D8C7DA735A");
            httpPost.addHeader("Wechatpay-Serial", wechatPayserialNo);
            //-------------------------核心认证 start-----------------------------------------------------------------
            String strToken = VechatPayV3Util.getToken("POST",
                    "/v3/transfer/batches",
                    requestJson,mchID4M,wechatPayserialNo, privatekeypath);

            log.error("微信转账token "+strToken);
            // 添加认证信息
            httpPost.addHeader("Authorization",
                    "WECHATPAY2-SHA256-RSA2048" + " "
                            + strToken);
            //---------------------------核心认证 end---------------------------------------------------------------
            httpPost.setEntity(new StringEntity(requestJson, "UTF-8"));
            //发起转账请求
            response = httpclient.execute(httpPost);
            entity = response.getEntity();//获取返回的数据
            log.info("-----getHeaders.Request-ID:"+response.getHeaders("Request-ID"));
            return EntityUtils.toString(entity);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭流
        }
        return null;
    }

}
