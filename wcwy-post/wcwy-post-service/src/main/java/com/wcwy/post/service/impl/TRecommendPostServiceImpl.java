package com.wcwy.post.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wcwy.post.dto.TRecommendPostDTO;
import com.wcwy.post.entity.TRecommendPost;
import com.wcwy.post.service.TRecommendPostService;
import com.wcwy.post.mapper.TRecommendPostMapper;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【t_recommend_post(企业招聘岗位表)】的数据库操作Service实现
* @createDate 2023-06-15 16:01:23
*/
@Service
public class TRecommendPostServiceImpl extends ServiceImpl<TRecommendPostMapper, TRecommendPost>
    implements TRecommendPostService{
    @Autowired
    private TRecommendPostMapper tRecommendPostMapper;


    @Override
    public Page<TRecommendPostDTO> select(Page page, String keyword, String userid,Integer state) {
        return tRecommendPostMapper.select(page,keyword,userid,state);
    }

    @Override
    public TRecommendPostDTO selectById(String postId) {
        return tRecommendPostMapper.selectId(postId);
    }
}




