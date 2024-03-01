package com.wcwy.post.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;
import com.wawy.company.api.*;
import com.wawy.company.pojo.DeductGold;
import com.wcwy.common.base.result.R;
import com.wcwy.common.base.utils.DateUtils;
import com.wcwy.common.base.utils.OrderState;
import com.wcwy.common.redis.enums.*;
import com.wcwy.common.redis.util.IDGenerator;
import com.wawy.company.pojo.ProportionDTO;
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.common.web.annotation.AutoIdempotent;
import com.wcwy.company.dto.InviteEntryDTO;
import com.wcwy.company.entity.TJobhunter;
import com.wcwy.company.vo.ProportionVO;
import com.wcwy.post.dto.OrderInfoDTO;
import com.wcwy.post.dto.ReferralRevenueDTO;
import com.wcwy.post.entity.*;
import com.wcwy.post.enums.OrderStatus;
import com.wcwy.post.po.ParticularsPO;
import com.wcwy.post.pojo.DivideIntoPOJO;
import com.wcwy.post.pojo.OrderInfoInviteEntryPOJO;
import com.wcwy.post.produce.CompanyUserRoleProduce;
import com.wcwy.post.produce.PutInResumeProduce;
import com.wcwy.post.query.OrderInfoInvoiceQuery;
import com.wcwy.post.query.OrderInfoQuery;
import com.wcwy.post.query.ReferralRevenueQuery;
import com.wcwy.post.service.*;
import com.wcwy.post.session.CompanyMetadata;
import com.wcwy.post.util.OrderNoUtils;
import com.wcwy.post.vo.*;
import com.wcwy.system.annotation.Log;
import com.wcwy.system.enums.BusinessType;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 * @description 针对表【order_info(岗位订单表)】的数据库操作Service
 * @createDate 2022-10-12 17:06:43
 */
@RestController
@Api(tags = "岗位订单接口")
@RequestMapping("/orderInfo")
public class OrderInfoController {
    @Autowired
    private OrderInfoService orderInfoService;
    @Autowired
    private TCompanyPostService tCompanyPostService;
    @Autowired
    private TPostShareService tPostShareService;
    @Autowired
    private IDGenerator idGenerator;
    @Autowired
    private TCompanyApi tCompanyApi;
    @Autowired
    private CompanyMetadata companyMetadata;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private CompanyUserRoleService companyUserRoleService;
    @Autowired
    private TJobHunterApi tJobHunterApi;
    @Autowired
    private RunningWaterService runningWaterService;
    @Autowired
    private ReferrerRecordApi referrerRecordApi;

    @Autowired
    private TRecommendApi tRecommendApi;

    @Autowired
    private TJobHunterHideCompanyApi tJobHunterHideCompanyApi;

    @Autowired
    private OrderApplyForService orderApplyForService;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private PutInResumeApi putInResumeApi;
    @Resource
    private WxPayService wxPayService;
    @Autowired
    private PutInResumeProduce putInResumeProduce;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private CompanyUserRoleApi companyUserRoleApi;
    @Autowired
    private CompanyUserRoleProduce companyUserRoleProduce;

/*    @GetMapping("/save")
    public R save(@RequestBody Payment payment) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("post_id",postId);
        TCompanyPost one = tCompanyPostService.getOne(queryWrapper);

    }*/

    /***
     *如果是别人推送暂未处理
     */
    // @PostMapping("/insertOrderInfo")
    //  @ApiOperation("支付费用")
    @GlobalTransactional(rollbackFor = Exception.class, timeoutMills = 60000)
    public R insertOrderInfo(@Valid @RequestBody OrderInfoVO orderInfoVO) throws Exception {
        RLock lock = redissonClient.getLock(Lock.DOWNLOAD.getLock() + orderInfoVO.getCompanyId());
        boolean tryLock = lock.tryLock(10, 100, TimeUnit.SECONDS); //尝试加锁，最多等待10秒，上锁以后30秒自动解锁
        if (tryLock) {
            try {
                String userid = companyMetadata.userid();
                if (!userid.equals(orderInfoVO.getCompanyId())) {
                    return R.fail("登录账号与传入企业不对应！");
                }
                //查看是否下载过
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("user_id", orderInfoVO.getJobhunterId());
                queryWrapper.eq("company_id", orderInfoVO.getCompanyId());
                int count = companyUserRoleService.count(queryWrapper);
                if (count > 0) {
                    return R.fail("该简历已支付,请不要重复支付!");
                }
                OrderInfo orderInfo = new OrderInfo();//生成订单
                orderInfo.setOrderId(idGenerator.generateCode("OD")); //订单id
                orderInfo.setPostId(orderInfoVO.getPostId());//岗位id
                orderInfo.setTitle(orderInfoVO.getTitle());
                //扣除无忧币
                ProportionVO proportionVO = new ProportionVO();//封装参数
                proportionVO.setOrderId(orderInfo.getOrderId());
                proportionVO.setJobhunterId(orderInfoVO.getJobhunterId());
                /* proportionVO.setTRecommend(orderInfoVO.getRecommend());*/
                proportionVO.setTCompanyId(orderInfoVO.getCompanyId());
                proportionVO.setIdentification(1);
                //获取下载简历需要的无忧币
                Map<String, Integer> map = this.currencyCount(orderInfoVO.getMoney());
                ValueOperations<String, TPayConfig> value = redisTemplate.opsForValue();
                TPayConfig tPayConfig = value.get(RedisCache.TPAYCONFIG.getValue());//获取无忧币分成机制
                double c = map.get("currencyCount") - map.get("currencyCount") * tPayConfig.getDiscountCurrency() * 0.01;
                proportionVO.setMoney(new BigDecimal(c));
                ProportionDTO data = tCompanyApi.deductExpenses(proportionVO);//下载分成
                // ProportionDTO data = proportionDTOR.getData();
                if (data.getPaySuccess()) {
                    //保存无忧币流水
                    boolean b = runningWaterService.saveBatch(data.getList());
                    if (!b) {
                        throw new Exception("无忧币流水未添加成功");
                    }
                    //保存订单
                    orderInfo.setPaymentType("3");//支付方式
                    orderInfo.setPutInResumeId(orderInfoVO.getPutInResumeId());// 投简id
                    orderInfo.setState(2); //交易状态
                    orderInfo.setJobhunterId(orderInfoVO.getJobhunterId()); //求职者id
                    orderInfo.setRecommendId(data.getTRecommend());//推荐官id
                    orderInfo.setMoney(new BigDecimal(c));//总无忧币金额
                    orderInfo.setReferrerId(data.getSharer());
                    orderInfo.setIdentification(1);
                    orderInfo.setReferrerMoney(data.getReferrerMoney());
                    orderInfo.setShareUserId(data.getSharer());
                    orderInfo.setShareMoney(data.getSharerMoney());
                    orderInfo.setPlatformMoney(data.getPlatformMoney());
                    orderInfo.setCreateId(companyMetadata.userid());
                    orderInfo.setCreateTime(LocalDateTime.now());
                    boolean save = orderInfoService.save(orderInfo);
                    if (save) {
                        //开放下载简历
                        CompanyUserRole companyUserRole = new CompanyUserRole();
                        companyUserRole.setCompanyUserRoleId(idGenerator.generateCode("CU"));
                        companyUserRole.setUserId(orderInfoVO.getJobhunterId());
                        companyUserRole.setCompanyId(orderInfoVO.getCompanyId());
                        companyUserRole.setPutInResumeId(orderInfoVO.getPutInResumeId());
                        companyUserRole.setCreateTime(LocalDateTime.now());
                        companyUserRole.setDeleted(0);
                        boolean save1 = companyUserRoleService.save(companyUserRole);
                        if (save1) {
                            tPostShareService.updateRedisPost(orderInfoVO.getJobhunterId(), orderInfoVO.getPostId(), 2, orderInfoVO.getPutInResumeId());
                            redisTemplate.opsForValue().set(Sole.PUT.getKey() + orderInfoVO.getCompanyId() + orderInfo.getJobhunterId(), orderInfo.getPostId());

                            return R.success("支付成功");
                        }
                    }
                    throw new Exception("支付失败");
                }
                //交易失败
                return R.fail(data.getCause());
            } finally {
                lock.unlock();
            }
        }
        return R.fail("服务忙！");
    }


    public Map<String, Integer> currencyCount(BigDecimal money) {
        ValueOperations<String, TPayConfig> value = redisTemplate.opsForValue();
        TPayConfig tPayConfig = value.get(RedisCache.TPAYCONFIG.getValue());//获取无忧币分成机制
        Map<String, Integer> map = new ConcurrentHashMap<>(2);
        if (money.compareTo(new BigDecimal(50)) == 1) {//年薪大于50万
            map.put("currencyCount", tPayConfig.getGradeE());
            map.put("gold", tPayConfig.getGoldC());
            return map;
        } else if (money.compareTo(new BigDecimal(30)) == 1 || money.compareTo(new BigDecimal(30)) == 0) {//年薪大于30万
            map.put("currencyCount", tPayConfig.getGradeE());
            map.put("gold", tPayConfig.getGoldB());
            return map;
       /* } else if (money.compareTo(new BigDecimal(20)) == 1) { //年薪大于20万
            return tPayConfig.getGradeC();
        } else if (money.compareTo(new BigDecimal(10)) == 1) { //年薪大于10万
            return tPayConfig.getGradeB();
        } else if (-1 == money.compareTo(new BigDecimal(10)) || 0 == money.compareTo(new BigDecimal(10))) { //年薪小于10万
            return tPayConfig.getGradeA();*/
        }
        map.put("currencyCount", tPayConfig.getGradeA());
        map.put("gold", tPayConfig.getGoldA());
        return map;

    }

    @GetMapping("/seletOrderInfo/{orderOn}")
    @ApiOperation("查询订单是否支付成功")
    @ApiImplicitParam(name = "orderOn", required = true, value = "商户订单编号")
    @Log(title = "查询订单是否支付成功", businessType = BusinessType.SELECT)
    public R<OrderInfo> seletOrderInfo(@PathVariable("orderOn") String orderOn) {
        if (StringUtils.isEmpty(orderOn)) {
            return R.fail("信息不能为空");
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("order_on", orderOn);
        OrderInfo one = orderInfoService.getOne(queryWrapper);
        return R.success(one);
    }


    @PostMapping("/selectOrder")
    @ApiOperation("查询订单")
    @Log(title = "查询订单", businessType = BusinessType.SELECT)
    public R<OrderInfoDTO> selectOrder(@Valid @RequestBody OrderInfoQuery orderInfoQuery) {
        LambdaQueryWrapper<OrderInfo> queryWrapper = new LambdaQueryWrapper<>();
        String userid = companyMetadata.userid();

        if (!StringUtils.isEmpty(orderInfoQuery.getOrderId())) {
            queryWrapper.eq(OrderInfo::getOrderId, orderInfoQuery.getOrderId());
        }
        if (!StringUtils.isEmpty(orderInfoQuery.getState())) {
            queryWrapper.eq(OrderInfo::getState, orderInfoQuery.getState());
        }
        if (!StringUtils.isEmpty(orderInfoQuery.getKeyword())) {
            queryWrapper.like(OrderInfo::getParticulars, orderInfoQuery.getKeyword());
        }
        if (!StringUtils.isEmpty(orderInfoQuery.getIdentification())) {
            /*  orderInfoQuery.setOption(null);*/
            if (orderInfoQuery.getIdentification() == 1) {
                queryWrapper.eq(OrderInfo::getIdentification, orderInfoQuery.getIdentification()+1);
            }
            if (orderInfoQuery.getIdentification() == 2) {
                queryWrapper.eq(OrderInfo::getIdentification, orderInfoQuery.getIdentification()+1);
            }
            if (orderInfoQuery.getIdentification() == 3) {
                queryWrapper.eq(OrderInfo::getIdentification, 5);
            }
            if (orderInfoQuery.getIdentification() == 4){
                queryWrapper.eq(OrderInfo::getIdentification, 6);
            }
            if (orderInfoQuery.getIdentification() == 5){
                queryWrapper.eq(OrderInfo::getIdentification, 7);
            }
        }
        if (StringUtils.isEmpty(orderInfoQuery.getIdentification()) && !StringUtils.isEmpty(orderInfoQuery.getOption())) {
            if (orderInfoQuery.getOption() == 1) {
                queryWrapper.in(OrderInfo::getIdentification, 2, 3, 5);
            } else if (orderInfoQuery.getOption() == 2) {
                queryWrapper.eq(OrderInfo::getIdentification, 4);
            } else if (orderInfoQuery.getOption() == 3) {
                queryWrapper.eq(OrderInfo::getIdentification, 1);
            }
        }
        if (orderInfoQuery.getBeginTime() != null || orderInfoQuery.getEndTime() != null) {
            if (orderInfoQuery.getBeginTime() != null && orderInfoQuery.getEndTime() != null) {
                queryWrapper.between(OrderInfo::getCreateTime, orderInfoQuery.getBeginTime(), orderInfoQuery.getEndTime());
            }
            if (orderInfoQuery.getBeginTime() != null && orderInfoQuery.getEndTime() == null) {
                queryWrapper.le(OrderInfo::getCreateTime, orderInfoQuery.getBeginTime());
            }
            if (orderInfoQuery.getEndTime() != null && orderInfoQuery.getBeginTime() == null) {
                queryWrapper.ge(OrderInfo::getCreateTime, orderInfoQuery.getEndTime());
            }
        }
        queryWrapper.and(i -> i.eq(OrderInfo::getPayer, userid).or().eq(OrderInfo::getCreateId, userid));

        queryWrapper.orderByDesc(OrderInfo::getCreateTime);
        IPage page = orderInfoService.page(orderInfoQuery.createPage(), queryWrapper);
        List<OrderInfo> records = page.getRecords();
        List<OrderInfoDTO> list = new ArrayList<>();
        for (OrderInfo record : records) {
            OrderInfoDTO dto = new OrderInfoDTO();
            dto.setOrderInfo(record);
            dto.setParticularsPO(JSON.parseObject(record.getParticulars(), ParticularsPO.class));
      /*      OrderInfoInviteEntryPOJO orderInfoInviteEntryPOJO = new OrderInfoInviteEntryPOJO();
            BeanUtils.copyProperties(record, orderInfoInviteEntryPOJO);
            if (!StringUtils.isEmpty(record.getPutInResumeId())) {
                InviteEntryDTO inviteEntryDTO = putInResumeApi.selectInviteEntry(record.getPutInResumeId());
                orderInfoInviteEntryPOJO.setInviteEntryDTO(inviteEntryDTO);
            }
            list.add(orderInfoInviteEntryPOJO);*/
            list.add(dto);
        }
        page.setRecords(list);
        return R.success(page);
    }

/*    //无忧币支付
    @ApiOperation("支付赏金+猎头订单")
    @PostMapping("/paymentOrder")
    @GlobalTransactional(rollbackFor = Exception.class, timeoutMills = 60000)
    @Log(title = "支付赏金+猎头订单", businessType = BusinessType.INSERT)
    public R paymentOrder(@Valid @RequestBody OrderInfoOrder orderInfoOrder) throws Exception {
        String userid = companyMetadata.userid();
        if (!orderInfoOrder.getCreateId().equals(userid)) {
            return R.fail("账号不一致,请检查!");
        }
        OrderInfo byId = orderInfoService.getById(orderInfoOrder.getOrderId());
        if (byId.getState() == 2) {
            return R.fail("该订单已支付");
        }
        DeductGold deductGold = tCompanyApi.paymentDivideInto(byId);
        Boolean isOK = deductGold.getIsOK();
        if (isOK) {
            //保存无忧币流水
            boolean rw = runningWaterService.saveBatch(deductGold.getList());
            if (!rw) {
                throw new Exception("无忧币流水未添加成功");
            }
            byId.setState(2);
            byId.setUpdateTime(LocalDateTime.now());
            byId.setUpdateId(companyMetadata.userid());
            boolean b = orderInfoService.updateById(byId);
            if (b) {
                return R.success("支付成功", b);
            }
            return R.fail(deductGold.getCause(), b);
        }
        return R.fail("支付失败,请重新支付！");

    }*/

    @PostMapping("/applyForInvoice")
    @ApiOperation("查询未开发票订单")
    @Log(title = "查询未开发票订单", businessType = BusinessType.SELECT)
    public R<OrderInfoInviteEntryPOJO> applyForInvoice(@Valid @RequestBody OrderInfoInvoiceQuery orderInfoInvoiceQuery) {
        if (!StringUtils.pathEquals(orderInfoInvoiceQuery.getLoginUser(), companyMetadata.userid())) {
            return R.fail("查询与登录账户不一致!");
        }
        if (orderInfoInvoiceQuery.getEndTime() != null || orderInfoInvoiceQuery.getBeginTime() != null) {
            if (orderInfoInvoiceQuery.getEndTime() != null) {
                orderInfoInvoiceQuery.setEndTime(orderInfoInvoiceQuery.getEndTime().plusDays(1));
            } else {
                orderInfoInvoiceQuery.setEndTime(LocalDate.now().plusDays(1));
            }
            if (orderInfoInvoiceQuery.getBeginTime() == null) {
                orderInfoInvoiceQuery.setBeginTime(LocalDate.parse("2022-08-08"));
            }
        }
        IPage infoIPage = orderInfoService.selectApplyForInvoice(orderInfoInvoiceQuery);
        Map map = new HashMap(2);
        map.put("IPage", infoIPage);
        Double d = orderInfoService.sumMoney(companyMetadata.userid());
        map.put("availableAmount", d);
     /*   List<OrderInfo> records = infoIPage.getRecords();
        List<OrderInfoInviteEntryPOJO> list = new ArrayList<>();
        for (OrderInfo record : records) {
            OrderInfoInviteEntryPOJO orderInfoInviteEntryPOJO = new OrderInfoInviteEntryPOJO();
            BeanUtils.copyProperties(record, orderInfoInviteEntryPOJO);
            if (!StringUtils.isEmpty(record.getPutInResumeId())) {
                InviteEntryDTO inviteEntryDTO = putInResumeApi.selectInviteEntry(record.getPutInResumeId());
                orderInfoInviteEntryPOJO.setInviteEntryDTO(inviteEntryDTO);
            }
            list.add(orderInfoInviteEntryPOJO);
            orderInfoInviteEntryPOJO = null;
        }
        infoIPage.setRecords(list);*/
        return R.success(map);
    }

/*    @PostMapping("/selectListOrder")
    public  R<OrderInfo> */


/*    @PostMapping("/topUp")
    @ApiOperation("无忧币充值")
    @GlobalTransactional(rollbackFor = Exception.class, timeoutMills = 60000)
    public R topUp(@Valid @RequestBody TopUpVO topUpVO) throws Exception {
        String userid1 = companyMetadata.userid();
        RLock lock = redissonClient.getLock(Lock.DOWNLOAD.getLock() + userid1);
        boolean tryLock = lock.tryLock(10, 100, TimeUnit.SECONDS); //尝试加锁，最多等待10秒，上锁以后30秒自动解锁
        if (tryLock) {
            try {

                OrderInfo orderInfo = new OrderInfo();//生成订单
                orderInfo.setOrderId(idGenerator.generateCode("OD")); //订单id
                orderInfo.setTitle(topUpVO.getTitle());
                //扣除无忧币
                ProportionVO proportionVO = new ProportionVO();//封装参数
                proportionVO.setOrderId(orderInfo.getOrderId());
                proportionVO.setJobhunterId(orderInfoVO.getJobhunterId());
                *//* proportionVO.setTRecommend(orderInfoVO.getRecommend());*//*
                proportionVO.setTCompanyId(orderInfoVO.getCompanyId());
                proportionVO.setIdentification(1);
                //获取下载简历需要的无忧币
                int i = this.currencyCount(orderInfoVO.getMoney());
                ValueOperations<String, TPayConfig> value = redisTemplate.opsForValue();
                TPayConfig tPayConfig = value.get(RedisCache.TPAYCONFIG.getValue());//获取无忧币分成机制
                double c = i - i * tPayConfig.getDiscount() * 0.01;
                proportionVO.setMoney(new BigDecimal(c));
                ProportionDTO data = tCompanyApi.deductExpenses(proportionVO);//下载分成
                // ProportionDTO data = proportionDTOR.getData();
                if (data.getPaySuccess()) {
                    //保存无忧币流水
                    boolean b = runningWaterService.saveBatch(data.getList());
                    if (!b) {
                        throw new Exception("无忧币流水未添加成功");
                    }
                    //保存订单
                    orderInfo.setPaymentType("3");//支付方式
                    orderInfo.setPutInResumeId(orderInfoVO.getPutInResumeId());// 投简id
                    orderInfo.setState(2); //交易状态
                    orderInfo.setJobhunterId(orderInfoVO.getJobhunterId()); //求职者id
                    orderInfo.setRecommendId(data.getTRecommend());//推荐官id
                    orderInfo.setMoney(new BigDecimal(c));//总无忧币金额
                    orderInfo.setReferrerId(data.getSharer());
                    orderInfo.setIdentification(1);
                    orderInfo.setReferrerMoney(data.getReferrerMoney());
                    orderInfo.setShareUserId(data.getSharer());
                    orderInfo.setShareMoney(data.getSharerMoney());
                    orderInfo.setPlatformMoney(data.getPlatformMoney());
                    orderInfo.setCreateId(companyMetadata.userid());
                    orderInfo.setCreateTime(LocalDateTime.now());
                    boolean save = orderInfoService.save(orderInfo);
                    if (save) {
                        //开放下载简历
                        CompanyUserRole companyUserRole = new CompanyUserRole();
                        companyUserRole.setCompanyUserRoleId(idGenerator.generateCode("CU"));
                        companyUserRole.setUserId(orderInfoVO.getJobhunterId());
                        companyUserRole.setCompanyId(orderInfoVO.getCompanyId());
                        companyUserRole.setPutInResumeId(orderInfoVO.getPutInResumeId());
                        companyUserRole.setCreateTime(LocalDateTime.now());
                        companyUserRole.setDeleted(0);
                        boolean save1 = companyUserRoleService.save(companyUserRole);
                        if (save1) {
                            tPostShareService.updateRedisPost(orderInfoVO.getJobhunterId(), orderInfoVO.getPostId(), 2, orderInfoVO.getPutInResumeId());
                            redisTemplate.opsForValue().set(Sole.PUT.getKey() + orderInfoVO.getCompanyId() + orderInfo.getJobhunterId(), orderInfo.getPostId());

                            return R.success("支付成功");
                        }
                    }
                    throw new Exception("支付失败");
                }
                //交易失败
                return R.fail(data.getCause());
            } finally {
                lock.unlock();
            }
        }
        return R.fail("服务忙！");
    }*/

    @PostMapping("/resumeOrder")
    //@ApiOperation("下载简历生成订单")
    @Log(title = "下载简历生成订单", businessType = BusinessType.INSERT)
    public R resumeOrder(@Valid @RequestBody ResumeOrderVO resumeOrderVO) throws Exception {
        RLock lock = redissonClient.getLock(Lock.DOWNLOAD.getLock() + resumeOrderVO.getCompanyId());
        boolean tryLock = lock.tryLock(10, 100, TimeUnit.SECONDS); //尝试加锁，最多等待10秒，上锁以后30秒自动解锁
        try {
            if (tryLock) {
                String userid = companyMetadata.userid();
                if (!userid.equals(resumeOrderVO.getCompanyId())) {
                    return R.fail("登录账号与传入企业不对应！");
                }

                //查看是否屏蔽
                List<Object> objects = tJobHunterHideCompanyApi.cacheCompany(resumeOrderVO.getJobhunterId());
                boolean contains = objects.contains(companyMetadata.userid());
                if (contains) {
                    return R.fail("该求职者屏蔽了简历,暂时不能下载！");
                }
                boolean b = redisUtils.sHasKey(CompanyUserRoleEnums.COMPANY_USER_ROLE.getCompanyUserRole() + userid, resumeOrderVO.getJobhunterId());
                if (b) {
                    return R.fail("该简历已下载,请勿重复下载");
                }
                if (!b) {
                    Boolean exist = companyUserRoleApi.exist(resumeOrderVO.getJobhunterId(), userid);
                    if (exist) {
                        return R.fail("该简历已下载,请勿重复下载");
                    }
                }

                //查询订单是否存在并且未支付
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("create_id", userid);
                queryWrapper.eq("identification", 1);
                queryWrapper.eq("state", 1);
                queryWrapper.eq("jobhunter_id", resumeOrderVO.getJobhunterId());
                OrderInfo one = orderInfoService.getOne(queryWrapper);
                if (one != null) {
                    //返回二维码
                    Map<String, Object> map = new HashMap<>();
                    /*  map.put("codeUrl", one.getCodeUrl());*/
                    map.put("orderNo", one.getOrderOn());
                    map.put("createTime", one.getCreateTime());
                    map.put("title", one.getTitle());
                    map.put("orderId", one.getOrderId());
                    map.put("money", one.getMoney());
                    return R.success(map);
                }

                boolean exist = redisUtils.sHasKey(CompanyUserRoleEnums.COMPANY_USER_ROLE.getCompanyUserRole() + resumeOrderVO.getCompanyId(), resumeOrderVO.getJobhunterId());
                if (exist) {
                    exist = companyUserRoleApi.exist(resumeOrderVO.getJobhunterId(), resumeOrderVO.getCompanyId());
                }
                if (exist) {
                    return R.fail("该简历已支付,请不要重复支付!");
                }
                OrderInfo orderInfo = new OrderInfo();
                orderInfo.setOrderId(idGenerator.generateCode("OD"));
                orderInfo.setOrderOn(OrderNoUtils.getOrderNo());
                orderInfo.setTitle(resumeOrderVO.getTitle());
                orderInfo.setPaymentType("2");
                orderInfo.setState(1);
                orderInfo.setPutInResumeId(resumeOrderVO.getPutInResumeId());
                orderInfo.setIdentification(1);
                orderInfo.setCreateTime(LocalDateTime.now());
                orderInfo.setCreateId(userid);
                orderInfo.setPostId(resumeOrderVO.getPostId());
                orderInfo.setJobhunterId(resumeOrderVO.getJobhunterId());
                Map<String, Object> currentSalary = tJobHunterApi.getCurrentSalary(resumeOrderVO.getJobhunterId());
                if (currentSalary == null) {
                    return R.fail("该求职者简历存在问题,管理员在审核！");
                }
                /*map.put("recommend_id", share.getSharePerson()) ;
                map.put("recommend_share",share.getRecommendShare()) ;
                map.put("currentSalary",share.getCurrentSalary()) ;*/
                //获取下载简历需要的无忧币
                BigDecimal money = new BigDecimal((Double) currentSalary.get("currentSalary"));
                Map<String, Integer> map = this.currencyCount(money);
                orderInfo.setPlatformMoney(new BigDecimal(map.get("currencyCount")));
            /*    //下载简历
                if (orderInfo.getIdentification() == 1) {
                    String recommend_id1 = (String) currentSalary.get("recommend_id");
                    String recommend_share = (String) currentSalary.get("recommend_share");
                    if (!StringUtils.isEmpty(recommend_id1)) {
                        boolean recommend_id = redisUtils.sHasKey(Register.RECOMMEND_REGISTER.getType(), recommend_id1);
                        //判断是否1200内注册的
                        double earningsRate = 0.1;
                        if (recommend_id) {
                            earningsRate = 0.2;
                        }
                        Map<String, Object> map = DivideIntoUtil.downloadResume(earningsRate, new BigDecimal(i), recommend_id1, recommend_share);
                        orderInfo.setRecommendId(recommend_id1);
                        orderInfo.setReferrerMoney((BigDecimal) map.get(recommend_id1));
                        //邀请推荐推荐官分成
                        if (!StringUtils.isEmpty(recommend_share)) {
                            orderInfo.setRecommendId(recommend_share);
                            orderInfo.setReferrerMoney((BigDecimal) map.get(recommend_share));
                        }
                        orderInfo.setPlatformMoney((BigDecimal) map.get("platformMoney"));
                    }
                }*/
                // orderInfo.setReferrerMoney(new BigDecimal(0.00));
                orderInfo.setMoney(BigDecimal.valueOf(map.get("currencyCount")));
                Map<String, Object> qrCode = wxPayService.createQRCode(orderInfo);

                /*if(){//是否产生金币价格信息

                }*/
                return R.success(qrCode);
            }
        } finally {
            lock.unlock();
        }
        return R.fail("服务忙！");
    }

    @PostMapping("/payForTheOrder")
   /* @ApiOperation("支付简历订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", required = true, value = "订单编号"),
            @ApiImplicitParam(name = "paymentType", required = true, value = "支付方式(1:无忧币 2:金币支付)"),
            @ApiImplicitParam(name = "tCompany", required = true, value = "企业id")
    })*/
    @Log(title = "下载生成订单的简历", businessType = BusinessType.UPDATE)
    @GlobalTransactional(rollbackFor = Exception.class, timeoutMills = 60000)
    @AutoIdempotent
    public R payForTheOrder(@RequestParam("orderId") String orderId, @RequestParam("paymentType") Integer paymentType, @RequestParam("tCompany") String tCompany) throws Exception {
        if (paymentType != 1 && paymentType != 2) {
            return R.fail("未知支付方式!");
        }
        String userid = companyMetadata.userid();
        if (!userid.equals(tCompany)) {
            return R.fail("企业与登录账号不一致!");
        }
        RLock lock = redissonClient.getLock(Lock.PAYMENT.getLock() + orderId);
        boolean tryLock = lock.tryLock(10, 100, TimeUnit.SECONDS); //尝试加锁，最多等待10秒，上锁以后30秒自动解锁
        try {
            if (tryLock) {
                OrderInfo byId = orderInfoService.getById(orderId);
                if (byId.getState() != 1) {
                    return R.success(OrderState.state(byId.getState()));
                }
                //判断是否入职付岗位或是推荐官分享注册的企业
                if (!StringUtils.isEmpty(byId.getRecommendId()) && paymentType == 2) {
                    return R.fail("该订单暂不能使用金币支付!");
                }
                Map<String, Object> map1 = null;
                if (paymentType == 1) {
                    map1 = tCompanyApi.deductCurrency(byId.getMoney(), userid, byId.getJobhunterId(), byId.getOrderId());
                } else if (paymentType == 2) {
                    BigDecimal multiply = byId.getMoney().multiply(new BigDecimal(this.proportion()));
                    map1 = tCompanyApi.deductGold(multiply, userid, byId.getJobhunterId(), byId.getOrderId());
                    byId.setMoney(multiply);
                }
                Integer state = (Integer) map1.get("state");
                if (state == 0) {
                    byId.setState(2);
                    if (paymentType == 1) {
                        byId.setPaymentType("3");

                    } else if (paymentType == 2) {
                        byId.setPaymentType("4");
                    }
                    byId.setPaymentAmount(byId.getMoney());
                    boolean b = orderInfoService.saveOrUpdate(byId);
                    if (b) {
                        //更新简历下载
                        companyUserRoleProduce.sendAsyncMessage(byId);
                        //关单请求
                        //  wxPayService.closeOrder(byId.getOrderOn());
                        //更新发布岗位纪录表
                        tPostShareService.updateShare(byId.getPostId());

                        return R.success(map1.get("msg"));
                    }

                }
                return R.fail((String) map1.get("msg"));
            }
        } finally {
            lock.unlock();
        }
        return R.fail("有订单正在支付中!");
    }

    /**
     * 无忧币与金币的比例
     */
    public int proportion() {
        ValueOperations<String, TPayConfig> value = redisTemplate.opsForValue();
        TPayConfig tPayConfig = value.get(RedisCache.TPAYCONFIG.getValue());//获取无忧币分成机制
        return tPayConfig.getProportion();

    }

    /**
     * @param identityId  :身份id
     * @param recommendId :推荐官id
     * @param type        :查询的类型
     * @return null
     * @Description:
     * @Author tangzhuo
     * @CreateTime 2023/4/10 18:52
     */

    @GetMapping("/divideInto")
    public DivideIntoPOJO divideInto(@RequestParam("identityId") String
                                             identityId, @RequestParam("recommendId") String recommendId, @RequestParam("type") Integer type) {
        DivideIntoPOJO divideIntoPOJO = orderInfoService.divideInto(identityId, recommendId, type);
        return divideIntoPOJO;
    }


/*    @Log(title = "支付订单", businessType = BusinessType.UPDATE)
    @ApiImplicitParam(name = "orderId", required = true, value = "订单id")
    public R paymentOrder(@RequestParam("orderId") String orderId) {

    }*/

    /**
     * 简历下载
     */
    @PostMapping("/resumeDownload")
    @ApiOperation("企业支付下载简历")
    @GlobalTransactional(rollbackFor = Exception.class, timeoutMills = 6000)
    @AutoIdempotent
    @Log(title = "企业支付下载简历", businessType = BusinessType.INSERT)
    public R resumeDownload(@Valid @RequestBody ResumeOrderVO resumeOrderVO) throws InterruptedException {
        Integer paymentType = resumeOrderVO.getPaymentType();
        if (paymentType != 1 && paymentType != 0) {
            return R.fail("暂不支持其他支付方式");
        }
        Integer source = resumeOrderVO.getSource();
        if (source == 1) {
            if (StringUtils.isEmpty(resumeOrderVO.getPutInResumeId()) || StringUtils.isEmpty(resumeOrderVO.getPutInUser()) || StringUtils.isEmpty(resumeOrderVO.getPostId())) {
                return R.fail("请传入完整值!");
            }
        }
        RLock lock = redissonClient.getLock(Lock.DOWNLOAD.getLock() + resumeOrderVO.getCompanyId());
        boolean tryLock = lock.tryLock(0, 30, TimeUnit.SECONDS); //尝试加锁，最多等待0秒，上锁以后30秒自动解锁
        try {
            if (tryLock) {
                String userid = companyMetadata.userid();
                if (!userid.equals(resumeOrderVO.getCompanyId())) {
                    return R.fail("登录账号与传入企业不对应！");
                }
                boolean b = redisUtils.sHasKey(CompanyUserRoleEnums.COMPANY_USER_ROLE.getCompanyUserRole() + userid, resumeOrderVO.getJobhunterId());
                if (b) {
                    return R.fail("该简历已下载,请勿重复下载");
                }
                if (!b) {
                    Boolean exist = companyUserRoleApi.exist(resumeOrderVO.getJobhunterId(), userid);
                    if (exist) {
                        return R.fail("该简历已下载,请勿重复下载");
                    }
                }
                Integer postType = 0;
                TCompanyPost byId = null;
                //查看岗位的类型
                if (!StringUtils.isEmpty(resumeOrderVO.getPostId())) {
                    byId = tCompanyPostService.getById(resumeOrderVO.getPostId());
                    if (byId == null) {
                        return R.fail("该职位不存在!");
                    }
                    postType = byId.getPostType();
                    //判断职位类型判断支付类型
                    //简历付只能使用无忧币下载
                    if (postType >= 4 && paymentType == 1) {
                        return R.fail("简历付职位只能使用无忧币下载!");
                    }
                }
                //调用支付接口
                Map<String, Object> map = tJobHunterApi.payResume(resumeOrderVO.getJobhunterId(), resumeOrderVO.getPaymentType(), postType);
                Integer state = (Integer) map.get("state");
                //保存订单
                OrderInfo orderInfo = new OrderInfo();
                orderInfo.setOrderId(idGenerator.generateCode("OD"));
                // orderInfo.setOrderOn(OrderNoUtils.getOrderNo());
                orderInfo.setTitle(resumeOrderVO.getTitle());
                //规范数据库类型
                if (paymentType == 1) {
                    orderInfo.setPaymentType("4");
                } else if (paymentType == 0) {
                    orderInfo.setPaymentType("3");
                    orderInfo.setMoney(new BigDecimal(map.get("currencyCount").toString()));
                }
                orderInfo.setPutInResumeId(resumeOrderVO.getPutInResumeId());
                //区分是简历下载服务还是简历付服务
                if (postType >= 4) {
                    if(postType==4){
                        orderInfo.setIdentification(6);
                    }else if(postType==5){
                        orderInfo.setIdentification(7);
                    }
                    orderInfo.setRecommendId(resumeOrderVO.getPutInUser());
                } else {
                    orderInfo.setIdentification(1);
                }
                orderInfo.setCreateTime(LocalDateTime.now());
                orderInfo.setCreateId(userid);
                orderInfo.setPostId(resumeOrderVO.getPostId());
                orderInfo.setJobhunterId(resumeOrderVO.getJobhunterId());
                orderInfo.setCreateName(companyMetadata.userName());
                orderInfo.setPayer(companyMetadata.userid());
                orderInfo.setPayerTime(LocalDateTime.now());
                if (state == 0) {
                    orderInfo.setState(2);
                    // boolean b1 = orderInfoService.saveOrUpdate(orderInfo);
                    //更新简历下载
                    //关单请求
                    //  wxPayService.closeOrder(byId.getOrderOn());
                    //更新发布岗位纪录表
                    if (!StringUtils.isEmpty(resumeOrderVO.getPostId())) {
                        tPostShareService.updateShare(resumeOrderVO.getPostId());
                    }
                    if (paymentType == 0) {
                        orderInfo.setPaymentAmount(new BigDecimal(map.get("currencyCount").toString()));
                        //存储订单详情副属表
                        ParticularsPO particularsPO = new ParticularsPO();
                        if(! StringUtils.isEmpty(map.get("currentSalary"))){
                            particularsPO.setCurrentSalary(new BigDecimal(map.get("currentSalary").toString()));
                        }
                        particularsPO.setAvatar(map.get("avatar").toString());
                        particularsPO.setJobHunter(map.get("jobHunter").toString());
                        particularsPO.setUserName(map.get("userName").toString());
                       if(map.get("workTime") !=null){
                           particularsPO.setWorkTime(LocalDate.parse(map.get("workTime").toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                       }
                        particularsPO.setEducation(map.get("education").toString());
                        particularsPO.setBirthday(LocalDate.parse(map.get("birthday").toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                        if (!StringUtils.isEmpty(resumeOrderVO.getPostId())) {
                            particularsPO.setPostType(byId.getPostType());
                            particularsPO.setPostLabel(byId.getPostLabel());
                            particularsPO.setBeginSalary(byId.getBeginSalary());
                            particularsPO.setEndSalary(byId.getEndSalary());
                            particularsPO.setCompanyName(byId.getCompanyName());
                        }
                        orderInfo.setParticulars(JSON.toJSONString(particularsPO));
                        //添加修改订单
                        orderInfoService.saveOrUpdate(orderInfo);
                    }
                    //更新投简记录
                    companyUserRoleProduce.sendAsyncMessage(orderInfo);
                    //避免mq延迟
                   // redisUtils.sSetAndTime(CompanyUserRoleEnums.COMPANY_USER_ROLE + resumeOrderVO.getCompanyId(), 3600, resumeOrderVO.getJobhunterId());
                    return R.success(map.get("msg"));
                } else if (state == 1) {
                    orderInfo.setState(1);
                    return R.fail(map.get("msg").toString());
                }
            }
        } finally {
            lock.unlock();
        }
        return R.fail("服务忙！");
    }

    @ApiOperation("支付订单")
    @GetMapping("/paymentOrder")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "order", required = true, value = "订单")
    })
    @GlobalTransactional(rollbackFor = Exception.class, timeoutMills = 60000)
    @Log(title = "支付订单", businessType = BusinessType.UPDATE)
    @AutoIdempotent
    public R paymentOrder(@RequestParam("order") String order) {
        OrderInfo byId = orderInfoService.getById(order);
        Map<String, Object> map = null;
        if (byId == null) {
            return R.fail("该订单不存在!");
        }
        if (byId.getState() != 1) {
            return R.fail(OrderStatus.getType(byId.getState()) + "不能支付！");
        }
        if (byId.getIdentification() == 1) {
            map = tCompanyApi.deductCurrency(byId.getMoney(), companyMetadata.userid(), byId.getJobhunterId(), byId.getOrderId());
        }
        if (byId.getIdentification() == 2 || byId.getIdentification() == 3 || byId.getIdentification() == 5) {
            String explain = "";
            if (byId.getIdentification() == 2) {
                explain = "入职付服务费;订单编号" + byId.getOrderId();
            }
            if (byId.getIdentification() == 3) {
                explain = "满月付服务费;订单编号" + byId.getOrderId();
            }
            if (byId.getIdentification() == 5) {
                explain = "到面付服务费;订单编号" + byId.getOrderId();
            }
            if (byId.getIdentification() == 6) {
                explain = "简历付~校;订单编号" + byId.getOrderId();
            }
            if (byId.getIdentification() == 7) {
                explain = "简历付~职;订单编号" + byId.getOrderId();
            }
            map = tCompanyApi.deductCurrencyExplain(byId.getMoney(), companyMetadata.userid(), explain, byId.getOrderId());
        }
        if (map != null) {
            Integer state = (Integer) map.get("state");
            if (state == 0) {
                byId.setState(2);
                byId.setPaymentType("3");
                byId.setPayerTime(LocalDateTime.now());
                byId.setUpdateId(companyMetadata.userid());
                byId.setUpdateTime(LocalDateTime.now());
                byId.setPaymentAmount(byId.getMoney());
                boolean b = orderInfoService.updateById(byId);
                if (b) {
                    //修改投简记录的订单记录状态
                    if (byId.getIdentification() == 2 || byId.getIdentification() == 3 || byId.getIdentification() == 5 || byId.getIdentification() == 6 || byId.getIdentification() == 7) {
                        putInResumeApi.updateCloseAnAccount(byId.getPutInResumeId(), 3);
                    }
                    return R.success(map.get("msg"));
                }

            }
            return R.fail(map.get("msg").toString());
        }
        return R.fail("未知错误！");
    }


    @GetMapping("/cancelOrder")
    @ApiOperation("取消订单")
    @Log(title = "取消订单", businessType = BusinessType.UPDATE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "order", required = true, value = "订单"),
            @ApiImplicitParam(name = "cause", required = false, value = "原因"),
            @ApiImplicitParam(name = "attachment", required = false, value = "附件")
    })
    @Transactional(rollbackFor = Exception.class)
    @AutoIdempotent
    public R cancelOrder(@RequestParam("order") String order, @RequestParam(value = "cause", required = false) String cause, @RequestParam(value = "attachment", required = false) List<String> attachment) throws Exception {
        OrderInfo byId = orderInfoService.getById(order);
        if (byId == null) {
            return R.fail("订单不存在!");
        }
        if (byId.getState() != 1) {
            return R.fail("该订单未处于交易中!");
        }
        //职位订单需要提交申请
        if (byId.getIdentification() == 2 || byId.getIdentification() == 3 || byId.getIdentification() == 5) {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("dispose_state", 1);
            queryWrapper.eq("order_id", order);
            List<OrderApplyFor> list = orderApplyForService.list(queryWrapper);
            if (list != null && list.size() > 0) {
                return R.fail("申请已提交,请耐心等待!");
            }
            OrderApplyFor orderApplyFor = new OrderApplyFor();
            orderApplyFor.setOrderId(order);
            orderApplyFor.setAttachment(attachment);
            orderApplyFor.setCause(cause);
            orderApplyFor.setCreateId(companyMetadata.userid());
            orderApplyFor.setCreateTime(LocalDateTime.now());
            orderApplyFor.setDisposeState(1);
            orderApplyFor.setState(1);
            boolean save = orderApplyForService.save(orderApplyFor);
            if (save) {
                return R.success("已提交申请,等待客服同意！");
            }

        } else {
            if (byId.getIdentification() == 4) {
                wxPayService.cancelOrder(byId.getOrderOn());
            }
            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("order_id", order);
            updateWrapper.set("state", 4);
            boolean update = orderInfoService.update(updateWrapper);
            if (update) {
                return R.success("订单已取消!");
            }
        }
        return R.fail("取消失败！");
    }

    @GetMapping("/delete")
    @ApiOperation("删除订单")
    @Log(title = "删除订单", businessType = BusinessType.DELETE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "order", required = true, value = "订单")
    })
    @AutoIdempotent
    public R delete(@RequestParam("order") List<String> order) {
        if (order == null) {
            return R.fail("请选择要删除的订单");
        }
        for (String s : order) {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("create_id", companyMetadata.userid());
            queryWrapper.eq("order_id", s);
            OrderInfo byId = orderInfoService.getOne(queryWrapper);
            if (byId == null) {
                return R.fail("非创建人不能删除！");
            }
            if (byId.getIdentification() == 2 || byId.getIdentification() == 3 || byId.getIdentification() == 5) {
                if (byId.getState() == 3 || byId.getState() == 4 || byId.getState() == 8) {
                    boolean b = orderInfoService.removeById(byId);
                } else {
                   /* if (byId.getState() == 2) {
                        return R.fail("交易成功订单不能进行删除!");
                    }*/
                    return R.fail("请取消" + s + "订单,再进行删除");
                }
            }
            boolean b = orderInfoService.removeById(byId);
        }
        return R.success();
    }


    @GetMapping("/auditStatus")
    @ApiOperation("审核状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "order", required = true, value = "订单"),
            @ApiImplicitParam(name = "pageNo", required = true, value = "第条")
    })
    public R<OrderApplyFor> auditStatus(@RequestParam("order") String order, @RequestParam("pageNo") Integer pageNo) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("order_id", order);
        Page page = orderApplyForService.page(new Page(pageNo, 1), queryWrapper);
        return R.success(page);
    }

    @GetMapping("/selectOrderInfo")
    @ApiImplicitParam(name = "orders", required = true, value = "订单")
    @Log(title = "查询订单集合", businessType = BusinessType.SELECT)
    @ApiOperation("查询订单集合")
    public R<OrderInfo> selectOrderInfo(@RequestParam("orders") List<String> orders) {
        List<OrderInfo> orderInfoList = orderInfoService.listByIds(orders);
        return R.success(orderInfoList);
    }


    @PostMapping("/orderReceiving")
    @ApiOperation("推荐官的订单管理")
    @Log(title = "推荐官的订单管理 ", businessType = BusinessType.SELECT)
    public R<OrderInfoDTO> orderReceiving(@Valid @RequestBody OrderInfoQuery orderInfoQuery) {
        LambdaQueryWrapper<OrderInfo> queryWrapper = new LambdaQueryWrapper<>();
        String userid = companyMetadata.userid();

        if (!StringUtils.isEmpty(orderInfoQuery.getOrderId())) {
            queryWrapper.eq(OrderInfo::getOrderId, orderInfoQuery.getOrderId());
        }
        if (!StringUtils.isEmpty(orderInfoQuery.getState())) {
            queryWrapper.eq(OrderInfo::getState, orderInfoQuery.getState());
        }
        if (!StringUtils.isEmpty(orderInfoQuery.getKeyword())) {
            queryWrapper.like(OrderInfo::getParticulars, orderInfoQuery.getKeyword());
        }
        if (!StringUtils.isEmpty(orderInfoQuery.getIdentification())) {
            /*  orderInfoQuery.setOption(null);*/
            queryWrapper.eq(OrderInfo::getRecommendId, userid);
            if (orderInfoQuery.getIdentification() == 1) {
                queryWrapper.eq(OrderInfo::getIdentification, orderInfoQuery.getIdentification()+1);
            }
            if (orderInfoQuery.getIdentification() == 2) {
                queryWrapper.eq(OrderInfo::getIdentification, orderInfoQuery.getIdentification()+1);
            }
            if (orderInfoQuery.getIdentification() == 3) {
                queryWrapper.eq(OrderInfo::getIdentification, 5);
            }
            if (orderInfoQuery.getIdentification() == 4) {
                queryWrapper.eq(OrderInfo::getIdentification, 6);
            }
            if (orderInfoQuery.getIdentification() == 5) {
                queryWrapper.eq(OrderInfo::getIdentification, 7);
            }
        }
        if (StringUtils.isEmpty(orderInfoQuery.getIdentification()) && !StringUtils.isEmpty(orderInfoQuery.getOption())) {
            if (orderInfoQuery.getOption() == 1) {
                queryWrapper.eq(OrderInfo::getRecommendId, userid);
                queryWrapper.in(OrderInfo::getIdentification, 2, 3, 5);
            } else if (orderInfoQuery.getOption() == 2) {
                queryWrapper.eq(OrderInfo::getCreateId, userid);
                queryWrapper.eq(OrderInfo::getIdentification, 4);
            } else if (orderInfoQuery.getOption() == 3) {
                queryWrapper.eq(OrderInfo::getCreateId, userid);
                queryWrapper.eq(OrderInfo::getIdentification, 1);
            }
        }
        if (orderInfoQuery.getBeginTime() != null || orderInfoQuery.getEndTime() != null) {
            if (orderInfoQuery.getBeginTime() != null && orderInfoQuery.getEndTime() != null) {
                queryWrapper.between(OrderInfo::getCreateTime, orderInfoQuery.getBeginTime(), orderInfoQuery.getEndTime());
            }
            if (orderInfoQuery.getBeginTime() != null) {
                queryWrapper.le(OrderInfo::getCreateTime, orderInfoQuery.getBeginTime());
            }
            if (orderInfoQuery.getEndTime() != null) {
                queryWrapper.ge(OrderInfo::getCreateTime, orderInfoQuery.getEndTime());
            }
        }


        queryWrapper.orderByDesc(OrderInfo::getCreateTime);
        IPage page = orderInfoService.page(orderInfoQuery.createPage(), queryWrapper);
        List<OrderInfo> records = page.getRecords();
        List<OrderInfoDTO> list = new ArrayList<>();
        for (OrderInfo record : records) {
            OrderInfoDTO dto = new OrderInfoDTO();
            dto.setOrderInfo(record);
            dto.setParticularsPO(JSON.parseObject(record.getParticulars(), ParticularsPO.class));
      /*      OrderInfoInviteEntryPOJO orderInfoInviteEntryPOJO = new OrderInfoInviteEntryPOJO();
            BeanUtils.copyProperties(record, orderInfoInviteEntryPOJO);
            if (!StringUtils.isEmpty(record.getPutInResumeId())) {
                InviteEntryDTO inviteEntryDTO = putInResumeApi.selectInviteEntry(record.getPutInResumeId());
                orderInfoInviteEntryPOJO.setInviteEntryDTO(inviteEntryDTO);
            }
            list.add(orderInfoInviteEntryPOJO);*/
            list.add(dto);
        }
        page.setRecords(list);
        return R.success(page);
    }

  /*  @PostMapping("/resumeRecommendDownload")
    @ApiOperation("推荐官支付下载简历")
    @GlobalTransactional(rollbackFor = Exception.class, timeoutMills = 60000)
    @AutoIdempotent
    @Log(title = "推荐官支付下载简历", businessType = BusinessType.INSERT)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "jobHunter", required = true, value = "求职者")
    })
    public R resumeRecommendDownload(@RequestParam("jobHunter") String jobHunter) throws InterruptedException {

        RLock lock = redissonClient.getLock(Lock.DOWNLOAD.getLock() + companyMetadata.userid());
        boolean tryLock = lock.tryLock(10, 100, TimeUnit.SECONDS); //尝试加锁，最多等待10秒，上锁以后30秒自动解锁
        try {
            if (tryLock) {
                String userid = companyMetadata.userid();
                boolean b = redisUtils.sHasKey(CompanyUserRoleEnums.COMPANY_USER_ROLE.getCompanyUserRole() + userid, jobHunter);
                if (b) {
                    return R.fail("该简历已下载,请勿重复下载");
                }
                if (!b) {
                    Boolean exist = referrerRecordApi.exist(jobHunter);
                    if (exist) {
                        return R.fail("该简历已下载,请勿重复下载");
                    }
                }
                Map<String, Object> map = tRecommendApi.deductCurrency(jobHunter, resumeOrderVO.getPaymentType());
                Integer state = (Integer) map.get("state");
                OrderInfo orderInfo = new OrderInfo();
                orderInfo.setOrderId(idGenerator.generateCode("OD"));
                // orderInfo.setOrderOn(OrderNoUtils.getOrderNo());
                orderInfo.setTitle(resumeOrderVO.getTitle());
                if (paymentType == 1) {
                    orderInfo.setPaymentType("4");
                } else if (paymentType == 0) {
                    orderInfo.setPaymentType("3");
                    orderInfo.setMoney(new BigDecimal(map.get("currencyCount").toString()));
                }
                orderInfo.setPutInResumeId(resumeOrderVO.getPutInResumeId());
                orderInfo.setIdentification(1);
                orderInfo.setCreateTime(LocalDateTime.now());
                orderInfo.setCreateId(userid);
                orderInfo.setPostId(resumeOrderVO.getPostId());
                orderInfo.setJobhunterId(resumeOrderVO.getJobhunterId());
                orderInfo.setCreateName(companyMetadata.userName());
                orderInfo.setPayer(companyMetadata.userid());
                orderInfo.setPayerTime(LocalDateTime.now());
                if (state == 0) {
                    orderInfo.setState(2);
                    // boolean b1 = orderInfoService.saveOrUpdate(orderInfo);
                    //更新简历下载
                    //关单请求
                    //  wxPayService.closeOrder(byId.getOrderOn());
                    //更新发布岗位纪录表
                    if (!StringUtils.isEmpty(resumeOrderVO.getPostId())) {
                        tPostShareService.updateShare(resumeOrderVO.getPostId());
                    }
                    if (paymentType == 0) {
                        orderInfo.setPaymentAmount(new BigDecimal(map.get("currencyCount").toString()));
                        ParticularsPO particularsPO = new ParticularsPO();
                        particularsPO.setAvatar(map.get("avatar").toString());
                        particularsPO.setJobHunter(map.get("jobHunter").toString());
                        particularsPO.setUserName(map.get("userName").toString());
                        particularsPO.setWorkTime(LocalDate.parse(map.get("workTime").toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                        particularsPO.setEducation(map.get("education").toString());
                        particularsPO.setBirthday(LocalDate.parse(map.get("birthday").toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                        if (!StringUtils.isEmpty(resumeOrderVO.getPostId())) {
                            TCompanyPost byId = tCompanyPostService.getById(resumeOrderVO.getPostId());
                            particularsPO.setPostType(byId.getPostType());
                            particularsPO.setPostLabel(byId.getPostLabel());
                            particularsPO.setBeginSalary(byId.getBeginSalary());
                            particularsPO.setEndSalary(byId.getEndSalary());
                            particularsPO.setCompanyName(byId.getCompanyName());
                        }
                        orderInfo.setParticulars(JSON.toJSONString(particularsPO));
                        orderInfoService.saveOrUpdate(orderInfo);
                    }
                    companyUserRoleProduce.sendAsyncMessage(orderInfo);
                    return R.success(map.get("msg"));
                } else if (state == 1) {
                    orderInfo.setState(1);
                    return R.fail(map.get("msg").toString());
                }
            }
        } finally {
            lock.unlock();
        }
        return R.fail("服务忙！");
    }*/

    @GetMapping("/addOrder")
    public Boolean addOrder(@RequestParam("orderInfo") String orderInfo) {
        // System.out.println(orderInfo);
        OrderInfo orderInfo1 = JSON.parseObject(orderInfo, OrderInfo.class);
        // int i=1/0;
        boolean save = orderInfoService.save(orderInfo1);
        return save;
    }

    @GetMapping("/getPostLabel")
    @ApiOperation("推荐收益-企业名称查询")
    @ApiImplicitParam(name = "keyword", required = false, value = "关键词")
    @Log(title = "推荐收益-岗位名称查询", businessType = BusinessType.SELECT)
    public R<String> getPostLabel(@RequestParam(value = "keyword", required = false) String keyword) {
        List<String> list = orderInfoService.getPostLabel(companyMetadata.userid(), keyword);

        List list2 = new ArrayList(list.size());
        list.forEach(i -> {
            list2.add(i.substring(1, i.length() - 1));
        });
        return R.success(list2);
    }


    @GetMapping("/correct")
    @ApiOperation("矫正推荐时间接口")
    public void correct() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.in("identification", 2, 3, 5);
        List<OrderInfo> list = orderInfoService.list(queryWrapper);
        list.forEach(i -> {
            LocalDateTime correct = putInResumeApi.correct(i.getPutInResumeId());
            i.setRecommendTime(correct);
            orderInfoService.updateById(i);
        });
    }


    @PostMapping("/referralRevenue")
    @ApiOperation("推荐收益")
    public R<ReferralRevenueDTO> referralRevenue(@RequestBody ReferralRevenueQuery referralRevenueQuery) {
        referralRevenueQuery.setUserId(companyMetadata.userid());
        if (!StringUtils.isEmpty(referralRevenueQuery.getDate())) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            String formattedDate = sdf.format(referralRevenueQuery.getDate());
            referralRevenueQuery.setYear(formattedDate.substring(0, 4));
            referralRevenueQuery.setMonth(formattedDate.substring(5));
        }
        IPage<ReferralRevenueDTO> referralRevenueDTOIPage = orderInfoService.referralRevenue(referralRevenueQuery);
        List<ReferralRevenueDTO> records = referralRevenueDTOIPage.getRecords();
        records.forEach(i -> {
            i.setParticularsPO(JSON.parseObject(i.getParticulars(), ParticularsPO.class));
        });
        return R.success(referralRevenueDTOIPage);
    }


    @GetMapping("/postServiceCharge")
    public BigDecimal postServiceCharge(@RequestParam(value = "postId") String postId) {
        return orderInfoService.postServiceCharge(companyMetadata.userid(), postId);
    }

    @GetMapping("/mistake")
    @ApiOperation("报错上传")
    @ApiImplicitParam(name = "id", required = false, value = "选着")
    public void mistake(@RequestParam(value = "id") Integer id) throws Exception {
        if(id==1){
            throw  new Exception("错误");
        }else if(id==2){
            throw new  InterruptedException();
        }else if(id==3){
            throw new MysqlDataTruncation("错误",1,false,false,1,1,1);
        }
    }
}
