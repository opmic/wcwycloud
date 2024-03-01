package com.wcwy.company.service;

import com.wcwy.company.dto.TProvincesCitieDTO;
import com.wcwy.company.entity.TProvinces;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author Administrator
 * @description 针对表【t_provinces(省份)】的数据库操作Service
 * @createDate 2022-09-02 10:33:39
 */
public interface TProvincesService extends IService<TProvinces> {
    /**
     * @param null
     * @return null
     * @Description:获取省市
     * @Author tangzhuo
     * @CreateTime 2022/9/2 10:55
     */

    List<TProvincesCitieDTO> selectPC();

    /**
     * @Description: 获取推荐官联系方式
     * @param null
     * @return null
     * @Author tangzhuo
     * @CreateTime 2022/12/23 14:38
     */

    String selectPhone(String putInUser);

}
