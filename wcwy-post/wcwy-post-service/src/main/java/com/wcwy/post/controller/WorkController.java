package com.wcwy.post.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wcwy.common.base.result.R;
import com.wcwy.common.redis.enums.RedisCache;
import com.wcwy.company.entity.TJobhunterResumeConfig;
import com.wcwy.post.entity.Work;
import com.wcwy.post.service.WorkService;
import com.wcwy.post.session.CompanyMetadata;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;

/**
 * ClassName: WorkController
 * Description:
 * date: 2022/9/14 15:29
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@RestController
@Api(tags = "工作经验表")
@RequestMapping("/work")

public class WorkController {


    @Autowired
    private WorkService workService;
    @Autowired
    private CompanyMetadata CompanyMetadata;
    @Autowired
    private RedisTemplate redisTemplate;

    @PostConstruct
    public void doConstruct() throws Exception {
        List<Work> list = workService.list();
        ValueOperations<String, List<Work>> value = redisTemplate.opsForValue();
        redisTemplate.delete(RedisCache.WORK.getValue());
        if(list.size()>0){
            value.set(RedisCache.WORK.getValue(), list);
        }


    }

    @GetMapping("/selectList")
    @ApiOperation(value = "查询工作经验词典")
    public R<Work> selectList() {
        ValueOperations<String, List<Work>> value = redisTemplate.opsForValue();
        if(value.get(RedisCache.WORK.getValue())!=null){
            return R.success(value.get(RedisCache.WORK.getValue()));
        }
        List<Work> list = workService.list();
        if(list.size()>0){
            value.set(RedisCache.WORK.getValue(), list);
        }
        return R.success(list);
    }

    @GetMapping("/addWork/{workExperience}")
    @ApiOperation(value = "添加工作经验接口")
    public R addWork(@PathVariable("workExperience") String workExperience) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("work_experience", workExperience);
        Work one = workService.getOne(queryWrapper);
        if (one != null) {
            return R.fail("该字段已存在");
        }
        Work work = new Work();
        work.setWorkExperience(workExperience);
        work.setCreateTime(LocalDateTime.now());
        /* work.setCreateId();*/
        boolean save = workService.save(work);
        if (save) {
            return R.success("添加成功");
        }
        return R.fail("添加失败!");
    }




/*    @PostMapping("/updateWork")
    @ApiOperation(value = "更新工经验")
    public R updateWork(){

    }*/


}
