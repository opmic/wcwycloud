package com.wcwy.company.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wcwy.company.dto.CooCommentDTO;
import com.wcwy.company.entity.CooComment;
import com.wcwy.company.query.CooCommentQuery;
import com.wcwy.company.service.CooCommentService;
import com.wcwy.company.mapper.CooCommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【coo_comment】的数据库操作Service实现
* @createDate 2024-01-19 11:18:22
*/
@Service
public class CooCommentServiceImpl extends ServiceImpl<CooCommentMapper, CooComment>
    implements CooCommentService{


    @Autowired
    private CooCommentMapper cooCommentMapper;
    @Override
    public IPage<CooCommentDTO> getPage(CooCommentQuery pageQuery) {
        return cooCommentMapper.getPage(pageQuery.createPage(),pageQuery);
    }
}




