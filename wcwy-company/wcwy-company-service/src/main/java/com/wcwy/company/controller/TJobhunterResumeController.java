package com.wcwy.company.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wcwy.common.base.result.R;
import com.wcwy.common.redis.enums.RedisCache;
import com.wcwy.common.redis.enums.Sole;
import com.wcwy.common.redis.util.IDGenerator;
import com.wcwy.company.dto.DetailedTJobhunterResumeDTO;
import com.wcwy.company.entity.CompanyGlanceOver;
import com.wcwy.company.entity.TJobhunter;
import com.wcwy.company.entity.TJobhunterResume;
import com.wcwy.company.entity.TJobhunterResumeConfig;
import com.wcwy.company.po.TJobhunterPO;
import com.wcwy.company.query.SendAResumeQuery;
import com.wcwy.company.service.*;
import com.wcwy.company.session.CompanyMetadata;
import com.wcwy.company.vo.AdvantageVO;
import com.wcwy.post.api.CompanyUserRoleApi;
import com.wcwy.system.annotation.Log;
import com.wcwy.system.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 * @description 针对表【t_jobhunter_resume(求职者简历)】的数据库操作Service
 * @createDate 2022-10-21 10:09:56
 */
@RestController
@RequestMapping("/jobhunterResume")
@Api(tags = "求职者简历接口")
@Slf4j
public class TJobhunterResumeController {
    @Autowired
    private TJobhunterResumeService tJobhunterResumeService;
    @Autowired
    private TJobhunterEducationRecordService tJobhunterEducationRecordService;
    @Resource
    private TJobhunterExpectPositionService tJobhunterExpectPositionService;
    @Autowired
    private TJobhunterProjectRecordService tJobhunterProjectRecordService;
    @Autowired
    private TJobhunterWorkRecordService tJobhunterWorkRecordService;
    @Autowired
    private ReferrerRecordService referrerRecordService;

    @Autowired
    private TJobhunterHideCompanyService tJobhunterHideCompanyService;
    @Autowired
    private CompanyUserRoleService companyUserRoleService;
    @Autowired
    private CompanyMetadata companyMetadata;
    @Resource
    private PutInResumeService putInResumeService;
    @Autowired
    private TJobhunterResumeConfigService tJobhunterResumeConfigService;
    @Autowired
    private CompanyGlanceOverService companyGlanceOverService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private TJobhunterService tJobhunterService;
    @Autowired
    private CompanyUserRoleApi companyUserRoleApi;
    @Autowired
    private IDGenerator idGenerator;

    @PostMapping("/updateAdvantage")
    @ApiOperation("修改/添加 优势亮点")
/*    @ApiImplicitParams({
            @ApiImplicitParam(name = "advantage", required = true, value = "优势亮点"),
            @ApiImplicitParam(name = "resumeId", required = false, value = "简历id")
    })*/
    @Transactional
    @Log(title = "修改/添加 优势亮点", businessType = BusinessType.SELECT)
    public R updateAdvantage(@RequestBody AdvantageVO advantage) {
        String userid = companyMetadata.userid();

        String substring = userid.substring(0, 2);
        if (!substring.equals("TJ")) {
            return R.fail("请使用求职者账号！");
        }
        List<String> strings = new ArrayList<>(1);
        strings.add(advantage.getAdvantage());
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("t_jobhunter_id", companyMetadata.userid());
        int count = tJobhunterResumeService.count(queryWrapper);
        //如果未创建  就创建一下
        if (count == 0) {
            TJobhunterResume tJobhunterResume = new TJobhunterResume();
            tJobhunterResume.setResumeId(idGenerator.generateCode("JR"));
            tJobhunterResume.setJobhunterId(companyMetadata.userid());
            tJobhunterResume.setResumeName("我的简历");
            tJobhunterResume.setResumeExamineStatus(1);
            tJobhunterResume.setResume(1);
            tJobhunterResume.setDeleted(1);

            tJobhunterResume.setAdvantage(strings);
            tJobhunterResume.setCreateTime(LocalDateTime.now());
            boolean save1 = tJobhunterResumeService.save(tJobhunterResume);
            if (save1) {
                log.info("简历建成功");
            } else {
                log.error("简历建失败");
            }
            TJobhunterResumeConfig tJobhunterResumeConfig = new TJobhunterResumeConfig();
            tJobhunterResumeConfig.setUserId(companyMetadata.userid());
            tJobhunterResumeConfig.setCreateTime(LocalDateTime.now());
            boolean save = tJobhunterResumeConfigService.save(tJobhunterResumeConfig);
            if (save) {
                log.info("简历配置创建成功");
            } else {
                log.error("简历配置创建失败");
            }
            return R.success("添加成功");
        }
        if (count > 0 && StringUtils.isEmpty(advantage.getResumeId())) {
            return R.fail("你有两份简历,请传入简历id!");
        }
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("resume_id", advantage.getResumeId());
        updateWrapper.eq("t_jobhunter_id", companyMetadata.userid());
        updateWrapper.set("advantage", JSON.toJSONString(strings));
        boolean update = tJobhunterResumeService.update(updateWrapper);
        if (update) {
            return R.success("添加成功");
        }
        return R.fail("添加失败");
    }

    @GetMapping("/recommendingOfficerUpdate")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "advantage", required = true, value = "优势亮点"),
            @ApiImplicitParam(name = "userId", required = true, value = "求职者id"),
            @ApiImplicitParam(name = "resumeId", required = true, value = "简历id")
    })
    @ApiOperation("推荐官修改求职者的个人优势")
    @Log(title = "推荐官修改求职者的个人优势", businessType = BusinessType.UPDATE)
    public R recommendingOfficerUpdate(@RequestParam(value = "advantage") String advantage, @RequestParam(value = "userId") String userId, @RequestParam(value = "resumeId") String resumeId) {
        if (StringUtils.isEmpty(advantage) || StringUtils.isEmpty(userId)) {
            return R.fail("优势亮点或求职者id不能为空!");
        }
        Boolean is = referrerRecordService.getCorrelationType(companyMetadata.userid(), userId);
        if (!is) {
            return R.fail("该求职者不是您的新增人才!");
        }
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("resume_id", resumeId);
        ArrayList<Object> objects = new ArrayList<>(1);
        boolean add = objects.add(advantage);
        updateWrapper.set("advantage", JSON.toJSONString(objects));
        updateWrapper.set("update_time", LocalDateTime.now());
        boolean update = tJobhunterResumeService.update(updateWrapper);
        if (update) {
            return R.success("修改成功!");
        }
        return R.fail("修改失败!");
    }


    @ApiOperation("获取求职者详细信息")
    @GetMapping("/detailedTJobhunterResumeDTO/{jobhunterId}")
    @ApiImplicitParam(name = "jobhunterId", required = true, value = "求职者id")
    @Log(title = "获取求职者详细信息", businessType = BusinessType.SELECT)
    public R<DetailedTJobhunterResumeDTO> detailedTJobhunterResumeDTO(@PathVariable("jobhunterId") String jobhunterId) {
        if (StringUtils.isEmpty(jobhunterId)) {
            return R.fail("求职者id不能为空");
        }
       /* ValueOperations<String, TJobhunterResumeConfig> TJobhunterResumeConfig = redisTemplate.opsForValue();
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        TJobhunterResumeConfig tJobhunterResumeConfig = TJobhunterResumeConfig.get(RedisCache.TJOBHUNTERRESUMECONFIG.getValue() + ":" + jobhunterId);
*/
        //查看是否是下载了该简历  如果下载则放行
        Boolean download = companyUserRoleService.isDownload(jobhunterId, companyMetadata.userid());
        if (!download) {
            List<Object> objects = tJobhunterHideCompanyService.cacheCompany(jobhunterId);
            boolean contains = objects.contains(companyMetadata.userid());
            if (contains) {
                return R.fail("该求职者屏蔽了简历！");
            }
        }

        //获取求职者详细信息
        DetailedTJobhunterResumeDTO detailedTJobhunterResumeDTO = tJobhunterResumeService.detailedTJobhunterResumeDTO(jobhunterId);
        if (detailedTJobhunterResumeDTO == null) {
            return R.fail("该求职者不存在!");
        }
       /* if (tJobhunterResumeConfig == null) {
            tJobhunterResumeConfigService.updateRedis(jobhunterId);//去创建
            tJobhunterResumeConfig = TJobhunterResumeConfig.get(RedisCache.TJOBHUNTERRESUMECONFIG.getValue() + ":" + jobhunterId);
        }*/
        //当不是求职者自己查看时
   /*     if (!companyMetadata.userid().equals(jobhunterId)) {
            //查看企业是否有权限查简历
            String substring = companyMetadata.userid().substring(0, 2);
            if ("TC".equals(substring)) {
                //判断是否投了改简历
                String s = valueOperations.get(Sole.SELECT_TJ.getKey() + companyMetadata.userid() + jobhunterId);
                if (StringUtils.isEmpty(s)) {
                    //没有的话查看企业7天投简接口
                    QueryWrapper queryWrapper = new QueryWrapper();
                    queryWrapper.eq("put_in_comppany", companyMetadata.userid());
                    queryWrapper.eq("put_in_jobhunter", jobhunterId);
                    queryWrapper.le("update_time", LocalDateTime.of(LocalDate.now().plusDays(-6), LocalTime.MIN));
                    List list = putInResumeService.list(queryWrapper);
                    if (list.size() > 0) {
                        //如果查看一周类还有联系的更新7天查看权限
                        valueOperations.set(Sole.SELECT_TJ.getKey() + companyMetadata.userid() + jobhunterId, jobhunterId, 604800, TimeUnit.SECONDS);
                        s = valueOperations.get(Sole.SELECT_TJ.getKey() + companyMetadata.userid() + jobhunterId);
                    }
                }
                if (StringUtils.isEmpty(s) && tJobhunterResumeConfig != null && tJobhunterResumeConfig.getHunterVisible() == 1) {
                    return R.fail("该求职者对企业隐藏了简历");
                } else {  //查看企业是否有权限查看联系方式
                    R<Boolean> booleanR = companyUserRoleApi.selectExamineRole(companyMetadata.userid(), jobhunterId);
                    detailedTJobhunterResumeDTO.setViewDetails(booleanR.getData());
                }
            } else if (tJobhunterResumeConfig != null && tJobhunterResumeConfig.getVisible() == 1) {
                return R.fail("该求职者隐藏了简历");
            }
        }*/
        //添加浏览
        if ("TC".equals(companyMetadata.userid().substring(0, 2))) {
            CompanyGlanceOver companyGlanceOver = new CompanyGlanceOver();
            companyGlanceOver.setGlanceOverTime(LocalDateTime.now());
            companyGlanceOver.setGlanceOverId(idGenerator.generateCode("CG"));
            companyGlanceOver.setGlanceOverUserId(jobhunterId);
            companyGlanceOver.setCompanyId(companyMetadata.userid());
            companyGlanceOver.setDeleted(0);
            companyGlanceOverService.addCompanyGlanceOver(companyGlanceOver);
        }
        TJobhunter byId = tJobhunterService.getById(jobhunterId);
        TJobhunterPO tJobhunterPO = new TJobhunterPO();
        if (byId != null) {
            BeanUtils.copyProperties(byId, tJobhunterPO);
        }
        detailedTJobhunterResumeDTO.setTJobhunterPO(tJobhunterPO);

        return R.success(detailedTJobhunterResumeDTO);
    }

    @ApiOperation("企业通过子企业获取求职者详细信息")
    @GetMapping("/detailedTJobhunterResumeDTO/{jobhunterId}/{companyId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "jobhunterId", required = true, value = "求职者id"),
            @ApiImplicitParam(name = "companyId", required = true, value = "子企业id")
    })
    @Log(title = "企业通过子企业获取求职者详细信息", businessType = BusinessType.SELECT)
    public R<DetailedTJobhunterResumeDTO> selectTJobhunterResumeDTO(@PathVariable("jobhunterId") String jobhunterId, @PathVariable("companyId") String companyId) {
        if (StringUtils.isEmpty(jobhunterId) || StringUtils.isEmpty(companyId)) {
            return R.fail("求职者id和子企业id不能为空");
        }

        ValueOperations<String, TJobhunterResumeConfig> TJobhunterResumeConfig = redisTemplate.opsForValue();
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        TJobhunterResumeConfig tJobhunterResumeConfig = TJobhunterResumeConfig.get(RedisCache.TJOBHUNTERRESUMECONFIG.getValue() + ":" + jobhunterId);
        //获取求职者详细信息
        DetailedTJobhunterResumeDTO detailedTJobhunterResumeDTO = tJobhunterResumeService.detailedTJobhunterResumeDTO(jobhunterId);
        if (tJobhunterResumeConfig == null) {
            tJobhunterResumeConfigService.updateRedis(jobhunterId);//去创建
            tJobhunterResumeConfig = TJobhunterResumeConfig.get(RedisCache.TJOBHUNTERRESUMECONFIG.getValue() + ":" + jobhunterId);
        }
        //当不是求职者自己查看时
        if (!companyId.equals(jobhunterId)) {
            //查看企业是否有权限查简历
            if (companyId.substring(0, 2).equals("TC")) {
                //判断是否投了改简历
                String s = valueOperations.get(Sole.SELECT_TJ.getKey() + companyId + jobhunterId);
                if (StringUtils.isEmpty(s)) {
                    //没有的话查看企业7天投简接口
                    QueryWrapper queryWrapper = new QueryWrapper();
                    queryWrapper.eq("put_in_comppany", companyId);
                    queryWrapper.eq("put_in_jobhunter", jobhunterId);
                    queryWrapper.le("update_time", LocalDateTime.of(LocalDate.now().plusDays(-6), LocalTime.MIN));
                    List list = putInResumeService.list(queryWrapper);
                    if (list.size() > 0) {
                        //如果查看一周类还有联系的更新7天查看权限
                        valueOperations.set(Sole.SELECT_TJ.getKey() + companyMetadata.userid() + jobhunterId, jobhunterId, 604800, TimeUnit.SECONDS);
                        s = valueOperations.get(Sole.SELECT_TJ.getKey() + companyMetadata.userid() + jobhunterId);
                    }
                }
                if (StringUtils.isEmpty(s) && tJobhunterResumeConfig != null && tJobhunterResumeConfig.getHunterVisible() == 1) {
                    return R.fail("该求职者对企业隐藏了简历");
                } else {  //查看企业是否有权限查看联系方式
                    R<Boolean> booleanR = companyUserRoleApi.selectExamineRole(companyId, jobhunterId);
                    detailedTJobhunterResumeDTO.setViewDetails(booleanR.getData());
                }
            } else if (tJobhunterResumeConfig != null && tJobhunterResumeConfig.getVisible() == 1) {
                return R.fail("该求职者隐藏了简历");
            }
        }
        //添加浏览
        if ("TC".equals(companyMetadata.userid().substring(0, 2))) {
            CompanyGlanceOver companyGlanceOver = new CompanyGlanceOver();
            companyGlanceOver.setGlanceOverTime(LocalDateTime.now());
            companyGlanceOver.setGlanceOverId(idGenerator.generateCode("CG"));
            companyGlanceOver.setGlanceOverUserId(jobhunterId);
            companyGlanceOver.setCompanyId(companyMetadata.userid());
            companyGlanceOver.setDeleted(0);
            companyGlanceOverService.addCompanyGlanceOver(companyGlanceOver);
        }


        TJobhunter byId = tJobhunterService.getById(jobhunterId);
        TJobhunterPO tJobhunterPO = new TJobhunterPO();
        if (byId != null) {
            BeanUtils.copyProperties(byId, tJobhunterPO);
        }
        detailedTJobhunterResumeDTO.setTJobhunterPO(tJobhunterPO);
        return R.success(detailedTJobhunterResumeDTO);
    }


    @PostMapping("/sendAResume")
    @ApiOperation("获取能投简的求职者")
    @Log(title = "获取能投简的求职者", businessType = BusinessType.SELECT)
    public R<DetailedTJobhunterResumeDTO> sendAResume(@Valid @RequestBody SendAResumeQuery sendAResumeQuery) {
        String userid = companyMetadata.userid();
        String key = Sole.DELIVER_TR.getKey() + sendAResumeQuery.getPostId() + userid;
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        Set<String> members = setOperations.members(key);
        List<String> list = new ArrayList<>(members);
        if (list.size() > 0) {
            sendAResumeQuery.setList(list);
        }
        sendAResumeQuery.setUserId(userid);
        Page<DetailedTJobhunterResumeDTO> detailedTJobHunterResumeDTO = tJobhunterResumeService.sendAResume(sendAResumeQuery);
        return R.success(detailedTJobHunterResumeDTO);
    }


    @GetMapping("/select")
    @ApiOperation("我的简历")
    @Log(title = "我的简历", businessType = BusinessType.SELECT)
    public R<TJobhunterResume> selectTJobHunterResume() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("t_jobhunter_id", companyMetadata.userid());
        TJobhunterResume byId = tJobhunterResumeService.getOne(queryWrapper);
        return R.success(byId);
    }



    @GetMapping("/createPerfectDegree")
    @ApiOperation("判断简历完善度")
    @Log(title = "判断简历完善度", businessType = BusinessType.SELECT)
    public R isSendAResume() {
        String userid = companyMetadata.userid().substring(0, 2);

        //Map map=new HashMap();
        if ("TJ".equals(userid)) {
            R r = tJobhunterService.isSendAResume(companyMetadata.userid());
            return r;
        }
        return R.fail("请使用求职者账号!");
    }

    @GetMapping("/test")
    public R test() {
        List<TJobhunter> list = tJobhunterService.list();
        for (TJobhunter tJobhunter : list) {
            R sendAResume = tJobhunterService.isSendAResume(tJobhunter.getUserId());
        }
        return R.success();

    }


    @GetMapping("/isSendAResume")
    @ApiOperation("判断是否能投简")
    @Log(title = "判断是否能投简", businessType = BusinessType.SELECT)
    public R createPerfectDegree() {
        Map map = tJobhunterService.createPerfectDegree(companyMetadata.userid());
        return R.success(map);

    }


  /*  @GetMapping("/deleteTJobHunterResume")
    @ApiOperation("删除")
    @ApiImplicitParam(name = "resumeId", required = true, value = "简历id")
    public R deleteTJobHunterResume(@RequestParam(value = "resumeId",required = true) String resumeId){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("resume_id",resumeId);
        tJobhunterResumeService.removeById(resumeId);
        tJobhunterResumeConfigService.remove(queryWrapper);
    }*/


}
