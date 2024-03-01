package com.wcwy.company.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wcwy.company.dto.RecommendDataDTO;
import com.wcwy.company.entity.TPermission;
import com.wcwy.company.entity.TRecommend;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wcwy.company.po.JobHunterShare;
import com.wcwy.company.po.TRecommendPO;
import com.wcwy.company.po.TRecommendShare;
import com.wcwy.company.query.ShareQuery;
import com.wcwy.company.query.TRecommendQuery;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
* @author Administrator
* @description 针对表【t_recommend(推荐官)】的数据库操作Service
* @createDate 2022-09-07 14:02:34
*/
public interface TRecommendService extends IService<TRecommend> {

    List<TPermission> rolePermission(String companyId);

    boolean UpdateCurrencyCount(String tRecommend, BigDecimal money,int ifIncome);

    IPage<TRecommendPO> pageList(TRecommendQuery tRecommendQuery);

    /**
     * @Description: 获取电话号码
     * @return null
     * @Author tangzhuo
     * @CreateTime 2022/11/14 16:23
     */

    String selectPhone(String companyId);

    /**
     * 获取邀请的推荐官
     * @param shareQuery
     * @param userid
     * @return
     */
    IPage<TRecommendShare> shareRecommend(ShareQuery shareQuery, String userid);

    /**
     * 获取推荐官电话
     * @param userId
     * @return
     */
    String phoneNumbers(String userId);

    /**
     * 新增金币
     * @param bigDecimal
     * @param userid
     * @return
     */
    BigDecimal addGold(BigDecimal bigDecimal, String userid) throws InterruptedException, Exception;

    /**
     * 获取推荐官id及邀请人
     * @param id:推荐官id
     * @return
     */
    Map<String, Object> getSharePersonRecommend(String id);

     Map<String, Object> deductCurrency(BigDecimal money, String userId, String jobHunter, String orderId) throws Exception;
    /**
     * 扣除金币
     * @param money
     * @param userId
     * @param jobHunter
     * @param orderId
     * @return
     * @throws Exception
     */
    Map<String, Object> deductGold(BigDecimal money, String userId, String jobHunter, String orderId) throws Exception;

    /**
     * @Description: 按年+月份分类获取推荐官数量
     * @param userid：用户id ：年份
     * @return null
     * @Author tangzhuo
     * @CreateTime 2023/7/21 16:09
     */
    List<Map> graph(String userid, String year);

    /**
     * 获取推荐官资产及人才数据
     * @param userid
     * @return
     */
    RecommendDataDTO getRecommendDataDTO(String userid);

    /**
     * @Description: 获取推荐官的基本信息
     * @param id:推荐官id
     * @return null
     * @Author tangzhuo
     * @CreateTime 2023/9/27 9:50
     */

    TRecommend selectBasic(String id);

    /**
     * 获取团队成员
     * @param userid
     * @return
     */
    List<String> getMember(String userid);

    /**
     * 获取所有团队的数据
     * @param list
     * @return
     */
    List<RecommendDataDTO> getMemberRecommendDataDTO(List<String> list);

    /**
     * 获取企业邀请人
     * @param companyId
     * @return
     */
    TRecommend enterpriseInviter(String companyId);

    /**
     * 获取推荐官
     * @param id
     * @param currentStartDate
     * @param currentEndTime
     * @param city
     * @param identity
     * @return
     */
    int selectCount(String id, LocalDateTime currentStartDate, LocalDateTime currentEndTime, String city, int identity);

    /**
     *按天查询数据总量
     * @param id
     * @param currentStartDate
     * @param currentEndTime
     * @param city
     * @param identity
     * @return
     */
    List<Map> mapList(String id, LocalDateTime currentStartDate, LocalDateTime currentEndTime, String city, int identity);
}
