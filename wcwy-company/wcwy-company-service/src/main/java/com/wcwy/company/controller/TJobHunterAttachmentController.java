package com.wcwy.company.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wcwy.common.base.result.R;
import com.wcwy.common.redis.util.IDGenerator;
import com.wcwy.company.entity.TJobHunterAttachment;
import com.wcwy.company.service.TJobHunterAttachmentService;
import com.wcwy.company.service.TJobhunterService;
import com.wcwy.company.session.CompanyMetadata;
import com.wcwy.system.annotation.Log;
import com.wcwy.system.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

/**
 * ClassName: TJobHunterAttachmentController
 * Description:
 * date: 2023/3/13 14:09
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Api(tags = "求职者附件接口")
@RestController
@RequestMapping("/jobHunterAttachment")
public class TJobHunterAttachmentController {

    @Resource
    private TJobHunterAttachmentService tJobHunterAttachmentService;
    @Autowired
    private IDGenerator idGenerator;
    @Autowired
    private CompanyMetadata companyMetadata;

    @GetMapping("/select")
    @ApiOperation(value = "查询求职者简历")
    @Log(title = "查询求职者简历", businessType = BusinessType.SELECT)
    public R<TJobHunterAttachment> select(){
        String userid = companyMetadata.userid();
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("t_job_hunter_id",userid);
        List<TJobHunterAttachment> list = tJobHunterAttachmentService.list(queryWrapper);
        return R.success(list);
    }

    @GetMapping("/save")
    @ApiOperation(value = "添加求职者附件")
    @ApiImplicitParam(name = "path", required = true, value = "地址")
    @Log(title = "添加求职者附件", businessType = BusinessType.UPDATE)
    public R save(@RequestParam("path") String path){
        String userid = companyMetadata.userid();
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("t_job_hunter_id",userid);
        int count = tJobHunterAttachmentService.count(queryWrapper);
        if(count>=3){
            return R.fail("附件数据不能大于3!");
        }
        TJobHunterAttachment tJobHunterAttachment=new TJobHunterAttachment();
        tJobHunterAttachment.setPath(path);
        tJobHunterAttachment.setTJobHunterId(userid);
        tJobHunterAttachment.setAttachmentId(idGenerator.generateCode("AC"));
        tJobHunterAttachment.setCreateDate(LocalDate.now());
        boolean save = tJobHunterAttachmentService.save(tJobHunterAttachment);
        if(save){
            return R.success("添加成功!");
        }
        return R.fail("添加失败!");
    }
    @GetMapping("/delete")
    @ApiOperation(value = "删除求职者附件")
    @ApiImplicitParam(name = "attachmentId", required = true, value = "附件id")
    @Log(title = "删除求职者附件", businessType = BusinessType.DELETE)
    public R delete(@RequestParam("attachmentId") String attachmentId){
        String userid = companyMetadata.userid();
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("attachment_id",attachmentId);
        queryWrapper.eq("t_job_hunter_id",userid);
        boolean remove = tJobHunterAttachmentService.remove(queryWrapper);
       if(remove){
           return R.success("删除成功!");
       }
        return R.fail("删除失败!");
    }
}
