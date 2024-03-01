package com.wcwy.company.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wcwy.company.dto.CooCommentDTO;
import com.wcwy.company.entity.CooComment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wcwy.company.query.CooCommentQuery;
import org.apache.ibatis.annotations.Param;

/**
* @author Administrator
* @description 针对表【coo_comment】的数据库操作Mapper
* @createDate 2024-01-19 11:18:22
* @Entity com.wcwy.company.entity.CooComment
*/
public interface CooCommentMapper extends BaseMapper<CooComment> {

    /**
     * 分页获取
     * @param page
     * @param pageQuery
     * @return
     */
    IPage<CooCommentDTO> getPage(@Param("page") IPage page,@Param("pageQuery") CooCommentQuery pageQuery);
}




