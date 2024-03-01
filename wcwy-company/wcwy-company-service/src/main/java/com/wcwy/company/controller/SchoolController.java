package com.wcwy.company.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wcwy.common.base.result.R;
import com.wcwy.common.redis.enums.RedisCache;
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.company.entity.School;
import com.wcwy.company.service.SchoolService;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ClassName: SchooController
 * Description:
 * date: 2022/9/8 16:02
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Api(tags = "学校信息接口接口")
@RestController
@RequestMapping("/school")
public class SchoolController {

    @Autowired
   private SchoolService schoolService;
    @Autowired
   private RedisUtils redisUtils;
 /*   @Autowired
    private WorkApi workApi;*/
/*    @GetMapping("/cc")
    @ApiOperation("测试")
    @GlobalTransactional
    public String cc() {
        *//*  return companyMetadata.userid();*//*

          *//*  String aa = workApi.cc("ccccc");*//*
            return aa;
    }*/

    @GetMapping("/select")
    @ApiOperation(value = "查询学校信息")
    @ApiImplicitParam(name = "schoolName", required = false, value = "学校名称")
    public R<School> select(@RequestParam(value = "schoolName",required = false) String schoolName){
       List<School> schoolList= (List<School>) redisUtils.get(RedisCache.SCHOOL.getValue());
       if(schoolList!=null && schoolList.size() >0 ){
           return R.success(schoolList);
       }
        QueryWrapper queryWrapper=new QueryWrapper();
        if(! StrUtil.isEmpty(schoolName)){
            queryWrapper.like("school_name",schoolName);
        }
        List<School> list = schoolService.list(queryWrapper);
        if(list.size()==0){
            redisUtils.set(RedisCache.SCHOOL.getValue(),list,60*5);//防止缓存穿透
        }
        redisUtils.set(RedisCache.SCHOOL.getValue(),list);
        return R.success(list);
    }

    @GetMapping("/insert")
    @ApiOperation(value = "添加学校")
    @ApiImplicitParam(name = "schoolName", required = true, value = "学校名称")
  /*  @PreAuthorize("hasAnyAuthority('admin')")*/
    public R insert(@RequestParam(value = "schoolName") String schoolName){

        if(StrUtil.isEmpty(schoolName)){
           return R.fail("请输入学校名！");
        }
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("school_name",schoolName);
        School one = schoolService.getOne(queryWrapper);
        if(one !=null){
            return R.fail("该学校已经存在！");
        }
        School school=new School();
        school.setSchoolName(schoolName);
        school.setOther("未定");
        boolean save = schoolService.save(school);
        if(save){
            return R.success("添加成功！");
        }
        return R.fail("添加失败");
    }

}
