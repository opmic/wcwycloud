package com.wcwy.post.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wcwy.common.base.query.PageQuery;
import com.wcwy.post.dto.*;
import com.wcwy.post.entity.TCompanyPost;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wcwy.post.po.LatestPostPO;
import com.wcwy.post.po.PostLabel;
import com.wcwy.post.pojo.TCompanyPostRecord;
import com.wcwy.post.query.*;

import java.util.List;
import java.util.Map;

/**
* @author Administrator
* @description 针对表【t_company_post(企业招聘岗位表)】的数据库操作Service
* @createDate 2022-09-14 14:54:54
*/
public interface TCompanyPostService extends IService<TCompanyPost> {

    IPage<TCompanyPost> selectListPage(IPage page, TCompanyPostQuery tCompanyPostQuery);

    IPage<PostShare> selectListPagePostShare(IPage page, TCompanyPostQuery tCompanyPostQuery);

    IPage<CompanyPostDTO> getPage(TCompanyNewstQuery tCompanyNewstQuery);

    IPage<CompanyPostDTO> listPost(TCompanyNewstQuery tCompanyNewstQuery);

    String selectCompany(String post);

    IPage<CompanyPostDTO> recommendlistPost(TCompanyNewstQuery tCompanyNewstQuery);


    IPage<PostShare> selectSonPostShare(Page page, Integer identification, String postName, String companyId);

    /**
     * @Description:查询热招职位
     * @param page  页数
     * @return CompanyPost 岗位
     * @Author tangzhuo
     * @CreateTime 2023/1/11 17:03
     */
    public IPage<TCompanyPost> hotJob(Page page);
    
    /**
     * @Description: 查询热门企业
     * @param page:页数
     * @return null
     * @Author tangzhuo
     * @CreateTime 2023/1/12 14:23
     */

    public IPage<TCompanyHotPostDTO> tCompanyHotPostDTOS(Page page);


    List<String> hot();

    /**
     * @Description: 获取公司最新职位(近一个星期)
     * @param companyId：企业id   pageQuery：分页
     * @return null
     * @Author tangzhuo
     * @CreateTime 2023/2/14 11:32
     */

    IPage<LatestPostPO> latestPost(String companyId, PageQuery pageQuery);

    List<String> jobType(String companyId);
    /**
     * @Description: 获取职位中心
     * @param positionQuery:筛选条件
     * @return null
     * @Author tangzhuo
     * @CreateTime 2023/2/15 9:19
     */

    IPage<PostCollectDTO> openForPositions(PositionQuery positionQuery);

    IPage<PostShare> selectPosition(IPage page, PostShareQuery postShareQuery);

    IPage<CompanyPostDTO> jobSearch(JobSearchQuery jobSearchQuery);


    List<TCompanyPostRecord> list1();

    /**
     * @Description: 获取该公司的所有岗位的岗位名称
     * @param companyId:企业id
     * @return 岗位名称集合
     * @Author tangzhuo
     * @CreateTime 2023/3/31 17:23
     */

    List<String> JobTitle(String companyId);


    /**
     * @Description:获取该企业的岗位名称
     * @param userid：企业id
     * @return 企业岗位名称
     * @Author tangzhuo
     * @CreateTime 2023/4/1 10:29
     */

    List<PostLabel> postLabel(String userid,String keyword);

    /**
     * 模糊查询企业
     * @param keyword
     * @return
     */
    List<String> firm(String keyword);

    /**
     * 模糊查询岗位名称
     * @param keyword
     * @param i
     * @return
     */
    List<String> post(String keyword, int i);

    /**
     * 获取岗位的基本信息
     * @param postId
     * @return
     */
    TCompanyPostRecord postRecord(String postId);

    /**
     * 判断岗位是否存在
     * @param postId
     * @return
     */
    Boolean postAmend(String postId);


    /**
     * 获取接单详情
     * @param orderReceivingQuery
     * @param userid
     * @return
     */
    IPage<OrderReceivingDTO> recommendSelect(OrderReceivingQuery orderReceivingQuery, String userid);

    IPage<OrderReceivingDTO> recommendSelectB(OrderReceivingQuery orderReceivingQuery, String userid);

    /**
     * 获取企业名称
     * @param list
     * @return
     */
    List<Map<String, Object>> companyName(List<String> list);

    /**
     * 推荐官自营岗位查询
     * @param page
     * @param keyword
     * @param userid
     * @param state
     * @return
     */
    Page<PostShare> selectRecommend(Page page, String keyword, String userid, Integer state,Integer individualOrTeam);

    /**
     * 获取校园
     * 后端开发自动筛选的开发依据
     * 1、岗位名称含有实习生或应届生的
     * 2、岗位类别是校园的
     * 3、经验是无经验或不限经验的
     * 符合上述任一条件的报名企业的岗位都归到广东交通职业技术学院的已报名的企业列表；
     * @param page
     * @param keyword
     * @return
     */
    IPage<SchoolyardDTO> selectCompanyId(Page page, String keyword);

    /**
     * 获取岗位及职位福利
     */
    List<TCompanyPost> selectPostName(List<String> list);

    /**
     * 校园查询搜索关键字
     * @param keyword
     * @return
     */
    List<String> keywordSchoolyard(String keyword);


    /**
     * 查询企业有那些求职类型
     * @param userid
     * @return
     */
    List<Integer> selectPostType(String userid,Integer state);

    /**
     *获取企业发布职位的工作地点
     * @param id
     * @return
     */
    List<String> workPlace(String id);

    /**
     * 查看收藏的
     * @param orderReceivingQuery
     * @param userid
     * @return
     */
    IPage<OrderReceivingDTO> recommendSelectC(OrderReceivingQuery orderReceivingQuery, String userid);

    String basic(String postId);

    /**
     * 查询
     * @param postId
     * @return
     */
    TCompanyPost byId(String postId);
}
