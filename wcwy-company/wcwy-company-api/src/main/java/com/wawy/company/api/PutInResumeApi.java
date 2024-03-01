package com.wawy.company.api;

import com.wcwy.company.dto.InviteEntryDTO;
import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.stereotype.Component;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


/**
 * @author Administrator
 * @description 针对表【put_in_resume(投递简历表)】的数据库操作Service
 * @createDate 2022-10-13 17:15:29
 */
@Component
@FeignClient(value = "wcwy-company")
public interface PutInResumeApi {

    @GetMapping("/putInResume/updateDownloadResume")
    public void updateDownloadResume(@RequestParam("companyId") String companyId,@RequestParam("userId") String userId,@RequestParam("putInResumeId") String putInResumeId);
    @GetMapping("/putInResume/selectInviteEntry")
    public InviteEntryDTO selectInviteEntry(@RequestParam("resumeId") String resumeId) ;

    @GetMapping("/putInResume/receiveResume")
    public int receiveResume(@RequestParam("companyId") String companyId);
    @GetMapping("/putInResume/postAmend")
    public Boolean postAmend(@RequestParam("postId") String postId,@RequestParam("type") Integer type);

    @GetMapping("/putInResume/updateCloseAnAccount")
    public void updateCloseAnAccount(@RequestParam("putInResume") String putInResume, @RequestParam("state") Integer state);


    /**
     * 推荐时间矫正接口
     * @param id
     * @return
     */
    @GetMapping("/putInResume/correct")
    public LocalDateTime correct(@RequestParam("id") String id);
}
