package com.wcwy.company.controller;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wcwy.common.base.result.R;
import com.wcwy.common.base.utils.NameUtils;
import com.wcwy.common.base.utils.PhoneUtils;
import com.wcwy.common.redis.enums.InvitationCode;
import com.wcwy.common.redis.util.IDGenerator;
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.company.dto.PositionAppliedDTO;
import com.wcwy.company.dto.ReferrerRecordJobHunterDTO;
import com.wcwy.company.entity.EiCompanyPost;
import com.wcwy.company.entity.PositionApplied;
import com.wcwy.company.entity.ReferrerRecord;
import com.wcwy.company.entity.TJobhunter;
import com.wcwy.company.query.PositionAppliedQuery;
import com.wcwy.company.service.*;
import com.wcwy.company.session.CompanyMetadata;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * ClassName: PositionAppliedController
 * Description:
 * date: 2023/8/30 13:59
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Api(tags = "求职者意向职位接口")
@RequestMapping("/positionApplied")
@RestController
public class PositionAppliedController {
    @Autowired
    private PositionAppliedService positionAppliedService;
    @Autowired
    private EiCompanyPostService eiCompanyPostService;
    @Autowired
    private CompanyMetadata companyMetadata;
    @Autowired
    private TJobhunterService tJobhunterService;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private TJobhunterResumeService tJobhunterResumeService;
    @Autowired
    private TJobhunterExpectPositionService tJobhunterExpectPositionService;
    @Autowired
    private ReferrerRecordService referrerRecordService;
    @Autowired
    private TJobhunterEducationRecordService tJobhunterEducationRecordService;

    @Autowired
    private IDGenerator idGenerator;

/*    @GetMapping("/put")
    @ApiOperation("投递推荐官分享职位")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "postId", required = true, value = "岗位id"),
            @ApiImplicitParam(name = "time", required = true, value = "推广时间"),
            @ApiImplicitParam(name = "QRCode", required = true, value = "邀请码")
    })*/
    public R put(@RequestParam("postId") String postId, @RequestParam("time") Long time, @RequestParam("QRCode") String QRCode) {
        String userid = companyMetadata.userid();
        String substring = userid.substring(0, 2);
        if (!"TJ".equals(substring)) {
            return R.fail("请使用求职者账号!");
        }
        if ("TJ".equals(userid)) {
            TJobhunter byId = tJobhunterService.getById(companyMetadata.userid());
            if (StringUtils.isEmpty(byId.getUserName())) {
                return R.fail("请完善简历信息,该岗位已给您收藏!");
            }
            String advantage = tJobhunterResumeService.getAdvantage(companyMetadata.userid());
            if (StringUtils.isEmpty(advantage)) {
                return R.fail("请完善个人优势,该岗位已给您收藏!");
            }
            String eduId = tJobhunterEducationRecordService.getEduId(companyMetadata.userid());
            if (StringUtils.isEmpty(eduId)) {
                return R.fail("请完善个人学历,该岗位已给您收藏!");
            }
            String postionId = tJobhunterExpectPositionService.postionId(companyMetadata.userid());
            if (StringUtils.isEmpty(postionId)) {
                return R.fail("请完善求职期望,该岗位已给您收藏!");
            }
        }
        boolean aBoolean = redisUtils.setIfAbsent(userid + postId, postId, 7 * 24);
        if (!aBoolean) {
            return R.fail("七日内不可重复投递!");
        }
        //判断是否携带邀请码
        if (!StringUtils.isEmpty(QRCode)) {
            //获取邀请的推荐官
            Object o = redisUtils.get(InvitationCode.INVITATION_URL_CODE.getType() + QRCode);
            if (!StringUtils.isEmpty(o)) {
                EiCompanyPost byId = eiCompanyPostService.getById(postId);
                Integer postType = byId.getPostType();
                //判断是否是入职付,满月付,到面付
                if (postType > 0 && postType < 4) {
                    PositionApplied positionApplied = new PositionApplied();
                    QueryWrapper queryWrapper = new QueryWrapper();
                    queryWrapper.eq("t_job_hunter_id", userid);
                    queryWrapper.eq("recommend_id", o);
                    ReferrerRecord one = referrerRecordService.getOne(queryWrapper);
                    //判断人才对推荐官的类型
                    if (one != null && one.getCorrelationType() == 0) {
                        positionApplied.setSource(0);
                    } else {
                        positionApplied.setSource(1);
                    }

                    positionApplied.setPositionAppliedId(idGenerator.generateCode("PA"));
                    positionApplied.setJobHunter(userid);
                    positionApplied.setPutTime(LocalDateTime.now());
                    // 将时间戳转换为LocalDateTime
                    Instant instant = Instant.ofEpochMilli(time);
                    positionApplied.setPromotionTime(LocalDateTime.ofInstant(instant, ZoneId.systemDefault()));
                    positionApplied.setPostLabel(byId.getPostLabel());
                    positionApplied.setPostType(byId.getPostType());
                    positionApplied.setCompanyId(byId.getCompanyId());
                    positionApplied.setRecommendId(o.toString());
                    boolean save = positionAppliedService.save(positionApplied);
                    if (save) {
                        return R.success("操作成功!");
                    }
                }
            }
        }
        redisUtils.del(userid + postId);
        return R.fail("邀请链接已过期!");
    }

    @PostMapping("/select")
    @ApiOperation("人才库数据")
    public R<PositionAppliedDTO> select(@RequestBody PositionAppliedQuery positionAppliedQuery) {
        if(StringUtils.isEmpty(positionAppliedQuery.getSource())){
            positionAppliedQuery.setSource(0);
        }
        positionAppliedQuery.setUserid(companyMetadata.userid());
        IPage<PositionAppliedDTO> positionAppliedDTOIPage = positionAppliedService.select(positionAppliedQuery);

        List<PositionAppliedDTO> records = positionAppliedDTOIPage.getRecords();
        if(records.size()>0){
            for (PositionAppliedDTO record : records) {
                try {
                    if(record.getDownloadIf()==null || record.getDownloadIf() ==0){
                        String asterisk = NameUtils.createAsterisk(record.getUserName(), 1);
                        record.setUserName(asterisk);
                    }
                }catch (Exception e){
                    continue;
                }
            }

        }

        return R.success(positionAppliedDTOIPage);
    }

}
