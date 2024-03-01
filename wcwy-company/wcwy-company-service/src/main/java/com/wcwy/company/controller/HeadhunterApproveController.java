package com.wcwy.company.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wcwy.common.base.result.R;
import com.wcwy.common.base.utils.ScaleUtil;
import com.wcwy.common.redis.enums.Cache;
import com.wcwy.common.redis.util.IDGenerator;
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.common.web.annotation.AutoIdempotent;
import com.wcwy.common.web.config.CacheClient;
import com.wcwy.company.entity.HeadhunterApprove;
import com.wcwy.company.service.HeadhunterApproveService;
import com.wcwy.company.service.TRecommendService;
import com.wcwy.company.session.CompanyMetadata;
import com.wcwy.company.vo.HeadhunterApproveVO;
import com.wcwy.post.api.RecommendBasicsApi;
import com.wcwy.system.annotation.Log;
import com.wcwy.system.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/**
 * ClassName: HeadhunterApproveController
 * Description:
 * date: 2023/6/16 9:21
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Api(tags = "推荐官猎企业认证")
@RequestMapping("/headhunterApprove")
@RestController
public class HeadhunterApproveController {
    @Autowired
    private HeadhunterApproveService headhunterApproveService;
    @Autowired
    private CompanyMetadata companyMetadata;
    @Autowired
    private IDGenerator idGenerator;
    @Autowired
    private TRecommendService tRecommendService;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private CacheClient cacheClient;
    @Autowired
    private RecommendBasicsApi recommendBasicsApi;
    @PostMapping("/save")
    @ApiOperation(value = "推荐官申请猎企")
    @Log(title = "推荐官申请猎企", businessType = BusinessType.INSERT)
    @Transactional(rollbackFor = Exception.class)
    @AutoIdempotent
    public R save(@Valid @RequestBody  HeadhunterApproveVO headhunterApproveVO){
        String userid = companyMetadata.userid();
        if(! "TR".equals(userid.substring(0,2))){
            return R.fail("请使用推荐官的身份!");
        }
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("recommend_id",companyMetadata.userid());
        HeadhunterApprove one = headhunterApproveService.getOne(queryWrapper);
        if(one !=null){
            return R.fail("请不要重复提交！");
        }
        HeadhunterApprove headhunterApprove=new HeadhunterApprove();
        headhunterApprove.setHeadhunterApproveId(idGenerator.generateCode("HA"));
        BeanUtils.copyProperties(headhunterApproveVO,headhunterApprove);
        headhunterApprove.setCreateName(companyMetadata.userid());
        //headhunterApprove.setProvincesCities(headhunterApproveVO.getProvincesCities());
        headhunterApprove.setScale(ScaleUtil.screen(headhunterApproveVO.getScale()));
        boolean save = headhunterApproveService.save(headhunterApprove);
        if(save){
            UpdateWrapper updateWrapper=new UpdateWrapper();
            updateWrapper.eq("id",companyMetadata.userid());
            updateWrapper.set("administrator",1);
            tRecommendService.update(updateWrapper);
            recommendBasicsApi.administrator(companyMetadata.userid(),headhunterApproveVO.getFirmName());
            cacheClient.deleteCache(Cache.CACHE_RECOMMEND.getKey(),userid);
            return R.success();
        }
        return R.fail();
    }
    @GetMapping("/select")
    @ApiOperation(value = "查询申请信息(如果为空则未申请)")
    @Log(title = "推荐官申请猎企", businessType = BusinessType.SELECT)
    public R<HeadhunterApprove> select(){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("recommend_id",companyMetadata.userid());
        HeadhunterApprove one = headhunterApproveService.getOne(queryWrapper);
        return R.success(one);
    }


/*    @DeleteMapping("/delect")
    @ApiOperation(value = "")
    @Log(title = "推荐官申请猎企", businessType = BusinessType.SELECT)*/
}
