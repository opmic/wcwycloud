package com.wcwy.company.mapper;

import com.wcwy.company.dto.MajorDTO;
import com.wcwy.company.dto.MajorParentDTO;
import com.wcwy.company.entity.Major;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author Administrator
* @description 针对表【major(专业名称表)】的数据库操作Mapper
* @createDate 2022-09-07 15:01:32
* @Entity com.wcwy.company.entity.Major
*/
@Mapper
public interface MajorMapper extends BaseMapper<Major> {

    List<MajorParentDTO> selectLists();
}




