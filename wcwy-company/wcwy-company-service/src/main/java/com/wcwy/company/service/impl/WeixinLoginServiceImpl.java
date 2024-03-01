package com.wcwy.company.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.wcwy.common.base.constant.Constanst;
import com.wcwy.common.base.utils.AesUtil;
import com.wcwy.common.base.utils.DateUtils;
import com.wcwy.common.base.utils.HttpClientUtil;
import com.wcwy.common.base.utils.PropertiesUtil;
import com.wcwy.company.service.WeixinLoginService;

import com.wcwy.company.vo.AccessToken;
import com.wcwy.company.vo.HttpParame;
import com.wcwy.company.vo.WechatUserUnionID;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author java1234_小锋
 * @site www.java1234.com
 * @company Java知识分享网
 * @create 2019-02-25 下午 10:49
 */
@Service
@Slf4j
public class WeixinLoginServiceImpl implements WeixinLoginService {

    @Override
    public String genLoginUrl() {
        String content = Constanst.PWD_MD5+ DateUtils.getYYYYMMdd();
        byte[] encrypt = AesUtil.encrypt(content, AesUtil.PASSWORD_SECRET_KEY, 16);
        String parseByte2HexStr = AesUtil.parseByte2HexStr(encrypt);
        String url = HttpParame.AUTHORIZATION_URL;
        url = url.replaceAll("APPID", PropertiesUtil.getValue(HttpParame.APPID));
        try {
            url = url.replaceAll("REDIRECT_URI", URLEncoder.encode(
                    PropertiesUtil.getValue(HttpParame.REDIRECT_URI),"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        url = url.replaceAll("SCOPE", "snsapi_login");
        url = url.replace("STATE", parseByte2HexStr);	//加密state进行验证 回调地址当天有效 防止恶意攻击
        return url;
    }

    @Override
    public AccessToken getAccessToken(String code) {
        String accessTokenUrl = HttpParame.ACCESS_TOKEN_URL;
        accessTokenUrl = accessTokenUrl.replaceAll("APPID", PropertiesUtil.getValue(HttpParame.APPID));
        accessTokenUrl = accessTokenUrl.replaceAll("SECRET", PropertiesUtil.getValue(HttpParame.SECRET));
        accessTokenUrl = accessTokenUrl.replaceAll("CODE", code);
        String responseContent = HttpClientUtil.getInstance().sendHttpGet(accessTokenUrl);
        if (responseContent == null || responseContent == "") {
            return null;
        }
        JSONObject parseObject = JSONObject.parseObject(responseContent);
        AccessToken accessToken = JSONObject.toJavaObject(parseObject, AccessToken.class);
        return accessToken;
    }

    @Override
    public WechatUserUnionID getUserUnionID(String access_token, String openid) {
        String unionIDUrl = HttpParame.GET_UNIONID_URL;
        unionIDUrl = unionIDUrl.replace("ACCESS_TOKEN", access_token);
        unionIDUrl = unionIDUrl.replace("OPENID", openid);
        String responseContent = HttpClientUtil.getInstance().sendHttpGet(unionIDUrl);
        System.out.println("responseContent:"+responseContent);
        if (responseContent == null || responseContent == "") {
            return null;
        }
        JSONObject parseObject = JSONObject.parseObject(responseContent);
        WechatUserUnionID userUnionID = JSONObject.toJavaObject(parseObject, WechatUserUnionID.class);
        return userUnionID;
    }

    @Override
    public String state() {
        String content = Constanst.PWD_MD5+ DateUtils.getYYYYMMdd();
        byte[] encrypt = AesUtil.encrypt(content, AesUtil.PASSWORD_SECRET_KEY, 16);
        String parseByte2HexStr = AesUtil.parseByte2HexStr(encrypt);
        return parseByte2HexStr;
    }
}
