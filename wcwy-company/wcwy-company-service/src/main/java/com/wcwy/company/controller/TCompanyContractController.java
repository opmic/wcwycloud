package com.wcwy.company.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.wcwy.common.base.result.R;
import com.wcwy.common.redis.util.IDGenerator;
import com.wcwy.common.web.annotation.AutoIdempotent;
import com.wcwy.company.config.CosUtils;
import com.wcwy.company.dto.TCompanyContractDTO;
import com.wcwy.company.entity.TCompanyContract;
import com.wcwy.company.entity.TCompanyContractAudit;
import com.wcwy.company.service.TCompanyContractAuditService;
import com.wcwy.company.service.TCompanyContractService;
import com.wcwy.company.session.CompanyMetadata;
import com.wcwy.company.vo.AddContractVO;
import com.wcwy.company.vo.ValidList;
import com.wcwy.system.annotation.Log;
import com.wcwy.system.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;

/**
 * ClassName: TCompanyContractController
 * Description:
 * date: 2022/11/17 17:22
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Api(tags = "企业合同表")
@RestController
@RequestMapping("/companyContract")
@Slf4j
public class TCompanyContractController {
    @Autowired
    private CompanyMetadata companyMetadata;
    @Autowired
    private IDGenerator idGenerator;
    @Autowired
    private TCompanyContractService tCompanyContractService;


    @Autowired
    private TCompanyContractAuditService tCompanyContractAuditService;
    @Autowired
    private CosUtils cosUtils;
    private final static Integer FILE_SIZE = 40;//文件上传限制大小
    private final static String FILE_UNIT = "M";//文件上传限制单位（B,K,M,G）
   /* @ApiOperation("添加合同")
    @PostMapping("/addContract")
    public R addContract(@Valid @RequestBody AddContractVO addContractVO) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("create_id",companyMetadata.userid());
        TCompanyContract byId = tCompanyContractService.getOne(queryWrapper);
        if(byId !=null){
            return R.fail("您已上传合同!");
        }

        if (!companyMetadata.userid().equals(addContractVO.getCompanyId())) {
            return R.fail("登录与传入id不一致 请检查！");
        }
        TCompanyContract tCompanyContract = new TCompanyContract();
        tCompanyContract.setCreateId(addContractVO.getCompanyId());
        tCompanyContract.setContractDate(addContractVO.getContractDate());
        tCompanyContract.setSignContract(addContractVO.getSignContract());
        boolean update = tCompanyContractService.save(tCompanyContract);
        if (update) {
            return R.success("添加成功!");
        }
        return R.fail("添加失败!请联系管理员");
    }

    @ApiOperation("审核合同")
    @PostMapping("/auditContract")
    public R auditContract(@Valid @RequestBody AuditContractVO auditContractVO) {
        if (auditContractVO.getAuditContract() == 2 && StringUtils.isEmpty(auditContractVO.getAuditCause())) {
            return R.fail("请填写失败原因！");
        }
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("contract_id", auditContractVO.getContractId());
        updateWrapper.set("audit_contract", auditContractVO.getAuditContract());
        updateWrapper.set("audit_cause", auditContractVO.getAuditCause());
        updateWrapper.set("audit_time", LocalDateTime.now());
        boolean update = tCompanyContractService.update(updateWrapper);
        if (update) {
            return R.success("审核成功!");
        }
        return R.fail("审核失败!");
    }

    @GetMapping("/existContract")
    @ApiOperation("查看合同是否存在或者是是否过期")
    public R existContract() {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("create_id",companyMetadata.userid());
        TCompanyContract byId = tCompanyContractService.getOne(queryWrapper);
        if (byId != null) {
            if (StringUtils.isEmpty(byId.getSignContract())) {
                return R.success("请下载上传您的合同!", false);
            }
            if(byId.getAuditContract()==0){
                return R.success("您的合同在审核中！", false);
            }
            if(byId.getAuditContract()==2){
                return R.success("您的合同在审核失败,请查看审核失败原因！", false);
            }
            if (!LocalDate.now().isBefore(byId.getContractDate())) {
                return R.success("您的合同已过期，请重新下载上传！", false);
            }
            return R.success("合同有效", true);
        }
        return R.fail("您未上传合同,请上传您的合同!");
    }*/

/*
    @GetMapping("/selectContract")
    @ApiOperation("查看合同")
    public R selectContract(){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("create_id",companyMetadata.userid());
        TCompanyContract byId = tCompanyContractService.getOne(queryWrapper);
        if(byId !=null){
            return R.success(byId);
        }
        return R.success("您未上传合同!");
    }
*/


 /*   @GetMapping("/applyFor")
    @ApiOperation("入职付+满月付+到面付")
    @ApiImplicitParam(name = "postType", required = true, value = "岗位类型(1:入职付 2:满月付 3:到面付)")
    @Log(title = "入职付+满月付+到面付", businessType = BusinessType.INSERT)
    public R applyFor(@RequestParam("postType") Integer postType){
        if(postType <1 && postType >3){
            return R.fail("未知操作!");
        }
        String userid = this.companyMetadata.userid();
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("create_id",userid);
        queryWrapper.eq("post_type",postType);
        queryWrapper.eq("deleted",0);
        int count = tCompanyContractService.count(queryWrapper);
        if(count>0){
            return R.fail("您已提交申请,请不要重复提交申请");
        }
        TCompanyContract tCompanyContract=new TCompanyContract();
        tCompanyContract.setContractId( idGenerator.generateCode("TCT"));
        tCompanyContract.setCreateId(userid);
        tCompanyContract.setCreateTime(LocalDateTime.now());
        tCompanyContract.setPostType(postType);
        boolean save = tCompanyContractService.save(tCompanyContract);
        if(save){
            return R.success();
        }
        return R.fail();
    }*/

    /*    @GetMapping("/jurisdiction")
        @ApiOperation("查看是否有发布入职付+满月付+到面付权限")
        @ApiImplicitParam(name = "postType", required = true, value = "岗位类型(1:入职付 2:满月付  3:到面付)")
        @Log(title = "入职付+满月付+到面付", businessType = BusinessType.SELECT)
        public R jurisdiction(@RequestParam("postType") Integer postType){
            if(postType <1 && postType >3){
                return R.fail("未知操作!");
            }
            String userid = this.companyMetadata.userid();
            QueryWrapper queryWrapper=new QueryWrapper();
            queryWrapper.eq("create_id",userid);
            queryWrapper.eq("post_type",postType);
            queryWrapper.eq("deleted",0);
            TCompanyContract one = tCompanyContractService.getOne(queryWrapper);
            Map map=new HashMap();
            String type="";
            if(postType==1){
                type="入职付岗位申请";
            }
            if(postType==2){
                type="满月付岗位申请";
            }
            if(postType==3){
                type="到面付岗位申请";
            }
            if(one==null){
                map.put("auditContract",3);
                map.put("msg",type+"暂未提交申请!");
               return R.success(map);
            }
            if(one.getAuditContract()==0){
                map.put("auditContract",one.getAuditContract());
                map.put("msg",type+"审核中!");
                return R.success(map);
            }else if(one.getAuditContract() ==1){
                map.put("auditContract",one.getAuditContract());
                map.put("msg",type+"审核成功!");
                return R.success(map);
            }else if(one.getAuditContract()==2){
                map.put("auditContract",one.getAuditContract());
                map.put("msg",type+"审核失败!");
                map.put("auditCause",one.getAuditCause());
                return R.success(map);
            }
            return R.fail("未知状态!");
        }*/
    @PostMapping("/jurisdiction")
    @ApiOperation("添加合同")
    @ApiImplicitParam(name = "signContract", required = true, value = "合同地址")
    @Log(title = "添加合同", businessType = BusinessType.INSERT)
    @AutoIdempotent
    public R addContract(@RequestBody @Validated ValidList<String> signContract) {
       if( signContract.size()>=9){
           return R.fail("文件数据量限制为8张!");
       }
        String userid = companyMetadata.userid();
        TCompanyContract one = tCompanyContractService.one(userid);

        if (one != null) {
            return R.fail("请勿重复上传!");
        }
        TCompanyContract tCompanyContract = new TCompanyContract();
        tCompanyContract.setContractId(idGenerator.generateCode("TCT"));
        tCompanyContract.setSignContract(signContract);
        tCompanyContract.setState(0);
        tCompanyContract.setCreateId(companyMetadata.userid());
        tCompanyContract.setName("人才推荐服务");
        tCompanyContract.setCreateTime(LocalDateTime.now());
        tCompanyContract.setAuditContract(0);
        boolean update = tCompanyContractService.save(tCompanyContract);
        if (update) {
            return R.success("添加成功!");
        }
        return R.fail("添加失败!请联系管理员");
    }

    @PostMapping("/batchUploadFile")
    @ApiOperation(value = "合同上传")
    @Log(title = "batchUploadFile", businessType = BusinessType.OTHER)
    public R batchUploadFile(MultipartFile[] files)  {
        if (ObjectUtils.isEmpty(files) || files[0].getSize() <= 0) {
            return R.fail("上传文件大小为空");
        }
        int length = files.length;
        if (length >= 9) {
            return R.fail("文件数据量限制为8张!");
        }
        for (MultipartFile file : files) {
            boolean flag = checkFileSize(file.getSize(), FILE_SIZE, FILE_UNIT);
            if (!flag) {
                throw new RuntimeException("上传文件大小超出40M限制");
            }
        }
/*        long startTime1 = System.currentTimeMillis();    //获取开始时间
        //获取结束时间
        List<String> s1= iCosFileService.uploadFile(files,"/contract");
        System.out.println(s1);
        long endTime1 = System.currentTimeMillis();

        log.info("程序运行时间：" + (endTime1 - startTime1) + "ms");*/

        long startTime = System.currentTimeMillis();    //获取开始时间
        //获取结束时间
        List<String> s = cosUtils.batchUploadFile(files, "/contract");
        long endTime = System.currentTimeMillis();
        log.info("程序运行时间：" + (endTime - startTime) + "ms");
        return R.success("上传成功", s);
    }

    /**
     * @param len  文件长度
     * @param size 限制大小
     * @param unit 限制单位（B,K,M,G）
     * @描述 判断文件大小
     */
    public static boolean checkFileSize(Long len, int size, String unit) {
        double fileSize = 0;
        if ("B".equalsIgnoreCase(unit)) {
            fileSize = (double) len;
        } else if ("K".equalsIgnoreCase(unit)) {
            fileSize = (double) len / 1024;
        } else if ("M".equalsIgnoreCase(unit)) {
            fileSize = (double) len / 1048576;
        } else if ("G".equalsIgnoreCase(unit)) {
            fileSize = (double) len / 1073741824;
        }
        return !(fileSize > size);
    }

    @GetMapping("/select")
    @ApiOperation(value = "合同查询")
    @Log(title = "合同查询", businessType = BusinessType.SELECT)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startDate", required = false, value = "关键词"),
            @ApiImplicitParam(name = "endDate", required = false, value = "关键词"),
            @ApiImplicitParam(name = "state", required = false, value = "0:审核中 1:审核成功 2:审核失败")
    })
    public R<TCompanyContractDTO> select(@RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate, @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,@RequestParam(value = "state", required = false)  Integer state) {
     /*   System.out.println(startDate);
        System.out.println(endDate);*/
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("create_id", companyMetadata.userid());
        queryWrapper.orderByDesc("create_time");
        if (startDate != null && endDate != null) {
            queryWrapper.between("create_time", startDate, endDate);
        } else if (startDate != null && endDate == null) {
            queryWrapper.ge("create_time", startDate);
        } else if (startDate == null && endDate != null) {
            queryWrapper.le("create_time", endDate);
        }
        if(state !=null){
            queryWrapper.eq("audit_contract",state);
        }
        List<TCompanyContract> lists = tCompanyContractService.list(queryWrapper);
        List<String> list1 = new ArrayList<>(lists.size());
        List<TCompanyContractDTO> tCompanyContractDTOS = new ArrayList<>(lists.size());
        for (TCompanyContract tCompanyContract : lists) {
            list1.add(tCompanyContract.getContractId());
            TCompanyContractDTO dto = new TCompanyContractDTO();
            BeanUtils.copyProperties(tCompanyContract, dto);
            tCompanyContractDTOS.add(dto);
        }
        if(list1.size()==0){
            return R.success();
        }
        QueryWrapper queryWrapper1 = new QueryWrapper();
        queryWrapper1.in("contract_id", list1);
        queryWrapper1.orderByDesc("audit_time");
        List<TCompanyContractAudit> list2 = tCompanyContractAuditService.list(queryWrapper1);
        for (TCompanyContractAudit tCompanyContractAudit : list2) {
            for (TCompanyContractDTO tCompanyContractDTO : tCompanyContractDTOS) {
                if (tCompanyContractDTO.getContractId().equals(tCompanyContractAudit.getContractId())) {
                    List<TCompanyContractAudit> list = tCompanyContractDTO.getList();
                    if(list==null){
                        list=new ArrayList<>(5);
                    }
                    list.add(tCompanyContractAudit);
                    tCompanyContractDTO.setList(list);
                }
            }
        }
        return R.success(tCompanyContractDTOS);
    }

    @GetMapping("/passTheAudit")
    //@ApiOperation(value = "查看合同是否有效")
    //@Log(title = "查看合同是否有效", businessType = BusinessType.SELECT)
    public Map<String, Object> passTheAudit() {
        Map<String, Object> map = new HashMap(2);
        TCompanyContract tCompanyContract = tCompanyContractService.selectOne(companyMetadata.userid());
        if (tCompanyContract != null) {
            Integer state = tCompanyContract.getState();
            //判断是否过期
            if (state == 1) {
                map.put("passTheAudit", false);
                map.put("msg", "合同已过期");
                return map;
            } else if (state == 0) {
                Integer auditContract = tCompanyContract.getAuditContract();
                if (auditContract == 0) {
                    map.put("passTheAudit", false);
                    map.put("msg", "合同审核中!");
                } else if (auditContract == 1) {
                    map.put("passTheAudit", true);
                    map.put("msg", "合同有效!");
                } else if (auditContract == 2) {
                    map.put("passTheAudit", false);
                    map.put("msg", "合同审核失败!");
                }
                return map;
            }
        }
        map.put("passTheAudit", false);
        map.put("msg", "请申请合同!");
        return map;
    }

    @GetMapping("/audit")
    @ApiOperation(value = "查看合同是否有效")
    @Log(title = "查看合同是否有效", businessType = BusinessType.SELECT)
    public R audit() {
        Map<String, Object> map = new HashMap(2);
        TCompanyContract tCompanyContract = tCompanyContractService.selectOne(companyMetadata.userid());
        if (tCompanyContract != null) {
            Integer state = tCompanyContract.getState();
            //判断是否过期
            if (state == 1) {
                map.put("passTheAudit", false);
                map.put("msg", "合同已过期!");
                return R.success(map);
            } else if (state == 0) {
                Integer auditContract = tCompanyContract.getAuditContract();
                if (auditContract == 0) {
                    map.put("passTheAudit", false);
                    map.put("msg", "合同审核中!");
                    map.put("isok", true);
                } else if (auditContract == 1) {
                    map.put("passTheAudit", true);
                    map.put("msg", "合同有效!");
                } else if (auditContract == 2) {
                    map.put("passTheAudit", false);
                    map.put("msg", "合同审核未通过!");
                    map.put("isok", false);
                }
                return R.success(map);
            }

        }
        map.put("passTheAudit", false);
        map.put("msg", "请申请合同!");
        map.put("msg", "请申请合同!");
        return R.success(map);
    }


    @PostMapping("/updateAddContract")
    @ApiOperation("修改合同")
    @Log(title = "修改合同", businessType = BusinessType.INSERT)
    @AutoIdempotent
    public R updateAddContract(@Valid @RequestBody AddContractVO addContractVO) {
        TCompanyContract one = tCompanyContractService.getById(addContractVO.getContractId());
        if (one != null) {
            if (!one.getCreateId().equals(companyMetadata.userid())) {
                return R.fail("是否不一致!");
            }
            Integer state = one.getState();
            if (state == 1) {
                return R.fail("该合同已过期,不能二次修改!");
            } else if (state == 0) {
                //未通过则就可以修改
                if (one.getAuditContract() == 2) {
                    //修改简历
                    one.setSignContract(addContractVO.getSignContract());
                    one.setUpdateTime(LocalDateTime.now());
                    one.setAuditContract(0);
                    boolean b = tCompanyContractService.updateById(one);
                    if (b) {
                        return R.success("修改成功!");
                    }
                    return R.fail("修改失败!");
                } else if (one.getAuditContract() == 1) {
                    return R.fail("该合同审核已通过,不能二次修改!");
                } else if (one.getAuditContract() == 0) {
                    return R.fail("该合同审核中,不能修改!");
                }
            }
        }
        return R.fail("未查到该合同!");
    }

    @GetMapping("selectId")
    @ApiOperation("根据id查询")
    @Log(title = "根据id查询", businessType = BusinessType.SELECT)
    @ApiImplicitParam(name = "id", required = true, value = "id")
    public R<TCompanyContract>  selectId(@RequestParam("id") Long id){
        TCompanyContract byId = tCompanyContractService.getById(id);
        return R.success(byId);
    }
}
