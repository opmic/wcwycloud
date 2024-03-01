package com.wcwy.company.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wcwy.company.dto.CooCommentDTO;
import com.wcwy.company.entity.CooComment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wcwy.company.query.CooCommentQuery;

/**
* @author Administrator
* @description 针对表【coo_comment】的数据库操作Service
* @createDate 2024-01-19 11:18:22
*/
public interface CooCommentService extends IService<CooComment> {

    /**
     * 获取评论
     * @param pageQuery
     * @return
     */
    IPage<CooCommentDTO> getPage(CooCommentQuery pageQuery);
}
