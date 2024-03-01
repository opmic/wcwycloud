package com.wcwy.company.mapper;

import com.wcwy.company.entity.PutInResumeRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

/**
* @author Administrator
* @description 针对表【put_in_resume_record(投简记录)】的数据库操作Mapper
* @createDate 2023-05-15 10:03:41
* @Entity com.wcwy.company.entity.PutInResumeRecord
*/
@Mapper
public interface PutInResumeRecordMapper extends BaseMapper<PutInResumeRecord> {
    @Select("SELECT	ei_company_post.post_label as postLabel,t_company.contact_phone as phone FROM	put_in_resume	INNER JOIN	ei_company_post	ON 		put_in_resume.put_in_post = ei_company_post.post_id	INNER JOIN	t_company	ON 	put_in_resume.put_in_comppany = t_company.company_id	WHERE		put_in_resume.put_in_resume_id=#{putPost}")
    Map<String, String> phoneAndPost(@Param("putPost") String putPost);

    @Select("SELECT ei_company_post.post_label as postLabel, t_recommend.phone as phone FROM put_in_resume INNER JOIN ei_company_post ON put_in_resume.put_in_post = ei_company_post.post_id INNER JOIN t_recommend ON put_in_resume.put_in_user = t_recommend.id WHERE put_in_resume.put_in_resume_id =#{putPost} ")
    Map<String, String> RmPhoneAndPost(@Param("putPost")String putPost);

    @Select("SELECT t_recommend.phone as phone, t_company.company_name as company FROM put_in_resume INNER JOIN t_company ON put_in_resume.put_in_comppany = t_company.company_id INNER JOIN t_recommend ON put_in_resume.put_in_user = t_recommend.id WHERE put_in_resume.put_in_resume_id =#{putPost} ")
    Map<String, String> RmPhoneAndCompany(@Param("putPost")String putPost);
}




