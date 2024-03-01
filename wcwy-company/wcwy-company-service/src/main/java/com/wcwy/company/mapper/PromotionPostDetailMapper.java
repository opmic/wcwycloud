package com.wcwy.company.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wcwy.company.dto.PromotionPostDTO;
import com.wcwy.company.entity.PromotionPostDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wcwy.company.query.PromotionPostQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import javax.validation.constraints.Max;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
* @author Administrator
* @description 针对表【promotion_post_detail(推广职位明细)】的数据库操作Mapper
* @createDate 2023-08-29 16:25:43
* @Entity com.wcwy.company.entity.PromotionPostDetail
*/
@Mapper
public interface PromotionPostDetailMapper extends BaseMapper<PromotionPostDetail> {


    IPage<PromotionPostDTO> select(@Param("page") IPage page,@Param("promotionPostQuery") PromotionPostQuery promotionPostQuery);

    //查询推广的次数
    List<PromotionPostDTO> listCount(@Param("query")  List<String> query);

    /**
     * 按分钟查询
     * @param page
     * @param promotionPostId
     * @return
     */
    IPage<PromotionPostDTO> getDetail(@Param("page") Page page,@Param("promotionPostId") String promotionPostId);


    int count(@Param("id")  String id,@Param("beginDate") LocalDateTime beginDate, @Param("endDate") LocalDateTime endDate, @Param("city") String city);

    List<Map> mapList(@Param("id")  String id,@Param("beginDate") LocalDateTime beginDate, @Param("endDate") LocalDateTime endDate, @Param("city") String city);
}




