package com.wcwy.post.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wawy.company.api.TRecommendApi;
import com.wcwy.company.entity.TRecommend;
import com.wcwy.post.entity.RecommendBasics;
import com.wcwy.post.service.RecommendBasicsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ClassName: RecommendBasicsController
 * Description:
 * date: 2023/6/19 8:30
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@RestController
@Api(tags = "推荐基本信息")
@RequestMapping("/recommendBasics")
public class RecommendBasicsController {
    @Autowired
    private RecommendBasicsService recommendBasicsService;
    @Autowired
    private TRecommendApi tRecommendApi;
    @ApiOperation("基本信息同步")
    @GetMapping("/synchronization")
    public void synchronization() {
        List<TRecommend> selects = tRecommendApi.selects();
        for (TRecommend select : selects) {
            RecommendBasics recommendBasics=new RecommendBasics();
            recommendBasics.setRecommendId(select.getId());
            recommendBasics.setPhone(select.getPhone());
            recommendBasics.setHeadPath(select.getHeadPath());
            recommendBasics.setSex(select.getSex());
            recommendBasics.setUsername(select.getUsername());
            recommendBasicsService.saveOrUpdate(recommendBasics);
        }

    }
   /* @GetMapping("/update")
    public void update(@RequestParam("/id") String id,@RequestParam("/id") String id){

    }*/
   // @ApiOperation("添加人才推荐管基本信息")
    @PostMapping("/save")
    public void  save(@RequestBody RecommendBasics recommendBasics){
        boolean b = recommendBasicsService.saveOrUpdate(recommendBasics);
    }

    /***
     * 猎企认证申请
     * @param recommend
     */
    @GetMapping("/administrator")
    public void  administrator(@RequestParam("recommend") String recommend,@RequestParam("firmName") String firmName){
        UpdateWrapper updateWrapper=new UpdateWrapper();
        updateWrapper.eq("recommend_id",recommend);
        updateWrapper.set("administrator",1);
        updateWrapper.set("firm_name",firmName);
        recommendBasicsService.update(updateWrapper);

    }

}
