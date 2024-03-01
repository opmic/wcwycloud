package com.wcwy.company.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wcwy.company.dto.PromotionPostDTO;
import com.wcwy.company.entity.PromotionPostDetail;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Administrator
* @description 针对表【promotion_post_detail(推广职位明细)】的数据库操作Service
* @createDate 2023-08-29 16:25:43
*/
public interface PromotionPostDetailService extends IService<PromotionPostDetail> {

    /**
     * @Description: 查询推管对应的次数
     * @param null 
     * @return null
     * @Author tangzhuo
     * @CreateTime 2023/8/30 9:55
     */
    
    List<PromotionPostDTO> listCount(List<String> query);

    /**
     * 按分钟查询次数
     * @param page
     * @param promotionPostId
     * @return
     */
    IPage<PromotionPostDTO> getDetail(Page page, String promotionPostId);
}
