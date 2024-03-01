package com.wcwy.company.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wcwy.company.dto.MajorDTO;
import com.wcwy.company.dto.MajorParentDTO;
import com.wcwy.company.entity.Major;
import com.wcwy.company.service.MajorService;
import com.wcwy.company.mapper.MajorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Administrator
* @description 针对表【major(专业名称表)】的数据库操作Service实现
* @createDate 2022-09-07 15:01:32
*/
@Service
public class MajorServiceImpl extends ServiceImpl<MajorMapper, Major>
    implements MajorService{
    @Autowired
    private MajorMapper majorMapper;
    @Override
    public List<MajorParentDTO> selectList() {
        return majorMapper.selectLists();
    }
}




