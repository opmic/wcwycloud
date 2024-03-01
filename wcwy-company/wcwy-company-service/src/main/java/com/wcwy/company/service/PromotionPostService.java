package com.wcwy.company.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wcwy.company.dto.PromotionPostDTO;
import com.wcwy.company.entity.PromotionPost;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wcwy.company.query.PromotionPostQuery;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
* @author Administrator
* @description 针对表【promotion_post(推广职位)】的数据库操作Service
* @createDate 2023-08-29 16:25:40
*/
public interface PromotionPostService extends IService<PromotionPost> {

    /**
     * 添加推广记录
     * @param post
     * @param userID
     */
    public void add(String post,String userID);

    /**
     * 查询推管接口
     * @param promotionPostQuery
     * @return
     */
    IPage<PromotionPostDTO> select(PromotionPostQuery promotionPostQuery);

    /*
    * 查询职位分享数据预览
    * */
    int selectCount(String id, LocalDateTime beginDate, LocalDateTime endDate, String city);

    List<Map> mapList(String id, LocalDateTime currentStartDate, LocalDateTime currentEndTime, String city);
}
