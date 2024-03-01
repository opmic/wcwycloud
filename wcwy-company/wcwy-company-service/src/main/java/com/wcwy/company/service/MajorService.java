package com.wcwy.company.service;

import com.wcwy.company.dto.MajorDTO;
import com.wcwy.company.dto.MajorParentDTO;
import com.wcwy.company.entity.Major;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Administrator
* @description 针对表【major(专业名称表)】的数据库操作Service
* @createDate 2022-09-07 15:01:32
*/

public interface MajorService extends IService<Major> {

    List<MajorParentDTO> selectList();
}
