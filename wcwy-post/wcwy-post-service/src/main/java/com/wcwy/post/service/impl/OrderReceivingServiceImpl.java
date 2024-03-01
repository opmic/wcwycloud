package com.wcwy.post.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wcwy.company.po.PutPostShareDataPO;
import com.wcwy.post.dto.OrderReceivingDTO;
import com.wcwy.post.dto.OrderReceivingPostDTO;
import com.wcwy.post.dto.PostShare;
import com.wcwy.post.entity.OrderReceiving;
import com.wcwy.post.query.OrderReceivingCollectQuery;
import com.wcwy.post.query.OrderReceivingPostQuery;
import com.wcwy.post.service.OrderReceivingService;
import com.wcwy.post.mapper.OrderReceivingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Administrator
 * @description 针对表【order_receiving(接单表)】的数据库操作Service实现
 * @createDate 2023-05-25 10:01:43
 */
@Service
public class OrderReceivingServiceImpl extends ServiceImpl<OrderReceivingMapper, OrderReceiving>
        implements OrderReceivingService {
    @Autowired
    private OrderReceivingMapper orderReceivingMapper;

    @Override
    public IPage<OrderReceivingPostDTO> selectCollect(OrderReceivingCollectQuery orderReceivingCollectQuery, String userid) {
        return orderReceivingMapper.selectCollect(orderReceivingCollectQuery.createPage(),orderReceivingCollectQuery,userid);
    }

    @Override
    public List<PostShare> getCount(List<String> postId) {
        return orderReceivingMapper.getCount(postId);
    }

    @Override
    public List<String> orderReceivingCompanyName(String userid, String keyword) {
        return orderReceivingMapper.orderReceivingCompanyName(userid,keyword);
    }

    @Override
    public List<String> orderReceivingPostName(String userid, String keyword) {
        return orderReceivingMapper.orderReceivingPostName(userid,keyword);
    }

    @Override
    public IPage<OrderReceivingDTO> orderReceivingPost(OrderReceivingPostQuery orderReceivingPostQuery) {
        return orderReceivingMapper.orderReceivingPost(orderReceivingPostQuery.createPage(),orderReceivingPostQuery);
    }

    @Override
    public List<PutPostShareDataPO> orderReceivingDay(String id, LocalDate localDate,String city,Integer postType) {
        return orderReceivingMapper.orderReceivingDay(id,localDate,city,postType);
    }

    @Override
    public List<PutPostShareDataPO> orderReceivingWeek(String id, LocalDate beginDate, LocalDate allEndDate,String city,Integer postType) {
        return orderReceivingMapper.orderReceivingWeek(id,beginDate,allEndDate,city,postType);
    }


}




