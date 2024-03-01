package com.wcwy.oauth.service;

import com.wawy.company.api.TCompanyLoginApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ClassName: GitOpenidServiceImpl
 * Description:
 * date: 2022/9/27 8:43
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Service
public class GetOpenidServiceImpl {
    @Autowired
    private TCompanyLoginApi tCompanyLoginApi;

    public String getOpenid(String code ,String state){
        String openid = tCompanyLoginApi.openid(code, state);
        return openid;
    }
}
