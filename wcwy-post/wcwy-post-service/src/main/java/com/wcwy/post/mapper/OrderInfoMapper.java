package com.wcwy.post.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wcwy.post.dto.ReferralRevenueDTO;
import com.wcwy.post.entity.OrderInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wcwy.post.pojo.DivideIntoPOJO;
import com.wcwy.post.query.OrderInfoInvoiceQuery;
import com.wcwy.post.query.ReferralRevenueQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
* @author Administrator
* @description 针对表【order_info(岗位订单表)】的数据库操作Mapper
* @createDate 2022-10-12 17:06:43
* @Entity com.wcwy.post.entity.OrderInfo
*/
@Mapper
public interface OrderInfoMapper extends BaseMapper<OrderInfo> {

    IPage<OrderInfo> selectApplyForInvoice(@Param("page") IPage page, @Param("orderInfoInvoiceQuery") OrderInfoInvoiceQuery orderInfoInvoiceQuery);

    /**
     * 获取求职的分成
     * @param identityId:身份id
     * @param recommendId:推荐官id
     * @param   type:查找的类型（1:企业 2:推荐官 3:求职者）
     * @return
     */
    DivideIntoPOJO divideInto(@Param("identityId")String identityId,@Param("recommendId") String recommendId,@Param("type") Integer type);

    /**
     *
     * @return
     */
    List<Map<String,String>> companys();

    Double sumMoney(@Param("userid") String userid);


    List<String> getPostLabel(@Param("userid") String userid, @Param("keyword") String keyword);

    IPage<ReferralRevenueDTO> referralRevenue(@Param("page") IPage page,@Param("referralRevenueQuery") ReferralRevenueQuery referralRevenueQuery);

    @Select("SELECT sum(order_info.share_money) FROM order_info WHERE order_info.divide_into_if = 3 AND order_info.share_user_id =#{userid}  AND order_info.post_id =#{postId}  AND order_info.state = 2 AND  order_info.identification IN (2,3,5)")
    BigDecimal postServiceCharge(@Param("userid") String userid,@Param("postId") String postId);
}




