package com.wcwy.company.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wcwy.company.dto.RMDownloadDTO;
import com.wcwy.company.dto.ReferrerRecordJobHunterDTO;
import com.wcwy.company.entity.ReferrerRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wcwy.company.query.RMDownloadQuery;
import com.wcwy.company.query.ReferrerRecordQuery;
import com.wcwy.company.vo.RecommendationReportVO;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.Map;

/**
* @author Administrator
* @description 针对表【referrer_record(推荐人数据记录)】的数据库操作Service
* @createDate 2022-12-28 10:04:50
*/
public interface ReferrerRecordService extends IService<ReferrerRecord> {

    /**
     * @Description: 查询推荐总数据量
     * @param userid:推荐官id
     * @return 查询推荐总数据量
     * @Author tangzhuo
     * @CreateTime 2022/12/28 11:43
     */

    ReferrerRecord referrerRecordSUM(String userid);

    /**
     * @Description: 增加推荐数量
     * @param identification:标识  tJobHunter:求职者id  recommend:企业id
     * @return null
     * @Author tangzhuo
     * @CreateTime 2022/12/28 11:45
     */
    @Async
    void augment(String tJobHunter,String recommend,Integer identification);

    /**
     * @Description: 减少推荐数量
     * @param identification:标识 tJobHunter:求职者id  recommend:企业id
     * @return null
     * @Author tangzhuo
     * @CreateTime 2022/12/28 11:47
     */
    @Async
    void reduce(String tJobHunter,String recommend,Integer identification);

    /**
     * @Description: 查询我的人才数据
     * @param userid：推荐官id
     * @return null
     * @Author tangzhuo
     * @CreateTime 2023/5/23 10:22
     */

    List<Map> talents(String userid);


    /**
     * @Description: 查询我的人才
     * @param referrerRecordQuery
     * @return null
     * @Author tangzhuo
     * @CreateTime 2023/5/23 16:10
     */
    IPage<ReferrerRecordJobHunterDTO> selectReferrerRecordJobHunter(String userid,ReferrerRecordQuery referrerRecordQuery);



    /**
     * 获取求职者的求职期望列表
     * @param keyword
     * @param pageSize
     * userid
     * @return
     */
    List<String> expectation(String keyword, Integer pageSize, String userid);

    /**
     * 判断求职者和推荐官是否绑定
     * @param userId
     * @param userid
     * @return
     */
    Boolean getCorrelationType(String userId, String userid);

    /**
     * 获取我的人才数量
     * @param userid
     * @return
     */
    Map<String,Long>  countTalents(String userid);

    /**
     * 我的下载
     * @param rmDownloadQuery
     * @param userid
     * @return
     */
    IPage<RMDownloadDTO> selectDownload(RMDownloadQuery rmDownloadQuery, String userid);

    /**
     * @Description: 修改求职者的推荐报告
     * @param userId:推荐官id recommendationReportVO:投简记录
     * @return null
     * @Author tangzhuo
     * @CreateTime 2023/6/8 15:47
     */


    void updateReport(RecommendationReportVO recommendationReportVO,String userId);



    /**
     * @Description: 按年+月份分类获取求职者数量
     * @param userid：用户id ：年份
     * @return null
     * @Author tangzhuo
     * @CreateTime 2023/7/21 16:09
     */
    List<Map> graph(String userid, String year);

  /**
   * @Description: 应聘职位
   * @param userId:推荐官id
   * @return jobHunter:求职者id
   * @Author tangzhuo
   * @CreateTime 2023/9/26 16:29
   */

    
  void   positionApplied(String userId,String jobHunter);

  /*
  *
  *
  * */
    int countJobHunter(String userid);

}
