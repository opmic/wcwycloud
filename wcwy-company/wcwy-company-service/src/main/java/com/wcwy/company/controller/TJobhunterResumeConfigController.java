package com.wcwy.company.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wcwy.common.base.result.R;
import com.wcwy.common.base.utils.judgment_user;
import com.wcwy.common.redis.enums.RedisCache;
import com.wcwy.company.entity.TJobhunterResumeConfig;
import com.wcwy.company.po.TJobhunterResumeConfigPO;
import com.wcwy.company.service.TJobhunterResumeConfigService;
import com.wcwy.company.session.CompanyMetadata;
import com.wcwy.system.annotation.Log;
import com.wcwy.system.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * ClassName: TJobhunterResumeConfigController
 * Description:
 * date: 2022/10/24 11:37
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@RestController
@RequestMapping("/JobhunterResumeConfig")
@Api(tags = "求职者简历配置接口")
@Slf4j
public class TJobhunterResumeConfigController {
    @Autowired
    private TJobhunterResumeConfigService tJobhunterResumeConfigService;
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private CompanyMetadata companyMetadata;

    @PostConstruct
    public void doConstruct() throws Exception {
        List<TJobhunterResumeConfig> list = tJobhunterResumeConfigService.list();
        ValueOperations<String, Object> value = redisTemplate.opsForValue();
        redisTemplate.delete(RedisCache.TJOBHUNTERRESUMECONFIG.getValue());
        for (TJobhunterResumeConfig tJobhunterResumeConfig : list) {
            value.set(RedisCache.TJOBHUNTERRESUMECONFIG.getValue() + ":" + tJobhunterResumeConfig.getUserId(), tJobhunterResumeConfig);
        }
    }


    @GetMapping("/update")
    @ApiOperation("/修改简历配置表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", required = true, value = "求职者id"),
            @ApiImplicitParam(name = "configId", required = true, value = "简历配置id"),
            @ApiImplicitParam(name = "visible", required = true, value = "简历配置 1:所以可看 2:都不可看 3:推荐官可看")
    })
    @Log(title = "修改简历配置表", businessType = BusinessType.UPDATE)
    public R update(@RequestParam("userId") String userId, @RequestParam("configId") String configId, @RequestParam("visible") String visible) throws Exception {
        Boolean aBoolean = judgment_user.UserEql(companyMetadata.userid(), userId);
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("id", configId);
        if ("1".equals(visible)) {
            updateWrapper.set("visible", 0);
            updateWrapper.set("hunter_visible", 0);
        } else if ("2".equals(visible)) {
            updateWrapper.set("visible", 1);
            updateWrapper.set("hunter_visible", 1);
        } else if ("3".equals(visible)) {
            updateWrapper.set("visible", 0);
            updateWrapper.set("hunter_visible", 1);
        }
        boolean update = tJobhunterResumeConfigService.update(updateWrapper);
        tJobhunterResumeConfigService.updateRedis(userId);
        if (update) {
            return R.success("修改成功");
        }
        return R.success("修改失败");
    }

    @GetMapping("/selectResumeConfig/{id}")
    @ApiOperation("获取简历配置")
    @ApiImplicitParam(name = "id", required = true, value = "求职者id")
    @Log(title = "获取简历配置", businessType = BusinessType.SELECT)
    public R<TJobhunterResumeConfigPO> selectResumeConfig(@PathVariable("id") String id) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id", id);
        TJobhunterResumeConfig one = tJobhunterResumeConfigService.getOne(queryWrapper);

        if(one==null){
            return R.fail("该求职者无简历配置");
        }
        TJobhunterResumeConfigPO tJobhunterResumeConfigPO=new TJobhunterResumeConfigPO();
        tJobhunterResumeConfigPO.setId(one.getId());
        if(0==one.getVisible() && 0== one.getHunterVisible()){
            tJobhunterResumeConfigPO.setVisible(1);
        }else if(1==one.getVisible() && 1== one.getHunterVisible()){
            tJobhunterResumeConfigPO.setVisible(2);
        } else if(0==one.getVisible() && 1== one.getHunterVisible()) {
            tJobhunterResumeConfigPO.setVisible(3);
        }
        return R.success(tJobhunterResumeConfigPO);
    }
}
