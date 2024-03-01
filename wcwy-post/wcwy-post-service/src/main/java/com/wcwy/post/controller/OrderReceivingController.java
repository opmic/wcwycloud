package com.wcwy.post.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wcwy.common.base.result.R;
import com.wcwy.common.base.utils.LocalDateTimeUtils;
import com.wcwy.common.redis.enums.QRCode;
import com.wcwy.common.redis.util.IDGenerator;
import com.wcwy.common.web.annotation.AutoIdempotent;
import com.wcwy.company.dto.ShareDataDTO;
import com.wcwy.company.po.PutPostShareDataPO;
import com.wcwy.post.dto.OrderReceivingDTO;
import com.wcwy.post.dto.OrderReceivingPostDTO;
import com.wcwy.post.entity.OrderReceiving;
import com.wcwy.post.entity.TCompanyPost;
import com.wcwy.post.query.OrderReceivingCollectQuery;
import com.wcwy.post.query.OrderReceivingPostQuery;
import com.wcwy.post.service.OrderReceivingService;
import com.wcwy.post.service.TCompanyPostService;
import com.wcwy.post.session.CompanyMetadata;
import com.wcwy.system.annotation.Log;
import com.wcwy.system.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName: OrderReceivingController
 * Description:
 * date: 2023/5/25 10:05
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@RestController
@Api(tags = "岗位接单接口")
@RequestMapping("/orderReceiving")
public class OrderReceivingController {

    @Autowired
    private OrderReceivingService orderReceivingService;
    @Autowired
    private IDGenerator idGenerator;
    @Autowired
    TCompanyPostService tCompanyPostService;
    @Autowired
    private CompanyMetadata companyMetadata;

    /**
     * @param rmUserId:推荐官id
     * @return null
     * @Description: 获取接单
     * @Author tangzhuo
     * @CreateTime 2023/5/25 10:07
     */

    @GetMapping("/selectCount")
    public  Map<String,Integer> selectCount(@RequestParam("rmUserId") String rmUserId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("recommerd", rmUserId);
        queryWrapper.eq("deleted", 0);
        queryWrapper.eq("cancel", 1);

        QueryWrapper queryWrapper1 = new QueryWrapper();
        queryWrapper1.eq("recommerd", rmUserId);
        queryWrapper1.eq("deleted", 0);
        queryWrapper1.eq("collect", 1);
        queryWrapper1.eq("cancel",2);
        Map<String,Integer> map=new HashMap<>(2);
        map.put("post",orderReceivingService.count(queryWrapper));
        map.put("collectPost",orderReceivingService.count(queryWrapper1));
        return map;
    }

    @GetMapping("/orderReceiving")
    @ApiOperation("岗位接单")
    @Log(title = "岗位接单", businessType = BusinessType.INSERT)
    @ApiImplicitParam(name = "postId", required = true, value = "岗位接单")
    public R orderReceiving(@RequestParam("postId") String postId) {
        TCompanyPost byId = tCompanyPostService.getById(postId);
        if (byId == null) {
            return R.fail("该岗位不存在！");
        }
        if (byId.getPostType() == 0) {
            return R.fail("普通岗位为不能接单！");
        }
        if (byId.getStatus() == 0) {
            return R.fail("该岗位已停止招聘！");
        }
        //查询是否存在
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("post_id", postId);
        queryWrapper.eq("recommerd", companyMetadata.userid());
        OrderReceiving one = orderReceivingService.getOne(queryWrapper);
        if (one != null) {
            one.setCancel(1);
            one.setUpdateTime(LocalDateTime.now());
            boolean b = orderReceivingService.updateById(one);
            if (b) {
                return R.success("已接单!");
            }
            return R.fail("操作失败!");
        }
        //不存在则添加
        OrderReceiving orderReceiving = new OrderReceiving();
        orderReceiving.setOrderReceivingId(idGenerator.generateCode("ORV"));
        orderReceiving.setCreateTime(LocalDateTime.now());
        orderReceiving.setDeleted(0);
        orderReceiving.setCancel(1);
        orderReceiving.setPostId(postId);
        orderReceiving.setRecommerd(companyMetadata.userid());
        orderReceiving.setUpdateTime(LocalDateTime.now());
        boolean save = orderReceivingService.save(orderReceiving);
        if (save) {
            return R.success("已接单!");
        }
        return R.fail("操作失败!");
    }

    @GetMapping("/cancelOrderReceivingId")
    @ApiOperation("取消接单")
    @Log(title = "取消接单", businessType = BusinessType.INSERT)
    @ApiImplicitParam(name = "postId", required = true, value = "接单id")
    @AutoIdempotent
    public R cancelOrderReceivingId(@RequestParam("postId") String postId) {
        UpdateWrapper queryWrapper = new UpdateWrapper();
        queryWrapper.eq("post_id", postId);
        queryWrapper.eq("recommerd", companyMetadata.userid());
        queryWrapper.set("cancel", 0);
        boolean update = orderReceivingService.update(queryWrapper);
        if (update) {
            return R.success("已取消!");
        }
        return R.fail("操作失败!");
    }

    @GetMapping("/collect")
    @ApiOperation("收藏岗位")
    @Log(title = "收藏岗位", businessType = BusinessType.INSERT)
    @ApiImplicitParam(name = "postId", required = true, value = "岗位接单")
    @AutoIdempotent
    public R collect(@RequestParam("postId") String postId) {
        TCompanyPost byId = tCompanyPostService.getById(postId);
        if (byId == null) {
            return R.fail("该岗位不存在！");
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("post_id", postId);
        queryWrapper.eq("recommerd", companyMetadata.userid());
        OrderReceiving one = orderReceivingService.getOne(queryWrapper);
        if (one != null) {
            one.setCollect(1);
            one.setCollectTime(LocalDateTime.now());
            boolean b = orderReceivingService.updateById(one);
            if (b) {
                return R.success("已收藏!");
            }
            return R.fail("操作失败!");
        }
        //不存在则添加
        OrderReceiving orderReceiving = new OrderReceiving();
        orderReceiving.setOrderReceivingId(idGenerator.generateCode("ORV"));
        orderReceiving.setCollectTime(LocalDateTime.now());
        orderReceiving.setDeleted(0);
        orderReceiving.setCollect(1);
        orderReceiving.setPostId(postId);
        orderReceiving.setRecommerd(companyMetadata.userid());
        boolean save = orderReceivingService.save(orderReceiving);
        if (save) {
            return R.success("已收藏!");
        }
        return R.fail("操作失败!");
    }

    @GetMapping("/cancelCollect")
    @ApiOperation("取消收藏岗位")
    @Log(title = "取消收藏岗位", businessType = BusinessType.UPDATE)
    @ApiImplicitParam(name = "postId", required = true, value = "岗位id")
    @AutoIdempotent
    public R cancelCollect(@RequestParam("postId") String postId) {
        UpdateWrapper queryWrapper = new UpdateWrapper();
        queryWrapper.eq("post_id", postId);
        queryWrapper.eq("recommerd", companyMetadata.userid());
        queryWrapper.set("collect", 0);
        boolean update = orderReceivingService.update(queryWrapper);
        if (update) {
            return R.success("已取消!");
        }
        return R.fail("操作失败!");
    }


    @PostMapping("/selectCollect")
    @ApiOperation("查询收藏的岗位")
    @Log(title = "查询收藏的岗位", businessType = BusinessType.SELECT)
    public R<OrderReceivingPostDTO> selectCollect(@RequestBody OrderReceivingCollectQuery orderReceivingCollectQuery) {
        String substring = companyMetadata.userid().substring(0, 2);
        if (!"TR".equals(substring)) {
            return R.fail("请使用推荐官账号!");
        }
        IPage<OrderReceivingPostDTO> iPage = orderReceivingService.selectCollect(orderReceivingCollectQuery, companyMetadata.userid());
        return R.success(iPage);
    }

    @PostMapping("/orderReceivingConpanyName")
    @ApiOperation("查询接单企业的名称")
    @Log(title = "查询接单企业的名称", businessType = BusinessType.SELECT)
    @ApiImplicitParam(name = "keyword", required = false, value = "关键字")
    public R<String> orderReceivingCompanyName(@RequestParam(value = "keyword", required = false) String keyword) {
        List<String> list = orderReceivingService.orderReceivingCompanyName(companyMetadata.userid(), keyword);
        return R.success(list);
    }

    @PostMapping("/orderReceivingPostName")
    @ApiOperation("查询接单岗位的名称")
    @Log(title = "查询接单岗位的名称", businessType = BusinessType.SELECT)
    @ApiImplicitParam(name = "keyword", required = false, value = "关键字")
    public R<String> orderReceivingPostName(@RequestParam(value = "keyword", required = false) String keyword) {
        List<String> list = orderReceivingService.orderReceivingPostName(companyMetadata.userid(), keyword);
        return R.success(list);
    }

    @PostMapping("/orderReceivingPost")
    @ApiOperation("我接单的岗位")
    @Log(title = "我接单的岗位", businessType = BusinessType.SELECT)
    public R<OrderReceivingDTO> orderReceivingPost(@Valid @RequestBody OrderReceivingPostQuery orderReceivingPostQuery) {
        orderReceivingPostQuery.setUserId(companyMetadata.userid());
        IPage<OrderReceivingDTO> iPage = orderReceivingService.orderReceivingPost(orderReceivingPostQuery);
        Map<String, Object> map = new HashMap<>(2);
        map.put("iPage", iPage);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("recommerd", companyMetadata.userid());
        queryWrapper.eq("cancel", 1);
        map.put("count", orderReceivingService.count(queryWrapper));
        return R.success(map);
    }


    @GetMapping("/orderReceivingDay")
    public Map<String, List<PutPostShareDataPO>> orderReceivingDay(@RequestParam("id") String id, @RequestParam("localDate") LocalDate localDate,@RequestParam(value = "city",required = false) String city,@RequestParam(value = "postType") Integer postType) {
        List<PutPostShareDataPO> orderReceivingDay = orderReceivingService.orderReceivingDay(id, localDate,city,postType);
        LocalDate date = localDate.plusDays(-1);
        List<PutPostShareDataPO> orderReceivingDay1 = orderReceivingService.orderReceivingDay(id, date,city,postType);
        Map<String, List<PutPostShareDataPO>> map = new HashMap<>();
        map.put("A", orderReceivingDay);
        map.put("B", orderReceivingDay1);
        return map;
    }

    @GetMapping("/orderReceivingWeek")
    public Map<String, List<PutPostShareDataPO>> orderReceivingWeek(@RequestParam("id") String id, @RequestParam("beginDate") LocalDate beginDate,@RequestParam("allEndDate") LocalDate allEndDate,@RequestParam(value = "city",required = false) String city,@RequestParam(value = "postType") Integer postType) {
        List<PutPostShareDataPO> orderReceivingDay = orderReceivingService.orderReceivingWeek(id, beginDate,allEndDate,city,postType);

        LocalDate date = LocalDateTimeUtils.lastWeekStartTime(beginDate);
        LocalDate date1 = LocalDateTimeUtils.lastWeekEndTime(beginDate);
        List<PutPostShareDataPO> orderReceivingDay1 = orderReceivingService.orderReceivingWeek(id, date,date1,city,postType);
        Map<String, List<PutPostShareDataPO>> map = new HashMap<>();
        map.put("A", orderReceivingDay);
        map.put("B", orderReceivingDay1);
        return map;
    }
    @GetMapping("/orderReceivingMonth")
    public Map<String, List<PutPostShareDataPO>> orderReceivingMonth(@RequestParam("id") String id, @RequestParam("beginDate") LocalDate beginDate,@RequestParam("allEndDate") LocalDate allEndDate,@RequestParam(value = "city",required = false) String city,@RequestParam(value = "postType") Integer postType){
        List<PutPostShareDataPO> orderReceivingDay = orderReceivingService.orderReceivingWeek(id, beginDate,allEndDate,city,postType);

        List<PutPostShareDataPO> orderReceivingDay1 = orderReceivingService.orderReceivingWeek(id, LocalDateTimeUtils.lastMonthStartTime(beginDate),LocalDateTimeUtils.lastMonthEndTime(beginDate),city,postType);
        Map<String, List<PutPostShareDataPO>> map = new HashMap<>();
        map.put("A", orderReceivingDay);
        map.put("B", orderReceivingDay1);
        return map;
    }
}
