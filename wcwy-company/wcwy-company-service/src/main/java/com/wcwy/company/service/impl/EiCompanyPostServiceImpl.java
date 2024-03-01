package com.wcwy.company.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wcwy.company.entity.EiCompanyPost;
import com.wcwy.company.service.EiCompanyPostService;
import com.wcwy.company.mapper.EiCompanyPostMapper;
import com.wcwy.post.api.TCompanyPostApi;
import com.wcwy.post.po.ProvincesCitiesPO;
import com.wcwy.post.po.TCompanyPostPO;
import com.wcwy.post.pojo.TCompanyPostRecord;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Administrator
 * @description 针对表【ei_company_post(岗位基础信息)】的数据库操作Service实现
 * @createDate 2023-03-30 14:41:39
 */
@Service
public class EiCompanyPostServiceImpl extends ServiceImpl<EiCompanyPostMapper, EiCompanyPost>
        implements EiCompanyPostService {
    @Autowired
    private TCompanyPostApi tCompanyPostApi;

    @Override
    public EiCompanyPost add(String putInPost) {
        TCompanyPostRecord tCompanyPostRecord = tCompanyPostApi.postRecord(putInPost);
        EiCompanyPost eiCompanyPost=null;
        if(tCompanyPostRecord !=null){
             eiCompanyPost=new EiCompanyPost();
            eiCompanyPost.setPostId(tCompanyPostRecord.getPostId());
            eiCompanyPost.setCompanyId(tCompanyPostRecord.getCompanyId());
            eiCompanyPost.setBeginSalary(tCompanyPostRecord.getBeginSalary());
            eiCompanyPost.setEndSalary(tCompanyPostRecord.getEndSalary());
            eiCompanyPost.setPostLabel(tCompanyPostRecord.getPostLabel());
            ProvincesCitiesPO workCity = tCompanyPostRecord.getWorkCity();
            com.wcwy.company.po.ProvincesCitiesPO citiesPO=new com.wcwy.company.po.ProvincesCitiesPO();
            citiesPO.setCity(workCity.getCity());
            citiesPO.setCityid(workCity.getCityid());
            citiesPO.setProvince(workCity.getProvince());
            citiesPO.setProvinceid(workCity.getProvinceid());
            eiCompanyPost.setWorkCity(citiesPO);
            eiCompanyPost.setPostType(tCompanyPostRecord.getPostType());
            eiCompanyPost.setHiredBounty(tCompanyPostRecord.getHiredBounty());
            eiCompanyPost.setMoneyReward(JSON.toJSONString(tCompanyPostRecord.getHeadhunterPositionRecord()));
            this.saveOrUpdate(eiCompanyPost);
        }

        return eiCompanyPost;
    }

}




