package com.wcwy.company.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wcwy.common.redis.enums.Cache;
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.company.entity.TJobhunterHideCompany;
import com.wcwy.company.service.TJobhunterHideCompanyService;
import com.wcwy.company.mapper.TJobhunterHideCompanyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Administrator
 * @description 针对表【t_jobhunter_hide_company(用户屏蔽公司表)】的数据库操作Service实现
 * @createDate 2022-10-08 11:58:44
 */
@Service
public class TJobhunterHideCompanyServiceImpl extends ServiceImpl<TJobhunterHideCompanyMapper, TJobhunterHideCompany>
        implements TJobhunterHideCompanyService {
    @Autowired
    private TJobhunterHideCompanyMapper tJobhunterHideCompanyMapper;

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public List<Object> cacheCompany(String JobHunter) {
      //
        Set<Object> objects = redisUtils.sGet(Cache.JOB_HUNTER_HIDE_COMPANY.getKey() + JobHunter);
        if(objects !=null && objects.size()>0){
            List<Object> list=null;
            for (Object object : objects) {
                list= (List<Object>) object;
            }
            return  list;
        }
        List<Object> objects1 = tJobhunterHideCompanyMapper.cacheCompany(JobHunter);
        if(objects1!=null && objects1.size()>0){
            redisUtils.sSetAndTime(Cache.JOB_HUNTER_HIDE_COMPANY.getKey()+JobHunter,3600,objects1);
        }
        return objects1;
    }
}




