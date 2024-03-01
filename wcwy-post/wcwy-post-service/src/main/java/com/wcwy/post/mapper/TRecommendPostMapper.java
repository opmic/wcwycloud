package com.wcwy.post.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wcwy.post.dto.TRecommendPostDTO;
import com.wcwy.post.entity.TRecommendPost;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* @author Administrator
* @description 针对表【t_recommend_post(企业招聘岗位表)】的数据库操作Mapper
* @createDate 2023-06-15 16:01:23
* @Entity com.wcwy.post.entity.TRecommendPost
*/
@Mapper
public interface TRecommendPostMapper extends BaseMapper<TRecommendPost> {


    /**
     * 获取自营岗位
     * @param page
     * @param keyword
     * @param userid
     * @return
     */
    Page<TRecommendPostDTO> select(@Param("page") Page page,@Param("keyword") String keyword,@Param("userid") String userid,@Param("state")  Integer state);

    /**
     * 获取自营岗位详细信息
     * @param postId
     * @return
     */
    TRecommendPostDTO selectId(@Param("postId") String postId);
}




