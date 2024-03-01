package com.wcwy.company.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wcwy.company.dto.CollectPostCompanyDTO;
import com.wcwy.company.entity.CollerctPost;
import com.wcwy.company.query.CollectPostQuery;
import com.wcwy.company.service.CollerctPostService;
import com.wcwy.company.mapper.CollerctPostMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Administrator
 * @description 针对表【collerct_post(岗位收藏表)】的数据库操作Service实现
 * @createDate 2022-10-13 17:27:25
 */
@Service
public class CollerctPostServiceImpl extends ServiceImpl<CollerctPostMapper, CollerctPost>
        implements CollerctPostService {
    @Resource
    private CollerctPostMapper collerctPostMapper;


    @Override
    public String selectCompany(String post) {
        return collerctPostMapper.selectCompany(post);
    }

    @Override
    public IPage<CollectPostCompanyDTO> selectCollect(CollectPostQuery collectPostQuery) {
        return collerctPostMapper.selectCollect(collectPostQuery.createPage(),collectPostQuery);
    }
}




