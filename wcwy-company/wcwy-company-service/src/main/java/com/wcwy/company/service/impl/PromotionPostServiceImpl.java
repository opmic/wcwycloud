package com.wcwy.company.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wcwy.common.redis.util.IDGenerator;
import com.wcwy.company.dto.PromotionPostDTO;
import com.wcwy.company.entity.PromotionPost;
import com.wcwy.company.entity.PromotionPostDetail;
import com.wcwy.company.mapper.PromotionPostDetailMapper;
import com.wcwy.company.query.PromotionPostQuery;
import com.wcwy.company.service.PromotionPostDetailService;
import com.wcwy.company.service.PromotionPostService;
import com.wcwy.company.mapper.PromotionPostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
* @author Administrator
* @description 针对表【promotion_post(推广职位)】的数据库操作Service实现
* @createDate 2023-08-29 16:25:40
*/
@Service
public class PromotionPostServiceImpl extends ServiceImpl<PromotionPostMapper, PromotionPost>
    implements PromotionPostService{

    @Autowired
    private PromotionPostDetailService promotionPostDetailService;

    @Autowired
    private PromotionPostDetailMapper promotionPostDetailMapper;

    @Autowired
    private IDGenerator idGenerator;
    @Override
    public void add(String post, String userID) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("post_id",post);
        queryWrapper.eq("recommend_id",userID);
        PromotionPost one  =this.getOne(queryWrapper);
        //不存在重新赋值
        if(one==null){
            PromotionPost promotionPost=new PromotionPost();
            promotionPost.setPromotionPostId(idGenerator.generateCode("PP"));
            promotionPost.setPostId(post);
            promotionPost.setRecommendId(userID);
            promotionPost.setPromotionTime(LocalDateTime.now());
            boolean save = this.save(promotionPost);
            if(save){
                one=promotionPost;
            }

        }
        if(one==null){
            return;
        }
        PromotionPostDetail promotionPostDetail=new PromotionPostDetail();
        promotionPostDetail.setPromotionPostId(one.getPromotionPostId());
        promotionPostDetail.setPromotionTime(LocalDateTime.now());
        promotionPostDetailService.save(promotionPostDetail);
    }

    @Override
    public IPage<PromotionPostDTO> select(PromotionPostQuery promotionPostQuery) {
        return promotionPostDetailMapper.select(promotionPostQuery.createPage(),promotionPostQuery);
    }

    @Override
    public int selectCount(String id, LocalDateTime beginDate, LocalDateTime endDate, String city) {
        return promotionPostDetailMapper.count(id,beginDate,endDate,city);
    }

    @Override
    public List<Map> mapList(String id, LocalDateTime currentStartDate, LocalDateTime currentEndTime, String city) {
        return promotionPostDetailMapper.mapList(id,currentStartDate,currentEndTime,city);
    }
}




