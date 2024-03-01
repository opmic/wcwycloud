package com.wcwy.post.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wcwy.company.po.PutPostShareDataPO;
import com.wcwy.post.dto.OrderReceivingDTO;
import com.wcwy.post.dto.OrderReceivingPostDTO;
import com.wcwy.post.dto.PostShare;
import com.wcwy.post.entity.OrderReceiving;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wcwy.post.query.OrderReceivingCollectQuery;
import com.wcwy.post.query.OrderReceivingPostQuery;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
* @author Administrator
* @description 针对表【order_receiving(接单表)】的数据库操作Mapper
* @createDate 2023-05-25 10:01:43
* @Entity com.wcwy.post.entity.OrderReceiving
*/
public interface OrderReceivingMapper extends BaseMapper<OrderReceiving> {


    IPage<OrderReceivingPostDTO> selectCollect(@Param("page") IPage page, @Param("orderReceivingCollectQuery") OrderReceivingCollectQuery orderReceivingCollectQuery, @Param("userid")  String userid);

    List<PostShare> getCount(@Param("postId") List<String> postId);


    List<String> orderReceivingCompanyName(@Param("userid") String userid,@Param("keyword") String keyword);

    List<String> orderReceivingPostName(@Param("userid") String userid,@Param("keyword") String keyword);

    IPage<OrderReceivingDTO> orderReceivingPost(@Param("page") IPage page,@Param("orderReceivingPostQuery") OrderReceivingPostQuery orderReceivingPostQuery);

    List<PutPostShareDataPO> orderReceivingDay(@Param("id") String id,@Param("localDate") LocalDate localDate, @Param("city") String city,@Param("postType") Integer postType);

    List<PutPostShareDataPO> orderReceivingWeek(@Param("id") String id, @Param("beginDate")LocalDate beginDate,@Param("allEndDate") LocalDate allEndDate,@Param("city") String city,@Param("postType") Integer postType);
}




