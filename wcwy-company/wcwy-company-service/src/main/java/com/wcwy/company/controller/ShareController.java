package com.wcwy.company.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wcwy.common.base.result.R;
import com.wcwy.common.base.utils.DateUtils;
import com.wcwy.common.base.utils.LocalDateTimeUtils;
import com.wcwy.common.redis.enums.QRCode;
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.company.dto.GraphDTO;
import com.wcwy.company.dto.OperationalDataDTO;
import com.wcwy.company.dto.ShareDateDTO;
import com.wcwy.company.entity.Share;
import com.wcwy.company.entity.SourceOfReturns;
import com.wcwy.company.po.JobHunterShare;
import com.wcwy.company.po.TCompanySharePO;
import com.wcwy.company.po.TRecommendShare;
import com.wcwy.company.query.ShareQuery;
import com.wcwy.company.service.*;
import com.wcwy.company.session.CompanyMetadata;
import com.wcwy.post.api.OrderInfoApi;
import com.wcwy.post.pojo.DivideIntoPOJO;
import com.wcwy.system.annotation.Log;
import com.wcwy.system.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.parameters.P;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * ClassName: ShareController
 * Description:
 * date: 2023/4/10 15:00
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Api(tags = "分享管理")
@RestController
@RequestMapping("/share")
public class ShareController {
    @Autowired
    private ReferrerRecordService referrerRecordService;
    @Autowired
    private SourceOfReturnsService sourceOfReturnsService;
    @Autowired
    private TCompanyService tCompanyService;
    @Autowired
    private PromotionPostService promotionPostService;
    @Autowired
    private TRecommendService tRecommendService;
    @Autowired
    private ShareService shareService;
    @Autowired
    private ShareDataService shareDataService;
    @Autowired
    private PutInResumeService putInResumeService;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private TJobhunterService tJobhunterService;

    @Autowired
    private CompanyMetadata companyMetadata;
    @Autowired
    private OrderInfoApi orderInfoApi;
    @PostMapping("/shareJobHunter")
    @ApiOperation("求职者数据")
    @Log(title = "求职者数据", businessType = BusinessType.SELECT)
    public R<JobHunterShare> shareJobHunter(@RequestBody ShareQuery shareQuery){
      IPage<JobHunterShare> iPage= tJobhunterService.shareJobHunter(shareQuery,companyMetadata.userid());
        List<JobHunterShare> records = iPage.getRecords();
        for (JobHunterShare record : records) {
          if(  record.getReferrerRecordId()==null){
              record.setIsDelete(true);
          }else {
              record.setIsDelete(false);
          }
            Object o = redisUtils.get("loginTime:" + record.getUserId());
            if(! StringUtils.isEmpty(o)){
                record.setLoginTime(o);
            }
            long incr = redisUtils.incr("loginTimeSum:" + record.getUserId(), 1);
            record.setLoginTimeSum(incr);
        }
        iPage.setRecords(records);
       /* List<JobHunterShare> records = iPage.getRecords();
        for (JobHunterShare record : records) {
            DivideIntoPOJO divideIntoPOJO = orderInfoApi.divideInto(record.getUserId(), companyMetadata.userid(),3);
          *//*  record.setCosts(new BigDecimal(0.00));
            record.setEarnings(new BigDecimal(0.00));*//*
            record.setDownload(0L);
           if(divideIntoPOJO==null){
               continue;
           }
        *//*    if(divideIntoPOJO.getCosts() !=null){
                record.setCosts(divideIntoPOJO.getCosts());
            }*//*

            if(divideIntoPOJO.getDownload() !=null){
                record.setDownload(divideIntoPOJO.getDownload());
            }
            *//*if(divideIntoPOJO.getEarnings() !=null){
                record.setEarnings(divideIntoPOJO.getEarnings());
            }*//*
        }*/
        return R.success(iPage);
    }

    @PostMapping("/shareRecommend")
    @ApiOperation("推荐官数据")
    @Log(title = "推荐官数据", businessType = BusinessType.SELECT)
    public R<TRecommendShare> shareRecommend(@RequestBody ShareQuery shareQuery){
        IPage<TRecommendShare> iPage= tRecommendService.shareRecommend(shareQuery,companyMetadata.userid());
        List<TRecommendShare> records = iPage.getRecords();
       // List<TRecommendShare> records1 = new ArrayList<>(records.size());
        for (TRecommendShare record : records) {
            Object o = redisUtils.get("loginTime:" + record.getId());
            if(! StringUtils.isEmpty(o)){
                record.setLoginTime(o);
            }
            long incr = redisUtils.incr("loginTimeSum:" + record.getId(), 1);
            record.setLoginTimeSum(incr);

            // records1.add(record);
        }
        iPage.setRecords(records);
/*        for (JobHunterShare record : records) {
            DivideIntoPOJO divideIntoPOJO = orderInfoApi.divideInto(record.getUserId(), companyMetadata.userid(),2);
            record.setCosts(new BigDecimal(0.00));
            record.setEarnings(new BigDecimal(0.00));
            record.setDownload(0L);
            if(divideIntoPOJO==null){
                continue;
            }
            if(divideIntoPOJO.getCosts() !=null){
                record.setCosts(divideIntoPOJO.getCosts());
            }

            if(divideIntoPOJO.getDownload() !=null){
                record.setDownload(divideIntoPOJO.getDownload());
            }
            if(divideIntoPOJO.getEarnings() !=null){
                record.setEarnings(divideIntoPOJO.getEarnings());
            }
        }*/
        return R.success(iPage);
    }
    @PostMapping("/shareTCompany")
    @ApiOperation("邀请企业的数据")
    @Log(title = "邀请企业的数据", businessType = BusinessType.SELECT)
    public R<TCompanySharePO> shareTCompany(@RequestBody ShareQuery shareQuery){
        IPage<TCompanySharePO> iPage= tCompanyService.shareTCompany(shareQuery,companyMetadata.userid());
        List<TCompanySharePO> records = iPage.getRecords();
        //List<TCompanySharePO> records1 = new ArrayList<>(records.size());
        for (TCompanySharePO record : records) {
            Object o = redisUtils.get("loginTime:" + record.getCompanyId());
            if(! StringUtils.isEmpty(o)){
                record.setLoginTime(o);
            }
            long incr = redisUtils.incr("loginTimeSum:" + record.getCompanyId(), 1);
            record.setLoginTimeSum(incr);
           // records1.add(record);
        }
        iPage.setRecords(records);
/*        List<JobHunterShare> records = iPage.getRecords();
        for (JobHunterShare record : records) {
            DivideIntoPOJO divideIntoPOJO = orderInfoApi.divideInto(record.getUserId(), companyMetadata.userid());
            record.setCosts(new BigDecimal(0.00));
            record.setEarnings(new BigDecimal(0.00));
            record.setDownload(0L);
            if(divideIntoPOJO==null){
                continue;
            }
            if(divideIntoPOJO.getCosts() !=null){
                record.setCosts(divideIntoPOJO.getCosts());
            }

            if(divideIntoPOJO.getDownload() !=null){
                record.setDownload(divideIntoPOJO.getDownload());
            }
            if(divideIntoPOJO.getEarnings() !=null){
                record.setEarnings(divideIntoPOJO.getEarnings());
            }
        }*/
        return R.success(iPage);
    }


    @GetMapping("/shareData")
    @ApiOperation("分享是记录")
    public  R<ShareDateDTO> shareData(){
        ShareDateDTO shareDateDTO=new ShareDateDTO();
      List<Map> list= sourceOfReturnsService.shareData(companyMetadata.userid());
        if(list.size()>0){
            for (Map map : list) {
                int identity = (int) map.get("identity");
                if(identity==0){
                    shareDateDTO.setCompanyEarnings( map.get("earnings").toString());
                }else if(identity==1){
                    shareDateDTO.setRecommendEarnings( map.get("earnings").toString());
                }else if(identity==2){
                    shareDateDTO.setJobHunterEarnings( map.get("earnings").toString());
                }
            }
        }
        Share byId = shareService.getById(companyMetadata.userid());
        if(byId !=null){
            shareDateDTO.setJobHunterShare(byId.getJobHunterAmount());
            shareDateDTO.setCompanyShare(byId.getCompanyAmout());
            shareDateDTO.setRecommendShare(byId.getRecommendAmount());
        }
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("correlation_type",0);
        queryWrapper.eq("recommend_id",companyMetadata.userid());
        int count = referrerRecordService.count(queryWrapper);
        shareDateDTO.setJobHunterRegister(count);

        QueryWrapper queryWrapper1=new QueryWrapper();
        queryWrapper1.eq("share_person",companyMetadata.userid());
        int count1 = tCompanyService.count(queryWrapper1);
        shareDateDTO.setCompanyRegister(count1);
        QueryWrapper queryWrapper2=new QueryWrapper();
        queryWrapper2.eq("share_person",companyMetadata.userid());
        int count2 = tRecommendService.count(queryWrapper2);
        shareDateDTO.setRecommendRegister(count2);

        shareDateDTO.setTodayProfit( sourceOfReturnsService.todayProfit(companyMetadata.userid()));
        shareDateDTO.setMonthProfit( sourceOfReturnsService.setMonthProfit(companyMetadata.userid()));
        shareDateDTO.setSumProfit( sourceOfReturnsService.setSumProfit(companyMetadata.userid()));
        
        return R.success(shareDateDTO);
    }

    @GetMapping("/graph")
    @ApiOperation("入驻数量增长")
    @ApiImplicitParam(name = "year", required = false, value = "年份(传年份如（2023，2022）)")
    public R<GraphDTO> graph(@RequestParam(value = "year",required = false) String year){
        if(StringUtils.isEmpty(year)){
            year=DateUtils.getCurrentDateStr();
        }else {
            year=year+"-01-01";
        }
        GraphDTO dto=new GraphDTO();
        dto.setJobHunter(referrerRecordService.graph(companyMetadata.userid(),year));
        dto.setCompany(tCompanyService.graph(companyMetadata.userid(),year));
        dto.setRecommend(tRecommendService.graph(companyMetadata.userid(),year));
        return R.success(dto);
    }

    @GetMapping("/selectCount")
    @ApiOperation("入驻数量增长总数")
    @Log(title = "入驻数量增长总数", businessType = BusinessType.SELECT)
    @ApiImplicitParam(name ="id", value = "推荐官id")
    public R<OperationalDataDTO> selectCount(@RequestParam("id") String id){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("recommend_id",id);
        int count = promotionPostService.count(queryWrapper);
        OperationalDataDTO dto=new OperationalDataDTO();


        dto.setPostCount(count);
        QueryWrapper queryWrapperJob=new QueryWrapper();
        queryWrapperJob.eq("share_person",id);
     //  queryWrapperJob.eq("perfect",1);
        int count1 = tJobhunterService.count(queryWrapperJob);
        dto.setJobHunterCount(count1);

        QueryWrapper queryWrapperJob1=new QueryWrapper();
        queryWrapperJob1.eq("share_person",id);
        queryWrapperJob1.between("create_time", LocalDateTimeUtils.yesterdayStartTime(),LocalDateTimeUtils.yesterdayEndTime());
        dto.setJobHunterCountYesterday(tJobhunterService.count(queryWrapperJob1));

        QueryWrapper queryWrapperJob11=new QueryWrapper();
        queryWrapperJob11.eq("share_person",id);
        queryWrapperJob11.between("create_time", LocalDateTimeUtils.todayStartTime(),LocalDateTimeUtils.todayEndTime());
        dto.setJobHunterCountDay(tJobhunterService.count(queryWrapperJob11));

        QueryWrapper socialRecommend=new QueryWrapper();
        socialRecommend.eq("share_person",id);
        socialRecommend.eq("identity",1);
        int socialRecommendCount = tRecommendService.count(socialRecommend);
        dto.setSocialRecommend(socialRecommendCount);
        QueryWrapper schoolRecommend=new QueryWrapper();
        schoolRecommend.eq("share_person",id);
        schoolRecommend.eq("identity",0);
        int schoolRecommendCount = tRecommendService.count(schoolRecommend);
        dto.setSchoolRecommend(schoolRecommendCount);


        QueryWrapper socialRecommend1=new QueryWrapper();
        socialRecommend1.eq("share_person",id);
        socialRecommend1.between("registrant_time", LocalDateTimeUtils.yesterdayStartTime(),LocalDateTimeUtils.yesterdayEndTime());
        dto.setSchoolRecommendYesterday(tRecommendService.count(socialRecommend1));

        QueryWrapper socialRecommend11=new QueryWrapper();
        socialRecommend11.eq("share_person",id);
        socialRecommend11.between("registrant_time", LocalDateTimeUtils.todayStartTime(),LocalDateTimeUtils.todayEndTime());
        dto.setSchoolRecommendDay(tRecommendService.count(socialRecommend11));






        QueryWrapper ordinaryCompany=new QueryWrapper();
        ordinaryCompany.eq("share_person",id);
        ordinaryCompany.eq("company_type",0);
        int ordinaryCompanyCount = tCompanyService.count(ordinaryCompany);
        dto.setOrdinaryCompany(ordinaryCompanyCount);



        QueryWrapper headhunterCompany=new QueryWrapper();
        headhunterCompany.eq("share_person",id);
        headhunterCompany.eq("company_type",1);
        int headhunterCompanyCount = tCompanyService.count(headhunterCompany);
        dto.setHeadhunterCompany(headhunterCompanyCount);

        QueryWrapper queryWrapper1=new QueryWrapper();
        queryWrapper1.eq("share_person",id);
        queryWrapper1.between("create_time", LocalDateTimeUtils.yesterdayStartTime(),LocalDateTimeUtils.yesterdayEndTime());
        dto.setOrdinaryCompanyYesterday(tCompanyService.count(queryWrapper1));
        QueryWrapper queryWrapper11=new QueryWrapper();
        queryWrapper11.eq("share_person",id);
        queryWrapper11.between("create_time", LocalDateTimeUtils.todayStartTime(),LocalDateTimeUtils.todayEndTime());
        dto.setOrdinaryCompanyDay(tCompanyService.count(queryWrapper11));
    //查询所投的职位
        List<Map<String, Object>> mapList=  putInResumeService.putCount(id);
        if(mapList !=null && mapList.size()>0){
            for (Map<String, Object> map : mapList) {
                int post_type = Integer.parseInt(map.get("post_type").toString());
                Long sums= Long.valueOf(map.get("sums").toString());
                if( post_type==1){

                    dto.setEntryPay(sums);
                    continue;
                }
                if(post_type==2){
                    dto.setFullMoonPay(sums);
                    continue;
                }
                if(post_type ==3){
                    dto.setInterviewPay(sums);
                    continue;
                }
                if(post_type ==4){
                   dto.setSchoolResumePay(sums);
                    continue;
                }
                if(post_type ==5){
                    dto.setResumePay(sums);
                    continue;
                }
            }
        }
        return R.success(dto);
    }

    @GetMapping("/selectCount1")
    @ApiOperation("入驻数量增长总数新")
    @Log(title = "入驻数量增长总数新", businessType = BusinessType.SELECT)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "推荐官id", required = true),
            @ApiImplicitParam(name = "beginDate", value = "开始时间", required = true),
            @ApiImplicitParam(name = "endDate", value = "结束时间", required = true),
            @ApiImplicitParam(name = "endDate", value = "结束时间", required = true),
            @ApiImplicitParam(name = "timeType", value = "时间类型(0:天 1周 2月)", required = true),
            @ApiImplicitParam(name = "city", value = "市", required = false)
    })
    public R<OperationalDataDTO> selectCount1(@RequestParam("id") String id,@RequestParam("timeType") int timeType,@RequestParam("beginDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate beginDate,@RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,@RequestParam(value = "city",required = false)String city){

        LocalDateTime  currentStartDate=null;
        LocalDateTime currentEndTime =null;
        currentStartDate= LocalDateTime.of(beginDate, LocalTime.MIN);
        currentEndTime=  LocalDateTime.of(endDate, LocalTime.MAX);
        OperationalDataDTO dto=new OperationalDataDTO();
        int count =  promotionPostService.selectCount(id,currentStartDate,currentEndTime,city);
        dto.setPostCount(count);
        int countJobHunter =   tJobhunterService.selectCount(id,currentStartDate,currentEndTime,city);
        dto.setJobHunterCount(countJobHunter);
;
        int countRecommend=  tRecommendService.selectCount(id,currentStartDate,currentEndTime,city,0);
        dto.setSocialRecommend(countRecommend);

        int countRecommend1=  tRecommendService.selectCount(id,currentStartDate,currentEndTime,city,1);
        dto.setSchoolRecommend(countRecommend1);


        int ordinaryCompanyCount=   tCompanyService.selectCount(id,currentStartDate,currentEndTime,city,0);
        dto.setOrdinaryCompany(ordinaryCompanyCount);

        int ordinaryCompanyCount1=   tCompanyService.selectCount(id,currentStartDate,currentEndTime,city,1);
        dto.setHeadhunterCompany(ordinaryCompanyCount1);

        LocalDateTime  yesterdaysStartTime=null;
        LocalDateTime  yesterdaysEndTime  =null;
        if(timeType==0){
            yesterdaysStartTime= LocalDateTimeUtils.yesterdayStartTime(beginDate);
            yesterdaysEndTime=  LocalDateTimeUtils.yesterdayEndTime(endDate);
        }else if(timeType==1){
            yesterdaysStartTime=LocalDateTime.of(LocalDateTimeUtils.lastWeekStartTime(beginDate), LocalTime.MIN) ;
            yesterdaysEndTime= LocalDateTime.of(LocalDateTimeUtils.lastWeekEndTime(beginDate), LocalTime.MAX) ;
        }else if(timeType==2){
            yesterdaysStartTime=LocalDateTime.of(LocalDateTimeUtils.lastMonthStartTime(beginDate), LocalTime.MIN) ;
            yesterdaysEndTime= LocalDateTime.of(LocalDateTimeUtils.lastMonthEndTime(beginDate), LocalTime.MAX) ;
        }

        OperationalDataDTO dto1=new OperationalDataDTO();
        dto1.setPostCount(promotionPostService.selectCount(id,yesterdaysStartTime,yesterdaysEndTime,city));
        dto1.setJobHunterCount( tJobhunterService.selectCount(id,yesterdaysStartTime,yesterdaysEndTime,city));
        dto1.setSocialRecommend( tRecommendService.selectCount(id,yesterdaysStartTime,yesterdaysEndTime,city,1));
        dto1.setSchoolRecommend( tRecommendService.selectCount(id,yesterdaysStartTime,yesterdaysEndTime,city,0));
        dto1.setOrdinaryCompany(tCompanyService.selectCount(id,yesterdaysStartTime,yesterdaysEndTime,city,0));
        dto1.setHeadhunterCompany(tCompanyService.selectCount(id,yesterdaysStartTime,yesterdaysEndTime,city,1));



    /*    LocalDateTime  todayStartTime=LocalDateTimeUtils.todayStartTime();
        LocalDateTime  todayEndTime  =LocalDateTimeUtils.todayEndTime();
        OperationalDataDTO dto2=new OperationalDataDTO();
        dto2.setPostCount(promotionPostService.selectCount(id,todayStartTime,todayEndTime,city));
        dto2.setJobHunterCount( tJobhunterService.selectCount(id,todayStartTime,todayEndTime,city));
        dto2.setSocialRecommend( tRecommendService.selectCount(id,todayStartTime,todayEndTime,city,1));
        dto2.setSchoolRecommend( tRecommendService.selectCount(id,todayStartTime,todayEndTime,city,0));
        dto2.setOrdinaryCompany(tCompanyService.selectCount(id,todayStartTime,todayEndTime,city,0));
        dto2.setHeadhunterCompany(tCompanyService.selectCount(id,todayStartTime,todayEndTime,city,1));*/



        Map map=new HashMap(3);
       map.put("atPresent", dto);
        map.put("yesterday", dto1);
      //  map.put("today", dto2);
        return R.success(map);
    }

    @GetMapping("/selectRegisterCount")
    @ApiOperation("获取注册量")
    @Log(title = "获取注册量", businessType = BusinessType.SELECT)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "推荐官id", required = true),
            @ApiImplicitParam(name = "beginDate", value = "开始时间", required = true),
            @ApiImplicitParam(name = "endDate", value = "结束时间", required = true),
            @ApiImplicitParam(name = "endDate", value = "结束时间", required = true),
            @ApiImplicitParam(name = "city", value = "市", required = false),
            @ApiImplicitParam(name = "type", value = "类型(0:职位分享 1:求职者 2:猎企 3：企业 4:职场运营官 5：校园运营官) ", required = true)
    })
    public R selectRegisterCount(@RequestParam("id") String id,@RequestParam("beginDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate beginDate,@RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,@RequestParam(value = "city",required = false)String city,@RequestParam(value = "type") int type){
        LocalDateTime  currentStartDate=null;
        LocalDateTime currentEndTime =null;
        currentStartDate= LocalDateTime.of(beginDate, LocalTime.MIN);
        currentEndTime=  LocalDateTime.of(endDate, LocalTime.MAX);
        if(type==0){
            List<Map> mapList= promotionPostService.mapList(id,currentStartDate,currentEndTime,city);
            List<List> tool = tool(mapList,beginDate,endDate);
            return  R.success( tool);
        }else if(type==1){
            List<Map> mapList= tJobhunterService.mapList(id,currentStartDate,currentEndTime,city);
            List<List> tool = tool(mapList,beginDate,endDate);
            return  R.success( tool);
        }else if(type==4){
            List<Map> mapList= tRecommendService.mapList(id,currentStartDate,currentEndTime,city,1);
            List<List> tool = tool(mapList,beginDate,endDate);
            return  R.success( tool);
        }else if(type==5){
            List<Map> mapList= tRecommendService.mapList(id,currentStartDate,currentEndTime,city,0);
            List<List> tool = tool(mapList,beginDate,endDate);
            return  R.success( tool);
        }else if(type==2){
            List<Map> mapList= tCompanyService.mapList(id,currentStartDate,currentEndTime,city,1);
            List<List> tool = tool(mapList,beginDate,endDate);
            return  R.success( tool);
        }else if(type==3){
            List<Map> mapList= tCompanyService.mapList(id,currentStartDate,currentEndTime,city,0);
            List<List> tool = tool(mapList,beginDate,endDate);
            return  R.success( tool);
        }
        return R.success();
    }

    public List<List> tool(List<Map> mapList,LocalDate beginDat,LocalDate endDate){
        long daysBetween = ChronoUnit.DAYS.between(beginDat, endDate);
        List<List> list=new ArrayList<>((int) daysBetween);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (int a = 0; a <= daysBetween; a++) {
            LocalDate localDate = beginDat.plusDays(a);
            List list1=new ArrayList(2);
            String formattedDate = localDate.format(formatter);
            list1.add(formattedDate);
            list1.add(0);
            for (Map map : mapList) {
               if( map.get("time").equals(formattedDate)){
                   list1.set(1,map.get("counts"));
               }

            }
            list.add(list1);
        }
        return list;
    }

}
