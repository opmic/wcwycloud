package com.wcwy.company.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wcwy.company.dto.TProvincesCitieDTO;
import com.wcwy.company.entity.TProvinces;
import com.wcwy.company.service.TProvincesService;
import com.wcwy.company.mapper.TProvincesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Administrator
* @description 针对表【t_provinces(省份)】的数据库操作Service实现
* @createDate 2022-09-02 10:33:39
*/
@Service
public class TProvincesServiceImpl extends ServiceImpl<TProvincesMapper, TProvinces>
    implements TProvincesService{
    @Autowired
    private TProvincesMapper tProvincesMapper;

    @Override
    public List<TProvincesCitieDTO> selectPC() {
        return tProvincesMapper.selectPC();
    }

    @Override
    public String selectPhone(String putInUser) {
        return tProvincesMapper.selectPhone(putInUser);
    }
}




