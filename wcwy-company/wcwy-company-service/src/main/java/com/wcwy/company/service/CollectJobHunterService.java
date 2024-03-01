package com.wcwy.company.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wcwy.company.dto.CollectJobHunterDTO;
import com.wcwy.company.entity.CollectJobHunter;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wcwy.company.mapper.CollectJobHunterMapper;
import com.wcwy.company.query.CollectJobHunterQuery;
import org.apache.ibatis.annotations.Param;

/**
* @author Administrator
* @description 针对表【collect_job_hunter(企业收藏表)】的数据库操作Service
* @createDate 2023-04-03 14:04:49
*/
public interface CollectJobHunterService extends IService<CollectJobHunter> {

    /**
     * @Description: 判断是否收藏
     * @param tCompany:企业id  tJobHunter：求职者id
     * @return null
     * @Author tangzhuo
     * @CreateTime 2023/4/3 15:07
     */
    
    public Integer isCollect( String tCompany,String tJobHunter);

    /**
     * 查询收藏的求职者
     * @param collectJobHunterQuery
     * @return
     */
    IPage<CollectJobHunterDTO> select(CollectJobHunterQuery collectJobHunterQuery,String userId);
}
