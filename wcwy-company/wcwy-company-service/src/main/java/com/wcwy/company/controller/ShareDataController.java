package com.wcwy.company.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wcwy.common.base.result.R;
import com.wcwy.common.base.utils.CalculateProportionUtil;
import com.wcwy.common.base.utils.DateUtils;
import com.wcwy.common.base.utils.IpUtils;
import com.wcwy.common.base.utils.LocalDateTimeUtils;
import com.wcwy.common.redis.enums.InvitationCode;
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.company.asyn.RegisterAsync;
import com.wcwy.company.asyn.ShareDataAsync;
import com.wcwy.company.dto.ShareDataDTO;
import com.wcwy.company.entity.PutInResume;
import com.wcwy.company.entity.ShareData;
import com.wcwy.company.po.PutPostShareDataPO;
import com.wcwy.company.query.PostDataQuery;
import com.wcwy.company.query.ShareDataQuery;
import com.wcwy.company.service.PutInResumeRecordService;
import com.wcwy.company.service.PutInResumeService;
import com.wcwy.company.service.ShareDataService;
import com.wcwy.company.session.CompanyMetadata;
import com.wcwy.post.api.OrderReceivingApi;
import com.wcwy.system.annotation.Log;
import com.wcwy.system.enums.BusinessType;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

/**
 * ClassName: ShareDataController
 * Description:
 * date: 2023/8/25 10:16
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Api(tags = "分享运营数据")
@RestController
@RequestMapping("/shareData")
public class ShareDataController {

    @Resource
    private ShareDataService shareDataService;

    @Resource
    private RedisUtils redisUtils;
    @Resource
    private ShareDataAsync shareDataAsync;
    @Autowired
    private CompanyMetadata companyMetadata;

    @Autowired
    private PutInResumeService putInResumeService;
    @Autowired
    private OrderReceivingApi orderReceivingApi;
    @Resource
    private RegisterAsync registerAsync;

    @GetMapping("/sharingRecommend")
    @ApiOperation("添加推荐官分享访问记录")
    @Log(title = "添加推荐官分享访问记录", businessType = BusinessType.INSERT)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "QRCode", required = true, value = "邀请码"),
            @ApiImplicitParam(name = "type", required = true, value = "0职位分享 1求职者 2猎企 3企业 4职场推荐官 5校园推荐官"),
            @ApiImplicitParam(name = "second", required = true, value = "访问时间(秒)")
    })
    public String sharingRecommend(HttpServletRequest request, @RequestParam("type") Integer type, @RequestParam("QRCode") String QRCode, @RequestParam("second") Long second) {
        String ipAddr = IpUtils.getIpAddr(request);

        if (!StringUtils.isEmpty(QRCode)) {
            Object o = redisUtils.get(InvitationCode.INVITATION_URL_CODE.getType() + QRCode);
            if (StringUtils.isEmpty(o)) {
                return ipAddr;
            }
            String substring = o.toString().substring(0, 2);
            if (substring.equals("TR")) {
                shareDataAsync.postSharingRecommend(ipAddr, type, second, o.toString());
            }
        }
        return ipAddr;
    }

    /**
     * 类型获取曲线图selectType
     */
/*    @ApiOperation("测试")
    @GetMapping("/selectType")
    public R selectType(){

    }*/


    @GetMapping("/shareDataDTOR1")
    @ApiOperation("人才推荐官数据概览")
    @Log(title = "人才推荐官数据概览", businessType = BusinessType.SELECT)
    @ApiImplicitParams({

            @ApiImplicitParam(name = "id", required = true, value = "推荐官id"),
            @ApiImplicitParam(name = "beginDate", required = true, value = "开始时间"),
            @ApiImplicitParam(name = "endDate", required = true, value = "结束时间"),
            @ApiImplicitParam(name = "city", required = false, value = "地区"),
            @ApiImplicitParam(name = "timeType", value = "时间类型(0:天 1周 2月)", required = true),
            @ApiImplicitParam(name = "type", required = true, value = "职位详情(1:入职付 2:满月付 3:到面付 4:简历付)")
    })
    public R<ShareDataDTO> shareDataDTOR1(@RequestParam("id") String id,@RequestParam("timeType") int timeType, @RequestParam("beginDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate beginDate, @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate, @RequestParam(value = "city",required = false)String city, @RequestParam(value = "type") int type) throws ParseException {
        LocalDateTime  currentStartDate=null;
        LocalDateTime currentEndTime =null;
        currentStartDate= LocalDateTime.of(beginDate, LocalTime.MIN);
        currentEndTime=  LocalDateTime.of(endDate, LocalTime.MAX);
        ShareDataDTO shareDataDTO=new ShareDataDTO();
        Map<String, List<PutPostShareDataPO>> map2 = orderReceivingApi.orderReceivingWeek(id,beginDate,endDate,city, type);
        List<PutPostShareDataPO> putPostShareDataPOS1 = map2.get("A");
        Map<String, Long> map3 = timelyStatistics(putPostShareDataPOS1);
        Long counts = map3.get("counts");
        shareDataDTO.setOrderTakingSUM(counts);
        shareDataDTO.setSlipSUM( putInResumeService.selectCounts(id,currentStartDate,currentEndTime,city,type,0));
        shareDataDTO.setBrowseSUM( putInResumeService.selectCounts(id,currentStartDate,currentEndTime,city,type,1));


/*        LocalDateTime  todayStartTime=LocalDateTimeUtils.todayStartTime();
        LocalDateTime  todayEndTime  =LocalDateTimeUtils.todayEndTime();


        ShareDataDTO shareDataDTO1=new ShareDataDTO();
        Map<String, List<PutPostShareDataPO>> map4 = orderReceivingApi.orderReceivingDay(id,LocalDate.now(),city, type);
        List<PutPostShareDataPO> putPostShareDataPOS4 = map4.get("A");
        Map<String, Long> map5 = timelyStatistics(putPostShareDataPOS4);
        Long counts4 = map5.get("counts");
        shareDataDTO1.setOrderTakingSUM(counts4);
        shareDataDTO1.setSlipSUM(putInResumeService.selectCounts(id,currentStartDate,currentEndTime,city,type,0));
        shareDataDTO1.setBrowseSUM(putInResumeService.selectCounts(id,todayStartTime,todayEndTime,city,type,1));*/



        LocalDateTime  yesterdaysStartTime=LocalDateTimeUtils.yesterdayStartTime();
        LocalDateTime  yesterdaysEndTime  =LocalDateTimeUtils.yesterdayEndTime();

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

        ShareDataDTO shareDataDTO2=new ShareDataDTO();
        List<PutPostShareDataPO> putPostShareDataPOS2 = map2.get("B");
        Map<String, Long> map6 = timelyStatistics(putPostShareDataPOS2);
        Long counts2 = map6.get("counts");
        shareDataDTO2.setOrderTakingSUM(counts2);
        shareDataDTO2.setSlipSUM(putInResumeService.selectCounts(id,yesterdaysStartTime,yesterdaysEndTime,city,type,0));
        shareDataDTO2.setBrowseSUM(putInResumeService.selectCounts(id,yesterdaysStartTime,yesterdaysEndTime,city,type,1));

        //shareDataDTO.setOrderTakingSUM();

        if(type==4){
            shareDataDTO.setDownloadSUM(putInResumeService.selectCounts(id,currentStartDate,currentEndTime,city,type,2));
            //shareDataDTO1.setDownloadSUM(putInResumeService.selectCounts(id,todayStartTime,todayEndTime,city,type,2));
            shareDataDTO2.setDownloadSUM(putInResumeService.selectCounts(id,yesterdaysStartTime,yesterdaysEndTime,city,type,2));
        }
        if (type==3 || type==1 || type==2){
            shareDataDTO.setInterviewSUM(putInResumeService.selectCounts(id,currentStartDate,currentEndTime,city,type,3));
            //shareDataDTO1.setInterviewSUM(putInResumeService.selectCounts(id,todayStartTime,todayEndTime,city,type,3));
            shareDataDTO2.setInterviewSUM(putInResumeService.selectCounts(id,yesterdaysStartTime,yesterdaysEndTime,city,type,3));
        }
        if (type==1 || type==2){
            shareDataDTO.setOfferSUM(putInResumeService.selectCounts(id,currentStartDate,currentEndTime,city,type,5));
           // shareDataDTO1.setOfferSUM(putInResumeService.selectCounts(id,todayStartTime,todayEndTime,city,type,5));
            shareDataDTO2.setOfferSUM(putInResumeService.selectCounts(id,yesterdaysStartTime,yesterdaysEndTime,city,type,5));

            shareDataDTO.setEntrySUM(putInResumeService.selectCounts(id,currentStartDate,currentEndTime,city,type,6));
           // shareDataDTO1.setEntrySUM(putInResumeService.selectCounts(id,todayStartTime,todayEndTime,city,type,6));
            shareDataDTO2.setEntrySUM(putInResumeService.selectCounts(id,yesterdaysStartTime,yesterdaysEndTime,city,type,6));
        }
        if (type==2){
            shareDataDTO.setFullMoonSUM(putInResumeService.selectCounts(id,currentStartDate,currentEndTime,city,type,7));
            //shareDataDTO1.setFullMoonSUM(putInResumeService.selectCounts(id,todayStartTime,todayEndTime,city,type,7));
            shareDataDTO2.setFullMoonSUM(putInResumeService.selectCounts(id,yesterdaysStartTime,yesterdaysEndTime,city,type,7));
        }
        Map map=new HashMap(3);
        map.put("atPresent", shareDataDTO);
        map.put("yesterday", shareDataDTO2);
      //  map.put("today", shareDataDTO1);
        return R.success(map);

    }

    @GetMapping("/shareDataDay1")
    @ApiOperation("人才推荐官数据概览按天查询")
    @Log(title = "人才推荐官数据概览按天查询", businessType = BusinessType.SELECT)
    @ApiImplicitParams({

            @ApiImplicitParam(name = "id", required = true, value = "推荐官id"),
            @ApiImplicitParam(name = "beginDate", required = true, value = "开始时间"),
            @ApiImplicitParam(name = "endDate", required = true, value = "结束时间"),
            @ApiImplicitParam(name = "city", required = false, value = "地区"),
            @ApiImplicitParam(name = "type", required = true, value = "职位详情(1:入职付 2:满月付 3:到面付 4:简历付)"),
            @ApiImplicitParam(name = "screen", required = true, value = "筛选(1接单 2:推荐3：浏览 4:下载  5面试 6:offer 7：入职 8:满月)")
    })
    public R shareDataDay1(@RequestParam("id") String id, @RequestParam("beginDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate beginDate, @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate, @RequestParam(value = "city",required = false)String city, @RequestParam(value = "type") int type, @RequestParam(value = "screen") int screen) throws ParseException {
        LocalDateTime  currentStartDate=null;
        LocalDateTime currentEndTime =null;
        currentStartDate= LocalDateTime.of(beginDate, LocalTime.MIN);
        currentEndTime=  LocalDateTime.of(endDate, LocalTime.MAX);
       if(screen==1){
           Map<String, List<PutPostShareDataPO>> map2 = orderReceivingApi.orderReceivingWeek(id,beginDate,endDate,city, type);
           List<PutPostShareDataPO> putPostShareDataPOS1 = map2.get("A");
           List<List> list = tool1(putPostShareDataPOS1,beginDate,endDate);
           return R.success(list);
       }else if(screen==2){
           List<Map> mapList=  putInResumeService.mapListCount(id,currentStartDate,currentEndTime,city,type,0);
           List<List> tool = tool(mapList, beginDate, endDate);
           return R.success(tool);
       }else if(screen==3){
           List<Map> mapList=  putInResumeService.mapListCount(id,currentStartDate,currentEndTime,city,type,1);
           List<List> tool = tool(mapList, beginDate, endDate);
           return R.success(tool);
       }else if(screen==4){
           List<Map> mapList=  putInResumeService.mapListCount(id,currentStartDate,currentEndTime,city,type,2);
           List<List> tool = tool(mapList, beginDate, endDate);
           return R.success(tool);
       }
       else if(screen==5){
           List<Map> mapList=  putInResumeService.mapListCount(id,currentStartDate,currentEndTime,city,type,3);
           List<List> tool = tool(mapList, beginDate, endDate);
           return R.success(tool);
       }
       else if(screen==6){
           List<Map> mapList=  putInResumeService.mapListCount(id,currentStartDate,currentEndTime,city,type,4);
           List<List> tool = tool(mapList, beginDate, endDate);
           return R.success(tool);
       }
       else if(screen==7){
           List<Map> mapList=  putInResumeService.mapListCount(id,currentStartDate,currentEndTime,city,type,5);
           List<List> tool = tool(mapList, beginDate, endDate);
           return R.success(tool);
       }
       else if(screen==8){
           List<Map> mapList=  putInResumeService.mapListCount(id,currentStartDate,currentEndTime,city,type,6);
           List<List> tool = tool(mapList, beginDate, endDate);
           return R.success(tool);
       }
        return R.success();

    }
    public List<List> tool1(List<PutPostShareDataPO> putPostShareDataPOS,LocalDate beginDat,LocalDate endDate){
        long daysBetween = ChronoUnit.DAYS.between(beginDat, endDate);
        List<List> list=new ArrayList<>((int) daysBetween);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (int a = 0; a <= daysBetween; a++) {
            LocalDate localDate = beginDat.plusDays(a);
            List list1=new ArrayList(2);
            String formattedDate = localDate.format(formatter);
            list1.add(formattedDate);
            list1.add(0);
            for (PutPostShareDataPO putPostShareDataPO : putPostShareDataPOS) {
                String format = putPostShareDataPO.getTime().format(formatter);
                if( format.equals(formattedDate)){
                    list1.set(1,putPostShareDataPO.getCounts());
                }
            }

            list.add(list1);
        }
        return list;
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
    @GetMapping("/test")
    @ApiOperation("测试")
    public void test() {

        String ipAddr = "14.219.241.123";
        String user = "TR2303171351261-2";
        registerAsync.register(ipAddr, 1, user);
        // cc(ipAddr, user);
        // cc(ipAddr,"TR2303171351261-21");
        // shareDataAsync.shareDataPost("广东省", "东莞市", user);
        //shareDataAsync.shareDataPost("广东省", "东莞市", user);

    /*    String ipAddr1="59.39.156.238";
        cc(ipAddr1,user);
        cc(ipAddr1,"TR2303171351261-21");
        cc("223.73.63.180",user);
        cc("223.73.63.180","TR2303171351261-21");*/
    }

    public void cc(String ipAddr, String user) {
        shareDataAsync.postSharingRecommend(ipAddr, 0, 20L, user);
        shareDataAsync.postSharingRecommend(ipAddr, 1, 20L, user);
        shareDataAsync.postSharingRecommend(ipAddr, 2, 20L, user);
        shareDataAsync.postSharingRecommend(ipAddr, 3, 20L, user);
        shareDataAsync.postSharingRecommend(ipAddr, 4, 20L, user);
        shareDataAsync.postSharingRecommend(ipAddr, 5, 20L, user);
        registerAsync.register(ipAddr, 0, user);
        registerAsync.register(ipAddr, 1, user);
        registerAsync.register(ipAddr, 2, user);
        registerAsync.register(ipAddr, 3, user);
        registerAsync.register(ipAddr, 4, user);
        registerAsync.register(ipAddr, 5, user);
        shareDataAsync.shareDataPost("广东省", "东莞市", user);
        shareDataAsync.shareDataDownload("PS2211281730060-11", user);
        shareDataAsync.shareDataDownload("PS2211290827404-1", user);
        shareDataAsync.shareDataDownload("PS2211290851724-2", user);
        shareDataAsync.shareDataDownload("PS2211290858035-3", user);
        shareDataAsync.shareDataDownload("PS2211290934146-4", user);
    }


   /* @PostMapping("/postData")
    @ApiOperation("职位推广数据运营查询")
    @Log(title = "职位推广数据运营查询", businessType = BusinessType.SELECT)
    public R<ShareDataDTO> postData(@Valid @RequestBody PostDataQuery postDataQuery ) throws ParseException {

    }*/


    @PostMapping("/select")
    @ApiOperation("数据运营查询")
    @Log(title = "数据运营查询", businessType = BusinessType.SELECT)
    public R<ShareDataDTO> shareDataDTOR(@Valid @RequestBody ShareDataQuery shareDataQuery) throws ParseException {
        /*String userid = companyMetadata.userid();
        shareDataQuery.setUserId(userid);*/
        ShareDataDTO shareDataDTO = new ShareDataDTO();
        if (shareDataQuery.getPageNo() == null || shareDataQuery.getPageNo() == 0) {
            shareDataQuery.setPageNo(1L);
        }
        shareDataDTO.setCurrent(shareDataQuery.getPageNo());

        //全部选项
        if (shareDataQuery.getAllBeginDate() != null && shareDataQuery.getAllEndDate() != null) {

               /* SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date parse11 = dateFormat.parse("2023-10-31 24:00:00");*/
            long between = ChronoUnit.DAYS.between(shareDataQuery.getAllBeginDate(), shareDataQuery.getAllEndDate()) + 1;
            long between1 = ChronoUnit.MONTHS.between(shareDataQuery.getAllBeginDate(), shareDataQuery.getAllEndDate()) + 1;
            System.out.println(between1);

            if (between < 7) {
                shareDataDTO.setTotal(between);
                LocalDate allBeginDate = shareDataQuery.getAllBeginDate();
                LocalDate date = allBeginDate.plusDays(shareDataQuery.getPageNo() - 1);
                shareDataQuery.setDay(date);
                shareDataQuery.setPostType(0);
            } else if (between >= 7 && between < 30) {
                double ceil = Math.ceil(between / (double) 7);
                shareDataDTO.setTotal((long) ceil);
                if (shareDataQuery.getPageNo() > between) {
                    return R.fail("分页数据大于筛选数据");
                }

                Long pageNo = shareDataQuery.getPageNo();
                int a = (int) (pageNo - 1);
                shareDataQuery.setBeginDate(shareDataQuery.getAllBeginDate().plusDays(7 * a));
                LocalDate date = shareDataQuery.getBeginDate().plusDays(6);
                if (date.isBefore(shareDataQuery.getAllEndDate())) {
                    shareDataQuery.setEndDate(date);
                } else {
                    shareDataQuery.setEndDate(shareDataQuery.getAllEndDate());
                }

                List<ShareData> week = shareDataService.week(shareDataQuery);
                Map<String, Map<String, Long>> mapMap = weekUtils(week, shareDataQuery.getBeginDate());
                shareDataDTO.setAtPresent(mapMap);
                shareDataQuery.setDateType(4);
            } else if (between1 < 6 && between >= 30) {
                LocalDate localDate = LocalDateTimeUtils.weekStartTime(shareDataQuery.getAllBeginDate());
                LocalDateTime localDateTime = LocalDateTimeUtils.weekEndTime(shareDataQuery.getAllEndDate());
                long between2 = ChronoUnit.WEEKS.between(localDate, localDateTime) + 1;
                // double ceil = Math.ceil(between2 / (double) 6);
                shareDataDTO.setTotal(between2);
                if (shareDataQuery.getPageNo() > between) {
                    return R.fail("分页数据大于筛选数据");
                }

                Long pageNo = shareDataQuery.getPageNo();
                int a = (int) (pageNo - 1);

                shareDataQuery.setBeginDate(LocalDateTimeUtils.nextWeekStartTime(shareDataQuery.getAllBeginDate(), 6 * a));
                LocalDate date = LocalDateTimeUtils.nextWeekEndTime(shareDataQuery.getBeginDate(), 6);
                if (date.isBefore(shareDataQuery.getAllEndDate())) {
                    shareDataQuery.setEndDate(date);
                } else {
                    shareDataQuery.setEndDate(shareDataQuery.getAllEndDate());
                }


                List<ShareData> week = shareDataService.week(shareDataQuery);
                Map<String, Map<String, Long>> mapMap = monthUtils1(week, shareDataQuery.getBeginDate());
                shareDataDTO.setAtPresent(mapMap);
                shareDataQuery.setDateType(4);
            } else if (between1 >= 6) {
                double ceil = Math.ceil(between1 / (double) 6);
                shareDataDTO.setTotal((long) ceil);
                if (shareDataQuery.getPageNo() > between1) {
                    return R.fail("分页数据大于筛选数据");
                }

                Long pageNo = shareDataQuery.getPageNo();
                int a = (int) (pageNo - 1);
                shareDataQuery.setBeginDate(LocalDateTimeUtils.monthStartTime(shareDataQuery.getAllBeginDate().plusMonths(6 * a)));
                LocalDate date = shareDataQuery.getBeginDate().plusMonths(5);
                if (date.isBefore(shareDataQuery.getAllEndDate())) {
                    shareDataQuery.setEndDate(LocalDateTimeUtils.monthEndTime(date));
                } else {
                    shareDataQuery.setEndDate(shareDataQuery.getAllEndDate());
                }

                List<ShareData> month = shareDataService.month(shareDataQuery);
                Map<String, Map<String, Long>> mapMap = yearUtils(month, shareDataQuery.getBeginDate(), shareDataQuery.getEndDate());
                shareDataDTO.setAtPresent(mapMap);
                shareDataQuery.setDateType(4);
            }


        }


        if (shareDataQuery.getDateType() == 0) {

            //如果不是全部是时间选项则进入当天的选项
            if (shareDataQuery.getDay() == null) {
                return R.fail("请选择日期!");
            }
            List<ShareData> shareData = shareDataService.day(shareDataQuery);
            Map<String, Map<String, Long>> map = hourUtils(shareData);
            shareDataDTO.setAtPresent(map);
            Map<String, Long> map3 = map.get("count");
            shareDataQuery.setDay(shareDataQuery.getDay().plusDays(-1));
            List<ShareData> shareData1 = shareDataService.day(shareDataQuery);
            Map<String, Map<String, Long>> map1 = hourUtils(shareData1);
            shareDataDTO.setSuperior(map1);
            Map<String, Long> map2 = map1.get("count");
            // percentage(shareDataDTO, map3, map2);
        } else if (shareDataQuery.getDateType() == 1) {
    /*        if (shareDataQuery.getAllBeginDate() != null && shareDataQuery.getAllEndDate() != null) {
                LocalDate date1 = LocalDateTimeUtils.getLastMonOfWeek(shareDataQuery.getAllBeginDate(), shareDataQuery.getPageNo() - 1);
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String sunOfWeek = LocalDateTimeUtils.getSunOfWeek(date1.format(dateTimeFormatter));
                LocalDate date = DateUtils.StringToLocalDate(sunOfWeek);
                shareDataQuery.setEndDate(date);
                if (date.isAfter(shareDataQuery.getAllEndDate())) {
                    shareDataQuery.setEndDate(shareDataQuery.getAllEndDate());
                }
                shareDataQuery.setBeginDate(date1);
                //long between = ChronoUnit.WEEKS.between(shareDataQuery.getAllBeginDate(), shareDataQuery.getAllEndDate())+2;
                long between = ChronoUnit.DAYS.between(shareDataQuery.getAllBeginDate(), shareDataQuery.getAllEndDate())+1;
                double ceil = Math.ceil(between / (double) 7);
                shareDataDTO.setTotal((long) ceil);

            }*/
            //如果不是全部是时间选项则进入当天的选项
            if (shareDataQuery.getBeginDate() == null && shareDataQuery.getEndDate() == null) {
                return R.fail("请选择周期!");
            }
            List<ShareData> week = shareDataService.week(shareDataQuery);
            Map<String, Map<String, Long>> mapMap = weekUtils(week, shareDataQuery.getBeginDate());
            shareDataDTO.setAtPresent(mapMap);
            LocalDate beginDate = shareDataQuery.getBeginDate();
            shareDataQuery.setBeginDate(LocalDateTimeUtils.lastWeekStartTime(beginDate));
            shareDataQuery.setEndDate(LocalDateTimeUtils.lastWeekEndTime(beginDate));
            List<ShareData> week1 = shareDataService.week(shareDataQuery);
            Map<String, Map<String, Long>> mapMap1 = weekUtils(week1, shareDataQuery.getBeginDate());
            shareDataDTO.setSuperior(mapMap1);
            //  percentage(shareDataDTO, mapMap.get("count"), mapMap1.get("count"));
        } else if (shareDataQuery.getDateType() == 2) {
           /* if (shareDataQuery.getAllBeginDate() != null && shareDataQuery.getAllEndDate() != null) {
                LocalDate nextWeek = shareDataQuery.getAllBeginDate().plus(shareDataQuery.getPageNo() - 1, ChronoUnit.MONTHS);
                long between = ChronoUnit.MONTHS.between(shareDataQuery.getAllBeginDate(), shareDataQuery.getAllEndDate()) + 1;
                shareDataDTO.setTotal(between);
                shareDataQuery.setMonth(nextWeek);
            }*/
            //如果不是全部是时间选项则进入当天的选项
            if (shareDataQuery.getMonth() == null) {
                return R.fail("请选择月份!");
            }
            shareDataQuery.setBeginDate(LocalDateTimeUtils.monthStartTime(shareDataQuery.getMonth()));
            shareDataQuery.setEndDate(LocalDateTimeUtils.monthEndTime(shareDataQuery.getMonth()));
            List<ShareData> week = shareDataService.week(shareDataQuery);
            Map<String, Map<String, Long>> mapMap = monthUtils(week, DateUtils.LocalDateToString(shareDataQuery.getBeginDate()));
            shareDataDTO.setAtPresent(mapMap);


            shareDataQuery.setBeginDate(LocalDateTimeUtils.lastMonthStartTime(shareDataQuery.getMonth()));
            shareDataQuery.setEndDate(LocalDateTimeUtils.lastMonthEndTime(shareDataQuery.getMonth()));
/*            LocalDate beginDate = shareDataQuery.getBeginDate();
            shareDataQuery.setBeginDate(LocalDateTimeUtils.lastWeekStartTime(beginDate));
            shareDataQuery.setEndDate(LocalDateTimeUtils.lastWeekEndTime(beginDate));*/
            List<ShareData> week1 = shareDataService.week(shareDataQuery);
            Map<String, Map<String, Long>> mapMap1 = monthUtils(week1, DateUtils.LocalDateToString(shareDataQuery.getBeginDate()));
            shareDataDTO.setSuperior(mapMap1);
            // percentage(shareDataDTO, mapMap.get("count"), mapMap1.get("count"));
        }

        if (shareDataQuery.getAllBeginDate() != null && shareDataQuery.getAllEndDate() != null) {
            shareDataQuery.setDay(LocalDate.now());
            List<ShareData> shareData = shareDataService.day(shareDataQuery);
            Map<String, Map<String, Long>> map = hourUtils(shareData);
            // shareDataDTO.setAtPresent(map);
            Map<String, Long> map2 = map.get("count");
            System.out.println(map2);
            shareDataDTO.getAtPresent().put("count", map.get("count"));
            //Map<String, Long> map3 = map.get("count");
            shareDataQuery.setDay(shareDataQuery.getDay().plusDays(-1));
            List<ShareData> shareData1 = shareDataService.day(shareDataQuery);
            Map<String, Map<String, Long>> map1 = hourUtils(shareData1);

            Map<String, Long> put = shareDataDTO.getSuperior().put("count", map1.get("count"));
            // shareDataDTO.setSuperior(put);
            //Map<String, Long> map2 = map1.get("count");
            // percentage(shareDataDTO, map3, map2);
        }
        return R.success(shareDataDTO);
    }

    //月工具类
    public Map<String, Map<String, Long>> monthUtils1(List<ShareData> shareData, LocalDate date) {
        List<Map> scope = LocalDateTimeUtils.getWeek(date, 6);
        Map mapOne = new HashMap(3);
        Map mapTwo = new HashMap(3);
        Map mapThree = new HashMap(3);
        Map mapFour = new HashMap(3);
        Map mapFive = new HashMap(3);
        Map mapSix = new HashMap(3);
        // Map mapSeven = new HashMap(3);
        for (Map map : scope) {
            String order1 = map.get("order").toString();
            Integer order = Integer.parseInt(order1);
            if (order == 0) {
                mapOne = map;
            } else if (order == 1) {
                mapTwo = map;
            } else if (order == 2) {
                mapThree = map;
            } else if (order == 3) {
                mapFour = map;
            } else if (order == 4) {
                mapFive = map;
            } else if (order == 5) {
                mapSix = map;
            } else if (order == 6) {
                //mapSeven = map;
            }
        }
        Map<String, Long> ipAddress = createMap(7);
        Map<String, Long> second = createMap(7);
        Map<String, Long> post = createMap(7);
        Map<String, Long> visit = createMap(7);
        Map<String, Long> register = createMap(7);
        Map<String, Long> download = createMap(7);
        Map<String, Map<String, Long>> mapMap = new HashMap<>(7);
        Map<String, Long> count = new HashMap<>(6);
        Long ipAddressCount = 0L;
        Long secondCount = 0L;
        Long postCount = 0L;
        Long visitCount = 0L;
        Long registerCount = 0L;
        Long downloadCount = 0L;
        for (ShareData shareDatum : shareData) {
            //计算总量
            //ip
            ipAddressCount = ipAddressCount + shareDatum.getIpAddress();
            //访问量
            secondCount = secondCount + shareDatum.getSecond();
            //职位数
            postCount = postCount + shareDatum.getPostCount();
            //总时长
            visitCount = visitCount + shareDatum.getVisit();
            //注册量
            registerCount = registerCount + shareDatum.getRegister();
            //下载量
            downloadCount = downloadCount + shareDatum.getResumeDownload();


            LocalDate createTime = LocalDate.from(shareDatum.getCreateTime());


            if (DateUtils.isEffectiveDateBoolean(createTime, DateUtils.StringToLocalDate(mapOne.get("start").toString()), DateUtils.StringToLocalDate(mapOne.get("end").toString()))) {
                String A = "A";
                //ip地址数据
                Long ipAddressInteger = ipAddress.get(A);
                ipAddressInteger = ipAddressInteger + shareDatum.getIpAddress();
                ipAddress.put(A, ipAddressInteger);

                //访问时长
                Long secondInteger = second.get(A);
                secondInteger = secondInteger + shareDatum.getSecond();
                second.put(A, secondInteger);


                //职位分享数据
                Long postInteger = post.get(A);
                postInteger = postInteger + shareDatum.getPostCount();
                post.put(A, postInteger);


                //职位分享访问量
                Long visitInteger = visit.get(A);
                visitInteger = visitInteger + shareDatum.getVisit();
                visit.put(A, visitInteger);


                //注册
                Long registerInteger = register.get(A);
                registerInteger = registerInteger + shareDatum.getRegister();
                register.put(A, registerInteger);


                //下载
                Long downloadInteger = download.get(A);
                downloadInteger = downloadInteger + shareDatum.getResumeDownload();
                download.put(A, downloadInteger);
            } else if (DateUtils.isEffectiveDateBoolean(createTime, DateUtils.StringToLocalDate(mapTwo.get("start").toString()), DateUtils.StringToLocalDate(mapTwo.get("end").toString()))) {
                String B = "B";
                //ip地址数据
                Long ipAddressInteger = ipAddress.get(B);
                ipAddressInteger = ipAddressInteger + shareDatum.getIpAddress();
                ipAddress.put(B, ipAddressInteger);

                //访问时长
                Long secondInteger = second.get(B);
                secondInteger = secondInteger + shareDatum.getSecond();
                second.put(B, secondInteger);


                //职位分享数据
                Long postInteger = post.get(B);
                postInteger = postInteger + shareDatum.getPostCount();
                post.put(B, postInteger);


                //职位分享访问量
                Long visitInteger = visit.get(B);
                visitInteger = visitInteger + shareDatum.getVisit();
                visit.put(B, visitInteger);

                //注册
                Long registerInteger = register.get(B);
                registerInteger = registerInteger + shareDatum.getRegister();
                register.put(B, registerInteger);


                //下载
                Long downloadInteger = download.get(B);
                downloadInteger = downloadInteger + shareDatum.getResumeDownload();
                download.put(B, downloadInteger);
            } else if (DateUtils.isEffectiveDateBoolean(createTime, DateUtils.StringToLocalDate(mapThree.get("start").toString()), DateUtils.StringToLocalDate(mapThree.get("end").toString()))) {
                String C = "C";
                Long ipAddressInteger = ipAddress.get(C);
                ipAddressInteger = ipAddressInteger + shareDatum.getIpAddress();
                ipAddress.put(C, ipAddressInteger);


                //访问时长
                Long secondInteger = second.get(C);
                secondInteger = secondInteger + shareDatum.getSecond();
                second.put(C, secondInteger);


                //职位分享数据
                Long postInteger = post.get(C);
                postInteger = postInteger + shareDatum.getPostCount();
                post.put(C, postInteger);

                //职位分享访问量
                Long visitInteger = visit.get(C);
                visitInteger = visitInteger + shareDatum.getVisit();
                visit.put(C, visitInteger);


                //注册
                Long registerInteger = register.get(C);
                registerInteger = registerInteger + shareDatum.getRegister();
                register.put(C, registerInteger);

                //下载
                Long downloadInteger = download.get(C);
                downloadInteger = downloadInteger + shareDatum.getResumeDownload();
                download.put(C, downloadInteger);
            } else if (DateUtils.isEffectiveDateBoolean(createTime, DateUtils.StringToLocalDate(mapFour.get("start").toString()), DateUtils.StringToLocalDate(mapFour.get("end").toString()))) {
                String D = "D";
                Long ipAddressInteger = ipAddress.get(D);
                ipAddressInteger = ipAddressInteger + shareDatum.getIpAddress();
                ipAddress.put(D, ipAddressInteger);


                //访问时长
                Long secondInteger = second.get(D);
                secondInteger = secondInteger + shareDatum.getSecond();
                second.put(D, secondInteger);


                //职位分享数据
                Long postInteger = post.get(D);
                postInteger = postInteger + shareDatum.getPostCount();
                post.put(D, postInteger);


                //职位分享访问量
                Long visitInteger = visit.get(D);
                visitInteger = visitInteger + shareDatum.getVisit();
                visit.put(D, visitInteger);


                //注册
                Long registerInteger = register.get(D);
                registerInteger = registerInteger + shareDatum.getRegister();
                register.put(D, registerInteger);

                //下载
                Long downloadInteger = download.get(D);
                downloadInteger = downloadInteger + shareDatum.getResumeDownload();
                download.put(D, downloadInteger);
            } else if (DateUtils.isEffectiveDateBoolean(createTime, DateUtils.StringToLocalDate(mapFive.get("start").toString()), DateUtils.StringToLocalDate(mapFive.get("end").toString()))) {
                String E = "E";
                Long ipAddressInteger = ipAddress.get(E);
                ipAddressInteger = ipAddressInteger + shareDatum.getIpAddress();
                ipAddress.put(E, ipAddressInteger);


                //访问时长
                Long secondInteger = second.get(E);
                secondInteger = secondInteger + shareDatum.getSecond();
                second.put(E, secondInteger);


                //职位分享数据
                Long postInteger = post.get(E);
                postInteger = postInteger + shareDatum.getPostCount();
                post.put(E, postInteger);


                //职位分享访问量
                Long visitInteger = visit.get(E);
                visitInteger = visitInteger + shareDatum.getVisit();
                visit.put(E, visitInteger);

                //注册
                Long registerInteger = register.get(E);
                registerInteger = registerInteger + shareDatum.getRegister();
                register.put(E, registerInteger);

                //下载
                Long downloadInteger = download.get(E);
                downloadInteger = downloadInteger + shareDatum.getResumeDownload();
                download.put(E, downloadInteger);
            } else if (DateUtils.isEffectiveDateBoolean(createTime, DateUtils.StringToLocalDate(mapSix.get("start").toString()), DateUtils.StringToLocalDate(mapSix.get("end").toString()))) {
                String F = "F";
                Long ipAddressInteger = ipAddress.get(F);
                ipAddressInteger = ipAddressInteger + shareDatum.getIpAddress();
                ipAddress.put(F, ipAddressInteger);


                //访问时长
                Long secondInteger = second.get(F);
                secondInteger = secondInteger + shareDatum.getSecond();
                second.put(F, secondInteger);


                //职位分享数据
                Long postInteger = post.get(F);
                postInteger = postInteger + shareDatum.getPostCount();
                post.put(F, postInteger);


                //职位分享访问量
                Long visitInteger = visit.get(F);
                visitInteger = visitInteger + shareDatum.getVisit();
                visit.put(F, visitInteger);

                //注册
                Long registerInteger = register.get(F);
                registerInteger = registerInteger + shareDatum.getRegister();
                register.put(F, registerInteger);

                //下载
                Long downloadInteger = download.get(F);
                downloadInteger = downloadInteger + shareDatum.getResumeDownload();
                download.put(F, downloadInteger);
            } /*else if (DateUtils.isEffectiveDateBoolean(createTime, DateUtils.StringToLocalDate(mapSeven.get("start").toString()), DateUtils.StringToLocalDate(mapSeven.get("end").toString()))) {
                String G = "G";
                Long ipAddressInteger = ipAddress.get(G);
                ipAddressInteger = ipAddressInteger + shareDatum.getIpAddress();
                ipAddress.put(G, ipAddressInteger);


                //访问时长
                Long secondInteger = second.get(G);
                secondInteger = secondInteger + shareDatum.getSecond();
                second.put(G, secondInteger);


                //职位分享数据
                Long postInteger = post.get(G);
                postInteger = postInteger + shareDatum.getPostCount();
                post.put(G, postInteger);


                //职位分享访问量
                Long visitInteger = visit.get(G);
                visitInteger = visitInteger + shareDatum.getVisit();
                visit.put(G, visitInteger);

                //注册
                Long registerInteger = register.get(G);
                registerInteger = registerInteger + shareDatum.getRegister();
                register.put(G, registerInteger);

                //下载
                Long downloadInteger = download.get(G);
                downloadInteger = downloadInteger + shareDatum.getResumeDownload();
                download.put(G, downloadInteger);
            }*/

        }
        mapMap.put("ipAddress", ipAddress);
        mapMap.put("post", post);
        mapMap.put("visit", visit);
        mapMap.put("register", register);
        mapMap.put("download", download);
        mapMap.put("second", second);
        count.put("ipAddressCount", ipAddressCount);
        count.put("secondCount", secondCount);
        count.put("postCount", postCount);
        count.put("visitCount", visitCount);
        count.put("registerCount", registerCount);
        count.put("downloadCount", downloadCount);
        mapMap.put("count", count);
        return mapMap;
    }

    //月工具类
    public Map<String, Map<String, Long>> monthUtils(List<ShareData> shareData, String month) {
        List<Map> scope = LocalDateTimeUtils.getScope(month);
        Map mapOne = new HashMap(3);
        Map mapTwo = new HashMap(3);
        Map mapThree = new HashMap(3);
        Map mapFour = new HashMap(3);
        Map mapFive = new HashMap(3);
        Map mapSix = new HashMap(3);
        Map mapSeven = new HashMap(3);
        for (Map map : scope) {
            String order1 = map.get("order").toString();
            Integer order = Integer.parseInt(order1);
            if (order == 1) {
                mapOne = map;
            } else if (order == 2) {
                mapTwo = map;
            } else if (order == 3) {
                mapThree = map;
            } else if (order == 4) {
                mapFour = map;
            } else if (order == 5) {
                mapFive = map;
            } else if (order == 6) {
                mapSix = map;
            } else if (order == 7) {
                mapSeven = map;
            }
        }
        Map<String, Long> ipAddress = createMap(7);
        Map<String, Long> second = createMap(7);
        Map<String, Long> post = createMap(7);
        Map<String, Long> visit = createMap(7);
        Map<String, Long> register = createMap(7);
        Map<String, Long> download = createMap(7);
        Map<String, Map<String, Long>> mapMap = new HashMap<>(7);
        Map<String, Long> count = new HashMap<>(6);
        Long ipAddressCount = 0L;
        Long secondCount = 0L;
        Long postCount = 0L;
        Long visitCount = 0L;
        Long registerCount = 0L;
        Long downloadCount = 0L;
        for (ShareData shareDatum : shareData) {
            //计算总量
            //ip
            ipAddressCount = ipAddressCount + shareDatum.getIpAddress();
            //访问量
            secondCount = secondCount + shareDatum.getSecond();
            //职位数
            postCount = postCount + shareDatum.getPostCount();
            //总时长
            visitCount = visitCount + shareDatum.getVisit();
            //注册量
            registerCount = registerCount + shareDatum.getRegister();
            //下载量
            downloadCount = downloadCount + shareDatum.getResumeDownload();


            LocalDate createTime = LocalDate.from(shareDatum.getCreateTime());


            if (DateUtils.isEffectiveDateBoolean(createTime, DateUtils.StringToLocalDate(mapOne.get("start").toString()), DateUtils.StringToLocalDate(mapOne.get("end").toString()))) {
                String A = "A";
                //ip地址数据
                Long ipAddressInteger = ipAddress.get(A);
                ipAddressInteger = ipAddressInteger + shareDatum.getIpAddress();
                ipAddress.put(A, ipAddressInteger);

                //访问时长
                Long secondInteger = second.get(A);
                secondInteger = secondInteger + shareDatum.getSecond();
                second.put(A, secondInteger);


                //职位分享数据
                Long postInteger = post.get(A);
                postInteger = postInteger + shareDatum.getPostCount();
                post.put(A, postInteger);


                //职位分享访问量
                Long visitInteger = visit.get(A);
                visitInteger = visitInteger + shareDatum.getVisit();
                visit.put(A, visitInteger);


                //注册
                Long registerInteger = register.get(A);
                registerInteger = registerInteger + shareDatum.getRegister();
                register.put(A, registerInteger);


                //下载
                Long downloadInteger = download.get(A);
                downloadInteger = downloadInteger + shareDatum.getResumeDownload();
                download.put(A, downloadInteger);
            } else if (DateUtils.isEffectiveDateBoolean(createTime, DateUtils.StringToLocalDate(mapTwo.get("start").toString()), DateUtils.StringToLocalDate(mapTwo.get("end").toString()))) {
                String B = "B";
                //ip地址数据
                Long ipAddressInteger = ipAddress.get(B);
                ipAddressInteger = ipAddressInteger + shareDatum.getIpAddress();
                ipAddress.put(B, ipAddressInteger);

                //访问时长
                Long secondInteger = second.get(B);
                secondInteger = secondInteger + shareDatum.getSecond();
                second.put(B, secondInteger);


                //职位分享数据
                Long postInteger = post.get(B);
                postInteger = postInteger + shareDatum.getPostCount();
                post.put(B, postInteger);


                //职位分享访问量
                Long visitInteger = visit.get(B);
                visitInteger = visitInteger + shareDatum.getVisit();
                visit.put(B, visitInteger);

                //注册
                Long registerInteger = register.get(B);
                registerInteger = registerInteger + shareDatum.getRegister();
                register.put(B, registerInteger);


                //下载
                Long downloadInteger = download.get(B);
                downloadInteger = downloadInteger + shareDatum.getResumeDownload();
                download.put(B, downloadInteger);
            } else if (DateUtils.isEffectiveDateBoolean(createTime, DateUtils.StringToLocalDate(mapThree.get("start").toString()), DateUtils.StringToLocalDate(mapThree.get("end").toString()))) {
                String C = "C";
                Long ipAddressInteger = ipAddress.get(C);
                ipAddressInteger = ipAddressInteger + shareDatum.getIpAddress();
                ipAddress.put(C, ipAddressInteger);


                //访问时长
                Long secondInteger = second.get(C);
                secondInteger = secondInteger + shareDatum.getSecond();
                second.put(C, secondInteger);


                //职位分享数据
                Long postInteger = post.get(C);
                postInteger = postInteger + shareDatum.getPostCount();
                post.put(C, postInteger);

                //职位分享访问量
                Long visitInteger = visit.get(C);
                visitInteger = visitInteger + shareDatum.getVisit();
                visit.put(C, visitInteger);


                //注册
                Long registerInteger = register.get(C);
                registerInteger = registerInteger + shareDatum.getRegister();
                register.put(C, registerInteger);

                //下载
                Long downloadInteger = download.get(C);
                downloadInteger = downloadInteger + shareDatum.getResumeDownload();
                download.put(C, downloadInteger);
            } else if (DateUtils.isEffectiveDateBoolean(createTime, DateUtils.StringToLocalDate(mapFour.get("start").toString()), DateUtils.StringToLocalDate(mapFour.get("end").toString()))) {
                String D = "D";
                Long ipAddressInteger = ipAddress.get(D);
                ipAddressInteger = ipAddressInteger + shareDatum.getIpAddress();
                ipAddress.put(D, ipAddressInteger);


                //访问时长
                Long secondInteger = second.get(D);
                secondInteger = secondInteger + shareDatum.getSecond();
                second.put(D, secondInteger);


                //职位分享数据
                Long postInteger = post.get(D);
                postInteger = postInteger + shareDatum.getPostCount();
                post.put(D, postInteger);


                //职位分享访问量
                Long visitInteger = visit.get(D);
                visitInteger = visitInteger + shareDatum.getVisit();
                visit.put(D, visitInteger);


                //注册
                Long registerInteger = register.get(D);
                registerInteger = registerInteger + shareDatum.getRegister();
                register.put(D, registerInteger);

                //下载
                Long downloadInteger = download.get(D);
                downloadInteger = downloadInteger + shareDatum.getResumeDownload();
                download.put(D, downloadInteger);
            } else if (DateUtils.isEffectiveDateBoolean(createTime, DateUtils.StringToLocalDate(mapFive.get("start").toString()), DateUtils.StringToLocalDate(mapFive.get("end").toString()))) {
                String E = "E";
                Long ipAddressInteger = ipAddress.get(E);
                ipAddressInteger = ipAddressInteger + shareDatum.getIpAddress();
                ipAddress.put(E, ipAddressInteger);


                //访问时长
                Long secondInteger = second.get(E);
                secondInteger = secondInteger + shareDatum.getSecond();
                second.put(E, secondInteger);


                //职位分享数据
                Long postInteger = post.get(E);
                postInteger = postInteger + shareDatum.getPostCount();
                post.put(E, postInteger);


                //职位分享访问量
                Long visitInteger = visit.get(E);
                visitInteger = visitInteger + shareDatum.getVisit();
                visit.put(E, visitInteger);

                //注册
                Long registerInteger = register.get(E);
                registerInteger = registerInteger + shareDatum.getRegister();
                register.put(E, registerInteger);

                //下载
                Long downloadInteger = download.get(E);
                downloadInteger = downloadInteger + shareDatum.getResumeDownload();
                download.put(E, downloadInteger);
            } else if (DateUtils.isEffectiveDateBoolean(createTime, DateUtils.StringToLocalDate(mapSix.get("start").toString()), DateUtils.StringToLocalDate(mapSix.get("end").toString()))) {
                String F = "F";
                Long ipAddressInteger = ipAddress.get(F);
                ipAddressInteger = ipAddressInteger + shareDatum.getIpAddress();
                ipAddress.put(F, ipAddressInteger);


                //访问时长
                Long secondInteger = second.get(F);
                secondInteger = secondInteger + shareDatum.getSecond();
                second.put(F, secondInteger);


                //职位分享数据
                Long postInteger = post.get(F);
                postInteger = postInteger + shareDatum.getPostCount();
                post.put(F, postInteger);


                //职位分享访问量
                Long visitInteger = visit.get(F);
                visitInteger = visitInteger + shareDatum.getVisit();
                visit.put(F, visitInteger);

                //注册
                Long registerInteger = register.get(F);
                registerInteger = registerInteger + shareDatum.getRegister();
                register.put(F, registerInteger);

                //下载
                Long downloadInteger = download.get(F);
                downloadInteger = downloadInteger + shareDatum.getResumeDownload();
                download.put(F, downloadInteger);
            } else if (DateUtils.isEffectiveDateBoolean(createTime, DateUtils.StringToLocalDate(mapSeven.get("start").toString()), DateUtils.StringToLocalDate(mapSeven.get("end").toString()))) {
                String G = "G";
                Long ipAddressInteger = ipAddress.get(G);
                ipAddressInteger = ipAddressInteger + shareDatum.getIpAddress();
                ipAddress.put(G, ipAddressInteger);


                //访问时长
                Long secondInteger = second.get(G);
                secondInteger = secondInteger + shareDatum.getSecond();
                second.put(G, secondInteger);


                //职位分享数据
                Long postInteger = post.get(G);
                postInteger = postInteger + shareDatum.getPostCount();
                post.put(G, postInteger);


                //职位分享访问量
                Long visitInteger = visit.get(G);
                visitInteger = visitInteger + shareDatum.getVisit();
                visit.put(G, visitInteger);

                //注册
                Long registerInteger = register.get(G);
                registerInteger = registerInteger + shareDatum.getRegister();
                register.put(G, registerInteger);

                //下载
                Long downloadInteger = download.get(G);
                downloadInteger = downloadInteger + shareDatum.getResumeDownload();
                download.put(G, downloadInteger);
            }

        }
        mapMap.put("ipAddress", ipAddress);
        mapMap.put("post", post);
        mapMap.put("visit", visit);
        mapMap.put("register", register);
        mapMap.put("download", download);
        mapMap.put("second", second);
        count.put("ipAddressCount", ipAddressCount);
        count.put("secondCount", secondCount);
        count.put("postCount", postCount);
        count.put("visitCount", visitCount);
        count.put("registerCount", registerCount);
        count.put("downloadCount", downloadCount);
        mapMap.put("count", count);
        return mapMap;
    }

    //周工具类
    public Map<String, Map<String, Long>> weekUtils(List<ShareData> shareData, LocalDate beginDate) throws ParseException {
        Map<String, Long> ipAddress = createMap(7);
        Map<String, Long> second = createMap(7);
        Map<String, Long> post = createMap(7);
        Map<String, Long> visit = createMap(7);
        Map<String, Long> register = createMap(7);
        Map<String, Long> download = createMap(7);
        Map<String, Map<String, Long>> mapMap = new HashMap<>(7);
        Map<String, Long> count = new HashMap<>(6);
        Long ipAddressCount = 0L;
        Long secondCount = 0L;
        Long postCount = 0L;
        Long visitCount = 0L;
        Long registerCount = 0L;
        Long downloadCount = 0L;
        for (ShareData shareDatum : shareData) {
            //计算总量
            //ip
            ipAddressCount = ipAddressCount + shareDatum.getIpAddress();
            //访问量
            secondCount = secondCount + shareDatum.getSecond();
            //职位数
            postCount = postCount + shareDatum.getPostCount();
            //总时长
            visitCount = visitCount + shareDatum.getVisit();
            //注册量
            registerCount = registerCount + shareDatum.getRegister();
            //下载量
            downloadCount = downloadCount + shareDatum.getResumeDownload();


            LocalDateTime createTime = shareDatum.getCreateTime();
            if (LocalDateTimeUtils.isSameDay(createTime, 0, beginDate)) {
                String A = "A";
                //ip地址数据
                Long ipAddressInteger = ipAddress.get(A);
                ipAddressInteger = ipAddressInteger + shareDatum.getIpAddress();
                ipAddress.put(A, ipAddressInteger);

                //访问时长
                Long secondInteger = second.get(A);
                secondInteger = secondInteger + shareDatum.getSecond();
                second.put(A, secondInteger);


                //职位分享数据
                Long postInteger = post.get(A);
                postInteger = postInteger + shareDatum.getPostCount();
                post.put(A, postInteger);


                //职位分享访问量
                Long visitInteger = visit.get(A);
                visitInteger = visitInteger + shareDatum.getVisit();
                visit.put(A, visitInteger);


                //注册
                Long registerInteger = register.get(A);
                registerInteger = registerInteger + shareDatum.getRegister();
                register.put(A, registerInteger);


                //下载
                Long downloadInteger = download.get(A);
                downloadInteger = downloadInteger + shareDatum.getResumeDownload();
                download.put(A, downloadInteger);
            } else if (LocalDateTimeUtils.isSameDay(createTime, 1, beginDate)) {
                String B = "B";
                //ip地址数据
                Long ipAddressInteger = ipAddress.get(B);
                ipAddressInteger = ipAddressInteger + shareDatum.getIpAddress();
                ipAddress.put(B, ipAddressInteger);

                //访问时长
                Long secondInteger = second.get(B);
                secondInteger = secondInteger + shareDatum.getSecond();
                second.put(B, secondInteger);


                //职位分享数据
                Long postInteger = post.get(B);
                postInteger = postInteger + shareDatum.getPostCount();
                post.put(B, postInteger);


                //职位分享访问量
                Long visitInteger = visit.get(B);
                visitInteger = visitInteger + shareDatum.getVisit();
                visit.put(B, visitInteger);

                //注册
                Long registerInteger = register.get(B);
                registerInteger = registerInteger + shareDatum.getRegister();
                register.put(B, registerInteger);


                //下载
                Long downloadInteger = download.get(B);
                downloadInteger = downloadInteger + shareDatum.getResumeDownload();
                download.put(B, downloadInteger);
            } else if (LocalDateTimeUtils.isSameDay(createTime, 2, beginDate)) {
                String C = "C";
                Long ipAddressInteger = ipAddress.get(C);
                ipAddressInteger = ipAddressInteger + shareDatum.getIpAddress();
                ipAddress.put(C, ipAddressInteger);


                //访问时长
                Long secondInteger = second.get(C);
                secondInteger = secondInteger + shareDatum.getSecond();
                second.put(C, secondInteger);


                //职位分享数据
                Long postInteger = post.get(C);
                postInteger = postInteger + shareDatum.getPostCount();
                post.put(C, postInteger);

                //职位分享访问量
                Long visitInteger = visit.get(C);
                visitInteger = visitInteger + shareDatum.getVisit();
                visit.put(C, visitInteger);


                //注册
                Long registerInteger = register.get(C);
                registerInteger = registerInteger + shareDatum.getRegister();
                register.put(C, registerInteger);

                //下载
                Long downloadInteger = download.get(C);
                downloadInteger = downloadInteger + shareDatum.getResumeDownload();
                download.put(C, downloadInteger);
            } else if (LocalDateTimeUtils.isSameDay(createTime, 3, beginDate)) {
                String D = "D";
                Long ipAddressInteger = ipAddress.get(D);
                ipAddressInteger = ipAddressInteger + shareDatum.getIpAddress();
                ipAddress.put(D, ipAddressInteger);


                //访问时长
                Long secondInteger = second.get(D);
                secondInteger = secondInteger + shareDatum.getSecond();
                second.put(D, secondInteger);


                //职位分享数据
                Long postInteger = post.get(D);
                postInteger = postInteger + shareDatum.getPostCount();
                post.put(D, postInteger);


                //职位分享访问量
                Long visitInteger = visit.get(D);
                visitInteger = visitInteger + shareDatum.getVisit();
                visit.put(D, visitInteger);


                //注册
                Long registerInteger = register.get(D);
                registerInteger = registerInteger + shareDatum.getRegister();
                register.put(D, registerInteger);

                //下载
                Long downloadInteger = download.get(D);
                downloadInteger = downloadInteger + shareDatum.getResumeDownload();
                download.put(D, downloadInteger);
            } else if (LocalDateTimeUtils.isSameDay(createTime, 4, beginDate)) {
                String E = "E";
                Long ipAddressInteger = ipAddress.get(E);
                ipAddressInteger = ipAddressInteger + shareDatum.getIpAddress();
                ipAddress.put(E, ipAddressInteger);


                //访问时长
                Long secondInteger = second.get(E);
                secondInteger = secondInteger + shareDatum.getSecond();
                second.put(E, secondInteger);


                //职位分享数据
                Long postInteger = post.get(E);
                postInteger = postInteger + shareDatum.getPostCount();
                post.put(E, postInteger);


                //职位分享访问量
                Long visitInteger = visit.get(E);
                visitInteger = visitInteger + shareDatum.getVisit();
                visit.put(E, visitInteger);

                //注册
                Long registerInteger = register.get(E);
                registerInteger = registerInteger + shareDatum.getRegister();
                register.put(E, registerInteger);

                //下载
                Long downloadInteger = download.get(E);
                downloadInteger = downloadInteger + shareDatum.getResumeDownload();
                download.put(E, downloadInteger);
            } else if (LocalDateTimeUtils.isSameDay(createTime, 5, beginDate)) {
                String F = "F";
                Long ipAddressInteger = ipAddress.get(F);
                ipAddressInteger = ipAddressInteger + shareDatum.getIpAddress();
                ipAddress.put(F, ipAddressInteger);


                //访问时长
                Long secondInteger = second.get(F);
                secondInteger = secondInteger + shareDatum.getSecond();
                second.put(F, secondInteger);


                //职位分享数据
                Long postInteger = post.get(F);
                postInteger = postInteger + shareDatum.getPostCount();
                post.put(F, postInteger);


                //职位分享访问量
                Long visitInteger = visit.get(F);
                visitInteger = visitInteger + shareDatum.getVisit();
                visit.put(F, visitInteger);

                //注册
                Long registerInteger = register.get(F);
                registerInteger = registerInteger + shareDatum.getRegister();
                register.put(F, registerInteger);

                //下载
                Long downloadInteger = download.get(F);
                downloadInteger = downloadInteger + shareDatum.getResumeDownload();
                download.put(F, downloadInteger);
            } else if (LocalDateTimeUtils.isSameDay(createTime, 6, beginDate)) {
                String G = "G";
                Long ipAddressInteger = ipAddress.get(G);
                ipAddressInteger = ipAddressInteger + shareDatum.getIpAddress();
                ipAddress.put(G, ipAddressInteger);


                //访问时长
                Long secondInteger = second.get(G);
                secondInteger = secondInteger + shareDatum.getSecond();
                second.put(G, secondInteger);


                //职位分享数据
                Long postInteger = post.get(G);
                postInteger = postInteger + shareDatum.getPostCount();
                post.put(G, postInteger);


                //职位分享访问量
                Long visitInteger = visit.get(G);
                visitInteger = visitInteger + shareDatum.getVisit();
                visit.put(G, visitInteger);

                //注册
                Long registerInteger = register.get(G);
                registerInteger = registerInteger + shareDatum.getRegister();
                register.put(G, registerInteger);

                //下载
                Long downloadInteger = download.get(G);
                downloadInteger = downloadInteger + shareDatum.getResumeDownload();
                download.put(G, downloadInteger);
            }

        }
        mapMap.put("ipAddress", ipAddress);
        mapMap.put("post", post);
        mapMap.put("visit", visit);
        mapMap.put("register", register);
        mapMap.put("download", download);
        mapMap.put("second", second);
        count.put("ipAddressCount", ipAddressCount);
        count.put("secondCount", secondCount);
        count.put("postCount", postCount);
        count.put("visitCount", visitCount);
        count.put("registerCount", registerCount);
        count.put("downloadCount", downloadCount);
        mapMap.put("count", count);
        return mapMap;
    }

    //时工具类
    public Map<String, Map<String, Long>> hourUtils(List<ShareData> shareData) {
        Map<String, Long> ipAddress = createMap(7);
        Map<String, Long> second = createMap(7);
        Map<String, Long> post = createMap(7);
        Map<String, Long> visit = createMap(7);
        Map<String, Long> register = createMap(7);
        Map<String, Long> download = createMap(7);
        Map<String, Map<String, Long>> mapMap = new HashMap<>(7);
        Map<String, Long> count = new HashMap<>(6);
        Long ipAddressCount = 0L;
        Long secondCount = 0L;
        Long postCount = 0L;
        Long visitCount = 0L;
        Long registerCount = 0L;
        Long downloadCount = 0L;
        if (shareData.size() > 0) {
            for (ShareData shareDatum : shareData) {
                //计算总量
                //ip
                ipAddressCount = ipAddressCount + shareDatum.getIpAddress();
                //访问量
                secondCount = secondCount + shareDatum.getSecond();
                //职位数
                postCount = postCount + shareDatum.getPostCount();
                //总时长
                visitCount = visitCount + shareDatum.getVisit();
                //注册量
                registerCount = registerCount + shareDatum.getRegister();
                //下载量
                downloadCount = downloadCount + shareDatum.getResumeDownload();

                LocalDateTime createTime = shareDatum.getCreateTime();
                int hour = createTime.getHour();
                if (hour <= 4) {
                    String A = "A";
                    //ip地址数据
                    Long ipAddressInteger = ipAddress.get(A);
                    ipAddressInteger = ipAddressInteger + shareDatum.getIpAddress();
                    ipAddress.put(A, ipAddressInteger);

                    //访问时长
                    Long secondInteger = second.get(A);
                    secondInteger = secondInteger + shareDatum.getSecond();
                    second.put(A, secondInteger);


                    //职位分享数据
                    Long postInteger = post.get(A);
                    postInteger = postInteger + shareDatum.getPostCount();
                    post.put(A, postInteger);


                    //职位分享访问量
                    Long visitInteger = visit.get(A);
                    visitInteger = visitInteger + shareDatum.getVisit();
                    visit.put(A, visitInteger);


                    //注册
                    Long registerInteger = register.get(A);
                    registerInteger = registerInteger + shareDatum.getRegister();
                    register.put(A, registerInteger);


                    //下载
                    Long downloadInteger = download.get(A);
                    downloadInteger = downloadInteger + shareDatum.getResumeDownload();
                    download.put(A, downloadInteger);
                } else if (hour > 4 && hour <= 8) {
                    String B = "B";
                    //ip地址数据
                    Long ipAddressInteger = ipAddress.get(B);
                    ipAddressInteger = ipAddressInteger + shareDatum.getIpAddress();
                    ipAddress.put(B, ipAddressInteger);

                    //访问时长
                    Long secondInteger = second.get(B);
                    secondInteger = secondInteger + shareDatum.getSecond();
                    second.put(B, secondInteger);


                    //职位分享数据
                    Long postInteger = post.get(B);
                    postInteger = postInteger + shareDatum.getPostCount();
                    post.put(B, postInteger);


                    //职位分享访问量
                    Long visitInteger = visit.get(B);
                    visitInteger = visitInteger + shareDatum.getVisit();
                    visit.put(B, visitInteger);

                    //注册
                    Long registerInteger = register.get(B);
                    registerInteger = registerInteger + shareDatum.getRegister();
                    register.put(B, registerInteger);


                    //下载
                    Long downloadInteger = download.get(B);
                    downloadInteger = downloadInteger + shareDatum.getResumeDownload();
                    download.put(B, downloadInteger);
                } else if (hour > 8 && hour <= 12) {
                    String C = "C";
                    Long ipAddressInteger = ipAddress.get(C);
                    ipAddressInteger = ipAddressInteger + shareDatum.getIpAddress();
                    ipAddress.put(C, ipAddressInteger);


                    //访问时长
                    Long secondInteger = second.get(C);
                    secondInteger = secondInteger + shareDatum.getSecond();
                    second.put(C, secondInteger);


                    //职位分享数据
                    Long postInteger = post.get(C);
                    postInteger = postInteger + shareDatum.getPostCount();
                    post.put(C, postInteger);

                    //职位分享访问量
                    Long visitInteger = visit.get(C);
                    visitInteger = visitInteger + shareDatum.getVisit();
                    visit.put(C, visitInteger);


                    //注册
                    Long registerInteger = register.get(C);
                    registerInteger = registerInteger + shareDatum.getRegister();
                    register.put(C, registerInteger);

                    //下载
                    Long downloadInteger = download.get(C);
                    downloadInteger = downloadInteger + shareDatum.getResumeDownload();
                    download.put(C, downloadInteger);
                } else if (hour > 12 && hour <= 16) {
                    String D = "D";
                    Long ipAddressInteger = ipAddress.get(D);
                    ipAddressInteger = ipAddressInteger + shareDatum.getIpAddress();
                    ipAddress.put(D, ipAddressInteger);


                    //访问时长
                    Long secondInteger = second.get(D);
                    secondInteger = secondInteger + shareDatum.getSecond();
                    second.put(D, secondInteger);


                    //职位分享数据
                    Long postInteger = post.get(D);
                    postInteger = postInteger + shareDatum.getPostCount();
                    post.put(D, postInteger);


                    //职位分享访问量
                    Long visitInteger = visit.get(D);
                    visitInteger = visitInteger + shareDatum.getVisit();
                    visit.put(D, visitInteger);


                    //注册
                    Long registerInteger = register.get(D);
                    registerInteger = registerInteger + shareDatum.getRegister();
                    register.put(D, registerInteger);

                    //下载
                    Long downloadInteger = download.get(D);
                    downloadInteger = downloadInteger + shareDatum.getResumeDownload();
                    download.put(D, downloadInteger);
                } else if (hour > 16 && hour <= 20) {
                    String E = "E";
                    Long ipAddressInteger = ipAddress.get(E);
                    ipAddressInteger = ipAddressInteger + shareDatum.getIpAddress();
                    ipAddress.put(E, ipAddressInteger);


                    //访问时长
                    Long secondInteger = second.get(E);
                    secondInteger = secondInteger + shareDatum.getSecond();
                    second.put(E, secondInteger);


                    //职位分享数据
                    Long postInteger = post.get(E);
                    postInteger = postInteger + shareDatum.getPostCount();
                    post.put(E, postInteger);


                    //职位分享访问量
                    Long visitInteger = visit.get(E);
                    visitInteger = visitInteger + shareDatum.getVisit();
                    visit.put(E, visitInteger);

                    //注册
                    Long registerInteger = register.get(E);
                    registerInteger = registerInteger + shareDatum.getRegister();
                    register.put(E, registerInteger);

                    //下载
                    Long downloadInteger = download.get(E);
                    downloadInteger = downloadInteger + shareDatum.getResumeDownload();
                    download.put(E, downloadInteger);
                } else if (hour > 20 && hour <= 24) {
                    String F = "F";
                    Long ipAddressInteger = ipAddress.get(F);
                    ipAddressInteger = ipAddressInteger + shareDatum.getIpAddress();
                    ipAddress.put(F, ipAddressInteger);


                    //访问时长
                    Long secondInteger = second.get(F);
                    secondInteger = secondInteger + shareDatum.getSecond();
                    second.put(F, secondInteger);


                    //职位分享数据
                    Long postInteger = post.get(F);
                    postInteger = postInteger + shareDatum.getPostCount();
                    post.put(F, postInteger);


                    //职位分享访问量
                    Long visitInteger = visit.get(F);
                    visitInteger = visitInteger + shareDatum.getVisit();
                    visit.put(F, visitInteger);

                    //注册
                    Long registerInteger = register.get(F);
                    registerInteger = registerInteger + shareDatum.getRegister();
                    register.put(F, registerInteger);

                    //下载
                    Long downloadInteger = download.get(F);
                    downloadInteger = downloadInteger + shareDatum.getResumeDownload();
                    download.put(F, downloadInteger);
                }
            }

        }
        mapMap.put("ipAddress", ipAddress);
        mapMap.put("post", post);
        mapMap.put("visit", visit);
        mapMap.put("register", register);
        mapMap.put("download", download);
        mapMap.put("second", second);
        count.put("ipAddressCount", ipAddressCount);
        count.put("secondCount", secondCount);
        count.put("postCount", postCount);
        count.put("visitCount", visitCount);
        count.put("registerCount", registerCount);
        count.put("downloadCount", downloadCount);
        mapMap.put("count", count);
        return mapMap;
    }

    public HashMap createMap(int i) {
        HashMap<String, Long> hashMap = new HashMap<>(i);
        hashMap.put("A", 0L);
        hashMap.put("B", 0L);
        hashMap.put("C", 0L);
        hashMap.put("D", 0L);
        hashMap.put("E", 0L);
        hashMap.put("F", 0L);
        hashMap.put("G", 0L);
        return hashMap;
    }

    public HashMap countMap() {
        HashMap<String, Long> hashMap = new HashMap<>(6);
        hashMap.put("ipAddress", 0L);
        hashMap.put("second", 0L);
        hashMap.put("post", 0L);
        hashMap.put("visit", 0L);
        hashMap.put("register", 0L);
        hashMap.put("download", 0L);
        return hashMap;
    }

    /*当天与后一天比较*/
    public ShareDataDTO percentage(ShareDataDTO shareDataDTO, Map<String, Long> map1, Map<String, Long> map2) {

        Map<String, Double> ipCount = CalculateProportionUtil.proportionInt(Math.toIntExact(map1.get("ipAddressCount")), Math.toIntExact(map2.get("ipAddressCount")));
        shareDataDTO.setIpAddressCount(ipCount);
        Map<String, Double> secondCount = CalculateProportionUtil.proportionInt(Math.toIntExact(map1.get("secondCount")), Math.toIntExact(map2.get("secondCount")));
        shareDataDTO.setSecondCount(secondCount);
        Map<String, Double> postCount = CalculateProportionUtil.proportionInt(Math.toIntExact(map1.get("postCount")), Math.toIntExact(map2.get("postCount")));
        shareDataDTO.setPostCount(postCount);
        Map<String, Double> visitCount = CalculateProportionUtil.proportionInt(Math.toIntExact(map1.get("visitCount")), Math.toIntExact(map2.get("visitCount")));
        shareDataDTO.setVisitCount(visitCount);
        Map<String, Double> registerCount = CalculateProportionUtil.proportionInt(Math.toIntExact(map1.get("registerCount")), Math.toIntExact(map2.get("registerCount")));
        shareDataDTO.setRegisterCount(registerCount);
        Map<String, Double> downloadCount = CalculateProportionUtil.proportionInt(Math.toIntExact(map1.get("downloadCount")), Math.toIntExact(map2.get("downloadCount")));
        shareDataDTO.setDownloadCount(downloadCount);
        return shareDataDTO;
    }


    @PostMapping("/recommendationData")
    @ApiOperation("人才推荐交付数据展板")
    public R<ShareDataDTO> recommendationData(@Valid @RequestBody ShareDataQuery shareDataQuery) throws ParseException {
        ShareDataDTO shareDataDTO = new ShareDataDTO();
        if (shareDataQuery.getPageNo() == null || shareDataQuery.getPageNo() == 0) {
            shareDataQuery.setPageNo(1L);
        }
        shareDataQuery.setUserId(companyMetadata.userid());
        if (shareDataQuery.getPostType() == null) {
            return R.fail("职位详情不能为空!");
        }
        //全部选项
        if (shareDataQuery.getAllBeginDate() != null && shareDataQuery.getAllEndDate() != null) {

               /* SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date parse11 = dateFormat.parse("2023-10-31 24:00:00");*/
            long between = ChronoUnit.DAYS.between(shareDataQuery.getAllBeginDate(), shareDataQuery.getAllEndDate()) + 1;
            long between1 = ChronoUnit.MONTHS.between(shareDataQuery.getAllBeginDate(), shareDataQuery.getAllEndDate()) + 1;
            // System.out.println(between1);
            if (between < 7) {
                //全部选项
                if (shareDataQuery.getAllBeginDate() != null && shareDataQuery.getAllEndDate() != null) {

               /* SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date parse11 = dateFormat.parse("2023-10-31 24:00:00");*/
                    shareDataDTO.setTotal(between);
                    LocalDate allBeginDate = shareDataQuery.getAllBeginDate();
                    LocalDate date = allBeginDate.plusDays(-shareDataQuery.getPageNo() + 1);
                    shareDataQuery.setDay(date);
                    shareDataQuery.setDateType(0);
                }
            } else if (between >= 7 && between < 30) {
                double ceil = Math.ceil(between / (double) 7);
                shareDataDTO.setTotal((long) ceil);
                if (shareDataQuery.getPageNo() > between) {
                    return R.fail("分页数据大于筛选数据");
                }

                Long pageNo = shareDataQuery.getPageNo();
                int a = (int) (pageNo - 1);
                shareDataQuery.setBeginDate(shareDataQuery.getAllBeginDate().plusDays(7 * a));
                LocalDate date = shareDataQuery.getBeginDate().plusDays(6);
                if (date.isBefore(shareDataQuery.getAllEndDate())) {
                    shareDataQuery.setEndDate(date);
                } else {
                    shareDataQuery.setEndDate(shareDataQuery.getAllEndDate());
                }

                shareDataQuery.setDateType(1);
            } else if (between >= 30 && between1 < 6) {
                LocalDate localDate = LocalDateTimeUtils.weekStartTime(shareDataQuery.getAllBeginDate());
                LocalDateTime localDateTime = LocalDateTimeUtils.weekEndTime(shareDataQuery.getAllEndDate());
                long between2 = ChronoUnit.WEEKS.between(localDate, localDateTime) + 1;
                double ceil = Math.ceil(between2 / (double) 6);
                shareDataDTO.setTotal((long) ceil);
                if (shareDataQuery.getPageNo() > between) {
                    return R.fail("分页数据大于筛选数据");
                }

                Long pageNo = shareDataQuery.getPageNo();
                int a = (int) (pageNo - 1);

                shareDataQuery.setBeginDate(LocalDateTimeUtils.nextWeekStartTime(localDate, 6 * a));
                LocalDate date = LocalDateTimeUtils.nextWeekEndTime(shareDataQuery.getBeginDate(), 6);
                if (date.isBefore(shareDataQuery.getAllEndDate())) {
                    shareDataQuery.setEndDate(date);
                } else {
                    shareDataQuery.setEndDate(shareDataQuery.getAllEndDate());
                }


                Map<String, List<PutPostShareDataPO>> map2 = orderReceivingApi.orderReceivingMonth(companyMetadata.userid(), shareDataQuery.getBeginDate(), shareDataQuery.getEndDate(), shareDataQuery.getCity(), shareDataQuery.getPostType());
                List<PutPostShareDataPO> putPostShareDataPOS1 = map2.get("A");
                shareDataDTO.setOrderTaking(monthTimelyStatistics1(putPostShareDataPOS1, shareDataQuery.getBeginDate()));

                List<PutPostShareDataPO> putInResumeList = putInResumeService.browseWeeks(shareDataQuery);
                Map<String, Long> map = monthTimelyStatistics1(putInResumeList, shareDataQuery.getBeginDate());
                shareDataDTO.setBrowse(map);
                List<PutPostShareDataPO> list = putInResumeService.slipWeeks(shareDataQuery);
                shareDataDTO.setSlip(monthTimelyStatistics1(list, shareDataQuery.getBeginDate()));
                if (shareDataQuery.getPostType() != 4) {
                    List<PutPostShareDataPO> interviewList = putInResumeService.interviewWeeks(shareDataQuery);
                    shareDataDTO.setInterview(monthTimelyStatistics1(interviewList, shareDataQuery.getBeginDate()));
                }
                if (shareDataQuery.getPostType() != 2) {
                    List<PutPostShareDataPO> downloadDay = putInResumeService.downloadWeeks(shareDataQuery);
                    shareDataDTO.setDownload(monthTimelyStatistics1(downloadDay, shareDataQuery.getBeginDate()));
                }
                if (shareDataQuery.getPostType() == 1 || shareDataQuery.getPostType() == 2) {
                    List<PutPostShareDataPO> offerDay = putInResumeService.offerWeeks(shareDataQuery);
                    shareDataDTO.setOffer(monthTimelyStatistics1(offerDay, shareDataQuery.getBeginDate()));

                }
                if (shareDataQuery.getPostType() == 2) {
                    List<PutPostShareDataPO> fullMoonDay = putInResumeService.fullMoonWeeks(shareDataQuery);
                    shareDataDTO.setFullMoon(monthTimelyStatistics1(fullMoonDay, shareDataQuery.getBeginDate()));
                }

                shareDataQuery.setDateType(6);
            } else if (between1 >= 6) {
                double ceil = Math.ceil(between1 / (double) 6);
                shareDataDTO.setTotal((long) ceil);
                if (shareDataQuery.getPageNo() > between1) {
                    return R.fail("分页数据大于筛选数据");
                }

                Long pageNo = shareDataQuery.getPageNo();
                int a = (int) (pageNo - 1);
                shareDataQuery.setBeginDate(LocalDateTimeUtils.monthStartTime(shareDataQuery.getAllBeginDate().plusMonths(6 * a)));
                LocalDate date = shareDataQuery.getBeginDate().plusMonths(5);
                if (date.isBefore(shareDataQuery.getAllEndDate())) {
                    shareDataQuery.setEndDate(LocalDateTimeUtils.monthEndTime(date));
                } else {
                    shareDataQuery.setEndDate(shareDataQuery.getAllEndDate());
                }

                Map<String, List<PutPostShareDataPO>> map2 = orderReceivingApi.orderReceivingMonth(companyMetadata.userid(), shareDataQuery.getBeginDate(), shareDataQuery.getEndDate(), shareDataQuery.getCity(), shareDataQuery.getPostType());
                List<PutPostShareDataPO> putPostShareDataPOS1 = map2.get("A");
                shareDataDTO.setOrderTaking(yearUtils1(putPostShareDataPOS1, shareDataQuery.getBeginDate(), shareDataQuery.getEndDate()));
                List<PutPostShareDataPO> putInResumeList = putInResumeService.browseMonth(shareDataQuery);
                Map<String, Long> map = yearUtils1(putInResumeList, shareDataQuery.getBeginDate(), shareDataQuery.getEndDate());
                shareDataDTO.setBrowse(map);
                List<PutPostShareDataPO> list = putInResumeService.slipMonth(shareDataQuery);
                Map<String, Long> map4 = yearUtils1(list, shareDataQuery.getBeginDate(), shareDataQuery.getEndDate());
                shareDataDTO.setSlip(map4);

                if (shareDataQuery.getPostType() != 4) {
                    List<PutPostShareDataPO> interviewList = putInResumeService.interviewMonth(shareDataQuery);
                    Map<String, Long> map1 = yearUtils1(interviewList, shareDataQuery.getBeginDate(), shareDataQuery.getEndDate());
                    shareDataDTO.setInterview(map1);
                }
                if (shareDataQuery.getPostType() != 2) {
                    List<PutPostShareDataPO> downloadDay = putInResumeService.downloadMonth(shareDataQuery);
                    Map<String, Long> map1 = yearUtils1(downloadDay, shareDataQuery.getBeginDate(), shareDataQuery.getEndDate());
                    shareDataDTO.setDownload(map1);
                }
                if (shareDataQuery.getPostType() == 1 || shareDataQuery.getPostType() == 2) {
                    List<PutPostShareDataPO> offerDay = putInResumeService.offerMonth(shareDataQuery);
                    Map<String, Long> map1 = yearUtils1(offerDay, shareDataQuery.getBeginDate(), shareDataQuery.getEndDate());

                    shareDataDTO.setOffer(map1);
                    List<PutPostShareDataPO> entryDay = putInResumeService.entryMonth(shareDataQuery);
                    Map<String, Long> map8 = yearUtils1(entryDay, shareDataQuery.getBeginDate(), shareDataQuery.getEndDate());
                    shareDataDTO.setEntry(map8);
                }
                if (shareDataQuery.getPostType() == 2) {
                    List<PutPostShareDataPO> fullMoonDay = putInResumeService.fullMoonMonth(shareDataQuery);
                    Map<String, Long> map1 = yearUtils1(fullMoonDay, shareDataQuery.getBeginDate(), shareDataQuery.getEndDate());
                    shareDataDTO.setFullMoon(map1);
                }


                shareDataQuery.setDateType(6);
            }


        }


        if (shareDataQuery.getDateType() == 0) {

            //如果不是全部是时间选项则进入当天的选项
            if (shareDataQuery.getDay() == null) {
                return R.fail("请选择日期!");
            }

            Map<String, List<PutPostShareDataPO>> map2 = orderReceivingApi.orderReceivingDay(companyMetadata.userid(), shareDataQuery.getDay(), shareDataQuery.getCity(), shareDataQuery.getPostType());
            List<PutPostShareDataPO> putPostShareDataPOS1 = map2.get("A");
            Map<String, Long> map3 = timelyStatistics(putPostShareDataPOS1);
            Long counts = map3.get("counts");
            shareDataDTO.setOrderTakingSUM(counts);
            map3.remove("counts");
            shareDataDTO.setOrderTaking(map3);

            List<PutPostShareDataPO> putInResumeList = putInResumeService.browseDay(shareDataQuery);
            Map<String, Long> map = timelyStatistics(putInResumeList);
            shareDataDTO.setBrowseSUM(map.get("counts"));
            map.remove("counts");
            shareDataDTO.setBrowse(map);
            List<PutPostShareDataPO> list = putInResumeService.slipDay(shareDataQuery);
            Map<String, Long> map4 = timelyStatistics(list);
            shareDataDTO.setSlipSUM(map4.get("counts"));
            map4.remove("counts");
            shareDataDTO.setSlip(map4);

            if (shareDataQuery.getPostType() != 4) {
                List<PutPostShareDataPO> interviewList = putInResumeService.interviewDay(shareDataQuery);
                Map<String, Long> map1 = timelyStatistics(interviewList);
                shareDataDTO.setInterviewSUM(map1.get("counts"));
                map1.remove("counts");
                shareDataDTO.setInterview(map1);
            }
            if (shareDataQuery.getPostType() != 2) {
                List<PutPostShareDataPO> downloadDay = putInResumeService.downloadDay(shareDataQuery);
                Map<String, Long> map1 = timelyStatistics(downloadDay);
                shareDataDTO.setDownloadSUM(map1.get("counts"));
                map1.remove("counts");
                shareDataDTO.setDownload(map1);
            }
            if (shareDataQuery.getPostType() == 1 || shareDataQuery.getPostType() == 2) {
                List<PutPostShareDataPO> offerDay = putInResumeService.offerDay(shareDataQuery);
                Map<String, Long> map1 = timelyStatistics(offerDay);
                shareDataDTO.setOfferSUM(map1.get("counts"));
                map1.remove("counts");
                shareDataDTO.setOffer(map1);
                List<PutPostShareDataPO> entryDay = putInResumeService.entryDay(shareDataQuery);
                Map<String, Long> map8 = timelyStatistics(entryDay);
                shareDataDTO.setEntrySUM(map8.get("counts"));
                map8.remove("counts");
                shareDataDTO.setEntry(map8);
            }
            if (shareDataQuery.getPostType() == 2) {
                List<PutPostShareDataPO> fullMoonDay = putInResumeService.fullMoonDay(shareDataQuery);
                Map<String, Long> map1 = timelyStatistics(fullMoonDay);
                shareDataDTO.setFullMoonSUM(map1.get("counts"));
                map1.remove("counts");
                shareDataDTO.setFullMoon(map1);
            }

            if (shareDataQuery.getAllBeginDate() == null && shareDataQuery.getAllEndDate() == null) {
                shareDataQuery.setDay(shareDataQuery.getDay().plusDays(-1));
                List<PutPostShareDataPO> putPostShareDataPOS2 = map2.get("B");
                Map<String, Long> map5 = timelyStatistics(putPostShareDataPOS2);
                shareDataDTO.setSuperiorsOrderTakingSUM(map5.get("counts"));
                map5.remove("counts");
                shareDataDTO.setSuperiorOrderTaking(map5);


                List<PutPostShareDataPO> putInResumeList1 = putInResumeService.browseDay(shareDataQuery);
                Map<String, Long> map1 = timelyStatistics(putInResumeList1);
                shareDataDTO.setSuperiorsBrowseSUM(map1.get("counts"));
                map1.remove("counts");
                shareDataDTO.setSuperiorBrowse(map1);
                List<PutPostShareDataPO> list1 = putInResumeService.slipDay(shareDataQuery);
                Map<String, Long> map6 = timelyStatistics(list1);
                shareDataDTO.setSuperiorsSlipSUM(map6.get("counts"));
                map6.remove("counts");
                shareDataDTO.setSuperiorSlip(map6);

                if (shareDataQuery.getPostType() != 4) {
                    List<PutPostShareDataPO> interviewList1 = putInResumeService.interviewDay(shareDataQuery);
                    Map<String, Long> map7 = timelyStatistics(interviewList1);
                    shareDataDTO.setSuperiorsInterviewSUM(map7.get("counts"));
                    map7.remove("counts");
                    shareDataDTO.setSuperiorInterview(map7);

                }
                if (shareDataQuery.getPostType() != 2) {
                    List<PutPostShareDataPO> downloadDay1 = putInResumeService.downloadDay(shareDataQuery);
                    Map<String, Long> map7 = timelyStatistics(downloadDay1);
                    shareDataDTO.setSuperiorsDownloadSUM(map7.get("counts"));
                    map7.remove("counts");
                    shareDataDTO.setSuperiorDownload(map7);
                }
                if (shareDataQuery.getPostType() == 1 || shareDataQuery.getPostType() == 2) {
                    List<PutPostShareDataPO> offerDay1 = putInResumeService.offerDay(shareDataQuery);
                    Map<String, Long> map7 = timelyStatistics(offerDay1);
                    shareDataDTO.setSuperiorsOfferSUM(map7.get("counts"));
                    map7.remove("counts");
                    shareDataDTO.setSuperiorOffer(map7);

                    List<PutPostShareDataPO> entryDay = putInResumeService.entryDay(shareDataQuery);
                    Map<String, Long> map8 = timelyStatistics(entryDay);
                    shareDataDTO.setSuperiorsEntrySUM(map8.get("counts"));
                    map8.remove("counts");
                    shareDataDTO.setSuperiorEntry(map8);
                }
                if (shareDataQuery.getPostType() == 2) {
                    List<PutPostShareDataPO> fullMoonDay1 = putInResumeService.fullMoonDay(shareDataQuery);
                    Map<String, Long> map7 = timelyStatistics(fullMoonDay1);
                    shareDataDTO.setSuperiorsFullMoonSUM(map7.get("counts"));
                    map7.remove("counts");
                    shareDataDTO.setSuperiorFullMoon(map7);
                }
            }
        } else if (shareDataQuery.getDateType() == 1) {
       /*     if (shareDataQuery.getAllBeginDate() != null && shareDataQuery.getAllEndDate() != null) {
                LocalDate date1 = LocalDateTimeUtils.getLastMonOfWeek(shareDataQuery.getAllBeginDate(), shareDataQuery.getPageNo() - 1);
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String sunOfWeek = LocalDateTimeUtils.getSunOfWeek(date1.format(dateTimeFormatter));
                LocalDate date = DateUtils.StringToLocalDate(sunOfWeek);
                shareDataQuery.setEndDate(date);
                if (date.isAfter(shareDataQuery.getAllEndDate())) {
                    shareDataQuery.setEndDate(shareDataQuery.getAllEndDate());
                }
                shareDataQuery.setBeginDate(date1);
                long between = ChronoUnit.WEEKS.between(shareDataQuery.getAllBeginDate(), shareDataQuery.getAllEndDate());
                shareDataDTO.setTotal(between);

            }*/
            //如果不是全部是时间选项则进入当天的选项
            if (shareDataQuery.getBeginDate() == null || shareDataQuery.getEndDate() == null) {
                return R.fail("请选择周期!");
            }
            System.out.println(shareDataQuery.getBeginDate() + "" + shareDataQuery.getEndDate());
            Map<String, List<PutPostShareDataPO>> map2 = orderReceivingApi.orderReceivingWeek(companyMetadata.userid(), shareDataQuery.getBeginDate(), shareDataQuery.getEndDate(), shareDataQuery.getCity(), shareDataQuery.getPostType());
            List<PutPostShareDataPO> putPostShareDataPOS1 = map2.get("A");
            Map<String, Long> map3 = weekTimelyStatistics(putPostShareDataPOS1, shareDataQuery.getBeginDate());
            shareDataDTO.setOrderTakingSUM(map3.get("counts"));
            map3.remove("counts");
            shareDataDTO.setOrderTaking(map3);


            List<PutPostShareDataPO> putInResumeList = putInResumeService.browseWeeks(shareDataQuery);
            Map<String, Long> map = weekTimelyStatistics(putInResumeList, shareDataQuery.getBeginDate());
            shareDataDTO.setBrowseSUM(map.get("counts"));
            map.remove("counts");
            shareDataDTO.setBrowse(map);

            List<PutPostShareDataPO> list = putInResumeService.slipWeeks(shareDataQuery);
            Map<String, Long> map4 = weekTimelyStatistics(list, shareDataQuery.getBeginDate());
            shareDataDTO.setSlipSUM(map4.get("counts"));
            map4.remove("counts");
            shareDataDTO.setSlip(map4);


            if (shareDataQuery.getPostType() != 4) {

                List<PutPostShareDataPO> interviewList = putInResumeService.interviewWeeks(shareDataQuery);
                Map<String, Long> map1 = weekTimelyStatistics(interviewList, shareDataQuery.getBeginDate());
                shareDataDTO.setInterviewSUM(map1.get("counts"));
                map1.remove("counts");
                shareDataDTO.setInterview(map1);
            }

            if (shareDataQuery.getPostType() != 2) {

                List<PutPostShareDataPO> downloadDay = putInResumeService.downloadWeeks(shareDataQuery);
                Map<String, Long> map1 = weekTimelyStatistics(downloadDay, shareDataQuery.getBeginDate());
                shareDataDTO.setInterviewSUM(map1.get("counts"));
                map1.remove("counts");
                shareDataDTO.setDownload(map1);
            }
            if (shareDataQuery.getPostType() == 1 || shareDataQuery.getPostType() == 2) {
                List<PutPostShareDataPO> offerDay = putInResumeService.offerWeeks(shareDataQuery);
                Map<String, Long> map1 = weekTimelyStatistics(offerDay, shareDataQuery.getBeginDate());
                shareDataDTO.setOfferSUM(map1.get("counts"));
                map1.remove("counts");
                shareDataDTO.setOffer(map1);
                List<PutPostShareDataPO> entryDay = putInResumeService.entryWeeks(shareDataQuery);
                Map<String, Long> map8 = weekTimelyStatistics(entryDay, shareDataQuery.getBeginDate());
                shareDataDTO.setEntrySUM(map8.get("counts"));
                map8.remove("counts");
                shareDataDTO.setEntry(map8);
            }
            if (shareDataQuery.getPostType() == 2) {
                List<PutPostShareDataPO> fullMoonDay = putInResumeService.fullMoonWeeks(shareDataQuery);
                Map<String, Long> map1 = weekTimelyStatistics(fullMoonDay, shareDataQuery.getBeginDate());
                shareDataDTO.setFullMoonSUM(map1.get("counts"));
                map1.remove("counts");
                shareDataDTO.setFullMoon(map1);

            }

            if (shareDataQuery.getAllBeginDate() == null && shareDataQuery.getAllEndDate() == null) {
                LocalDate beginDate = shareDataQuery.getBeginDate();
                shareDataQuery.setBeginDate(LocalDateTimeUtils.lastWeekStartTime(beginDate));
                shareDataQuery.setEndDate(LocalDateTimeUtils.lastWeekEndTime(beginDate));

                List<PutPostShareDataPO> putPostShareDataPOS2 = map2.get("B");
                Map<String, Long> map5 = weekTimelyStatistics(putPostShareDataPOS2, shareDataQuery.getBeginDate());
                shareDataDTO.setSuperiorsOrderTakingSUM(map5.get("counts"));
                map5.remove("counts");
                shareDataDTO.setSuperiorOrderTaking(map5);

                List<PutPostShareDataPO> putInResumeList1 = putInResumeService.browseWeeks(shareDataQuery);
                Map<String, Long> map1 = weekTimelyStatistics(putInResumeList1, shareDataQuery.getBeginDate());
                shareDataDTO.setSuperiorsBrowseSUM(map1.get("counts"));
                map1.remove("counts");
                shareDataDTO.setSuperiorBrowse(map1);
                List<PutPostShareDataPO> list1 = putInResumeService.slipWeeks(shareDataQuery);
                Map<String, Long> map6 = weekTimelyStatistics(list1, shareDataQuery.getBeginDate());
                shareDataDTO.setSuperiorsSlipSUM(map6.get("counts"));
                map6.remove("counts");
                shareDataDTO.setSuperiorSlip(map6);


                if (shareDataQuery.getPostType() != 4) {
                    List<PutPostShareDataPO> interviewList1 = putInResumeService.interviewWeeks(shareDataQuery);
                    Map<String, Long> map7 = weekTimelyStatistics(interviewList1, shareDataQuery.getBeginDate());
                    shareDataDTO.setSuperiorsInterviewSUM(map7.get("counts"));
                    map7.remove("counts");
                    shareDataDTO.setSuperiorInterview(map7);
                }
                if (shareDataQuery.getPostType() != 2) {
                    List<PutPostShareDataPO> downloadDay1 = putInResumeService.downloadWeeks(shareDataQuery);
                    Map<String, Long> map7 = weekTimelyStatistics(downloadDay1, shareDataQuery.getBeginDate());
                    shareDataDTO.setSuperiorsDownloadSUM(map7.get("counts"));
                    map7.remove("counts");
                    shareDataDTO.setSuperiorDownload(map7);

                }
                if (shareDataQuery.getPostType() == 1 || shareDataQuery.getPostType() == 2) {
                    List<PutPostShareDataPO> offerDay1 = putInResumeService.offerWeeks(shareDataQuery);
                    Map<String, Long> map7 = weekTimelyStatistics(offerDay1, shareDataQuery.getBeginDate());
                    shareDataDTO.setSuperiorsOfferSUM(map7.get("counts"));
                    map7.remove("counts");
                    shareDataDTO.setSuperiorOffer(map7);

                    List<PutPostShareDataPO> entryDay = putInResumeService.entryWeeks(shareDataQuery);
                    Map<String, Long> map8 = weekTimelyStatistics(entryDay, shareDataQuery.getBeginDate());
                    shareDataDTO.setSuperiorsEntrySUM(map8.get("counts"));
                    map8.remove("counts");
                    shareDataDTO.setSuperiorEntry(map8);
                }
                if (shareDataQuery.getPostType() == 2) {
                    List<PutPostShareDataPO> fullMoonDay1 = putInResumeService.fullMoonWeeks(shareDataQuery);
                    Map<String, Long> map7 = weekTimelyStatistics(fullMoonDay1, shareDataQuery.getBeginDate());
                    shareDataDTO.setSuperiorsFullMoonSUM(map7.get("counts"));
                    map7.remove("counts");
                    shareDataDTO.setSuperiorFullMoon(map7);
                }

            }


        } else if (shareDataQuery.getDateType() == 2) {
/*            if (shareDataQuery.getAllBeginDate() != null && shareDataQuery.getAllEndDate() != null) {
                LocalDate nextWeek = shareDataQuery.getAllBeginDate().plus(shareDataQuery.getPageNo() - 1, ChronoUnit.MONTHS);
                long between = ChronoUnit.MONTHS.between(shareDataQuery.getAllBeginDate(), shareDataQuery.getAllEndDate());
                shareDataDTO.setTotal(between);
                shareDataQuery.setMonth(nextWeek);
            }*/
            //如果不是全部是时间选项则进入当天的选项
            if (shareDataQuery.getMonth() == null) {
                return R.fail("请选择月份!");
            }
            shareDataQuery.setBeginDate(LocalDateTimeUtils.monthStartTime(shareDataQuery.getMonth()));
            shareDataQuery.setEndDate(LocalDateTimeUtils.monthEndTime(shareDataQuery.getMonth()));
            Map<String, List<PutPostShareDataPO>> map2 = orderReceivingApi.orderReceivingMonth(companyMetadata.userid(), shareDataQuery.getBeginDate(), shareDataQuery.getEndDate(), shareDataQuery.getCity(), shareDataQuery.getPostType());
            List<PutPostShareDataPO> putPostShareDataPOS1 = map2.get("A");
            Map<String, Long> map3 = monthTimelyStatistics(putPostShareDataPOS1, DateUtils.LocalDateToString(shareDataQuery.getBeginDate()));
            shareDataDTO.setOrderTakingSUM(map3.get("counts"));
            map3.remove("counts");
            shareDataDTO.setOrderTaking(map3);

            List<PutPostShareDataPO> putInResumeList = putInResumeService.browseWeeks(shareDataQuery);
            Map<String, Long> map = monthTimelyStatistics(putInResumeList, DateUtils.LocalDateToString(shareDataQuery.getBeginDate()));
            shareDataDTO.setBrowseSUM(map.get("counts"));
            map.remove("counts");
            shareDataDTO.setBrowse(map);
            List<PutPostShareDataPO> list = putInResumeService.slipWeeks(shareDataQuery);
            Map<String, Long> map4 = monthTimelyStatistics(list, DateUtils.LocalDateToString(shareDataQuery.getBeginDate()));
            shareDataDTO.setSlipSUM(map4.get("counts"));
            map4.remove("counts");
            shareDataDTO.setSlip(map4);
            if (shareDataQuery.getPostType() != 4) {
                List<PutPostShareDataPO> interviewList = putInResumeService.interviewWeeks(shareDataQuery);
                Map<String, Long> map1 = monthTimelyStatistics(interviewList, DateUtils.LocalDateToString(shareDataQuery.getBeginDate()));
                shareDataDTO.setInterviewSUM(map1.get("counts"));
                map1.remove("counts");
                shareDataDTO.setInterview(map1);
            }
            if (shareDataQuery.getPostType() != 2) {
                List<PutPostShareDataPO> downloadDay = putInResumeService.downloadWeeks(shareDataQuery);
                Map<String, Long> map1 = monthTimelyStatistics(downloadDay, DateUtils.LocalDateToString(shareDataQuery.getBeginDate()));
                shareDataDTO.setDownloadSUM(map1.get("counts"));
                map1.remove("counts");
                shareDataDTO.setDownload(map1);
            }
            if (shareDataQuery.getPostType() == 1 || shareDataQuery.getPostType() == 2) {
                List<PutPostShareDataPO> offerDay = putInResumeService.offerWeeks(shareDataQuery);
                Map<String, Long> map1 = monthTimelyStatistics(offerDay, DateUtils.LocalDateToString(shareDataQuery.getBeginDate()));
                shareDataDTO.setOfferSUM(map1.get("counts"));
                map1.remove("counts");
                shareDataDTO.setOffer(map1);
                List<PutPostShareDataPO> entryDay = putInResumeService.entryWeeks(shareDataQuery);
                Map<String, Long> map8 = monthTimelyStatistics(entryDay, DateUtils.LocalDateToString(shareDataQuery.getBeginDate()));
                shareDataDTO.setEntrySUM(map8.get("counts"));
                map8.remove("counts");
                shareDataDTO.setEntry(map8);

            }
            if (shareDataQuery.getPostType() == 2) {
                List<PutPostShareDataPO> fullMoonDay = putInResumeService.fullMoonWeeks(shareDataQuery);
                Map<String, Long> map1 = monthTimelyStatistics(fullMoonDay, DateUtils.LocalDateToString(shareDataQuery.getBeginDate()));
                shareDataDTO.setFullMoonSUM(map1.get("counts"));
                map1.remove("counts");
                shareDataDTO.setFullMoon(map1);
            }

            if (shareDataQuery.getAllBeginDate() == null && shareDataQuery.getAllEndDate() == null) {
                shareDataQuery.setBeginDate(LocalDateTimeUtils.lastMonthStartTime(shareDataQuery.getMonth()));
                shareDataQuery.setEndDate(LocalDateTimeUtils.lastMonthEndTime(shareDataQuery.getMonth()));
                List<PutPostShareDataPO> putPostShareDataPOS2 = map2.get("B");
                Map<String, Long> map5 = monthTimelyStatistics(putPostShareDataPOS2, DateUtils.LocalDateToString(shareDataQuery.getBeginDate()));
                shareDataDTO.setSuperiorsOrderTakingSUM(map5.get("counts"));
                map5.remove("counts");
                shareDataDTO.setSuperiorOrderTaking(map5);
                List<PutPostShareDataPO> putInResumeList1 = putInResumeService.browseWeeks(shareDataQuery);
                Map<String, Long> map1 = monthTimelyStatistics(putInResumeList1, DateUtils.LocalDateToString(shareDataQuery.getBeginDate()));
                shareDataDTO.setSuperiorsBrowseSUM(map1.get("counts"));
                map1.remove("counts");
                shareDataDTO.setSuperiorBrowse(map1);
                List<PutPostShareDataPO> list1 = putInResumeService.slipWeeks(shareDataQuery);
                Map<String, Long> map6 = monthTimelyStatistics(list1, DateUtils.LocalDateToString(shareDataQuery.getBeginDate()));
                shareDataDTO.setSuperiorsSlipSUM(map6.get("counts"));
                map6.remove("counts");
                shareDataDTO.setSuperiorSlip(map6);

                if (shareDataQuery.getPostType() != 4) {
                    List<PutPostShareDataPO> interviewList1 = putInResumeService.interviewWeeks(shareDataQuery);
                    Map<String, Long> map7 = monthTimelyStatistics(interviewList1, DateUtils.LocalDateToString(shareDataQuery.getBeginDate()));
                    shareDataDTO.setSuperiorsInterviewSUM(map7.get("counts"));
                    map7.remove("counts");
                    shareDataDTO.setSuperiorInterview(map7);
                }
                if (shareDataQuery.getPostType() != 2) {
                    List<PutPostShareDataPO> downloadDay1 = putInResumeService.downloadWeeks(shareDataQuery);
                    Map<String, Long> map7 = monthTimelyStatistics(downloadDay1, DateUtils.LocalDateToString(shareDataQuery.getBeginDate()));
                    shareDataDTO.setSuperiorsDownloadSUM(map7.get("counts"));
                    map7.remove("counts");
                    shareDataDTO.setSuperiorDownload(map7);
                }


                if (shareDataQuery.getPostType() == 1 || shareDataQuery.getPostType() == 2) {
                    List<PutPostShareDataPO> offerDay1 = putInResumeService.offerWeeks(shareDataQuery);
                    Map<String, Long> map7 = monthTimelyStatistics(offerDay1, DateUtils.LocalDateToString(shareDataQuery.getBeginDate()));
                    shareDataDTO.setSuperiorsOfferSUM(map7.get("counts"));
                    map7.remove("counts");
                    shareDataDTO.setSuperiorOffer(map7);
                    List<PutPostShareDataPO> entryDay = putInResumeService.entryWeeks(shareDataQuery);
                    Map<String, Long> map8 = monthTimelyStatistics(entryDay, DateUtils.LocalDateToString(shareDataQuery.getBeginDate()));
                    shareDataDTO.setSuperiorsEntrySUM(map8.get("counts"));
                    map8.remove("counts");
                    shareDataDTO.setSuperiorEntry(map8);
                }
                if (shareDataQuery.getPostType() == 2) {
                    List<PutPostShareDataPO> fullMoonDay1 = putInResumeService.fullMoonWeeks(shareDataQuery);
                    Map<String, Long> map7 = monthTimelyStatistics(fullMoonDay1, DateUtils.LocalDateToString(shareDataQuery.getBeginDate()));
                    shareDataDTO.setSuperiorsFullMoonSUM(map7.get("counts"));
                    map7.remove("counts");
                    shareDataDTO.setSuperiorFullMoon(map7);
                }
            }


        }


        if (shareDataQuery.getAllBeginDate() != null && shareDataQuery.getAllEndDate() != null) {
            shareDataQuery.setDay(LocalDate.now());

            Map<String, List<PutPostShareDataPO>> map2 = orderReceivingApi.orderReceivingDay(companyMetadata.userid(), shareDataQuery.getDay(), shareDataQuery.getCity(), shareDataQuery.getPostType());
            List<PutPostShareDataPO> putPostShareDataPOS1 = map2.get("A");
            Map<String, Long> map3 = timelyStatistics(putPostShareDataPOS1);
            Long counts = map3.get("counts");
            shareDataDTO.setOrderTakingSUM(counts);
            List<PutPostShareDataPO> putInResumeList = putInResumeService.browseDay(shareDataQuery);
            Map<String, Long> map = timelyStatistics(putInResumeList);
            shareDataDTO.setBrowseSUM(map.get("counts"));
            List<PutPostShareDataPO> list = putInResumeService.slipDay(shareDataQuery);
            Map<String, Long> map4 = timelyStatistics(list);
            shareDataDTO.setSlipSUM(map4.get("counts"));
            if (shareDataQuery.getPostType() != 4) {
                List<PutPostShareDataPO> interviewList = putInResumeService.interviewDay(shareDataQuery);
                Map<String, Long> map1 = timelyStatistics(interviewList);
                shareDataDTO.setInterviewSUM(map1.get(counts));
            }
            if (shareDataQuery.getPostType() != 2) {
                List<PutPostShareDataPO> downloadDay = putInResumeService.downloadDay(shareDataQuery);
                Map<String, Long> map1 = timelyStatistics(downloadDay);
                shareDataDTO.setDownloadSUM(map1.get("counts"));
            }
            if (shareDataQuery.getPostType() == 1 || shareDataQuery.getPostType() == 2) {
                List<PutPostShareDataPO> offerDay = putInResumeService.offerDay(shareDataQuery);
                Map<String, Long> map1 = timelyStatistics(offerDay);
                shareDataDTO.setOfferSUM(map1.get("counts"));
            }
            if (shareDataQuery.getPostType() == 2) {
                List<PutPostShareDataPO> fullMoonDay = putInResumeService.fullMoonDay(shareDataQuery);
                Map<String, Long> map1 = timelyStatistics(fullMoonDay);
                shareDataDTO.setFullMoonSUM(map1.get("counts"));
            }
            shareDataQuery.setDay(shareDataQuery.getDay().plusDays(-1));
            List<PutPostShareDataPO> putPostShareDataPOS2 = map2.get("B");
            Map<String, Long> map5 = timelyStatistics(putPostShareDataPOS2);
            shareDataDTO.setSuperiorsOrderTakingSUM(map5.get("counts"));
            List<PutPostShareDataPO> putInResumeList1 = putInResumeService.browseDay(shareDataQuery);
            Map<String, Long> map1 = timelyStatistics(putInResumeList1);
            shareDataDTO.setSuperiorsBrowseSUM(map1.get("counts"));

            List<PutPostShareDataPO> list1 = putInResumeService.slipDay(shareDataQuery);
            Map<String, Long> map6 = timelyStatistics(list1);
            shareDataDTO.setSuperiorsSlipSUM(map6.get("counts"));
            if (shareDataQuery.getPostType() != 4) {
                List<PutPostShareDataPO> interviewList1 = putInResumeService.interviewDay(shareDataQuery);
                Map<String, Long> map7 = timelyStatistics(interviewList1);
                shareDataDTO.setSuperiorsInterviewSUM(map7.get("counts"));
            }
            if (shareDataQuery.getPostType() != 2) {
                List<PutPostShareDataPO> downloadDay1 = putInResumeService.downloadDay(shareDataQuery);
                Map<String, Long> map7 = timelyStatistics(downloadDay1);
                shareDataDTO.setSuperiorsDownloadSUM(map7.get("counts"));
            }
            if (shareDataQuery.getPostType() == 1 || shareDataQuery.getPostType() == 2) {
                List<PutPostShareDataPO> offerDay1 = putInResumeService.offerDay(shareDataQuery);
                Map<String, Long> map7 = timelyStatistics(offerDay1);
                shareDataDTO.setSuperiorsOfferSUM(map7.get("counts"));
            }
            if (shareDataQuery.getPostType() == 2) {
                List<PutPostShareDataPO> fullMoonDay1 = putInResumeService.fullMoonDay(shareDataQuery);
                Map<String, Long> map7 = timelyStatistics(fullMoonDay1);
                shareDataDTO.setSuperiorsFullMoonSUM(map7.get("counts"));
            }
        }
        return R.success(shareDataDTO);
    }

    public Map<String, Long> timelyStatistics(List<PutPostShareDataPO> putInResumeList) {

        Map<String, Long> second = createMap(7);
        Long sum = 0L;
        if (putInResumeList == null) {
            return second;
        }
        for (PutPostShareDataPO putPostShareDataPO : putInResumeList) {
            sum = sum + putPostShareDataPO.getCounts();
            LocalDateTime createTime = putPostShareDataPO.getTime();
            int hour = createTime.getHour();
            if (hour <= 4) {
                String A = "A";
                Long counts = second.get(A);
                counts = counts + putPostShareDataPO.getCounts();
                second.put(A, counts);
            } else if (hour > 4 && hour <= 8) {
                String B = "B";
                Long counts = second.get(B);
                counts = counts + putPostShareDataPO.getCounts();
                second.put(B, counts);
            } else if (hour > 8 && hour <= 12) {
                String C = "C";
                Long counts = second.get(C);
                counts = counts + putPostShareDataPO.getCounts();
                second.put(C, counts);
            } else if (hour > 12 && hour <= 16) {
                String D = "D";
                Long counts = second.get(D);
                counts = counts + putPostShareDataPO.getCounts();
                second.put(D, counts);
            } else if (hour > 16 && hour <= 20) {
                String E = "E";
                Long counts = second.get(E);
                counts = counts + putPostShareDataPO.getCounts();
                second.put(E, counts);
            } else if (hour > 20 && hour <= 24) {
                String F = "F";
                Long counts = second.get(F);
                counts = counts + putPostShareDataPO.getCounts();
                second.put(F, counts);
            }
        }

        second.put("counts", sum);
        return second;

    }

    //周工具类
    public Map<String, Long> weekTimelyStatistics(List<PutPostShareDataPO> shareData, LocalDate beginDate) throws ParseException {
        Map<String, Long> second = createMap(7);
        Long sum = 0L;
        for (PutPostShareDataPO shareDatum : shareData) {
            sum = sum + shareDatum.getCounts();
            if (LocalDateTimeUtils.isSameDay(shareDatum.getTime(), 0, beginDate)) {
                String A = "A";

                Long secondCount = second.get(A);
                secondCount = secondCount + shareDatum.getCounts();
                second.put(A, secondCount);

            } else if (LocalDateTimeUtils.isSameDay(shareDatum.getTime(), 1, beginDate)) {
                String B = "B";
                Long secondCount = second.get(B);
                secondCount = secondCount + shareDatum.getCounts();
                second.put(B, secondCount);
            } else if (LocalDateTimeUtils.isSameDay(shareDatum.getTime(), 2, beginDate)) {
                String C = "C";
                Long secondCount = second.get(C);
                secondCount = secondCount + shareDatum.getCounts();
                second.put(C, secondCount);
            } else if (LocalDateTimeUtils.isSameDay(shareDatum.getTime(), 3, beginDate)) {
                String D = "D";
                Long secondCount = second.get(D);
                secondCount = secondCount + shareDatum.getCounts();
                second.put(D, secondCount);
            } else if (LocalDateTimeUtils.isSameDay(shareDatum.getTime(), 4, beginDate)) {
                String E = "E";
                Long secondCount = second.get(E);
                secondCount = secondCount + shareDatum.getCounts();
                second.put(E, secondCount);
            } else if (LocalDateTimeUtils.isSameDay(shareDatum.getTime(), 5, beginDate)) {
                String F = "F";
                Long secondCount = second.get(F);
                secondCount = secondCount + shareDatum.getCounts();
                second.put(F, secondCount);
            } else if (LocalDateTimeUtils.isSameDay(shareDatum.getTime(), 6, beginDate)) {
                String G = "G";
                Long secondCount = second.get(G);
                secondCount = secondCount + shareDatum.getCounts();
                second.put(G, secondCount);

            }
        }
        second.put("counts", sum);
        return second;
    }

    /**
     * 年工具
     *
     * @param shareData
     * @return
     */
    public Map<String, Long> yearUtils1(List<PutPostShareDataPO> shareData, LocalDate beginDate, LocalDate endDate) {
        Map<String, Long> sum = createMap(7);

        List<Map> scope = LocalDateTimeUtils.getMonthScope(beginDate, endDate);
        Map mapOne = new HashMap(3);
        Map mapTwo = new HashMap(3);
        Map mapThree = new HashMap(3);
        Map mapFour = new HashMap(3);
        Map mapFive = new HashMap(3);
        Map mapSix = new HashMap(3);
        // Map mapSeven = new HashMap(3);
        for (Map map : scope) {
            String order1 = map.get("order").toString();
            Integer order = Integer.parseInt(order1);
            if (order == 0) {
                mapOne = map;
            } else if (order == 1) {
                mapTwo = map;
            } else if (order == 2) {
                mapThree = map;
            } else if (order == 3) {
                mapFour = map;
            } else if (order == 4) {
                mapFive = map;
            } else if (order == 5) {
                mapSix = map;
            } else if (order == 6) {
                //  mapSeven = map;
            }
        }

        Long count = 0L;
        for (PutPostShareDataPO shareDatum : shareData) {

            count = count + shareDatum.getCounts();
            if (DateUtils.isEffectiveDateBoolean(LocalDate.from(shareDatum.getTime()), DateUtils.StringToLocalDate(mapOne.get("start").toString()), DateUtils.StringToLocalDate(mapOne.get("end").toString()))) {
                String A = "A";
                Long sums = sum.get(A);
                sums = sums + shareDatum.getCounts();
                sum.put(A, sums);

            } else if (DateUtils.isEffectiveDateBoolean(LocalDate.from(shareDatum.getTime()), DateUtils.StringToLocalDate(mapTwo.get("start").toString()), DateUtils.StringToLocalDate(mapTwo.get("end").toString()))) {
                String B = "B";
                Long sums = sum.get(B);
                sums = sums + shareDatum.getCounts();
                sum.put(B, sums);
            } else if (DateUtils.isEffectiveDateBoolean(LocalDate.from(shareDatum.getTime()), DateUtils.StringToLocalDate(mapThree.get("start").toString()), DateUtils.StringToLocalDate(mapThree.get("end").toString()))) {
                String C = "C";
                Long sums = sum.get(C);
                sums = sums + shareDatum.getCounts();
                sum.put(C, sums);
            } else if (DateUtils.isEffectiveDateBoolean(LocalDate.from(shareDatum.getTime()), DateUtils.StringToLocalDate(mapFour.get("start").toString()), DateUtils.StringToLocalDate(mapFour.get("end").toString()))) {
                String D = "D";
                Long sums = sum.get(D);
                sums = sums + shareDatum.getCounts();
                sum.put(D, sums);
            } else if (DateUtils.isEffectiveDateBoolean(LocalDate.from(shareDatum.getTime()), DateUtils.StringToLocalDate(mapFive.get("start").toString()), DateUtils.StringToLocalDate(mapFive.get("end").toString()))) {
                String E = "E";
                Long sums = sum.get(E);
                sums = sums + shareDatum.getCounts();
                sum.put(E, sums);
            } else if (DateUtils.isEffectiveDateBoolean(LocalDate.from(shareDatum.getTime()), DateUtils.StringToLocalDate(mapSix.get("start").toString()), DateUtils.StringToLocalDate(mapSix.get("end").toString()))) {
                String F = "F";
                Long sums = sum.get(F);
                sums = sums + shareDatum.getCounts();
                sum.put(F, sums);
            } /*else if (DateUtils.isEffectiveDateBoolean(LocalDate.from(shareDatum.getTime()), DateUtils.StringToLocalDate(mapSeven.get("start").toString()), DateUtils.StringToLocalDate(mapSeven.get("end").toString()))) {
                String G = "G";
                Long sums = sum.get(G);
                sums = sums + shareDatum.getCounts();
                sum.put(G, sums);
            }*/

        }

        // sum.put("count", count);
        return sum;
    }

    //周工具类
    public Map<String, Long> monthTimelyStatistics1(List<PutPostShareDataPO> shareData, LocalDate date) {

        Map<String, Long> sum = createMap(7);

        List<Map> scope = LocalDateTimeUtils.getWeek(date, 6);
        Map mapOne = new HashMap(3);
        Map mapTwo = new HashMap(3);
        Map mapThree = new HashMap(3);
        Map mapFour = new HashMap(3);
        Map mapFive = new HashMap(3);
        Map mapSix = new HashMap(3);
        // Map mapSeven = new HashMap(3);
        for (Map map : scope) {
            String order1 = map.get("order").toString();
            Integer order = Integer.parseInt(order1);
            if (order == 0) {
                mapOne = map;
            } else if (order == 1) {
                mapTwo = map;
            } else if (order == 2) {
                mapThree = map;
            } else if (order == 3) {
                mapFour = map;
            } else if (order == 4) {
                mapFive = map;
            } else if (order == 5) {
                mapSix = map;
            } else if (order == 6) {
                //  mapSeven = map;
            }
        }

        Long count = 0L;
        for (PutPostShareDataPO shareDatum : shareData) {

            count = count + shareDatum.getCounts();
            if (DateUtils.isEffectiveDateBoolean(LocalDate.from(shareDatum.getTime()), DateUtils.StringToLocalDate(mapOne.get("start").toString()), DateUtils.StringToLocalDate(mapOne.get("end").toString()))) {
                String A = "A";
                Long sums = sum.get(A);
                sums = sums + shareDatum.getCounts();
                sum.put(A, sums);

            } else if (DateUtils.isEffectiveDateBoolean(LocalDate.from(shareDatum.getTime()), DateUtils.StringToLocalDate(mapTwo.get("start").toString()), DateUtils.StringToLocalDate(mapTwo.get("end").toString()))) {
                String B = "B";
                Long sums = sum.get(B);
                sums = sums + shareDatum.getCounts();
                sum.put(B, sums);
            } else if (DateUtils.isEffectiveDateBoolean(LocalDate.from(shareDatum.getTime()), DateUtils.StringToLocalDate(mapThree.get("start").toString()), DateUtils.StringToLocalDate(mapThree.get("end").toString()))) {
                String C = "C";
                Long sums = sum.get(C);
                sums = sums + shareDatum.getCounts();
                sum.put(C, sums);
            } else if (DateUtils.isEffectiveDateBoolean(LocalDate.from(shareDatum.getTime()), DateUtils.StringToLocalDate(mapFour.get("start").toString()), DateUtils.StringToLocalDate(mapFour.get("end").toString()))) {
                String D = "D";
                Long sums = sum.get(D);
                sums = sums + shareDatum.getCounts();
                sum.put(D, sums);
            } else if (DateUtils.isEffectiveDateBoolean(LocalDate.from(shareDatum.getTime()), DateUtils.StringToLocalDate(mapFive.get("start").toString()), DateUtils.StringToLocalDate(mapFive.get("end").toString()))) {
                String E = "E";
                Long sums = sum.get(E);
                sums = sums + shareDatum.getCounts();
                sum.put(E, sums);
            } else if (DateUtils.isEffectiveDateBoolean(LocalDate.from(shareDatum.getTime()), DateUtils.StringToLocalDate(mapSix.get("start").toString()), DateUtils.StringToLocalDate(mapSix.get("end").toString()))) {
                String F = "F";
                Long sums = sum.get(F);
                sums = sums + shareDatum.getCounts();
                sum.put(F, sums);
            } /*else if (DateUtils.isEffectiveDateBoolean(LocalDate.from(shareDatum.getTime()), DateUtils.StringToLocalDate(mapSeven.get("start").toString()), DateUtils.StringToLocalDate(mapSeven.get("end").toString()))) {
                String G = "G";
                Long sums = sum.get(G);
                sums = sums + shareDatum.getCounts();
                sum.put(G, sums);
            }*/

        }

        // sum.put("count", count);
        return sum;
    }

    //周工具类
    public Map<String, Long> monthTimelyStatistics(List<PutPostShareDataPO> shareData, String month) {

        Map<String, Long> sum = createMap(7);

        List<Map> scope = LocalDateTimeUtils.getScope(month);
        Map mapOne = new HashMap(3);
        Map mapTwo = new HashMap(3);
        Map mapThree = new HashMap(3);
        Map mapFour = new HashMap(3);
        Map mapFive = new HashMap(3);
        Map mapSix = new HashMap(3);
        Map mapSeven = new HashMap(3);
        for (Map map : scope) {
            String order1 = map.get("order").toString();
            Integer order = Integer.parseInt(order1);
            if (order == 1) {
                mapOne = map;
            } else if (order == 2) {
                mapTwo = map;
            } else if (order == 3) {
                mapThree = map;
            } else if (order == 4) {
                mapFour = map;
            } else if (order == 5) {
                mapFive = map;
            } else if (order == 6) {
                mapSix = map;
            } else if (order == 7) {
                mapSeven = map;
            }
        }

        Long count = 0L;
        for (PutPostShareDataPO shareDatum : shareData) {

            count = count + shareDatum.getCounts();
            if (DateUtils.isEffectiveDateBoolean(LocalDate.from(shareDatum.getTime()), DateUtils.StringToLocalDate(mapOne.get("start").toString()), DateUtils.StringToLocalDate(mapOne.get("end").toString()))) {
                String A = "A";
                Long sums = sum.get(A);
                sums = sums + shareDatum.getCounts();
                sum.put(A, sums);

            } else if (DateUtils.isEffectiveDateBoolean(LocalDate.from(shareDatum.getTime()), DateUtils.StringToLocalDate(mapTwo.get("start").toString()), DateUtils.StringToLocalDate(mapTwo.get("end").toString()))) {
                String B = "B";
                Long sums = sum.get(B);
                sums = sums + shareDatum.getCounts();
                sum.put(B, sums);
            } else if (DateUtils.isEffectiveDateBoolean(LocalDate.from(shareDatum.getTime()), DateUtils.StringToLocalDate(mapThree.get("start").toString()), DateUtils.StringToLocalDate(mapThree.get("end").toString()))) {
                String C = "C";
                Long sums = sum.get(C);
                sums = sums + shareDatum.getCounts();
                sum.put(C, sums);
            } else if (DateUtils.isEffectiveDateBoolean(LocalDate.from(shareDatum.getTime()), DateUtils.StringToLocalDate(mapFour.get("start").toString()), DateUtils.StringToLocalDate(mapFour.get("end").toString()))) {
                String D = "D";
                Long sums = sum.get(D);
                sums = sums + shareDatum.getCounts();
                sum.put(D, sums);
            } else if (DateUtils.isEffectiveDateBoolean(LocalDate.from(shareDatum.getTime()), DateUtils.StringToLocalDate(mapFive.get("start").toString()), DateUtils.StringToLocalDate(mapFive.get("end").toString()))) {
                String E = "E";
                Long sums = sum.get(E);
                sums = sums + shareDatum.getCounts();
                sum.put(E, sums);
            } else if (DateUtils.isEffectiveDateBoolean(LocalDate.from(shareDatum.getTime()), DateUtils.StringToLocalDate(mapSix.get("start").toString()), DateUtils.StringToLocalDate(mapSix.get("end").toString()))) {
                String F = "F";
                Long sums = sum.get(F);
                sums = sums + shareDatum.getCounts();
                sum.put(F, sums);
            } else if (DateUtils.isEffectiveDateBoolean(LocalDate.from(shareDatum.getTime()), DateUtils.StringToLocalDate(mapSeven.get("start").toString()), DateUtils.StringToLocalDate(mapSeven.get("end").toString()))) {
                String G = "G";
                Long sums = sum.get(G);
                sums = sums + shareDatum.getCounts();
                sum.put(G, sums);
            }

        }

        sum.put("counts", count);
        return sum;
    }


    //周工具类
    public Map<String, Map<String, Long>> yearUtils(List<ShareData> shareData, LocalDate beginDate, LocalDate endDate) {
        List<Map> scope = LocalDateTimeUtils.getMonthScope(beginDate, endDate);
        Map mapOne = new HashMap(3);
        Map mapTwo = new HashMap(3);
        Map mapThree = new HashMap(3);
        Map mapFour = new HashMap(3);
        Map mapFive = new HashMap(3);
        Map mapSix = new HashMap(3);
        Map mapSeven = new HashMap(3);
        for (Map map : scope) {
            String order1 = map.get("order").toString();
            Integer order = Integer.parseInt(order1);
            if (order == 1) {
                mapOne = map;
            } else if (order == 2) {
                mapTwo = map;
            } else if (order == 3) {
                mapThree = map;
            } else if (order == 4) {
                mapFour = map;
            } else if (order == 5) {
                mapFive = map;
            } else if (order == 6) {
                mapSix = map;
            } else if (order == 7) {
                mapSeven = map;
            }
        }
        Map<String, Long> ipAddress = createMap(7);
        Map<String, Long> second = createMap(7);
        Map<String, Long> post = createMap(7);
        Map<String, Long> visit = createMap(7);
        Map<String, Long> register = createMap(7);
        Map<String, Long> download = createMap(7);
        Map<String, Map<String, Long>> mapMap = new HashMap<>(7);
        Map<String, Long> count = new HashMap<>(6);
        Long ipAddressCount = 0L;
        Long secondCount = 0L;
        Long postCount = 0L;
        Long visitCount = 0L;
        Long registerCount = 0L;
        Long downloadCount = 0L;
        for (ShareData shareDatum : shareData) {
            //计算总量
            //ip
            ipAddressCount = ipAddressCount + shareDatum.getIpAddress();
            //访问量
            secondCount = secondCount + shareDatum.getSecond();
            //职位数
            postCount = postCount + shareDatum.getPostCount();
            //总时长
            visitCount = visitCount + shareDatum.getVisit();
            //注册量
            registerCount = registerCount + shareDatum.getRegister();
            //下载量
            downloadCount = downloadCount + shareDatum.getResumeDownload();


            LocalDate createTime = LocalDate.from(shareDatum.getCreateTime());


            if (DateUtils.isEffectiveDateBoolean(createTime, DateUtils.StringToLocalDate(mapOne.get("start").toString()), DateUtils.StringToLocalDate(mapOne.get("end").toString()))) {
                String A = "A";
                //ip地址数据
                Long ipAddressInteger = ipAddress.get(A);
                ipAddressInteger = ipAddressInteger + shareDatum.getIpAddress();
                ipAddress.put(A, ipAddressInteger);

                //访问时长
                Long secondInteger = second.get(A);
                secondInteger = secondInteger + shareDatum.getSecond();
                second.put(A, secondInteger);


                //职位分享数据
                Long postInteger = post.get(A);
                postInteger = postInteger + shareDatum.getPostCount();
                post.put(A, postInteger);


                //职位分享访问量
                Long visitInteger = visit.get(A);
                visitInteger = visitInteger + shareDatum.getVisit();
                visit.put(A, visitInteger);


                //注册
                Long registerInteger = register.get(A);
                registerInteger = registerInteger + shareDatum.getRegister();
                register.put(A, registerInteger);


                //下载
                Long downloadInteger = download.get(A);
                downloadInteger = downloadInteger + shareDatum.getResumeDownload();
                download.put(A, downloadInteger);
            } else if (DateUtils.isEffectiveDateBoolean(createTime, DateUtils.StringToLocalDate(mapTwo.get("start").toString()), DateUtils.StringToLocalDate(mapTwo.get("end").toString()))) {
                String B = "B";
                //ip地址数据
                Long ipAddressInteger = ipAddress.get(B);
                ipAddressInteger = ipAddressInteger + shareDatum.getIpAddress();
                ipAddress.put(B, ipAddressInteger);

                //访问时长
                Long secondInteger = second.get(B);
                secondInteger = secondInteger + shareDatum.getSecond();
                second.put(B, secondInteger);


                //职位分享数据
                Long postInteger = post.get(B);
                postInteger = postInteger + shareDatum.getPostCount();
                post.put(B, postInteger);


                //职位分享访问量
                Long visitInteger = visit.get(B);
                visitInteger = visitInteger + shareDatum.getVisit();
                visit.put(B, visitInteger);

                //注册
                Long registerInteger = register.get(B);
                registerInteger = registerInteger + shareDatum.getRegister();
                register.put(B, registerInteger);


                //下载
                Long downloadInteger = download.get(B);
                downloadInteger = downloadInteger + shareDatum.getResumeDownload();
                download.put(B, downloadInteger);
            } else if (DateUtils.isEffectiveDateBoolean(createTime, DateUtils.StringToLocalDate(mapThree.get("start").toString()), DateUtils.StringToLocalDate(mapThree.get("end").toString()))) {
                String C = "C";
                Long ipAddressInteger = ipAddress.get(C);
                ipAddressInteger = ipAddressInteger + shareDatum.getIpAddress();
                ipAddress.put(C, ipAddressInteger);


                //访问时长
                Long secondInteger = second.get(C);
                secondInteger = secondInteger + shareDatum.getSecond();
                second.put(C, secondInteger);


                //职位分享数据
                Long postInteger = post.get(C);
                postInteger = postInteger + shareDatum.getPostCount();
                post.put(C, postInteger);

                //职位分享访问量
                Long visitInteger = visit.get(C);
                visitInteger = visitInteger + shareDatum.getVisit();
                visit.put(C, visitInteger);


                //注册
                Long registerInteger = register.get(C);
                registerInteger = registerInteger + shareDatum.getRegister();
                register.put(C, registerInteger);

                //下载
                Long downloadInteger = download.get(C);
                downloadInteger = downloadInteger + shareDatum.getResumeDownload();
                download.put(C, downloadInteger);
            } else if (DateUtils.isEffectiveDateBoolean(createTime, DateUtils.StringToLocalDate(mapFour.get("start").toString()), DateUtils.StringToLocalDate(mapFour.get("end").toString()))) {
                String D = "D";
                Long ipAddressInteger = ipAddress.get(D);
                ipAddressInteger = ipAddressInteger + shareDatum.getIpAddress();
                ipAddress.put(D, ipAddressInteger);


                //访问时长
                Long secondInteger = second.get(D);
                secondInteger = secondInteger + shareDatum.getSecond();
                second.put(D, secondInteger);


                //职位分享数据
                Long postInteger = post.get(D);
                postInteger = postInteger + shareDatum.getPostCount();
                post.put(D, postInteger);


                //职位分享访问量
                Long visitInteger = visit.get(D);
                visitInteger = visitInteger + shareDatum.getVisit();
                visit.put(D, visitInteger);


                //注册
                Long registerInteger = register.get(D);
                registerInteger = registerInteger + shareDatum.getRegister();
                register.put(D, registerInteger);

                //下载
                Long downloadInteger = download.get(D);
                downloadInteger = downloadInteger + shareDatum.getResumeDownload();
                download.put(D, downloadInteger);
            } else if (DateUtils.isEffectiveDateBoolean(createTime, DateUtils.StringToLocalDate(mapFive.get("start").toString()), DateUtils.StringToLocalDate(mapFive.get("end").toString()))) {
                String E = "E";
                Long ipAddressInteger = ipAddress.get(E);
                ipAddressInteger = ipAddressInteger + shareDatum.getIpAddress();
                ipAddress.put(E, ipAddressInteger);


                //访问时长
                Long secondInteger = second.get(E);
                secondInteger = secondInteger + shareDatum.getSecond();
                second.put(E, secondInteger);


                //职位分享数据
                Long postInteger = post.get(E);
                postInteger = postInteger + shareDatum.getPostCount();
                post.put(E, postInteger);


                //职位分享访问量
                Long visitInteger = visit.get(E);
                visitInteger = visitInteger + shareDatum.getVisit();
                visit.put(E, visitInteger);

                //注册
                Long registerInteger = register.get(E);
                registerInteger = registerInteger + shareDatum.getRegister();
                register.put(E, registerInteger);

                //下载
                Long downloadInteger = download.get(E);
                downloadInteger = downloadInteger + shareDatum.getResumeDownload();
                download.put(E, downloadInteger);
            } else if (DateUtils.isEffectiveDateBoolean(createTime, DateUtils.StringToLocalDate(mapSix.get("start").toString()), DateUtils.StringToLocalDate(mapSix.get("end").toString()))) {
                String F = "F";
                Long ipAddressInteger = ipAddress.get(F);
                ipAddressInteger = ipAddressInteger + shareDatum.getIpAddress();
                ipAddress.put(F, ipAddressInteger);


                //访问时长
                Long secondInteger = second.get(F);
                secondInteger = secondInteger + shareDatum.getSecond();
                second.put(F, secondInteger);


                //职位分享数据
                Long postInteger = post.get(F);
                postInteger = postInteger + shareDatum.getPostCount();
                post.put(F, postInteger);


                //职位分享访问量
                Long visitInteger = visit.get(F);
                visitInteger = visitInteger + shareDatum.getVisit();
                visit.put(F, visitInteger);

                //注册
                Long registerInteger = register.get(F);
                registerInteger = registerInteger + shareDatum.getRegister();
                register.put(F, registerInteger);

                //下载
                Long downloadInteger = download.get(F);
                downloadInteger = downloadInteger + shareDatum.getResumeDownload();
                download.put(F, downloadInteger);
            } else if (DateUtils.isEffectiveDateBoolean(createTime, DateUtils.StringToLocalDate(mapSeven.get("start").toString()), DateUtils.StringToLocalDate(mapSeven.get("end").toString()))) {
                String G = "G";
                Long ipAddressInteger = ipAddress.get(G);
                ipAddressInteger = ipAddressInteger + shareDatum.getIpAddress();
                ipAddress.put(G, ipAddressInteger);


                //访问时长
                Long secondInteger = second.get(G);
                secondInteger = secondInteger + shareDatum.getSecond();
                second.put(G, secondInteger);


                //职位分享数据
                Long postInteger = post.get(G);
                postInteger = postInteger + shareDatum.getPostCount();
                post.put(G, postInteger);


                //职位分享访问量
                Long visitInteger = visit.get(G);
                visitInteger = visitInteger + shareDatum.getVisit();
                visit.put(G, visitInteger);

                //注册
                Long registerInteger = register.get(G);
                registerInteger = registerInteger + shareDatum.getRegister();
                register.put(G, registerInteger);

                //下载
                Long downloadInteger = download.get(G);
                downloadInteger = downloadInteger + shareDatum.getResumeDownload();
                download.put(G, downloadInteger);
            }

        }
        mapMap.put("ipAddress", ipAddress);
        mapMap.put("post", post);
        mapMap.put("visit", visit);
        mapMap.put("register", register);
        mapMap.put("download", download);
        mapMap.put("second", second);
        count.put("ipAddressCount", ipAddressCount);
        count.put("secondCount", secondCount);
        count.put("postCount", postCount);
        count.put("visitCount", visitCount);
        count.put("registerCount", registerCount);
        count.put("downloadCount", downloadCount);
        mapMap.put("count", count);
        return mapMap;
    }


}
