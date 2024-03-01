package com.wcwy.company.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wcwy.company.dto.CooTribeDTO;
import com.wcwy.company.entity.CooTribe;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wcwy.company.query.CooTribeQuery;
import org.apache.ibatis.annotations.Param;

/**
* @author Administrator
* @description 针对表【coo_tribe】的数据库操作Mapper
* @createDate 2024-01-19 11:18:16
* @Entity com.wcwy.company.entity.CooTribe
*/
public interface CooTribeMapper extends BaseMapper<CooTribe> {

    IPage<CooTribeDTO> pageAnswer(@Param("page") IPage page,@Param("id") Long id);

    IPage<CooTribe> selectType(@Param("page") IPage page,@Param("cooTribeQuery") CooTribeQuery cooTribeQuery,@Param("userid") String userid);


    IPage<CooTribeDTO> getPageDTO(@Param("page") IPage page,@Param("cooTribeQuery") CooTribeQuery cooTribeQuery);

    CooTribeDTO getAnswer(@Param("id") Long id,@Param("userId") String userId);

    IPage<CooTribeDTO> inquirePage(@Param("page") IPage page,@Param("cooTribeQuery") CooTribeQuery cooTribeQuery);

    CooTribeDTO selectId(@Param("id") Long id);
}




