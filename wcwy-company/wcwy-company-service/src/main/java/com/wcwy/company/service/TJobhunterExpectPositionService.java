package com.wcwy.company.service;

import com.wcwy.company.entity.TJobhunterExpectPosition;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wcwy.company.vo.AddTJobhunterExpectPositionVO;
import com.wcwy.company.vo.TJobhunterExpectPositionVO;

import java.util.List;

/**
* @author Administrator
* @description 针对表【t_jobhunter_expect_position(期望职位表)】的数据库操作Service
* @createDate 2022-10-08 11:58:41
*/
public interface TJobhunterExpectPositionService extends IService<TJobhunterExpectPosition> {

    /**
     * 查询求职期望
     * @param userid
     * @return
     */
    String postionId(String userid);

    /**
     * 添加项目经历
     * @param tJobhunterExpectPositionVO
     * @param s
     * @return
     */
    Boolean adds(List<AddTJobhunterExpectPositionVO> tJobhunterExpectPositionVO, String s);

    /**
     * 查询存在几份求职期望
     * @param userId
     * @return
     */
    int selectCount(String userId);

    //查询求职者期望职位
    List<String> selectPositionName(String userid);
}
