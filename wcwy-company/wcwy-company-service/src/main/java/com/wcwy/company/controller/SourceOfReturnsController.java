package com.wcwy.company.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wcwy.common.base.result.R;
import com.wcwy.common.base.utils.DateUtils;
import com.wcwy.company.dto.IdentityEarningsDTO;
import com.wcwy.company.dto.IdentitySharingDTO;
import com.wcwy.company.dto.SourceOfReturnsCompanyDTO;
import com.wcwy.company.dto.SourceOfReturnsJobHunterDTO;
import com.wcwy.company.entity.SourceOfReturns;
import com.wcwy.company.query.ShareRevenueDetailsQuery;
import com.wcwy.company.query.SourceOfReturnsCompanyQuery;
import com.wcwy.company.query.SourceOfReturnsQuery;
import com.wcwy.company.service.SourceOfReturnsService;
import com.wcwy.company.session.CompanyMetadata;
import com.wcwy.system.annotation.Log;
import com.wcwy.system.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * ClassName: SourceOfReturnsController
 * Description:
 * date: 2023/7/19 11:50
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Api(tags = "分享收益来源详情")
@RestController
@RequestMapping("/sourceOfReturns")
@Slf4j
public class SourceOfReturnsController {

    @Autowired
    private SourceOfReturnsService sourceOfReturnsService;
    @Autowired
    private CompanyMetadata companyMetadata;

    @PostMapping("/selectJobHunter")
    @ApiOperation("求职者收益详情")
    public R<SourceOfReturnsJobHunterDTO> selectJobHunter(@RequestBody SourceOfReturnsQuery sourceOfReturnsQuery) {
        if (StringUtils.isEmpty(sourceOfReturnsQuery.getJobHunter())) {
            return R.fail("求职者信息不完善！");
        }
        sourceOfReturnsQuery.setUserId(companyMetadata.userid());
        IPage<SourceOfReturnsJobHunterDTO> source = sourceOfReturnsService.selectJobHunter(sourceOfReturnsQuery);
        return R.success(source);
    }

    @PostMapping("/selectCompany")
    @ApiOperation("企业+推荐官收益详情")
    public R<SourceOfReturnsCompanyDTO> selectCompany(@RequestBody SourceOfReturnsCompanyQuery sourceOfReturnsQuery) {

        sourceOfReturnsQuery.setUserId(companyMetadata.userid());

        if(StringUtils.isEmpty(sourceOfReturnsQuery.getYear())){
            sourceOfReturnsQuery.setYear(DateUtils.getCurrentDateStr());
        }else {
            sourceOfReturnsQuery.setYear(sourceOfReturnsQuery.getYear()+"-01-01");
        }
        IPage<SourceOfReturnsCompanyDTO> source = sourceOfReturnsService.selectCompany(sourceOfReturnsQuery);
        return R.success(source);
    }

 /*   @PostMapping("/selectRecommend")
    @ApiOperation("推荐官益详情")
    public R<SourceOfReturnsCompanyDTO> selectRecommend(@RequestBody SourceOfReturnsCompanyQuery sourceOfReturnsQuery) {

        sourceOfReturnsQuery.setUserId(companyMetadata.userid());
        IPage<SourceOfReturnsCompanyDTO> source = sourceOfReturnsService.selectCompany(sourceOfReturnsQuery);
        return R.success(source);
    }*/


    @GetMapping("/selectCompany")
    @ApiOperation("推荐收益-企业名称查询")
    @ApiImplicitParam(name = "keyword", required = false, value = "关键词")
    @Log(title = "推荐收益-企业名称查询", businessType = BusinessType.SELECT)
    public R<String> selectCompany(@RequestParam(value = "keyword",required = false) String keyword){
       List<String> strings= sourceOfReturnsService.selectCompanyName(companyMetadata.userid(),keyword);
       return R.success(strings);
    }


    @GetMapping("/identityEarnings")
    @ApiOperation("分身份收益金额")
    @Log(title = "分身份收益金额", businessType = BusinessType.SELECT)
    public R<IdentitySharingDTO> identityEarnings(){
        IdentitySharingDTO identitySharingDTO=new IdentitySharingDTO();
       List<Map<String,Object>> list= sourceOfReturnsService.identityEarnings(companyMetadata.userid(),null,null);
        list.forEach(i->{
           if((Integer) i.get("type") ==0){
               identitySharingDTO.setRevenueSharing( (BigDecimal) i.get("earnings"));
           }else if((Integer) i.get("type") ==1){
               identitySharingDTO.setReferralRevenue( (BigDecimal) i.get("earnings"));
           }
        });
       return R.success(identitySharingDTO);
    }

    @PostMapping("/shareRevenueDetails")
    @ApiOperation("分享收益详情")
    @Log(title = "分享收益详情", businessType = BusinessType.SELECT)
    public R<IdentitySharingDTO> shareRevenueDetails(@Valid @RequestBody ShareRevenueDetailsQuery shareRevenueDetailsQuery){
        shareRevenueDetailsQuery.setUserId(companyMetadata.userid());
        if (!StringUtils.isEmpty(shareRevenueDetailsQuery.getDate())) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            String formattedDate = sdf.format(shareRevenueDetailsQuery.getDate());
            shareRevenueDetailsQuery.setYear(formattedDate.substring(0, 4));
            shareRevenueDetailsQuery.setMonth(formattedDate.substring(5));
        }
        IPage<IdentityEarningsDTO> identityEarningsDTOIPage=null;
        //简历下载
        if(shareRevenueDetailsQuery.getIdentity() ==2){
          identityEarningsDTOIPage=  sourceOfReturnsService.identityEarningsJobHunter(shareRevenueDetailsQuery);
        }else if (shareRevenueDetailsQuery.getIdentity() ==1){
            //引入推荐官
            identityEarningsDTOIPage=  sourceOfReturnsService.identityEarningsRecommend(shareRevenueDetailsQuery);
        }else if (shareRevenueDetailsQuery.getIdentity() ==0){
            //引入企业
            identityEarningsDTOIPage=  sourceOfReturnsService.identityEarningsCompany(shareRevenueDetailsQuery);
        }
        IdentitySharingDTO identitySharingDTO=new IdentitySharingDTO();
        List<Map<String,Object>> list= sourceOfReturnsService.identityShareEarnings(companyMetadata.userid(),shareRevenueDetailsQuery.getYear(),shareRevenueDetailsQuery.getMonth());
        list.forEach(i->{
            if((Integer) i.get("identity") ==0){
                identitySharingDTO.setCompanySharing( (BigDecimal) i.get("earnings"));
            }else if((Integer) i.get("identity") ==1){
                identitySharingDTO.setRecommendSharing( (BigDecimal) i.get("earnings"));
            }else if((Integer) i.get("identity") ==2){
                identitySharingDTO.setJobHunterSharing( (BigDecimal) i.get("earnings"));
            }
        });
        identitySharingDTO.setIPage(identityEarningsDTOIPage);
        return R.success(identitySharingDTO);

    }
}
