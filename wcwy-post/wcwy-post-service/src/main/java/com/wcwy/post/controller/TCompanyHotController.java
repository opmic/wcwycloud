package com.wcwy.post.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wcwy.common.redis.enums.Cache;
import com.wcwy.company.entity.TCompany;
import com.wcwy.post.entity.TCompanyHot;
import com.wcwy.post.service.TCompanyHotService;
import com.wcwy.post.session.CompanyMetadata;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;

/**
 * ClassName: TCompanyHotController
 * Description:
 * date: 2023/1/12 11:41
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@RestController
@RequestMapping("/tCompanyHot")
@Api(tags = "企业热度表接口")
@Slf4j
public class TCompanyHotController {
    @Autowired
    private TCompanyHotService tCompanyHotService;

    @Autowired
    private RedisTemplate redisTemplate;

    @ApiOperation("测试环境")
    @GetMapping("/test")
    public void test(){
        SetOperations setOperations = redisTemplate.opsForSet();
        Set<TCompany> listCompany = setOperations.members("listCompany");
        for (TCompany tCompany : listCompany) {

            QueryWrapper queryWrapper=new QueryWrapper();
            queryWrapper.eq("company_id",tCompany.getCompanyId());
            int count = tCompanyHotService.count(queryWrapper);
            if(count>0){
                continue;
            }
            TCompanyHot tCompanyHot=new TCompanyHot();
            tCompanyHot.setCompanyId(tCompany.getCompanyId());
            tCompanyHot.setHot(0L);
            tCompanyHot.setFirmSize(tCompany.getFirmSize());
            tCompanyHot.setIndustry(tCompany.getIndustry());
            tCompanyHot.setCompanyTypeId(tCompany.getCompanyTypeId());
            tCompanyHot.setDeleted(0);
            tCompanyHot.setCompanyName(tCompany.getCompanyName());
            tCompanyHot.setLogo(tCompany.getLogoPath());
            boolean save = tCompanyHotService.save(tCompanyHot);
            if(! save){
                log.error("没有记录成功"+tCompany.getCompanyId());
            }
            tCompanyHot=null;
        }
    }
  /*  @ApiOperation("添加热度")
    @GetMapping("/addTCompanyHot")*/
    @PostConstruct
    public void TCompanyHotCache(){
        ValueOperations<String,TCompanyHot> valueOperations = redisTemplate.opsForValue();
        List<TCompanyHot> list = tCompanyHotService.list();
        for (TCompanyHot tCompanyHot : list) {
            valueOperations.set(Cache.CACHE_COMPANY_HOT.getKey()+tCompanyHot.getCompanyId(),tCompanyHot);
        }
    }
}
