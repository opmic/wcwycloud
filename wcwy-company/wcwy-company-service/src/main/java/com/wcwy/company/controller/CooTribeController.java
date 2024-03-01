package com.wcwy.company.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wcwy.common.base.result.R;
import com.wcwy.common.redis.enums.Cache;
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.company.dto.CooTribeDTO;
import com.wcwy.company.entity.CooTribe;
import com.wcwy.company.query.CooTribeQuery;
import com.wcwy.company.service.CooTribeService;
import com.wcwy.company.session.CompanyMetadata;
import com.wcwy.company.vo.CooTribeVO;
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

import javax.validation.Valid;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * ClassName: CooTribeController
 * Description:
 * date: 2024/1/19 11:35
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@RestController
@RequestMapping("/cooTribe")
@Api(tags = "coo部落发帖")
public class CooTribeController {


    @Autowired
   private CooTribeService cooTribeService;

    @Autowired
    private CompanyMetadata companyMetadata;
    @Autowired
    private RedisUtils redisUtils;

    @PostMapping("/add")
    @ApiOperation(value = "发帖")
    @Log(title = "发帖", businessType = BusinessType.INSERT)
    public R add(@Valid @RequestBody CooTribeVO cooTribeVO){
        if(cooTribeVO.getType()==0){

            if(StringUtils.isEmpty(cooTribeVO.getTitle())){
                return R.fail("信息不完整");
            }
        }
        if(cooTribeVO.getType()==2){
            if(StringUtils.isEmpty(cooTribeVO.getTitle())){
                return R.fail("信息不完整");
            }
        }
        if(cooTribeVO.getType()==3){
            if(cooTribeVO.getFather()==null || org.springframework.util.StringUtils.isEmpty(cooTribeVO.getUserId1())){
                return R.fail("信息不完整");
            }
            Boolean isReply= cooTribeService.isReply(cooTribeVO.getFather());
            if(isReply){
                return R.fail("不能重复回答！");
            }
        }
      boolean add=  cooTribeService.add(cooTribeVO);
        if(add){
            if(cooTribeVO.getType()==3){
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String format1 = format.format(new Date());
                redisUtils.incrTime3(Cache.CACHE_COMMENT_TRIBE.getKey()+format1+cooTribeVO.getUserId1(),1);
                redisUtils.incr(Cache.CACHE_COMMENT_TRIBE.getKey()+cooTribeVO.getUserId1(),1);
            }
            return R.success();
        }

        return R.fail();
    }





    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改发帖")
    @Log(title = "修改发帖", businessType = BusinessType.UPDATE)
    @ApiImplicitParam(name = "id", required = true, value = "发帖id")
    public R add(@PathVariable("id") Long id, @Valid @RequestBody CooTribeVO cooTribeVO){
      /*  LambdaQueryWrapper<CooTribe> lambdaQueryWrapper=new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(CooTribe::getId,id);*/
        CooTribe cooTribe=new CooTribe();
        BeanUtils.copyProperties(cooTribeVO,cooTribe);
        cooTribe.setId(id);
        cooTribe.setUserId(companyMetadata.userid());
        cooTribe.setCreateTime(LocalDateTime.now());
        boolean update = cooTribeService.updateById(cooTribe);
        if(update){
            return R.success();
        }
        return R.fail();
    }



    @GetMapping("/del")
    @ApiOperation("删帖")
    @ApiImplicitParam(name = "id", required = true, value = "帖子id")
    @Log(title = "删帖", businessType = BusinessType.DELETE)
    public R del(@Valid @RequestParam("id") Long id){
       Boolean del= cooTribeService.del(id);
        if(del){
            return R.success();
        }
        return R.fail();
    }
    @GetMapping("/byId/{id}")
    @ApiOperation(value = "通过id获取")
    @Log(title = "通过id获取", businessType = BusinessType.SELECT)
    @ApiImplicitParam(name = "id", required = true, value = "发帖id")
    public R byId(@PathVariable("id") Long id){
        CooTribeDTO byId = cooTribeService.selectId(id);
        if(byId.getType()==1){
            CooTribeDTO  record1 = cooTribeService.getAnswer(byId.getId(),byId.getUserId());
            if(record1 !=null){
                byId.setCopyWriter(record1.getCopyWriter());
                byId.setUsername(record1.getUsername());
                byId.setSex(record1.getSex());
                byId.setHeadPath(record1.getHeadPath());
            }
            Boolean isReply= cooTribeService.isReply(id);
            byId.setReply(isReply);
        }


        if(! StringUtils.isEmpty(companyMetadata.userid())){
            boolean b = redisUtils.sHasKey(Cache.CACHE_ZAN_TRIBE.getKey() + byId.getId(), companyMetadata.userid());
            byId.setBooleanZan(b);
        }
        return R.success(byId);
    }
    @PostMapping("/page")
    @ApiOperation("获取帖子")
    @Log(title = "删帖", businessType = BusinessType.DELETE)
    public R<CooTribeDTO> page(@RequestBody CooTribeQuery cooTribeQuery){
        if(companyMetadata.userid() !=null){
            cooTribeQuery.setLoginUserId(companyMetadata.userid());
        }

        if( cooTribeQuery.getType() !=null && cooTribeQuery.getType()==3){
            IPage<CooTribe> iPage=  cooTribeService.selectType(cooTribeQuery,companyMetadata.userid());
            return R.success(iPage);
        }
       IPage<CooTribeDTO> iPage= cooTribeService.getPageDTO(cooTribeQuery);
        List<CooTribeDTO> records = iPage.getRecords();
        List<CooTribeDTO> records1 = new ArrayList<>(records.size());
        for (CooTribeDTO record : records) {
            if(record.getType()==1){
                CooTribeDTO  record1 = cooTribeService.getAnswer(record.getId(),record.getUserId());
                if(record1 !=null){
                    record.setCopyWriter(record1.getCopyWriter());
                    record.setUsername(record1.getUsername());
                    record.setSex(record1.getSex());
                    record.setHeadPath(record1.getHeadPath());
                }
            }
            if(! StringUtils.isEmpty(companyMetadata.userid())){
                boolean b = redisUtils.sHasKey(Cache.CACHE_ZAN_TRIBE.getKey() + record.getId(), companyMetadata.userid());
                record.setBooleanZan(b);
            }
            records1.add(record);
        }
        return R.success(iPage);
    }




    @PostMapping("/inquirePage")
    @ApiOperation("获取关于我的帖子")
    @Log(title = "获取关于我的帖子", businessType = BusinessType.DELETE)
    public R<CooTribeDTO> inquirePage(@RequestBody CooTribeQuery cooTribeQuery){
        if(companyMetadata.userid() !=null){
            cooTribeQuery.setLoginUserId(companyMetadata.userid());
        }

        if( cooTribeQuery.getType() !=null && cooTribeQuery.getType()==3){
            IPage<CooTribe> iPage=  cooTribeService.selectType(cooTribeQuery,companyMetadata.userid());
            return R.success(iPage);
        }
        IPage<CooTribeDTO> iPage= cooTribeService.inquirePage(cooTribeQuery);
        List<CooTribeDTO> records = iPage.getRecords();
        List<CooTribeDTO> records1 = new ArrayList<>(records.size());
        for (CooTribeDTO record : records) {
            if(record.getType()==1){
                CooTribeDTO  record1 = cooTribeService.getAnswer(record.getId(),record.getUserId());
                if(record1 !=null){
                    record.setCopyWriter(record1.getCopyWriter());
                    record.setUsername(record1.getUsername());
                    record.setSex(record1.getSex());
                    record.setHeadPath(record1.getHeadPath());
                }
            }
            if(! StringUtils.isEmpty(companyMetadata.userid())){
                boolean b = redisUtils.sHasKey(Cache.CACHE_ZAN_TRIBE.getKey() + record.getId(), companyMetadata.userid());
                record.setBooleanZan(b);
            }
            records1.add(record);
        }
        return R.success(iPage);
    }



    @GetMapping("/dinZan")
    @ApiOperation("点赞")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", required = true, value = "帖子id"),
            @ApiImplicitParam(name = "userId", required = true, value = "发帖人")

    })
    public R dinZan(@RequestParam("id") Long id,@RequestParam("userId") String userId){
        String userid = companyMetadata.userid();
        String substring = userid.substring(0, 2);
        if( ! substring.equals("TR")){
            return R.fail("请使用推荐管账号！");
        }
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String format1 = format.format(new Date());
        boolean b = redisUtils.sHasKey(Cache.CACHE_ZAN_TRIBE.getKey() + id, companyMetadata.userid());
        if(! b){
            redisUtils.sSet(Cache.CACHE_RECORD_ZAN_TRIBE.getKey(),id);
            redisUtils.sSet(Cache.CACHE_ZAN_TRIBE.getKey()+id,companyMetadata.userid());
            redisUtils.incrTime3(Cache.CACHE_ZAN_TRIBE.getKey()+format1+userId,1);
            return R.success();
        }
            redisUtils.incrTime3(Cache.CACHE_ZAN_TRIBE.getKey()+format1+userId,-1);
            redisUtils.setRemove(Cache.CACHE_ZAN_TRIBE.getKey()+id,companyMetadata.userid());
            return R.success();
    }
    @GetMapping("/share")
    @ApiImplicitParam(name = "id", required = true, value = "帖子id")
    @ApiOperation("分享")
    public R share(@RequestParam("id") Long id){
        redisUtils.incr(Cache.CACHE_SHARE_TRIBE.getKey()+id,1);
        redisUtils.sSet(Cache.CACHE_RECORD_SHARE_TRIBE.getKey(),id);
        return R.success();
    }
    @GetMapping("/browse")
    @ApiImplicitParam(name = "id", required = true, value = "帖子id")
    @ApiOperation("浏览")
    public R browse(@RequestParam("id") Long id){
        redisUtils.incr(Cache.CACHE_TRIBE_BROWSE.getKey()+id,1);
        redisUtils.sSet(Cache.CACHE_RECORD_TRIBE_BROWSE.getKey(),id);
        return R.success();
    }

    @GetMapping("/compare")
    @ApiOperation("今日点赞今日评论")
    public R compare(){
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String format1 = format.format(new Date());
        String format2 = dateTimeFormatter.format( LocalDateTime.now().plusDays(-1));
        long incr = redisUtils.incr(Cache.CACHE_ZAN_TRIBE.getKey() + format1 + companyMetadata.userid(), 0);
        long incr1 = redisUtils.incr(Cache.CACHE_ZAN_TRIBE.getKey() + format2 + companyMetadata.userid(), 0);

        long l = redisUtils.incrTime3(Cache.CACHE_COMMENT_TRIBE.getKey() + format1 + companyMetadata.userid(), 0);
        long l2 = redisUtils.incrTime3(Cache.CACHE_COMMENT_TRIBE.getKey() + format2 + companyMetadata.userid(), 0);
        Map map=new HashMap(4);
        map.put("todayZan",incr);
        map.put("yesterdayZan",incr1);
        map.put("todayComment",l);
        map.put("yesterdayComment",l2);
        return R.success(map);
    }

    @PostMapping("/answer")
    @ApiOperation("获取回答")
    public R<CooTribeDTO> answer(@RequestBody CooTribeQuery cooTribeQuery){

        if(cooTribeQuery.getId()==null){
            return R.fail("请传入发帖id");
        }
        IPage<CooTribeDTO> page = cooTribeService.pageAnswer(cooTribeQuery);
        Boolean isReply= cooTribeService.isReply(cooTribeQuery.getId());
        LambdaQueryWrapper<CooTribe> lambdaQueryWrapper=new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(CooTribe::getFather,cooTribeQuery.getId());
        lambdaQueryWrapper.eq(CooTribe::getUserId,companyMetadata.userid());
        CooTribe one = cooTribeService.getOne(lambdaQueryWrapper);
        List<CooTribeDTO> records = page.getRecords();
        List list=new ArrayList(records.size());
        for (CooTribeDTO record : records) {
            if(! StringUtils.isEmpty(companyMetadata.userid())){
                boolean b = redisUtils.sHasKey(Cache.CACHE_ZAN_TRIBE.getKey() + record.getId(), companyMetadata.userid());
                record.setBooleanZan(b);
            }
            list.add(record);
        }
        if(list !=null && list.size()>0){
            page.setRecords(list);
        }

        Map map=new HashMap(3);
        map.put("data",page);
        map.put("isReply",isReply);
        map.put("one",one);
        if(one !=null){
            CooTribeDTO cooTribeDTO=new CooTribeDTO();
            BeanUtils.copyProperties(one,cooTribeDTO);
            if(! StringUtils.isEmpty(companyMetadata.userid())){
                boolean b = redisUtils.sHasKey(Cache.CACHE_ZAN_TRIBE.getKey() + cooTribeDTO.getId(), companyMetadata.userid());
                cooTribeDTO.setBooleanZan(b);
            }
            map.put("one",cooTribeDTO);
        }
        return R.success(map);
    }


    @GetMapping("/topSearch")
    @ApiOperation("高赞榜")
    public R<CooTribe> topSearch(){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("on_line",0);
        queryWrapper.orderByDesc("zan");
        IPage<CooTribe> page = cooTribeService.page(new Page<>(0,10),queryWrapper);
        return R.success(page);
    }
}
