package com.wcwy.company.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wcwy.common.base.result.R;
import com.wcwy.company.entity.TPosition;
import com.wcwy.company.service.TPositionService;
import com.wcwy.company.vo.TPositionVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ClassName: TPositionController
 * Description:职位表
 * date: 2022/9/2 14:00
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@RestController
@RequestMapping("/tPosition")
@Api(tags = "职位表接口")
public class TPositionController {
    @Autowired
   private TPositionService tPositionService;

    @GetMapping("/selectList")
    @ApiOperation(value = "职位查询",notes = "职位查询")
    public R<TPosition> selectList(){
        List<TPosition> list = tPositionService.list();
        return R.success(list);
    }

    @GetMapping("/insert/{position}")
    @ApiOperation(value = "职位添加",notes = "职位添加  ")
    public R<TPosition> insert(@PathVariable("position") String position){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("position",position);
        TPosition one = tPositionService.getOne(queryWrapper);
        if(one !=null){
            return R.fail("该职位已经存在");
        }
        TPosition tPosition=new TPosition();
        tPosition.setPosition(position);
        tPosition.setDeleted(0);
        boolean save = tPositionService.save(tPosition);
        if(save){
            return R.fail("添加成功");
        }
        return R.fail("添加失败");
    }

    @ApiOperation(value = "删除职位",notes = "删除职位")
    @GetMapping("/delect/{positionId}")
  /*  @PreAuthorize("hasAnyAuthority('admin')")*/
    public R  delect (@PathVariable("positionId") int positionId){
        boolean b = tPositionService.removeById(positionId);
        if(b){
            return R.success("删除成功");
        }
        return R.fail("删除失败");
    }

    @ApiOperation(value = "修改职位",notes = "修改职位")
    @PostMapping("/update")
    public R update(@RequestBody TPositionVo tPositionVo){
        TPosition position=new TPosition();
        BeanUtils.copyProperties(tPositionVo,position);
        boolean b = tPositionService.updateById(position);
        if (b) {
            return R.success("修改成功");
        }
        return R.fail("修改失败");

    }



}
