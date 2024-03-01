package com.wcwy.post.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wcwy.common.base.result.R;
import com.wcwy.post.query.LeadingEndMistakeQuery;
import com.wcwy.post.vo.LeadingEndMistakeVO;
import com.wcwy.system.entity.LeadingEndMistake;
import com.wcwy.system.service.LeadingEndMistakeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * ClassName: MistakeController
 * Description:
 * date: 2023/5/20 11:26
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Api(tags = "前端信息报错接口")
@RequestMapping("/mistake")
@RestController
public class MistakeController {
    @Autowired
    private LeadingEndMistakeService leadingEndMistakeService;

    @PostMapping("/add")
    @ApiOperation("添加错误")
    public R  add(@RequestBody LeadingEndMistakeVO leadingEndMistakeVO){
        LeadingEndMistake leadingEndMistake=new LeadingEndMistake();
        BeanUtils.copyProperties(leadingEndMistakeVO,leadingEndMistake);
        leadingEndMistake.setCreateTime(LocalDateTime.now());
        boolean save = leadingEndMistakeService.save(leadingEndMistake);
        return R.success();
    }
    @PostMapping("/select")
    @ApiOperation("查询添加错误")
    public R<LeadingEndMistake> select(@RequestBody LeadingEndMistakeQuery leadingEndMistakeQuery){
        QueryWrapper queryWrapper=new QueryWrapper();

        if(!StringUtils.isEmpty(leadingEndMistakeQuery.getRoute())){
            queryWrapper.eq("route",leadingEndMistakeQuery.getRoute());
        }
        if(leadingEndMistakeQuery.getCreateTime() !=null ){
            queryWrapper.eq("create_time",leadingEndMistakeQuery.getCreateTime() );
        }
        IPage<LeadingEndMistake> page = leadingEndMistakeService.page(leadingEndMistakeQuery.createPage(), queryWrapper);
        return R.success(page);
    }
}
