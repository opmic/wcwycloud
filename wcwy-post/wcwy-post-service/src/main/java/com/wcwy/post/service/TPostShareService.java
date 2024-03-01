package com.wcwy.post.service;

import com.wcwy.post.dto.TotalPostShare;
import com.wcwy.post.entity.TPostShare;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Administrator
* @description 针对表【t_post_share(发布岗位纪录表)】的数据库操作Service
* @createDate 2022-09-15 17:26:24
*/
public interface TPostShareService extends IService<TPostShare> {
    /**
     * @Description: 更新岗位记录
     * @param jobHunter:求职者id  post:岗位 state:状态 putInUser:投简人
     * @return null
     * @Author tangzhuo
     * @CreateTime 2023/1/3 14:27
     */

    void updateRedisPost(String jobHunter,String post, Integer state, String putInUser);

    /**
     * 取消岗位记录
     * @param jobHunter
     * @param post
     * @param state
     * @param putInUser
     */
    void cancelRedisPost(String jobHunter,String post, Integer state, String putInUser);
    /**
     * @Description:查询当前企业发布岗位的总数量记录
     * @param companyId:企业id
     * @return
     * @Author tangzhuo
     * @CreateTime 2022/12/26 15:34
     */

    TotalPostShare selectTotalPostShare(String companyId);

    void updateShare(String postId);
}
