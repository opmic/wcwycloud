package com.wcwy.company.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wcwy.company.dto.PositionAppliedDTO;
import com.wcwy.company.entity.PositionApplied;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wcwy.company.query.PositionAppliedQuery;
import org.apache.ibatis.annotations.Param;

/**
* @author Administrator
* @description 针对表【position_applied(求职者意向职位)】的数据库操作Mapper
* @createDate 2023-08-30 13:56:27
* @Entity com.wcwy.company.entity.PositionApplied
*/
public interface PositionAppliedMapper extends BaseMapper<PositionApplied> {

    /**
     * 查询求职者意向
     * @param page
     * @param positionAppliedQuery
     * @return
     */
    IPage<PositionAppliedDTO> select(@Param("page") IPage page,@Param("positionAppliedQuery") PositionAppliedQuery positionAppliedQuery);
}




