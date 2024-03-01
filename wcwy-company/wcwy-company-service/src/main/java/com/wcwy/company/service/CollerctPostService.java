package com.wcwy.company.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wcwy.company.dto.CollectPostCompanyDTO;
import com.wcwy.company.entity.CollerctPost;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wcwy.company.query.CollectPostQuery;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Administrator
* @description 针对表【collerct_post(岗位收藏表)】的数据库操作Service
* @createDate 2022-10-13 17:27:25
*/
@Mapper
public interface CollerctPostService extends IService<CollerctPost> {

    String selectCompany(String post);
    
    /**
     * @Description: 查询收藏岗位接口
     * @param null 
     * @return null
     * @Author tangzhuo
     * @CreateTime 2023/8/3 11:51
     */
    
    IPage<CollectPostCompanyDTO> selectCollect(CollectPostQuery collectPostQuery);
}
