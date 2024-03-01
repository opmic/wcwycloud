package com.wcwy.post.api;


import com.wcwy.common.base.result.R;
import com.wcwy.post.entity.TCompanyPost;
import com.wcwy.post.po.TCompanyPostPO;
import com.wcwy.post.pojo.TCompanyPostRecord;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;


/**
 * ClassName: TCompanyPost
 * Description:
 * date: 2022/9/14 14:55
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Component
@FeignClient(value = "wcwy-post")
public interface TCompanyPostApi {
    @GetMapping("/tCompanyPost/selectTCompanyPostPO")
    public TCompanyPostPO selectTCompanyPostPO(@RequestParam("postId") String postId);
    @GetMapping("/tCompanyPost/postCount")
    public int postCount(@RequestParam("companyId") String companyId);
  /*  @GetMapping("/tCompanyPost/onLinePost")
     Integer onLinePost(@RequestParam("companyId") String companyId);*/
    @GetMapping("/tCompanyPost/onLinePost")
    Integer onLinePost(@RequestParam("companyId") String companyId,@RequestParam("type") Integer type);
    @GetMapping("/tCompanyPost/list")
    public List<TCompanyPostRecord> list1();
    @GetMapping("/tCompanyPost/postRecord")
    public TCompanyPostRecord postRecord(@RequestParam("postId") String postId);
    @GetMapping("/tCompanyPost/onLine")
    public Boolean onLine(@RequestParam("postId") String postId);
    @GetMapping("/tCompanyPost/getCompanyName")
    public List<Map<String,Object>> getCompanyName(@RequestParam("list") List<String> list);
    @GetMapping("/tCompanyPost/selectCount")
     Map<String,Integer> selectCount(@RequestParam("userId") String userId);

    /*查看岗位总数*/
    @GetMapping("/recommendPost/amount")
    public R<Map<String,Integer>> amount(@RequestParam(value = "individualOrTeam") Integer individualOrTeam);
}
