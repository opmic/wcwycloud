package com.wcwy.company.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wcwy.company.dto.CooTribeDTO;
import com.wcwy.company.entity.CooTribe;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wcwy.company.query.CooTribeQuery;
import com.wcwy.company.vo.CooTribeVO;

/**
* @author Administrator
* @description 针对表【coo_tribe】的数据库操作Service
* @createDate 2024-01-19 11:18:16
*/
public interface CooTribeService extends IService<CooTribe> {

    /**
     * 添加发帖
     * @param cooTribeVO
     * @return
     */
    boolean add(CooTribeVO cooTribeVO);

    /**
     * 删除帖子
     * @param id
     * @return
     */
    Boolean del(Long id);

    /**
     * 分页获取
     * @param cooTribeQuery
     * @return
     */
    IPage<CooTribe> getPage(CooTribeQuery cooTribeQuery);

    /**
     * 获取问答
     * @param cooTribeQuery
     * @return
     */
    IPage<CooTribeDTO> pageAnswer(CooTribeQuery cooTribeQuery);

    /**
     * 判断是否回答
     * @param id
     * @return
     */
    Boolean isReply(Long id);



    /**
     * 获取回答
     * @param cooTribeQuery
     * @return
     */
    IPage<CooTribe> selectType(CooTribeQuery cooTribeQuery, String userid);

    /*
    获取发帖人的基本信息
     */
    IPage<CooTribeDTO> getPageDTO(CooTribeQuery cooTribeQuery);

    /**
     *
     * @param id 发帖id
     * @param userId 用户id
     * @return
     */
    CooTribeDTO getAnswer(Long id, String userId);

    /**
     * 获取我的帖子
     * @param cooTribeQuery
     * @return
     */
    IPage<CooTribeDTO> inquirePage(CooTribeQuery cooTribeQuery);

    /**
     * 通过id查询
     * @param id
     * @return
     */
    CooTribeDTO selectId(Long id);
}
