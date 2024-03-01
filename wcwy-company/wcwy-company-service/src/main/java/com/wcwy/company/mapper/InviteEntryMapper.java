package com.wcwy.company.mapper;

import com.wcwy.company.dto.InviteEntryDTO;
import com.wcwy.company.dto.InviteEntryPutInResume;
import com.wcwy.company.dto.NotEntryDTO;
import com.wcwy.company.dto.OneInterviewDTO;
import com.wcwy.company.entity.InviteEntry;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author Administrator
* @description 针对表【invite_entry(发送offer表)】的数据库操作Mapper
* @createDate 2022-11-08 16:23:15
* @Entity com.wcwy.company.entity.InviteEntry
*/
public interface InviteEntryMapper extends BaseMapper<InviteEntry> {

    List<InviteEntryPutInResume> select();

    InviteEntryPutInResume selectListPutInResumeId(@Param("putInResumeId") String putInResumeId);

    InviteEntryDTO selectResumeId(@Param("resumeId")String resumeId);
    @Select("SELECT invite_entry.cancel_cause FROM company.invite_entry WHERE invite_entry.invite_entry_id = #{inviteEntryId}")
    String selectCancelCause(String inviteEntryId);

    NotEntryDTO notEntry(@Param("putInResumeId")String putInResumeId);

    OneInterviewDTO oneInterview(@Param("userid") String userid,@Param("putInResumeId") String putInResumeId);
}




