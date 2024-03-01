package com.wcwy.company.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wcwy.company.dto.CollectJobHunterDTO;
import com.wcwy.company.entity.CollectJobHunter;
import com.wcwy.company.query.CollectJobHunterQuery;
import com.wcwy.company.service.CollectJobHunterService;
import com.wcwy.company.mapper.CollectJobHunterMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author Administrator
* @description 针对表【collect_job_hunter(企业收藏表)】的数据库操作Service实现
* @createDate 2023-04-03 14:04:49
*/
@Service
public class CollectJobHunterServiceImpl extends ServiceImpl<CollectJobHunterMapper, CollectJobHunter>
    implements CollectJobHunterService{
    @Resource
   private CollectJobHunterMapper collectJobHunterMapper;

    @Override
    public Integer isCollect(String tCompany, String tJobHunter) {
        return collectJobHunterMapper.isCollect(tCompany,tJobHunter);
    }

    @Override
    public IPage<CollectJobHunterDTO> select(CollectJobHunterQuery collectJobHunterQuery,String userId) {
        return collectJobHunterMapper.select(collectJobHunterQuery.createPage(),userId,collectJobHunterQuery);
    }
}




