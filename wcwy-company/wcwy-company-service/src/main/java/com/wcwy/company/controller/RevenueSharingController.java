package com.wcwy.company.controller;

import com.wcwy.common.base.result.R;
import com.wcwy.company.dto.RevenueSharingDTO;
import com.wcwy.company.entity.RevenueSharing;
import com.wcwy.company.service.RevenueSharingService;
import com.wcwy.company.service.SourceOfReturnsService;
import com.wcwy.company.session.CompanyMetadata;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * ClassName: RevenueSharingController
 * Description:
 * date: 2023/7/19 11:51
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Api(tags = "分享收益提现表")
@RestController
@RequestMapping("/revenueSharing")
@Slf4j
public class RevenueSharingController {

    @Autowired
    private RevenueSharingService revenueSharingService;

    @Autowired
    private SourceOfReturnsService sourceOfReturnsService;
    @Autowired
    private CompanyMetadata companyMetadata;
    @GetMapping("/selectId")
    @ApiOperation("查询分享收益")
    public R<RevenueSharing> select(){
        RevenueSharing byId = revenueSharingService.getById(companyMetadata.userid());
        return R.success(byId);
    }


    @GetMapping("/earnings")
    @ApiOperation("我的收益详情")
    public R<RevenueSharingDTO> earnings(){
        RevenueSharingDTO revenueSharingDTO=new RevenueSharingDTO();

      List<Map<Integer,Object>> list= sourceOfReturnsService.earnings(companyMetadata.userid());
        for (Map<Integer, Object> integerIntegerMap : list) {
          Integer type= (Integer) integerIntegerMap.get("type");
            BigDecimal  earnings=new BigDecimal(integerIntegerMap.get("earnings").toString());
            if(type==0){
                revenueSharingDTO.setShare(earnings);
            }else if(type==1){
                revenueSharingDTO.setRecommend(earnings);
            }
        }
        RevenueSharing byId = revenueSharingService.getById(companyMetadata.userid());
        if(! StringUtils.isEmpty(byId)){
            revenueSharingDTO.setTotalRevenue(byId.getTotalRevenue());
        }
        return R.success(revenueSharingDTO);
    }
}
