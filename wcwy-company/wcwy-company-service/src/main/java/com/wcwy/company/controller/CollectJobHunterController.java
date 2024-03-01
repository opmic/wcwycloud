package com.wcwy.company.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wcwy.common.base.result.R;
import com.wcwy.common.redis.enums.Lock;
import com.wcwy.common.redis.util.IDGenerator;
import com.wcwy.common.web.annotation.AutoIdempotent;
import com.wcwy.company.dto.CollectJobHunterDTO;
import com.wcwy.company.entity.CollectJobHunter;
import com.wcwy.company.entity.TJobhunter;
import com.wcwy.company.query.CollectJobHunterQuery;
import com.wcwy.company.service.CollectJobHunterService;
import com.wcwy.company.service.CompanyUserRoleService;
import com.wcwy.company.service.TJobhunterService;
import com.wcwy.company.session.CompanyMetadata;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * ClassName: CollectJobHunterController
 * Description:
 * date: 2023/4/3 14:06
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@RestController
@RequestMapping("/changeIdentity")
@Api(tags = "企业收藏表")
public class CollectJobHunterController {
    @Autowired
    private CollectJobHunterService collectJobHunterService;
    @Autowired
    private CompanyMetadata companyMetadata;
    @Autowired
    private TJobhunterService tJobhunterService;
    @Resource
    private RedissonClient redissonClient;
    @Autowired
    private CompanyUserRoleService companyUserRoleService;
    @Autowired
    private IDGenerator idGenerator;
    @PutMapping("/save")
    @ApiImplicitParam(name = "tJobHunterId", required = true, value = "求职者id")
    @ApiOperation("收藏求职者")
    @AutoIdempotent
    public R save(@RequestParam("tJobHunterId") List<String> tJobHunterId) throws InterruptedException {
        if(tJobHunterId.size()==0){
            return R.fail("请选择求职者!");
        }
        String userid = companyMetadata.userid();
        RLock lock = redissonClient.getLock(Lock.COLLECT_JOB_HUNTER.getLock()+userid);
        boolean isLock = lock.tryLock(1, 10, TimeUnit.SECONDS);
        // 判断释放获取成功
        if (isLock) {
            try {
                for (String s : tJobHunterId) {
                    TJobhunter r = tJobhunterService.getById(s);
                    if(r==null){
                        if(tJobHunterId.size()>0){
                            continue;
                        }
                        return R.fail("该求职者不存在!");
                    }

                    QueryWrapper queryWrapper=new QueryWrapper();
                    queryWrapper.eq("t_job_hunter_id",s);
                    queryWrapper.eq("t_company_id",userid);
                    int count = collectJobHunterService.count(queryWrapper);
                    if (count>0) {
                        if(tJobHunterId.size()>0){
                           continue;
                        }
                        return R.fail("该求职者已被收藏!");

                    }
                    CollectJobHunter collectJobHunter=new CollectJobHunter();
                    collectJobHunter.setCollectId(idGenerator.generateCode("CJH"));
                    collectJobHunter.setTCompanyId(userid);
                    collectJobHunter.setTJobHunterId(s);
                    collectJobHunter.setCreateTime(LocalDateTime.now());
                    boolean save = collectJobHunterService.save(collectJobHunter);
                    if(tJobHunterId.size()==1){
                        if(save){
                            return R.success("收藏成功");
                        }
                    }
                }
            } finally {
                // 释放锁
                lock.unlock();
            }
        }

        return R.success("收藏成功");
    }

    @DeleteMapping("/delete")
    @ApiImplicitParam(name = "jobHunter", required = true, value = "求职者id")
    @ApiOperation("删除收藏求职者")
    @AutoIdempotent
    public R delete(@RequestParam("jobHunter") List<String> jobHunter){

        if(jobHunter.size()==0){
            return R.fail("请选择求职者!");
        }
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("t_company_id",companyMetadata.userid());
        queryWrapper.in("t_job_hunter_id",jobHunter);
        boolean remove = collectJobHunterService.remove(queryWrapper);
        if(remove){
            return R.success("已取消收藏!");
        }
        return R.fail("操作失败!");
    }

    @PostMapping("select")
    @ApiOperation("我的收藏")
    public R<CollectJobHunterDTO> select(@RequestBody CollectJobHunterQuery collectJobHunterQuery){
        IPage<CollectJobHunterDTO> collectJobHunterDTO= collectJobHunterService.select(collectJobHunterQuery,companyMetadata.userid());
      /*  List<CollectJobHunterDTO> records = collectJobHunterDTO.getRecords();*/
        return R.success(collectJobHunterDTO);
    }



}
