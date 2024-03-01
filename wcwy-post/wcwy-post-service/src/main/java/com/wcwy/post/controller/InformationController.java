package com.wcwy.post.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wcwy.common.base.result.R;
import com.wcwy.common.redis.util.IDGenerator;
import com.wcwy.common.web.annotation.AutoIdempotent;
import com.wcwy.post.entity.Information;
import com.wcwy.post.service.InformationService;
import com.wcwy.post.vo.InformationVO;
import com.wcwy.system.annotation.Log;
import com.wcwy.system.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.xml.namespace.QName;
import java.time.LocalDateTime;

/**
 * ClassName: InformationController
 * Description:
 * date: 2023/4/19 11:06
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Api(tags = "资讯接口")
@RequestMapping("/information")
@RestController
public class InformationController {
    @Autowired
    private InformationService informationService;
    @Autowired
    private IDGenerator idGenerator;

    /*public R<Information> select(){

    } */
    @PostMapping("/add")
    @ApiOperation("资讯添加/更新")
    @Log(title = "资讯添加/更新", businessType = BusinessType.INSERT)
    @AutoIdempotent
    public R add(@Valid @RequestBody InformationVO informationVO) {
        Information information = new Information();
        BeanUtils.copyProperties(informationVO, information);
        if(StringUtils.isEmpty(informationVO.getInformationId())){
            information.setInformationId(idGenerator.generateCode("IFM"));
        }
        information.setCreateTime(LocalDateTime.now());
        boolean save = informationService.saveOrUpdate(information);
        if (save) {
            return R.success("添加成功!");
        }
        return R.fail("添加失败!");
    }

    @GetMapping("/delete")
    @ApiOperation("删除")
    @Log(title = "资讯删除", businessType = BusinessType.DELETE)
    @ApiImplicitParam(name = "informationId", required = true,value = "资讯id")
    public R delete(@RequestParam("informationId") String informationId) {
        boolean b = informationService.removeById(informationId);
        if(b){
            return R.success();
        }
        return R.fail("删除失败!");
    }
    @GetMapping("/select")
    @ApiOperation("查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "theme", required = false, value = "话题(0:全部 1数据增长动态 2:职场资讯 3:公司动态)"),
            @ApiImplicitParam(name = "pageSize", required = false),
            @ApiImplicitParam(name = "pageNo", required = false)
    })
    @Log(title = "查询", businessType = BusinessType.SELECT)
    public R<Information> select(@RequestParam(value = "theme",required = false) Integer theme,@RequestParam(value = "pageSize",required = false) Integer pageSize,@RequestParam(value = "pageNo",required = false) Integer pageNo) {
            if(pageNo==null ){
                pageNo=1;
            }
            if(pageSize ==null){
                pageSize=10;
            }
        Page iPage=new Page(pageNo,pageSize);
        QueryWrapper queryWrapper=new QueryWrapper();
        if(theme !=null && theme !=0){
            queryWrapper.eq("theme",theme);
        }
        queryWrapper.eq("deleted",0);
        queryWrapper.orderByDesc("create_time");
        IPage<Information> page = informationService.page(iPage, queryWrapper);
        return R.success(page);

    }


}
