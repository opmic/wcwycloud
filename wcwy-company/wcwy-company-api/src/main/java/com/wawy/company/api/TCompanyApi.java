package com.wawy.company.api;

import com.wawy.company.config.FeignConfig;
import com.wawy.company.pojo.DeductGold;
import com.wawy.company.pojo.ProportionDTO;
import com.wcwy.common.base.result.R;
import com.wcwy.company.dto.CompanyCollerctPutInResume;
import com.wcwy.company.entity.TCompany;
import com.wcwy.company.query.SchoolQuery;
import com.wcwy.company.query.TCompanyQuery;
import com.wcwy.company.vo.ProportionVO;
import com.wcwy.post.entity.OrderInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * ClassName: TCompanyApi
 * Description:
 * date: 2022/10/13 11:12
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Component
@FeignClient(value = "wcwy-company",configuration = FeignConfig.class)
public interface TCompanyApi {
    @PostMapping("/company/selectList")
    R<TCompany> selectList(@RequestBody TCompanyQuery query);

    @GetMapping("/company/cccs")
    public String cc();

    @PostMapping(value = "/company/asc", consumes = "application/json")
    public String asc(@RequestBody SchoolQuery schoolQuery);

    @PostMapping(value = "/asccc")
    public String asccc();

    @GetMapping("/company/tCompanyById")
    public CompanyCollerctPutInResume tCompanyById(@RequestParam("id") String id, @RequestParam("industry") String industryID, @RequestParam("postID") String postID);

    @PostMapping("/company/deductExpenses")
    public ProportionDTO deductExpenses(@RequestBody ProportionVO proportionVO);

    @PostMapping("/company/paymentDivideInto")
    DeductGold paymentDivideInto(@RequestBody OrderInfo orderInfo);

    @GetMapping("/company/getPhone")
     String selectPhone(@RequestParam("companyId") String companyId) ;

    @GetMapping("/company/selectId")
    TCompany selectId(@RequestParam("companyId") String companyId);

    /**
     * 简历下载扣除无忧币
     * @return
     */

    @GetMapping("/company/deductCurrency")
    Map<String, Object> deductCurrency(@RequestParam("money") BigDecimal money,@RequestParam("userId") String userId ,@RequestParam("jobHunter") String jobHunter,@RequestParam("orderId") String orderId);

    /**
     * 简历下载扣除金币
     * @return
     */
    @GetMapping("/company/deductGold")
     Map<String,Object> deductGold(@RequestParam("money") BigDecimal money,@RequestParam("userId") String userId,@RequestParam("jobHunter")  String jobHunter,@RequestParam("orderId") String orderId );
    @GetMapping("/company/getSharePersonRecommend")
    public Map<String,String> getSharePersonRecommend(@RequestParam("companyId") String companyId);

    @GetMapping("/company/deductCurrencyExplain")
    public Map<String,Object> deductCurrencyExplain(@RequestParam("money") BigDecimal money,@RequestParam("userId") String userId,@RequestParam("explain")  String explain,@RequestParam("orderId") String orderId);
    @GetMapping("/company/companyLists")
    List<TCompany> companyLists(@RequestParam("id") List<String> id);
    @PostMapping("/company/selectUser")
    Map<String,Map> selectUser(@RequestBody List<String> id);
    }
