package com.wcwy.company.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wcwy.common.base.result.R;
import com.wcwy.common.redis.util.IDGenerator;
import com.wcwy.company.entity.TJobhunterExpectPosition;
import com.wcwy.company.service.TJobhunterExpectPositionService;
import com.wcwy.company.service.TJobhunterService;
import com.wcwy.company.session.CompanyMetadata;
import com.wcwy.company.vo.TJobhunterExpectPositionVO;
import com.wcwy.company.vo.TJobhunterProjectRecordAppVO;
import com.wcwy.system.annotation.Log;
import com.wcwy.system.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * @description 针对表【t_jobhunter_expect_position(期望职位表)】的数据库操作Service
 * @createDate 2022-10-08 11:58:41
 */
@RestController
@RequestMapping("/jobhunterExpectPosition")
@Api(tags = "期望职位接口")
public class TJobhunterExpectPositionController {
    @Resource
    private TJobhunterExpectPositionService tJobhunterExpectPositionService;
    @Autowired
    private IDGenerator idGenerator;
    @Autowired
    private TJobhunterService tJobhunterService;
    @Autowired
    private CompanyMetadata companyMetadata;

    @PostMapping("/addExpectPosition")
    @ApiOperation("添加+修改求职期望")
    @Log(title = "添加+修改求职期望", businessType = BusinessType.INSERT)
    public R addExpectPosition(@Valid @RequestBody TJobhunterExpectPositionVO tJobhunterExpectPositionVO) {

        R jurisdiction = tJobhunterService.jurisdiction(companyMetadata.userid(), tJobhunterExpectPositionVO.getUserId());
        if(jurisdiction.getCode()==405){
            return jurisdiction;
        }
        TJobhunterExpectPosition tJobhunterExpectPosition = new TJobhunterExpectPosition();
        BeanUtils.copyProperties(tJobhunterExpectPositionVO, tJobhunterExpectPosition);

        boolean save =false;
        if(StringUtils.isEmpty(tJobhunterExpectPositionVO.getPostionId())){
            int a= tJobhunterExpectPositionService.selectCount(tJobhunterExpectPositionVO.getUserId());
            if(a>=3){
                return R.fail("请限制在三份以内！");
            }
            tJobhunterExpectPosition.setPostionId(idGenerator.generateCode("PT"));
            tJobhunterExpectPosition.setCreateTime(LocalDateTime.now());
             save = tJobhunterExpectPositionService.save(tJobhunterExpectPosition);
        }else {
            tJobhunterExpectPosition.setUpdateTime(LocalDateTime.now());
            save = tJobhunterExpectPositionService.updateById(tJobhunterExpectPosition);
        }

        if (save) {
            return R.success("操作成功！");
        }
        return R.fail("操作失败！");
    }
    @GetMapping("delete")
    @ApiOperation("删除求职期望")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", required = true, value = "求职者id"),
            @ApiImplicitParam(name = "postionId", required = true, value = "求职期望Id")
    })
    @Log(title = "删除求职期望", businessType = BusinessType.DELETE)
    public R delete(@RequestParam("userId") String userId ,@RequestParam("postionId") String postionId ){
        R jurisdiction = tJobhunterService.jurisdiction(companyMetadata.userid(), userId);
        Integer code = jurisdiction.getCode();
        if(code==405){
            return jurisdiction;
        }
       int a= tJobhunterExpectPositionService.selectCount(userId);
        if(a==1){
            return R.fail("请保留一份求职期望！");
        }
        boolean b = tJobhunterExpectPositionService.removeById(postionId);
        if(b){
            return R.success("删除成功!");
        }
        return R.fail("删除失败!");
    }

    @GetMapping("/selectPositionName")
    @ApiOperation("查询期望职位")
    @Log(title = "查询期望职位", businessType = BusinessType.SELECT)
    @ApiImplicitParam(name = "jobHunter", required = true, value = "求职者id")
    public R<List<String>> selectPositionName(@RequestParam("jobHunter") String jobHunter){
      List<String>  list= tJobhunterExpectPositionService.selectPositionName(jobHunter);
      List<String> list2=new ArrayList<>(list.size());
      if(list.size()>0){
          for (String s : list) {
              List<String> list1 = JSON.parseObject(s, List.class);
              String s1 = list1.get(list1.size() - 1);
              list2.add(s1);
          }
      }
      return R.success(list2);
    }

}
