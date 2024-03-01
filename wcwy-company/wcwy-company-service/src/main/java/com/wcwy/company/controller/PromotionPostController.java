package com.wcwy.company.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wcwy.common.base.result.R;
import com.wcwy.company.dto.PromotionPostDTO;
import com.wcwy.company.entity.PromotionPost;
import com.wcwy.company.query.PromotionPostQuery;
import com.wcwy.company.service.PromotionPostDetailService;
import com.wcwy.company.service.PromotionPostService;
import com.wcwy.company.session.CompanyMetadata;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ClassName: PromotionPostController
 * Description:
 * date: 2023/8/29 17:07
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Api(tags = "职位推广接口")
@RequestMapping("/promotionPost")
@RestController
public class PromotionPostController {

    @Autowired
    private PromotionPostService promotionPostService;
    @Autowired
    private CompanyMetadata companyMetadata;
    @Autowired
    private PromotionPostDetailService promotionPostDetailService;

    @PostMapping("/select")
    @ApiOperation("查询已推广的职位")
    public R<PromotionPostDTO> select(@RequestBody PromotionPostQuery promotionPostQuery) {
        promotionPostQuery.setRecommend(companyMetadata.userid());
        IPage<PromotionPostDTO> page = promotionPostService.select(promotionPostQuery);
        List<PromotionPostDTO> records = page.getRecords();
        List<String> query = new ArrayList<>(records.size());
        for (PromotionPostDTO record : records) {
            query.add(record.getPromotionPostId());
        }
        if (query.size() > 0) {
            List<PromotionPostDTO> dtos = promotionPostDetailService.listCount(query);
            records.stream().map(record -> {
                return dtos.stream().filter(dto -> {
                    return record.getPromotionPostId().equals(dto.getPromotionPostId());
                }).map(dto -> {
                    record.setCount(dto.getCount());
                    return null;                            //返回的结果
                }).collect(Collectors.toList());
            }).flatMap(List::stream).collect(Collectors.toList());//设置返回结果类型
        }
        return R.success(page);
    }

    @PostMapping("/getDetail")
    @ApiOperation("查询运营次数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "promotionPostId", required = true, value = "推广职位id "),
            @ApiImplicitParam(name = "pageSize", required = true),
            @ApiImplicitParam(name = "pageNo", required = true)
    })
    public R<PromotionPostDTO> getDetail(@RequestParam("promotionPostId") String promotionPostId, @RequestParam("pageSize") Integer pageSize, @RequestParam("pageNo") Integer pageNo) {
        IPage<PromotionPostDTO> promotionPostDTOIPage = promotionPostDetailService.getDetail(new Page(pageNo, pageSize), promotionPostId);
        return R.success(promotionPostDTOIPage);
    }

}
