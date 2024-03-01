package com.wcwy.post.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wcwy.post.dto.*;
import com.wcwy.post.entity.TCompanyPost;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wcwy.post.po.LatestPostPO;
import com.wcwy.post.po.PostLabel;
import com.wcwy.post.pojo.TCompanyPostRecord;
import com.wcwy.post.query.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
* @author Administrator
* @description 针对表【t_company_post(企业招聘岗位表)】的数据库操作Mapper
* @createDate 2022-09-14 14:54:54
* @Entity com.wcwy.post.entity.TCompanyPost
*/
@Mapper
public interface TCompanyPostMapper extends BaseMapper<TCompanyPost> {

    IPage<TCompanyPost> seletListPage(@Param("page") IPage page,@Param("tCompanyPostQuery") TCompanyPostQuery tCompanyPostQuery);


    IPage<PostShare> selectListPagePostShare(@Param("page") IPage page,@Param("tCompanyPostQuery")TCompanyPostQuery tCompanyPostQuery);

    IPage<CompanyPostDTO> getPage(@Param("page") IPage page, @Param("tCompanyNewstQuery") TCompanyNewstQuery tCompanyNewstQuery);

    IPage<CompanyPostDTO> listPost(@Param("page") IPage page, @Param("tCompanyNewstQuery") TCompanyNewstQuery tCompanyNewstQuery);
    @Select("SELECT  t_company_post.company_id FROM  t_company_post  WHERE  t_company_post.post_id = #{post}")
    String selectCompany(String post);

    Page<CompanyPostDTO> recommendlistPost(@Param("page") IPage page, @Param("tCompanyNewstQuery") TCompanyNewstQuery tCompanyNewstQuery);

    IPage<PostShare> selectSonPostShare(@Param("page") Page page,@Param("identification") Integer identification,@Param("postName")  String postName, @Param("companyId") String companyId);

    IPage<TCompanyPost> hotJob(@Param("page") Page page);

    IPage<TCompanyHotPostDTO> tCompanyHotPostDTOS(@Param("page")Page page);

    List<String> hot();

    IPage<LatestPostPO> latestPost(@Param("companyId") String companyId,@Param("page") IPage page);

    List<String> jobType(@Param("companyId") String companyId);

    IPage<PostCollectDTO> openForPositions(@Param("page")IPage page,@Param("positionQuery") PositionQuery positionQuery);

    IPage<PostShare> selectPosition(@Param("page")IPage page,@Param("positionQuery") PostShareQuery postShareQuery);


    IPage<CompanyPostDTO> jobSearch(@Param("page") IPage page, @Param("jobSearchQuery")JobSearchQuery jobSearchQuery);


    List<TCompanyPostRecord> list1();

    List<String> JobTitle(@Param("companyId") String companyId);

    List<PostLabel> postLabel(@Param("userid") String userid,@Param("keyword") String keyword);

    /**
     * 企业模糊查询
     * @param keyword
     * @return
     */
    List<String> firm(@Param("keyword")String keyword);

    /**
     * 模糊查询岗位
     * @param keyword
     * @param i
     * @return
     */
    List<String> post(@Param("keyword") String keyword, @Param("i") int i);

    TCompanyPostRecord postRecord(@Param("postId") String postId);

    /**
     * 接单大厅
     * @param page
     * @param orderReceivingQuery
     * @param userid
     * @return
     */
    IPage<OrderReceivingDTO> recommendSelect(@Param("page") IPage page,@Param("orderReceivingQuery") OrderReceivingQuery orderReceivingQuery,@Param("userid") String userid);
    IPage<OrderReceivingDTO> recommendSelectB(@Param("page") IPage page,@Param("orderReceivingQuery") OrderReceivingQuery orderReceivingQuery,@Param("userid") String userid);
    IPage<OrderReceivingDTO> recommendSelectC(@Param("page") IPage page,@Param("orderReceivingQuery") OrderReceivingQuery orderReceivingQuery,@Param("userid") String userid);
    List<Map<String, Object>> companyName(@Param("list") List<String> list);

    /**
     * 查询自营岗位
     * @param page
     * @param keyword
     * @param userid
     * @param state
     * @return
     */
    Page<PostShare> selectRecommend(@Param("page")Page page,@Param("keyword") String keyword,@Param("userid") String userid,@Param("state") Integer state,@Param("individualOrTeam") Integer individualOrTeam);

    IPage<SchoolyardDTO> selectCompanyId(@Param("page") Page page, @Param("keyword")String keyword);

    List<TCompanyPost> selectPostName(@Param("list") List<String> list);

    List<String> keywordSchoolyard(@Param("keyword") String keyword);

    /**
     * 查询企业发布的岗位有哪些类型
     * @param userid
     * @return
     */

    List<Integer> tCompanyPostMapper(@Param("userid") String userid,@Param("state") Integer state);

    /***
     * 查询企业发不职位的工作地点
     * @param id
     * @return
     */
    List<String> workPlace(@Param("id") String id);


}




