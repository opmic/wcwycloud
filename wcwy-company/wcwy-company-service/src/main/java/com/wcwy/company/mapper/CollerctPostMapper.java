package com.wcwy.company.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wcwy.company.dto.CollectPostCompanyDTO;
import com.wcwy.company.entity.CollerctPost;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wcwy.company.query.CollectPostQuery;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
* @author Administrator
* @description 针对表【collerct_post(岗位收藏表)】的数据库操作Mapper
* @createDate 2022-10-13 17:27:25
* @Entity com.wcwy.company.entity.CollerctPost
*/
public interface CollerctPostMapper extends BaseMapper<CollerctPost> {
    @Select("SELECT post.t_company_post.company_id FROM  post.t_company_post WHERE  post.t_company_post.post_id =#{post} ")
    String selectCompany(@Param("post") String post);


    IPage<CollectPostCompanyDTO> selectCollect(@Param("page")IPage page,@Param("collectPostQuery") CollectPostQuery collectPostQuery);
}




