package com.wcwy.company.controller;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.wcwy.common.base.result.R;
import com.wcwy.common.base.utils.PhoneUtil;
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.company.asyn.InformAsync;
import com.wcwy.company.config.CosUtils;
import com.wcwy.company.entity.Report;
import com.wcwy.company.entity.TJobhunter;
import com.wcwy.company.service.ReportService;
import com.wcwy.company.service.TJobhunterService;
import com.wcwy.company.session.CompanyMetadata;
import com.wcwy.company.vo.ReportJobHunterVO;
import com.wcwy.company.vo.ReportPostVO;
import com.wcwy.system.annotation.Log;
import com.wcwy.system.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

/**
 * ClassName: ReportController
 * Description:
 * date: 2023/9/20 15:20
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Api(tags = "举报接口")
@RestController
@RequestMapping("/report")
@Slf4j
public class ReportController {
    @Autowired
    private TJobhunterService tJobhunterService;
    @Autowired
    private ReportService reportService;
    @Autowired
    private CompanyMetadata companyMetadata;
    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private InformAsync informAsync;

    @Autowired
    private CosUtils cosUtils;
    private final static Integer FILE_SIZE = 20;//文件上传限制大小
    private final static String FILE_UNIT = "M";//文件上传限制单位（B,K,M,G）
    @PostMapping("/addReportPost")
    @ApiOperation("添加岗位举报")
    public R addReportPost(@Valid @RequestBody ReportPostVO reportPostVO){
    /*    Integer type = reportVO.getType();
        if(type==1){
            if(StringUtils.isEmpty(reportVO.getJobHunterId())){
                return R.fail("求职者id不能为空！");
            }
            if(StringUtils.isEmpty(reportVO.getJobHunterName())){
                return R.fail("求职者名称不能为空！");
            }
        }*/
        Boolean aBoolean = redisUtils.setIfAbsent("report:" + companyMetadata.userid() + reportPostVO.getPostId(), reportPostVO.getPostId(), 168);
        if(! aBoolean){
            return R.fail("请七天后再次举报！");
        }
        //验证电话号码
        boolean mobileNumber = PhoneUtil.isMobileNumber(reportPostVO.getPhone());
        if(! mobileNumber){
            return R.fail("电话号码格式不正确!");
        }
        Report report=new Report();
        BeanUtils.copyProperties(reportPostVO,report);
        report.setReportUserId(companyMetadata.userid());
        report.setType(0);
        report.setState(1);
        report.setReportName(companyMetadata.userName());
        String substring = companyMetadata.userid().substring(0, 2);
        int identity=0;
        if(substring.equals("TC")){
            identity=0;
        }else if(substring.equals("TR")){
            identity=1;
        }else if(substring.equals("TJ")){
            identity=2;
        }
        report.setIdentity(identity);
        boolean save = reportService.save(report);
        if(save){
            informAsync.reportPost(report);
            return R.success("举报成功！");
        }
        return R.fail("举报失败！");
    }


    @PostMapping("/reportJobHunter")
    @ApiOperation("举报求职者")
    public R reportJobHunter(@Valid @RequestBody ReportJobHunterVO companyReportJobHunterVO){
        boolean mobileNumber = PhoneUtil.isMobileNumber(companyReportJobHunterVO.getPhone());
        if(! mobileNumber){
            return R.fail("电话号码格式不正确!");
        }
        Boolean aBoolean = redisUtils.setIfAbsent("report:" + companyMetadata.userid() + companyReportJobHunterVO.getJobHunterId(), companyReportJobHunterVO.getJobHunterId(), 168);
        if(! aBoolean){
            return R.fail("请七天后再次举报！");
        }
        TJobhunter byId = tJobhunterService.getById(companyReportJobHunterVO.getJobHunterId());
        if(byId==null){
            return R.fail("求职者不存在！");
        }
        Report report=new Report();
        BeanUtils.copyProperties(companyReportJobHunterVO,report);
        report.setType(1);
        report.setReportUserId(companyMetadata.userid());
        report.setReportName(companyMetadata.userName());
        report.setJobHunterName(byId.getUserName());
        String substring = companyMetadata.userid().substring(0, 2);
        int identity=0;
        if(substring.equals("TC")){
            identity=0;
        }else if(substring.equals("TR")){
            identity=1;
        }else if(substring.equals("TJ")){
            identity=2;
        }
        report.setIdentity(identity);
        boolean save = reportService.save(report);
        if(save){
            return R.success("举报成功！");
        }
        return R.fail("举报失败！");
    }





    @PostMapping("/batchUploadFile")
    @ApiOperation(value = "证据上传")
    @Log(title = "batchUploadFile", businessType = BusinessType.OTHER)
    public R batchUploadFile(MultipartFile[] files) {
        if (ObjectUtils.isEmpty(files) || files[0].getSize() <= 0) {
            return R.fail("上传文件大小为空");
        }
        int length = files.length;
        if (length > 6) {
            return R.fail("文件数据量限制为6张!");
        }
        for (MultipartFile file : files) {
            boolean flag = checkFileSize(file.getSize(), FILE_SIZE, FILE_UNIT);
            if (!flag) {
                throw new RuntimeException("上传文件大小超出20M限制");
            }
        }
        List<String> s = cosUtils.batchUploadFile(files, "/report");
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
}
