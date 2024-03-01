package com.wcwy.company.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wcwy.common.base.result.R;
import com.wcwy.company.dto.CompanyCollerctPutInResume;
import com.wcwy.company.dto.CompanyHomeDTO;
import com.wcwy.company.dto.SendAResumeRecord;
import com.wcwy.company.entity.TCompany;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wcwy.company.entity.TPermission;
import com.wcwy.company.po.TCompanyBasicInformation;
import com.wcwy.company.po.TCompanyPO;
import com.wcwy.company.po.TCompanySharePO;
import com.wcwy.company.query.ShareQuery;
import com.wcwy.company.query.TCompanyQuery;
import com.wcwy.company.query.inviterQuery;
import com.wcwy.company.vo.ProportionVO;
import com.wcwy.company.vo.TCompanyUserVO;
import com.wcwy.company.vo.TJobhunterVO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
* @author Administrator
* @description 针对表【t_company(企业表)】的数据库操作Service
* @createDate 2022-09-01 09:30:00
*/

public interface TCompanyService extends IService<TCompany> {


    R cc(String id);

    List<TPermission> rolePermission(String companyId);

    TCompany getId(String userid);

    CompanyCollerctPutInResume CompanyIndustryPutInResume(String id, String industryID);

    Boolean updateMoney(String  tCompanyId , BigDecimal money);


    IPage<TCompanyPO> pageList(TCompanyQuery query);


    IPage<TCompanyPO> listInviterCompany(inviterQuery inviterQuery);


    IPage<TCompany> selectSubsidiaryCorporation(String loginName, Page page, String keyword);

    /**
     * @Description: 查看
     * @param userid:当前登录的id
     * @param companyId:子账号id
     * @return true
     * @Author tangzhuo
     * @CreateTime 2022/12/27 9:20
     */

    Boolean subsidiary(String userid, String companyId);

    /**
     * @Description: 获取公司基本信息
     * @param tCompanyId ：企业id
     * @return null
     * @Author tangzhuo
     * @CreateTime 2023/2/14 14:25
     */

    TCompanyBasicInformation basicInformation(String tCompanyId);

    SendAResumeRecord sendAResumeRecord(String userid, String tCompanyId);

    /**
     * @Description: 修改个人信息
     * @param null 
     * @return null
     * @Author tangzhuo
     * @CreateTime 2023/3/30 13:28
     */
    
    boolean updateUserinfo(TCompanyUserVO tCompanyUserVO,String companyId);

    /**
     * @Description: 获取微信名称
     * @param null
     * @return null
     * @Author tangzhuo
     * @CreateTime 2023/3/30 15:12
     */

    String getWeChat(String userid);

    /**
     * 查看邀请的企业
     * @param shareQuery
     * @param userid
     * @return
     */
    IPage<TCompanySharePO> shareTCompany(ShareQuery shareQuery, String userid);

    /**
     * @Description:  查询企业的邀请及推荐官的邀请人
     * @param companyId  企业id
     * @return null
     * @Author tangzhuo
     * @CreateTime 2023/4/21 11:21
     */
    Map<String, String> getSharePerson(String companyId);

    /**
     * 获取电话号码
     * @param company
     * @return
     */
    String phoneNumbers(String company);


    /**
     * 获取其他身份
     * loginName:电话号码
     */
    List<String> authorization(String loginName);

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
     * 扣除无忧币
     * @param money 需要支付的无忧币
     * @param userId ：用户id
     * @param jobHunter 求职者id
     * @param orderId 订单id
     * @return
     */
    Map<String, Object> deductCurrency(BigDecimal money, String userId, String jobHunter, String orderId) throws Exception;

    /**
     * 扣除无忧币
     * @param money
     * @param userId
     * @param explain
     * @param orderId
     * @return
     */
    Map deductCurrencyExplain(BigDecimal money, String userId, String explain, String orderId) throws Exception;

    /**
     * 添加金币
     * @param bigDecimal
     * @param userid
     * @return
     */
    BigDecimal addGold(BigDecimal bigDecimal, String userid) throws InterruptedException, Exception;

/**
 * @Description: 按年+月份分类获取企业数量
 * @param userid：用户id ：年份
 * @return null
 * @Author tangzhuo
 * @CreateTime 2023/7/21 16:09
 */

    List<Map> graph(String userid, String year);

    /***
     * 关键字查询企业名称
     * @param keyword
     * @return
     */
    List<String> selectCompanyName(String keyword);

    List<String> selectCompanyName(List<String> company, String userid);


    List<TCompany> companyLists(List<String> id);


    /*
    查询企业首页数据
     */
    CompanyHomeDTO selectCompanyHomeDTO(String userid);

    /**
     * 邀请的查询企业
     * @param id
     * @param currentStartDate
     * @param currentEndTime
     * @param city
     * @param i
     * @return
     */
    int selectCount(String id, LocalDateTime currentStartDate, LocalDateTime currentEndTime, String city, int i);

    List<Map> mapList(String id, LocalDateTime currentStartDate, LocalDateTime currentEndTime, String city, int i);
}
