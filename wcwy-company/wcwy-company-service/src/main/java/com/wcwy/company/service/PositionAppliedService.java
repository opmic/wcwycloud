package com.wcwy.company.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wcwy.company.dto.PositionAppliedDTO;
import com.wcwy.company.entity.PositionApplied;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wcwy.company.query.PositionAppliedQuery;
import org.springframework.web.bind.annotation.RequestParam;

/**
* @author Administrator
* @description 针对表【position_applied(求职者意向职位)】的数据库操作Service
* @createDate 2023-08-30 13:56:27
*/
public interface PositionAppliedService extends IService<PositionApplied> {

    /**
     * 获取意向职位人选
     * @param positionAppliedQuery
     * @return
     */
    IPage<PositionAppliedDTO> select(PositionAppliedQuery positionAppliedQuery);

    /**
     * @Description: 分享职位被投简记录接口
     * @param postId 职位id time推广时间戳 QRCode:分享邀请码 userid:求职者id
     * @return null
     * @Author tangzhuo
     * @CreateTime 2023/8/31 11:55
     */
    public Boolean sendAResume(String postId,Long time,String QRCode,String userid);
}
