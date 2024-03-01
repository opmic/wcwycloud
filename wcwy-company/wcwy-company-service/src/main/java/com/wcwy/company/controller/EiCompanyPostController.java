package com.wcwy.company.controller;

import com.alibaba.fastjson.JSON;
import com.wcwy.common.base.result.R;
import com.wcwy.company.entity.EiCompanyPost;
import com.wcwy.company.service.EiCompanyPostService;
import com.wcwy.post.api.TCompanyPostApi;
import com.wcwy.post.entity.TCompanyPost;
import com.wcwy.post.po.ProvincesCitiesPO;
import com.wcwy.post.pojo.TCompanyPostRecord;
import com.wcwy.system.annotation.Log;
import com.wcwy.system.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * ClassName: EiCompanyPostController
 * Description:
 * date: 2023/3/30 17:16
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@RestController
@RequestMapping("/eiCompanyPost")
@Api(tags = "岗位基础信息接口")
public class EiCompanyPostController {
    @Autowired
    private TCompanyPostApi tCompanyPostApi;
    @Autowired
    private  EiCompanyPostService eiCompanyPostService;
    @GetMapping("/test")
    @ApiOperation(value = "生成岗位基础信息")
    @Log(title = "生成岗位基础信息", businessType = BusinessType.SELECT)
    public R test(){
        List<TCompanyPostRecord> tCompanyPostRecords = tCompanyPostApi.list1();
        for (TCompanyPostRecord tCompanyPost : tCompanyPostRecords) {
            EiCompanyPost eiCompanyPost=new EiCompanyPost();
            eiCompanyPost.setPostId(tCompanyPost.getPostId());
            eiCompanyPost.setCompanyId(tCompanyPost.getCompanyId());
            eiCompanyPost.setBeginSalary(tCompanyPost.getBeginSalary());
            eiCompanyPost.setEndSalary(tCompanyPost.getEndSalary());
            eiCompanyPost.setPostLabel(tCompanyPost.getPostLabel());
            ProvincesCitiesPO workCity = tCompanyPost.getWorkCity();
            com.wcwy.company.po.ProvincesCitiesPO provincesCitiesPO=new com.wcwy.company.po.ProvincesCitiesPO();
            BeanUtils.copyProperties(workCity,provincesCitiesPO);
            eiCompanyPost.setWorkCity(provincesCitiesPO);
            eiCompanyPost.setPostType(tCompanyPost.getPostType());
            eiCompanyPost.setHiredBounty(tCompanyPost.getHiredBounty());
            eiCompanyPost.setMoneyReward(JSON.toJSONString(tCompanyPost.getHeadhunterPositionRecord()));
            eiCompanyPost.setCompanyName(tCompanyPost.getCompanyName());
            eiCompanyPostService.saveOrUpdate(eiCompanyPost);
        }
        return R.success();
    }

}
