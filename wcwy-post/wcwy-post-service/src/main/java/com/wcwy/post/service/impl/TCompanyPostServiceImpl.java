package com.wcwy.post.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wawy.company.api.PutInResumeApi;
import com.wawy.company.api.TCompanyApi;
import com.wcwy.common.base.query.PageQuery;
import com.wcwy.common.base.result.R;
import com.wcwy.common.redis.enums.Cache;
import com.wcwy.common.web.config.CacheClient;
import com.wcwy.company.entity.TCompany;
import com.wcwy.post.dto.*;
import com.wcwy.post.entity.TCompanyPost;
import com.wcwy.post.po.LatestPostPO;
import com.wcwy.post.po.PostLabel;
import com.wcwy.post.pojo.TCompanyPostRecord;
import com.wcwy.post.query.*;
import com.wcwy.post.service.TCompanyPostService;
import com.wcwy.post.mapper.TCompanyPostMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 * @description 针对表【t_company_post(企业招聘岗位表)】的数据库操作Service实现
 * @createDate 2022-09-14 14:54:54
 */
//@Primary
@Service
public class TCompanyPostServiceImpl extends ServiceImpl<TCompanyPostMapper, TCompanyPost>
        implements TCompanyPostService {
    @Autowired
    private TCompanyPostMapper tCompanyPostMapper;
    @Autowired
    private TCompanyApi tCompanyApi;
    @Autowired
    private CacheClient cacheClient;

    @Autowired
    private PutInResumeApi putInResumeApi;
    @Override
    public IPage<TCompanyPost> selectListPage(IPage page, TCompanyPostQuery tCompanyPostQuery) {
        return tCompanyPostMapper.seletListPage(page,tCompanyPostQuery);
    }

    @Override
    public IPage<PostShare> selectListPagePostShare(IPage page, TCompanyPostQuery tCompanyPostQuery) {
        return tCompanyPostMapper.selectListPagePostShare(page,tCompanyPostQuery);
    }

    @Override
    public IPage<CompanyPostDTO> getPage(TCompanyNewstQuery tCompanyNewstQuery) {
        return tCompanyPostMapper.getPage(tCompanyNewstQuery.createPage(),tCompanyNewstQuery);
    }

    @Override
    public IPage<CompanyPostDTO> listPost(TCompanyNewstQuery tCompanyNewstQuery) {
        return tCompanyPostMapper.listPost(tCompanyNewstQuery.createPage(),tCompanyNewstQuery);
    }

    @Override
    public String selectCompany(String post) {
        return tCompanyPostMapper.selectCompany(post);
    }

    @Override
    public IPage<CompanyPostDTO> recommendlistPost(TCompanyNewstQuery tCompanyNewstQuery) {
        Page page = (Page) tCompanyNewstQuery.createPage();
        return tCompanyPostMapper.recommendlistPost(page,tCompanyNewstQuery);
    }

    @Override
    public IPage<PostShare> selectSonPostShare(Page page, Integer identification, String postName, String companyId) {
        return tCompanyPostMapper.selectSonPostShare(page,identification,postName,companyId);
    }

    @Override
    public IPage<TCompanyPost> hotJob(Page page) {
        return tCompanyPostMapper.hotJob(page);
    }

    @Override
    public IPage<TCompanyHotPostDTO> tCompanyHotPostDTOS(Page page) {
        return tCompanyPostMapper.tCompanyHotPostDTOS(page);
    }

    @Override
    public List<String> hot() {
        return tCompanyPostMapper.hot();
    }

    @Override
    public IPage<LatestPostPO> latestPost(String companyId, PageQuery pageQuery) {
        return tCompanyPostMapper.latestPost(companyId,pageQuery.createPage());
    }

    @Override
    public List<String> jobType(String companyId) {
        return tCompanyPostMapper.jobType(companyId);
    }

    @Override
    public IPage<PostCollectDTO> openForPositions(PositionQuery positionQuery) {
        return tCompanyPostMapper.openForPositions(positionQuery.createPage(),positionQuery);
    }

    @Override
    public IPage<PostShare> selectPosition(IPage page, PostShareQuery postShareQuery) {
        return tCompanyPostMapper.selectPosition(page,postShareQuery);
    }

    @Override
    public IPage<CompanyPostDTO> jobSearch(JobSearchQuery jobSearchQuery) {
        return tCompanyPostMapper.jobSearch(jobSearchQuery.createPage(),jobSearchQuery);
    }

    @Override
    public List<TCompanyPostRecord> list1() {
        return tCompanyPostMapper.list1();
    }

    @Override
    public List<String> JobTitle(String companyId) {
        return tCompanyPostMapper.JobTitle(companyId);
    }

    @Override
    public List<PostLabel> postLabel(String userid,String keyword) {
        return tCompanyPostMapper.postLabel(userid,keyword);
    }

    @Override
    public List<String> firm(String keyword) {
        return tCompanyPostMapper.firm(keyword);
    }

    @Override
    public List<String> post(String keyword, int i) {
        return tCompanyPostMapper.post(keyword,i);
    }

    @Override
    public TCompanyPostRecord postRecord(String postId) {
        return tCompanyPostMapper.postRecord(postId);
    }

    @Override
    public Boolean postAmend(String postId) {
        TCompanyPost byId = this.getById(postId);
        if(byId ==null){
            return null;
        }

        //简历付校园 简历付职场
        if(byId.getPostType() ==0 || byId.getPostType()==4 || byId.getPostType()==5){
            return true;
        }
        if(byId.getPostType() !=0){
            return putInResumeApi.postAmend(postId,byId.getPostType());
        }
        return true;
    }

    @Override
    public IPage<OrderReceivingDTO> recommendSelect(OrderReceivingQuery orderReceivingQuery, String userid) {
        return tCompanyPostMapper.recommendSelect(orderReceivingQuery.createPage(),orderReceivingQuery,userid);
    }

    @Override
    public IPage<OrderReceivingDTO> recommendSelectB(OrderReceivingQuery orderReceivingQuery, String userid) {
        return tCompanyPostMapper.recommendSelectB(orderReceivingQuery.createPage(),orderReceivingQuery,userid);
    }

    @Override
    public List<Map<String, Object>> companyName(List<String> list) {
        return tCompanyPostMapper.companyName(list);
    }

    @Override
    public Page<PostShare> selectRecommend(Page page, String keyword, String userid, Integer state,Integer individualOrTeam) {
        return tCompanyPostMapper.selectRecommend(page,keyword,userid,state,individualOrTeam);
    }

    @Override
    public IPage<SchoolyardDTO> selectCompanyId(Page page, String keyword) {
        return tCompanyPostMapper.selectCompanyId(page,keyword);
    }

    @Override
    public List<TCompanyPost> selectPostName(List<String> list) {
        return tCompanyPostMapper.selectPostName(list);
    }

    @Override
    public List<String> keywordSchoolyard(String keyword) {
        return tCompanyPostMapper.keywordSchoolyard(keyword);
    }

    @Override
    public List<Integer> selectPostType(String userid,Integer state) {
        return tCompanyPostMapper.tCompanyPostMapper(userid,state);
    }

    @Override
    public List<String> workPlace(String id) {
        return tCompanyPostMapper.workPlace(id);
    }

    @Override
    public IPage<OrderReceivingDTO> recommendSelectC(OrderReceivingQuery orderReceivingQuery, String userid) {
        return tCompanyPostMapper.recommendSelectC(orderReceivingQuery.createPage(),orderReceivingQuery,userid);
    }

    @Override
    @Cacheable(value="com:CompanyPostDTO:cache:one", key="#postId")
    public String basic(String postId) {
        TCompanyPost byId = this.getById(postId);
        if(byId !=null){
            CompanyPostDTO companyPostDTO=new CompanyPostDTO();
            BeanUtils.copyProperties(byId,companyPostDTO);
            TCompany tCompany = tCompanyApi.selectId(byId.getCompanyId());
            companyPostDTO.setContactName(tCompany.getContactName());
            companyPostDTO.setSex(tCompany.getSex());
            companyPostDTO.setJobTitle(tCompany.getJobTitle());
            companyPostDTO.setLoginTime(tCompany.getLoginTime());
            return JSON.toJSONString(companyPostDTO);
        }
        return null;
    }

    @Override
    public TCompanyPost byId(String postId) {
        TCompanyPost byId = cacheClient.queryWithLogicalExpireCount(Cache.CACHE_POST.getKey(), postId, TCompanyPost.class, this::getById, 2L, TimeUnit.MINUTES);
     /*   TCompanyPost byId = getById(postId);*/

        return byId;
    }


}




