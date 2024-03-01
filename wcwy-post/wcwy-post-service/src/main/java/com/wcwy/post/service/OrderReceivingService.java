package com.wcwy.post.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wcwy.company.po.PutPostShareDataPO;
import com.wcwy.post.dto.OrderReceivingDTO;
import com.wcwy.post.dto.OrderReceivingPostDTO;
import com.wcwy.post.dto.PostShare;
import com.wcwy.post.entity.OrderReceiving;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wcwy.post.query.OrderReceivingCollectQuery;
import com.wcwy.post.query.OrderReceivingPostQuery;

import java.time.LocalDate;
import java.util.List;

/**
* @author Administrator
* @description 针对表【order_receiving(接单表)】的数据库操作Service
* @createDate 2023-05-25 10:01:43
*/
public interface OrderReceivingService extends IService<OrderReceiving> {

    /**
     * 获取收藏的岗位
     * @param orderReceivingCollectQuery
     * @param userid
     * @return
     */
    IPage<OrderReceivingPostDTO> selectCollect(OrderReceivingCollectQuery orderReceivingCollectQuery, String userid);

    /***
     * 查看接单数据
     * @param postId
     * @return
     */
    List<PostShare> getCount(List<String> postId);



    /**
     * 关键字查询接单的企业名称
     * @param keyword
     * @return
     */
    List<String> orderReceivingCompanyName(String userid, String keyword);

    /**
     * 关键字查询接单的岗位名称
     * @param keyword
     * @return
     */
    List<String> orderReceivingPostName(String userid, String keyword);

    /**
     * 我的接单岗位
     * @param orderReceivingPostQuery
     * @return
     */
    IPage<OrderReceivingDTO> orderReceivingPost(OrderReceivingPostQuery orderReceivingPostQuery);

    /**
     * 获取当前接单的数量
     * @param id
     * @param localDate
     * @return
     */
    List<PutPostShareDataPO> orderReceivingDay(String id, LocalDate localDate,String city,Integer postType);

    List<PutPostShareDataPO> orderReceivingWeek(String id, LocalDate beginDate, LocalDate allEndDate,String city,Integer postType);
}
