package com.wcwy.post.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wcwy.post.dto.TRecommendPostDTO;
import com.wcwy.post.entity.TRecommendPost;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Administrator
* @description 针对表【t_recommend_post(企业招聘岗位表)】的数据库操作Service
* @createDate 2023-06-15 16:01:23
*/
public interface TRecommendPostService extends IService<TRecommendPost> {

    /**
     * 推荐官获取自营岗位
     * @param page
     * @param keyword
     * @param userid
     * @return
     */
    Page<TRecommendPostDTO> select(Page page, String keyword, String userid,Integer state);

    /**
     * 获取当前职位详细信息
      * @param postId
     * @return
     */
    TRecommendPostDTO selectById(String postId);
}
