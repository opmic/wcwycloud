package com.wcwy.company.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wcwy.company.dto.PromotionPostDTO;
import com.wcwy.company.entity.PromotionPostDetail;
import com.wcwy.company.service.PromotionPostDetailService;
import com.wcwy.company.mapper.PromotionPostDetailMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author Administrator
* @description 针对表【promotion_post_detail(推广职位明细)】的数据库操作Service实现
* @createDate 2023-08-29 16:25:43
*/
@Service
public class PromotionPostDetailServiceImpl extends ServiceImpl<PromotionPostDetailMapper, PromotionPostDetail>
    implements PromotionPostDetailService{

    @Resource
    private PromotionPostDetailMapper promotionPostDetailMapper;

    @Override
    public List<PromotionPostDTO> listCount(List<String> query) {
        return promotionPostDetailMapper.listCount(query);
    }

    @Override
    public IPage<PromotionPostDTO> getDetail(Page page, String promotionPostId) {
        return promotionPostDetailMapper.getDetail(page,promotionPostId);
    }
}




