package com.wcwy.company.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wcwy.company.dto.CompanyCollerctPutInResume;
import com.wcwy.company.dto.SendAResumeRecord;
import com.wcwy.company.entity.TCompany;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wcwy.company.entity.TPermission;
import com.wcwy.company.po.TCompanyBasicInformation;
import com.wcwy.company.po.TCompanyPO;
import com.wcwy.company.po.TCompanySharePO;
import com.wcwy.company.query.ShareQuery;
import com.wcwy.company.query.TCompanyQuery;
import com.wcwy.company.query.inviterQuery;
import com.wcwy.company.vo.ProportionVO;
import com.wcwy.company.vo.TCompanyUserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
* @author Administrator
* @description 针对表【t_company(企业表)】的数据库操作Mapper
* @createDate 2022-09-01 09:30:00
* @Entity com.wcwy.company.entity.TCompany
*/
@Mapper
public interface TCompanyMapper extends BaseMapper<TCompany> {

    List<TPermission> rolePermission(@Param("companyId") String companyId);

    TCompany getId(@Param("userid") String userid);

    CompanyCollerctPutInResume CompanyIndustryPutInResume(@Param("id") String id, @Param("industryID") String industryID);

    Boolean updateMoney(@Param("tCompanyId") String  tCompanyId , @Param("money") BigDecimal money);


    IPage<TCompanyPO> pageList(@Param("page")IPage page,@Param("query") TCompanyQuery query);

    IPage<TCompanyPO> listInviterCompany(@Param("page") IPage page,@Param("inviterQuery") inviterQuery inviterQuery);

    IPage<TCompany> selectSubsidiaryCorporation(@Param("page") Page page, @Param("loginName") String loginName,@Param("keyword") String keyword);

    TCompanyBasicInformation basicInformation(@Param("tCompanyId") String tCompanyId);

    SendAResumeRecord sendAResumeRecord(@Param("userid") String userid, @Param("tCompanyId") String tCompanyId);

    boolean updateUserinfo(@Param("tCompanyUserVO") TCompanyUserVO tCompanyUserVO,@Param("companyId") String companyId);

    String getWeChat(@Param("userid") String userid);

    IPage<TCompanySharePO> shareTCompany(@Param("page") IPage page,@Param("shareQuery") ShareQuery shareQuery,@Param("userid") String userid);


    @Select("SELECT t_company.share_person AS recommendA,  t_recommend.share_person AS recommendB FROM t_company INNER JOIN t_recommend ON t_company.share_person = t_recommend.id WHERE t_company.company_id = #{companyId}")
    Map<String, String> getSharePerson(String companyId);
    @Select("SELECT t_company.contact_phone FROM t_company WHERE t_company.company_id = #{company}")
    String phoneNumbers(@Param("company") String company);

    @Select("(SELECT IFNULL('TC','') as authorization FROM	t_company WHERE	t_company.login_name =#{loginName})	UNION	(SELECT IFNULL('TJ','') as authorization FROM	t_jobhunter WHERE	t_jobhunter.login_name =#{loginName})	UNION	(SELECT IFNULL('TR','')  as authorization FROM	t_recommend WHERE	t_recommend.login_name =#{loginName})")
    List<String> authorization(@Param("loginName")String loginName);

    List<Map> graph(@Param("userid") String userid,@Param("year") String year);



    List<String> selectCompanyName(@Param("keyword") String keyword);

    List<String> selectCompanyNameS(@Param("companyName") List<String> companyName,@Param("userid") String userid);

    List<TCompany> companyLists(@Param("id") List<String> id);

    int count(@Param("id") String id,@Param("currentStartDate") LocalDateTime currentStartDate, @Param("currentEndTime") LocalDateTime currentEndTime, @Param("i") int i,@Param("city") String city);

    List<Map> mapList(@Param("id") String id,@Param("currentStartDate") LocalDateTime currentStartDate, @Param("currentEndTime") LocalDateTime currentEndTime, @Param("i") int i,@Param("city") String city);
}




